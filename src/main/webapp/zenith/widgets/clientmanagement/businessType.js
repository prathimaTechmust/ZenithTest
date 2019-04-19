var businessType_includeDataObjects = 
[
	'widgets/clientmanagement/BusinessTypeData.js'
];


includeDataObjects (businessType_includeDataObjects, "businessType_loaded ()");

function businessType_memberData ()
{
	this.m_strBusinessName = "";	
	this.m_nBusinessId = -1;
}

var m_obusinessType_memberData = new businessType_memberData ();

function businessType_init ()
{
	createPopup ("dialog", "#businessType_button_submit", "#businessType_button_cancel", true);
	initFormValidateBoxes ("businessType_form_id");
}

function businessType_new ()
{
	businessType_init ();
}

function businessType_edit()
{
	businessType_init ();
	document.getElementById("businessType_button_submit").setAttribute('update',true);
	document.getElementById("businessType_button_submit").innerHTML = "Update";
	var oBusinessTypeData = new BusinessTypeData();
	oBusinessTypeData.m_nBusinessTypeId = m_obusinessType_memberData.m_nBusinessId;
	BusinessTypeDataProcessor.get(oBusinessTypeData,businessType_gotData);
}

function businessType_getFormData ()
{
	var oBusinessTypeData = new BusinessTypeData();
	oBusinessTypeData.m_strBusinessName = $("#businessType_input_businessName").val();
	return oBusinessTypeData;
}

function businessType_validate (formId)
{
	return validateForm("businessType_form_id");
}

function businessType_submit ()
{
	if (businessType_validate ())
	{
		var oBusinessTypeData = businessType_getFormData ();
		if(document.getElementById("businessType_button_submit").getAttribute('update') == "false")
			BusinessTypeDataProcessor.create(oBusinessTypeData, businessType_created);
		else
		{
			oBusinessTypeData.m_nBusinessTypeId = m_obusinessType_memberData.m_nBusinessId;
			BusinessTypeDataProcessor.update(oBusinessTypeData, businessType_updated);
		}
	}
}

function businessType_updated(oBusinessTypeResponse, strBusinessName)
{
	if(oBusinessTypeResponse.m_bSuccess)
	{
		informUser ("Business type updated successfully.", "kSuccess");
		navigate('businesstypelist','widgets/clientmanagement/listBusinessType.js');
	}
	else
		informUser("Updation failed!", "kError");
}

function businessType_created (oBusinessTypeResponse)
{
	if(oBusinessTypeResponse.m_bSuccess)
	{
		informUser("Business type created successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate('businesstypelist','widgets/clientmanagement/listBusinessType.js');
	}
	else
		informUser("creation failed!", "kError");
}

function businessType_cancel ()
{
	HideDialog ("dialog");
}

function businessType_clear()
{
	document.getElementById("businessType_input_businessName").value = "";
}

function businessType_gotData (oBusinessTypeResponse)
{	
	var oBusinessTypeData = oBusinessTypeResponse.m_arrBusinessType[0];
	$("#businessType_input_businessName").val(oBusinessTypeData.m_strBusinessName);
	initFormValidateBoxes ("businessType_form_id");
}