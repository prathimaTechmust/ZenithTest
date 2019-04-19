//var VendorItemDataProcessor= (function __VendorItemDataProcessor ()
//{
//	function create (oVendorItemDataProcessor,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/vendorItemDataCreate",
// beforeSend: function (xhr) {
//		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//		    	},
//		    data: JSON.stringify(oVendorItemDataProcessor),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);
//			    }
//		    });
//	}
//	
//	function get(oVendorItemDataProcessor,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/vendorItemDataGet",
//		    	 beforeSend: function (xhr) {
//				    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//				    	},
//		    data: JSON.stringify(oVendorItemDataProcessor),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);
//			    }
//		    });
//	}
//	
//	function list(oVendorItemData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
//	{
//		var oTradeMustHelper = new TradeMustHelper ();
//		var oTradeMustHelper = {
//				m_oVendorItemData:oVendorItemData,
//				m_strColumn:strSortColumn, 
//				m_strOrderBy:strSortOrder,
//				m_nPageNo: nPageNo,
//				m_nPageSize: nPageSize
//		} 
//		console.log(JSON.stringify(oTradeMustHelper));
//		$.ajax({
//		    url: m_strLocationURL+"/vendorItemDataList",
//		 beforeSend: function (xhr) {
//		xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//		},
//		    data: JSON.stringify(oTradeMustHelper),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);
//			    }
//		    });
//	}
//	
//	function update (oVendorItemDataProcessor,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/vendorItemDataUpdate",
//			beforeSend: function (xhr) {
//			xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//			},
//		    data: JSON.stringify(oVendorItemDataProcessor),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);
//			    }
//		    });
//	}
//	
//	function getXML (oVendorItemDataProcessor,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/vendorItemDataGetXML",
//			beforeSend: function (xhr) {
//			xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//			},
//		    data: JSON.stringify(oVendorItemDataProcessor),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(strXMLData){
//				fn = eval(callback);
//				fn(strXMLData);
//			    }
//		    });
//	}
//	
//	function deleteData (oVendorItemDataProcessor,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/vendorItemDataDelete",
//		beforeSend: function (xhr) {
//		xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//		},
//		    data: JSON.stringify(oVendorItemDataProcessor),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);
//			    }
//		    });
//	}
//	
//	return {
//		create:create,
//		update:update,
//		list:list,
//		get:get,
//		getXML:getXML,
//		deleteData:deleteData
//	};
//})();