package com.kl.tourstudy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.activity.ABookActivity;
import com.kl.tourstudy.gsonbean.BookInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;
import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 订单界面的适配器
 * Created by KL on 2017/5/26 0026.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private Context mContext;
    private int count;
    private static final String TAG = "BookAdapter";

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView bookImage;
        TextView tourName,tourGo;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            bookImage = (ImageView) itemView.findViewById(R.id.iv_book);
            tourName = (TextView) itemView.findViewById(R.id.tour_name);
            tourGo = (TextView) itemView.findViewById(R.id.tour_go);
        }
    }

    public BookAdapter(int count){
        this.count = count;
//        Log.e(TAG, "BookAdapter: 构造方法中的count:" + count);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        InfoTask task = new InfoTask(holder);
        task.execute(position);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    //这里通过位置获得servlet的相应位置的数据
    class InfoTask extends AsyncTask<Integer, Void, BookInfo> implements View.OnClickListener{

        private ViewHolder holder;
        private int id = 0;

        public InfoTask(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        protected BookInfo doInBackground(Integer... params) {
            BookInfo bookInfo = null;
            try {
                SharedPreferences preferences = mContext.getSharedPreferences("user_info", MODE_PRIVATE);
                int userId = preferences.getInt("userId",0);
                String servlet = "AllBookInfoServletApp";
                String url = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .addParams("position",params[0] + "")
                        .addParams("userId", userId + "")
                        .url(url)
                        .build().execute().body().string();
                Log.e(TAG, "run: 从订单查询回来的信息是：" + info );
                Gson gson = new Gson();
                bookInfo = new BookInfo();
                bookInfo = gson.fromJson(info, BookInfo.class);
                //信息成功获取
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bookInfo;
        }

        @Override
        protected void onPostExecute(BookInfo bookInfo) {
            super.onPostExecute(bookInfo);
            Glide.with(mContext).load(IP + bookInfo.getImage()).into(holder.bookImage);
            holder.tourName.setText(bookInfo.getTourName());
            holder.tourGo.setText(bookInfo.getGo());
            id = bookInfo.getBookId();
            holder.cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ABookActivity.class);
            intent.putExtra("bookId", id);
            mContext.startActivity(intent);
        }
    }

}
