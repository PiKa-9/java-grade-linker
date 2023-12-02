package com.gradeLinker.domain.course;

import java.util.List;

public interface CourseGrades {
    List<GradeInfo> getGradeInfoList();
    List<String> getStudentUsernames();
    List<Double> getGradesByUsername(String username);
    List<Double> getGradesByGradeInfoId(int i);

    void add(GradeInfo gradeInfo, List<Double> gradeValues);
    void addStudent(Student student);
}
