var studentInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js',
	'widgets/scholarshipmanagement/courselist/CourseInformationData.js',
	'widgets/scholarshipmanagement/scholarshipdetails/ScholarshipDetails.js',
	'widgets/scholarshipmanagement/academicdetails/AcademicDetails.js',
	'widgets/usermanagement/facilitator/FacilitatorInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js'
];

 includeDataObjects (studentInfo_includeDataObjects, "studentInfo_loaded()");

function studentInfo_memberData ()
{
	this.m_oStudentData = new StudentInformationData();
	this.m_nStudentId = -1;
	this.m_strImageName = "";
	this.m_bIsNew = false;
	this.m_oInstitutionDataRow = new InstitutionInformationData ();
	this.m_oCourseDataRow = new CourseInformationData();
	this.m_oformData = new FormData();
	this.m_arrStudentUIDData = new Array();
	this.m_studentInstitution = "";
	this.m_studentCourse = "";
	this.m_studentDateofBirth = "";
	this.m_nAcademicId = -1;
	this.m_nSholarshipId = -1;
	this.m_arrScholarshipDetails = new Array ();
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
	$('#student_div_tabs').tabs ();
	student_academicyearList();
	student_institutionsNamelistCombobox();
	student_courseNamesListCombobox();
	student_coursefee ();
	studentReligionDropDown();
	studentParentalStatusDropDown();
	studentScoreDropDown();
	student_facilitatorlistCombobox();
}

function student_academicyearList ()
{
	var oAcademicYear = new AcademicYear();
	AcademicYearProcessor.list(oAcademicYear,"m_strAcademicYear","asc",0,0,academicyearResponse);	
}

function academicyearResponse(oYearResponse)
{
	populateYear("select_student_academicyear",oYearResponse);
}

function populateYear(academicyear,oYearResponse)
{
	var arrAcademicYears = new Array();
	for(var nIndex = 0; nIndex < oYearResponse.m_arrAcademicYear.length; nIndex++)
	{
		arrAcademicYears.push(CreateOption(oYearResponse.m_arrAcademicYear[nIndex].m_strAcademicYear,oYearResponse.m_arrAcademicYear[nIndex].m_strAcademicYear));		
	}
	PopulateDD(academicyear,arrAcademicYears);	
}

function student_facilitatorlistCombobox ()
{
	$('#selectStudentInfo_input_studentfacilitator').combobox
	({
		valueField:'m_nFacilitatorId',
	    textField:'m_strFacilitatorName',
	    selectOnNavigation: false,
	    loader: getFilteredFacilitatorData,
		mode: 'remote',
		formatter:function(row)
    	{
        	var opts = $(this).combobox('options');
        	return decodeURIComponent(row[opts.textField]);
    	},
		onSelect:function(row)
	    {
			m_oStudentInfoMemberData.m_oFacilitatorDataRow = row;
    		setFacilitatorName ();
	    }
	 });
	var facilitatorTextBox = $('#selectStudentInfo_input_studentfacilitator').combobox('textbox');
	facilitatorTextBox.bind('keydown', function (e)
		    {
		      	facilitator_handleKeyboardNavigation (e);
		    });
	
}

var getFilteredFacilitatorData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oFacilitatorInformationData = new FacilitatorInformationData ();
		oFacilitatorInformationData.m_strFacilitatorName = strQuery;
		FacilitatorInformationDataProcessor.getFacilitatorSuggesstions (oFacilitatorInformationData, "", "", function(oFacilitatorResponse)
				{
					var arrFacilitatorInfo = new Array ();
					for(var nIndex=0; nIndex< oFacilitatorResponse.m_arrFacilitatorInformationData.length; nIndex++)
				    {
						arrFacilitatorInfo.push(oFacilitatorResponse.m_arrFacilitatorInformationData[nIndex]);
						arrFacilitatorInfo[nIndex].m_strFacilitatorName = encodeURIComponent(oFacilitatorResponse.m_arrFacilitatorInformationData[nIndex].m_strFacilitatorName);
				    }
					success(arrFacilitatorInfo);
				});
	}
	else
		success(new Array ());
	
}

function facilitator_handleKeyboardNavigation (e)
{
	assert.isObject(e, "e expected to be an Object.");
	if(e.keyCode == 13)
		setFacilitatorName();
}

