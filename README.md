# Rest Api file handler
Rest API challange:
- Upload file
- Download stored file
- List files uploaded with details

#App Backend
This application is a RESTful API developed in JAVA using the Jersey Framework (JAX-RS Reference Implementation) to expose some POST
and GET methods capable to upload and download any type of file and also list this uploaded files. All the API method, returns and
parameters are docummented by Swagger Framework. The data is stored in a MySQL database that was managed all operations through
Hibernate. The application untill now is served locally by Apache Tomcat V8.0.37.

#App Frontent
The simple frontend used here is just a page using angular to control and manage the requisitions and responses from server, using
Angular Material to provide some friendly interface.

#Purpose of development
- Create an rest api to upload any kind of file splitting in 1Mb chunks if those file are greater than that.
- Store all this files and provide a way to serve them merging the splitted chunks in  single archive again possible to download
- List all stored files providing some details about them, like number of chunks, status of upload, uploader etc.

#Instalation
- Create a folder on your computer and execute git clone with URL: https://github.com/tsr-thulio/restapi.git or by SSH: git@github.com:tsr-thulio/restapi.git
- Import Existing Maven Projects from this folder that you cloned
- Download and configure on your IDE the Apache Tomcat V8.0.37 (http://tomcat.apache.org/download-80.cgi#8.0.37)
- The DataBase is configured on persistence.xml to use the MySQL DB on this path: 127.0.0.1:3306/restdb
- Run your project on server (Tomcat) and open the URL: localhost:8080/RestApi