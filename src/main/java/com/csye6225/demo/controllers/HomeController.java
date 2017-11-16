package com.csye6225.demo.controllers;
/**
 * <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 * <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 * <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 * <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>
 **/
import java.io.IOException;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.csye6225.demo.dao.PersonDao;
import com.csye6225.demo.dao.TaskDao;
import com.csye6225.demo.entity.Customer;
import com.csye6225.demo.entity.Person;
import com.csye6225.demo.entity.ProductInfo;
import com.csye6225.demo.entity.Task;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

//  private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
//
//  @Autowired
//  public HomeController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
//    this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
//  }

  private DynamoDBMapper dynamoDBMapper;


  @Value("${amazon.aws.accesskey}")
  private String amazonAWSAccessKey;
  @Value("${amazon.aws.secretkey}")
  private String amazonAWSSecretKey;


  @Autowired
  private AmazonDynamoDB amazonDynamoDB;

  @Autowired
  private AWSCredentials awsCredentials;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private PersonDao personDao;

  @Autowired
  private TaskDao taskDao;

  private final static Logger logger = LoggerFactory.getLogger(HomeController.class);
  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype());

  @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String welcome() {

//    dynamoCreation();
    JsonObject jsonObject = new JsonObject();

    if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
      jsonObject.addProperty("message", "you are not logged in!!!");
    } else {
      jsonObject.addProperty("message", "you are logged in. current time is " + new Date().toString());
    }

    return jsonObject.toString();
  }

  @RequestMapping(value = "/dynamo", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String dynamoAdd() {

//    dynamoCreation();

    BasicAWSCredentials awsCreds = new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_EAST_1).build();


    DynamoDBMapper mapper = new DynamoDBMapper(client);

    long now = Instant.now().getEpochSecond(); // unix time
    long ttl = 20 * 60 ; // 24 hours in sec

    ProductInfo c = new ProductInfo();
    c.setId(String.valueOf(200));
    c.setCost("1550");
    c.setMsrp("1750");
    c.setTtl(ttl + now);


    mapper.save(c);

    JsonObject jsonObject = new JsonObject();


    jsonObject.addProperty("message", "dynamo item created");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String test() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /test");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/testPost", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String testPost() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /testPost");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/user/register", method = RequestMethod.POST, produces = {"application/json"}
          , consumes = "application/json", headers = {"content-type=application/json; charset=utf-8"})
  @ResponseBody
  public String registerUser(@RequestBody Person person) {

    List<Person> personList = personDao.findByEmail(person.getEmail());
    List<Person> personListName = personDao.findByName(person.getName());
    if (personList.size() > 0  || personListName.size() > 0) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("message", "user name/email already exists");
      return jsonObject.toString();
    } else {
      String encrypt = bCryptPasswordEncoder.encode(person.getPassword());
      Person p = new Person();
      p.setName(person.getName());
      p.setEmail(person.getEmail());
      p.setPassword(encrypt);
      personDao.save(p);
     // inMemoryUserDetailsManager.createUser(User.withUsername(person.getName()).password(bCryptPasswordEncoder.encode(person.getPassword())).roles("USER").build());
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("message", "authorized and user saved");
      return jsonObject.toString();
    }

  }


  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public String uploadSample(@RequestParam("taskId") Integer taskId,
                             @RequestParam("file") MultipartFile files) throws IOException {
    System.out.println("entered post method");
//    List<Attachmentdata> container = new ArrayList<Attachmentdata>();
//    System.out.println(taskIds.length);

    int i = 0;
//    for (MultipartFile multipartFile : files) {
//      Attachmentdata p = new Attachmentdata();
//      p.set(multipartFile.getBytes());
//      p.setPhotoContent(multipartFile.getContentType());
//       p.setAttachId(name[i]);
    System.out.println(files.getOriginalFilename());
    System.out.println(files.getBytes());
    System.out.println(taskId);
//      i = i +1;
//      container.add(p);
//    }
//
//    Response r = this.service.addPhotos(container);
    return "test";


  }


  @RequestMapping(value = "/user/find", method = RequestMethod.POST, produces = {"application/json"}
          , consumes = "application/json", headers = {"content-type=application/json; charset=utf-8"})
  @ResponseBody
  public String findUser(@RequestBody Person person) {
//    System.out.println("Email :" + person.getEmail());
//    System.out.println("Password :" + person.getPassword());


    List<Person> personList = personDao.findByEmail(person.getEmail());

    if (personList.size() > 0) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("message", "user found");
      return jsonObject.toString();
    } else {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("message", "user not found");
      return jsonObject.toString();
    }

  }




//  @RequestMapping(value = "/tasks/{userId}", method = RequestMethod.POST, produces = {"application/json"}
//          , consumes = "application/json", headers = {"content-type=application/json; charset=utf-8"})
//  @ResponseBody
//  public String updateTask(@RequestParam("userId") Integer userId,@RequestBody Task task) {
//    JsonObject jsonObject = new JsonObject();
////    Task t = taskDao.findByuser(userId);
//    if (t == null)
//    {
//      jsonObject.addProperty("message", "Task cannot be updated as userId does not exist");
//      return jsonObject.toString();
//    }
//    t.setDesc(task.getDesc());
//    t.setUserId(userId);
//    taskDao.save(t);
//    jsonObject.addProperty("message", "Task updated");
//    return jsonObject.toString();
//
//  }
  public void dynamoCreation(){

//    AmazonDynamoDB client = AmazonDynamoDBClientBuilder;
    dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

    System.out.println("THE AMA IS :     " +amazonDynamoDB);


    CreateTableRequest tableRequest = dynamoDBMapper
            .generateCreateTableRequest(Customer.class);
    tableRequest.setProvisionedThroughput(
            new ProvisionedThroughput(1L, 1L));
    amazonDynamoDB.createTable(tableRequest);
  }

  @RequestMapping(value = "/forgot-password", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String passwordReset(@RequestParam(value = "username") String username){
    JsonObject object = new JsonObject();
    List<Person> user = personDao.findByName(username);
    object.addProperty("message: ","A mail with a reset link has been sent to: "+ username);

    if(user == null){
      return object.toString();
    }

    AmazonSNS sns = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    CreateTopicResult topicResult = sns.createTopic("SESTopic");

    String arn = topicResult.getTopicArn();//obj.get("TopicArn").getAsString();
    PublishRequest publishRequest = new PublishRequest(arn,""+username+","+user.get(0).getUserId());
    PublishResult publishResult = sns.publish(publishRequest);

    return object.toString();
  }
}

