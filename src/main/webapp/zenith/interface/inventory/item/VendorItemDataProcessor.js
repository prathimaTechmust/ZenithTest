var VendorItemDataProcessor = (function __VendorItemDataProcessor ()
{
	
	function list(oItemData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oItemData:oItemData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		console.log( JSON.stringify(oTradeMustHelper));
		ajaxCall(oTradeMustHelper, "/vendoritemDataList", callback);
	}










})();