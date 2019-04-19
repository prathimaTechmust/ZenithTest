var itemCategoryList_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js'
];


includeDataObjects (itemCategoryList_includeDataObjects, "itemCategoryList_loaded()");

function itemCategoryList_memberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_nSelectedCategoryId = -1;
}

var m_oItemCategoryListMemberData = new itemCategoryList_memberData ();

function itemCategoryList_loaded ()
{
	loadPage ("inventorymanagement/item/itemCategoryList.html", "workarea", "itemCategoryList_init ()");
}

function itemCategoryList_init ()
{
	itemCategoryList_createDataGrid ();
}

function itemCategoryList_createDataGrid ()
{
	initHorizontalSplitter("#itemCategoryList_div_horizontalSplitter", "#itemCategoryList_table_itemCategoryListDG");
	$('#itemCategoryList_table_itemCategoryListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strCategoryName',title:'Category Name',sortable:true,width:200},
					{field:'m_strUserName',title:'Created By',sortable:true,width:150,
						formatter:function(value,row,index)
			        	{
			        		return row.m_oCreatedBy.m_strUserName;
			        	}
				  	},
					{field:'m_strDate',title:'Date',sortable:true,width:80},
					{field:'Actions',title:'Action',width:80,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return itemCategoryList_displayImages (row, index);
			        	}
					}
				]],
				onSelect: function (rowIndex, rowData)
				{
					itemCategoryList_selectedRowData (rowData);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oItemCategoryListMemberData.m_strSortColumn = strColumn;
					m_oItemCategoryListMemberData.m_strSortOrder = strOrder;
					itemCategoryList_list (strColumn, strOrder, m_oItemCategoryListMemberData.m_nPageNumber, m_oItemCategoryListMemberData.m_nPageSize);
				}
		}
	);
	itemCategoryList_initDGPagination ();
	itemCategoryList_list (m_oItemCategoryListMemberData.m_strSortColumn,m_oItemCategoryListMemberData.m_strSortOrder,1, 10);
}

function itemCategoryList_displayImages (row, index)
{
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="itemCategoryList_edit ('+row.m_nCategoryId+')"/> </td>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="itemCategoryList_delete ('+index+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function itemCategoryList_initDGPagination ()
{
	$('#itemCategoryList_table_itemCategoryListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oItemCategoryListMemberData.m_nPageNumber = nPageNumber;
				itemCategoryList_list (m_oItemCategoryListMemberData.m_strSortColumn, m_oItemCategoryListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("itemCategoryList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oItemCategoryListMemberData.m_nPageNumber = nPageNumber;
				m_oItemCategoryListMemberData.m_nPageSize = nPageSize;
				itemCategoryList_list (m_oItemCategoryListMemberData.m_strSortColumn, m_oItemCategoryListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("itemCategoryList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function itemCategoryList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oItemCategoryListMemberData.m_strSortColumn = strColumn;
	m_oItemCategoryListMemberData.m_strSortOrder = strOrder;
	m_oItemCategoryListMemberData.m_nPageNumber = nPageNumber;
	m_oItemCategoryListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "itemGroupList_progressbarLoaded ()");
}

function itemGroupList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oItemCategoryData = itemCategoryList_getFormData ();
	ItemCategoryDataProcessor.list(oItemCategoryData, m_oItemCategoryListMemberData.m_strSortColumn, m_oItemCategoryListMemberData.m_strSortOrder, m_oItemCategoryListMemberData.m_nPageNumber, m_oItemCategoryListMemberData.m_nPageSize, itemCategoryList_listed);
}

function itemCategoryList_listed (oResponse)
{
	clearGridData ("#itemCategoryList_table_itemCategoryListDG");
	document.getElementById("itemCategoryList_div_listDetail").innerHTML = "";
    $('#itemCategoryList_table_itemCategoryListDG').datagrid('loadData', oResponse.m_arrItemCategory);
	$('#itemCategoryList_table_itemCategoryListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oItemCategoryListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function itemCategoryList_filter()
{
	itemCategoryList_list (m_oItemCategoryListMemberData.m_strSortColumn, m_oItemCategoryListMemberData.m_strSortOrder,m_oItemCategoryListMemberData.m_nPageNumber ,m_oItemCategoryListMemberData.m_nPageSize);
}

function itemCategoryList_getFormData ()
{
	var oItemCategoryData = new ItemCategoryData ();
	oItemCategoryData.m_strCategoryName = $("#itemCategoryList_input_categoryName").val();
	return oItemCategoryData;
}

function itemCategoryList_showAddPopup ()
{
	navigate ("newItemCategory", "widgets/inventorymanagement/item/itemCategoryNew.js");
}

function itemCategoryList_edit (nCategoryId)
{
	m_oItemCategoryListMemberData.m_nSelectedCategoryId = nCategoryId;
	navigate ("itemCategoryEdit", "widgets/inventorymanagement/item/itemCategoryEdit.js");
}

function itemCategoryList_delete (nIndex)
{
	var oItemCategoryData = new ItemCategoryData ();
	var oListData = $("#itemCategoryList_table_itemCategoryListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oItemCategoryData.m_nCategoryId = oData.m_nCategoryId;
	oItemCategoryData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	var bConfirm = getUserConfirmation("Do you really want to delete this Category?")
	if(bConfirm)
		ItemCategoryDataProcessor.deleteData(oItemCategoryData, itemCategoryList_deleted);
}

function itemCategoryList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("Item Category deleted successfully", "kSuccess");
		document.getElementById("itemCategoryList_div_listDetail").innerHTML = "";
		itemCategoryList_list (m_oItemCategoryListMemberData.m_strSortColumn, m_oItemCategoryListMemberData.m_strSortOrder, 1, 10);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function itemCategoryList_selectedRowData (oRowData) 
{
	document.getElementById("itemCategoryList_div_listDetail").innerHTML = "";
	var oItemCategoryData = new ItemCategoryData ();
	oItemCategoryData.m_nCategoryId = oRowData.m_nCategoryId;
	ItemCategoryDataProcessor.getXML (oItemCategoryData,itemCategoryList_gotXML);
}
function itemCategoryList_gotXML(strXMLData)
{
    populateXMLData (strXMLData, "inventorymanagement/item/itemCategoryDetails.xslt", 'itemCategoryList_div_listDetail');
}