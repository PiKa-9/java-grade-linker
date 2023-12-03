package com.gradeLinker.dto.web.request;

import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.GradeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GradeTableRequestMapper {
    private final GradeFactory gradeFactory;

    @Autowired
    public GradeTableRequestMapper(GradeFactory gradeFactory) {
        this.gradeFactory = gradeFactory;
    }

    public CourseGrades fromDTO(GradeTableRequest gradeTable) {
        List<String> studentUsernames = gradeTable.getStudentUsernames();
        if (studentUsernames == null) { studentUsernames = new ArrayList<>(); }
        CourseGrades courseGrades = gradeFactory.createCourseGrades(
                studentUsernames
        );

        try {
            int m = gradeTable.getDates().size();
            for (int i = 0; i < m; ++i) {
                GradeInfo gradeInfo = new GradeInfo(
                        gradeTable.getGradeCategories().get(i),
                        gradeTable.getDates().get(i)
                );

                List<Double> grades = new ArrayList<>();
                for (int j = 0; j < studentUsernames.size(); ++j) {
                    String grade = gradeTable.getGrades().get(j*m + i); // be careful with index
                    if (grade == null || grade.equals("")) {
                        grades.add(null);
                    } else {
                        grades.add(Double.valueOf(grade));
                    }
                }

                courseGrades.add(
                        gradeInfo,
                        grades
                );
            }
        } catch (NullPointerException e) {
            // do nothing...
        }

        return courseGrades;
    }
}
