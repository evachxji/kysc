package com.kysc.utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//参考：http://blog.csdn.net/catoop/article/details/50668896
//swagger-ui.html
@Configuration
@EnableSwagger2
public class Swagger2{

    /*@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kysc.api"))
                .paths(PathSelectors.any())
                .build();
    }*/

    @Bean
    public Docket webApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("web")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kysc.controller"))
                //过滤的接口
                .paths(PathSelectors.any())
                .build()
                .apiInfo(webApiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("使用Swagger2构建RESTful API")
                //.description("mindao官网：http://mindao.com.cn")
                //.termsOfServiceUrl("http://mindao.com.cn")
                .contact("夜晨、文彬")
                .version("1.0")
                .build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                //大标题
                .title("科院书城")
                //详细描述
                .description("springboot+SM+shiro+angular+bootstrap")
                //版本
                .version("1.0")
                .license("The Apache License, Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

}