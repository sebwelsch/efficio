package keac4.efficio.controller;

import jakarta.servlet.http.HttpSession;
import keac4.efficio.model.User;
import keac4.efficio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("newUser", new User());
        return "signUp";
    }

    @PostMapping("/signup")
    public String saveNewUser(@ModelAttribute User newUser, RedirectAttributes redirectAttributes) {
        if (userService.findByUsername(newUser.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("error", "This username is already in use");
            return "redirect:/signup";
        }

        // This code compares and confirms the passwords are equal to each other.
        if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/signup";
        }

        userService.saveNewUser(newUser);
        redirectAttributes.addFlashAttribute("success", "Your account was created! You can now log in");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String verifyLogin(HttpSession session, @RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(username);
        if (user != null && userService.authenticate(password, user.getPassword())) {
            session.setAttribute("userSession", user);
            redirectAttributes.addFlashAttribute("success", "You have been logged in");
            return "redirect:/overview";
        }
        redirectAttributes.addFlashAttribute("error", "Username or password is incorrect, try again.");
        return "redirect:/login";
    }

    //Endpoint to invalidate the users sessions
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}