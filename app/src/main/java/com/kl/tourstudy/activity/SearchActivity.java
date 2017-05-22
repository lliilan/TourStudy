package com.kl.tourstudy.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.TourInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import static com.kl.tourstudy.util.PreferenceUtil.ERROR;
import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SearchActivity";
    private TagFlowLayout tfl;
    private EditText et_search;
    private Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        tfl = (TagFlowLayout) findViewById(R.id.tfl);
//        et_search = (EditText) findViewById(R.id.et_search);
//        btn_search = (Button) findViewById(R.id.btn_search);
//        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_search:
//                String text = et_search.getText().toString();
//                if (text.equals("")){
//                    Toast.makeText(SearchActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
//                } else {
//                    SearchTask task = new SearchTask();
//                    task.execute(text);
//                }
//                break;
            default:
                break;
        }
    }

    public class SearchTask extends AsyncTask<String, Void, String>{

        private String data = null;   //储存返回的JSON数据
        private String servlet = "QueryATourInfoServletApp";
        private String url = IP + PROJECT + servlet;
        @Override
        protected String doInBackground(String... params) {
            try {
                // 这里写网络连接认证服务
                data = OkHttpUtils.post()
                        .url(url)
                        .addParams("name",params[0])
                        .build()
                        .execute().body().string();
            } catch (Exception e) {
                return ERROR;
            }
            return data;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Gson gson = new Gson();
            TourInfo info = gson.fromJson(str, TourInfo.class);
            //改成跳转
            Toast.makeText(SearchActivity.this, info.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
