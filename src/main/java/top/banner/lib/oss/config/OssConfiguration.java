package top.banner.lib.oss.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.banner.lib.oss.ali.AliOssClient;
import top.banner.lib.oss.ali.AliProperties;
import top.banner.lib.oss.core.OssClient;
import top.banner.lib.oss.qiniu.QiniuOssClient;
import top.banner.lib.oss.qiniu.QiniuProperties;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(OssClient.class)
@EnableConfigurationProperties({AliProperties.class, QiniuProperties.class})
public class OssConfiguration {

    @Resource
    private AliProperties aliProperties;
    @Resource
    private QiniuProperties qiniuProperties;

    @Bean
    public AliOssClient aliOssClient() {
        return new AliOssClient(aliProperties);
    }

    @Bean
    public QiniuOssClient qiniuOssClient() {
        return new QiniuOssClient(qiniuProperties);
    }

}