function setFacilitatorName ()
{
	var strFacilitatorName = decodeURIComponent(m_oStudentInfoMemberData.m_oFacilitatorDataRow[$('#selectStudentInfo_input_studentfacilitator').combobox('options').textField])
	$("#selectStudentInfo_input_studentfacilitator").combobox('setText',strFacilitatorName);	
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

function student_coursefee ()
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
    var scholarshiprows = document.getElementById("scholarship_Organization");    
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

function student_courseNamesListCombobox()
{
	$('#select_input_studentcourse').combobox
	({
		valueField:'m_nCourseId',
	    textField:'m_strShortCourseName',
	    selectOnNavigation: false,
	    loader: getFilteredCourseData,
		mode: 'remote',
		formatter:function(row)
    	{
        	var opts = $(this).combobox('options');
        	return decodeURIComponent(row[opts.textField]);
    	},
		onSelect:function(row)
	    {
			m_oStudentInfoMemberData.m_oCourseDataRow = row;
    		course_setCourseName ();
	    }
	 });
	var courseTextBox = $('#select_input_studentcourse').combobox('textbox');
		courseTextBox.bind('keydown', function (e)
		    {
		      	course_handleKeyboardNavigation (e);
		    });
}

function course_handleKeyboardNavigation(e)
{
	assert.isObject(e, "e expected to be an Object.");
	if(e.keyCode == 13)
		course_setCourseName();
}

function course_setCourseName ()
{
	var strCourseName = decodeURIComponent(m_oStudentInfoMemberData.m_oCourseDataRow[$('#select_input_studentcourse').combobox('options').textField])
	$("#select_input_studentcourse").combobox('setText',strCourseName);
}

var getFilteredCourseData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oCourseInformationData = new CourseInformationData ();
		oCourseInformationData.m_strShortCourseName = strQuery;
		CourseInformationDataProcessor.getCourseSuggesstions (oCourseInformationData, "", "", function(oCourseResponse)
				{
					var arrCourseInfo = new Array ();
					for(var nIndex=0; nIndex< oCourseResponse.m_arrCourseInformationData.length; nIndex++)
				    {
						arrCourseInfo.push(oCourseResponse.m_arrCourseInformationData[nIndex]);
						arrCourseInfo[nIndex].m_strShortCourseName = encodeURIComponent(oCourseResponse.m_arrCourseInformationData[nIndex].m_strShortCourseName);
				    }
					success(arrCourseInfo);
				});
	}
	else
		success(new Array ());
}



function student_institutionsNamelistCombobox ()
{
	$('#select_input_academic_name').combobox
	({
		valueField:'m_nInstitutionId',
	    textField:'m_strInstitutionName',
	    selectOnNavigation: false,
	    loader: getFilteredInstitutionData,
		mode: 'remote',
		formatter:function(row)
    	{
        	var opts = $(this).combobox('options');
        	return decodeURIComponent(row[opts.textField]);
    	},
		onSelect:function(row)
	    {
			m_oStudentInfoMemberData.m_oInstitutionDataRow = row;
    		institution_setInstitutionName ();
	    }
	 });
	var institutionTextBox = $('#select_input_academic_name').combobox('textbox');
		institutionTextBox.bind('keydown', function (e)
		    {
		      	institution_handleKeyboardNavigation (e);
		    });
}

function institution_handleKeyboardNavigation (e)
{
	assert.isObject(e, "e expected to be an Object.");
	if(e.keyCode == 13)
		institution_setInstitutionName ();
}

function institution_setInstitutionName ()
{
	var strInstitutionName = decodeURIComponent(m_oStudentInfoMemberData.m_oInstitutionDataRow[$('#select_input_academic_name').combobox('options').textField])
	$("#select_input_academic_name").combobox('setText',strInstitutionName);
}

