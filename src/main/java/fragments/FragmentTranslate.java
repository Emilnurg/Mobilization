package ru.s4nchez.translater.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.s4nchez.translater.model.Language;
import ru.s4nchez.translater.R;
import ru.s4nchez.translater.Utils;
import ru.s4nchez.translater.model.Word;
import ru.s4nchez.translater.model.Words;
import ru.s4nchez.translater.model.Yandex;

public class FragmentTranslate extends Fragment {

    private final Language DEFAULT_LANGUAGE_FROM = new Language("ru", "Русский");
    private final Language DEFAULT_LANGUAGE_TO = new Language("en", "Английский");

    private EditText mEditText;
    private TextView mTextViewTranslate;
    private TextView mTextViewFrom;
    private TextView mTextViewTo;
    private ImageView mImageViewChangeLang;
    private ImageView mImageViewClearField;
    private ImageView mImageViewCopy;

    private Language mLanguageFrom = DEFAULT_LANGUAGE_FROM;
    private Language mLanguageTo = DEFAULT_LANGUAGE_TO;

    private ArrayList<Language> mLanguages;

    private Word mWordBuf;
    private int mCountOfAsyncStarted = 0;
    private int mCountOfAsyncFinished = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translate, container, false);

        LangsRequest request =
                new LangsRequest(Yandex.getListOfTranslations());
        request.execute();

        initViews(v);
        initKeyboardVisibilityEvent();

        return v;
    }

    private void initViews(View v) {
        mEditText = (EditText) v.findViewById(R.id.editText);
        mTextViewTranslate = (TextView) v.findViewById(R.id.textViewTranslate);
        mTextViewFrom = (TextView) v.findViewById(R.id.textViewFrom);
        mTextViewTo = (TextView) v.findViewById(R.id.textViewTo);
        mImageViewChangeLang = (ImageView) v.findViewById(R.id.imageViewChangeLang);
        mImageViewClearField = (ImageView) v.findViewById(R.id.imageViewClear);
        mImageViewCopy = (ImageView) v.findViewById(R.id.imageViewCopy);

        mTextViewFrom.setVisibility(View.VISIBLE);
        mTextViewTo.setVisibility(View.VISIBLE);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TranslateRequest request =
                        new TranslateRequest(Yandex.getTranslateRequest(s.toString(),
                                mLanguageFrom, mLanguageTo));
                request.execute();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mTextViewFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLangsFromDialog();
            }
        });

        mTextViewTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLangsToDialog();
            }
        });

        mImageViewChangeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Language buf = mLanguageFrom;
                mLanguageFrom = mLanguageTo;
                mLanguageTo = buf;
                setLang();
            }
        });

        mImageViewClearField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });

        mImageViewCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.copyToBuffer(getActivity(), mEditText.getText().toString());
                Toast.makeText(getActivity(), R.string.copyToBuffer,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLang() {
        mTextViewFrom.setText(mLanguageFrom.getTitle());
        mTextViewTo.setText(mLanguageTo.getTitle());
        TranslateRequest request =
                new TranslateRequest(Yandex.getTranslateRequest(mEditText.getText().toString(),
                        mLanguageFrom, mLanguageTo));
        request.execute();
    }

    // Отслеживание закрытия экранной клавиатуры
    // Необходимо для записи слов в историю
    private void initKeyboardVisibilityEvent() {
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (mEditText.getText().toString().trim().equals("")) {
                            return;
                        }
                        if (isOpen) {
                            return;
                        }

                        if (mCountOfAsyncStarted == mCountOfAsyncFinished) {
                            saveWord();
                            mWordBuf = null;
                        } else {
                            mWordBuf = new Word(mEditText.getText().toString(), "",
                                    Yandex.getLangsParam(mLanguageFrom, mLanguageTo), 0);
                        }
                    }
                });
    }

    private void saveWord() {
        String sourceWord = mEditText.getText().toString();
        String translatedWord = mTextViewTranslate.getText().toString();
        String langCodes = Yandex.getLangsParam(mLanguageFrom, mLanguageTo);
        Words.get(getActivity()).saveWord(new Word(sourceWord, translatedWord, langCodes, 0));
    }

    private void showLangsFromDialog() {
        if (mLanguages == null || mLanguages.isEmpty()) {
            Toast.makeText(getContext(), R.string.langsNotDownloads, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = buildDialog();
        builder.setTitle(R.string.chooseYourLang);
        builder.setItems(Yandex.getLangsTitles(mLanguages), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                mLanguageFrom = mLanguages.get(item);
                mTextViewFrom.setText(mLanguageFrom.getTitle());
                TranslateRequest request =
                        new TranslateRequest(Yandex.getTranslateRequest(mEditText.getText().toString(),
                                mLanguageFrom, mLanguageTo));
                request.execute();
            }
        });
        builder.show();
    }

    private void showLangsToDialog() {
        if (mLanguages == null) {
            Toast.makeText(getContext(), R.string.langsNotDownloads, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = buildDialog();
        builder.setTitle(R.string.chooseLangForTranslate);
        builder.setItems(Yandex.getLangsTitles(mLanguages), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                mLanguageTo = mLanguages.get(item);
                mTextViewTo.setText(mLanguageTo.getTitle());
                TranslateRequest request =
                        new TranslateRequest(Yandex.getTranslateRequest(mEditText.getText().toString(),
                                mLanguageFrom, mLanguageTo));
                request.execute();
            }
        });
        builder.show();
    }

    private AlertDialog.Builder buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.create();
        return builder;
    }

    class TranslateRequest extends AsyncTask<Void, Void, String> {

        private static final String ERROR = "error1";

        private Request mRequest;
        private OkHttpClient mClient;


        public TranslateRequest(Request request) {
            mCountOfAsyncStarted++;
            mRequest = request;
            mClient = new OkHttpClient();
        }

        String run() throws IOException {
            Response response = mClient.newCall(mRequest).execute();
            return response.body().string();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                result = run();
            } catch (IOException e) {
                return ERROR;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals(ERROR)) {
                Toast.makeText(getActivity(), R.string.errorMessage,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mCountOfAsyncFinished++;
            mTextViewTranslate.setText(Yandex.parseTranslate(result));
            if (mWordBuf == null) {
                return;
            }
            if (mCountOfAsyncFinished == mCountOfAsyncStarted) {
                if (mWordBuf.getSourceWord().equals(mEditText.getText().toString().trim())) {
                    saveWord();
                }
            }
        }
    }

    class LangsRequest extends AsyncTask<Void, Void, String> {

        private static final String ERROR = "error1";

        private Request mRequest;
        private OkHttpClient mClient;


        public LangsRequest(Request request) {
            mRequest = request;
            mClient = new OkHttpClient();
        }

        String run() throws IOException {
            Response response = mClient.newCall(mRequest).execute();
            return response.body().string();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                result = run();
            } catch (IOException e) {
                return ERROR;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals(ERROR)) {
                Toast.makeText(getActivity(), R.string.error2Message,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mTextViewFrom.setVisibility(View.VISIBLE);
            mTextViewTo.setVisibility(View.VISIBLE);
            mLanguages = Yandex.getLanguages(result);
        }
    }
}