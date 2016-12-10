package com.example.lifecircle.Model;

import java.util.HashSet;
import java.util.Set;
/**
 * Tag entity. @author MyEclipse Persistence Tools
 */

public class Tag implements java.io.Serializable {

	// Fields

	private Integer id;
	private Double weight;
	private String name;
	private Set<UserPerference> userPerferences = new HashSet<UserPerference>(0);
	private Set<TaskTag> taskTags = new HashSet<TaskTag>(0);

	// Constructors

	/** default constructor */
	public Tag() {
	}

	/** minimal constructor */
	public Tag(Integer id, Double weight, String name) {
		this.id = id;
		this.weight = weight;
		this.name = name;
	}

	/** full constructor */
	public Tag(Integer id, Double weight, String name,
			Set<UserPerference> userPerferences, Set<TaskTag> taskTags) {
		this.id = id;
		this.weight = weight;
		this.name = name;
		this.userPerferences = userPerferences;
		this.taskTags = taskTags;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UserPerference> getUserPerferences() {
		return this.userPerferences;
	}

	public void setUserPerferences(Set<UserPerference> userPerferences) {
		this.userPerferences = userPerferences;
	}

	public Set<TaskTag> getTaskTags() {
		return this.taskTags;
	}

	public void setTaskTags(Set<TaskTag> taskTags) {
		this.taskTags = taskTags;
	}

}