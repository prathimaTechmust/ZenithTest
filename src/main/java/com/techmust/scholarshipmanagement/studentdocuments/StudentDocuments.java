package com.techmust.scholarshipmanagement.studentdocuments;

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

import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.userinfo.UserInformationData;

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
	private String m_strVerifyScanDocument;	
	
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
	
	public String getM_strVerifyScanDocument()
	{
		return m_strVerifyScanDocument;
	}

	public void setM_strVerifyScanDocument(String m_strVerifyScanDocument) 
	{
		this.m_strVerifyScanDocument = m_strVerifyScanDocument;
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
