package com.example.lifecircle.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * Member entity. @author MyEclipse Persistence Tools
 */

public class Member implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Lifecircle lifecircle;
	private Credit credit;
	private Integer reward;
	private String name;

	// Constructors

	/** default constructor */
	public Member() {
	}

	/** minimal constructor */
	public Member(Integer id, User user, Lifecircle lifecircle, Credit credit,
			Integer reward, String name) {
		this.id = id;
		this.user = user;
		this.lifecircle = lifecircle;
		this.credit = credit;
		this.reward = reward;
		this.name = name;
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

	public Lifecircle getLifecircle() {
		return this.lifecircle;
	}

	public void setLifecircle(Lifecircle lifecircle) {
		this.lifecircle = lifecircle;
	}

	public Credit getCredit() {
		return this.credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public Integer getReward() {
		return this.reward;
	}

	public void setReward(Integer reward) {
		this.reward = reward;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


}