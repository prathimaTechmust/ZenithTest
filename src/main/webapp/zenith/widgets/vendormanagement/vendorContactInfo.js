var contactInfo_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorContactData.js',
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
	createPopup("vendorInfo_contactInfo_dialog", "#contactInfo_button_submit", "#contactInfo_button_cancel", true);
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
	var oListData = $("#vendorInfo_table_listContactPersons").datagrid('getData');
	var oData = oListData.rows[m_ovendorInfoMemberData.m_nIndex];
	contactInfo_setFormData (oData);
}

function contactInfo_editContactDetail ()
{
	var oUpdateButton = document.getElementById("contactInfo_button_submit");
	oUpdateButton.setAttribute('editContact','true');
	createPopup('dialog', '#contactInfo_button_cancel', '#contactInfo_button_submit', true);
	m_ocontactInfoMemberData.m_bIsNew = false;
	contactInfo_loadElements(m_ocontactInfoMemberData.m_bIsNew);
	var oContactData = new contactInfo_getFormData ();
	oContactData.m_nContactId = m_oVendorListMemberData.m_oSelectedContactId;
	oContactData.m_oVendorData.m_nVendorId = m_oVendorListMemberData.m_oSelectedVendorId;
	ContactDataProcessor.get (oContactData, contactInfo_gotData);
}

function contactInfo_getFormData ()
{
	var oContactData = new ContactData ();
	var oVendorData = new VendorData ();
	oContactData.m_strContactName =  $("#contactInfo_input_contactName").val();
	oContactData.m_strPhoneNumber = $("#contactInfo_input_phoneNumber").val();
	oContactData.m_strEmail = $("#contactInfo_input_emailAddress").val();
	oContactData.m_strDepartment = $("#contactInfo_input_department").val();
	oContactData.m_strDesignation = $("#contactInfo_input_designation").val();
	oContactData.m_oVendorData = oVendorData;
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
		if(document.getElementById("contactInfo_button_submit").getAttribute('update') == "false")
		{
			informUser("Contact added successfully", "kSuccess");
			HideDialog ("vendorInfo_contactInfo_dialog");
			contactInfo_list (oContactData);
		}
		else if (document.getElementById("contactInfo_button_submit").getAttribute('update') == "true" &&
				document.getElementById("contactInfo_button_submit").getAttribute('editContact') == "true")
		{
			oContactData.m_nContactId = m_oVendorListMemberData.m_oSelectedContactId;
			oContactData.m_oVendorData.m_nVendorId = m_oVendorListMemberData.m_oSelectedVendorId;
			contactInfo_isUpdate(oContactData);
		}
		else
		{
			HideDialog ("vendorInfo_contactInfo_dialog");
			$('#vendorInfo_table_listContactPersons').datagrid('updateRow',{index : m_ovendorInfoMemberData.m_nIndex, row : oContactData});
			informUser("Contact updated successfully", "kSuccess");
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
	$("#contactInfo_input_contactName").val(oContactData.m_strContactName);
	$("contactInfo_input_phoneNumber").val(oContactData.m_strPhoneNumber);
	$("#contactInfo_input_emailAddress").val( oContactData.m_strEmail);
	$("#contactInfo_input_department").val( oContactData.m_strDepartment);
	$("#contactInfo_input_designation").val(oContactData.m_strDesignation);
	initFormValidateBoxes ("contactInfo_form_id");
}

function contactInfo_list (oContactData)
{
	$('#vendorInfo_table_listContactPersons').datagrid('appendRow',oContactData);
}

function contactInfo_cancel ()
{
	if(document.getElementById("contactInfo_button_submit").getAttribute('editContact') == "true")
		HideDialog ("dialog");
	HideDialog ("vendorInfo_contactInfo_dialog");
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
		informUser("Contact updated successfully", "kSuccess");
		HideDialog ("dialog");
		listVendor_clearDetail ();
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
		document.getElementById("contactInfo_button_submit").setAttribute('update','false');
	}
	else
	{
		document.getElementById("contactInfo_button_submit").setAttribute('update','true');
		document.getElementById("contactInfo_button_submit").innerHTML = "Update";
	}
}

function contactInfo_errorHandler (msg, oException) 
{
	informUser("Error message is: " + msg + " - Error Details: " + "kError");
}

function contactInfo_loadcontactEdit ()
{
	loadPage ("{path to widget}", "dialog", "contactInfo_edit()");
}
