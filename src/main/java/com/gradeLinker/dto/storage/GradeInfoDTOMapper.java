package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.course.GradeInfo;
import org.springframework.stereotype.Component;

@Component
public class GradeInfoDTOMapper {
    public GradeInfoDTO toDTO(GradeInfo gradeInfo) {
        return new GradeInfoDTO(
                gradeInfo.getCategory(),
                gradeInfo.getDate()
        );
    }

    public GradeInfo fromDTO(GradeInfoDTO dto) {
        return new GradeInfo(
                dto.getCategory(),
                dto.getDate()
        );
    }
}
