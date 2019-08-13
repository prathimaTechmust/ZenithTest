var studentInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js',
	'widgets/scholarshipmanagement/courselist/CourseInformationData.js',
	'widgets/scholarshipmanagement/scholarshipdetails/ScholarshipDetails.js',
	'widgets/scholarshipmanagement/academicdetails/AcademicDetails.js',
	'widgets/usermanagement/facilitator/FacilitatorInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js',
	'widgets/scholarshipmanagement/siblingsDetails/SiblingsDetails.js'
];

 includeDataObjects (studentInfo_includeDataObjects, "studentInfo_loaded()");

function studentInfo_memberData ()
{
	this.m_nStudentId = -1;
	this.m_strImageId = "";
	this.m_bIsNew = false;
	this.m_oformData = new FormData();
	this.m_arrStudentUIDData = new Array();
	this.m_studentInstitution = "";
	this.m_studentCourse = "";
	this.m_studentDateofBirth = "";
	this.m_nAcademicId = -1;
	this.m_nSholarshipId = -1;
	this.m_strApplicationType = "";
	this.m_arrScholarshipDetails = new Array ();
	this.m_arrSiblingsDetails = new Array();
	
	this.m_nRowOrgCount = -1; 
	this.m_nRowOrgAmountCount = -1;
	this.m_nOrgId = 1;
	this.m_nOrgAmount = 1;
	this.m_arrOrgId = new Array();
	this.m_nUpdatedOrgRowCount = 0;
	this.m_nUpdatedAmountRowCount = 0;
	
	this.m_strAcademicYear = "";
	this.m_nInstitutionId = -1;
	this.m_nCourseId = -1;
	
	this.m_nRowSiblingsUIDIdCount = -1;
	this.m_nRowSiblingsNameCount= -1;
	this.m_nRowSiblingsStudying = -1;
	this.m_nRowSiblingsSchoolCollege = -1;
	
	this.m_nRowSiblingsCount = -1;
	this.m_nSiblingsUIDId = 1;
	this.m_nSiblingsName = 1;
	this.m_nSiblingsStudying = 1;
	this.m_nSiblingsSchoolCollege = 1;

	this.m_nUpdatedSiblingsUIDIdRowCount = 0;
	this.m_nUpdatedSiblingsNameRowCount = 0;
	this.m_nUpdatedSiblingStudyingRowCount = 0;
	this.m_nUpdatedSiblingSchoolCollegeRowCount = 0;	
	this.m_oStudentResponse="";
	
	this.m_nSiblingRowId=-1;
}

var m_oStudentInfoMemberData = new studentInfo_memberData ();

function studentInfo_new ()
{
	studentInfo_init();
	initFormValidateBoxes ("studentInfo_form_id");
}

function studentInfo_old ()
{
	createPopup("dialog", "#uidInfo_button_submit", "#uidInfo_button_cancel", true);
	initFormValidateBoxes ("oldstudentUIDForm");	
}

function studentInfo_init ()
{
	createPopup("dialog", "#studentInfo_button_submit", "#studentInfo_button_cancel", true);
	document.getElementById("DocumentUpload_details_btn").style.display="none";
	document.getElementById("defaultOpen").click();
	populatAcademicYearDropDown('selectacademicyear');	
	calculate_Student_CourseFee ();
	studentReligionDropDown();
	studentParentalStatusDropDown();
	studentScoreDropDown();
	getDropDownValues ();	
}

function getDropDownValues ()
{
	getFacilitatorListToDropDown();
	getInstitutionsNamesListDropDown();
	getCourseNamesListDropDown();
}

function getCourseNamesListDropDown ()
{
	var oCourseInformationData = new CourseInformationData ();
	CourseInformationDataProcessor.list (oCourseInformationData, "", "",1,10,loadCourseListResponseToDropDown);
}

function loadCourseListResponseToDropDown(oCourseListResponse)
{
	m_oStudentInfoMemberData.m_arrCourseInformationData = oCourseListResponse.m_arrCourseInformationData;
	$(document).ready(function ()
		 	{
				$("#select_input_studentcourse").jqxComboBox({	source :oCourseListResponse.m_arrCourseInformationData,
																displayMember: "m_strShortCourseName",
																valueMember: "m_nCourseId",
																placeHolder:"Select Course",
																autoComplete:true,
																searchMode :"startswithignorecase",
																width :"200px",
																height:"25px"});
		 	}
		 );		
}

function getInstitutionsNamesListDropDown ()
{
	var oInstitutionInformationData = new InstitutionInformationData ();
	InstitutionInformationDataProcessor.list (oInstitutionInformationData, "", "",1,10, loadInstitutionListResponseToDropDown);
}

function loadInstitutionListResponseToDropDown (oInstitutionResponse)
{
	m_oStudentInfoMemberData.m_arrInstitutionInformationData = oInstitutionResponse.m_arrInstitutionInformationData;
	$(document).ready(function ()
			{
				$("#select_input_academic_name").jqxComboBox({	source :oInstitutionResponse.m_arrInstitutionInformationData,
																displayMember: "m_strInstitutionName",
																valueMember: "m_nInstitutionId",
																placeHolder:"Select Institution",
																autoComplete:true,
																searchMode :"startswithignorecase",
																width :"200px",
																height:"25px"});
			}					 
		 );	
}

function getFacilitatorListToDropDown ()
{
	var oFacilitatorInformationData = new FacilitatorInformationData ();
	FacilitatorInformationDataProcessor.list (oFacilitatorInformationData,"","",1,10,loadFacilitatorListResponseToDropDown);			
}

function loadFacilitatorListResponseToDropDown (oReponse)
{
	m_oStudentInfoMemberData.m_arrFacilitatorInformationData = oReponse.m_arrFacilitatorInformationData;
	$(document).ready(function ()
				{
			   $("#selectStudentInfo_input_studentfacilitator").jqxComboBox({	source :oReponse.m_arrFacilitatorInformationData,
																				displayMember: "m_strFacilitatorName",
																				valueMember: "m_nFacilitatorId",
																				placeHolder:"Select Facilitator",
																				autoComplete:true,
																				searchMode :"startswithignorecase",
																				width :"200px",
																				height:"25px"});
				}
			);	
}

function studentScoreDropDown ()
{
	var studentScoreArray = new Array("Exemplary","Star","Distinction","First Class","Second Class","Third Class","Repeater","Fail");
	var dropdown = document.getElementById("studentInfo_input_studentScore");
	for (var i = 0; i < studentScoreArray.length; ++i)
	{	    
	    dropdown[dropdown.length] = new Option(studentScoreArray[i], studentScoreArray[i]);
	}
}

function studentParentalStatusDropDown()
{
	var parentalArray = new Array("Single Mother","Single Father","Widow","Widower","No Parents","Both Parents");
	var dropdown = document.getElementById("select_input_studentParentalStatus");
	for (var i = 0; i < parentalArray.length; ++i)
	{	    
	    dropdown[dropdown.length] = new Option(parentalArray[i], parentalArray[i]);
	}
}

function studentReligionDropDown ()
{
	var religionArray = new Array("Muslim","Non-Muslim","Memon");
	var dropdown = document.getElementById("studentInfo_input_religion");	
	for (var i = 0; i < religionArray.length; ++i)
	{	    
	    dropdown[dropdown.length] = new Option(religionArray[i], religionArray[i]);
	}
}

