package com.kl.tourstudy.gsonbean;

import java.util.Date;

/**
 * 订单信息--用于将服务器返回的json数据用GSON解析
 * Created by KL on 2017/5/8 0008.
 */

public class BookInfo {
    private boolean flag;
    private int tourId;
    private int userId;
    private int bookId;
    private Date bookDate;
    private String tourName;
    private String country;
    private String city;
    private String age;
    private int money;
    private int deposit;
    private Date go;
    private int day;
    private int bookStatus;


}
