var studentReoprtsInfo_includeDataObjects =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
];

includeDataObjects (studentReoprtsInfo_includeDataObjects, "studentReportsInfo_loaded()");

function studentReportsInfo_loaded() 
{
	loadPage("downloadReports/downloadReports.html", "dialog","studentReportsInfo_init()");
}

function studentReportsInfo_init()
{
	createPopup('dialog','#reportsInfo_button_submit','#reportsInfo_button_cancel',true);
	populateDropDown ();
	initFormValidateBoxes ('studentReportsForm');
}	

function populateDropDown() 
{
	populateAcademicYearDropDown ('reportAcademicYear');
	reportCourseNameDropDown ();
	reportInstitutionNameDropDown ();
	reportCityNameDropDown ();
	reportParentOccupationDropDown ();
	reportStudentReligionDropDown ();
	reportStudentGenderDropdown ();
	reportFacilitatorNameDropDown ();
}

function studentReportsInfo_submit ()
{
	if(studentReportsValidate ())
		loadPage ("include/process.html", "ProcessDialog", "studentreports_progressbarLoaded ()");
	else
	{
		alert("Please Enter feilds");
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

function reportStudentReligionDropDown ()
{
	var religionArray = new Array("Muslim","Non-Muslim","Memon");
	var dropdown = document.getElementById("studentReport_input_religion");	
	for (var i = 0; i < religionArray.length; ++i)
	{	    
	    dropdown[dropdown.length] = new Option(religionArray[i], religionArray[i]);
	}
}

function reportStudentGenderDropdown ()
{
	var gender = new Array("Male","Female");
	var dropdown = document.getElementById("studentReport_input_gender");
	for(var i = 0; i < gender.length; ++i)
	{
		 dropdown[dropdown.length] = new Option(gender[i], gender[i]);
    }
}

function reportCourseNameDropDown () 
{
	$(document).ready(function ()
				{
			   $("#courseListInfo_input_name").jqxComboBox({           autoComplete:true,
																				searchMode :"startswithignorecase",
																				width :"200px",
																				height:"25px",
																				
			   																	});
				}
	);
			
	
}

function reportInstitutionNameDropDown () 
{
	$(document).ready(function () 
			{
		      $("#institutionNameInfo_input_name").jqxComboBox({       autoComplete:true,
											    	                   searchMode :"startswithignorecase",
											    	                   width:"200px",
											    	                   height:"25px",
		    	  
		      });
	
			
			}
	);
	
}

function reportCityNameDropDown ()
{
	$(document).ready(function ()
			{
		       $("#cityListInfo_input_name").jqxComboBox({      autoComplete:true,
                                                                searchMode :"startswithignorecase",
											                    width:"200px",
											                     height:"25px",
		    	   
		       });
		
			}
			);
	
}


function reportParentOccupationDropDown ()
{
	$(document).ready(function ()
			{
			$("#parentOccupationInfo_input_name").jqxComboBox({       autoComplete:true,
	                                                                  searchMode :"startswithignorecase",
												                      width:"200px",
												                      height:"25px",
				
			});
			});
}

function reportFacilitatorNameDropDown ()
{
	$(document).ready(function ()
			{
			$("#facilitatorListInfo_input_name").jqxComboBox({       autoComplete:true,
	                                                                  searchMode :"startswithignorecase",
												                      width:"200px",
												                      height:"25px",
				
			});
			});
	
	
}



