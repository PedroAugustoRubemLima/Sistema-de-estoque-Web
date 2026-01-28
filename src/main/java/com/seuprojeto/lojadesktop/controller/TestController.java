package com.seuprojeto.lojadesktop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/database-info")
    public Map<String, String> getDatabaseInfo() {
        Map<String, String> info = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            info.put("databaseProductName", metaData.getDatabaseProductName());
            info.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            info.put("url", metaData.getURL());
            info.put("userName", metaData.getUserName());
            info.put("driverName", metaData.getDriverName());
            info.put("driverVersion", metaData.getDriverVersion());
            
        } catch (Exception e) {
            info.put("error", e.getMessage());
        }
        
        return info;
    }
}