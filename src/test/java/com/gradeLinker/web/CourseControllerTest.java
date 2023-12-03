package com.gradeLinker.web;


import com.gradeLinker.domain.CourseFactory;
import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.GradeInfo;
import com.gradeLinker.domain.user.User;
import com.gradeLinker.service.CourseService;
import com.gradeLinker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {
    @Autowired
    private GradeFactory gradeFactory;
    @Autowired
    private CourseFactory courseFactory;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @SpyBean
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
                new HashSet<>(Arrays.asList("join_course", "create_course")),
                null, null,
                new HashSet<>()
        );
        participant = new CourseParticipant(
                usernameT,
                "full-nameT",
                new HashSet<>()
        );
        courseId = "course-idT";
        CourseGrades courseGrades = gradeFactory.createCourseGrades(
                Arrays.asList("user1T", "user2T")
        );
        courseGrades.add(
                new GradeInfo("ordinary", "02-01-2024"),
                Arrays.asList(null, 75.)
        );
        course = courseFactory.createCourse(
                courseId,
                "course-titleT",
                new HashMap<>(),
                gradeFactory.createCourseGrades(new ArrayList<>())
        );

        when(userService.getUserByUsername(usernameT)).thenReturn(user);
        doNothing().when(courseService).saveCourse(any());

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

    @Test
    void ShouldDisplayAllGradesView() throws Exception {
        participant.addRoles("all-grade-viewer", "all-grade-changer");
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);

        mockMvc.perform(get("/c/{course_id}/all-grades", courseId).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/all_grades_view.html"))
                .andExpect(model().attributeExists("courseHeading", "gradeTable"))
                .andExpect(model().attributeExists("saveGradeTable", "addGradeColumn"));
    }

    @Test
    void ShouldDisplayOwnGradesView() throws Exception {
        participant.addRoles("has-grades");
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);

        mockMvc.perform(get("/c/{course_id}/own-grades", courseId).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/own_grades_view.html"))
                .andExpect(model().attributeExists("courseHeading", "gradeTable"));
    }

    @Test
    void ShouldSaveGradeTable() throws Exception {
        participant.addRoles("all-grade-changer");
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);


        this.mockMvc.perform(post("/c/{course_id}/all-grades/save-grade-table", courseId).session(session)
                        // .param("studentUsernames", ...)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(String.format("/c/%s/all-grades", courseId)));

        verify(courseService, times(1)).saveCourse(
                argThat(i -> i.getId().equals(courseId))
        );
    }

    @Test
    void ShouldNotSaveGradeTable() throws Exception {
        /* participant hasn't got 'all-grade-changer' role */
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);


        this.mockMvc.perform(post("/c/{course_id}/all-grades/save-grade-table", courseId).session(session)
                        // .param("studentUsernames", ...)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));

        verify(courseService, times(0)).saveCourse(
                any()
        );
    }

    @Test
    void ShouldAddGradeColumn() throws Exception {
        participant.addRoles("all-grade-changer");
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);


        this.mockMvc.perform(post("/c/{course_id}/all-grades/add-grade-column", courseId).session(session)
                        // .param("studentUsernames", ...)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(String.format("/c/%s/all-grades", courseId)));

        verify(courseService, times(1)).saveCourse(
                argThat(i -> i.getId().equals(courseId))
        );
    }

    @Test
    void ShouldNotAddGradeColumn() throws Exception {
        /* participant hasn't got 'all-grade-changer' role */
        course.addParticipant(participant);
        when(courseService.getCourseById(anyString())).thenReturn(course);


        this.mockMvc.perform(post("/c/{course_id}/all-grades/add-grade-column", courseId).session(session)
                        // .param("studentUsernames", ...)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));

        verify(courseService, times(0)).saveCourse(
                any()
        );
    }
}
