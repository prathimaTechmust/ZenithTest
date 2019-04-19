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
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac38_payment_purchases")
public class PurchasePaymentData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac38_Purchase_payment_id")
	private int m_nPurchasePaymentId;
	@ManyToOne
	@JoinColumn(name = "ac38_purchase_id")
	private PurchaseData m_oPurchaseData;
	@Column(name = "ac38_amount")
	private float m_nAmount;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac38_payment_id")
	private PaymentData m_oPaymentData;
	@Column(name = "ac38_serial_number")
	private int m_nSerialNumber;
	
	public PurchasePaymentData() 
	{
		m_nPurchasePaymentId = -1;
		m_nAmount = 0;
		m_nSerialNumber = 0;
	}

	public void setM_nPurchasePaymentId(int m_nPurchasePaymentId) 
	{
		this.m_nPurchasePaymentId = m_nPurchasePaymentId;
	}

	public int getM_nPurchasePaymentId() 
	{
		return m_nPurchasePaymentId;
	}

	public void setM_oPurchaseData(PurchaseData m_oPurchaseData) 
	{
		this.m_oPurchaseData = m_oPurchaseData;
	}

	public PurchaseData getM_oPurchaseData() 
	{
		return m_oPurchaseData;
	}

	public void setM_nAmount(float m_nAmount)
	{
		this.m_nAmount = m_nAmount;
	}

	public float getM_nAmount() 
	{
		return m_nAmount;
	}

	public void setM_oPaymentData(PaymentData oPaymentData) 
	{
		m_oPaymentData = oPaymentData;
	}

	public PaymentData getM_oPaymentData() 
	{
		return m_oPaymentData;
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
		String strInvoicePaymentDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "PurchasePaymentData");
 			addChild (oXmlDocument, oRootElement, "m_nPurchasePaymentId", m_nPurchasePaymentId);
			addChild (oXmlDocument, oRootElement, "m_nAmount", m_nAmount);
			Document oInvoiceDoc = getXmlDocument (m_oPurchaseData.generateXML());
			Node oInvoiceNode = oXmlDocument.importNode (oInvoiceDoc.getFirstChild (), true);
			oRootElement.appendChild (oInvoiceNode);
			strInvoicePaymentDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strInvoicePaymentDataXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strCloumn, String strOrderBy)
	{
		if(m_oPurchaseData != null && m_oPurchaseData.getM_nId() > 0)
			oCriteria.createCriteria("m_oPurchaseData").add(Restrictions.eq("m_nId", m_oPurchaseData.getM_nId()));
		if(m_oPaymentData != null && m_oPaymentData.getM_nPaymentId() > 0)
			oCriteria.createCriteria("m_oPaymentData").add(Restrictions.eq("m_nPaymentId", m_oPaymentData.getM_nPaymentId()));
		return oCriteria;
	}

	@SuppressWarnings("unchecked")
	public int getPurchasePaymentId(int nPurchaseId, int nPaymentId) throws Exception 
	{
		int nId = -1;
		if(nPaymentId > 0)
		{
			PurchasePaymentData oPurchasePaymentData = new PurchasePaymentData ();
			PurchasePaymentDataResponse oPurchasePaymentDataResponse = new PurchasePaymentDataResponse ();
			PurchaseData oPurchaseData = new PurchaseData ();
			oPurchaseData.setM_nId(nPurchaseId);
			oPurchasePaymentData.setM_oPurchaseData(oPurchaseData);
			PaymentData oPaymentData = new PaymentData ();
			oPaymentData.setM_nPaymentId(nPaymentId);
			oPurchasePaymentData.setM_oPaymentData(oPaymentData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oPurchasePaymentDataResponse.m_arrPurchasePaymentData = new ArrayList(oPurchasePaymentData.list(oOrderBy));
			if(oPurchasePaymentDataResponse.m_arrPurchasePaymentData.size() > 0)
			{
				oPurchasePaymentData = (PurchasePaymentData) oPurchasePaymentDataResponse.m_arrPurchasePaymentData.get(0) ;                  
				nId = oPurchasePaymentData.m_nPurchasePaymentId;
			}
		}
		return nId;
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
		if(m_oPurchaseData != null && m_oPurchaseData.getM_nId() > 0)
			oCriteria.createCriteria("m_oPurchaseData").add(Restrictions.eq("m_nId", m_oPurchaseData.getM_nId()));
		if(m_oPaymentData != null && m_oPaymentData.getM_nPaymentId() > 0)
			oCriteria.createCriteria("m_oPaymentData").add(Restrictions.eq("m_nPaymentId", m_oPaymentData.getM_nPaymentId()));
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
