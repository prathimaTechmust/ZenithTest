var POChallanDataProcessor = (function _POChallanDataProcessor ()
{
	function createChallan (oPOChallanData,callback)
	{
		ajaxCall(oPOChallanData, "/oPOChallanDatacreateChallan", callback);
		
	}
	
	function updateAndMakeChallan (oPOChallanData,callback)
	{
		ajaxCall(oPOChallanData, "/oPOChallanDataupdateAndMakeChallan", callback);
		
	}
	function getChallan(oPOChallaneData,callback)
	{
		ajaxCall(oPOChallaneData, "/oPOChallanDatagetChallan", callback);
		
	}
	function unBilledChallan(oPOChallaneData,callback)
	{
		
			ajaxCall(oPOChallaneData, "/oPOChallanDataunBilledChallanChallan", callback);
		   
	}
//	function deleteChallan(oPOChallaneData,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/oPOChallanDatadeleteChallan",
//	 beforeSend: function (xhr) {
//	    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//	    	},
//	    	data: JSON.stringify(oPOChallanData),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);			    }
//		    });
//	}
//	function getXMLChallan(oPOChallaneData,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/oPOChallanDatagetXMLChallan",
//	 beforeSend: function (xhr) {
//	    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//	    	},
//	    	data: JSON.stringify(oPOChallanData),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);			    }
//		    });
//	}
//	function updateChallan(oPOChallaneData,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/oPOChallanDataupdateChallan",
//	 beforeSend: function (xhr) {
//	    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//	    	},
//	    	data: JSON.stringify(oPOChallanData),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);			    }
//		    });
//	}
//	function deletePurchaseOrderLineItemChallan(oPOChallaneData,callback)
//	{
//		$.ajax({
//		    url: m_strLocationURL+"/oPOChallanDatadeletePurchaseOrderLineItemChallanChallan",	
//	 beforeSend: function (xhr) {
//	    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
//	    	},
//	    	data: JSON.stringify(oPOChallanData),
//		    type: "POST",
//		    contentType: "application/json",
//		    success: function(oResponse){
//				fn = eval(callback);
//				fn(oResponse);			    }
//		    });
//	}
//	
//	
	
	return { 
		createChallan : createChallan,
		updateAndMakeChallan : updateAndMakeChallan,
		getChallan : getChallan,
		unBilledChallan:unBilledChallan
//		updateChallan:updateChallan,
//		deletePurchaseOrderLineItemChallan:deletePurchaseOrderLineItemChallan,
//		deleteChallan:deleteChallan,
//		getXMLChallan:getXMLChallan,
//		listChallan:listChallan
		
	};
})();
