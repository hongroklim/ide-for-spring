package dev.rokong.ship.main;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShipDAOImpl implements ShipDAO {
    
    @Autowired SqlSession sqlSession;

    public static final String PREFIX = "dev.rokong.ship.";



}