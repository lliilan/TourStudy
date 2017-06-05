package com.kl.tourstudy.fargment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.ChatListAdapter;
import com.kl.tourstudy.adapter.ChatListServerAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;
import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 聊天用Fragment
 * Created by KL on 2017/4/12 0012.
 */

public class MessageFragment extends Fragment {

    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main2, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rv = (RecyclerView) view.findViewById(R.id.rv_chat);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        rv.setLayoutManager(layoutManager);
        getRvCount();
    }

    private void getRvCount() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String servlet = null;
                SharedPreferences preferences = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
                int status = preferences.getInt("LOG_STATUS",0);
                int userId = preferences.getInt("userId",0);
                if (status == 1){
                    servlet = "ServerCountServletApp";
                }else if (status == 3){
                    servlet = "ChatterCountServletApp";
                }
                String url = IP + PROJECT + servlet;
                OkHttpUtils.get().addParams("chatA",userId + "").url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
//                        int status = preferences.getInt("LOG_STATUS",0);
//                        if (status == 1){
                            ChatListAdapter chatAdapter = new ChatListAdapter(Integer.parseInt(response));
                            rv.setAdapter(chatAdapter);
//                        } else if (status == 3){
//                            ChatListServerAdapter chatServerAdapter = new ChatListServerAdapter(Integer.parseInt(response));
//                            rv.setAdapter(chatServerAdapter);
//                        }
                    }
                });
            }
        });
        thread.start();
    }

}
