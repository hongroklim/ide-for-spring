package dev.rokong.review.main;

import dev.rokong.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<ReviewDTO> getReviews(@RequestBody ReviewDTO review){
        return reviewService.getReviewList(review);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ReviewDTO getReview(@PathVariable int id){
        return reviewService.getReview(id);
    }

    @RequestMapping(value="/", method=RequestMethod.POST)
    public ReviewDTO createReview(@RequestBody ReviewDTO review){
        return reviewService.createReview(review);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PATCH)
    public ReviewDTO updateReview(@RequestBody ReviewDTO review){
        return reviewService.updateReview(review);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public void removeReview(@PathVariable int id){
        reviewService.deleteReview(id);
    }
}
