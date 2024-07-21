package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import platform.domain.Review;
import platform.domain.User;
import platform.service.ReviewService;
import platform.service.UserService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public String userPage(Model model) {
        List<List<User>> userList = userService.getUserLists();
        model.addAttribute("userList", userList);
        model.addAttribute("userService", userService);
        return "users";
    }

    @GetMapping("/user/{id}")
    public String viewUser(Model model, @PathVariable int id, @AuthenticationPrincipal UserDetails currentUser) throws ExecutionException, InterruptedException {
        User receiver = userService.findById(id);
        if (receiver == null) {
            return "redirect:/users";
        }

        List<Review> reviews = reviewService.getByReceiverId(id);

        double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average().orElse(0);

        model.addAttribute("receiver", receiver);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviews_num", reviews.size());
        model.addAttribute("reviews_avg", String.format("%.1f", avgRating));
        model.addAttribute("userService", userService);

        return "user";
    }

    @PostMapping("/users")
    public String editUser(String editEmail, String editPassword, int userId) {
        if (!editEmail.isEmpty()) {
            User oldUser = userService.findById(userId);
            if (oldUser == null) {
                return "redirect:/users";
            }

            User user;
            if (userService.isValidEmail(editEmail)) {
                user = userService.findByEmail(editEmail);
                if (user != null) {;
                    if (user.getId() != oldUser.getId()) {
                        return "redirect:/users";
                    }
                }
                oldUser.setEmail(editEmail);
            }

            if (!editPassword.isEmpty()) {
                oldUser.setPassword(passwordEncoder.encode(editPassword));
            }

            userService.save(oldUser);
        }

        return "redirect:/users";
    }

    @GetMapping("/users/{letter}")
    public String userPageLetter(Model model, @PathVariable String letter) {
        List<List<User>> userList = userService.getUserListsByLetter(letter);
        model.addAttribute("userList", userList);
        model.addAttribute("userService", userService);
        return "users";
    }
}
