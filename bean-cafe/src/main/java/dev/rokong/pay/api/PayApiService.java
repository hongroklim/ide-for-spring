package dev.rokong.pay.api;

import dev.rokong.dto.PayStatusDTO;

public interface PayApiService {
    public int getPayTypeId();
    public String makeRequest(int orderId);
    public PayStatusDTO getPayStatus(int orderId);
    public void approvePay(int orderId);
}