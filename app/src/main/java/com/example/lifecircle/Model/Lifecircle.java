package com.example.lifecircle.Model;

import java.util.HashSet;
import java.util.Set;

public class Lifecircle implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String info;

	// Constructors

	/** default constructor */
	public Lifecircle() {
	}

	/** minimal constructor */
	public Lifecircle(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public Lifecircle(Integer id, String name, String info, Set<Member> members) {
		this.id = id;
		this.name = name;
		this.info = info;
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

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}