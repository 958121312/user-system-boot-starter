package com.zjdx.user.security.datasource;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author yuntian
 * @since 2022-02-23
 * 初始化数据库配置
 */
@Component
@AllArgsConstructor
public class InitDataSource {
    private final SqlSessionFactory sqlSessionFactory;

    private final DataSource dataSource;

    @PostConstruct
    public void init() throws SQLException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CheckTableIsPresent checkTableIsPresent = new CheckTableIsPresent(dataSource);
        if(!checkTableIsPresent.check("sys_users") && !checkTableIsPresent.check("sys_roles")) {
            DbSqlSession dbSqlSession = new DbSqlSession(sqlSession);
            dbSqlSession.executeMandatorySchemaResource("create");
        }
    }
}
