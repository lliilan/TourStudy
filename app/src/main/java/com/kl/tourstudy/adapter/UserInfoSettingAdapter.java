package com.kl.tourstudy.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kl.tourstudy.R;

/**
 * Created by KL on 2017/5/3 0003.
 */

public class UserInfoSettingAdapter extends RecyclerView.Adapter<UserInfoSettingAdapter.ViewHolder> {

    private static final String TAG = "UserInfoSettingAdapter";
    private Context mContext;
    private String[] setting = {"头像","详细资料","订单管理"};

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView text;

        ViewHolder(final View itemView, final Context context) {
            super(itemView);
            cardView = (CardView) itemView;
            image = (ImageView) itemView.findViewById(R.id.user_image);
            text = (TextView) itemView.findViewById(R.id.user_text);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //写跳转Intent
                    switch (getAdapterPosition()){
                        case 0:

                            break;
                        case 1:

                            break;
                        case 2:

                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    public UserInfoSettingAdapter(){

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recy_user, parent, false);
        return new UserInfoSettingAdapter.ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(setting[position]);
    }

    @Override
    public int getItemCount() {
        return setting.length;
    }


}