function calculate_Student_CourseFee ()
{
	$(document).ready(function()
			{			   
			    $("#academicInfo_input_annualfee, #academicInfo_input_paidfee").on("keydown keyup", function()
			    {
			        substraction();
		    	});
			});
}

function substraction ()
{
	
	var annualFee = document.getElementById('academicInfo_input_annualfee').value;
    var paidFee = document.getElementById('academicInfo_input_paidfee').value;
	var balance = annualFee - paidFee;
	document.getElementById('academicInfo_input_balancefee').value = balance;
}

function scholarship_addNewOrganization ()
{	
	if(m_oStudentInfoMemberData.m_nRowOrgCount != -1)
	{
		$("#scholarship_Organization").append('<tr><td class="fieldHeading">Organization</td><td style="padding-right: 10px"> </td><td><input  type="text" id="scholarshipInfo_input_organization'+(m_oStudentInfoMemberData.m_nRowOrgCount++)+'" class="zenith"/></td><td style="padding-right: 10px"> </td><td class="fieldHeading">Amount(Rs)</td><td style="padding-right: 10px"> </td><td><input  type="text" id="scholarshipInfo_input_organizationamount'+(m_oStudentInfoMemberData.m_nRowOrgAmountCount++)+'" class="zenith" onkeyup="validateNumber(this)"/></td><td style="padding-right: 10px"> </td><td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete Organization" class = "removeOrganization" onClick="scholarship_removeEditOrganizationrow()"/> </td></tr>');		
		m_oStudentInfoMemberData.m_nUpdatedEditOrgRowCount = m_oStudentInfoMemberData.m_nRowOrgCount;
		m_oStudentInfoMemberData.m_nUpdatedEditAmountRowCount = m_oStudentInfoMemberData.m_nRowOrgAmountCount;
	}
	else
	{
		$("#scholarship_Organization").append('<tr><td class="fieldHeading">Organization</td><td style="padding-right: 10px"> </td><td><input  type="text" id="scholarshipInfo_input_organization'+(m_oStudentInfoMemberData.m_nOrgId++)+'" class="zenith"/></td><td style="padding-right: 10px"> </td><td class="fieldHeading">Amount(Rs)</td><td style="padding-right: 10px"> </td><td><input  type="text" id="scholarshipInfo_input_organizationamount'+(m_oStudentInfoMemberData.m_nOrgAmount++)+'" class="zenith" onkeyup="validateNumber(this)"/></td><td style="padding-right: 10px"> </td><td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete Organization" class = "removeOrganization" onClick="scholarship_removeNewOrganizationrow()"/> </td></tr>');
		m_oStudentInfoMemberData.m_nUpdatedOrgRowCount = m_oStudentInfoMemberData.m_nOrgId;
		m_oStudentInfoMemberData.m_nUpdatedAmountRowCount = m_oStudentInfoMemberData.m_nOrgAmount;
	}
	
}

function scholarship_removeEditOrganizationrow(clicked_id)
{
	var bUserConfirm = getUserConfirmation("Are you sure do you want to delete?");
	if(bUserConfirm)
	{
		$("#scholarship_Organization").on('click','.removeOrganization',function() { $(this).parent().parent().remove(); });
		var oOrganization = new ScholarshipDetails();
		oOrganization.m_strOrganizationName = $("#scholarshipInfo_input_organization"+clicked_id).val();
		checkOrganization(oOrganization);		
	}	
    var siblingsrows = document.getElementById("scholarship_Organization");    
    m_oStudentInfoMemberData.m_nRowOrgCount = scholarshiprows.rows.length;
    m_oStudentInfoMemberData.m_nRowOrgAmountCount = scholarshiprows.rows.length;
    
}

function checkOrganization (oOrganization)
{
	if(oOrganization.m_strOrganizationName != undefined)
		ScholarshipOrganizationProcessor.deleteOrganization(oOrganization,deleteOrganizationResponse);
}

function deleteOrganizationResponse ()
{
	student_displayInfo("organization deleted successfully");
}

function scholarship_removeNewOrganizationrow ()
{
	$("#scholarship_Organization").on('click','.removeOrganization',function() { $(this).parent().parent().remove(); });
    var scholarshiprows = document.getElementById("scholarship_Organization");   
    m_oStudentInfoMemberData.m_nOrgId = scholarshiprows.rows.length;
    m_oStudentInfoMemberData.m_nOrgAmount = scholarshiprows.rows.length;
}
function validateUIDField ()
{
	var bIsValid = true;
	var decimalOnly = /^\s*-?[0-9]\d*(\.\d{1,2})?\s*$/;
	 var uidNumber = document.getElementById("studentInfo_input_studentUID").value;
	 if(uidNumber != '')
	   if(decimalOnly.test(uidNumber))
	   {
		   bIsValid = true;
	   }		  
	   else
	   {
		   alert('Please Enter Numbers Only');
		   bIsValid = false;
	   }
	 return bIsValid;
}

function studentInfo_submit ()
{
	if (studentInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "student_progressbarLoaded ()");
	else
	{
		alert("Please Fill Mandiatory Fields");
		$('#studentInfo_form_id').focus();
	}
		
}

function student_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	oStudentInformationData = studentInfo_getFormData ();
	if(document.getElementById("studentInfo_button_submit").getAttribute('update') == "false")
		StudentInformationDataProcessor.create(oStudentInformationData, studentInfo_created);
	else
		StudentInformationDataProcessor.update (oStudentInformationData,studentInfo_updated);
}

function studentInfo_edit ()
{
	studentInfo_init();
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nStudentId = m_oStudentInfoListMemberData.m_nSelectedStudentId;
	oStudentInformationData.m_strAcademicYear = m_oStudentInfoListMemberData.m_strAcademicYear;
	document.getElementById("studentInfo_button_submit").setAttribute('update', true);
	document.getElementById("studentInfo_button_submit").innerHTML = "Update";
	document.getElementById("DocumentUpload_details_btn").style.display="inline";
	StudentInformationDataProcessor.get (oStudentInformationData, studentInfo_gotData);
}

