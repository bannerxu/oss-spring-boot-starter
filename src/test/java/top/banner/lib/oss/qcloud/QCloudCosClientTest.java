package top.banner.lib.oss.qcloud;


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
    public void sign() {
        System.out.println(qCloudCosClient.sign());
    }


    @SneakyThrows
    @Test
    public void upload() {
        System.out.println(qCloudCosClient.upload("https://img.tukuppt.com/png_preview/00/35/65/K3L3SnCFDl.jpg!/fw/780", "/system/user" + System.currentTimeMillis() + ".jpg"));
    }
}