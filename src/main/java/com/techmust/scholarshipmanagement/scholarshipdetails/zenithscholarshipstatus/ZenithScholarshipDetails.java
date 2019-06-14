package com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.student.StudentInformationData;

@Entity
@Table(name = "zenithscholarshipdetails")
public class ZenithScholarshipDetails extends MasterData
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zenithscholarshipid")
	private int m_nZenithScholarshipId;
	
	@Column(name = "status")
	private String m_strStatus;	
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "studentid")
	private StudentInformationData m_oStudentInformationData;
	
	@Transient
	private int m_nStudentId;
	
	public ZenithScholarshipDetails()
	{
		m_nZenithScholarshipId = -1;
		m_strStatus = "";
		m_oStudentInformationData = new StudentInformationData();
	}	
	
	public int getM_nStudentId()
	{
		return m_nStudentId;
	}

	public void setM_nStudentId(int m_nStudentId)
	{
		this.m_nStudentId = m_nStudentId;
	}

	public int getM_nZenithScholarshipId()
	{
		return m_nZenithScholarshipId;
	}

	public void setM_nZenithScholarshipId(int m_nZenithScholarshipId) 
	{
		this.m_nZenithScholarshipId = m_nZenithScholarshipId;
	}

	public String getM_strStatus()
	{
		return m_strStatus;
	}

	public void setM_strStatus(String m_strStatus) 
	{
		this.m_strStatus = m_strStatus;
	}	

	public StudentInformationData getM_oStudentInformationData() 
	{
		return m_oStudentInformationData;
	}

	public void setM_oStudentInformationData(StudentInformationData m_oStudentInformationData) 
	{
		this.m_oStudentInformationData = m_oStudentInformationData;
	}
}
