var reorderList_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js'
];


includeDataObjects (reorderList_includeDataObjects, "reorderList_loaded()");

function reorderList_memberData ()
{
	this.m_strActionItemsFunction = "reorderList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_strXMLData = "";
    this.m_arrSelectedData = new Array();
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			reorderList_list("", "", 1, 20);
			$('#filterReorder_input_articleNumber').combobox('textbox').unbind('keydown', m_oReorderListMemberData.m_oKeyDownHandler);
			$('#filterReorder_input_articleNumber').combobox('textbox').focus();
		}
    };
}

var m_oReorderListMemberData = new reorderList_memberData ();

function reorderList_loaded ()
{
	loadPage ("inventorymanagement/item/reorderList.html", "workarea", "reorderList_init ()");
}

function reorderList_init ()
{
	initArticleNumberCombobox ('#filterReorder_input_articleNumber', m_oReorderListMemberData.m_oKeyDownHandler);
	reorderList_createDataGrid ();
}

function reorderList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function reorderList_createDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#reorderList_table_reorderListDG");
	$('#reorderList_table_reorderListDG').datagrid
	(
		{
			fit:true,
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
					{field:'m_strDetail',title:'Detail',sortable:true,width:125},
					{field:'m_nReorderLevel',title:'Reorder Level',width:125,sortable:true,align:'right',
						formatter:function(value,row,index)
			        	{
						 	return value.toFixed(2);
			        	}
					},
					{field:'m_nCurrentStock',title:'Current Stock',width:125,sortable:true,align:'right',
						formatter:function(value,row,index)
			        	{
						 	var nCurrentStock =  row.m_nOpeningStock + row.m_nReceived - row.m_nIssued;
						 	return nCurrentStock.toFixed(2);
			        	}
					}
				]],
			    onCheck: function (rowIndex, rowData)
				{
					reorderlist_holdCheckedData (rowData, true);
					reorderlist_addVendorButton ()
				},
				onUncheck: function (rowIndex, rowData)
				{
					reorderlist_holdCheckedData (rowData, false); 
					reorderlist_addVendorButton ()
				},
				onCheckAll: function (arrRows)
				{
					reorderlist_holdAllCheckedData (arrRows);
					reorderlist_addVendorButton ()
				},
				onUncheckAll: function (arrRows)
				{
					reorderlist_holdAllUnCheckedData (arrRows); 
					reorderlist_addVendorButton ()
				},
				onSortColumn: function (strColumn, strOrder)
				{
					m_oReorderListMemberData.m_strSortColumn = strColumn;
					m_oReorderListMemberData.m_strSortOrder = strOrder;
					reorderList_list (strColumn, strOrder, m_oReorderListMemberData.m_nPageNumber, m_oReorderListMemberData.m_nPageSize);
				}
		}
	);
	
	reorderList_initDGPagination ();
	reorderList_list (m_oReorderListMemberData.m_strSortColumn,m_oReorderListMemberData.m_strSortOrder,1, 20);
}

