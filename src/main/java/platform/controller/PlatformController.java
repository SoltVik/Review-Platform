package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import platform.domain.Rating;
import platform.domain.Review;
import platform.domain.Role;
import platform.domain.User;
import platform.service.RatingService;
import platform.service.ReviewService;
import platform.service.RoleService;
import platform.service.UserService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class PlatformController {

    @Autowired
    UserService userService;
    @Autowired
    RatingService ratingService;
    @Autowired
    RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({"/"})
    public String index(Model model) throws ExecutionException, InterruptedException {
        List<Rating> ratingList = ratingService.getTop10Rating();

        model.addAttribute("ratingList", ratingList);
        model.addAttribute("userService", userService);
        return "index";
    }

    @GetMapping("/login")
    public String login(@CurrentSecurityContext SecurityContext context) {
        if (context.getAuthentication().getName().equalsIgnoreCase("anonymousUser")) {
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/reg")
    public String regPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "reg";
    }

    @PostMapping("/reg")
    public String createUser(Model model, @ModelAttribute("user") User user, BindingResult result, String rePassword) {
        if (!user.getPassword().equals(rePassword)) {
            result.rejectValue("pswd", "1", "Password mismatch");
        }
        if (user.getPassword().isEmpty()) {
            result.rejectValue("pswd_empty", "2", "Password is empty");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "3", "There is already an account registered with the same username");
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            result.rejectValue("username", "4", "There is already an account registered with the same email");
        }
        if (!result.hasErrors()) {
            String username = user.getUsername();
            String password = passwordEncoder.encode(user.getPassword());
            String email = user.getEmail();
            Role role = roleService.findById(Role.ROLE_USER);

            User newUser = new User(username, password, email, role, 1);
            userService.add(newUser);
            return "redirect:/login";
        }
        return "reg";
    }

}
