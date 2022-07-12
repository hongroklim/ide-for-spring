package com.company.pay.type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.annotation.PayType;
import com.company.dto.PayTypeDTO;
import com.company.exception.BusinessException;
import com.company.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PayTypeServiceImpl implements PayTypeService {
    
    @Autowired PayTypeDAO pTypeDAO;

    public List<PayTypeDTO> getPayTypes(){
        return pTypeDAO.selectPayTypeList();
    }

    public PayTypeDTO getPayType(int id){
        return pTypeDAO.selectPayType(id);
    }

    public PayTypeDTO getPayTypeNotNull(int id){
        if(id == 0){
            throw new BusinessException("pay type's id is not defined");
        }

        PayTypeDTO payType = this.getPayType(id);
        if(payType == null){
            throw new BusinessException(id+" pay type is not exists");
        }
        return payType;
    }

    private PayTypeDTO getPayTypeNotNull(PayTypeDTO payType){
        return this.getPayTypeNotNull(payType.getId());
    }

    public PayTypeDTO createPayType(PayTypeDTO payType){
        //pay type can not be null
        if(payType.getPayType() == null){
            log.debug("pay type parameter : "+payType.toString());
            throw new BusinessException("pay type is not defined");
        }

        //option2 can not be defined unless option1 is not defined
        if(ObjUtil.isNotEmpty(payType.getOption2())){
            if(ObjUtil.isEmpty(payType.getOption1())){
                log.debug("pay type parameter : "+payType.toString());
                throw new BusinessException("option2 should be empty because option1 is empty");
            }
        }

        //default is enabled=true
        if(payType.getEnabled() == null){
            payType.setEnabled(true);
        }

        //insert
        pTypeDAO.insertPayType(payType);

        return this.getPayTypeNotNull(payType);
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
