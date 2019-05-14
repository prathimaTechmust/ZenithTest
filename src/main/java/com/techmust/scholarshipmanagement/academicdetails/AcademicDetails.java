package com.techmust.scholarshipmanagement.academicdetails;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "academicdetails")
public class AcademicDetails extends MasterData
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "academicid")
	private int m_nAcademicId;
	
	@Column(name = "institutionname")
	private String m_strInstitutionName;
	
	@Column(name = "coursename")
	private String m_strCourseName;
	
	@Column(name = "annualfee")
	private float m_fAnnualFee;
	
	@Column(name = "paidfee")
	private float m_fPaidFee;
	
	@Column(name = "balancefee")
	private float m_fBalanceFee;
	
	@Column(name = "date")
	private Calendar m_dDate;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "studentid")
	private StudentInformationData m_oStudentInformationData;

	public AcademicDetails()
	{
		m_nAcademicId = -1;
		m_strInstitutionName = "";
		m_strCourseName = "";		
		m_fAnnualFee = 0;
		m_fPaidFee = 0;
		m_fBalanceFee = 0;
		m_dDate = Calendar.getInstance();
		m_oStudentInformationData = new StudentInformationData ();
	}	

	public StudentInformationData getM_oStudentInformationData()
	{
		return m_oStudentInformationData;
	}

	public void setM_oStudentInformationData(StudentInformationData m_oStudentInformationData) 
	{
		this.m_oStudentInformationData = m_oStudentInformationData;
	}		

	public int getM_nAcademicId()
	{
		return m_nAcademicId;
	}

	public void setM_nAcademicId(int m_nAcademicId) 
	{
		this.m_nAcademicId = m_nAcademicId;
	}

	public String getM_strInstitutionName()
	{
		return m_strInstitutionName;
	}

	public void setM_strInstitutionName(String m_strInstitutionName)
	{
		this.m_strInstitutionName = m_strInstitutionName;
	}

	public String getM_strCourseName() 
	{
		return m_strCourseName;
	}

	public void setM_strCourseName(String m_strCourseName) 
	{
		this.m_strCourseName = m_strCourseName;
	}	

	public float getM_fAnnualFee() 
	{
		return m_fAnnualFee;
	}

	public void setM_fAnnualFee(float m_fAnnualFee) 
	{
		this.m_fAnnualFee = m_fAnnualFee;
	}

	public float getM_fPaidFee() 
	{
		return m_fPaidFee;
	}

	public void setM_fPaidFee(float m_fPaidFee) 
	{
		this.m_fPaidFee = m_fPaidFee;
	}

	public float getM_fBalanceFee()
	{
		return m_fBalanceFee;
	}

	public void setM_fBalanceFee(float m_fBalanceFee)
	{
		this.m_fBalanceFee = m_fBalanceFee;
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
