//package com.kl.tourstudy.thread;
//
//import android.os.Handler;
//import android.os.Looper;
//
//import com.google.gson.Gson;
//
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//
///**
// * Created by KL on 2017/4/11 0011.
// */
//
//public class OkHttpManage implements IHttpManager {
//
//    private static final String TAG = "OkHttpManage";
//    private static OkHttpManage mInstance;
//
//    //默认的请求回调类
//    private final RequestCallBack<String> DEFAULT_RESULT_CALLBACK = new RequestCallBack<String>() {
//        @Override
//        public void onError(Request request, Exception e) {
//
//        }
//
//        @Override
//        public void onResponse(String response) {
//
//        }
//    };
//
//    private OkHttpClient mOkHttpClient;
//    private Handler mDelivery;
//    private Gson mGson;
////    private GetDelegate mGetDelegate = new GetDelegate();
////    private PostDelegate mPostDelegate = new PostDelegate();
////    private DownloadDelegate mDownloadDelegate = new DownloadDelegate();
//
//    private OkHttpManage(){
//        mOkHttpClient = new OkHttpClient();
//        mDelivery = new Handler(Looper.getMainLooper());
//        mGson = new Gson();
//    }
//
//    public static OkHttpManage getInstance(){
//        if (mInstance == null){
//            synchronized (OkHttpManage.class){
//                if (mInstance == null){
//                    mInstance = new OkHttpManage();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//
//    @Override
//    public void getAsyn() {
//
//    }
//
//    @Override
//    public void postAsyn() {
//
//
//    }
//
//    @Override
//    public void displayImage() {
//
//    }
//
//    @Override
//    public void downloadAsyn() {
//
//    }
//}
