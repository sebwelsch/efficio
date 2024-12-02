package keac4.efficio.component;

import jakarta.servlet.http.HttpSession;
import keac4.efficio.model.User;
import keac4.efficio.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
public class ValidateAccess {

    private final UserService userService;

    public ValidateAccess(UserService userService) {
        this.userService = userService;
    }

    // validateUserAccess is used to validate if a user has access to a page
    public String validateUserAccess(HttpSession session, RedirectAttributes redirectAttributes, Integer projectId) {
        User userSession = (User) session.getAttribute("userSession");

        // Check if there is a HTTPSession
        if (userSession == null) {
            redirectAttributes.addFlashAttribute("error", "You need to log in to access this page");
            return "redirect:/login";
        }

        // If there is a HTTPSession and a projectId is provided, check if the user has access to the project
        if (projectId != null) {
            boolean hasAccess = userService.doesUserHaveAccess(projectId, userSession.getUserId());
            if (!hasAccess) {
                redirectAttributes.addFlashAttribute("error", "You do not have access to this project. Ask someone with access to share it with you.");
                return "error/403";
            }
        }

        return null;
    }
}
