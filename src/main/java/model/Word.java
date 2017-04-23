package ru.s4nchez.translater.model;

public class Word {

    private String mSourceWord;
    private String mTranslatedWord;
    private String mLangCodes;
    private int mId;
    private boolean mIsFavorite;


    public Word(String sourceWord, String translatedWord,
                String langCodes, int id) {
        mSourceWord = sourceWord.trim();
        mTranslatedWord = translatedWord.trim();
        mLangCodes = langCodes.trim();
        mId = id;
    }

    public Word(String sourceWord, String translatedWord,
                String langCodes, int id, boolean isFavorite) {
        mSourceWord = sourceWord;
        mTranslatedWord = translatedWord;
        mLangCodes = langCodes;
        mId = id;
        mIsFavorite = isFavorite;
    }

    public String getSourceWord() {
        return mSourceWord;
    }

    public String getTranslatedWord() {
        return mTranslatedWord;
    }

    public String getLangCodes() {
        return mLangCodes;
    }

    public int getId() {
        return mId;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    public boolean isEmpty() {
        return mSourceWord.trim().equals("") ||
                mTranslatedWord.trim().equals("");
    }
}