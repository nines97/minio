package vip.nines.minio.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tanyujie
 * @classname MinioFileInfo
 * @description Minio文件对象信息
 * @date 2022/11/19 22:35
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinioFileInfo {

    /**
     * 桶
     */
    private String bucket;
    /**
     * minio上文件唯一标识(存储路径+文件名)
     */
    private String object;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 外链(分享链接)
     */
    private String linkedUrl;
}
