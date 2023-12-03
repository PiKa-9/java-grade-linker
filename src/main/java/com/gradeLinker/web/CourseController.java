package com.gradeLinker.web;

import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.GradeInfo;
import com.gradeLinker.domain.user.User;
import com.gradeLinker.dto.web.*;
import com.gradeLinker.dto.web.request.GradeColumnRequest;
import com.gradeLinker.dto.web.request.GradeTableRequest;
import com.gradeLinker.dto.web.request.GradeTableRequestMapper;
import com.gradeLinker.dto.web.request.RegistrationRequest;
import com.gradeLinker.service.CourseService;
import com.gradeLinker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class CourseController {
    private final AccountViewDTOMapper accountDTOMapper;
    private final CourseTabViewDTOMapper courseTabDTOMapper;
    private final GradeTableViewDTOMapper gradeTableViewDTOMapper;
    private final GradeTableRequestMapper gradeTableRequestMapper;
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public CourseController(AccountViewDTOMapper accountDTOMapper, CourseTabViewDTOMapper courseTabDTOMapper, GradeTableViewDTOMapper gradeTableViewDTOMapper, GradeTableRequestMapper gradeTableRequestMapper, UserService userService, CourseService courseService) {
        this.accountDTOMapper = accountDTOMapper;
        this.courseTabDTOMapper = courseTabDTOMapper;
        this.gradeTableViewDTOMapper = gradeTableViewDTOMapper;
        this.gradeTableRequestMapper = gradeTableRequestMapper;
        this.userService = userService;
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
    public String coursePage(@PathVariable("course_id") String courseId, HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        Course course = courseService.getCourseById(courseId);
        if (course == null) { return "redirect:/error"; }

        CourseParticipant participant = course.getParticipantByUsername(user.getUsername());
        if (participant == null) { return "redirect/error"; }

        if (participant.hasRoles("all-grade-viewer")) {
            return String.format("redirect:/c/%s/all-grades", courseId);
        }
        if (participant.hasRoles("has-grades")) {
            return String.format("redirect:/c/%s/own-grades", courseId);
        }

        return "pages/error.html";
    }


    @GetMapping("/c/{course_id}/all-grades")
    public String allGradesView(@PathVariable("course_id") String courseId, HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        Course course = courseService.getCourseById(courseId);
        if (course == null) { return "redirect:/error"; }

        CourseParticipant participant = course.getParticipantByUsername(user.getUsername());
        if (participant == null) { return "redirect/error"; }
        if (!participant.hasRoles("all-grade-viewer")) { return "pages/error.html"; }

        this.setLeftTab(model, user);
        model.addAttribute("courseHeading", new CourseHeadingViewDTO(
                course.getId(),
                course.getTitle()
        ));
        model.addAttribute("gradeTable", gradeTableViewDTOMapper.toDTO(course));
        if (participant.hasRoles("all-grade-changer")) {
            model.addAttribute("saveGradeTable", true);
            model.addAttribute("addGradeColumn", true);
        }

        return "pages/all_grades_view.html";
    }

    @GetMapping("/c/{course_id}/own-grades")
    public String ownGradesView(@PathVariable("course_id") String courseId, HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) {
            return "redirect:/error";
        }

        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return "redirect:/error";
        }

        CourseParticipant participant = course.getParticipantByUsername(user.getUsername());
        if (participant == null) {
            return "redirect/error";
        }
        if (!participant.hasRoles("has-grades")) { return "pages/error.html"; }

        this.setLeftTab(model, user);
        model.addAttribute("courseHeading", new CourseHeadingViewDTO(
                course.getId(),
                course.getTitle()
        ));
        model.addAttribute("gradeTable", gradeTableViewDTOMapper.toDTO(course, participant.getUsername()));

        return "pages/own_grades_view.html";
    }

    private boolean validGradeTableRequest(GradeTableRequest gradeTableRequest) {
        // TODO
        return true;
    }
    @PostMapping("/c/{course_id}/all-grades/save-grade-table")
    public String saveGradeTable(@PathVariable("course_id") String courseId, @ModelAttribute GradeTableRequest gradeTableRequest, HttpSession session, Model model) {
        if (!validGradeTableRequest(gradeTableRequest)) { return "/redirect:error"; }
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        Course course = courseService.getCourseById(courseId);
        if (course == null) { return "redirect:/error"; }

        CourseParticipant participant = course.getParticipantByUsername(user.getUsername());
        if (participant == null) { return "redirect/error"; }
        if (!participant.hasRoles("all-grade-changer")) { return "pages/error.html"; }

        course.setCourseGrades(gradeTableRequestMapper.fromDTO(gradeTableRequest));
        courseService.saveCourse(course);

        return String.format("redirect:/c/%s/all-grades", courseId);
    }

    private boolean validGradeColumnRequest(GradeColumnRequest gradeColumnRequest) {
        // TODO
        return true;
    }
    @PostMapping("/c/{course_id}/all-grades/add-grade-column")
    public String addGradeColumn(@PathVariable("course_id") String courseId, @ModelAttribute GradeColumnRequest gradeColumnRequest, HttpSession session, Model model) {
        if (!validGradeColumnRequest(gradeColumnRequest)) { return "/redirect:error"; }
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) { return "redirect:/error"; }

        Course course = courseService.getCourseById(courseId);
        if (course == null) { return "redirect:/error"; }

        CourseParticipant participant = course.getParticipantByUsername(user.getUsername());
        if (participant == null) { return "redirect/error"; }
        if (!participant.hasRoles("all-grade-changer")) { return "pages/error.html"; }

        GradeInfo gradeInfo = new GradeInfo(
                "ordinary",
                gradeColumnRequest.getDate()
        );
        course.getCourseGrades().add(gradeInfo, null);
        courseService.saveCourse(course);

        return String.format("redirect:/c/%s/all-grades", courseId);
    }
}
