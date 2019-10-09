var userInfo_includeDataObjects = 
[
	'widgets/usermanagement/role/RoleData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

 includeDataObjects (userInfo_includeDataObjects, "userInfo_loaded()");

function userInfo_memberData ()
{
	this.m_strNewPassword = "";
	this.m_strPassword = "";
	this.m_buffImage = null;
	this.m_strUserImageName = "";	
}

var m_oUserInfoMemberData = new userInfo_memberData ();

function userInfo_new ()
{
	userInfo_init();
	userInfo_removeElements ();
	initFormValidateBoxes ("userInfo_form_id");
}

function userInfo_edit ()
{
	userInfo_init();
	userInfo_appendElements ()
	var oUserInformationData = new UserInformationData ();
	oUserInformationData.m_nUserId = m_oUserInfoListMemberData.m_nSelectedUserId;
	oUserInformationData.m_nUID = m_oUserInfoListMemberData.m_nSelectedUID;
	UserInformationDataProcessor.get (oUserInformationData, userInfo_gotData);
}

function userInfo_init ()
{
	createPopup("dialog", "#userInfo_button_submit", "#userInfo_button_cancel", true);
//	$( "#userInfo_input_dateOfBirth" ).datebox({ required: true });
//	$( "#userInfo_input_dateOfBirth" ).datepicker({minDate:"-90y", maxDate: new Date()});
	userInfo_populateRoleNameList();
}

function userInfo_populateRoleNameList ()
{
	var oRoleData = new RoleData ();
	RoleDataProcessor.list(oRoleData, "m_strRoleName", "asc", 1, 10, 
			function (oRoleResponse)
			{
				userInfo_prepareRoleNameDD ("userInfo_select_roleName", oRoleResponse);
			}				
		);
}

function userInfo_prepareRoleNameDD (strRoleDD, oRoleResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oRoleResponse.m_arrRoleData.length; nIndex++)
		arrOptions.push (CreateOption (oRoleResponse.m_arrRoleData [nIndex].m_nRoleId,
				oRoleResponse.m_arrRoleData [nIndex].m_strRoleName));
	PopulateDD (strRoleDD, arrOptions);
}

function userInfo_removeElements ()
{
	$("#userInfo_a_changePassword").remove();
	$("#userInfo_label_active").remove();
	$("#userInfo_input_checkActive").remove();
}

function userInfo_appendElements ()
{
	$("#userInfo_a_changePassword").append();
	$("#userInfo_label_active").append();
	$("#userInfo_input_checkActive").append();
	document.getElementById("userInfo_button_submit").setAttribute('update',true);
	document.getElementById("userInfo_button_submit").innerHTML = "Update";
}

function userInfo_gotData (oUserInfoResponse)
{	
	var oUserInfoData = oUserInfoResponse.m_arrUserInformationData[0];
	m_oUserInfoMemberData.m_strUserImageName = oUserInfoData.m_strUserPhotoFileName;
	$("#userInfo_input_userName").val(oUserInfoData.m_strUserName);
	if(oUserInfoData.m_strGender =='Female')
	{
		$("#userInfo_input_female").prop("checked", true);
	}
		else
	{
		$("#userInfo_input_male").prop("checked", true);
	}
	 document.getElementById("userInfo_input_dateOfBirth").value = userDateofBirth(oUserInfoData.m_dDOB);
	$("#userInfo_img_userPhoto").attr('src', oUserInfoData.m_buffImgUserPhoto);
	$("#userInfo_img_userPhoto").attr('width','85px');
	$("#userInfo_img_userPhoto").attr('width','85px');
	$("#userInfo_select_roleName").val(oUserInfoData.m_oRole.m_nRoleId);
	$("#userInfo_input_employeeId").val(oUserInfoData.m_strEmployeeId);
	$("#userInfo_input_loginId").val(oUserInfoData.m_strLoginId);
	$("#userInfo_input_password").val(oUserInfoData.m_strPassword);
	$("#userInfo_input_password").attr('readonly', 'readonly');
	$("#userInfo_input_confirmPassword").val(oUserInfoData.m_strPassword);
	$("#userInfo_input_confirmPassword").attr('readonly', 'readonly');
	$("#userInfo_textarea_address").val(oUserInfoData.m_strAddress);
	$("#userInfo_input_phoneNumber").val(oUserInfoData.m_strPhoneNumber);
	$("#userInfo_input_email").val(oUserInfoData.m_strEmailAddress);
	m_oUserInfoMemberData.m_strPassword = oUserInfoData.m_strPassword;
	if(oUserInfoData.m_nStatus == 1)
		$("#userInfo_input_checkActive").prop("checked",true);
	initFormValidateBoxes ("userInfo_form_id");
	document.getElementById("userInfo_input_gender");
}

