package dev.rokong.pay.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.util.ObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class AbstractPayApiService implements PayApiService {

    protected final String PAY_NAME;  //API's name

    private final int PAY_TYPE_ID;  //API's payId in payType

    private final MediaType MEDIA_TYPE;   //API's content type

    private final Charset CHARSET = StandardCharsets.UTF_8;    //API's encoding

    private final int TIMEOUT = 60 * 1000;  //milli second

    private final String REDIRECT_URL_KEY;  //key's name indicating to return

    //API's endpoint (class)
    private class ApiUrl {
        private final String PREPARE;   //prepare payment
        private final String APPROVE;   //approve payment
        private final String STATUS;    //payment status

        private ApiUrl(String prepare, String approve, String status) {
            this.PREPARE = prepare;
            this.APPROVE = approve;
            this.STATUS = status;
        }
    }
    private final ApiUrl API_URL;   //API URL (instance)

    //redirect URL in beanCafe (static class)
    protected static class REDIRECT_URL {
        protected static final String CONTEXT_PATH = "http://rokong.dev/beanCafe";
        protected static final String SUCCESS = CONTEXT_PATH+"/pay/api/complete";
        protected static final String CANCEL = CONTEXT_PATH+"/pay/api/cancel";
        protected static final String FAIL = CONTEXT_PATH+"/pay/api/fail";
    }

    /**
     * initialize abstract pay api service
     *
     * @param payTypeId id in pay_type table
     * @param payName API's name
     * @param contentType API's content type
     * @param prepareUrl API's url to prepare payment
     * @param approveUrl API's url to approve payment
     * @param statusUrl API's url to figure out status of payment
     */
    protected AbstractPayApiService(int payTypeId, String payName, MediaType contentType,
                                    String prepareUrl, String approveUrl, String statusUrl,
                                    String redirectUrlKey) {
        this.PAY_TYPE_ID = payTypeId;
        this.PAY_NAME = payName;
        this.MEDIA_TYPE = contentType;
        this.API_URL = new ApiUrl(prepareUrl, approveUrl, statusUrl);
        this.REDIRECT_URL_KEY = redirectUrlKey;
    }

    protected PayApiDAO payApiDAO;
    protected OrderService orderService;
    private MappingJackson2HttpMessageConverter messageConverter;
    protected ObjectMapper objectMapper;

    //make setter in final (not to be override)

    @Autowired
    public final void setPayApiDAO(PayApiDAO payApiDAO){
        this.payApiDAO = payApiDAO;
    }

    @Autowired
    public final void setOrderService(OrderService orderService){
        this.orderService = orderService;
    }

    @Autowired(required = false) @Qualifier("mvcMessageConverter")
    public final void setMessageConverter(MappingJackson2HttpMessageConverter messageConverter){
        this.messageConverter = messageConverter;
    }

    /**
     * After <code>@Autowired</code>, extract objectMapper
     * into TossService from messageConverter
     */
    @PostConstruct
    public final void init() {
        if (messageConverter != null) {
            this.objectMapper = messageConverter.getObjectMapper();
        } else {
            //when dispatcher is not initialized
            this.objectMapper = new ObjectMapper();
        }
    }

    /**
     * get API's pay id
     *
     * @return pay id in pay_type
     */
    public int getPayTypeId() {
        return this.PAY_TYPE_ID;
    }

    /**
     * parse string into map
     *
     * @param string to parse string
     * @return map
     */
    private ObjectNode parseJson(String string) {
        ObjectNode result = null;

        try {
            result = this.objectMapper.readValue(string, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * verify response from API
     *
     * @param json responsebody from API
     * @throws BusinessException invalid response
     */
    protected abstract void verifyResponse(ObjectNode json) throws BusinessException;

    /**
     * communication with another server by HTTP protocol
     *
     * @param url request url
     * @param requestBody request body to transfer to other
     * @return response
     */
    private ObjectNode requestURL(String url, Object requestBody) {
        URL u = null;
        HttpURLConnection connection = null;
        StringBuffer contentType = new StringBuffer();
        StringBuffer responseBody = new StringBuffer();

        //append media type and encoding into content type
        contentType.append(this.MEDIA_TYPE.toString())
                .append(";")
                .append(this.CHARSET.name());

        try {
            u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.addRequestProperty("Content-Type", contentType.toString());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(this.TIMEOUT);
            connection.setReadTimeout(this.TIMEOUT);

            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());

            //set requestBody
            bos.write(requestBody.toString().getBytes(this.CHARSET));
            bos.flush();
            bos.close();

            //get responseBody
            BufferedReader br = null;
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                //if response is ok
                br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), this.CHARSET)
                );

            }else{
                br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), this.CHARSET)
                );
                //responseBody.append(connection.getResponseMessage());
            }

            String line = null;
            while (br != null && (line = br.readLine()) != null) {
                responseBody.append(line);
            }
            br.close();

        } catch (Exception e) {
            responseBody.append(e);
        }

        //deserialize response body
        ObjectNode response = this.parseJson(responseBody.toString());

        //verify response
        this.verifyResponse(response);

        return response;
    }

    /**
     * create parameter which will be used in prepare payment
     *
     * @param orderId order to be prepared
     * @return parameter
     */
    protected abstract ObjectNode paramToPrepare(int orderId);

    /**
     * keep payKey information in payApi
     *
     * @param json response from API
     */
    protected abstract void insertPayApi(ObjectNode json);

    /**
     * make request to API
     *
     * @param orderId to request order id
     * @return redirect URL
     */
    public String preparePay(int orderId) {
        //create parameter
        ObjectNode param = this.paramToPrepare(orderId);

        //API call
        ObjectNode response = this.requestURL(API_URL.PREPARE, param);

        //insert payApi
        this.insertPayApi(response);

        return response.get(this.REDIRECT_URL_KEY).asText("");
    }

    /**
     * API's status into current project's order status
     * <p/>this method can return null when {@link #parsePayStatus(ObjectNode)}
     * does not refer this method
     *
     * @param status API's status
     * @see OrderStatus
     * @return status as current project
     */
    protected abstract OrderStatus translateStatus(String status);

    /**
     * parse API's payment status information
     * when parsing, this method can refer {@link #translateStatus(String)}
     * which is overridden
     * 
     * @param json response from API
     * @return pay status
     * @see #translateStatus(String)
     */
    protected abstract PayStatusDTO parsePayStatus(ObjectNode json);

    /**
     * create parameter for API to query payment
     *
     * @param orderId order to search
     * @return parameter
     * @see #getPayStatus(int)
     */
    protected abstract ObjectNode paramToQuery(int orderId);

    /**
     * get payment status in API
     *
     * @param orderId order to query
     * @return payment status
     */
    public PayStatusDTO getPayStatus(int orderId) {
        ObjectNode param = this.paramToQuery(orderId);

        ObjectNode response = this.requestURL(API_URL.STATUS, param);

        return this.parsePayStatus(response);
    }

    /**
     * create parameter to approve payment
     *
     * @param orderId order to approve payment
     * @return parameter
     */
    protected abstract ObjectNode paramToApprove(int orderId);

    /**
     * return pay status after check whether
     * order is available order status to approve
     *
     * @param orderId order to get pay status
     * @return pay status
     */
    private PayStatusDTO getVerifiedPayStatus(int orderId){
        PayStatusDTO payStatus = this.getPayStatus(orderId);
        if(payStatus.getOrderStatus() != OrderStatus.CHECKING){
            throw new BusinessException("payment is not completed");
        }
        return payStatus;
    }

    /**
     * add set (key, value) into ObjectNode instance
     *
     * @param json objectNode (if null, create new one)
     * @param key key
     * @param value value
     */
    private void putIntoObjectNode(ObjectNode json, String key, Object value){
        if(json == null){
            json = this.objectMapper.createObjectNode();
        }

        if(value == null){
            json.put(key, "");
        }else if(value.getClass() == Boolean.class){
            json.put(key, (Boolean) value);
        }else if(value.getClass() == Double.class){
            json.put(key, (Double) value);
        } else if (value.getClass() == Integer.class) {
            json.put(key, (Integer) value);
        } else {
            json.put(key, (String) value);
        }
    }

    /**
     *
     * @param orderId order to approve
     * @param appendParam set(k,v) to append parameters
     * @see #paramToApprove(int)
     */
    public void approvePay(int orderId, Map<String, Object> appendParam) {
        //get pay status
        PayStatusDTO payStatus = this.getVerifiedPayStatus(orderId);

        //create parameter
        ObjectNode param = this.paramToApprove(orderId);
        //if other set to be appended exists
        if (ObjUtil.isNotEmpty(appendParam)) {
            appendParam.forEach((key, value) -> this.putIntoObjectNode(param, key, value));
        }

        //request to API
        this.requestURL(API_URL.APPROVE, param);
    }


    //TODO refund pay

    protected String getPayKeyNotNull(int orderId){
        String payKey = payApiDAO.getApiKey(orderId);
        if (ObjUtil.isEmpty(payKey)) {
            throw new BusinessException("payId is not exists in order : " + orderId);
        }
        return payKey;
    }
}