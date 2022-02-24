package top.banner.lib.oss.ali;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.auth.sts.AssumeRoleResponse.Credentials;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import top.banner.lib.oss.core.OssClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 阿里云oss https://help.aliyun.com/document_detail/84837.html
 */
public class AliOssClient implements OssClient {
    private static final Logger log = LoggerFactory.getLogger(AliOssClient.class);

    private final AliProperties aliProperties;

    public AliOssClient(AliProperties aliProperties) {
        this.aliProperties = aliProperties;
    }

    @Override
    public String upload(MultipartFile file, String key) throws IOException {
        InputStream inputStream = file.getInputStream();
        return upload(inputStream, key);
    }


    /**
     * 通过流上传，并关闭输入流
     */
    @Override
    public String upload(InputStream inputStream, String key) throws IOException {
        key = removeFirstSlash(key);
        OSS ossClient = getOSSClient();
        // 上传文件流。
        try {
            PutObjectResult putObjectResult = ossClient.putObject(aliProperties.getBucket(), key, inputStream);
            ObjectMetadata objectMetadata = ossClient.headObject(aliProperties.getBucket(), key);
            if (!putObjectResult.getETag().equals(objectMetadata.getETag())) {
                throw new IllegalArgumentException("文件上传失败");
            }
            return aliProperties.getUrlPrefix() + File.separator + key;
        } finally {
            ossClient.shutdown();
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    /**
     * 网络资源上传
     *
     * @param url 网络资源地址
     * @param key 文件路径+文件名
     * @return 文件地址
     */
    @Override
    public String upload(String url, String key) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        return upload(inputStream, key);
    }

    @Override
    public Boolean existByKey(String key) {
        key = removeFirstSlash(key);
        OSS ossClient = getOSSClient();
        try {
            return ossClient.doesObjectExist(aliProperties.getBucket(), key);
        } catch (OSSException | com.aliyun.oss.ClientException e) {
            log.error("query file by key error", e);
            return false;
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public Boolean existByUrl(String url) {
        return existByKey(url.replace(aliProperties.getUrlPrefix() + "/", ""));
    }

    @Override
    public void deleteByKey(String key) {
        key = removeFirstSlash(key);
        OSS ossClient = getOSSClient();
        try {
            ossClient.deleteObject(aliProperties.getBucket(), key);
        } catch (OSSException | com.aliyun.oss.ClientException e) {
            log.error("delete file by key error", e);
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void deleteByUrl(String url) {
        deleteByKey(url.replace(aliProperties.getUrlPrefix() + "/", ""));
    }

    /**
     * https://help.aliyun.com/document_detail/100624.html
     */
    @SuppressWarnings("unchecked")
    @Override
    public Credentials sign() {
        // STS接入地址，例如sts.cn-hangzhou.aliyuncs.com。
        String endpoint = "sts.cn-hangzhou.aliyuncs.com";
        // 填写步骤1生成的访问密钥AccessKey ID和AccessKey Secret。
        String AccessKeyId = aliProperties.getAccessKeyId();
        String accessKeySecret = aliProperties.getAccessKeySecret();
        // 填写步骤3获取的角色ARN。
        String roleArn = aliProperties.getRoleArn();
        // 自定义角色会话名称，用来区分不同的令牌，例如可填写为SessionTest。
        String roleSessionName = "system-java";

        try {
            // regionId表示RAM的地域ID。以华东1（杭州）地域为例，regionID填写为cn-hangzhou。也可以保留默认值，默认值为空字符串（""）。
            String regionId = "";

            // 添加endpoint。适用于Java SDK 3.12.0以下版本。
            DefaultProfile.addEndpoint(regionId, "Sts", endpoint);
            // 构造default profile。
            IClientProfile profile = DefaultProfile.getProfile(regionId, AccessKeyId, accessKeySecret);
            // 构造client。
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            // 适用于Java SDK 3.12.0以下版本。
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(null); // 如果policy为空，则用户将获得该角色下所有权限。
            request.setDurationSeconds(3600L); // 设置临时访问凭证的有效时间为3600秒。
            final AssumeRoleResponse response = client.getAcsResponse(request);
            log.debug("Expiration: " + response.getCredentials().getExpiration());
            log.debug("Access Key Id: " + response.getCredentials().getAccessKeyId());
            log.debug("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
            log.debug("Security Token: " + response.getCredentials().getSecurityToken());
            log.debug("RequestId: " + response.getRequestId());
            return response.getCredentials();
        } catch (ClientException e) {
            log.error("Error code: {} Error message: {} RequestId: {}", e.getErrCode(), e.getErrMsg(), e.getRequestId(), e);
        }
        throw new IllegalArgumentException("无法获取SecurityToken");
    }


    /**
     * 获取图片上传客户端
     */
    public OSS getOSSClient() {
        //return new OSSClientBuilder().build(endpoint, aliProperties.getAccessKeyId(), aliProperties.getAccessKeySecret());
        Credentials token = sign();
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = aliProperties.getOssEndpoint();
        // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
        String accessKeyId = token.getAccessKeyId();
        String accessKeySecret = token.getAccessKeySecret();
        // 从STS服务获取的安全令牌（SecurityToken）。
        String securityToken = token.getSecurityToken();
        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, securityToken);
    }
}
