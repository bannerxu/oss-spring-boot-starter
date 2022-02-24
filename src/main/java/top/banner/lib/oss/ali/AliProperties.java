package top.banner.lib.oss.ali;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.oss.ali")
public class AliProperties {
    private String region;
    /**
     * RAM用户 AccessKey ID
     * https://help.aliyun.com/document_detail/100624.html
     */
    private String accessKeyId;
    /**
     * RAM用户 AccessKey Secret
     * https://help.aliyun.com/document_detail/100624.html
     */
    private String accessKeySecret;
    /**
     * 临时访问凭证的角色
     * https://help.aliyun.com/document_detail/100624.html
     */
    private String roleArn;
    /**
     * bucket
     */
    private String bucket;
    /**
     * Bucket所在地域对应的Endpoint。
     * 以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
     */
    private String ossEndpoint;
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
