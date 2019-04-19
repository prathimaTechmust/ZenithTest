package com.techmust.inventory.stocktransfer;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.traderp.util.TraderpUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.location.LocationData;

@Entity
@Table(name = "tac43_stock_transfer_memo")
public class StockTransferMemoData extends TenantData
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac43_stockTransfer_memo_id")
	private int m_nStockTransferMemoId;
	@ManyToOne
	@JoinColumn(name = "ac43_transferred_from")
	private LocationData m_oTransferredFrom;
	@ManyToOne
	@JoinColumn(name = "ac43_transferred_to")
	private LocationData m_oTransferredTo;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac43_transferred_on")
	private Date m_dTransferredOn;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ac43_stock_transfer_memo_id")
	private Set<StockTransferData> m_oStockTransferSet;
	
	// non-persistent data used by xsl
	@Transient
	private String m_strDate;
	@Transient
	private String m_strTime;
	@Transient
	private String m_strFromDate = "";
	@Transient
    private String m_strToDate = "";

	
	public StockTransferMemoData ()
	{
		m_nStockTransferMemoId = -1;
		m_dTransferredOn = Calendar.getInstance().getTime();
		m_oStockTransferSet = new HashSet<StockTransferData> ();
		
	}
	public int getM_nStockTransferMemoId() 
	{
		return m_nStockTransferMemoId;
	}
	
	public void setM_nStockTransferMemoId(int nStockTransferMemoId) 
	{
		m_nStockTransferMemoId = nStockTransferMemoId;
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
	
	public Date getM_dTransferredOn()
	{
		return m_dTransferredOn;
	}
	
	public void setM_dTransferredOn(Date dTransferredOn) 
	{
		m_dTransferredOn = dTransferredOn;
	}
	
	public Set<StockTransferData> getM_oStockTransferSet() 
	{
		return m_oStockTransferSet;
	}
	
	public void setM_oStockTransferSet(Set<StockTransferData> oStockTransferSet) 
	{
		m_oStockTransferSet = oStockTransferSet;
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
	
	public void setM_strFromDate(String strFromDate) 
	{
		this.m_strFromDate = strFromDate;
	}
	
	public String getM_strFromDate() 
	{
		return m_strFromDate;
	}
	
	public void setM_strToDate(String strToDate) 
	{
		this.m_strToDate = strToDate;
	}
	
	public String getM_strToDate() 
	{
		return m_strToDate;
	}
	
	@Override
	public String generateXML() 
	{
		m_oLogger.info("generateXML");
		String strStockTransferMemoXML = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "StockTransferMemoData");
			addChild (oXmlDocument, oRootElement, "m_nStockTransferMemoId", m_nStockTransferMemoId);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dTransferredOn));
			addChild (oXmlDocument, oRootElement, "m_strTime", TraderpUtil.getTime(m_dTransferredOn));
			Document oTransferredFromDoc = getXmlDocument ("<TransferredFrom>"+m_oTransferredFrom.generateXML()+"</TransferredFrom>");
			Node oTransferredFromNode = oXmlDocument.importNode (oTransferredFromDoc.getFirstChild (), true);
			oRootElement.appendChild (oTransferredFromNode);
			Document oTransferredToDoc = getXmlDocument ("<TransferredTo>"+m_oTransferredTo.generateXML()+"</TransferredTo>");
			Node oTransferredToNode = oXmlDocument.importNode (oTransferredToDoc.getFirstChild (), true);
			oRootElement.appendChild (oTransferredToNode);
			Document oStockTransferXmlDoc = getXmlDocument ("<m_oStockTransferSet>" + buildStockTransferSetXml () + "</m_oStockTransferSet>");
			Node oStockTransferNode = oXmlDocument.importNode (oStockTransferXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oStockTransferNode);
			strStockTransferMemoXML = getXmlString (oXmlDocument);
		}
		catch(Exception oException)
		{
			m_oLogger.debug("generateXML - oException " + oException);
		}
		return strStockTransferMemoXML;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oTransferredFromCriteria = oCriteria.createCriteria ("m_oTransferredFrom");
		Criteria oTransferredToCriteria = oCriteria.createCriteria ("m_oTransferredTo");
		if (m_nStockTransferMemoId > 0)
			oCriteria.add (Restrictions.eq ("m_nStockTransferMemoId", m_nStockTransferMemoId));
		if(m_oTransferredFrom != null && !m_oTransferredFrom.getM_strName().isEmpty())
			oTransferredFromCriteria.add(Restrictions.ilike ("m_strName", m_oTransferredFrom.getM_strName().trim(), MatchMode.ANYWHERE));
		if(m_oTransferredTo != null && !m_oTransferredTo.getM_strName().isEmpty())
			oTransferredToCriteria.add(Restrictions.ilike ("m_strName", m_oTransferredTo.getM_strName().trim(), MatchMode.ANYWHERE));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dTransferredOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.gt("m_dTransferredOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.lt ("m_dTransferredOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.equalsIgnoreCase("m_strTransferredFrom"))
			oTransferredFromCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("m_strName") : Order.asc("m_strName"));
		else if (strColumn.equalsIgnoreCase("m_strTransferredTo"))
			oTransferredToCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("m_strName") : Order.asc("m_strName"));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nStockTransferMemoId");
		return oCriteria;
	}

	public void addStockTransfer(StockTransferData oStockTransferData) 
	{
		m_oStockTransferSet.add(oStockTransferData);
	}
	
	private String buildStockTransferSetXml() 
	{
		String strXml = "";
		m_oLogger.debug("buildStockTransferSetXml");
		Iterator<StockTransferData> oIterator = m_oStockTransferSet.iterator();
		while (oIterator.hasNext())
			strXml += oIterator.next().generateXML();
	    return strXml;
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
//		Criteria oTransferredFromCriteria = oCriteria.createCriteria ("m_oTransferredFrom");
//		Criteria oTransferredToCriteria = oCriteria.createCriteria ("m_oTransferredTo");
		if (m_nStockTransferMemoId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStockTransferMemoId"), m_nStockTransferMemoId)); 
/*
		if(m_oTransferredFrom != null && !m_oTransferredFrom.getM_strName().isEmpty())
			oTransferredFromCriteria.add(Restrictions.ilike ("m_strName", m_oTransferredFrom.getM_strName().trim(), MatchMode.ANYWHERE));
		if(m_oTransferredTo != null && !m_oTransferredTo.getM_strName().isEmpty())
			oTransferredToCriteria.add(Restrictions.ilike ("m_strName", m_oTransferredTo.getM_strName().trim(), MatchMode.ANYWHERE));
*/
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dTransferredOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.gt(oRootObject.get("m_dTransferredOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate).toString())));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.lt(oRootObject.get("m_dTransferredOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
/*
		if (strColumn.equalsIgnoreCase("m_strTransferredFrom"))
			oTransferredFromCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("m_strName") : Order.asc("m_strName"));
		else if (strColumn.equalsIgnoreCase("m_strTransferredTo"))
			oTransferredToCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("m_strName") : Order.asc("m_strName"));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nStockTransferMemoId");
*/
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nStockTransferMemoId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStockTransferMemoId"), m_nStockTransferMemoId)); 
		return oConjunct;
	}
}
