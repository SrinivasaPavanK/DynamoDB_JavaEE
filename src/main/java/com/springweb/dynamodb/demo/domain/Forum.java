package com.springweb.dynamodb.demo.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * This is a clone of the Forum class in the AmazonDB examples at
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/
 * JavaQueryScanORMModelExample.html
 * 
 * @author Pavan KS
 */
@DynamoDBTable(tableName = "Forum")
public class Forum {
	private String name;
	private String category;
	private int threads;

	@DynamoDBHashKey(attributeName = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DynamoDBAttribute(attributeName = "Category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@DynamoDBAttribute(attributeName = "Threads")
	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}
}
