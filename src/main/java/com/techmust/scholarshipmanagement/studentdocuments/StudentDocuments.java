package com.techmust.scholarshipmanagement.studentdocuments;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.techmust.generic.data.MasterData;

@Entity
@Table(name = "studentdocuments")
public class StudentDocuments  extends MasterData
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "documentid")
	private int m_nDocumentId;
	
	@Column(name = "aadhar")
	private String m_strStudentAadhar;
	
	@Column(name = "electricitybill")
	private String m_strStudentElectricityBill;	

	public StudentDocuments() 
	{
		m_nDocumentId = -1;
		m_strStudentAadhar = "";
		m_strStudentElectricityBill = "";		
	}
	
	public int getM_nDocumentId() 
	{
		return m_nDocumentId;
	}

	public void setM_nDocumentId(int nDocumentId)
	{
		this.m_nDocumentId = nDocumentId;
	}

	public String getM_strStudentAadhar()
	{
		return m_strStudentAadhar;
	}

	public void setM_strStudentAadhar(String strStudentAadhar) 
	{
		this.m_strStudentAadhar = strStudentAadhar;
	}

	public String getM_strStudentElectricityBill()
	{
		return m_strStudentElectricityBill;
	}

	public void setM_strStudentElectricityBill(String strStudentElectricityBill)
	{
		this.m_strStudentElectricityBill = strStudentElectricityBill;
	}
}
