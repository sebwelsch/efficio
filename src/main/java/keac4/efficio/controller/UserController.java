package keac4.efficio.controller;

import keac4.efficio.model.User;
import keac4.efficio.Service.UserService;
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
        return "signup";
    }

    @PostMapping("/signup")
    public String saveNewUser(@ModelAttribute User newUser, Model model, RedirectAttributes redirectAttributes) {
        if (userService.findByUsername(newUser.getUsername()) != null) {
            model.addAttribute("error", "Dette username er allerede registreret");
            return "signup";
        }

        userService.saveNewUser(newUser);
        redirectAttributes.addFlashAttribute("success", "Din konto blev oprettet. Du kan nu logge ind.");
        return "redirect:/login";
    }
}