var PurchaseOrderDataProcessor = (function _PurchaseOrderDataProcessor ()
{
	function create (oPurchaseOrderData,callback)
	{
		ajaxCall(oPurchaseOrderData,"/PurchaseOrderDataCreate",callback);
	}
	
	function get(oPurchaseOrderData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataGet",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	
	function list(oPurchaseOrderData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oPurchaseOrderData:oPurchaseOrderData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataList",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oTradeMustHelper),
		    type: "POST",
		    contentType: "application/json",
		    async:false,
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    },
		error:function(xhr, textStatus, errorThrown){
			    	console.log(xhr );
			    	console.log(textStatus);
			    	console.log(errorThrown);
			    	
			    }
		    });
	}
	
	function update (oPurchaseOrderData, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataUpdate",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function deleteData (oPurchaseOrderData, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataDelete",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function getXML (oPurchaseOrderData, callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataGetXML",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    aynsc:false,
		    success: function(strXML){
				fn = eval(callback);
				fn(strXML);
			    }
		    });                                
	}
	
	function deletePurchaseOrderLineItems(oPurchaseOrderData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataDeletePurchaseOrderLineItems",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function getPurchases(oPurchaseOrderData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataGetPurchases",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function cancelOrder(oPurchaseOrderData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataCancelOrder",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function reallow(oPurchaseOrderData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataReallow",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	function validateOrderNumber(oPurchaseOrderData,callback)
	{
		$.ajax({
		    url: m_strLocationURL+"/PurchaseOrderDataValidateOrderNumber",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    data: JSON.stringify(oPurchaseOrderData),
		    type: "POST",
		    contentType: "application/json",
		    success: function(oResponse){
				fn = eval(callback);
				fn(oResponse);
			    }
		    });
	}
	
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getXML : getXML,
		deletePurchaseOrderLineItems : deletePurchaseOrderLineItems,
		getPurchases : getPurchases,
		cancelOrder : cancelOrder,
		reallow : reallow,
		validateOrderNumber : validateOrderNumber
		
	};
})();