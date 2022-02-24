package top.banner.lib.oss;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upyun.UpException;
import com.upyun.UpYunUtils;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.banner.lib.oss.core.OssClient;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OssClientApplicationTests {
    @Resource
    private OssClient aliOssClient;

    @Resource
    private OssClient qiniuOssClient;

    @SneakyThrows
    @Test
    public void aliUpload() {
        System.out.println(aliOssClient.upload("https://img.tukuppt.com/png_preview/00/35/65/K3L3SnCFDl.jpg!/fw/780", "/system/user/1645613971545" + System.currentTimeMillis()));
    }

    @Test
    public void aliSign() throws JsonProcessingException {
        AssumeRoleResponse.Credentials sign = aliOssClient.sign();
        System.out.println(new ObjectMapper().writeValueAsString(sign));
    }

    @SneakyThrows
    @Test
    public void aliExist() {
        System.out.println(aliOssClient.existByUrl("https://img.tukuppt.com/png_preview/00/35/65/K3L3SnCFDl.jpg!/fw/780"));
    }

    @Test
    public void qiniuSign() {
        String sign = qiniuOssClient.sign();
        System.out.println(sign);
    }

    @SneakyThrows
    @Test
    public void qiniuUpload() {
        String s = "https://img.tukuppt.com/png_preview/00/35/65/K3L3SnCFDl.jpg!/fw/780";
        System.out.println(qiniuOssClient.upload(s, "/system/user/1645613971545"));
        System.out.println(qiniuOssClient.upload("https://xiaotua.com/images/headers/header.jpg", "/system/user/1645613971545"));
    }


    @SneakyThrows
    @Test
    public void qiniuExist() {
        System.out.println(qiniuOssClient.existByKey("system/user/1645613971545"));
        qiniuOssClient.deleteByKey("system/user/1645613971545");
        qiniuOssClient.deleteByKey("system/user/1645613971545");
        System.out.println(qiniuOssClient.existByKey("system/user/1645613971545"));
    }

    static final String HOST = "https://v0.api.upyun.com";
    static final String bucketName = "bannerxu";


    String bucket = "api-apply-for-bucket";
    String hello = "hello";
    String password = "zuovGQug6TQ5zVcjHwQOwgI3eO7SqR92";

    /**
     * https://www.yuque.com/u160746/fk96qt/dtxsm9
     */
    @Test
    public void upYunSign() throws UpException {
        Map<String, Object> map = new HashMap<>();
        map.put("bucket", "sd");
        map.put("save-key", 1);
        map.put("expiration", 1);
        String policy = UpYunUtils.getPolicy(map);
        System.out.println(policy);

        String url = HOST + UpYunUtils.formatPath(bucketName, "/abc/123");
        System.out.println("url： " + url);
        String uri = HttpUrl.get(url).encodedPath();
        System.out.println("uri： " + uri);
        String sign = UpYunUtils.sign("GET", UpYunUtils.getGMTDate(), uri, "bannerxu001", "RvnhjV8Fv254oni9BlK0Wd5EDsLENSMK", null);

        String signature = UpYunUtils.getSignature(policy, sign);
        System.out.println(signature);
    }

}
