package keac4.efficio.controller;

import keac4.efficio.model.User;
import keac4.efficio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String saveNewUser(@ModelAttribute User newUser, Model model, RedirectAttributes redirectAttributes) {
        if (userService.findByUsername(newUser.getUsername()) != null) {
            model.addAttribute("error", "This username is already in use");
            return "redirect:/signup";
        }

        // This code compares and confirms the passwords are equal to each other.
        if(!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "signUp";
        }

        userService.saveNewUser(newUser);
        redirectAttributes.addFlashAttribute("success", "Your account has been created. You can now log in");
        return "redirect:/signup";
    }
}