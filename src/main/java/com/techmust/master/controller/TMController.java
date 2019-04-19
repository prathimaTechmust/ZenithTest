package com.techmust.master.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class TMController
{
	@Autowired
	protected DataSource m_datasource;

	@PersistenceContext
	protected EntityManager m_oMasterEntityManager;

	@PersistenceContext
	protected EntityManager m_oTenantEntityManager;
}