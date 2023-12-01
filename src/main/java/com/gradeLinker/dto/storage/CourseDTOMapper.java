package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class CourseDTOMapper {
    private final CourseParticipantDTOMapper courseParticipantDTOMapper;
    private final GradeSetDTOMapper gradeSetDTOMapper;

    @Autowired
    public CourseDTOMapper(CourseParticipantDTOMapper courseParticipantDTOMapper, GradeSetDTOMapper gradeSetDTOMapper) {
        this.courseParticipantDTOMapper = courseParticipantDTOMapper;
        this.gradeSetDTOMapper = gradeSetDTOMapper;
    }

    public CourseDTO toDTO(Course course) {
        Set<CourseParticipantDTO> participantsDTO = new HashSet<>();
        for (CourseParticipant participant: course.getParticipants().values()) {
            participantsDTO.add(courseParticipantDTOMapper.toDTO(participant));
        }

        Map<String, GradeSetDTO> gradesMap = new HashMap<>();
        for (String username: course.getGrades().keySet()) {
            gradesMap.put(
                    username,
                    gradeSetDTOMapper.toDTO(course.getGrades().get(username))
            );
        }

        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                participantsDTO,
                gradesMap
        );
    }

    public Course fromDTO(CourseDTO dto) {
        Course course = new Course(
                dto.getId(),
                dto.getTitle(),
                new HashMap<>(),
                new HashMap<>()
        );

        Map<String, CourseParticipant> participants = new HashMap<>();
        for (CourseParticipantDTO participantDTO: dto.getParticipants()) {
            if (course.getGrades().get(participantDTO.getUsername()) == null) {
                /* participant without grades */
                CourseParticipant participant = new CourseParticipant(
                        participantDTO.getUsername(),
                        participantDTO.getRoles()
                );

                course.addParticipant(participant);
            } else {
                /* participant with grades */
                Student participant = new Student(
                        participantDTO.getUsername(),
                        participantDTO.getRoles(),
                        course.getGrades().get(participantDTO.getUsername())
                );

                course.addParticipant(participant);
            }
        }

        return course;
    }
}
