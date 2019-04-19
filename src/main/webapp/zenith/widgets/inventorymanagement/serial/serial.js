var serial_includeDataObjects = 
[
	'widgets/inventorymanagement/serial/SerialNumberData.js'
];



 includeDataObjects (serial_includeDataObjects, "serial_loaded ()");

function serial_memberData ()
{
	this.m_strSerialType = "";	
}

var m_oSerialMemberData = new serial_memberData ();

function serial_new ()
{
	serial_init ();
}

function serial_init ()
{
	createPopup ("dialog", "#serial_button_submit", "#serial_button_cancel", true);
	initFormValidateBoxes ("serial_form_id");
}

function serial_edit()
{
	serial_init ();
	document.getElementById("serial_button_submit").setAttribute('update',true);
	document.getElementById("serial_button_submit").innerHTML = "Update";
	var oSerialNumberData = new SerialNumberData();
	oSerialNumberData.m_nSerialId = m_oSerialListMemberData.m_nSelectedId;
	SerialNumberDataProcessor.get(oSerialNumberData,serial_gotData);
}

function serial_gotData (oResponse)
{	
	var oSerialNumberData = oResponse.m_arrSerialNumber[0];
	m_oSerialMemberData.m_strSerialType = oSerialNumberData.m_nSerialType;
	$("#serial_input_serialType").val(oSerialNumberData.m_nSerialType);
	$("#serial_input_serialNumber").val(oSerialNumberData.m_nSerialNumber);
	$("#serial_input_prefix").val(oSerialNumberData.m_strPrefix);
	initFormValidateBoxes ("serial_form_id");
}

function serial_submit ()
{
	if (serial_validate ())
	{
		var oSerialNumberData = serial_getFormData ();
		if(document.getElementById("serial_button_submit").getAttribute('update') == "false")
			SerialNumberDataProcessor.create(oSerialNumberData, serial_created);
		else
		{
			oSerialNumberData.m_nSerialId = m_oSerialListMemberData.m_nSelectedId;
			SerialNumberDataProcessor.update(oSerialNumberData, serial_updated);
		}
	}
}

function serial_validate (formId)
{
	return validateForm("serial_form_id") && !serial_validateType ();
}

function serial_validateType ()
{
	var bSerialType = false;
	var strSerialType = $("#serial_input_serialType").val();
	if(strSerialType == "Select")
	{
		informUser ("Please select the serial type!", "kWarning");
		bSerialType = true;
	}
	return bSerialType;
}

function serial_getFormData ()
{
	var oSerialNumberData = new SerialNumberData();
	oSerialNumberData.m_nSerialType = $("#serial_input_serialType").val();
	oSerialNumberData.m_nSerialNumber = $("#serial_input_serialNumber").val();
	oSerialNumberData.m_strPrefix = $("#serial_input_prefix").val();
	return oSerialNumberData;
}

function serial_updated(oResponse, strBusinessName)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("Serial updated successfully.", "kSuccess");
		HideDialog ("dialog");
		serialList_list();
	}
	else
		informUser("Updation failed!", "kError");
}

function serial_created (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Serial created successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate ("serialList", "widgets/inventorymanagement/serial/serialList.js");
	}
	else
		informUser("creation failed!", "kError");
}

function serial_validateSerialType ()
{
	var strSerialType = $("#serial_input_serialType").val();
	if(strSerialType != "Select" && strSerialType != m_oSerialMemberData.m_strSerialType)
	{
		var oSerialNumberData = new SerialNumberData();
		oSerialNumberData.m_nSerialType = strSerialType;
		SerialNumberDataProcessor.list(oSerialNumberData, "", "", serial_listed);
	}
}

function serial_listed (oResponse)
{
	if(oResponse.m_arrSerialNumber.length > 0)
	{
		informUser("Serial type already Exist!", "kWarning");
		document.getElementById("serial_input_serialType").value = "Select";
		document.getElementById('serial_input_serialType').focus();
	}
}

function serial_cancel ()
{
	HideDialog ("dialog");
}