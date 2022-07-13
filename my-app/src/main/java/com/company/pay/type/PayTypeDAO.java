package com.company.pay.type;

import java.util.List;

import com.company.dto.PayTypeDTO;

public interface PayTypeDAO {
    public List<PayTypeDTO> selectPayTypeList();
    public PayTypeDTO selectPayType(int id);
    public int insertPayType(PayTypeDTO payType);
}
