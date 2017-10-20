package com.dapidi.scheduler.configs;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class HikariConfig {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public HikariConfig(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);

        return ds;
    }
}
