package dev.rokong.pay.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayApiDTO;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.exception.BusinessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service("tossService")
public class TossService extends AbstractPayApiService {

    private final String API_KEY = "sk_real_w5lNQylNqa5lNQe013Nq";

    /**
     * initialize Toss Service
     */
    protected TossService() {
        super(7, "TOSS", MediaType.APPLICATION_JSON,
                "https://pay.toss.im/api/v2/payments",
                "https://pay.toss.im/api/v2/execute",
                "https://pay.toss.im/api/v2/status",
                "checkoutPage");
    }

    @Override
    protected void verifyResponse(ObjectNode json) throws BusinessException {
        int code = json.get("code").asInt(0);
        if(code == -1){
            //if payment status response is failed
            String tossMessage = (String) json.get("msg").asText("");
            int tossCode = json.get("errorCode").asInt();
            throw new BusinessException("code : "+tossCode+" / message : "+tossMessage);
        }
    }

    @Override
    protected ObjectNode paramToPrepare(int orderId) {
        //get order
        OrderDTO order = orderService.getOrderNotNull(orderId);

        ObjectNode json = this.objectMapper.createObjectNode();
        json.put("orderNo", orderId);
        json.put("amount", order.getPrice()+order.getDeliveryPrice());
        json.put("amountTaxFree", 0);
        json.put("productDesc", orderService.getOrderDesc(orderId));
        json.put("apiKey", this.API_KEY);
        json.put("autoExecute", false);
        json.put("retUrl", REDIRECT_URL.SUCCESS);
        json.put("retCancelUrl", REDIRECT_URL.CANCEL);

        return json;
    }

    @Override
    protected void insertPayApi(ObjectNode json) {
        PayApiDTO payApi = new PayApiDTO();

        payApi.setOrderId(json.get("orderNo").asInt());
        payApi.setApiKey(json.get("payToken").asText());
        payApi.setApiName(this.PAY_NAME);

        payApiDAO.insertPayApi(payApi);
    }

    @Override
    protected OrderStatus translateStatus(String status) {
        OrderStatus orderStatus = null;

        if ("PAY_STANDBY".equals(status)) { //결제 대기 중
            orderStatus = OrderStatus.PAYMENT_STANDBY;
        } else if ("PAY_APPROVED".equals(status)) { //구매자 인증 완료
            orderStatus = OrderStatus.CHECKING;
        } else if ("PAY_CANCEL".equals(status)) { //결제 취소
            orderStatus = OrderStatus.CANCELED_PAYMENT;
        } else if ("PAY_PROGRESS".equals(status)) { //결제 진행 중
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("PAY_COMPLETE".equals(status)) { //결제 완료
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("REFUND_PROGRESS".equals(status)) { //환불 진행 중
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("REFUND_SUCCESS".equals(status)) { //환불 성공
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("SETTLEMENT_COMPLETE".equals(status)) {  //정산 완료
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else if ("SETTLEMENT_REFUND_COMPLETE".equals(status)) {   //환불 정산 완료
            orderStatus = OrderStatus.CHECK_COMPLETE;
        } else {
            throw new IllegalStateException("Unexpected value: " + status);
        }

        return orderStatus;
    }

    @Override
    protected PayStatusDTO parsePayStatus(ObjectNode json) {
        PayStatusDTO payStatus = new PayStatusDTO();

        payStatus.setApiName(this.PAY_NAME);
        payStatus.setApiKey(json.get("payToken").asText());

        String status = json.get("payStatus").asText();
        payStatus.setOrderStatus(this.translateStatus(status));

        payStatus.setPayMethod(json.get("payMethod").asText());
        payStatus.setPrice(json.get("amount").asInt());

        return payStatus;
    }

    @Override
    protected ObjectNode paramToQuery(int orderId) {
        //get order
        orderService.getOrderNotNull(orderId);

        ObjectNode json = this.objectMapper.createObjectNode();
        json.put("apiKey", this.API_KEY);
        json.put("payToken", this.getPayKeyNotNull(orderId));

        return json;
    }

    @Override
    protected ObjectNode paramToApprove(int orderId) {
        //same as paramToQuery(int)
        return this.paramToQuery(orderId);
    }
}
