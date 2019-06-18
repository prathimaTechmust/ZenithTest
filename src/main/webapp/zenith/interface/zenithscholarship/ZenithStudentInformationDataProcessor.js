var ZenithStudentInformationDataProcessor = (function __ZenithStudentInformationDataProcessor ()
{
	
	function verifiedStatusUpdate(oVerifiedData,callback)
	{
		ajaxCall(oVerifiedData, "/studentStatusInfoUpdate", callback);
	}
	
	function approvedStatusUpdate(oApprovedData,callback)
	{
		ajaxCall(oApprovedData, "/studentApprovedStatusInfoUpdate", callback);
	}
	
	function rejectStatusUpdate(oRecjectedData,callback)
	{
		
		ajaxCall(oRecjectedData,"/studentRejectedStatusUpdate",callback);
	}
	
	return { 		
		verifiedStatusUpdate:verifiedStatusUpdate,		
		approvedStatusUpdate :approvedStatusUpdate,
		rejectStatusUpdate :rejectStatusUpdate
	};
})();
