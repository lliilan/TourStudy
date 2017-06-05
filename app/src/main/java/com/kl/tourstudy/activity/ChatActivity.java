package com.kl.tourstudy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.ChatAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChatActivity";
    private EditText message;
    private RecyclerView rv_msg;
    private int serverId = 0;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        serverId = intent.getIntExtra("serverId", 0);
        initView();
    }

    private void initView() {
        Button send = (Button) findViewById(R.id.btn_send);
        message = (EditText) findViewById(R.id.ed_text);
        rv_msg = (RecyclerView) findViewById(R.id.rv_message);
        send.setOnClickListener(this);
        ChatCountTask countTask = new ChatCountTask(serverId);
        countTask.execute();
//        commentsList.setAdapter(commentsAdapter);//重新为listView设置更新后的commentsAdapter
//        commentsAdapter.notifyDataSetChanged(); //更新显示
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send){
            final String text = message.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        SharedPreferences preferences = getSharedPreferences("user_info",MODE_PRIVATE);
                        int id = preferences.getInt("userId",0);
                        if (id != 0){
                            String servlet = "InsertMessageServletApp";
                            String url = IP + PROJECT + servlet;
                            String info = OkHttpUtils.post()
                                    .addParams("message",text)
                                    .addParams("idA",serverId + "")
                                    .addParams("idB",id + "")
                                    .url(url)
                                    .build()
                                    .execute().body().string();
                            if (info.equals("error")){
                                Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            message.setText("");
            Intent intent2 = new Intent(ChatActivity.this, ChatActivity.class);
            intent2.putExtra("serverId",serverId);
            startActivity(intent2);
            finish();
//            ChatCountTask countTask = new ChatCountTask(serverId);
//            countTask.execute();
//            rv_msg.setAdapter(chatAdapter);
//            chatAdapter.notifyDataSetChanged();
        }
    }

    private class ChatCountTask extends AsyncTask<Object, Object, int[]> {

        int serverId = 0;

        ChatCountTask(int serverId){
            this.serverId = serverId;
        }

        @Override
        protected int[] doInBackground(Object... params) {
            int[] param = new int[3];
            int count = 0;
            try{
                SharedPreferences preferences = ChatActivity.this.getSharedPreferences("user_info", MODE_PRIVATE);
                int userId = preferences.getInt("userId",0);
                String servlet = "ChatCountServletApp";
                String url = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .addParams("idA", String.valueOf(userId))
                        .addParams("idB", String.valueOf(serverId))
                        .url(url)
                        .build()
                        .execute().body().string();
                count = Integer.parseInt(info);
                param[0] = count;
                param[1] = userId;
                param[2] = serverId;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return param;
        }

        @Override
        protected void onPostExecute(int[] param) {
            super.onPostExecute(param);
            chatAdapter = new ChatAdapter(param);
            GridLayoutManager layoutManager = new GridLayoutManager(ChatActivity.this, 1);
            rv_msg.setLayoutManager(layoutManager);
            rv_msg.setAdapter(chatAdapter);
//            chatAdapter.notifyDataSetChanged();
//            rv_msg.smoothScrollToPosition(chatAdapter.getItemCount()-1);
        }
    }
}
