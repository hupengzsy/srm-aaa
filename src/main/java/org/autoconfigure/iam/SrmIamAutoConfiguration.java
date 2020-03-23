package org.autoconfigure.iam;


import org.hzero.autoconfigure.iam.EnableHZeroIam;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: java类作用描述
 * @Author: wangzhen
 * @CreateDate: 2019/5/14 18:35
 */
@ComponentScan({"org.hzero.iam.api", "org.hzero.iam.app", "org.hzero.iam.config", "org.hzero.iam.domain", "org.hzero.iam.infra","org.srm.iam.api", "org.srm.iam.app", "org.srm.iam.config", "org.srm.iam.domain", "org.srm.iam.infra"})
@EnableFeignClients({"org.hzero.iam", "org.hzero.boot.imported"})
@EnableHZeroIam
@Configuration
public class SrmIamAutoConfiguration {
}