var listBusinessType_includeDataObjects = 
[
	'widgets/clientmanagement/BusinessTypeData.js'
];


includeDataObjects (listBusinessType_includeDataObjects, "listBusinessType_loaded()");

function listBusinessType_loaded ()
{
	loadPage ("clientmanagement/listBusinessType.html", "workarea", "listBusinessType_init ()");
}

function listBusinessType_memberData ()
{
	this.m_nSelectedBusinessTypeId = -1;
	this.m_oBusinessTypeData = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
	this.m_nPageSize = 10;
	this.m_strSortColumn = "m_strBusinessName";
    this.m_strSortOrder = "asc";
}

var m_olistBusinessType_memberData = new listBusinessType_memberData ();

function listBusinessType_init ()
{
	listBusinessType_createDataGrid ();
}

function listBusinessType_createDataGrid ()
{
	initHorizontalSplitter("#listBusinessType_div_horizontalSplitter", "#listBusinessType_table_business");
	$('#listBusinessType_table_business').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nBusinessTypeId',title:'Id',sortable:true,width:40,align:'center'},
				{field:'m_strBusinessName',title:'Business Type Name',sortable:true,width:680},
				{field:'Actions',title:'Action',width:240,align:'center',
					formatter:function(value,row,index)
					{
						return listBusinessType_displayImages (row, index);
					}
				},
			]],
		}
	);
	
	$('#listBusinessType_table_business').datagrid
	(
		{
			onSortColumn: function (strColumn, strOrder)
			{
				m_olistBusinessType_memberData.m_strSortColumn = strColumn;
				m_olistBusinessType_memberData.m_strSortOrder = strOrder;
				listBusinessType_list (strColumn, strOrder, m_olistBusinessType_memberData.m_nPageNumber, m_olistBusinessType_memberData.m_nPageSize);
			}
		}
	)
	
	$('#listBusinessType_table_business').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				listBusinessType_selectedRowData (rowData,rowIndex);
			}
		}
	)
	$('#listBusinessType_table_business').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(pageNumber, pageSize)
			{
				listBusinessType_list ('','');
				document.getElementById("listBusinessType_div_listDetail").innerHTML = "";
			}
		}
	)
	
	listBusinessType_initDGPagination ();
	listBusinessType_list (m_olistBusinessType_memberData.m_strSortColumn, m_olistBusinessType_memberData.m_strSortOrder, 1, 10);
}

function listBusinessType_initDGPagination ()
{
	$('#listBusinessType_table_business').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_olistBusinessType_memberData.m_nPageNumber = nPageNumber;
				listBusinessType_list (m_olistBusinessType_memberData.m_strSortColumn,m_olistBusinessType_memberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listBusinessType_div_listDetail").innerHTML = "";
			},
			onSelectPage:function(nPageNumber, nPageSize)
			{
				m_olistBusinessType_memberData.m_nPageNumber = nPageNumber;
				m_olistBusinessType_memberData.m_nPageSize = nPageSize;
				listBusinessType_list (m_olistBusinessType_memberData.m_strSortColumn,m_olistBusinessType_memberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listBusinessType_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listBusinessType_filter ()
{
	listBusinessType_list (m_olistBusinessType_memberData.m_strSortColumn, m_olistBusinessType_memberData.m_strSortOrder, 1, 10);
}

function listBusinessType_selectedRowData (oRowData, nIndex)
{
    assert.isObject(oRowData, "oRowData expected to be an Object.");
    assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	m_olistBusinessType_memberData.m_oBusinessTypeData = oRowData;
	m_olistBusinessType_memberData.m_nIndex = nIndex;	
	document.getElementById("listBusinessType_div_listDetail").innerHTML = "";
	var oBusinessTypeData = new BusinessTypeData ();
	oBusinessTypeData.m_nBusinessTypeId = oRowData.m_nBusinessTypeId;
	BusinessTypeDataProcessor.getXML (oBusinessTypeData,gotXML);
	
//	{
//		async:false, 
//		callback: function (strXMLData)
//		{
//			populateXMLData (strXMLData, "clientmanagement/businessTypeDetails.xslt", 'listBusinessType_div_listDetail');
//		}
//	});
}
function gotXML(strXMLData)
{
	populateXMLData (strXMLData, "clientmanagement/businessTypeDetails.xslt", 'listBusinessType_div_listDetail');
}
function listBusinessType_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_olistBusinessType_memberData.m_strSortColumn = strColumn;
	m_olistBusinessType_memberData.m_strSortOrder = strOrder;
	m_olistBusinessType_memberData.m_nPageNumber = nPageNumber;
	m_olistBusinessType_memberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "listBusinessType_progressbarLoaded ()");
} 

function listBusinessType_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oBusinessTypeData = new BusinessTypeData ();
	oBusinessTypeData.m_strBusinessName = $("#filterBusinessType_input_businessTypeName").val();
	BusinessTypeDataProcessor.list(oBusinessTypeData, m_olistBusinessType_memberData.m_strSortColumn , m_olistBusinessType_memberData.m_strSortOrder, m_olistBusinessType_memberData.m_nPageNumber , m_olistBusinessType_memberData.m_nPageSize, listBusinessType_listed );
}

