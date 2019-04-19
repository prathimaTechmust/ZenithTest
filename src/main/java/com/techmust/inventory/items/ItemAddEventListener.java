package com.techmust.inventory.items;

import com.techmust.generic.listener.ITradeMustEventListener;

public class ItemAddEventListener extends Thread implements ITradeMustEventListener<ItemData>
{
    public void handle(String strEventName, ItemData oItemData)
    {
		if (strEventName.equals(ITradeMustEventListener.kNew))
		{
			Thread.yield();
			if (oItemData.m_arrItemGroups.length > 0)
			{
				return;
			}
		}
    }
	
	public void register (ItemData oItemData)
	{
		oItemData.addListener(this);
	}
}