var getFilteredInstitutionData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oInstitutionInformationData = new InstitutionInformationData ();
		oInstitutionInformationData.m_strInstitutionName = strQuery;
		InstitutionInformationDataProcessor.getInstitutionSuggesstions (oInstitutionInformationData, "", "", function(oInstitutionResponse)
				{
					var arrInstitutionInfo = new Array ();
					for(var nIndex=0; nIndex< oInstitutionResponse.m_arrInstitutionInformationData.length; nIndex++)
				    {
						arrInstitutionInfo.push(oInstitutionResponse.m_arrInstitutionInformationData[nIndex]);
						arrInstitutionInfo[nIndex].m_strInstitutionName = encodeURIComponent(oInstitutionResponse.m_arrInstitutionInformationData[nIndex].m_strInstitutionName);
				    }
					success(arrInstitutionInfo);
				});
	}
	else
		success(new Array ());
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
	document.getElementById("studentInfo_button_submit").setAttribute('update', true);
	document.getElementById("studentInfo_button_submit").innerHTML = "Update";
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
	oStudentInformationData.m_oFacilitatorInformationData.m_nFacilitatorId = $("#selectStudentInfo_input_studentfacilitator").combobox('getValue');
	 if(document.getElementById("studentInfo_input_male").checked)
		 oStudentInformationData.m_strGender = document.getElementById("studentInfo_input_male").value;
	 else if(document.getElementById("studentInfo_input_female").checked)
		 oStudentInformationData.m_strGender = document.getElementById("studentInfo_input_female").value;
	 else
		 oStudentInformationData.m_strGender = document.getElementById("studentInfo_input_other").value;	
	if($("#studentInfo_input_studentPhoto").val() == '')
		oStudentInformationData.m_strStudentImageName = m_oStudentInfoMemberData.m_strImageName;			
	else
		oStudentInformationData.m_strStudentImageName = $("#studentInfo_input_studentPhoto").val().replace(/.*(\/|\\)/, '');
	oStudentInformationData.m_dDateOfBirth = $("#student_input_dateofbirth").val();
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
	      /*Academic details*/
	oStudentInformationData.m_oAcademicDetails = getAcademicDetails ();			
	
	return oStudentInformationData;
}

function getAcademicDetails ()
{
	var oArrAcademicDetails = new Array();	
	var oAcademicDetails = new AcademicDetails ();	
	oAcademicDetails.m_oInstitutionInformationData = new InstitutionInformationData ();
	oAcademicDetails.m_oInstitutionInformationData.m_nInstitutionId = $("#select_input_academic_name").combobox('getValue');
	oAcademicDetails.m_oCourseInformationData = new CourseInformationData();
	oAcademicDetails.m_oCourseInformationData.m_nCourseId = $("#select_input_studentcourse").combobox('getValue');	
	if(m_oStudentInfoMemberData.m_strAcademicYear == $("#select_student_academicyear").val())
		oAcademicDetails.m_nAcademicId = m_oStudentInfoMemberData.m_nAcademicId ;	
	oAcademicDetails.m_strAcademicYear = $("#select_student_academicyear").val();
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
		    if(m_oStudentInfoMemberData.m_strAcademicYear == $("#select_student_academicyear").val())
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
			if((nIndex < arrAddscholarshipDetails.length) && (m_oStudentInfoMemberData.m_strAcademicYear == $("#select_student_academicyear").val()))
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
			var strImageName = oFormData.get("studentimage");
			if(strImageName == "")
			{
				student_details_updated();
			}
			else if(oStudentInfoResponse.m_arrStudentInformationData[0].m_strStudentImageName == "" || m_oStudentInfoMemberData.m_strImageName != strImageName)
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
	m_oStudentInfoMemberData.m_strImageName = oStudentInfoData.m_strStudentImageName;
	m_oStudentInfoMemberData.m_studentDateofBirth = oStudentInfoData.m_dDateOfBirth;
	m_oStudentInfoMemberData.m_nAcademicId = oStudentInfoData.m_oAcademicDetails[0].m_nAcademicId;
	m_oStudentInfoMemberData.m_strAcademicYear = oStudentInfoData.m_oAcademicDetails[0].m_strAcademicYear;
	m_oStudentInfoMemberData.m_nInstitutionId = oStudentInfoData.m_oAcademicDetails[0].m_oInstitutionInformationData.m_nInstitutionId;
	m_oStudentInfoMemberData.m_nCourseId = oStudentInfoData.m_oAcademicDetails[0].m_oCourseInformationData.m_nCourseId;
	m_oStudentInfoMemberData.m_arrScholarshipDetails = 	oStudentInfoData.m_oAcademicDetails[0].m_oScholarshipDetails;
	m_oStudentInfoMemberData.m_nRowOrgCount = m_oStudentInfoMemberData.m_arrScholarshipDetails.length;
	m_oStudentInfoMemberData.m_nRowOrgAmountCount = m_oStudentInfoMemberData.m_arrScholarshipDetails.length;
	m_oStudentInfoMemberData.m_nStudentId = oStudentInfoData.m_nStudentId;
	 $("#studentInfo_input_studentUIDNumber").val(oStudentInfoData.m_nUID);
	// $("#select_student_academicyear").val(oStudentInfoData.m_oAcademicYear.m_strAcademicYear);
	 $("#studentInfo_input_studentAadharNumber").val(oStudentInfoData.m_nStudentAadharNumber);
	 $("#studentInfo_input_studentName").val(oStudentInfoData.m_strStudentName);
	 facilitatorPopulateCombobox(oStudentInfoData);
	 $('#selectStudentInfo_input_studentfacilitator').combobox('select', oStudentInfoData.m_oFacilitatorInformationData.m_nFacilitatorId);
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
	 document.getElementById("student_input_dateofbirth").value = studentDateofBirth(oStudentInfoData.m_dDateOfBirth);
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
	 $("#select_input_academic_name").combobox('select',oStudentInfoData.m_oAcademicDetails[0].m_oInstitutionInformationData.m_nInstitutionId);	
	 $("#select_input_studentcourse").combobox('select',oStudentInfoData.m_oAcademicDetails[0].m_oCourseInformationData.m_nCourseId);	
	 $("#select_input_studentSpecialization").val(oStudentInfoData.m_oAcademicDetails[0].m_strSpecialization);
	 $("#studentInfo_input_studentScore").val(oStudentInfoData.m_oAcademicDetails[0].m_strStudentScore);
	 $("#academicInfo_input_annualfee").val(oStudentInfoData.m_oAcademicDetails[0].m_fAnnualFee);
	 $("#academicInfo_input_paidfee").val(oStudentInfoData.m_oAcademicDetails[0].m_fPaidFee);	 
	 substraction();
	 /* Scholarship Details*/
	 for(var nIndex = 0; nIndex < m_oStudentInfoMemberData.m_arrScholarshipDetails.length; nIndex++ )
	 {
		 if(nIndex !=0)
			 $("#scholarship_Organization").append('<tr><td class="fieldHeading">Organization</td><td style="padding-right: 10px"> </td><td><input  type="text" id="scholarshipInfo_input_organization'+(nIndex)+'" class="zenith"/></td><td style="padding-right: 10px"> </td><td class="fieldHeading">Amount(Rs)</td><td style="padding-right: 10px"> </td><td><input  type="text" id="scholarshipInfo_input_organizationamount'+(nIndex)+'" class="zenith" onkeyup="validateNumber(this)"/></td><td style="padding-right: 10px"> </td><td> <img src="images/delete.png" width="20" align="center" id="'+(nIndex)+'" title="Delete Organization" class = "removeOrganization" onClick="scholarship_removeEditOrganizationrow(this.id)"/> </td></tr>');		
		 $("#scholarshipInfo_input_organization"+nIndex).val(m_oStudentInfoMemberData.m_arrScholarshipDetails[nIndex].m_strOrganizationName);
		 $("#scholarshipInfo_input_organizationamount"+nIndex).val(m_oStudentInfoMemberData.m_arrScholarshipDetails[nIndex].m_fAmount);
	 }
	 initFormValidateBoxes ("studentInfo_form_id");
}

