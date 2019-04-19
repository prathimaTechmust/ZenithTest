package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;
import java.util.HashMap;

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

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac32_receipt_invoices")
public class InvoiceReceiptData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac32_invoice_receipt_id")
	private int m_nInvoiceReceiptId;
	@ManyToOne
	@JoinColumn(name = "ac32_Invoice_id")
	private InvoiceData m_oInvoiceData;
	@Column(name = "ac32_amount")
	private float m_nAmount;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac32_receipt_id")
	private ReceiptData m_oReceiptData;
	@Column(name = "ac32_serial_number")
	private int m_nSerialNumber;
	
	public InvoiceReceiptData ()
	{
		m_nInvoiceReceiptId = -1;
		m_nAmount = 0;
		m_nSerialNumber = 0;
	}
	
	public void setM_nInvoiceReceiptId(int nInvoiceReceiptId) 
	{
		this.m_nInvoiceReceiptId = nInvoiceReceiptId;
	}

	public int getM_nInvoiceReceiptId() 
	{
		return m_nInvoiceReceiptId;
	}

	public void setM_oInvoiceData(InvoiceData oInvoiceData) 
	{
		this.m_oInvoiceData = oInvoiceData;
	}

	public InvoiceData getM_oInvoiceData() 
	{
		return m_oInvoiceData;
	}

	public void setM_nAmount(float nAmount) 
	{
		this.m_nAmount = nAmount;
	}

	public float getM_nAmount()
	{
		return m_nAmount;
	}

	public int getM_nSerialNumber() 
	{
		return m_nSerialNumber;
	}

	public void setM_nSerialNumber(int nSerialNumber) 
	{
		m_nSerialNumber = nSerialNumber;
	}

	@Override
	public String generateXML() 
	{
		String strInvoiceReceiptDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "InvoiceReceiptData");
 			addChild (oXmlDocument, oRootElement, "m_nInvoiceReceiptId", m_nInvoiceReceiptId);
			addChild (oXmlDocument, oRootElement, "m_nAmount", m_nAmount);
			Document oInvoiceDoc = getXmlDocument (m_oInvoiceData.generateXML());
			Node oInvoiceNode = oXmlDocument.importNode (oInvoiceDoc.getFirstChild (), true);
			oRootElement.appendChild (oInvoiceNode);
			strInvoiceReceiptDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strInvoiceReceiptDataXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if(m_oInvoiceData != null && m_oInvoiceData.getM_nInvoiceId() > 0)
			oCriteria.createCriteria("m_oInvoiceData").add(Restrictions.eq("m_nInvoiceId", m_oInvoiceData.getM_nInvoiceId()));
		if(m_oReceiptData != null && m_oReceiptData.getM_nReceiptId() > 0)
			oCriteria.createCriteria("m_oReceiptData").add(Restrictions.eq("m_nReceiptId", m_oReceiptData.getM_nReceiptId()));
		return oCriteria;
	}	

	@SuppressWarnings("unchecked")
	public int getInvoiceReceiptId(int nInvoiceId, int nReceiptId) throws Exception 
	{
		int nId = -1;
		if(nReceiptId > 0)
		{
			InvoiceReceiptData oInvoiceReceiptData = new InvoiceReceiptData ();
			InvoiceReceiptDataResponse oInvoiceReceiptDataResponse = new InvoiceReceiptDataResponse ();
			InvoiceData oInvoiceData = new InvoiceData ();
			oInvoiceData.setM_nInvoiceId(nInvoiceId);
			oInvoiceReceiptData.setM_oInvoiceData(oInvoiceData);
			ReceiptData oReceiptData = new ReceiptData ();
			oReceiptData.setM_nReceiptId(nReceiptId);
			oInvoiceReceiptData.setM_oReceiptData(oReceiptData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData = new ArrayList(oInvoiceReceiptData.list(oOrderBy));
			if(oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData.size() > 0)
			{
				oInvoiceReceiptData = (InvoiceReceiptData) oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData.get(0) ;                  
				nId = oInvoiceReceiptData.m_nInvoiceReceiptId;
			}
		}
		return nId;
	}

	public void setM_oReceiptData(ReceiptData oReceiptData)
	{
		this.m_oReceiptData = oReceiptData;
	}

	public ReceiptData getM_oReceiptData() 
	{
		return m_oReceiptData;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
		if(m_oInvoiceData != null && m_oInvoiceData.getM_nInvoiceId() > 0)
			oCriteria.createCriteria("m_oInvoiceData").add(Restrictions.eq("m_nInvoiceId", m_oInvoiceData.getM_nInvoiceId()));
		if(m_oReceiptData != null && m_oReceiptData.getM_nReceiptId() > 0)
			oCriteria.createCriteria("m_oReceiptData").add(Restrictions.eq("m_nReceiptId", m_oReceiptData.getM_nReceiptId()));
*/
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}
