package dev.rokong.pay.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayApiDTO;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.exception.BusinessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service("kakaoPayService")
public class KakaoPayService extends AbstractPayApiService {

    private final String CID = "TC0ONETIME";

    public KakaoPayService(){
        super(8, "KAKAOPAY", MediaType.APPLICATION_FORM_URLENCODED,
                "https://kapi.kakao.com/v1/payment/ready",
                "https://kapi.kakao.com/v1/payment/approve",
                "https://kapi.kakao.com/v1/payment/order",
                "next_redirect_pc_url");
    }

    @Override
    protected void verifyResponse(ObjectNode json) throws BusinessException {
        if(json.hasNonNull("code") && json.hasNonNull("msg")){
            //get code if exists
            int code = json.get("code").asInt();
            if (code > 0) {
                //if code status is ok, return
                return;
            }

            //get message
            String message = json.get("msg").asText();
            throw new RuntimeException("message : "+code+" / code : "+message);
        }
    }

    @Override
    protected ObjectNode paramToPrepare(int orderId) {
        //get order and description
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
        json.put("approval_url", REDIRECT_URL.SUCCESS);
        json.put("cancel_url", REDIRECT_URL.CANCEL);
        json.put("fail_url", REDIRECT_URL.FAIL);

        return json;
    }

    @Override
    protected void insertPayApi(ObjectNode json) {
        PayApiDTO payApi = new PayApiDTO();

        payApi.setOrderId(json.get("partner_order_id").asInt());
        payApi.setApiKey(json.get("tid").asText());
        payApi.setApiName(this.PAY_NAME);

        payApiDAO.insertPayApi(payApi);
    }

    @Override
    protected OrderStatus translateStatus(String status) {
        OrderStatus orderStatus = null;

        if ("READY".equals(status) || "SEND_TMS".equals(status)
                || "OPEN_PAYMENT".equals(status) || "SELECT_METHOD".equals(status)
                || "ARS_WAITING".equals(status)) {
            orderStatus = OrderStatus.PAYMENT_PROGRESS;
        } else if ("AUTH_PASSWORD".equals(status)) {
            orderStatus = OrderStatus.CHECKING;
        } else if ("SUCCESS_PAYMENT".equals(status)) {
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("QUIT_PAYMENT".equals(status) || "FAIL_PAYMENT".equals(status)) {
            orderStatus = OrderStatus.CANCELED_PAYMENT;
        } else {
            throw new IllegalStateException("Unexpected value: " + status);
        }

        return orderStatus;
    }

    @Override
    protected PayStatusDTO parsePayStatus(ObjectNode json) {
        PayStatusDTO payStatus = new PayStatusDTO();

        payStatus.setApiName(this.PAY_NAME);
        payStatus.setApiKey(json.get("cid").asText());

        String status = json.get("status").asText();
        payStatus.setOrderStatus(this.translateStatus(status));

        payStatus.setPayMethod(json.get("payment_method_type").asText());
        payStatus.setPrice(json.get("amount").asInt());

        return payStatus;
    }

    @Override
    protected ObjectNode paramToQuery(int orderId) {
        //get order
        orderService.getOrderNotNull(orderId);

        ObjectNode json = this.objectMapper.createObjectNode();

        json.put("cid", this.CID);
        json.put("tid", this.getPayKeyNotNull(orderId));

        return json;
    }

    @Override
    protected ObjectNode paramToApprove(int orderId) {
        //get order
        OrderDTO order = orderService.getOrderNotNull(orderId);

        ObjectNode json = this.objectMapper.createObjectNode();

        json.put("cid", this.CID);
        json.put("tid", this.getPayKeyNotNull(orderId));
        json.put("partner_order_id", order.getId());
        json.put("partner_user_id", order.getUserNm());
        //json.put("pg_token", pgToken);

        return json;
    }
}
