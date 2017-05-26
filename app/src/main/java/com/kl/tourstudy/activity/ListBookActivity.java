package com.kl.tourstudy.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.BookAdapter;
import com.kl.tourstudy.gsonbean.BookInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

public class ListBookActivity extends AppCompatActivity {

    private static final String TAG = "ListBookActivity";
    private int count = 0;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        ListBookTask task = new ListBookTask();
        task.execute(getUserId());
        rv = (RecyclerView) findViewById(R.id.recycler_item);
    }

    public int getUserId() {
        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        int id = preferences.getInt("userId",0);
        return id;
    }

    private class ListBookTask extends AsyncTask<Integer, Void, BookInfo>{

        @Override
        protected BookInfo doInBackground(Integer... params) {
            BookInfo bookInfo = new BookInfo();
            try{
                String servlet = "UserBookServletApp";
                String url = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .addParams("userId", String.valueOf(params[0]))
                        .url(url)
                        .build()
                        .execute().body().string();
//                Log.e(TAG, "doInBackground: 用户订单数量info=" + info );
                count = Integer.parseInt(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bookInfo;
        }

        @Override
        protected void onPostExecute(BookInfo bookInfo) {
            super.onPostExecute(bookInfo);
//            Log.e(TAG, "onPostExecute: " + bookInfo.getUserName() );
            //设置适配器
            BookAdapter bookAdapter = new BookAdapter(count);
            GridLayoutManager layoutManager = new GridLayoutManager(ListBookActivity.this, 1);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(bookAdapter);
        }
    }
}
