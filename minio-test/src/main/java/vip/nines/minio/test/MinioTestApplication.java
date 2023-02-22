package vip.nines.minio.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tanyujie
 * @classname MinioTestApplication
 * @description minio 测试
 * @date 2022/11/24 14:51
 * @since 1.0
 */
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "vip.nines.minio")
@SpringBootApplication
public class MinioTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinioTestApplication.class, args);
    }

}
