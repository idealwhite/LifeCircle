package com.example.lifecircle.Model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Task entity. @author MyEclipse Persistence Tools
 */
public class Task implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Member member;
	private String audioPath;
	private String content;
	private Integer reward;
	private Timestamp time;
	private String picturePath;
	private Integer state;
	private Set<TaskTag> taskTags = new HashSet<TaskTag>(0);

	// Constructors

	/** default constructor */
	public Task() {
	}

	/** minimal constructor */
	public Task(Integer id, Member member, Integer reward, Timestamp time,
			Integer state) {
		this.id = id;
		this.member = member;
		this.reward = reward;
		this.time = time;
		this.state = state;
	}

	/** full constructor */
	public Task(Integer id, User user, Member member, String audioPath,
			String content, Integer reward, Timestamp time, String picturePath,
			Integer state, Set<TaskTag> taskTags) {
		this.id = id;
		this.user = user;
		this.member = member;
		this.audioPath = audioPath;
		this.content = content;
		this.reward = reward;
		this.time = time;
		this.picturePath = picturePath;
		this.state = state;
		this.taskTags = taskTags;
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

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getAudioPath() {
		return this.audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getReward() {
		return this.reward;
	}

	public void setReward(Integer reward) {
		this.reward = reward;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getPicturePath() {
		return this.picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Set<TaskTag> getTaskTags() {
		return this.taskTags;
	}

	public void setTaskTags(Set<TaskTag> taskTags) {
		this.taskTags = taskTags;
	}

}