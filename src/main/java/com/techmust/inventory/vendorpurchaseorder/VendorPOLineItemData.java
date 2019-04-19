package com.techmust.inventory.vendorpurchaseorder;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.items.ItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac48_vendorPO_lineItems")
public class VendorPOLineItemData extends TenantData 
{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac48_id")
	private int m_nLineItemId;
	@ManyToOne
	@JoinColumn(name = "ac48_item_id")
	private ItemData m_oItemData;
	@Column(name = "ac48_quantity")
	private float m_nQuantity; 
	@Column(name = "ac48_price")
	private float m_nPrice; 
	@Column(name ="ac48_discount")
	private float m_nDiscount; 
	@Column(name = "ac48_tax_name")
	private String m_strTaxName;
	@Transient
	private float m_nTax;
	@Column(name = "ac48_Received_qty")
	@ColumnDefault("0")
	private float m_nReceivedQty; 
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac48_vendor_PO_id")
	private VendorPurchaseOrderData m_oVendorPurchaseOrderData;
	
	@Column(name = "ac48_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac48_created_on")
	private Date m_dCreatedOn; 
	@Column(name = "ac48_serial_number")
	@ColumnDefault("0")
	private int m_nSerialNumber;
	@Transient
	private float m_nReceiveQty; 
	
	public VendorPOLineItemData ()
	{
		m_nLineItemId = -1;
		m_nQuantity = 0;
		m_nPrice = 0;
		m_nDiscount = 0;
		m_nTax = 0;
		m_nReceivedQty = 0;
		m_strTaxName = "TAX";
		m_oVendorPurchaseOrderData = new VendorPurchaseOrderData();
		setM_nCreatedBy(-1);
		setM_dCreatedOn(Calendar.getInstance().getTime()); 
		m_nSerialNumber = 0;
	}

	public int getM_nLineItemId()
	{
		return m_nLineItemId;
	}

	public void setM_nLineItemId(int nLineItemId) 
	{
		m_nLineItemId = nLineItemId;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		m_oItemData = oItemData;
	}

	public float getM_nQuantity() 
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public float getM_nPrice() 
	{
		return m_nPrice;
	}

	public void setM_nPrice(float nPrice) 
	{
		m_nPrice = nPrice;
	}

	public float getM_nDiscount() 
	{
		return m_nDiscount;
	}

	public void setM_nDiscount(float nDiscount) 
	{
		m_nDiscount = nDiscount;
	}

	public String getM_strTaxName() 
	{
		return m_strTaxName;
	}

	public void setM_strTaxName(String strTaxName)
	{
		m_strTaxName = strTaxName;
	}

	public float getM_nTax() 
	{
		return m_nTax;
	}

	public void setM_nTax(float nTax) 
	{
		m_nTax = nTax;
	}

	public float getM_nReceivedQty() 
	{
		return m_nReceivedQty;
	}

	public void setM_nReceivedQty(float nReceivedQty) 
	{
		m_nReceivedQty = nReceivedQty;
	}

	public VendorPurchaseOrderData getM_oVendorPurchaseOrderData() 
	{
		return m_oVendorPurchaseOrderData;
	}

	public void setM_oVendorPurchaseOrderData(VendorPurchaseOrderData oVendorPurchaseOrderData) 
	{
		m_oVendorPurchaseOrderData = oVendorPurchaseOrderData;
	}

	public int getM_nCreatedBy() 
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public int getM_nSerialNumber() 
	{
		return m_nSerialNumber;
	}

	public void setM_nSerialNumber(int nSerialNumber) 
	{
		m_nSerialNumber = nSerialNumber;
	}

	public void setM_nReceiveQty(float nReceiveQty) 
	{
		m_nReceiveQty = nReceiveQty;
	}

	public float getM_nReceiveQty() 
	{
		return m_nReceiveQty;
	}

	@Override
	public String generateXML() 
	{
		String strVendorPOLineItemDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "VendorPOLineItemData");
			addChild (oXmlDocument, oRootElement, "m_nLineItemId", m_nLineItemId);
			addChild (oXmlDocument, oRootElement, "m_nQuantity", m_nQuantity);
			addChild (oXmlDocument, oRootElement, "m_nPrice", m_nPrice);
			addChild (oXmlDocument, oRootElement, "m_nDiscount", m_nDiscount);
			addChild (oXmlDocument, oRootElement, "m_strTaxName", m_strTaxName);
			addChild (oXmlDocument, oRootElement, "m_nTax", m_nTax);
			addChild (oXmlDocument, oRootElement, "m_nReceivedQty", m_nReceivedQty);
			addChild (oXmlDocument, oRootElement, "m_nAmount", getAmount ());
			Document oItemDataDoc = getXmlDocument (m_oItemData.generateXML());
			Node oItemDataNode = oXmlDocument.importNode (oItemDataDoc.getFirstChild (), true);
			oRootElement.appendChild (oItemDataNode);
			strVendorPOLineItemDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strVendorPOLineItemDataXml;
	}
	
	private float getAmount ()
	{
		float nAmount = 0;
		nAmount =  m_nQuantity * m_nPrice ;
		nAmount -= nAmount *(m_nDiscount/100);
		nAmount += nAmount *(m_nTax/100);
		return nAmount;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (getM_nLineItemId()> 0)
			oCriteria.add (Restrictions.eq ("m_nLineItemId", getM_nLineItemId()));
		if (m_oVendorPurchaseOrderData != null && m_oVendorPurchaseOrderData.getM_nPurchaseOrderId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorPurchaseOrderData", m_oVendorPurchaseOrderData));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nLineItemId");
		return oCriteria;
	}

	public void prepareForUpdate(VendorPurchaseOrderData oPOData) throws Exception 
	{
		m_nCreatedBy = oPOData.getM_nCreatedBy();
		m_oVendorPurchaseOrderData = oPOData;
		setM_oItemData(ItemData.getInstance(getM_oItemData().getM_strArticleNumber(), null));
	}

	public void setReceived(float nQuantity) 
	{
		m_nReceivedQty += nQuantity;
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
		if (getM_nLineItemId()> 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLineItemId"), getM_nLineItemId()));
		if (m_oVendorPurchaseOrderData != null && m_oVendorPurchaseOrderData.getM_nPurchaseOrderId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oVendorPurchaseOrderData"), m_oVendorPurchaseOrderData));
		//addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nLineItemId");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nLineItemId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLineItemId"), m_nLineItemId)); 
		return oConjunct;
	}
}
