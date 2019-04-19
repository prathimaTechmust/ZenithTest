var propertyType_includeDataObjects = 
[
	'widgets/propertymanagement/propertytype/PropertyTypeData.js'
];

(function() {
	if (m_oTrademustMemberData.m_arrObjects.indexOf(propertyType_includeDataObjects[0]) != -1) {
		propertyType_loaded ();
	} else {
		includeDataObjects (propertyType_includeDataObjects, "propertyType_loaded()");
	}	
})();

// includeDataObjects (propertyType_includeDataObjects, "propertyType_loaded ()");

function propertyType_memberData ()
{
	this.m_strBusinessName = "";	
	this.m_nPropertyId = -1;
}

var m_oPropertyTypeMemberData = new propertyType_memberData ();

function propertyType_init ()
{
	createPopup ("dialog", "#propertyType_button_submit", "#propertyType_button_cancel", true);
	initFormValidateBoxes ("propertyType_form_id");
}

function propertyType_new ()
{
	propertyType_init ();
}

function propertyType_edit()
{
	propertyType_init ();
	document.getElementById("propertyType_button_submit").setAttribute('update',true);
	document.getElementById("propertyType_button_submit").innerHTML = "Update";
	var oPropertyTypeData = new PropertyTypeData();
	oPropertyTypeData.m_nPropertyTypeId = m_oPropertyTypeMemberData.m_nPropertyId;
	PropertyTypeDataProcessor.get(oPropertyTypeData,propertyType_gotData);
}

function propertyType_getFormData ()
{
	var oPropertyTypeData = new PropertyTypeData();
	oPropertyTypeData.m_strPropertyType = $("#propertyType_input_propertyTypeName").val();
	return oPropertyTypeData;
}

function propertyType_validate ()
{
	return validateForm("propertyType_form_id");
}

function propertyType_submit ()
{
	if (propertyType_validate ())
	{
		var oPropertyTypeData = propertyType_getFormData ();
		if(document.getElementById("propertyType_button_submit").getAttribute('update') == "false")
			PropertyTypeDataProcessor.create(oPropertyTypeData, propertyType_created);
		else
		{
			oPropertyTypeData.m_nPropertyTypeId = m_oPropertyTypeMemberData.m_nPropertyId;
			PropertyTypeDataProcessor.update(oPropertyTypeData, propertyType_updated);
		}
	}
}

function propertyType_updated(oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("Property type updated successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate('propertytypelist','widgets/propertymanagement/propertytype/propertyTypeList.js');
	}
	else
		informUser("Updation failed!", "kError");
}

function propertyType_created (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Property type created successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate('propertytypelist','widgets/propertymanagement/propertytype/propertyTypeList.js');
	}
	else
	{
		informUser("creation failed!", "kError");
		document.getElementById("propertyType_input_propertyTypeName").value = "";
	}
}

function propertyType_cancel ()
{
	HideDialog ("dialog");
}

function propertyType_gotData (oResponse)
{	
	var oPropertyTypeData = oResponse.m_arrPropertyTypeData[0];
	$("#propertyType_input_propertyTypeName").val(oPropertyTypeData.m_strPropertyType);
	initFormValidateBoxes ("propertyType_form_id");
}