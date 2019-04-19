package com.techmust.property;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac44_property_detail")
public class PropertyDetailData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac44_detail_id")
	private int m_nDetailId;
	@Column(name = "ac44_description")
	private String m_strDescription;
	@Column(name = "ac44_bhk")
	private int m_nBHK;
	@Column(name = "ac44_size")
	private String m_strSize;
	@Column(name = "ac44_toilets")
	private int m_nToilets;
	@Column(name = "ac01_study_room")
	@ColumnDefault("0")
	private boolean m_bStudyRoom;
	@Column(name = "ac44_balcony")
	@ColumnDefault("0")
	private boolean m_bBalcony;
	@Column(name = "ac44_park")
	@ColumnDefault("0")
	private boolean m_bPark;
	@Column(name = "ac44_bus_stop")
	@ColumnDefault("0")
	private boolean m_bBusStop;
	@Column(name = "ac44_school")
	@ColumnDefault("0")
	private boolean m_bSchool;
	@Column(name = "ac44_railway_station")
	@ColumnDefault("0")
	private boolean m_bRailwayStation;
	@Column(name = "ac44_shopping_mall")
	@ColumnDefault("0")
	private boolean m_bShoppingMall;
	
	public PropertyDetailData ()
	{
		m_nDetailId = -1;
		m_strDescription = "";
		m_nBHK = 0;
		m_strSize = "";
		m_nToilets = 0;
		m_bStudyRoom = false;
		m_bBalcony = false;
		m_bPark = false;
		m_bBusStop = false;
		m_bSchool = false;
		m_bRailwayStation = false;
		m_bShoppingMall = false;
	}

	public void setM_nDetailId(int m_nDetailId) 
	{
		this.m_nDetailId = m_nDetailId;
	}

	public int getM_nDetailId() 
	{
		return m_nDetailId;
	}

	public void setM_strDescription(String m_strDescription) 
	{
		this.m_strDescription = m_strDescription;
	}

	public String getM_strDescription() 
	{
		return m_strDescription;
	}

	public void setM_nBHK(int m_nBHK) 
	{
		this.m_nBHK = m_nBHK;
	}

	public int getM_nBHK() 
	{
		return m_nBHK;
	}

	public void setM_strSize(String m_strSize) 
	{
		this.m_strSize = m_strSize;
	}

	public String getM_strSize() 
	{
		return m_strSize;
	}

	public void setM_nToilets (int nToilets) 
	{
		m_nToilets = nToilets;
	}
	
	public int getM_nToilets ()
	{
		return m_nToilets;
	}

	public void setM_bStudyRoom(boolean m_bStudyRoom) 
	{
		this.m_bStudyRoom = m_bStudyRoom;
	}

	public boolean isM_bStudyRoom() 
	{
		return m_bStudyRoom;
	}

	public void setM_bBalcony(boolean m_bBalcony) 
	{
		this.m_bBalcony = m_bBalcony;
	}

	public boolean isM_bBalcony() 
	{
		return m_bBalcony;
	}

	public void setM_bPark(boolean m_bPark) 
	{
		this.m_bPark = m_bPark;
	}

	public boolean isM_bPark() 
	{
		return m_bPark;
	}

	public void setM_bBusStop(boolean m_bBusStop) 
	{
		this.m_bBusStop = m_bBusStop;
	}

	public boolean isM_bBusStop() 
	{
		return m_bBusStop;
	}

	public void setM_bSchool(boolean m_bSchool) 
	{
		this.m_bSchool = m_bSchool;
	}

	public boolean isM_bSchool() 
	{
		return m_bSchool;
	}

	public void setM_bRailwayStation(boolean m_bRailwayStation) 
	{
		this.m_bRailwayStation = m_bRailwayStation;
	}

	public boolean isM_bRailwayStation() 
	{
		return m_bRailwayStation;
	}

	public void setM_bShoppingMall(boolean m_bShoppingMall) 
	{
		this.m_bShoppingMall = m_bShoppingMall;
	}

	public boolean isM_bShoppingMall() 
	{
		return m_bShoppingMall;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strXml = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "PropertyDetailData");
			addChild (oXmlDocument, oRootElement, "m_nDetailId", m_nDetailId);
			addChild (oXmlDocument, oRootElement, "m_nBHK", m_nBHK);
			addChild (oXmlDocument, oRootElement, "m_strSize", m_strSize);
			addChild (oXmlDocument, oRootElement, "m_strDescription", m_strDescription);
			addChild (oXmlDocument, oRootElement, "m_nToilets", m_nToilets);
			addChild (oXmlDocument, oRootElement, "m_bStudyRoom", m_bStudyRoom ? "Yes" : "No" );
			addChild (oXmlDocument, oRootElement, "m_bBalcony", m_bBalcony ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bPark", m_bPark ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bBusStop", m_bBusStop ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bSchool", m_bSchool ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bRailwayStation", m_bRailwayStation ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bShoppingMall", m_bShoppingMall ? "Yes" : "No");
			strXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
		return strXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (!m_strDescription.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strDescription", m_strDescription, MatchMode.ANYWHERE));
		if (m_strSize != null && !m_strSize.trim().isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strSize", m_strSize.trim()));
		if (m_nBHK > 0)
			oCriteria.add (Restrictions.eq ("m_nBHK", m_nBHK));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strDescription");
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
		if (!m_strDescription.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strDescription")), m_strDescription)); 
		if (m_strSize != null && !m_strSize.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strSize"), m_strSize.trim()));
		if (m_nBHK > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nBHK"), m_nBHK));
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nDetailId"), m_nDetailId)); 
		return oConjunct;
	}
}
