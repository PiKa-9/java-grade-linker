package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.UserFactory;
import com.gradeLinker.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {
    private final UserFactory userFactory;

    @Autowired
    public UserDTOMapper(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getPasswordHash(),
                user.getRoles(),
                user.getFirstName(),
                user.getLastName(),
                user.getCourseIds()
        );
    }

    public User fromDTO(UserDTO dto) {
        if (dto == null) { return null; }
        return userFactory.createUser(
                dto.getUsername(),
                dto.getPasswordHash(),
                dto.getRoles(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getCourseIds()
        );
    }
}
