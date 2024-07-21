package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import platform.domain.Review;
import platform.service.ReviewService;
import platform.service.UserService;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/review/add")
    public String addReview(int rating, String text, int author, int receiver) throws ExecutionException, InterruptedException {
        UUID uid = UUID.randomUUID();
        Review review = new Review(uid.toString(), rating, text, author, receiver);

        reviewService.saveReview(review);

        return "redirect:/user/" + receiver;
    }

}