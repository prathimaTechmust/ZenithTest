package com.techmust.inventory.paymentsandreceipt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac34_transaction_mode")
public class TransactionMode extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac34_id")
	private int m_nModeId;
	@Column(name = "ac34_mode_name")
	private String m_strModeName;

	public TransactionMode() 
	{
		setM_nModeId(-1);
		setM_strModeName("");
	}
	
	public void setM_nModeId(int nModeId) 
	{
		this.m_nModeId = nModeId;
	}

	public int getM_nModeId() 
	{
		return m_nModeId;
	}

	public void setM_strModeName(String strModeName) 
	{
		this.m_strModeName = strModeName;
	}

	public String getM_strModeName() 
	{
		return m_strModeName;
	}

	@Override
	public String generateXML() 
	{
		String strTransactionDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "TransactionMode");
			addChild (oXmlDocument, oRootElement, "m_nModeId", m_nModeId);
			addChild (oXmlDocument, oRootElement, "m_strModeName", m_strModeName);
			strTransactionDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strTransactionDataXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nModeId > 0)
			oCriteria.add (Restrictions.eq ("m_nModeId", m_nModeId));
		if (!m_strModeName.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strModeName", m_strModeName.trim(), MatchMode.EXACT));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strModeName");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nModeId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nModeId"), m_nModeId)); 
		if (!m_strModeName.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strModeName")), m_strModeName.toLowerCase())); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strModeName");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nModeId"), m_nModeId));
		return oConjunct;
	}
}
