function NonStockPurchaseLineItem ()
{
	this.m_nLineItemId = -1;
	this.m_strArticleDescription = "";
	this.m_nPrice = 0;
	this.m_nDiscount = 0;
	this.m_nExcise = 0;
	this.m_nTax = 0;
	this.m_nOtherCharges = 0;
	this.m_dCreatedOn = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oPurchaseData = new PurchaseData ();
}

dataObjectLoaded ();