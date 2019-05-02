package com.techmust.scholarshipmanagement.student;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.constants.Constants;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.course.CourseInformationData;
import com.techmust.scholarshipmanagement.institution.InstitutionInformationData;
import com.techmust.utils.Utils;

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
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "studentinstitution",joinColumns = {@JoinColumn(name = "studentid")},inverseJoinColumns = {@JoinColumn(name = "institutionid")})
	private Set<InstitutionInformationData> m_oInstitutionInformationData;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "studentcourse",joinColumns = {@JoinColumn(name = "studentid")},inverseJoinColumns = {@JoinColumn(name = "courseid")})
	private Set<CourseInformationData> m_oCourseInformationData;
	
	public StudentInformationData()
	{
		m_nStudentId = -1;
		m_strStudentName = "";
		m_dDateOfBirth = null; 
		m_strFatherName = "";
		m_strFatherOccupation = "";
		m_strMotherName = "";
		m_strMotherOccupation = "";
		m_strGender = "";
		m_nFamilyIncome = 0;
		m_strEmailAddress = "";
		m_strPhoneNumber = "";
		m_strAlternateNumber = "";
		m_strReligion = "";
		m_strCurrentAddress = "";
		m_strCity = "";
		m_strState = "";
		m_nPincode = -1;
		m_strStudentImageName = "";
		m_nUID = -1;
		
	}		

	public Set<InstitutionInformationData> getM_oInstitutionInformationData()
	{
		return m_oInstitutionInformationData;
	}

	public void setM_oInstitutionInformationData(Set<InstitutionInformationData> m_oInstitutionInformationData)
	{
		this.m_oInstitutionInformationData = m_oInstitutionInformationData;
	}

	public Set<CourseInformationData> getM_oCourseInformationData()
	{
		return m_oCourseInformationData;
	}

	public void setM_oCourseInformationData(Set<CourseInformationData> m_oCourseInformationData)
	{
		this.m_oCourseInformationData = m_oCourseInformationData;
	}

	public long getM_nUID()
	{
		return m_nUID;
	}

	public void setM_nUID(long m_nUID)
	{
		this.m_nUID = m_nUID;
	}

	public Date getM_dDateOfBirth()
	{
		return m_dDateOfBirth;
	}

	public void setM_dDateOfBirth(Date m_dDateOfBirth)
	{
		this.m_dDateOfBirth = m_dDateOfBirth;
	}

	public String getM_strReligion()
	{
		return m_strReligion;
	}

	public void setM_strReligion(String m_strReligion)
	{
		this.m_strReligion = m_strReligion;
	}

	public String getM_strAlternateNumber()
	{
		return m_strAlternateNumber;
	}

	public void setM_strAlternateNumber(String m_strAlternateNumber)
	{
		this.m_strAlternateNumber = m_strAlternateNumber;
	}

	public int getM_nStudentId()
	{
		return m_nStudentId;
	}

	public void setM_nStudentId(int m_nStudentId) 
	{
		this.m_nStudentId = m_nStudentId;
	}

	public String getM_strStudentName() 
	{
		return m_strStudentName;
	}

	public void setM_strStudentName(String m_strStudentName) 
	{
		this.m_strStudentName = m_strStudentName;
	}

	public String getM_strFatherName() 
	{
		return m_strFatherName;
	}

	public void setM_strFatherName(String m_strFatherName) 
	{
		this.m_strFatherName = m_strFatherName;
	}

	public String getM_strFatherOccupation()
	{
		return m_strFatherOccupation;
	}

	public void setM_strFatherOccupation(String m_strFatherOccupation)
	{
		this.m_strFatherOccupation = m_strFatherOccupation;
	}

	public String getM_strMotherName() 
	{
		return m_strMotherName;
	}

	public void setM_strMotherName(String m_strMotherName)
	{
		this.m_strMotherName = m_strMotherName;
	}

	public String getM_strMotherOccupation() 
	{
		return m_strMotherOccupation;
	}

	public void setM_strMotherOccupation(String m_strMotherOccupation) 
	{
		this.m_strMotherOccupation = m_strMotherOccupation;
	}

	public String getM_strGender() 
	{
		return m_strGender;
	}

	public void setM_strGender(String m_strGender) 
	{
		this.m_strGender = m_strGender;
	}	

	public float getM_nFamilyIncome()
	{
		return m_nFamilyIncome;
	}

	public void setM_nFamilyIncome(float m_nFamilyIncome)
	{
		this.m_nFamilyIncome = m_nFamilyIncome;
	}

	public String getM_strEmailAddress() 
	{
		return m_strEmailAddress;
	}

	public void setM_strEmailAddress(String m_strEmailAddress) 
	{
		this.m_strEmailAddress = m_strEmailAddress;
	}

	public String getM_strPhoneNumber()
	{
		return m_strPhoneNumber;
	}

	public void setM_strPhoneNumber(String m_strPhoneNumber) 
	{
		this.m_strPhoneNumber = m_strPhoneNumber;
	}

	public String getM_strCurrentAddress() 
	{
		return m_strCurrentAddress;
	}

	public void setM_strCurrentAddress(String m_strCurrentAddress)
	{
		this.m_strCurrentAddress = m_strCurrentAddress;
	}

	public String getM_strCity() 
	{
		return m_strCity;
	}

	public void setM_strCity(String m_strCity)
	{
		this.m_strCity = m_strCity;
	}

	public String getM_strState()
	{
		return m_strState;
	}

	public void setM_strState(String m_strState)
	{
		this.m_strState = m_strState;
	}

	public int getM_nPincode() 
	{
		return m_nPincode;
	}

	public void setM_nPincode(int m_nPincode) 
	{
		this.m_nPincode = m_nPincode;
	}

	public String getM_strStudentImageName() 
	{
		return m_strStudentImageName;
	}

	public void setM_strStudentImageName(String m_strStudentImageName)
	{
		this.m_strStudentImageName = m_strStudentImageName;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nStudentId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStudentId"), m_nStudentId));
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nStudentId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStudentId"), m_nStudentId));
		if (!m_strStudentName.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strStudentName"), m_strStudentName));
		}
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strItemInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "StudentInformationData");
			addChild (oXmlDocument, oRootElement, "m_nStudentId", m_nStudentId);
			addChild (oXmlDocument, oRootElement, "m_strStudentName", m_strStudentName);
			addChild (oXmlDocument, oRootElement, "m_strGender", m_strGender);	
			addChild (oXmlDocument, oRootElement, "m_dDateOfBirth", m_dDateOfBirth != null ? m_dDateOfBirth.toString () : "");
			addChild (oXmlDocument, oRootElement, "m_strFatherName", m_strFatherName);
			addChild (oXmlDocument, oRootElement, "m_strMotherName", m_strMotherName);
			addChild (oXmlDocument, oRootElement, "m_strFatherOccupation", m_strFatherOccupation);
			addChild (oXmlDocument, oRootElement, "m_strMotherOccupation", m_strMotherOccupation);			
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
			strItemInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strItemInfoXML;		
	}

	private static String getStudentImageURL(int nStudentId, String strStudentImageName)
	{
		String strFileExtension = FilenameUtils.getExtension(strStudentImageName);
		String strStudentImageUrl = Constants.STUDENTIMAGEURL + Constants.STUDENTIMAGEFOLDER+ nStudentId + "." + strFileExtension;
		return strStudentImageUrl;
	}
}
