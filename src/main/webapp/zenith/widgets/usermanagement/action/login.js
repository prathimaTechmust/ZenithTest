var login_includeDataObjects = 
[
	'widgets/usermanagement/actionmanager/ActionManagerData.js'
];

 includeDataObjects (login_includeDataObjects, "login_loadLogo ()");

function login_memberData ()
{
	this.m_strLoginId = "";
	this.m_nUserId = -1;
	this.m_oActionManagerData;
}

var m_oLoginMemberData = new login_memberData ();

function login_loadLogo ()
{
	loadPage ("usermanagement/actionmanager/logo.html", "workarea", "login_showLogo ()");
}

function login_loaded ()
{
	var oInCookies = document.cookie.split(";");
	m_oLoginMemberData.m_oActionManagerData = new ActionManagerData ();
	m_oLoginMemberData.m_oActionManagerData.m_strUserName = login_getCookieByName(oInCookies, "zenithUserId");
	m_oLoginMemberData.m_oActionManagerData.m_strPassword = login_getCookieByName(oInCookies, " zenithPassword");
	if (m_oLoginMemberData.m_oActionManagerData != null && m_oLoginMemberData.m_oActionManagerData.m_strUserName == "")
		loadPage ("usermanagement/actionmanager/login.html", "dialog", "init_popUp ()");
	else
		ActionManagerDataProcessor.get (m_oLoginMemberData.m_oActionManagerData, login_gotData);
	loadDWRHeaders ();
}

function login_getCookieByName (oAllCookies, strName)
{
	assert.isArray(oAllCookies, "oAllCookies expected to be an Array.");
	assert.isString(strName, "strName expected to be a string.");
	var strReturnValue = "";
	for(var nIndex =0 ; nIndex < oAllCookies.length ; nIndex++  )
	{
		var selectCookies = oAllCookies[nIndex].split("=");
		if(selectCookies[0] == strName)
		{
			strReturnValue = selectCookies[1];
			break;
		}
	}
	return strReturnValue;
}

function init_popUp ()
{
	$("#login_form_Id").form ();
	initFormValidateBoxes ("login_form_Id");
	createPopup ("dialog", "#login_button_submit", "#login_button_submit", true);
}

function login_getFormData ()
{
	var oActionManagerData = new ActionManagerData ();
	oActionManagerData.m_strUserName = $("#login_input_userName").val ();
	oActionManagerData.m_strPassword = CryptoJS.SHA3($("#login_input_password").val()).toString ();
	return oActionManagerData;
}

function login_validate ()
{
	return validateForm ("login_form_Id");
}

function login_submit ()
{
	if(login_validate ())
	{
		m_oLoginMemberData.m_oActionManagerData = login_getFormData ();
		console.log(m_oZenithMemberData.m_strLocationURL);
		ActionManagerDataProcessor.get (m_oLoginMemberData.m_oActionManagerData, login_gotData);
	}
}

function login_gotData (oActionManagerResponse)
{
	if(oActionManagerResponse.m_bSuccess)
	{
		$('#verticalSplitter').jqxSplitter({
	        width: '99.5%',
	        height: '100%',
	        orientation: 'vertical',
	        panels: [
	           { size: "15%", min: "5%", collapsible: true},
	           { size: '85%', min: "70%", collapsible: false}]
	    });
		document.cookie ="zenithUserId="+m_oLoginMemberData.m_oActionManagerData.m_strUserName;
		document.cookie ="zenithPassword="+m_oLoginMemberData.m_oActionManagerData.m_strPassword;
		document.getElementById ("menubar").innerHTML = oActionManagerResponse.m_strMenuHTML;
		document.getElementById("index_div_userName").innerHTML = oActionManagerResponse.m_strUser;
		m_oLoginMemberData.m_strLoginId = oActionManagerResponse.m_strLoginId;
		m_oLoginMemberData.m_nUserId = oActionManagerResponse.m_nUserId;
		m_oLoginMemberData.m_nUID = oActionManagerResponse.m_nUID;
		m_oZenithMemberData.m_nUserId = oActionManagerResponse.m_nUserId;
		m_oZenithMemberData.m_nUID = oActionManagerResponse.m_nUID;
		index_div_login.style.visibility = "visible";
		initMenu();
        HideDialog("dialog");
        HideDialog("secondDialog");
        HideDialog("thirdDialog");
        HideDialog("ProcessDialog");
		 try
		 {
			parent.ID_HandleLogin (oActionManagerResponse.m_strUser);
		 }
		 catch (oException){}
	}
	else
	{
		informUser ("usermessage_login_invaliduser", "kAlert");
		if(m_oZenithMemberData.m_nUserId > 0)
			logout ();
	}
}