function studentInfo_getFormData ()
{	
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nStudentId = m_oStudentInfoMemberData.m_nStudentId;
	oStudentInformationData.m_nUID = $("#studentInfo_input_studentUIDNumber").val();	
	oStudentInformationData.m_nStudentAadharNumber = $("#studentInfo_input_studentAadharNumber").val();
	oStudentInformationData.m_strStudentName = $("#studentInfo_input_studentName").val();
	oStudentInformationData.m_oFacilitatorInformationData = new FacilitatorInformationData();
	oStudentInformationData.m_oFacilitatorInformationData.m_nFacilitatorId = $("#selectStudentInfo_input_studentfacilitator").val();
	 if(document.getElementById("studentInfo_input_male").checked)
		 oStudentInformationData.m_strGender = document.getElementById("studentInfo_input_male").value;
	 else if(document.getElementById("studentInfo_input_female").checked)
		 oStudentInformationData.m_strGender = document.getElementById("studentInfo_input_female").value;
	 else
		 oStudentInformationData.m_strGender = document.getElementById("studentInfo_input_other").value;	
	if($("#studentInfo_input_studentPhoto").val() == '')
		oStudentInformationData.m_strStudentImageId = m_oStudentInfoMemberData.m_strImageId;			
	/*else
		oStudentInformationData.m_strStudentImageName = $("#studentInfo_input_studentPhoto").val().replace(/.*(\/|\\)/, '');*/
	oStudentInformationData.m_dDateOfBirth = convertDateToTimeStamp($("#student_input_dateofbirth").val());
	if($("#student_input_dateofbirth").val() == '')
		oStudentInformationData.m_dDateOfBirth = m_oStudentInfoMemberData.m_studentDateofBirth;
	oStudentInformationData.m_strFatherName = $("#studentInfo_input_fathername").val();
	oStudentInformationData.m_strFatherOccupation = $("#studentInfo_input_fatheroccupation").val();
	oStudentInformationData.m_nFatherAadharNumber = $("#studentInfo_input_fatherAadharNumber").val();
	oStudentInformationData.m_strMotherName = $("#studentInfo_input_mothername").val();
	oStudentInformationData.m_strMotherOccupation = $("#studentInfo_input_motheroccupation").val();
	oStudentInformationData.m_nMotherAadharNumber = $("#studentInfo_input_motherAadharNumber").val();
	oStudentInformationData.m_strPhoneNumber = $("#studentInfo_input_phoneNumber1").val();
	oStudentInformationData.m_strAlternateNumber = $("#studentInfo_input_phoneNumber2").val();
	oStudentInformationData.m_nFamilyIncome = $("#studentInfo_input_income").val();	
	oStudentInformationData.m_strEmailAddress = $("#studentInfo_input_email").val();	
	oStudentInformationData.m_strReligion = $("#studentInfo_input_religion :selected").val();
	oStudentInformationData.m_strParentalStatus = $("#select_input_studentParentalStatus").val();
	oStudentInformationData.m_strCurrentAddress = $("#studentInfo_textarea_address").val();
	oStudentInformationData.m_strCity = $("#studentInfo_input_cityName").val();
	oStudentInformationData.m_strState= $("#studentInfo_input_stateName").val();
	oStudentInformationData.m_nPincode = $("#studentInfo_input_pincodeName").val();
	if(m_oStudentInfoMemberData.m_strAcademicYear != $("#selectacademicyear").val())
		oStudentInformationData.m_oZenithScholarshipDetails = getZenithstatus();
	      /*Academic details*/
	oStudentInformationData.m_oAcademicDetails = getAcademicDetails ();
	if(document.getElementById("studentInfo_input_yes").checked)
		oStudentInformationData.m_oSibilingDetails = getSiblingsDetails();	
	return oStudentInformationData;
}

function getAcademicDetails ()
{
	var oArrAcademicDetails = new Array();	
	var oAcademicDetails = new AcademicDetails ();	
	oAcademicDetails.m_oInstitutionInformationData = new InstitutionInformationData ();
	oAcademicDetails.m_oInstitutionInformationData.m_nInstitutionId = $("#select_input_academic_name").val();
	oAcademicDetails.m_oCourseInformationData = new CourseInformationData();
	oAcademicDetails.m_oCourseInformationData.m_nCourseId = $("#select_input_studentcourse").val();	
	if(m_oStudentInfoMemberData.m_strAcademicYear == $("#selectacademicyear").val())
		oAcademicDetails.m_nAcademicId = m_oStudentInfoMemberData.m_nAcademicId ;	
	oAcademicDetails.m_strAcademicYear = $("#selectacademicyear").val();
	oAcademicDetails.m_strStudentScore = $("#studentInfo_input_studentScore :selected").val();
	oAcademicDetails.m_strSpecialization = $("#select_input_studentSpecialization").val();
	oAcademicDetails.m_fAnnualFee = $("#academicInfo_input_annualfee").val();	
	oAcademicDetails.m_fPaidFee = $("#academicInfo_input_paidfee").val();
	oAcademicDetails.m_fBalanceFee = $("#academicInfo_input_balancefee").val();
		/*Scholarship details*/	
	var scholarshipdetails = m_oStudentInfoMemberData.m_arrScholarshipDetails;
	if(scholarshipdetails.length == 0)
		oAcademicDetails.m_oScholarshipDetails = getNewScholarshipDetails ();
	else if(scholarshipdetails.length >= 1)
		oAcademicDetails.m_oScholarshipDetails = getAddScolarshipDetails ();	
	oArrAcademicDetails.push(oAcademicDetails);		
	return oArrAcademicDetails;
}

function getZenithstatus()
{
	var oArrScholarshipStatus = new Array();
	var oZenithSholarshipstatus = new ZenithScholarshipDetails ();
	oZenithSholarshipstatus.m_strAcademicYear = $("#select_student_academicyear").val();
	oArrScholarshipStatus.push(oZenithSholarshipstatus);
	return oArrScholarshipStatus;
}

function getNewScholarshipDetails ()
{
	var oArrScholarshipDetails = new Array();
		checkRowCount();			
	    for(var nIndex = 0; nIndex < m_oStudentInfoMemberData.m_nUpdatedOrgRowCount; nIndex++)
	    {
	    	var oScholarshipDetails = new ScholarshipDetails();
	    	if(($("#scholarshipInfo_input_organization"+nIndex).val() != '' && $("#scholarshipInfo_input_organizationamount"+nIndex).val() !='')&&($("#scholarshipInfo_input_organization"+nIndex).val() != undefined && $("#scholarshipInfo_input_organizationamount"+nIndex).val() != undefined))
	    	{		   
    			oScholarshipDetails.m_strOrganizationName = $("#scholarshipInfo_input_organization"+nIndex).val();
				oScholarshipDetails.m_fAmount = $("#scholarshipInfo_input_organizationamount"+nIndex).val();
				oArrScholarshipDetails.push(oScholarshipDetails);
	    	}
	    }
	return oArrScholarshipDetails;
}



function checkRowCount ()
{
	var result = m_oStudentInfoMemberData.m_nUpdatedOrgRowCount;
	if(result == 0)
		m_oStudentInfoMemberData.m_nUpdatedOrgRowCount = 1;
}

function getAddScolarshipDetails ()
{
	var oArrScholarshipDetails = new Array();
	var arrAddscholarshipDetails = m_oStudentInfoMemberData.m_arrScholarshipDetails;	
	var scholarshiporganizations = document.getElementById("scholarship_Organization");
	if(scholarshiporganizations.rows.length == arrAddscholarshipDetails.length)
	{
		for(var nIndex = 0; nIndex < arrAddscholarshipDetails.length; nIndex++)
		{
			var oScholarshipDetails = new ScholarshipDetails();				    
		    if(m_oStudentInfoMemberData.m_strAcademicYear == $("#selectacademicyear").val())
		    	oScholarshipDetails.m_nScholarshipId = arrAddscholarshipDetails[nIndex].m_nScholarshipId;				
			oScholarshipDetails.m_strOrganizationName = $("#scholarshipInfo_input_organization"+nIndex).val();
			oScholarshipDetails.m_fAmount = $("#scholarshipInfo_input_organizationamount"+nIndex).val();
			oArrScholarshipDetails.push(oScholarshipDetails);
		}
	}
	else
	{
		for(var nIndex = 0; nIndex < m_oStudentInfoMemberData.m_nUpdatedEditOrgRowCount; nIndex++)
		{
			var oScholarshipDetails = new ScholarshipDetails();			
			if(($("#scholarshipInfo_input_organization"+nIndex).val() != '' && $("#scholarshipInfo_input_organizationamount"+nIndex).val() !='')&&($("#scholarshipInfo_input_organization"+nIndex).val() != undefined && $("#scholarshipInfo_input_organizationamount"+nIndex).val() != undefined))
	    	{					   
				oScholarshipDetails.m_strOrganizationName = $("#scholarshipInfo_input_organization"+nIndex).val();
				oScholarshipDetails.m_fAmount = $("#scholarshipInfo_input_organizationamount"+nIndex).val();
				oArrScholarshipDetails.push(oScholarshipDetails);
	    	}
			if((nIndex < arrAddscholarshipDetails.length) && (m_oStudentInfoMemberData.m_strAcademicYear == $("#selectacademicyear").val()))
			{
				oScholarshipDetails.m_nScholarshipId = arrAddscholarshipDetails[nIndex].m_nScholarshipId;
			}
		}		
	}	
	return oArrScholarshipDetails;
}
function studentInfo_created (oStudentInfoResponse)
{
	HideDialog ("ProcessDialog");
	if (oStudentInfoResponse.m_bSuccess)
	{
		studentInfo_displayInfo("student created successfully", "kSuccess");
		try
		{
			var oForm = $('#studentInfo_form_id')[0];
			var oFormData = new FormData (oForm);
			oFormData.append('studentId',oStudentInfoResponse.m_arrStudentInformationData[0].m_nStudentId);
			StudentInformationDataProcessor.setImagetoS3bucket (oFormData, student_image_created);
		}
		catch(oException){}
	}
	else
		studentInfo_displayInfo("student creation failed", "kError");
}

