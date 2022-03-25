# [获取七牛云秘钥](https://portal.qiniu.com/user/key)

密钥（AccessKey/SecretKey） 密钥用于几种凭证的生成。以 SecretKey 为参数，配合适当的签名算法，可以得到原始信息的数字签名，防止内容在传递过程中被伪造或篡改。

密钥通常是成对创建和使用，包含一个 **AccessKey** 和一个 **SecretKey**。其中 **AccessKey** 会在传输中包含，而用户必须保管好 **SecretKey** 不在网络上传输以防止被窃取。若 **
SecretKey** 被恶意第三方窃取，可能导致非常严重的数据泄漏风险。因此，如发现 **SecretKey**
被非法使用，管理员应第一时间在七牛开发者平台的[密钥管理](https://portal.qiniu.com/user/key)中更换密钥。

# 如何配置

```properties
spring.oss.qiniu.access-key=
spring.oss.qiniu.secret-key=
spring.oss.qiniu.bucket=book001
spring.oss.qiniu.region=z2
spring.oss.qiniu.url-prefix=http://book001.xuguoliang.top
```

- access-key 密钥Key
- secret-key 密钥Secret
- bucket bucket
- region 存储区域,参见[存储区域](https://developer.qiniu.com/kodo/1671/region-endpoint-fq)
- url-prefix bucket对应的图片访问域名

# 签名结果
> 请求方式：
```java
@Test
public void qiniuSign(){
        String sign=qiniuOssClient.sign();
        System.out.println(sign);
        }
```

> 响应：
```text
F8tcZoYecBHjzQgbssdfsa1G2HTm7RfP8wjWvZPXMooSWdGZ
```