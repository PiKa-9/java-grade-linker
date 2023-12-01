package com.gradeLinker.web;

import com.gradeLinker.domain.UserFactory;
import com.gradeLinker.domain.user.User;
import com.gradeLinker.dto.storage.UserDTOMapper;
import com.gradeLinker.dto.web.RegistrationRequest;
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
    private final UserFactory userFactory;
    private final UserDTOMapper userDTOMapper;
    private final UsersRepository usersRepo;

    @Autowired
    public RegistrationController(PasswordHasher hasher, UserFactory userFactory, UserDTOMapper userDTOMapper, UsersRepository usersRepo) {
        this.hasher = hasher;
        this.userFactory = userFactory;
        this.userDTOMapper = userDTOMapper;
        this.usersRepo = usersRepo;
    }



    @GetMapping("/register")
    public String register(Model model) {
        return "pages/register.html";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute RegistrationRequest response, HttpSession session, Model model) {
        if (!response.getPassword().equals(response.getConfirmPassword())) {
            model.addAttribute("invalid", "Passwords aren't equal");
            return "pages/register.html";
        }

        if (usersRepo.existsById(response.getUsername())) {
            model.addAttribute("invalid", "Such username already exists");
            return "pages/register.html";
        }

        /* Add user to users repository */
        User user = userFactory.createUser(
                response.getUsername(),
                hasher.hash(response.getPassword()),
                new HashSet<>(),
                response.getFirstName(),
                response.getLastName(),
                new HashSet<>()
        );

        user.addRoles("join_course");
        if (response.getType().equals("teacher")) {
            user.addRoles("create_course");
        }
        usersRepo.save(user.getUsername(), userDTOMapper.toDTO(user));
        session.setAttribute("username", response.getUsername());

        return "redirect:/h";
    }
}
