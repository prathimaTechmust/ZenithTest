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

	function reIssueChequeStatusUpdate(oReIssueCheque, callback) 
	{
	    ajaxCall(oReIssueCheque,"/reIssueCheque",callback);
	
	}

	function claimCheque(oClaimCheque,callback)
	{
		ajaxCall(oClaimCheque,"/claimChequeUpdate",callback);
	}
	
	function counselingStatusUpdate(oCounselingStatusUpdate,callback)
	{
		ajaxCall(oCounselingStatusUpdate,"/counselingStatusUpdate",callback);	
	}
	
	function aproveCounselingStudentUpdate(oAproveCounselingStudentUpdate,callback)
	{
		ajaxCall(oAproveCounselingStudentUpdate,"/approveCounselingStudent",callback);
	}
	
	return { 		
		verifiedStatusUpdate:verifiedStatusUpdate,		
		approvedStatusUpdate :approvedStatusUpdate,
		rejectStatusUpdate :rejectStatusUpdate,
		issueCheque:issueCheque,
		reIssueChequeStatusUpdate:reIssueChequeStatusUpdate,
		reVerifiedStatusUpdate :reVerifiedStatusUpdate,
		counselingStatusUpdate:counselingStatusUpdate,
		aproveCounselingStudentUpdate:aproveCounselingStudentUpdate,
		claimCheque:claimCheque

	};
})();
