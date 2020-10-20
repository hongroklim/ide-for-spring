package dev.rokong.dto;

import java.util.Date;

public class OrderMainDTO {
    private int id;
    private String userNm;
    private int price;
    private int deliveryPrice;
    private String payType;
    private String payDetail;
    private String CashReceiptType;
    private Date requestDt;
    private String statusCd;
    private Date lastEditDt;
    private String editorNm;

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEditorNm() {
        return editorNm;
    }

    public void setEditorNm(String editorNm) {
        this.editorNm = editorNm;
    }

    public Date getLastEditDt() {
        return lastEditDt;
    }

    public void setLastEditDt(Date lastEditDt) {
        this.lastEditDt = lastEditDt;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public Date getRequestDt() {
        return requestDt;
    }

    public void setRequestDt(Date requestDt) {
        this.requestDt = requestDt;
    }

    public String getCashReceiptType() {
        return CashReceiptType;
    }

    public void setCashReceiptType(String cashReceiptType) {
        this.CashReceiptType = cashReceiptType;
    }

    public String getPayDetail() {
        return payDetail;
    }

    public void setPayDetail(String payDetail) {
        this.payDetail = payDetail;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public void setId(int id) {
		this.id = id;
	}
}