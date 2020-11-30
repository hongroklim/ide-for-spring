package dev.rokong.pay.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.annotation.PayType;
import dev.rokong.dto.PayTypeDTO;
import dev.rokong.exception.BusinessException;

@Service
public class PayTypeServiceImpl implements PayTypeService {
    
    @Autowired PayTypeDAO pTypeDAO;

    public PayTypeDTO getPayType(int id){
        return pTypeDAO.selectPayType(id);
    }

    public PayTypeDTO getPayTypeNotNull(int id){
        PayTypeDTO payType = this.getPayType(id);
        if(payType == null){
            throw new BusinessException(id+" pay type is not exists");
        }
        return payType;
    }

    public String getPayTypeFullNm(int id){
        PayTypeDTO payType = this.getPayTypeNotNull(id);

        //get type enum
        PayType type = PayType.valueOf(PayType.class, payType.getType());

        StringBuffer sbuf = new StringBuffer();
        //type name
        sbuf.append(type.getTypeNm());
        
        //option 1
        if(payType.getOption1() != null){
            sbuf.append(":")
                .append(payType.getOption1());

            //option2
            if(payType.getOption2() != null){
                sbuf.append("(")
                    .append(payType.getOption2())
                    .append(")");
            }
        }

        return sbuf.toString();
    }
}
