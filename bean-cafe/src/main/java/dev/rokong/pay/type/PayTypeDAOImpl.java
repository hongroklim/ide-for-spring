package dev.rokong.pay.type;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import dev.rokong.dto.PayTypeDTO;

public class PayTypeDAOImpl implements PayTypeDAO {
    
    public static final String PREFIX = "dev.rokong.payType.";

    @Autowired SqlSessionTemplate sqlSession;

    public PayTypeDTO selectPayType(int id){
        return sqlSession.selectOne(PREFIX+"selectPayType", id);
    }
}