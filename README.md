DynamoDB_JavaEE
===============

18/10/2014
One Page document describing each of the topic and their advantages and pitfalls for Dynamo DB and Java EE


#Configuring Dynamo DB in Eclipse using Plugin:

Please follow the below url for install instructions

http://java.awsblog.com/post/TxPTR0HTAPBTM2/DynamoDB-Local-Test-Tool-Integration-for-Eclipse


#Build and Deployment:


Populate the src/main/resources/environment.properties file with your DynamoDB endpoint, and AWS credentials.

mvn jetty:run

Access http://localhost:8080 and explore a simple REST CRUD API for the Reply,Thread and Forum objects.
