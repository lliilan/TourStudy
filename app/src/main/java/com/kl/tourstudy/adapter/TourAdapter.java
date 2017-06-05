package com.kl.tourstudy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

import java.io.IOException;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 加载主页面ListView的适配器
 * Created by KL on 2017/4/12 0012.
 */

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {

    private static final String TAG = "TourAdapter";
    private Context mContext;
    private int sum;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView tourImg;
        TextView tourTitle;
        TextView tourPrice,tourDate,tourDay;

        ViewHolder(final View itemView, final Context context) {
            super(itemView);
            cardView = (CardView) itemView;
            tourImg = (ImageView) itemView.findViewById(R.id.tour_img);
            tourTitle = (TextView) itemView.findViewById(R.id.tour_title);
            tourPrice = (TextView) itemView.findViewById(R.id.tour_price);
            tourDate = (TextView) itemView.findViewById(R.id.tour_date);
            tourDay = (TextView) itemView.findViewById(R.id.tour_day);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecyclerItemActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    Log.e(TAG, "onClick: position-Adapter:" + getAdapterPosition());
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
        LoadTask task = new LoadTask(holder.tourTitle, holder.tourImg, holder.tourPrice, holder.tourDate, holder.tourDay);
        task.execute(position);
    }

    @Override
    public int getItemCount() {
        return sum;
    }

    private class LoadTask extends AsyncTask<Integer, Void, TourInfo>{

        private TextView tourTitle;
        private ImageView tourImage;
        private TextView tourPrice,tourDate,tourDay;

        LoadTask(TextView tourTitle, ImageView tourImg, TextView tourPrice, TextView tourDate, TextView tourDay) {
            this.tourTitle = tourTitle;
            this.tourImage = tourImg;
            this.tourPrice = tourPrice;
            this.tourDate = tourDate;
            this.tourDay = tourDay;
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
                String price = tourInfo.getPrice() + "";

                //对游学信息中的具体价格字符进行颜色改变
                String priceStr = "￥" + tourInfo.getPrice() + "/人";
                int colorPriceStart = priceStr.indexOf("￥") + 1;    //获得价格在其所处字符串的index
                int colorPriceEnd = colorPriceStart + price.length();   //获得价格在其字符串的结束index
                //以下语句设置价格所在字符串位置的前景色，即价格的颜色
                SpannableStringBuilder stylePrice = new SpannableStringBuilder(priceStr);
                stylePrice.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFF33")),
                        colorPriceStart, colorPriceEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                String date = tourInfo.getEndSignDate();
                String day = tourInfo.getDay();
                String dayStr = "为期" + day + "天游学";
                int colorDayStart = dayStr.indexOf("期") + 1;
                int colorDayEnd = colorPriceStart + day.length() + 2;
                SpannableStringBuilder styleDay = new SpannableStringBuilder(dayStr);
                styleDay.setSpan(new ForegroundColorSpan(Color.parseColor("#F79700")),
                        colorDayStart, colorDayEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                tourTitle.setText(name);
                Glide.with(mContext).load(image).into(tourImage);
//                Log.e(TAG, "onPostExecute: " + image );
                tourPrice.setText(stylePrice);
                tourDate.setText("截至报名日期:" + date);
                tourDay.setText(styleDay);
            }
        }
    }
}
