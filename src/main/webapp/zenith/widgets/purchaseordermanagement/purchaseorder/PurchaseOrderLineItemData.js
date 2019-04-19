function PurchaseOrderLineItemData ()
{
	this.m_nId = -1;
	this.m_strDesc = "";
	this.m_nQty = 0;
	this.m_nPrice = 0;
	this.m_nTax = 0;
	this.m_nShippedQty = 0;
	this.m_arrPurchaseOrderStockLineItems = new Array ();
}
dataObjectLoaded ();
