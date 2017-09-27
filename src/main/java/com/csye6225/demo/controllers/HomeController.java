package com.csye6225.demo.controllers;


import com.csye6225.demo.dao.PersonDao;
import com.csye6225.demo.entity.Person;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private PersonDao personDao;

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
  if (personList.size() > 0){
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "user email already exists");
    return jsonObject.toString();
  }

  else {
    String encrypt = bCryptPasswordEncoder.encode(person.getPassword());
    Person p = new Person();
    p.setName(person.getName());
    p.setEmail(person.getEmail());
    p.setPassword(encrypt);
    personDao.save(p);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized and user saved");
    return jsonObject.toString();
  }

  }


  @RequestMapping(value = "/user/find", method = RequestMethod.POST, produces = {"application/json"}
          , consumes = "application/json", headers = {"content-type=application/json; charset=utf-8"})
  @ResponseBody
  public String findUser(@RequestBody Person person) {
    System.out.println("Email :" + person.getEmail());
    System.out.println("Password :" + person.getPassword());

    List<Person> personList = personDao.findByEmail(person.getEmail());

    if (personList.size() > 0) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("message", "user found");
      return jsonObject.toString();
    }
    else
    {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("message", "user not found");
      return jsonObject.toString();
    }

  }
}
