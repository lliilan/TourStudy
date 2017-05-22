package com.kl.tourstudy.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kl.tourstudy.R;
import com.kl.tourstudy.gsonbean.User;
import com.zhy.http.okhttp.OkHttpUtils;

import static com.kl.tourstudy.util.PreferenceUtil.ERROR;
import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener,OnClickListener {

    private static final String TAG = "LoginActivity";
    //跟踪登录任务，以确保我们可以根据需要取消登录。
    private UserLoginTask mAuthTask = null;

    //用户名，使用了自动提示TextView,需要设置adapter
    private AutoCompleteTextView mUserNameView;
    //密码
    private EditText mPasswordView;
    //登录过程中显示的进度条
    private View mProgressView;
    //登录界面，当进行尝试登录的时候，该界面隐藏并显示登录进度条
    private View mLoginFormView;
    //登录按钮
    private Button mUserNameSignInButton;
    private TextView mRegTextView;
    //用于设置焦点View
    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initViewListener();
    }

    private void initViewListener() {
        mUserNameView.setOnEditorActionListener(this);
        mPasswordView.setOnEditorActionListener(this);
        mUserNameSignInButton.setOnClickListener(this);
        mRegTextView.setOnClickListener(this);
    }

    private void initView() {
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.user_name);
        mPasswordView = (EditText) findViewById(R.id.password);
        mUserNameSignInButton = (Button) findViewById(R.id.sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mRegTextView = (TextView) findViewById(R.id.reg);
    }

    private void changeFocusToPassword() {
        focusView = mPasswordView;
        focusView.requestFocus();
    }

    /**
     * 尝试根据登录表单进行登录或者注册，如果出现表单错误，将不进行登录尝试
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // 清空EditText之前产生的错误提示
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // 储存登录时用户输入的用户名与密码
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        //用于判断是否取消登录的标记
        boolean cancel = false;

        // 进行密码检查
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // 检查用户名是否输入，检查用户名是否有问题
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // 如果通过前面的检测将cancel标记设置为true了，那就将焦点设置在出错的输入框内
            focusView.requestFocus();
        } else {
            // 如果cancel标记为false,这显示进度条并在后台进行登录连接
            showProgress(true);
            mAuthTask = new UserLoginTask(userName, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * 显示登录进度条并且隐藏登录信息的录入界面
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()){
            case R.id.user_name:
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    changeFocusToPassword();
                    return true;
                }
                break;
            case R.id.password:
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    attemptLogin();
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                attemptLogin();
                break;
            case R.id.reg:
                startActivity(new Intent(LoginActivity.this, RegActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 用户异步登录任务类
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        //用户名，密码
        private final String mUserName;
        private final String mPassword;
        private String data = "";   //储存返回的JSON数据
        private String servlet = "LoginServletApp";
        private String url = IP + PROJECT + servlet;

        UserLoginTask(String email, String password) {
            mUserName = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                // 这里写网络连接认证服务
                data = OkHttpUtils.post()
                        .url(url)
                        .addParams("userPhone",mUserName)
                        .addParams("userPwd",mPassword)
                        .build()
                        .execute().body().string();
            } catch (Exception e) {
                return ERROR;
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            mAuthTask = null;
            showProgress(false);
            Gson gson = new Gson();
            User user = gson.fromJson(result, User.class);
            if (user.getName() == null){
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            } else {
                int message1 = user.getStatus();
                String message2 = user.getName();
                String icon = user.getIcon();

                switch (message1) {
                    case 1:
                        //将用户名密码储存到SharedPreferences中
                        SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                        editor.putString("name", message2);
                        editor.putString("pwd", user.getPwd());
                        editor.putInt("userId", user.getId());
                        editor.putString("icon", user.getIcon());
                        editor.putInt("LOG_STATUS", 1);
                        editor.apply();
                        Intent intent = new Intent();
                        intent.putExtra("userName", message2);
                        intent.putExtra("icon", icon);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this, "管理员请登录网页进行操作", Toast.LENGTH_SHORT).show();
                        mUserNameView.requestFocus();
                        break;
                    case 0:
                        Toast.makeText(LoginActivity.this, "您的账号处于未激活状态", Toast.LENGTH_SHORT).show();
                        mUserNameView.requestFocus();
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
