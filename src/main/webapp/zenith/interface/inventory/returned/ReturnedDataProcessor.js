var ReturnedDataProcessor = (function __ReturnedDataProcessor ()
{
	function create (oData,callback)
	{
		ajaxCall(oData,"/returnedCreate",callback);
	}
	
	function deleteData (oData,callback)
	{
		ajaxCall(oData,"/returnedDeleteData",callback);
	}
	
	function get (oData,callback)
	{
		ajaxCall(oData,"/returnedget",callback);
	}
	
	function getXML (oData,callback)
	{
		ajaxXMLCall(oData,"/returnedgetXML",callback);
	}
	
	function update (oData,callback)
	{
		ajaxCall(oData,"/returnedupdate",callback);
	}
	
	function updateAndPrint(oData,callback)
	{
		ajaxCall(oData,"/returnedupdateAndPrint",callback);
	}
	
	function saveAndPrint(oData,callback)
	{
		ajaxCall(oData,"/returnedsaveAndPrint",callback);
	}
	
	function list (oData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oReturnedData:oData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper,"/returnedList",callback);
	}
	
	return {
		create:create,
		list:list,
		deleteData:deleteData,
		get:get,
		getXML:getXML,
		update:update,
		updateAndPrint:updateAndPrint,
		saveAndPrint:saveAndPrint
	}
})();