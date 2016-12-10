package com.example.lifecircle.Model;

/**
 * UserPerference entity. @author MyEclipse Persistence Tools
 */
public class UserPerference implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Tag tag;
	private Double weight;

	// Constructors

	/** default constructor */
	public UserPerference() {
	}

	/** full constructor */
	public UserPerference(Integer id, User user, Tag tag, Double weight) {
		this.id = id;
		this.user = user;
		this.tag = tag;
		this.weight = weight;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

}