package com.techmust.inventory.purchasereturned;

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
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
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
import com.techmust.inventory.purchase.PurchaseLineItem;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac58_purchase_returned_lineItems")
public class PurchaseReturnedLineItemData extends TenantData 
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac58_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac58_purchase_lineItem")
	private PurchaseLineItem m_oPurchaseLineItem;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac58_returned_id")
	private PurchaseReturnedData m_oPurchaseReturnedData;
	@Column(name = "ac58_quantity")
	@ColumnDefault("0")
	private float m_nQuantity;
	@Transient
	private boolean m_bForStockMovementReport;
	
	public PurchaseReturnedLineItemData() 
	{
		m_nId = -1;
		m_nQuantity = 0;
		m_oPurchaseLineItem = new PurchaseLineItem ();
		m_oPurchaseReturnedData = new PurchaseReturnedData ();
		setM_bForStockMovementReport(false);
	}

	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId) 
	{
		m_nId = nId;
	}

	public PurchaseLineItem getM_oPurchaseLineItem () 
	{
		return m_oPurchaseLineItem;
	}

	public void setM_oPurchaseLineItem (PurchaseLineItem oPurchaseLineItem) 
	{
		m_oPurchaseLineItem = oPurchaseLineItem;
	}

	public PurchaseReturnedData getM_oPurchaseReturnedData () 
	{
		return m_oPurchaseReturnedData;
	}

	public void setM_oPurchaseReturnedData (PurchaseReturnedData oPurchaseReturnedData) 
	{
		m_oPurchaseReturnedData = oPurchaseReturnedData;
	}

	public float getM_nQuantity () 
	{
		return m_nQuantity;
	}

	public void setM_nQuantity (float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public boolean isM_bForStockMovementReport () 
	{
		return m_bForStockMovementReport;
	}

	public void setM_bForStockMovementReport (boolean bForStockMovementReport)  
	{
		m_bForStockMovementReport = bForStockMovementReport;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strReturnedLineItemXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "PurchaseReturnedLineItemData");
			PurchaseLineItem oPurchaseLineItem = new PurchaseLineItem ();
			oPurchaseLineItem.setM_nLineItemId(m_oPurchaseLineItem.getM_nLineItemId());
			oPurchaseLineItem = (PurchaseLineItem) GenericIDataProcessor.populateObject(oPurchaseLineItem);
			Document oPurchaseLineItemXmlDoc = getXmlDocument ("<m_oPurchaseLineItem>" + oPurchaseLineItem.generateXML () + "</m_oPurchaseLineItem>");
			Node oPurchaseLineItemXmlDocNode = oXmlDocument.importNode (oPurchaseLineItemXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oPurchaseLineItemXmlDocNode);
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_nQuantity", m_nQuantity);
			strReturnedLineItemXml = getXmlString(oXmlDocument);
			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strReturnedLineItemXml;
	}

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData credentials) throws Exception 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn,String strOrderBy)
	{
		if (m_bForStockMovementReport)
			buildStockMovementCriteria (oCriteria, strColumn, strOrderBy);
		return oCriteria;
	}

	private void buildStockMovementCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oPurchaseReturnDataCriteria = oCriteria.createCriteria("m_oPurchaseReturnedData");
		Criteria oPurchaseLineCriteria = oCriteria.createCriteria("m_oPurchaseLineItem", "PurchaseLineItem");
		if(m_oPurchaseLineItem.getM_oItemData() != null && (!m_oPurchaseLineItem.getM_oItemData().getM_strItemName().trim().isEmpty() || !m_oPurchaseLineItem.getM_oItemData().getM_strBrand().trim().isEmpty() || !m_oPurchaseLineItem.getM_oItemData().getM_strArticleNumber().trim().isEmpty()))
			buildItemCriteria (oCriteria, strColumn, strOrderBy, oPurchaseLineCriteria);
		if ((m_oPurchaseReturnedData.getM_strFromDate() != null && !m_oPurchaseReturnedData.getM_strFromDate().isEmpty()) && (m_oPurchaseReturnedData.getM_strToDate() != null && !m_oPurchaseReturnedData.getM_strToDate().isEmpty()))
			oPurchaseReturnDataCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oPurchaseReturnedData.getM_strFromDate(), false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oPurchaseReturnedData.getM_strToDate(), true)));
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("PurchaseLineItem.m_oItemData"));
		oProjectionList.add(Projections.sum("m_nQuantity"));
		oCriteria.setProjection(oProjectionList);
	}

	private void buildItemCriteria(Criteria oCriteria, String strColumn, String strOrderBy, Criteria oPurchaseLineCriteria) 
	{
		Criteria oItemDataCriteria = oPurchaseLineCriteria.createCriteria("m_oItemData");
		if (m_oPurchaseLineItem.getM_oItemData() != null && !m_oPurchaseLineItem.getM_oItemData().getM_strItemName().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike("m_strItemName", m_oPurchaseLineItem.getM_oItemData().getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oPurchaseLineItem.getM_oItemData() != null && !m_oPurchaseLineItem.getM_oItemData().getM_strBrand().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike("m_strBrand", m_oPurchaseLineItem.getM_oItemData().getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oPurchaseLineItem.getM_oItemData() != null && !m_oPurchaseLineItem.getM_oItemData().getM_strArticleNumber().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.eq("m_strArticleNumber", m_oPurchaseLineItem.getM_oItemData().getM_strArticleNumber().trim()));
	}

	@SuppressWarnings("unchecked")
	public static ArrayList getReturnedStockForPeriod(String strFromDate, String strToDate, ItemData oItemData) throws Exception 
	{
		PurchaseReturnedLineItemData oPurchaseReturnedLineItemData = new PurchaseReturnedLineItemData ();
		oPurchaseReturnedLineItemData.getM_oPurchaseReturnedData().setM_strFromDate(strFromDate);
		oPurchaseReturnedLineItemData.getM_oPurchaseReturnedData().setM_strToDate(strToDate);
		oPurchaseReturnedLineItemData.setM_bForStockMovementReport(true);
		oPurchaseReturnedLineItemData.getM_oPurchaseLineItem().setM_oItemData(oItemData);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList arrReturnedPurchaseStock = oPurchaseReturnedLineItemData.list(oOrderBy);
		return arrReturnedPurchaseStock;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
		if (m_bForStockMovementReport)
			buildStockMovementCriteria (oCriteria, strColumn, strOrderBy);
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
