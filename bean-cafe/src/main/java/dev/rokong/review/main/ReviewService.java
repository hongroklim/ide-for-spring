package dev.rokong.review.main;

import dev.rokong.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    public List<ReviewDTO> getReviewList(ReviewDTO review);
    public ReviewDTO getReview(int id);
    public ReviewDTO getReviewNotNull(int id);
    public void checkReviewExist(int id);
    public ReviewDTO createReview(ReviewDTO review);
    public ReviewDTO updateReview(ReviewDTO review);
    public void deleteReview(int id);
    public void updateOProductInvalid(int productId, String optionCd);
}
