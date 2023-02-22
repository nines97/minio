package vip.nines.minio.api.service.fallbcak;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vip.nines.minio.api.entity.MinioFileInfo;
import vip.nines.minio.api.entity.R;
import vip.nines.minio.api.service.MinioApi;

import java.util.List;

/**
 * @author tanyujie
 * @classname MinioApiFallbackFactory
 * @description minio远程调用失败 服务降级处理
 * @date 2022/11/24 14:33
 * @since 1.0
 */
@Slf4j
@Component
public class MinioApiFallbackFactory implements FallbackFactory<MinioApi> {


    @Override
    public MinioApi create(Throwable cause) {
        log.error("minio服务调用失败", cause);
        return new MinioApi() {
            @Override
            public R<MinioFileInfo> fileInfo(String bucket, String object) {
                return R.fail("获取文件信息失败:" + cause.getMessage());
            }

            @Override
            public R<MinioFileInfo> upload(MultipartFile multipartFile, String module, String bucket, String object) {
                return R.fail("文件上传失败:" + cause.getMessage());
            }

            @Override
            public R<List<MinioFileInfo>> upload(MultipartFile[] multipartFiles, String bucket, String module) {
                return R.fail("文件批量上传失败:" + cause.getMessage());
            }

            @Override
            public R<Boolean> removeObject(String bucket, String object) {
                return R.fail("文件删除失败:" + cause.getMessage());
            }
        };
    }
}
