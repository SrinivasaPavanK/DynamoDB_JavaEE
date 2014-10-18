Java EE
=======

Java Platform, Enterprise Edition (Java EE) is the standard in community-driven enterprise software. Java EE is developed using the Java Community Process, with contributions from industry experts, commercial and open source organizations, Java User Groups, and countless individuals. Each release integrates new features that align with industry needs, improves application portability, and increases developer productivity.


A Java EE application or a Java Platform, Enterprise Edition application is any deployable unit of Java EE functionality. 

This can be a single Java EE module or a group of modules packaged into an EAR file along with a Java EE application deployment descriptor. Java EE applications are typically engineered to be distributed across multiple computing tiers.



Enterprise applications can consist of combinations of the following:


1.Enterprise JavaBean (EJB) modules (packaged in JAR files)


2.Web modules (packaged in WAR files)


3.connector modules or resource adapters (packaged in RAR files)


4.Session Initiation Protocol (SIP) modules (packaged in SAR files)


5.application client modules


6.Additional JAR files containing dependent classes or other components required by the application



Advantages:
===========


The J2EE platform provides the following:


1.Complete Web services support: The J2EE platform provides a framework for developing and deploying web services on the Java platform. The Java API for XML-based RPC (JAX-RPC) enables Java technology developers to develop SOAP based interoperable and portable web services. Developers use the standard JAX-RPC programming model to develop SOAP based web service clients and endpoints. A web service endpoint is described using a Web Services Description Language (WSDL) document. JAX-RPC enables JAX-RPC clients to invoke web services developed across heterogeneous platforms. In a similar manner, JAX-RPC web service endpoints can be invoked by heterogeneous clients. For more info, see http://java.sun.com/webservices/.


2.Faster solutions delivery time to market: The J2EE platform uses "containers" to simplify development. J2EE containers provide for the separation of business logic from resource and lifecycle management, which means that developers can focus on writing business logic -- their value add -- rather than writing enterprise infrastructure. For example, the Enterprise JavaBeans (EJB) container (implemented by J2EE technology vendors) handles distributed communication, threading, scaling, transaction management, etc. Similarly, Java Servlets simplify web development by providing infrastructure for component, communication, and session management in a web container that is integrated with a web server.


3.Freedom of choice: J2EE technology is a set of standards that many vendors can implement. The vendors are free to compete on implementations but not on standards or APIs. Sun supplies a comprehensive J2EE Compatibility Test Suite (CTS) to J2EE licensees. The J2EE CTS helps ensure compatibility among the application vendors which helps ensure portability for the applications and components written for the J2EE platform. The J2EE platform brings Write Once, Run Anywhere (WORA) to the server.


4.Simplified connectivity: J2EE technology makes it easier to connect the applications and systems you already have and bring those capabilities to the web, to cell phones, and to devices. J2EE offers Java Message Service for integrating diverse applications in a loosely coupled, asynchronous way. The J2EE platform also offers CORBA support for tightly linking systems through remote method calls. In addition, the J2EE platform has J2EE Connectors for linking to enterprise information systems such as ERP systems, packaged financial applications, and CRM applications.

By offering one platform with faster solution delivery time to market, freedom of choice, and simplified connectivity, the J2EE platform helps IT by reducing TCO and simultaneously avoiding single-source for their enterprise software needs.



Pitfalls:
=========


1: Ignore the default lock of a @Singleton: Java EE uses a container managed write lock as default.This results in a serialized processing of all method calls and lowers the scalability and performance of the application. 
This is in the case of implementing  EJB Singleton.

2: The primary disadvantage of JavaEE is also it’s standardization. Because it’s a committee process the time to respond to industry changes is slow.

3: Out of memory Errors: Application crash because of low performance
