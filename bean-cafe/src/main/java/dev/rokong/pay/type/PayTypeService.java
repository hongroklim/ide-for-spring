package dev.rokong.pay.type;

import java.util.List;

import dev.rokong.dto.PayTypeDTO;

public interface PayTypeService {
    public List<PayTypeDTO> getPayTypes();
    public PayTypeDTO getPayType(int id);
    public PayTypeDTO getPayTypeNotNull(int id);
    public PayTypeDTO createPayType(PayTypeDTO payType);
    public String getPayTypeFullNm(int id);
}