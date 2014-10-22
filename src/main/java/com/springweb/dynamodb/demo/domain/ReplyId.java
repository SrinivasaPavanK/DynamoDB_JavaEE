package com.springweb.dynamodb.demo.domain;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

/**
 * Composite id for the Reply entity. For spring-data-dynamodb to be able to
 * identify which attribute is the hash key and which is the range key the
 * methods must be annotated with @DynamoDBHashKey or @DynamoDBRangeKey
 * 
 * 
 * @author Pavan KS
 */
public class ReplyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ThreadId threadId;
	private String replyDateTime;

	@DynamoDBRangeKey
	public String getReplyDateTime() {
		return replyDateTime;
	}

	public void setReplyDateTime(String replyDateTime) {
		this.replyDateTime = replyDateTime;
	}

	@DynamoDBHashKey
	public ThreadId getThreadId() {
		return threadId;
	}

	public void setThreadId(ThreadId id) {
		this.threadId = id;
	}

}
