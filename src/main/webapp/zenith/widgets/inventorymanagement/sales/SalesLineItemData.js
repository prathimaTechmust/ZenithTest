function SalesLineItemData ()
{
	this.m_strArticleNumber = "";
	this.m_nLineItemId = -1;
	this.m_nQuantity = 0;
	this.m_nPrice = 0;
	this.m_nTax = 0;
	this.m_oItemData = new ItemData ();
	this.m_oSalesData = new SalesData ();
	this.m_nMaxResult = 0;
}
dataObjectLoaded ();
