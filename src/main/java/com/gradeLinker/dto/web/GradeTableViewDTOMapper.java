package com.gradeLinker.dto.web;

import com.gradeLinker.domain.course.Course;
import com.gradeLinker.domain.course.CourseGrades;
import com.gradeLinker.domain.course.CourseParticipant;
import com.gradeLinker.domain.course.GradeInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GradeTableViewDTOMapper {
    public GradeTableViewDTO toDTO(Course course) {
        CourseGrades courseGrades = course.getCourseGrades();

        List<String> studentUsernames = new ArrayList<>();
        List<String> studentFullNames = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<String> gradeCategories = new ArrayList<>();
        List<List<String>> grades = new ArrayList<>();

        for (GradeInfo gradeInfo: courseGrades.getGradeInfoList()) {
            dates.add(gradeInfo.getDate());
            gradeCategories.add(gradeInfo.getCategory());
        }

        for (String studentUsername: courseGrades.getStudentUsernames()) {
            CourseParticipant participant = course.getParticipantByUsername(studentUsername);
            studentUsernames.add(participant.getUsername());
            studentFullNames.add(participant.getFullName());

            List<String> studentGrades = new ArrayList<>();
            for (Double grade: courseGrades.getGradesByUsername(participant.getUsername())) {
                studentGrades.add(grade.toString());
            }
            grades.add(studentGrades);
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
