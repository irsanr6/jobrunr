package com.irsan.jobrunr.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class DataSourceConfig {

    @Value("${jdbc.driver}")
    private String jdbcDriver;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${spring.application.name}")
    private String appName;

    @Primary
    @Bean("dataSource")
    public DataSource dataSource() {
        HikariConfig dataSource = new HikariConfig();
        dataSource.setPoolName(appName);
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        dataSource.setAutoCommit(true);
        dataSource.setLeakDetectionThreshold(30000);
        dataSource.setConnectionTimeout(SECONDS.toMillis(30));
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setIdleTimeout(MINUTES.toMillis(1));
        dataSource.setMinimumIdle(10);
        dataSource.setMaxLifetime(MINUTES.toMillis(30));
        dataSource.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2); //((core_count * 2) + effective_spindle_count == 0)

        dataSource.addDataSourceProperty("cachePrepStmts", "true");
        dataSource.addDataSourceProperty("prepStmtCacheSize", "500");
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource.addDataSourceProperty("useServerPrepStmts", "true");
        dataSource.addDataSourceProperty("useLocalSessionState", "true");
        dataSource.addDataSourceProperty("rewriteBatchedStatements", "true");
        dataSource.addDataSourceProperty("cacheResultSetMetadata", "true");
        dataSource.addDataSourceProperty("cacheServerConfiguration", "true");
        dataSource.addDataSourceProperty("elideSetAutoCommits", "true");
        dataSource.addDataSourceProperty("maintainTimeStats", "true");

        return new HikariDataSource(dataSource);
    }

}
