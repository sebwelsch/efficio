package keac4.efficio.component;

import jakarta.servlet.http.HttpSession;
import keac4.efficio.model.User;
import keac4.efficio.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
public class ValidateAccess {

    private final UserService userService;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public ValidateAccess(UserService userService) {
        this.userService = userService;
    }

    /**
     * Used to validate if a user has access to an endpoint. If user does have access it will return null.
     *
     * @param session            to check if there is a HTTPSession
     * @param redirectAttributes add flash attributes to a redirect
     * @param projectId          if provided, used to check if user has access to the project
     * @return either login view, error view or null based on the users session
     */
    public String validateUserAccess(HttpSession session, Model model, RedirectAttributes redirectAttributes, Integer projectId) {
        // If the active spring profile is "dev" then bypass authentication
//        if (Objects.equals(activeProfile, "dev")) {
//            return null;
//        }

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
                model.addAttribute("error", "You do not have access to this project. Ask someone with access to share it with you.");
                return "error/403";
            }
        }

        return null;
    }
}
