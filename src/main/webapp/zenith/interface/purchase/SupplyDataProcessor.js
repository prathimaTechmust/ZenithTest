var SupplyDataProcessor = (function __SupplyDataProcessor ()
{
	function create (oTaxData,callback)
	{
		ajaxCall(oTaxData, "/supplyCreate", callback);
		
	}
	
	return {
		create:create
	}
	
})();