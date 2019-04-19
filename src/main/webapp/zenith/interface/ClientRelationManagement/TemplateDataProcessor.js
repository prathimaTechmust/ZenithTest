var TemplateDataProcessor = (function __TemplateDataProcessor ()
{
	function create (oTemplateData,callback)
	{
		ajaxCall(oTemplateData, "/templateDataCreate", callback);
	}
	function list(oTemplateData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oTemplateData:oTemplateData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/templateDataList", callback);
			}
	function get(oTemplateData,callback)
	{
		ajaxCall(oTemplateData, "/templateDataGet", callback);
	}
	function getXML (oTemplateData, callback)
	{
		ajaxXMLCall(oTemplateData, "/templateDataGetXML", callback);
	}
	function update (oTemplateData,callback)
	{
		ajaxCall(oTemplateData, "/templateDataUpdate", callback);
	}
	function deleteData (oTemplateData,callback)
	{
		ajaxCall(oTemplateData, "/templateDataDelete", callback);
	}
	
	return { 
		create : create,
		list : list,
		get:get,
		getXML:getXML,
		update:update,
		deleteData:deleteData		
	};
})();