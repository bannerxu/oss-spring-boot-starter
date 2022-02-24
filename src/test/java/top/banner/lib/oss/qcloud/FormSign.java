package top.banner.lib.oss.qcloud;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;

public class FormSign {

    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data 被签名的字符串
     * @param key  秘钥
     * @return HMAC-SHA1 返回的原生二进制数据进行 Base64 编码后的字符串
     */
    public static String genrateHmacSha1(String data, String key) {
        try {
            // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            // 生成一个指定 Mac 算法的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            // 用给定密钥初始化 Mac 对象
            mac.init(signKey);
            // 取得原生二进制数据
            byte[] rawHmac = mac.doFinal(data.getBytes());
            // Base 64 编码 mac 结果
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        return "";
    }

    /**
     * /** 计算 MD5 值
     *
     * @param msg 加密内容
     * @return 32 位小写字符串
     */
    public static String md5(String msg) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(msg.getBytes(StandardCharsets.UTF_8));
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5 = new BigInteger(1, md.digest()).toString(16);
            // BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
        }
    }

    public static String fillMD5(String md5) {
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }

    /**
     * 制作签名
     *
     * @param operator 操作员名
     * @param password 操作员密码
     * @param method   请求方法
     * @param uri      请求资源路径
     * @param policy   上传参数的 Base64 编码
     * @param md5      文件的 MD5 值
     */
    public static String sign(String operator, String password, String method, String uri, String policy, String md5) {
        /* 取得密码的 MD5 值 */
        String key = md5(password);
        System.out.println("MD5_PASSWORD：" + key);


        /* 请求方式必须为大写单词 */
        method = method.toUpperCase();

        /* 按照文档顺序拼接参数 */
        String data = method + "&" + uri;

        /* 如果 policy 不为空则拼接 */
        if (!policy.isEmpty()) {
            data = data + "&" + policy;
        }
        /* 如果文件 MD5 值不为空则拼接 */
        if (!md5.isEmpty()) {
            data = data + "&" + md5;
        }

        /* 加密数据 */
        String signature = genrateHmacSha1(data, key);

        return "UPYUN " + operator + ":" + signature;
    }

    /**
     * 获取此刻到指定几个月后的 unix 时间戳
     *
     * @param month 需要叠加的月份
     */
    public static String getUnixTime(String month) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, Integer.parseInt(month));
        // 将毫秒转换成秒
        return String.valueOf((cal.getTimeInMillis() / 1000));
    }

    /**
     * 生成 policy
     */
    public static String policy(String bucket, String save_key) {

        //long expiration = System.currentTimeMillis() / 1000 + 1800;  //默认签名30分钟有效
        long expiration = 1595991840;
        String json = "{\"bucket\":\"" + bucket + "\",\"save-key\":\"" + save_key + "\",\"expiration\":" + expiration + "}";

        System.out.println(json);

        /* 对 json 进行 Base64 编码 */
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }


    public static void main(String[] args) {
        String bucket = "api-apply-for-bucket"; //存储服务名
        String operator = "hello";  //操作员
        String pwd = "zuovGQug6TQ5zVcjHwQOwgI3eO7SqR92"; //操作员密码
        String uri = "/" + bucket; // 前端请求接口地址的uri部分

        String save_key = "/image/captain_american.jpg"; //上传文件在存储空间中的完整保存路径

        String policy = policy(bucket, save_key);

        System.out.println("policy：" + policy);

        //从左只有参数分别是 操作员名， 操作员密码， 请求方法， URI， 上传参数policy，MD5，MD5 值一般不填写
        System.out.println(sign(operator, pwd, "POST", uri, policy, ""));
    }

}
