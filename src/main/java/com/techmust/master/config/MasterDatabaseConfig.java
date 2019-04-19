package com.techmust.master.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Configuration of the master database which holds information about tenants in
 * the application.
 * 
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.techmust.master", "com.techmust.model"}, entityManagerFactoryRef = "m_BeanMasterEntityManagerFactory", transactionManagerRef = "m_BeanMasterTransactionManager")
public class MasterDatabaseConfig
{
	private static final Logger LOG = LoggerFactory.getLogger(MasterDatabaseConfig.class);
	/**
	* Master database configuration properties like username, password, etc.
	**/
	@Autowired
	private MasterDatabaseConfigProperties m_BeanMasterDbProperties;
	/**
	 * Creates the master datasource bean which is required for creating the
	 * entity manager factory bean <br/>
	 * <br/>
	 * Note that using names for beans is not mandatory but it is a good
	 * practice to ensure that the intended beans are being used where required.
	 * 
	 * @return
	 */
	@Bean
	public DataSource createMasterDataSource()
	{
		LOG.info("Setting up masterDataSource with: "+ m_BeanMasterDbProperties.toString());
		HikariDataSource oHikariDataSource = new HikariDataSource();
		oHikariDataSource.setUsername(m_BeanMasterDbProperties.getUsername());
		oHikariDataSource.setPassword(m_BeanMasterDbProperties.getPassword());
		oHikariDataSource.setJdbcUrl(m_BeanMasterDbProperties.getUrl());
		oHikariDataSource.setDriverClassName(m_BeanMasterDbProperties.getDriverClassName());
		oHikariDataSource.setPoolName(m_BeanMasterDbProperties.getPoolName());
		// HikariCP settings
		// Maximum number of actual connection in the pool
		oHikariDataSource.setMaximumPoolSize(m_BeanMasterDbProperties.getMaxPoolSize());
		// Minimum number of idle connections in the pool
		oHikariDataSource.setMinimumIdle(m_BeanMasterDbProperties.getMinIdle());
		// Maximum waiting time for a connection from the pool
		oHikariDataSource.setConnectionTimeout(m_BeanMasterDbProperties.getConnectionTimeout());
		// Maximum time that a connection is allowed to sit idle in the pool
		oHikariDataSource.setIdleTimeout(m_BeanMasterDbProperties.getIdleTimeout());
		LOG.info("Setup of masterDataSource succeeded.");
		return oHikariDataSource;
	}
	
	/**
	 * Creates the entity manager factory bean which is required to access the
	 * JPA functionalities provided by the JPA persistence provider, i.e.
	 * Hibernate in this case. <br/>
	 * <br/>
	 * Note the <b>{@literal @}Primary</b> annotation which tells Spring boot to
	 * create this entity manager as the first thing when starting the
	 * application.
	 * 
	 * @return
	 */
	@Primary
	@Bean(name = "m_BeanMasterEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory()
	{
		LocalContainerEntityManagerFactoryBean oEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		// Set the master data source
		oEntityManagerFactoryBean.setDataSource(createMasterDataSource());
		// The master tenant entity and repository need to be scanned
		oEntityManagerFactoryBean.setPackagesToScan("com.techmust.master", "com.techmust.utils", "com.techmust.model", "com.techmust.generic.util", "com.techmust.usermanagement", "com.techmust.organization","com.techmust.facilitator");
		// Setting a name for the persistence unit as Spring sets it as
		// 'default' if not defined
		oEntityManagerFactoryBean.setPersistenceUnitName("masterdb-persistence-unit");
		// Setting Hibernate as the JPA provider
		JpaVendorAdapter oJpaVendorAdapter = new HibernateJpaVendorAdapter();
		oEntityManagerFactoryBean.setJpaVendorAdapter(oJpaVendorAdapter);
		// Set the hibernate properties
		oEntityManagerFactoryBean.setJpaProperties(hibernateProperties());
		LOG.info("Setup of masterEntityManagerFactory succeeded.");
		return oEntityManagerFactoryBean;
	}
	/**
	 * This transaction manager is appropriate for applications that use a
	 * single JPA EntityManagerFactory for transactional data access. <br/>
	 * <br/>
	 * Note the <b>{@literal @}Qualifier</b> annotation to ensure that the
	 * <tt>masterEntityManagerFactory</tt> is used for setting up the
	 * transaction manager.
	 * 
	 * @param emf
	 * @return
	 */
	
	private void dataSourceInitializer(DataSource oHikariDataSource) 
	{
	      ResourceDatabasePopulator oResourceDatabasePopulator = new ResourceDatabasePopulator();
	      oResourceDatabasePopulator.addScript(new ClassPathResource("/import.sql"));
	      DatabasePopulatorUtils.execute(oResourceDatabasePopulator, oHikariDataSource);
	}
	
	@Bean(name = "m_BeanMasterTransactionManager")
	public JpaTransactionManager masterTransactionManager(@Qualifier("m_BeanMasterEntityManagerFactory") EntityManagerFactory oEntityManagerFactory, DataSource oHikariDataSource)
	{
		JpaTransactionManager oJpaTransactionManager = new JpaTransactionManager();
		oJpaTransactionManager.setEntityManagerFactory(oEntityManagerFactory);
		dataSourceInitializer(oHikariDataSource);
		return oJpaTransactionManager;
	}
	/**
	 * Bean post-processor that automatically applies persistence exception
	 * translation to any bean marked with Spring's @Repository annotation,
	 * adding a corresponding PersistenceExceptionTranslationAdvisor to the
	 * exposed proxy (either an existing AOP proxy or a newly generated proxy
	 * that implements all of the target's interfaces).     
	 * 
	 * @return
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() 
	{
		return new PersistenceExceptionTranslationPostProcessor();
	}
	/**
	 * The properties for configuring the JPA provider Hibernate.
	 * 
	 * @return
	 */
	private Properties hibernateProperties()
	{
		Properties properties = new Properties();
		properties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
		properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
		properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
		properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");
		properties.put(org.hibernate.cfg.Environment.C3P0_MAX_SIZE, "1000");
		return properties;
	}
}