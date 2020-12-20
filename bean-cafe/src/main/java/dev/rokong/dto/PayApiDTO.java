package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayApiDTO {
    private int orderId;
    private String apiKey;
    private String apiName;
}