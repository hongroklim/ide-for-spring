package dev.rokong.dto;

public class OrderDelivery {
    private int orderId;
    private String userNm;
    private String senderNm;
    private String recipientNm;
    private int zipCd;
    private String address1;
    private String address2;
    private String contact;
    private String method;
    private String methodDetail;
    private String message;

    public int getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethodDetail() {
        return methodDetail;
    }

    public void setMethodDetail(String methodDetail) {
        this.methodDetail = methodDetail;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public int getZipCd() {
        return zipCd;
    }

    public void setZipCd(int zipCd) {
        this.zipCd = zipCd;
    }

    public String getRecipientNm() {
        return recipientNm;
    }

    public void setRecipientNm(String recipientNm) {
        this.recipientNm = recipientNm;
    }

    public String getSenderNm() {
        return senderNm;
    }

    public void setSenderNm(String senderNm) {
        this.senderNm = senderNm;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}