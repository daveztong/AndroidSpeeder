/**
 * Project name: AndroidSpeeder
 * Package name: me.waye.androidspeeder.widget
 * Filename: CustomProgressDialog.java
 * Created time: Nov 18, 2013
 * Copyright: Copyright(c) 2013. All Rights Reserved.
 */
 
package me.waye.androidspeeder.widget;

import me.waye.androidspeeder.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: CustomProgressDialog
 * @Description: 自定义对话框
 * @author tangwei
 * @date Nov 18, 2013 1:52:54 PM
 * 
 */
public class CustomProgressDialog extends Dialog {

    private static CustomProgressDialog customProgressDialog = null;

    /**
     * @param context
     */
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static CustomProgressDialog createDialog(Context context) {
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.custom_progress_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return customProgressDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (customProgressDialog == null) {
            return;
        }

        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    /**
     * @Title: setMessage
     * @Description: 设置对话框提示信息
     * @param strMessage
     * @return customProgressDialog
     */
    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }

        return customProgressDialog;
    }

}
