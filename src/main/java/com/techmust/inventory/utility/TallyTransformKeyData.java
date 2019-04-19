package com.techmust.inventory.utility;

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
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac54_tally_transform_key")
public class TallyTransformKeyData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac54_Key_id")
	private int m_nTallyTranformKeyId;
	@Column(name = "ac54_key")
	private String m_strKey;
	public TallyTransformKeyData ()
	{
		m_nTallyTranformKeyId = -1;
    	m_strKey = "";
	}

	public int getM_nTallyTranformKeyId () 
	{
		return m_nTallyTranformKeyId;
	}

	public void setM_nTallyTranformKeyId (int nTallyTranformKeyId) 
	{
		m_nTallyTranformKeyId = nTallyTranformKeyId;
	}

	public String getM_strKey ()
	{
		return m_strKey;
	}

	public void setM_strKey (String strKey) 
	{
		m_strKey = strKey;
	}

	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strTallyTransformKeyXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "TallyTransformKeyData");
			addChild (oXmlDocument, oRootElement, "m_nTallyTranformKeyId", m_nTallyTranformKeyId);
			addChild (oXmlDocument, oRootElement, "m_strKey", m_strKey);
			strTallyTransformKeyXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strTallyTransformKeyXML;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nTallyTranformKeyId > 0)
			oCriteria.add (Restrictions.eq ("m_nTallyTranformKeyId", m_nTallyTranformKeyId));
		if (!m_strKey.trim().isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strKey", m_strKey.trim()));
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
		if (m_nTallyTranformKeyId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nTallyTranformKeyId"), m_nTallyTranformKeyId)); 
		if (!m_strKey.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strKey"), m_strKey.trim())); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nTallyTranformKeyId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nTallyTranformKeyId"), m_nTallyTranformKeyId)); 
		return oConjunct;
	}
}
