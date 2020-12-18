package dev.rokong.dto;

import dev.rokong.annotation.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayStatusDTO {
    private int orderId;
    private int payId;
    private String apiName;
    private String apiKey;
    private OrderStatus orderStatus;
    private int price;
    private String payMethod;
}
