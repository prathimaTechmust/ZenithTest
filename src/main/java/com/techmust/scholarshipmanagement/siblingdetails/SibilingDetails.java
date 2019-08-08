package com.techmust.scholarshipmanagement.siblingdetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.MasterData;

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

	public SibilingDetails()
	{
		m_nSibilingId = -1;
		m_nZenithUID = -1;
		m_strSibilingName = "";
		m_strStudying = "";
		m_strStudyingInstitution = "";		
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
