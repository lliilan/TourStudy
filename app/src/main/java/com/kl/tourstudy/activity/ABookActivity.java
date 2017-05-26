package com.kl.tourstudy.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.BookInfo;
import com.kl.tourstudy.gsonbean.BooleanReturn;
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
 * 订单详情页面，用户点击个人信息位置的订单管理可以查看自己的订单
 */
public class ABookActivity extends AppCompatActivity {

    private static final String TAG = "ABookActivity";
    private Button btn_pay;
    private CircleImageView ci_icon;
    private ImageView iv_phone;
    private List<TextView> list_tv = new ArrayList<>();
    private String phones;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abook);

        Intent intentBook = getIntent();
        int bookId = intentBook.getIntExtra("bookId", 0);
        initView();
        ABookTask task = new ABookTask(btn_pay, iv_phone, ci_icon, list_tv);
        task.execute(bookId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    private void initView() {
        btn_pay = (Button) findViewById(R.id.btn_pay);
        ci_icon = (CircleImageView) findViewById(R.id.civ_image);
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
        TextView tv_book_id = (TextView) findViewById(R.id.book_id);
        TextView tv_book_date = (TextView) findViewById(R.id.book_date);
        TextView tv_book_head = (TextView) findViewById(R.id.user_head);
        TextView tv_pay_date = (TextView) findViewById(R.id.pay_date);
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
        list_tv.add(tv_book_date);
        list_tv.add(tv_book_head);
        list_tv.add(tv_pay_date);
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
                    Toast.makeText(ABookActivity.this, "你没有给于授权", Toast.LENGTH_SHORT).show();
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

    public void setPhones(String phone){
        this.phones = phone;
    }

    private class ABookTask extends AsyncTask<Integer, Void, BookInfo> implements View.OnClickListener{

        private Button btnPay;
        private ImageView ivPhone;
        private CircleImageView ciIcon;
        private List<TextView> list;
        private String phone;
        private String tourName;
        private String payDescript = "游学订单支付";  //订单支付的描述
        private float payMoney;
        private float payDeposit;
        private Boolean AliPayOrWechat = true;      //默认使用支付宝付款，暂时该第三方支付不支持微信付款
        private int id;


        ABookTask(Button btnPay, ImageView ivPhone, CircleImageView ciIcon, List<TextView> list){
            this.btnPay = btnPay;
            this.ciIcon = ciIcon;
            this.ivPhone = ivPhone;
            this.list = list;
        }

        @Override
        protected BookInfo doInBackground(Integer... params) {
            BookInfo info = null;
            id = params[0];
            try{
                String servlet = "ABookInfoServletApp";
                String url = IP + PROJECT + servlet;
                String bookInfo = OkHttpUtils.post()
                        .addParams("bookId", String.valueOf(id))
                        .url(url)
                        .build()
                        .execute().body().string();
                Gson gson = new Gson();
                info = gson.fromJson(bookInfo, BookInfo.class);
//                Log.e(TAG, "doInBackground: 获得SERVLET的bookInfo" + bookInfo);
            } catch (Exception e){
                e.printStackTrace();
            }
            return info;
        }

        @Override
        protected void onPostExecute(BookInfo info) {
            super.onPostExecute(info);
            Glide.with(ABookActivity.this).load(IP + info.getImage()).into(ciIcon);

            TextView status = list_tv.get(0);
            switch (info.getBookStatus()){
                case 0:
                    status.setText("订单失效");
                    break;
                case 1:
                    status.setText("下单成功");
                    break;
                case 2:
                    status.setText("订金支付成功");
                    break;
                case 3:
                    status.setText("总金额支付成功");
                    break;
            }

            TextView tv_title = list_tv.get(1);
            tourName = info.getTourName();
            tv_title.setText(tourName);
            tv_title.setOnClickListener(this);
            this.btnPay.setOnClickListener(this);
            ivPhone.setOnClickListener(this);

            payDeposit = info.getDeposit();
            payMoney = info.getMoney();
            list.get(2).setText(getString(R.string.tour_place) + info.getCountry());
            list.get(3).setText(getString(R.string.go_place) + info.getCity());
            list.get(4).setText(getString(R.string.go_date) + info.getGo());
            list.get(5).setText(getString(R.string.tour_day) + info.getDay() + getString(R.string.day));
            list.get(6).setText(getString(R.string.tour_deposit) + payDeposit + getString(R.string.yuan));
            list.get(7).setText(getString(R.string.tour_money) + payMoney + getString(R.string.yuan));
            list.get(8).setText(getString(R.string.tour_manager) + info.getManager() + "  " + info.getManagerPhone());
            phone = info.getManagerPhone();
            setPhones(phone);

            list.get(9).setText(getString(R.string.book_id) + info.getBookId());
            list.get(10).setText(getString(R.string.book_date) + info.getBookDate());
            list.get(11).setText(getString(R.string.book_user) + info.getUserName() + " " + info.getUserPhone());

            String payTime = info.getPayTime();
            if (payTime.equals("1999-01-01")){
                list.get(12).setText(getString(R.string.pay_time) + getString(R.string.pay_not)
                );
            } else {
                list.get(12).setText(getString(R.string.pay_time) + payTime);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_pay:
                    //支付，弹出dialog
                if (!checkPackageInstalled("com.eg.android.AlipayGphone",
                        "https://www.alipay.com")){
                    Toast.makeText(ABookActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT).show();
                } else {
                    builderDialog();
                }
                    break;
                case R.id.iv_phone:
                    //给联系人打电话
                    if (ContextCompat.checkSelfPermission(ABookActivity.this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(ABookActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call();
                    }
                    break;
                case R.id.tv_title:
                    //跳转到路线
                    break;
                default:
                    break;
            }
        }

        private void builderDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ABookActivity.this);
            builder.setTitle("付款");
            builder.setMessage("请选择支付款项");
            builder.setNegativeButton("支付订金", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showProgressDialog("正在获取订单...\nSDK版本号:" + BP.getPaySdkVersion());
                    pay();
                }
            });

            builder.setPositiveButton("支付全款", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(ABookActivity.this, "支付全款功能尚未完成", Toast.LENGTH_SHORT).show();
//                    finish();
                }
            });
            builder.show();
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
                        Toast.makeText(ABookActivity.this,
                                "您的手机上没有支付宝，也没有应用市场或者浏览器，请在安装好支付宝后，再进行付款",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return false;
        }

        private void pay() {
            //支付先使用0.02作为支付金额
            BP.pay(tourName, payDescript, 0.02, AliPayOrWechat, new PListener() {
                @Override
                public void orderId(final String s) {
                    Log.e(TAG, "orderId: orderId=" + s );
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //保存订单号进数据库,以便以后查询
                                String servlet = "BookPayIdServlet";
                                String url = IP + PROJECT + servlet;
                                String info = OkHttpUtils.post()
                                        .addParams("payCode", s)
                                        .addParams("bookId",  id+ "")
                                        .url(url)
                                        .build()
                                        .execute().body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                @Override
                public void succeed() {
//                    Toast.makeText(TourBookActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String servlet = "PayDepositServlet";//更改订单的支付状态
                                String url = IP + PROJECT + servlet;
                                String info = OkHttpUtils.post()
                                        .addParams("id", id + "")
                                        .addParams("status", 2 + "")
                                        .url(url)
                                        .build()
                                        .execute().body().string();
                                Gson gson = new Gson();
                                BooleanReturn br = gson.fromJson(info, BooleanReturn.class);
//                                flagIntent = br.isFlag();
                                Log.e(TAG, "run:info= " + info );
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
                @Override
                public void fail(int code, String reason) {
                    if (code == -2) {
                        Toast.makeText(ABookActivity.this,
                                "支付中断,错误代码:" + code + "原因是:" + reason, Toast.LENGTH_SHORT)
                                .show();
                    }
                    hideProgressDialog();
                }

                @Override
                public void unknow() {
                    Toast.makeText(ABookActivity.this, "支付结果未知，请稍后手动查询", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            });
        }

        private void hideProgressDialog() {
            if (progress != null && progress.isShowing()) {
                try {
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void showProgressDialog(String message) {
            try {
                if (progress == null) {
                    progress = new ProgressDialog(ABookActivity.this);
                    progress.setCancelable(true);
                }
                progress.setMessage(message);
                progress.show();
            } catch (Exception e) {
                //在其他线程调用dialog会报错
            }
        }

    }


}
