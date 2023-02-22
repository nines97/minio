package vip.nines.minio.core.mc;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author tanyujie
 * @classname MinioClientInitConfigTest
 * @description 测试 minio client 初始化
 * @date 2022/11/19 22:18
 * @since 1.0
 */
@Slf4j
@SpringBootTest
class MinioClientInitConfigTest {

    @Resource
    MinioClient minioClient;

    @Test
    public void minioClientInitTest() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("java-test").build());
        log.info("桶 java-test 是否存在：" + found);
    }

}