package top.banner.lib.oss.qcloud;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import top.banner.lib.oss.core.OssClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.TreeMap;

public class QCloudCosClient implements OssClient {
    private static final Logger log = LoggerFactory.getLogger(QCloudCosClient.class);

    private final QCloudProperties qCloudProperties;

    public QCloudCosClient(QCloudProperties qCloudProperties) {
        this.qCloudProperties = qCloudProperties;
    }

    @Override
    public String upload(MultipartFile file, String key) throws IOException {
        InputStream inputStream = file.getInputStream();
        return upload(inputStream, key);
    }

    @Override
    public String upload(InputStream inputStream, String key) throws IOException {

        COSClient cosClient = createCOSClient();

        PutObjectRequest putObjectRequest = new PutObjectRequest(qCloudProperties.getBucket(), key, inputStream, new ObjectMetadata());

        try {
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            log.info("QCloud Cos upload file success requestId：{} etag：{}", putObjectResult.getRequestId(), putObjectResult.getETag());

            return qCloudProperties.getUrlPrefix() + File.separator + key;
        } catch (CosClientException e) {
            log.error("QCloud Cos upload fail", e);
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
            if (inputStream != null) {
                inputStream.close();
            }
        }
        throw new IllegalArgumentException("文件上传失败");
    }

    @Override
    public String upload(String url, String key) throws IOException {
        key = removeFirstSlash(key);
        InputStream inputStream = new URL(url).openStream();
        return upload(inputStream, key);
    }

    @Override
    public Boolean existByKey(String key) {
        COSClient cosClient = createCOSClient();
        try {
            return cosClient.doesObjectExist(qCloudProperties.getBucket(), key);
        } catch (CosClientException ignored) {
        } finally {
            cosClient.shutdown();
        }
        return false;

    }

    @Override
    public void deleteByKey(String key) {
        COSClient cosClient = createCOSClient();
        try {
            cosClient.deleteObject(qCloudProperties.getBucket(), key);
        } catch (CosClientException e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public Response sign() {
        TreeMap<String, Object> config = new TreeMap<>();
        try {
            // 云 api 密钥 SecretId
            config.put("secretId", qCloudProperties.getSecretId());
            // 云 api 密钥 SecretKey
            config.put("secretKey", qCloudProperties.getSecretKey());

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", 1800);

            // 换成你的 bucket
            config.put("bucket", qCloudProperties.getBucket());
            // 换成 bucket 所在地区
            config.put("region", qCloudProperties.getRegion());

            // 可以通过 allowPrefixes 指定前缀数组, 例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险, 请谨慎评估使用)
            config.put("allowPrefixes", new String[]{
                    "*"
            });

            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    //简单上传操作
                    "name/cos:PutObject",
                    //表单上传对象
                    "name/cos:PostObject",
                    //分块上传：初始化分块操作
                    "name/cos:InitiateMultipartUpload",
                    //分块上传：List 进行中的分块上传
                    "name/cos:ListMultipartUploads",
                    //分块上传：List 已上传分块操作
                    "name/cos:ListParts",
                    //分块上传：上传分块块操作
                    "name/cos:UploadPart",
                    //分块上传：完成所有分块上传操作
                    "name/cos:CompleteMultipartUpload",
                    //取消分块上传操作
                    "name/cos:AbortMultipartUpload",
                    "name/cos:DeleteObject"
            };
            config.put("allowActions", allowActions);

            Response response = CosStsClient.getCredential(config);
            log.info("tmpSecretId：{}", response.credentials.tmpSecretId);
            log.info("tmpSecretKey：{}", response.credentials.tmpSecretKey);
            log.info("sessionToken：{}", response.credentials.sessionToken);
            return response;
        } catch (Exception e) {
            log.error("Tencent Cloud sign failure", e);
        }
        return null;
    }

    private COSClient createCOSClient() {
        Response response = sign();
        // 1 传入获取到的临时密钥 (tmpSecretId, tmpSecretKey, sessionToken)
        BasicSessionCredentials cred = new BasicSessionCredentials(response.credentials.tmpSecretId,
                response.credentials.tmpSecretKey, response.credentials.sessionToken);
        // 2 设置 bucket 的地域, COS 地域的简称请参阅 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参阅源码或者常见问题 Java SDK 部分
        ClientConfig clientConfig = new ClientConfig(new Region(qCloudProperties.getRegion()));
        // 3 生成 cos 客户端
        return new COSClient(cred, clientConfig);
    }
}
