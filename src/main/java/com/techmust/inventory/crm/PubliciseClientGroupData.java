package com.techmust.inventory.crm;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientGroupData;
import com.techmust.clientmanagement.ClientGroupDataProcessor;
import com.techmust.clientmanagement.ContactData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.sales.SalesData;

public class PubliciseClientGroupData extends ClientGroupDataProcessor 
{
	private static final long serialVersionUID = 1L;
	private String m_strFromDate;
	private String m_strToDate;
	private boolean m_bIsBought;
	
	public PubliciseClientGroupData ()
	{
		m_strFromDate = "";
		m_strToDate = "";
		m_bIsBought = false;
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
	
	public boolean isM_bIsBought() 
	{
		return m_bIsBought;
	}

	public void setM_bIsBought(boolean bIsBought) 
	{
		m_bIsBought = bIsBought;
	}
	
	@SuppressWarnings("unchecked")
	public ContactData [] getClientContacts(PubliciseData oPubliciseData) throws Exception 
	{
		ClientGroupData oClientGroupData = new ClientGroupData ();
		oClientGroupData.setM_nGroupId(oPubliciseData.getM_nClientGroupId());
		m_strFromDate = oPubliciseData.getM_strFromDate();
		m_strToDate = oPubliciseData.getM_strToDate();
		m_bIsBought = oPubliciseData.isM_bIsBought();
		return buildClients(new ArrayList (oClientGroupData.listCustomData(this)));
	}

	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, ClientGroupData oClientGroupData) throws RuntimeException 
	{
		if (m_bIsBought)
			buildBoughtListCriteria (oCriteria, oClientGroupData);
		else
			buildNotBoughtListCriteria(oCriteria, oClientGroupData);
		return oCriteria;
	}
	
	private void buildBoughtListCriteria(Criteria oCriteria, ClientGroupData oClientGroupData) 
	{
		DetachedCriteria oSubquery = DetachedCriteria.forClass(SalesData.class).setProjection( Property.forName("m_oClientData"));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oSubquery.add (Restrictions.between ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		oCriteria.add(Restrictions.eq("m_nGroupId", oClientGroupData.getM_nGroupId()));
		oCriteria.createAlias("m_oClientSet", "Clients");
		oCriteria.add(Subqueries.propertyIn("Clients.m_nClientId", oSubquery));
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("Clients.m_nClientId"));
		oCriteria.setProjection(oProjectionList);
	}

	private void buildNotBoughtListCriteria(Criteria oCriteria, ClientGroupData oClientGroupData) 
	{
		DetachedCriteria oSubquery = DetachedCriteria.forClass(SalesData.class)
										.createAlias("m_oClientData", "ClientData")
										.setProjection( Property.forName("ClientData.m_nClientId"));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oSubquery.add (Restrictions.between ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		oCriteria.add(Restrictions.eq("m_nGroupId", oClientGroupData.getM_nGroupId()));
		oCriteria.createAlias("m_oClientSet", "Clients");
		oCriteria.add(Subqueries.propertyNotIn("Clients.m_nClientId", oSubquery));
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("Clients.m_nClientId"));
		oCriteria.setProjection(oProjectionList);
	}
	
	@SuppressWarnings("unchecked")
	private ContactData [] buildClients(ArrayList arrClients) throws Exception 
	{
		ArrayList<ContactData> arrContactList = new ArrayList<ContactData> ();
		for(int nIndex = 0; nIndex < arrClients.size(); nIndex++)
		{
			Integer nClientId = (Integer)arrClients.get(nIndex);
			ClientData oClientdata = new ClientData ();
			oClientdata.setM_nClientId((int) nClientId);
			oClientdata = (ClientData) GenericIDataProcessor.populateObject(oClientdata);
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
