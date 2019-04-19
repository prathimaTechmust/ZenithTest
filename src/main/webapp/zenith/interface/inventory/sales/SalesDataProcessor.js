var SalesDataProcessor = (function __SalesDataProcessor()
{
	function create (oSalesData,callback)
	{
		ajaxCall(oSalesData,"/SalesDataCreate",callback);
		
	}
	
	function getXML (oSalesData,callback)
	{
		ajaxXMLCall(oSalesData,"/salesGetXML",callback);
	}
	
	function get (oSalesData,callback)
	{
		ajaxCall(oSalesData,"/salesGet",callback);
	}
	
	function saveAndPrint (oSalesData,callback)
	{
		ajaxCall(oSalesData,"/salesSaveAndPrint",callback);
	}
	
	function getClientItem (oSalesData,callback)
	{
		ajaxCall(oSalesData,"/salesgetClientItem",callback);
	}
	
	function getClientReport (oSalesData, strColumn, strOrderBy, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oSalesData:oSalesData,
				 m_strColumn:strColumn, 
				 m_strOrderBy:strOrderBy
		} 
		 ajaxCall(oTradeMustHelper,"/salesgetClientReport",callback);
	}
	
	function update (oSalesData,callback)
	{
		ajaxCall(oSalesData,"/salesDataUpdate",callback);
	}
	
	function updateAndPrint (oSalesData,callback)
	{
		ajaxCall(oSalesData,"/salesUpdateAndPrint",callback);
	}
	
	
	function list(oSalesData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oSalesData:oSalesData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper,"/salesDataList",callback);
	}
	
	function listClientSales (oSalesData, strColumn, strOrderBy, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oSalesData:oSalesData,
				 m_strColumn:strColumn, 
				 m_strOrderBy:strOrderBy
		} 
		 ajaxCall(oTradeMustHelper,"/saleslistClientSales",callback);
	}
	
	return {
		create :create,
	     list:list,
	     getXML:getXML,
	     get:get,
	     saveAndPrint:saveAndPrint,
	     getClientItem:getClientItem,
	     getClientReport:getClientReport,
	     update:update,
	     updateAndPrint:updateAndPrint,
	     listClientSales:listClientSales
   }; 
})();