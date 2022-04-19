package com.zjdx.user.security.datasource;

import com.zjdx.user.security.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;

/**
 * 自动创建数据库表，只支持MySQL
 * @author yuntian
 * @since 2022-02-23
 */
@Log4j2
@AllArgsConstructor
public class DbSqlSession {

    private final SqlSession sqlSession;

    public void executeMandatorySchemaResource(String operation) {
        executeSchemaResource(operation,
                getResourceForDbOperation(),
                false);
    }

    public String getResourceForDbOperation() {
        return "user_system_create.sql";
    }

    public void executeSchemaResource(String operation,
                                      String resourceName,
                                      boolean isOptional) {
        try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                if (isOptional) {
                    log.info("no schema resource {} for {}",
                            resourceName,
                            operation);
                } else {
                    throw new RuntimeException("resource '" + resourceName + "' is not available");
                }
            } else {
                executeSchemaResource(operation,
                        resourceName,
                        inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void executeSchemaResource(String operation,
                                       String resourceName,
                                       InputStream inputStream) {
        log.info("performing {} on {} with resource {}",
                operation,
                resourceName);
        String sqlStatement = null;
        String exceptionSqlStatement = null;
        try {
            Connection connection = sqlSession.getConnection();
            Exception exception = null;
            byte[] bytes = IoUtil.readBytes(inputStream, false);
            String ddlStatements = new String(bytes);

            // Special DDL handling for certain databases
            try {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                int majorVersion = databaseMetaData.getDatabaseMajorVersion();
                int minorVersion = databaseMetaData.getDatabaseMinorVersion();
                log.info("Found MySQL: majorVersion=" + majorVersion + " minorVersion=" + minorVersion);

                // Special care for MySQL < 5.6
                if (majorVersion <= 5 && minorVersion < 6) {
                    ddlStatements = updateDdlForMySqlVersionLowerThan56(ddlStatements);
                }
            } catch (Exception e) {
                log.info("Could not get database metadata",
                        e);
            }

            BufferedReader reader = new BufferedReader(new StringReader(ddlStatements));
            String line = readNextTrimmedLine(reader);
            while (line != null) {
                if (line.startsWith("# ")) {
                    log.debug(line.substring(2));
                } else if (line.startsWith("-- ")) {
                    log.debug(line.substring(3));
                } else if (line.length() > 0) {
                    if ((line.endsWith(";")) || (line.startsWith("/"))) {
                        sqlStatement = addSqlStatementPiece(sqlStatement,
                                line.substring(0,
                                        line.length() - 1));
                        Statement jdbcStatement = connection.createStatement();
                        try {
                            // no logging needed as the connection will log it
                            log.debug("SQL: {}",
                                    sqlStatement);
                            jdbcStatement.execute(sqlStatement);
                            jdbcStatement.close();
                        } catch (Exception e) {
                            if (exception == null) {
                                exception = e;
                                exceptionSqlStatement = sqlStatement;
                            }
                            log.error("problem during schema {}, statement {}",
                                    operation,
                                    sqlStatement,
                                    e);
                        } finally {
                            sqlStatement = null;
                        }
                    } else {
                        sqlStatement = addSqlStatementPiece(sqlStatement,
                                line);
                    }
                }

                line = readNextTrimmedLine(reader);
            }

            if (exception != null) {
                throw exception;
            }

            log.debug("activiti db schema {} for component {} successful",
                    operation);
        } catch (Exception e) {
            throw new RuntimeException("couldn't " + operation + " db schema: " + exceptionSqlStatement,
                    e);
        }
    }

    protected String addSqlStatementPiece(String sqlStatement,
                                          String line) {
        if (sqlStatement == null) {
            return line;
        }
        return sqlStatement + " \n" + line;
    }

    protected String updateDdlForMySqlVersionLowerThan56(String ddlStatements) {
        return ddlStatements.replace("timestamp(3)",
                "timestamp").replace("datetime(3)",
                "datetime").replace("TIMESTAMP(3)",
                "TIMESTAMP").replace("DATETIME(3)",
                "DATETIME");
    }

    protected String readNextTrimmedLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            line = line.trim();
        }
        return line;
    }
}
