package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseParticipantDTOMapper {
    private final UserService userService;

    @Autowired
    public CourseParticipantDTOMapper(UserService userService) {
        this.userService = userService;
    }

    public CourseParticipantDTO toDTO(CourseParticipant participant) {
        return new CourseParticipantDTO(
                participant.getUsername(),
                participant.getRoles()
        );
    }

    public CourseParticipant fromDTO(CourseParticipantDTO dto) {
        if (dto == null) { return null; }
        return new CourseParticipant(
                dto.getUsername(),
                userService.getUserByUsername(dto.getUsername()).getFullName(),
                dto.getRoles()
        );
    }
}
