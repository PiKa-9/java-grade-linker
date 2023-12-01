package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.course.Grade;
import com.gradeLinker.domain.course.IntGrade;
import org.springframework.stereotype.Component;

@Component
public class GradeDTOMapper {
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
            return new IntGrade(
                    Integer.valueOf(dto.getValue()),
                    dto.getCategory()
            );
        } else {
            return null;
        }
    }
}
