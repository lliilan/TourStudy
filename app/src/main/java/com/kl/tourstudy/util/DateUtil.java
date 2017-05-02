package com.kl.tourstudy.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Calendar c;
	public static int year,month,day,hour,minute,second;

	public static String getDate() {
		c=Calendar.getInstance();
		year=c.get(Calendar.YEAR);
		month=c.get(Calendar.MONTH);
		day=c.get(Calendar.DATE);
		hour= c.get(Calendar.HOUR_OF_DAY);
		minute=c.get(Calendar.MINUTE);
		second=c.get(Calendar.SECOND);
		month=month+1;
		return year+"-"+editTime(month)+"-"+editTime(day)+" "+editTime(hour)+":"+editTime(minute)+":"+editTime(second);
	}

	public static String editTime(int n){
		String str=String .valueOf(n);
		if(String.valueOf(n).length()==1){
			str=0+String.valueOf(n);
		}
		return str;
	}

	public static String compareDate(String now,String before){
		long n=changeDate(now);
		long b=changeDate(before);
		long result=(n-b)/1000;
		long resultYear=result/3600/24/365;//和年做比较
		long resultMonth=result/3600/24/30;//和月做比较
		long resultDay=result/3600/24;//和天数做比较
		long resultHour=result/3600;//和小时做比较
		long resultMinute=result/60;//和分钟做比较
		if(resultYear>0){
			System.out.println(resultYear+"年前");
			return resultYear+"年前";
		}else if(resultMonth>0){
			System.out.println(resultMonth+"月前");
			return resultMonth+"月前";
		}else if(resultDay>0){
			System.out.println(resultDay+"天前");
			return resultDay+"天前";
		}else if(resultHour>0){
			System.out.println(resultHour+"小时前");
			return resultHour+"小时前";
		}else if(resultMinute>0){
			System.out.println(resultMinute+"分钟前");
			return resultMinute+"分钟前";
		}else {
			System.out.println("刚刚");
			return "刚刚";
		}
	}

	//把字符串形式的时间改为毫秒，方便比较
	public static long changeDate(String date){
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//字符串转换器
		try {
			Date d=df.parse(date);
			long m=d.getTime();
			return m;
		} catch (ParseException e) {
			e.printStackTrace();
			return -111111;
		}
	}

}
