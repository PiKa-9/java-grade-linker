package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.CourseGrades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseGradesDTOMapper {
    private final GradeFactory gradeFactory;
    private final GradeInfoDTOMapper gradeInfoDTOMapper;

    @Autowired
    public CourseGradesDTOMapper(GradeFactory gradeFactory, GradeInfoDTOMapper gradeInfoDTOMapper) {
        this.gradeFactory = gradeFactory;
        this.gradeInfoDTOMapper = gradeInfoDTOMapper;
    }

    public CourseGradesDTO toDTO(CourseGrades courseGrades) {
        List<GradeInfoDTO> gradeInfoDTOList = new ArrayList<>();
        List<List<Double>> grades = new ArrayList<>();

        /* i - grade info id */
        for (int i = 0; i < courseGrades.getGradeInfoList().size(); ++i) {
            gradeInfoDTOList.add(gradeInfoDTOMapper.toDTO(courseGrades.getGradeInfoList().get(i)));
            grades.add(
                    courseGrades.getGradesByGradeInfoId(i)
            );
        }

        return new CourseGradesDTO(
                courseGrades.getStudentUsernames(),
                gradeInfoDTOList,
                grades
        );
    }

    public CourseGrades fromDTO(CourseGradesDTO dto) {
        if (dto == null) { return null; }
        CourseGrades courseGrades = gradeFactory.createCourseGrades(
                dto.getStudentUsernames()
        );

        /* i - grade info id */
        for (int i = 0; i < dto.getGradeInfoList().size(); ++i) {
            courseGrades.add(
                    gradeInfoDTOMapper.fromDTO(dto.getGradeInfoList().get(i)),
                    dto.getGrades().get(i)
            );
        }
        return courseGrades;
    }
}
