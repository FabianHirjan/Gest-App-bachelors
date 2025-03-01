package hirjanfabian.gestapp.controllers;


import hirjanfabian.gestapp.business.ComplaintsService;
import hirjanfabian.gestapp.business.UserService;
import hirjanfabian.gestapp.entities.Complaints;
import hirjanfabian.gestapp.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static hirjanfabian.gestapp.Routes.*;



@Controller
@RequestMapping(COMPLAINTS)
public class AddComplaintController {
    private final ComplaintsService complaintsService;
    private final UserService userService;

    public AddComplaintController(ComplaintsService complaintsService, UserService userService) {
        this.complaintsService = complaintsService;
        this.userService = userService;
    }

    @GetMapping(NEW)
    public String showComplaintForm(Model model, Authentication authentication,
                                    @RequestParam(required = false) String succes) {
        String username = authentication.getName();
        User loggedUser = userService.getUserByUsername(username);
        model.addAttribute("complaint", new Complaints());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("loggedUser", loggedUser);
        if (succes != null) {
            model.addAttribute("message", "Plângerea a fost depusă cu succes!");
        }
        return "complaintForm";
    }


    @PostMapping(SAVE)
    public String saveComplaint(@ModelAttribute("complaint") Complaints complaint, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User loggedUser = userService.getUserByUsername(username);

        if (complaint.getComplaintAgainst() != null && complaint.getComplaintAgainst().getId().equals(loggedUser.getId())) {
            redirectAttributes.addFlashAttribute("error", "Nu te poți reclama pe tine însuți.");
            return "redirect:/complaints/new";
        }

        complaint.setUser(loggedUser);
        complaintsService.saveComplaint(complaint);
        redirectAttributes.addFlashAttribute("message", "Plângerea a fost depusă cu succes!");
        return "redirect:/complaints/new?succes";
    }



}
