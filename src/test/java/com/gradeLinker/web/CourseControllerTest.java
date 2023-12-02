package com.gradeLinker.web;


import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.user.User;
import com.gradeLinker.service.CourseService;
import com.gradeLinker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private CourseService courseService;

    private MockHttpSession session;

    private String courseId;
    private Course course;
    private CourseParticipant participant;
    private User user;
    private String usernameT;

    @BeforeEach
    void setUp() {
        usernameT = "usernameT";
        user = new User(
                usernameT,
                0,
                new HashSet<>(),
                null, null,
                new HashSet<>()
        );
        participant = new CourseParticipant(
                usernameT,
                new HashSet<>()
        );
        courseId = "course-idT";
        course = new Course(
                courseId,
                null,
                new HashMap<>(),
                new HashMap<>()
        );

        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        session = new MockHttpSession();
        session.setAttribute("username", usernameT);
    }

    @Test
    void ShouldRedirectToAllGradesView() throws Exception {
        participant.addRoles("all-grade-viewer");
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);

        mockMvc.perform(get("/c/{course_id}", courseId).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(String.format("/c/%s/all-grades", courseId)));

        verify(courseService, times(1)).getCourseById(courseId);
    }

    @Test
    void ShouldRedirectToOwnGradesView() throws Exception {
        participant.addRoles("has-grades");
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);

        mockMvc.perform(get("/c/{course_id}", courseId).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(String.format("/c/%s/own-grades", courseId)));

        verify(courseService, times(1)).getCourseById(courseId);
    }

    @Test
    void ShouldRedirectToErrorIfInvalidRole() throws Exception {
        /* participant with no course-roles */
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);

        mockMvc.perform(get("/c/{course_id}", courseId).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));

        verify(courseService, times(1)).getCourseById(courseId);
    }
}