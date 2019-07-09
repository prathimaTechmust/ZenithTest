var AcademicDetailsDataProcessor = (function __AcademicDetailsDataProcessor ()
{
	
	function uploadDocumentstoS3bucket(oAcademicData,callback)
	{
		multipartAjaxCall(oAcademicData, "/uploadStudentDocuments", callback);   
	}
	
	
	return { 		
		uploadDocumentstoS3bucket :uploadDocumentstoS3bucket		
	};
})();