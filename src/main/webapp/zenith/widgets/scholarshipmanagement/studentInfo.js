var studentInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/StudentInformationData.js',
	'widgets/scholarshipmanagement/InstitutionInformationData.js',
	'widgets/scholarshipmanagement/CourseInformationData.js'
];

 includeDataObjects (studentInfo_includeDataObjects, "studentInfo_loaded()");

function studentInfo_memberData ()
{
	this.m_oStudentData = new StudentInformationData();
	this.m_nStudentId = -1;
	this.m_strImageName = "";
	this.m_oformData = new FormData();
}

var m_oStudentInfoMemberData = new studentInfo_memberData ();

function studentInfo_new ()
{
	studentInfo_init();
	initFormValidateBoxes ("studentInfo_form_id");
}

function studentInfo_init ()
{
	createPopup("dialog", "#studentInfo_button_submit", "#studentInfo_button_cancel", true);
	$('#student_div_tabs').tabs ();
	student_institutionsNamelistCombox();
	student_courseNamesListCombox();
	student_coursefee ();
}

function student_coursefee ()
{
	$(document).ready(function()
			{
			    substraction();
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

function student_courseNamesListCombox()
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



function student_institutionsNamelistCombox ()
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
	oStudentInformationData.m_nUID = $("#studentInfo_input_studentUIDNumber").val();
	oStudentInformationData.m_strStudentName = $("#studentInfo_input_studentName").val();
	var selectedvalue = $("input:radio[name=studentInfo_input_gender]:checked").val();
	oStudentInformationData.m_strGender = selectedvalue;
	if($("#studentInfo_input_studentPhoto").val() == '')
		oStudentInformationData.m_strStudentImageName = m_oStudentInfoMemberData.m_strImageName;			
	else
		oStudentInformationData.m_strStudentImageName = $("#studentInfo_input_studentPhoto").val().replace(/.*(\/|\\)/, '');
	oStudentInformationData.m_strFatherName = $("#studentInfo_input_fathername").val();
	oStudentInformationData.m_strFatherOccupation = $("#studentInfo_input_fatheroccupation").val();
	oStudentInformationData.m_strMotherName = $("#studentInfo_input_mothername").val();
	oStudentInformationData.m_strMotherOccupation = $("#studentInfo_input_motheroccupation").val();
	oStudentInformationData.m_strPhoneNumber = $("#studentInfo_input_phoneNumber1").val();
	oStudentInformationData.m_strAlternateNumber = $("#studentInfo_input_phoneNumber2").val();
	oStudentInformationData.m_nFamilyIncome = $("#studentInfo_input_income").val();	
	oStudentInformationData.m_strEmailAddress = $("#studentInfo_input_email").val();
	oStudentInformationData.m_dDateOfBirth = $("#student_input_dateofbirth").val();
	oStudentInformationData.m_strReligion = $("#studentInfo_input_religion").val();
	oStudentInformationData.m_strCurrentAddress = $("#studentInfo_textarea_address").val();
	oStudentInformationData.m_strCity = $("#studentInfo_input_cityName").val();
	oStudentInformationData.m_strState= $("#studentInfo_input_stateName").val();
	oStudentInformationData.m_nPincode = $("#studentInfo_input_pincodeName").val();
	return oStudentInformationData;
}

function studentInfo_created (oStudentInfoResponse)
{
	HideDialog ("ProcessDialog");
	if (oStudentInfoResponse.m_bSuccess)
	{
		studentInfo_displayInfo("studentcreatedsuccessfully", "kSuccess");
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
		studentInfo_displayInfo("studentcreationcreationfailed", "kError");
}

function studentInfo_updated (oStudentInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oStudentInfoResponse.m_bSuccess)
	{
		studentInfo_displayInfo ("studentupdatedsuccessfully");
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
	navigate("studentList", "widgets/scholarshipmanagement/listStudent.js")
}

function studentInfo_gotData (oStudentInfoResponse)
{	
	var oStudentInfoData = oStudentInfoResponse.m_arrStudentInformationData[0];
	m_oStudentInfoMemberData.m_strImageName = oStudentInfoData.m_strStudentImageName;
	 oStudentInfoData.m_nStudentId = oStudentInfoData.m_nStudentId;
	 $("#studentInfo_input_studentUIDNumber").val(oStudentInfoData.m_nUID);
	 $("#studentInfo_input_studentName").val(oStudentInfoData.m_strStudentName);
	 if(oStudentInfoData.m_strGender == "M")
	 {
		var radiobutton = document.getElementById("studentInfo_input_male");
		radiobutton.checked = true;
	 }
	 else if(oStudentInfoData.m_strGender == "F")
	 {
		 var radiobutton = document.getElementById("studentInfo_input_female");
			radiobutton.checked = true;
	 }
	 else
	 {
		 var radiobutton = document.getElementById("studentInfo_input_others");
			radiobutton.checked = true;
	 }
	 $("#studentInfo_input_male").val(oStudentInfoData.m_strGender);
	 $("#student_input_dateofbirth").val(oStudentInfoData.m_dDateOfBirth);
	 $("#studentInfo_input_fathername").val(oStudentInfoData.m_strFatherName);
	 $("#studentInfo_input_fatheroccupation").val(oStudentInfoData.m_strFatherOccupation);
	 $("#studentInfo_input_mothername").val(oStudentInfoData.m_strMotherName);
	 $("#studentInfo_input_motheroccupation").val(oStudentInfoData.m_strMotherOccupation);
	 $("#studentInfo_input_income").val(oStudentInfoData.m_nFamilyIncome);
	 $("#studentInfo_input_phoneNumber1").val(oStudentInfoData.m_strPhoneNumber);
	 $("#studentInfo_input_phoneNumber2").val(oStudentInfoData.m_strAlternateNumber);
	 $("#studentInfo_input_religion").val(oStudentInfoData.m_strReligion);
	 $("#studentInfo_input_email").val(oStudentInfoData.m_strEmailAddress);
	 $("#studentInfo_textarea_address").val(oStudentInfoData.m_strCurrentAddress);
	 $("#studentInfo_input_cityName").val(oStudentInfoData.m_strCity);
	 $("#studentInfo_input_stateName").val(oStudentInfoData.m_strState);
	 $("#studentInfo_input_pincodeName").val(oStudentInfoData.m_nPincode);
	 initFormValidateBoxes ("studentInfo_form_id");
}

function studentInfo_cancel()
{
	HideDialog ("dialog");
}
