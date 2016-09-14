package com.liverton.livecheck.dao.config;

import com.liverton.livecheck.dao.config.conditional.IsMySQLDatabaseCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by sshah on 11/08/2016.
 */
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.liverton.livecheck"})
@PropertySource(value = "classpath:database.properties")
@Configuration
@Conditional(value = IsMySQLDatabaseCondition.class)
public class MySQLDatabaseConfiguration {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLDatabaseConfiguration.class);

    @Value(value = "${db.url}")
    private String dbUrl;

    @Value(value = "${db.driver}")
    private String dbDriver;

    @Value(value = "${db.username}")
    private String dbUsername;

    @Value(value = "${db.password}")
    private String dbPassword;

    @PostConstruct
    public void init() {
        LOGGER.info("####################################");
        LOGGER.info("PRODUCTION DAO configuration is enabled");
        LOGGER.info("####################################");
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(shouldLogSql());
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabase(Database.MYSQL);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.liverton.livecheck");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(setAdditionalProperties());
        return em;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    private Properties setAdditionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.setProperty("hibernate.batch_size", "100");
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");
//        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }




    /**
     * Can be overidden by extending classes to log sql
     *
     * @return
     */
    protected boolean shouldLogSql() {
        return false;
    }



}
