package com.company.pay.type;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.company.dto.PayTypeDTO;

@Repository
public class PayTypeDAOImpl implements PayTypeDAO {
    
    public static final String PREFIX = "com.company.payType.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<PayTypeDTO> selectPayTypeList(){
        return sqlSession.selectList(PREFIX+"selectPayType");
    }

    public PayTypeDTO selectPayType(int id){
        return sqlSession.selectOne(PREFIX+"selectPayType", id);
    }

    public int insertPayType(PayTypeDTO payType){
        sqlSession.insert(PREFIX+"insertPayType", payType);
        return payType.getId();
    }
}
