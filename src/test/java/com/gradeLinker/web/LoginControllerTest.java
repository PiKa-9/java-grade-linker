package com.gradeLinker.web;

import com.gradeLinker.domain.UserFactory;
import com.gradeLinker.domain.user.LoginUser;
import com.gradeLinker.service.LoginService;
import com.gradeLinker.service.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    private PasswordHasher hasher;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserFactory userFactory;

    @MockBean
    private LoginService loginService;

    private LoginUser user;
    private String usernameT;
    private String passwordT;
    @BeforeEach
    void setUp() {
        usernameT = "usernameT";
        passwordT = "passwordT";
        user = userFactory.createLoginUser(
                usernameT,
                hasher.hash(passwordT)
        );

        when(loginService.login(usernameT, passwordT)).thenReturn(user);
        when(loginService.login(not(eq(usernameT)), anyString())).thenReturn(null);
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
                .andExpect(model().attribute("invalidCredentials", "Invalid Credentials"));
    }

}
