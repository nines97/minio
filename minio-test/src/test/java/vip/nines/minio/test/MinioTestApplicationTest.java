package vip.nines.minio.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import vip.nines.minio.api.entity.MinioFileInfo;
import vip.nines.minio.api.entity.R;
import vip.nines.minio.api.service.MinioApi;

import javax.annotation.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tanyujie
 * @classname MinioTestApplicationTest
 * @description minio api 测试
 * @date 2022/11/24 14:56
 * @since 1.0
 */
@Slf4j
@SpringBootTest
class MinioTestApplicationTest {

    private final static String bucket = "version-test";

    @Resource
    MinioApi minioApi;

    @Test
    void fileInfo() {
        String object = "hangpai.mp4";
        R<MinioFileInfo> fileInfoR = minioApi.fileInfo(bucket, object);
        MinioFileInfo fileInfo = fileInfoR.getData();

        log.info("object:" + fileInfo.getObject());
        log.info("fileName:" + fileInfo.getFileName());
        log.info("linkedUrl:" + fileInfo.getLinkedUrl());
    }
    
    @Test
    void upload() throws IOException {
        File file = new File("/Users/nines/Downloads/Redis.png");
        FileInputStream fis = new FileInputStream(file);
        String contentType = MediaTypeFactory.getMediaType(file.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        // 构建MultipartFile
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), contentType, fis);
        R<MinioFileInfo> fileInfoR = minioApi.upload(multipartFile, "minioapi", bucket, null);
        if (fileInfoR.getCode() == 200) {
            MinioFileInfo fileInfo = fileInfoR.getData();
            log.info("object:" + fileInfo.getObject());
            log.info("fileName:" + fileInfo.getFileName());
            log.info("linkedUrl:" + fileInfo.getLinkedUrl());
        } else {
            log.error(fileInfoR.getMsg());
        }
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

        R<List<MinioFileInfo>> r = minioApi.upload(new MultipartFile[]{multipartFile1, multipartFile2}, bucket, "multiple");
        if (r.getCode() == 200) {
            List<MinioFileInfo> fileInfoList = r.getData();
            fileInfoList.forEach(fileInfo -> {
                log.info("object:" + fileInfo.getObject());
                log.info("fileName:" + fileInfo.getFileName());
                log.info("linkedUrl:" + fileInfo.getLinkedUrl());
            });
        } else {
            log.error(r.getMsg());
        }
    }

    @Test
    void remove() {
        String object = "multiple/2022/11/24/1595717751170314240.jpeg";
        R<Boolean> r = minioApi.removeObject(bucket, object);
        if (r.getCode() == 200) {
            log.info("文件{}删除成功:{}", object, r.getData());
        } else {
            log.error(r.getMsg());
        }
    }

}