package dev.rokong.pay.api;

import dev.rokong.dto.PayApiDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PayApiDAOImpl implements PayApiDAO {

    public static final String PREFIX = "dev.rokong.payApi.";

    @Autowired SqlSessionTemplate sqlSession;

    public String getApiKey(int orderId){
        return sqlSession.selectOne(PREFIX+"getApiKey", orderId);
    }

    public void insertPayApi(PayApiDTO payApi){
        sqlSession.insert(PREFIX+"insertPayApi", payApi);
    }
}
