function StockTransferMemoData ()
{
	this.m_nStockTransferMemoId = -1;
	this.m_oTransferredFrom = new LocationData ();
	this.m_oTransferredTo = new LocationData ();
}

dataObjectLoaded ();