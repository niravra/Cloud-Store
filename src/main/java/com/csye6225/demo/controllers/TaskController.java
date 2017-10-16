package com.csye6225.demo.controllers;
/**
 * <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 * <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 * <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 * <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>
 **/

import java.io.IOException;

import com.csye6225.demo.dao.AttachmentDao;
import com.csye6225.demo.dao.PersonDao;
import com.csye6225.demo.dao.TaskDao;
import com.csye6225.demo.entity.AttachmentData;
import com.csye6225.demo.entity.Person;
import com.csye6225.demo.entity.Task;
import com.google.gson.JsonArray;
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

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class TaskController {


//    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
//
//    @Autowired
//    public TaskController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
//        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
//    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private AttachmentDao attachmentDao;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public String registerTask(HttpServletRequest httpRequest) {

        JsonObject jsonObject = new JsonObject();
        String[] headValue = HeaderCheck(httpRequest);
        boolean auth = checkAuth(headValue[0], headValue[1]);

        if (headValue.equals(null)) {
            jsonObject.addProperty("message", "The Basic Auth is not provided");
            return jsonObject.toString();
        }


        if (auth) {
            jsonObject.addProperty("message", "Task created");
            return jsonObject.toString();
        } else {
            jsonObject.addProperty("message", "Not Authorized User Not Found");
            return jsonObject.toString();
        }

    }


    @RequestMapping(value = "/tasks", method = RequestMethod.POST, produces = {"application/json"}
            , consumes = "application/json", headers = {"content-type=application/json; charset=utf-8"})
    @ResponseBody
    public String registerTask(@RequestBody Task task, HttpServletRequest httpRequest, HttpServletResponse response) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        String[] headValue = HeaderCheck(httpRequest);
        boolean auth = false;

        if (headValue == null) {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("Response code", "The Basic Auth is not provided");
            return jsonObject.toString();
        } else
            auth = checkAuth(headValue[0], headValue[1]);


        if (auth) {
            Task t = new Task();
            t.setDesc(task.getDesc());

            List<Person> p = personDao.findByName(headValue[0]);
            t.setPerson(p.get(0));
            taskDao.save(t);
            jsonObject.addProperty("message", "Task created");
            List<Task> taskList = taskDao.findByPerson(p.get(0));
            for (Task t1 : taskList) {
                JsonObject jobj = new JsonObject();

                jobj.addProperty("id", String.valueOf(t1.getTaskId()));
                jobj.addProperty("description", t1.getDesc());
                jsonArray.add(jobj);
            }
            response.setStatus(201, "Created");
            return jsonArray.toString();
        } else {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("message", "Not Authorized User Not Found");
            return jsonObject.toString();
        }

    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT, produces = {"application/json"}
            , consumes = "application/json", headers = {"content-type=application/json; charset=utf-8"})
    @ResponseBody
    public String UpdateTask(@PathVariable("id") String id, @RequestBody Task task, HttpServletRequest httpRequest, HttpServletResponse response) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        String[] headValue = HeaderCheck(httpRequest);
        boolean auth = false;
        boolean taskChecker = false;

        if (headValue == null) {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("Response code", "The Basic Auth is not provided");
            return jsonObject.toString();
        } else
            auth = checkAuth(headValue[0], headValue[1]);


        if (auth) {

            List<Task> check = taskDao.findByTaskId(UUID.fromString(id));

            if (check.size() > 0 )
                System.out.println("hahahahahhahahahahha");


            List<Person> p = personDao.findByName(headValue[0]);

            List<Task> taskList = taskDao.findByPerson(p.get(0));
            for (Task t1 : taskList) {
                System.out.println( "The ID from request is  : " +id);
                System.out.println("The ID inside the method is " +t1.getTaskId());
                if (t1.getTaskId().toString().equals(id)){
                    System.out.println("IT entered hereeeeeee");
                    t1.setDesc(task.getDesc());
                    t1.setPerson(p.get(0));
                    taskDao.save(t1);
                    jsonObject.addProperty("id", String.valueOf(t1.getTaskId()));
                    jsonObject.addProperty("description", t1.getDesc());
                    response.setStatus(200, "Created");
                    taskChecker = true;
                }
            }
            if(taskChecker){
                response.setStatus(403, "Forbidden");
            }

            return jsonObject.toString();
        } else {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("message", "Authorized User Not Found");
            return jsonObject.toString();
        }

    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = {"application/json"}
            , consumes = "application/json", headers = {"content-type=application/json; charset=utf-8"})
    @ResponseBody
    public String DeleteTask(@PathVariable("id") String id, @RequestBody Task task, HttpServletRequest httpRequest, HttpServletResponse response) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        String[] headValue = HeaderCheck(httpRequest);
        boolean auth = false;
        boolean taskChecker = false;

        if (headValue == null) {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("Response code", "The Basic Auth is not provided");
            return jsonObject.toString();
        } else
            auth = checkAuth(headValue[0], headValue[1]);


        if (auth) {

            List<Person> p = personDao.findByName(headValue[0]);

            List<Task> taskList = taskDao.findByPerson(p.get(0));
            for (Task t1 : taskList) {
                System.out.println( "The ID from request is  : " +id);
                System.out.println("The ID inside the method is " +t1.getTaskId());
                if (t1.getTaskId().toString().equals(id)){

                    taskDao.delete(t1);
                    jsonObject.addProperty("message", "deleted");

                    response.setStatus(204, "No Content");
                    taskChecker = true;
                }
            }
            if(taskChecker){
                response.setStatus(401, "Unauthorized");
            }

            return jsonObject.toString();
        } else {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("message", "Authorized User Not Found");
            return jsonObject.toString();
        }

    }


    public boolean checkAuth(String username, String password) {

        String encrypt = bCryptPasswordEncoder.encode(password);
        List<Person> personList = personDao.findByName(username);
        if (personList.size() > 0) {
            if (bCryptPasswordEncoder.matches(password, personList.get(0).getPassword())) {
                return true;
            } else
                return false;

        } else
            return false;
    }

    public String[] HeaderCheck(HttpServletRequest httpRequest) {
        String[] values = new String[2];
        final String authorization = httpRequest.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            values = credentials.split(":", 2);
            return values;
        } else
            return null;
    }





    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.POST, produces = {"application/json"}
            )
    @ResponseBody
    public String AddAttachment(@PathVariable("id") String id, @PathVariable("file") MultipartFile files, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        String[] headValue = HeaderCheck(httpRequest);
        boolean auth = false;
        boolean taskChecker = false;

        if (headValue == null) {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("Response code", "The Basic Auth is not provided");
            return jsonObject.toString();
        } else
            auth = checkAuth(headValue[0], headValue[1]);


        if (auth) {
            System.out.println(UUID.fromString(id));
            List<Task> tlist = taskDao.findByTaskId(UUID.fromString(id));
            System.out.println(tlist.size());
            if (tlist.size() > 0) {
                System.out.println(files.getOriginalFilename());
                System.out.println(files.getBytes() );
                AttachmentData ad = new AttachmentData();
                ad.setAttachName(files.getBytes());
                ad.setContent(files.getContentType());
                Task task = tlist.get(0);
                ad.setTask(task);

                attachmentDao.save(ad);
                List<Task> tlist1 = taskDao.findByTaskId(UUID.fromString(id));
                List<AttachmentData> ads = attachmentDao.findByTask(tlist1.get(0));
                for (AttachmentData tk : ads) {
                    JsonObject jobj = new JsonObject();

                    jobj.addProperty("id", String.valueOf(tk.getAttachId()));
                    jobj.addProperty("url", tk.getContent());
                    jsonArray.add(jobj);
                }

                return jsonArray.toString();


            } else {

                response.setStatus(401, "Unauthorized");
                jsonObject.addProperty("message", "No attachments found");
                return jsonObject.toString();

            }


        } else {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("message", "Authorized User Not Found");
            return jsonObject.toString();

        }

    }

}
