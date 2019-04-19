package com.techmust.inventory.crm;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.clientmanagement.ClientGroupData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.email.AttachmentData;
import com.techmust.generic.email.template.TemplateData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class PubliciseData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	private TemplateData m_oTemplateData;
	private int m_nNoOfDays;
	private boolean m_bIsBought;
	private int m_nItemGroupId;
	private int m_nClientGroupId;
	private int m_nItemId;
	private String m_strSubject;  
	private UserInformationData m_oUserCredentialsData;
	private UserInformationData m_oCreatedBy;
	private String m_strFromDate;
	private String m_strToDate;
	public AttachmentData [] m_arrAttachmentData;
	public ClientGroupData [] m_arrClientGroup;
	
	public PubliciseData ()
	{
		setM_oTemplateData(new TemplateData ());
		m_nNoOfDays = 0;
		m_bIsBought = false;
		m_nItemGroupId = -1;
		m_nClientGroupId = -1;
		m_nItemId = -1;
		m_strSubject = "";
		m_oUserCredentialsData = new UserInformationData ();
		setM_oCreatedBy(new UserInformationData ());
	}
	
	public TemplateData getM_oTemplateData() 
	{
		return m_oTemplateData;
	}
	
	public void setM_oTemplateData(TemplateData oTemplateData) 
	{
		m_oTemplateData = oTemplateData;
	}

	public int getM_nNoOfDays() 
	{
		return m_nNoOfDays;
	}
	
	public void setM_nNoOfDays(int nNoOfDays) 
	{
		m_nNoOfDays = nNoOfDays;
	}

	public boolean isM_bIsBought() 
	{
		return m_bIsBought;
	}

	public void setM_bIsBought(boolean bIsBought) 
	{
		m_bIsBought = bIsBought;
	}

	public int getM_nItemGroupId() 
	{
		return m_nItemGroupId;
	}

	public void setM_nItemGroupId(int nItemGroupId) 
	{
		m_nItemGroupId = nItemGroupId;
	}

	public int getM_nClientGroupId() 
	{
		return m_nClientGroupId;
	}

	public void setM_nClientGroupId(int nClientGroupId) 
	{
		m_nClientGroupId = nClientGroupId;
	}

	public int getM_nItemId() 
	{
		return m_nItemId;
	}

	public void setM_nItemId(int nItemId) 
	{
		m_nItemId = nItemId;
	}

	public void setM_strSubject(String strSubject) 
	{
		m_strSubject = strSubject;
	}

	public String getM_strSubject() 
	{
		return m_strSubject;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public void setM_oCreatedBy(UserInformationData oCreatedBy) 
	{
		m_oCreatedBy = oCreatedBy;
	}

	public UserInformationData getM_oCreatedBy()
	{
		return m_oCreatedBy;
	}
	
	public void setM_strFromDate (String strFromDate)
	{
		this.m_strFromDate = strFromDate;
	}

	public String getM_strFromDate () 
	{
		return m_strFromDate;
	}

	public void setM_strToDate (String strToDate) 
	{
		this.m_strToDate = strToDate;
	}

	public String getM_strToDate ()
	{
		return m_strToDate;
	}
	
	@Override
	public String generateXML() 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		return null;
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oUserInformationData) throws Exception 
	{
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}
