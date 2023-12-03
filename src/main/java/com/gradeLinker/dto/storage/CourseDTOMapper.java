package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.CourseFactory;
import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CourseDTOMapper {
    private final GradeFactory gradeFactory;
    private final CourseFactory courseFactory;
    private final CourseParticipantDTOMapper courseParticipantDTOMapper;
    private final CourseGradesDTOMapper courseGradesDTOMapper;

    @Autowired
    public CourseDTOMapper(GradeFactory gradeFactory, CourseFactory courseFactory, CourseParticipantDTOMapper courseParticipantDTOMapper, CourseGradesDTOMapper courseGradesDTOMapper) {
        this.gradeFactory = gradeFactory;
        this.courseFactory = courseFactory;
        this.courseParticipantDTOMapper = courseParticipantDTOMapper;
        this.courseGradesDTOMapper = courseGradesDTOMapper;
    }

    public CourseDTO toDTO(Course course) {
        Set<CourseParticipantDTO> participantsDTO = new HashSet<>();
        for (CourseParticipant participant: course.getParticipants().values()) {
            participantsDTO.add(courseParticipantDTOMapper.toDTO(participant));
        }

        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                participantsDTO,
                courseGradesDTOMapper.toDTO(course.getCourseGrades())
        );
    }

    public Course fromDTO(CourseDTO dto) {
        if (dto == null) { return null; }

        Map<String, CourseParticipant> participants = new HashMap<>();
        for (CourseParticipantDTO participantDTO: dto.getParticipants()) {
            participants.put(
                    participantDTO.getUsername(),
                    courseParticipantDTOMapper.fromDTO(participantDTO)
            );
        }

        return courseFactory.createCourse(
                dto.getId(),
                dto.getTitle(),
                participants,
                courseGradesDTOMapper.fromDTO(dto.getCourseGrades())
        );
    }
}