function studentInfo_updated (oStudentInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oStudentInfoResponse.m_bSuccess)
	{
		studentInfo_displayInfo ("student updated successfully");
		try
		{
			var oForm = $('#studentInfo_form_id')[0];
			var oFormData = new FormData (oForm);
			var strImageFile = oFormData.get("studentimage");
			if(strImageFile.name == "")
			{
				student_details_updated();
			}
			else if(oStudentInfoResponse.m_arrStudentInformationData[0].m_strStudentImageId == "" || m_oStudentInfoMemberData.m_strImageId != strImageFile.name)
			{
				oFormData.append('studentId',oStudentInfoResponse.m_arrStudentInformationData[0].m_nStudentId);				
				StudentInformationDataProcessor.setImagetoS3bucket (oFormData, student_details_updated);
			}			
		}
		catch(oException){}		
	}	
}

function student_image_created ()
{
	HideDialog ("secondDialog");
}

function student_details_updated ()
{
	HideDialog ("secondDialog");
}

function studentInfo_validate ()
{
	return validateForm("studentInfo_form_id");
}

function studentInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("studentList", "widgets/scholarshipmanagement/studentlist/listStudent.js")
}

function student_displayInfo(strMessage)
{
	informUser(strMessage, "kSuccess");
}

function studentInfo_gotData (oStudentInfoResponse)
{	
	
	var oStudentInfoData = oStudentInfoResponse.m_arrStudentInformationData[0];	
	m_oStudentInfoMemberData.m_strImageId = oStudentInfoData.m_strStudentImageId;
	m_oStudentInfoMemberData.m_studentDateofBirth = oStudentInfoData.m_dDateOfBirth;
	m_oStudentInfoMemberData.m_nAcademicId = oStudentInfoData.m_oAcademicDetails[0].m_nAcademicId;
	m_oStudentInfoMemberData.m_strAcademicYear = oStudentInfoData.m_oAcademicDetails[0].m_strAcademicYear;
	m_oStudentInfoMemberData.m_nInstitutionId = oStudentInfoData.m_oAcademicDetails[0].m_oInstitutionInformationData.m_nInstitutionId;
	m_oStudentInfoMemberData.m_nCourseId = oStudentInfoData.m_oAcademicDetails[0].m_oCourseInformationData.m_nCourseId;
	m_oStudentInfoMemberData.m_arrSiblingsDetails = oStudentInfoData.m_oSibilingDetails;
	m_oStudentInfoMemberData.m_arrScholarshipDetails = 	oStudentInfoData.m_oAcademicDetails[0].m_oScholarshipDetails;
	m_oStudentInfoMemberData.m_nRowOrgCount = m_oStudentInfoMemberData.m_arrScholarshipDetails.length;
	m_oStudentInfoMemberData.m_nRowOrgAmountCount = m_oStudentInfoMemberData.m_arrScholarshipDetails.length;
	m_oStudentInfoMemberData.m_nStudentId = oStudentInfoData.m_nStudentId;
	 $("#studentInfo_input_studentUIDNumber").val(oStudentInfoData.m_nUID);
	 $("#studentInfo_input_studentAadharNumber").val(oStudentInfoData.m_nStudentAadharNumber);
	 $("#studentInfo_input_studentName").val(oStudentInfoData.m_strStudentName);
	 facilitatorPopulateCombobox(oStudentInfoData);
	 $('#selectStudentInfo_input_studentfacilitator').jqxComboBox('val', oStudentInfoData.m_oFacilitatorInformationData.m_nFacilitatorId);
	 if(oStudentInfoData.m_strGender == "Male")
	 {
		var radiobutton = document.getElementById("studentInfo_input_male");
		radiobutton.checked = true;
	 }
	 else if(oStudentInfoData.m_strGender == "Female")
	 {
		 var radiobutton = document.getElementById("studentInfo_input_female");
			radiobutton.checked = true;
	 }
	 else
	 {
		 var radiobutton = document.getElementById("studentInfo_input_other");
			radiobutton.checked = true;
	 }
	 document.getElementById("student_input_dateofbirth").value = convertTimestampToDate(oStudentInfoData.m_dDateOfBirth);
	 $("#studentInfo_input_fathername").val(oStudentInfoData.m_strFatherName);
	 $("#studentInfo_input_fatheroccupation").val(oStudentInfoData.m_strFatherOccupation);
	 $("#studentInfo_input_fatherAadharNumber").val(oStudentInfoData.m_nFatherAadharNumber);
	 $("#studentInfo_input_mothername").val(oStudentInfoData.m_strMotherName);
	 $("#studentInfo_input_motheroccupation").val(oStudentInfoData.m_strMotherOccupation);
	 $("#studentInfo_input_motherAadharNumber").val(oStudentInfoData.m_nMotherAadharNumber);
	 $("#studentInfo_input_income").val(oStudentInfoData.m_nFamilyIncome);
	 $("#studentInfo_input_phoneNumber1").val(oStudentInfoData.m_strPhoneNumber);
	 $("#studentInfo_input_phoneNumber2").val(oStudentInfoData.m_strAlternateNumber);
	 $("#select_input_studentParentalStatus").val(oStudentInfoData.m_strParentalStatus);
	 $("#studentInfo_input_religion").val(oStudentInfoData.m_strReligion);
	 $("#studentInfo_input_email").val(oStudentInfoData.m_strEmailAddress);
	 $("#studentInfo_textarea_address").val(oStudentInfoData.m_strCurrentAddress);
	 $("#studentInfo_input_cityName").val(oStudentInfoData.m_strCity);
	 $("#studentInfo_input_stateName").val(oStudentInfoData.m_strState);
	 $("#studentInfo_input_pincodeName").val(oStudentInfoData.m_nPincode);
	  /*Academic Details*/
	 institutionPopulateCombobox(oStudentInfoData);
	 coursePopulateCombobox(oStudentInfoData);
	 $("#select_input_academic_name").jqxComboBox('val',oStudentInfoData.m_oAcademicDetails[0].m_oInstitutionInformationData.m_nInstitutionId);	
	 $("#select_input_studentcourse").jqxComboBox('val',oStudentInfoData.m_oAcademicDetails[0].m_oCourseInformationData.m_nCourseId);
	 $("#select_input_studentSpecialization").val(oStudentInfoData.m_oAcademicDetails[0].m_strSpecialization);
	 $("#studentInfo_input_studentScore").val(oStudentInfoData.m_oAcademicDetails[0].m_strStudentScore);
	 $("#academicInfo_input_annualfee").val(oStudentInfoData.m_oAcademicDetails[0].m_fAnnualFee);
	 $("#academicInfo_input_paidfee").val(oStudentInfoData.m_oAcademicDetails[0].m_fPaidFee);	 
	 substraction();
	 /* Scholarship Details*/	
	 gotScholarshipdata(m_oStudentInfoMemberData.m_arrScholarshipDetails);	 
	 gotStudentDocuments(oStudentInfoResponse.m_oStudentDocuments);
	 setReadableFields();
	 if(m_oStudentInfoMemberData.m_arrSiblingsDetails.length > 0)
		 gotSiblingDetails(m_oStudentInfoMemberData.m_arrSiblingsDetails);
	 initFormValidateBoxes ("studentInfo_form_id");
	 siblingsRowCount(m_oStudentInfoMemberData.m_arrSiblingsDetails);
}
/*siblings details*/
function siblingsRowCount(siblingsEditRowCount) {
	
	m_oStudentInfoMemberData.m_nRowSiblingsUIDIdCount = siblingsEditRowCount.length;
	m_oStudentInfoMemberData.m_nRowSiblingsNameCount = siblingsEditRowCount.length;
	m_oStudentInfoMemberData.m_nRowSiblingStudyingCount = siblingsEditRowCount.length;
	m_oStudentInfoMemberData.m_nRowSiblingSchoolCollegeCount = siblingsEditRowCount.length;
}

