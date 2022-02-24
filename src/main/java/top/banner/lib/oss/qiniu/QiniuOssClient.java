package top.banner.lib.oss.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import top.banner.lib.oss.core.OssClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * https://developer.qiniu.com/kodo/1239/java#install-by-maven
 */
public class QiniuOssClient implements OssClient {
    private static final Logger log = LoggerFactory.getLogger(QiniuOssClient.class);

    private final QiniuProperties qiniuProperties;

    public QiniuOssClient(QiniuProperties qiniuProperties) {
        this.qiniuProperties = qiniuProperties;
    }


    @Override
    public String upload(MultipartFile file, String key) throws IOException {
        InputStream inputStream = file.getInputStream();
        return upload(inputStream, key);
    }

    @Override
    public String upload(InputStream inputStream, String key) {
        key = removeFirstSlash(key);

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(qiniuProperties.getRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String upToken = sign();
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("qiniu upload file success key：{} hash：{}", putRet.key, putRet.hash);
            return qiniuProperties.getUrlPrefix() + File.separator + key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("qiniu upload error：{}", r.toString());
            try {
                log.error("qiniu upload error：{}", r.bodyString());
            } catch (QiniuException ignored) {
            }
        }
        return null;
    }

    @Override
    public String upload(String url, String key) throws IOException {
        BucketManager bucketManager = getBucketManager();
        FetchRet fetchRet = bucketManager.fetch(url, qiniuProperties.getBucket(), key);
        log.info("qiniu upload file success key：{} hash：{}", fetchRet.key, fetchRet.hash);
        return qiniuProperties.getUrlPrefix() + File.separator + key;
    }

    @Override
    public Boolean existByKey(String key) {
        key = removeFirstSlash(key);
        BucketManager bucketManager = getBucketManager();
        try {
            FileInfo fileInfo = bucketManager.stat(qiniuProperties.getBucket(), key);
            log.debug("file-hash：{}", fileInfo.hash);
            log.debug("file-size：{}", fileInfo.fsize);
            log.debug("file-mine-type：{}", fileInfo.mimeType);
            log.debug("file-put-time：{}", fileInfo.putTime);
            return true;
        } catch (QiniuException e) {
            Response r = e.response;
            log.error("query file by key error：{}", r.toString());
            try {
                log.error("query file by key error：{}", r.bodyString());
            } catch (QiniuException ignored) {
            }
        }
        return false;
    }

    private BucketManager getBucketManager() {
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        Configuration cfg = new Configuration(qiniuProperties.getRegion());
        return new BucketManager(auth, cfg);
    }

    @Override
    public Boolean existByUrl(String url) {
        return existByKey(url.replace(qiniuProperties.getUrlPrefix() + "/", ""));
    }

    @Override
    public void deleteByKey(String key) {
        final BucketManager bucketManager = getBucketManager();
        try {
            bucketManager.delete(qiniuProperties.getBucket(), key);
        } catch (QiniuException e) {
            Response r = e.response;
            log.error("delete file by key error：{}", r.toString());
            try {
                log.error("delete file by key error：{}", r.bodyString());
            } catch (QiniuException ignored) {
            }
        }
    }

    @Override
    public void deleteByUrl(String url) {
        deleteByKey(url.replace(qiniuProperties.getUrlPrefix() + "/", ""));
    }

    @SuppressWarnings("unchecked")
    @Override
    public String sign() {
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        String token = auth.uploadToken(qiniuProperties.getBucket());
        log.debug("create token success ：{}", token);
        return token;
    }
}