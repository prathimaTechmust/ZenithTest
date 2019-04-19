var listClient_includeDataObjects = 
	[
	 	'widgets/clientmanagement/ClientData.js',
	 	'widgets/clientmanagement/DemographyData.js',
	 	'widgets/clientmanagement/ContactData.js',
	];


includeDataObjects (listClient_includeDataObjects, "listClient_loaded ()");

function listClient_memberData ()
{
	this.m_oSelectedClientId = -1;
	this.m_oSelectedContactId = -1;
	this.m_strActionClentFunction = "listClient_addHyphen()";
	this.m_nPageNumber = 1;
	this.m_nPageSize = 10;
	this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "asc";
    this.m_oClientData = "";
}

var m_oClientListMemberData = new listClient_memberData ();

function listClient_addHyphen()
{
	var oHyphen = '<table class="trademust">'+
	'<tr>' +
		'<td style="border-style:none;">-</td>'+
	'</tr>'+
	'</table>'
	return oHyphen;
}

function listClient_init ()
{
	listClient_createDataGrid ();
}

function listClient_initEdit ()
{
	m_oClientListMemberData.m_strActionClentFunction = "listClient_addActions (row, index)";
	document.getElementById ("listClient_button_add").style.visibility="visible";
	document.getElementById ("listClient_button_import").style.visibility="visible";
	document.getElementById ("listClient_button_export").style.visibility="visible";
	listClient_init ();
}

function listClient_addActions (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
				'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="listClient_edit ('+row.m_nClientId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="listClient_delete ('+index+')"/> </td>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="listClient_getInfo ('+row.m_nClientId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function listClient_getFormData () 
{
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = $("#filterClientInfo_input_clientName").val();
	oClientData.m_strCity = $("#filterClientInfo_input_city").val();
	var oDemographyData = new DemographyData ();
	oClientData.m_oDemography = oDemographyData ;
	return oClientData;
}

function listClient_createDataGrid ()
{
	initHorizontalSplitter("#listClient_div_horizontalSplitter", "#listClient_table_clients");
	$('#listClient_table_clients').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_strCompanyName',title:'Client Name',sortable:true,width:200,
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
				},
				{field:'m_strCity',title:'City',sortable:true,width:240},
				{field:'m_strTelephone',title:'Telephone',sortable:true,width:150},
				{field:'m_strEmail',title:'Email',sortable:true,width:190},
				{field:'Action',title:'Action',sortable:false,width:100,align:'center',
		        	formatter:function(value,row,index)
		        	{
		        		return listClient_displayImages (row, index);
		        	}
		        },
			]]
		}
	);
	
	$('#listClient_table_clients').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				listClient_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				m_oClientListMemberData.m_strSortColumn = strColumn;
				m_oClientListMemberData.m_strSortOrder = strOrder;
				listClient_list (strColumn, strOrder, m_oClientListMemberData.m_nPageNumber, m_oClientListMemberData.m_nPageSize);
			}
		}
	)
	listClient_initDGPagination ();
	listClient_list (m_oClientListMemberData.m_strSortColumn, m_oClientListMemberData.m_strSortOrder, 1, 10);
}

function listClient_filter ()
{
	listClient_list (m_oClientListMemberData.m_strSortColumn, m_oClientListMemberData.m_strSortOrder, 1, 10);
}

function listClient_initDGPagination ()
{
	$('#listClient_table_clients').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oClientListMemberData.m_nPageNumber = nPageNumber;
				listClient_list (m_oClientListMemberData.m_strSortColumn,m_oClientListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listClient_div_listDetail").innerHTML = "";
			},
			onSelectPage:function(nPageNumber, nPageSize)
			{
				m_oClientListMemberData.m_nPageNumber = nPageNumber;
				m_oClientListMemberData.m_nPageSize = nPageSize;
				listClient_list (m_oClientListMemberData.m_strSortColumn,m_oClientListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listClient_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listClient_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oClientListMemberData.m_nIndex = nIndex;
	document.getElementById("listClient_div_listDetail").innerHTML = "";
	var oClientData = new ClientData ();
	oClientData.m_nClientId = oRowData.m_nClientId;
	InvoiceDataProcessor.getClientDetails (oRowData.m_nClientId, function (strXMLData)
		{
			m_oClientListMemberData.m_oClientData = oRowData;
			populateXMLData (strXMLData, "clientmanagement/clientDetails.xslt", 'listClient_div_listDetail');
			listClient_initializeContactDetailsDG ();
			listClient_initializeSiteDetailsDG ();
			ClientDataProcessor.get (oClientData, listClient_gotData);
		});
}

function listClient_initializeContactDetailsDG ()
{
	$('#clientDetails_table_clientContactDetailsDG').datagrid
	(
		{
			columns:
			[[
				{field:'m_nContactId',title:'Contact ID',sortable:true,width:100},
				{field:'m_strContactName',title:'Contact Name',sortable:true,width:130},
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:150},
				{field:'m_strEmail',title:'E-mail',sortable:true,width:150}
			]]
		}
	);
}

function listClient_initializeSiteDetailsDG ()
{
	$('#clientDetails_table_clientSiteDetailsDG').datagrid
	(
		{
			columns:
			[[
				{field:'m_nSiteId',title:'Site ID',sortable:true,width:60},
				{field:'m_strSiteName',title:'Site Name',sortable:true,width:100},
				{field:'m_strSiteAddress',title:'Site Address',sortable:true,width:150},
				{field:'m_nSiteStatus',title:'Site Status',sortable:true,width:100}
			]]
		}
	);
}

function listClient_gotData (oResponse)
{
	var oClientData = oResponse.m_arrClientData[0];
	$('#clientDetails_table_clientContactDetailsDG').datagrid ('loadData',oClientData.m_oContacts);
	$('#clientDetails_table_clientSiteDetailsDG').datagrid ('loadData',oClientData.m_oSites);
}

function listClient_showToolTip (strValue, fieldId)
{
	assert.isString(strValue, "strValue expected to be a string.");
	assert.isString(fieldId, "fieldId expected to be a string.");
	var strName = "";
	if(strValue !=null && strValue.length > 38) 
	{
		strName = strValue.substr(0,38)+"...";
		document.getElementById(fieldId).title = strValue;
	}
	else
	{
		document.getElementById(fieldId).title = "";
		strName = strValue;
	}
	return strName;
}

function listClient_displayImages (row, index)
{
	var oImage = eval (m_oClientListMemberData.m_strActionClentFunction);
	return oImage;
}

function listClient_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oClientListMemberData.m_strSortColumn = strColumn;
	m_oClientListMemberData.m_strSortOrder = strOrder;
	m_oClientListMemberData.m_nPageNumber = nPageNumber;
	m_oClientListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", " listClient_progressbarLoaded ()");
}

