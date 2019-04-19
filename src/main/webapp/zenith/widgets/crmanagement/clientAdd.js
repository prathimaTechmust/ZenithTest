var clientAdd_includeDataObjects = 
[
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
];

includeDataObjects (clientAdd_includeDataObjects, "clientAdd_loaded ()");

function clientAdd_loaded ()
{
	loadPage ("crmanagement/clientAdd.html", "thirdDialog", "clientAdd_init ()");
}

function clientAdd_memberData ()
{
	this.m_nSelectedItemId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "asc";
    this.m_arrSelectedData = new Array ();
    this.m_arrBusinessTypeData = new Array ();
}

var m_oClientAddMemberData = new clientAdd_memberData ();

function clientAdd_init ()
{
	var arrContactData = $('#emailMessage_table_emailMessageContactDG').datagrid('getRows');
	m_oClientAddMemberData.m_arrSelectedData = arrContactData;
	createPopup ("thirdDialog", "#clientAdd_button_add", "#clientAdd_button_cancel", true);
	clientAdd_getBusinessTypeData ();
	clientAdd_initBusinessTypeCombobox ();
	clientAdd_initDatagrid ();
}

function clientAdd_getBusinessTypeData ()
{
	var oBusinessTypeData = new BusinessTypeData ();
	BusinessTypeDataProcessor.list (oBusinessTypeData, "", "", clientAdd_gotBusinessTypeData);
} 

function clientAdd_gotBusinessTypeData (oResponse)
{
	m_oClientAddMemberData.m_arrBusinessTypeData = oResponse.m_arrBusinessType;
}

function clientAdd_initBusinessTypeCombobox ()
{
	$(document).ready(function () 
		{
	    	$("#clientAdd_input_demography").jqxComboBox({source : m_oClientAddMemberData.m_arrBusinessTypeData, displayMember: "m_strBusinessName", valueMember: "m_nBusinessTypeId", multiSelect: true, width:'100%', height:'22px'});
		});
}

function clientAdd_initDatagrid ()
{
	$('#clientAdd_table_clientGroupAddDG').datagrid ({
	    columns:[[  
	        {field:'ckBox',checkbox:true},
	        {field:'m_strCompanyName',title:'Client Name',sortable:true,width:150,
	        	formatter:function(value,row,index)
	        	{
	        		try
	        		{
	        			var strCompanyName = row.m_oClientData.m_strCompanyName;
	        		} 
	        		catch(oException)
	        		{
	        			strCompanyName = "";
	        		}
	        		return strCompanyName;
	        	}
	        },
	        {field:'m_strContactName',title:'Contact Name',sortable:true,width:150},
	        {field:'m_strEmail',title:'Email Address',sortable:true,width:150}
	    ]],
	    onSortColumn: function (strColumn, strOrder)
		{
			m_oClientAddMemberData.m_strSortColumn = strColumn;
			m_oClientAddMemberData.m_strSortOrder = strOrder;
			clientAdd_buildContactList (m_oClientAddMemberData.m_strSortColumn,m_oClientAddMemberData.m_strSortOrder,1, 10);
		},
	    onCheck: function (rowIndex, rowData)
		{
			clientAdd_holdCheckedData(rowData, true);
		},
		onUncheck: function (rowIndex, rowData)
		{
			clientAdd_holdCheckedData (rowData, false);
		},
		onCheckAll: function (rows)
		{
		},
		onUncheckAll: function (rows)
		{
		}
	});
	clientAdd_initDGPagination ();
	clientAdd_buildContactList (m_oClientAddMemberData.m_strSortColumn,m_oClientAddMemberData.m_strSortOrder,1, 10);
}

