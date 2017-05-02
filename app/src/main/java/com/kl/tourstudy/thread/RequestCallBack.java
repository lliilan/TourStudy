//package com.kl.tourstudy.thread;
//
//import com.google.gson.internal.$Gson$Types;
//
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//
//import okhttp3.Request;
//
///**
// * 抽象类，用于请求成功后的回调
// * Created by KL on 2017/4/15 0015.
// */
//
//public abstract class RequestCallBack<T> {
//    //这是请求数据的返回类型，包含常见的（Bean，List等）
//    Type mType;
//
//    public RequestCallBack(){
//        mType = getSuperclassTypeParameter(getClass());
//    }
//
//    /**
//     * 通过反射想要的返回类型
//     *
//     * @param subclass
//     * @return
//     */
//    static Type getSuperclassTypeParameter(Class<?> subclass) {
//        Type superclass = subclass.getGenericSuperclass();
//        if (superclass instanceof Class) {
//            throw new RuntimeException("Missing type parameter.");
//        }
//        ParameterizedType parameterized = (ParameterizedType) superclass;
//        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
//    }
//
//
//    /**
//     * 在请求之前的方法，一般用于加载框展示
//     */
//    public void onBefore(Request request){
//    }
//
//    /**
//     * 在请求之后的方法，一般用于加载框隐藏
//     */
//    public void onAfter(){
//    }
//
//    /**
//     * 请求失败的时候
//     */
//    public abstract void onError(Request request, Exception e);
//
//    /**
//     * 成功获得请求回报的时候
//     */
//    public abstract void onResponse(T response);
//
//}