function userDateofBirth (strUserDOB)
{
	var strUserDateOfBirth = "/Date("+strUserDOB+")/";
	var date = new Date(parseFloat(strUserDateOfBirth.substr(6)));	
	var strDate = date.getFullYear() + "-" +("0" + (date.getMonth() + 1)).slice(-2) + "-" +date.getDate();	
	return strDate;
}

function userInfo_submit ()
{
	if (userInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "user_progressbarLoaded ()");
}

function user_progressbarLoaded()
{
	createPopup('ProcessDialog', '', '', true);
	var oUserInformationData = userInfo_getFormData ();
	if(document.getElementById("userInfo_button_submit").getAttribute('update') == "false")
		UserInformationDataProcessor.create(oUserInformationData, userInfo_created);
	else
		userInfo_updateUserInfo (oUserInformationData);
}

function userInfo_validate ()
{
	return validateForm("userInfo_form_id");
}

function userInfo_getFormData ()
{
	/*var m_chkGender = $('[name=userInfo_input_gender]:checked').val();
	var oUserInformationData = new UserInformationData ();
	oUserInformationData.m_strLoginId = $("#userInfo_input_loginId").val();
	oUserInformationData.m_strPassword = CryptoJS.SHA3($("#userInfo_input_password").val()).toString();
	oUserInformationData.m_strUserName = $("#userInfo_input_userName").val();
//	var m_strDate = $('#userInfo_input_dateOfBirth').datebox('getValue');
	var m_strDate = $('#userInfo_input_dateOfBirth').val();
	oUserInformationData.m_strDOB =m_strDate;
	//oUserInformationData.m_strDOB = FormatDate (m_strDate);
	oUserInformationData.m_strGender = m_chkGender;
    oUserInformationData.m_buffImgUserPhoto = m_oUserInfoMemberData.m_buffImage;
	oUserInformationData.m_oRole = new RoleData ();
	oUserInformationData.m_oRole.m_nRoleId = $("#userInfo_select_roleName").val();
	oUserInformationData.m_strEmployeeId = $("#userInfo_input_employeeId").val();
	oUserInformationData.m_strAddress = $("#userInfo_textarea_address").val();
	oUserInformationData.m_strPhoneNumber = $("#userInfo_input_phoneNumber").val();
	oUserInformationData.m_strEmailAddress = $("#userInfo_input_email").val();
	oUserInformationData.m_nStatus = "kActive";
	return oUserInformationData;*/
	
	// Get User Form Values
	var oUserInformationData = new UserInformationData ();
	oUserInformationData.m_strUserName = $("#userInfo_input_userName").val();
	oUserInformationData.m_dDOB = $('#userInfo_input_dateOfBirth').val();
	if(document.getElementById("userInfo_input_male").checked)
		oUserInformationData.m_strGender = document.getElementById("userInfo_input_male").value;
	 else if(document.getElementById("userInfo_input_female").checked)
		 oUserInformationData.m_strGender = document.getElementById("userInfo_input_female").value;
	if($("#userInfo_input_userPhoto").val() == '')
		oUserInformationData.m_strUserPhotoFileName = m_oUserInfoMemberData.m_strUserImageName;			
	else
	oUserInformationData.m_strUserPhotoFileName = $("#userInfo_input_userPhoto").val().replace(/.*(\/|\\)/, '');
	oUserInformationData.m_oRole = new RoleData ();
	oUserInformationData.m_oRole.m_nRoleId = $("#userInfo_select_roleName").val();
	oUserInformationData.m_strEmployeeId = $("#userInfo_input_employeeId").val();
	oUserInformationData.m_strLoginId = $("#userInfo_input_loginId").val();
	oUserInformationData.m_strPassword = CryptoJS.SHA3($("#userInfo_input_password").val()).toString();
	oUserInformationData.m_strAddress = $("#userInfo_textarea_address").val();
	oUserInformationData.m_strPhoneNumber = $("#userInfo_input_phoneNumber").val();
	oUserInformationData.m_strEmailAddress = $("#userInfo_input_email").val();
	oUserInformationData.m_nStatus = "kActive";
	return oUserInformationData;
}

