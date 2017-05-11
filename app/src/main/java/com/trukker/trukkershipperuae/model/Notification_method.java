package com.trukker.trukkershipperuae.model;

public class Notification_method {

	private String name;
	
	private String title;
	private String id;
	private String subtitle;
	private String date;

	public Notification_method()
	{
		// TODO Auto-generated constructor stub
	}

	public Notification_method(String id ,String title,String subtitle,String date) {
		super();
		this.title = title;
		this.id = id;
		this.subtitle = subtitle;
		this.date = date;
	}


	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}


	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		this.title = title;
	}

	public String getsubtitle()
	{
		return subtitle;
	}

	public void setsubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getdate() {
		return date;
	}

	public void setdate(String date) {
		this.date = date;
	}

}
