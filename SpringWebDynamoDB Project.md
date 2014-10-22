21/10/2014

#Project: Spring Web DynamoDB

- Create the tables and upload the data to DynamoDB using CreateTablesUploadData.java

- SpringDataDynamoDemoConfig class configures the DynamoDB credentials and EndPoint

- Created Pojos for Thread, Forum and Reply which are Tables in DyanamoDB

- Created Pojos for ThreadId, ReplyId which are composite keys

Whenever the request from browser, searching forums table by ThreadId , the Name and Category of the service will be displayed.
Whenever request from Browser searching forums table by Category, the ThreadId, Name of the serrvice will be displayed.

The project involves Serializing the Composite Ids and marshalls at Provider/Sender,unmarshalls at Reciever/Consumer, so that
the requested service will be dispalyed on Browser.


#Before Building the Application:

Populate the src/main/resources/environment.properties file with DynamoDB endpoint, and AWS credentials

Signup for http://aws.amazon.com/dynamodb/ and get the credentials(for security reason cannot share on github)


amazon.dynamodb.endpoint= <>

amazon.aws.accesskey= <>

amazon.aws.secretkey=<>


#Build and deploy Instructions:


1.Run the file CreateTablesUploadData.java to create tables and load data.

2.Run the command mvn jetty:run against pom.xml

3.Access the URL http://localhost:8080 


http://localhost:8080/forums/search/findByThreads?threads=2

http://localhost:8080/forums/search/findByThreads?threads=0

http://localhost:8080/forums/search/findByCategory?category=Amazon Web Services
