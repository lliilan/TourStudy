package com.kl.tourstudy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.activity.ChatActivity;
import com.kl.tourstudy.gsonbean.ChatInfo;
import com.kl.tourstudy.gsonbean.Server;
import com.zhy.http.okhttp.OkHttpUtils;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 主界面-聊天界面-list的适配器
 * Created by KL on 2017/5/27 0027.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Context mContext;
    private int count = 0;
    private static final String TAG = "ChatListAdapter";

    public ChatListAdapter(int count) {
        this.count = count;
    }

    static public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private CircleImageView cv_icon;
        private TextView tv_name,tv_content,tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            cv_icon = (CircleImageView) itemView.findViewById(R.id.civ_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatTask task = new ChatTask(holder);
        task.execute(position);
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: 主界面聊天list的count:" + count );
        return count;
    }

    class ChatTask extends AsyncTask<Integer, Void, Server> implements View.OnClickListener {

        private ViewHolder holder = null;
        private int serverId = 0;
        ChatTask(ViewHolder holder){
            this.holder = holder;
        }

        @Override
        protected Server doInBackground(Integer... params) {
            Server server = null;
            try {
                SharedPreferences preferences = mContext.getSharedPreferences("user_info", MODE_PRIVATE);
                int chatA = preferences.getInt("userId",0);
                int status = preferences.getInt("LOG_STATUS",0);
                String servlet = "";
                Log.e(TAG, "doInBackground: status=" + status );
                if(status == 1){
                    servlet = "FindAllServletInfoServletApp";
                } else if (status == 3) {
                    servlet = "ServerInfoServletApp";
                }
                String url = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .addParams("chatA",chatA + "")
                        .addParams("position",params[0] + "")
                        .url(url)
                        .build().execute().body().string();
                server = new Server();
                Gson gson = new Gson();
                server = gson.fromJson(info, Server.class);
                serverId = server.getId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return server;
        }

        @Override
        protected void onPostExecute(Server server) {
            super.onPostExecute(server);
            Glide.with(mContext).load(IP + PROJECT + server.getIcon()).into(holder.cv_icon);
            Log.e(TAG, "onPostExecute: " + IP + server.getIcon() );
            holder.tv_name.setText(server.getName());
            holder.tv_content.setText(server.getPhone());
            holder.cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra("serverId", serverId);
            mContext.startActivity(intent);
        }
    }

}
