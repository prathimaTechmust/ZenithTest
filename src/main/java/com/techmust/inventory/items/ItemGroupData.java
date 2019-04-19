package com.techmust.inventory.items;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.clientmanagement.ContactData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.crm.PubliciseData;
import com.techmust.inventory.sales.PubliciseToNotBoughtClients;
import com.techmust.inventory.sales.SalesData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac35_item_group")
public class ItemGroupData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac35_id")
	public int m_nItemGroupId;
	@Column(name = "ac35_group_name")
	private String m_strGroupName;
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "tac36_group_items",joinColumns = {@JoinColumn(name ="ac36_group_id")},inverseJoinColumns = {@JoinColumn(name = "ac36_item_id")})
	private Set<ItemData> m_oGroupItems;
	@Column(name = "ac35_created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac35_created_on")
	private Date m_dCreatedOn;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Transient
	public ItemData [] m_arrGroupItems;
	@Transient
	private String m_strDate;
	@Transient
	public int m_nItemId;
	
	public ItemGroupData ()
	{
		m_nItemGroupId = -1;
		m_strGroupName = "";
		m_nItemId = -1;
		m_dCreatedOn = Calendar.getInstance ().getTime ();
		m_oGroupItems = new HashSet<ItemData> ();
	}
	
	public int getM_nItemGroupId() 
	{
		return m_nItemGroupId;
	}

	public void setM_nItemGroupId(int nItemGroupId) 
	{
		m_nItemGroupId = nItemGroupId;
	}

	public String getM_strGroupName() 
	{
		return m_strGroupName;
	}

	public void setM_strGroupName(String strGroupName) 
	{
		m_strGroupName = strGroupName;
	}

	public void setM_oGroupItems(Set<ItemData> oGroupItems) 
	{
		m_oGroupItems = oGroupItems;
	}

	public Set<ItemData> getM_oGroupItems() 
	{
		return m_oGroupItems;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public int getM_nCreatedBy() 
	{
		return m_nCreatedBy;
	}

	public Date getM_dCreatedOn()
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date oCreatedOn) 
	{
		m_dCreatedOn = oCreatedOn;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public ItemData[] getM_arrGroupItems() 
	{
		return m_arrGroupItems;
	}

	public void setM_arrGroupItems(ItemData[] arrGroupItems) 
	{
		m_arrGroupItems = arrGroupItems;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strGroupInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "GroupData");
			addChild (oXmlDocument, oRootElement, "m_nItemGroupId", m_nItemGroupId);
			addChild (oXmlDocument, oRootElement, "m_strGroupName", m_strGroupName);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
//			addChild (oXmlDocument, oRootElement, "m_strUserName", getM_oCreatedBy() != null ? getM_oCreatedBy().getM_strUserName() : "");
			Node oItemGroupXmlNode = oXmlDocument.importNode (getXmlDocument ("<m_oGroupItems>"+getItemsXML ()+"</m_oGroupItems>").getFirstChild (), true);
			oRootElement.appendChild (oItemGroupXmlNode);
			strGroupInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateXML - strGroupInfoXML [OUT] : " + strGroupInfoXML);
	    return strGroupInfoXML;
	}

	private String getItemsXML() 
	{
		m_oLogger.info ("getItemsXML");
		String strXml = "";
	    Iterator<ItemData> oIterator = m_oGroupItems.iterator ();
	    while (oIterator.hasNext ())
	    	strXml += ((ItemData)oIterator.next ()).generateXML ();
	    m_oLogger.debug ( "getItemsXML - strXml [OUT] : " + strXml);
		return strXml;
	}

	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (!m_strGroupName.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strGroupName", m_strGroupName.trim(), MatchMode.ANYWHERE));
		if (m_nItemGroupId > 0)
			oCriteria.add (Restrictions.eq ("m_nItemGroupId", m_nItemGroupId));
		if (m_nItemId >0)
		{
			oCriteria.createAlias("m_oGroupItems", "Items");
			oCriteria.add(Restrictions.eq("Items.m_nItemId", m_nItemId));
		}
		if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria("m_oCreatedBy").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strGroupName");
		return oCriteria;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<ItemGroupData> getItemGroups (ItemData oItemData) throws Exception 
	{
		ItemGroupData oItemGroupData = new ItemGroupData ();
		oItemGroupData.m_nItemId = oItemData.getM_nItemId();
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return new ArrayList(oItemGroupData.list(oOrderBy));
	}

	public static void addToGroup(ItemData oItemData) throws Exception 
	{
		for (int nIndex = 0; oItemData.m_arrItemGroups != null && nIndex < oItemData.m_arrItemGroups.length; nIndex++)
		{
			ItemGroupData oItemGroupData = (ItemGroupData) GenericIDataProcessor.populateObject(oItemData.m_arrItemGroups[nIndex]);
			oItemGroupData.getM_oGroupItems().add(oItemData);
			oItemGroupData.updateObject();
		}
	}

	public static void removeFromGroup(ItemData oItemData) throws Exception 
	{
		ItemData oData = (ItemData) GenericIDataProcessor.populateObject(oItemData);
		Iterator<ItemGroupData> oIterator = oData.getM_oItemGroups().iterator();
		while (oIterator.hasNext())
		{
			ItemGroupData oItemGroupData = oIterator.next();
			remove (oItemGroupData, oItemData);
			oItemGroupData.updateObject();
		}
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) 
	{
		return null;
	}
	
	public static ContactData [] getClientContacts(PubliciseData oPubliciseData) throws Exception 
	{
		if(oPubliciseData.isM_bIsBought())
		{
			SalesData oSalesData = new SalesData ();
			oSalesData.setM_bIsForItemGroupClientProfile(true);
			oSalesData.setM_nItemGroupId(oPubliciseData.getM_nItemGroupId());
			return SalesData.getContactsToPublicise (oPubliciseData, oSalesData);
		}
		else
		{
			PubliciseToNotBoughtClients oPubliciseToNotBoughtClients = new PubliciseToNotBoughtClients ();
			oPubliciseToNotBoughtClients.setM_nItemGroupId(oPubliciseData.getM_nItemGroupId());
			return oPubliciseToNotBoughtClients.getContactsToPublicise (oPubliciseData);
		}
	}
	
	private static void remove(ItemGroupData oItemGroupData, ItemData oItemData) throws CloneNotSupportedException 
	{
		Iterator<ItemData> oItems = oItemGroupData.m_oGroupItems.iterator();
		while (oItems.hasNext())
		{
			ItemData oItem = oItems.next();
			if (oItem.getM_nItemId() == oItemData.getM_nItemId())
			{
				oItemGroupData.m_oGroupItems.remove(oItem);
				break;
			}
		}
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strGroupName.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strGroupName")), m_strGroupName.toLowerCase())); 
		if (m_nItemGroupId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nItemGroupId"), m_nItemGroupId)); 
/*		
  		if (m_nItemId >0)
		{
			oCriteria.createAlias("m_oGroupItems", "Items");
			oCriteria.add(Restrictions.eq("Items.m_nItemId", m_nItemId));
		}
		if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria("m_oCreatedBy").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strGroupName");
*/
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(m_nItemGroupId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nItemGroupId"), m_nItemGroupId));
		return oConjunct;
	}
}