function facilitatorPopulateCombobox(oStudentInfoData)
{
	assert.isObject(oStudentInfoData, "oStudentInfoData expected to be an Object.");
	assert( Object.keys(oStudentInfoData).length >0 , "oStudentInfoData cannot be an empty .");// checks for non emptyness 
	var oFacilitatorInformationData = new FacilitatorInformationData ();
	oFacilitatorInformationData.m_strFacilitatorName = oStudentInfoData.m_oFacilitatorInformationData.m_strFacilitatorName;
	FacilitatorInformationDataProcessor.getFacilitatorSuggesstions (oFacilitatorInformationData, "", "", function(oFacilitatorResponse)
			{
				var arrFacilitatorInfo = new Array ();
				for(var nIndex=0; nIndex< oFacilitatorResponse.m_arrFacilitatorInformationData.length; nIndex++)
			    {
					arrFacilitatorInfo.push(oFacilitatorResponse.m_arrFacilitatorInformationData[nIndex]);
					arrFacilitatorInfo[nIndex].m_strFacilitatorName = encodeURIComponent(oFacilitatorResponse.m_arrFacilitatorInformationData[nIndex].m_strFacilitatorName);
			    }
				$('#selectStudentInfo_input_studentfacilitator').combobox('loadData',arrFacilitatorInfo)
			});
	
}
function institutionPopulateCombobox(oStudentInfoData)
{
	assert.isObject(oStudentInfoData, "oStudentInfoData expected to be an Object.");
	assert( Object.keys(oStudentInfoData).length >0 , "oStudentInfoData cannot be an empty .");// checks for non emptyness
	var oInstitutionInformationData = new InstitutionInformationData ();
	oInstitutionInformationData.m_strInstitutionName = oStudentInfoData.m_oAcademicDetails[0].m_oInstitutionInformationData.m_strInstitutionName;
	InstitutionInformationDataProcessor.getInstitutionSuggesstions (oInstitutionInformationData, "", "", function(oInstitutionResponse)
			{
				var arrInstitutionInfo = new Array ();
				for(var nIndex=0; nIndex< oInstitutionResponse.m_arrInstitutionInformationData.length; nIndex++)
			    {
					arrInstitutionInfo.push(oInstitutionResponse.m_arrInstitutionInformationData[nIndex]);
					arrInstitutionInfo[nIndex].m_strInstitutionName = encodeURIComponent(oInstitutionResponse.m_arrInstitutionInformationData[nIndex].m_strInstitutionName);
			    }
				$('#select_input_academic_name').combobox('loadData',arrInstitutionInfo)
			});
}