function login_showLogo ()
{
	var OffsetHeight = getStyleHeight ("ZenithLogo") + getStyleHeight ("footer") + 10
	$("#logo_div_backgroundImage").jqxPanel({ width: '100%', height: this.innerHeight - OffsetHeight, autoUpdate: true, scrollBarSize:10});
	$(window).unbind ('resize');
    $(window).resize(function(){$("#logo_div_backgroundImage").jqxPanel({height: this.innerHeight - OffsetHeight})});
    login_loaded ();
}

function login_forgotPasswordLoad ()
{
	loadPage("usermanagement/actionmanager/forgotPassword.html", "dialog", "login_forgotPasswordInit ()");
}

function login_forgotPasswordInit ()
{
	createPopup('dialog', '#forgotPassword_button_submit', '#forgotPassword_button_cancel', true);
}

function login_forgotPasswordSubmit()
{
	var strLoginId = document.getElementById("forgotPassword_input_userId").value;
	var strEmailId =  document.getElementById("forgotPassword_input_emailId").value;
	if(login_IsEmpty("forgotPassword_input_userId") && login_IsEmpty("forgotPassword_input_emailId"))
		informUser ("usermessage_login_pleaseprovideusedidoremailid",  "kWarning");
	else if(document.getElementById("forgotPassword_input_userId").value != "" && document.getElementById("forgotPassword_input_emailId").value != "")
	{
		if($("#forgotPassword_form_id").validate().form())
			login_processForgotPassword (strLoginId, strEmailId);
	}
	else if(document.getElementById("forgotPassword_input_userId").value != "")
	{
		if($("#forgotPassword_form_id").validate().element("#forgotPassword_input_userId"))
			login_processForgotPassword (strLoginId, strEmailId);
	}
	else
	{
		if($("#forgotPassword_form_id").validate().element("#forgotPassword_input_emailId"))
			login_processForgotPassword (strLoginId, strEmailId);
	}
}

function login_processForgotPassword (strLoginId, strEmailId)
{
	ActionManagerDataProcessor.processForgotPassword(strLoginId, strEmailId, login_gotForgotPassword);
}

function login_gotForgotPassword (oPasswordResponse)
{
	if(oPasswordResponse.m_bSuccess)
	{
		informUser ("usermessage_login_pleasecheckmailforlogindetails", "kSuccess");
		login_forgotPasswordClear ();
		loadPage ("usermanagement/actionmanager/login.html", "dialog", "init_popUp ()");
	}
	else
	{
		informUser ("usermessage_login_pleaseprovidecorrectuseridoremailid", "kWarning");
		login_forgotPasswordClear ();
	}
}

function login_forgotPasswordClear ()
{
	document.getElementById("forgotPassword_input_userId").value = "";
	document.getElementById("forgotPassword_input_emailId").value = "";
}

function login_IsEmpty(strElementId)
{ 
	assert.isString(strElementId, "strElementId expected to be a string.");
	var bEmpty;
	var oElementId = document.getElementById(strElementId);
	if(oElementId.value == "")
		bEmpty = true;
	else
		bEmpty = false;
	return bEmpty;
}

function login_loadLogin()
{
	loadPage ("usermanagement/actionmanager/login.html", "dialog", "init_popUp ()");
}