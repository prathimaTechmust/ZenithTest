var ReceiptDataProcessor = (function __ReceiptDataProcessor ()
{
	function create (oReceiptData,callback)
	{
		ajaxCall(oReceiptData, "/ReceiptDataCreate", callback);
	}
	
	function get(oReceiptData,callback)
	{
		ajaxCall(oReceiptData, "/ReceiptDataGet", callback);
	}
	
	function list(oReceiptData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oReceiptData:oReceiptData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/ReceiptDataList", callback);
	}
	
	function update (oReceiptData, callback)
	{
		ajaxCall(oReceiptData, "/ReceiptDataUpdate", callback);
	}
	
	function deleteData (oReceiptData, callback)
	{
		ajaxCall(oReceiptData, "/ReceiptDataDelete", callback);
	}
	
	function getXML (oReceiptData, callback)
	{
		ajaxXMLCall(oReceiptData, "/ReceiptDataGetXML", callback);
	}
	
	function saveAndPrint(oReceiptData,callback)
	{
		ajaxCall(oReceiptData, "/ReceiptDataSaveAndPrint", callback);
	}
	function getReports(oReceiptData,callback)
	{
		ajaxCall(oReceiptData, "/ReceiptDatagetReports", callback);
	}
	function exportToTally(oReceiptData,callback)
	{
		ajaxCall(oReceiptData, "/ReceiptDataexportToTally", callback);
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getXML : getXML,
		getReports:getReports,
		exportToTally:exportToTally,
		saveAndPrint : saveAndPrint
	};
})();