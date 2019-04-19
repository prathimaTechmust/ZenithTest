var TemplateDataProcessor = (function __TemplateDataProcessor()
{ 
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
		 ajaxCall(oTradeMustHelper,"/templateDataList",callback);
	}

	return {
	     list:list
          }; 
	
})();