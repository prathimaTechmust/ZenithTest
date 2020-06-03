package com.techmust.scholarshipmanagement.activitylog;

import java.io.StringReader;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.utils.Utils;

@Entity
@Table(name = "activitylog")
public class ActivityLog  extends MasterData
{	
	private static final long serialVersionUID = 1L;	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activityid")
	private int m_nActivityId;
	
	@Column(name = "username")
	private String m_strLoginUserName;
	
	@Column(name = "taskPerformed")
	private String m_strTaskPerformed;
	
	@Column(name = "xmlparameter",columnDefinition = "TEXT")
	private String m_strXMLString;
	
	//Mandatory Columns	
	@Column(name = "created_on")
	private Date m_dCreatedOn;
	
	@Column(name = "updated_on")
	private Date m_dUpdatedOn;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by")
	private UserInformationData m_oUserCreatedBy;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "updated_by")
	private UserInformationData m_oUserUpdatedBy;
	
	//Transient Variables
	@Transient
	private Date m_dFromDate;
	
	@Transient
	private Date m_dToDate;

	public ActivityLog() 
	{
		m_nActivityId = -1;
		m_strLoginUserName = "";
		m_strTaskPerformed = "";		
		m_strXMLString = "";
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_dUpdatedOn = Calendar.getInstance().getTime();		
	}	
	
	public Date getM_dFromDate()
	{
		return m_dFromDate;
	}

	public void setM_dFromDate(Date dFromDate)
	{
		this.m_dFromDate = dFromDate;
	}

	public Date getM_dToDate()
	{
		return m_dToDate;
	}

	public void setM_dToDate(Date dToDate)
	{
		this.m_dToDate = dToDate;
	}

	public int getM_nActivityId()
	{
		return m_nActivityId;
	}

	public void setM_nActivityId(int nActivityId)
	{
		this.m_nActivityId = nActivityId;
	}

	public String getM_strLoginUserName()
	{
		return m_strLoginUserName;
	}

	public void setM_strLoginUserName(String strLoginUserName)
	{
		this.m_strLoginUserName = strLoginUserName;
	}

	public String getM_strTaskPerformed()
	{
		return m_strTaskPerformed;
	}

	public void setM_strTaskPerformed(String strTaskPerformed)
	{
		this.m_strTaskPerformed = strTaskPerformed;
	}	

	public String getM_strXMLString()
	{
		return m_strXMLString;
	}

	public void setM_strXMLString(String strXMLString)
	{
		this.m_strXMLString = strXMLString;
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

	public UserInformationData getM_oUserCreatedBy() 
	{
		return m_oUserCreatedBy;
	}

	public void setM_oUserCreatedBy(UserInformationData m_oUserCreatedBy) 
	{
		this.m_oUserCreatedBy = m_oUserCreatedBy;
	}

	public UserInformationData getM_oUserUpdatedBy() 
	{
		return m_oUserUpdatedBy;
	}

	public void setM_oUserUpdatedBy(UserInformationData m_oUserUpdatedBy)
	{
		this.m_oUserUpdatedBy = m_oUserUpdatedBy;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteriaQuery,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(getM_nActivityId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nActivityId"), m_nActivityId));
		if(!m_strLoginUserName.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strLoginUserName"), m_strLoginUserName));
		if(!m_strTaskPerformed.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oRootObject.<String>get("m_strTaskPerformed"), "%"+m_strTaskPerformed+"%"));
		if(m_dFromDate != null && m_dToDate != null)		
			oConjunct = oCriteriaBuilder.and(oConjunct,oCriteriaBuilder.between(oRootObject.<Date>get("m_dCreatedOn"),m_dFromDate,m_dToDate));					
		return oConjunct;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{	
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(getM_nActivityId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nActivityId"), m_nActivityId));
		if(!m_strLoginUserName.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strLoginUserName"), m_strLoginUserName));
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strActivityInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ActivityLog");			
			Document oXMLParameter = convertXmlStringToXmlDocument(m_strXMLString);
			Node oXmlParameterNode = oXmlDocument.importNode(oXMLParameter.getFirstChild(), true);			
			oRootElement.appendChild(oXmlParameterNode);
			addChild (oXmlDocument, oRootElement, "m_nActivityId", m_nActivityId);
			addChild (oXmlDocument, oRootElement, "m_strLoginUserName", m_strLoginUserName);
			addChild (oXmlDocument, oRootElement, "m_strTaskPerformed", m_strTaskPerformed);
			addChild (oXmlDocument, oRootElement, "m_dCreatedOn", m_dCreatedOn != null ? getCreatedDate(m_dCreatedOn):"");
			strActivityInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strActivityInfoXML;		
	}

	@SuppressWarnings("deprecation")
	private String getCreatedDate(Date dCreatedOn)
	{	
		return Utils.formatDate(dCreatedOn.toString());
	}

	private Document convertXmlStringToXmlDocument(String strXMLString)
	{		
		Document oDocument = null;
		try
		{
			DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder();
			oDocument = oDocumentBuilder.parse(new InputSource(new StringReader(strXMLString)));
		} 
		catch (Exception oException)
		{
			m_oLogger.error("XML String Parses To Xml" + oException);
		}		
		return oDocument;
	}
}
