package dev.rokong.dto;

import java.util.Date;

public class CartDTO {
    private String userNm;
    private int productId;
    private String optionCd;
    private int cnt;
    private Date updateDt;

    public String getUserNm() {
        return userNm;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
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

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }
}