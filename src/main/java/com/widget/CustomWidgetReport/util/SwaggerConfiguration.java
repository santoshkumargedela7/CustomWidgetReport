package com.widget.CustomWidgetReport.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@Component
public class SwaggerConfiguration {

	 @Bean
	    Docket getDocket() {
	        return new Docket(DocumentationType.SWAGGER_2).groupName("WEB-apis")
	                .apiInfo(getApiInfo())
	                .select().
	                apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).
	                build();
	    }
		
		public ApiInfo getApiInfo() {
			return new ApiInfoBuilder().title("CustomWidgetReport").description("CustomWidgetReport")
					.version("2").build();
			
		}
}
