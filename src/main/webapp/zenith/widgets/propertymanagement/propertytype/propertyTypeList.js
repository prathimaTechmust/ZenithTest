var propertyTypeList_includeDataObjects = 
[
	'widgets/propertymanagement/propertytype/PropertyTypeData.js'
];

(function() {
	if (m_oTrademustMemberData.m_arrObjects.indexOf(propertyTypeList_includeDataObjects[0]) != -1) {
		propertyTypeList_loaded ();
	} else {
		includeDataObjects (propertyTypeList_includeDataObjects, "propertyTypeList_loaded()");
	}	
})();

// includeDataObjects (propertyTypeList_includeDataObjects, "propertyTypeList_loaded()");

function propertyTypeList_loaded ()
{
	loadPage ("propertymanagement/propertytype/propertyTypeList.html", "workarea", "propertyTypeList_init ()");
}

function propertyTypeList_memberData ()
{
	this.m_oPropertyTypeData = null;
	this.m_nPropertyTypeId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
	this.m_nPageSize = 10;
	this.m_strSortColumn = "m_strPropertyType";
    this.m_strSortOrder = "asc";
}

var m_oPropertyTypeListMemberData = new propertyTypeList_memberData ();


function propertyTypeList_init ()
{
	propertyTypeList_createDataGrid ();
}

function propertyTypeList_createDataGrid ()
{
	initHorizontalSplitter("#propertyTypeList_div_horizontalSplitter", "#propertyTypeList_table_propertyTypeDG");
	$('#propertyTypeList_table_propertyTypeDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nPropertyTypeId',title:'Id',sortable:true,width:40,align:'center'},
				{field:'m_strPropertyType',title:'Property Type Name',sortable:true,width:680},
				{field:'Actions',title:'Action',width:240,align:'center',
					formatter:function(value,row,index)
					{
						return propertyTypeList_displayImages (row, index);
					}
				},
			]],
			onSelect: function (rowIndex, rowData)
			{
				propertyTypeList_selectedRowData (rowData,rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				m_oPropertyTypeListMemberData.m_strSortColumn = strColumn;
				m_oPropertyTypeListMemberData.m_strSortOrder = strOrder;
				propertyTypeList_list (strColumn, strOrder, m_oPropertyTypeListMemberData.m_nPageNumber, m_oPropertyTypeListMemberData.m_nPageSize);
			}
		}
	);
	propertyTypeList_initDGPagination ();
	propertyTypeList_list (m_oPropertyTypeListMemberData.m_strSortColumn, m_oPropertyTypeListMemberData.m_strSortOrder, 1, 10);
}

function propertyTypeList_initDGPagination ()
{
	$('#propertyTypeList_table_propertyTypeDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oPropertyTypeListMemberData.m_nPageNumber = nPageNumber;
				propertyTypeList_list (m_oPropertyTypeListMemberData.m_strSortColumn,m_oPropertyTypeListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("propertyTypeList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function(nPageNumber, nPageSize)
			{
				m_oPropertyTypeListMemberData.m_nPageNumber = nPageNumber;
				m_oPropertyTypeListMemberData.m_nPageSize = nPageSize;
				propertyTypeList_list (m_oPropertyTypeListMemberData.m_strSortColumn,m_oPropertyTypeListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("propertyTypeList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function propertyTypeList_selectedRowData (oRowData, nIndex)
{
	m_oPropertyTypeListMemberData.m_oPropertyTypeData = oRowData;
	m_oPropertyTypeListMemberData.m_nIndex = nIndex;	
	document.getElementById("propertyTypeList_div_listDetail").innerHTML = "";
	var oPropertyTypeData = new PropertyTypeData ();
	oPropertyTypeData.m_nPropertyTypeId = oRowData.m_nPropertyTypeId;
	PropertyTypeDataProcessor.getXML (oPropertyTypeData, propertyTypeList_gotXML);
}

function propertyTypeList_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "propertymanagement/propertytype/propertyTypeDetails.xslt", 'propertyTypeList_div_listDetail');
}

function propertyTypeList_filter ()
{
	propertyTypeList_list (m_oPropertyTypeListMemberData.m_strSortColumn, m_oPropertyTypeListMemberData.m_strSortOrder, 1, 10);
}

function propertyTypeList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oPropertyTypeListMemberData.m_nPageNumber = nPageNumber;
	m_oPropertyTypeListMemberData.m_nPageSize =nPageSize;
	m_oPropertyTypeListMemberData.m_strSortColumn = strColumn;
	m_oPropertyTypeListMemberData.m_strSortOrder = strOrder;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "propertyTypeList_progressbarLoaded ()");
}

function propertyTypeList_progressbarLoaded ()
{
	var oPropertyTypeData = new PropertyTypeData ();
	oPropertyTypeData.m_strPropertyType = $("#filterPropertyType_input_propertyTypeName").val();
	PropertyTypeDataProcessor.list(oPropertyTypeData, m_oPropertyTypeListMemberData.m_strSortColumn , m_oPropertyTypeListMemberData.m_strSortOrder, m_oPropertyTypeListMemberData.m_nPageNumber, m_oPropertyTypeListMemberData.m_nPageSize, propertyTypeList_listed );
}

function propertyTypeList_listed (oResponse)
{
	$('#propertyTypeList_table_propertyTypeDG').datagrid ('loadData',oResponse.m_arrPropertyTypeData);
	$('#propertyTypeList_table_propertyTypeDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oPropertyTypeListMemberData.m_nPageNumber});
	HideDialog("dialog");
}


function propertyTypeList_displayImages (row, index) 
{
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" onClick="propertyTypeList_edit('+ row.m_nPropertyTypeId  +')" width="20" /> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="propertyTypeList_delete('+index+')" width="20"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function propertyTypeList_edit (nPropertyTypeId)
{
	m_oPropertyTypeListMemberData.m_nPropertyTypeId = nPropertyTypeId;
	navigate ("propertytype","widgets/propertymanagement/propertytype/editPropertType.js");
}

function propertyTypeList_delete (nIndex)
{
	var oPropertyTypeData = new PropertyTypeData ();
	var oListData = $("#propertyTypeList_table_propertyTypeDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oPropertyTypeData.m_nPropertyTypeId = oData.m_nPropertyTypeId;
	var bConfirm = getUserConfirmation("Do you really want to delete.")
	if (bConfirm == true)
		PropertyTypeDataProcessor.deleteData(oPropertyTypeData,propertyTypeList_deleted);
}

function propertyTypeList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Property type deleted successfully.", "kSuccess");
		document.getElementById("propertyTypeList_div_listDetail").innerHTML = "";
		propertyTypeList_list (m_oPropertyTypeListMemberData.m_strSortColumn, m_oPropertyTypeListMemberData.m_strSortOrder, 1, 10);
	}
}

function propertyTypeList_addNewProperty ()
{
	navigate ("BusinessType", "widgets/propertymanagement/propertytype/newPropertyType.js");
}

