package com.techmust.property;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.property.propertytype.PropertyTypeData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac43_property")
public class PropertyData extends TenantData
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac43_id")
	private int m_nPropertyId;
	@Column(name = "ac43_address")
	private String m_strAddress;
	@Column(name = "ac43_locality")
	private String m_strLocality;
	@Column(name = "ac43_city")
	private String m_strCity;
	@Column(name = "ac43_price")
	private int m_nPrice;
	@ManyToOne
	@JoinColumn(name = "ac43_property_type_id")
	@ColumnDefault("-1")
	private PropertyTypeData m_oPropertyType;
	@ManyToOne
	@JoinColumn(name = "ac43_property_detail_id")
	@ColumnDefault("-1")
	private PropertyDetailData m_oPropertyDetails;
	@ManyToOne
	@JoinColumn(name = "ac43_Client_id")
	@ColumnDefault("-1")
	private ClientData m_oClientData;
	@Column(name = "ac43_Created_by")
	@ColumnDefault("-1")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac43_created_on")
	private Date m_dCreatedOn;
	@Column(name = "ac43_active")
	@ColumnDefault("0")
	private Boolean m_bActive;
	@OneToMany
	@JoinColumn(name = "ac46_property_id")
	private Set<PropertyPhotoData> m_oPropertyPhoto;
	@Transient
	public PropertyPhotoData [] m_arrPropertyPhoto;
	
	public PropertyData () 
	{
		m_nPropertyId = -1;
		m_strAddress = "";
		m_strLocality = "";
		m_strCity = "";
		m_nPrice = 0;
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_bActive = false;
	}
	
	public int getM_nPropertyId ()
	{
		return m_nPropertyId;
	}
	
	public void setM_nPropertyId (int nPropertyId)
	{
		m_nPropertyId = nPropertyId;
	}
	
	public String getM_strAddress () 
	{
		return m_strAddress;
	}
	
	public void setM_strAddress (String strAddress)
	{
		m_strAddress = strAddress;
	}
	
	public String getM_strLocality () 
	{
		return m_strLocality;
	}
	
	public void setM_strLocality (String strLocality) 
	{
		m_strLocality = strLocality;
	}
	
	public String getM_strCity ()  
	{
		return m_strCity;
	}
	
	public void setM_strCity (String strCity)
	{
		m_strCity = strCity;
	}
	
	public int getM_nPrice () 
	{
		return m_nPrice;
	} 
	
	public void setM_nPrice (int nPrice)
	{
		m_nPrice = nPrice;
	}
	
	public PropertyTypeData getM_oPropertyType () 
	{
		return m_oPropertyType;
	}
	
	public void setM_oPropertyType (PropertyTypeData oPropertyType) 
	{
		m_oPropertyType = oPropertyType;
	}

	public PropertyDetailData getM_oPropertyDetails () 
	{
		return m_oPropertyDetails;
	}
	
	public void setM_oPropertyDetails (PropertyDetailData oPropertyDetails)
	{
		m_oPropertyDetails = oPropertyDetails;
	}

	public ClientData getM_oClientData ()
	{
		return m_oClientData;
	}
	
	public void setM_oClientData (ClientData oClientData) 
	{
		m_oClientData = oClientData;
	}
	
	public int getM_nCreatedBy () 
	{
		return m_nCreatedBy;
	}
	
	public void setM_nCreatedBy (int nCreatedBy)
	{
		m_nCreatedBy = nCreatedBy;
	}
	
	public Date getM_dCreatedOn () 
	{
		return m_dCreatedOn;
	}
	
	public void setM_dCreatedOn (Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}
	
	public Boolean getM_bActive () 
	{
		return m_bActive;
	}
	
	public void setM_bActive (Boolean bActive)
	{
		m_bActive =  bActive;
	}
	
	public Set<PropertyPhotoData> getM_oPropertyPhoto ()
	{
		return m_oPropertyPhoto;
	}

	public void setM_oPropertyPhoto (Set<PropertyPhotoData> oPropertyPhoto) 
	{
		m_oPropertyPhoto = oPropertyPhoto;
	}

	@Override
	public String generateXML () 
	{
		String strXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "PropertyData");
			addChild (oXmlDocument, oRootElement, "m_nPropertyId", m_nPropertyId);
			addChild (oXmlDocument, oRootElement, "m_strAddress", m_strAddress);
			addChild (oXmlDocument, oRootElement, "m_strLocality", m_strLocality);
			addChild (oXmlDocument, oRootElement, "m_strCity", m_strCity);
			addChild (oXmlDocument, oRootElement, "m_nPrice", m_nPrice);
			Document oPropertyTypeXmlDoc = getXmlDocument ("<m_oPropertyType>"+m_oPropertyType.generateXML()+"</m_oPropertyType>");
			Node oPropertyTypeNode = oXmlDocument.importNode (oPropertyTypeXmlDoc.getFirstChild(), true);
			oRootElement.appendChild (oPropertyTypeNode);
			Document oPropertyDetailsXmlDoc = getXmlDocument ("<m_oPropertyDetails>"+m_oPropertyDetails.generateXML()+"</m_oPropertyDetails>");
			Node oPropertyDetailsNode = oXmlDocument.importNode (oPropertyDetailsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild (oPropertyDetailsNode);
			Document oClientDoc = getXmlDocument (m_oClientData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			strXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strXml;
	}

	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nPropertyId > 0)
			oCriteria.add (Restrictions.eq ("m_nPropertyId", m_nPropertyId));
		if (m_strAddress != null && !m_strAddress.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strAddress", m_strAddress.trim()));
		if (m_strLocality != null && !m_strLocality.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strLocality", m_strLocality.trim()));
		if (m_strCity != null && !m_strCity.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strCity", m_strCity.trim()));
		if (m_oPropertyType != null && m_oPropertyType.getM_nPropertyTypeId() > 0)
			oCriteria.add(Restrictions.eq("m_oPropertyType", m_oPropertyType));
		if (m_oPropertyDetails != null && m_oPropertyDetails.getM_nDetailId() > 0)
			oCriteria.add(Restrictions.eq("m_oPropertyDetails", m_oPropertyDetails));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", m_oClientData));
		if (m_nCreatedBy > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPropertyId");
		return oCriteria;
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
		if (m_nPropertyId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nPropertyId"), m_nPropertyId));
		if (m_strAddress != null && !m_strAddress.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strAddress")), m_strAddress.trim())); 
		if (m_strLocality != null && !m_strLocality.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strLocality")), m_strLocality.trim()));
		if (m_strCity != null && !m_strCity.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCity")), m_strCity.trim()));
		if (m_oPropertyType != null && m_oPropertyType.getM_nPropertyTypeId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oPropertyType"), m_oPropertyType));
		if (m_oPropertyDetails != null && m_oPropertyDetails.getM_nDetailId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oPropertyDetails"), m_oPropertyDetails));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oClientData"), m_oClientData));
		if (m_nCreatedBy > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCreatedBy"), m_nCreatedBy));
		//addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPropertyId");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nPropertyId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nPropertyId"), m_nPropertyId)); 
		return oConjunct;
	}
}
