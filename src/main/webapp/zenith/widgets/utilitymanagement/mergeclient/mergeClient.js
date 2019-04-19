var mergeClient_includeDataObjects = 
[
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js'
];

 includeDataObjects (mergeClient_includeDataObjects, "mergeClient_loaded ()");

function mergeClient_loaded ()
{
	loadPage ("utilitymanagement/mergeclient/mergeClient.html", "dialog", "mergeClient_init ()");
}

function mergeClient_init ()
{
	createPopup("dialog", "#mergeClient_button_merge", "#mergeClient_button_cancel", true);
	mergeClient_initClientCombobox ();
	mergeClient_initializeDataGrid ();
}

function mergeClient_initClientCombobox ()
{
	$('#mergeClient_input_clientName').combobox
	({
		valueField:'m_nClientId',
	    textField:'m_strCompanyName',
	    selectOnNavigation: false,
	    loader: getFilteredClientData,
		mode: 'remote',
	});
	var clientNameTextBox = $('#mergeClient_input_clientName').combobox('textbox');
	clientNameTextBox[0].placeholder = "Enter Your Client Name";
}

var getFilteredClientData = function(param, success, error)
{
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oClientData = new ClientData ();
		oClientData.m_strCompanyName = strQuery;
		ClientDataProcessor.getClientSuggesstions (oClientData, "", "", function(oResponse)
				{
					success(oResponse.m_arrClientData);
				});
	}
	else
		success(new Array ());
}

function mergeClient_initializeDataGrid ()
{
	$('#mergeClient_table_clientList').datagrid
	(
		{
			columns:
			[[
			  	{field:'ckBox',checkbox:true},
				{field:'m_strCompanyName',title:'Client Name',sortable:true,width:200}
			]]
		}
	);
	
	$('#mergeClient_table_clientList').datagrid
	(
		{
			onSortColumn: function (strColumn, strOrder)
			{
				mergeClient_list (strColumn, strOrder, 0, 0);
			}
		}
	)
	
	mergeClient_list ("m_strCompanyName", "asc", 0, 0);
}

function mergeClient_filter ()
{
	mergeClient_list ("m_strCompanyName", "asc", 0, 0);
}

function mergeClient_getFormData () 
{
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = dwr.util.getValue("mergeClient_input_clientNameFilter");
	oClientData.m_oDemography = new DemographyData ();
	return oClientData;
}


function mergeClient_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "progressbarLoaded ()");
	var oClientData = mergeClient_getFormData ();
	ClientDataProcessor.list(oClientData, strColumn, strOrder, nPageNumber, nPageSize, mergeClient_listed);
}

function mergeClient_listed (oClientResponse)
{
	$('#mergeClient_table_clientList').datagrid('loadData', oClientResponse.m_arrClientData);
	HideDialog("secondDialog");
}

function mergeClient_cancel ()
{
	HideDialog ("dialog");
}

function mergeClient_validate ()
{
	return validateForm("mergeClient_form_id") && mergeClient_validateSelectField ();
}

function mergeClient_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if(!isValidClient())
	{
		informUser("Please Select Main Account Client Name", "kWarning");
		$('#mergeClient_input_clientName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function isValidClient()
{
	var bIsValid = false;
	try
	{
		var strClient = $('#mergeClient_input_clientName').combobox('getValue');
		if(strClient != null && strClient > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function mergeClient_validateDGMergeClients ()
{
	var bIsValid = false;
	var arrClients = $('#mergeClient_table_clientList').datagrid('getChecked');
	if (arrClients.length > 0)
		bIsValid = true;
	else
		informUser("Please Select Atleast One Merge Account", "kWarning")
	return bIsValid;
}

function mergeClient_getSelectedClients ()
{
	var arrClient = $('#mergeClient_table_clientList').datagrid('getChecked');
	var arrClientData = new Array ();
	for(var nIndex = 0; nIndex < arrClient.length; nIndex++)
	{
		var oClientData = new ClientData ();
		oClientData.m_nClientId = arrClient[nIndex].m_nClientId;
		arrClientData.push(oClientData)
	}
	return arrClientData;
}

function mergeClient_submit ()
{
	if (mergeClient_validate () && mergeClient_validateDGMergeClients ())
	{
		disable ("mergeClient_button_merge");
		var nClientId = $('#mergeClient_input_clientName').combobox('getValue');
		var arrClientData = mergeClient_getSelectedClients ()
		SalesDataProcessor.mergeClient (nClientId, arrClientData, mergeClient_mergedClients);
	}
}

function mergeClient_mergedClients (oResponse)
{
	if (oResponse.m_bSuccess)
	{	
		HideDialog ("dialog");
		informUser ("Client(s) Merged Succesfully", "kSuccess");
	}
	else
	{
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Client(s) Merge Failed", "kError");
		enable ("mergeClient_button_merge");
	}
}
