var studentReoprtsInfo_includeDataObjects =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js',
	'widgets/scholarshipmanagement/courselist/CourseInformationData.js',
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js',
	'widgets/usermanagement/facilitator/FacilitatorInformationData.js'
];

includeDataObjects (studentReoprtsInfo_includeDataObjects, "studentReportsInfo_loaded()");

function studentReportsInfo_loaded() 
{
	loadPage("downloadReports/downloadReports.html", "dialog","studentReportsInfo_init()");
}

function studentReportsInfo_init()
{
	createPopup('dialog','#reportsInfo_button_submit','#reportsInfo_button_cancel',true);
	populateReportDropDowns ();
	initFormValidateBoxes ('studentReportsForm');
}	

function populateReportDropDowns () 
{
	populateAcademicYearDropDown ('reportAcademicYear');
	populateCourseNameDropDown ();
	populateInstitutionNameDropDown ();
	populateCityNameDropDown ();
	populateParentOccupationDropDown ();
	populateFacilitatorNameDropDown ();
	populateStudentReligionDropDown ();
	populateStudentGenderDropdown ();
	
}

function studentReportsInfo_submit ()
{
	if(studentReportsValidate ())
	{
		loadPage ("include/process.html", "ProcessDialog", "studentreports_progressbarLoaded ()");
	}		
	else
	{
		alert("Please Select fields");
		$("#studentReportsForm").focus();
	}	
}

function studentreports_progressbarLoaded ()
{
	createPopup('dialog','','',true);
}
function studentReportsValidate ()
{
	return validateForm("studentReportsForm");
}

function studentReportsInfo_cancel ()
{
	HideDialog("dialog");
}

function populateStudentReligionDropDown ()
{
	var religionArray = new Array({religionId:1,religionValue:"Muslim"},{religionId:2,religionValue:"Non-Muslim"},{religionId:3,religionValue:"Memon"});
	/*var dropdown = document.getElementById("studentReport_input_religion");	
	for (var i = 0; i < religionArray.length; ++i)
	{	    
	    dropdown[dropdown.length] = new Option(religionArray[i], religionArray[i]);
	}*/
	$("#studentReport_input_religion").jqxComboBox({   source:religionArray,
													   displayMember:"religionValue",
													   valueMember:"religionValue",
													   autoComplete:true,
													   searchMode :"startswithignorecase",
													   placeHolder:"Select Religion",
													   width :"200px",
													   height:"25px",																				
});
}

function populateStudentGenderDropdown ()
{
	var genderArray = new Array({genderId:1,genderValue:"Male"},{genderId:2,genderValue:"Female"},{genderId:3,genderValue:"Other"});
	/*var dropdown = document.getElementById("studentReport_input_gender");
	for(var i = 0; i < gender.length; ++i)
	{
		 dropdown[dropdown.length] = new Option(gender[i], gender[i]);
    }*/
	$("#studentReport_input_gender").jqxComboBox({  source:genderArray,
												   displayMember:"genderValue",
												   valueMember:"genderValue",
												   autoComplete:true,
												   searchMode :"startswithignorecase",
												   placeHolder:"Select Gender",
												   width :"200px",
												   height:"25px",																				
	  });
}

function populateCourseNameDropDown ()
{
	var oCourseDataObject = new CourseInformationData ();
	CourseInformationDataProcessor.list(oCourseDataObject,"","",1,10,reportCourseNameDropDown);
}

function populateInstitutionNameDropDown ()
{
	var oInstitutionDataObject = new InstitutionInformationData ();
	InstitutionInformationDataProcessor.list(oInstitutionDataObject,"","",1,10,reportInstitutionNameDropDown);
}

function populateCityNameDropDown ()
{
	var oStudentDataObject = new StudentInformationData ();
	StudentInformationDataProcessor.getCityNames(oStudentDataObject,reportCityNameDropDown);
}

function populateParentOccupationDropDown ()
{
	var oStudentObject = new StudentInformationData ();
	StudentInformationDataProcessor.getParentalOccupations(oStudentObject,reportParentOccupationDropDown);
}

function populateFacilitatorNameDropDown ()
{
	var oFacilitatorObject = new FacilitatorInformationData ();
	FacilitatorInformationDataProcessor.list(oFacilitatorObject,"","",1,10,reportFacilitatorNameDropDown);
}

//response functions

