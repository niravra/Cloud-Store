package com.csye6225.demo.controllers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSSessionCredentialsProvider;

public class AWSauth implements AWSCredentials {


    @Override
    public String getAWSAccessKeyId() {
        return "AKIAJGB4OJCNHSEHDBNQ";
    }

    @Override
    public String getAWSSecretKey() {
        return "C6EYy8Z1p0rqHY3ZwYLB63Pblur5I12y7E/v8Q4Z";
    }
}
