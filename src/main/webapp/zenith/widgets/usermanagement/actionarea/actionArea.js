var actionArea_includeDataObjects = 
[
	'widgets/usermanagement/actionarea/ActionAreaData.js'
];

 includeDataObjects (actionArea_includeDataObjects, "actionArea_loaded()");

function actionArea_MemberData ()
{
	this.m_strActionAreaName = "";
	this.m_strCurrentActionArea = "";
	this.m_arrActionAreaName = [];
}

var m_oActionAreaMemberData = new actionArea_MemberData ();

function actionArea_init()
{
	createPopup ("dialog", "#actionArea_button_submit", "#actionArea_button_cancel", true);
	initFormValidateBoxes ("actionArea_form_id");
}

function actionArea_new ()
{   
	actionArea_init();
	actionArea_getActionAreas();
}

function actionArea_getActionAreas ()
{
	var oActionAreaData = new ActionAreaData;
	ActionAreaDataProcessor.list(oActionAreaData, "", "", 0, 0, actionArea_gotActionAreas);
}

function actionArea_gotActionAreas (oResponse)
{
	m_oActionAreaMemberData.m_arrActionAreaName = oResponse.m_arrActionArea
}

function actionArea_edit()
{
	actionArea_init();
	document.getElementById("actionArea_button_submit").setAttribute('update',true);
	document.getElementById("actionArea_button_submit").innerHTML = "Update";
	var oActionAreaData = new ActionAreaData();
	oActionAreaData.m_nActionAreaId = m_olistActionArea_MemberData.m_nActionAreaId;
	ActionAreaDataProcessor.get(oActionAreaData,actionArea_gotData);
}

function actionArea_getFormData()
{
	var oActionAreaData = new ActionAreaData;
	oActionAreaData.m_strActionAreaName = $("#actionArea_input_actionAreaName").val();
	oActionAreaData.m_nSequenceNumber = $("#actionArea_input_sequenceNumber").val();
	return oActionAreaData;
}

function actionArea_validate()
{
	return validateForm("actionArea_form_id");
}

function actionArea_submit()
{
	if (actionArea_validate())
	{
		var oActionAreaData = actionArea_getFormData ();
		if((document.getElementById("actionArea_button_submit").getAttribute('update') == "false"))
			ActionAreaDataProcessor.create(oActionAreaData, 
					function (oActionAreaResponse)
					{
						actionArea_created(oActionAreaResponse, oActionAreaData.m_strActionAreaName)
					});
		else
		{
			oActionAreaData.m_nActionAreaId = m_olistActionArea_MemberData.m_nActionAreaId;
			ActionAreaDataProcessor.update(oActionAreaData, 
					function (oActionAreaResponse)
					{
						actionArea_updated(oActionAreaResponse, oActionAreaData.m_strActionAreaName)
					});
		}
	}
}

function actionArea_cancel()
{
	 HideDialog("dialog");
}

function actionArea_clear()
{
	document.getElementById("actionArea_input_actionAreaName").value = "";
}

function actionArea_created(oActionAreaResponse, strActionAreaName)
{
	if(oActionAreaResponse.m_bSuccess)
	{
		informUser("usermessage_actionarea_createdsuccessfully", "kSuccess");
		HideDialog("dialog");
		navigate("actionArea", "widgets/usermanagement/actionarea/listActionArea.js");
	}
	else if(oActionAreaResponse.m_nErrorID == 1)
	{
		informUser ("usermessage_actionarea_actionareaalreadyexists", "kError");
		actionArea_clear();
	}
}

function actionArea_updated(oActionAreaResponse,strActionAreaName)
{
	if(oActionAreaResponse.m_bSuccess)
	{
		informUser ("usermessage_actionarea_updatedsuccessfully", "kSuccess");
		clearGridData ("#listActionArea_table_dataGridId");
		navigate("actionArea", "widgets/usermanagement/actionarea/listActionArea.js");
	}
	HideDialog("dialog");
}

function actionArea_gotData(oActionAreaResponse)
{
	m_oActionAreaMemberData.m_strActionAreaName = oActionAreaResponse.m_arrActionArea[0].m_strActionAreaName;
	m_oActionAreaMemberData.m_strCurrentActionArea = oActionAreaResponse.m_arrActionArea[0].m_strActionAreaName;
	$("#actionArea_input_actionAreaName").val(oActionAreaResponse.m_arrActionArea[0].m_strActionAreaName);
	$("#actionArea_input_sequenceNumber").val(oActionAreaResponse.m_arrActionArea[0].m_nSequenceNumber);
	initFormValidateBoxes ("actionArea_form_id");
}

function actionArea_checkActionArea ()
{
	var strActionArea = $("#actionArea_input_actionAreaName").val();
	if (strActionArea != m_oActionAreaMemberData.m_strCurrentActionArea)
		actionArea_checkForActionArea(strActionArea);
}

function actionArea_checkForActionArea (strActionArea)
{
	for(var nIndex=0; nIndex<m_oActionAreaMemberData.m_arrActionAreaName.length;nIndex++)
	{
		if(strActionArea == m_oActionAreaMemberData.m_arrActionAreaName[nIndex].m_strActionAreaName)
		{
			informUser ("usermessage_actionArea_actionAreaalreadyexists", "kWarning");
			$("#actionArea_input_actionAreaName").val("");
			document.getElementById("actionArea_input_actionAreaName").focus();
		}
	}
}