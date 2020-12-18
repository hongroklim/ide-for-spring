package dev.rokong.pay.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.order.main.OrderService;
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

@Service("TossService")
public class TossService implements PayAPIService {

    //https://tossdev.github.io/api.html#payments

    //결재 생성
    private final String requestPaymentURL = "https://pay.toss.im/api/v2/payments";

    private final String paymentStatusURL = "https://pay.toss.im/api/v2/status";

    //성공 시 redirect
    private final String requestReturnURL = "http://test/pay/api/complete";

    //취소 시 redirect
    private final String requestCancelURL = "http://test/pay/api/cancel";



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
    OrderService orderService;

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

    private String requestURL(String url, Object requestBody){
        URL u = null;
        URLConnection connection = null;
        StringBuilder responseBody = new StringBuilder();

        try {
            u = new URL(url);
            connection = u.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());

            bos.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
            bos.flush();
            bos.close();

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
     * @return
     */
    private Map<String, Object> requestPayment(int orderId) {
        ObjectNode param = this.createRequestOrderParam(orderId);
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
        Map<String, Object> result = this.requestPayment(orderId);

        int code = (int) result.get("code");

        if(code == -1){
            //if response code is falied
            //TODO handle exception status
        }

        String payToken = (String) result.get("payToken");

        //TODO insert pay token

        String redirectPage = (String) result.get("checkoutPage");

        return redirectPage;
    }

    private ObjectNode createPayStatusParam(int orderId){
        //get order and description
        OrderDTO order = orderService.getOrderNotNull(orderId);

        String payToken = "";   //TODO get payToken in database

        ObjectNode json = this.objectMapper.createObjectNode();
        json.put("apiKey", apiKey);
        json.put("payToken", payToken);

        return json;
    }

    private PayStatusDTO parsePayStatus(Map<String, Object> map) {
        PayStatusDTO payStatus = new PayStatusDTO();

        payStatus.setApiName("TOSS");
        payStatus.setApiKey((String) map.get("payToken"));

        OrderStatus o = null;
        String status = (String) map.get("payStatus");
        if ("PAY_STANDBY".equals(status)) {
            o = OrderStatus.PAYMENT_READY;
        } else if ("PAY_PROGRESS".equals(status)) {
            o = OrderStatus.PAYMENT_READY;
        } else {
            throw new IllegalStateException("Unexpected value: " + map.get("payStatus"));
        }

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
        ObjectNode param = this.createPayStatusParam(orderId);

        //request and get response
        String response = this.requestURL(this.paymentStatusURL, param);
        Map<String, Object> map = this.parseMap(response);

        return this.parsePayStatus(map);
    }

}