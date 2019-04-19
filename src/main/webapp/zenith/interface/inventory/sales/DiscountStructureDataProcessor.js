var DiscountStructureDataProcessor = (function __DiscountStructureDataProcessor()
{
	function create (oDiscountData,callback)
	{
		 ajaxCall(oDiscountData, "/discountStructureCreate", callback);
	}
	
	
	function list(oDiscountData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oDiscountStructureData:oDiscountData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
			 ajaxCall(oTradeMustHelper, "/discountStructureList", callback);
	}
	
	function getDiscount (oClientData, oItemData, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oClientData:oClientData,
				 m_oItemData:oItemData
		} 
			 ajaxCall(oTradeMustHelper, "/getDiscount", callback);
	}
	
	function save (arrDiscountStructureData, callback)
	{
		var arrDiscountData = arrDiscountStructureData;
		 ajaxCall(arrDiscountStructureData, "/discountStructureSave", callback);
	}
	
	return {
		create :create,
	     list:list,
	     getDiscount:getDiscount,
	     save:save
   }; 
})();