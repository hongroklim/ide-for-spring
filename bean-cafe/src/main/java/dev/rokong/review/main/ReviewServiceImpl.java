package dev.rokong.review.main;

import dev.rokong.dto.OrderProductDTO;
import dev.rokong.dto.ReviewDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.user.UserService;
import dev.rokong.util.ObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderProductService oProductService;

    public List<ReviewDTO> getReviewList(ReviewDTO review) {
        if(review == null){
            throw new IllegalArgumentException("review is null");
        }

        if(review.getProductId() == 0 && ObjUtil.isEmpty(review.getUserNm())){
            throw new BusinessException("product id or userNm must be defined at least one");
        }

        return reviewDAO.selectList(review);
    }

    private void verifyIdDefined(int id){
        if(id == 0){
            throw new IllegalArgumentException("id is not defined");
        }
    }

    public ReviewDTO getReview(int id) {
        this.verifyIdDefined(id);
        return reviewDAO.select(id);
    }

    public ReviewDTO getReviewNotNull(int id) {
        ReviewDTO review = reviewDAO.select(id);
        if(review == null){
            throw new BusinessException(id+" review is not exists");
        }
        return review;
    }

    public void checkReviewExist(int id) {
        this.verifyIdDefined(id);

        if(reviewDAO.count(id) == 0){
            throw new BusinessException(id + " review is not exists");
        }
    }

    public ReviewDTO createReview(ReviewDTO review) {
        //check rate and content
        if(review.getRate() == null){
            throw new BusinessException("review rate is not defined");

        } else if(ObjUtil.isEmpty(review.getContent())){
            throw new BusinessException("review content is not defined");
        }

        //check userNm
        userService.checkUserExist(review.getUserNm());

        //check order product
        OrderProductDTO oProduct = new OrderProductDTO();
        oProduct.setOrderId(review.getOrderId());
        oProduct.setProductId(review.getProductId());
        oProduct.setOptionCd(review.getOptionCd());
        oProductService.checkOProductExist(oProduct);

        //set default isVisible
        if(review.getIsVisible() == null){
            review.setIsVisible(true);
        }

        //insert
        int id = reviewDAO.insert(review);

        return this.getReviewNotNull(id);
    }

    public ReviewDTO updateReview(ReviewDTO review) {
        this.checkReviewExist(review.getId());

        if(review.getRate() == null && ObjUtil.isEmpty(review.getContent())
                && review.getIsVisible() == null ){
            //if nothing to update, return
            return this.getReviewNotNull(review.getId());
        }

        //update
        reviewDAO.update(review);

        return this.getReviewNotNull(review.getId());
    }

    public void deleteReview(int id) {
        this.checkReviewExist(id);
        reviewDAO.delete(id);
    }

    public void updateOProductInvalid(int productId, String optionCd){
        ReviewDTO review = new ReviewDTO();
        review.setProductId(productId);
        review.setOptionCd(optionCd);

        reviewDAO.updateOProductInvalid(review);
    }
}
