package com.gradeLinker.dto.storage;

import com.gradeLinker.domain.GradeFactory;
import com.gradeLinker.domain.course.Grade;
import com.gradeLinker.domain.course.GradeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GradeSetDTOMapper {
    private final GradeFactory gradeFactory;
    private final GradeDTOMapper gradeDTOMapper;

    @Autowired
    public GradeSetDTOMapper(GradeFactory gradeFactory, GradeDTOMapper gradeDTOMapper) {
        this.gradeFactory = gradeFactory;
        this.gradeDTOMapper = gradeDTOMapper;
    }

    public GradeSetDTO toDTO(GradeSet gradeSet) {
        Set<GradeDTO> gradeDTOSet = new HashSet<>();
        for (Grade grade: gradeSet.getGrades()) {
            gradeDTOSet.add(gradeDTOMapper.toDTO(grade));
        }

        return new GradeSetDTO(
                gradeDTOSet
        );
    }

    public GradeSet fromDTO(GradeSetDTO dto) {
        if (dto == null) { return null; }
        GradeSet gradeSet = gradeFactory.createGradeSet(new HashSet<>());

        for (GradeDTO gradeDTO: dto.getGrades()) {
            gradeSet.addGrades(gradeDTOMapper.fromDTO(gradeDTO));
        }

        return gradeSet;
    }
}
