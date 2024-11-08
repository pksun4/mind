package com.mind.api.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@ComponentScan(basePackages = ["com.mind"])
@EnableJpaAuditing
class ApiConfig
