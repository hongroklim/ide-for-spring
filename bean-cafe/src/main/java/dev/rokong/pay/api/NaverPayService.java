package dev.rokong.pay.api;

import dev.rokong.dto.PayApiDTO;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.order.main.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaverPayService {
    //TODO NaverPayService
    //https://developer.pay.naver.com/docs/v2/api#getstarted

    private final String API_NAME = "NAVERPAY";

    @Autowired
    PayApiDAO payApiDAO;

    @Autowired
    OrderService orderService;

    public void insertPayApi(int orderId, String paymentId){
        orderService.checkOrderExist(orderId);

        //create dto
        PayApiDTO payApi = new PayApiDTO();
        payApi.setOrderId(orderId);
        payApi.setApiKey(paymentId);
        payApi.setApiName(API_NAME);

        //insert
        payApiDAO.insertPayApi(payApi);
    }

    public void approvePay(int orderId){

    }
}