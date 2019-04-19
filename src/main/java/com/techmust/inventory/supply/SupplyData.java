package com.techmust.inventory.supply;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.sales.NonStockSalesLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name = "tac20_supply")
public class SupplyData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac20_id")
	private int m_nId;
	@Column(name ="ac20_from")
	private String m_strFrom;
	@Column(name ="ac20_invoiceNo")
	private String m_strInvoiceNo;
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "ac20_supply_date")
	private Date m_dDate;
	private String m_strDate;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac20_created_on")
	private Date m_dCreatedOn;
	@Column(name = "ac20_Created_by")
	private int m_nCreatedBy;
	@OneToMany
	@JoinColumn(name = "ac19_supply_id")
	private Set<NonStockSalesLineItemData> m_oNonStockSalesLineItems;
	@Transient
	public NonStockSalesLineItemData [] m_arrNonStockSalesLineItems;
	
	public SupplyData ()
	{
		m_nId = -1;
		m_strFrom = "";
		m_strInvoiceNo = "";
		m_dDate = Calendar.getInstance().getTime ();
		m_dCreatedOn = Calendar.getInstance().getTime ();
		m_oNonStockSalesLineItems = new HashSet<NonStockSalesLineItemData> ();
	}

	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId) 
	{
		m_nId = nId;
	}

	public String getM_strFrom () 
	{
		return m_strFrom;
	}

	public void setM_strFrom (String strFrom) 
	{
		m_strFrom = strFrom;
	}

	public String getM_strInvoiceNo () 
	{
		return m_strInvoiceNo;
	}

	public void setM_strInvoiceNo (String strInvoiceNo) 
	{
		m_strInvoiceNo = strInvoiceNo;
	}

	public Date getM_dDate () 
	{
		return m_dDate;
	}

	public void setM_dDate (Date dDate) 
	{
		m_dDate = dDate;
	}

	public String getM_strDate () 
	{
		return m_strDate;
	}

	public void setM_strDate (String strDate) 
	{
		m_strDate = strDate;
	}

	public Date getM_dCreatedOn () 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn (Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public int getM_nCreatedBy () 
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy (int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public Set<NonStockSalesLineItemData> getM_oNonStockSalesLineItems () 
	{
		return m_oNonStockSalesLineItems;
	}

	public void setM_oNonStockSalesLineItems (Set<NonStockSalesLineItemData> oNonStockSalesLineItems) 
	{
		m_oNonStockSalesLineItems = oNonStockSalesLineItems;
	}

	@Override
	public String generateXML() 
	{
		String strXML = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "SupplyData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_strFrom", m_strFrom);
			addChild (oXmlDocument, oRootElement, "m_strInvoiceNo", m_strInvoiceNo);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dDate));
			strXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strXML;
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
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
		return oConjunct;
	}
}
