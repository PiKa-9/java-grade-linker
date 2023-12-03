package com.gradeLinker.dto;

import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.GradeInfo;
import com.gradeLinker.dto.web.request.GradeTableRequest;
import com.gradeLinker.dto.web.request.GradeTableRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GradeTableRequestMapperTest {
    @Autowired
    private GradeFactory gradeFactory;
    @Autowired
    private GradeTableRequestMapper gradeTableRequestMapper;

    private GradeTableRequest gradeTableRequest;
    private CourseGrades courseGrades;
    @BeforeEach
    void setUp() {
        gradeTableRequest = new GradeTableRequest(
                Arrays.asList("user1T"),
                Arrays.asList("01-01-2024"),
                Arrays.asList("ordinary"),
                Arrays.asList("89")
        ) ;
        /* Expected grade table:
                  01-01-2024
           user1      89.0
        */

        courseGrades = gradeFactory.createCourseGrades(
                Arrays.asList("user1T")
        );
        courseGrades.add(
                new GradeInfo("ordinary", "01-01-2024"),
                Arrays.asList(89.0)
        );
    }

    @Test
    void ShouldTransformGradeTableRequestToCourseGrades() {
        assertThat(gradeTableRequestMapper.fromDTO(gradeTableRequest))
                .usingRecursiveComparison()
                .isEqualTo(courseGrades);

    }
}