function listBusinessType_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" onClick="listBusinessType_edit('+ row.m_nBusinessTypeId +')"/> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="listBusinessType_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listBusinessType_edit (nBusinessTypeId)
{	
	assert.isNumber(nBusinessTypeId, "nBusinessTypeId expected to be a Number.");
	assert(nBusinessTypeId !== 0, "nBusinessTypeId cannot be equal to zero.");
	m_olistBusinessType_memberData.m_nSelectedBusinessTypeId = nBusinessTypeId;
	navigate ("businessInformation","widgets/clientmanagement/editBusinessType.js");
}

function listBusinessType_showAddBusinessPopup ()
{
	navigate ("BusinessType", "widgets/clientmanagement/newBusinessType.js");
}

function listBusinessType_listDetail_delete ()
{
	listBusinessType_delete (m_olistBusinessType_memberData.m_nIndex);
}

function listBusinessType_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oBusinessTypeData = new BusinessTypeData();
	var oListData = $("#listBusinessType_table_business").datagrid('getData');
	var oData = oListData.rows[nIndex];	
	oBusinessTypeData.m_nBusinessTypeId = oData.m_nBusinessTypeId;
	var bConfirm = getUserConfirmation("Do you really want to delete " + oData.m_strBusinessName + "?")
	if (bConfirm)
		BusinessTypeDataProcessor.deleteData(oBusinessTypeData,listBusinessType_deleted); 
}

function listBusinessType_listed (oBusinessTypeResponse)
{
	clearGridData ("#listBusinessType_table_business");
	for (var nIndex = 0; nIndex < oBusinessTypeResponse.m_arrBusinessType.length; nIndex++)
		$('#listBusinessType_table_business').datagrid('appendRow',oBusinessTypeResponse.m_arrBusinessType[nIndex]);
	$('#listBusinessType_table_business').datagrid('getPager').pagination ({total:oBusinessTypeResponse.m_nRowCount, pageNumber:m_olistBusinessType_memberData.m_nPageNumber});
	HideDialog("dialog");
}

function listBusinessType_deleted (oBusinessTypeResponse)
{
	if(oBusinessTypeResponse.m_bSuccess)
	{
		informUser("Business type deleted successfully.", "kSuccess");
		clearGridData ("#listBusinessType_table_business");
		document.getElementById("listBusinessType_div_listDetail").innerHTML = "";
		listBusinessType_list (m_olistBusinessType_memberData.m_strSortColumn, m_olistBusinessType_memberData.m_strSortOrder, 1, 10);
	}
}

