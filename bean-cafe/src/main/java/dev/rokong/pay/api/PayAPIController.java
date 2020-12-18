package dev.rokong.pay.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay/api")
public class PayAPIController {
    
    /**
     * API로부터 결재완료 요청을 수신받는 곳이다.
     * 
     * @param orderId
     * @return 주문상태로 redirect
     */
    @RequestMapping("/complete")
    public String completePayment(@RequestParam int orderId){
        //TODO 주문완료 처리

        //order status -> CHECKING

        return "redirect:/order/{id}/status";
    }

    @RequestMapping("/cancel")
    public String cancelPayment(@RequestParam int orderId){
        //TODO 결제취소

        //order status -> PAYMENT_CANCELED

        return "redirect:/order/{id}/status";
    }

    @RequestMapping("/status")
    public String paymentStatus(@RequestParam int orderId){
        //TODO payment status

        //order status -> PAYMENT_CANCELED

        return "redirect:/order/{id}/status";
    }
}