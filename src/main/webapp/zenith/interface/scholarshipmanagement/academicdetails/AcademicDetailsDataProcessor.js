var AcademicDetailsDataProcessor = (function __AcademicDetailsDataProcessor ()
{
	
	function uploadDocumentstoS3bucket(oAcademicData,callback)
	{
		multipartAjaxCall(oAcademicData, "/uploadStudentDocuments", callback);   
	}
	
	function getStudentDocuments(oStudentDocuments,callback)
	{
		ajaxCall(oStudentDocuments,"/getStudentUploadedDocuments",callback);
	}
	
	
	return { 		
		uploadDocumentstoS3bucket :uploadDocumentstoS3bucket,
		getStudentDocuments :getStudentDocuments
	};
})();