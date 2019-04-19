package com.techmust.master.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techmust.master.model.tenant.MasterTenant;
import com.techmust.master.repository.MasterTenantRepository;

/**
 * Implementation of the {@link MasterTenantService} which accesses the
 * {@link MasterTenant} entity. This is the recommended way to access the
 * entities through an interface rather than using the corresponding repository.
 * This allows for separation into repository code and the service layer.
 * 
 */
@Service
public class MasterTenantServiceImpl implements MasterTenantService
{
	@Autowired
	MasterTenantRepository masterTenantRepo;

	@Override
	public MasterTenant findByTenantId(int tenantId)
	{
		return masterTenantRepo.findByTenantId(tenantId);
	}
}