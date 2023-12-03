package com.gradeLinker.dto;

import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.GradeInfo;
import com.gradeLinker.dto.web.GradeTableViewDTO;
import com.gradeLinker.dto.web.GradeTableViewDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GradeTableViewDTOMapperTest {
    @Autowired
    private GradeFactory gradeFactory;
    @Autowired
    private GradeTableViewDTOMapper gradeTableViewDTOMapper;

    private Course course;
    @BeforeEach
    void setUp() {
        Map<String, CourseParticipant> participants = new HashMap<>();
        participants.put("user1T", new CourseParticipant(
                "user1T",
                "full-name-1T",
                new HashSet<>(Arrays.asList("has-grades"))
        ));
        participants.put("user2T", new CourseParticipant(
                "user2T",
                "full-name-2T",
                new HashSet<>(Arrays.asList("has-grades"))
        ));

        CourseGrades courseGrades = gradeFactory.createCourseGrades(
                Arrays.asList("user1T", "user2T")
        );
        courseGrades.add(
                new GradeInfo("ordinary", "02-01-2024"),
                Arrays.asList(null, 75.)
        );
        courseGrades.add(
                new GradeInfo("ordinary", "01-01-2024"),
                Arrays.asList(100., 25.)
        );
        courseGrades.add(
                new GradeInfo("ordinary", "03-01-2024"),
                Arrays.asList(73., 90.5)
        );
        /* Expected grade table:
                  01-01-2024  02-01-2024 03-01-2024
           user1      100.       null        73.
           user2      25.        75.         90.
        */

        course = new Course(
                "course-idT",
                "course-titleT",
                participants,
                courseGrades
        );
    }

    @Test
    void ShouldGetGradeTableDTOFromCourse() {
        GradeTableViewDTO dto = gradeTableViewDTOMapper.toDTO(course);

        assertThat(dto.getStudentUsernames())
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList("user1T", "user2T"));

        assertThat(dto.getDates())
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList("01-01-2024", "02-01-2024", "03-01-2024"));

        assertThat(dto.getGrades())
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(
                        Arrays.asList("100.0", null, "73.0"),
                        Arrays.asList("25.0", "75.0", "90.5")
                ));
    }

    @Test
    void ShouldGetSingleStudentGradeTableDTOFromCourse() {
        GradeTableViewDTO dto = gradeTableViewDTOMapper.toDTO(course, "user1T");

        assertThat(dto.getStudentUsernames())
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList("user1T"));

        assertThat(dto.getDates())
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList("01-01-2024", "02-01-2024", "03-01-2024"));

        assertThat(dto.getGrades())
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(
                        Arrays.asList("100.0", null, "73.0")
                ));
    }
}
