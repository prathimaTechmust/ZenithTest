package com.techmust.inventory.sales;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DiscountStructureDataEmmbedable implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name ="ac3X_client_group_id")
    private int clientGroudId;
	@Column(name = "ac3X_item_group_id")
	private int itemGroupId;
	
	public DiscountStructureDataEmmbedable()
	{
	}
	
	public DiscountStructureDataEmmbedable(int clientGroudId, int itemGroupId)
	{
		this.clientGroudId = clientGroudId;
		this.itemGroupId = itemGroupId;
	}

	public int getClientGroudId() 
	{
		return clientGroudId;
	}

	public void setClientGroudId(int clientGroudId)
	{
		this.clientGroudId = clientGroudId;
	}

	public int getItemGroupId()
	{
		return itemGroupId;
	}

	public void setItemGroupId(int itemGroupId) 
	{
		this.itemGroupId = itemGroupId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + clientGroudId;
		result = prime * result + itemGroupId;
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
		DiscountStructureDataEmmbedable other = (DiscountStructureDataEmmbedable) obj;
		if (clientGroudId != other.clientGroudId)
			return false;
		if (itemGroupId != other.itemGroupId)
			return false;
		return true;
	}	
}
