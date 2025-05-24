package com.example.app.model.constant;

public enum TitleGenderCode {
    Male("Mr."),
    MarriedFemale("Mrs."),
    SingleFemale("Ms."),
    Other("");

    private final String titles;

    TitleGenderCode(String titles) {
        this.titles = titles;
    }

    public String getTitles() {
        return titles;
    }
}
