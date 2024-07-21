package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import platform.domain.Review;
import platform.service.ReviewService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class RestReviewController {
    @Autowired
    private ReviewService reviewService;


    @PostMapping("/reviews")
    public String saveReview(@RequestBody Review review) throws ExecutionException, InterruptedException {
        return reviewService.saveReview(review);
    }

   @GetMapping("/reviews/{uid}")
    public Review getReview(@PathVariable String  uid) throws ExecutionException, InterruptedException {
        return reviewService.getReviewDetails(uid);
    }

    @GetMapping("/reviews")
    public List<Review> getReviews() throws ExecutionException, InterruptedException {
        return reviewService.getAll();
    }

    @PutMapping("/reviews")
    public String update(@RequestBody Review review) throws ExecutionException, InterruptedException {
        return reviewService.update(review);
    }

    @DeleteMapping("/reviews/{uid}")
    public String delete(@PathVariable String  uid) throws ExecutionException, InterruptedException {
        return reviewService.delete(uid);
    }



}
