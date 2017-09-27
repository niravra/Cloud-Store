package com.csye6225.demo.dao;

import com.csye6225.demo.entity.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonDao extends CrudRepository<Person, Long>{

    List<Person> findByEmail(String email);

}
