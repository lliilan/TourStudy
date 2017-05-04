package com.kl.tourstudy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.TourInfoAdapter;
import com.kl.tourstudy.gsonbean.TourInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

public class RecyclerItemActivity extends AppCompatActivity implements View.OnClickListener {

//    private ImageView image;
    private RecyclerView image;
    private int position = 1;
    private Button buttonBook,buttonQuestion;
    private Intent intent;
    private static final String TAG = "RecyclerItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_item);
        initView();
        intent = getIntent();
        position = intent.getIntExtra("position", 0);
        ImageTask imageTask = new ImageTask(image);
        imageTask.execute(position);

    }

    private void initView() {
        image = (RecyclerView) findViewById(R.id.item_image);
        buttonBook = (Button) findViewById(R.id.item_button1);
        buttonQuestion = (Button) findViewById(R.id.button_question);
        buttonBook.setOnClickListener(this);
        buttonQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_button1:
                SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
                if(preferences.getInt("LOG_STATUS",0) == 0){
                    Toast.makeText(RecyclerItemActivity.this, "请在登录后进行预定", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RecyclerItemActivity.this, LoginActivity.class));
                } else {
                    Intent intent2 = new Intent(RecyclerItemActivity.this, TourBookActivity.class);
                    intent2.putExtra("tourId",getIntent().getIntExtra("position", 0));
                    intent2.putExtra("userId",preferences.getInt("userId", 0));        //获取登录成功的用户的ID
                    startActivity(intent2);
                    finish();
                }
                break;
            case R.id.button_question:
                Intent intent = new Intent(RecyclerItemActivity.this, QuestionListActivity.class);
                intent.putExtra("tour_id",getIntent().getIntExtra("position", 0));
                startActivity(intent);

                break;
        }
    }

    private class ImageTask extends AsyncTask<Integer, Void, TourInfo>{

        private RecyclerView image;

        ImageTask(RecyclerView image){
              this.image = image;
        }

        @Override
        protected TourInfo doInBackground(Integer... params) {
            TourInfo tourInfo = null;
            try {
                String servlet = "QueryTourServletApp";
                String urls = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .url(urls)
                        .addParams("id", params[0] + "")
                        .build()
                        .execute().body().string();
                Gson gson = new Gson();
                //解析JSON数据
                tourInfo = gson.fromJson(info, TourInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tourInfo;
        }

        @Override
        protected void onPostExecute(TourInfo tourInfo) {
            super.onPostExecute(tourInfo);
            String[] picUrl = tourInfo.getPicInfo().split(";");GridLayoutManager layoutManager = new GridLayoutManager(RecyclerItemActivity.this, 1);
            image.setLayoutManager(layoutManager);
            TourInfoAdapter tourInfoAdapter = new TourInfoAdapter(picUrl, RecyclerItemActivity.this);
            image.setAdapter(tourInfoAdapter);
        }
    }

}
