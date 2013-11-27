package me.waye.androidspeeder.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.text.ClipboardManager;

/**
 * @author Tangwei
 * @date 2013-10-18
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class TextViewUtil {

    /**
     * @param ctx 上下文对象Context
     * @param text 放入剪贴板的内容
     */
    @SuppressLint("NewApi")
    public static void copyToClipboard(Context ctx, String text) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        } else {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            // clipboardManager.setText(view.getText()); // Deprecated
            ClipData clip = ClipData.newPlainText("复制的内容", text);
            clipboardManager.setPrimaryClip(clip);
        }
    }

    /**
     * @param ctx 上下文对象
     * @return 复制的文本
     */
    @SuppressLint("NewApi")
    public static String pasteFromClipboard(Context ctx) {
        String text = null;
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboardManager.hasText())
                text = clipboardManager.getText().toString();
        } else {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboardManager.hasPrimaryClip()) {
                CharSequence pasteData = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                if (pasteData != null)
                    text = pasteData.toString();
            }
        }
        return text;
    }
}
