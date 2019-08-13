package com.techmust.scholarshipmanagement.chequeFavourOf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "institution_id")
	private InstitutionInformationData m_oInstitutionInformationData;

	public ChequeInFavourOf()
	{
		m_nChequeFavourId = -1;
		m_strChequeFavourOf = "";
		m_oInstitutionInformationData = new InstitutionInformationData();
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
