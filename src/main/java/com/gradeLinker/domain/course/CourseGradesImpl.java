package com.gradeLinker.domain.course;

import java.util.*;

/* Making simple List implementation since we need grade-table only for displaying it,
   and adding a 'date' column */
public class CourseGradesImpl implements CourseGrades {
    private List<String> studentUsernames;
    private List<GradeInfo> gradeInfoList;
    /* grades matrix shape: len(gradeInfoList) × len(studentUsernames) */
    private List<List<Double>> grades = new ArrayList<>();

    public CourseGradesImpl(List<String> studentUsernames, List<GradeInfo> gradeInfoList, List<List<Double>> grades) {
        this.studentUsernames = studentUsernames;
        this.gradeInfoList = gradeInfoList;
        if (grades != null) { this.grades = grades; }
    }


    @Override
    public List<GradeInfo> getGradeInfoList() {
        return gradeInfoList;
    }
    @Override
    public List<String> getStudentUsernames() {
        return studentUsernames;
    }
    @Override
    public List<Double> getGradesByUsername(String username) {
        int studentId = studentUsernames.indexOf(username);
        if (studentId == -1) { return null; }

        List<Double> studentGrades = new ArrayList<>();
        for (List<Double> gradeList: grades) {
            studentGrades.add(gradeList.get(studentId));
        }
        return studentGrades;
    }
    @Override
    public List<Double> getGradesByGradeInfoId(int i) {
        return grades.get(i);
    }


    @Override
    public void sortGradesByDate() {
        for (int i = 0; i < gradeInfoList.size(); ++i) {
            gradeInfoList.get(i).setId(i);
        }

        Collections.sort(gradeInfoList);

        List<List<Double>> sortedGrades =  new ArrayList<>();
        for (GradeInfo gradeInfo : gradeInfoList) {
            sortedGrades.add(grades.get(gradeInfo.getId()));
        }

        grades = sortedGrades;
    }
    @Override
    public void add(GradeInfo gradeInfo, List<Double> gradeValues) {
        gradeInfoList.add(gradeInfo);
        grades.add(gradeValues);
    }
    @Override
    public void addStudent(Student student) {
        studentUsernames.add(student.getUsername());
        List<Double> studentGrades = student.getStudentGrades();

        /* i - grade info id */
        for (int i = 0; i < gradeInfoList.size(); ++i) {
            grades.get(i).add(studentGrades.get(i));
        }
    }
}
