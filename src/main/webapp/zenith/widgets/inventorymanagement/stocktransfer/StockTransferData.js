function StockTransferData ()
{
	this.m_nStockTransferID = -1;
	this.m_oItemData = new ItemData();
	this.m_oTransferredFrom = new LocationData;
	this.m_oTransferredTo = new LocationData;
	this.m_nQuantity = 0;
	this.m_oTransferredBy = new UserInformationData ();
	this.m_arrStockTransferLineItem = new Array ();
}

dataObjectLoaded ();