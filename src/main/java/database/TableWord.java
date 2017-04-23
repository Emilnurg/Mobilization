package ru.s4nchez.translater.database;

public class TableWord {

    public static final String NAME = "history";

    public static final String SOURCE_WORD = "source";
    public static final String TRANSLATED_WORD = "translated";
    public static final String LANG_CODES = "codes";
    public static final String FAVORITE = "favorite";

    public static final String CREATE_TABLE = "create table " + NAME +
            "( _id integer primary key autoincrement, " +
            SOURCE_WORD + ", " +
            TRANSLATED_WORD + ", " +
            FAVORITE + ", " +
            LANG_CODES + ")";
}
