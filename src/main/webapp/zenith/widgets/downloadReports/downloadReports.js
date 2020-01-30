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
	populateCategoryDropDown();
	populateFacilitatorNameDropDown ();
	populateStudentReligionDropDown ();
	populateStudentGenderDropdown ();
	populateParentOccupationDropDown ();
	populateMotherOccupationDropDown ();
	populateParentalStatusDropDown ();
	populateStudentScore(); 
	familyCountDropDown();
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
	createPopup('ProcessDialog','','',true);
	studentReportsInfo_Download();
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

function populateStudentScore()
{
	var scoreArray = new Array({scoreId:1,scoreValue:"Exemplary"},{scoreId:1,scoreValue:"Star"},{scoreId:1,scoreValue:"Distinction"},{scoreId:1,scoreValue:"First Class"},{scoreId:1,scoreValue:"Second Class"},{scoreId:1,scoreValue:"Third Class"},{scoreId:1,scoreValue:"Repeater"},{scoreId:1,scoreValue:"Fail"})	
	$("#studentReport_input_Score").jqxComboBox({  source:scoreArray,
												   displayMember:"scoreValue",
												   valueMember:"scoreValue",
												   autoComplete:true,
												   searchMode :"startswithignorecase",
												   placeHolder:"Select Score",
												   width :"200px",
												   height:"25px",																				
												});	
}

function populateCourseNameDropDown ()
{
	var oCourseDataObject = new CourseInformationData ();
	CourseInformationDataProcessor.populateDropDownList(oCourseDataObject,"","",0,0,reportCourseNameDropDown);
}

function populateInstitutionNameDropDown ()
{
	var oInstitutionDataObject = new InstitutionInformationData ();
	InstitutionInformationDataProcessor.populateDropDownList(oInstitutionDataObject,"","",0,0,reportInstitutionNameDropDown);
}

function populateCityNameDropDown ()
{
	var oStudentDataObject = new StudentInformationData ();
	StudentInformationDataProcessor.getCityNames(oStudentDataObject,reportCityNameDropDown);
}

function populateCategoryDropDown ()
{
	var oStudentDataObject = new StudentInformationData ();
	StudentInformationDataProcessor.getStudentCategory(oStudentDataObject,reportCategoryDropDown);
}

function populateParentOccupationDropDown ()
{
	var oStudentObject = new StudentInformationData ();
	StudentInformationDataProcessor.getParentalOccupations(oStudentObject,reportParentOccupationDropDown);
}

function populateMotherOccupationDropDown ()
{
	var oStudentObject = new StudentInformationData ();
	StudentInformationDataProcessor.getMotherOccupations(oStudentObject,reportMotheOccupationDropDown);
}

function populateParentalStatusDropDown ()
{
	var oStudentObject = new StudentInformationData ();
	StudentInformationDataProcessor.getParentalStatus(oStudentObject,reportParentalStatusDropDown);
}


function populateFacilitatorNameDropDown ()
{
	var oFacilitatorObject = new FacilitatorInformationData ();
	FacilitatorInformationDataProcessor.populateDropDownList(oFacilitatorObject,"","",0,0,reportFacilitatorNameDropDown);
}

//response functions

function reportCategoryDropDown (oCategoryResponse)
{
	$(document).ready(function ()
					  {
					       $("#categoryListInfo_input_name").jqxComboBox({  source:oCategoryResponse.m_arrStudentInformationData,
																    	displayMember:"m_strCategory",
																		valueMember:"m_strCategory",
					    	   											autoComplete:true,
			                                                            searchMode :"startswithignorecase",
			                                                            placeHolder:"Select Category",
														                width:"200px",
														                height:"25px",					    	   
					       											});					
					 });	
}

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

function reportMotheOccupationDropDown (oMotherOccupationResponse)
{
$(document).ready(function ()
				  {
						$("#motherOccupationInfo_input_name").jqxComboBox({	 source:oMotherOccupationResponse.m_arrStudentInformationData,
																			 displayMember:"m_strMotherOccupation",
																			 valueMember:"m_strMotherOccupation",
																			 autoComplete:true,
				                                                             searchMode :"startswithignorecase",
				                                                             placeHolder:"Select Occupation",
															                 width:"200px",
															                 height:"25px",							
																		 });
					  });
}


