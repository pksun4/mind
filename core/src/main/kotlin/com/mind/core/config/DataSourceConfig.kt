package com.mind.core.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan(basePackages = ["com.mind.core.config", "com.mind.core.domain"])
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.mind.core.domain"]
)
@Import(QueryDslConfig::class)
class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    fun dataConfig() = DataSourceProperties()

    @Bean
    @Primary
    fun dataSource(@Qualifier("dataConfig") dataConfig: DataSourceProperties) = HikariDataSource().apply {
        driverClassName = dataConfig.driverClassName
        jdbcUrl = dataConfig.url
        username = dataConfig.username
        password = dataConfig.password
    }

    @Bean
    @Primary
    fun entityManagerFactory(
        dataSource: HikariDataSource
    ) : EntityManagerFactory? = LocalContainerEntityManagerFactoryBean().apply {
        jpaVendorAdapter = HibernateJpaVendorAdapter()
        persistenceUnitName = "api"
        setDataSource(dataSource)
        setPackagesToScan("com.mind.core.domain")
        setJpaPropertyMap(jpaProperties())
        afterPropertiesSet()
    }.`object`

    @Bean
    @Primary
    fun transactionManager(
        @Qualifier("entityManagerFactory") entityManagerFactory: EntityManagerFactory
    ) = JpaTransactionManager(entityManagerFactory)

    private fun jpaProperties() = mapOf(
        "hibernate.hbm2ddl.auto" to DDL_AUTO,
        "hibernate.show_sql" to SHOW_SQL,
        "hibernate.format_sql" to FORMAT_SQL,
        "hibernate.highlight_sql" to HIGHLIGHT_SQL,
        "hibernate.dialect" to DIALECT,
        "hibernate.id.new_generator_mapping" to NEW_GENERATOR_MAPPING,
        "hibernate.physical_naming_strategy" to PHYSICAL_STRATEGY
    )

    companion object {
        private const val DDL_AUTO = "none"
        private const val SHOW_SQL = true
        private const val FORMAT_SQL = true
        private const val HIGHLIGHT_SQL = true
        private const val DIALECT = "org.hibernate.dialect.MySQLDialect"
        private const val NEW_GENERATOR_MAPPING = false
        private const val PHYSICAL_STRATEGY = "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
    }
}
