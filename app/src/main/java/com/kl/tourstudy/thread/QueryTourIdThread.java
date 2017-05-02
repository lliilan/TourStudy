package com.kl.tourstudy.thread;

import android.os.Bundle;
import android.os.Message;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * Created by KL on 2017/4/17 0017.
 */

public class QueryTourIdThread extends Thread {

    @Override
    public void run() {
        super.run();
        String data = "";   //储存返回的JSON数据
        String servlet = "QueryTourSumServlet";
        String url = IP + PROJECT + servlet;

        try {
            int sum = Integer.parseInt(
                    OkHttpUtils.get()
                    .url(url)
                    .build()
                    .execute().body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
