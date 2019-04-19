var MessageDataProcessor = (function __MessageDataProcessor ()
{
	function create (oEmailMessageData,callback)
	{
		 ajaxCall(oEmailMessageData, "/messageDataCreate", callback);
	}

	
	function list(oEmailMessageData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oEmailMessageData:oEmailMessageData,
				 m_strColumn:strSortColumn,
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/messageDataList", callback);
	}

	
	return { 
		create : create,
		list : list
	};
})();