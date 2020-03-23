package org.autoconfigure.iam;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用 Srm register 服务扫描
 *
 * @author wangzhen 2019/5/15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SrmIamAutoConfiguration.class)
public @interface EnableSrmIam {
}
