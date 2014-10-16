
Amazon DynamoDB


DynamoDB differs from other Amazon services by allowing developers to purchase a service based on throughput, rather than storage. Although the database does not automatically scale, 
administrators can request more throughput and DynamoDB will spread the data and traffic over a number of servers using solid-state drives, allowing predictable performance. It offers integration
with Hadoop via Elastic MapReduce.

==========
Advantages:
============
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

11.Secure- Amazon DynamoDB is secure and uses proven cryptographic methods to authenticate users and prevent unauthorized data access. It also integrates with AWS Identity and Access Management for fine-grained access control for users within your organization. Uses secure algorithms to keep your data safe

12.Amazon Elastic MapReduce Integration- Tightly integrated with Amazon Elastic Map Reduce (Amazon EMR) that allows businesses to perform complex analytics of their large datasets using a hosted pay-as-you-go Hadoop framework on AWS.

13.Strong Consistency, Atomic Counters- Single API call allows you to atomically increment or decrement numerical attributes

14.Provisioned Throughput — when creating a table, simply specify how much throughput capacity you require. Amazon DynamoDB allocates dedicated resources to your table to meet your performance requirements, and automatically partitions data over a sufficient number of servers to meet your request capacity.

15.Supports Compression-Data can be compressed and stored in binary form  using Compression algorithms, such as GZIP or LZO

16.Low learning curve

17.Tunable consistency

18.Composite key support

19.Offers Conditional updates

20.Supports Hadoop integration Map Reduce, Hive
============
Pitfalls:
============
1.64KB limit on row size

2.1MB limit on querying

3.Deployable Only on AWS

4.Dynamo is an expensive and extremely low latency solution, If you are trying to store more than 64KB per item

5.Consistency comes with cost– Read capacity units are based on strongly consistent read operations, which require more effort and consume twice as many database resources as eventually consistent reads.

6.Size is multiple of 4KB for Read operations: If you get an item of 3.5 KB, Amazon DynamoDB rounds the items size to 4 KB. If you get an item of 10 KB, Amazon DynamoDB rounds the item size to 12 KB. If Batch reads a 1.5 KB item and a 6.5 KB item, Amazon DynamoDB will calculate the size as 12 KB (4 KB + 8 KB), not 8 KB (1.5 KB + 6.5 KB).

7.Queries - Querying data is extremely limited. Especially if you want to query non-indexed data. 
Unable to do complex queries. DynamoDB is great for lookups by key, not so good for queries, and abysmal for queries with multiple predicates. (Esp. for Eventlog tables)
Secondary indexes are not supported.

8.Indexing - Changing or adding keys on-the-fly is impossible without creating a new table. Indexes on column values are not supported. 

9.Joins are impossible -you have to manage complex data relations on your code/cache layer.

10.Backup - tedious backup procedure as compared to the slick backup of RDS

11.Latency in availability- When you create a table programatically (or even using AWS Console), the table doesn’t become available instantly

12.NO ACID-In RDBMS we get ACID guarantee, but in Dynamo-db there is no such guarantee. 

13.Speed - Response time is problematic compared to RDS. You find yourself building elaborate caching mechanism to compensate for it in places you would have settled for RDS's internal caching.

14.No support for atomic transactions- Each write operation is atomic to an item. A write operation either successfully updates all of the item's attributes or none of its attributes.

15.Additional storage cost for each item- In computing the storage used by the table, Amazon DynamoDB adds 100 bytes of overhead to each item for indexing purposes, this extra 100 bytes is not used in computing the capacity unit calculation.

16.No triggers 

17.Poor query comparison operators

18.No Foreign Keys

19.No Server-side scripts 
