var publishItem_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
];



 includeDataObjects (publishItem_includeDataObjects, "publishItem_loaded()");

function publishItem_loaded ()
{
	loadPage ("onlinetradingmanagement/publishItem.html", "dialog", "publishItem_init()");
}

function publishItem_memberData ()
{
	this.m_nSelectedItemId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =15;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_arrSelectedData = new Array ();
    this.m_arrRemovedData = new Array ();
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			publishItem_list("", "", 1, 15);
			$('#filterItem_input_articleNumber').combobox('textbox').unbind('keydown', m_oPublishItemMemberData.m_oKeyDownHandler);
			$('#filterItem_input_articleNumber').combobox('textbox').focus();
		}
    };
}


var m_oPublishItemMemberData = new publishItem_memberData ();

function publishItem_init ()
{
	createPopup("dialog", "#publishItem_button_create", "#publishItem_button_cancel", true);
	initArticleNumberCombobox ('#filterItem_input_articleNumber', m_oPublishItemMemberData.m_oKeyDownHandler);
	publishItem_getCheckedItems ();
	publishItem_createDataGrid ();
}

function publishItem_getCheckedItems (strColumn, strOrder, nPageNumber, nPageSize)
{
	dwr.engine.setAsync(false);
	var oItemData = new ItemData ();
	oItemData.m_bPublishOnline = true;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.list(oItemData, "", "", publishItem_gotCheckedItems);
}

function publishItem_gotCheckedItems (oResponse)
{
	m_oPublishItemMemberData.m_arrSelectedData =  oResponse.m_arrItems;
	dwr.engine.setAsync(true);
}

function publishItem_createDataGrid ()
{
	$('#publishItem_table_itemListDG').datagrid
	(
		{
			columns:
				[[
				  	{field:'ckBox',checkbox:true},
				  	{field:'m_strArticleNumber',title:'Article Number',sortable:true,width:100},
					{field:'m_strItemName',title:'Item Name',sortable:true,width:315,
				  		styler: function(value,row,index)
				  		{
				  			return {class:'DGcolumn'};
				  		}
					},
					{field:'m_strBrand',title:'Brand',sortable:true,width:100},
					{field:'m_strDetail',title:'Detail',width:125},
				]],
				onCheck: function (rowIndex, rowData)
				{
					publishItem_holdCheckedData (rowData, true);
				},
				onUncheck: function (rowIndex, rowData)
				{
					publishItem_holdCheckedData (rowData, false);
				},
				onCheckAll: function (arrRows)
				{
					publishItem_holdAllCheckedData (arrRows)
				},
				onUncheckAll: function (arrRows)
				{
					publishItem_holdAllUnCheckedData (arrRows);
				}
		}
	);
	$('#publishItem_table_itemListDG').datagrid
	(
		{
			onSortColumn: function (strColumn, strOrder)
			{
				m_oPublishItemMemberData.m_strSortColumn = strColumn;
				m_oPublishItemMemberData.m_strSortOrder = strOrder;
				publishItem_list (strColumn, strOrder, m_oPublishItemMemberData.m_nPageNumber, m_oPublishItemMemberData.m_nPageSize);
			}
		}
	)
	publishItem_initDGPagination ();
	publishItem_list (m_oPublishItemMemberData.m_strSortColumn,m_oPublishItemMemberData.m_strSortOrder,1, 15);
}

