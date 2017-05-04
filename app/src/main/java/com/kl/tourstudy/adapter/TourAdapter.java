package com.kl.tourstudy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.activity.RecyclerItemActivity;
import com.kl.tourstudy.gsonbean.TourInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 加载主页面ListView
 * Created by KL on 2017/4/12 0012.
 */

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {

    private static final String TAG = "TourAdapter";
    private Context mContext;
    private int sum;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView tourImg;
        TextView tourText;

        ViewHolder(final View itemView, final Context context) {
            super(itemView);
            cardView = (CardView) itemView;
            tourImg = (ImageView) itemView.findViewById(R.id.tour_img);
            tourText = (TextView) itemView.findViewById(R.id.tour_text);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecyclerItemActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public TourAdapter(String response){
        sum = Integer.parseInt(response);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.tour_info, parent, false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        LoadTask task = new LoadTask(holder.tourText, holder.tourImg);
        task.execute(position);
    }

    @Override
    public int getItemCount() {
        return sum;
    }

    private class LoadTask extends AsyncTask<Integer, Void, TourInfo>{

        private TextView tourText;
        private ImageView tourImage;

        LoadTask(TextView tourText, ImageView tourImg) {
            this.tourText = tourText;
            this.tourImage = tourImg;
        }

        @Override
        protected TourInfo doInBackground(Integer... params) {
            TourInfo tourInfo = null;
            try {
                String servlet = "QueryTourServletApp";
                String urls = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .url(urls)
                        .addParams("id", params[0] + "")
                        .build()
                        .execute().body().string();
//                Log.e(TAG, "doInBackground: " + info );
                Gson gson = new Gson();
                //解析JSON数据
                tourInfo = gson.fromJson(info, TourInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return tourInfo;
        }

        @Override
        protected void onPostExecute(TourInfo tourInfo) {
            super.onPostExecute(tourInfo);
            if (tourInfo != null) {
                String name = tourInfo.getName();
                String image = IP + tourInfo.getImage();

                tourText.setText(name);
                Glide.with(mContext).load(image).into(tourImage);
            }
        }
    }
}
