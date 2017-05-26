package com.kl.tourstudy.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kl.tourstudy.R;

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

        ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            bookImage = (ImageView) itemView.findViewById(R.id.iv_book);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        //这里通过位置获得servlet的相应位置的数据
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
        Glide.with(mContext).load(R.drawable.first1).into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return count;
    }

}