function reportCourseNameDropDown (oCourseNamesResponse) 
{
	$(document).ready(function ()
					  {
						   	$("#courseListInfo_input_name").jqxComboBox({  source:oCourseNamesResponse.m_arrCourseInformationData,
						   												   displayMember:"m_strShortCourseName",
						   												   valueMember:"m_nCourseId",
						   												   autoComplete:true,
																		   searchMode :"startswithignorecase",
																		   placeHolder:"Select Course",
																		   width :"200px",
														        		   height:"25px",																				
																	  });
					 });	
}

function reportInstitutionNameDropDown (oInstitutionNamesResponse) 
{
	$(document).ready(function () 
					 {
					      $("#institutionNameInfo_input_name").jqxComboBox({   source:oInstitutionNamesResponse.m_arrInstitutionInformationData,
					    	  												   displayMember:"m_strInstitutionName",
					    	  												   valueMember:"m_nInstitutionId",
					    	  												   autoComplete:true,
														    	               searchMode :"startswithignorecase",
														    	               placeHolder:"Select Institution",
														    	               width:"200px",
														    	               height:"25px",					    	  
					      													});						
					});	
}

function reportCityNameDropDown (oCityNamesResponse)
{
	$(document).ready(function ()
					  {
					       $("#cityListInfo_input_name").jqxComboBox({  source:oCityNamesResponse.m_arrStudentInformationData,
																    	displayMember:"m_strCity",
																		valueMember:"m_strCity",
					    	   											autoComplete:true,
			                                                            searchMode :"startswithignorecase",
			                                                            placeHolder:"Select City",
														                width:"200px",
														                height:"25px",					    	   
					       											});					
					 });	
}


function reportParentOccupationDropDown (oParentalOccupationResponse)
{
	$(document).ready(function ()
					  {
							$("#parentOccupationInfo_input_name").jqxComboBox({	 source:oParentalOccupationResponse.m_arrStudentInformationData,
																				 displayMember:"m_strFatherOccupation",
																				 valueMember:"m_strFatherOccupation",
																				 autoComplete:true,
					                                                             searchMode :"startswithignorecase",
					                                                             placeHolder:"Select Occupation",
																                 width:"200px",
																                 height:"25px",							
																			 });
					  });
}

function reportFacilitatorNameDropDown (oFacilitatorResponse)
{
	$(document).ready(function ()
					  {
							$("#facilitatorListInfo_input_name").jqxComboBox({  source:oFacilitatorResponse.m_arrFacilitatorInformationData,
																				displayMember:"m_strFacilitatorName",
																				valueMember:"m_nFacilitatorId",
																				autoComplete:true,
					                                                            searchMode :"startswithignorecase",
					                                                            placeHolder:"Select Facilitator",
																                width:"200px",
																                height:"25px",								
																		    });
					  });	
}

function studentReportsInfo_Download ()
{
	var oReportsInformation = getReportFormData ();
	StudentInformationDataProcessor.downloadReports(oReportsInformation,downloadReportResponse);
	
}

function getReportFormData ()
{
	var oStudentData = new StudentInformationData ();
	oStudentData.m_nAcademicYearId = $("#reportAcademicYear").val();
	if($("#cityListInfo_input_name").val() != "")
		oStudentData.m_strCity = $("#cityListInfo_input_name").val();
	if($("#courseListInfo_input_name").val() != "")
		oStudentData.m_nCourseId = $("#courseListInfo_input_name").val();
	if($("#facilitatorListInfo_input_name").val() != "")
		oStudentData.m_nFacilitatorId = $("#facilitatorListInfo_input_name").val();
	if($("#parentOccupationInfo_input_name").val() != "")
		oStudentData.m_strFatherOccupation = $("#parentOccupationInfo_input_name").val();
	if($("#institutionNameInfo_input_name").val() != "")
		oStudentData.m_nInstitutionId = $("#institutionNameInfo_input_name").val();
	if($("#studentReport_input_gender").val() != "")
		oStudentData.m_strGender = $("#studentReport_input_gender").val();
	if($("#studentReport_input_religion").val() != "")
		oStudentData.m_strReligion = $("#studentReport_input_religion").val();
	return oStudentData;
}

function downloadReportResponse (oDownloadResponse)
{
	if(oDownloadResponse.m_bSuccess)
	{
		informUser("Reports Downloading","kSuccess");
		var strExcelURL = oDownloadResponse.m_strStudentDownloadReportURL;
		document.getElementById('my_iframe').src = strExcelURL;
		HideDialog("dialog");
	}		
	else
	{
		informUser("Reports Download Failed","kError");
	}
	
}

