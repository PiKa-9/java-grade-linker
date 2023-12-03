package com.gradeLinker.domain.course;

public class GradeInfo implements Comparable<GradeInfo> {
    private int id; // utility field for sorting the grades
    private String category;
    private String date;

    public GradeInfo(String category, String date) {
        this.category = category;
        this.date = date;
    }

    @Override
    public int compareTo(GradeInfo o) {
        if (date.equals(o.date)) {
            return category.compareTo(o.getCategory());
        }
        return date.compareTo(o.date);
    }

    public int getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }
    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }
}
