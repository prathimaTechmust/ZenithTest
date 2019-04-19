var TaxDataProcessor = (function __TaxDataProcessor ()
{
	function create (oTaxData,callback)
	{
		ajaxCall(oTaxData, "/taxCreate", callback);
		
	}
	
	function get(oTaxData,callback)
	{
		ajaxCall(oTaxData, "/taxGet", callback);
		
	}
	
	function list(oTaxData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oTaxData:oTaxData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/taxList", callback);
		
	}
	
	function update (oTaxData,callback)
	{
		ajaxCall(oTaxData, "/taxUpdate", callback);
		
	}
	
	function getXML (oTaxData,callback)
	{
		ajaxXMLCall(oTaxData, "/taxGetXML", callback);
		
	}
	
	function deleteData (oTaxData,callback)
	{
		ajaxCall(oTaxData, "/taxDelete", callback);
		
	}
	
	function getUniqueTaxNames (callback)
	{
	
		$.ajax({
		    url: m_strLocationURL+"/getUniqueTaxNames",
		    beforeSend: function (xhr) {
		    	xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
		    	},
		    type: "POST",
		    async:false, 
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
		deleteData:deleteData,
		getUniqueTaxNames:getUniqueTaxNames
	};
})();