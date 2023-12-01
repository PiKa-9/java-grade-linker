package com.gradeLinker.web;


import com.gradeLinker.repository.UsersRepository;
import com.gradeLinker.service.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {
    @Autowired
    private PasswordHasher hasher;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersRepository usersRepo;

    private String usernameT;
    private String passwordT;
    @BeforeEach
    void setUp() {
        usernameT = "usernameT";
        passwordT = "passwordT";
    }

    @Test
    void shouldDisplayRegistrationForm() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/register.html"));
    }

    @Test
    void shouldRegisterStudent() throws Exception {
        this.mockMvc.perform(post("/register")
                        .param("username", usernameT)
                        .param("type", "student")
                        .param("password", passwordT)
                        .param("confirmPassword", passwordT)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/h"));

        verify(usersRepo, times(1)).save(
                eq(usernameT),
                argThat(i -> i.getRoles().contains("join_course")
                        && !(i.getRoles().contains("create_course")))
        );
    }
    @Test
    void shouldRegisterTeacher() throws Exception {
        this.mockMvc.perform(post("/register")
                        .param("username", usernameT)
                        .param("type", "teacher")
                        .param("password", passwordT)
                        .param("confirmPassword", passwordT)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/h"));

        verify(usersRepo, times(1)).save(
                eq(usernameT),
                argThat(i -> i.getRoles().contains("join_course")
                        && i.getRoles().contains("create_course"))
        );
    }

    @Test
    void shouldNotRegisterIfPasswordNotConfirmed() throws Exception {
        this.mockMvc.perform(post("/register")
                        .param("username", usernameT)
                        .param("type", "student")
                        .param("password", passwordT)
                        .param("confirmPassword", passwordT + "-wrong")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/register.html"))
                .andExpect(model().attribute("invalid", "Passwords aren't equal"));

        verify(usersRepo, times(0)).save(eq(usernameT), any());
    }

    @Test
    void shouldNotRegisterIfUsernameAlreadyExists() throws Exception {
        when(usersRepo.existsById(eq("username_exists"))).thenReturn(true);

        this.mockMvc.perform(post("/register")
                        .param("username", "username_exists")
                        .param("type", "student")
                        .param("password", passwordT)
                        .param("confirmPassword", passwordT)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("pages/register.html"))
                .andExpect(model().attribute("invalid", "Such username already exists"));

        verify(usersRepo, times(0)).save(eq(usernameT), any());
    }
}