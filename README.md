# csye6225-fall2017
# Team Members #
* Ashwini Thaokar (thaokar.a@husky.neu.edu)
* Niravra Kar (kar.n@husky.neu.edu)
* Parakh Mahajan (mahajan.p@husky.neu.edu)
* Sumedh Saraf (saraf.s@husky.neu.edu)
# Requirements #
* IntelliT IDEA Ultimate
* Gradle Build Tool
* MySQL 
* Apache Tomcat 8
* RESTClient
# Build & Deploy Instructions #
* Import project on IntelliJ IDEA 
* Launch MySQL Workbench & create a schema
* Make sure the name of schema is same in Application Properties
* Provide your SQL username and Password
* Edit build configuration to add Tomcat server 
* Run the application
# Unit Tests #
* Launch a RESTClient on Mozilla Firefox
* For basic authorization, enter username and password to add authentication header
* Set content type as application/json
* Type url: "localhost:8080"
* To register a new go to /user/register
* In the body, enter name, email and password in json format
# Integration Test #
Continuous integration tests were build using Travis CI
# Load Tests using Apache JMETER #
* Create a thread group and add 100 threads for concurrent users  
* Add HTTP Authorization Manager for Basic Authntication 
* Add HTTP Header Manager and add Header Name as content/type and Value as application/json
* Add a HTTP request for Local Server
* Add a HTTP request for Register User with method as POST and Path as /user/register
* Add a JSON Body Data with values name, email and password. Make sure the values are generated randomly
* Add Listener Table/Tree to view the results
# Travis CI link for the Project #
[Travis CI Link](https://travis-ci.com/Niravra/csye6225-fall2017)