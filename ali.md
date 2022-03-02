# 使用STS临时访问凭证访问OSS

前提：按照文档 [使用STS临时访问凭证访问OSS](https://help.aliyun.com/document_detail/100624.html) 获取
**一个安全令牌（SecurityToken）、临时访问密钥（AccessKeyId和AccessKeySecret）以及过期时间。**

# 如何配置

```properties
spring.oss.ali.region=oss-ap-southeast-1
spring.oss.ali.access-key-id=xxx
spring.oss.ali.access-key-secret=xxx
spring.oss.ali.role-arn=xxx
spring.oss.ali.bucket=xxx
spring.oss.ali.oss-endpoint=https://oss-accelerate.aliyuncs.com
spring.oss.ali.url-prefix=https://abc.oss-accelerate.aliyuncs.com
```

- region
  Region表示OSS的数据中心所在物理位置。详情请参见[OSS已经开通的Region](https://help.aliyun.com/document_detail/31837.htm#concept-zt4-cvy-5db)。
- access-key-id RAM用户 AccessKey ID
- access-key-secret RAM用户 AccessKey Secret
- role-arn 临时访问凭证的角色
- bucket bucket
- oss-endpoint Bucket所在地域对应的Endpoint。详情请参见[访问域名和数据中心](https://help.aliyun.com/document_detail/31837.html)
- url-prefix bucket对应的图片访问域名

# 为什么要使用STS，而不是使用主账号的

- STS服务给其他用户颁发一个临时访问凭证。
- RAM用户权限粒度更细。一般情况下，只配置OSS的上传权限。
- 临时访问凭证具有时效性。

# 签名结果
> 请求方式：
```java
@Test
public void aliSign()throws JsonProcessingException{
        AssumeRoleResponse.Credentials sign=aliOssClient.sign();
        System.out.println(new ObjectMapper().writeValueAsString(sign));
        }
```

> 响应：
```json
{
  "securityToken": "CAIS7QF1q6Ft5B2yr5fzJMuGnbFrw4yASGz5h2siZvhK2ZOYozz2IH1NenNuB+8etfQwnW9R6/4ZlqJoWoRZSEmBb8Ju58zveq4N/82T1fau5Jko1beHewHKeTOZsebWZ+LmNqC/Ht6md1HDkAJq3LL+bk/Mdle5MJqP+/UFB5ZtKWveVzddA8pMLQZPsdITMWCrVcygKRn3mGHdfiEK00he8T4gtvzhmJbAsECF1Q2gk7Evyt6vcsj0Xa5FJ4xiVtq55utye5fa3TRYgxowr/ov0vYap2if5YHCWgYIvk3dKYHP7sZ/zOrwt1/Z08wagAFDr3T+5YMJ/INlVo/gki50HHQdWO9qXTkT3XX+eA0e2NtAWXndLXvzuF+cfyhYAnDHpFLhhyVVzS90prgFwns4baO5q+CxDE3Qmc/ZUN6uNggKx7/DFhKra0pqncApPQna77JTfpVoK/2myERJeSlprLutUWX8HE0k0mHqM23shQ==",
  "accessKeySecret": "F8tcZoYecBHjzQgb1G2HTm7RfP8wjWvZPXMooSWdGZ",
  "accessKeyId": "STS.NTFoq2pnJtNkJckwjwf6V3A",
  "expiration": "2022-03-02T02:59:25Z"
}
```