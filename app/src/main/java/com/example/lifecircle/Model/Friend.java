package com.example.lifecircle.Model;


public class Friend implements java.io.Serializable {

	// Fields

	private Integer id;
	private User userByUserId;
	private User userByFriendId;
	private Integer relationReward;

	// Constructors

	/** default constructor */
	public Friend() {
	}

	/** minimal constructor */
	public Friend(Integer id, User userByUserId, User userByFriendId) {
		this.id = id;
		this.userByUserId = userByUserId;
		this.userByFriendId = userByFriendId;
	}

	/** full constructor */
	public Friend(Integer id, User userByUserId, User userByFriendId,
			Integer relationReward) {
		this.id = id;
		this.userByUserId = userByUserId;
		this.userByFriendId = userByFriendId;
		this.relationReward = relationReward;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUserByUserId() {
		return this.userByUserId;
	}

	public void setUserByUserId(User userByUserId) {
		this.userByUserId = userByUserId;
	}

	public User getUserByFriendId() {
		return this.userByFriendId;
	}

	public void setUserByFriendId(User userByFriendId) {
		this.userByFriendId = userByFriendId;
	}

	public Integer getRelationReward() {
		return this.relationReward;
	}

	public void setRelationReward(Integer relationReward) {
		this.relationReward = relationReward;
	}

}