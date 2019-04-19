package com.techmust.clientmanagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "TCL05_Site")
public class SiteData extends TenantData 
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CL05_site_id")	
	private int m_nSiteId;
	@Column(name = "CL05_address")
	private String m_strSiteAddress;
	@Column(name = "CL05_name")
	private String m_strSiteName;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CL01_client_id",nullable = false)
	@JsonBackReference
	private ClientData m_oClientData;
	@Column(name = "CL05_STATUS")
	@Enumerated(EnumType.ORDINAL)
	private SiteStatus m_nSiteStatus;
	
	public SiteData ()
	{
		m_nSiteId = -1;
		m_strSiteAddress = "";
		m_strSiteName = "";
		m_nSiteStatus = SiteStatus.kActive;
		m_oClientData = new ClientData();
	}
	
	public int getM_nSiteId()
	{
		return m_nSiteId;
	}

	public void setM_nSiteId(int nSiteId) 
	{
		m_nSiteId = nSiteId;
	}

	public String getM_strSiteAddress() 
	{
		return m_strSiteAddress;
	}

	public void setM_strSiteAddress(String strSiteAddress) 
	{
		m_strSiteAddress = strSiteAddress;
	}

	public String getM_strSiteName() 
	{
		return m_strSiteName;
	}

	public void setM_strSiteName(String strSiteName) 
	{
		m_strSiteName = strSiteName;
	}

	@Override
	public String generateXML() 
	{
		String strSiteDataXml = "";
		m_oLogger.info ("generateXML");
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "SiteData");
			addChild (oXmlDocument, oRootElement, "m_nSiteId", m_nSiteId);
			addChild (oXmlDocument, oRootElement, "m_strSiteName", m_strSiteName);
			addChild (oXmlDocument, oRootElement, "m_strSiteAddress", m_strSiteAddress);
			addChild (oXmlDocument, oRootElement, "m_nSiteStatus", m_nSiteStatus.toString ());
			strSiteDataXml = getXmlString(oXmlDocument);
		} 
		catch (Exception oException ) 
		{
			m_oLogger.debug ("generateXML - oException" + oException);
		}
		m_oLogger.debug ("generateXML - strSiteDataXml [OUT] : " + strSiteDataXml);
		return strSiteDataXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn,
			String strOrderBy)
	{
		return oCriteria;
	}


	public void setM_oClientData(ClientData oClientData) 
	{
		this.m_oClientData = oClientData;
	}

	public ClientData getM_oClientData() 
	{
		return m_oClientData;
	}

	public void setM_nSiteStatus (SiteStatus nSiteStatus)
	{
		this.m_nSiteStatus = nSiteStatus;
	}

	public SiteStatus getM_nSiteStatus ()
	{
		return m_nSiteStatus;
	}

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData oCredentials) throws Exception
	{
		SiteData oSiteData = new SiteData ();
		try
		{
			Document oXMLDocument = getXmlDocument(strXML);
			Node oSiteNode = oXMLDocument.getFirstChild();
	    	oSiteData.setM_strSiteName(UserManagementInitializer.getValue(oSiteNode,"m_strSiteName"));
			oSiteData.setM_strSiteAddress(UserManagementInitializer.getValue(oSiteNode,"m_strSiteAddress "));
			oSiteData.setM_nSiteStatus(UserManagementInitializer.getValue(oSiteNode,"m_nSiteStatus").equalsIgnoreCase("Active") ? SiteStatus.kActive : SiteStatus.kInactive);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getInstance - oException : " + oException);
			throw oException;
		}
		return oSiteData;
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
		if (m_nSiteId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nSiteId"), m_nSiteId));
		return oConjunct;
	}
}