function publishItem_initDGPagination ()
{
	$('#publishItem_table_itemListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oPublishItemMemberData.m_nPageNumber = nPageNumber;
				publishItem_list (m_oPublishItemMemberData.m_strSortColumn, m_oPublishItemMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oPublishItemMemberData.m_nPageNumber = nPageNumber;
				m_oPublishItemMemberData.m_nPageSize = nPageSize;
				publishItem_list (m_oPublishItemMemberData.m_strSortColumn, m_oPublishItemMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function publishItem_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oItemData = publishItem_getFilterData ();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.list(oItemData, strColumn, strOrder, nPageNumber, nPageSize, publishItem_listed);
}

function publishItem_filter()
{
	publishItem_list (m_oPublishItemMemberData.m_strSortColumn, m_oPublishItemMemberData.m_strSortOrder, 1, 15);
}

function publishItem_getFilterData ()
{
	var oItemData = new ItemData ();
	oItemData.m_oUserCredentialsData = new UserInformationData ();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oItemData.m_strArticleNumber = $('#filterItem_input_articleNumber').combobox('getValue');
	oItemData.m_strItemName = dwr.util.getValue ("filterItem_input_itemname");
	oItemData.m_strBrand = dwr.util.getValue ("filterItem_input_brand");
	return oItemData;
}

function publishItem_listed (oResponse)
{
	$('#publishItem_table_itemListDG').datagrid('loadData', oResponse.m_arrItems);
	$('#publishItem_table_itemListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oPublishItemMemberData.m_nPageNumber});
	publishItem_checkDGRow ();
}

function publishItem_checkDGRow ()
{
	var arrItemData = $('#publishItem_table_itemListDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrItemData.length; nIndex++)
	{
		if(publishItem_isRowSelectable(arrItemData[nIndex].m_strArticleNumber))
			$("#publishItem_table_itemListDG").datagrid('checkRow', nIndex);
	}
}

function publishItem_isRowSelectable (strArticleNumber)
{
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oPublishItemMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oPublishItemMemberData.m_arrSelectedData[nIndex].m_strArticleNumber == strArticleNumber)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function publishItem_submit ()
{
	loadPage ("include/process.html", "ProcessDialog", "publishItem_progressbarLoaded ()");
}

function publishItem_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var arrSelectedItemData = publishItem_getItems (m_oPublishItemMemberData.m_arrSelectedData);
	var arrRemovedItemData = publishItem_getItems (m_oPublishItemMemberData.m_arrRemovedData);
	ItemDataProcessor.updatePublishOnline(arrSelectedItemData, arrRemovedItemData, publishItem_created);
}

function publishItem_getItems (arrItems)
{
	var arrItemData = new Array ();
	for (var nIndex = 0; nIndex < arrItems.length; nIndex++)
	{
		var oItemData = new ItemData ();
		oItemData.m_nItemId = arrItems[nIndex].m_nItemId;
		oItemData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		arrItemData.push (oItemData);
	}
	return arrItemData;
}

function publishItem_cancel ()
{
	HideDialog("dialog");
}

function publishItem_created (oResponse)
{
	HideDialog("ProcessDialog");
	if(oResponse.m_bSuccess)
	{
		informUser("Items Published Successfully.", "kSuccess");
	    HideDialog("dialog");
	}
	else if(oResponse.m_nErrorID == 1)
	{
		informUser("Items Published Failed.", "kError");
	}
}

function publishItem_holdCheckedData (oRowData, bIsForAdd)
{
	if(bIsForAdd)
	{
		if(!publishItem_isRowAdded (m_oPublishItemMemberData.m_arrSelectedData, oRowData))
			m_oPublishItemMemberData.m_arrSelectedData.push(oRowData);
	}
	else
		publishItem_remove (oRowData);
}

function publishItem_remove (oRowData)
{
	if(!publishItem_isRowAdded (m_oPublishItemMemberData.m_arrRemovedData, oRowData))
		m_oPublishItemMemberData.m_arrRemovedData.push(oRowData);
	for (var nIndex = 0; nIndex < m_oPublishItemMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oPublishItemMemberData.m_arrSelectedData[nIndex].m_nItemId == oRowData.m_nItemId)
		{
			m_oPublishItemMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function publishItem_isRowAdded (arrItems, oRowData)
{
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < arrItems.length; nIndex++)
		bIsRowAdded = (arrItems [nIndex].m_nItemId == oRowData.m_nItemId);
	return bIsRowAdded;
}

function publishItem_holdAllCheckedData (arrRows)
{
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		publishItem_holdCheckedData(arrRows[nIndex], true);
}

function publishItem_holdAllUnCheckedData (arrRows)
{
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		publishItem_holdCheckedData(arrRows[nIndex], false);
}