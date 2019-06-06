package com.techmust.scholarshipmanagement.academicdetails;

import java.util.Calendar;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.course.CourseInformationData;
import com.techmust.scholarshipmanagement.institution.InstitutionInformationData;
import com.techmust.scholarshipmanagement.scholarshipdetails.ScholarshipDetails;
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
	
	@Column(name = "academicyear")
	private String m_strAcademicYear;
	
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
	private Set<ScholarshipDetails> m_oScholarshipDetails;	

	@Transient
	public ScholarshipDetails[] m_arrScholarshipDetails;
	
	public AcademicDetails()
	{
		m_nAcademicId = -1;			
		m_strSpecialization = "";
		m_strStudentScore = "";
		m_fAnnualFee = 0;
		m_fPaidFee = 0;		
		m_dDate = Calendar.getInstance();
		m_strAcademicYear = "";
		m_oStudentInformationData = new StudentInformationData ();
		m_oInstitutionInformationData = new InstitutionInformationData();
		m_oCourseInformationData = new CourseInformationData();	
		m_oScholarshipDetails = new HashSet<ScholarshipDetails> ();	
	}	
	
	public String getM_strAcademicYear() 
	{
		return m_strAcademicYear;
	}

	public void setM_strAcademicYear(String m_strAcademicYear)
	{
		this.m_strAcademicYear = m_strAcademicYear;
	}

	public Set<ScholarshipDetails> getM_oScholarshipDetails()
	{
		return m_oScholarshipDetails;
	}

	public void setM_oScholarshipDetails(Set<ScholarshipDetails> m_oScholarshipDetails)
	{
		this.m_oScholarshipDetails = m_oScholarshipDetails;
	}

	public ScholarshipDetails[] getM_arrScholarshipDetails()
	{
		return m_arrScholarshipDetails;
	}

	public void setM_arrScholarshipDetails(ScholarshipDetails[] m_arrScholarshipDetails)
	{
		this.m_arrScholarshipDetails = m_arrScholarshipDetails;
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
	public String generateXML() 
	{
		String strAcademicDetails = "";
		m_oLogger.info ("generateXML");
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "AcademicDetails");
			Document oInstitutionDetalsXmlDoc = getXmlDocument ("<m_oInstitutionInformationData>"+buildInstitutionDetails (m_oInstitutionInformationData)+"</m_oInstitutionInformationData>");
			Node oInstitutionNode = oXmlDocument.importNode(oInstitutionDetalsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oInstitutionNode);
			Document oCourseDetalsXmlDoc = getXmlDocument ("<m_oCourseInformationData>"+buildCourseDetails (m_oCourseInformationData)+"</m_oCourseInformationData>");
			Node oCourseNode = oXmlDocument.importNode(oCourseDetalsXmlDoc.getFirstChild(), true);
			Document oScholarshipDetalsXmlDoc = getXmlDocument ("<m_oScholarshipDetails>"+buildScholarshipDetails (m_oScholarshipDetails)+"</m_oScholarshipDetails>");
			Node oScholarshipNode = oXmlDocument.importNode(oScholarshipDetalsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oScholarshipNode);
			oRootElement.appendChild(oCourseNode);
			addChild (oXmlDocument, oRootElement, "m_nAcademicId", m_oStudentInformationData.getM_nStudentId());			
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
	
	private String buildScholarshipDetails(Set<ScholarshipDetails> oScholarshipDetails)
	{
		String strXML = "";
		Object [] arrScholarshipcDetails = oScholarshipDetails.toArray ();
		for (int nIndex = 0; nIndex < arrScholarshipcDetails.length; nIndex ++)
		{
			ScholarshipDetails oScholarshipData = (ScholarshipDetails) arrScholarshipcDetails [nIndex];
			strXML += oScholarshipData.generateXML ();
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
