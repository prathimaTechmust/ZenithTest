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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techmust.constants.Constants;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.academicyear.AcademicYear;
import com.techmust.scholarshipmanagement.chequeFavourOf.ChequeInFavourOf;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.usermanagement.userinfo.UserInformationData;
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
	
	@Column(name = "counseling_date")
	private Date m_dCounselingDate;
	
	@Column(name = "chequeRemark")
	private String m_strChequeRemark;	
	
	@Column(name = "appl_submit_date")
	private Date m_dApplicationSubmitDate;
	
	@Column(name="paymentType")
	private String m_strPaymentType;
	
	@Column(name="verifyRemarks")
	private String m_strVerifyRemarks;
	
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
	
	//Mappings
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
	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="approvedBy")
	private UserInformationData m_oApprovedBy;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "chequeDisburseBy")
	private UserInformationData m_oChequeDisburseBy;
	
	//Transient Variables
	@Transient
	private int m_nStudentId;
	
	@Transient
	private int m_nAcademicId;
	
	@Transient
	private int m_nAcademicYearId;
	
	@Transient
	private String m_strStudentName;
	
	@Transient
	private long m_nStudentUID;
	
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
		m_dCounselingDate = null;
		m_strPaymentType = null;
		m_strStudentRemarks = "";
		m_strVerifyRemarks = "";
		m_dApplicationSubmitDate = Calendar.getInstance().getTime();
		m_oAcademicYear = new AcademicYear();
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_dUpdatedOn = Calendar.getInstance().getTime();
		m_oUserCreatedBy = new UserInformationData();
		m_oUserUpdatedBy = new UserInformationData();
	}
	
	public String getM_strStudentName()
	{
		return m_strStudentName;
	}

	public void setM_strStudentName(String m_strStudentName) 
	{
		this.m_strStudentName = m_strStudentName;
	}
	
	public long getM_nStudentUID() 
	{
		return m_nStudentUID;
	}

	public void setM_nStudentUID(long m_nStudentUID) 
	{
		this.m_nStudentUID = m_nStudentUID;
	}

	public int getM_nAcademicYearId()
	{
		return m_nAcademicYearId;
	}

	public void setM_nAcademicYearId(int m_nAcademicYearId)
	{
		this.m_nAcademicYearId = m_nAcademicYearId;
	}

	public UserInformationData getM_oApprovedBy()
	{
		return m_oApprovedBy;
	}

	public void setM_oApprovedBy(UserInformationData m_oApprovedBy) 
	{
		this.m_oApprovedBy = m_oApprovedBy;
	}

	public UserInformationData getM_oChequeDisburseBy() 
	{
		return m_oChequeDisburseBy;
	}

	public void setM_oChequeDisburseBy(UserInformationData m_oChequeDisburseBy)
	{
		this.m_oChequeDisburseBy = m_oChequeDisburseBy;
	}

	public String getM_strVerifyRemarks()
	{
		return m_strVerifyRemarks;
	}


	public void setM_strVerifyRemarks(String m_strVerifyRemarks) 
	{
		this.m_strVerifyRemarks = m_strVerifyRemarks;
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

	public Date getM_dCounselingDate() 
	{
		return m_dCounselingDate;
	}

	public void setM_dCounselingDate(Date m_dCounselingDate) 
	{
		this.m_dCounselingDate = m_dCounselingDate;
	}

	public String getM_strPaymentType() 
	{
		return m_strPaymentType;
	}

	public void setM_strPaymentType(String m_strPaymentType)
	{
		this.m_strPaymentType = m_strPaymentType;
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
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root) 
	{					
		return null;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> ocCriteriaQuery,CriteriaBuilder oCriteriaBuilder)
	{		
		return null;
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
			if(m_oApprovedBy != null)
			{
				Document oApproveUserInformationDataXmlDoc = getXmlDocument ("<m_oApprovedBy>"+buildUserDetails (m_oApprovedBy)+"</m_oApprovedBy>");
				Node oApproveBuildUserDetailsDataNode = oXmlDocument.importNode(oApproveUserInformationDataXmlDoc.getFirstChild(), true);
				oRootElement.appendChild(oApproveBuildUserDetailsDataNode);
			}
			if(m_oChequeDisburseBy != null)
			{
				Document oDisburseUserInformationDataXmlDoc = getXmlDocument ("<m_oChequeDisburseBy>"+buildUserDetails (m_oChequeDisburseBy)+"</m_oChequeDisburseBy>");
				Node oDisburseBuildUserDetailsDataNode = oXmlDocument.importNode(oDisburseUserInformationDataXmlDoc.getFirstChild(), true);
				oRootElement.appendChild(oDisburseBuildUserDetailsDataNode);
			}		
			addChild (oXmlDocument, oRootElement, "m_nZenithScholarshipId",m_nZenithScholarshipId);
			addChild (oXmlDocument, oRootElement, "m_strStatus",m_strStatus);
			addChild (oXmlDocument, oRootElement, "m_fApprovedAmount",m_fApprovedAmount);
			addChild (oXmlDocument, oRootElement, "m_strReceiverName",m_strReceiverName);
			addChild (oXmlDocument, oRootElement, "m_strReceiverContactNumber",m_strReceiverContactNumber);	
			addChild (oXmlDocument, oRootElement, "m_dChequeIssueDate",m_dChequeIssueDate != 0 ? getChequeIssueDate(m_dChequeIssueDate) : "");
			addChild (oXmlDocument, oRootElement, "m_dApprovedDate",m_dApprovedDate != null ? getDate(m_dApprovedDate.toString()) :"");
			addChild (oXmlDocument, oRootElement, "m_strImage",m_strImage);
			addChild (oXmlDocument, oRootElement, "m_strScanCopyImageURL",getScanCopyImageURL(m_strImage));	
			addChild(oXmlDocument, oRootElement, "m_strChequeRemark", m_strChequeRemark);
			addChild(oXmlDocument, oRootElement, "m_strPaymentType", m_strPaymentType);
			addChild(oXmlDocument, oRootElement, "m_strStudentRemarks", m_strStudentRemarks);
			addChild(oXmlDocument, oRootElement, "m_strVerifyRemarks", m_strVerifyRemarks);
			addChild (oXmlDocument, oRootElement, "m_dClaimedDate",m_dClaimedDate != null ? getDate(m_dClaimedDate.toString()) :"");
			addChild (oXmlDocument, oRootElement, "m_dCounselingDate",m_dCounselingDate != null ? getDate(m_dCounselingDate.toString()) :"");
			addChild (oXmlDocument, oRootElement, "m_dApplicationSubmitDate",m_dApplicationSubmitDate != null ? getDate(m_dApplicationSubmitDate.toString()) :"");
			addChild (oXmlDocument, oRootElement, "m_nAcademicYearId",m_nAcademicYearId);
			addChild (oXmlDocument, oRootElement, "m_strStudentName",m_strStudentName);
			addChild (oXmlDocument, oRootElement, "m_nStudentUID",m_nStudentUID);
			strZenithScholarshipDetailsInfoXML = getXmlString (oXmlDocument);			 
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strZenithScholarshipDetailsInfoXML;
	}
	
	private String buildUserDetails(UserInformationData oUserDetails) 
	{
		return oUserDetails.generateXML();
	}
	
	private String getDate(String strDate)
	{
		return Utils.formatDate(strDate);
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
