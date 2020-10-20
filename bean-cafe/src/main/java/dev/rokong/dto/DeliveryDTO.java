package dev.rokong.dto;

public class DeliveryDTO {
    private int orderId;
    private String sellerNm;
    private int price;
    private int deliveryPrice;
    private String statusCd;

    public int getOrderId() {
        return orderId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSellerNm() {
        return sellerNm;
    }

    public void setSellerNm(String sellerNm) {
        this.sellerNm = sellerNm;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}