function userInfo_created (oUserInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oUserInfoResponse.m_bSuccess && oUserInfoResponse.m_strError_Desc != "kLoginIdExist")
	{
		userInfo_displayInfo ("usermessage_userinfo_usercreatedsuccessfully");
		try
		{
			var oForm = $('#userInfo_form_id')[0];
			var oFormData = new FormData (oForm);
			oFormData.append('userId',oUserInfoResponse.m_arrUserInformationData[0].m_nUserId);
			UserInformationDataProcessor.setImagetoS3bucket (oFormData, user_image_created);
		}
		catch(oException)
		{}
		
	}		
	else
		userInfo_displayErrorInfo ("usermessage_userinfo_loginidalreadyexists");
}

function userInfo_updateUserInfo (oUserInformationData)
{
	oUserInformationData.m_nUserId = m_oUserInfoListMemberData.m_nSelectedUserId;
	oUserInformationData.m_nUID = m_oUserInfoListMemberData.m_nSelectedUID;
	oUserInformationData.m_strPassword = m_oUserInfoMemberData.m_strPassword;
	oUserInformationData.m_strNewPassword = m_oUserInfoMemberData.m_strNewPassword;
	UserInformationDataProcessor.update(oUserInformationData, userInfo_updated);
}

function user_image_created ()
{
	HideDialog ("secondDialog");
}

function userInfo_updated (oUserInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oUserInfoResponse.m_bSuccess)
	{
		userInfo_displayInfo ("usermessage_userinfo_userupdatedsuccessfully")
		try
		{
			var oForm = $('#userInfo_form_id')[0];
			var oFormData = new FormData (oForm);
			var strImageName = oFormData.get("userImage");
			if(strImageName == "")
			{
				user_details_updated();
			}
			else if(oUserInfoResponse.m_arrUserInformationData[0].m_strUserPhotoFileName == "" || m_oUserInfoMemberData.m_strUserImageName != strImageName)
			{
				oFormData.append('userId',oUserInfoResponse.m_arrUserInformationData[0].m_nUserId);				
				UserInformationDataProcessor.setImagetoS3bucket (oFormData, user_details_updated);
			}			
		}
		catch(oException){}
	}		
	else if(oUserInfoResponse.m_nErrorID)
		userInfo_displayErrorInfo ("usermessage_userinfo_loginidalreadyexistspleaseenteranewloginid")
}

function user_details_updated ()
{
	HideDialog ("secondDialog");
}

function userInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("userList", "widgets/usermanagement/userinfo/listUserInfo.js")
}

function userInfo_displayErrorInfo (strErrorMessage)
{
	informUser(strErrorMessage, "kError");
	document.getElementById("userInfo_input_loginId").value = "";
}

function userInfo_loadImagePreview ()
{
	var oUserInformationData = new UserInformationData ();
	oUserInformationData.m_buffImgUserPhoto = $("#userInfo_input_userPhoto").val();  
	oUserInformationData.m_nUID = m_oZenithMemberData.m_nUID ;
	oUserInformationData.m_nUserId = m_oZenithMemberData.m_nUserId;
	m_oUserInfoMemberData.m_buffImage = oUserInformationData.m_buffImgUserPhoto;
	validateImageFile($("#userInfo_input_userPhoto")[0].files, "#userInfo_input_userPhoto");
	UserInformationDataProcessor.getImagePreview(oUserInformationData, gotImagePreviewData);
}

function gotImagePreviewData (oData)
{
	$("#userInfo_img_userPhoto").attr('src', oData.m_buffImgUserPhoto);
	$("#userInfo_img_userPhoto").attr('width','50px');
	$("#userInfo_img_userPhoto").attr('height','50px');
}

function userInfo_loadNewPassword ()
{
	loadPage("usermanagement/userinfo/newPassword.html", "secondDialog", "userInfo_loadNewPasswordInit ()");
}

function userInfo_loadNewPasswordInit ()
{
	createPopup('secondDialog', '#newPassword_button_submit', '#newPassword_button_cancel', true);
	initFormValidateBoxes ("newPassword_form_id");
}

function userInfo_newPasswordSubmit ()
{
	if(userInfo_validateNewPassword ())
	{
		m_oUserInfoMemberData.m_strNewPassword = CryptoJS.SHA3(document.getElementById("newPassword_input_password").value).toString ();
		HideDialog ("secondDialog");
	}
}

function userInfo_validateNewPassword ()
{
	return validateForm("newPassword_form_id");
}

function userInfo_cancel ()
{
	HideDialog ("dialog");
}

function userinfo_newPasswordCancel ()
{
	HideDialog ("secondDialog");
}