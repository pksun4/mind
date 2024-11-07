package com.mind.api.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SwaggerConfig : WebMvcConfigurer {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("MIND SHARE API")
                    .description("MIND SHARE API")
                    .version("1.0.0")
            )
    }

}

