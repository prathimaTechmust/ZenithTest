package com.techmust.inventory.returned;

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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.sales.NonStockSalesLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac25_nonstock_returned_lineItems")
public class NonStockReturnedLineItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac25_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac25_nonStock_sales_lineItem")
	private NonStockSalesLineItemData m_oNonStockSalesLineItemData;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name ="ac25_returned_id",nullable = false)
	private ReturnedData m_oReturnedData;
	@Column(name = "ac25_quantity")
	@ColumnDefault("0")
	private float m_nQuantity;
	
	public NonStockReturnedLineItemData ()
	{
		m_nId = -1;
		m_nQuantity = 0;
		m_oNonStockSalesLineItemData = new NonStockSalesLineItemData ();
	}

	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_nId(int nId) 
	{
		m_nId = nId;
	}

	public NonStockSalesLineItemData getM_oNonStockSalesLineItemData()
	{
		return m_oNonStockSalesLineItemData;
	}

	public void setM_oNonStockSalesLineItemData(NonStockSalesLineItemData oNonStockSalesLineItemData) 
	{
		m_oNonStockSalesLineItemData = oNonStockSalesLineItemData;
	}

	public float getM_nQuantity ()
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public void setM_oReturnedData(ReturnedData oReturnedData) 
	{
		m_oReturnedData = oReturnedData;
	}

	public ReturnedData getM_oReturnedData() 
	{
		return m_oReturnedData;
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
			NonStockSalesLineItemData oNonStockSalesLineItemData = new NonStockSalesLineItemData ();
			oNonStockSalesLineItemData.setM_nLineItemId(m_oNonStockSalesLineItemData.getM_nLineItemId());
			oNonStockSalesLineItemData = (NonStockSalesLineItemData) GenericIDataProcessor.populateObject(oNonStockSalesLineItemData);
			Document oNonStockSalesLineItemDataXmlDoc = getXmlDocument ("<m_oSalesLineItemData>" + oNonStockSalesLineItemData.generateXML () + "</m_oSalesLineItemData>");
			Node oNonStockSalesLineItemDataXmlDocNode = oXmlDocument.importNode (oNonStockSalesLineItemDataXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oNonStockSalesLineItemDataXmlDocNode);
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
		return oCriteria;
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
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}