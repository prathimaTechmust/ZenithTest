var ZenithStudentInformationDataProcessor = (function __ZenithStudentInformationDataProcessor ()
{
	
	function verifiedStatusUpdate(oVerifiedFormData,callback)
	{
		multipartAjaxCall(oVerifiedFormData, "/studentStatusInfoUpdate", callback);
	}
	
	function approvedStatusUpdate(oApprovedData,callback)
	{
		ajaxCall(oApprovedData, "/studentApprovedStatusInfoUpdate", callback);
	}
	
	function rejectStatusUpdate(oRecjectedData,callback)
	{
		
		ajaxCall(oRecjectedData,"/studentRejectedStatusUpdate",callback);
	}
	
	function issueCheque(oChequeData,callback)
	{
		ajaxCall(oChequeData,"/studentIssueCheque",callback);
	}
	
	function reVerifiedStatusUpdate (oReVerification,callback)
	{
		
		ajaxCall(oReVerification,"/reverifyapplication",callback);
	}
	
	return { 		
		verifiedStatusUpdate:verifiedStatusUpdate,		
		approvedStatusUpdate :approvedStatusUpdate,
		rejectStatusUpdate :rejectStatusUpdate,
		issueCheque:issueCheque,
		reVerifiedStatusUpdate :reVerifiedStatusUpdate
	};
})();
