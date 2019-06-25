var StudentScholarshipAccountsProcessor = (function __StudentScholarshipAccountsProcessor ()
{	
	function create(oStudentAccount,callback)
	{
		ajaxCall(oStudentAccount, "/studentAccountDataCreate", callback);
	}
	
	function list(oStudentAccountlist,strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		ajaxCall(oStudentAccountlist, "/studentAccountlist", callback);
	}
	
	function getXML(oAccountData,callback)
	{
		
		ajaxXMLCall(oAccountData,"/studentAccountXML",callback);
	}
	
	return {		
		create:create,
		list :list,
		getXML :getXML
	};
})();