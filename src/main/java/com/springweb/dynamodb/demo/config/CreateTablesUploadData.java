package com.springweb.dynamodb.demo.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;

/**
 * To create the tables and upload the data to DynamoDB
 * 
 * @author Pavan KS
 *
 */
public class CreateTablesUploadData {

	static Properties prop = new Properties();
	static String propFileName = "environment.properties";
	static AmazonDynamoDB client;
	static InputStream inputStream;
	static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	static String productCatalogTableName = "ProductCatalog";
	static String forumTableName = "Forum";
	static String threadTableName = "Thread";
	static String replyTableName = "Reply";

	public static void main(String[] args) throws Exception {

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = cl.getResourceAsStream(propFileName);
		prop.load(is);
		if (is == null) {
			throw new FileNotFoundException("property file '" + propFileName
					+ "' not found in the classpath");
		}
		AWSCredentials credentials = new BasicAWSCredentials(
				prop.getProperty("amazon.aws.accesskey"),
				prop.getProperty("amazon.aws.secretkey"));
		client = new AmazonDynamoDBClient(credentials);
		client.setEndpoint(prop.getProperty("amazon.dynamodb.endpoint"));

		try {

			deleteTable(productCatalogTableName);
			deleteTable(forumTableName);
			deleteTable(threadTableName);
			deleteTable(replyTableName);

			waitForTableToBeDeleted(productCatalogTableName);
			waitForTableToBeDeleted(forumTableName);
			waitForTableToBeDeleted(threadTableName);
			waitForTableToBeDeleted(replyTableName);

			// Parameter1: table name
			// Parameter2: reads per second
			// Parameter3: writes per second
			// Parameter4/5: hash key and type
			// Parameter6/7: range key and type (if applicable)

			createTable(productCatalogTableName, 10L, 5L, "Id", "N");
			createTable(forumTableName, 10L, 5L, "Name", "S");
			createTable(threadTableName, 10L, 5L, "ForumName", "S", "Subject",
					"S");
			createTable(replyTableName, 10L, 5L, "Id", "S", "ReplyDateTime",
					"S");

			waitForTableToBecomeAvailable(productCatalogTableName);
			waitForTableToBecomeAvailable(forumTableName);
			waitForTableToBecomeAvailable(threadTableName);
			waitForTableToBecomeAvailable(replyTableName);

			uploadSampleProducts(productCatalogTableName);
			uploadSampleForums(forumTableName);
			uploadSampleThreads(threadTableName);
			uploadSampleReplies(replyTableName);

		} catch (AmazonServiceException ase) {
			System.err.println("Data load script failed: " + ase);
			ase.printStackTrace();
		}
	}

	private static void deleteTable(String tableName) {
		try {

			DeleteTableRequest request = new DeleteTableRequest()
					.withTableName(tableName);

			client.deleteTable(request);

		} catch (AmazonServiceException ase) {
			System.err.println("Failed to delete table " + tableName + " "
					+ ase);
		}

	}

	private static void createTable(String tableName, long readCapacityUnits,
			long writeCapacityUnits, String hashKeyName, String hashKeyType) {

		createTable(tableName, readCapacityUnits, writeCapacityUnits,
				hashKeyName, hashKeyType, null, null);
	}

	private static void createTable(String tableName, long readCapacityUnits,
			long writeCapacityUnits, String hashKeyName, String hashKeyType,
			String rangeKeyName, String rangeKeyType) {

		try {
			System.out.println("Creating table " + tableName);
			ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();

			ks.add(new KeySchemaElement().withAttributeName(hashKeyName)
					.withKeyType(KeyType.HASH));
			attributeDefinitions.add(new AttributeDefinition()
					.withAttributeName(hashKeyName).withAttributeType(
							hashKeyType));

			if (rangeKeyName != null) {
				ks.add(new KeySchemaElement().withAttributeName(rangeKeyName)
						.withKeyType(KeyType.RANGE));
				attributeDefinitions.add(new AttributeDefinition()
						.withAttributeName(rangeKeyName).withAttributeType(
								rangeKeyType));
			}

			// Provide initial provisioned throughput values as Java long data
			// types
			ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput()
					.withReadCapacityUnits(readCapacityUnits)
					.withWriteCapacityUnits(writeCapacityUnits);

			CreateTableRequest request = new CreateTableRequest()
					.withTableName(tableName).withKeySchema(ks)
					.withProvisionedThroughput(provisionedthroughput);

			// If this is the Reply table, define a local secondary index
			if (replyTableName.equals(tableName)) {
				attributeDefinitions.add(new AttributeDefinition()
						.withAttributeName("PostedBy").withAttributeType("S"));

				ArrayList<KeySchemaElement> iks = new ArrayList<KeySchemaElement>();
				iks.add(new KeySchemaElement().withAttributeName(hashKeyName)
						.withKeyType(KeyType.HASH));
				iks.add(new KeySchemaElement().withAttributeName("PostedBy")
						.withKeyType(KeyType.RANGE));

				LocalSecondaryIndex lsi = new LocalSecondaryIndex()
						.withIndexName("PostedBy-Index")
						.withKeySchema(iks)
						.withProjection(
								new Projection()
										.withProjectionType(ProjectionType.KEYS_ONLY));

				ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
				localSecondaryIndexes.add(lsi);

				request.setLocalSecondaryIndexes(localSecondaryIndexes);
			}

			request.setAttributeDefinitions(attributeDefinitions);

			client.createTable(request);

		} catch (AmazonServiceException ase) {
			System.err.println("Failed to create table " + tableName + " "
					+ ase);
		}
	}

