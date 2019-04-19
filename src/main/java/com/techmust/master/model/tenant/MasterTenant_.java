package com.techmust.master.model.tenant;

import com.techmust.organization.OrganizationInformationData;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MasterTenant.class)
public abstract class MasterTenant_ {

	public static volatile SingularAttribute<MasterTenant, String> m_strUrl;
	public static volatile SingularAttribute<MasterTenant, String> m_strConnectionPassword;
	public static volatile SingularAttribute<MasterTenant, String> m_strConnectionUsername;
	public static volatile SingularAttribute<MasterTenant, OrganizationInformationData> m_oOrgData;
	public static volatile SingularAttribute<MasterTenant, String> m_strTenantId;

	public static final String M_STR_URL = "m_strUrl";
	public static final String M_STR_CONNECTION_PASSWORD = "m_strConnectionPassword";
	public static final String M_STR_CONNECTION_USERNAME = "m_strConnectionUsername";
	public static final String M_O_ORG_DATA = "m_oOrgData";

}

