package dev.rokong.pay.api;

import dev.rokong.dto.PayStatusDTO;

import java.util.Map;

public interface PayApiService {
    public int getPayTypeId();
    public String preparePay(int orderId);
    public PayStatusDTO getPayStatus(int orderId);
    public void approvePay(int orderId, Map<String, Object> appendParam);
}