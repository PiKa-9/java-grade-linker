package com.gradeLinker.service;


import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.GradeInfo;
import com.gradeLinker.domain.user.User;
import com.gradeLinker.dto.storage.CourseDTO;
import com.gradeLinker.dto.storage.CourseGradesDTO;
import com.gradeLinker.dto.storage.CourseParticipantDTO;
import com.gradeLinker.dto.storage.GradeInfoDTO;
import com.gradeLinker.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseServiceTest {
    @Autowired
    private GradeFactory gradeFactory;
    @Autowired
    private CourseService courseService;

    @MockBean
    private UserService userService;

    @MockBean
    private CourseRepository courseRepo;

    @BeforeEach
    void setUp() {
        Set<CourseParticipantDTO> participants = new HashSet<>();
        participants.add(new CourseParticipantDTO(
                "user1T",
                new HashSet<>(Arrays.asList("all-grade-viewer", "all-grade-changer"))
        ));
        participants.add(new CourseParticipantDTO(
                "user2T",
                new HashSet<>(Arrays.asList("has-grades"))
        ));

        CourseGradesDTO courseGradesDTO = new CourseGradesDTO(
                Arrays.asList("user2T"),
                Arrays.asList(new GradeInfoDTO("ordinary", "01-01-2024")),
                Arrays.asList(
                        Arrays.asList(100.0)
                )
        );

        CourseDTO courseDTO1 = new CourseDTO(
                "course-id-1T",
                "course-title-1T",
                participants,
                courseGradesDTO
        );
        CourseDTO courseDTO2 = new CourseDTO(
                "course-id-2T",
                "course-title-2T",
                participants,
                courseGradesDTO
        );

        when(courseRepo.getById("course-id-1T")).thenReturn(courseDTO1);
        when(courseRepo.getById("course-id-2T")).thenReturn(courseDTO2);

        when(userService.getUserByUsername("user1T")).thenReturn(
                new User(
                        "user1T",
                        0,
                        new HashSet<>(Arrays.asList("all-grade-viewer")),
                        null, null,
                        new HashSet<>(Arrays.asList("course-id-1T", "course-id-2T"))
                )
        );
        when(userService.getUserByUsername("user2T")).thenReturn(
                new User(
                        "user2T",
                        0,
                        new HashSet<>(Arrays.asList("has-grades")),
                        null, null,
                        new HashSet<>(Arrays.asList("course-id-1T", "course-id-2T"))
                )
        );
    }

    @Test
    void ShouldGetCoursesByIds() {
        Set<String> courseIds = new HashSet<>(Arrays.asList(
                "course-id-1T", "course-id-2T"
        ));

        List<Course> courses = courseService.getCoursesByIds(courseIds);
        Set<String> returnedCourseIds = new HashSet<>();
        for (Course course: courses) {
            returnedCourseIds.add(course.getId());
        }

        assertEquals(courseIds, returnedCourseIds);
    }

    @Test
    void ShouldSaveCourse() {
        Course course = new Course(
                "course-id-1T",
                null,
                new HashMap<>(),
                gradeFactory.createCourseGrades(new ArrayList<>())
        );
        courseService.saveCourse(course);

        verify(courseRepo, times(1)).save(eq("course-id-1T"), any());
    }

    @Test
    void ShouldCreateCourse() {
        String returnedId = courseService.createCourse(
                "course-titleT",
                new CourseParticipant(
                        "usernameT",
                        "full-nameT",
                        new HashSet<>(Arrays.asList("all-grade-viewer", "all-grade-changer"))
                )
        );

        assertEquals(6, returnedId.length());
        verify(courseRepo, times(1)).save(anyString(), any());
    }
}
