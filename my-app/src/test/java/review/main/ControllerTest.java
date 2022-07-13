package review.main;

import config.MvcUnitConfig;
import dev.rokong.review.main.ReviewController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ControllerTest extends MvcUnitConfig {

    @Autowired
    private ReviewController reviewController;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    //TODO review ControllerTest

    @Test
    public void getReviews() throws Exception {

    }

    public void getReview() throws Exception {

    }

    public void createReview() throws Exception {

    }

    public void updateReview() throws Exception {

    }

    public void removeReview() throws Exception {

    }
}