function reorderList_initDGPagination ()
{
	$('#reorderList_table_reorderListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oReorderListMemberData.m_nPageNumber = $('#reorderList_table_reorderListDG').datagrid('getPager').pagination('options').pageNumber;
				reorderList_list (m_oReorderListMemberData.m_strSortColumn, m_oReorderListMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oReorderListMemberData.m_nPageNumber = $('#reorderList_table_reorderListDG').datagrid('getPager').pagination('options').pageNumber;
				m_oReorderListMemberData.m_nPageSize = $('#reorderList_table_reorderListDG').datagrid('getPager').pagination('options').pageSize;
				reorderList_list (m_oReorderListMemberData.m_strSortColumn, m_oReorderListMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function reorderList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oReorderListMemberData.m_strSortColumn = strColumn;
	m_oReorderListMemberData.m_strSortOrder = strOrder;
	m_oReorderListMemberData.m_nPageNumber = nPageNumber;
	m_oReorderListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "reorderList_progressbarLoaded ()");
}

function reorderList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oItemData = reorderList_getFormData ();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oItemData.m_bIsForReOrderList = true;
	ItemDataProcessor.list(oItemData, m_oReorderListMemberData.m_strSortColumn, m_oReorderListMemberData.m_strSortOrder, m_oReorderListMemberData.m_nPageNumber, m_oReorderListMemberData.m_nPageSize, reorderList_listed);
}

function reorderList_filter()
{
	reorderList_list (m_oReorderListMemberData.m_strSortColumn, m_oReorderListMemberData.m_strSortOrder, 1, 20);
}

function reorderList_getFormData ()
{
	var oItemData = new ItemData ();
	oItemData.m_strArticleNumber = $('#filterReorder_input_articleNumber').combobox('getValue');
	oItemData.m_strItemName = $("#filterReorder_input_itemname").val ();
	oItemData.m_strBrand = $("#filterReorder_input_brand").val();
	return oItemData;
}

function reorderList_displayImages (row, index)
{
	var oImage = eval (m_oReorderListMemberData.m_strActionItemsFunction);
	return oImage;
}

function reorderList_showFilterPopup ()
{
	loadPage ("inventorymanagement/item/filterItem.html", "dialog", "reorderList_initFilter ()");
}

function reorderList_initFilter ()
{
	createPopup('dialog', '#filterItem_button_cancel', '#filterItem_button_submit', true);
}

function reorderList_cancel ()
{
	HideDialog("dialog");
}

function reorderList_listed (oResponse)
{
	$('#reorderList_table_reorderListDG').datagrid('loadData', oResponse.m_arrItems);
	$('#reorderList_table_reorderListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oReorderListMemberData.m_nPageNumber});
	HideDialog ("dialog");
	reorderList_checkDGRow ();
}

function reorderlist_addVendorButton ()
{
	var oCreateOrderButton = document.getElementById ("reorderList_button_add");
	var bDisableCreateOrderButton = true;
	if(m_oReorderListMemberData.m_arrSelectedData.length > 0)
		bDisableCreateOrderButton = false;
	oCreateOrderButton.disabled = bDisableCreateOrderButton;
	oCreateOrderButton.disabled ? oCreateOrderButton.style.backgroundColor = "#c0c0c0" : oCreateOrderButton.style.backgroundColor = "#0E486E"
}

function reorderList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("item deleted successfully", "kSuccess");
		document.getElementById("reorderList_div_listDetail").innerHTML = "";
		reorderList_list (m_oReorderListMemberData.m_strSortColumn, m_oReorderListMemberData.m_strSortOrder, 1, 20);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function reorderList_setPreview (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	loadPage ("inventorymanagement/item/imagePreview.html", "dialog", "reorderList_showImagePreview ()");
	var oItemData = new ItemData ();
	oItemData.m_nItemId = nItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.get (oItemData, reorderList_gotImagePreviewData);
}

function reorderList_showImagePreview ()
{
	createPopup ('dialog', '', '', true);
	document.getElementById('dialog').style.position = "fixed";
}

function reorderList_gotImagePreviewData (oResponse)
{
	var oItemData = oResponse.m_arrItems[0];
	if (oItemData != null && oItemData != undefined && oItemData.m_buffImgPhoto != null)
		$("#imagePreview_img_item").attr('src', oItemData.m_buffImgPhoto);
}

function reorderList_cancelImagePreview ()
{
	HideDialog ("dialog");
}

function reorderList_printReorderList ()
{
	var arrReorderData = reorderList_getData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strArticleNumberFilterBox", $('#filterReorder_input_articleNumber').combobox('getText'));
	addChild(xmlDoc, "root", "m_strItemNameFilterBox", $("#filterReorder_input_itemname").val());
	addChild(xmlDoc, "root", "m_strBrandFilterBox", $("#filterReorder_input_brand").val());
	strXML = generateXML (xmlDoc, arrReorderData, "root", "ReorderListData");
	m_oReorderListMemberData.m_strXMLData = strXML;
	navigate ('reportPrint','widgets/inventorymanagement/item/reorderListPrint.js');
}

function reorderList_getData ()
{
	var arrReorder = $('#reorderList_table_reorderListDG').datagrid('getRows');
	var arrReorderData = new Array ();
	for (var nIndex = 0; nIndex < arrReorder.length; nIndex++)
	{
		var oItemData = new ItemData ();
		oItemData.m_strArticleNumber = arrReorder [nIndex].m_strArticleNumber;
		oItemData.m_strBrand = arrReorder [nIndex].m_strBrand;
		oItemData.m_strItemName = arrReorder [nIndex].m_strItemName;
		oItemData.m_strDetail =  arrReorder [nIndex].m_strDetail;
		oItemData.m_nReorderLevel =  arrReorder [nIndex].m_nReorderLevel;
		var nCurrentStock =  arrReorder[nIndex].m_nOpeningStock + arrReorder[nIndex].m_nReceived - arrReorder[nIndex].m_nIssued;
		oItemData.m_nCurrentStock = nCurrentStock;
		arrReorderData.push (oItemData);
	}
	return arrReorderData;
}

function reorderlist_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	if(bIsForAdd)
	{
		if(!reorderlist_isRowAdded (oRowData))
			m_oReorderListMemberData.m_arrSelectedData.push(oRowData);
	}
	else
		reorderlist_remove (oRowData);
}

function reorderlist_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oReorderListMemberData.m_arrSelectedData.length; nIndex++)
		bIsRowAdded = (m_oReorderListMemberData.m_arrSelectedData [nIndex].m_nItemId == oRowData.m_nItemId);
	return bIsRowAdded;
}

function reorderlist_remove (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	for (var nIndex = 0; nIndex < m_oReorderListMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oReorderListMemberData.m_arrSelectedData[nIndex].m_nItemId == oRowData.m_nItemId)
		{
			m_oReorderListMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function reorderList_checkDGRow ()
{
	var arrReorderData = $('#reorderList_table_reorderListDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrReorderData.length; nIndex++)
	{
		if(reorderList_isRowSelectable(arrReorderData[nIndex].m_nItemId))
			$("#reorderList_table_reorderListDG").datagrid('checkRow', nIndex);
	}
}

function reorderList_isRowSelectable (nItemId)
{

	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oReorderListMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oReorderListMemberData.m_arrSelectedData[nIndex].m_nItemId == nItemId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function reorderlist_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		reorderlist_holdCheckedData(arrRows[nIndex], true);
}

function reorderlist_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		reorderlist_holdCheckedData(arrRows[nIndex], false);
}

function reorderList_createOrderPopup ()
{
	if(m_oReorderListMemberData.m_arrSelectedData.length > 0)
		navigate ("vendorPurchaseOrder", "widgets/vendorpurchaseorder/vendorPurchaseOrderForReorder.js");
}

function reorderList_ClearCheckedData()
{
	$('#reorderList_table_reorderListDG').datagrid('clearChecked');
	m_oReorderListMemberData.m_arrSelectedData = new Array ();
	//document.getElementById ("reorderList_button_add").style.visibility="hidden";
}