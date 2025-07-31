package fun.wswj.ai.config;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * @Author sws
 * @Date 2025/7/29 15:49
 * @description:
 */
@Configuration
public class DataSourceConfig {

    @Bean("mysqlDataSource")
    public DataSource mysqlDataSource(@Value("${spring.datasource.mysql.url}") String url,
                                      @Value("${spring.datasource.mysql.driver-class-name}") String driverClassName,
                                      @Value("${spring.datasource.mysql.username}") String username,
                                      @Value("${spring.datasource.mysql.password}") String passWord,
                                      @Value("${spring.datasource.mysql.hikari.pool-name}") String poolName,
                                      @Value("${spring.datasource.mysql.hikari.maximum-pool-size:10}") int maximumPoolSize,
                                      @Value("${spring.datasource.mysql.hikari.minimum-idle:5}") int minimumIdle,
                                      @Value("${spring.datasource.mysql.hikari.idle-timeout:30000}") long idleTimeout,
                                      @Value("${spring.datasource.mysql.hikari.connection-timeout:30000}") long connectionTimeout,
                                      @Value("${spring.datasource.mysql.hikari.max-lifetime:1800000}") long maxLifetime) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(passWord);

        dataSource.setPoolName(poolName);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMinimumIdle(minimumIdle);
        dataSource.setIdleTimeout(idleTimeout);
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setMaxLifetime(maxLifetime);
        return dataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("mysqlDataSource") DataSource mysqlDataSource) throws Exception {
        // mybatis 的sqlsession工厂bean
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mysqlDataSource);

        // mybatis 配置文件
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:/mybatis/config/mybatis-config.xml"));

        // 设置Mapper XML文件位置
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/mapper/*.xml"));

        return sqlSessionFactoryBean;
    }

    @Bean("sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactoryBean sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(Objects.requireNonNull(sqlSessionFactory.getObject()));
    }

    @Bean("pgVectorDataSource")
    public DataSource pgVectorDataSource(@Value("${spring.datasource.pgvector.url}") String url,
                                         @Value("${spring.datasource.pgvector.driver-class-name}") String driverClassName,
                                         @Value("${spring.datasource.pgvector.username}") String username,
                                         @Value("${spring.datasource.pgvector.password}") String passWord,
                                         @Value("${spring.datasource.pgvector.hikari.pool-name}") String poolName,
                                         @Value("${spring.datasource.pgvector.hikari.maximum-pool-size:10}") int maximumPoolSize,
                                         @Value("${spring.datasource.pgvector.hikari.minimum-idle:5}") int minimumIdle,
                                         @Value("${spring.datasource.pgvector.hikari.idle-timeout:30000}") long idleTimeout,
                                         @Value("${spring.datasource.pgvector.hikari.connection-timeout:30000}") long connectionTimeout){

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(passWord);

        dataSource.setPoolName(poolName);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMinimumIdle(minimumIdle);
        dataSource.setIdleTimeout(idleTimeout);
        dataSource.setConnectionTimeout(connectionTimeout);
        // 确保在启动时连接数据库
        dataSource.setInitializationFailTimeout(1);  // 设置为1ms，如果连接失败则快速失败
        dataSource.setConnectionTestQuery("SELECT 1"); // 简单的连接测试查询
        dataSource.setAutoCommit(true);

        return dataSource;
    }

    @Bean("pgVectorJdbcTemplate")
    public JdbcTemplate pgVectorJdbcTemplate(@Qualifier("pgVectorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
