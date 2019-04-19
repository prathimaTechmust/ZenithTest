package com.techmust.inventory.invoice;

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
@Table(name = "tac63_online_vendor_invoice")
public class OnlineVendorInvoiceData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac63_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac63_vendor_id")
    private VendorData m_oVendorData;
	@ManyToOne
	@JoinColumn(name = "ac63_invoice_id")
    private InvoiceData m_oInvoiceData;
	
    public OnlineVendorInvoiceData ()
    {
    	m_nId = -1;
    	m_oVendorData = new VendorData ();
    	m_oInvoiceData = new InvoiceData ();
    }
    
	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId) 
	{
		m_nId = nId;
	}

	public VendorData getM_oVendorData () 
	{
		return m_oVendorData;
	}

	public void setM_oVendorData (VendorData oVendorData) 
	{
		m_oVendorData = oVendorData;
	}

	public InvoiceData getM_oInvoiceData () 
	{
		return m_oInvoiceData;
	}

	public void setM_oInvoiceData (InvoiceData oInvoiceData) 
	{
		m_oInvoiceData = oInvoiceData;
	}

	@Override
	public String generateXML () 
	{
		String strVendorInvoiceDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "OnlineVendorInvoiceData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			Document oVendorDoc = getXmlDocument (m_oVendorData.generateXML());
			Node oVendorNode = oXmlDocument.importNode (oVendorDoc.getFirstChild (), true);
			oRootElement.appendChild (oVendorNode);
			Document oInvoiceDoc = getXmlDocument (m_oInvoiceData.generateXML());
			Node oInvoiceNode = oXmlDocument.importNode (oInvoiceDoc.getFirstChild (), true);
			oRootElement.appendChild (oInvoiceNode);
			strVendorInvoiceDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
			return strVendorInvoiceDataXml;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) throws Exception 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oInvoiceCriteria = oCriteria.createCriteria("m_oInvoiceData");
		Criteria oClientCriteria = oInvoiceCriteria.createCriteria("m_oClientData");
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorData", m_oVendorData));
		if (m_oInvoiceData != null && m_oInvoiceData.getM_nInvoiceId() > 0)
			oCriteria.add (Restrictions.eq ("m_oInvoiceData", m_oInvoiceData));
		if(m_oInvoiceData.getM_oClientData().getM_strCompanyName() != null && !m_oInvoiceData.getM_oClientData().getM_strCompanyName().trim().isEmpty())
			oClientCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oInvoiceData.getM_oClientData().getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if (m_oInvoiceData.getM_strInvoiceNumber() != null && !m_oInvoiceData.getM_strInvoiceNumber().isEmpty())
			oInvoiceCriteria.add (Restrictions.eq ("m_strInvoiceNumber", m_oInvoiceData.getM_strInvoiceNumber()));
		if (!m_oInvoiceData.getM_strRemarks().trim().isEmpty ())
		{
			oInvoiceCriteria.add(Restrictions.disjunction().add(Restrictions.ilike("m_strRemarks", m_oInvoiceData.getM_strRemarks().trim(), MatchMode.ANYWHERE))
			.add(Restrictions.ilike("m_strLRNumber", m_oInvoiceData.getM_strRemarks().trim(), MatchMode.ANYWHERE))
			.add(Restrictions.ilike("m_strESugamNumber", m_oInvoiceData.getM_strRemarks().trim(), MatchMode.ANYWHERE)));
		}
		if ((m_oInvoiceData.getM_strFromDate() != null && !m_oInvoiceData.getM_strFromDate().isEmpty()) && (m_oInvoiceData.getM_strToDate() != null && !m_oInvoiceData.getM_strToDate().isEmpty()))
			oInvoiceCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oInvoiceData.getM_strFromDate(), false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oInvoiceData.getM_strToDate(), true)));
		if ((m_oInvoiceData.getM_strFromDate() != null && !m_oInvoiceData.getM_strFromDate().isEmpty()) && m_oInvoiceData.getM_strToDate().isEmpty())
			oInvoiceCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_oInvoiceData.getM_strFromDate())));
		if (m_oInvoiceData.getM_strFromDate().isEmpty() && (m_oInvoiceData.getM_strToDate() != null && !m_oInvoiceData.getM_strToDate().isEmpty()))
			oInvoiceCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oInvoiceData.getM_strToDate(), true)));
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
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oVendorData"), m_oVendorData));
		if (m_oInvoiceData != null && m_oInvoiceData.getM_nInvoiceId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oInvoiceData"), m_oInvoiceData));
		if(m_oInvoiceData.getM_oClientData().getM_strCompanyName() != null && !m_oInvoiceData.getM_oClientData().getM_strCompanyName().trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCompanyName")),  m_oInvoiceData.getM_oClientData().getM_strCompanyName().trim()));
		if (m_oInvoiceData.getM_strInvoiceNumber() != null && !m_oInvoiceData.getM_strInvoiceNumber().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strInvoiceNumber"), m_oInvoiceData.getM_strInvoiceNumber()));
		if (!m_oInvoiceData.getM_strRemarks().trim().isEmpty ())
		{
			/*oInvoiceCriteria.add(Restrictions.disjunction().add(Restrictions.ilike("m_strRemarks", m_oInvoiceData.getM_strRemarks().trim(), MatchMode.ANYWHERE))
			.add(Restrictions.ilike("m_strLRNumber", m_oInvoiceData.getM_strRemarks().trim(), MatchMode.ANYWHERE))
			.add(Restrictions.ilike("m_strESugamNumber", m_oInvoiceData.getM_strRemarks().trim(), MatchMode.ANYWHERE)));*/
		}
		if ((m_oInvoiceData.getM_strFromDate() != null && !m_oInvoiceData.getM_strFromDate().isEmpty()) && (m_oInvoiceData.getM_strToDate() != null && !m_oInvoiceData.getM_strToDate().isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"),
				 						  oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oInvoiceData.getM_strFromDate(), false).toString()),
				 						  oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oInvoiceData.getM_strToDate(), true).toString())));

		if ((m_oInvoiceData.getM_strFromDate() != null && !m_oInvoiceData.getM_strFromDate().isEmpty()) && m_oInvoiceData.getM_strToDate().isEmpty())
			//oInvoiceCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_oInvoiceData.getM_strFromDate())));
			//oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.ge(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleDateFormat(m_oInvoiceData.getM_strFromDate()))));
		if (m_oInvoiceData.getM_strFromDate().isEmpty() && (m_oInvoiceData.getM_strToDate() != null && !m_oInvoiceData.getM_strToDate().isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.le(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oInvoiceData.getM_strToDate(), true).toString())));
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
