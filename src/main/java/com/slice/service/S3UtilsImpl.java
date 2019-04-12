package com.slice.service;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("s3Utils")
public class S3UtilsImpl implements S3Utils {

    private final static Logger LOGGER = LoggerFactory.getLogger(S3UtilsImpl.class);


    @Override
    public Map<String, String> readFromS3(String bucket_name, AmazonS3 s3) throws Exception {

        LOGGER.info("{} - reading objects from bucket", bucket_name);
        ObjectListing object_listing = s3.listObjects(bucket_name);
        Map<String, String> collectionJsonFromS3 = new HashMap<>();

        while (true) {
            for (Iterator<?> iterator =
                 object_listing.getObjectSummaries().iterator();
                 iterator.hasNext(); ) {
                S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                S3Object fullObject = s3.getObject(summary.getBucketName(), summary.getKey());
                collectionJsonFromS3.put(summary.getKey(), objectTextInputStream(fullObject.getObjectContent()));
            }
            if (object_listing.isTruncated()) {
                object_listing = s3.listNextBatchOfObjects(object_listing);
            } else {
                break;
            }
        }
        return collectionJsonFromS3;
    }

    @Override
    public void writeToS3(String bucketName, String path, String file, AmazonS3 s3) {

        try {
            s3.putObject(bucketName, path, file);
        } catch (AmazonServiceException e) {
            LOGGER.info(e.getErrorMessage());
            System.exit(1);
        }
        LOGGER.info("File {} saved", path);
    }

    private String objectTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}
