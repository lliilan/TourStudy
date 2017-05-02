package com.kl.tourstudy.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.kl.tourstudy.R;
import com.zhy.http.okhttp.OkHttpUtils;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

public class RegActivity extends AppCompatActivity {

    private EditText et_name,et_password,et_password2,et_phone,et_email;
    private EditText et_sex,et_city;
    private String name,pwd,pwd2,phone,email;
    private String sex,city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initView();
        getRegText();
        RegTask regTask = new RegTask();
        regTask.execute(name, pwd, pwd2, phone, email, sex, city);
    }

    private void getRegText() {
        name = et_name.getText().toString();
        pwd = et_password.getText().toString();
        pwd2 = et_password2.getText().toString();
        phone = et_phone.getText().toString();
        email = et_email.getText().toString();
        sex = et_sex.getText().toString();
        city = et_city.getText().toString();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.user_name);
        et_password = (EditText) findViewById(R.id.password);
        et_password2 = (EditText) findViewById(R.id.password2);
        et_phone = (EditText) findViewById(R.id.phone);
        et_email = (EditText) findViewById(R.id.email);
        et_sex = (EditText) findViewById(R.id.sex);
        et_city = (EditText) findViewById(R.id.city);
    }

    public class RegTask extends AsyncTask<String, Void, String>{

        private String servlet = "RegServletApp";
        private String url = IP + PROJECT + servlet;
        private String message = null;

        @Override
        protected String doInBackground(String... params) {
            try{
                message = OkHttpUtils.post()
                        .url(url)
                        .addParams("userName", params[0])
                        .addParams("userPwd", params[1])
                        .addParams("userPwd2", params[2])
                        .addParams("userPhone", params[3])
                        .addParams("userEmail", params[4])
                        .addParams("userSex", params[5])
                        .addParams("userCity", params[6])
                        .build()
                        .execute().body().string();
            } catch (Exception e){
                e.printStackTrace();
            }
            return message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