function reportParentalStatusDropDown (oParentalStatusResponse)
{
$(document).ready(function ()
				  {
						$("#parentalStatusInfo_input_name").jqxComboBox({	 source:oParentalStatusResponse.m_arrStudentInformationData,
																			 displayMember:"m_strParentalStatus",
																			 valueMember:"m_strParentalStatus",
																			 autoComplete:true,
				                                                             searchMode :"startswithignorecase",
				                                                             placeHolder:"Select parental status",
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
	if(document.getElementById("studentReport_input_uid").checked)
		oStudentData.m_strSortBy = document.getElementById("studentReport_input_uid").value;
	else
		oStudentData.m_strSortBy = document.getElementById("studentReport_input_name").value;
	if($("#categoryListInfo_input_name").val() != "")
		oStudentData.m_strCategory = $("#categoryListInfo_input_name").val();
	if($("#cityListInfo_input_name").val() != "")
		oStudentData.m_strCity = $("#cityListInfo_input_name").val();
	if($("#courseListInfo_input_name").val() != "")
		oStudentData.m_nCourseId = $("#courseListInfo_input_name").val();
	if($("#facilitatorListInfo_input_name").val() != "")
		oStudentData.m_nFacilitatorId = $("#facilitatorListInfo_input_name").val();
	if($("#fatherOccupationInfo_input_name").val() != "")
		oStudentData.m_strFatherOccupation = $("#fatherOccupationInfo_input_name").val();
	if($("#motherOccupationInfo_input_name").val() != "")
		oStudentData.m_strMotherOccupation = $("#motherOccupationInfo_input_name").val();
	if($("#parentalStatusInfo_input_name").val() != "")
		oStudentData.m_strParentalStatus = $("#parentalStatusInfo_input_name").val();		
	if($("#studentReport_input_Graduated_Students").prop("checked") == true)
		oStudentData.m_bStudentGraduate = $("#studentReport_input_Graduated_Students").val();
	if($("#studentReport_input_Student_Medical_condition").prop("checked") == true)
		oStudentData.m_bStudentMedicalCondition  = $("#studentReport_input_Student_Medical_condition").val();
	if($("#studentReport_input_Parent_Medical_condition").prop("checked") == true)
		oStudentData.m_bParentMedicalCondition = $("#studentReport_input_Parent_Medical_condition").val();
	if($("#institutionNameInfo_input_name").val() != "")
		oStudentData.m_nInstitutionId = $("#institutionNameInfo_input_name").val();
	if($("#studentReport_input_gender").val() != "")
		oStudentData.m_strGender = $("#studentReport_input_gender").val();
	if($("#studentReport_input_religion").val() != "")
		oStudentData.m_strReligion = $("#studentReport_input_religion").val();
	if($("#studentReport_input_FromDate").val() != "")
		oStudentData.m_dFromDate = $("#studentReport_input_FromDate").val();
	if($("#studentReport_input_ToDate").val() != "")
		oStudentData.m_dToDate = $("#studentReport_input_ToDate").val();
	if($("#studentReport_input_Score").val() != "")
		oStudentData.m_strScore = $("#studentReport_input_Score").val();
	if($("#StudentInfo_SelectFamilyCategory").val() != "")
		oStudentData.m_nFamilyCount = $("#StudentInfo_SelectFamilyCategory").val();
	return oStudentData;
}

function fromDateReport() 
{
	var checkBox = document.getElementById("StudentReport_input_ReportDate");
	var fromDate = document.getElementById("studentReport_input_fromDateDiv");
	var toDate = document.getElementById("studentReport_input_toDateDiv");
	 if (checkBox.checked == true){
		 fromDate.style.display = "block";
		 toDate.style.display = "block";
	  } 
	   else
	   {
		   fromDate.style.display = "none";
	    toDate.style.display = "none";
	  }
	 
	 $('#studentReport_input_FromDate').validatebox({
		    required: true,
		});
	 $('#studentReport_input_ToDate').validatebox({
		    required: true,
		});

}
function downloadReportResponse (oDownloadResponse)
{
	if(oDownloadResponse.m_bSuccess)
	{	
		HideDialog("ProcessDialog");	
		var strExcelURL = oDownloadResponse.m_strStudentDownloadReportURL;
		document.getElementById('my_iframe').src = strExcelURL;
		informUser("Reports Downloading","kSuccess");
		HideDialog("dialog");
	}		
	else
	{
		HideDialog("ProcessDialog");
		informUser("Reports Download Failed","kError");
	}
	
}

function familyCountDropDown ()
{	
	var categoryArrayValue = new Array({familyCountCategory:"F1",countValue:1},
									   {familyCountCategory:"F2",countValue:2},
									   {familyCountCategory:"F3",countValue:3},
									   {familyCountCategory:"F4",countValue:4},
									   {familyCountCategory:"F5",countValue:5},
									   {familyCountCategory:"F6",countValue:6},
									   {familyCountCategory:"F7",countValue:7},
									   {familyCountCategory:"F8",countValue:8},
									   {familyCountCategory:"F9",countValue:9},
									   {familyCountCategory:"F10",countValue:10});
	$("#StudentInfo_SelectFamilyCategory").jqxComboBox({  source:categoryArrayValue,
														  displayMember:"familyCountCategory",
														  valueMember:"countValue",
														  autoComplete:true,
														  searchMode :"startswithignorecase",
														  placeHolder:"Select Family Category",
														  width :"200px",
														  height:"25px",																				
												        });
}

