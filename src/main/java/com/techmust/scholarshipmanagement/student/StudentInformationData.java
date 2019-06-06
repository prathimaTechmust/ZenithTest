package com.techmust.scholarshipmanagement.student;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Criteria;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techmust.constants.Constants;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.academicdetails.AcademicYear;
import com.techmust.scholarshipmanagement.scholarshipdetails.ScholarshipDetails;
import com.techmust.usermanagement.facilitator.FacilitatorInformationData;

@Entity
@Table(name = "student")
public class StudentInformationData  extends MasterData
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "studentid")
	private int m_nStudentId;
	
	@Column(name = "studentname")
	private String m_strStudentName;
	
	@Column(name = "fathername")
	private String m_strFatherName;
	
	@Column(name = "fatheroccupation")
	private String m_strFatherOccupation;
	
	@Column(name = "mothername")
	private String m_strMotherName;
	
	@Column(name = "motheroccupation")
	private String m_strMotherOccupation;
	
	@Column(name = "gender")
	private String m_strGender;
	
	@Column(name = "dateofbirth")
	private Date m_dDateOfBirth;
	
	@Column(name = "familyincome")
	private float m_nFamilyIncome;
	
	@Column(name = "email")
	private String m_strEmailAddress;
	
	@Column(name = "phonenumber")
	private String m_strPhoneNumber;
	
	@Column(name = "alternatenumber")
	private String m_strAlternateNumber;
	
	@Column(name = "religion")
	private String m_strReligion;
	
	@Column(name = "address")
	private String m_strCurrentAddress;
	
	@Column(name = "city")
	private String m_strCity;
	
	@Column(name = "state")
	private String m_strState;
	
	@Column(name = "pincode")
	private int m_nPincode;
	
	@Column(name = "imagename")
	private String m_strStudentImageName;
	
	@Column(name = "UID")
	private long m_nUID;
	
	@Column(name = "studentaadhar")
	private long m_nStudentAadharNumber;
	
	@Column(name = "fatheraadhar")
	private long m_nFatherAadharNumber;
	
	@Column(name = "motheraadhar")
	private long m_nMotherAadharNumber;
	
	@Column(name = "parentalstatus")
	private String m_strParentalStatus;	
	
	@Transient	
	private String m_strAcademicYear;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "facilitatorid")
	private FacilitatorInformationData m_oFacilitatorInformationData;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "m_oStudentInformationData")
	private Set<AcademicDetails> m_oAcademicDetails;
	
	@Transient
	public AcademicDetails[] m_arrAcademicDetails;
	
	
	public StudentInformationData()
	{
		m_nStudentId = -1;
		m_strStudentName = "";
		m_nStudentAadharNumber = -1;
		m_dDateOfBirth = null; 
		m_strFatherName = "";
		m_strFatherOccupation = "";
		m_nFatherAadharNumber = -1;
		m_strMotherName = "";
		m_strMotherOccupation = "";
		m_nMotherAadharNumber = -1;
		m_strGender = "";
		m_nFamilyIncome = 0;
		m_strEmailAddress = "";
		m_strPhoneNumber = "";
		m_strAlternateNumber = "";
		m_strParentalStatus = "";
		m_strReligion = "";
		m_strCurrentAddress = "";
		m_strCity = "";
		m_strState = "";
		m_nPincode = -1;
		m_strStudentImageName = "";
		m_nUID = -1;
		m_oAcademicDetails = new HashSet<AcademicDetails> ();	
		m_oFacilitatorInformationData = new FacilitatorInformationData();
	}	
	
	public String getM_strAcademicYear()
	{
		return m_strAcademicYear;
	}

	public void setM_strAcademicYear(String m_strAcademicYear)
	{
		this.m_strAcademicYear = m_strAcademicYear;
	}

	public FacilitatorInformationData getM_oFacilitatorInformationData()
	{
		return m_oFacilitatorInformationData;
	}

	public void setM_oFacilitatorInformationData(FacilitatorInformationData m_oFacilitatorInformationData)
	{
		this.m_oFacilitatorInformationData = m_oFacilitatorInformationData;
	}

	public String getM_strParentalStatus()
	{
		return m_strParentalStatus;
	}

	public void setM_strParentalStatus(String strParentalStatus)
	{
		this.m_strParentalStatus = strParentalStatus;
	}

	public long getM_nStudentAadharNumber()
	{
		return m_nStudentAadharNumber;
	}
	
	public void setM_nStudentAadharNumber(long nStudentAadharNumber)
	{
		this.m_nStudentAadharNumber = nStudentAadharNumber;
	}

	public long getM_nFatherAadharNumber()
	{
		return m_nFatherAadharNumber;
	}
	
	public void setM_nFatherAadharNumber(long nFatherAadharNumber)
	{
		this.m_nFatherAadharNumber = nFatherAadharNumber;
	}
	
	public long getM_nMotherAadharNumber() 
	{
		return m_nMotherAadharNumber;
	}

	public void setM_nMotherAadharNumber(long nMotherAadharNumber)
	{
		this.m_nMotherAadharNumber = nMotherAadharNumber;
	}

	public Set<AcademicDetails> getM_oAcademicDetails()
	{
		return m_oAcademicDetails;
	}

	public void setM_oAcademicDetails(Set<AcademicDetails> oAcademicDetails) 
	{
		this.m_oAcademicDetails = oAcademicDetails;
	}	

	public AcademicDetails[] getM_arrAcademicDetails()
	{
		return m_arrAcademicDetails;
	}

	public void setM_arrAcademicDetails(AcademicDetails[] arrAcademicDetails)
	{
		this.m_arrAcademicDetails = arrAcademicDetails;
	}	

	public long getM_nUID()
	{
		return m_nUID;
	}

	public void setM_nUID(long nUID)
	{
		this.m_nUID = nUID;
	}

	public Date getM_dDateOfBirth()
	{
		return m_dDateOfBirth;
	}

	public void setM_dDateOfBirth(Date dDateOfBirth)
	{
		this.m_dDateOfBirth = dDateOfBirth;
	}

	public String getM_strReligion()
	{
		return m_strReligion;
	}

	public void setM_strReligion(String strReligion)
	{
		this.m_strReligion = strReligion;
	}

	public String getM_strAlternateNumber()
	{
		return m_strAlternateNumber;
	}

	public void setM_strAlternateNumber(String strAlternateNumber)
	{
		this.m_strAlternateNumber = strAlternateNumber;
	}

	public int getM_nStudentId()
	{
		return m_nStudentId;
	}

	public void setM_nStudentId(int nStudentId) 
	{
		this.m_nStudentId = nStudentId;
	}

	public String getM_strStudentName() 
	{
		return m_strStudentName;
	}

	public void setM_strStudentName(String strStudentName) 
	{
		this.m_strStudentName = strStudentName;
	}

	public String getM_strFatherName() 
	{
		return m_strFatherName;
	}

	public void setM_strFatherName(String strFatherName) 
	{
		this.m_strFatherName = strFatherName;
	}

	public String getM_strFatherOccupation()
	{
		return m_strFatherOccupation;
	}

	public void setM_strFatherOccupation(String strFatherOccupation)
	{
		this.m_strFatherOccupation = strFatherOccupation;
	}

	public String getM_strMotherName() 
	{
		return m_strMotherName;
	}

	public void setM_strMotherName(String strMotherName)
	{
		this.m_strMotherName = strMotherName;
	}

	public String getM_strMotherOccupation() 
	{
		return m_strMotherOccupation;
	}

	public void setM_strMotherOccupation(String strMotherOccupation) 
	{
		this.m_strMotherOccupation = strMotherOccupation;
	}

	public String getM_strGender() 
	{
		return m_strGender;
	}

	public void setM_strGender(String strGender) 
	{
		this.m_strGender = strGender;
	}	

	public float getM_nFamilyIncome()
	{
		return m_nFamilyIncome;
	}

	public void setM_nFamilyIncome(float nFamilyIncome)
	{
		this.m_nFamilyIncome = nFamilyIncome;
	}

	public String getM_strEmailAddress() 
	{
		return m_strEmailAddress;
	}

	public void setM_strEmailAddress(String strEmailAddress) 
	{
		this.m_strEmailAddress = strEmailAddress;
	}

	public String getM_strPhoneNumber()
	{
		return m_strPhoneNumber;
	}

	public void setM_strPhoneNumber(String strPhoneNumber) 
	{
		this.m_strPhoneNumber = strPhoneNumber;
	}

	public String getM_strCurrentAddress() 
	{
		return m_strCurrentAddress;
	}

	public void setM_strCurrentAddress(String strCurrentAddress)
	{
		this.m_strCurrentAddress = strCurrentAddress;
	}

	public String getM_strCity() 
	{
		return m_strCity;
	}

	public void setM_strCity(String strCity)
	{
		this.m_strCity = strCity;
	}

	public String getM_strState()
	{
		return m_strState;
	}

	public void setM_strState(String strState)
	{
		this.m_strState = strState;
	}

	public int getM_nPincode() 
	{
		return m_nPincode;
	}

	public void setM_nPincode(int nPincode) 
	{
		this.m_nPincode = nPincode;
	}

	public String getM_strStudentImageName() 
	{
		return m_strStudentImageName;
	}

	public void setM_strStudentImageName(String strStudentImageName)
	{
		this.m_strStudentImageName = strStudentImageName;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nStudentId() > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStudentId"), m_nStudentId));
		}			
		if(getM_nUID() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nUID"), m_nUID));
		if(getM_nFatherAadharNumber()>0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nFatherAadharNumber"), m_nFatherAadharNumber));
		if(getM_nMotherAadharNumber() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nMotherAadharNumber"), m_nMotherAadharNumber));		
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nStudentId() > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStudentId"), m_nStudentId));
		}			
		if (!m_strStudentName.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strStudentName"), m_strStudentName));
		}
		if(getM_nUID() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nUID"), m_nUID));	
		if(getM_nFatherAadharNumber()>0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nFatherAadharNumber"), m_nFatherAadharNumber));
		if(getM_nMotherAadharNumber() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nMotherAadharNumber"), m_nMotherAadharNumber));		
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strStudentInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "StudentInformationData");
			Document oAcademicDetalsXmlDoc = getXmlDocument ("<m_oAcademicDetails>"+buildAcademicDetails (m_oAcademicDetails)+"</m_oAcademicDetails>");
			Node oAcademicNode = oXmlDocument.importNode(oAcademicDetalsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oAcademicNode);
			Document oFacilitatorInformationDataXmlDoc = getXmlDocument ("<m_oFacilitatorInformationData>"+buildFacilitatorInformationDetails (m_oFacilitatorInformationData)+"</m_oFacilitatorInformationData>");
			Node oFacilitatorInformationDataNode = oXmlDocument.importNode(oFacilitatorInformationDataXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oFacilitatorInformationDataNode);
			
			addChild (oXmlDocument, oRootElement, "m_nUID", m_nUID);
			addChild (oXmlDocument, oRootElement, "m_nStudentAadharNumber", m_nStudentAadharNumber);
			addChild (oXmlDocument, oRootElement, "m_nStudentId", m_nStudentId);
			addChild (oXmlDocument, oRootElement, "m_strStudentName", m_strStudentName);
			addChild (oXmlDocument, oRootElement, "m_strGender", m_strGender);	
			addChild (oXmlDocument, oRootElement, "m_dDateOfBirth", m_dDateOfBirth != null ? getStudentDOB(m_dDateOfBirth.toString ()) : "");
			addChild (oXmlDocument, oRootElement, "m_strFatherName", m_strFatherName);
			addChild (oXmlDocument, oRootElement, "m_strMotherName", m_strMotherName);
			addChild (oXmlDocument, oRootElement, "m_strReligion", m_strReligion);
			addChild (oXmlDocument, oRootElement, "m_strParentalStatus", m_strParentalStatus);
			addChild (oXmlDocument, oRootElement, "m_strFatherOccupation", m_strFatherOccupation);
			addChild (oXmlDocument, oRootElement, "m_nFatherAadharNumber", m_nFatherAadharNumber);
			addChild (oXmlDocument, oRootElement, "m_strMotherOccupation", m_strMotherOccupation);
			addChild (oXmlDocument, oRootElement, "m_nMotherAadharNumber", m_nMotherAadharNumber);
			addChild (oXmlDocument, oRootElement, "m_nFamilyIncome", m_nFamilyIncome);
			addChild (oXmlDocument, oRootElement, "m_strEmailAddress", m_strEmailAddress);
			addChild (oXmlDocument, oRootElement, "m_strPhoneNumber", m_strPhoneNumber);
			addChild (oXmlDocument, oRootElement, "m_strAlternateNumber", m_strAlternateNumber);
			addChild (oXmlDocument, oRootElement, "m_strCurrentAddress", m_strCurrentAddress);
			addChild (oXmlDocument, oRootElement, "m_strCity", m_strCity);
			addChild (oXmlDocument, oRootElement, "m_strState", m_strState);
			addChild (oXmlDocument, oRootElement, "m_nPincode", m_nPincode);
			addChild (oXmlDocument, oRootElement, "m_strStudentImageName", m_strStudentImageName);					
			addChild (oXmlDocument, oRootElement, "m_strStudentImageUrl", getStudentImageURL(m_nStudentId,m_strStudentImageName));
			strStudentInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strStudentInfoXML;		
	}	

	private String buildFacilitatorInformationDetails(FacilitatorInformationData oFacilitatorInformationData)
	{
		
		return oFacilitatorInformationData.generateXML();
	}

	private String getStudentDOB(String strStudentDOB)
	{		
		return strStudentDOB.substring(0, 10);
	}	

	private String buildAcademicDetails(Set<AcademicDetails> oAcademicDetails)
	{
		String strXML = "";
		Object [] arrAcademicDetails = oAcademicDetails.toArray ();
		for (int nIndex = 0; nIndex < arrAcademicDetails.length; nIndex ++)
		{
			AcademicDetails oAcademicData = (AcademicDetails) arrAcademicDetails [nIndex];
			strXML += oAcademicData.generateXML ();
		}		
		return strXML;
	}


	private static String getStudentImageURL(int nStudentId, String strStudentImageName)
	{
		String strFileExtension = FilenameUtils.getExtension(strStudentImageName);
		String strStudentImageUrl = Constants.STUDENTIMAGEURL + Constants.STUDENTIMAGEFOLDER+ nStudentId + "." + strFileExtension;
		return strStudentImageUrl;
	}
}
