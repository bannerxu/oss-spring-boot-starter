package top.banner.lib.oss.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 上传客户端
 */
public interface OssClient {

    /**
     * 文件方式上传
     *
     * @param file 文件
     * @param key  路径+文件名
     * @return 图片链接
     */
    String upload(MultipartFile file, String key) throws IOException;

    /**
     * 流方式上传文件
     *
     * @param inputStream 输入流
     * @param key         路径+文件名  不要 '/' 开头，'/'会自动去掉
     * @return 图片链接
     */
    String upload(InputStream inputStream, String key) throws IOException;


    /**
     * 上传链接（图片）
     *
     * @param url 图片链接
     * @param key 路径+文件名
     * @return 图片链接
     */
    String upload(String url, String key) throws IOException;

    /**
     * exist 根据 路径+文件名 判断是否存在
     */
    Boolean existByKey(String key);


    void deleteByKey(String key);



    /**
     * 去除第一个斜杠
     */
    default String removeFirstSlash(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return str.replaceFirst("^/*", "");
    }


    /**
     * 签名
     */
    <T> T sign();
}
