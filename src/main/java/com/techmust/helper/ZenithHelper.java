package com.techmust.helper;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.generic.data.GenericData;
import com.techmust.scholarshipmanagement.academicdetails.AcademicYear;
import com.techmust.scholarshipmanagement.course.CourseInformationData;
import com.techmust.scholarshipmanagement.institution.InstitutionInformationData;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.actionarea.ActionAreaData;
import com.techmust.usermanagement.facilitator.FacilitatorInformationData;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ZenithHelper extends GenericData
{

	private static final long serialVersionUID = 1L;
	
	private UserInformationData m_oUserData;
	private FacilitatorInformationData m_oFacilitatorInformationData;
	private StudentInformationData m_oStudentInformationData;
	private CourseInformationData m_oCourseInformationData;
	private InstitutionInformationData m_oInstitutionInformationData;
	private AcademicYear m_oAcademicYear;
	private RoleData m_oRoleData;
	private ActionAreaData m_oActionArea;
	private ActionData m_oAction;	
	private String m_strColumn;
	private String m_strOrderBy;
	private int m_nPageNo;
	private int m_nPageSize;
	private int m_nUserId;
	private String m_strFromDate;
	private String m_strToDate;
	private boolean m_bIncludeZeroMovement;
	public boolean m_bsuccess;
	
	public ZenithHelper ()
	{
		m_oUserData = new UserInformationData ();
		m_oFacilitatorInformationData = new FacilitatorInformationData();
		m_oStudentInformationData = new StudentInformationData();
		m_oCourseInformationData = new CourseInformationData();
		m_oInstitutionInformationData = new InstitutionInformationData();
		m_oAcademicYear = new AcademicYear();
		m_oRoleData = new RoleData ();
		m_oActionArea = new ActionAreaData ();
		m_oAction = new ActionData ();
		m_strColumn = "";
		m_strOrderBy = "";
		m_nPageNo = 0;
		m_nPageSize = 10;
	}
		
	public boolean isM_bsuccess()
	{
		return m_bsuccess;
	}

	public void setM_bsuccess(boolean bsuccess)
	{
		this.m_bsuccess = bsuccess;
	}

	public AcademicYear getM_oAcademicYear() 
	{
		return m_oAcademicYear;
	}

	public void setM_oAcademicYear(AcademicYear oAcademicYear)
	{
		this.m_oAcademicYear = oAcademicYear;
	}
	
	public InstitutionInformationData getM_oInstitutionInformationData()
	{
		return m_oInstitutionInformationData;
	}

	public void setM_oInstitutionInformationData(InstitutionInformationData m_oInstitutionInformationData)
	{
		this.m_oInstitutionInformationData = m_oInstitutionInformationData;
	}

	public CourseInformationData getM_oCourseInformationData()
	{
		return m_oCourseInformationData;
	}

	public void setM_oCourseInformationData(CourseInformationData m_oCourseInformationData)
	{
		this.m_oCourseInformationData = m_oCourseInformationData;
	}

	public StudentInformationData getM_oStudentInformationData()
	{
		return m_oStudentInformationData;
	}

	public void setM_oStudentInformationData(StudentInformationData m_oStudentInformationData) 
	{
		this.m_oStudentInformationData = m_oStudentInformationData;
	}

	public FacilitatorInformationData getM_oFacilitatorInformationData()
	{
		return m_oFacilitatorInformationData;
	}
	
	public void setM_oFacilitatorInformationData(FacilitatorInformationData m_oFacilitatorInformationData)
	{
		this.m_oFacilitatorInformationData = m_oFacilitatorInformationData;
	}

	public UserInformationData getM_oUserData() 
	{
		return m_oUserData;
	}

	public void setM_oUserData(UserInformationData oUserData) 
	{
		m_oUserData = oUserData;
	}

	public RoleData getM_oRoleData() 
	{
		return m_oRoleData;
	}

	public void setM_oRoleData(RoleData oRoleData) 
	{
		m_oRoleData = oRoleData;
	}

	public ActionAreaData getM_oActionArea() 
	{
		return m_oActionArea;
	}

	public void setM_oActionArea(ActionAreaData oActionArea) 
	{
		m_oActionArea = oActionArea;
	}

	public void setM_oAction(ActionData oAction) 
	{
		this.m_oAction = oAction;
	}

	public ActionData getM_oAction() 
	{
		return m_oAction;
	}

	public String getM_strColumn() 
	{
		return m_strColumn;
	}

	public void setM_strColumn(String strColumn) 
	{
		m_strColumn = strColumn;
	}

	public String getM_strOrderBy() 
	{
		return m_strOrderBy;
	}

	public void setM_strOrderBy(String strOrderBy)
	{
		m_strOrderBy = strOrderBy;
	}

	public int getM_nPageNo()
	{
		return m_nPageNo;
	}

	public void setM_nPageNo(int nPageNo) 
	{
		m_nPageNo = nPageNo;
	}

	public int getM_nPageSize() 
	{
		return m_nPageSize;
	}

	public void setM_nPageSize(int nPageSize) 
	{
		m_nPageSize = nPageSize;
	}

	@Override
	public String generateXML() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1)throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria arg0, String arg1, String arg2)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getM_strFromDate()
	{
		return m_strFromDate;
	}

	public void setM_strFromDate(String fromDate)
	{
		m_strFromDate = fromDate;
	}

	public String getM_strToDate() 
	{
		return m_strToDate;
	}

	public void setM_strToDate(String toDate) 
	{
		m_strToDate = toDate;
	}

	public boolean isM_bIncludeZeroMovement() 
	{
		return m_bIncludeZeroMovement;
	}

	public void setM_bIncludeZeroMovement(boolean includeZeroMovement)
	{
		m_bIncludeZeroMovement = includeZeroMovement;
	}
	
	public int getM_nUserId() 
	{
		return m_nUserId;
	}

	public void setM_nUserId(int nUserId)
	{
		m_nUserId = nUserId;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}

	@Override
	public EntityManager _getEntityManager()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
