package top.banner.lib.oss.qiniu;

import com.qiniu.storage.Region;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.oss.qiniu")
public class QiniuProperties {
    private String region;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String urlPrefix;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
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

    public Region getRegion() {
        switch (region) {
            case "华东":
                return Region.huadong();
            case "华北":
                return Region.huabei();
            case "华南":
                return Region.huanan();
            case "北美":
                return Region.beimei();
            case "东南亚":
                return Region.xinjiapo();
            default:
                return Region.autoRegion();
        }
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
