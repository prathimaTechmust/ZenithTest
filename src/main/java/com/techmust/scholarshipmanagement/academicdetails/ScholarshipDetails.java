package com.techmust.scholarshipmanagement.academicdetails;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.student.StudentInformationData;

@Entity
@Table(name = "scholarshipdetails")
public class ScholarshipDetails extends MasterData
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scholarshipid")
	private int m_nScholarshipId;
	
	@Column(name = "organizationname")
	private String m_strOrganizationName;
	
	@Column(name = "amount")
	private float m_fAmount;	
	
	@Column(name = "date")
	private Calendar m_dDate;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "studentid")
	private StudentInformationData m_oStudentInformationData;
	

	public ScholarshipDetails()
	{
		m_nScholarshipId = -1;
		m_strOrganizationName = "";
		m_fAmount = 0;
		m_dDate = Calendar.getInstance();		
		m_oStudentInformationData = new StudentInformationData();
	}	
	
	public StudentInformationData getM_oStudentInformationData()
	{
		return m_oStudentInformationData;
	}

	public void setM_oStudentInformationData(StudentInformationData m_oStudentInformationData)
	{
		this.m_oStudentInformationData = m_oStudentInformationData;
	}

	public int getM_nScholarshipId()
	{
		return m_nScholarshipId;
	}

	public void setM_nScholarshipId(int m_nScholarshipId)
	{
		this.m_nScholarshipId = m_nScholarshipId;
	}

	public String getM_strOrganizationName()
	{
		return m_strOrganizationName;
	}

	public void setM_strOrganizationName(String m_strOrganizationName) 
	{
		this.m_strOrganizationName = m_strOrganizationName;
	}

	public float getM_fAmount() 
	{
		return m_fAmount;
	}

	public void setM_fAmount(float m_fAmount)
	{
		this.m_fAmount = m_fAmount;
	}

	public Calendar getM_dDate()
	{
		return m_dDate;
	}

	public void setM_dDate(Calendar m_dDate) 
	{
		this.m_dDate = m_dDate;
	}
	
}