function gotSiblingDetails (m_arrSiblingsDetails)
{	
		getSiblings(siblingdiv);
		document.getElementById("studentInfo_input_yes").checked = true;
	 for(var nIndex = 0; nIndex < m_arrSiblingsDetails.length; nIndex++ )
	 {
		 if(nIndex !=0)
		 $("#siblings").append('<tr><td class="fieldHeading">UID</td> <td><input type="text" id="studentInfo_input_SiblingsUID'+(nIndex)+'" class="zenith" style="margin-right: 60px"/></td><td class="fieldHeading">Name</td><td><input type="text"id="studentInfo_input_SiblingsName'+(nIndex)+'" class="zenith" /></td><td class="fieldHeading">Studying</td><td><input type="text" id="studentInfo_input_SiblingsStudying'+(nIndex)+'" class="zenith" /></td><td class="fieldHeading">School/College</td><td><input type="text" id="studentInfo_input_SiblingsSchoolCollege'+(nIndex)+'" class="zenith" /></td><td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="DeleteSiblings" class = "removeSiblings" onClick="deleteNewSiblings(this.id)"/> </td></tr>');
		 $("#studentInfo_input_SiblingsUID"+nIndex).val(m_arrSiblingsDetails[nIndex].m_nZenithUID);
		 $("#studentInfo_input_SiblingsName"+nIndex).val(m_arrSiblingsDetails[nIndex].m_strSibilingName);
		 $("#studentInfo_input_SiblingsStudying"+nIndex).val(m_arrSiblingsDetails[nIndex].m_strStudying);
		 $("#studentInfo_input_SiblingsSchoolCollege"+nIndex).val(m_arrSiblingsDetails[nIndex].m_strStudyingInstitution);		 
	 }
}

function gotScholarshipdata (m_arrScholarshipDetails)
{
	 for(var nIndex = 0; nIndex < m_arrScholarshipDetails.length; nIndex++ )
	 {
		 if(nIndex !=0)
		 $("#scholarship_Organization").append('<tr><td class="fieldHeading">Organization</td><td style="padding-right: 10px"> </td><td><input  type="text" id="scholarshipInfo_input_organization'+(nIndex)+'" class="zenith"/></td><td style="padding-right: 10px"> </td><td class="fieldHeading">Amount(Rs)</td><td style="padding-right: 10px"> </td><td><input  type="text" id="scholarshipInfo_input_organizationamount'+(nIndex)+'" class="zenith" onkeyup="validateNumber(this)"/></td><td style="padding-right: 10px"> </td><td> <img src="images/delete.png" width="20" align="center" id="'+(nIndex)+'" title="Delete Organization" class = "removeOrganization" onClick="scholarship_removeEditOrganizationrow(this.id)"/> </td></tr>');		
		 $("#scholarshipInfo_input_organization"+nIndex).val(m_arrScholarshipDetails[nIndex].m_strOrganizationName);
		 $("#scholarshipInfo_input_organizationamount"+nIndex).val(m_arrScholarshipDetails[nIndex].m_fAmount);
	 }
}

function setReadableFields()
{
	$("#studentInfo_input_studentUIDNumber").attr('readonly','readonly');
}

function  gotStudentDocuments(oStudentDocuments)
{
	if(oStudentDocuments != null && oStudentDocuments.m_strStudentAadhar != null)
		$("#studentInfo_input_studentaadhar").attr("src",oStudentDocuments.m_strStudentAadhar);
	if(oStudentDocuments != null && oStudentDocuments.m_strStudentElectricityBill != null)
		$("#studentInfo_input_studentElectricityBill").attr("src",oStudentDocuments.m_strStudentElectricityBill);
	if(oStudentDocuments != null && oStudentDocuments.m_strFatherAadharImageId != null)
		$("#studentInfo_input_fatheraadhar").attr("src",oStudentDocuments.m_strFatherAadharImageId);
	if(oStudentDocuments != null && oStudentDocuments.m_strMotherAadharImageId != null)
		$("#studentInfo_input_motheraadhar").attr("src",oStudentDocuments.m_strMotherAadharImageId);
	if(oStudentDocuments != null && oStudentDocuments.m_strStudentMarksCard1 != null)
		$("#studentInfo_input_studentMarksCard1").attr("src",oStudentDocuments.m_strStudentMarksCard1);
	if(oStudentDocuments != null && oStudentDocuments.m_strStudentMarksCard2 != null)
		$("#studentInfo_input_studentMarksCard2").attr("src",oStudentDocuments.m_strStudentMarksCard2);
	if(oStudentDocuments != null && oStudentDocuments.m_strOtherDocuments != null)
		$("#studentInfo_input_additionalDocuments").attr("src",oStudentDocuments.m_strOtherDocuments);
}

function facilitatorPopulateCombobox(oStudentInfoData)
{
	getFacilitatorListToDropDown();
}

function institutionPopulateCombobox(oStudentInfoData)
{
	getInstitutionsNamesListDropDown();
}

function coursePopulateCombobox(oStudentInfoData)
{	
	getCourseNamesListDropDown();
}

function searchStudentUID ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nUID = $("#studentInfo_input_studentUIDNumber").val();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	StudentInformationDataProcessor.getStudentDataUID (oStudentInformationData, studentInfo_setStudentData);
}

function studentInfo_setStudentData (oStudentUIDAadharResponse)
{
	m_oStudentInfoMemberData.m_oStudentResponse = oStudentUIDAadharResponse;
	HideDialog ("ProcessDialog");
	if(oStudentUIDAadharResponse.m_bSuccess)
	{
		
		document.getElementById("studentInfo_button_submit").setAttribute('update', true);
		document.getElementById("studentInfo_button_submit").innerHTML = "Update";
		document.getElementById("DocumentUpload_details_btn").style.display="inline";
		studentInfo_gotData(oStudentUIDAadharResponse);		
	}
	else
	{		
		$('#studentInfo_form_id').focus();
	}

}

