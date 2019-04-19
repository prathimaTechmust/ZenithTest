var clientGroup_includeDataObjects = 
[
	'widgets/clientmanagement/ClientGroupData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/clientmanagement/SiteData.js'
];


includeDataObjects (clientGroup_includeDataObjects, "clientGroup_loaded ()");

function clientGroup_memberData ()
{
	this.m_arrClient = new Array ();
	this.m_strGroupName = "";
	this.m_strClientGroupName = "";
}

var m_oClientGroupMemberData = new clientGroup_memberData ();

function clientGroup_new ()
{
	clientGroup_init ();
	initFormValidateBoxes ("clientGroup_form_id");
}

function clientGroup_init ()
{
	createPopup ("dialog", "#clientGroup_button_submit", "#clientGroup_button_cancel", true);
	clientGroup_initDataGrid ();
}

function clientGroup_initDataGrid ()
{
	$('#clientGroup_table_listofclientDG').datagrid ({
	    columns:[[  
	        {field:'m_strCompanyName',title:'Client Name',sortable:true,width:350},
	        {field:'Actions',title:'Action',width:80,align:'center',
				formatter:function(value,row,index)
	        	{
	        		return clientGroupData_addActions (row, index);
	        	}
			}
	    ]]
	});
}

function clientGroup_addClients ()
{
	navigate ("addClientGroup", "widgets/clientmanagement/clientGroupAdd.js");
}

function clientGroup_submit ()
{
	if (clientGroup_validate () && !clientGroup_checkGroupName ())
		loadPage ("include/process.html", "ProcessDialog", "clientGroup_submit_progressbarLoaded ()");
}

function clientGroup_submit_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var oClientGroupData = clientGroup_getFormData ();
	if((document.getElementById("clientGroup_button_submit").getAttribute('update') == "false"))
		ClientGroupDataProcessor.create(oClientGroupData, clientGroupData_created);
	else
	{
		oClientGroupData.m_nGroupId = m_oClientGroupListMemberData.m_nSelectedClientGroupId;
		ClientGroupDataProcessor.update(oClientGroupData, clientGroupData_updated);
	}
}
function clientGroup_validate ()
{
	return validateForm ("clientGroup_form_id");
}

function clientGroup_getFormData ()
{
	var oClientGroupData = new ClientGroupData ();
	oClientGroupData.m_oUserCredentialsData = new UserInformationData ();
	oClientGroupData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oClientGroupData.m_strGroupName = $("#clientGroup_input_clientGroupName").val();
	oClientGroupData.m_arrClientData = clientGroup_getClientDataArray (); 
	return oClientGroupData;
}

function clientGroup_getClientDataArray ()
{
	var oClientDataArray = new Array ();
	var arrSelectedClientData = $('#clientGroup_table_listofclientDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrSelectedClientData.length; nIndex++)
	{
		var oClientData = new ClientData ();
		oClientData.m_nClientId = arrSelectedClientData [nIndex].m_nClientId;
		oClientDataArray.push (oClientData);
	}
	return oClientDataArray;
}

function clientGroupData_created (oResponse)
{
	HideDialog ("ProcessDialog");
	if(oResponse.m_bSuccess)
	{
		informUser("Client group created successfully.", "kSuccess");
		navigate ("", "widgets/clientmanagement/clientGroupListAdmin.js");
		HideDialog("dialog");
	}
	else if(oResponse.m_nErrorID == 1)
	{
		informUser("Client group name already exists", "kError");
	}
}

function clientGroup_edit ()
{
	createPopup('ProcessDialog', '', '', true);
	clientGroup_init ();
	document.getElementById("clientGroup_button_submit").setAttribute('update', true);
	document.getElementById("clientGroup_button_submit").innerHTML = "Update";
	var oClientGroupData = new ClientGroupData ();
	oClientGroupData.m_nGroupId = m_oClientGroupMemberData.m_nGroupId;
	oClientGroupData.m_oUserCredentialsData = new UserInformationData ();
	oClientGroupData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ClientGroupDataProcessor.get (oClientGroupData, clientGroup_gotClientGroupData);
}

function clientGroup_gotClientGroupData (oResponse)
{
	HideDialog ("ProcessDialog");
	var oClientGroupData = oResponse.m_arrGroupData[0];
	m_oClientGroupMemberData.m_strClientGroupName = oClientGroupData.m_strGroupName;
	$("#clientGroup_input_clientGroupName").val(oClientGroupData.m_strGroupName);
	$('#clientGroup_table_listofclientDG').datagrid('loadData',oClientGroupData.m_oClientSet);
	initFormValidateBoxes ("clientGroup_form_id");
}

function clientGroupData_updated (oResponse)
{
	HideDialog ("ProcessDialog");
	if(oResponse.m_bSuccess)
	{
		informUser("Client group updated successfully.", "kSuccess" );
		document.getElementById("clientGroupList_div_listDetail").innerHTML = "";
		navigate("", "widgets/clientmanagement/clientGroupListAdmin.js");
		HideDialog ("dialog");
	}
	else
		informUser("Updation failed!", "kError");
}

function clientGroup_cancel ()
{
	HideDialog("dialog");
}

function clientGroupData_addActions (row, index)
{
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="clientGroupData_delete ('+index+')"/> </td>'+
				'</tr>'+
			'</table>'
		return oActions;
}

function clientGroupData_delete (nIndex)
{
	$('#clientGroup_table_listofclientDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#clientGroup_table_listofclientDG');
}

function clientGroup_checkGroupName ()
{
	var bIsGroupNameExist = false;
	var oClientGroupData = new ClientGroupData ();
	oClientGroupData.m_oUserCredentialsData = new UserInformationData ();
	oClientGroupData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ClientGroupDataProcessor.list(oClientGroupData, "", "",1,10, function(oResponse)
		{
			var strGroupName = $("#clientGroup_input_clientGroupName").val();
			if(m_oClientGroupMemberData.m_strClientGroupName != strGroupName)
			{
				for(var nIndex=0; nIndex <oResponse.m_arrGroupData.length ; nIndex++)
				{
					if(strGroupName == oResponse.m_arrGroupData[nIndex].m_strGroupName)
					{
						informUser ("Group name already exists.", "kWarning");
						$("#clientGroup_input_clientGroupName").val("");
						document.getElementById("clientGroup_input_clientGroupName").focus();
						bIsGroupNameExist = true;
					}
					
				}
			}
			return bIsGroupNameExist;
		});
}
