package com.trukker.trukkershipperuae.model;

public class Document_method {

	private String name;
	
	private String image;
	private String id;
	public Document_method() 
	{
		// TODO Auto-generated constructor stub
	}

	public Document_method(String id ,String name,  String image) {
		super();
		this.name = name;
		this.id = id;
		this.image = image;

	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
