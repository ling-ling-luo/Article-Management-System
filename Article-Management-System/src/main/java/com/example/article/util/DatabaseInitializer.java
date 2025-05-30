package com.example.article.util;

import com.example.article.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 数据库初始化工具
 * 注意：生产环境中请谨慎使用自动初始化
 */
@Component
@ConditionalOnProperty(name = "app.db.init.enabled", havingValue = "true")
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final DataSource dataSource;
    private final AppProperties appProperties;

    public DatabaseInitializer(DataSource dataSource, AppProperties appProperties) {
        this.dataSource = dataSource;
        this.appProperties = appProperties;
    }

    @Override
    public void run(String... args) {
        if (!appProperties.getDb().getInit().isEnabled()) {
            log.info("数据库初始化已禁用");
            return;
        }
        try {
            log.info("开始执行数据库初始化脚本...");
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8");
            
            // 添加SQL脚本
            resourceDatabasePopulator.addScript(new ClassPathResource("db/init.sql"));
            
            // 执行脚本
            resourceDatabasePopulator.execute(dataSource);
            log.info("数据库初始化脚本执行成功！");
        } catch (Exception e) {
            log.error("数据库初始化失败：{}", e.getMessage(), e);
        }
    }
} 