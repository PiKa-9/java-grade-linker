package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GradeDTOMapper {
    private final GradeFactory gradeFactory;

    @Autowired
    public GradeDTOMapper(GradeFactory gradeFactory) {
        this.gradeFactory = gradeFactory;
    }

    private String getType(Object value) {
        if (value instanceof Integer) {
            return  "Integer";
        } else if (value instanceof Double) {
            return "Double";
        }
        return "String";
    }
    public GradeDTO toDTO(Grade grade) {
        return new GradeDTO(
                grade.getValue().toString(),
                this.getType(grade.getValue()),
                grade.getGradeCategory()
        );
    }

    public Grade fromDTO(GradeDTO dto) {
        if (dto == null) { return null; }

        if (dto.getValueType().equals("Integer")) {
            return gradeFactory.createIntGrade(
                    Integer.valueOf(dto.getValue()),
                    dto.getCategory()
            );
        } else {
            return null;
        }
    }
}
