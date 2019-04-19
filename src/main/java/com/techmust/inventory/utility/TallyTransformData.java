package com.techmust.inventory.utility;

import java.util.ArrayList;
import java.util.HashMap;

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
@Table(name = "tac53_tally_tranform")
public class TallyTransformData extends TenantData 
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac53_tally_transform_id")
	private int m_nTallyTranformId;
	@Column(name ="ac53_key")
	private String m_strKey;
	@Column(name = "ac53_tax_name")
	private String m_strTaxName;
	@Column(name = "ac53_tax_percentage")
    private float m_nPercentage;
	@Column(name = "ac53_tally_key")
    private String m_strTallyKey;

    public TallyTransformData ()
    {
    	m_nTallyTranformId = -1;
    	m_strKey = "";
    	m_strTaxName = "";
    	m_nPercentage = 0;
    	m_strTallyKey = "";
    }
    
    public int getM_nTallyTranformId() 
	{
		return m_nTallyTranformId;
	}
    
	public void setM_nTallyTranformId(int nTallyTranformId) 
	{
		this.m_nTallyTranformId = nTallyTranformId;
	}

	public void setM_strKey(String strKey) 
	{
		this.m_strKey = strKey;
	}

	public String getM_strKey() 
	{
		return m_strKey;
	}
	
	public String getM_strTaxName() 
	{
		return m_strTaxName;
	}

	public void setM_strTaxName(String strTaxName) 
	{
		m_strTaxName = strTaxName;
	}

	public float getM_nPercentage() 
	{
		return m_nPercentage;
	}

	public void setM_nPercentage(float nPercentage) 
	{
		m_nPercentage = nPercentage;
	}
	
	public void setM_strTallyKey(String strTallyKey) 
	{
		this.m_strTallyKey = strTallyKey;
	}

	public String getM_strTallyKey() 
	{
		return m_strTallyKey;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strTallyTransformXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "TallyTransformData");
			addChild (oXmlDocument, oRootElement, "m_nTallyTranformId", m_nTallyTranformId);
			addChild (oXmlDocument, oRootElement, "m_strKey", m_strKey);
			addChild (oXmlDocument, oRootElement, "m_strTaxName", m_strTaxName);
			addChild (oXmlDocument, oRootElement, "m_nPercentage", m_nPercentage);
			addChild (oXmlDocument, oRootElement, "m_strTallyKey", m_strTallyKey);
			strTallyTransformXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strTallyTransformXML;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nTallyTranformId > 0)
			oCriteria.add (Restrictions.eq ("m_nTallyTranformId", m_nTallyTranformId));
		if (!m_strKey.trim().isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strKey", m_strKey.trim()));
		if (!m_strTaxName.isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strTaxName", m_strTaxName.trim()));
		if (m_nPercentage > 0)
			oCriteria.add (Restrictions.eq ("m_nPercentage", m_nPercentage));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strTaxName");
		return oCriteria;
	}

	@SuppressWarnings("unchecked")
	public static String getTallyTransformKeyValue(String strKey, String strTaxName, float nPercentage)
	{
		String strKeyValue = "";
		TallyTransformData oTallyTransformData = new TallyTransformData ();
		oTallyTransformData.setM_strKey(strKey);
		oTallyTransformData.setM_strTaxName(strTaxName);
		oTallyTransformData.setM_nPercentage(nPercentage);
		TallyTransformDataResponse oResponse = new TallyTransformDataResponse ();
		try
        {
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
	        oResponse.m_arrTallyTransformData =	new ArrayList (oTallyTransformData.list(oOrderBy));
	        if (oResponse.m_arrTallyTransformData.size() > 0)
	        	strKeyValue = oResponse.m_arrTallyTransformData.get(0).m_strTallyKey;
	        else
	        	strKeyValue = processTallyTransformData(oTallyTransformData).m_strTallyKey;
	        	
        } 
		catch (Exception oException)
        {
			m_oLogger.error("getTallyTransformKeyValue - oException" + oException);
        }
		return strKeyValue;
	}
	
	private static TallyTransformData processTallyTransformData(TallyTransformData oTallyTransformData) 
	{
		try 
		{
			oTallyTransformData.setM_strTallyKey(getTallyKeyValue(oTallyTransformData.m_strKey, oTallyTransformData.m_strTaxName, oTallyTransformData.m_nPercentage));
			oTallyTransformData.saveObject();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("processTallyTransformData - oException : " + oException);		
		}
		return oTallyTransformData;
	}

	private static String getTallyKeyValue(String strKey, String strTaxName, float nPercentage) 
	{
		String strKeyValue = "";
		if(strKey == "TAXCLASSIFICATIONNAME-OUTPUT")
			strKeyValue = "Output "+strTaxName+" @ "+nPercentage+"%";
		if(strKey == "LEDGERNAME-SALESAMOUNT")
			strKeyValue = "SALES "+strTaxName+" "+nPercentage+"%";
		if(strKey == "LEDGERNAME-OUPUTTAXAMOUNT")
			strKeyValue = nPercentage+"% OUTPUT TAX";
		if(strKey == "TAXCLASSIFICATIONNAME-INPUT")
			strKeyValue = "Input "+strTaxName+" @ "+nPercentage+"%";
		if(strKey == "LEDGERNAME-PURCHASESAMOUNT")
			strKeyValue = "PURCHASES "+strTaxName+" "+nPercentage+"%";
		if(strKey == "LEDGERNAME-INPUTTAXAMOUNT")
			strKeyValue = nPercentage+"% INPUT TAX";
		return strKeyValue;
	}

	@SuppressWarnings("unchecked")
	public static String getTallyZeroPercentageKeyValue(String strKey, String strTaxName) 
	{
		String strKeyValue = "";
		TallyTransformData oTallyTransformData = new TallyTransformData ();
		oTallyTransformData.setM_strKey(strKey);
		oTallyTransformData.setM_strTaxName(strTaxName);
		TallyTransformDataResponse oResponse = new TallyTransformDataResponse ();
		try
        {
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
	        oResponse.m_arrTallyTransformData =	new ArrayList (oTallyTransformData.list(oOrderBy));
	        for (int nIndex = 0; nIndex < oResponse.m_arrTallyTransformData.size(); nIndex++)
	        {
	        	TallyTransformData oData = oResponse.m_arrTallyTransformData.get(nIndex);
	        	if (oData.m_nPercentage == 0)
	        	{
	        		strKeyValue = oData.m_strTallyKey;
	        		break;
	        	}
	        }
	        if(strKeyValue.trim().isEmpty())
	        {
	        	oTallyTransformData.setM_nPercentage(0);
	        	strKeyValue = processTallyTransformData(oTallyTransformData).m_strTallyKey;
	        }
	        	
        } 
		catch (Exception oException)
        {
        }
		return strKeyValue;
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
		if (m_nTallyTranformId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nTallyTranformId"), m_nTallyTranformId)); 
		if (!m_strKey.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strKey"), m_strKey)); 
		if (!m_strTaxName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strTaxName"), m_strTaxName)); 
		if (m_nPercentage > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nPercentage"), m_nPercentage)); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strTaxName");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nTallyTranformId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nTallyTranformId"), m_nTallyTranformId)); 
		return oConjunct;
	}
}
