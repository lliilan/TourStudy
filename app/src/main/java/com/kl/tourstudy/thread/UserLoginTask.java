//package com.kl.tourstudy.thread;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.kl.tourstudy.activity.LoginActivity;
//import com.kl.tourstudy.gsonbean.User;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import static android.content.Context.MODE_PRIVATE;
//import static com.kl.tourstudy.util.PreferenceUtil.ERROR;
//import static com.kl.tourstudy.util.PreferenceUtil.IP;
//import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;
//
///**
// * 用户登录线程
// * Created by KL on 2017/5/16 0016.
// */
//
//public class UserLoginTask extends AsyncTask<Void, Void, String> {
//
//    private Context mContext;
//    //用户名，密码
//    private final String mUserName;
//    private final String mPassword;
//    private String data = "";   //储存返回的JSON数据
//    private String servlet = "LoginServletApp";
//    private String url = IP + PROJECT + servlet;
//
//    UserLoginTask(String email, String password, Context context) {
//        mUserName = email;
//        mPassword = password;
//        mContext = context;
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//
//        try {
//            // 这里写网络连接认证服务
//            data = OkHttpUtils.post()
//                    .url(url)
//                    .addParams("userPhone",mUserName)
//                    .addParams("userPwd",mPassword)
//                    .build()
//                    .execute().body().string();
//        } catch (Exception e) {
//            return ERROR;
//        }
//        return data;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
////        mAuthTask = null;
////        showProgress(false);
//        Gson gson = new Gson();
//        User user = gson.fromJson(result, User.class);
//        if (user.getName() == null){
//            Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//        } else {
//            int message1 = user.getStatus();
//            String message2 = user.getName();
//            String icon = user.getIcon();
//
//            switch (message1) {
//                case 1:
//                    //将用户名密码储存到SharedPreferences中
//                    SharedPreferences.Editor editor = mContext.getSharedPreferences("user_info", MODE_PRIVATE).edit();
//                    editor.putString("name", message2);
//                    editor.putString("pwd", user.getPwd());
//                    editor.putInt("userId", user.getId());
//                    editor.putString("icon", user.getIcon());
//                    editor.putInt("LOG_STATUS", 1);
//                    editor.apply();
////                    Intent intent = new Intent();
////                    intent.putExtra("userName", message2);
////                    intent.putExtra("icon", icon);
////                    setResult(RESULT_OK, intent);
////                    finish();
//                    break;
//                case 2:
//                    Toast.makeText(mContext, "管理员请登录网页进行操作", Toast.LENGTH_SHORT).show();
////                    mUserNameView.requestFocus();
//                    break;
//                case 0:
//                    Toast.makeText(mContext, "您的账号处于未激活状态", Toast.LENGTH_SHORT).show();
////                    mUserNameView.requestFocus();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    @Override
//    protected void onCancelled() {
////        mAuthTask = null;
////        showProgress(false);
//    }
//}
