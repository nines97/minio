package vip.nines.minio.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.nines.minio.api.entity.MinioFileInfo;
import vip.nines.minio.api.entity.R;
import vip.nines.minio.core.service.MinioService;

import java.io.IOException;
import java.util.List;

/**
 * @author tanyujie
 * @classname MinioController
 * @description minio中间件开放接口
 * @date 2022/11/21 16:08
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/minio-api")
public class MinioController {

    private final MinioService minioService;

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
    @GetMapping("/file_info/{bucket}")
    public R<MinioFileInfo> fileInfo(@PathVariable("bucket") String bucket, @RequestParam("object") String object) {
        return R.ok(minioService.fileInfo(bucket, object));
    }

    /**
     * 文件上传
     *
     * @param multipartFile 文件流
     * @param module	文件目录(object存在以object值为准, module不生效)
     * @param bucket	桶
     * @param object	文件唯一标识(存在则版本覆盖,空则随机生成)
     * @return vip.nines.minio.core.entity.R<vip.nines.minio.core.entity.MinioFileInfo>
     * @author tanyujie
     * @date 2022/11/21 21:08
     * @since v1.0
     */
    @PostMapping("/upload/{bucket}")
    public R<MinioFileInfo> upload(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("module") String module,
            @PathVariable("bucket") String bucket,
            @RequestParam(value = "object", defaultValue = "") String object) throws IOException {
        return R.ok(minioService.upload(multipartFile, module, bucket, object));
    }

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
    @PostMapping("/multiple_upload/{bucket}")
    public R<List<MinioFileInfo>> upload(
            @RequestParam("files") MultipartFile[] multipartFiles,
            @PathVariable("bucket") String bucket,
            @RequestParam("module") String module) throws IOException {
        return R.ok(minioService.upload(multipartFiles, bucket, module));
    }

    /**
     * 删除文件
     *
     * @param bucket    桶
     * @param object	文件唯一标识
     * @return vip.nines.minio.core.entity.R<java.lang.Boolean>
     * @author tanyujie
     * @date 2022/11/23 22:10
     * @since v1.0
     */
    @DeleteMapping("/remove/{bucket}")
    public R<Boolean> removeObject(@PathVariable("bucket") String bucket, @RequestParam("object") String object) {
        return R.ok(minioService.remove(bucket, object));
    }

}
