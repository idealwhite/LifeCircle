package com.example.lifecircle.Model;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
public class User implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private long phone;
	private String account;
	private String passwd;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String name, String account, String passwd) {
		this.name = name;
		this.account = account;
		this.passwd = passwd;
	}

	/** full constructor */
	public User(String name, long phone, String account, String passwd) {
		this.name = name;
		this.phone = phone;
		this.account = account;
		this.passwd = passwd;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPhone() {
		return this.phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}