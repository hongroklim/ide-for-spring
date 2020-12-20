package dev.rokong.pay.api;

import com.fasterxml.jackson.core.JsonProcessingException;
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

@Service
public class KakaoPayService {
    //TODO KakaoPayService
    //https://developers.kakao.com/docs/latest/ko/kakaopay/common

    private final int PAY_TYPE_ID = 8;

    private final int TIMEOUT = 1000;

    private final String API_NAME = "KAKAOPAY";

    private final String CID = "TC0ONETIME";

    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    //결제 준비
    private final String PAYMENT_READY_URL ="https://kapi.kakao.com/v1/payment/ready";

    //결제 승인
    private final String PAYMENT_APPROVE_URL = "https://kapi.kakao.com/v1/payment/approve";

    //결제 조회
    private final String PAYMENT_DETAIL_URL = "https://kapi.kakao.com/v1/payment/order";

    //성공 시 redirect
    private final String APPROVAL_URL = "http://test/pay/api/complete";

    //취소 시 redirect
    private final String CANCEL_URL = "http://test/pay/api/cancel";

    //실패 시 redirect
    private final String FAIL_URL = "http://test/pay/api/fail";

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
        if (messageConverter != null) {
            this.objectMapper = messageConverter.getObjectMapper();
        }
    }

    @Autowired
    PayApiDAO payApiDAO;

    @Autowired
    OrderService orderService;

    public int getPayTypeId() {
        return this.PAY_TYPE_ID;
    }

    private String getPayKeyNotNull(int orderId){
        String payKey = payApiDAO.getApiKey(orderId);
        if (ObjUtil.isEmpty(payKey)) {
            throw new BusinessException("payId is not exists in order : " + orderId);
        }
        return payKey;
    }

    private ObjectNode createPrepareParam(int orderId) {
        OrderDTO order = orderService.getOrderNotNull(orderId);
        String orderDesc = orderService.getOrderDesc(orderId);

        ObjectNode json = this.objectMapper.createObjectNode();

        json.put("cid", this.CID);
        json.put("partner_order_id", order.getId());
        json.put("partner_user_id", order.getUserNm());
        json.put("item_name", orderDesc);
        json.put("quantity", 0);
        json.put("total_amount", order.getPrice() + order.getDeliveryPrice());
        json.put("tax_free_amount", 0);
        json.put("approval_url", this.APPROVAL_URL);
        json.put("cancel_url", this.CANCEL_URL);
        json.put("fail_url", this.FAIL_URL);

        return json;
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
     * communication with another server by HTTP protocol
     *
     * @param url         request url
     * @param requestBody request body to transfer to other
     * @return response
     */
    private ObjectNode requestURL(String url, Object requestBody) {
        URL u = null;
        URLConnection connection = null;
        StringBuffer responseBody = new StringBuffer();

        try {
            u = new URL(url);
            connection = u.openConnection();
            connection.addRequestProperty("Content-Type", this.CONTENT_TYPE);
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

        return this.parseJson(responseBody.toString());
    }

    public String preparePayment(int orderId) {
        //request API
        ObjectNode param = this.createPrepareParam(orderId);

        ObjectNode response = this.requestURL(this.PAYMENT_READY_URL, param);

        //insert TID
        PayApiDTO payApi = new PayApiDTO();
        payApi.setOrderId(orderId);
        payApi.setApiKey(response.get("tid").asText());
        payApi.setApiName(this.API_NAME);
        payApiDAO.insertPayApi(payApi);

        //return redirect URL
        return response.get("next_redirect_pc_url").asText("");
    }

    public ObjectNode createTidParam(int orderId) {
        //get order
        orderService.getOrderNotNull(orderId);

        String tid = payApiDAO.getApiKey(orderId);
        if (ObjUtil.isEmpty(tid)) {
            throw new BusinessException("payId is not exists in order : " + orderId);
        }

        ObjectNode json = this.objectMapper.createObjectNode();
        json.put("cid", this.CID);
        json.put("tid", tid);

        return json;
    }

    public void verifyResponse(ObjectNode response){
        if(response.hasNonNull("code") && response.hasNonNull("msg")){
            int code = response.get("code").asInt(0);
            if (code > 0) {
                return;
            }

            String message = response.get("msg").asText();
            throw new RuntimeException("message : "+code+" / code : "+message);
        }
    }

    private OrderStatus translateStatus(String apiStatus) {
        OrderStatus orderStatus = null;
        if ("READY".equals(apiStatus) || "SEND_TMS".equals(apiStatus)
                || "OPEN_PAYMENT".equals(apiStatus) || "SELECT_METHOD".equals(apiStatus)
                || "ARS_WAITING".equals(apiStatus)) {
            orderStatus = OrderStatus.PAYMENT_PROGRESS;
        } else if ("AUTH_PASSWORD".equals(apiStatus)) {
            orderStatus = OrderStatus.CHECKING;
        } else if ("SUCCESS_PAYMENT".equals(apiStatus)) {
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("QUIT_PAYMENT".equals(apiStatus) || "FAIL_PAYMENT".equals(apiStatus)) {
            orderStatus = OrderStatus.CANCELED_PAYMENT;
        } else {
            throw new IllegalStateException("Unexpected value: " + apiStatus);
        }

        return orderStatus;
    }

    private PayStatusDTO parsePayStatus(ObjectNode json){
        PayStatusDTO payStatus = new PayStatusDTO();

        payStatus.setApiName(this.API_NAME);
        payStatus.setApiKey(json.get("cid").asText());

        String status = json.get("status").asText();
        payStatus.setOrderStatus(this.translateStatus(status));

        payStatus.setPayMethod(json.get("payment_method_type").asText());
        payStatus.setPrice(json.get("amount").asInt());

        return payStatus;
    }

    public PayStatusDTO getPayStatus(int orderId) {
        //create parameter
        ObjectNode param = this.createTidParam(orderId);

        ObjectNode response = this.requestURL(this.PAYMENT_DETAIL_URL, param);

        this.verifyResponse(response);

        return this.parsePayStatus(response);
    }

    public ObjectNode createApproveParam(int orderId, String pgToken){
        //get order
        OrderDTO order = orderService.getOrderNotNull(orderId);

        ObjectNode json = this.objectMapper.createObjectNode();
        json.put("cid", this.CID);
        json.put("tid", this.getPayKeyNotNull(orderId));
        json.put("partner_order_id", order.getId());
        json.put("partner_user_id", order.getUserNm());
        json.put("pg_token", pgToken);

        return json;
    }

    public void approvePay(int orderId, String pgToken) {
        //check order status
        PayStatusDTO payStatus = this.getPayStatus(orderId);
        if(payStatus.getOrderStatus() != OrderStatus.CHECKING){
            throw new BusinessException("payment is not completed");
        }

        //create parameter
        ObjectNode param = this.createApproveParam(orderId, pgToken);

        //approve payment
        ObjectNode response = this.requestURL(this.PAYMENT_APPROVE_URL, param);
        this.verifyResponse(response);
    }
}