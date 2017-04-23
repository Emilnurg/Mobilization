package ru.s4nchez.translater.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.s4nchez.translater.database.DataBase;
import ru.s4nchez.translater.database.TableWord;

public class Words {

    private static Words sWords;

    private ArrayList<Word> mWords;
    private SQLiteDatabase mDatabase;


    private Words(Context context) {
        mDatabase = new DataBase(context.getApplicationContext())
                .getWritableDatabase();
    }

    public static Words get(Context context) {
        if (sWords == null) {
            sWords = new Words(context);
        }
        return sWords;
    }

    public ArrayList<Word> getList() {
        if (mWords == null) {
            mWords = new ArrayList<>();
            updateList();
        }
        return mWords;
    }

    public void updateList() {
        if (mWords == null) {
            mWords = new ArrayList<>();
        } else {
            mWords.clear();
        }
        Cursor cursor = null;

        try {
            cursor = mDatabase.query(TableWord.NAME, null,
                    null,
                    null,
                    null,
                    null,
                    "_id DESC");

            String sourceWord;
            String translatedWord;
            String langCodes;
            boolean isFavorite;
            int id;

            while (cursor.moveToNext()) {
                sourceWord = cursor.getString(cursor.getColumnIndex(TableWord.SOURCE_WORD));
                translatedWord = cursor.getString(cursor.getColumnIndex(TableWord.TRANSLATED_WORD));
                langCodes = cursor.getString(cursor.getColumnIndex(TableWord.LANG_CODES));
                id = cursor.getInt(cursor.getColumnIndex("_id"));
                isFavorite = cursor.getInt(cursor.getColumnIndex(TableWord.FAVORITE)) == 1;
                mWords.add(new Word(sourceWord, translatedWord, langCodes, id, isFavorite));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ArrayList<Word> getFavoriteList() {
        if (mWords == null) {
            updateList();
        }
        ArrayList<Word> favoriteList = new ArrayList<>();

        for (Word word : mWords) {
            if (word.isFavorite()) {
                favoriteList.add(word);
            }
        }

        return favoriteList;
    }

    public void saveWord(Word word) {
        if (contains(word)) {
            return;
        }
        if (word.isEmpty()) {
            return;
        }
        ContentValues cv = getContentValues(word);
        mDatabase.insert(TableWord.NAME, null, cv);
    }

    public void updateWord(Word word) {
        ContentValues cv = getContentValues(word);
        mDatabase.update(TableWord.NAME, cv,  "_id == ?",
                new String[] { word.getId() + "" });
    }

    public void deleteWord(Word word) {
        mDatabase.delete(TableWord.NAME, "_id == ?",
                new String[] { word.getId() + "" });
    }

    private ContentValues getContentValues(Word word) {
        ContentValues cv = new ContentValues();

        cv.put(TableWord.SOURCE_WORD, word.getSourceWord());
        cv.put(TableWord.TRANSLATED_WORD, word.getTranslatedWord());
        cv.put(TableWord.LANG_CODES, word.getLangCodes());
        cv.put(TableWord.FAVORITE, word.isFavorite() ? 1 : 0);

        return cv;
    }

    private boolean contains(Word word) {
        if (mWords == null) {
            mWords = new ArrayList<>();
        } else {
            mWords.clear();
        }
        Cursor cursor = null;

        try {
            cursor = mDatabase.query(TableWord.NAME,
                    null,
                    TableWord.SOURCE_WORD + " = ? AND " + TableWord.LANG_CODES + " = ?",
                    new String[] { word.getSourceWord(), word.getLangCodes() },
                    null,
                    null,
                    "_id DESC");
            return  !(cursor.getCount() == 0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