function listClient_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oClientData = listClient_getFormData ();
	ClientDataProcessor.list(oClientData, m_oClientListMemberData.m_strSortColumn, m_oClientListMemberData.m_strSortOrder, m_oClientListMemberData.m_nPageNumber, m_oClientListMemberData.m_nPageSize, listClient_listed);
}

function listClient_showAddClientPopup ()
{
	navigate ("clientInfo", "widgets/clientmanagement/newClientInfo.js");
}

function listClient_edit (nClientId)
{
	assert.isNumber(nClientId, "nClientId expected to be a Number.");
	m_oClientListMemberData.m_oSelectedClientId = nClientId;
	navigate ("clientInfo", "widgets/clientmanagement/editClientInfo.js");
}

function listClient_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oListData = $("#listClient_table_clients").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oClientData = new listClient_getFormData ();
	oClientData.m_nClientId = oData.m_nClientId;
	oClientData.m_oDemography.m_nDemographyId = oData.m_oDemography.m_nDemographyId;
	oClientData.m_arrContactData = listClient_buildContactsArray (oData.m_oContacts);
	var bConfirm = getUserConfirmation("Do you really want to delete?")
	if(bConfirm)
		ClientDataProcessor.deleteData(oClientData, listClient_deleted);
}

function listClient_buildContactsArray (arrContacts)
{
	assert.isArray(arrContacts, "arrContacts expected to be an Array.");
	var oContactDataArray = new Array ();
	for (var nIndex = 0; nIndex < arrContacts.length; nIndex++)
	{
		var oContactData = new ContactData ();
		oContactData.m_nContactId = arrContacts [nIndex].m_nContactId;
		oContactData.m_strContactName = arrContacts [nIndex].m_strContactName;
		oContactData.m_strPhoneNumber = arrContacts [nIndex].m_strPhoneNumber;
		oContactData.m_strEmail = arrContacts [nIndex].m_strEmail;
		oContactData.m_strDepartment = arrContacts [nIndex].m_strDepartment;
		oContactData.m_strDesignation = arrContacts [nIndex].m_strDesignation;
		oContactDataArray.push (oContactData);
	}
	return oContactDataArray;
}

function listClient_listed (oClientResponse)
{
	clearGridData ("#listClient_table_clients");
	$('#listClient_table_clients').datagrid('loadData', oClientResponse.m_arrClientData);
	$('#listClient_table_clients').datagrid('getPager').pagination ({total:oClientResponse.m_nRowCount, pageNumber:m_oClientListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function listClient_deleted (oClientResponse)
{
	if(oClientResponse.m_bSuccess)
		informUser("client deleted successfully.", "kSuccess");
	clearGridData ("#listClient_table_clients");
	listClient_clearDetail ();
}

function listClient_contactInfo_gotData (oContactResponse)
{
	var oContactData =  oContactResponse.m_arrContactData[0];
	$("#contactInfo_input_contactName").val(oContactData.m_strContactName);
	$("#contactInfo_input_phoneNumber").val(oContactData.m_strPhoneNumber);
	$("#contactInfo_input_emailAddress").val(oContactData.m_strEmail);
	$("#contactInfo_input_department").val(oContactData.m_strDepartment);
	$("#contactInfo_input_designation").val(oContactData.m_strDesignation);
}

function clientInfo_handleAfterSave ()
{
	listClient_clearDetail ();
}

function clientInfo_handleAfterUpdate ()
{ 
	listClient_clearDetail ();
}

function listClient_clearDetail ()
{
	document.getElementById("listClient_div_listDetail").innerHTML = "";
	listClient_list (m_oClientListMemberData.m_strSortColumn, m_oClientListMemberData.m_strSortOrder, 1, 10);
}

function listClient_getInfo (nClientId)
{
	assert.isNumber(nClientId, "nClientId expected to be a Number.");
	m_oClientListMemberData.m_oSelectedClientId = nClientId;
	navigate ('clientInfo','widgets/clientmanagement/clientTransactionForList.js');
}

function listClient_import ()
{
	navigate ('importClientData','widgets/clientmanagement/importClientDetails.js');
}

function listClient_export ()
{
	navigate ('exportClientList','widgets/clientmanagement/exportClientList.js');
}
