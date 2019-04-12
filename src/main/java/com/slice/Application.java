package com.slice;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.slice.service.ItemMapperImpl;
import com.slice.service.ReplaceJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.Arrays;

@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion("us-east-1")
                .build();
    }

    @Bean
    public CommandLineRunner demo(ReplaceJob replaceJob) {
        return (args) -> {
            if (args.length > 0) {
                Arrays.asList(args).forEach(item -> replaceJob.startJob(item, s3Client()));
            }else{
                LOGGER.info("No args was found.");
                LOGGER.info("Please insert s3 bucket name or use 'esnegirov-test' for example");
                LOGGER.info("Or start 'gradle bootRun -Pargs=<your bucket>,<your bucket>'");
            }
        };
    }
}
