package com.kl.tourstudy.activity;

import android.Manifest;
import android.app.ProgressDialog;
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

import c.b.BP;
import c.b.PListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 用户下单后自动跳转到该页面查看其订单状态
 */
public class BookInfoActivity extends AppCompatActivity {

    private static final String TAG = "BookInfoActivity";
    private Button btn_call;
    private Button btn_pay;
    private CircleImageView image;
    private List<TextView> list_tv = new ArrayList<>();
    private String phones;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        //对第三方支付接口进行初始化
        BP.init("9547ff1068535a47e8e78dd30bd6e47e");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Intent intentBook = getIntent();
        int tourId = intentBook.getIntExtra("tourId", 0);
        int userId = intentBook.getIntExtra("userId", 0);
        initView();
        BookInfoTask task = new BookInfoTask(btn_pay, image, list_tv);
        task.execute(tourId, userId);
    }

    private void initView() {
        btn_call = (Button) findViewById(R.id.btn_call);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        image = (CircleImageView) findViewById(R.id.civ_image);
        TextView tv_status = (TextView) findViewById(R.id.tv_status);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_country = (TextView) findViewById(R.id.tv_country);
        TextView tv_city = (TextView) findViewById(R.id.tv_city);
        TextView tv_go = (TextView) findViewById(R.id.tv_go);
        TextView tv_day = (TextView) findViewById(R.id.tv_day);
        TextView tv_deposit = (TextView) findViewById(R.id.tv_deposit);
        TextView tv_money = (TextView) findViewById(R.id.tv_money);
        TextView tv_manage = (TextView) findViewById(R.id.tv_manage);
        TextView tv_book_id = (TextView) findViewById(R.id.tv_book_id);
        TextView tv_book_time = (TextView) findViewById(R.id.tv_book_time);
        TextView tv_headman = (TextView) findViewById(R.id.tv_headman);
        TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
        list_tv.add(tv_status);
        list_tv.add(tv_title);
        list_tv.add(tv_country);
        list_tv.add(tv_city);
        list_tv.add(tv_go);
        list_tv.add(tv_day);
        list_tv.add(tv_deposit);
        list_tv.add(tv_money);
        list_tv.add(tv_manage);
        list_tv.add(tv_book_id);
        list_tv.add(tv_book_time);
        list_tv.add(tv_headman);
        list_tv.add(tv_phone);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
//                    Log.e(TAG, "onRequestPermissionsResult: phone:" + phones );
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
        private List<TextView> list_tv = new ArrayList<>();
        private int tourId;
        private String phone;
        private String tourName;
        private String payDescript = "游学订单支付";
        private float payMoney;
        private Boolean AliPayOrWechat = true;      //默认使用支付宝付款，暂时该第三方支付不支持微信付款

        BookInfoTask(Button btn_pay, CircleImageView image, List<TextView> list_tv){
            this.btn_pay = btn_pay;
            this.image = image;
            this.list_tv = list_tv;
        }

        @Override
        protected BookInfo doInBackground(Integer... params) {
            BookInfo bookInfo = null;
            try {
                String servlet = "BookInfoServletApp";
                String urls = IP + PROJECT + servlet;
                String info = OkHttpUtils.post()
                        .url(urls)
                        .addParams("tourId", params[0] + "")
                        .addParams("userId", params[1] + "")
                        .build()
                        .execute().body().string();
                Log.e(TAG, "doInBackground: info:" + info );
                Gson gson = new Gson();
                //解析JSON数据
                bookInfo = gson.fromJson(info, BookInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bookInfo;
        }

        @Override
        protected void onPostExecute(BookInfo bookInfo) {
            super.onPostExecute(bookInfo);
            Log.e(TAG, "onPostExecute: " + bookInfo.getBookStatus() );
            Glide.with(BookInfoActivity.this).load(IP + bookInfo.getImage()).into(image);

            TextView tv_status = list_tv.get(0);
            int status = bookInfo.getBookStatus();
            if (status == 1){
                tv_status.setText("下单成功，等待付款");
            } else if (status == 0){
                tv_status.setText("订单已取消");
            } else if (status == 2){
                tv_status.setText("交易成功");
            }

            TextView tv_title = list_tv.get(1);

            btn_call.setOnClickListener(this);
            this.btn_pay.setOnClickListener(this);
            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_title);
            ll.setOnClickListener(this);

            tourName = bookInfo.getTourName();
            payMoney = bookInfo.getDeposit();
            tv_title.setText(tourName);
            list_tv.get(2).setText(getString(R.string.tour_place) + bookInfo.getCountry());
            list_tv.get(3).setText(getString(R.string.go_place) + bookInfo.getCity());
            list_tv.get(4).setText(getString(R.string.go_date) + bookInfo.getGo());
            list_tv.get(5).setText(getString(R.string.tour_day) + bookInfo.getDay() + getString(R.string.day));
            list_tv.get(6).setText(getString(R.string.tour_deposit) + payMoney + getString(R.string.yuan));
            list_tv.get(7).setText(getString(R.string.tour_money) + bookInfo.getMoney() + getString(R.string.yuan));
            list_tv.get(8).setText(getString(R.string.tour_manager) + bookInfo.getManager() + "  " + bookInfo.getManagerPhone());
            list_tv.get(9).setText(getString(R.string.book_id) + bookInfo.getBookId() + "");
            list_tv.get(10).setText(getString(R.string.book_date) + bookInfo.getBookDate());
            list_tv.get(11).setText(getString(R.string.book_user) + bookInfo.getUserName());
            list_tv.get(12).setText(getString(R.string.book_tel) + bookInfo.getUserName());
            tourId = bookInfo.getTourId();
            phone = bookInfo.getManagerPhone();
            setPhones(phone);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //联系负责人按钮
                case R.id.btn_call:
                    if (ContextCompat.checkSelfPermission(BookInfoActivity.this, Manifest.permission.CALL_PHONE)
                             != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(BookInfoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call();
                    }
                    break;
                case R.id.btn_pay:
                    if (!checkPackageInstalled("com.eg.android.AlipayGphone",
                        "https://www.alipay.com")){
                        Toast.makeText(BookInfoActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT).show();
                    } else{
                        showProgressDialog("正在获取订单...\nSDK版本号:" + BP.getPaySdkVersion());
                    }

                    Log.e(TAG, "onClick: tourName=" + tourName  + ",payDescript=" + payDescript + ",payMoney=" + payMoney);
                    BP.pay(tourName, payDescript, payMoney, AliPayOrWechat, new PListener() {
                        @Override
                        public void orderId(String s) {
                            // 此处应该保存订单号,比如保存进数据库等,以便以后查询
//                            order.setText(orderId);
//                            tv.append(name + "'s orderid is " + orderId + "\n\n");
//                            showDialog("获取订单成功!请等待跳转到支付页面~");
                        }

                        @Override
                        public void succeed() {
                            Toast.makeText(BookInfoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }

                        // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
                        @Override
                        public void fail(int code, String reason) {
                            if (code == -2){
                                Toast.makeText(BookInfoActivity.this,
                                        "支付中断,错误代码:" + code + "原因是:" + reason, Toast.LENGTH_SHORT)
                                        .show();
                            }
                            hideProgressDialog();
                        }

                        @Override
                        public void unknow() {
                            Toast.makeText(BookInfoActivity.this, "支付结果未知，请稍后手动查询", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }
                    });
                    break;
                //点击后显示下单后到出游成功之间的时间信息
//                case R.id.rl_status:
////                    Log.e(TAG, "onClick: tv_status" );
//
//                    break;
                //点击后跳转到游学路线详情
                case R.id.ll_title:
                    //给Position 可以直接使用，RecyclerItemActivity
                    Intent intent = new Intent(BookInfoActivity.this, RecyclerItemActivity.class);
                    intent.putExtra("position", tourId - 1);    //因为得到的是list的值，list从0开始
                    startActivity(intent);
                    break;
            }
        }

        private void hideProgressDialog() {
            if (progress != null && progress.isShowing())
                try {
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        private void showProgressDialog(String message) {
            try {
                if (progress == null) {
                    progress = new ProgressDialog(BookInfoActivity.this);
                    progress.setCancelable(true);
                }
                progress.setMessage(message);
                progress.show();
            } catch (Exception e) {
                //在其他线程调用dialog会报错
            }
        }

        /**
         * 检查某包名应用是否已经安装
         *
         * @param packageName 包名
         * @param browserUrl  如果没有应用市场，去官网下载
         * @return 返回值
         */
        private boolean checkPackageInstalled(String packageName, String browserUrl) {
            try {
                // 检查是否有支付宝客户端
                getPackageManager().getPackageInfo(packageName, 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                // 没有安装支付宝，跳转到应用市场
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + packageName));
                    startActivity(intent);
                } catch (Exception ee) {// 连应用市场都没有，用浏览器去支付宝官网下载
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(browserUrl));
                        startActivity(intent);
                    } catch (Exception eee) {
                        Toast.makeText(BookInfoActivity.this,
                                "您的手机上没有支付宝，也没有应用市场或者浏览器，请在安装好支付宝后，再进行付款",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return false;
        }

    }

    public void setPhones(String phone){
        this.phones = phone;
    }
}
