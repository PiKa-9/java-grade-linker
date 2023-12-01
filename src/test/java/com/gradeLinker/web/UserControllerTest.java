package com.gradeLinker.web;


import com.gradeLinker.domain.UserFactory;
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

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private PasswordHasher hasher;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserFactory userFactory;

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


        when(userService.getUserByUsername(usernameT)).thenReturn(user);
        when(courseService.getCoursesByIds(any())).thenReturn(null);

        session = new MockHttpSession();
        session.setAttribute("username", usernameT);
    }

    @Test
    void shouldDisplayHomepage() throws Exception {
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
    void shouldNotDisplayHomepageWithoutUsername() throws Exception {
        session.setAttribute("username", null);

        this.mockMvc.perform(get("/h").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
    @Test
    void shouldNotDisplayHomepageWithoutUser() throws Exception {
        when(userService.getUserByUsername(usernameT)).thenReturn(null);

        this.mockMvc.perform(get("/h").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }


    @Test
    void shouldBeAbleToJoinCourse() throws Exception {
        user.addRoles("join_course");
        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        this.mockMvc.perform(get("/join-course").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/join_course.html"));
    }
    @Test
    void shouldNotBeAbleToJoinCourse() throws Exception {
        /* User doesn't have 'join_course' role */
        this.mockMvc.perform(get("/join-course").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));
    }


    @Test
    void shouldBeAbleToCreateCourse() throws Exception {
        user.addRoles("create_course");
        when(userService.getUserByUsername(usernameT)).thenReturn(user);

        this.mockMvc.perform(get("/create-course").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/create_course.html"));
    }
    @Test
    void shouldNotBeAbleToCreateCourse() throws Exception {
        /* User doesn't have 'create_course' role */
        this.mockMvc.perform(get("/create-course").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/error.html"));
    }
}