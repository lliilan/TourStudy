package com.kl.tourstudy.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.BookInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;

import c.b.BP;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 让用户自己去订单页面查看自己的订单
 */
public class TourBookActivity extends AppCompatActivity {

    private static final String TAG = "TourBookActivity";
//    private ProgressDialog progress;
//    private String tourName;
//    private String payDescript = "游学订单支付";  //订单支付的描述
//    private float payMoney;
//    private float payDeposit;
//    private Boolean AliPayOrWechat;      //默认使用支付宝付款，暂时该第三方支付不支持微信付款

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //对第三方支付接口进行初始化
        BP.init("9547ff1068535a47e8e78dd30bd6e47e");

        setContentView(R.layout.activity_tour_bookctivity);
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", 0);
        int tourId = intent.getIntExtra("tourId", 0);
//        int tourId = intent.getIntExtra("tourId", 0) + 1;
//        tourName = intent.getStringExtra("name");
//        payDescript = intent.getStringExtra("descript");
//        payMoney = intent.getFloatExtra("payMoney",0);
//        payDeposit = intent.getFloatExtra("deposit",0);
//        AliPayOrWechat = intent.getBooleanExtra("payStyle",true);

        int status = 1;
        BookTask book = new BookTask();
        book.execute(userId, tourId,status);
    }

    private class BookTask extends AsyncTask<Integer ,Void, String>{

        int id = 0;
        @Override
        protected String doInBackground(Integer... params) {

            String servlet = "InsertTourBookServletApp";
            String url = IP + PROJECT + servlet;
            String data = null;
            try {
                data = OkHttpUtils.post()
                        .addParams("userId",params[0].toString())
                        .addParams("tourId",params[1].toString())
                        .addParams("status",params[2].toString())
                        .url(url)
                        .build()
                        .execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();

            BookInfo bookInfo = gson.fromJson(s, BookInfo.class);
            id = bookInfo.getBookId();
//            Log.e(TAG, "onPostExecute: " + id );
            if (id != 0){
                Toast.makeText(TourBookActivity.this, "预定成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TourBookActivity.this, ABookActivity.class);
                intent.putExtra("bookId",id);
                startActivity(intent);
                finish();
//                if (!checkPackageInstalled("com.eg.android.AlipayGphone",
//                        "https://www.alipay.com")){
//                    Toast.makeText(TourBookActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT).show();
//                } else {
//                    builderDialog();
//                }
            } else {
                Toast.makeText(TourBookActivity.this, "预定失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
