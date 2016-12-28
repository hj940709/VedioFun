package com.hj.vediofun;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTime {
	private String Date;
	private String DateTime;
	private int year;
	private int month;
	private int day;
	private Date date;
	@SuppressLint("SimpleDateFormat")
	public DateTime(){
		this.date = new Date();
	    this.DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	    this.Date = new SimpleDateFormat("yyyy-MM-dd").format(date);
	    Calendar calendar=Calendar.getInstance(Locale.CHINA);
	    calendar.setTime(date);
	    this.year=calendar.get(Calendar.YEAR); //获取Calendar对象中的年
	    this.month=calendar.get(Calendar.MONTH);//获取Calendar对象中的月
	    this.day=calendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
	}
	public String getDate() {
		return Date;
	}
	public String getDateTime() {
		return DateTime;
	}
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
}
