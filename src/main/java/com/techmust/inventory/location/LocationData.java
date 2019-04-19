package com.techmust.inventory.location;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac08_location")
public class LocationData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ac8_locationId")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int m_nLocationId;
	@Column(name="ac08_name")
	private String m_strName;
	@Column(name="ac08_address")
	private String m_strAddress;
	@Column(name = "ac08_is_default")
	private boolean m_bIsDefault;
	@Transient
	private boolean m_bMatchEqual;
	@Transient
	private boolean m_bFetchDefault;
	@Transient
	private UserInformationData m_oUserCredentialsData;

	public LocationData ()
	{
		m_nLocationId = -1;
		m_strName = "";
		m_strAddress = "";
		m_bMatchEqual = false;
		m_bIsDefault = false;
		setM_bFetchDefault(false);
	}
	
	public void setM_nLocationId(int nLocationId) 
	{
		this.m_nLocationId = nLocationId;
	}

	public int getM_nLocationId() 
	{
		return m_nLocationId;
	}

	public void setM_strName(String strName) 
	{
		this.m_strName = strName;
	}

	public String getM_strName() 
	{
		return m_strName;
	}

	public void setM_strAddress(String strAddress)
	{
		this.m_strAddress = strAddress;
	}

	public String getM_strAddress()
	{
		return m_strAddress;
	}

	public void setM_bIsDefault(boolean bIsDefault) 
	{
		this.m_bIsDefault = bIsDefault;
	}

	public boolean isM_bIsDefault() 
	{
		return m_bIsDefault;
	}
	
	public void setM_bFetchDefault(boolean m_bFetchDefault)
    {
	    this.m_bFetchDefault = m_bFetchDefault;
    }

	public boolean isM_bFetchDefault()
    {
	    return m_bFetchDefault;
    }

	public void setM_oUserCredentialsData(UserInformationData m_oUserCredentialsData) 
	{
		this.m_oUserCredentialsData = m_oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public boolean isM_bMatchEqual()
	{
		return m_bMatchEqual;
	}

	public void setM_bMatchEqual(boolean matchEqual) 
	{
		m_bMatchEqual = matchEqual;
	}

	@Override
	public String generateXML()
	{
		String strLocationXml = "";
		Document oXmlDocument = createNewXMLDocument ();
		Element oRootElement = createRootElement(oXmlDocument, "LocationData");
		addChild (oXmlDocument, oRootElement, "m_nLocationId", m_nLocationId);
		addChild (oXmlDocument, oRootElement, "m_strName", m_strName);
		addChild (oXmlDocument, oRootElement, "m_strAddress", m_strAddress);
		strLocationXml = getXmlString (oXmlDocument);
		return strLocationXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (!m_strName.isEmpty () && m_strName != null)
			oCriteria.add (Restrictions.ilike ("m_strName", m_strName.trim(), m_bMatchEqual ? MatchMode.EXACT : MatchMode.ANYWHERE));
		if (m_strAddress != null && !m_strAddress.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strAddress", m_strAddress));
		if (m_nLocationId > 0)
			oCriteria.add (Restrictions.eq ("m_nLocationId", m_nLocationId));
		if (m_bFetchDefault)
			oCriteria.add (Restrictions.eq ("m_bIsDefault", m_nLocationId));
		if (m_bIsDefault)
			oCriteria.add (Restrictions.eq ("m_bIsDefault", m_bIsDefault));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strName");
		return oCriteria;
	}

	public static LocationData getDefaultLocation() throws Exception 
	{
		LocationData oLocationData = new LocationData ();
		oLocationData.setM_bIsDefault(true);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return (LocationData) oLocationData.list(oOrderBy).get(0);
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strName.isEmpty () && m_strName != null)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strName")), m_strName.toLowerCase())); 
		if (m_strAddress != null && !m_strAddress.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strAddress")), m_strAddress.toLowerCase())); 
		if (m_nLocationId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLocationId"), m_nLocationId)); 
		if (m_bFetchDefault)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_bFetchDefault"), m_bFetchDefault)); 
		if (m_bIsDefault)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_bIsDefault"), m_bIsDefault)); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strName");
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nLocationId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLocationId"), m_nLocationId));
		return oConjunct;
	}
}
