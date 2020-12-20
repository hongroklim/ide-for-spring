package dev.rokong.pay.api;

import dev.rokong.dto.PayStatusDTO;

public interface PayApiService {
    public int getPayTypeId();
    public PayStatusDTO getPayStatus(int orderId);
    public String makeRequest(int orderId);
}