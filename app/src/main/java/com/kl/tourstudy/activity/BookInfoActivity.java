package com.kl.tourstudy.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.TourInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

public class BookInfoActivity extends AppCompatActivity {

    private static final String TAG = "BookInfoActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Intent intent = getIntent();
        int tourId = intent.getIntExtra("tourId", 0);
        int userId = intent.getIntExtra("userId", 0);
        BookInfoTask task = new BookInfoTask();
        task.execute(tourId, userId);

    }

    public class BookInfoTask extends AsyncTask<Integer, Void, String>{

        public BookInfoTask(){
            //传组件进来
        }

        @Override
        protected String doInBackground(Integer... params) {


            try {
                String servlet = "InsertTourBookServletApp";
                String urls = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .url(urls)
                        .addParams("tourId", params[0] + "")
                        .addParams("userId", params[1] + "")
                        .build()
                        .execute().body().string();
                Log.e(TAG, "doInBackground: info:" + info );
//                Gson gson = new Gson();
                //解析JSON数据
//                tourInfo = gson.fromJson(info, TourInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }



}
