package com.gradeLinker.domain.course;

public interface Grade {
    Object getValue();

    /* Possible categories: 'ordinary', 'thematic', ... */
    String getGradeCategory();
}
