var action_includeDataObjects = 
[
	'widgets/usermanagement/action/ActionData.js',
	'widgets/usermanagement/actionarea/ActionAreaData.js'
];

 includeDataObjects (action_includeDataObjects, "action_loaded()");

function action_init ()
{
	initFormValidateBoxes ("action_form_id");
	actionInformation_populateActionAreaList ();
}

function action_new ()
{
	createPopup ("dialog", "#action_button_submit", "#action_button_cancel", true);
	action_init ();
}

function action_edit ()
{
	var oActionData = new ActionData ();
	oActionData.m_nActionId = m_oActionListMemberData.m_nSelectedActionId;
	createPopup ("dialog", "#action_button_submit", "#action_button_cancel", true);
	action_init ();
	document.getElementById("action_button_submit").setAttribute('update',true);
	document.getElementById("action_button_submit").innerHTML = "Update";
	ActionDataProcessor.get (oActionData, action_gotData);
}

function action_getFormData ()
{
	var oActionData = new ActionData ();
	oActionData.m_oActionArea = new ActionAreaData ();
	oActionData.m_oActionArea.m_nActionAreaId = $("#action_select_actionAreaName").val();
	oActionData.m_strActionName =$("#action_input_actionName").val();
	oActionData.m_strActionTarget = $("#action_input_actionTarget").val();
	oActionData.m_nSequenceNumber = $("#action_input_sequenceNumber").val();
	return oActionData;
}

function action_validate ()
{
	return validateForm ("action_form_id") && action_selectValidate ();
}

function action_selectValidate ()
{
	var bIsSelectFieldValid = true;
	if(($("#action_select_actionAreaName").val())== -1)
	{
		informUser("usermessage_action_Pleaseselectactionarea", "kWarning");
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function action_submit ()
{
	if (action_validate())
	{
		var oActionData = action_getFormData ();
		if(document.getElementById("action_button_submit").getAttribute('update') == "false")
			ActionDataProcessor.create(oActionData, action_created);
		else
		{
			oActionData.m_nActionId = m_oActionListMemberData.m_nSelectedActionId;
			ActionDataProcessor.update(oActionData, action_updated);
		}
	}
}

function action_cancel ()
{
	HideDialog ("dialog");
}

function action_created (oActionResponse)
{
	if(oActionResponse.m_bSuccess)
	{
		informUser("usermessage_action_actioncreatedsuccessfully", "kSuccess");
		HideDialog ("dialog");
		navigate("listAction", "widgets/usermanagement/action/listAction.js");
	}
}

function action_updated (oActionResponse)
{
	if(oActionResponse.m_bSuccess)
	{
		informUser("usermessage_action_actionupdatedsuccessfully", "kSuccess");
		clearGridData ("#listAction_table_actions");
		HideDialog ("dialog");
		navigate("listAction", "widgets/usermanagement/action/listAction.js");
	}
}

function action_gotData (oActionResponse)
{	
	var oActionData = oActionResponse.m_arrActionData[0];
	$("#action_select_actionAreaName").val(oActionData.m_oActionArea.m_strActionAreaName);
	$("#action_input_actionName").val(oActionData.m_strActionName);
	$("#action_input_actionTarget").val(oActionData.m_strActionTarget);
	$("#action_input_sequenceNumber").val(oActionData.m_nSequenceNumber);
}

function actionInformation_populateActionAreaList ()
{
	var oActionData = action_getFormData ();
	ActionAreaDataProcessor.list (oActionData.m_oActionArea, "m_strActionAreaName", "asc", 0, 0,
			function (oActionAreaResponse)
			{
				actionInformation_prepareActionAreaDD ("action_select_actionAreaName", oActionAreaResponse);
			}				
		);
}

function actionInformation_prepareActionAreaDD (strActionAreaDD, oActionAreaResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption(-1, "Select"));
	for (var nIndex = 0; nIndex < oActionAreaResponse.m_arrActionArea.length; nIndex++)
		arrOptions.push (CreateOption (oActionAreaResponse.m_arrActionArea [nIndex].m_nActionAreaId,
				oActionAreaResponse.m_arrActionArea [nIndex].m_strActionAreaName));
	PopulateDD (strActionAreaDD, arrOptions);
}