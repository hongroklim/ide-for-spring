package dev.rokong.mock;

import dev.rokong.dto.ReviewDTO;
import dev.rokong.review.main.ReviewService;
import dev.rokong.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("mockReview")
public class MockReview extends AbstractMockObject<ReviewDTO> {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MockUser mUser;

    @Autowired
    private MockOrderProduct mOProduct;

    @Override
    public ReviewDTO temp() {
        ReviewDTO review = new ReviewDTO();

        review.setUserNm(mUser.any().getUserNm());
        review.setOrderId(mOProduct.any().getOrderId());
        review.setProductId(mOProduct.any().getProductId());
        review.setOptionCd(mOProduct.any().getOptionCd());
        review.setRate(RandomUtil.randomInt(1)+0.5);
        review.setContent("reviews-"+RandomUtil.randomString(20));
        review.setIsVisible(true);

        return review;
    }

    @Override
    protected ReviewDTO createObjService(ReviewDTO obj) {
        return reviewService.createReview(obj);
    }

    @Override
    protected ReviewDTO getObjService(ReviewDTO obj) {
        return reviewService.getReview(obj.getId());
    }
}
