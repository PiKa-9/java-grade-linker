package com.gradeLinker.web;


import com.gradeLinker.domain.user.User;
import com.gradeLinker.dto.web.AccountViewDTOMapper;
import com.gradeLinker.dto.web.CourseTabViewDTOMapper;
import com.gradeLinker.repository.UsersRepository;
import com.gradeLinker.service.CourseService;
import com.gradeLinker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final AccountViewDTOMapper accountDTOMapper;
    private final CourseTabViewDTOMapper courseTabDTOMapper;
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public UserController(AccountViewDTOMapper accountDTOMapper, CourseTabViewDTOMapper courseTabDTOMapper, UserService userService, CourseService courseService) {
        this.accountDTOMapper = accountDTOMapper;
        this.courseTabDTOMapper = courseTabDTOMapper;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/h")
    public String homePage(HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        model.addAttribute("account", accountDTOMapper.toDTO(user));
        model.addAttribute("courseTab",
                courseTabDTOMapper.toDTO(
                        courseService.getCoursesByIds(user.getCourseIds())
                )
        );
        /* user roles */
        if (user.hasRole("join_course")) {
            model.addAttribute("join_course", true);
        }
        if (user.hasRole("create_course")) {
            model.addAttribute("create_course", true);
        }

        return "pages/homepage.html";
    }

    @GetMapping("/join-course")
    public String joinCourse(HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        if (user.hasRole("join_course")) {
            return "pages/join_course.html";
        } else {
            return "pages/error.html";
        }
    }

    @GetMapping("/create-course")
    public String createCourse(HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        if (user.hasRole("create_course")) {
            return "pages/create_course.html";
        } else {
            return "pages/error.html";
        }
    }
}
