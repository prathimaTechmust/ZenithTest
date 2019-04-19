var PurchaseReturnedDataProcessor = (function __PurchaseReturnedDataProcessor ()
{
	function create (oData,callback)
	{
		ajaxCall(oData, "/purchaseReturnedCreate", callback);
	}
	
	function getXML(oData, callback)
	{
		ajaxXMLCall(oData, "/purchaseReturnedGetXML", callback);
	}
	
	function list(oData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oPReturnedData:oData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/purchaseReturnedList", callback);
	}
	
	function update (oData,callback)
	{
		ajaxCall(oData, "/purchaseReturnedUpdate", callback);
	}
	
	function deleteData (oData,callback)
	{
		ajaxCall(oData, "/purchaseReturnedDelete", callback);
	}
	
	function get(oData,callback)
	{
		ajaxCall(oData, "/purchaseReturnedGet", callback);
	}
	
	function updateAndPrint(oData,callback)
	{
		ajaxCall(oData, "/updateAndPrint", callback);
	}
	
	function saveAndPrint(oData,callback)
	{
		ajaxCall(oData, "/saveAndPrint", callback);
	}
	
	return {
		create:create,
		getXML:getXML,
		list:list,
		update:update,
		get:get,
		deleteData:deleteData,
		updateAndPrint:updateAndPrint,
		saveAndPrint:saveAndPrint
	}
})();