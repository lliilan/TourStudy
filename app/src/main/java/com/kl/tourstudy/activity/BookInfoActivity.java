package com.kl.tourstudy.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.BookInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 初步生成订单，给用户查看，但是并没有正式的下单
 */
public class BookInfoActivity extends AppCompatActivity {

    private Button btn_pay;
    private CircleImageView image;
    private ImageView iv_phone;
    private List<TextView> list_tv = new ArrayList<>();
    private String phones;
    private int tourId,userId;
    private static final String TAG = "BookInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        Intent intentBook = getIntent();
        tourId = intentBook.getIntExtra("tourId", 0) + 1;
        userId = intentBook.getIntExtra("userId", 0);
        initView();
        BookInfoTask task = new BookInfoTask(btn_pay, image, list_tv, iv_phone);
        task.execute(tourId, userId);
    }

    private void initView() {
        btn_pay = (Button) findViewById(R.id.btn_pay);
        image = (CircleImageView) findViewById(R.id.civ_image);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);
        TextView tv_status = (TextView) findViewById(R.id.tv_status);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_country = (TextView) findViewById(R.id.tv_country);
        TextView tv_city = (TextView) findViewById(R.id.tv_city);
        TextView tv_go = (TextView) findViewById(R.id.tv_go);
        TextView tv_day = (TextView) findViewById(R.id.tv_day);
        TextView tv_deposit = (TextView) findViewById(R.id.tv_deposit);
        TextView tv_money = (TextView) findViewById(R.id.tv_money);
        TextView tv_manage = (TextView) findViewById(R.id.tv_manage);
        list_tv.add(tv_status);
        list_tv.add(tv_title);
        list_tv.add(tv_country);
        list_tv.add(tv_city);
        list_tv.add(tv_go);
        list_tv.add(tv_day);
        list_tv.add(tv_deposit);
        list_tv.add(tv_money);
        list_tv.add(tv_manage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                //判断是否获得拨打游学管理员电话的权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                } else {
                    Toast.makeText(BookInfoActivity.this, "你没有给于授权", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phones));
            startActivity(intent);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private class BookInfoTask extends AsyncTask<Integer, Void, BookInfo> implements View.OnClickListener {

        private Button btn_pay;
        private CircleImageView image;
        private ImageView iv_phone; //联系负责人的图片按钮
        private List<TextView> list_tv = new ArrayList<>();
        private String phone;   //储存游学负责人的电话号码
        private String tourName;
        private String payDescript = "游学订单支付";  //订单支付的描述
        private float payMoney;
        private float payDeposit;
        private Boolean AliPayOrWechat = true;      //默认使用支付宝付款，暂时该第三方支付不支持微信付款

        BookInfoTask(Button btn_pay, CircleImageView image, List<TextView> list_tv, ImageView iv_phone){
            this.btn_pay = btn_pay;
            this.image = image;
            this.list_tv = list_tv;
            this.iv_phone = iv_phone;
        }

        @Override
        protected BookInfo doInBackground(Integer... params) {
            BookInfo bookInfo = null;
            try {
                //该servlet还没有正式下单，只是找到了该游学路线的相关信息
                String servlet = "BookInfoServletApp";
                String urls = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .url(urls)
                        .addParams("tourId", params[0] + "")
                        .addParams("userId", params[1] + "")
                        .build()
                        .execute().body().string();
                Gson gson = new Gson();
                bookInfo = gson.fromJson(info, BookInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bookInfo;
        }

        @Override
        protected void onPostExecute(BookInfo bookInfo) {
            super.onPostExecute(bookInfo);
            Glide.with(BookInfoActivity.this).load(IP + bookInfo.getImage()).into(image);

            TextView tv_title = list_tv.get(1);
            iv_phone.setOnClickListener(this);
            this.btn_pay.setOnClickListener(this);
            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_title);
            ll.setOnClickListener(this);

            tourName = bookInfo.getTourName();
            payDeposit = bookInfo.getDeposit();
            payMoney = bookInfo.getMoney();
            tv_title.setText(tourName);
            list_tv.get(2).setText(getString(R.string.tour_place) + bookInfo.getCountry());
            list_tv.get(3).setText(getString(R.string.go_place) + bookInfo.getCity());
            list_tv.get(4).setText(getString(R.string.go_date) + bookInfo.getGo());
            list_tv.get(5).setText(getString(R.string.tour_day) + bookInfo.getDay() + getString(R.string.day));
            list_tv.get(6).setText(getString(R.string.tour_deposit) + payDeposit + getString(R.string.yuan));
            list_tv.get(7).setText(getString(R.string.tour_money) + payMoney + getString(R.string.yuan));
            list_tv.get(8).setText(getString(R.string.tour_manager) + bookInfo.getManager() + "  " + bookInfo.getManagerPhone());
            phone = bookInfo.getManagerPhone();
            setPhones(phone);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
//                //联系负责人按钮
                case R.id.iv_phone:
                    if (ContextCompat.checkSelfPermission(BookInfoActivity.this, Manifest.permission.CALL_PHONE)
                             != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(BookInfoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call();
                    }
                    break;
                //确认下单按钮
                case R.id.btn_pay:
                    Intent intentPay = new Intent(BookInfoActivity.this, TourBookActivity.class);
                    intentPay.putExtra("userId",userId);
                    intentPay.putExtra("tourId",tourId);
                    intentPay.putExtra("name",tourName);
                    intentPay.putExtra("descript",payDescript);
                    intentPay.putExtra("money",payMoney);
                    intentPay.putExtra("deposit",payDeposit);
                    intentPay.putExtra("payStyle",AliPayOrWechat);
                    startActivity(intentPay);
                    finish();
                    break;
                //点击后跳转到游学路线详情
                case R.id.ll_title:
                    //给Position可以直接使用，RecyclerItemActivity
                    Intent intent = new Intent(BookInfoActivity.this, RecyclerItemActivity.class);
                    intent.putExtra("position", tourId - 1);    //因为得到的是list的值，list从0开始
                    startActivity(intent);
                    break;
            }
        }

    }

    public void setPhones(String phone){
        this.phones = phone;
    }
}
