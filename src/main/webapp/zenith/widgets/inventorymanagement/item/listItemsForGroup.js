var listItemsForGroup_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js'
];

includeDataObjects (listItemsForGroup_includeDataObjects, "listItemsForGroup_loaded()");

function listItemsForGroup_memberData ()
{
	this.m_nSelectedItemId = -1;
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "listItemsForGroup_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_arrSelectedGroupItems = new Array ();
    this.m_arrItemGroupLineItems = new Array ();
    this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			listItemsForGroup_list("", "", 1, 10);
			$('#listItemsForGroup_input_filterArticleNumber').combobox('textbox').unbind('keydown', m_oListItemsForGroupMemberData.m_oKeyDownHandler);
			$('#listItemsForGroup_input_filterArticleNumber').combobox('textbox').focus();
		}
    };
}

var m_oListItemsForGroupMemberData = new listItemsForGroup_memberData ();

function listItemsForGroup_loaded ()
{
	loadPage ("inventorymanagement/item/listItemsForGroup.html", "itemGroup_div_ItemsDialog", "listItemsForGroup_init ()");
}

function listItemsForGroup_init ()
{
	var arrItemGroupData = $('#itemGroup_table_groupItems').datagrid('getRows');
	m_oListItemsForGroupMemberData.m_arrSelectedGroupItems = arrItemGroupData;
	createPopup("itemGroup_div_ItemsDialog", "#listItemsForGroup_button_add", "#listItemsForGroup_button_cancel", true);
	initArticleNumberCombobox ('#listItemsForGroup_input_filterArticleNumber', m_oListItemsForGroupMemberData.m_oKeyDownHandler);
	listItemsForGroup_createDataGrid ();
}

function listItemsForGroup_createDataGrid ()
{
	$('#listItemsForGroup_table_itemsListDG').datagrid
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
					m_oListItemsForGroupMemberData.m_strSortColumn = strColumn;
					m_oListItemsForGroupMemberData.m_strSortOrder = strOrder;
					listItemsForGroup_list (strColumn, strOrder, m_oListItemsForGroupMemberData.m_nPageNumber, m_oListItemsForGroupMemberData.m_nPageSize);
				},
				onCheck: function (rowIndex, rowData)
				{
					listItemsForGroup_holdCheckedData (rowData, true); 
				},
				onUncheck: function (rowIndex, rowData)
				{
					listItemsForGroup_holdCheckedData (rowData, false); 
				},
				onCheckAll: function (arrRows)
				{
					listItemsForGroup_holdAllCheckedData (arrRows);
				},
				onUncheckAll: function (arrRows)
				{
					listItemsForGroup_holdAllUnCheckedData (arrRows); 
				}
		}
	);
	listItemsForGroup_initDGPagination ();
	listItemsForGroup_list (m_oListItemsForGroupMemberData.m_strSortColumn,m_oListItemsForGroupMemberData.m_strSortOrder,1, 10);
}

