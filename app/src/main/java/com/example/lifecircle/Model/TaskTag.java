package com.example.lifecircle.Model;

/**
 * TaskTag entity. @author MyEclipse Persistence Tools
 */
public class TaskTag implements java.io.Serializable {

	// Fields

	private Integer id;
	private Tag tag;
	private Task task;

	// Constructors

	/** default constructor */
	public TaskTag() {
	}

	/** full constructor */
	public TaskTag(Integer id, Tag tag, Task task) {
		this.id = id;
		this.tag = tag;
		this.task = task;
	}

	// Property accessors
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}