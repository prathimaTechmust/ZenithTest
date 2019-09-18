package com.techmust.scholarshipmanagement.chequeFavourOf;

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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.institution.InstitutionInformationData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "cheque_infavour_of")
public class ChequeInFavourOf extends MasterData
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "favourid")
	private int m_nChequeFavourId;
	
	@Column(name = "favour_of")
	private String m_strChequeFavourOf;
	
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
	@JoinColumn(name = "institution_id")
	private InstitutionInformationData m_oInstitutionInformationData;

	public ChequeInFavourOf()
	{
		m_nChequeFavourId = -1;
		m_strChequeFavourOf = "";
		m_oInstitutionInformationData = new InstitutionInformationData();
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

	public InstitutionInformationData getM_oInstitutionInformationData()
	{
		return m_oInstitutionInformationData;
	}

	public void setM_oInstitutionInformationData(InstitutionInformationData m_oInstitutionInformationData)
	{
		this.m_oInstitutionInformationData = m_oInstitutionInformationData;
	}

	public int getM_nChequeFavourId()
	{
		return m_nChequeFavourId;
	}

	public void setM_nChequeFavourId(int nChequeFavourId)
	{
		this.m_nChequeFavourId = nChequeFavourId;
	}
	
	public String getM_strChequeFavourOf()
	{
		return m_strChequeFavourOf;
	}

	public void setM_strChequeFavourOf(String strChequeFavourOf)
	{
		this.m_strChequeFavourOf = strChequeFavourOf;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunction = oCriteriaBuilder.conjunction();
		if(getM_nChequeFavourId() > 0)
			oConjunction = oCriteriaBuilder.and(oConjunction,oCriteriaBuilder.equal(oRootObject.get("m_nChequeFavourId"),m_nChequeFavourId));
		if(!m_strChequeFavourOf.isEmpty())
			oConjunction = oCriteriaBuilder.and(oConjunction,oCriteriaBuilder.equal(oRootObject.get("m_strChequeFavourOf"),m_strChequeFavourOf));
		return oConjunction;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> ocCriteriaQuery,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunction = oCriteriaBuilder.conjunction();
		if(getM_nChequeFavourId() > 0)
			oConjunction = oCriteriaBuilder.and(oConjunction,oCriteriaBuilder.equal(oRootObject.get("m_nChequeFavourId"),m_nChequeFavourId));
		if(!m_strChequeFavourOf.isEmpty())
			oConjunction = oCriteriaBuilder.and(oConjunction,oCriteriaBuilder.equal(oRootObject.get("m_strChequeFavourOf"),m_strChequeFavourOf));
		return oConjunction;
	}
	
	@Override
	public String generateXML()
	{
		String strChequeFavourXML = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument();
			Element oRootElement = createRootElement(oXmlDocument, "ChequeInFavourOf");
			addChild(oXmlDocument, oRootElement, "m_nChequeFavourId", m_nChequeFavourId);
			addChild(oXmlDocument, oRootElement, "m_strChequeFavourOf", m_strChequeFavourOf);
			strChequeFavourXML = getXmlString(oXmlDocument);			
		}
		catch (Exception oException)
		{
			m_oLogger.error("generate Cheque Favour XML - oException"+oException);
		}
		return strChequeFavourXML;
	}	
}
