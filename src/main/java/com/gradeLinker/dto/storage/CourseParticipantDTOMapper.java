package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.course.CourseParticipant;
import org.springframework.stereotype.Component;

@Component
public class CourseParticipantDTOMapper {
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
                dto.getRoles()
        );
    }
}
