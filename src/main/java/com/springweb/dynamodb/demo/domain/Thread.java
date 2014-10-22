package com.springweb.dynamodb.demo.domain;

import java.util.Set;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Thread")
/**
 * This is a clone of the Thread class in the AmazonDB examples at http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/JavaQueryScanORMModelExample.html
 * 
 * The Thread table has a composite primary key ( hash and range).  For spring-data-dynamodb to support datatypes with composite keys, the composite keys must be encapuslated
 * in a separate class, resulting in a single primary key attribute annotated with @Id.
 * 
 * This class has therefore been modified slightly - the forumName and subject attributes were properties of this class, but they are now part of the ThreadId composite id class
 * which is referenced.  Setters and getters must be still provided for the Amazon databinding to function - these are now delegator methods for the attributes of the referenced composite id.
 * 
 * @author Pavan KS
 */
public class Thread {

	private String message;
	private String lastPostedDateTime;
	private String lastPostedBy;
	private Set<String> tags;
	private int answered;
	private int views;
	private int replies;

	@Id
	private ThreadId threadId;

	@DynamoDBHashKey(attributeName = "ForumName")
	public String getForumName() {
		return threadId != null ? threadId.getForumName() : null;
	}

	public void setForumName(String forumName) {
		if (threadId == null) {
			threadId = new ThreadId();
		}
		this.threadId.setForumName(forumName);
	}

	@DynamoDBRangeKey(attributeName = "Subject")
	public String getSubject() {
		return threadId != null ? threadId.getSubject() : null;
	}

	public void setSubject(String subject) {
		if (threadId == null) {
			threadId = new ThreadId();
		}
		this.threadId.setSubject(subject);

	}

	@DynamoDBIndexRangeKey(attributeName = "Message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@DynamoDBAttribute(attributeName = "LastPostedDateTime")
	public String getLastPostedDateTime() {
		return lastPostedDateTime;
	}

	public void setLastPostedDateTime(String lastPostedDateTime) {
		this.lastPostedDateTime = lastPostedDateTime;
	}

	@DynamoDBAttribute(attributeName = "LastPostedBy")
	public String getLastPostedBy() {
		return lastPostedBy;
	}

	public void setLastPostedBy(String lastPostedBy) {
		this.lastPostedBy = lastPostedBy;
	}

	@DynamoDBAttribute(attributeName = "Tags")
	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@DynamoDBAttribute(attributeName = "Answered")
	public int getAnswered() {
		return answered;
	}

	public void setAnswered(int answered) {
		this.answered = answered;
	}

	@DynamoDBAttribute(attributeName = "Views")
	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	@DynamoDBAttribute(attributeName = "Replies")
	public int getReplies() {
		return replies;
	}

	public void setReplies(int replies) {
		this.replies = replies;
	}

	public ThreadId getThreadId() {
		return threadId;
	}

	public void setThreadId(ThreadId threadId) {
		this.threadId = threadId;
	}

}
