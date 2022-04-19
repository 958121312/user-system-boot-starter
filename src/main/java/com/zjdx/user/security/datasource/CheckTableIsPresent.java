package com.zjdx.user.security.datasource;

import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author yuntian
 * @since 2022-02-23
 * 检查表是否已创建，用于后续自动生成数据库表
 */
@AllArgsConstructor
public class CheckTableIsPresent {

    public static String[] JDBC_METADATA_TABLE_TYPES = {"TABLE"};

    private final DataSource dataSource;

    /**
     * @param tableName
     * @return
     * @throws SQLException
     */
    public Boolean check(String tableName) throws SQLException {
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        /* Only supports mysql   */
        final String SCHEMA_PATTERN = "MYSQL";
        ResultSet tables = metaData.getTables(connection.getCatalog(), SCHEMA_PATTERN, tableName, JDBC_METADATA_TABLE_TYPES);
        return tables.next();
    }
}
