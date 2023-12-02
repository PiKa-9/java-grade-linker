package com.gradeLinker.web;

import com.gradeLinker.domain.user.LoginUser;
import com.gradeLinker.service.LoginService;
import com.gradeLinker.service.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
    @Autowired
    private PasswordHasher hasher;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    private MockHttpSession session;

    private LoginUser user;
    private String usernameT;
    private String passwordT;
    @BeforeEach
    void setUp() {
        usernameT = "usernameT";
        passwordT = "passwordT";
        user = new LoginUser(
                usernameT,
                hasher.hash(passwordT)
        );

        when(loginService.login(usernameT, passwordT)).thenReturn(user);
        when(loginService.login(not(eq(usernameT)), anyString())).thenReturn(null);

        session = new MockHttpSession();
        session.setAttribute("username", usernameT);
    }

    @Test
    void shouldDisplayLoginForm() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/login.html"));
    }

    @Test
    void shouldLogIn() throws Exception {
        this.mockMvc.perform(post("/login")
                        .param("username", usernameT)
                        .param("password", passwordT)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/h"));
    }

    @Test
    void shouldNotLogIn() throws Exception {
        this.mockMvc.perform(post("/login")
                        .param("username", usernameT + "-wrong")
                        .param("password", passwordT)
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("invalid", "Invalid Credentials"));
    }

    @Test
    void ShouldLogout() throws Exception {
        assertNotNull(session.getAttribute("username"));

        this.mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        assertNull(session.getAttribute("username"));
    }
}
