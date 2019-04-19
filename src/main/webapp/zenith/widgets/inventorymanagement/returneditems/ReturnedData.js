function ReturnedData ()
{
	this.m_nId = -1;
	this.m_oClientData = new ClientData ();
	this.m_oCreatedBy = new UserInformationData ();
	this.m_arrReturnedLineItemData = new Array ();
	this.m_arrNonStockReturnedLineItemData = new Array ();
	this.m_strCreditNoteNumber = "";
}

dataObjectLoaded ();