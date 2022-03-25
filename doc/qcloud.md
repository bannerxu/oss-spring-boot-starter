# [授权子账号访问 COS，获取秘钥](https://portal.qiniu.com/user/key)

当使用子账号通过编程（例如 API、SDK 和工具等）访问 COS 资源时需要先获取主账号的 APPID、子账号的 SecretId 和 SecretKey 信息。您可以在访问管理控制台生成子账号的 SecretId 和
SecretKey。

主账号登录 [CAM 控制台](https://console.cloud.tencent.com/cam/capi) 。 选择【用户列表】，进入用户列表页面。 单击子账号用户名称，进入子账号信息详情页。 单击【API
密钥】页签，并单击【新建密钥】为该子账号创建 **SecretId** 和 **SecretKey**。

# 如何配置

```properties
spring.oss.qcloud.secret-id=
spring.oss.qcloud.secret-key=
spring.oss.qcloud.bucket=bannerxu-12510998
spring.oss.qcloud.region=ap-guangzhou
spring.oss.qcloud.url-prefix=https://bannerxu-12510998.cos.ap-guangzhou.myqcloud.com
```

- secret-id SecretId
- secret-key SecretKey
- bucket bucket
- region 存储区域,参见[地域和访问域名](https://cloud.tencent.com/document/product/436/6224)
- url-prefix bucket对应的图片访问域名

# 签名结果

> 请求方式：

```java
@Test
public void sign()throws JsonProcessingException{
        Response sign=qCloudCosClient.sign();
        System.out.println(new ObjectMapper().writeValueAsString(sign));
        }
```

> 响应：

```json
{
  "credentials": {
    "tmpSecretId": "AKID-fJ4skGOB27WQc4gw20lPN-QYEIWFPRZa02gXmLMHXgmS2c5tlrarDTsPTO_1ZTo",
    "tmpSecretKey": "/OP1NoEBk0GDWUORkWLAH4Cw+nejhe9aPbm4xM7XI/U=",
    "sessionToken": "Sjo8UlF4CSmh3d4gtq0tlhQNOCJnXhVa72e0cec4ade1c3bf06f3045f728f99f4dDXvLvY0Cck73MYjeP96iPsCB5XRdWYVgF5jCdhnkgiSC04T87CRwCdpQ06AClnR8vPKoLAJSq_F6Z1C5kw_Z4av5q3RxpE9W2qYKGTuNvSp5a6OMFR3CoDel2WuF_rNEV8hc9qX5kcKnkD9mPOKzssyrA9didSR-2uSdKMnvmb7iWLPGcxGv3I4OZFF0CqzZ7afa59jpsoDBDdxJrnTKwacp1vVR4cD4FO3IU1OCxgmgXvbX08jTGD5IXs66evj5mm5DjI9OPg4h5HLIuY1zREGJmSf4hMUEQDVYIpEKPuwkiFmsyMi-tdPgRvXho9tjwiuR0vnqkWBha1T_CjFlM_lK0JFw0i3RXutESh_Z2oKnIQhtlZ1TZXtV22ikHw3h8R61QyzDyPl_aOmHMn7_seo4SMowHQ_mUiK--kWgjONSQruGCiEPBsv-0xSVUhsI4uyHSvjDAQsXgJETeLpYMNv1ROvve8ARm3oQ6T5JVbrqFbJcaDDuBAI3WAJNIAvt182v8Isy2vcBLftt9c6Dr1i9sVrkT1lmslc9Y9Geu4iMUaZOsMGw3yyQ2WqjPJrocCGUJRhFbcuX6Jka5VpdloYKPahkYAPac1qzT2rwaVlz8lsGfRpV-DMDjJlBHDtChQgNgqlyVJGZJ-LXxMR14Y5qCyVfzkgx-9CNIxmb4Y"
  },
  "requestId": "c3a4db4a-31bb-401f-accb-4c92154610cd",
  "expiration": "2022-03-02T03:10:34Z",
  "startTime": 1646188834,
  "expiredTime": 1646190634
}
```