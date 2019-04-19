function PurchaseReturnedData ()
{
	this.m_nId = -1;
	this.m_oVendorData = new VendorData ();
	this.m_oCreatedBy = new UserInformationData ();
	this.m_arrPurchaseReturnedLineItemData = new Array ();
	this.m_strDebitNoteNumber = "";
}

dataObjectLoaded ();