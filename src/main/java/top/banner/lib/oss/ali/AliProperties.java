package top.banner.lib.oss.ali;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.oss.ali")
public class AliProperties {
    private String region;
    private String accessKeyId;
    private String accessKeySecret;
    private String roleArn;
    private String bucket;
    private String ossEndpoint;
    private String urlPrefix;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOssEndpoint() {
        return ossEndpoint;
    }

    public void setOssEndpoint(String ossEndpoint) {
        this.ossEndpoint = ossEndpoint;
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
