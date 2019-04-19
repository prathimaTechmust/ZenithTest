package com.techmust.inventory.sales;

import com.techmust.clientmanagement.ClientData;
import com.techmust.inventory.items.ItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ClientArticleData extends ItemData 
{
	private static final long serialVersionUID = 1L;
	private String m_strClientArticleNumber;
	private String m_strClientArticleDescription;
	
	public ClientArticleData()
	{
	}
	
	public ClientArticleData (ItemData oItemData)
	{
		super (oItemData);
	}
	
	public static ClientArticleData getInstance (ClientData oClientData, String strArticleNumber, UserInformationData oCredentials) throws Exception
	{
		ClientArticleData oClientArticleData = new ClientArticleData ();
		CustomizedItemData oCustomItemData = CustomizedItemData.getItemInstance(strArticleNumber, oClientData.getM_nClientId(), -1);
		if (oCustomItemData != null)
		{
			oClientArticleData = new ClientArticleData (oCustomItemData.getM_oItemData());
			oClientArticleData.setM_strClientArticleNumber(oCustomItemData.getM_strClientArticleNumber());
			oClientArticleData.setM_strClientArticleDescription(oCustomItemData.getM_strClientArticleDescription());
		}
		else
		{
			ItemData oData = ItemData.getInstance(strArticleNumber, oCredentials);
			oClientArticleData = new ClientArticleData (oData);
			oClientArticleData.setM_oItemCategoryData(oData.getM_oItemCategoryData());
		}
		return oClientArticleData;
	}

	public void setM_strClientArticleNumber(String strClientArticleNumber) 
	{
		m_strClientArticleNumber = strClientArticleNumber;
	}

	@Override
	public String getM_strArticleNumber() 
	{
		return m_strClientArticleNumber != null && !m_strClientArticleNumber.isEmpty() ? m_strClientArticleNumber : super.getM_strArticleNumber();
	}

	public void setM_strClientArticleDescription(String m_strClientArticleDescription) 
	{
		this.m_strClientArticleDescription = m_strClientArticleDescription;
	}

	@Override
	public String getM_strItemName () 
	{
		return m_strClientArticleDescription != null && !m_strClientArticleDescription.isEmpty() ? m_strClientArticleDescription : super.getM_strItemName();
	}
	
	public String getActualArticleNumber ()
	{
		return super.getM_strArticleNumber();
	}
	
	public String getActualArticleDescription ()
	{
		return super.getM_strItemName();
	}
}
