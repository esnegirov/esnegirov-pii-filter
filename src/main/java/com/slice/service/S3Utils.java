package com.slice.service;

import com.amazonaws.services.s3.AmazonS3;

import java.util.Map;

public interface S3Utils {

    Map<String ,String> readFromS3(String bucketName, AmazonS3 s3) throws Exception;

    void writeToS3 (String bucketName, String fileName, String file, AmazonS3 s3);


}