	private static void uploadSampleProducts(String tableName) {

		try {
			// Add books.
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			item.put("Id", new AttributeValue().withN("101"));
			item.put("Title", new AttributeValue().withS("Book 101 Title"));
			item.put("ISBN", new AttributeValue().withS("111-1111111111"));
			item.put("Authors",
					new AttributeValue().withSS(Arrays.asList("Author1")));
			item.put("Price", new AttributeValue().withN("2"));
			item.put("Dimensions",
					new AttributeValue().withS("8.5 x 11.0 x 0.5"));
			item.put("PageCount", new AttributeValue().withN("500"));
			// item.put("InPublication", new AttributeValue().withBOOL(true));
			item.put("ProductCategory", new AttributeValue().withS("Book"));

			PutItemRequest itemRequest = new PutItemRequest().withTableName(
					tableName).withItem(item);
			client.putItem(itemRequest);
			item.clear();

			item.put("Id", new AttributeValue().withN("102"));
			item.put("Title", new AttributeValue().withS("Book 102 Title"));
			item.put("ISBN", new AttributeValue().withS("222-2222222222"));
			item.put("Authors", new AttributeValue().withSS(Arrays.asList(
					"Author1", "Author2")));
			item.put("Price", new AttributeValue().withN("20"));
			item.put("Dimensions",
					new AttributeValue().withS("8.5 x 11.0 x 0.8"));
			item.put("PageCount", new AttributeValue().withN("600"));
			// item.put("InPublication", new AttributeValue().withBOOL(true));
			item.put("ProductCategory", new AttributeValue().withS("Book"));

			itemRequest = new PutItemRequest().withTableName(tableName)
					.withItem(item);
			client.putItem(itemRequest);
			item.clear();

			item.put("Id", new AttributeValue().withN("103"));
			item.put("Title", new AttributeValue().withS("Book 103 Title"));
			item.put("ISBN", new AttributeValue().withS("333-3333333333"));
			item.put("Authors", new AttributeValue().withSS(Arrays.asList(
					"Author1", "Author2")));
			// Intentional. Later we run scan to find price error. Find items >
			// 1000 in price.
			item.put("Price", new AttributeValue().withN("2000"));
			item.put("Dimensions",
					new AttributeValue().withS("8.5 x 11.0 x 1.5"));
			item.put("PageCount", new AttributeValue().withN("600"));
			// item.put("InPublication", new AttributeValue().withBOOL(false));
			item.put("ProductCategory", new AttributeValue().withS("Book"));

			itemRequest = new PutItemRequest().withTableName(tableName)
					.withItem(item);
			client.putItem(itemRequest);
			item.clear();

			// Add bikes.
			item.put("Id", new AttributeValue().withN("201"));
			item.put("Title", new AttributeValue().withS("18-Bike-201")); // Size,
																			// followed
																			// by
																			// some
																			// title.
			item.put("Description",
					new AttributeValue().withS("201 Description"));
			item.put("BicycleType", new AttributeValue().withS("Road"));
			item.put("Brand", new AttributeValue().withS("Mountain A")); // Trek,
																			// Specialized.
			item.put("Price", new AttributeValue().withN("100"));
			item.put("Gender", new AttributeValue().withS("M")); // Men's
			item.put("Color",
					new AttributeValue().withSS(Arrays.asList("Red", "Black")));
			item.put("ProductCategory", new AttributeValue().withS("Bicycle"));

			itemRequest = new PutItemRequest().withTableName(tableName)
					.withItem(item);
			client.putItem(itemRequest);
			item.clear();

			item.put("Id", new AttributeValue().withN("202"));
			item.put("Title", new AttributeValue().withS("21-Bike-202"));
			item.put("Description",
					new AttributeValue().withS("202 Description"));
			item.put("BicycleType", new AttributeValue().withS("Road"));
			item.put("Brand", new AttributeValue().withS("Brand-Company A"));
			item.put("Price", new AttributeValue().withN("200"));
			item.put("Gender", new AttributeValue().withS("M"));
			item.put("Color", new AttributeValue().withSS(Arrays.asList(
					"Green", "Black")));
			item.put("ProductCategory", new AttributeValue().withS("Bicycle"));

			itemRequest = new PutItemRequest().withTableName(tableName)
					.withItem(item);
			client.putItem(itemRequest);
			item.clear();

			item.put("Id", new AttributeValue().withN("203"));
			item.put("Title", new AttributeValue().withS("19-Bike-203"));
			item.put("Description",
					new AttributeValue().withS("203 Description"));
			item.put("BicycleType", new AttributeValue().withS("Road"));
			item.put("Brand", new AttributeValue().withS("Brand-Company B"));
			item.put("Price", new AttributeValue().withN("300"));
			item.put("Gender", new AttributeValue().withS("W")); // Women's
			item.put("Color", new AttributeValue().withSS(Arrays.asList("Red",
					"Green", "Black")));
			item.put("ProductCategory", new AttributeValue().withS("Bicycle"));

			itemRequest = new PutItemRequest().withTableName(tableName)
					.withItem(item);
			client.putItem(itemRequest);
			item.clear();

			item.put("Id", new AttributeValue().withN("204"));
			item.put("Title", new AttributeValue().withS("18-Bike-204"));
			item.put("Description",
					new AttributeValue().withS("204 Description"));
			item.put("BicycleType", new AttributeValue().withS("Mountain"));
			item.put("Brand", new AttributeValue().withS("Brand-Company B"));
			item.put("Price", new AttributeValue().withN("400"));
			item.put("Gender", new AttributeValue().withS("W"));
			item.put("Color", new AttributeValue().withSS(Arrays.asList("Red")));
			item.put("ProductCategory", new AttributeValue().withS("Bicycle"));

			itemRequest = new PutItemRequest().withTableName(tableName)
					.withItem(item);
			client.putItem(itemRequest);
			item.clear();

			item.put("Id", new AttributeValue().withN("205"));
			item.put("Title", new AttributeValue().withS("20-Bike-205"));
			item.put("Description",
					new AttributeValue().withS("205 Description"));
			item.put("BicycleType", new AttributeValue().withS("Hybrid"));
			item.put("Brand", new AttributeValue().withS("Brand-Company C"));
			item.put("Price", new AttributeValue().withN("500"));
			item.put("Gender", new AttributeValue().withS("B")); // Boy's
			item.put("Color",
					new AttributeValue().withSS(Arrays.asList("Red", "Black")));
			item.put("ProductCategory", new AttributeValue().withS("Bicycle"));

			itemRequest = new PutItemRequest().withTableName(tableName)
					.withItem(item);
			client.putItem(itemRequest);

		} catch (AmazonServiceException ase) {
			System.err.println("Failed to create item in " + tableName);
		}

	}

