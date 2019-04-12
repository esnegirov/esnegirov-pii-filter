package com.slice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.slice.dao.RegexpTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("replaceJob")
public class ReplaceJobImpl implements ReplaceJob {

    private ItemMapper orderMapper;

    private PiiFilter piiFilter;

    private S3Utils s3Utils;

    private RegexpTableRepository regexpTableRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceJobImpl.class);

    @Autowired
    public void ReplaceJob(ItemMapper orderMapper, PiiFilter piiFilter, S3Utils s3Utils, RegexpTableRepository regexpTableRepository) {
        this.orderMapper = orderMapper;
        this.piiFilter = piiFilter;
        this.s3Utils = s3Utils;
        this.regexpTableRepository = regexpTableRepository;
    }

    @Override
    public void startJob(String bucketName, AmazonS3 s3) {

        try {
            final Map<String, String> orderList = s3Utils.readFromS3(bucketName, s3);

            orderMapper.itemFromJson(orderList).forEach((fileName, item) -> {
                try {
                    s3Utils.writeToS3(bucketName, fileName, orderMapper.itemToJson(piiFilter.replacePii(item, regexpTableRepository.findAll())), s3);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("ERROR while work with S3 bucket, exit application");
            System.exit(1);
        }
    }

}
