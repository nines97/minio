package vip.nines.minio.core.mc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tanyujie
 * @classname MinioProperties
 * @description minio 参数
 * @date 2022/11/19 21:31
 * @since 1.0
 */
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {

    private String server;

    private int port;

    private String accessKey;

    private String secretKey;

}
