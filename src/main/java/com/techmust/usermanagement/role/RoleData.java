package com.techmust.usermanagement.role;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Size;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "role_table")
public class RoleData extends MasterData
{
	private static final long serialVersionUID = 1L;
	@Transient
	public ActionData [] m_arrActionData;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ROLE_ID")
	private int m_nRoleId;
	@Column(name = "ROLE_NAME")
	@Size(max = 128)
	private String m_strRoleName;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "ROLE_ACTION_TABLE", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "ACTION_ID") })
	private Set<ActionData> m_oActions;
	
	public RoleData ()
	{
		m_nRoleId = -1;
		m_strRoleName = "";
		m_oActions = new HashSet<ActionData> ();
	}

	public void setM_nRoleId (int nRoleId) 
	{
		m_nRoleId = nRoleId;
	}

	public int getM_nRoleId () 
	{
		return m_nRoleId;
	}

	public void setM_strRoleName (String strRoleName) 
	{
		m_strRoleName = strRoleName;
	}

	public String getM_strRoleName () 
	{
		return m_strRoleName;
	}

	public Set<ActionData> getm_oActions ()
	{
		return m_oActions;
	}
	
	public void setm_oActions (Set<ActionData> oActions)
	{
		m_oActions = oActions;
	}

	@Override
    public String generateXML ()
    {
		m_oLogger.info ("generateXML");
		String strXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "RoleData");
			addChild (oXmlDocument, oRootElement, "m_nRoleId", m_nRoleId);
			addChild (oXmlDocument, oRootElement, "m_strRoleName", m_strRoleName);
			Node oActionsXmlNode = oXmlDocument.importNode (getXmlDocument ("<m_oActions>"+getActionsXML ()+"</m_oActions>").getFirstChild (), true);
			oRootElement.appendChild (oActionsXmlNode);
			strXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
		m_oLogger.debug ( "generateXML - strXml [OUT] : " + strXml);
		return strXml;
    }
	
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if(!m_strRoleName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strRoleName", m_strRoleName, MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nRoleId");
		return oCriteria;
	}
	
	private String getActionsXML ()
    {
		m_oLogger.info ("getActionsXML");
		String strXml = "";
	    Iterator<ActionData> oIterator = m_oActions.iterator ();
	    while (oIterator.hasNext ())
	    {
	    	strXml += ((ActionData)oIterator.next ()).generateXML ();
	    }
	    m_oLogger.debug ( "getActionsXML - strXml [OUT] : " + strXml);
		return strXml;
    }

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData oCredentials) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(!m_strRoleName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strRoleName")), m_strRoleName.toLowerCase())); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(!m_strRoleName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strRoleName")), m_strRoleName.toLowerCase())); 
		if(m_nRoleId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nRoleId"), m_nRoleId)); 
		return oConjunct;
	}
}