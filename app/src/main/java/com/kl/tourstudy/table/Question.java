package com.kl.tourstudy.table;

public class Question {
	private int question_id;
	private int question_user_id;
	private int visit_count;
	private int answer_count;

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	private int img;
	private String question_title,question_info,question_type,question_date,user_name;

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Question(int question_id, int visit_count, int answer_count, String question_title, String question_info, String question_date, int img, String user_name) {
		this.question_id = question_id;
		this.visit_count = visit_count;
		this.answer_count = answer_count;
		this.question_title = question_title;
		this.question_info = question_info;
		this.question_date = question_date;
		this.img=img;
		this.user_name=user_name;

	}

	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public int getQuestion_user_id() {
		return question_user_id;
	}
	public void setQuestion_user_id(int question_user_id) {
		this.question_user_id = question_user_id;
	}
	public int getVisit_count() {
		return visit_count;
	}
	public void setVisit_count(int visit_count) {
		this.visit_count = visit_count;
	}
	public int getAnswer_count() {
		return answer_count;
	}
	public void setAnswer_count(int answer_count) {
		this.answer_count = answer_count;
	}
	public String getQuestion_title() {
		return question_title;
	}
	public void setQuestion_title(String question_title) {
		this.question_title = question_title;
	}
	public String getQuestion_info() {
		return question_info;
	}
	public void setQuestion_info(String question_info) {
		this.question_info = question_info;
	}
	public String getQuestion_type() {
		return question_type;
	}
	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}
	public String getQuestion_date() {
		return question_date;
	}
	public void setQuestion_date(String question_date) {
		this.question_date = question_date;
	}
	
	
}
