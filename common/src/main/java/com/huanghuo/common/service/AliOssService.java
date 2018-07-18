package com.huanghuo.common.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

/**
 * Created by huangcheng on 2018/7/16.
 */

@Service
public class AliOssService {

    @Value("${alioss.bucketname}")
    private String bucketName;

    @Value("${alioss.directory}")
    private String directory;

    @Value("${alioss.region}")
    private String region;

    @Autowired
    private OSSClient ossClient;

    @PostConstruct
    void init(){
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
            ossClient.createBucket(createBucketRequest);
        }
    }

    public String upload(InputStream input){
        PutObjectResult result = ossClient.putObject(bucketName, directory, input);
        return result.getRequestId();
    }

}
