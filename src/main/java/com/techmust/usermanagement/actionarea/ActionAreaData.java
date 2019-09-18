package com.techmust.usermanagement.actionarea;


import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.userinfo.UserInformationData;

/**
 * Holds Action Area data
 * @author Techmust software pvt ltd
 *
 */
@Entity
@Table(name = "action_area_table")
public class ActionAreaData extends MasterData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ACTION_AREA_ID")
	private int m_nActionAreaId;
	@Column(name = "ACTION_AREA_NAME")
	private String m_strActionAreaName;
	@Column(name = "ACTION_AREA_SEQUENCENUMBER")
	private int m_nSequenceNumber;
	
	//Mandatory Columns
	@Column(name = "CREATED_ON")
	private Date m_dCreatedOn;
	
	@Column(name = "UPDATED_ON")
	private Date m_dUpdatedOn;	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CREATED_BY")
	private UserInformationData m_oUserCreateBy;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "UPDATED_BY")
	private UserInformationData m_oUserUpdatedBy;
	
	@Transient
	private boolean m_bIsMatchEqual;
	/**
	  * Constructor
	 */
	public ActionAreaData () 
	{
		m_nActionAreaId = -1;
		m_strActionAreaName = "";
		m_bIsMatchEqual = false;
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_dUpdatedOn = Calendar.getInstance().getTime();		
	}
	
	public UserInformationData getM_oUserCreateBy() 
	{
		return m_oUserCreateBy;
	}
	
	public void setM_oUserCreateBy(UserInformationData m_oUserCreateBy) 
	{
		this.m_oUserCreateBy = m_oUserCreateBy;
	}

	public UserInformationData getM_oUserUpdatedBy() 
	{
		return m_oUserUpdatedBy;
	}

	public void setM_oUserUpdatedBy(UserInformationData m_oUserUpdatedBy) 
	{
		this.m_oUserUpdatedBy = m_oUserUpdatedBy;
	}

	public Date getM_dCreatedOn()
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date m_dCreatedOn) 
	{
		this.m_dCreatedOn = m_dCreatedOn;
	}

	public Date getM_dUpdatedOn()
	{
		return m_dUpdatedOn;
	}

	public void setM_dUpdatedOn(Date m_dUpdatedOn) 
	{
		this.m_dUpdatedOn = m_dUpdatedOn;
	}

	/**
	  * Sets the Action Area Id
	  *@param nActionAreaId - Action area id
	 */
	public void setM_nActionAreaId (int nActionAreaId)
	{
		m_nActionAreaId = nActionAreaId;
	}

	/**
	  *Returns the Action Area id
	  *@return Returns the Action Area id
	 */
	public int getM_nActionAreaId ()
	{
		return m_nActionAreaId;
	}

	public void setM_strActionAreaName (String strActionAreaName) 
	{
		m_strActionAreaName = strActionAreaName;
	}

	public String getM_strActionAreaName () 
	{
		return m_strActionAreaName;
	}
	
	public void setM_nSequenceNumber(int nSequenceNumber)
	{
		m_nSequenceNumber = nSequenceNumber;
	}

	public int getM_nSequenceNumber()
	{
		return m_nSequenceNumber;
	}

	@Override
    public String generateXML ()
    {
		m_oLogger.info ("generateXML");
		String strXML = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement  = createRootElement (oXmlDocument, "ActionAreaData");
			addChild (oXmlDocument, oRootElement, "m_nActionAreaId", m_nActionAreaId);
			addChild (oXmlDocument, oRootElement, "m_strActionAreaName", m_strActionAreaName);
			strXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
		m_oLogger.debug ( "generateXML - strXML [OUT] : " + strXML);
	    return strXML;
    }
	
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (!m_strActionAreaName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strActionAreaName", m_strActionAreaName, m_bIsMatchEqual ? MatchMode.EXACT : MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nActionAreaId");
		return oCriteria;
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
		if (!m_strActionAreaName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strActionAreaName")), m_strActionAreaName.toLowerCase())); 
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strActionAreaName.isEmpty ())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strActionAreaName")), m_strActionAreaName.toLowerCase())); 
		}
		else
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nActionAreaId"), m_nActionAreaId)); 
		}
		return oConjunct;
	}
}