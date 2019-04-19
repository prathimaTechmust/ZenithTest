var QuotationDataProcessor = (function __QuotationDataProcessor ()
{	
	function list(oQuotationData, strSortColumn, strSortColumn, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oQuotationData:oQuotationData,
				 m_strColumn:strSortColumn,
				 m_strOrderBy:strSortColumn,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/quotationDataList", callback);
	}	
	
	function create (oQuotationData,callback)
	{
		ajaxCall(oQuotationData, "/quotationDatacreate", callback);
	}
	
	function deleteData (oQuotationData, callback)
	{
		ajaxCall(oQuotationData, "/quotationDatadeleteData", callback);
	}
	
	function get(oQuotationData,callback)
	{
		ajaxCall(oQuotationData, "/quotationDataget", callback);
	}

	function update (oQuotationData, callback)
	{
		ajaxCall(oQuotationData, "/quotationDataupdate", callback);
	}
	
	function getXML (oQuotationData, callback)
	{
		ajaxXMLCall(oQuotationData, "/quotationDatagetXML", callback);
	}
	
	function archive (oQuotationData, callback)
	{
		ajaxCall(oQuotationData, "/quotationDataarchive", callback);
	}
	
	function unArchive (oQuotationData, callback)
	{
		ajaxCall(oQuotationData, "/quotationDataunArchive", callback);
	}
	
	function sendMail (oQuotationData, callback)
	{
		ajaxCall(oQuotationData, "/quotationDatasendMail", callback);
	}
	
	function saveAndPrint (oQuotationData, callback)
	{
		ajaxCall(oQuotationData, "/quotationDatasaveAndPrint", callback);
	}
	
	function saveAndSendMail (oQuotationData, callback)
	{
		ajaxCall(oQuotationData, "/quotationDatasaveAndSendMail", callback);
	}
	
	return { 
		list : list,
		create:create,
		deleteData:deleteData,
		get:get,
		update:update,
		getXML:getXML,
		archive:archive,
		unArchive:unArchive,
		sendMail:sendMail,
		saveAndPrint:saveAndPrint,
		saveAndSendMail:saveAndSendMail
	};
})();