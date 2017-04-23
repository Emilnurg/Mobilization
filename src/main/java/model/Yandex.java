package ru.s4nchez.translater.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class Yandex {

    private static final String KEY = "trnsl.1.1.20170404T015554Z.47ccadbb28255666.bb8edc4f24afcde68b5653be3cdbe3c3219c077d";


    public static Request getTranslateRequest(String text, Language from, Language to) {
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse("https://translate.yandex.net/api/v1.5/tr.json/translate").newBuilder();
        urlBuilder.addQueryParameter("key", KEY);
        urlBuilder.addQueryParameter("lang", getLangsParam(from, to));
        urlBuilder.addQueryParameter("text", text);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        return request;
    }

    public static String getLangsParam(Language from, Language to) {
        return from.getCode() + "-" + to.getCode();
    }

    public static Request getListOfTranslations() {
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse("https://translate.yandex.net/api/v1.5/tr.json/getLangs").newBuilder();
        urlBuilder.addQueryParameter("key", KEY);
        urlBuilder.addQueryParameter("ui", "ru");
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        return request;
    }

    public static String parseTranslate(String json) {
        String res = "";

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonWord = jsonObject.getJSONArray("text");
            res = jsonWord.get(0).toString();
        } catch (JSONException e) {
            return "";
        }

        return res;
    }

    public static ArrayList<Language> getLanguages(String json) {
        ArrayList<Language> langsArray = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject langsJson = jsonObject.getJSONObject("langs");
            Iterator<String> temp = langsJson.keys();
            StringBuilder stringBuilder = new StringBuilder();
            while (temp.hasNext()) {
                String code = temp.next();
                String title = langsJson.get(code).toString();
                langsArray.add(new Language(code, title));
            }
        } catch (JSONException e) {
            Log.d("myLog", e.toString());
            e.printStackTrace();
        }

        return langsArray;
    }

    public static String[] getLangsTitles(List<Language> langs) {
        if (langs == null) {
            return null;
        }

        String[] titles = new String[langs.size()];

        for (int i = 0; i < langs.size(); i++) {
            titles[i] = langs.get(i).getTitle();
        }

        return titles;
    }
}