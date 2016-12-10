package com.example.lifecircle.Model;

import java.util.HashSet;
import java.util.Set;

public class Credit implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer reward;
	private String creditName;

	// Constructors

	/** default constructor */
	public Credit() {
	}

	/** minimal constructor */
	public Credit(Integer id, Integer reward, String creditName) {
		this.id = id;
		this.reward = reward;
		this.creditName = creditName;
	}

	/** full constructor */
	public Credit(Integer id, Integer reward, String creditName,
			Set<Member> members) {
		this.id = id;
		this.reward = reward;
		this.creditName = creditName;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReward() {
		return this.reward;
	}

	public void setReward(Integer reward) {
		this.reward = reward;
	}

	public String getCreditName() {
		return this.creditName;
	}

	public void setCreditName(String creditName) {
		this.creditName = creditName;
	}


}