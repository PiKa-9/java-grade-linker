package com.gradeLinker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gradeLinker.dto.storage.CourseDTO;
import org.springframework.stereotype.Component;


@Component
public class CourseRepository extends JsonFilesRepo<CourseDTO> implements Repository<CourseDTO> {

    public CourseRepository() {
        super("src/main/resources/data/courses/", new TypeReference<CourseDTO>() {});
    }

}
