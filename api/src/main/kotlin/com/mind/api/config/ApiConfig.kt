package com.mind.api.config

import com.mind.api.security.SecurityUtils
import java.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@ComponentScan(basePackages = ["com.mind"])
@EnableJpaAuditing
class ApiConfig {

    @Bean
    fun auditorAware(): AuditorAware<Long> = AuditorAware {
        Optional.ofNullable(SecurityUtils.getCurrentUser()?.memberKey)
    }

}
