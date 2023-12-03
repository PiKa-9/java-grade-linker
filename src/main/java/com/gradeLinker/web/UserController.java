package com.gradeLinker.web;


import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.Student;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashSet;

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
    @PostMapping("/join-course")
    public String joinCoursePost(@RequestParam("courseId") String courseId,  HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        if (!user.hasRole("join_course")) { return "pages/error.html"; }
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            model.addAttribute("invalid", "Invalid Course Id");
            return "pages/join_course.html";
        }

        user.addCourseIds(courseId);
        userService.saveUser(user);
        /* User joins the course as a student */
        course.addParticipant(new Student(
                user.getUsername(),
                user.getFullName(),
                new HashSet<>(Arrays.asList("has-grades")),
                null
        ));
        courseService.saveCourse(course);

        return String.format("redirect:/c/%s", courseId);
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
    @PostMapping("/create-course")
    public String createCoursePost(@RequestParam("courseTitle") String courseTitle, HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        if (!user.hasRole("create_course")) { return "pages/error.html"; }
        String courseId = courseService.createCourse(
                courseTitle,
                new CourseParticipant(
                        user.getUsername(),
                        user.getFullName(),
                        new HashSet<>(Arrays.asList("all-grade-viewer", "all-grade-changer"))
                )
        );

        user.addCourseIds(courseId);
        userService.saveUser(user);

        return String.format("redirect:/c/%s", courseId);
    }
}
