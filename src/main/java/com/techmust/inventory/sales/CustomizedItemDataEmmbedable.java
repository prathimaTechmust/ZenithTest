package com.techmust.inventory.sales;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CustomizedItemDataEmmbedable implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "ac50_Client_id")
    private int clientDataId;
	
	@Column(name = "ac50_item_id")
	private int itemId;
	
	
	public CustomizedItemDataEmmbedable()
	{
	}
	
	
	public CustomizedItemDataEmmbedable(int clientDataId, int itemId) 
	{
		this.clientDataId = clientDataId;
		this.itemId = itemId;
	}

	public int getClientDataId()
	{
		return clientDataId;
	}

	public void setClientDataId(int clientDataId)
	{
		this.clientDataId = clientDataId;
	}

	public int getItemId()
	{
		return itemId;
	}

	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + clientDataId;
		result = prime * result + itemId;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomizedItemDataEmmbedable other = (CustomizedItemDataEmmbedable) obj;
		if (clientDataId != other.clientDataId)
			return false;
		if (itemId != other.itemId)
			return false;
		return true;
	}	
}
