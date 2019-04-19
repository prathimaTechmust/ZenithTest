var listItemsForChild_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/item/ChildItemData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js'
];


includeDataObjects (listItemsForChild_includeDataObjects, "listItemsForChild_loaded()");

function listItemsForChild_memberData ()
{
	this.m_nSelectedItemId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_arrSelectedItems = new Array ();
}

var m_oListItemsForChildMemberData = new listItemsForChild_memberData ();

function listItemsForChild_loaded ()
{
	loadPage ("inventorymanagement/item/listItemsForChild.html", "item_div_ChildItemsDialog", "listItemsForChild_init ()");
}

function listItemsForChild_init ()
{
	var arrItems = $('#item_table_itemListDG').datagrid('getRows');
	m_oListItemsForChildMemberData.m_arrSelectedItems = arrItems;
	createPopup("item_div_ChildItemsDialog", "#listItemsForChild_button_add", "#listItemsForChild_button_cancel", true);
	listItemsForChild_createDataGrid ();
}

function listItemsForChild_createDataGrid ()
{
	$('#listItemsForChild_table_itemsListDG').datagrid
	(
		{
			columns:
				[[
				    {field:'ckBox',checkbox:true,width:100},
				    {field:'m_strArticleNumber',title:'Article Number',sortable:true,width:100},
					{field:'m_strItemName',title:'Item Name',sortable:true,width:315}
				]],
				onSortColumn: function (strColumn, strOrder)
				{
					m_oListItemsForChildMemberData.m_strSortColumn = strColumn;
					m_oListItemsForChildMemberData.m_strSortOrder = strOrder;
					listItemsForChild_list (strColumn, strOrder, m_oListItemsForChildMemberData.m_nPageNumber, m_oListItemsForChildMemberData.m_nPageSize);
				},
				onCheck: function (rowIndex, rowData)
				{
					listItemsForChild_holdCheckedData (rowData, true); 
				},
				onUncheck: function (rowIndex, rowData)
				{
					listItemsForChild_holdCheckedData (rowData, false); 
				},
				onCheckAll: function (arrRows)
				{
					listItemsForChild_holdAllCheckedData (arrRows);
				},
				onUncheckAll: function (arrRows)
				{
					listItemsForChild_holdAllUnCheckedData (arrRows); 
				}
		}
	);
	listItemsForChild_initDGPagination ();
	listItemsForChild_list (m_oListItemsForChildMemberData.m_strSortColumn,m_oListItemsForChildMemberData.m_strSortOrder,1, 10);
}

function listItemsForChild_initDGPagination ()
{
	$('#listItemsForChild_table_itemsListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oListItemsForChildMemberData.m_nPageNumber = nPageNumber;
				listItemsForChild_list (m_oListItemsForChildMemberData.m_strSortColumn, m_oListItemsForChildMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oListItemsForChildMemberData.m_nPageNumber = nPageNumber;
				m_oListItemsForChildMemberData.m_nPageSize = nPageSize;
				listItemsForChild_list (m_oListItemsForChildMemberData.m_strSortColumn, m_oListItemsForChildMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function listItemsForChild_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	var oItemData = new ItemData ();
	oItemData.m_strItemName = $("#listItemsForChild_input_filterItemName").val();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.list(oItemData, strColumn, strOrder, nPageNumber, nPageSize, listItemsForChild_listed);
}

function listItemsForChild_filter()
{
	listItemsForChild_list (m_oListItemsForChildMemberData.m_strSortColumn, m_oListItemsForChildMemberData.m_strSortOrder, 1, 10);
}

function listItemsForChild_cancel ()
{
	HideDialog("item_div_ChildItemsDialog");
}

function listItemsForChild_listed (oResponse)
{
	$('#listItemsForChild_table_itemsListDG').datagrid('loadData',oResponse.m_arrItems);
	$('#listItemsForChild_table_itemsListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oListItemsForChildMemberData.m_nPageNumber});
	listItemsForChild_checkDGRow ();
}

function listItemsForChild_addItems ()
{
	$('#item_table_itemListDG').datagrid('loadData',m_oListItemsForChildMemberData.m_arrSelectedItems);
	if(m_oListItemsForChildMemberData.m_arrSelectedItems.length > 0)
		item_setSKUPartTax (m_oListItemsForChildMemberData.m_arrSelectedItems);
	else
	alert("please select article from the list");
	HideDialog ("item_div_ChildItemsDialog");
}

function listItemsForChild_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsadded = false;
	for (var nIndex = 0; !bIsadded && nIndex < m_oListItemsForChildMemberData.m_arrSelectedItems.length; nIndex++)
		bIsadded = (m_oListItemsForChildMemberData.m_arrSelectedItems [nIndex].m_oItemData.m_strArticleNumber == oRowData.m_strArticleNumber);
	return bIsadded;
}

function listItemsForChild_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	if(bIsForAdd)
	{
		if(!listItemsForChild_isRowAdded (oRowData))
		{
			var oChildItemData = new ChildItemData ();
			oChildItemData.m_oItemData = oRowData;
			oChildItemData.m_nQuantity = 1;
			m_oListItemsForChildMemberData.m_arrSelectedItems.push(oChildItemData);
		}
	}
	else
		listItemsForChild_remove (oRowData);
}

function listItemsForChild_remove (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	for (var nIndex = 0; nIndex < m_oListItemsForChildMemberData.m_arrSelectedItems.length; nIndex++)
	{
		if(m_oListItemsForChildMemberData.m_arrSelectedItems[nIndex].m_oItemData.m_strArticleNumber == oRowData.m_strArticleNumber)
		{
			m_oListItemsForChildMemberData.m_arrSelectedItems.splice(nIndex, 1);
			break;
		}
	}
}

function listItemsForChild_checkDGRow ()
{
	var arrItems = $('#listItemsForChild_table_itemsListDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrItems.length; nIndex++)
	{
		if(listItemsForChild_isRowSelectable(arrItems[nIndex].m_strArticleNumber))
			$("#listItemsForChild_table_itemsListDG").datagrid('checkRow', nIndex);
	}
}

function listItemsForChild_isRowSelectable (strArticleNumber)
{
	assert.isString(strArticleNumber, "strArticleNumber expected to be a string.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oListItemsForChildMemberData.m_arrSelectedItems.length && !bIsSelectable; nIndex++)
	{
		if(m_oListItemsForChildMemberData.m_arrSelectedItems[nIndex].m_oItemData.m_strArticleNumber == strArticleNumber)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function listItemsForChild_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
	{
		var oChildItemData = new ChildItemData ();
		oChildItemData.m_oItemData = arrRows[nIndex];
		oChildItemData.m_nQuantity = 1;
		listItemsForChild_holdCheckedData(oChildItemData, true);
	}
}

function listItemsForChild_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
	{
		var oChildItemData = new ChildItemData ();
		oChildItemData.m_oItemData = arrRows[nIndex];
		listItemsForChild_holdCheckedData(oChildItemData, false);
	}
}