function searchStudentAadhar ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nStudentAadharNumber = $("#studentInfo_input_studentAadharNumber").val();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	StudentInformationDataProcessor.getStudentDataUID (oStudentInformationData, studentInfo_setStudentData);
}

function studentInfo_createandprint()
{

    if(studentInfo_validate())
        loadPage("include/process.html", "ProcessDialog", "studentcreateandPrint_progressbarLoaded ()");
    else
    {
        alert("Please Enter Mandatory Fields");
        $('#studentInfo_form_id').focus();        
    }    
}

function studentcreateandPrint_progressbarLoaded ()
{
    createPopup('ProcessDialog', '', '', true);
    oStudentInformationData = studentInfo_getFormData ();    
    StudentInformationDataProcessor.createandprint(oStudentInformationData, studentInfo_createAndPrintResponse);
}

function studentInfo_createAndPrintResponse(oPrintResponse)
{
    if(oPrintResponse.m_bSuccess)
    {
        populateXMLData (oPrintResponse.m_strStudentXMLData, "applicationstatus/verified/printStudentDetails.xslt", 'printdetailsInfo');
        printDocument();
        HideDialog("dialog");
    }
    else
    {
        informUser("create and print is failed","kError");        
    }
}

function fatherAadharValidate ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nFatherAadharNumber = $("#studentInfo_input_fatherAadharNumber").val();
	StudentInformationDataProcessor.checkAadharExist (oStudentInformationData, aadharResponse);	
}

function motherAadharValidate()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nMotherAadharNumber = $("#studentInfo_input_motherAadharNumber").val();
	StudentInformationDataProcessor.checkAadharExist (oStudentInformationData, aadharResponse);
}

function studentUIDInfo_validate()
{
	return validateForm("oldstudentUIDForm");
}

function aadharResponse(oAadharResponse)
{
	if(oAadharResponse.m_bSuccess)
		alert("The Following Candidate Sibling is already linked to this foundation");
}

function studentInfo_cancel()
{
	HideDialog ("dialog");
}

function studentUIDInfo_cancel()
{
	HideDialog ("dialog");
}

function openTab(oEvent, TabName) 
{
	  var i, StudentInfoTab,tablinks;
	  StudentInfoTab = document.getElementsByClassName("StudentInfoTab");
	  for (i = 0; i < StudentInfoTab.length; i++) 
	  {
	    StudentInfoTab[i].style.display = "none";
	  }
	  
	  tablinks = document.getElementsByClassName("tablinks");
	  for (i = 0; i < tablinks.length; i++) 
	  {
	    tablinks[i].className = tablinks[i].className.replace(" active", "");
	  }
	  
      document.getElementById(TabName).style.display = "block";
	  oEvent.currentTarget.className += " active";
}

function openNextTab(oEvent, TabName, oNextBtn )
{	
	  var i, StudentInfoTab,tablinks;
	  StudentInfoTab = document.getElementsByClassName("StudentInfoTab");
	  for (i = 0; i < StudentInfoTab.length; i++) 
	  {
	    StudentInfoTab[i].style.display = "none";
	  }
	  
	  tablinks = document.getElementsByClassName("tablinks");
	  for (i = 0; i < tablinks.length; i++) 
	  {
	    tablinks[i].className = tablinks[i].className.replace(" active", "");
	  }
	   
	  if(oNextBtn != undefined || oNextBtn != null)
      {
		  oNextBtn = document.getElementById(oNextBtn.id);
		  oNextBtn.className  += " active";
	      document.getElementById(TabName).style.display = "block";
	  }
}

function getImage(studentInfo_input_image,fileInputTypeID, divId)
{
	document.getElementById(divId.id).style.display = "block";
	var oFileSource = document.getElementById(fileInputTypeID.id);
	var studentInfo_input_document = document.getElementById(studentInfo_input_image.id);
	showImage(oFileSource, studentInfo_input_document);
}

function showImage(oFileSource,studentInfo_input_document)
{
     var fr=new FileReader();
     fr.onload = function(e)
                     {
                         studentInfo_input_document.src = this.result;
                     };
     fr.readAsDataURL(oFileSource.files[0]);
    
}
function studentupload_documentPreview (studentInfo_input_previewimageId)
{
    
    var viewImageId = document.getElementById(studentInfo_input_previewimageId.id)
    var fileInputimageurl = document.getElementById (studentInfo_input_previewimageId.id).src;
    m_oStudentInfoMemberData.m_strDocUploadURL = fileInputimageurl;
    loadPage("scholarshipmanagement/student/studentDocumentPreview.html","secondDialog","showDocumentPreview()");    
}


function showDocumentPreview ()
{
    createPopup ('secondDialog', '', '', true);
    document.getElementById('secondDialog').style.position = "fixed";
    $(".imagePreview").attr('src', m_oStudentInfoMemberData.m_strDocUploadURL);    
}


function studentDocument_cancelImagePreview ()
{    
     HideDialog ("secondDialog");
}

function uploadStudentAadharDocuments(div)
{
	m_oStudentInfoMemberData.m_divisionId = div.id;
	var oFormData = new FormData ();
	if($("#aadharInputID").val() != '')
	{
		oFormData.append('studentaadhar',$("#aadharInputID")[0].files[0]);
		oFormData.append('academicId',m_oStudentInfoMemberData.m_nAcademicId);
	}
	AcademicDetailsDataProcessor.uploadDocumentstoS3bucket(oFormData,uploadResponse);    
}

function uploadStudentElectricityDocuments(div)
{
	m_oStudentInfoMemberData.m_divisionId = div.id;
	var oFormData = new FormData ();
	if($("#studentElectricityBill").val() != '')
	{
		oFormData.append('studentelectricitybill',$("#studentElectricityBill")[0].files[0]);
		oFormData.append('academicId',m_oStudentInfoMemberData.m_nAcademicId);
	}
	AcademicDetailsDataProcessor.uploadDocumentstoS3bucket(oFormData,uploadResponse);
}

function uploadFatherAadharDocuments(div)
{
	m_oStudentInfoMemberData.m_divisionId = div.id;
	var oFormData = new FormData ();
	if($("#fatheraadharInputID").val() != '')
	{
		oFormData.append('fatheraadhar',$("#fatheraadharInputID")[0].files[0]);
		oFormData.append('academicId',m_oStudentInfoMemberData.m_nAcademicId);
	}
	AcademicDetailsDataProcessor.uploadDocumentstoS3bucket(oFormData,uploadResponse);
}

function uploadMotherAadharDocuments(div)
{
	m_oStudentInfoMemberData.m_divisionId = div.id;
	var oFormData = new FormData ();
	if($("#motheraadharInputID").val() != '')
	{
		oFormData.append('motheraadhar',$("#motheraadharInputID")[0].files[0]);
		oFormData.append('academicId',m_oStudentInfoMemberData.m_nAcademicId);
	}
	AcademicDetailsDataProcessor.uploadDocumentstoS3bucket(oFormData,uploadResponse);
}

