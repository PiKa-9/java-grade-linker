package com.gradeLinker.dto.web;

import com.gradeLinker.domain.course.Course;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseTabViewDTOMapper {
    public List<CourseLinkViewDTO> toDTO(List<Course> courses) {
        List<CourseLinkViewDTO> courseLinks = new ArrayList<>();
        if (courses == null) { return courseLinks; }
        for (Course course: courses) {
            courseLinks.add(new CourseLinkViewDTO(
                    course.getId(),
                    course.getTitle()
            ));
        }
        return courseLinks;
    }
}
