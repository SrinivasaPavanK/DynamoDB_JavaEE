package com.springweb.dynamodb.demo.domain;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

/**
 * Composite id for the Thread entity. For spring-data-dynamodb to be able to
 * identify which attribute is the hash key and which is the range key the
 * methods must be annotated with @DynamoDBHashKey or @DynamoDBRangeKey
 * 
 * 
 * @author Pavan KS
 */
public class ThreadId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String forumName;
	private String subject;

	@DynamoDBHashKey
	public String getForumName() {
		return forumName;
	}

	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	@DynamoDBRangeKey
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
