package com.techmust.master.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.techmust.generic.data.MasterData;
import com.techmust.organization.OrganizationInformationData;

/**
 * This JPA entity represents the <tt>master_tenant</tt> table in the
 * <tt>masterdb</tt> database. This table holds the details of the tenant
 * databases.
 * 
 */
@Entity
@Table(name = "master_tenant")
public class MasterTenant extends MasterData
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tenant_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int m_nTenantId;

	@Size(max = 256)
	@Column(name = "db_url")
	private String m_strUrl;

	@Size(max = 64)
	@Column(name = "db_username")
	private String m_strConnectionUsername;

	@Size(max = 64)
	@Column(name = "db_password")
	private String m_strConnectionPassword;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="org_id")
	private OrganizationInformationData m_oOrgData;
	
	public int getM_nTenantId() 
	{
		return m_nTenantId;
	}

	public void setM_nTenantId(int m_nTenantId) 
	{
		this.m_nTenantId = m_nTenantId;
	}

	public String getM_strUrl() 
	{
		return m_strUrl;
	}

	public void setM_strUrl(String m_strUrl) 
	{
		this.m_strUrl = m_strUrl;
	}

	public String getM_strConnectionUsername() 
	{
		return m_strConnectionUsername;
	}

	public void setM_strConnectionUsername(String m_strConnectionUsername) 
	{
		this.m_strConnectionUsername = m_strConnectionUsername;
	}

	public String getM_strConnectionPassword() 
	{
		return m_strConnectionPassword;
	}

	public void setM_strConnectionPassword(String m_strConnectionPassword) 
	{
		this.m_strConnectionPassword = m_strConnectionPassword;
	}

	public OrganizationInformationData getM_oOrgData() 
	{
		return m_oOrgData;
	}

	public void setM_oOrgData(OrganizationInformationData m_oOrgData) 
	{
		this.m_oOrgData = m_oOrgData;
	}
}