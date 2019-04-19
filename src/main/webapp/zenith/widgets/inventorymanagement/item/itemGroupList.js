var itemGroupList_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/ItemGroupData.js'
];


includeDataObjects (itemGroupList_includeDataObjects, "itemGroupList_loaded()");

function itemGroupList_memberData ()
{
	this.m_nSelectedItemGroupId = -1;
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "itemGroupList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
}

var m_oItemGroupListMemberData = new itemGroupList_memberData ();

function itemGroupList_init ()
{
	itemGroupList_createDataGrid ();
}

function itemGroupList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function itemGroupList_initAdmin ()
{
	m_oItemGroupListMemberData.m_strActionItemsFunction = "itemGroupList_addActions (row, index)";
	document.getElementById ("itemGroupList_button_add").style.visibility="visible";
	itemGroupList_init ();
}

function itemGroupList_initUser ()
{
	itemGroupList_init ();
}

function itemGroupList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="itemGroupList_edit ('+row.m_nItemGroupId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="itemGroupList_delete ('+index+')"/> </td>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="itemGroupList_getInfo ('+row.m_nItemGroupId+')"/> </td>'+
						'<td> <img title="Publicise" src="images/globe.png" width="20" align="center" id="publiciseImageId" onClick="itemGroupList_publicise ('+row.m_nItemGroupId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function itemGroupList_getInfo (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	m_oItemGroupListMemberData.m_nSelectedItemGroupId = nItemId;
	navigate ("ItemGroupTransaction", "widgets/inventorymanagement/item/itemGroupTransactionForList.js");
}

function itemGroupList_createDataGrid ()
{
	initHorizontalSplitter("#itemGroupList_div_horizontalSplitter", "#itemGroupList_table_itemGroupListDG");
	$('#itemGroupList_table_itemGroupListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strGroupName',title:'Group Name',sortable:true,width:200},
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
			        		return itemGroupList_displayImages (row, index);
			        	}
					},
				]],
				onSelect: function (rowIndex, rowData)
				{
					itemGroupList_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oItemGroupListMemberData.m_strSortColumn = strColumn;
					m_oItemGroupListMemberData.m_strSortOrder = strOrder;
					itemGroupList_list (strColumn, strOrder, m_oItemGroupListMemberData.m_nPageNumber, m_oItemGroupListMemberData.m_nPageSize);
				}
		}
	);
	itemGroupList_initDGPagination ();
	itemGroupList_list (m_oItemGroupListMemberData.m_strSortColumn,m_oItemGroupListMemberData.m_strSortOrder,1, 10);
}

function itemGroupList_initDGPagination ()
{
	$('#itemGroupList_table_itemGroupListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oItemGroupListMemberData.m_nPageNumber = nPageNumber;
				itemGroupList_list (m_oItemGroupListMemberData.m_strSortColumn, m_oItemGroupListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("itemGroupList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oItemGroupListMemberData.m_nPageNumber = nPageNumber;
				m_oItemGroupListMemberData.m_nPageSize = nPageSize;
				itemGroupList_list (m_oItemGroupListMemberData.m_strSortColumn, m_oItemGroupListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("itemGroupList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function itemGroupList_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oItemGroupListMemberData.m_nIndex = nIndex;
	document.getElementById("itemGroupList_div_listDetail").innerHTML = "";
	var oItemGroupData = new ItemGroupData ();
	oItemGroupData.m_nItemGroupId = oRowData.m_nItemGroupId;
	oItemGroupData.m_oUserCredentialsData = new UserInformationData ();
	oItemGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemGroupDataProcessor.getXML (oItemGroupData, itemGroupList_gotXML);
	ItemGroupDataProcessor.get (oItemGroupData, itemGroupList_gotGroupItems);
}

function itemGroupList_gotXML(strXMLData)
{
	populateXMLData (strXMLData, "inventorymanagement/item/itemGroupDetails.xslt", 'itemGroupList_div_listDetail');
	itemGroupList_initializeDetailsDG ();
}
function itemGroupList_initializeDetailsDG ()
{
	$('#ItemGroupListDetails_table_itemDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:120},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:300},
				{field:'m_strBrand',title:'Brand',sortable:true,width:150},
				{field:'m_strDetail',title:'Details',sortable:true,width:150}
			]]
		}
	);
}

function itemGroupList_gotGroupItems (oResponse)
{
	try
	{
		$('#ItemGroupListDetails_table_itemDetailsDG').datagrid('loadData',oResponse.m_arrItemGroupData);
		var arrItemData = arrGroupItemData[0].m_oGroupItems;
	
	}
	catch(oException)
	{
		
	}
}

function itemGroupList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oItemGroupListMemberData.m_strSortColumn = strColumn;
	m_oItemGroupListMemberData.m_strSortOrder = strOrder;
	m_oItemGroupListMemberData.m_nPageNumber = nPageNumber;
	m_oItemGroupListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "itemGroupList_progressbarLoaded ()");
}
	
function itemGroupList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oItemGroupData = itemGroupList_getFormData ();
	oItemGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemGroupDataProcessor.list(oItemGroupData, m_oItemGroupListMemberData.m_strSortColumn, m_oItemGroupListMemberData.m_strSortOrder, m_oItemGroupListMemberData.m_nPageNumber, m_oItemGroupListMemberData.m_nPageSize, itemGroupList_listed);
}

function itemGroupList_listed (oResponse)
{	
	clearGridData ("#itemGroupList_table_itemGroupListDG");
		$('#itemGroupList_table_itemGroupListDG').datagrid('loadData', oResponse.m_arrItemGroupData);
	$('#itemGroupList_table_itemGroupListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oItemGroupListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function itemGroupList_filter()
{
	itemGroupList_list (m_oItemGroupListMemberData.m_strSortColumn, m_oItemGroupListMemberData.m_strSortOrder, 1, 10);
}

function itemGroupList_getFormData ()
{
	var oItemGroupData = new ItemGroupData ();
	oItemGroupData.m_strGroupName = $("#itemGroupList_input_groupName").val();
	return oItemGroupData;
}

function itemGroupList_displayImages (row, index)
{
	var oImage = eval (m_oItemGroupListMemberData.m_strActionItemsFunction);
	return oImage;
}

function itemGroupList_edit (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	m_oItemGroupListMemberData.m_nSelectedItemGroupId = nItemId;
	navigate ("editItem", "widgets/inventorymanagement/item/editItemGroup.js");
}

function itemGroupList_showAddPopup ()
{
	navigate ("newItem", "widgets/inventorymanagement/item/newItemGroup.js");
}

function itemGroupList_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oItemGroupData = new ItemGroupData ();
	var oListData = $("#itemGroupList_table_itemGroupListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oItemGroupData.m_nItemGroupId = oData.m_nItemGroupId;
	oItemGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var bConfirm = getUserConfirmation("Do you really want to delete this group?")
	if(bConfirm)
		ItemGroupDataProcessor.deleteData(oItemGroupData, itemGroupList_deleted);
}

function itemGroupList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("Item Group deleted successfully", "kSuccess");
		document.getElementById("itemGroupList_div_listDetail").innerHTML = "";
		itemGroupList_list (m_oItemGroupListMemberData.m_strSortColumn, m_oItemGroupListMemberData.m_strSortOrder, 1, 10);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function itemGroupList_publicise (nItemGroupId)
{
	assert.isNumber(nItemGroupId, "nItemGroupId expected to be a Number.");
	m_oItemGroupListMemberData.m_nSelectedItemGroupId = nItemGroupId;
	navigate ("publicise", "widgets/inventorymanagement/item/itemGroupPublicise.js");
}
