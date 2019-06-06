var ScholarshipOrganizationProcessor = (function __ScholarshipOrganizationProcessor ()
{
	
	function deleteOrganization (oOrganization,callback)
	{
		ajaxCall(oOrganization, "/orgInfoDelete", callback);
		
	}
	return { 
		
		deleteOrganization : deleteOrganization 
		
	};
})();
