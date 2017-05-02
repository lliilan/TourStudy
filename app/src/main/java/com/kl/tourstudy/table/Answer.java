package com.kl.tourstudy.table;

public class Answer {
	private int answer_id,answer_user_id,comment_count,support_count,question_id,img,support_img,showState;
	private String answer_info,answer_date,user_name;

	public int getSupport_img() {
		return support_img;
	}

	public void setSupport_img(int support_img) {
		this.support_img = support_img;
	}

	public int getShowState() {
		return showState;
	}

	public void setShowState(int showState) {
		this.showState = showState;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Answer(int answer_id, String answer_date, String answer_info, int support_count, int img, int support_img, int showState, String user_name) {
		this.answer_id=answer_id;
		this.answer_date = answer_date;
		this.answer_info = answer_info;
		this.support_count = support_count;
		this.img=img;
		this.support_img=support_img;
		this.showState=showState;
		this.user_name=user_name;


	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public int getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(int answer_id) {
		this.answer_id = answer_id;
	}
	public int getAnswer_user_id() {
		return answer_user_id;
	}
	public void setAnswer_user_id(int answer_user_id) {
		this.answer_user_id = answer_user_id;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getSupport_count() {
		return support_count;
	}
	public void setSupport_count(int support_count) {
		this.support_count = support_count;
	}
	public String getAnswer_info() {
		return answer_info;
	}
	public void setAnswer_info(String answer_info) {
		this.answer_info = answer_info;
	}
	public String getAnswer_date() {
		return answer_date;
	}
	public void setAnswer_date(String answer_date) {
		this.answer_date = answer_date;
	}
	
	
}
