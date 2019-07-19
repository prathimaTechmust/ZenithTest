package com.techmust.scholarshipmanagement.siblingdetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.techmust.generic.data.MasterData;

@Entity
@Table(name = "siblingdetails")
public class SiblingDetails extends MasterData
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "siblingId")
	private int m_nSiblingId;
	
	@Column(name = "zenithUID")
	private int m_nZenithUID;
	
	@Column(name = "siblingname")
	private String m_strSiblingName;
	
	@Column(name = "studying")
	private String m_strStudying;
	
	@Column(name = "studyinginstitution")
	private String m_strStudyingInstitution;

	public SiblingDetails()
	{
		m_nSiblingId = -1;
		m_nZenithUID = -1;
		m_strSiblingName = "";
		m_strStudying = "";
		m_strStudyingInstitution = "";		
	}

	public int getM_nSiblingId()
	{
		return m_nSiblingId;
	}

	public void setM_nSiblingId(int nSiblingId)
	{
		this.m_nSiblingId = nSiblingId;
	}

	public int getM_nZenithUID() 
	{
		return m_nZenithUID;
	}

	public void setM_nZenithUID(int nZenithUID)
	{
		this.m_nZenithUID = nZenithUID;
	}

	public String getM_strSiblingName()
	{
		return m_strSiblingName;
	}

	public void setM_strSiblingName(String strSiblingName)
	{
		this.m_strSiblingName = strSiblingName;
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
}
