package com.techmust.scholarshipmanagement.academicdetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.academicyear.AcademicYear;
import com.techmust.scholarshipmanagement.course.CourseInformationData;
import com.techmust.scholarshipmanagement.institution.InstitutionInformationData;
import com.techmust.scholarshipmanagement.scholarshipdetails.scholarshiporganization.ScholarshipOrganizationDetails;
import com.techmust.scholarshipmanagement.sholarshipaccounts.StudentScholarshipAccount;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;

@Entity
@Table(name = "academicdetails")
public class AcademicDetails extends MasterData implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "academicid")
	private int m_nAcademicId;	
	
	@Column(name = "studentscore")
	private String m_strStudentScore;
	
	@Column(name = "specialization")
	private String m_strSpecialization;
	
	@Column(name = "annualfee")
	private float m_fAnnualFee;
	
	@Column(name = "paidfee")
	private float m_fPaidFee;	
	
	@Column(name = "date")
	private Calendar m_dDate;	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "academicyearid")
	private AcademicYear m_oAcademicYear;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "studentid")
	private StudentInformationData m_oStudentInformationData;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "institutionid")
	private InstitutionInformationData m_oInstitutionInformationData;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "courseid")
	private CourseInformationData m_oCourseInformationData;	
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "m_oAcademicDetails")
	private Set<ScholarshipOrganizationDetails> m_oScholarshipOrganizationDetails;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "m_oAcademicDetails")
	private Set<StudentScholarshipAccount> m_oStudentScholarshipAccount;
	
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "academic_id")
	private List<StudentDocuments> m_arrStudentDocuments;
	
	@Transient
	public ScholarshipOrganizationDetails[] m_arrScholarshipOrganizationDetails;	
	
	public AcademicDetails()
	{
		m_nAcademicId = -1;			
		m_strSpecialization = "";
		m_strStudentScore = "";
		m_fAnnualFee = 0;
		m_fPaidFee = 0;		
		m_dDate = Calendar.getInstance();
		m_oStudentInformationData = new StudentInformationData ();
		m_oInstitutionInformationData = new InstitutionInformationData();
		m_oCourseInformationData = new CourseInformationData();	
		m_oScholarshipOrganizationDetails = new HashSet<ScholarshipOrganizationDetails> ();	
		m_oStudentScholarshipAccount = new HashSet<StudentScholarshipAccount> ();
		m_arrStudentDocuments = new ArrayList<StudentDocuments>();
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
	
	public List<StudentDocuments> getM_arrStudentDocuments()
	{
		return m_arrStudentDocuments;
	}

	public void setM_arrStudentDocuments(List<StudentDocuments> arrStudentDocuments)
	{
		this.m_arrStudentDocuments = arrStudentDocuments;
	}

	public Set<StudentScholarshipAccount> getM_oStudentScholarshipAccount()
	{
		return m_oStudentScholarshipAccount;
	}

	public void setM_oStudentScholarshipAccount(Set<StudentScholarshipAccount> oStudentScholarshipAccount)
	{
		this.m_oStudentScholarshipAccount = oStudentScholarshipAccount;
	}

	public Set<ScholarshipOrganizationDetails> getM_oScholarshipOrganizationDetails()
	{
		return m_oScholarshipOrganizationDetails;
	}

	public void setM_oScholarshipOrganizationDetails(Set<ScholarshipOrganizationDetails> m_oScholarshipOrganizationDetails)
	{
		this.m_oScholarshipOrganizationDetails = m_oScholarshipOrganizationDetails;
	}

	public ScholarshipOrganizationDetails[] getM_arrScholarshipOrganizationDetails()
	{
		return m_arrScholarshipOrganizationDetails;
	}

	public void setM_arrScholarshipOrganizationDetails(ScholarshipOrganizationDetails[] m_arrScholarshipOrganizationDetails)
	{
		this.m_arrScholarshipOrganizationDetails = m_arrScholarshipOrganizationDetails;
	}

	public InstitutionInformationData getM_oInstitutionInformationData()
	{
		return m_oInstitutionInformationData;
	}

	public void setM_oInstitutionInformationData(InstitutionInformationData oInstitutionInformationData)
	{
		this.m_oInstitutionInformationData = oInstitutionInformationData;
	}

	public CourseInformationData getM_oCourseInformationData()
	{
		return m_oCourseInformationData;
	}

	public void setM_oCourseInformationData(CourseInformationData oCourseInformationData)
	{
		this.m_oCourseInformationData = oCourseInformationData;
	}

	public String getM_strStudentScore()
	{
		return m_strStudentScore;
	}

	public void setM_strStudentScore(String strStudentScore)
	{
		this.m_strStudentScore = strStudentScore;
	}

	public String getM_strSpecialization()
	{
		return m_strSpecialization;
	}

	public void setM_strSpecialization(String strSpecialization)
	{
		this.m_strSpecialization = strSpecialization;
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

	public Calendar getM_dDate() 
	{
		return m_dDate;
	}

	public void setM_dDate(Calendar m_dDate)
	{
		this.m_dDate = m_dDate;
	}	
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nAcademicId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAcademicId"), m_nAcademicId));				
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nAcademicId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAcademicId"), m_nAcademicId));
		return oConjunct;
	}
	
	@Override
	public String generateXML() 
	{
		String strAcademicDetails = "";
		m_oLogger.info ("generateXML");
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "AcademicDetails");
			/* Institution ChildNode*/
			Document oInstitutionDetalsXmlDoc = getXmlDocument ("<m_oInstitutionInformationData>"+buildInstitutionDetails (m_oInstitutionInformationData)+"</m_oInstitutionInformationData>");
			Node oInstitutionNode = oXmlDocument.importNode(oInstitutionDetalsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oInstitutionNode);
			/*Course ChildNode*/
			Document oCourseDetalsXmlDoc = getXmlDocument ("<m_oCourseInformationData>"+buildCourseDetails (m_oCourseInformationData)+"</m_oCourseInformationData>");
			Node oCourseNode = oXmlDocument.importNode(oCourseDetalsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oCourseNode);
			/*Scholarship Child Node*/
			Document oScholarshipDetalsXmlDoc = getXmlDocument ("<m_oScholarshipOrganizationDetails>"+buildScholarshipDetails (m_oScholarshipOrganizationDetails)+"</m_oScholarshipOrganizationDetails>");
			Node oScholarshipNode = oXmlDocument.importNode(oScholarshipDetalsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oScholarshipNode);
		   /*ScholarshipAccount child Node*/
			Document oScholarshipAccountDetalsXmlDoc = getXmlDocument ("<m_oStudentScholarshipAccount>"+buildAccountDetails (m_oStudentScholarshipAccount)+"</m_oStudentScholarshipAccount>");
			Node oAccountNode = oXmlDocument.importNode(oScholarshipAccountDetalsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oAccountNode);
			
			addChild (oXmlDocument, oRootElement, "m_nAcademicId", m_nAcademicId);			
			addChild (oXmlDocument, oRootElement, "m_strSpecialization", m_strSpecialization);
			addChild (oXmlDocument, oRootElement, "m_strStudentScore", m_strStudentScore);
			addChild (oXmlDocument, oRootElement, "m_fAnnualFee", m_fAnnualFee);
			addChild (oXmlDocument, oRootElement, "m_fPaidFee", m_fPaidFee);
			strAcademicDetails = getXmlString (oXmlDocument);
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}		
		return strAcademicDetails;		
	}	

    private String buildAccountDetails(Set<StudentScholarshipAccount> m_oStudentScholarshipAccount)
    {
		String strXML = "";
		Object [] arrStudentScholarshipAccount= m_oStudentScholarshipAccount.toArray ();
		for (int nIndex = 0; nIndex < arrStudentScholarshipAccount.length; nIndex ++)
		{
			StudentScholarshipAccount oAccountData = (StudentScholarshipAccount) arrStudentScholarshipAccount [nIndex];
			strXML += oAccountData.generateXML ();		
		}		
		return strXML;
	}

	private String buildScholarshipDetails(Set<ScholarshipOrganizationDetails> oScholarshipOrganizationDetails)
	{
		String strXML = "";
		Object [] arrScholarshipcOrganizationDetails = oScholarshipOrganizationDetails.toArray ();
		for (int nIndex = 0; nIndex < arrScholarshipcOrganizationDetails.length; nIndex ++)
		{
			ScholarshipOrganizationDetails oScholarshipOrganizationData = (ScholarshipOrganizationDetails) arrScholarshipcOrganizationDetails [nIndex];
			strXML += oScholarshipOrganizationData.generateXML ();
		}		
		return strXML;
	}


	private String buildCourseDetails(CourseInformationData oCourseInformationData)	
	{
		
		return oCourseInformationData.generateXML();
	}

	private String buildInstitutionDetails(InstitutionInformationData oInstitutionInformationData)
	{
		return oInstitutionInformationData.generateXML();
	}

}
