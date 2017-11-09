package com.csye6225.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface DynamoDao<DynamoToken, String extends Serializable> extends CrudRepository<DynamoToken, String>{


}
