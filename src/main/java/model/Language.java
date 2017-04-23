package ru.s4nchez.translater.model;

public class Language {

    private String mCode;
    private String mTitle;


    public Language(String code, String title) {
        mCode = code;
        mTitle = title;
    }

    public String getCode() {
        return mCode;
    }

    public String getTitle() {
        return mTitle;
    }
}