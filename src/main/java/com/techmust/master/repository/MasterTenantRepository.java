package com.techmust.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techmust.master.model.tenant.MasterTenant;

@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> 
{
	@Query("select tenant_id from MasterTenant tenant_id where tenant_id.m_nTenantId = :tenantId")
	MasterTenant findByTenantId(@Param("tenantId") int tenantId);
}