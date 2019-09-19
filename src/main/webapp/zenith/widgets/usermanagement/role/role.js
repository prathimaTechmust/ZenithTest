var role_includeDataObjects = 
[
	'widgets/usermanagement/role/RoleData.js',
	'widgets/usermanagement/action/ActionData.js',
];

 includeDataObjects (role_includeDataObjects, "role_loaded ()");

function role_memberData ()
{
	this.m_arrActions = null;
	this.m_arrSelectedData = new Array ();
	this.m_strCurrentRoleName = "";
}

var m_oRoleMemberData = new role_memberData ();

function role_new ()
{
	initFormValidateBoxes ("role_form_id");
	createPopup ("dialog", "#role_button_submit", "#role_button_cancel", true);
	$('#role_table_listofactionDG').datagrid ({
	    columns:[[  
	        {field:'ckBox',checkbox:true,width:100},
	        {field:'m_strActionName',title:'List Of Actions',sortable:true,width:300}  
	    ]]
	});
	
	$('#role_table_listofactionDG').datagrid (
			{
				onCheck: function (rowIndex, rowData)
				{
					m_oRoleMemberData.m_arrSelectedData = $('#role_table_listofactionDG').datagrid('getChecked'); 
					role_hideErrorLabel ();
				}
			});
	
	$('#role_table_listofactionDG').datagrid (
			{
				onUncheck: function (rowIndex, rowData)
				{
					m_oRoleMemberData.m_arrSelectedData = $('#role_table_listofactionDG').datagrid('getChecked'); 
					role_hideErrorLabel ();
				}
			});
	$('#role_table_listofactionDG').datagrid (
			{
				onCheckAll: function (rows)
				{
					m_oRoleMemberData.m_arrSelectedData = rows; 
					role_hideErrorLabel ();
				}
			});
	
	$('#role_table_listofactionDG').datagrid (
			{
				onUncheckAll: function (rows)
				{
					m_oRoleMemberData.m_arrSelectedData = "";
					role_hideErrorLabel ();
				}
			});
	
	role_buildActionList ();
}

function role_edit ()
{
	role_new ();
	document.getElementById("role_button_submit").setAttribute('update',true);
	document.getElementById("role_button_submit").innerHTML = "Update";
	var oRoleData = new RoleData ();
	oRoleData.m_nRoleId = m_oRoleListMemberData.m_nSelectedRoleId;
	RoleDataProcessor.get(oRoleData, role_gotData);
}

function role_getFormData ()
{
	var arrActions = $('#role_table_listofactionDG').datagrid('getData');
	var oRoleData = new RoleData ();
	oRoleData.m_strRoleName = $("#role_input_roleName").val();
	oRoleData.m_nRoleId = -1;
	return oRoleData;
}

function role_validate ()
{
	return validateForm ("role_form_id") && role_validateDG ();
}
function role_submit ()
{
	if (role_validate ())
	{
		var oRoleData = role_getFormData ();
		oRoleData.m_arrActionData = role_getActionDataArray (); 
		if((document.getElementById("role_button_submit").getAttribute('update') == "false"))
			RoleDataProcessor.create(oRoleData, role_created);
		else
		{
			oRoleData.m_nRoleId = m_oRoleListMemberData.m_nSelectedRoleId;
			RoleDataProcessor.update(oRoleData, role_updated);
		}
	}
}

function role_cancel ()
{
    HideDialog("dialog");
}

function role_clear ()
{
	document.getElementById("role_input_roleName").value = "";
}

function role_created (oRoleResponse)
{
	if(oRoleResponse.m_bSuccess)
	{
		informUser("usermessage_role_rolecreatedsuccessfully", "kSuccess");
		navigate ("rolelist", "widgets/usermanagement/role/listRole.js");
		role_clear ();
	    HideDialog("dialog");
	}
	else if(oRoleResponse.m_nErrorID == 1)
	{
		informUser("usermessage_role_rolenamealreadyexists", "kError");
		role_clear ();
	}
}

function role_updated(oRoleResponse)
{
	if(oRoleResponse.m_bSuccess);
	{
		informUser("usermessage_role_roleupdatedsuccessfully", "kSuccess");
		navigate ("rolelist", "widgets/usermanagement/role/listRole.js");
	}
		
	role_clear ();
    HideDialog("dialog");
}

