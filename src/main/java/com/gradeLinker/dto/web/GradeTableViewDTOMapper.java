package com.gradeLinker.dto.web;

import com.gradeLinker.domain.course.*;
import com.gradeLinker.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GradeTableViewDTOMapper {
    private final CourseService courseService;

    @Autowired
    public GradeTableViewDTOMapper(CourseService courseService) {
        this.courseService = courseService;
    }

    private List<String> toString(List<Double> list) {
        List<String> result = new ArrayList<>();
        for (Double ele: list) {
            if (ele == null) {
                result.add(null);
            } else {
                result.add(ele.toString());
            }
        }
        return result;
    }
    public GradeTableViewDTO toDTO(Course course) {
        CourseGrades courseGrades = course.getCourseGrades();
        /* Sort grades by date */
        courseGrades.sortGradesByDate();

        /* Sort students by their fullNames */
        List<Student> students = courseService.getStudentsSortedByFullName(course);

        List<String> studentUsernames = new ArrayList<>();
        List<String> studentFullNames = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<String> gradeCategories = new ArrayList<>();
        List<List<String>> grades = new ArrayList<>();

        for (Student student: students) {
            studentUsernames.add(student.getUsername());
            studentFullNames.add(student.getFullName());

            /* Be careful, the grade order should be the same sa in grade table */
            grades.add(toString(student.getStudentGrades()));
        }
        for (GradeInfo gradeInfo: courseGrades.getGradeInfoList()) {
            dates.add(gradeInfo.getDate());
            gradeCategories.add(gradeInfo.getCategory());
        }

        return new GradeTableViewDTO(
                studentUsernames,
                studentFullNames,
                dates,
                gradeCategories,
                grades
        );
    }
}
