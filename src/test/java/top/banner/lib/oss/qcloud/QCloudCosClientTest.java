package top.banner.lib.oss.qcloud;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.cloud.Response;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QCloudCosClientTest {

    @Resource
    private QCloudCosClient qCloudCosClient;

    @Test
    public void sign() throws JsonProcessingException {
        Response sign = qCloudCosClient.sign();
        System.out.println(new ObjectMapper().writeValueAsString(sign));
    }


    @SneakyThrows
    @Test
    public void upload() {
        System.out.println(qCloudCosClient.upload("https://img.tukuppt.com/png_preview/00/35/65/K3L3SnCFDl.jpg!/fw/780", "/system/user" + System.currentTimeMillis() + ".jpg"));
    }
}