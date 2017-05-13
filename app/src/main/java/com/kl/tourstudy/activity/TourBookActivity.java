package com.kl.tourstudy.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.BooleanReturn;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 进行预定
 */
public class TourBookActivity extends AppCompatActivity {

    private static final String TAG = "TourBookActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_bookctivity);
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", 0);
        int tourId = intent.getIntExtra("tourId", 0) + 1;   //listview中的item不是以1开始，需要加1
        int status = 1;
        BookTask book = new BookTask();
        book.execute(userId,tourId,status);
    }

    public class BookTask extends AsyncTask<Integer ,Void, String[]>{

        @Override
        protected String[] doInBackground(Integer... params) {
            String servlet = "InsertTourBookServletApp";
            String url = IP + PROJECT + servlet;
            String[] data = new String[3];

            try {
                data[0] = OkHttpUtils.post()
                        .addParams("userId",params[0].toString())
                        .addParams("tourId",params[1].toString())
                        .addParams("status",params[2].toString())
                        .url(url)
                        .build()
                        .execute().body().string();
                //下面两个参数用于显示之后的订单界面时使用
                data[1] = params[1].toString();
                data[2] = params[0].toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            Gson gson = new Gson();

            BooleanReturn booleanReturn = gson.fromJson(s[0], BooleanReturn.class);
            boolean flag = booleanReturn.isFlag();
            if (flag){
                Toast.makeText(TourBookActivity.this, "预定成功", Toast.LENGTH_SHORT).show();
                Intent intentBook = new Intent(TourBookActivity.this,BookInfoActivity.class);
                intentBook.putExtra("tourId",Integer.parseInt(s[1]));
                intentBook.putExtra("userId",Integer.parseInt(s[2]));
                startActivity(intentBook);
                finish();
            } else {
                Toast.makeText(TourBookActivity.this, "预定失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
