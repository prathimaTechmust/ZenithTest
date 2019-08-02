package com.techmust.scholarshipmanagement.sholarshipaccounts;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;

@Entity
@Table(name = "scholarshipaccount")
public class StudentScholarshipAccount extends MasterData
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accountid")
	private int m_nAccountId;
	
	@Column(name = "scholarshiptype")
	private String m_strApplicationType;
	
	@Column(name = "paymenttype")
	private String m_strPaymentType;
	
	@Column(name = "payeename")
	private String m_strPayeeName;
	
	@Column(name = "sanctionamount")
	private float m_fSanctionedAmount;
	
	@Column(name = "sanctiondate")
	private Date m_dSanctionDate;
	
	@Column(name = "chequenumber")
	private int m_nChequeNumber;
	
	@Column(name = "ddnumber")
	private int m_nDDNumber;
	
	@Column(name = "remarks")
	private String m_strRemarks;
	
	@Column(name = "chequestatus")
	private String m_strChequeStatus;
	
	@Transient
	private int m_nStudentId;
	
	@Transient
	private String m_strAcademicYear;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "academicId")
	private AcademicDetails m_oAcademicDetails;
	
	public StudentScholarshipAccount()
	{
		m_nAccountId = -1;
		m_strApplicationType = "";
		m_strPaymentType = "";
		m_strPayeeName = "";
		m_fSanctionedAmount = 0;
		m_dSanctionDate = null;	
		m_oAcademicDetails = new AcademicDetails();
		m_nChequeNumber = 0;
		m_nDDNumber = 0;
		m_strRemarks = "";
		m_strChequeStatus = "";
	}
	
	public String getM_strRemarks()
	{
		return m_strRemarks;
	}

	public void setM_strRemarks(String strRemarks)
	{
		this.m_strRemarks = strRemarks;
	}

	public String getM_strChequeStatus()
	{
		return m_strChequeStatus;
	}

	public void setM_strChequeStatus(String strChequeStatus)
	{
		this.m_strChequeStatus = strChequeStatus;
	}

	public int getM_nChequeNumber()
	{
		return m_nChequeNumber;
	}
	
	public void setM_nChequeNumber(int nChequeNumber)
	{
		this.m_nChequeNumber = nChequeNumber;
	}
	
	public int getM_nDDNumber()
	{
		return m_nDDNumber;
	}

	public void setM_nDDNumber(int nDDNumber)
	{
		this.m_nDDNumber = nDDNumber;
	}
	
	public int getM_nStudentId() 
	{
		return m_nStudentId;
	}

	public void setM_nStudentId(int nStudentId)
	{
		this.m_nStudentId = nStudentId;
	}

	public String getM_strAcademicYear()
	{
		return m_strAcademicYear;
	}

	public void setM_strAcademicYear(String strAcademicYear)
	{
		this.m_strAcademicYear = strAcademicYear;
	}
	
	public AcademicDetails getM_oAcademicDetails()
	{
		return m_oAcademicDetails;
	}

	public void setM_oAcademicDetails(AcademicDetails oAcademicDetails)
	{
		this.m_oAcademicDetails = oAcademicDetails;
	}

	public int getM_nAccountId()
	{
		return m_nAccountId;
	}

	public void setM_nAccountId(int nAccountId)
	{
		this.m_nAccountId = nAccountId;
	}	

	public String getM_strApplicationType() 
	{
		return m_strApplicationType;
	}

	public void setM_strApplicationType(String strApplicationType) 
	{
		this.m_strApplicationType = strApplicationType;
	}

	public String getM_strPaymentType()
	{
		return m_strPaymentType;
	}

	public void setM_strPaymentType(String strPaymentType)
	{
		this.m_strPaymentType = strPaymentType;
	}

	public String getM_strPayeeName()
	{
		return m_strPayeeName;
	}

	public void setM_strPayeeName(String strPayeeName)
	{
		this.m_strPayeeName = strPayeeName;
	}

	public float getM_fSanctionedAmount()
	{
		return m_fSanctionedAmount;
	}

	public void setM_fSanctionedAmount(float fSanctionedAmount)
	{
		this.m_fSanctionedAmount = fSanctionedAmount;
	}

	public Date getM_dSanctionDate()
	{
		return m_dSanctionDate;
	}

	public void setM_dSanctionDate(Date dSanctionDate)
	{
		this.m_dSanctionDate = dSanctionDate;
	}
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nAccountId() > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAccountId"), m_nAccountId));
		}				
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nAccountId() > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAccountId"), m_nAccountId));
		}				
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strChequeInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument();
			Element oRootElement = createRootElement(oXmlDocument, "StudentScholarshipAccount");
			addChild(oXmlDocument, oRootElement, "m_nAccountId", m_nAccountId);
			addChild(oXmlDocument, oRootElement, "m_strApplicationType", m_strApplicationType);
			addChild(oXmlDocument, oRootElement, "m_strPaymentType", m_strPaymentType);		
			addChild(oXmlDocument, oRootElement, "m_dSanctionDate",m_dSanctionDate != null ? getSanctionDate(m_dSanctionDate.toString()) :"");
			addChild(oXmlDocument, oRootElement, "m_strPayeeName", m_strPayeeName);
			addChild(oXmlDocument, oRootElement, "m_fSanctionedAmount", m_fSanctionedAmount);
			addChild(oXmlDocument, oRootElement, "m_nChequeNumber", m_nChequeNumber);
			addChild(oXmlDocument, oRootElement, "m_nDDNumber", m_nDDNumber);
			strChequeInfoXML = getXmlString(oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}		
		return strChequeInfoXML;
	}

	private String getSanctionDate(String strSanctionDate)
	{		
		return strSanctionDate.substring(0, 10);
	}	
}
