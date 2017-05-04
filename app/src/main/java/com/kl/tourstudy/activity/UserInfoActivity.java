package com.kl.tourstudy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.UserInfoSettingAdapter;

public class UserInfoActivity extends AppCompatActivity {

    private RecyclerView setInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();

    }

    private void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        setInfo = (RecyclerView) findViewById(R.id.recycler_set_user);
        GridLayoutManager layoutManager = new GridLayoutManager(UserInfoActivity.this, 1);
        setInfo.setLayoutManager(layoutManager);
        UserInfoSettingAdapter adapter = new UserInfoSettingAdapter();
        setInfo.setAdapter(adapter);
    }
}
