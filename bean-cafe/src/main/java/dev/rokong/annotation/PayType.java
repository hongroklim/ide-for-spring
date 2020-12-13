package dev.rokong.annotation;

public enum PayType {
    API("API"),
    TRANSFER("계좌이체"),
    CARD("신용/체크카드"),
    PHONE("휴대폰");

    private String typeNm;

    PayType(String typeNm){
        this.typeNm = typeNm;
    }

    public String getTypeNm(){
        return this.typeNm;
    }
}