	private static void uploadSampleForums(String tableName) {
		try {
			// Add forums.
			Map<String, AttributeValue> forum = new HashMap<String, AttributeValue>();
			forum.put("Name", new AttributeValue().withS("Amazon DynamoDB"));
			forum.put("Category",
					new AttributeValue().withS("Amazon Web Services"));
			forum.put("Threads", new AttributeValue().withN("2"));
			forum.put("Messages", new AttributeValue().withN("4"));
			forum.put("Views", new AttributeValue().withN("1000"));

			PutItemRequest forumRequest = new PutItemRequest().withTableName(
					tableName).withItem(forum);
			client.putItem(forumRequest);
			forum.clear();

			forum.put("Name", new AttributeValue().withS("Amazon S3"));
			forum.put("Category",
					new AttributeValue().withS("Amazon Web Services"));
			forum.put("Threads", new AttributeValue().withN("0"));

			forumRequest = new PutItemRequest().withTableName(tableName)
					.withItem(forum);
			client.putItem(forumRequest);

		} catch (AmazonServiceException ase) {
			System.err.println("Failed to create item in " + tableName);
		}
	}

	private static void uploadSampleThreads(String tableName) {
		try {
			long time1 = (new Date()).getTime() - (7 * 24 * 60 * 60 * 1000); // 7
																				// days
																				// ago
			long time2 = (new Date()).getTime() - (14 * 24 * 60 * 60 * 1000); // 14
																				// days
																				// ago
			long time3 = (new Date()).getTime() - (21 * 24 * 60 * 60 * 1000); // 21
																				// days
																				// ago

			Date date1 = new Date();
			date1.setTime(time1);

			Date date2 = new Date();
			date2.setTime(time2);

			Date date3 = new Date();
			date3.setTime(time3);

			dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			// Add threads.
			Map<String, AttributeValue> forum = new HashMap<String, AttributeValue>();
			forum.put("ForumName",
					new AttributeValue().withS("Amazon DynamoDB"));
			forum.put("Subject",
					new AttributeValue().withS("DynamoDB Thread 1"));
			forum.put("Message",
					new AttributeValue().withS("DynamoDB thread 1 message"));
			forum.put("LastPostedBy", new AttributeValue().withS("User A"));
			forum.put("LastPostedDateTime",
					new AttributeValue().withS(dateFormatter.format(date2)));
			forum.put("Views", new AttributeValue().withN("0"));
			forum.put("Replies", new AttributeValue().withN("0"));
			forum.put("Answered", new AttributeValue().withN("0"));
			forum.put("Tags", new AttributeValue().withSS(Arrays.asList(
					"index", "primarykey", "table")));

			PutItemRequest forumRequest = new PutItemRequest().withTableName(
					tableName).withItem(forum);
			client.putItem(forumRequest);

			forum.clear();

			forum.put("ForumName",
					new AttributeValue().withS("Amazon DynamoDB"));
			forum.put("Subject",
					new AttributeValue().withS("DynamoDB Thread 2"));
			forum.put("Message",
					new AttributeValue().withS("DynamoDB thread 2 message"));
			forum.put("LastPostedBy", new AttributeValue().withS("User A"));
			forum.put("LastPostedDateTime",
					new AttributeValue().withS(dateFormatter.format(date3)));
			forum.put("Views", new AttributeValue().withN("0"));
			forum.put("Replies", new AttributeValue().withN("0"));
			forum.put("Answered", new AttributeValue().withN("0"));
			forum.put("Tags", new AttributeValue().withSS(Arrays.asList(
					"index", "primarykey", "rangekey")));

			forumRequest = new PutItemRequest().withTableName(tableName)
					.withItem(forum);
			client.putItem(forumRequest);

			forum.clear();

			forum.put("ForumName", new AttributeValue().withS("Amazon S3"));
			forum.put("Subject", new AttributeValue().withS("S3 Thread 1"));
			forum.put("Message",
					new AttributeValue().withS("S3 Thread 3 message"));
			forum.put("LastPostedBy", new AttributeValue().withS("User A"));
			forum.put("LastPostedDateTime",
					new AttributeValue().withS(dateFormatter.format(date1)));
			forum.put("Views", new AttributeValue().withN("0"));
			forum.put("Replies", new AttributeValue().withN("0"));
			forum.put("Answered", new AttributeValue().withN("0"));
			forum.put("Tags", new AttributeValue().withSS(Arrays.asList(
					"largeobjects", "multipart upload")));

			forumRequest = new PutItemRequest().withTableName(tableName)
					.withItem(forum);

			client.putItem(forumRequest);

		} catch (AmazonServiceException ase) {
			System.err.println("Failed to create item in " + tableName);
		}

	}

