package dev.rokong.pay.type;

import dev.rokong.dto.PayTypeDTO;

public interface PayTypeService {
    public PayTypeDTO getPayType(int id);
    public PayTypeDTO getPayTypeNotNull(int id);
    public String getPayTypeFullNm(int id);
}