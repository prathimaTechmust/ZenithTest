function NonStockSalesLineItemData ()
{
	this.m_nLineItemId = -1;
	this.m_strArticleDescription = "";
	this.m_nQuantity = "";
	this.m_nPrice = "";
	this.m_nDiscount = 0;
	this.m_nTax = 0;
	this.m_strTaxName = 0;
	this.m_oCreatedBy = new UserInformationData ();
//	this.m_oSalesData = new SalesData ();
	this.m_oSupplyData = new SupplyData ();
}
dataObjectLoaded ();
