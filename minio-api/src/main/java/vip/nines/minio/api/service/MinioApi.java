package vip.nines.minio.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.nines.minio.api.entity.MinioFileInfo;
import vip.nines.minio.api.entity.R;
import vip.nines.minio.api.service.fallbcak.MinioApiFallbackFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author tanyujie
 * @classname MinioApi
 * @description minio 远程调用api
 * @date 2022/11/24 14:30
 * @since 1.0
 */
@FeignClient(contextId = "minioApi", value = "minio", fallbackFactory = MinioApiFallbackFactory.class)
public interface MinioApi {

    /**
     * 获取文件信息
     *
     * @param bucket 桶
     * @param object 唯一标识
     * @return vip.nines.minio.core.entity.R<vip.nines.minio.core.entity.MinioFileInfo>
     * @author tanyujie
     * @date 2022/11/21 16:41
     * @since v1.0
     */
    @GetMapping("/minio-api/file_info/{bucket}")
    R<MinioFileInfo> fileInfo(@PathVariable("bucket") String bucket, @RequestParam("object") String object);

    /**
     * 文件上传
     *
     * @param multipartFile 文件流
     * @param module        文件目录(object存在以object值为准, module不生效)
     * @param bucket        桶
     * @param object        文件唯一标识(存在则版本覆盖,空则随机生成)
     * @return vip.nines.minio.core.entity.R<vip.nines.minio.core.entity.MinioFileInfo>
     * @author tanyujie
     * @date 2022/11/21 21:08
     * @since v1.0
     */
    @PostMapping(value = "/minio-api/upload/{bucket}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<MinioFileInfo> upload(
            @RequestPart("file") MultipartFile multipartFile,
            @RequestParam("module") String module,
            @PathVariable("bucket") String bucket,
            @RequestParam(value = "object", defaultValue = "") String object) throws IOException;

    /**
     * 文件批量上传
     *
     * @param multipartFiles 文件流数组
     * @param bucket         桶
     * @param module         文件目录
     * @return vip.nines.minio.core.entity.R<vip.nines.minio.core.entity.MinioFileInfo>
     * @author tanyujie
     * @date 2022/11/21 21:08
     * @since v1.0
     */
    @PostMapping(value = "/minio-api/multiple_upload/{bucket}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<List<MinioFileInfo>> upload(
            @RequestPart("files") MultipartFile[] multipartFiles,
            @PathVariable("bucket") String bucket,
            @RequestParam("module") String module) throws IOException;

    /**
     * 删除文件
     *
     * @param bucket 桶
     * @param object 文件唯一标识
     * @return vip.nines.minio.core.entity.R<java.lang.Boolean>
     * @author tanyujie
     * @date 2022/11/23 22:10
     * @since v1.0
     */
    @DeleteMapping("/minio-api/remove/{bucket}")
    R<Boolean> removeObject(@PathVariable("bucket") String bucket, @RequestParam("object") String object);

}
