package com.techmust.scholarshipmanagement.siblingdetails;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "sibilingdetails")
public class SibilingDetails extends MasterData
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sibilingId")
	private int m_nSibilingId;
	
	@Column(name = "zenithUID")
	private int m_nZenithUID;
	
	@Column(name = "siblingname")
	private String m_strSibilingName;
	
	@Column(name = "studying")
	private String m_strStudying;
	
	@Column(name = "studyinginstitution")
	private String m_strStudyingInstitution;
	
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
	
	public SibilingDetails()
	{
		m_nSibilingId = -1;
		m_nZenithUID = -1;
		m_strSibilingName = "";
		m_strStudying = "";
		m_strStudyingInstitution = "";	
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_dUpdatedOn = Calendar.getInstance().getTime();
		m_oUserCreatedBy = new UserInformationData();
		m_oUserUpdatedBy = new UserInformationData();
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

	public int getM_nSibilingId()
	{
		return m_nSibilingId;
	}

	public void setM_nSibilingId(int nSibilingId)
	{
		this.m_nSibilingId = nSibilingId;
	}
	
	public int getM_nZenithUID() 
	{
		return m_nZenithUID;
	}

	public void setM_nZenithUID(int nZenithUID)
	{
		this.m_nZenithUID = nZenithUID;
	}	

	public String getM_strSibilingName()
	{
		return m_strSibilingName;
	}

	public void setM_strSibilingName(String strSibilingName)
	{
		this.m_strSibilingName = strSibilingName;
	}

	public String getM_strStudying()
	{
		return m_strStudying;
	}

	public void setM_strStudying(String strStudying)
	{
		this.m_strStudying = strStudying;
	}

	public String getM_strStudyingInstitution() 
	{
		return m_strStudyingInstitution;
	}

	public void setM_strStudyingInstitution(String strStudyingInstitution)
	{
		this.m_strStudyingInstitution = strStudyingInstitution;
	}	
	
	@Override
	public String generateXML() 
	{
		String strSibilingsDetails = "";
		m_oLogger.info ("generateXML");
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "SiblingsDetails");
				
			addChild (oXmlDocument, oRootElement, "m_nSiblingId", m_nSibilingId);			
			addChild (oXmlDocument, oRootElement, "m_nZenithUID", m_nZenithUID);
			addChild (oXmlDocument, oRootElement, "m_strSiblingName", m_strSibilingName);
			addChild (oXmlDocument, oRootElement, "m_strStudying", m_strStudying);
			addChild (oXmlDocument, oRootElement, "m_strStudyingInstitution", m_strStudyingInstitution);
			strSibilingsDetails = getXmlString (oXmlDocument);
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}		
		return strSibilingsDetails;		
	}	
}
