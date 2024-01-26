package vip.nines.minio.core.util;

import cn.hutool.core.util.StrUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.RestoreRequest;
import io.minio.messages.VersioningConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author tanyujie
 * @classname MinioUtils
 * @description minio操作工具类
 * @date 2022/11/21 20:41
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtils {

    private final MinioClient minioClient;

    /**
     * 判断桶是否存在
     *
     * @param bucket 桶名称
     * @return boolean
     * @author tanyujie
     * @date 2022/11/19 23:02
     * @since v1.0
     */
    public boolean bucketExists(String bucket) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("bucketExists 方法 error: " + e);
            return false;
        }
    }

    /**
     * 创建桶
     *
     * @param bucket 桶名称
     * @return boolean
     * @author tanyujie
     * @date 2022/11/19 23:03
     * @since v1.0
     */
    public boolean makeBucket(String bucket) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            // 开启桶的版本控制，需要minio支持该功能
            //setBucketVersioning(bucket, true);
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("makeBucket 方法 error: " + e);
            return false;
        }
    }

    /**
     * 设置桶版本控制
     *
     * @param bucket    桶
     * @param open	    是否开启
     * @author tanyujie
     * @date 2022/11/22 16:21
     * @since v1.0
     */
    public void setBucketVersioning(String bucket, boolean open) {
        VersioningConfiguration.Status status = open ? VersioningConfiguration.Status.ENABLED : VersioningConfiguration.Status.SUSPENDED;
        try {
            minioClient.setBucketVersioning(SetBucketVersioningArgs.builder()
                    .bucket(bucket)
                    .config(new VersioningConfiguration(status, null))
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("setBucketVersioning 方法 error: " + e);
        }
    }

    /**
     * 删除空桶(桶中有文件则抛出异常)
     *
     * @param bucket 桶名称
     * @return boolean
     * @author tanyujie
     * @date 2022/11/19 23:04
     * @since v1.0
     */
    public boolean removeBucket(String bucket) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("removeBucket 方法 error: " + e);
            return false;
        }
    }

    /**
     * 通过InputStream流上传文件
     *
     * @param bucket      桶
     * @param object      文件唯一标识
     * @param fileName    文件原始名称
     * @param inputStream 文件流
     * @param size        文件大小
     * @return void
     * @author tanyujie
     * @date 2022/11/21 14:31
     * @since v1.0
     */
    public boolean putObject(String bucket, String object, String fileName, InputStream inputStream, long size) {
        // 通过fileName文件名获取contentType, 找不到则设置为字节流直接下载
        String contentType = MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        // 用户元数据
        Map<String, String> userMetadata = new HashMap<>(2);
        userMetadata.put("filename", fileName);
        // 上传
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .userMetadata(userMetadata)
                    .build()
            );
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("putObject 方法 error: " + e);
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param bucket 桶
     * @param object 文件唯一标识
     * @return boolean
     * @author tanyujie
     * @date 2022/11/21 15:47
     * @since v1.0
     */
    public boolean removeObject(String bucket, String object) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(object).build());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("removeObject 方法 error: " + e);
            return false;
        }
    }

    /**
     * 恢复删除的文件或恢复到某个版本
     *
     * @param bucket	桶
     * @param object	文件唯一标识
     * @param versionId	版本
     * @return boolean
     * @author tanyujie
     * @date 2022/11/23 22:03
     * @since v1.0
     */
    public boolean restoreObject(String bucket, String object, String versionId) {
        RestoreObjectArgs.Builder builder = RestoreObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .request(new RestoreRequest(null, null, null, null, null, null));
        // 判断版本号是否非空,来确定是否需要恢复到某个具体到版本号
        if (StrUtil.isNotBlank(versionId)) {
            builder.versionId(versionId);
        }
        try {
            minioClient.restoreObject(builder.build());
            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("restoreObject 方法 error: " + e);
            return false;
        }
    }

    /**
     * 获取文件外链(分享链接) 默认:Get 7天过期
     *
     * @param bucket 桶
     * @param object 文件唯一标识
     * @return java.lang.String
     * @author tanyujie
     * @date 2022/11/21 15:52
     * @since v1.0
     */
    public String getObjectLinkedUrl(String bucket, String object) {
        return getObjectLinkedUrl(Method.GET, bucket, object, 7, TimeUnit.DAYS);
    }

    /**
     * 获取文件外链(分享链接)
     *
     * @param method   请求方式
     * @param bucket   桶
     * @param object   文件唯一标识
     * @param duration 外链过期时间
     * @param unit     过期时间单位
     * @return java.lang.String
     * @author tanyujie
     * @date 2022/11/21 15:52
     * @since v1.0
     */
    public String getObjectLinkedUrl(Method method, String bucket, String object, int duration, TimeUnit unit) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(method)
                    .bucket(bucket)
                    .object(object)
                    .expiry(duration, unit)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
                 ServerException e) {
            log.error("getObjectLinkedUrl 方法 error: " + e);
            return null;
        }
    }

    /**
     * 获取文件对象信息和元数据
     *
     * @param bucket 桶
     * @param object 唯一标识
     * @return io.minio.StatObjectResponse
     * @author tanyujie
     * @date 2022/11/21 16:35
     * @since v1.0
     */
    public StatObjectResponse statObject(String bucket, String object) {
        try {
            return minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(object).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("statObject 方法 error: " + e);
            return null;
        }
    }

}
