package org.zxp.funk.hopper.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//启用注解事务管理，使用CGLib代理
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories("org.zxp.funk.hopper.jpa")
public class JPAConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(JPAConfiguration.class);
	/*
	 * 绑定资源属性
	 */
	@Value("${jdbc.driver}")
	String driverClass;
	@Value("${jdbc.url}")
	String url;
	@Value("${jdbc.username}")
	String userName;
	@Value("${jdbc.password}")
	String passWord;
	//配置初始化大小、最小、最大 
	@Value("${ds.initialSize}")
	int initialSize;
	@Value("${ds.minIdle}")
	int minIdle;
	@Value("${ds.maxActive}")
	int maxActive;
	@Value("${ds.maxWait}")
	int maxWait;
	@Value("${ds.timeBetweenEvictionRunsMillis}")
	int timeBetweenEvictionRunsMillis;
	@Value("${ds.minEvictableIdleTimeMillis}")
	int minEvictableIdleTimeMillis;
	@Value("${ds.validationQuery}")
	String validationQuery;
	@Value("${ds.testWhileIdle}")
	boolean testWhileIdle;
	@Value("${ds.testOnBorrow}")
	boolean testOnBorrow;
	@Value("${ds.testOnReturn}")
	boolean testOnReturn;
	@Value("${ds.poolPreparedStatements}")
	boolean poolPreparedStatements;
	@Value("${ds.maxPoolPreparedStatementPerConnectionSize}")
	int maxPoolPreparedStatementPerConnectionSize;
	@Value("${ds.filters}")
	String filters;
	
	/*
	 * <!-- 配置初始化大小、最小、最大 -->
        <property name="initialSize" value="${ds.initialSize}"/>
        <property name="minIdle" value="${ds.minIdle}"/>
        <property name="maxActive" value="${ds.maxActive}"/>

        <!-- 配置获取连接等待超时的时间 -->
        <property name="maxWait" value="${ds.maxWait}"/>

        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
        <property name="timeBetweenEvictionRunsMillis" value="${ds.timeBetweenEvictionRunsMillis}"/>

        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
        <property name="minEvictableIdleTimeMillis" value="${ds.minEvictableIdleTimeMillis}"/>

        <property name="validationQuery" value="SELECT 'x'"/>
        <property name="testWhileIdle" value="true"/>
        <property name="testOnBorrow" value="false"/>
        <property name="testOnReturn" value="false"/>

        <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
        <property name="poolPreparedStatements" value="false"/>
        <property name="maxPoolPreparedStatementPerConnectionSize" value="20"/>

        <!-- 配置监控统计拦截的filters -->
        <property name="filters" value="stat"/>
	*/
	
	
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		com.alibaba.druid.pool.DruidDataSource dataSource = new com.alibaba.druid.pool.DruidDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(passWord);
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxWait(maxWait);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		try {
			dataSource.setFilters(filters);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataSource;
	}
	
	@Value("${hibernate.dialect}")
	String hibernate_dialect;
	@Value("${hibernate.show_sql}")
	boolean hibernate_show_sql;
	@Value("${hibernate.format_sql}")
	boolean hibernate_format_sql;
	
	@Bean(name={"sessionFactory"})
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "org.zxp.funk.hopper.entity" });
        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernate_dialect);
        properties.put("hibernate.show_sql", hibernate_show_sql);
        properties.put("hibernate.format_sql", hibernate_format_sql);
        sessionFactory.setHibernateProperties(properties);
        return sessionFactory;
     }
    
	 @Bean
     public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
         LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
         entityManagerFactoryBean.setDataSource(dataSource());
         entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
         entityManagerFactoryBean.setPackagesToScan(new String[] { "org.zxp.funk.hopper.jpa.model" });
         Properties properties = new Properties();
         properties.put("hibernate.dialect", hibernate_dialect);
         properties.put("hibernate.show_sql", hibernate_show_sql);
         properties.put("hibernate.format_sql", hibernate_format_sql);
         entityManagerFactoryBean.setJpaProperties(properties);
         return entityManagerFactoryBean;
     }
	
	 @Bean
     public JpaTransactionManager transactionManager() {
         JpaTransactionManager transactionManager = new JpaTransactionManager();
         transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
         return transactionManager;
     }
	  

  
}
