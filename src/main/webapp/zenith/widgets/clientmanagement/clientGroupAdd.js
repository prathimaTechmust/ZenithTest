var clientGroupAdd_includeDataObjects = 
[
	'widgets/clientmanagement/ClientGroupData.js',
	'widgets/clientmanagement/ClientData.js'
];

includeDataObjects (clientGroupAdd_includeDataObjects, "clientGroupAdd_loaded ()");

function clientGroupAdd_loaded ()
{
	loadPage ("clientmanagement/clientGroupAdd.html", "secondDialog", "clientGroupAdd_init ()");
}

var m_oClientGroupAddMemberData = new clientGroupAdd_memberData ();

function clientGroupAdd_memberData ()
{
	this.m_nSelectedItemId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "desc";
    this.m_arrSelectedData = new Array ();
    this.m_arrClients = new Array ();
}

function clientGroupAdd_init ()
{
	var arrClientData = $('#clientGroup_table_listofclientDG').datagrid('getRows');
	m_oClientGroupAddMemberData.m_arrSelectedData = arrClientData;
	createPopup ("secondDialog", "#clientGroupAdd_button_add", "#clientGroupAdd_button_cancel", true);
	clientGroupAdd_initDataGrid ();
}

function clientGroupAdd_initDataGrid ()
{
	$('#clientGroupAdd_table_clientGroupAddDG').datagrid ({
	    columns:[[  
	        {field:'ckBox',checkbox:true,width:100},
	        {field:'m_strCompanyName',title:'List Of Clients',sortable:true,width:300}  
	    ]],
	    onCheck: function (rowIndex, rowData)
		{
			clientGroupAdd_holdCheckedData (rowData, true);
		},
		onUncheck: function (rowIndex, rowData)
		{
			clientGroupAdd_holdCheckedData (rowData, false); 
		},
		onCheckAll: function (rows)
		{
			clientGroupAdd_holdAllCheckedData (rows);
		},
		onUncheckAll: function (rows)
		{
			clientGroupAdd_holdAllUncheckedData (rows);
		}
	});
	clientGroupAdd_initDGPagination ();
	clientGroupAdd_buildClientList (m_oClientGroupAddMemberData.m_strSortColumn,m_oClientGroupAddMemberData.m_strSortOrder,1, 10);
}

function clientGroupAdd_initDGPagination ()
{
	$('#clientGroupAdd_table_clientGroupAddDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oClientGroupAddMemberData.m_nPageNumber = $('#clientGroupAdd_table_clientGroupAddDG').datagrid('getPager').pagination('options').pageNumber;
				clientGroupAdd_buildClientList (m_oClientGroupAddMemberData.m_strSortColumn, m_oClientGroupAddMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientGroupAddMemberData.m_nPageNumber = $('#clientGroupAdd_table_clientGroupAddDG').datagrid('getPager').pagination('options').pageNumber;
				m_oClientGroupAddMemberData.m_nPageSize = $('#clientGroupAdd_table_clientGroupAddDG').datagrid('getPager').pagination('options').pageSize;
				clientGroupAdd_buildClientList (m_oClientGroupAddMemberData.m_strSortColumn, m_oClientGroupAddMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function clientGroupAdd_buildClientList (strColumn, strOrder, nPageNumber, nPageSize)
{

	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = $("#clientGroupAdd_input_clientName").val();
	ClientDataProcessor.list(oClientData, strColumn, strOrder, nPageNumber, nPageSize, clientGroupAdd_gotClientList);
}

function clientGroupAdd_gotClientList (oResponse)
{
	//server response
	
	clearGridData ("#clientGroupAdd_table_clientGroupAddDG");
	$('#clientGroupAdd_table_clientGroupAddDG').datagrid('loadData',oResponse.m_arrClientData);
	$('#clientGroupAdd_table_clientGroupAddDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientGroupAddMemberData.m_nPageNumber});
	clientGroupAdd_checkDGRow ();
}

function clientGroupAdd_cancel ()
{
	HideDialog("secondDialog");
}

function clientGroupAdd_addClient ()
{
	$('#clientGroup_table_listofclientDG').datagrid('loadData',m_oClientGroupAddMemberData.m_arrSelectedData);
	HideDialog ("secondDialog");
}

function isRowAdded (arrCheckedClients, oClient)
{
	var bIsadded = false;
	for (var nIndex = 0; !bIsadded && nIndex < arrCheckedClients.length; nIndex++)
		bIsadded = (arrCheckedClients [nIndex].m_strCompanyName == oClient.m_strCompanyName);
	return bIsadded;
}

function clientGroupAdd_filter ()
{
	clientGroupAdd_buildClientList (m_oClientGroupAddMemberData.m_strSortColumn, m_oClientGroupAddMemberData.m_strSortOrder,1, 10);
}

function clientGroupAdd_holdCheckedData (oRowData, bIsForAdd)
{
	if(bIsForAdd)
	{
		if(!clientGroupAdd_isRowAdded (oRowData))
			m_oClientGroupAddMemberData.m_arrSelectedData.push(oRowData);
	}
	else
		clientGroupAdd_remove (oRowData);
}

function clientGroupAdd_remove (oRowData)
{
	for (var nIndex = 0; nIndex < m_oClientGroupAddMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oClientGroupAddMemberData.m_arrSelectedData[nIndex].m_nClientId == oRowData.m_nClientId)
		{
			m_oClientGroupAddMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function clientGroupAdd_isRowAdded (oRowData)
{
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oClientGroupAddMemberData.m_arrSelectedData.length; nIndex++)
		bIsRowAdded = (m_oClientGroupAddMemberData.m_arrSelectedData [nIndex].m_nClientId == oRowData.m_nClientId);
	return bIsRowAdded;
}

function clientGroupAdd_checkDGRow ()
{
	var arrClientData = $('#clientGroupAdd_table_clientGroupAddDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrClientData.length; nIndex++)
	{
		if(clientGroupAdd_isRowSelectable(arrClientData[nIndex].m_nClientId))
			$("#clientGroupAdd_table_clientGroupAddDG").datagrid('checkRow', nIndex);
	}
}

function clientGroupAdd_isRowSelectable (nClientId)
{
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oClientGroupAddMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oClientGroupAddMemberData.m_arrSelectedData[nIndex].m_nClientId == nClientId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function clientGroupAdd_holdAllCheckedData (arrRows)
{
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		clientGroupAdd_holdCheckedData(arrRows[nIndex], true);
}

function clientGroupAdd_holdAllUncheckedData (arrRows)
{
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		clientGroupAdd_holdCheckedData(arrRows[nIndex], false);
}