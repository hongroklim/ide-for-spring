package dev.rokong.pay.type;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.PayTypeDTO;

@Repository
public class PayTypeDAOImpl implements PayTypeDAO {
    
    public static final String PREFIX = "dev.rokong.payType.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<PayTypeDTO> selectPayTypeList(){
        return sqlSession.selectList(PREFIX+"selectPayType");
    }

    public PayTypeDTO selectPayType(int id){
        return sqlSession.selectOne(PREFIX+"selectPayType", id);
    }
}
