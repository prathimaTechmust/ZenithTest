var changePassword_includeDataObjects = 
	[
		'widgets/usermanagement/actionmanager/ActionManagerData.js',
	];

 includeDataObjects (changePassword_includeDataObjects, "changePassword_loaded ()");

function changePassword_loaded ()
{
	loadPage ("usermanagement/actionmanager/changePassword.html", "dialog", "changePassword_popUp ()");
}

function changePassword_memberData ()
{
}

var m_ochangePasswordMemberData = new changePassword_memberData ();

function changePassword_popUp ()
{
	initFormValidateBoxes ("changePassword_form_id");
	createPopup ("dialog", "#changePassword_button_submit", "#changePassword_button_cancel", true);
}

function changePassword_getFormData ()
{
	var oActionManagerData = new ActionManagerData ();
	oActionManagerData.m_strLoginId = m_oLoginMemberData.m_strLoginId;
	oActionManagerData.m_strPassword = CryptoJS.SHA3($("#changePassword_input_currentPassword").val()).toString ();
	oActionManagerData.m_strNewPassword = CryptoJS.SHA3($("#changePassword_input_newPassword").val()).toString ();
	return oActionManagerData;
}

function changePassword_validate ()
{
	 return validateForm ("changePassword_form_id");
}

function changePassword_submit ()
{
	if(changePassword_validate ())
	{
		var oActionManagerData = changePassword_getFormData ();
		ActionManagerDataProcessor.changePassword (oActionManagerData, changePassword_gotResult);
		changePassword_clear ();
	}
}

function changePassword_cancel ()
{
	HideDialog("dialog");
}

function changePassword_clear ()
{
	document.getElementById("changePassword_input_currentPassword").value = "";
	document.getElementById("changePassword_input_newPassword").value = "";
	document.getElementById("changePassword_input_confirmPassword").value = "";
}

function changePassword_gotResult (oActionManagerResponse)
{
	if(oActionManagerResponse.m_bSuccess)
	{
		informUser ("usermessage_changepassword_passwordchangedsuccessfully", "kSuccess");
		HideDialog("dialog");
	}
	else
		informUser ("usermessage_changepassword_pleaseenteravalidpassword", "kWarning");
}