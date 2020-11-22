package dev.rokong.pay.type;

import dev.rokong.dto.PayTypeDTO;

public interface PayTypeDAO {
    public PayTypeDTO selectPayType(int id);
}
