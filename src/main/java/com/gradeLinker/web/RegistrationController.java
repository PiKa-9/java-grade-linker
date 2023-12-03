package com.gradeLinker.web;

import com.gradeLinker.domain.user.User;
import com.gradeLinker.dto.storage.UserDTOMapper;
import com.gradeLinker.dto.web.request.GradeTableRequest;
import com.gradeLinker.dto.web.request.RegistrationRequest;
import com.gradeLinker.repository.UsersRepository;
import com.gradeLinker.service.PasswordHasher;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;


@Controller
public class RegistrationController {
    private final PasswordHasher hasher;
    private final UserDTOMapper userDTOMapper;
    private final UsersRepository usersRepo;

    @Autowired
    public RegistrationController(PasswordHasher hasher, UserDTOMapper userDTOMapper, UsersRepository usersRepo) {
        this.hasher = hasher;
        this.userDTOMapper = userDTOMapper;
        this.usersRepo = usersRepo;
    }



    @GetMapping("/register")
    public String register(Model model) {
        return "pages/register.html";
    }

    private boolean validRegistrationRequest(RegistrationRequest request) {
        // TODO
        return true;
    }
    @PostMapping("/register")
    public String registerPost(@ModelAttribute RegistrationRequest request, HttpSession session, Model model) {
        if (!validRegistrationRequest(request)) { return "redirect:/error"; }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("invalid", "Passwords aren't equal");
            return "pages/register.html";
        }

        if (usersRepo.existsById(request.getUsername())) {
            model.addAttribute("invalid", "Such username already exists");
            return "pages/register.html";
        }

        /* Add user to users repository */
        User user = new User(
                request.getUsername(),
                hasher.hash(request.getPassword()),
                new HashSet<>(),
                request.getFirstName(),
                request.getLastName(),
                new HashSet<>()
        );

        user.addRoles("join_course");
        if (request.getType().equals("teacher")) {
            user.addRoles("create_course");
        }
        usersRepo.save(user.getUsername(), userDTOMapper.toDTO(user));
        session.setAttribute("username", request.getUsername());

        return "redirect:/h";
    }
}
