
# Amazon DynamoDB


DynamoDB differs from other Amazon services by allowing developers to purchase a service based on throughput, rather than storage. Although the database does not automatically scale, 
administrators can request more throughput and DynamoDB will spread the data and traffic over a number of servers using solid-state drives, allowing predictable performance. It offers integration
with Hadoop via Elastic MapReduce.

#Configuring Dynamo DB in Eclipse using Plugin:

Please follow the below url for install instructions

http://java.awsblog.com/post/TxPTR0HTAPBTM2/DynamoDB-Local-Test-Tool-Integration-for-Eclipse

#Advantages:

1.Scalable- There is no limit to the amount of data you can store in an Amazon DynamoDB table, and the service automatically allocates more storage, as you store more data using the Amazon DynamoDB write APIs.

2.Distributed- Amazon DynamoDB scales horizontally and seamlessly scales a single table over hundreds of servers.

3.Flexible - Amazon DynamoDB does not have a fixed schema. Instead, each data item may have a different number of attributes. Multiple data types (strings, numbers, binary, and sets) add richness to the data model.

4.Easy Administration— Hosted by Amazon and receives Fully Managed Service from Amazon.

5.Cost Effective— a free tier allows more than 40 million database operations/month and pricing is based on throughput.

6.Built-in Fault Tolerance— Amazon DynamoDB has built-in fault tolerance, automatically and synchronously replicating your data across multiple Availability Zones in a Region for high availability and to help protect your data against individual machine, or even facility failures.

7.Automatic data replication - All data items are stored on Solid State Disks (SSDs) and are automatically replicated across multiple Availability Zones in a Region to provide built-in high availability ,data durability 

8.Amazon Redshift Integration-You can load data from Amazon DynamoDB tables into Amazon Redshift, a fully managed data warehouse service. You can connect to Amazon Redshift with a SQL client or business intelligence tool using standard PostgreSQL JDBC or ODBC drivers, and perform complex SQL queries and business intelligence tasks on your data.

9.Integrated Monitoring-Amazon DynamoDB displays key operational metrics for your table in the AWS Management Console. The service also integrates with CloudWatch so you can see your request throughput and latency for each Amazon DynamoDB table, and easily track your resource consumption.

10.Fast, Predictable Performance- Average service-side latencies in single-digit milliseconds

11.Provisioned Throughput — when creating a table, simply specify how much throughput capacity you require. Amazon DynamoDB allocates dedicated resources to your table to meet your performance requirements, and automatically partitions data over a sufficient number of servers to meet your request capacity.


#Pitfalls:

1.64KB limit on row size

2.1MB limit on querying

3.Deployable Only on AWS

4.Dynamo is an expensive and extremely low latency solution, If you are trying to store more than 64KB per item

5.Joins are impossible -you have to manage complex data relations on your code/cache lay

6.Additional storage cost for each item- In computing the storage used by the table, Amazon DynamoDB adds 100 bytes of overhead to each item for indexing purposes, this extra 100 bytes is not used in computing the capacity unit calculation.

7.No triggers 

8.Poor query comparison operators


