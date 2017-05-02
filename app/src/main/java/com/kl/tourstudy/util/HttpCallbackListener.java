package com.kl.tourstudy.util;

/**
 * Created by Administrator on 2016/12/18.
 */
public interface HttpCallbackListener {
    void onFinish(String response);//服务器成功响应的时候调用该方法

    void onError(Exception e);//网络操作错误的时候调用
}
