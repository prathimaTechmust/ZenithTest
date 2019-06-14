var VerifiedStudentInformationDataProcessor = (function __VerifiedStudentInformationDataProcessor ()
{
	
	function verified(oVerifiedData,callback)
	{
		ajaxCall(oVerifiedData, "/studentVerifiedInfoUpdate", callback);
	}
	
	function toBeStudentVerifiedlist(oStudentData,callback)
	{
		ajaxCall(oStudentData, "/studentVerifiedInfoList", callback);
	}
	
	return { 		
		verified:verified,
		toBeStudentVerifiedlist :toBeStudentVerifiedlist
		
	};
})();
