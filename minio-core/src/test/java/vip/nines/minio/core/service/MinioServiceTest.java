package vip.nines.minio.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import vip.nines.minio.api.entity.MinioFileInfo;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author tanyujie
 * @classname MinioServiceTest
 * @description minio服务测试
 * @date 2022/11/21 22:34
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@SpringBootTest
class MinioServiceTest {

    private final static String bucket = "version-test";

    @Resource
    MinioService minioService;

    @Test
    void fileInfo() {
        String object = "/2022/11/22/1594969988467916800.mp4";
        MinioFileInfo fileInfo = minioService.fileInfo(bucket, object);
        log.info("object:" + fileInfo.getObject());
        log.info("fileName:" + fileInfo.getFileName());
        log.info("linkedUrl:" + fileInfo.getLinkedUrl());
    }

    @Test
    void upload() throws IOException {
        File file = new File("/tmp/xuxubaobao.jpeg");
        FileInputStream fis = new FileInputStream(file);
        String contentType = MediaTypeFactory.getMediaType(file.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        // 构建MultipartFile
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), contentType, fis);
        MinioFileInfo fileInfo = minioService.upload(multipartFile, null, bucket, "/2022/11/22/1594969988467916800.mp4");

        log.info("object:" + fileInfo.getObject());
        log.info("fileName:" + fileInfo.getFileName());
        log.info("linkedUrl:" + fileInfo.getLinkedUrl());
    }

    @Test
    void multipleUpload() throws IOException {
        File file1 = new File("/tmp/hangpai.mp4");
        File file2 = new File("/tmp/xuxubaobao.jpeg");
        FileInputStream fis1 = new FileInputStream(file1);
        FileInputStream fis2 = new FileInputStream(file2);
        String contentType1 = MediaTypeFactory.getMediaType(file1.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        String contentType2 = MediaTypeFactory.getMediaType(file1.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        // 构建MultipartFile
        MultipartFile multipartFile1 = new MockMultipartFile(file1.getName(), file1.getName(), contentType1, fis1);
        MultipartFile multipartFile2 = new MockMultipartFile(file2.getName(), null, contentType2, fis2);

        List<MinioFileInfo> fileInfoList = minioService.upload(new MultipartFile[]{multipartFile1, multipartFile2}, bucket, null);

        fileInfoList.forEach(fileInfo -> {
            log.info("object:" + fileInfo.getObject());
            log.info("fileName:" + fileInfo.getFileName());
            log.info("linkedUrl:" + fileInfo.getLinkedUrl());
        });
    }

    @Test
    void remove() {
        String object = "/2022/11/22/1594969988467916800.mp4";
        boolean f = minioService.remove(bucket, object);
        log.info("文件{}删除成功:{}", object, f);
    }

    @Test
    void restore() {
        String object = "/2022/11/22/1594969988467916800.mp4";
        MinioFileInfo fileInfo = minioService.restore(bucket, object, null);

        log.info("文件恢复成功");
        log.info("object:" + fileInfo.getObject());
        log.info("fileName:" + fileInfo.getFileName());
        log.info("linkedUrl:" + fileInfo.getLinkedUrl());
    }
}