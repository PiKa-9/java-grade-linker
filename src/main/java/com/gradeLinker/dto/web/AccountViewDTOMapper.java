package com.gradeLinker.dto.web;

import com.gradeLinker.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class AccountViewDTOMapper {
    public AccountViewDTO toDTO(User user) {
        return new AccountViewDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
