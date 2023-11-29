package com.gradeLinker.service;

import com.gradeLinker.domain.user.LoginUser;
import com.gradeLinker.dto.storage.UserDTOMapper;
import com.gradeLinker.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final PasswordHasher hasher;
    private final UserDTOMapper userDTOMapper;
    private final UsersRepository usersRepo;

    @Autowired
    public LoginService(PasswordHasher hasher, UserDTOMapper userDTOMapper, UsersRepository usersRepo) {
        this.hasher = hasher;
        this.userDTOMapper = userDTOMapper;
        this.usersRepo = usersRepo;
    }

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
