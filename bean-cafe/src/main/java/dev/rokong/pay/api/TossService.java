package dev.rokong.pay.api;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import dev.rokong.dto.OrderDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;

@Service("TossService")
public class TossService implements PayAPIService {

    //https://tossdev.github.io/api.html#payments

    private String requestPaymentURL = "https://pay.toss.im/api/v2/payments";

    private String requestReturnURL = "http://test/pay/api/complete";

    private String requestCancelURL = "http://test/pay/api/cancel";

    @Autowired(required = false)
    private String apiKey = "sk_real_w5lNQylNqa5lNQe013Nq";

    @Autowired
    @Qualifier("mvcMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    private ObjectMapper objectMapper;

    /**
     * @Autowired 후 messageConverter로부터 objectMapper를 주입한다.
     */
    @PostConstruct
    public void init() {
        this.objectMapper = messageConverter.getObjectMapper();
    }

    @Autowired
    OrderService orderService;

    private ObjectNode createParameter(OrderDTO order) {
        ObjectNode jsonBody = this.objectMapper.createObjectNode();

        jsonBody.put("orderNo", order.getId());
        jsonBody.put("amount", order.getPrice()+order.getDeliveryPrice());
        jsonBody.put("amountTaxFree", 0);
        jsonBody.put("productDesc", "테스트 결제");
        jsonBody.put("apiKey", apiKey);
        jsonBody.put("autoExecute", false);
        jsonBody.put("retUrl", this.requestReturnURL);
        jsonBody.put("retCancelUrl", this.requestCancelURL);

        return jsonBody;
    }

    private Map<String, Object> requestPayment(OrderDTO order) {
        URL url = null;
        URLConnection connection = null;
        StringBuilder responseBody = new StringBuilder();
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            url = new URL(this.requestPaymentURL);
            connection = url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            ObjectNode jsonBody = this.createParameter(order);

            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());

            bos.write(jsonBody.toString().getBytes(StandardCharsets.UTF_8));
            bos.flush();
            bos.close();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = br.readLine()) != null) {
                responseBody.append(line);
            }
            br.close();

        } catch (Exception e) {
            responseBody.append(e);
        }

        try {
            result = this.objectMapper.readValue(
                responseBody.toString(), new TypeReference<Map<String, Object>>() {}
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
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
        //get order
        OrderDTO order = orderService.getOrderNotNull(orderId);
        
        Map<String, Object> result = this.requestPayment(order);

        int code = (int) result.get("code");

        if(code == -1){
            //if response code is falied
            //TODO handle exception status
        }

        String payToken = (String) result.get("payToken");

        //TODO insert pay token

        return (String) result.get("checkoutPage");
    }

    /**
     * Toss에서 결재상태를 확인한다.
     * <p>apiKey와 payToken을 요청으로 보내게 된다.
     * 
     * @param payToken 결재 고유 토큰
     */
    public void getPayStatus(String payToken){

    }


}