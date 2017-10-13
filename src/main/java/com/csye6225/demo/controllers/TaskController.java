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

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public String registerTask(HttpServletRequest httpRequest) {

        JsonObject jsonObject = new JsonObject();
        String[] values = new String[2];
        final String authorization = httpRequest.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
             values = credentials.split(":", 2);

//            inMemoryUserDetailsManager.createUser(User.withUsername(values[0]).password(bCryptPasswordEncoder.encode(values[1])).roles("USER").build());

            System.out.println("The Header is " +values[0]);
            System.out.println("The Header is " +values[1]);
            boolean auth = checkAuth(values[0],values[1]);

            if (auth){
                jsonObject.addProperty("message", "Task created");
                return jsonObject.toString();
            }
                else
            {
                jsonObject.addProperty("message", "Not Authorized User Not Found");
                return jsonObject.toString();
            }
        }
        else
            jsonObject.addProperty("message", "The Basic Auth is not provided");
        return jsonObject.toString();



    }

    public boolean checkAuth(String username, String password){

        String encrypt = bCryptPasswordEncoder.encode(password);
        List<Person> personList = personDao.findByName(username);
        if (personList.size() > 0) {
            if (bCryptPasswordEncoder.matches(password,personList.get(0).getPassword())){
                return true;
            } else
                return false;

        }
        else
            return false;
    }
}
