package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientDataProcessor;
import com.techmust.clientmanagement.ContactData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.crm.PubliciseData;

public class PubliciseToNotBoughtClients extends ClientDataProcessor
{
	private int m_nItemGroupId;
	private int m_nItemId;
	private String m_strFromDate;
	private String m_strToDate;
	
	public PubliciseToNotBoughtClients ()
	{
		setM_nItemGroupId(-1);
		setM_nItemId(-1);
		setM_strFromDate ("");
		setM_strToDate (""); 
	}
	
	public void setM_nItemGroupId(int nItemGroupId) 
	{
		this.m_nItemGroupId = nItemGroupId;
	}

	public int getM_nItemGroupId() 
	{
		return m_nItemGroupId;
	}
	
	public int getM_nItemId() 
	{
		return m_nItemId;
	}

	public void setM_nItemId(int nItemId) 
	{
		m_nItemId = nItemId;
	}
	
	public void setM_strFromDate (String strFromDate)
	{
		this.m_strFromDate = strFromDate;
	}

	public String getM_strFromDate () 
	{
		return m_strFromDate;
	}

	public void setM_strToDate (String strToDate) 
	{
		this.m_strToDate = strToDate;
	}

	public String getM_strToDate ()
	{
		return m_strToDate;
	}
	
	@SuppressWarnings("unchecked")
	public ContactData[] getContactsToPublicise(PubliciseData oPubliciseData) throws Exception 
	{
		ArrayList<ClientData> arrClientList = SalesData.buildClientList (oPubliciseData.m_arrClientGroup);
		setM_strFromDate(oPubliciseData.getM_strFromDate());
		setM_strToDate(oPubliciseData.getM_strToDate());
		ClientData oClientData = new ClientData ();
		return getClients(new ArrayList (oClientData.listCustomData(this)), arrClientList);
	}
	
	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, ClientData oClientData) throws RuntimeException 
	{
		oCriteria.add ((Restrictions.eq ("class", kClient)));
		if(m_nItemGroupId > 0)
			buildItemGroupListCriteria(oCriteria);
		if(m_nItemId > 0)
			buildItemListCriteria(oCriteria);
		return oCriteria;
	}
	
	private void buildItemGroupListCriteria(Criteria oCriteria) 
	{
		DetachedCriteria oSubquery = DetachedCriteria.forClass(SalesData.class).setProjection( Property.forName("m_oClientData"));
		DetachedCriteria oSalesLineItemDataCriteria = oSubquery.createCriteria("m_oSalesLineItems", "saleslineitems");
		DetachedCriteria oItemCriteria = oSalesLineItemDataCriteria.createCriteria("m_oItemData", "itemdata");
		DetachedCriteria oItemGroupCriteria = oItemCriteria.createCriteria("m_oItemGroups", "itemgroupdata");
		oItemGroupCriteria.add(Restrictions.eq("m_nItemGroupId", m_nItemGroupId));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oSubquery.add (Restrictions.between ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		oCriteria.add(Subqueries.propertyNotIn("m_nClientId", oSubquery));
	}

	private void buildItemListCriteria(Criteria oCriteria) 
	{
		DetachedCriteria oSubquery = DetachedCriteria.forClass(SalesData.class).setProjection( Property.forName("m_oClientData"));
		DetachedCriteria oSalesLineItemDataCriteria = oSubquery.createCriteria("m_oSalesLineItems", "saleslineitems");
		DetachedCriteria oItemCriteria = oSalesLineItemDataCriteria.createCriteria("m_oItemData", "itemdata");
		oItemCriteria.add(Restrictions.eq("m_nItemId", m_nItemId));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oSubquery.add (Restrictions.between ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		oCriteria.add(Subqueries.propertyNotIn("m_nClientId", oSubquery));
	}
	
	@SuppressWarnings("unchecked")
	private static ContactData [] getClients(ArrayList arrClients, ArrayList<ClientData> arrClientList) 
	{
		ArrayList<ContactData> arrContactList = new ArrayList<ContactData> ();
		for(int nIndex = 0; nIndex < arrClients.size(); nIndex++)
		{
			ClientData oClientdata = (ClientData) arrClients.get(nIndex);
			if(SalesData.isClientExist (oClientdata, arrClientList))
				buildContacts (arrContactList, oClientdata);
		}
		return arrContactList.toArray(new ContactData[arrContactList.size()]);
	}
	
	private static void buildContacts(ArrayList<ContactData> arrContactList, ClientData oClientdata) 
	{
		Iterator<ContactData> oContactsSet = oClientdata.getM_oContacts().iterator();
		while (oContactsSet.hasNext())
		{
			arrContactList.add(oContactsSet.next());
		}
	}
}
