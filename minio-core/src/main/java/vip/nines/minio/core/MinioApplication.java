package vip.nines.minio.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author tanyujie
 * @classname MinioApplication
 * @description Minio 中间件
 * @date 2022/11/19 18:27
 * @since 1.0
 */
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
public class MinioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinioApplication.class, args);
    }

}
