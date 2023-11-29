package com.gradeLinker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gradeLinker.dto.storage.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UsersRepository extends JsonFilesRepo<UserDTO> implements Repository<UserDTO> {

    public UsersRepository() {
        super("src/main/resources/data/users/", new TypeReference<UserDTO>() {});
    }
}
