package dev.rokong.pay.type;

import java.util.List;

import dev.rokong.dto.PayTypeDTO;

public interface PayTypeDAO {
    public List<PayTypeDTO> selectPayTypeList();
    public PayTypeDTO selectPayType(int id);
    public int insertPayType(PayTypeDTO payType);
}
