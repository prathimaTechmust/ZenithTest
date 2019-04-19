package com.techmust.inventory.paymentsandreceipt;

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

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Entity
@Table(name = "tac64_online_receipt")
public class OnlineReceiptData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac64_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac64_vendor_id")	
    private VendorData m_oVendorData;
	@ManyToOne
	@JoinColumn(name = "ac64_receipt_id")
    private ReceiptData m_oReceiptData;
    
    public OnlineReceiptData ()
    {
    	m_nId = -1;
    	m_oVendorData = new VendorData ();
    	m_oReceiptData = new ReceiptData ();
    }

	public int getM_nId ()
	{
		return m_nId;
	}

	public void setM_nId(int nId) 
	{
		m_nId = nId;
	}

	public VendorData getM_oVendorData () 
	{
		return m_oVendorData;
	}

	public void setM_oVendorData(VendorData oVendorData) 
	{
		m_oVendorData = oVendorData;
	}

	public ReceiptData getM_oReceiptData () 
	{
		return m_oReceiptData;
	}

	public void setM_oReceiptData (ReceiptData oReceiptData) 
	{
		m_oReceiptData = oReceiptData;
	}

	@Override
	public String generateXML() 
	{
		String strVendorReceiptDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "OnlineReceiptData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			Document oVendorDoc = getXmlDocument (m_oVendorData.generateXML());
			Node oVendorNode = oXmlDocument.importNode (oVendorDoc.getFirstChild (), true);
			oRootElement.appendChild (oVendorNode);
			Document oReceiptDoc = getXmlDocument (m_oReceiptData.generateXML());
			Node oReceiptNode = oXmlDocument.importNode (oReceiptDoc.getFirstChild (), true);
			oRootElement.appendChild (oReceiptNode);
			strVendorReceiptDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
			return strVendorReceiptDataXml;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String arg1, String arg2) 
	{
		Criteria oReceiptCriteria = oCriteria.createCriteria("m_oReceiptData");
		Criteria oClientCriteria = oReceiptCriteria.createCriteria("m_oClientData");
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorData", m_oVendorData));
		if (m_oReceiptData != null && m_oReceiptData.getM_nReceiptId() > 0)
			oReceiptCriteria.add (Restrictions.eq ("m_oReceiptData", m_oReceiptData));
		if ((m_oReceiptData.getM_strFromDate() != null && !(m_oReceiptData.getM_strFromDate().isEmpty())) && (m_oReceiptData.getM_strToDate() != null && !m_oReceiptData.getM_strToDate().isEmpty()))
			oReceiptCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oReceiptData.getM_strFromDate(), false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oReceiptData.getM_strToDate(), true)));
		if ((m_oReceiptData.getM_strFromDate() != null && !m_oReceiptData.getM_strFromDate().isEmpty()) && m_oReceiptData.getM_strToDate().isEmpty())
			oReceiptCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_oReceiptData.getM_strFromDate())));
		if (m_oReceiptData.getM_strFromDate().isEmpty() && (m_oReceiptData.getM_strToDate() != null && !m_oReceiptData.getM_strToDate().isEmpty()))
			oReceiptCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oReceiptData.getM_strToDate(), true)));
		if (m_oReceiptData.getM_oClientData()!= null && !m_oReceiptData.getM_oClientData().getM_strCompanyName().trim().isEmpty())
			oClientCriteria.add(Restrictions.ilike("m_strCompanyName", m_oReceiptData.getM_oClientData().getM_strCompanyName().trim(),MatchMode.ANYWHERE));
		return oCriteria;
	}

	public String addVendorToXML(Document oXmlDocument, VendorData oVendorData) 
	{
		String strXML ="";
		try
		{
			if(oVendorData != null)
			{
				Element oRootElement = oXmlDocument.getDocumentElement();
				Document oVendorDataXmlDoc = getXmlDocument ("<m_oVendorData>" + oVendorData.generateXML () + "</m_oVendorData>");
				Node oVendorDataNode = oXmlDocument.importNode (oVendorDataXmlDoc.getFirstChild (), true);
				oRootElement.appendChild (oVendorDataNode);
				strXML = getXmlString (oXmlDocument);
			}
		}
		catch (Exception oException) 
		{
			m_oLogger.error("addVendorToXML - oException : " + oException);
		}
	    m_oLogger.debug( "addVendorToXML - strXML [OUT] : " + strXML);
	    return strXML;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
		Criteria oReceiptCriteria = oCriteria.createCriteria("m_oReceiptData");
		Criteria oClientCriteria = oReceiptCriteria.createCriteria("m_oClientData");
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorData", m_oVendorData));
		if (m_oReceiptData != null && m_oReceiptData.getM_nReceiptId() > 0)
			oReceiptCriteria.add (Restrictions.eq ("m_oReceiptData", m_oReceiptData));
		if ((m_oReceiptData.getM_strFromDate() != null && !(m_oReceiptData.getM_strFromDate().isEmpty())) && (m_oReceiptData.getM_strToDate() != null && !m_oReceiptData.getM_strToDate().isEmpty()))
			oReceiptCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oReceiptData.getM_strFromDate(), false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oReceiptData.getM_strToDate(), true)));
		if ((m_oReceiptData.getM_strFromDate() != null && !m_oReceiptData.getM_strFromDate().isEmpty()) && m_oReceiptData.getM_strToDate().isEmpty())
			oReceiptCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_oReceiptData.getM_strFromDate())));
		if (m_oReceiptData.getM_strFromDate().isEmpty() && (m_oReceiptData.getM_strToDate() != null && !m_oReceiptData.getM_strToDate().isEmpty()))
			oReceiptCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oReceiptData.getM_strToDate(), true)));
		if (m_oReceiptData.getM_oClientData()!= null && !m_oReceiptData.getM_oClientData().getM_strCompanyName().trim().isEmpty())
			oClientCriteria.add(Restrictions.ilike("m_strCompanyName", m_oReceiptData.getM_oClientData().getM_strCompanyName().trim(),MatchMode.ANYWHERE));
*/
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId));
		return oConjunct;
	}
}
