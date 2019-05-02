package com.clover.entity;

public class Contact {

	private String contactName;
	
	private String contactNumber;

	private String contactPhone;
	
	private String contactEmail;
	
	private String contactRoom;

	public Contact(String contactName, String contactNumber,
			String contactPhone, String contactEmail, String contactRoom) {
		super();
		this.contactName = contactName;
		this.contactNumber = contactNumber;
		this.contactPhone = contactPhone;
		this.contactEmail = contactEmail;
		this.contactRoom = contactRoom;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactRoom() {
		return contactRoom;
	}

	public void setContactRoom(String contactRoom) {
		this.contactRoom = contactRoom;
	}
	
}
