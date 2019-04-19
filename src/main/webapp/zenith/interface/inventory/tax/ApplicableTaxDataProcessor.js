var ApplicableTaxDataProcessor = (function __ApplicableTaxDataProcessor ()
{
	function create (oApplicableTaxData,callback)
	{
		 ajaxCall(oApplicableTaxData, "/applicableTaxCreate", callback);
		
	}
	
	function get(oApplicableTaxData,callback)
	{
		ajaxCall(oApplicableTaxData, "/applicableTaxGet", callback);
		
	}
	
	function list(oApplicableTaxData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oApplicablTax:oApplicableTaxData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/applicableTaxList", callback);
		
	}
	
	function update (oApplicableTaxData,callback)
	{
		ajaxCall(oApplicableTaxData, "/applicableTaxUpdate", callback);
		
	}
	
	function getXML (oApplicableTaxData,callback)
	{
		ajaxXMLCall(oApplicableTaxData, "/applicableTaxGetXML", callback);
		
	}
	
	function deleteData (oApplicableTaxData,callback)
	{
		ajaxCall(oApplicableTaxData, "/applicableTaxDelete", callback);
		
	}
	
	function listApplicableTaxData (oApplicableTaxData, callback)
	{
		ajaxCall(oApplicableTaxData, "/listApplicableTax", callback);
		
	}
	
	return {
		create:create,
		update:update,
		list:list,
		get:get,
		getXML:getXML,
		deleteData:deleteData,
		listApplicableTaxData:listApplicableTaxData
	};
})();