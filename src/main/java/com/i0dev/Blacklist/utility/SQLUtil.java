package com.i0dev.Blacklist.utility;

import com.i0dev.Blacklist.Heart;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class SQLUtil {

    @Getter
    public static Connection connection = null;

    @SneakyThrows
    public static void establishConnection(Heart heart, boolean useHikari) {
        if (useHikari) {

            String address = "";
            String port = "3306";
            String database = "";
            String username = "";
            String password = "";

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + address + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            HikariDataSource ds = new HikariDataSource(config);
            connection = ds.getConnection();
            heart.getLogger().info("Connected to MySQL database");
        } else {
            Class.forName("org.sqlite.JDBC");
            String database = heart.getDataFolder() + "/database.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + database);
            heart.getLogger().info("SQLite connection established");
        }
    }


    @SneakyThrows
    public static void createTable(Heart heart, String name) {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS " + name + " (");

        /*
             double - DOUBLE(16,10)
             long - BIGINT
             int - INT
             String - VARCHAR(255)
             boolean - BIT
         */

        //               --Name-- -Type- --Null-- ---Primary- ----Auto----- Default -Value-
        // query.append("`{name}` BIGINT NOT NULL PRIMARY KEY AUTOINCREMENT DEFAULT {value}, ");

        query.append(")");
        connection.prepareStatement(query.toString()).execute();
    }


    @SneakyThrows
    public static void executeQuery(String query) {
        connection.prepareStatement(query).execute();
    }

    @SneakyThrows
    public static ResultSet executeQueryResult(String query) {
        return connection.prepareStatement(query).executeQuery();
    }


}