function listItemsForGroup_initDGPagination ()
{
	$('#listItemsForGroup_table_itemsListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oListItemsForGroupMemberData.m_nPageNumber = $('#listItemsForGroup_table_itemsListDG').datagrid('getPager').pagination('options').pageNumber;
				listItemsForGroup_list (m_oListItemsForGroupMemberData.m_strSortColumn, m_oListItemsForGroupMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oListItemsForGroupMemberData.m_nPageNumber = $('#listItemsForGroup_table_itemsListDG').datagrid('getPager').pagination('options').pageNumber;
				m_oListItemsForGroupMemberData.m_nPageSize = $('#listItemsForGroup_table_itemsListDG').datagrid('getPager').pagination('options').pageSize;
				listItemsForGroup_list (m_oListItemsForGroupMemberData.m_strSortColumn, m_oListItemsForGroupMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function listItemsForGroup_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	var oItemData = new ItemData ();
	oItemData.m_strArticleNumber = $('#listItemsForGroup_input_filterArticleNumber').combobox('getValue');
	oItemData.m_strItemName = $("#listItemsForGroup_input_filterItemName").val();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.list(oItemData, strColumn, strOrder, nPageNumber, nPageSize, listItemsForGroup_listed);
}

function listItemsForGroup_filter()
{
	listItemsForGroup_list (m_oListItemsForGroupMemberData.m_strSortColumn, m_oListItemsForGroupMemberData.m_strSortOrder, 1, 10);
}

function listItemsForGroup_getFormData ()
{
	var oItemData = new ItemData ();
	oItemData.m_strArticleNumber = $('#filterItem_input_articleNumber').combobox('getValue');
	oItemData.m_strItemName = $("#filterItem_input_itemname").val();
	oItemData.m_strBrand = $("#filterItem_input_brand").val();
	return oItemData;
}

function listItemsForGroup_cancel ()
{
	HideDialog("itemGroup_div_ItemsDialog");
}

function listItemsForGroup_listed (oResponse)
{
	clearGridData ("#listItemsForGroup_table_itemsListDG");
	$('#listItemsForGroup_table_itemsListDG').datagrid('loadData',oResponse.m_arrItems);
	$('#listItemsForGroup_table_itemsListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oListItemsForGroupMemberData.m_nPageNumber});
	listItemsForGroup_checkDGRow ();
}

function listItemsForGroup_addItems ()
{
	if(m_oListItemsForGroupMemberData.m_arrSelectedGroupItems.length > 0)
	$('#itemGroup_table_groupItems').datagrid('loadData',m_oListItemsForGroupMemberData.m_arrSelectedGroupItems);
	else
	alert("please select list of items");
	HideDialog ("itemGroup_div_ItemsDialog");
}

function listItemsForGroup_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsadded = false;
	for (var nIndex = 0; !bIsadded && nIndex < m_oListItemsForGroupMemberData.m_arrSelectedGroupItems.length; nIndex++)
		bIsadded = (m_oListItemsForGroupMemberData.m_arrSelectedGroupItems [nIndex].m_strArticleNumber == oRowData.m_strArticleNumber);
	return bIsadded;
}

function listItemsForGroup_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	if(bIsForAdd)
	{
		if(!listItemsForGroup_isRowAdded (oRowData))
			m_oListItemsForGroupMemberData.m_arrSelectedGroupItems.push(oRowData);
	}
	else
		listItemsForGroup_remove (oRowData);
}

function listItemsForGroup_remove (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	for (var nIndex = 0; nIndex < m_oListItemsForGroupMemberData.m_arrSelectedGroupItems.length; nIndex++)
	{
		if(m_oListItemsForGroupMemberData.m_arrSelectedGroupItems[nIndex].m_strArticleNumber == oRowData.m_strArticleNumber)
		{
			m_oListItemsForGroupMemberData.m_arrSelectedGroupItems.splice(nIndex, 1);
			break;
		}
	}
}

function listItemsForGroup_checkDGRow ()
{
	var arrItemGroupData = $('#listItemsForGroup_table_itemsListDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrItemGroupData.length; nIndex++)
	{
		if(listItemsForGroup_isRowSelectable(arrItemGroupData[nIndex].m_strArticleNumber))
			$("#listItemsForGroup_table_itemsListDG").datagrid('checkRow', nIndex);
	}
}

function listItemsForGroup_isRowSelectable (strArticleNumber)
{

	assert.isString(strArticleNumber, "strArticleNumber expected to be a string.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oListItemsForGroupMemberData.m_arrSelectedGroupItems.length && !bIsSelectable; nIndex++)
	{
		if(m_oListItemsForGroupMemberData.m_arrSelectedGroupItems[nIndex].m_strArticleNumber == strArticleNumber)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function listItemsForGroup_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		listItemsForGroup_holdCheckedData(arrRows[nIndex], true);
}

function listItemsForGroup_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		listItemsForGroup_holdCheckedData(arrRows[nIndex], false);
}