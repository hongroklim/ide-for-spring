package dev.rokong.review.main;

import dev.rokong.dto.ReviewDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/review", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Review"})
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value="", method= RequestMethod.GET)
    @ApiOperation(value="get reviews", notes="[get reviews (productId), (optionCd), (userNm)]")
    public List<ReviewDTO> getReviews(@RequestBody ReviewDTO review){
        return reviewService.getReviewList(review);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ApiOperation(value="get review", notes="get review")
    public ReviewDTO getReview(@PathVariable int id){
        return reviewService.getReview(id);
    }

    @RequestMapping(value="/", method=RequestMethod.POST)
    @ApiOperation(value="create review", notes="[userNm, orderId, productId, optionCd, rate, content, (isVisible)]")
    public ReviewDTO createReview(@RequestBody ReviewDTO review){
        return reviewService.createReview(review);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PATCH)
    @ApiOperation(value="update review", notes="[(rate), (content), (isVisible)]")
    public ReviewDTO updateReview(@RequestBody ReviewDTO review){
        return reviewService.updateReview(review);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ApiOperation(value="delete review", notes="")
    public void removeReview(@PathVariable int id){
        reviewService.deleteReview(id);
    }
}