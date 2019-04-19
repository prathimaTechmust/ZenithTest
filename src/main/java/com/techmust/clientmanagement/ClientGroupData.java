package com.techmust.clientmanagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tcl08_group")
public class ClientGroupData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cl08_groupid")
	private int m_nGroupId;
	@Column(name = "cl08_group_name")
	private String m_strGroupName;
	@ManyToMany
	@JoinTable(name = "tcl09_client_group", joinColumns = { @JoinColumn(name = "cl08_client_group_id") }, inverseJoinColumns = { @JoinColumn(name = "cl08_client_id") })
	private Set<ClientData> m_oClientSet;	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cl08_created_on")
	private Date m_dCreatedOn;
	@Column(name = "cl08_Created_by")
	private int m_nCreatedBy;
	@Transient
	public ClientData [] m_arrClientData;
	@Transient
	private String m_strDate;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Transient
	public int m_nClientId;
	
	public ClientGroupData ()
	{
		m_nGroupId = -1;
		m_strGroupName = "";
		m_nClientId = -1;
		m_dCreatedOn = Calendar.getInstance().getTime ();
		setM_oClientSet(new HashSet<ClientData> ());
	}

	public int getM_nGroupId() 
	{
		return m_nGroupId;
	}

	public void setM_nGroupId(int nGroupId) 
	{
		m_nGroupId = nGroupId;
	}

	public String getM_strGroupName()
	{
		return m_strGroupName;
	}

	public void setM_strGroupName(String strGroupName) 
	{
		m_strGroupName = strGroupName;
	}

	public Date getM_dCreatedOn()
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public int getM_nCreatedBy() 
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public void setM_oClientSet(Set<ClientData> oClientSet) 
	{
		m_oClientSet = oClientSet;
	}

	public Set<ClientData> getM_oClientSet() 
	{
		return m_oClientSet;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	@Override
	public String generateXML() 
	{
		String strXML = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ClientGroupData");
			addChild (oXmlDocument, oRootElement, "m_nGroupId", m_nGroupId);
			addChild (oXmlDocument, oRootElement, "m_strGroupName", m_strGroupName);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
//			addChild (oXmlDocument, oRootElement, "m_strUserName", getM_oCreatedBy() != null ? getM_oCreatedBy().getM_strUserName() : "");
			Node oClientXmlNode = oXmlDocument.importNode (getXmlDocument ("<m_oClientSet>"+ getClientGroupXML ()+"</m_oClientSet>").getFirstChild (), true);
			oRootElement.appendChild (oClientXmlNode);
			strXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strXML;
	}

	private String getClientGroupXML() 
	{
		m_oLogger.info ("getClientGroupXML");
		String strXml = "";
	    Iterator<ClientData> oIterator = m_oClientSet.iterator ();
	    while (oIterator.hasNext ())
	    	strXml += ((ClientData)oIterator.next ()).generateXML ();
	    m_oLogger.debug ( "getClientGroupXML - strXml [OUT] : " + strXml);
		return strXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (!m_strGroupName.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strGroupName", m_strGroupName.trim(), MatchMode.ANYWHERE));
		if (m_nGroupId > 0)
			oCriteria.add (Restrictions.eq ("m_nGroupId", m_nGroupId));
		if (m_nClientId > 0)
		{
			oCriteria.createAlias("m_oClientSet", "Clients");
			oCriteria.add(Restrictions.eq("Clients.m_nClientId", m_nClientId));
		}
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strGroupName");
		return oCriteria;
	}

	
	@SuppressWarnings("unchecked")
	public static ArrayList<ClientGroupData> getClientGroups (ClientData oClientData) throws Exception 
	{
		ClientGroupData oClientGroupData = new ClientGroupData ();
		oClientGroupData.m_nClientId = oClientData.getM_nClientId();
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return new ArrayList(oClientGroupData.list(oOrderBy));
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strGroupName.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strGroupName")), m_strGroupName.toLowerCase())); 
		if (m_nGroupId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nGroupId"), m_nGroupId)); 
		if (m_nClientId > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("Clients.m_nClientId"), m_nClientId)); 
		}
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strGroupName");
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nGroupId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nGroupId"), m_nGroupId));
		return oConjunct;
	}
}
