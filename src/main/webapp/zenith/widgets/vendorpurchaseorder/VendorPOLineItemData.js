function VendorPOLineItemData ()
{
	this.m_nLineItemId = -1;
	this.m_oItemData = new ItemData ();
	this.m_nQuantity = 0;
	this.m_nPrice = 0;
	this.m_nDiscount = 0;
	this.m_nTax = 0;
	this.m_nReceivedQty = 0;
	this.m_nReceiveQty = 0;
}
dataObjectLoaded ();
