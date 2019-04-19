var tallyTransformKey_includeDataObjects = 
[
	'widgets/utilitymanagement/tallytransform/TallyTransformKeyData.js'
];

 includeDataObjects (tallyTransformKey_includeDataObjects, "tallyTransformKey_loaded ()");

function tallyTransformKey_MemberData ()
{
	this.m_nTallyTranformKeyId = -1;
	this.m_strKey = "";
}

var m_oTallyTransformKeyMemberData = new tallyTransformKey_MemberData ();

function tallyTransformKey_init()
{
	createPopup ("dialog", "#tallyTransformKey_button_create", "#tallyTransformKey_button_cancel", true);
	initFormValidateBoxes ("tallyTransformKey_form_Id");
}

function tallyTransformKey_new ()
{   
	tallyTransformKey_init();
}

function tallyTransformKey_edit()
{
	tallyTransformKey_init();
	document.getElementById("tallyTransformKey_button_create").setAttribute('update', true);
	document.getElementById("tallyTransformKey_button_create").innerHTML = "Update";
	var oTallyTransformKeyData = new TallyTransformKeyData ();
	oTallyTransformKeyData.m_nTallyTranformKeyId = m_oTallyTransformKeyMemberData.m_nTallyTranformKeyId;
	TallyTransformKeyDataProcessor.get(oTallyTransformKeyData, tallyTransformKey_gotData);
}

function tallyTransformKey_gotData(oResponse)
{
	var oTallyTransformKeyData = oResponse.m_arrTallyTransformKeyData[0];
	m_oTallyTransformKeyMemberData.m_strKey = oTallyTransformKeyData.m_strKey;
	dwr.util.setValue("tallyTransformKey_input_key", oTallyTransformKeyData.m_strKey);
	initFormValidateBoxes ("tallyTransformKey_form_Id");
}

function tallyTransformKey_getFormData()
{
	var oTallyTransformKeyData = new TallyTransformKeyData ();
	oTallyTransformKeyData.m_strKey = dwr.util.getValue("tallyTransformKey_input_key");
	return oTallyTransformKeyData;
}

function tallyTransformKey_validate ()
{
	return validateForm("tallyTransformKey_form_Id");
}

function tallyTransformKey_submit()
{
	if (tallyTransformKey_validate())
	{
		disable ("tallyTransformKey_button_create");
		var oTallyTransformKeyData = tallyTransformKey_getFormData ();
		if((document.getElementById("tallyTransformKey_button_create").getAttribute('update') == "false"))
			TallyTransformKeyDataProcessor.create(oTallyTransformKeyData, tallyTransformKey_created);
		else
		{
			oTallyTransformKeyData.m_nTallyTranformKeyId = m_oTallyTransformKeyMemberData.m_nTallyTranformKeyId;
			TallyTransformKeyDataProcessor.update(oTallyTransformKeyData, tallyTransformKey_updated);
		}
	}
}

function tallyTransformKey_created(oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Tally Transform Key Created Succesfully", "kSuccess");
		HideDialog("dialog");
		navigate("tallyTransformKeyList", "widgets/utilitymanagement/tallytransform/tallyTransformKeyList.js");
	}
	else if(oResponse.m_nErrorID == 1)
	{
		informUser ("Tally Transform Key Creation Failed", "kError");
		enable ("tallyTransformKey_button_create");
	}
}

function tallyTransformKey_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Tally Transform Key Updated Succesfully", "kSuccess");
		HideDialog("dialog");
		navigate("tallyTransformList", "widgets/utilitymanagement/tallytransform/tallyTransformKeyList.js");
	}
	else
	{
		informUser ("Tally Transform Key Updation Failed", "kError");
		enable ("tallyTransformKey_button_create");
	}
}

function tallyTransformKey_cancel()
{
	 HideDialog("dialog");
}

function tallyTransformKey_validateForDuplicate ()
{
	dwr.engine.setAsync(false);
	var strKey = dwr.util.getValue("tallyTransformKey_input_key");
	if(strKey != m_oTallyTransformKeyMemberData.m_strKey)
	{
		var oTallyTransformKeyData = new TallyTransformKeyData ();
		oTallyTransformKeyData.m_strKey = strKey;
		TallyTransformKeyDataProcessor.list(oTallyTransformKeyData, "", "", tallyTransformKey_gotList);
	}
}

function tallyTransformKey_gotList (oResponse)
{
	if(oResponse.m_arrTallyTransformKeyData.length > 0)
	{
		informUser("Key already Exist!", "kWarning");
		document.getElementById("tallyTransformKey_input_key").value = "";
		document.getElementById('tallyTransformKey_input_key').focus();
	}
	dwr.engine.setAsync(true);
}
