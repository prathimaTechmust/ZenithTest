var ContactDataProcessor = (function __ContactDataProcessor ()
{	
	function list(oContactData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oEmailMessageData:oContactData,
				 m_strColumn:strSortColumn,
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/contactDataList", callback);
	}

	
	return { 
		list : list
	};
})();