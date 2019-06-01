package com.esb.env;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * @Description:配置数据库
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @Company: 
 * @author yangzl 2019-5-14
 * @version 1.00.00
 * @history:
 */
@Configuration
public class DBConfig {

	@ConfigurationProperties(prefix = "spring.datasource") // 配置文件中读取spring.datasource
	@Bean
	public DataSource druid() {

		return new DruidDataSource();
	}

	@Bean
	public LocalSessionFactoryBean localSessionFactoryBean() throws IOException {

		LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
		sfb.setDataSource(druid());
		sfb.setPackagesToScan(new String[] { "com.esb.entity" });
		Properties props = new Properties();
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.current_session_context_class",
				"org.springframework.orm.hibernate5.SpringSessionContext");
		sfb.setHibernateProperties(props);
		sfb.afterPropertiesSet();
		return sfb;
	}

	@Bean()
	@Primary
	public SessionFactory sessionFactory(LocalSessionFactoryBean localSessionFactoryBean)
			throws PropertyVetoException, IOException {
		SessionFactory sessionFactory = localSessionFactoryBean.getObject();
		return sessionFactory;
	}
	
/*	@Bean
	public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
		
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager(sessionFactory);
		hibernateTransactionManager.afterPropertiesSet();
		return hibernateTransactionManager;
	}*/
	
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
		dataSourceTransactionManager.afterPropertiesSet();
		return dataSourceTransactionManager;
	}
	
	@Bean
	public TransactionTemplate transactionTemplate(DataSourceTransactionManager d) {
		
		TransactionTemplate t = new TransactionTemplate(d);
		t.afterPropertiesSet();
		return t;
	}
	
	
	/*@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setPackagesToScan(new String[] { "com.example.demo.entity" });
		// 设置完属性之后需要调用 afterPropertiesSet方法使配置生效
		Properties props = new Properties();
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.current_session_context_class",
				"org.springframework.orm.hibernate5.SpringSessionContext");
		em.setJpaProperties(props);
		em.afterPropertiesSet();
		return em;
	}

	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(5);
		return registrationBean;
	}*/

	// 配置Druid的监控
	// 1.配置一个管理后台的servlet
	@Bean
	public ServletRegistrationBean statViewServlet() {
		// 处理druid下的所有请求
		ServletRegistrationBean bean = new ServletRegistrationBean(
				new StatViewServlet(), "/druid/*");
		Map<String, String> map = new HashMap<>();
		map.put("loginUsername", "admin");
		map.put("loginPassword", "123456");
		//map.put("allow", "localhost");// 不写就是允许所有
		// map.put("deny", "192.168.1.195");//拒绝IP访问
		bean.setInitParameters(map);
		return bean;
	}

	public FilterRegistrationBean webStatFilter() {

		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new WebStatFilter());
		Map<String, String> map = new HashMap<>();
		map.put("exclusions", "*.js,*.css,/druid/*");
		bean.setInitParameters(map);
		bean.setUrlPatterns(Arrays.asList("/*"));

		return bean;
	}
}