	private static void uploadSampleReplies(String tableName) {
		try {
			long time0 = (new Date()).getTime() - (1 * 24 * 60 * 60 * 1000); // 1
																				// day
																				// ago
			long time1 = (new Date()).getTime() - (7 * 24 * 60 * 60 * 1000); // 7
																				// days
																				// ago
			long time2 = (new Date()).getTime() - (14 * 24 * 60 * 60 * 1000); // 14
																				// days
																				// ago
			long time3 = (new Date()).getTime() - (21 * 24 * 60 * 60 * 1000); // 21
																				// days
																				// ago

			Date date0 = new Date();
			date0.setTime(time0);

			Date date1 = new Date();
			date1.setTime(time1);

			Date date2 = new Date();
			date2.setTime(time2);

			Date date3 = new Date();
			date3.setTime(time3);

			dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

			// Add threads.
			Map<String, AttributeValue> reply = new HashMap<String, AttributeValue>();
			reply.put("Id", new AttributeValue()
					.withS("Amazon DynamoDB#DynamoDB Thread 1"));
			reply.put("ReplyDateTime",
					new AttributeValue().withS(dateFormatter.format(date3)));
			reply.put("Message", new AttributeValue()
					.withS("DynamoDB Thread 1 Reply 1 text"));
			reply.put("PostedBy", new AttributeValue().withS("User A"));

			PutItemRequest replyRequest = new PutItemRequest().withTableName(
					tableName).withItem(reply);
			client.putItem(replyRequest);

			reply.clear();

			reply = new HashMap<String, AttributeValue>();
			reply.put("Id", new AttributeValue()
					.withS("Amazon DynamoDB#DynamoDB Thread 1"));
			reply.put("ReplyDateTime",
					new AttributeValue().withS(dateFormatter.format(date2)));
			reply.put("Message", new AttributeValue()
					.withS("DynamoDB Thread 1 Reply 2 text"));
			reply.put("PostedBy", new AttributeValue().withS("User B"));

			replyRequest = new PutItemRequest().withTableName(tableName)
					.withItem(reply);
			client.putItem(replyRequest);

			reply.clear();

			reply = new HashMap<String, AttributeValue>();
			reply.put("Id", new AttributeValue()
					.withS("Amazon DynamoDB#DynamoDB Thread 2"));
			reply.put("ReplyDateTime",
					new AttributeValue().withS(dateFormatter.format(date1)));
			reply.put("Message", new AttributeValue()
					.withS("DynamoDB Thread 2 Reply 1 text"));
			reply.put("PostedBy", new AttributeValue().withS("User A"));

			replyRequest = new PutItemRequest().withTableName(tableName)
					.withItem(reply);
			client.putItem(replyRequest);

			reply.clear();

			reply = new HashMap<String, AttributeValue>();
			reply.put("Id", new AttributeValue()
					.withS("Amazon DynamoDB#DynamoDB Thread 2"));
			reply.put("ReplyDateTime",
					new AttributeValue().withS(dateFormatter.format(date0)));
			reply.put("Message", new AttributeValue()
					.withS("DynamoDB Thread 2 Reply 2 text"));
			reply.put("PostedBy", new AttributeValue().withS("User A"));

			replyRequest = new PutItemRequest().withTableName(tableName)
					.withItem(reply);

			client.putItem(replyRequest);

		} catch (AmazonServiceException ase) {
			System.err.println("Failed to create item in " + tableName);
		}
	}