function uploadStudentMarksCard1(div)
{
	m_oStudentInfoMemberData.m_divisionId = div.id;
	var oFormData = new FormData ();
	if($("#studentMarksCard1").val() != '')
	{
		oFormData.append('studentmarkscard1',$("#studentMarksCard1")[0].files[0]);
		oFormData.append('academicId',m_oStudentInfoMemberData.m_nAcademicId);
	}
	AcademicDetailsDataProcessor.uploadDocumentstoS3bucket(oFormData,uploadResponse);
}

function uploadStudentMarksCard2(div)
{
	m_oStudentInfoMemberData.m_divisionId = div.id;
	var oFormData = new FormData ();
	if($("#studentMarksCard2").val() != '')
	{
		oFormData.append('studentmarkscard2',$("#studentMarksCard2")[0].files[0]);
		oFormData.append('academicId',m_oStudentInfoMemberData.m_nAcademicId);
	}
	AcademicDetailsDataProcessor.uploadDocumentstoS3bucket(oFormData,uploadResponse);
}

function uploadAdditionalDocuments(div)
{
	m_oStudentInfoMemberData.m_divisionId = div.id;
	var oFormData = new FormData ();
	if($("#additionalDocuments").val() != '')
	{
		oFormData.append('otherdocuments',$("#additionalDocuments")[0].files[0]);
		oFormData.append('academicId',m_oStudentInfoMemberData.m_nAcademicId);
	}
	AcademicDetailsDataProcessor.uploadDocumentstoS3bucket(oFormData,uploadResponse);
}

function uploadResponse(oUploadResponse)
{
	if(oUploadResponse.m_bSuccess)
	{
		document.getElementById(m_oStudentInfoMemberData.m_divisionId).style.display = "none";
		informUser("Document Uploaded Success","kSuccess");
	}
	else
	{
		informUser("Document Uploaded Failed","kError");
	}
}


function getSiblings(divId)
{
	document.getElementById(divId.id).style.display = "block";
}

function hideSiblings(divId)
{
	document.getElementById(divId.id).style.display = "none";
}

function siblingsAddSiblings () 
{
	if(m_oStudentInfoMemberData.m_nRowSiblingsUIDIdCount != -1)
	{
		$("#siblings").append('<tr id=rowCountId><td class="fieldHeading">UID</td> <td><input type="text"id="studentInfo_input_SiblingsUID'+(m_oStudentInfoMemberData.m_nRowSiblingsUIDIdCount++)+'" class="zenith" style="margin-right: 60px" maxlength = "4"  onchange="scarchSiblingsUID(rowCountId)"/></td><td class="fieldHeading">Name</td><td><input type="text"id="studentInfo_input_SiblingsName'+(m_oStudentInfoMemberData.m_nRowSiblingsNameCount++)+'" class="zenith" /></td><td class="fieldHeading">Studying</td><td><input type="text" id="studentInfo_input_SiblingsStudying'+(m_oStudentInfoMemberData.m_nRowSiblingStudyingCount++)+'" class="zenith" /></td><td class="fieldHeading">School/College</td><td><input type="text" id="studentInfo_input_SiblingsSchoolCollege'+(m_oStudentInfoMemberData.m_nRowSiblingSchoolCollegeCount++)+'" class="zenith" /></td><td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete Siblings" class = "removeSiblings" onClick="deletSiblings()"/> </td></tr>');
		m_oStudentInfoMemberData.m_nNewSiblingRowCount = m_oStudentInfoMemberData.m_nUpdatedEditSiblingsUIDIdRowCount = m_oStudentInfoMemberData.m_nRowSiblingsUIDIdCount;
		m_oStudentInfoMemberData.m_nUpdatedEditSiblingsNameRowCount = m_oStudentInfoMemberData.m_nRowSiblingsNameCount;
		m_oStudentInfoMemberData.m_nUpdatedEditSiblingsStudyingRowCount = m_oStudentInfoMemberData.m_nRowSiblingsStudyingCount;
		m_oStudentInfoMemberData.m_nUpdatedEditSiblingsSchoolCollegeRowCount = m_oStudentInfoMemberData.m_nRowSiblingsSchoolCollegeCount;
	}
	else
	{
		$("#siblings").append('<tr id=rowCountId><td class="fieldHeading">UID</td> <td><input type="text" id="studentInfo_input_SiblingsUID'+(m_oStudentInfoMemberData.m_nSiblingsUIDId++)+'" class="zenith" style="margin-right: 60px" maxlength = "4" onchange="scarchSiblingsUID(rowCountId)"/></td><td class="fieldHeading">Name</td><td><input type="text"id="studentInfo_input_SiblingsName'+(m_oStudentInfoMemberData.m_nSiblingsName++)+'" class="zenith" /></td><td class="fieldHeading">Studying</td><td><input type="text" id="studentInfo_input_SiblingsStudying'+(m_oStudentInfoMemberData.m_nSiblingsStudying++)+'" class="zenith" /></td><td class="fieldHeading">School/College</td><td><input type="text" id="studentInfo_input_SiblingsSchoolCollege'+(m_oStudentInfoMemberData.m_nSiblingsSchoolCollege++)+'" class="zenith" /></td><td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="DeleteSiblings" class = "removeSiblings" onClick="deleteNewSiblings()"/> </td></tr>');
		m_oStudentInfoMemberData.m_nNewSiblingRowCount = m_oStudentInfoMemberData.m_nUpdatedSiblingsUIDIdRowCount = m_oStudentInfoMemberData.m_nSiblingsUIDId;
		m_oStudentInfoMemberData.m_nUpdatedSiblingsNameRowCount = m_oStudentInfoMemberData.m_nSiblingsName;
		m_oStudentInfoMemberData.m_nUpdatedSiblingsStudyingRowCount = m_oStudentInfoMemberData.m_nSiblingsStudying;
		m_oStudentInfoMemberData.m_nUpdatedSiblingsSchoolCollegeRowCount = m_oStudentInfoMemberData.m_nSiblingsSchoolCollege;		
	}

}
function deleteNewSiblings (clicked_id)
{
var bUserConfirm = getUserConfirmation("Are you sure do you want to delete?");
if(bUserConfirm)
{

	$("#siblings").on('click','.removeSiblings',function() { $(this).parent().parent().remove(); });
	var oSiblingsDetails = new SiblingsDetails();
	oSiblingsDetails.m_nSiblingId = $("#studentInfo_input_SiblingsUID"+clicked_id).val();
	checkSiblings(oSiblingsDetails);
}
	var siblingsrows = document.getElementById("siblings");   
    m_oStudentInfoMemberData.m_nSiblingsUIDId = siblingsrows.rows.length;
    m_oStudentInfoMemberData.m_nSiblingsName = siblingsrows.rows.length;
    m_oStudentInfoMemberData.m_nSiblingsStudying = siblingsrows.rows.length;
    m_oStudentInfoMemberData.m_nSiblingsSchoolCollege =  siblingsrows.rows.length;
}

function checkSiblings(oSiblingsDetails)
{
	if(oSiblingsDetails.m_nSinlingsId != undefined)
		StudentInformationDataProcessor.deleteSiblings(oSiblingsDetails,deleteSiblingsResponse);
}

function deletSiblings() 
{
	$("#siblings").on('click','.removeSiblings',function() { $(this).parent().parent().remove(); });
	var siblingsrows = document.getElementById("siblings");   
    m_oStudentInfoMemberData.m_nRowSiblingsUIDIdCount = siblingsrows.rows.length;
    m_oStudentInfoMemberData.m_nRowSiblingsNameCount = siblingsrows.rows.length;
    m_oStudentInfoMemberData.m_nRowSiblingsStudyingCount = siblingsrows.rows.length;
    m_oStudentInfoMemberData.m_nRowSiblingsSchoolCollegeCount =  siblingsrows.rows.length;
}

