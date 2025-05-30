package com.itheima.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class DatabaseTestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/db")
    public Map<String, Object> testDB() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 执行一个简单的SQL查询，测试数据库连接
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM article", Integer.class);
            result.put("code", 0);
            result.put("msg", "数据库连接成功");
            result.put("data", Map.of("articleCount", count));
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "数据库连接失败: " + e.getMessage());
        }
        return result;
    }
} 