package com.gradeLinker.dto.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GradeDTO {
    private String value;
    private String valueType;
    private String category;

    public GradeDTO(@JsonProperty("value") String value, @JsonProperty("valueType") String valueType, @JsonProperty("category") String category) {
        this.value = value;
        this.valueType = valueType;
        this.category = category;
    }

    public String getValue() {
        return value;
    }
    public String getValueType() {
        return valueType;
    }
    public String getCategory() {
        return category;
    }
}