function deleteSiblingsResponse ()
{
	student_displayInfo("siblings deleted successfully");
}

function getSiblingsDetails()
{
	var arrSiblingDetails = null;
	var siblingDetails = m_oStudentInfoMemberData.m_arrSiblingsDetails;
	if(siblingDetails.length == 0)
		arrSiblingDetails = getNewSiblingsDetails ();
	else if(siblingDetails.length >= 1)
		arrSiblingDetails = addSibilingsDetails();
	return arrSiblingDetails;
}

function addSibilingsDetails ()
{
	var oArrSiblingsDetails = new Array();
	var arrSiblingDetails = m_oStudentInfoMemberData.m_arrSiblingsDetails;	
	var siblingDetails = document.getElementById("siblings");
	if(siblingDetails.rows.length == arrSiblingDetails.length)
	{
		for(var nIndex = 0; nIndex < arrSiblingDetails.length; nIndex++)
		{
			var oSiblingsDeatils = new SiblingsDetails();				    
			oSiblingsDeatils.m_nSiblingId = arrSiblingDetails[nIndex].m_nSiblingId;				
			oSiblingsDeatils.m_nZenithUID = $("#studentInfo_input_SiblingsUID"+nIndex).val();
			oSiblingsDeatils.m_strSibilingName = $("#studentInfo_input_SiblingsName"+nIndex).val();
			oSiblingsDeatils.m_strStudying = $("#studentInfo_input_SiblingsStudying"+nIndex).val();
			oSiblingsDeatils.m_strStudyingInstitution = $("#studentInfo_input_SiblingsSchoolCollege"+nIndex).val();
			oArrSiblingsDetails.push(oSiblingsDeatils);
		}
	}
	else
	{
		for(var nIndex = 0; nIndex < m_oStudentInfoMemberData.m_nUpdatedEditSiblingsUIDIdRowCount; nIndex++)
		{
			var oSiblingsDeatils = new SiblingsDetails();			
			if(($("#studentInfo_input_SiblingsUID"+nIndex).val() != '' && $("#studentInfo_input_SiblingsName"+nIndex).val() !='' && $("#studentInfo_input_SiblingsStudying"+nIndex).val() != '' && $("#studentInfo_input_SiblingsSchoolCollege"+nIndex).val() !='')&&($("#studentInfo_input_SiblingsUID"+nIndex).val() != undefined && $("#studentInfo_input_SiblingsName"+nIndex).val() != undefined  && $("#studentInfo_input_SiblingsStudying"+nIndex).val() != undefined && $("#studentInfo_input_SiblingsSchoolCollege"+nIndex).val() != undefined  ))
	    	{					   
				oSiblingsDeatils.m_nZenithUID = $("#studentInfo_input_SiblingsUID"+nIndex).val();
				oSiblingsDeatils.m_strSibilingName = $("#studentInfo_input_SiblingsName"+nIndex).val();
				oSiblingsDeatils.m_strStudying = $("#studentInfo_input_SiblingsStudying"+nIndex).val();
				oSiblingsDeatils.m_strStudyingInstitution = $("#studentInfo_input_SiblingsSchoolCollege"+nIndex).val();
				oArrSiblingsDetails.push(oSiblingsDeatils);
	    	}
			if((nIndex < arrSiblingDetails.length))
			{
				oSiblingsDeatils.m_nSiblingId = arrSiblingDetails[nIndex].m_nSiblingId;	
			}
		}		
	}	
	return oArrSiblingsDetails;
}
function getNewSiblingsDetails ()
{
	var oArrSiblingsDetails = new Array();
	checkSiblingsRowCount();
	for(var nIndex=0; nIndex < m_oStudentInfoMemberData.m_nUpdatedSiblingsUIDIdRowCount; nIndex++)
	{
		var oSiblingsDeatils = new SiblingsDetails();
		if(($("#studentInfo_input_SiblingsUID"+nIndex).val() != '' && $("#studentInfo_input_SiblingsName"+nIndex).val() !='' && $("#studentInfo_input_SiblingsStudying"+nIndex).val() != '' && $("#studentInfo_input_SiblingsSchoolCollege"+nIndex).val() !='')&&($("#studentInfo_input_SiblingsUID"+nIndex).val() != undefined && $("#studentInfo_input_SiblingsName"+nIndex).val() != undefined  && $("#studentInfo_input_SiblingsStudying"+nIndex).val() != undefined && $("#studentInfo_input_SiblingsSchoolCollege"+nIndex).val() != undefined  ))
		{
			oSiblingsDeatils.m_nZenithUID = $("#studentInfo_input_SiblingsUID"+nIndex).val();
			oSiblingsDeatils.m_strSibilingName = $("#studentInfo_input_SiblingsName"+nIndex).val();
			oSiblingsDeatils.m_strStudying = $("#studentInfo_input_SiblingsStudying"+nIndex).val();
			oSiblingsDeatils.m_strStudyingInstitution = $("#studentInfo_input_SiblingsSchoolCollege"+nIndex).val();
			oArrSiblingsDetails.push(oSiblingsDeatils);			
		}		
	}	
	return oArrSiblingsDetails;
}

function checkSiblingsRowCount ()
{
	var result = m_oStudentInfoMemberData.m_nUpdatedSiblingsUIDIdRowCount;
	if(result == 0)
		 m_oStudentInfoMemberData.m_nUpdatedSiblingsUIDIdRowCount = 1;
}


function scarchSiblingsUID(oRowCountId) 
{
	
	
	m_oStudentInfoMemberData.m_nSiblingRowId = 0;
	if(m_oStudentInfoMemberData.m_nNewSiblingRowCount != 0 && m_oStudentInfoMemberData.m_nNewSiblingRowCount != undefined)
    {
		m_oStudentInfoMemberData.m_nSiblingRowId = --m_oStudentInfoMemberData.m_nNewSiblingRowCount;
    }
	var oStudentInformationData = new  StudentInformationData();
	oStudentInformationData.m_nUID = $("#studentInfo_input_SiblingsUID"+m_oStudentInfoMemberData.m_nSiblingRowId).val();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	StudentInformationDataProcessor.getStudentDataUID (oStudentInformationData, studentInfo_setSiblingsData);
}


function studentInfo_setSiblingsData(oResponse) 
{
	if(oResponse.m_bSuccess)
	{
		 $("#studentInfo_input_SiblingsUID"+m_oStudentInfoMemberData.m_nSiblingRowId).val(oResponse.m_arrStudentInformationData[0].m_nUID);
		 $("#studentInfo_input_SiblingsName"+m_oStudentInfoMemberData.m_nSiblingRowId).val(oResponse.m_arrStudentInformationData[0].m_strStudentName);
		 $("#studentInfo_input_SiblingsStudying"+m_oStudentInfoMemberData.m_nSiblingRowId).val(oResponse.m_arrStudentInformationData[0].m_oAcademicDetails[0].m_oCourseInformationData.m_strShortCourseName);
		 $("#studentInfo_input_SiblingsSchoolCollege"+m_oStudentInfoMemberData.m_nSiblingRowId).val(oResponse.m_arrStudentInformationData[0].m_oAcademicDetails[0].m_oInstitutionInformationData.m_strInstitutionName);	
		 
	}

} 

