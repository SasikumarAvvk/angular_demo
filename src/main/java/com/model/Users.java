package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Users {
	
	@Id@GeneratedValue
	private int Id;
	private String Name;
	private String gender;
	private String country;
	private String emailId;
	private String phonenumber;
	private String password;
	
	public Users(){
		super();
	}
	
	public Users(int Id, String Name, String gender, String country, String emailId,String phonenumber, String password) {
		super();
		this.Id = Id;
		this.Name = Name;
		this.gender = gender;
		this.country = country;
		this.emailId = emailId;
		this.phonenumber =phonenumber;
		this.password = password;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return Id;
	}

	@Override
	public String toString() {
		return "Users [Id=" + Id + ", Name=" + Name + ", gender=" + gender + ", country=" + country + ", emailId="
				+ emailId + ", phonenumber=" + phonenumber + ", password=" + password + "]";
	}
	
	
	

}
