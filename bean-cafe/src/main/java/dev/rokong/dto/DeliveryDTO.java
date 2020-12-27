package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryDTO {
    private int orderId;
    private String userNm;
    private String senderNm;
    private String recipientNm;
    private Integer zipCd;
    private String address1;
    private String address2;
    private String contact1;
    private String contact2;
    private String message;

    public DeliveryDTO(int orderId){
        this.orderId = orderId;
    }

    public DeliveryDTO(int orderId, String userNm){
        this(orderId);
        this.userNm = userNm;
    }
}