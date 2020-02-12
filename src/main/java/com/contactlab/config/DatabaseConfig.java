package com.contactlab.config;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;


@Configuration
public class DatabaseConfig {

    @Bean
    public Connection getConnection() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setServerName("127.0.0.1");
        dataSource.setPortNumber(3306);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("biblioteca");

        return dataSource.getConnection();
    }



}
