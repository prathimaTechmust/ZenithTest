var vendotGroupAdd_includeDataObjects = 
[
	'widgets/vendormanagement/VendorGroupData.js',
 	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

 includeDataObjects (vendotGroupAdd_includeDataObjects, "vendotGroupAdd_loaded ()");

function vendotGroupAdd_memberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "asc";
    this.m_arrSelectedData = new Array ();
    this.m_arrVendors = new Array ();
    this.m_nIndex = -1;
}

var m_ovendotGroupAddMemberData = new vendotGroupAdd_memberData ();

function vendotGroupAdd_loaded ()
{
	loadPage ("vendormanagement/vendorGroupAdd.html", "secondDialog", "vendotGroupAdd_init ()");
}

function vendotGroupAdd_init ()
{
	var arrVendorData = $('#vendorGroup_table_listofvendorGroupDG').datagrid('getRows');
	m_ovendotGroupAddMemberData.m_arrSelectedData = arrVendorData;
	createPopup("secondDialog", "#vendorGroupAdd_button_add", "#vendorGroupAdd_button_cancel", true);
	vendotGroupAdd_initializeDataGrid ();
}

function vendotGroupAdd_initializeDataGrid ()
{
	$('#vendorGroupAdd_table_vendorGroupAddDG').datagrid ({
	    columns:
	    [[ 
	        {field:'ckBox',checkbox:true},
	        {field:'m_strCompanyName',title:'Vendor Name', width:200},
	    ]],
	    onCheck: function (rowIndex, rowData)
		{
			vendorGroupAdd_holdCheckedData (rowData, true);
		},
		onUncheck: function (rowIndex, rowData)
		{
			vendorGroupAdd_holdCheckedData (rowData, false); 
		},
		onCheckAll: function (arrRows)
		{
			vendorGroupAdd_holdAllCheckedData (arrRows);
		},
		onUncheckAll: function (arrRows)
		{
			vendorGroupAdd_holdAllUnCheckedData (arrRows); 
		},
		onSortColumn: function (strColumn, strOrder)
		{
			m_ovendotGroupAddMemberData.m_strSortColumn = strColumn;
			m_ovendotGroupAddMemberData.m_strSortOrder = strOrder;
			vendorGroupList_getvendorList (strColumn, strOrder, m_ovendotGroupAddMemberData.m_nPageNumber, m_oVendorListMemberData.m_nPageSize);
		}
	});
	vendorGroupAdd_initDGPagination ();
	vendorGroupAdd_getvendorList (m_ovendotGroupAddMemberData.m_strSortColumn, m_ovendotGroupAddMemberData.m_strSortOrder, 1, 10);
}

function vendorGroupAdd_initDGPagination()
{
	$('#vendorGroupAdd_table_vendorGroupAddDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_ovendotGroupAddMemberData.m_nPageNumber = $('#vendorGroupAdd_table_vendorGroupAddDG').datagrid('getPager').pagination('options').pageNumber;
				vendorGroupAdd_getvendorList (m_ovendotGroupAddMemberData.m_strSortColumn, m_ovendotGroupAddMemberData.m_strSortOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_ovendotGroupAddMemberData.m_nPageNumber = $('#vendorGroupAdd_table_vendorGroupAddDG').datagrid('getPager').pagination('options').pageNumber;
				m_ovendotGroupAddMemberData.m_nPageSize = $('#vendorGroupAdd_table_vendorGroupAddDG').datagrid('getPager').pagination('options').pageSize;
				vendorGroupAdd_getvendorList (m_ovendotGroupAddMemberData.m_strSortColumn, m_ovendotGroupAddMemberData.m_strSortOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function vendorGroupAdd_getvendorList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = $ ("#vendorGroupAdd_input_vendorName").val();
	VendorDataProcessor.listVendor(oVendorData, strColumn, strOrder, nPageNumber, nPageSize, vendorGroupAdd_listed);
}

function vendorGroupAdd_listed (oResponse)
{
	clearGridData ("#vendorGroupAdd_table_vendorGroupAddDG");
	for (var nIndex = 0; nIndex < oResponse.m_arrVendorData.length; nIndex++)
		$('#vendorGroupAdd_table_vendorGroupAddDG').datagrid('appendRow',oResponse.m_arrVendorData[nIndex]);
	$('#vendorGroupAdd_table_vendorGroupAddDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_ovendotGroupAddMemberData.m_nPageNumber});
	vendorGroupAdd_checkDGRow ()
}

function vendorGroupAdd_add ()
{
	$('#vendorGroup_table_listofvendorGroupDG').datagrid('loadData',m_ovendotGroupAddMemberData.m_arrSelectedData);
	HideDialog ("secondDialog");
} 

function vendorGroupAdd_cancel ()
{
	$('#vendorGroupAdd_table_vendorGroupAddDG').datagrid('clearChecked');
	HideDialog("secondDialog");
}

function vendorGroupAdd_filter ()
{
	vendorGroupAdd_getvendorList (m_ovendotGroupAddMemberData.m_strSortColumn, m_ovendotGroupAddMemberData.m_strSortOrder, 1, 10);
}


function vendorGroupAdd_holdCheckedData (oRowData, bIsForAdd)
{
	if(bIsForAdd)
	{
		if(!vendorGroupAdd_isRowAdded (oRowData))
			m_ovendotGroupAddMemberData.m_arrSelectedData.push(oRowData);
	}
	else
		vendortGroupAdd_remove (oRowData);
}

function vendortGroupAdd_remove (oRowData)
{
	for (var nIndex = 0; nIndex < m_ovendotGroupAddMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_ovendotGroupAddMemberData.m_arrSelectedData[nIndex].m_nClientId == oRowData.m_nClientId)
		{
			m_ovendotGroupAddMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function vendorGroupAdd_holdAllCheckedData (arrRows)
{
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		vendorGroupAdd_holdCheckedData(arrRows[nIndex], true);
}

function vendorGroupAdd_holdAllUnCheckedData (arrRows)
{
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		vendorGroupAdd_holdCheckedData(arrRows[nIndex], false);
}

function vendorGroupAdd_isRowAdded (oRowData)
{
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_ovendotGroupAddMemberData.m_arrSelectedData.length; nIndex++)
		bIsRowAdded = (m_ovendotGroupAddMemberData.m_arrSelectedData [nIndex].m_nClientId == oRowData.m_nClientId);
	return bIsRowAdded;
}

function vendorGroupAdd_checkDGRow ()
{
	var arrVendorData = $('#vendorGroupAdd_table_vendorGroupAddDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrVendorData.length; nIndex++)
	{
		if(vendorGroupAdd_isRowSelectable(arrVendorData[nIndex].m_nClientId))
			$("#vendorGroupAdd_table_vendorGroupAddDG").datagrid('checkRow', nIndex);
	}
}

function vendorGroupAdd_isRowSelectable (nClientId)
{
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_ovendotGroupAddMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_ovendotGroupAddMemberData.m_arrSelectedData[nIndex].m_nClientId == nClientId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}