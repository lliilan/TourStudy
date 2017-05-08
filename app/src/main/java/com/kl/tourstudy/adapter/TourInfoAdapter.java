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

import static com.kl.tourstudy.util.PreferenceUtil.IP;

/**
 *
 * Created by KL on 2017/5/3 0003.
 */

public class TourInfoAdapter extends RecyclerView.Adapter<TourInfoAdapter.ViewHolder> {

    private static final String TAG = "TourInfoAdapter";
    private Context mContext;
    private String[] picUrl;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView tourImg;

        ViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tourImg = (ImageView) itemView.findViewById(R.id.image);

        }
    }

    public TourInfoAdapter(String[] picUrl, Context mContext){
        this.picUrl = picUrl;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.tour_info_pic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(IP + picUrl[position]).into(holder.tourImg);
    }

    @Override
    public int getItemCount() {
        return picUrl.length;
    }
}
