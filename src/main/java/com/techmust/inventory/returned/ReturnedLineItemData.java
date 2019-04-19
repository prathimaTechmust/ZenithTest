package com.techmust.inventory.returned;

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
import com.techmust.inventory.sales.SalesLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac24_returned_lineItems")
public class ReturnedLineItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac24_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name ="ac24_sales_lineItem")
	private SalesLineItemData m_oSalesLineItemData;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name ="ac24_returned_id",nullable = false)
	private ReturnedData m_oReturnedData;
	@Column(name = "ac24_quantity") 
	@ColumnDefault("0")
	private float m_nQuantity;
	@Transient
	private boolean m_bForStockMovementReport;
	
	public ReturnedLineItemData ()
	{
		m_nId = -1;
		m_nQuantity = 0;
		m_oSalesLineItemData = new SalesLineItemData ();
		m_oReturnedData = new ReturnedData ();
		setM_bForStockMovementReport(false);
	}
	
	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_nId(int nId)
	{
		m_nId = nId;
	}

	public SalesLineItemData getM_oSalesLineItemData()
	{
		return m_oSalesLineItemData;
	}

	public void setM_oSalesLineItemData(SalesLineItemData oSalesLineItemData) 
	{
		m_oSalesLineItemData = oSalesLineItemData;
	}

	public float getM_nQuantity() 
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public void setM_oReturnedData(ReturnedData oReturnedData)
	{
		this.m_oReturnedData = oReturnedData;
	}

	public ReturnedData getM_oReturnedData()
	{
		return m_oReturnedData;
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
			Element oRootElement = createRootElement (oXmlDocument, "ReturnedLineItemData");
			SalesLineItemData oSalesLineItemData = new SalesLineItemData ();
			oSalesLineItemData.setM_nLineItemId(m_oSalesLineItemData.getM_nLineItemId());
			oSalesLineItemData = (SalesLineItemData) GenericIDataProcessor.populateObject(oSalesLineItemData);
			Document oSalesLineItemDataXmlDoc = getXmlDocument ("<m_oSalesLineItemData>" + oSalesLineItemData.generateXML () + "</m_oSalesLineItemData>");
			Node oSalesLineItemDataXmlDocNode = oXmlDocument.importNode (oSalesLineItemDataXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oSalesLineItemDataXmlDocNode);
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
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_bForStockMovementReport)
			buildStockMovementCriteria (oCriteria, strColumn, strOrderBy);
		return oCriteria;
	}

	private void buildStockMovementCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oReturnDataCriteria = oCriteria.createCriteria("m_oReturnedData");
		Criteria oSalesLineCriteria = oCriteria.createCriteria("m_oSalesLineItemData", "SalesLineItemData");
		if(m_oSalesLineItemData.getM_oItemData() != null && (!m_oSalesLineItemData.getM_oItemData().getM_strItemName().trim().isEmpty() || !m_oSalesLineItemData.getM_oItemData().getM_strBrand().trim().isEmpty() || !m_oSalesLineItemData.getM_oItemData().getM_strArticleNumber().trim().isEmpty()))
			buildItemCriteria (oCriteria, strColumn, strOrderBy, oSalesLineCriteria);
		if ((m_oReturnedData.getM_strFromDate() != null && !m_oReturnedData.getM_strFromDate().isEmpty()) && (m_oReturnedData.getM_strToDate() != null && !m_oReturnedData.getM_strToDate().isEmpty()))
			oReturnDataCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oReturnedData.getM_strFromDate(), false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oReturnedData.getM_strToDate(), true)));
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("SalesLineItemData.m_oItemData"));
		oProjectionList.add(Projections.sum("m_nQuantity"));
		oCriteria.setProjection(oProjectionList);
	}

	private void buildItemCriteria (Criteria oCriteria, String strColumn, String strOrderBy, Criteria oSalesLineCriteria) 
	{
		Criteria oItemDataCriteria = oSalesLineCriteria.createCriteria("m_oItemData");
		if (m_oSalesLineItemData.getM_oItemData() != null && !m_oSalesLineItemData.getM_oItemData().getM_strItemName().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike("m_strItemName", m_oSalesLineItemData.getM_oItemData().getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oSalesLineItemData.getM_oItemData() != null && !m_oSalesLineItemData.getM_oItemData().getM_strBrand().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike("m_strBrand", m_oSalesLineItemData.getM_oItemData().getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oSalesLineItemData.getM_oItemData() != null && !m_oSalesLineItemData.getM_oItemData().getM_strArticleNumber().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.eq("m_strArticleNumber", m_oSalesLineItemData.getM_oItemData().getM_strArticleNumber().trim()));
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList getReturnedStockForPeriod (String strFromDate, String strToDate, ItemData oItemData) throws Exception 
	{
		ReturnedLineItemData oReturnedLineItemData = new ReturnedLineItemData ();
		oReturnedLineItemData.getM_oReturnedData().setM_strFromDate(strFromDate);
		oReturnedLineItemData.getM_oReturnedData().setM_strToDate(strToDate);
		oReturnedLineItemData.getM_oSalesLineItemData().setM_oItemData(oItemData);
		oReturnedLineItemData.setM_bForStockMovementReport(true);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList arrReturnedStock = oReturnedLineItemData.list(oOrderBy);
		return arrReturnedStock;
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
		return oConjunct;
	}
}