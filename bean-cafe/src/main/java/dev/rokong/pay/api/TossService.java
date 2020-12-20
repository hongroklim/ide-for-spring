package dev.rokong.pay.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayApiDTO;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.util.ObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service("tossService")
public class TossService implements PayApiService {

    //https://tossdev.github.io/api.html#payments
    private final int TIMEOUT = 2000;

    private final int PAY_TYPE_ID = 7;

    private final String API_NAME = "TOSS";

    //결재 생성 (from API)
    private final String requestPaymentURL = "https://pay.toss.im/api/v2/payments";

    //결제 상태 (from API)
    private final String paymentStatusURL = "https://pay.toss.im/api/v2/status";

    //결제 요청 (from API)
    private final String approvePaymentURL = "https://pay.toss.im/api/v2/execute";

    //성공 시 redirect
    private final String requestReturnURL = "http://test/pay/api/complete";

    //취소 시 redirect
    private final String requestCancelURL = "http://test/pay/api/cancel";

    //실패 시 redirect
    private final String requestFailURL = "http://test/pay/api/fail";

    private final String apiKey = "sk_real_w5lNQylNqa5lNQe013Nq";

    @Autowired(required = false) @Qualifier("mvcMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    private ObjectMapper objectMapper;

    /**
     * After <code>@Autowired</code>, extract objectMapper
     * into TossService from messageConverter
     */
    @PostConstruct
    public void init() {
        //avoid null pointer exception when spring test(not mvc)
        if(messageConverter != null){
            this.objectMapper = messageConverter.getObjectMapper();
        }
    }

    @Autowired
    PayApiDAO payApiDAO;

    @Autowired
    OrderService orderService;

    public int getPayTypeId(){
        return this.PAY_TYPE_ID;
    }

    private ObjectNode createRequestOrderParam(int orderId) {
        //get order and description
        OrderDTO order = orderService.getOrderNotNull(orderId);
        String orderDesc = orderService.getOrderDesc(orderId);

        ObjectNode json = this.objectMapper.createObjectNode();

        json.put("orderNo", order.getId());
        json.put("amount", order.getPrice()+order.getDeliveryPrice());
        json.put("amountTaxFree", 0);
        json.put("productDesc", orderDesc);
        json.put("apiKey", apiKey);
        json.put("autoExecute", false);
        json.put("retUrl", this.requestReturnURL);
        json.put("retCancelUrl", this.requestCancelURL);

        return json;
    }

    /**
     * parse string into map
     *
     * @param string to parse string
     * @return map
     */
    private Map<String, Object> parseMap(String string){
        Map<String, Object> result = null;
        try {
            result = this.objectMapper.readValue(
                    string, new TypeReference<Map<String, Object>>() {}
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * communication with another server by HTTP protocol
     *
     * @param url request url
     * @param requestBody request body to transfer to other
     * @return response
     */
    private String requestURL(String url, Object requestBody){
        URL u = null;
        URLConnection connection = null;
        StringBuffer responseBody = new StringBuffer();

        try {
            u = new URL(url);
            connection = u.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(this.TIMEOUT);
            connection.setReadTimeout(this.TIMEOUT);

            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());

            //set requestBody
            bos.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
            bos.flush();
            bos.close();

            //get responseBody
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
            );
            String line = null;
            while ((line = br.readLine()) != null) {
                responseBody.append(line);
            }
            br.close();

        } catch (Exception e) {
            responseBody.append(e);
        }

        return responseBody.toString();
    }

    /**
     * make request and get response with Toss API
     *
     * @param orderId order id
     * @return response from API
     */
    private Map<String, Object> requestPayment(int orderId) {
        //create parameter
        ObjectNode param = this.createRequestOrderParam(orderId);

        //make request and get response
        String response = this.requestURL(this.requestPaymentURL, param);
        return this.parseMap(response);
    }

    /**
     * Toss에게 결재생성을 요청하는 API를 호출한다.
     * <p>결재생성 후 response의 payToken을 저장하고,
     * checkoutPage를 사용자에게 Redirect 시킨다.
     * 
     * @param orderId 요청할 주문번호
     * @return 사용자에게 반환할 Redirect URL
     */
    public String makeRequest(int orderId){
        //request payment
        Map<String, Object> response = this.requestPayment(orderId);
        this.verifyResponseCode(response);

        //insert pay token
        PayApiDTO payApi = new PayApiDTO();
        payApi.setOrderId(orderId);
        payApi.setApiKey((String) response.get("payToken"));
        payApi.setApiName(this.API_NAME);
        payApiDAO.insertPayApi(payApi);

        return (String) response.get("checkoutPage");
    }

    /**
     * verify response from toss API
     *
     * @param response responseBody (Map Type)
     * @throws RuntimeException when response is failed
     */
    private void verifyResponseCode(Map<String, Object> response) {
        int code = (int) response.get("code");
        if(code == -1){
            //if payment status response is failed
            String tossMessage = (String) response.get("msg");
            String tossCode = (String) response.get("errorCode");
            throw new RuntimeException("code : "+tossCode+" / message : "+tossMessage);
        }
    }

    /**
     * to check payment status, prepare parameters
     * to be used in requestbody for toss API
     * @param orderId to check order id
     * @return json
     */
    private ObjectNode createPayTokenParam(int orderId){
        //get order
        orderService.getOrderNotNull(orderId);

        String payToken = payApiDAO.getApiKey(orderId);
        if(ObjUtil.isEmpty(payToken)){
            throw new BusinessException("payId is not exists in order : "+orderId);
        }

        ObjectNode json = this.objectMapper.createObjectNode();
        json.put("apiKey", apiKey);
        json.put("payToken", payToken);

        return json;
    }

    /**
     * convert Toss's payment status into order status
     *
     * @param payStatus payment status from toss
     * @return order status
     */
    private OrderStatus translateStatus(String payStatus){
        OrderStatus orderStatus = null;

        if ("PAY_STANDBY".equals(payStatus)) { //결제 대기 중
            orderStatus = OrderStatus.PAYMENT_STANDBY;
        } else if ("PAY_APPROVED".equals(payStatus)) { //구매자 인증 완료
            orderStatus = OrderStatus.CHECKING;
        } else if ("PAY_CANCEL".equals(payStatus)) { //결제 취소
            orderStatus = OrderStatus.CANCELED_PAYMENT;
        } else if ("PAY_PROGRESS".equals(payStatus)) { //결제 진행 중
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("PAY_COMPLETE".equals(payStatus)) { //결제 완료
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("REFUND_PROGRESS".equals(payStatus)) { //환불 진행 중
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("REFUND_SUCCESS".equals(payStatus)) { //환불 성공
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("SETTLEMENT_COMPLETE".equals(payStatus)) {  //정산 완료
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("SETTLEMENT_REFUND_COMPLETE".equals(payStatus)) {   //환불 정산 완료
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else {
            throw new IllegalStateException("Unexpected value: " + payStatus);
        }

        return orderStatus;
    }

    /**
     * parse toss's payment status map into PayStatusDTO
     *
     * @param map toss's payment status
     * @return PayStatusDTO
     */
    private PayStatusDTO parsePayStatus(Map<String, Object> map) {
        PayStatusDTO payStatus = new PayStatusDTO();

        payStatus.setApiName(this.API_NAME);
        payStatus.setApiKey((String) map.get("payToken"));

        String status = (String) map.get("payStatus");
        payStatus.setOrderStatus(this.translateStatus(status));

        payStatus.setPayMethod((String) map.get("payMethod"));
        payStatus.setPrice((int) map.get("amount"));

        return payStatus;
    }

    /**
     * Toss에서 결재상태를 확인한다.
     * <p>apiKey와 payToken을 요청으로 보내게 된다.
     * 
     * @param orderId order id
     */
    public PayStatusDTO getPayStatus(int orderId){
        //create parameter
        ObjectNode param = this.createPayTokenParam(orderId);

        //request and get response
        String resp = this.requestURL(this.approvePaymentURL, param);
        Map<String, Object> response = this.parseMap(resp);

        this.verifyResponseCode(response);

        return this.parsePayStatus(response);
    }

    /**
     * approve transaction by seller
     *
     * @param orderId to approve order
     */
    public void approvePay(int orderId){
        //check order status
        PayStatusDTO payStatus = this.getPayStatus(orderId);
        if(payStatus.getOrderStatus() != OrderStatus.CHECKING){
            throw new BusinessException("payment is not completed");
        }

        //create parameter
        ObjectNode param = this.createPayTokenParam(orderId);

        //request and get response
        String resp = this.requestURL(this.paymentStatusURL, param);
        Map<String, Object> response = this.parseMap(resp);

        this.verifyResponseCode(response);
    }

}