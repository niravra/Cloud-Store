package com.csye6225.demo.controllers;
/**
 * <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 * <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 * <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 * <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>
 **/
import java.io.IOException;
import com.csye6225.demo.dao.PersonDao;
import com.csye6225.demo.dao.TaskDao;
import com.csye6225.demo.entity.Person;
import com.csye6225.demo.entity.Task;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    JsonObject jsonObject = new JsonObject();

    if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
      jsonObject.addProperty("message", "you are not logged in!!!");
    } else {
      jsonObject.addProperty("message", "you are logged in. current time is " + new Date().toString());
    }

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

  @RequestMapping(value = "/tasks", method = RequestMethod.POST, produces = {"application/json"}
          , consumes = "application/json", headers = {"content-type=application/json; charset=utf-8"})
  @ResponseBody
  public String registerTask(@RequestBody Task task) {
    Task t = new Task();
    t.setDesc(task.getDesc());
    taskDao.save(t);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "Task created");
    return jsonObject.toString();

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
}

