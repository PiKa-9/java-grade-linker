package com.gradeLinker.service;

import com.gradeLinker.domain.user.LoginUser;
import com.gradeLinker.dto.storage.UserDTOMapper;
import com.gradeLinker.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private PasswordHasher hasher;
    @Autowired
    private UserDTOMapper userDTOMapper;
    @Autowired
    private UsersRepository usersRepo;

    /* Returns null, if invalid credentials */
    public LoginUser login(String username, String password) {
        LoginUser user = userDTOMapper.fromDTO(usersRepo.getById(username));

        if (user == null) { return null; }
        if (user.getPasswordHash() == hasher.hash(password)) {
            return user;
        } else {
            return null;
        }
    }
}
