package com.techmust.vendormanagement;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tcl06_vendor_group")
public class VendorGroupData extends TenantData
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cl06_vendor_group_id")
	private int m_nGroupId;
	@Column(name = "cl08_group_name")
	private String m_strGroupName; 
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="cl08_created_on")
	private Date m_dCreatedOn;
	@Column(name = "cl08_Created_by")
	private int m_nCreatedBy;
	@ManyToMany
	@JoinTable(name = "tcl07_group_vendors", joinColumns = { @JoinColumn(name = "cl07_group_id") }, inverseJoinColumns = { @JoinColumn(name = "cl07_vendor_id")})
	private Set<VendorData> m_oVendors;
	@Transient
	public VendorData [] m_arrVendorData;
	@Transient
	private String m_strDate;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	
	public VendorGroupData ()
	{
		m_strGroupName = "";
		m_nGroupId = -1;
		m_dCreatedOn = Calendar.getInstance().getTime();
		setM_oVendors(new HashSet<VendorData> ());
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

	public Set<VendorData> getM_oVendors() 
	{
		return m_oVendors;
	}

	public void setM_oVendors(Set<VendorData> oVendors) 
	{
		m_oVendors = oVendors;
	}

	public VendorData[] getM_arrVendorData() 
	{
		return m_arrVendorData;
	}

	public void setM_arrVendorData(VendorData[] arrVendorData)
	{
		m_arrVendorData = arrVendorData;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData()
	{
		return m_oUserCredentialsData;
	}

	@Override
	public String generateXML() 
	{
		String strVendorGroupXml = "";
		Document oXmlDocument = createNewXMLDocument ();
		Element oRootElement = createRootElement(oXmlDocument, "VendorGroupData");
		addChild (oXmlDocument, oRootElement, "m_nGroupId", m_nGroupId);
		addChild (oXmlDocument, oRootElement, "m_strGroupName", m_strGroupName);
		addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
//		addChild (oXmlDocument, oRootElement, "m_strUserName", getM_oCreatedBy() != null ? getM_oCreatedBy().getM_strUserName() : "");
		Node oVendorsXmlNode = oXmlDocument.importNode (getXmlDocument ("<m_oVendors>"+ getVendorGroupXML ()+"</m_oVendors>").getFirstChild (), true);
		oRootElement.appendChild (oVendorsXmlNode);
		strVendorGroupXml = getXmlString (oXmlDocument);
		return strVendorGroupXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (!m_strGroupName.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strGroupName", m_strGroupName.trim(), MatchMode.ANYWHERE));
		if (m_nGroupId > 0)
			oCriteria.add (Restrictions.eq ("m_nGroupId", m_nGroupId));
		if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria ("m_oCreatedBy").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strGroupName");
		return oCriteria;
	}
	
	private String getVendorGroupXML ()
    {
		m_oLogger.info ("getVendorGroupXML");
		String strXml = "";
	    Iterator<VendorData> oIterator = m_oVendors.iterator ();
	    while (oIterator.hasNext ())
	    	strXml += ((VendorData)oIterator.next ()).generateXML ();
	    m_oLogger.debug ( "getVendorGroupXML - strXml [OUT] : " + strXml);
		return strXml;
    }

	@Override
	public GenericData getInstanceData(String strXML,
			UserInformationData oCredentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strGroupName.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strGroupName")), m_strGroupName.trim())); 
		if (m_nGroupId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nGroupId"), m_nGroupId));	
		/*if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria ("m_oCreatedBy").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));*/
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nGroupId"), m_nGroupId)); 
		return oConjunct;
	}
}