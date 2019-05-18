package com.clover.entity;

public class History {
	
	private String number; //风机编号
	
	private String status; //风机运行状态
	
	private String contact; //负责人
	
	private String time; //时间

	public History(String number, String status, String contact, String time) {
		super();
		this.number = number;
		this.status = status;
		this.contact = contact;
		this.time = time;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
