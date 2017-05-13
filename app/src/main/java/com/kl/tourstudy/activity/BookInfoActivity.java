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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.BookInfo;
import com.zhy.http.okhttp.OkHttpUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 对动态权限的判断处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                    Log.e(TAG, "onRequestPermissionsResult: phone:" + phones );
                } else {
                    Toast.makeText(BookInfoActivity.this, "你没有给于授权", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
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

    public class BookInfoTask extends AsyncTask<Integer, Void, BookInfo> implements View.OnClickListener {

        private Button btn_pay;
        private CircleImageView image;
        private List<TextView> list_tv = new ArrayList<>();
        private int tourId;
        private String phone;

        public BookInfoTask(Button btn_pay, CircleImageView image, List<TextView> list_tv){
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
            } else if (status == 0){
                tv_status.setText("交易成功");
            }

            TextView tv_title = list_tv.get(1);

            btn_call.setOnClickListener(this);
            this.btn_pay.setOnClickListener(this);
//            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_status);
            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_title);
//            rl.setOnClickListener(this);
            ll.setOnClickListener(this);

            tv_title.setText(bookInfo.getTourName());
            list_tv.get(2).setText("游学地点:" + bookInfo.getCountry());
            list_tv.get(3).setText("出发地点:" + bookInfo.getCity());
            list_tv.get(4).setText("出发时间:" + bookInfo.getGo());
            list_tv.get(5).setText("出行天数:" + bookInfo.getDay() + "天");
            list_tv.get(6).setText("订金:" + bookInfo.getDeposit() + "元");
            list_tv.get(7).setText("总金额:" + bookInfo.getMoney() + "元");
            list_tv.get(8).setText("负责人:" + bookInfo.getManager() + "  " + bookInfo.getManagerPhone());
            list_tv.get(9).setText("订单编号:" + bookInfo.getBookId() + "");
            list_tv.get(10).setText("下单日期:" + bookInfo.getBookDate());
            list_tv.get(11).setText("订单联系人:" + bookInfo.getUserName());
            list_tv.get(12).setText("联系方式" + bookInfo.getUserName());
            tourId = bookInfo.getTourId();
            phone = bookInfo.getManagerPhone();
            setPhones(phone);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //联系负责人按钮
                case R.id.btn_call:
//                    Log.e(TAG, "onClick: btn" );
                    if (ContextCompat.checkSelfPermission(BookInfoActivity.this, Manifest.permission.CALL_PHONE)
                             != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(BookInfoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call();
                    }
                    break;
                case R.id.btn_pay:
                    Log.e(TAG, "onClick: pay" );
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
//                    Log.e(TAG, "onClick: tourId = "+ tourId );
                    intent.putExtra("position", tourId - 1);    //因为得到的是list的值，list从0开始
                    startActivity(intent);
                    break;
            }
        }

    }

    public void setPhones(String phone){
        this.phones = phone;
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
}