function coursePopulateCombobox(oStudentInfoData)
{
	assert.isObject(oStudentInfoData, "oStudentInfoData expected to be an Object.");
	assert( Object.keys(oStudentInfoData).length >0 , "oStudentInfoData cannot be an empty .");// checks for non emptyness
	var oCourseInformationData = new CourseInformationData ();
	oCourseInformationData.m_strShortCourseName = oStudentInfoData.m_oAcademicDetails[0].m_oCourseInformationData.m_strShortCourseName;
	CourseInformationDataProcessor.getCourseSuggesstions (oCourseInformationData, "", "", function(oCourseResponse)
			{
				var arrCourseInfo = new Array ();
				for(var nIndex=0; nIndex< oCourseResponse.m_arrCourseInformationData.length; nIndex++)
			    {
					arrCourseInfo.push(oCourseResponse.m_arrCourseInformationData[nIndex]);
					arrCourseInfo[nIndex].m_strShortCourseName = encodeURIComponent(oCourseResponse.m_arrCourseInformationData[nIndex].m_strShortCourseName);
			    }
				$('#select_input_studentcourse').combobox('loadData',arrCourseInfo)
			});
}

function studentDateofBirth (strdateofBirth)
{
	var strStudentDateOfBirth = "/Date("+strdateofBirth+")/";
	var date = new Date(parseFloat(strStudentDateOfBirth.substr(6)));	
	var strDate = date.getFullYear() + "-" +("0" + (date.getMonth() + 1)).slice(-2) + "-" +date.getDate();	
	return strDate;
}

function studentUIDInfo_submit ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nUID = $("#studentInfo_input_studentUID").val();
	StudentInformationDataProcessor.getStudentUID (oStudentInformationData, studentInfo_gotStudentUIDData);
	
}

function studentInfo_gotStudentUIDData (oStudentUIDResponse)
{
	HideDialog ("ProcessDialog");
	if(oStudentUIDResponse.m_bSuccess)
	{
		m_oStudentInfoMemberData.m_arrStudentUIDData = oStudentUIDResponse;
		loadPage ("scholarshipmanagement/student/studentInfo.html", "dialog", "studentUIDInfo_gotData()");
	}
	else
	{
		alert("Student UID does not exist!");
		$('#oldstudentUIDForm').focus();
	}
}

function searchStudentUID ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nUID = $("#studentInfo_input_studentUIDNumber").val();
	StudentInformationDataProcessor.getStudentUID (oStudentInformationData, studentInfo_setStudentUIDData);
}

function studentInfo_setStudentUIDData (oStudentSetUIDResponse)
{
	
	HideDialog ("ProcessDialog");
	if(oStudentSetUIDResponse.m_bSuccess)
	{
		
		document.getElementById("studentInfo_button_submit").setAttribute('update', true);
		document.getElementById("studentInfo_button_submit").innerHTML = "Update";
		studentInfo_gotData(oStudentSetUIDResponse);		
	}
	else
	{		
		$('#studentInfo_form_id').focus();
	}

}

function studentUIDInfo_gotData ()
{
	
	studentInfo_init ();
	document.getElementById("studentInfo_button_submit").setAttribute('update', true);
	document.getElementById("studentInfo_button_submit").innerHTML = "Update";	
	studentInfo_gotData(m_oStudentInfoMemberData.m_arrStudentUIDData);	
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
