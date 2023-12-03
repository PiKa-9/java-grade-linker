package com.gradeLinker.web;


import com.gradeLinker.domain.CourseFactory;
import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.user.User;
import com.gradeLinker.service.CourseService;
import com.gradeLinker.service.PasswordHasher;
import com.gradeLinker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private PasswordHasher hasher;
    @Autowired
    private GradeFactory gradeFactory;
    @Autowired
    private CourseFactory courseFactory;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private CourseService courseService;

    private MockHttpSession session;

    private User user;
    private String usernameT;
    private String passwordT;


    @BeforeEach
    void setUp() {
        usernameT = "usernameT";
        passwordT = "passwordT";
        user = new User(
                usernameT,
                hasher.hash(passwordT),
                new HashSet<>(),
                null, null,
                new HashSet<>()
        );

        Course course = courseFactory.createCourse(
                "course-idT",
                "course-titleT",
                new HashMap<>(),
                gradeFactory.createCourseGrades(new ArrayList<>())
        );

        when(userService.getUserByUsername(usernameT)).thenReturn(user);
        when(courseService.getCourseById("course-idT")).thenReturn(course);
        when(courseService.getCoursesByIds(any())).thenReturn(null);


        session = new MockHttpSession();
        session.setAttribute("username", usernameT);
    }

    @Test
    void ShouldDisplayHomepage() throws Exception {
        user.addRoles("join_course", "create_course");
        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        this.mockMvc.perform(get("/h").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/homepage.html"))
                .andExpect(model().attributeExists("account", "courseTab"))
                .andExpect(model().attributeExists("join_course", "create_course"));

        verify(courseService, times(1)).getCoursesByIds(user.getCourseIds());
    }
    @Test
    void ShouldNotDisplayHomepageWithoutUsername() throws Exception {
        session.setAttribute("username", null);

        this.mockMvc.perform(get("/h").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
    @Test
    void ShouldNotDisplayHomepageWithoutUser() throws Exception {
        when(userService.getUserByUsername(usernameT)).thenReturn(null);

        this.mockMvc.perform(get("/h").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }


    @Test
    void ShouldBeAbleToJoinCourse() throws Exception {
        user.addRoles("join_course");
        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        this.mockMvc.perform(get("/join-course").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/join_course.html"));
    }
    @Test
    void ShouldNotBeAbleToJoinCourse() throws Exception {
        /* User doesn't have 'join_course' role */
        this.mockMvc.perform(get("/join-course").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));
    }
    @Test
    void ShouldJoinCourse() throws Exception {
        user.addRoles("join_course");
        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        this.mockMvc.perform(post("/join-course").session(session)
                        .param("courseId", "course-idT")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/c/course-idT"));

        verify(courseService, times(1)).saveCourse(
                argThat(i -> i.getId().equals("course-idT"))
        );
        verify(userService, times(1)).saveUser(any());
    }
    @Test
    void ShouldNotJoinCourse() throws Exception {
        /* User doesn't have 'join_course' role */
        this.mockMvc.perform(post("/join-course").session(session)
                        .param("courseId", "course-idT")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));
    }
    @Test
    void ShouldNotJoinCourseIfInvalidCourseId() throws Exception {
        user.addRoles("join_course");
        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        this.mockMvc.perform(post("/join-course").session(session)
                        .param("courseId", "course-id-wrongT")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/join_course.html"));
    }



    @Test
    void ShouldBeAbleToCreateCourse() throws Exception {
        user.addRoles("create_course");
        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        this.mockMvc.perform(get("/create-course").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/create_course.html"));
    }
    @Test
    void ShouldNotBeAbleToCreateCourse() throws Exception {
        /* User doesn't have 'create_course' role */
        this.mockMvc.perform(get("/create-course").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));
    }
    @Test
    void ShouldCreateCourse() throws Exception {
        user.addRoles("create_course");
        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        this.mockMvc.perform(post("/create-course").session(session)
                        .param("courseTitle", "course-titleT")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/c/*"));

        verify(courseService, times(1)).createCourse(
                eq("course-titleT"),
                any()
        );
        verify(userService, times(1)).saveUser(any());
    }
    @Test
    void ShouldNotCreateCourse() throws Exception {
        /* User doesn't have 'create_course' role */
        this.mockMvc.perform(post("/create-course").session(session)
                        .param("courseTitle", "course-titleT")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));
    }
}