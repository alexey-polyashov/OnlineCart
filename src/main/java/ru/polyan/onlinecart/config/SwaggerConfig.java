package ru.polyan.onlinecart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.polyan.onlinecart.controllers"))
                .paths(PathSelectors.regex("/api.*"))
                //.paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "OnlineCart REST API",
                "Описание методов API v1.0 и сущностей.",
                "v1",
                "",
                new Contact("Алексей Поляшов", "https://alexey-polyashov.github.io/javaCode/", "polyashofff@yandex.ru"),
                "", "", Collections.emptyList());
    }

}