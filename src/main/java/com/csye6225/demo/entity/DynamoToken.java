package com.csye6225.demo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import javax.persistence.Entity;


@DynamoDBTable(tableName = "csye6225")
public class DynamoToken {

private String userName;

private String passwordToken;

private String ttl;

    @DynamoDBAttribute
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @DynamoDBHashKey
    public String getPasswordToken() {
        return passwordToken;
    }


    public void setPasswordToken(String passwordToken) {
        this.passwordToken = passwordToken;
    }

    @DynamoDBAttribute
    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
}
