package com.kl.tourstudy.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.ChatInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 详细聊天界面的Adapter
 * Created by KL on 2017/6/4 0004.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final String TAG = "ChatAdapter";
    private Context mContext;
    int count = 0;
    int idA = 0;
    int idB = 0;

    public ChatAdapter(int[] param) {
        this.count = param[0];
        this.idA = param[1];
        this.idB = param[2];
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_left,tv_right;
        private LinearLayout ll_left,ll_right;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_left = (LinearLayout) itemView.findViewById(R.id.ll_left);
            ll_right = (LinearLayout) itemView.findViewById(R.id.ll_right);
            tv_left = (TextView) itemView.findViewById(R.id.msg_left);
            tv_right = (TextView) itemView.findViewById(R.id.msg_right);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatInfoTask chatInfoTask = new ChatInfoTask(holder);
        chatInfoTask.execute(position);
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: 聊天的count:"+ count);
        return count;
    }

    class ChatInfoTask extends AsyncTask<Integer, Void, ChatInfo>{

        private ViewHolder holder;

        ChatInfoTask(ViewHolder holder){
            this.holder = holder;
        }

        @Override
        protected ChatInfo doInBackground(Integer... params) {
            ChatInfo chatInfo = new ChatInfo();
            try{
                String servlet = "AllChatContentServletApp";
                String url = IP + PROJECT + servlet;
                String data = OkHttpUtils.post()
                        .addParams("idA", String.valueOf(idA))
                        .addParams("idB", String.valueOf(idB))
                        .addParams("position", String.valueOf(params[0]))
                        .url(url)
                        .build().execute().body().string();
                Log.e(TAG, "doInBackground: data=" + data );
                Gson gson = new Gson();
                chatInfo = gson.fromJson(data,ChatInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return chatInfo;
        }

        @Override
        protected void onPostExecute(ChatInfo chatInfo) {
            super.onPostExecute(chatInfo);
            if (chatInfo.getChatA() == idB){
                holder.tv_right.setText(chatInfo.getContent());
                holder.tv_left.setVisibility(View.GONE);
            } else if(chatInfo.getChatA() == idA){
                holder.tv_right.setVisibility(View.GONE);
                holder.tv_left.setText(chatInfo.getContent());
            }
        }
    }

}
