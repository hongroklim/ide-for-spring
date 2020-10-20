package dev.rokong.dto;

public class OrderProductDTO {
    private int orderId;
    private int productId;
    private String optionCd;
    private String sellerNm;
    private int cnt;
    private int price;
    private int discountPrice;

    public int getOrderId() {
        return orderId;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getSellerNm() {
        return sellerNm;
    }

    public void setSellerNm(String sellerNm) {
        this.sellerNm = sellerNm;
    }

    public String getOptionCd() {
        return optionCd;
    }

    public void setOptionCd(String optionCd) {
        this.optionCd = optionCd;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}