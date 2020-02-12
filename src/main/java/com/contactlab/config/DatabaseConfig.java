package com.contactlab.config;

import com.contactlab.Properties.DataSourceProperties;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;


@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    public Connection getConnection() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setServerName(dataSourceProperties.getServerName());
        dataSource.setPortNumber(dataSourceProperties.getPortNumber());
        dataSource.setUser(dataSourceProperties.getUser());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setDatabaseName(dataSourceProperties.getDataBaseName());

        return dataSource.getConnection();
    }



}
