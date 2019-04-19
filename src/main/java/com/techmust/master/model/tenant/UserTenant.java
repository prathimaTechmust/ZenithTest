package com.techmust.master.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.techmust.generic.data.MasterData;
import com.techmust.organization.OrganizationInformationData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name="user_tenants")
public class UserTenant extends MasterData 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="usertenant_id")
	private int m_nUserTenantID;

	@ManyToOne
	@JoinColumn(name="user_id")
	private UserInformationData m_oUser;
	
	@ManyToOne
	@JoinColumn(name="org_id")
	private OrganizationInformationData m_oOrganization;

	public void setM_nUserTenantID(int m_nUserTenantID) 
	{
		this.m_nUserTenantID = m_nUserTenantID;
	}

	public UserInformationData getM_oUser() 
	{
		return m_oUser;
	}

	public void setM_oUser(UserInformationData m_oUser) 
	{
		this.m_oUser = m_oUser;
	}

	public OrganizationInformationData getM_oOrganization() 
	{
		return m_oOrganization;
	}

	public void setM_oOrganization(OrganizationInformationData m_oOrganization) 
	{
		this.m_oOrganization = m_oOrganization;
	}
}
