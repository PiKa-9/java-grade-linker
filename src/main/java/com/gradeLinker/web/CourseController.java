package com.gradeLinker.web;

import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.user.User;
import com.gradeLinker.dto.web.AccountViewDTOMapper;
import com.gradeLinker.dto.web.CourseHeadingViewDTO;
import com.gradeLinker.dto.web.CourseTabViewDTOMapper;
import com.gradeLinker.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CourseController {
    private final AccountViewDTOMapper accountDTOMapper;
    private final CourseTabViewDTOMapper courseTabDTOMapper;
    private final CourseService courseService;

    @Autowired
    public CourseController(AccountViewDTOMapper accountDTOMapper, CourseTabViewDTOMapper courseTabDTOMapper, CourseService courseService) {
        this.accountDTOMapper = accountDTOMapper;
        this.courseTabDTOMapper = courseTabDTOMapper;
        this.courseService = courseService;
    }

    /* We have same left tab logic in many cases, so DRY principle in action */
    private void setLeftTab(Model model, User user) {
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
    }

    @GetMapping("/c/{course_id}")
    public String coursePage(@PathVariable("course_id") String courseId, HttpServletRequest request, HttpSession session, Model model) {
        User user = (User) request.getAttribute("user");
        if (user == null) { return "redirect:/error"; }

        Course course = courseService.getCourseById(courseId);
        CourseParticipant participant = course.getParticipantByUsername(user.getUsername());
        if (participant.hasRoles("all-grade-viewer")) {
            return String.format("redirect:/c/%s/all-grades", courseId);
        }
        if (participant.hasRoles("has-grades")) {
            return String.format("redirect:/c/%s/own-grades", courseId);
        }

        return "pages/error.html";
    }


    @GetMapping("/c/{course_id}/all-grades")
    public String allGradesView(@PathVariable("course_id") String courseId, HttpServletRequest request, HttpSession session, Model model) {
        User user = (User) request.getAttribute("user");
        if (user == null) { return "redirect:/error"; }

        Course course = courseService.getCourseById(courseId);
        CourseParticipant participant = course.getParticipantByUsername(user.getUsername());
        if (!participant.hasRoles("all-grade-viewer")) { return "pages/error.html"; }


        this.setLeftTab(model, user);
        model.addAttribute("courseHeading", new CourseHeadingViewDTO(
                course.getId(),
                course.getTitle()
        ));
        // model.addAttribute("gradeTable", // TODO);

        return "pages/all_grades_view.html";
    }

}
