var TransactionModeDataProcessor = (function __TransactionModeDataProcessor ()
{
	function create (oTransactionMode,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/TransactionModeCreate",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oTransactionMode),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function get(oTransactionMode,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/TransactionModeGet",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oTransactionMode),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	
	function list(oTransactionMode, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oTransactionMode:oTransactionMode,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		$.ajax({
		    url: m_strLocationURL+"/TransactionModeList",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oTradeMustHelper),
		    type: "POST",
		    contentType: "application/json",
		    async: false,
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function update (oTransactionMode, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/TransactionModeUpdate",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oTransactionMode),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function deleteData (oTransactionMode, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/TransactionModeDelete",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oTransactionMode),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function getXML (oTransactionMode, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/TransactionModeGetXML",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oTransactionMode),
		    type: "POST",
		    contentType: "application/json",
		    success: function(strXML){
				fn = eval(callback);
				fn(strXML);
			    } 
		    });                                
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getXML : getXML
	};
})();