var contactInfo_includeDataObjects = 
[
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/ContactData.js',
];

includeDataObjects (contactInfo_includeDataObjects, "contactInfo_loaded ()");

function contactInfo_memberData ()
{
	this.m_bIsNew = false;
}

var m_ocontactInfoMemberData = new contactInfo_memberData ();

function contactInfo_init ()
{
	initFormValidateBoxes ("contactInfo_form_id");
	createPopup("clientInfo_contactInfo_dialog", "#contactInfo_button_submit", "#contactInfo_button_cancel", true);
}

function contactInfo_new ()
{
	contactInfo_init ();
	m_ocontactInfoMemberData.m_bIsNew = true;
	contactInfo_loadElements(m_ocontactInfoMemberData.m_bIsNew);
}

function contactInfo_edit ()
{
	contactInfo_init ();
	m_ocontactInfoMemberData.m_bIsNew = false;
	contactInfo_loadElements(m_ocontactInfoMemberData.m_bIsNew);
	var oListData = $("#clientInfo_table_listContactPersons").datagrid('getData');
	var oData = oListData.rows[m_oClientInfoMemberData.m_nIndex];
	contactInfo_setFormData (oData);
}

function contactInfo_editContactDetail ()
{
	document.getElementById("contactInfo_button_submit").setAttribute('editContact',true);
	createPopup('dialog', '#contactInfo_button_cancel', '#contactInfo_button_submit', true);
	m_ocontactInfoMemberData.m_bIsNew = false;
	contactInfo_loadElements(m_ocontactInfoMemberData.m_bIsNew);
	var oContactData = new contactInfo_getFormData ();
	oContactData.m_nContactId = m_oClientListMemberData.m_oSelectedContactId;
	oContactData.m_oClientData.m_nClientId = m_oClientListMemberData.m_oSelectedClientId;
	ContactDataProcessor.get (oContactData, contactInfo_gotData);
}

function contactInfo_getFormData ()
{
	var oContactData = new ContactData ();
	var oClientData = new ClientData ();
	oContactData.m_strContactName =  $("#contactInfo_input_contactName").val();
	oContactData.m_strPhoneNumber = $("#contactInfo_input_phoneNumber").val();
	oContactData.m_strEmail = $("#contactInfo_input_emailAddress").val();
	oContactData.m_strDepartment = $("#contactInfo_input_department").val();
	oContactData.m_strDesignation = $("#contactInfo_input_designation").val();
	oContactData.m_oClientData = oClientData;
	return oContactData;
}

function contactInfo_validate ()
{
	return validateForm("contactInfo_form_id");
}

function contactInfo_submit ()
{
	if (contactInfo_validate())
	{
		var oContactData = contactInfo_getFormData ();
		if(document.getElementById("contactInfo_button_submit").getAttribute('update')== "false")
		{
			informUser("contact added successfully.", "kSuccess");
			HideDialog ("clientInfo_contactInfo_dialog");
			contactInfo_list (oContactData);
		}
		else if (document.getElementById("contactInfo_button_submit").getAttribute('update')== "true" &&
				document.getElementById("contactInfo_button_submit").getAttribute('editContact') == "true")
		{
			oContactData.m_nContactId = m_oClientListMemberData.m_oSelectedContactId;
			oContactData.m_oClientData.m_nClientId = m_oClientListMemberData.m_oSelectedClientId;
			contactInfo_isUpdate(oContactData);
		}
		else
		{
			HideDialog ("clientInfo_contactInfo_dialog");
			$('#clientInfo_table_listContactPersons').datagrid('updateRow',{index : m_oClientInfoMemberData.m_nIndex, row : oContactData});
			informUser("contact updated successfully.", "kSuccess");
		}
	}
}

function contactInfo_isUpdate (ocontactInfoData)
{
	ContactDataProcessor.update (ocontactInfoData, contactInfo_updated);
}

function contactInfo_setFormData (oContactData)
{
	assert.isObject(oContactData, "oContactData expected to be an Object.");
	assert( Object.keys(oContactData).length >0 , "oContactData cannot be an empty .");//checks for non emptyness 
	$("#contactInfo_input_contactName").val( oContactData.m_strContactName);
	$("#contactInfo_input_phoneNumber").val(oContactData.m_strPhoneNumber);
	$("#contactInfo_input_emailAddress").val(oContactData.m_strEmail);
	$("#contactInfo_input_department").val(oContactData.m_strDepartment);
	$("#contactInfo_input_designation").val(oContactData.m_strDesignation);
	initFormValidateBoxes ("contactInfo_form_id");
}

function contactInfo_list (oContactData)
{
	$('#clientInfo_table_listContactPersons').datagrid('appendRow', oContactData);
}

function contactInfo_cancel ()
{
	if(document.getElementById("contactInfo_button_submit").getAttribute('editContact')== "true")
		HideDialog ("dialog");
	HideDialog ("clientInfo_contactInfo_dialog");
}

function contactInfo_clear ()
{
	document.getElementById("{fiels id}").value = "";
}

function contactInfo_created (ocontactInfoResponse)
{
}

function contactInfo_updated (ocontactInfoResponse)
{
	if(ocontactInfoResponse.m_bSuccess)
	{
		informUser("contact updated successfully.", "kSuccess");
		HideDialog ("dialog");
		listClient_clearDetail ();
	}
	else if(ocontactInfoResponse.m_nErrorID)
	{
		informUser("Error message", "kError");
	}
}

function contactInfo_gotData (oContactResponse)
{	
	var oContactData = oContactResponse.m_arrContactData[0];
	contactInfo_setFormData (oContactData);
}

function contactInfo_loadElements (bIsNew)
{
	assert.isBoolean(bIsNew, "bIsNew should be a boolean value");
	if(bIsNew)
	{
		document.getElementById("contactInfo_button_submit").setAttribute('update',false);
	}
	else
	{
		document.getElementById("contactInfo_button_submit").setAttribute('update',true);
		document.getElementById("contactInfo_button_submit").innerHTML = "Update";
	}
}

function contactInfo_errorHandler (msg, oException) 
{
	informUser("Error message is: " + msg + " - Error Details: " + dwr.util.toDescriptiveString(oException, 2), "kError");
}

function contactInfo_loadcontactEdit ()
{
	loadPage ("{path to widget}", "dialog", "contactInfo_edit()");
}
