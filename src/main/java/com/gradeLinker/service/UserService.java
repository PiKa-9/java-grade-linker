package com.gradeLinker.service;

import com.gradeLinker.domain.user.User;
import com.gradeLinker.dto.storage.UserDTOMapper;
import com.gradeLinker.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UsersRepository usersRepo;
    private final UserDTOMapper userDTOMapper;

    @Autowired
    public UserService(UsersRepository usersRepo, UserDTOMapper userDTOMapper) {
        this.usersRepo = usersRepo;
        this.userDTOMapper = userDTOMapper;
    }

    public User getUserByUsername(String username) {
        return userDTOMapper.fromDTO(usersRepo.getById(username));
    }

    public void saveUser(User user) {
        usersRepo.save(user.getUsername(), userDTOMapper.toDTO(user));
    }
}
