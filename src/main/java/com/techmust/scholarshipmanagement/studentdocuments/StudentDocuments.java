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
	
	@Column(name = "fatheraadhar")
	private String m_strFatherAadharImageId;
	
	@Column(name = "motheraadhar")
	private String m_strMotherAadharImageId;
	
	@Column(name = "markscard1")
	private String m_strStudentMarksCard1;
	
	@Column(name = "markscard2")
	private String m_strStudentMarksCard2;
	
	@Column(name = "other_documents")
	private String m_strOtherDocuments;	
	
	public StudentDocuments() 
	{
		m_nDocumentId = -1;
		m_strStudentAadhar = null;
		m_strStudentElectricityBill = null;
		m_strFatherAadharImageId = null;
		m_strMotherAadharImageId = null;
		m_strStudentMarksCard1 = null;
		m_strStudentMarksCard2 = null;
		m_strOtherDocuments = null;
	}
	
	public String getM_strFatherAadharImageId() 
	{
		return m_strFatherAadharImageId;
	}
	
	public void setM_strFatherAadharImageId(String strFatherAadharImageId) 
	{
		this.m_strFatherAadharImageId = strFatherAadharImageId;
	}
	
	public String getM_strMotherAadharImageId()
	{
		return m_strMotherAadharImageId;
	}
	
	public void setM_strMotherAadharImageId(String strMotherAadharImageId)
	{
		this.m_strMotherAadharImageId = strMotherAadharImageId;
	}

	public String getM_strStudentMarksCard1()
	{
		return m_strStudentMarksCard1;
	}
	
	public void setM_strStudentMarksCard1(String strStudentMarksCard1)
	{
		this.m_strStudentMarksCard1 = strStudentMarksCard1;
	}
	
	public String getM_strStudentMarksCard2()
	{
		return m_strStudentMarksCard2;
	}
	
	public void setM_strStudentMarksCard2(String strStudentMarksCard2)
	{
		this.m_strStudentMarksCard2 = strStudentMarksCard2;
	}

	public String getM_strOtherDocuments()
	{
		return m_strOtherDocuments;
	}
	
	public void setM_strOtherDocuments(String strOtherDocuments)
	{
		this.m_strOtherDocuments = strOtherDocuments;
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
