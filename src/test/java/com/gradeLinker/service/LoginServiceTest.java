package com.gradeLinker.service;


import com.gradeLinker.domain.user.LoginUser;
import com.gradeLinker.dto.storage.UserDTO;
import com.gradeLinker.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LoginServiceTest {
    @Autowired
    private PasswordHasher hasher;
    @Autowired
    private LoginService loginService;

    @MockBean
    private UsersRepository usersRepo;

    private String usernameT;
    private String passwordT;
    @BeforeEach
    void setUp() {
        usernameT = "usernameT";
        passwordT = "passwordT";
        UserDTO userDTO = new UserDTO(
                usernameT,
                hasher.hash(passwordT),
                null, null, null, null
        );

        when(usersRepo.getById(usernameT)).thenReturn(userDTO);
        when(usersRepo.getById(not(eq(usernameT)))).thenReturn(null);
    }

    @Test
    void ShouldLogin() {
        LoginUser user = loginService.login(usernameT, passwordT);

        assertNotNull(user);
        assertEquals(usernameT, user.getUsername());
        assertEquals(hasher.hash(passwordT), user.getPasswordHash());
    }

    @Test
    void ShouldNotLogin() {
        LoginUser user = loginService.login(usernameT, passwordT + "-wrong");

        assertNull(user);
    }
}
