package com.gradeLinker.service;


import com.gradeLinker.domain.course.Course;
import com.gradeLinker.dto.storage.CourseDTO;
import com.gradeLinker.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @MockBean
    private CourseRepository courseRepo;

    @BeforeEach
    void setUp() {
        CourseDTO courseDTO1 = new CourseDTO(
                "course-id-1T",
                null,
                new HashSet<>(),
                new HashMap<>()
        );
        CourseDTO courseDTO2 = new CourseDTO(
                "course-id-2T",
                null,
                new HashSet<>(),
                new HashMap<>()
        );

        when(courseRepo.getById("course-id-1T")).thenReturn(courseDTO1);
        when(courseRepo.getById("course-id-2T")).thenReturn(courseDTO2);
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
}
