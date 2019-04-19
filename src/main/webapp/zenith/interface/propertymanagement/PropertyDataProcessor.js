var PropertyDataProcessor = (function __PropertyDataProcessor ()
{
	function create (oPropertyData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyDataCreate",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function get(oPropertyData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyDataGet",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function list(oPropertyData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oPropertyTypeData:oPropertyData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 console.log(JSON.stringify(oTradeMustHelper));
		$.ajax({
		    url: m_strLocationURL+"/propertyDataList",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oTradeMustHelper),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function update (oPropertyData, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyDataUpdate",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function deleteData (oPropertyData, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyDataDelete",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function getXML (oPropertyData, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyDataGetXML",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(strXML){
				fn = eval(callback);
				fn(strXML, oRoleData);
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