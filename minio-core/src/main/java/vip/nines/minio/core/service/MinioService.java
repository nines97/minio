package vip.nines.minio.core.service;

import org.springframework.web.multipart.MultipartFile;
import vip.nines.minio.api.entity.MinioFileInfo;

import java.io.IOException;
import java.util.List;

/**
 * @author tanyujie
 * @classname MinioService
 * @description minio功能
 * @date 2022/11/19 22:59
 * @since 1.0
 */
public interface MinioService {

    /**
     * 获取文件信息
     *
     * @param bucket    桶
     * @param object	唯一标识
     * @return vip.nines.minio.core.entity.R<vip.nines.minio.core.entity.MinioFileInfo>
     * @author tanyujie
     * @date 2022/11/21 16:41
     * @since v1.0
     */
    MinioFileInfo fileInfo(String bucket, String object);

    /**
     * 文件上传
     *
     * @param module	文件目录
     * @param multipartFile 文件流
     * @param bucket	桶
     * @param object	文件唯一标识(存在则更新文件)
     * @return vip.nines.minio.core.entity.R<vip.nines.minio.core.entity.MinioFileInfo>
     * @author tanyujie
     * @date 2022/11/21 21:08
     * @since v1.0
     */
    MinioFileInfo upload(MultipartFile multipartFile, String module, String bucket, String object) throws IOException;

    /**
     * 文件批量上传
     *
     * @param multipartFiles 文件流数组
     * @param bucket	桶
     * @param module	文件目录
     * @return vip.nines.minio.core.entity.R<vip.nines.minio.core.entity.MinioFileInfo>
     * @author tanyujie
     * @date 2022/11/21 21:08
     * @since v1.0
     */
    List<MinioFileInfo> upload(MultipartFile[] multipartFiles, String bucket, String module) throws IOException;

    /**
     * 删除文件
     *
     * @param bucket    桶
     * @param object	文件唯一标识
     * @author tanyujie
     * @date 2022/11/21 22:26
     * @since v1.0
     */
    boolean remove(String bucket, String object);

    /**
     * 恢复文件或恢复到某个具体到版本
     * TODO 无效,暂不清楚原因,后台管理页面上restore有效
     *
     * @param bucket	桶
     * @param object	文件唯一标识
     * @param versionId	版本号
     * @return vip.nines.minio.core.entity.MinioFileInfo
     * @author tanyujie
     * @date 2022/11/23 22:11
     * @since v1.0
     */
    MinioFileInfo restore(String bucket, String object, String versionId);
}
