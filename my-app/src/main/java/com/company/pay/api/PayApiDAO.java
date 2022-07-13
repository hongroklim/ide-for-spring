package dev.rokong.pay.api;

import dev.rokong.dto.PayApiDTO;

public interface PayApiDAO {
    public String getApiKey(int orderId);
    public void insertPayApi(PayApiDTO payApi);
}