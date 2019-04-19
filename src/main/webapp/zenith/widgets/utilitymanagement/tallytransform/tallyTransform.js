var tallyTransform_includeDataObjects = 
[
	'widgets/utilitymanagement/tallytransform/TallyTransformData.js',
	'widgets/utilitymanagement/tallytransform/TallyTransformKeyData.js'
];

 includeDataObjects (tallyTransform_includeDataObjects, "tallyTransform_loaded ()");

function tallyTransform_MemberData ()
{
	this.m_nTallyTranformId = -1;
}

var m_oTallyTransformMemberData = new tallyTransform_MemberData ();

function tallyTransform_init()
{
	createPopup ("dialog", "#tallyTransform_button_create", "#tallyTransform_button_cancel", true);
	dwr.engine.setAsync(false);
	tallyTransform_populateTallyKeyList ();
	initFormValidateBoxes ("tallyTransform_form_Id");
	dwr.engine.setAsync(true);
}

function tallyTransform_new ()
{   
	tallyTransform_init();
}

function tallyTransform_edit()
{
	tallyTransform_init();
	document.getElementById("tallyTransform_button_create").setAttribute('update', true);
	document.getElementById("tallyTransform_button_create").innerHTML = "Update";
	var oTallyTransformData = new TallyTransformData ();
	oTallyTransformData.m_nTallyTranformId = m_oTallyTransformMemberData.m_nTallyTranformId;
	TallyTransformDataProcessor.get(oTallyTransformData, tallyTransform_gotData);
}

function tallyTransform_gotData(oResponse)
{
	var oTallyTransformData = oResponse.m_arrTallyTransformData[0];
	dwr.util.setValue("tallyTransform_select_key", oTallyTransformData.m_strKey);
	dwr.util.setValue("tallyTransform_input_taxName", oTallyTransformData.m_strTaxName);
	dwr.util.setValue("tallyTransform_input_percentage", oTallyTransformData.m_nPercentage);
	dwr.util.setValue("tallyTransform_input_tallyKey", oTallyTransformData.m_strTallyKey);
	initFormValidateBoxes ("tallyTransform_form_Id");
}

function tallyTransform_getFormData()
{
	var oTallyTransformData = new TallyTransformData ();
	oTallyTransformData.m_strKey = dwr.util.getValue("tallyTransform_select_key");
	oTallyTransformData.m_strTaxName = dwr.util.getValue("tallyTransform_input_taxName");
	oTallyTransformData.m_nPercentage = dwr.util.getValue("tallyTransform_input_percentage");
	oTallyTransformData.m_strTallyKey = dwr.util.getValue("tallyTransform_input_tallyKey");
	return oTallyTransformData;
}

function tallyTransform_validate ()
{
	return validateForm("tallyTransform_form_Id") && tallyTransform_validateSelectBox ();
}

function tallyTransform_submit()
{
	if (tallyTransform_validate())
	{
		disable ("tallyTransform_button_create");
		var oTallyTransformData = tallyTransform_getFormData ();
		if((document.getElementById("tallyTransform_button_create").getAttribute('update') == "false"))
			TallyTransformDataProcessor.create(oTallyTransformData, tallyTransform_created);
		else
		{
			oTallyTransformData.m_nTallyTranformId = m_oTallyTransformMemberData.m_nTallyTranformId;
			TallyTransformDataProcessor.update(oTallyTransformData, tallyTransform_updated);
		}
	}
}

function tallyTransform_created(oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Tally Transform Information Created Succesfully", "kSuccess");
		HideDialog("dialog");
		navigate("tallyTransformList", "widgets/utilitymanagement/tallytransform/tallyTransformList.js");
	}
	else if(oResponse.m_nErrorID == 1)
	{
		informUser ("Tally Transform Information Creation Failed", "kError");
		enable ("tallyTransform_button_create");
	}
}

function tallyTransform_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Tally Transform Information Updated Succesfully", "kSuccess");
		HideDialog("dialog");
		navigate("tallyTransformList", "widgets/utilitymanagement/tallytransform/tallyTransformList.js");
	}
	else
	{
		informUser ("Tally Transform Information Updation Failed", "kError");
		enable ("tallyTransform_button_create");
	}
}

function tallyTransform_cancel()
{
	 HideDialog("dialog");
}

function tallyTransform_populateTallyKeyList ()
{
	var oTallyTransformKeyData = new TallyTransformKeyData ();
	TallyTransformKeyDataProcessor.list (oTallyTransformKeyData, "m_strKey", "asc",
			function (oResponse)
			{
				tallyTransform_prepareTallyKeyListDD ("tallyTransform_select_key", oResponse);
			}				
		);
}

function tallyTransform_prepareTallyKeyListDD (strApplicableTaxDD, oResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption (-1,"Select"));
	for (var nIndex = 0; nIndex < oResponse.m_arrTallyTransformKeyData.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrTallyTransformKeyData [nIndex].m_strKey, oResponse.m_arrTallyTransformKeyData [nIndex].m_strKey));
	PopulateDD (strApplicableTaxDD, arrOptions);
}

function tallyTransform_validateSelectBox ()
{
	var bIsSelectFieldValid = true;
	if(dwr.util.getValue ("tallyTransform_select_key") == -1)
	{
		informUser("Please Select a Key", "kWarning");
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}