function role_gotData (oRoleResponse)
{	
	$("#role_input_roleName").val(oRoleResponse.m_arrRoleData[0].m_strRoleName);
	m_oRoleMemberData.m_strCurrentRoleName = oRoleResponse.m_arrRoleData[0].m_strRoleName;
	role_selectRoles (oRoleResponse.m_arrRoleData[0].m_oActions);
	initFormValidateBoxes ("role_form_id");
}

function role_buildActionList ()
{
	RoleDataProcessor.listActionData (role_gotList);
} 

function role_gotList (oRoleResponse)
{
	m_oRoleMemberData.m_arrActions = oRoleResponse.m_arrActionData;
	for(var nIndex = 0; nIndex < oRoleResponse.m_arrActionData.length; nIndex++)
	{
		oRoleResponse.m_arrActionData[nIndex].m_strActionName;
		$("#role_table_listofactionDG").datagrid('appendRow', oRoleResponse.m_arrActionData[nIndex]);
	}
}

function role_hideErrorLabel ()
{
	var hideLabel = document.getElementById("role_div_lblError");
	hideLabel.style.display = "none";
}

function role_validateDG ()
{
	var bIsValidateDG = true;
	if(m_oRoleMemberData.m_arrSelectedData == "")
	{
		bIsValidateDG = false;
		var addLabel = document.getElementById("role_div_lblError");
		addLabel.style.display = "block";
		addLabel.innerHTML = "<Label style='float: bottom;padding-left:.5em; color: red; vertical-align: top; font-size:14px'>Select list of actions</Label>";
	}
	else
		role_hideErrorLabel ();
	return bIsValidateDG;
}

function role_getActionDataArray ()
{
	var oActionDataArray = new Array ();
	var arrSelectedActionData = $('#role_table_listofactionDG').datagrid('getChecked');
	for (var nIndex = 0; nIndex < arrSelectedActionData.length; nIndex++)
	{
		var oActionData = new ActionData ();
		oActionData.m_nActionAreaId = arrSelectedActionData [nIndex].m_nActionAreaId;
		oActionData.m_nActionId = arrSelectedActionData [nIndex].m_nActionId;
		oActionData.m_strActionName = arrSelectedActionData [nIndex].m_strActionName;
		oActionData.m_strActionTarget = arrSelectedActionData [nIndex].m_strActionTarget;
		oActionDataArray.push (oActionData);
	}
	return oActionDataArray;
}

function role_selectRoles (arrActionsToSelect)
{
	assert.isArray(arrActionsToSelect, "arrActionsToSelect expected to be an Array.");
	var arrActions = $('#role_table_listofactionDG').datagrid('getData').rows;
	for (var nIndex= 0; nIndex < arrActions.length; nIndex++)
		if (role_isActionSelected (arrActions[nIndex].m_nActionId, arrActionsToSelect))
			$('#role_table_listofactionDG').datagrid ('selectRow', nIndex);
}

function role_isActionSelected (nActionId, arrSelectedActions)
{
	assert.isNumber(nActionId, "nActionId expected to be a Number.");
	assert(nActionId !== 0, "nActionId cannot be equal to zero.");
	assert.isArray(arrSelectedActions, "arrSelectedActions expected to be a Array.");
	var bActionSelected = false;
	for (var nIndex=0; nIndex < arrSelectedActions.length && !bActionSelected; nIndex++)
		bActionSelected = arrSelectedActions[nIndex].m_nActionId == nActionId;
	return bActionSelected
}

function role_checkRoleName ()
{
	var strRoleName = $("#role_input_roleName").val();
	if (strRoleName != m_oRoleMemberData.m_strCurrentRoleName)
	{
		var oRoleData = new RoleData ();
		oRoleData.m_strRoleName = strRoleName;
		RoleDataProcessor.list(oRoleData, "", "", 0, 0, role_listed);
	}
}

function role_listed (oResponse)
{
	if (oResponse.m_arrRoleData.length > 0)
	{
		informUser ("usermessage_role_rolealreadyexists", "kWarning");
		$("#role_input_roleName").val("");
		document.getElementById("role_input_roleName").focus();
	}
}