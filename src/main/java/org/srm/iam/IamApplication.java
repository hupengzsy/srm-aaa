package org.srm.iam;

import org.autoconfigure.iam.EnableSrmIam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * description
 * 增加ComponentScan，扫描org.hzero.common路径，解决服务更改redisDB配置未加载问题
 * @author jinliang 2019/04/23 16:38
 */
@EnableSrmIam
@EnableDiscoveryClient
@SpringBootApplication
public class IamApplication {
    public static void main(String[] args) {
        SpringApplication.run(IamApplication.class, args);
    }
}