function clientAdd_initDGPagination ()
{
	$('#clientAdd_table_clientGroupAddDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
			m_oClientAddMemberData.m_nPageNumber = $('#clientAdd_table_clientGroupAddDG').datagrid('getPager').pagination('options').pageNumber;
			clientAdd_buildContactList (m_oClientAddMemberData.m_strSortColumn, m_oClientAddMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientAddMemberData.m_nPageNumber = $('#clientAdd_table_clientGroupAddDG').datagrid('getPager').pagination('options').pageNumber;
				m_oClientAddMemberData.m_nPageSize = $('#clientAdd_table_clientGroupAddDG').datagrid('getPager').pagination('options').pageSize;
				clientAdd_buildContactList (m_oClientAddMemberData.m_strSortColumn, m_oClientAddMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function clientAdd_getBusinessTypeArray (arrSelectedData)
{
	var arrBusinessTypeData = new Array ();
	for (var nIndex = 0; nIndex < arrSelectedData.length; nIndex++)
	{
		var oBusinessTypeData = new BusinessTypeData ();
		oBusinessTypeData.m_nBusinessTypeId = arrSelectedData [nIndex].value;
		if (oBusinessTypeData.m_nBusinessTypeId > 0)
			arrBusinessTypeData.push (oBusinessTypeData);
	}
	return arrBusinessTypeData;
}

function clientAdd_buildContactList (strColumn, strOrder, nPageNumber, nPageSize)
{
	
	var oContactData = new ContactData ();
	oContactData.m_oClientData = new ClientData ();
	oContactData.m_oClientData.m_strCompanyName = $("#clientAdd_input_clientName").val();
	oContactData.m_strContactName = $("#clientAdd_input_ContactName").val();
	oContactData.m_arrBusinessTypeData = clientAdd_getBusinessTypeArray($("#clientAdd_input_demography").jqxComboBox('getSelectedItems'));
	ContactDataProcessor.list(oContactData, strColumn, strOrder, clientAdd_gotContactList);
}

function clientAdd_gotContactList (oResponse)
{
	clearGridData ("#clientAdd_table_clientGroupAddDG");
	$('#clientAdd_table_clientGroupAddDG').datagrid('loadData',oResponse.m_arrContactData);
	$('#clientAdd_table_clientGroupAddDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientAddMemberData.m_nPageNumber});
	clientAdd_checkDGRow ();
}

function clientAdd_cancel ()
{
	HideDialog("thirdDialog");
}

function clientAdd_addContacts ()
{
	var arrContacts = $('#clientAdd_table_clientGroupAddDG').datagrid('getChecked');
	$('#emailMessage_table_emailMessageContactDG').datagrid('loadData',arrContacts);
	HideDialog ("thirdDialog");
	emailMessage_displayButton (arrContacts);
}

function clientAdd_filter ()
{
	clientAdd_buildContactList (m_oClientAddMemberData.m_strSortColumn, m_oClientAddMemberData.m_strSortOrder,1, 10);
}

function clientAdd_holdCheckedData (oRowData, bIsForAdd)
{
	if(bIsForAdd)
	{
		if(!clientAdd_isRowAdded (oRowData))
			m_oClientAddMemberData.m_arrSelectedData.push(oRowData);
	}
	else
		clientAdd_remove (oRowData);
}

function clientAdd_remove (oRowData)
{
	for (var nIndex = 0; nIndex < m_oClientAddMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oClientAddMemberData.m_arrSelectedData[nIndex].m_nContactId == oRowData.m_nContactId)
		{
			m_oClientAddMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function clientAdd_isRowAdded (oRowData)
{
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oClientAddMemberData.m_arrSelectedData.length; nIndex++)
		bIsRowAdded = (m_oClientAddMemberData.m_arrSelectedData [nIndex].m_nContactId == oRowData.m_nContactId);
	return bIsRowAdded;
}

function clientAdd_checkDGRow ()
{
	var arrClientData = $('#clientAdd_table_clientGroupAddDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrClientData.length; nIndex++)
	{
		if(clientAdd_isRowSelectable(arrClientData[nIndex].m_nContactId))
			$("#clientAdd_table_clientGroupAddDG").datagrid('checkRow', nIndex);
	}
}

function clientAdd_isRowSelectable (nContactId)
{
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oClientAddMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oClientAddMemberData.m_arrSelectedData[nIndex].m_nContactId == nContactId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}