package com.techmust.master.config;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.DataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.techmust.constants.Constants;
import com.techmust.master.model.tenant.MasterTenant;
import com.techmust.master.repository.MasterTenantRepository;
import com.techmust.utils.DBUtil;

@Configuration
public class DataSourceBasedMultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
{
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceBasedMultiTenantConnectionProviderImpl.class);
	private static final long serialVersionUID = 1L;
	@Autowired
	private MasterTenantRepository m_oMasterTenantRepo;
	
	private Map<Integer, DataSource> m_arrMTdataSourcesMap = new TreeMap<>();
	
	@Override
	protected DataSource selectAnyDataSource()
	{
		// This method is called more than once. So check if the data source map
		// is empty. If it is then rescan master_tenant table for all tenant
		// entries.
		if (m_arrMTdataSourcesMap.isEmpty())
			initTenants ();
		return m_arrMTdataSourcesMap.get(Constants.DEFAULT_TENANT_ID);
	}
	 
	@Override
	protected DataSource selectDataSource(String strTenantIdentifier) 
	{
		if (!this.m_arrMTdataSourcesMap.containsKey(strTenantIdentifier)) 
		{
			List<MasterTenant> arrMasterTenantList = m_oMasterTenantRepo.findAll();
			LOG.info(">>>> selectDataSource() -- tenant:" + strTenantIdentifier + " Total tenants:" + arrMasterTenantList.size());
			for (MasterTenant oNthMasterTenant : arrMasterTenantList)
				m_arrMTdataSourcesMap.put(oNthMasterTenant.getM_nTenantId(), DBUtil.createAndConfigureDataSource(oNthMasterTenant));
		}
		return this.m_arrMTdataSourcesMap.get(strTenantIdentifier);
	}
	
	private void initTenants ()
	{
		List<MasterTenant> arrMasterTenantList = m_oMasterTenantRepo.findAll();
		LOG.info(">>>> selectAnyDataSource() -- Total tenants:" + arrMasterTenantList.size());
		for (MasterTenant oNthMasterTenant : arrMasterTenantList) 
			m_arrMTdataSourcesMap.put(oNthMasterTenant.getM_nTenantId(), DBUtil.createAndConfigureDataSource(oNthMasterTenant));

	}
}