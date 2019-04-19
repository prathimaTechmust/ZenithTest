package com.techmust.inventory.stocktransfer;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

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

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.traderp.util.TraderpUtil;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.location.LocationData;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.purchase.PurchaseLineItem;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name="tac10_stocktransfer")
public class StockTransferData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ac10_stockTransferId")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int m_nStockTransferID;
	@ManyToOne
	@JoinColumn(name="ac10_item_id")
	private ItemData m_oItemData;
	@ManyToOne
	@JoinColumn(name="ac10_transferred_from")
	private LocationData m_oTransferredFrom;
	@ManyToOne
	@JoinColumn(name="ac10_transferred_to")
	private LocationData m_oTransferredTo;
	@Column(name="ac10_quantity")
	private float m_nQuantity;
	@Column(name="ac04_transfered_date")
	@Basic
	@Temporal(TemporalType.DATE)
	private Date m_dDate;
	@Column(name="ac10_transferred_on")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date m_dTransferredOn;
	@Column(name="ac10_Transferred_by")
	private int m_nTransferredBy;
	@Transient
	public StockTransferLineItemData [] m_arrStockTransferLineItem;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	
	// non-persistent data used by xsl
	@Transient
	private String m_strDate;
	@Transient
	private String m_strTime;
	@Transient
	public String strOrderColumnName;
	public StockTransferData ()
	{
		m_nStockTransferID = -1;
		m_dDate = Calendar.getInstance().getTime();
		m_dTransferredOn = Calendar.getInstance().getTime();
		m_oItemData = new ItemData ();
		m_oTransferredFrom = new LocationData ();
		m_oTransferredTo = new LocationData ();
		m_nTransferredBy = -1;
		m_nQuantity = 0;
	}
	
	public int getM_nStockTransferID() 
	{
		return m_nStockTransferID;
	}

	public void setM_nStockTransferID(int nStockTransferID)
	{
		m_nStockTransferID = nStockTransferID;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		m_oItemData = oItemData;
	}

	public LocationData getM_oTransferredFrom() 
	{
		return m_oTransferredFrom;
	}

	public void setM_oTransferredFrom(LocationData oTransferredFrom)
	{
		m_oTransferredFrom = oTransferredFrom;
	}

	public LocationData getM_oTransferredTo() 
	{
		return m_oTransferredTo;
	}

	public void setM_oTransferredTo(LocationData oTransferredTo)
	{
		m_oTransferredTo = oTransferredTo;
	}

	public float getM_nQuantity() 
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public void setM_dDate(Date dDate) 
	{
		m_dDate = dDate;
	}

	public Date getM_dDate() 
	{
		return m_dDate;
	}

	public Date getM_dTransferredOn() 
	{
		return m_dTransferredOn;
	}

	public void setM_dTransferredOn(Date dTransferredOn)
	{
		m_dTransferredOn = dTransferredOn;
	}

	public int getM_nTransferredBy() 
	{
		return m_nTransferredBy;
	}

	public void setM_nTransferredBy(int nTransferredBy) 
	{
		m_nTransferredBy = nTransferredBy;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}


	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public void setM_strDate(String strDate) 
	{
		this.m_strDate = strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_strTime(String strTime) 
	{
		this.m_strTime = strTime;
	}

	public String getM_strTime() 
	{
		return m_strTime;
	}

	public String getStrOrderColumnName() 
	{
		return strOrderColumnName;
	}

	public void setStrOrderColumnName(String strOrderColumnName) 
	{
		this.strOrderColumnName = strOrderColumnName;
	}

	@Override
	public String generateXML () 
	{
		m_oLogger.info("generateXML");
		String strStockTransferXML = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "StockTransferData");
			addChild (oXmlDocument, oRootElement, "m_nStockTransferID", m_nStockTransferID);
			addChild (oXmlDocument, oRootElement, "m_dDate", m_dDate != null ? m_dDate.toString() : "");
			addChild (oXmlDocument, oRootElement, "m_nQuantity", m_nQuantity);
//			addChild (oXmlDocument, oRootElement, "m_strTransferredBy", getM_oTransferredBy() != null ? getM_oTransferredBy().getM_strUserName() : "");
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dDate));
			addChild (oXmlDocument, oRootElement, "m_strTime", TraderpUtil.getTime(m_dDate));
			Document oItemtDoc = getXmlDocument (m_oItemData.generateXML());
			Node oItemNode = oXmlDocument.importNode (oItemtDoc.getFirstChild (), true);
			oRootElement.appendChild (oItemNode);
			Document oTransferredFromDoc = getXmlDocument ("<TransferredFrom>"+m_oTransferredFrom.generateXML()+"</TransferredFrom>");
			Node oTransferredFromNode = oXmlDocument.importNode (oTransferredFromDoc.getFirstChild (), true);
			oRootElement.appendChild (oTransferredFromNode);
			Document oTransferredToDoc = getXmlDocument ("<TransferredTo>"+m_oTransferredTo.generateXML()+"</TransferredTo>");
			Node oTransferredToNode = oXmlDocument.importNode (oTransferredToDoc.getFirstChild (), true);
			oRootElement.appendChild (oTransferredToNode);
			strStockTransferXML = getXmlString (oXmlDocument);
		}
		catch(Exception oException)
		{
			m_oLogger.debug("generateXML - oException " + oException);
		}
		return strStockTransferXML;
	}

	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nStockTransferID > 0)
			oCriteria.add (Restrictions.eq ("m_nStockTransferID", m_nStockTransferID));
		if (strColumn.contains("m_strArticleNumber"))
			oCriteria.createCriteria ("m_oItemData").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_strItemName"))
			oCriteria.createCriteria ("m_oItemData").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strOrderColumnName.equalsIgnoreCase("m_strTransferredFrom"))
			oCriteria.createCriteria ("m_oTransferredFrom").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("m_strName") : Order.asc("m_strName"));
		else if (strOrderColumnName.equalsIgnoreCase("m_strTransferredTo"))
			oCriteria.createCriteria ("m_oTransferredTo").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("m_strName") : Order.asc("m_strName"));
		else if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria ("m_oTransferredBy").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else	
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nStockTransferID");
		return oCriteria;
	}

	public void moveToLocation(PurchaseData oPurchaseData, LocationData oLocationData, LocationData oDefaultLocationData) throws Exception 
	{
		StockTransferDataProcessor oDataProcessor = new StockTransferDataProcessor ();
		StockTransferData oStockTransferData = new StockTransferData ();
		oStockTransferData.setM_oTransferredFrom(oDefaultLocationData);
		oStockTransferData.setM_oTransferredTo(oLocationData);
		oStockTransferData.setM_strDate(oPurchaseData.getM_strDate());
		oStockTransferData.setM_nTransferredBy(oPurchaseData.getM_oUserCredentialsData().getM_nUserId());
		oStockTransferData.m_arrStockTransferLineItem = getLineItemArray (oPurchaseData);
		oDataProcessor.transfer(oStockTransferData);
	}

	private StockTransferLineItemData [] getLineItemArray(PurchaseData oPurchaseData) 
	{
		Set<PurchaseLineItem> oPurchaseLineItemSet = oPurchaseData.getM_oPurchaseLineItems();
		StockTransferLineItemData [] arrLineItemArray = new StockTransferLineItemData [oPurchaseLineItemSet.size()];
		Iterator<PurchaseLineItem> oIterator = oPurchaseLineItemSet.iterator();
		int nIndex = 0;
		while (oIterator.hasNext()) 
		{
			StockTransferLineItemData oStockTransferLineItemData = new StockTransferLineItemData ();
			PurchaseLineItem oPurchaseLineItemData = (PurchaseLineItem) oIterator.next();
			oStockTransferLineItemData.setM_strArticleNumber(oPurchaseLineItemData.getM_strArticleNo());
			oStockTransferLineItemData.setM_nQuantity(oPurchaseLineItemData.getM_nQuantity());
			if(oStockTransferLineItemData.getM_strArticleNumber().trim() != "")
				arrLineItemArray[nIndex++] = oStockTransferLineItemData;
		}
		return arrLineItemArray;
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
		if (m_nStockTransferID > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStockTransferID"), m_nStockTransferID)); 
/*
		if (strColumn.contains("m_strArticleNumber"))
			oCriteria.createCriteria ("m_oItemData").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_strItemName"))
			oCriteria.createCriteria ("m_oItemData").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strOrderColumnName.equalsIgnoreCase("m_strTransferredFrom"))
			oCriteria.createCriteria ("m_oTransferredFrom").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("m_strName") : Order.asc("m_strName"));
		else if (strOrderColumnName.equalsIgnoreCase("m_strTransferredTo"))
			oCriteria.createCriteria ("m_oTransferredTo").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("m_strName") : Order.asc("m_strName"));
		else if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria ("m_oTransferredBy").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else	
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nStockTransferID");
*/
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nStockTransferID > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStockTransferID"), m_nStockTransferID)); 
		return oConjunct;
	}
}
