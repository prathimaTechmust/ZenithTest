package com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techmust.constants.Constants;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.academicyear.AcademicYear;
import com.techmust.scholarshipmanagement.chequeFavourOf.ChequeInFavourOf;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.utils.Utils;

@Entity
@Table(name = "zenithscholarshipdetails")
public class ZenithScholarshipDetails extends MasterData implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zenithscholarshipid")
	private int m_nZenithScholarshipId;
	
	@Column(name = "status")
	private String m_strStatus;	
	
	@Column(name = "receivername")
	private String m_strReceiverName;
	
	@Column(name = "receivercontactnumber")
	private String m_strReceiverContactNumber;
	
	@Column(name = "issuedate")
	private long m_dChequeIssueDate;
	
	@Column(name = "studentRemarks")
	private String m_strStudentRemarks;	
	
	@Column(name = "approvedamount")
	private float m_fApprovedAmount;
	
	@Column(name="ScanDocument")
	private String m_strImage;
	
	@Column(name = "approved_date")
	private Date m_dApprovedDate;
	
	@Column(name = "claimed_date")
	private Date m_dClaimedDate;
	
	@Column(name = "chequeRemark")
	private String m_strChequeRemark;	
	
	@Column(name = "appl_submit_date")
	private Date m_dApplicationSubmitDate;
	
	@Transient
	private int m_nStudentId;
	
	@Transient
	private int m_nAcademicId;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "studentid")
	private StudentInformationData m_oStudentInformationData;	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cheque_InFavour_Id")
	private ChequeInFavourOf m_oChequeInFavourOf;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "academicyearid")
	private AcademicYear m_oAcademicYear;
	
	public ZenithScholarshipDetails()
	{
		m_nZenithScholarshipId = -1;
		m_strStatus = "";
		m_oStudentInformationData = new StudentInformationData();
		m_fApprovedAmount = 0;
		m_strReceiverName = "";
		m_strReceiverContactNumber = "";
		m_dChequeIssueDate = 0;
		m_strImage = null;
		m_strChequeRemark= "";
		m_dApprovedDate = null;
		m_dClaimedDate = null;		
		m_dApplicationSubmitDate = Calendar.getInstance().getTime();
		m_oAcademicYear = new AcademicYear();
	}	
	
	public AcademicYear getM_oAcademicYear()
	{
		return m_oAcademicYear;
	}

	public void setM_oAcademicYear(AcademicYear m_oAcademicYear)
	{
		this.m_oAcademicYear = m_oAcademicYear;
	}

	public Date getM_dApplicationSubmitDate()
	{
		return m_dApplicationSubmitDate;
	}

	public void setM_dApplicationSubmitDate(Date m_dApplicationSubmitDate)
	{
		this.m_dApplicationSubmitDate = m_dApplicationSubmitDate;
	}

	public ChequeInFavourOf getM_oChequeInFavourOf()
	{
		return m_oChequeInFavourOf;
	}
	
	public void setM_oChequeInFavourOf(ChequeInFavourOf m_oChequeInFavourOf)
	{
		this.m_oChequeInFavourOf = m_oChequeInFavourOf;
	}
	
	public int getM_nAcademicId()
	{
		return m_nAcademicId;
	}

	public void setM_nAcademicId(int m_nAcademicId)
	{
		this.m_nAcademicId = m_nAcademicId;
	}

	public Date getM_dClaimedDate()
	{
		return m_dClaimedDate;
	}

	public void setM_dClaimedDate(Date dClaimedDate)
	{
		this.m_dClaimedDate = dClaimedDate;
	}

	public Date getM_dApprovedDate()
	{
		return m_dApprovedDate;
	}

	public void setM_dApprovedDate(Date dApprovedDate)
	{
		this.m_dApprovedDate = dApprovedDate;
	}

	public String getM_strStudentRemarks() 
	{
		return m_strStudentRemarks;
	}

	public void setM_strStudentRemarks(String strStudentRemarks)
	{
		this.m_strStudentRemarks = strStudentRemarks;
	}
	
	
	public long getM_dChequeIssueDate()

	{
		return m_dChequeIssueDate;
	}

	public void setM_dChequeIssueDate(long dChequeIssueDate)
	{
		this.m_dChequeIssueDate = dChequeIssueDate;
	}

	public String getM_strReceiverName()
	{
		return m_strReceiverName;
	}

	public String getM_strImage()
	{
		return m_strImage;
	}

	public void setM_strImage(String m_strImage)
	{
		this.m_strImage = m_strImage;
	}

	public void setM_strReceiverName(String strReceiverName)
	{
		this.m_strReceiverName = strReceiverName;
	}


	public String getM_strReceiverContactNumber()
	{
		return m_strReceiverContactNumber;
	}

	public void setM_strReceiverContactNumber(String strReceiverContactNumber)
	{
		this.m_strReceiverContactNumber = strReceiverContactNumber;
	}

	public float getM_fApprovedAmount() 
	{
		return m_fApprovedAmount;
	}

	public void setM_fApprovedAmount(float fApprovedAmount) 
	{
		this.m_fApprovedAmount = fApprovedAmount;
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

	public String getM_strChequeRemark() 
	{
		return m_strChequeRemark;
	}

	public void setM_strChequeRemark(String m_strChequeRemark)
	{
		this.m_strChequeRemark = m_strChequeRemark;
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
	
	@Override
	public String generateXML()
	{
		
		m_oLogger.info ("generateXML");
		String strZenithScholarshipDetailsInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ZenithScholarshipDetails");			
			addChild (oXmlDocument, oRootElement, "m_nZenithScholarshipId",m_nZenithScholarshipId);
			addChild (oXmlDocument, oRootElement, "m_strStatus",m_strStatus);
			addChild (oXmlDocument, oRootElement, "m_fApprovedAmount",m_fApprovedAmount);
			addChild (oXmlDocument, oRootElement, "m_strReceiverName",m_strReceiverName);
			addChild (oXmlDocument, oRootElement, "m_strReceiverContactNumber",m_strReceiverContactNumber);	
			addChild (oXmlDocument, oRootElement, "m_dChequeIssueDate",m_dChequeIssueDate != 0 ? getChequeIssueDate(m_dChequeIssueDate) : "");
			addChild (oXmlDocument, oRootElement, "m_dApprovedDate",m_dApprovedDate != null ? getApproveDate(m_dApprovedDate.toString()) :"");
			addChild (oXmlDocument, oRootElement, "m_strImage",m_strImage);
			addChild (oXmlDocument, oRootElement, "m_strScanCopyImageURL",getScanCopyImageURL(m_strImage));	
			addChild(oXmlDocument, oRootElement, "m_strChequeRemark", m_strChequeRemark);
			addChild (oXmlDocument, oRootElement, "m_dClaimedDate",m_dClaimedDate != null ? getDate(m_dClaimedDate.toString()) :"");
			addChild (oXmlDocument, oRootElement, "m_dApplicationSubmitDate",m_dApplicationSubmitDate != null ? getDate(m_dApplicationSubmitDate.toString()) :"");
			
		
			strZenithScholarshipDetailsInfoXML = getXmlString (oXmlDocument);			 
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strZenithScholarshipDetailsInfoXML;
	}

	private String getDate(String string)
	{
		return string.substring(0, 10);
	}

	private String getApproveDate(String string)
	{		
		return string.substring(0, 10);
	}	

	private String getScanCopyImageURL(String strImage)
	{
		String strImageURL = Constants.S3BUCKETURL + Constants.VERIFIEDAPPLICATION + strImage + Constants.IMAGE_DEFAULT_EXTENSION;
		return strImageURL;
	}

	private String getChequeIssueDate(long dChequeIssueDate)
	{		
		return Utils.convertTimeStampToDate(dChequeIssueDate);
	}
}
