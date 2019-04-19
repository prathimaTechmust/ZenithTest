var SerialNumberDataProcessor = (function _SerialNumberDataProcessor()
{
	function create (oSerialNumber,callback)
	{
		 ajaxCall(oSerialNumber, "/SerialNumberCreate", callback);
	}	
	function get(oSerialNumber,callback)
	{    
		 ajaxCall(oSerialNumber, "/SerialNumberGet", callback);
	}
	function list(oSerialNumber, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oSerialNumber:oSerialNumber,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/SerialNumberList", callback);
	}
	
	function update (oSerialNumber, callback)
	{
		 ajaxCall(oSerialNumber, "/SerialNumberUpdate", callback);
	}
	
	function deleteData (oSerialNumber, callback)
	{
		 ajaxCall(oSerialNumber, "/SerialNumberDelete", callback);
	}
	
	function getXML (oSerialNumber, callback)
	{
		ajaxXMLCall(oSerialNumber, "/SerialNumberGetXML", callback);
	}
	
	
	return { 
	    create : create,
		get : get,
    	list : list,
		update :update,
		deleteData : deleteData, 
		getXML : getXML,
	};
})();