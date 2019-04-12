package com.slice.service;

import com.amazonaws.services.s3.AmazonS3;

public interface ReplaceJob {
    void startJob(String bucketName, AmazonS3 s3);
}
