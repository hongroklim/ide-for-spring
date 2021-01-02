package dev.rokong.review.main;

import dev.rokong.dto.ReviewDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDAOImpl implements ReviewDAO {

    public static final String PREFIX = "dev.rokong.review.";

    @Autowired
    private SqlSession sqlSession;

    public List<ReviewDTO> selectList(ReviewDTO review) {
        return sqlSession.selectList(PREFIX+"selectList", review);
    }

    public ReviewDTO select(int id) {
        return sqlSession.selectOne(PREFIX+"select", id);
    }

    public int count(int id) {
        return sqlSession.selectOne(PREFIX+"count", id);
    }

    public int insert(ReviewDTO review) {
        sqlSession.insert(PREFIX+"insert", review);
        return review.getId();
    }

    public void update(ReviewDTO review) {
        sqlSession.update(PREFIX+"update", review);
    }

    public void delete(int id) {
        sqlSession.update(PREFIX+"delete", id);
    }

    public void updateOProductInvalid(ReviewDTO review){
        sqlSession.update(PREFIX + "updateOProductInvalid", review);
    }
}