	private static void waitForTableToBecomeAvailable(String tableName) {
		System.out.println("Waiting for " + tableName + " to become ACTIVE...");

		long startTime = System.currentTimeMillis();
		long endTime = startTime + (10 * 60 * 1000);
		while (System.currentTimeMillis() < endTime) {
			DescribeTableRequest request = new DescribeTableRequest()
					.withTableName(tableName);
			TableDescription tableDescription = client.describeTable(request)
					.getTable();
			String tablename = tableDescription.getTableName();
			String tableStatus = tableDescription.getTableStatus();
			System.out.println("  - Table name: " + tablename);
			System.out.println("  - current state: " + tableStatus);
			if (tableStatus.equals(TableStatus.ACTIVE.toString()))
				return;
			try {
				Thread.sleep(1000 * 20);
			} catch (Exception e) {
			}
		}
		throw new RuntimeException("Table " + tableName + " never went active");
	}

	private static void waitForTableToBeDeleted(String tableName) {
		System.out.println("Waiting for " + tableName
				+ " while status DELETING...");

		long startTime = System.currentTimeMillis();
		long endTime = startTime + (10 * 60 * 1000);
		while (System.currentTimeMillis() < endTime) {
			try {
				DescribeTableRequest request = new DescribeTableRequest()
						.withTableName(tableName);
				TableDescription tableDescription = client.describeTable(
						request).getTable();
				String tableStatus = tableDescription.getTableStatus();
				System.out.println("  - current state: " + tableStatus);
				if (tableStatus.equals(TableStatus.ACTIVE.toString()))
					return;
			} catch (ResourceNotFoundException e) {
				System.out.println("Table " + tableName
						+ " is not found. It was deleted.");
				return;
			}
			try {
				Thread.sleep(1000 * 20);
			} catch (Exception e) {
			}
		}
		throw new RuntimeException("Table " + tableName + " was never deleted");
	}
}
