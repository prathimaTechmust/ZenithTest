var PropertyTypeDataProcessor = (function __PropertyTypeDataProcessor ()
{
	function create (oPropertyTypeData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyTypeDataCreate",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyTypeData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function get(oPropertyTypeData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyTypeDataGet",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyTypeData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function list(oPropertyTypeData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oPropertyTypeData:oPropertyTypeData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
	
		$.ajax({
		    url: m_strLocationURL+"/propertyTypeDataList",
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
	
	function update (oPropertyTypeData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyTypeDataUpdate",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyTypeData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function getXML (oPropertyTypeData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyTypeDataGetXML",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyTypeData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(strXMLData){
				fn = eval(callback);
				fn(strXMLData);
			    }
		    });
	}
	
	function deleteData (oPropertyTypeData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/propertyTypeDataDelete",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPropertyTypeData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	return {
		create:create,
		update:update,
		list:list,
		get:get,
		getXML:getXML,
		deleteData:deleteData
	};
})();