package com.trukker.trukkershipperuae.model;

public class Order_method {

	private String load_inquiry_no;
	private String SizeTypeDesc;
	private String load_inquiry_shipping_date;
	private String Total_cost;
	private String inquiry_source_addr;
	private String inquiry_destination_addr;
	private String NoOfTruck;
	private String NoOfDriver;
	private String NoOfLabour;
	private String NoOfHandiman;
	private String TotalDistance;
	private String TotalPackingCharge;
	private String extra1;
	private String extra2;
	private String extra3;
	private String extra4;
	private String extra5;
	private String status;

	private String s_lat;
	private String s_lng;
	private String d_lat;
	private String d_lng;

	private String trackurl;
	private String feedback_rating;
	private String feedback_msg;
	private String load_inquiry_shipping_time;
	private String shipper_id;

	public Order_method()
	{
		// TODO Auto-generated constructor stub
	}

	public Order_method(String load_inquiry_no ,String SizeTypeDesc,String load_inquiry_shipping_date,String Total_cost,
	String inquiry_source_addr,String inquiry_destination_addr,String NoOfTruck,String NoOfDriver,String NoOfLabour,String NoOfHandiman,
						String TotalDistance,String TotalPackingCharge,String status,String extra1,String extra2,String extra3,String extra4,String extra5,
						String s_lat,String s_lng,String d_lat,String d_lng,String trackurl,String feedback_rating,String feedback_msg,String load_inquiry_shipping_time,String shipper_id) {
		super();
		this.load_inquiry_no = load_inquiry_no;
		this.SizeTypeDesc = SizeTypeDesc;
		this.load_inquiry_shipping_date = load_inquiry_shipping_date;
		this.Total_cost = Total_cost;
		this.inquiry_source_addr = inquiry_source_addr;
		this.inquiry_destination_addr = inquiry_destination_addr;
		this.NoOfTruck = NoOfTruck;
		this.NoOfDriver = NoOfDriver;
		this.NoOfLabour = NoOfLabour;
		this.NoOfHandiman = NoOfHandiman;
		this.TotalDistance = TotalDistance;
		this.TotalPackingCharge = TotalPackingCharge;
		this.status = status;
		this.extra1 = extra1;
		this.extra2 = extra2;
		this.extra3 = extra3;
		this.extra4 = extra4;
		this.extra5 = extra5;
		this.s_lat = s_lat;
		this.s_lng = s_lng;
		this.d_lat = d_lat;
		this.d_lng = d_lng;

		this.trackurl = trackurl;
		this.feedback_rating = feedback_rating;
		this.feedback_msg = feedback_msg;
		this.load_inquiry_shipping_time = load_inquiry_shipping_time;
		this.shipper_id=shipper_id;

	}

	public String getload_inquiry_shipping_time() {
		return load_inquiry_shipping_time;
	}
	public void setload_inquiry_shipping_time(String load_inquiry_shipping_time) {
		this.load_inquiry_shipping_time = load_inquiry_shipping_time;
	}

	public String getshipper_id() {
		return shipper_id;
	}
	public void setshipper_id(String shipper_id) {
		this.shipper_id = shipper_id;
	}


	public String getfeedback_msg() {
		return feedback_msg;
	}
	public void setfeedback_msg(String feedback_msg) {
		this.feedback_msg = feedback_msg;
	}

	public String getfeedback_rating() {
		return feedback_rating;
	}
	public void setfeedback_rating(String feedback_rating) {
		this.feedback_rating = feedback_rating;
	}

	public String gettrackurl() {
		return trackurl;
	}
	public void settrackurl(String trackurl) {
		this.trackurl = trackurl;
	}


	public String getd_lng() {
		return d_lng;
	}
	public void setd_lng(String d_lng) {
		this.d_lng = d_lng;
	}


	public String gets_lng() {
		return s_lng;
	}
	public void sets_lng(String s_lng) {
		this.s_lng = s_lng;
	}



	public String gets_lat() {
		return s_lat;
	}
	public void sets_lat(String s_lat) {
		this.s_lat = s_lat;
	}


	public String getd_lat() {
		return d_lat;
	}
	public void setd_lat(String d_lat) {
		this.d_lat = d_lat;
	}


	public String getstatus() {
		return status;
	}
	public void setstatus(String status) {
		this.status = status;
	}


	public String getTotalPackingCharge() {
		return TotalPackingCharge;
	}
	public void setTotalPackingCharge(String TotalPackingCharge) {
		this.TotalDistance = TotalPackingCharge;
	}

	public String getTotalDistance() {
		return TotalDistance;
	}
	public void setTotalDistance(String TotalDistance) {
		this.TotalDistance = TotalDistance;
	}

	public String getNoOfHandiman() {
		return NoOfHandiman;
	}
	public void setNoOfHandiman(String NoOfHandiman) {
		this.NoOfHandiman = NoOfHandiman;
	}

	public String getNoOfLabour() {
		return NoOfLabour;
	}
	public void setNoOfLabour(String NoOfLabour) {
		this.NoOfLabour = NoOfLabour;
	}


	public String getNoOfDriver() {
		return NoOfDriver;
	}
	public void setNoOfDriver(String NoOfDriver) {
		this.NoOfDriver = NoOfDriver;
	}

	public String getNoOfTruck() {
		return NoOfTruck;
	}
	public void setNoOfTruck(String NoOfTruck) {
		this.NoOfTruck = NoOfTruck;
	}


	public String getinquiry_destination_addr() {
		return inquiry_destination_addr;
	}
	public void setinquiry_destination_addr(String inquiry_destination_addr) {
		this.inquiry_destination_addr = inquiry_destination_addr;
	}

	public String getinquiry_source_addr() {
		return inquiry_source_addr;
	}
	public void setinquiry_source_addr(String inquiry_source_addr) {
		this.inquiry_source_addr = inquiry_source_addr;
	}

	public String getTotal_cost() {
		return Total_cost;
	}
	public void setTotal_cost(String Total_cost) {
		this.Total_cost = Total_cost;
	}

	public String getload_inquiry_shipping_date() {
		return load_inquiry_shipping_date;
	}
	public void setload_inquiry_shipping_date(String load_inquiry_shipping_date) {
		this.load_inquiry_shipping_date = load_inquiry_shipping_date;
	}


	public String getSizeTypeDesc() {
		return SizeTypeDesc;
	}
	public void setSizeTypeDesc(String SizeTypeDesc) {
		this.SizeTypeDesc = SizeTypeDesc;
	}

	public String getload_inquiry_no() {
		return load_inquiry_no;
	}
	public void setload_inquiry_no(String load_inquiry_no) {
		this.load_inquiry_no = load_inquiry_no;
	}


	public String getextra1() {
		return extra1;
	}
	public void setextra1(String extra1) {
		this.extra1 = extra1;
	}

	public String getextra2() {
		return extra2;
	}
	public void setextra2(String extra2) {
		this.extra2 = extra2;
	}

	public String getextra3() {
		return extra3;
	}
	public void setextra3(String extra3) {
		this.extra3 = extra3;
	}


	public String getextra4() {
		return extra4;
	}
	public void setextra4(String extra4) {
		this.extra4 = extra4;
	}

	public String getextra5() {
		return extra5;
	}
	public void setextra5(String extra5) {
		this.extra5 = extra5;
	}


}
