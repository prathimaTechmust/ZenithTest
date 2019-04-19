var CustomizedItemDataProcessor = (function __CustomizedItemDataProcessor()
{
// 	function create (oEmailMessageData,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/CustomizedItemData",
//		    data: JSON.stringify(oEmailMessageData),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);
//			    }
//		    });
//	}

	
	function list(oCustomizedItemData, strColumn, strOrderBy, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oCustomizedItemData:oCustomizedItemData,
				 m_strColumn:strColumn,
				 m_strOrderBy:strOrderBy
		} 
		 ajaxCall(oTradeMustHelper, "/CustomizedItemDataList", callback);
	}

	
	return { 
// 		create : create,
		list : list
	};
})();