var ZenithStudentInformationDataProcessor = (function __ZenithStudentInformationDataProcessor ()
{
	
	function verifiedStatusUpdate(oVerifiedData,callback)
	{
		ajaxCall(oVerifiedData, "/studentStatusInfoUpdate", callback);
	}
	
	function approvedStatusUpdate(oVerifiedData,callback)
	{
		ajaxCall(oVerifiedData, "/studentApprovedStatusInfoUpdate", callback);
	}
	
	
	return { 		
		verifiedStatusUpdate:verifiedStatusUpdate,		
		approvedStatusUpdate :approvedStatusUpdate
	};
})();
