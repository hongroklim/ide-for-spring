package com.company.annotation;

public enum DeliveryType {
    DOMESTIC("국내배송"),
    FOREIGN("해외배송"),
    ONLINE("온라인"),
    ETC("기타");

    private String typeNm;

    DeliveryType(String typeNm) {
        this.typeNm = typeNm;
    }

    public String getTypeNm(){
        return this.typeNm;
    }
}