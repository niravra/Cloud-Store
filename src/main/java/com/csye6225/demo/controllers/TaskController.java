package com.csye6225.demo.controllers;
/**
 * <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 * <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 * <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 * <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>
 **/

import java.io.File;
import java.io.IOException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.csye6225.demo.dao.AttachmentDao;
import com.csye6225.demo.dao.PersonDao;
import com.csye6225.demo.dao.TaskDao;
import com.csye6225.demo.entity.AttachmentData;
import com.csye6225.demo.entity.Person;
import com.csye6225.demo.entity.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

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

//    @Autowired
//    private PasswordTokenRepository passwordTokenRepository;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public String registerTask(HttpServletRequest httpRequest, HttpServletResponse response) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        String[] headValue = HeaderCheck(httpRequest);
        boolean auth = checkAuth(headValue[0], headValue[1]);

        if (headValue.equals(null)) {
            jsonObject.addProperty("message", "The Basic Auth is not provided");
            return jsonObject.toString();
        }


        if (auth) {
            Task t = new Task();


            List<Person> p = personDao.findByName(headValue[0]);
            t.setPerson(p.get(0));
            List<Task> taskList = taskDao.findByPerson(p.get(0));
            for (Task t1 : taskList) {
                JsonObject jobj = new JsonObject();

                jobj.addProperty("id", String.valueOf(t1.getTaskId()));
                jobj.addProperty("description", t1.getDesc());
                jsonArray.add(jobj);
            }
            response.setStatus(201, "OK");
            return jsonArray.toString();
        } else {
            jsonObject.addProperty("message", "Authorized User Not Found");
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
            jsonObject.addProperty("message", "Authorized User Not Found");
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
            List<Person> p = personDao.findByName(headValue[0]);

            List<Task> taskList = taskDao.findByPerson(p.get(0));
            for (Task t1 : taskList) {
                System.out.println("The ID from request is  : " + id);
                if (t1.getTaskId().toString().equals(id)) {
                    t1.setDesc(task.getDesc());
                    t1.setPerson(p.get(0));
                    taskDao.save(t1);
                    jsonObject.addProperty("id", String.valueOf(t1.getTaskId()));
                    jsonObject.addProperty("description", t1.getDesc());
                    response.setStatus(200, "Created");
                    taskChecker = true;
                }
            }
            if (taskChecker) {
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
            , headers = {"content-type=application/json; charset=utf-8"})
    @ResponseBody
    public String DeleteTask(@PathVariable("id") String id, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
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
                System.out.println("The ID from request is  : " + id);
                System.out.println("The ID inside the method is " + t1.getTaskId());

                if (t1.getTaskId().toString().equals(id)) {

                    List<AttachmentData> yy = attachmentDao.findAttachmentDataByTask(t1);

                    for (AttachmentData a : yy) {

                        File f = new File(a.getContent());
                        f.delete();


                    }

                    attachmentDao.delete(yy);


                    taskDao.delete(t1);
                    jsonObject.addProperty("message", "deleted");

                    response.setStatus(204, "No Content");
                    taskChecker = true;
                }
            }
            if (taskChecker) {
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
        if (username == null || password == null) return false;
        String encrypt = bCryptPasswordEncoder.encode(password);
        List<Person> personList = personDao.findByName(username);
        if (personList.size() > 0) {

            //    for Bcrypt password from normal header
            if (bCryptPasswordEncoder.matches(password, personList.get(0).getPassword())) {
                return true;
            } else
                return false;

            // for Jmeter
//            if (password.equals(personList.get(0).getPassword())) {
//                return true;
//            } else
//                return false;

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


    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.POST, produces = {"application/json"}
    )
    @ResponseBody
    public String AddAttachment(@PathVariable("id") String id, @RequestParam("file") MultipartFile files,
                                @RequestParam("file_Path") String file_Path,
                                HttpServletRequest httpRequest, HttpServletResponse response)
            throws IOException {
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
                //String name = "/home/sumedh/assignment5/code1/" + files.getOriginalFilename();
                //String filepath = System.getProperty("user.dir");
                // filepath.concat(name);
                // files.transferTo(new File(file_Path));

//                AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
                AmazonS3 s3client = new AmazonS3Client(DefaultAWSCredentialsProviderChain.getInstance());
                Bucket b = null;

                String bucketname = "csye6225-fall2017-csye-nasp.com";

//                 String bucketname = "csye6225nasp";


//


                InputStream is = files.getInputStream();

                String contentType = files.getContentType();

                Long fileSize = files.getSize();

                ObjectMetadata meta = new ObjectMetadata();

                meta.setContentType(contentType);
                String key = files.getOriginalFilename();

                meta.setContentLength(fileSize);
                if (s3client.doesBucketExist(bucketname)) {
                    s3client.putObject(new PutObjectRequest("csye6225-fall2017-csye-nasp.com", key, is, meta));
                } else {
                    s3client.createBucket("csye6225-fall2017-csye-nasp.com");
                    s3client.putObject(new PutObjectRequest("csye6225-fall2017-csye-nasp.com", key, is, meta));
                }

//        File trans = new File("/home/snigdha/"+file.getOriginalFilename());

//        file.transferTo(trans);

//        System.out.println(trans);

//        PutObjectRequest req = new PutObjectRequest(bucketName, key, trans);

//        s3Client.putObject(req);


                //s3client.putObject("csye6225nasp","csye6225nasp", (File) files);
                AttachmentData ad = new AttachmentData();
                ad.setContent(file_Path);
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


    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.GET, produces = {"application/json"}
    )
    @ResponseBody
    public String getAttachments(@PathVariable("id") String id, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
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

    @RequestMapping(value = "/tasks/{taskId}/attachments/{attachementId}", method = RequestMethod.DELETE, produces = {"application/json"}
    )
    @ResponseBody
    public String deleteAttachment(@PathVariable("taskId") String taskId, @PathVariable("attachementId") String attachementId,
                                   HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        String[] headValue = HeaderCheck(httpRequest);
        boolean auth = false;
        boolean taskChecker = false;
        List<AttachmentData> alist = new ArrayList<AttachmentData>();
        List<Task> tlist = new ArrayList<Task>();

        if (headValue == null) {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("Response code", "The Basic Auth is not provided");
            return jsonObject.toString();
        } else {
            auth = checkAuth(headValue[0], headValue[1]);
        }


        if (auth) {
            System.out.println(UUID.fromString(taskId));
            //alist = attachmentDao.findByAttachId(UUID.fromString(attachementId));
            tlist = taskDao.findByTaskId(UUID.fromString(taskId));
            System.out.println(alist.size());
            if (tlist.size() > 0) {

                //List<Task> tlist1 = taskDao.findByTaskId(UUID.fromString(id));
                List<AttachmentData> ads = attachmentDao.findByAttachId(UUID.fromString(attachementId));
                JsonObject jobj = new JsonObject();
                if (ads.size() > 0) {
                    // for (AttachmentData tk : ads) {
                    AttachmentData nn = new AttachmentData();
                    nn.setContent(ads.get(0).getContent());
                    nn.setAttachId(ads.get(0).getAttachId());
                    attachmentDao.delete(nn);


//                    AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
                    AmazonS3 s3client = new AmazonS3Client(DefaultAWSCredentialsProviderChain.getInstance());
                    ObjectListing objectListing = s3client.listObjects("csye6225-fall2017-csye-nasp.com");

                    //  while (true) {
                    for (Iterator<?> iterator = objectListing.getObjectSummaries().iterator();
                         iterator.hasNext(); ) {
                        S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                        s3client.deleteObject("csye6225-fall2017-csye-nasp.com", nn.getContent());

                        jobj.addProperty("id", String.valueOf(ads.get(0).getAttachId()));
                        jobj.addProperty("url", ads.get(0).getContent());
                        jsonArray.add(jobj);
                    }
                    //}


                    //File file = new File(nn.getContent());
                    //file.delete();


                    // }
                } else {

                    response.setStatus(201, "Unauthorized");
                    jsonObject.addProperty("message", "No attachments found for the given taskId");

                }

                return jsonArray.toString();

            } else {

                response.setStatus(201, "Unauthorized");
                jsonObject.addProperty("message", "No tasks found");
                return jsonObject.toString();

            }


        } else {
            response.setStatus(401, "Unauthorized");
            jsonObject.addProperty("message", "Authorized User Not Found");
            return jsonObject.toString();

        }

    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public String forgotPassword(HttpServletRequest httpRequest, HttpServletResponse response) {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        String[] headValue = HeaderCheck(httpRequest);
        boolean auth = checkAuth(headValue[0], headValue[1]);

        if (headValue.equals(null)) {
            jsonObject.addProperty("message", "The Basic Auth is not provided");
            return jsonObject.toString();
        }


        if (auth) {

        }
        response.setStatus(201, "OK");
        return jsonArray.toString();


    }

}



