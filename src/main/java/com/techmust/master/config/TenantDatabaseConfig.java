package com.techmust.master.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This is the tenant data sources configuration which sets up the multitenancy.
 *
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.techmust.tenant"})

public class TenantDatabaseConfig
{
	private static final Logger LOG = LoggerFactory.getLogger(TenantDatabaseConfig.class);	
	
	public JpaVendorAdapter jpaVendorAdapter()
	{
		return new HibernateJpaVendorAdapter();
	}

	@Bean(name = "m_BeanTenantTransactionManager")
	public JpaTransactionManager transactionManager(@Qualifier("m_BeanTenantEntityManagerFactory") EntityManagerFactory oTenantEntityManagerFactory)
	{
		JpaTransactionManager oJpaTransactionManager = new JpaTransactionManager();
		oJpaTransactionManager.setEntityManagerFactory(oTenantEntityManagerFactory);
		return oJpaTransactionManager;
	}

	/**
	 * The multi tenant connection provider
	 * 
	 * @return
	 */
	@Bean(name = "m_BeanDatasourceBasedMultitenantConnectionProvider")
	@ConditionalOnBean(name = "m_BeanMasterEntityManagerFactory")
	public MultiTenantConnectionProvider multiTenantConnectionProvider()
	{
		// Autowires the multi connection provider
		return new DataSourceBasedMultiTenantConnectionProvider();
	}

	/**
	 * The current tenant identifier resolver
	 * 
	 * @return
	 */
	@Bean(name = "m_BeanCurrentTenantIdentifierResolver")
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver()
	{
		return new CurrentTenantIdentifierResolverImpl();
	}

	/**
	 * Creates the entity manager factory bean which is required to access the
	 * JPA functionalities provided by the JPA persistence provider, i.e.
	 * Hibernate in this case.
	 * 
	 * @param connectionProvider
	 * @param tenantResolver
	 * @return
	 */
	@Bean(name = "m_BeanTenantEntityManagerFactory")
	@ConditionalOnBean(name = "m_BeanDatasourceBasedMultitenantConnectionProvider")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("m_BeanDatasourceBasedMultitenantConnectionProvider") MultiTenantConnectionProvider oMultiTenantConnectionProvider, @Qualifier("m_BeanCurrentTenantIdentifierResolver") CurrentTenantIdentifierResolver oCurrentTenantIdentifierResolver)
	{
		LocalContainerEntityManagerFactoryBean oLocalContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		oLocalContainerEntityManagerFactoryBean.setPackagesToScan("com.techmust.clientmanagement", "com.techmust.vendormanagement", "com.techmust.inventory", "com.techmust.generic", "com.techmust.property");
		oLocalContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		oLocalContainerEntityManagerFactoryBean.setPersistenceUnitName("tenantdb-persistence-unit");
		Map<String, Object> arrPropertiesMap = new HashMap<>();
		arrPropertiesMap.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
		arrPropertiesMap.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, oMultiTenantConnectionProvider);
		arrPropertiesMap.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, oCurrentTenantIdentifierResolver);
		arrPropertiesMap.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
		arrPropertiesMap.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
		arrPropertiesMap.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
		arrPropertiesMap.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");
		arrPropertiesMap.put(org.hibernate.cfg.Environment.C3P0_TIMEOUT, "1800");
		arrPropertiesMap.put(org.hibernate.cfg.Environment.C3P0_MIN_SIZE, "1");
		arrPropertiesMap.put(org.hibernate.cfg.Environment.C3P0_MAX_SIZE, "15");
		arrPropertiesMap.put(org.hibernate.cfg.Environment.C3P0_MAX_STATEMENTS , "15");
		oLocalContainerEntityManagerFactoryBean.setJpaPropertyMap(arrPropertiesMap);
		LOG.info("tenantEntityManagerFactory set up successfully!" + "PersistenceUnitName : tenantdb-persistence-unit");
		return oLocalContainerEntityManagerFactoryBean;
	}
}