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
	
	function sendSMSAndMail(oSMSSentData,callback)
	{
		ajaxCall(oSMSSentData,"/sendSMSAndMailNotification",callback);		
	}
	
	return {		
		create:create,
		list :list,
		sendSMSAndMail:sendSMSAndMail,
		getXML :getXML
	};
})();