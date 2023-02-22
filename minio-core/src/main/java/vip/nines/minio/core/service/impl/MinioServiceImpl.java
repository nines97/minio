package vip.nines.minio.core.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vip.nines.minio.api.entity.MinioFileInfo;
import vip.nines.minio.core.handle.CustomMinioException;
import vip.nines.minio.core.service.MinioService;
import vip.nines.minio.core.util.MinioUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author tanyujie
 * @classname MinioServiceImpl
 * @description minio功能实现
 * @date 2022/11/19 22:59
 * @since 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    private final MinioUtils minioUtils;

    @Override
    public MinioFileInfo fileInfo(String bucket, String object) {
        // 检查桶是否存在
        if (!minioUtils.bucketExists(bucket)) {
            throw new CustomMinioException(bucket + "桶不存在");
        }
        // 判断文件是否存在
        StatObjectResponse statObjectResponse = minioUtils.statObject(bucket, object);
        Optional.ofNullable(statObjectResponse).orElseThrow(() -> new CustomMinioException(object + "文件不存在"));
        // 获取fileName
        String fileName = statObjectResponse.userMetadata().get("filename");
        if (StrUtil.isBlank(fileName)) {
            // fileName为空说明文件是从minio管理后台页面上传,将object用/分割后到最后一部分作为fileName
            String[] objectArray = object.split("/");
            fileName = objectArray[objectArray.length - 1];
        }
        // 获取外链
        String linkedUrl = minioUtils.getObjectLinkedUrl(bucket, object);
        // 构建文件信息
        return new MinioFileInfo(bucket, object, fileName, linkedUrl);
    }

    @Override
    public MinioFileInfo upload(MultipartFile multipartFile, String module, String bucket, String object) throws IOException {
        // 检查桶是否存在
        if (!minioUtils.bucketExists(bucket)) {
            // 桶不存在,创建桶
            minioUtils.makeBucket(bucket);
        }
        // 文件名
        String fileName = multipartFile.getOriginalFilename();
        if (StrUtil.isBlank(fileName)) {
            throw new CustomMinioException("无效的multipartFile,获取不到原始名称");
        }
        // 检查object是否非空, 为空按日期目录+雪花id构建一个object, 否则直接使用object
        if (StrUtil.isBlank(object)) {
            // 日期目录
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            // 构建object唯一标识
            if (StrUtil.isNotBlank(module)) {
                // module不为空则加到最前方做文件夹
                object = module + "/";
            }
            object += dateFolder + "/" + IdUtil.getSnowflakeNextIdStr() + "." + FileNameUtil.getSuffix(fileName);
        }
        // 上传文件
        if (minioUtils.putObject(bucket, object, fileName, multipartFile.getInputStream(), multipartFile.getSize())) {
            // 获取上传后文件的信息
            return fileInfo(bucket, object);
        }
        throw new CustomMinioException("文件上传失败");
    }

    @Override
    public List<MinioFileInfo> upload(MultipartFile[] multipartFiles, String bucket, String module) throws IOException {
        List<MinioFileInfo> minioFileInfoList = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                minioFileInfoList.add(upload(multipartFile, module, bucket, null));
            }
            return minioFileInfoList;
        } catch (CustomMinioException e) {
            // 将以上传成功的文件删除
            log.info("多文件上传失败, 开始进行删除。。。");
            if (!minioFileInfoList.isEmpty()) {
                minioFileInfoList.forEach(minioFileInfo -> remove(bucket, minioFileInfo.getObject()));
            }
            throw new CustomMinioException(e.getMessage());
        }
    }

    @Override
    public boolean remove(String bucket, String object) {
        return minioUtils.removeObject(bucket, object);
    }

    @Override
    public MinioFileInfo restore(String bucket, String object, String versionId) {
        if (minioUtils.restoreObject(bucket, object, versionId)) {
            return fileInfo(bucket, object);
        }
        throw new CustomMinioException("文件恢复失败");
    }
}
