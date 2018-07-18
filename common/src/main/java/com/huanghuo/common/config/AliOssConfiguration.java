package com.huanghuo.common.config;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.comm.Protocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by huangcheng on 2018/7/16.
 */

@Configuration
public class AliOssConfiguration {
    @Value("${alioss.accesskeyid}")
    private String accessKeyId;

    @Value("${alioss.accesskeysecret}")
    private String accessKeySecret;

    @Value("${alioss.endpoint}")
    private String endpoint;

    @Bean
    public ClientConfiguration config(){
        ClientConfiguration conf = new ClientConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
                conf.setMaxConnections(200);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
                conf.setSocketTimeout(10000);
        // 设置建立连接的超时时间，默认为50000毫秒。
                conf.setConnectionTimeout(10000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
                conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
                conf.setIdleConnectionTime(10000);
        // 设置失败请求重试次数，默认为3次。
                conf.setMaxErrorRetry(5);
        // 设置是否支持将自定义域名作为Endpoint，默认支持。
                conf.setSupportCname(true);
        // 设置是否开启二级域名的访问方式，默认不开启。
                conf.setSLDEnabled(true);
        // 设置连接OSS所使用的协议（HTTP/HTTPS），默认为HTTP。
                conf.setProtocol(Protocol.HTTPS);
        return conf;
    }

    @Bean
    public OSSClient ossClient(OssClientFactoryBean bean) throws Exception{
        return bean.getObject();
    }

    @Bean
    public OssClientFactoryBean ossClientFactoryBean(ClientConfiguration config){
        OssClientFactoryBean bean = new OssClientFactoryBean();
        bean.setAccessKeyId(accessKeyId);
        bean.setAccessKeySecret(accessKeySecret);
        bean.setEndpoint(endpoint);
        bean.setConfiguration(config);
        return bean;
    }
}
