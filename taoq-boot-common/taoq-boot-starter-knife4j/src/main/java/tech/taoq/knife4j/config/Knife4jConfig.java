package tech.taoq.knife4j.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Knife4j 配置类
 *
 * @author keqi
 */
public class Knife4jConfig {

    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public Knife4jConfig(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket defaultApi2() {
        String groupName = "default group";
        return new Docket(DocumentationType.SWAGGER_2)
                // 关闭默认的响应信息
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfoBuilder()
                        .description("rest api")
                        .termsOfServiceUrl("https://doc.xiaominfo.com/")
                        .version("1.0")
                        .build())
                .groupName(groupName)
                .select()
                // 所有使用了 @RestController 注解的类都会被扫描出来
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
    }
}
