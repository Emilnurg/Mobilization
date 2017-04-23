package ru.s4nchez.translater;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class Utils {

    public static void copyToBuffer(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
    }
}