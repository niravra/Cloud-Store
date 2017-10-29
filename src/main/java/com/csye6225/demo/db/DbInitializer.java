package com.csye6225.demo.db;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.csye6225.demo.entity.Customer;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;


public class DbInitializer {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;




    private static final String EXPECTED_COST = "20";
    private static final String EXPECTED_PRICE = "50";


    public void setup() throws Exception {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        System.out.println("THE AMA IS :     " +amazonDynamoDB);


        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(Customer.class);
        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);

        //...

//        dynamoDBMapper.batchDelete(
//                (List<ProductInfo>)repository.findAll());
    }
}
