# csye6225-fall2017
NetWork Structures and Cloud Computing
=======
# Team Members #
* Ashwini Thaokar (thaokar.a@husky.neu.edu)
* Niravra Kar (kar.n@husky.neu.edu)
* Parakh Mahajan (mahajan.p@husky.neu.edu)
* Sumedh Saraf (saraf.s@husky.neu.edu)
# Requirements #
* IntelliJ IDEA Ultimate
* Gradle Build Tool
* Java SDK 
* Apache Tomcat 8
* Code-Deploy Agent
* Postman
# Build & Deploy Instructions #
* Import project on IntelliJ IDEA 
* Launch MySQL Workbench & create a schema
* Make sure the name of schema is same in Application Properties
* Provide your SQL username and Password
* Edit build configuration to add Tomcat server 
* Run the application
# Unit Tests #
* Launch a POSTMAN
* Set content type as application/json
* Type url: "localhost:8080"
* To register a new user go to /user/register and method POST
* In the body, enter name, email and password in json format
* To add a new task for user go to /tasks and method POST
* In the body, enter description in json format
* To update a task for user go to /tasks/taskId and method PUT
* To delte a task for user go to /tasks/tasksId and method DELETE
* To add a new attachment of task for user go to /tasks/tasksId/attachments and method POST
* To delete a attachment of task for user go to /tasks/tasksId/attachments and method DELETE
# Integration Test #
Continuous integration tests were build using Travis CI
# Load Tests using Apache JMETER #
* Create a thread group for user and add 100 threads for concurrent users
* Add HTTP Authorization Manager for Basic Authntication 
* Add HTTP Header Manager and add Header Name as content/type and Value as application/json
* Add a Default HTTP request for Local Server
* Add a HTTP request for Register User with method as POST and Path as /user/register
* Add a JSON Body Data with values name, email and password. Make sure the values are generated randomly
* Add Listener Table/Tree to view the results

* Create a thread group for adding task for users created above
* Add CSV Config to read the csv file exported from the MySQL workbench for registered user for basic authentication 
* Add HTTP Header Manager and add Header Name as content/type and Value as application/json
* Add a Default HTTP request for Local Server
* Add a HTTP request for Adding task for users with method as POST and Path as /tasks
* Add a JSON Body Data with value description. Make sure the values are generated randomly

* Create a thread group for updating task for users created above
* Add CSV Config to read the csv file exported from the MySQL workbench for registered user for basic authentication
* Add CSV Config to read the csv file exported from the MySQL workbench for the task to be updated 
* Add HTTP Header Manager and add Header Name as content/type and Value as application/json
* Add a Default HTTP request for Local Server
* Add a HTTP request for updating task for users with method as PUT and Path as /tasks/tasksId
* Add a JSON Body Data with value description. Make sure the values are generated randomly

* Create a thread group for deleting task for users created above
* Add CSV Config to read the csv file exported from the MySQL workbench for registered user for basic authentication
* Add CSV Config to read the csv file exported from the MySQL workbench for the task to be deleted 
* Add HTTP Header Manager and add Header Name as content/type and Value as application/json
* Add a Default HTTP request for Local Server
* Add a HTTP request for deleteing task for users with method as DELETE and Path as /tasks/tasksId

* Create a thread group for adding attachments for a task for particular users created above
* Add CSV Config to read the csv file exported from the MySQL workbench for registered user for basic authentication
* Add CSV Config to read the csv file exported from the MySQL workbench for the attachments to be added for the task created above 
* Add HTTP Header Manager and add Header Name as content/type and Value as application/json
* Add a Default HTTP request for Local Server
* Add a HTTP request for Adding task for users with method as POST and Path as /tasks/tasksId/attachments
* Add a JSON Body Data with path of the file to be attached

* Create a thread group for deleting attachments for particular task of users created above
* Add CSV Config to read the csv file exported from the MySQL workbench for registered user for basic authentication
* Add CSV Config to read the csv file exported from the MySQL workbench for the task of particular attachment
* Add CSV Config to read the csv file exported from the MySQL workbench for attachments to be deleted 
* Add HTTP Header Manager and add Header Name as content/type and Value as application/json
* Add a Default HTTP request for Local Server
* Add a HTTP request for delteing attachment of task for users with method as DELETE and Path as /tasks/tasksId/attachmentId
# Travis CI link for the Project #
[Travis CI Link](https://travis-ci.com/Niravra/csye6225-fall2017)
>>>>>>> assignment7
