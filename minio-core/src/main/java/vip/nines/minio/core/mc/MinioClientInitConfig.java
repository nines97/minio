package vip.nines.minio.core.mc;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tanyujie
 * @classname MinioClientInitConfig
 * @description minio client 初始化配置
 * @date 2022/11/19 21:39
 * @since 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "minio", name = "enable", havingValue = "true")
@EnableConfigurationProperties(MinioProperties.class)
@RequiredArgsConstructor
public class MinioClientInitConfig {

    private final MinioProperties minioProperties;

    /**
     * 初始化MinioClient 加入spring容器托管
     *
     * @return io.minio.MinioClient
     * @author tanyujie
     * @date 2022/11/19 22:33
     * @since v1.0
     */
    @Bean
    public MinioClient minioClientInit() {
        return MinioClient.builder()
                .endpoint(minioProperties.getServer(), minioProperties.getPort(), true)
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

}
