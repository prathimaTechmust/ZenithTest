package com.techmust.inventory.purchase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.items.ItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name="tac05_purchase_lineitems")
public class PurchaseLineItem extends TenantData
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ac05_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int m_nLineItemId;
	@Transient
	private String m_strArticleNo;
	@ManyToOne
	@JoinColumn(name = "ac01_item_id")
	private ItemData m_oItemData;
	@Column(name = "ac05_quantity")
	private float m_nQuantity;
	@Column(name = "ac05_price")
	private float m_nPrice;
	@Column(name="ac05_discount")
	@ColumnDefault("0")
	private float m_nDiscount;
	@Column(name="ac05_excise")
	@ColumnDefault("0")
	private float m_nExcise;
	@Column(name="ac05_tax")
	@ColumnDefault("0")
	private float m_nTax;
	@Column(name="ac05_other_charges")
	@ColumnDefault("0")
	private float m_nOtherCharges;
	@Column(name="ac05_created_on")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date m_dCreatedOn;
	@Column(name="ac05_Created_by")
	private int m_nCreatedBy;
	
	@JsonBackReference 
	@ManyToOne
	@JoinColumn(name="ac05_purchaseId")
	private PurchaseData m_oPurchaseData;
	
	@Transient
	private String m_strFromDate;
	@Transient
	private String m_strToDate;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Column(name = "ac05_serial_number")
	@ColumnDefault("0")
	private int m_nSerialNumber;
	@Transient
	private boolean m_bForStockMovementReport;
	@Column(name="ac05_returned_quantity")
	private float m_nReturnedQuantity;
	
	public PurchaseLineItem ()
	{
		m_nLineItemId = -1;
		m_nQuantity = 0;
		m_nPrice = 0;
		m_nDiscount = 0;
		m_nExcise = 0;
		m_nTax = 0;
		m_nOtherCharges = 0;
		m_oItemData = new ItemData ();
		setM_dCreatedOn(Calendar.getInstance().getTime());
		m_nSerialNumber = 0;
		m_bForStockMovementReport = false; 
		m_nReturnedQuantity = 0;
	}
	
	public void setM_nLineItemId(int nLineItemId) 
	{
		this.m_nLineItemId = nLineItemId;
	}

	public int getM_nLineItemId()
	{
		return m_nLineItemId;
	}
	
	public void setM_oItemData (ItemData oItemData)
	{
		this.m_oItemData = oItemData;
	}

	public ItemData getM_oItemData () 
	{
		return m_oItemData;
	}
	
	public void setM_nQuantity (float nQuantity) 
	{
		this.m_nQuantity = nQuantity;
	}

	public float getM_nQuantity()
	{
		return m_nQuantity;
	}
	
	public void setM_nPrice (float nPrice) 
	{
		this.m_nPrice = nPrice;
	}

	public float getM_nPrice ()
	{
		return m_nPrice;
	}
	
	public void setM_dCreatedOn (Date dCreatedOn) 
	{
		this.m_dCreatedOn = dCreatedOn;
	}

	public Date getM_dCreatedOn () 
	{
		return m_dCreatedOn;
	}
	
	public void setM_nCreatedBy(int nCreatedBy) 
	{
		this.m_nCreatedBy = nCreatedBy;
	}

	public int getM_nCreatedBy()
	{
		return m_nCreatedBy;
	}
	
	public void setM_strFromDate(String m_strFromDate)
	{
		this.m_strFromDate = m_strFromDate;
	}

	public String getM_strFromDate()
	{
		return m_strFromDate;
	}

	public void setM_strToDate(String m_strToDate) 
	{
		this.m_strToDate = m_strToDate;
	}

	public String getM_strToDate()
	{
		return m_strToDate;
	}

	public void setM_oPurchaseData (PurchaseData oPurchaseData)
	{
		this.m_oPurchaseData = oPurchaseData;
	}

	public PurchaseData getM_oPurchaseData ()
	{
		return m_oPurchaseData;
	}
	
	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		this.m_oUserCredentialsData = oUserCredentialsData; 
	}

	public UserInformationData getM_oUserCredentialsData()
	{
		return m_oUserCredentialsData;
	}

	public int getM_nSerialNumber () 
	{
		return m_nSerialNumber;
	}

	public void setM_nSerialNumber (int nSerialNumber) 
	{
		m_nSerialNumber = nSerialNumber;
	}

	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strPurchaseLineItemXml = "";
		try 
		{
			
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "PurchaseLineItem");
			
			Document oItemDataXmlDoc = getXmlDocument ("<m_oItemData>" + m_oItemData.generateXML () + "</m_oItemData>");
			Node oItemDataXmlDocNode = oXmlDocument.importNode (oItemDataXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oItemDataXmlDocNode);
			
			addChild (oXmlDocument, oRootElement, "m_nLineItemId", m_nLineItemId);
			addChild (oXmlDocument, oRootElement, "m_strArticleNo", m_strArticleNo);
			addChild (oXmlDocument, oRootElement, "m_nQuantity", m_nQuantity);
			addChild (oXmlDocument, oRootElement, "m_nPrice", m_nPrice);
			addChild (oXmlDocument, oRootElement, "m_nDiscount", m_nDiscount);
			addChild (oXmlDocument, oRootElement, "m_nTax", m_nTax);
			strPurchaseLineItemXml = getXmlString(oXmlDocument);
			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strPurchaseLineItemXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (m_bForStockMovementReport)
			buildStockMovementCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria (oCriteria, strColumn, strOrderBy);
		return oCriteria;
	}
	
	private void buildStockMovementCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if(m_oItemData != null && (!m_oItemData.getM_strItemName().trim().isEmpty() || !m_oItemData.getM_strBrand().trim().isEmpty() || !m_oItemData.getM_strArticleNumber().trim().isEmpty()))
			buildItemCriteria (oCriteria, strColumn, strOrderBy);
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("m_oItemData"));
		oProjectionList.add(Projections.sum("m_nQuantity"));
		oCriteria.setProjection(oProjectionList);
	}

	private void buildItemCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oItemCriteria = oCriteria.createCriteria("m_oItemData");
		if (m_oItemData != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemCriteria.add(Restrictions.eq("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim()));
	}
	
	private void buildListCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		Criteria oItemCriteria = oCriteria.createCriteria("m_oItemData");
		if (m_nLineItemId > 0)
			oCriteria.add (Restrictions.eq("m_nLineItemId",m_nLineItemId));
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oCriteria.add (Restrictions.eq ("m_oItemData", m_oItemData));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false),GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (m_oItemData != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemCriteria.add(Restrictions.eq("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim()));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nLineItemId");
	}
	
	public void setM_strArticleNo(String strArticleNo) 
	{
		this.m_strArticleNo = strArticleNo;
	}

	public String getM_strArticleNo () 
	{
		return m_strArticleNo;
	}

	public void setM_nDiscount(float nDiscount)
    {
	    this.m_nDiscount = nDiscount;
    }

	public float getM_nDiscount()
    {
	    return m_nDiscount;
    }

	public void setM_nExcise(float nExcise)
    {
	    this.m_nExcise = nExcise;
    }

	public float getM_nExcise()
    {
	    return m_nExcise;
    }

	public void setM_nTax(float nTax)
    {
	    this.m_nTax = nTax;
    }

	public float getM_nTax()
    {
	    return m_nTax;
    }

	public void setM_nOtherCharges(float nOtherCharges)
    {
	    this.m_nOtherCharges = nOtherCharges;
    }

	public float getM_nOtherCharges()
    {
	    return m_nOtherCharges;
    }
	
	public float getM_nReturnedQuantity () 
	{
		return m_nReturnedQuantity;
	}

	public void setM_nReturnedQuantity (float nReturnedQuantity) 
	{
		m_nReturnedQuantity = nReturnedQuantity;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setM_bForStockMovementReport(boolean bForStockMovementReport) 
	{
		this.m_bForStockMovementReport = bForStockMovementReport;
	}

	public boolean isM_bForStockMovementReport() 
	{
		return m_bForStockMovementReport;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList getStockMovementForPeriod (String strFromDate, String strToDate, ItemData oItemData) throws Exception
	{
		PurchaseLineItem oPurchaseLineItemData = new PurchaseLineItem ();
		oPurchaseLineItemData.setM_strFromDate(strFromDate);
		oPurchaseLineItemData.setM_strToDate(strToDate);
		oPurchaseLineItemData.setM_bForStockMovementReport(true);
		oPurchaseLineItemData.setM_oItemData(oItemData);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList arrPurchasesStockMovement = oPurchaseLineItemData.list(oOrderBy);
		return arrPurchasesStockMovement;
	}

	public void returned(float nQuantity) 
	{
		m_nReturnedQuantity += nQuantity;
		try
		{
			updateObject();
			m_oItemData.issued(nQuantity);
			m_oItemData.updateObject();
		}
		catch (Exception oException)
		{
			
		}
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*		
		if (m_bForStockMovementReport)
			buildStockMovementCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria (oCriteria, strColumn, strOrderBy);
*/
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
