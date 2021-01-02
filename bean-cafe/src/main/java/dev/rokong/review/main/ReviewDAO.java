package dev.rokong.review.main;

import dev.rokong.dto.ReviewDTO;

import java.util.List;

public interface ReviewDAO {

    /**
     * select review list by parameters not null
     *
     * @param review productId, optionCd, userNm
     * @return list of review
     */
    public List<ReviewDTO> selectList(ReviewDTO review);
    public ReviewDTO select(int id);
    public int count(int id);
    public int insert(ReviewDTO review);

    /**
     * update review specific field not null
     *
     * @param review rate, content, isVisible
     */
    public void update(ReviewDTO review);
    public void delete(int id);

    public void updateOProductInvalid(ReviewDTO review);
}
