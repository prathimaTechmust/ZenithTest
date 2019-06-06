var AcademicYearProcessor = (function __AcademicYearProcessor ()
{
	function create(oAcademicYear,callback)
	{
		 ajaxCall(oAcademicYear, "/academicyearcreate", callback);
	}
	
	function update(oAcademicYear,callback)
	{
		ajaxCall(oAcademicYear, "/academicyearupdate", callback);		
	}
	
	function list(oAcademicYear, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		 ajaxCall(oAcademicYear, "/academicyearInfoList", callback);		 
	}		
	
	return { 
		create:create,
		update:update,
		list : list	
	};
})();
