package com.lj.blog.config;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Value("${spark.app.name}")
    private String appName;
    @Value("${spark.master.uri}")
    private String sparkMasterUri;

    @Bean
    public SparkConf sparkConf() {
        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(sparkMasterUri);
        return sparkConf;
    }


    @Bean
    public SparkSession sparkSession() {
        return SparkSession
                .builder()
                .config(sparkConf())
                .getOrCreate();
    }
}

