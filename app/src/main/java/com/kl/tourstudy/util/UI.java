package com.kl.tourstudy.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/4/8.
 */
public class UI {
    private static ProgressDialog progressDialog;
    /**
     * 显示进度对话框
     */
    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    public static void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
