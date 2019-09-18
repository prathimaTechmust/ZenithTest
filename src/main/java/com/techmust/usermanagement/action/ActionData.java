package com.techmust.usermanagement.action;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.actionarea.ActionAreaData;
import com.techmust.usermanagement.userinfo.UserInformationData;
/**
 * Holds Action data
 * @author Techmust software pvt ltd
 *
 */
@Entity
@Table(name = "action_table")
public class ActionData extends MasterData
{
	private static final long serialVersionUID = 1L;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ACTION_AREA_ID")	
	private ActionAreaData m_oActionArea;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ACTION_ID")
	private int m_nActionId;
	@Column(name = "ACTION_NAME")
	@Size(max = 128)
	private String m_strActionName;
	@Column(name = "ACTION_TARGET")
	@Size(max = 128)
	private String m_strActionTarget;
	@Column(name = "ACTION_SEQUENCENUMBER")
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
	
	/**
	  * Constructor
	 */
	public ActionData ()
	{
		m_nActionId = -1;
		m_strActionName = "";
		m_strActionTarget = "";
		m_nSequenceNumber = -1;
		m_oActionArea = new ActionAreaData ();
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
	  * Sets the ActionAreaData
	  *@param oActionArea - Instance of ActionAreaData
	 */
	public void setM_oActionArea (ActionAreaData oActionArea) 
	{
		m_oActionArea = oActionArea;
	}

	/**
	  *Returns the ActionAreaData
	  *@return Returns the ActionAreaData
	 */
	public ActionAreaData getM_oActionArea () 
	{
		return m_oActionArea;
	}

	/**
	  *Sets the action Id
	  *@param nActionId - Action ID
	 */
	public void setM_nActionId (int nActionId) 
	{
		m_nActionId = nActionId;
	}
	
	/**
	 * Returns the action id 
	  *@return returns the action id 
	 */
	public int getM_nActionId () 
	{
		return m_nActionId;
	}
	
	/**
	   Sets the name of the action
	  @param strActionName - Action name 
	 */
	public void setM_strActionName (String strActionName) 
	{
		m_strActionName = strActionName;
	}
	
	/**
	  *@return Returns the name of the Action
	 */
	public String getM_strActionName () 
	{
		return m_strActionName;
	}
	
	/**
	  *Sets the target of the Action
	  *@param strActionTarget - the target of the Action 
	 */
	public void setM_strActionTarget (String strActionTarget) 
	{
		m_strActionTarget = strActionTarget;
	}
	
	/**
	  *Returns the target of the Action
	  *@return Returns the target of the Action
	 */
	public String getM_strActionTarget () 
	{
		return m_strActionTarget;
	}
	
	public void setM_nSequenceNumber(int nSequenceNumber) 
	{
		this.m_nSequenceNumber = nSequenceNumber;
	}

	public int getM_nSequenceNumber()
	{
		return m_nSequenceNumber;
	}
	
	/**
	  * Used to add the hibernate Criteria
	  *@param oCriteria -  Criteria object
	 */


	/**
	  *Generates xml representation of ActionData 
	 */
	@Override
    public String generateXML ()
    {
		String strXML = "";
		m_oLogger.info ("generateXML");
	    try 
	    {
			Document oXmlDocument = createNewXMLDocument ();
			Document oActionAreaXmlDoc = getXmlDocument ("<m_oActionArea>" + m_oActionArea.generateXML () + "</m_oActionArea>");
			Element oRootElement = createRootElement (oXmlDocument, "ActionData");
			Node oActionAreaNode = oXmlDocument.importNode (oActionAreaXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oActionAreaNode);
			addChild (oXmlDocument, oRootElement, "m_nActionId", m_nActionId);
			addChild (oXmlDocument, oRootElement, "m_strActionName", m_strActionName);
			addChild (oXmlDocument, oRootElement, "m_strActionTarget", m_strActionTarget);
			addChild (oXmlDocument, oRootElement, "m_nSequenceNumber", m_nSequenceNumber);
			strXML = getXmlString (oXmlDocument);
		} 
	    catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateXML - strXML [OUT] : " + strXML);
	    return strXML;
    }
	
	/**
	  *Sets the Criteria for listing Actions
	  *@param oCriteria - Criteria object
	  *@param strColumn - String the column name
	  *@param strOrderBy - String the order by key ( asc for accending , dsc for descending)
	 */
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_oActionArea.getM_nActionAreaId () > 0)
			oCriteria.add (Restrictions.eq ("m_oActionArea", m_oActionArea));
		if (!m_strActionName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strActionName", m_strActionName, MatchMode.ANYWHERE));
		if (!m_strActionTarget.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strActionTarget", m_strActionTarget, MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nActionId");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData oCredentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_oActionArea.getM_nActionAreaId () > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oActionArea"), m_oActionArea)); 
		if (!m_strActionName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strActionName")), m_strActionName.toLowerCase())); 
		if (!m_strActionTarget.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strActionTarget")), m_strActionTarget.toLowerCase())); 
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nActionId"), m_nActionId)); 
		return oConjunct;
	}
}