package com.techmust.inventory.items;

import com.techmust.generic.listener.ITradeMustEventListener;

public class ItemGroupAddEventListener extends Thread implements  ITradeMustEventListener<ItemGroupData>
{

    public void handle(String strEventName, ItemGroupData oItemGroupData)
    {
	    // TODO Auto-generated method stub
		strEventName = strEventName;
    }

    public void register(ItemGroupData oItemGroupData)
    {
	    // TODO Auto-generated method stub
		oItemGroupData.addListener(this);
    }

}
