package com.huanghuo.common.config;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Created by huangcheng on 2018/7/17.
 */

public class OssClientFactoryBean implements FactoryBean<OSSClient>, InitializingBean, DisposableBean {

    private OSSClient ossClient;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private ClientConfiguration configuration;
    @Override
    public OSSClient getObject() throws Exception {
        return this.ossClient;
    }

    @Override
    public Class<?> getObjectType() {
        return OSSClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        if (this.ossClient != null) {
            this.ossClient.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.endpoint, "'endpoint' must be not null");
        Assert.notNull(this.accessKeyId, "'accessKeyId' must be not null");
        Assert.notNull(this.accessKeySecret, "'accessKeySecret' must be not null");
        this.ossClient = new OSSClient(this.endpoint, this.accessKeyId, this.accessKeySecret, this.configuration);
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessKeyId(final String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(final String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public ClientConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ClientConfiguration configuration) {
        this.configuration = configuration;
    }
}
