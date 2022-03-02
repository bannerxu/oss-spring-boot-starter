package top.banner.lib.oss.qcloud;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.oss.qcloud")
public class QCloudProperties {
    /**
     * 地区
     */
    private String region;
    /**
     * 账户secretId
     */
    private String secretId;
    /**
     * 账户secretKey
     */
    private String secretKey;
    /**
     * bucket
     */
    private String bucket;
    /**
     * bucket 绑定的域名
     */
    private String urlPrefix;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getUrlPrefix() {
        if (urlPrefix.endsWith("/")) {
            urlPrefix = urlPrefix.substring(0, urlPrefix.lastIndexOf("/"));
        }
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
