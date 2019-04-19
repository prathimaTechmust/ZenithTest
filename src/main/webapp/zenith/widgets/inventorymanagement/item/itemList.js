var itemList_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/inventorymanagement/sales/CustomizedItemData.js'
];

includeDataObjects (itemList_includeDataObjects, "itemList_loaded()");

function itemList_memberData ()
{
	this.m_nSelectedItemId = -1;
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "itemList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_nItemId";
    this.m_strSortOrder = "desc";
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			itemList_list("", "", 1, 10);
			$('#filterItem_input_articleNumber').combobox('textbox').unbind('keydown', m_oItemListMemberData.m_oKeyDownHandler);
			$('#filterItem_input_articleNumber').combobox('textbox').focus();
		}
    };
}


var m_oItemListMemberData = new itemList_memberData ();

function itemList_init ()
{
	initArticleNumberCombobox ('#filterItem_input_articleNumber', m_oItemListMemberData.m_oKeyDownHandler);
	itemList_createDataGrid ();
}

function itemList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function itemList_initEdit ()
{
	m_oItemListMemberData.m_strActionItemsFunction = "itemList_addActions (row, index)";
	document.getElementById ("itemList_button_add").style.visibility="visible";
	document.getElementById ("itemList_button_export").style.visibility="visible";
	document.getElementById ("itemList_button_import").style.visibility="visible";
	itemList_init ();
}

function itemList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="itemList_edit ('+row.m_nItemId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="itemList_delete ('+index+')"/> </td>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="itemList_getInfo ('+row.m_nItemId+')"/> </td>'+
						'<td> <img title="Publicise" src="images/globe.png" width="20" align="center" id="publiciseImageId" onClick="itemList_publicise ('+row.m_nItemId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function itemList_createDataGrid ()
{
	initHorizontalSplitter("#itemList_div_horizontalSplitter", "#itemList_table_itemListDG");
	$('#itemList_table_itemListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strArticleNumber',title:'Article Number',sortable:true,width:100},
					{field:'m_strItemName',title:'Item Name',sortable:true,width:300,
				  		styler: function(value,row,index)
				  		{
				  			return {class:'DGcolumn'};
				  		}
					},
					{field:'m_strBrand',title:'Brand',sortable:true,width:100},
					{field:'m_strDetail',title:'Detail',sortable:true,width:125},
					{field:'Actions',title:'Action',width:90,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return itemList_displayImages (row, index);
			        	}
					},
				]]
		}
	);
	$('#itemList_table_itemListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				itemList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				m_oItemListMemberData.m_strSortColumn = strColumn;
				m_oItemListMemberData.m_strSortOrder = strOrder;
				itemList_list (strColumn, strOrder, m_oItemListMemberData.m_nPageNumber, m_oItemListMemberData.m_nPageSize);
			}
		}
	)
	itemList_initDGPagination ();
	itemList_list (m_oItemListMemberData.m_strSortColumn,m_oItemListMemberData.m_strSortOrder,1, 10);
}

function itemList_initDGPagination ()
{
	$('#itemList_table_itemListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oItemListMemberData.m_nPageNumber = nPageNumber;
				itemList_list (m_oItemListMemberData.m_strSortColumn, m_oItemListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("itemList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oItemListMemberData.m_nPageNumber = nPageNumber;
				m_oItemListMemberData.m_nPageSize = nPageSize;
				itemList_list (m_oItemListMemberData.m_strSortColumn, m_oItemListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("itemList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function itemList_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oItemListMemberData.m_nIndex = nIndex;
	document.getElementById("itemList_div_listDetail").innerHTML = "";
	var oItemData = new ItemData ();
	oItemData.m_nItemId = oRowData.m_nItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var oSalesLineItemData = new SalesLineItemData ();
	oSalesLineItemData.m_oItemData.m_nItemId = oRowData.m_nItemId;
	oSalesLineItemData.m_nMaxResult = 5;
	ItemDataProcessor.getXML (oItemData,itemList_gotXML);
	itemList_setImagePreview(oItemData);
	SalesLineItemDataProcessor.list (oSalesLineItemData, "", "",itemList_gotSalesTransactions)
}

function itemList_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "inventorymanagement/item/itemDetails.xslt", 'itemList_div_listDetail');
	itemList_initializeDetailsDG ();
}

function itemList_gotSalesTransactions (oResponse)
{
	clearGridData ("#ItemListDetails_table_itemDetailsDG");
	$('#ItemListDetails_table_itemDetailsDG').datagrid('loadData', oResponse.m_arrSalesLineItem);
	var nTotal = 0;
	$('#ItemListDetails_table_itemDetailsDG').datagrid('reloadFooter',[{m_nDiscount:'<b>Total</b>', m_nAmount:nTotal}]);
}

function itemList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oItemListMemberData.m_strSortColumn = strColumn;
	m_oItemListMemberData.m_strSortOrder = strOrder;
	m_oItemListMemberData.m_nPageNumber = nPageNumber;
	m_oItemListMemberData.m_nPageSize = nPageSize; 
	loadPage ("inventorymanagement/progressbar.html", "dialog", "itemList_progressbarLoaded ()");
}

function itemList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oItemData = itemList_getFormData ();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.list(oItemData, m_oItemListMemberData.m_strSortColumn, m_oItemListMemberData.m_strSortOrder, m_oItemListMemberData.m_nPageNumber, m_oItemListMemberData.m_nPageSize, itemList_listed);
}

function itemList_filter()
{
	itemList_list (m_oItemListMemberData.m_strSortColumn, m_oItemListMemberData.m_strSortOrder, 1, 10);
}

function itemList_getFormData ()
{
	var oItemData = new ItemData ();
	oItemData.m_strArticleNumber = $('#filterItem_input_articleNumber').combobox('getValue');
	oItemData.m_strItemName =$("#filterItem_input_itemname").val();
	oItemData.m_strBrand = $("#filterItem_input_brand").val();
	return oItemData;
}

function itemList_displayImages (row, index)
{
	var oImage = eval (m_oItemListMemberData.m_strActionItemsFunction);
	return oImage;
}

function itemList_edit (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	m_oItemListMemberData.m_nSelectedItemId = nItemId;
	loadPage ("include/process.html", "ProcessDialog", "itemList_progressbarLoadedForEdit ()");
}

function itemList_progressbarLoadedForEdit ()
{
	createPopup('ProcessDialog', '', '', true);
	navigate ("editItem", "widgets/inventorymanagement/item/editItem.js");
}

function itemList_getInfo (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	m_oItemListMemberData.m_nSelectedItemId = nItemId;
	loadPage ("include/process.html", "ProcessDialog", "itemList_progressbarLoadedForInfo ()");
}

function itemList_progressbarLoadedForInfo ()
{
	createPopup('ProcessDialog', '', '', true);
	navigate ("ItemTransaction", "widgets/inventorymanagement/item/itemTransactionForList.js");
}

function itemList_showFilterPopup ()
{
	loadPage ("inventorymanagement/item/filterItem.html", "dialog", "itemList_initFilter ()");
}

function itemList_showAddPopup ()
{
	navigate ("newItem", "widgets/inventorymanagement/item/itemAdmin.js");
}

function itemList_initFilter ()
{
	createPopup('dialog', '#filterItem_button_cancel', '#filterItem_button_submit', true);
}

function itemList_cancel ()
{
	HideDialog("dialog");
}

function itemList_listDetail_delete ()
{
	itemList_delete (m_oItemListMemberData.m_nIndex);
}

function itemList_delete (nIndex)
{  
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oItemData = new ItemData ();
	var oListData = $("#itemList_table_itemListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oItemData.m_nItemId = oData.m_nItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	alert("Item cannot be deleted as it is being used for sales or purchase");
//	var bConfirm = getUserConfirmation("Item cannot be deleted as it is being used for sales or purchase")
//	if(bConfirm)
//		ItemDataProcessor.deleteData(oItemData, itemList_deleted);
}

function itemList_listed (oResponse)
{
	document.getElementById("itemList_div_listDetail").innerHTML = "";
	$('#itemList_table_itemListDG').datagrid('loadData', oResponse.m_arrItems);
	$('#itemList_table_itemListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oItemListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function itemList_setImagePreview (oItemData)
{
	ItemDataProcessor.get (oItemData, itemList_gotData);
}

function itemList_gotData (oResponse)
{
	var oItemData = oResponse.m_arrItems[0];
	if (oItemData != null && oItemData != undefined && oItemData.m_buffImgPhoto != null)
		$("#ItemListDetails_img_itemImage").attr('src', oItemData.m_buffImgPhoto);
}

function itemList_initializeDetailsDG ()
{
	$('#ItemListDetails_table_itemDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strDate',title:'Date',sortable:true,width:120},
				{field:'m_strTo',title:'To',sortable:true,width:200},
				{field:'m_strInvoiceNo',title:'INV#',sortable:true,width:100},
				{field:'m_nQuantity',title:'Qty',sortable:true,align:'right',width:100},
				{field:'m_nPrice',title:'Price',sortable:true,align:'right',width:120,
					formatter:function(value,row,index)
		        	{
					    var nPrice = row.m_nPrice;
					    try
						{
							if (!isNaN(value))
								nPrice = '<span class="rupeeSign">R </span>' + formatNumber (nPrice,row,index);
							else
								return value;
						}
					    catch(oException){}
						return nPrice;
		        	}},
				{field:'m_nDiscount',title:'Disc(%)',sortable:true,align:'right',width:120},
				{field:'m_nAmount',title:'Amount',sortable:true,align:'right',width:120,
					formatter:function(value,row,index)
		        	{
						var nIndianFormat = formatNumber (value.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	},
		        	styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
				}
			]]
		}
	);
}

function itemList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("usermessage_itemlist_itemdeletedsuccessfully", "kSuccess");
		document.getElementById("itemList_div_listDetail").innerHTML = "";
		itemList_list (m_oItemListMemberData.m_strSortColumn, m_oItemListMemberData.m_strSortOrder, 1, 10);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function itemList_setPreview (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	loadPage ("inventorymanagement/item/imagePreview.html", "dialog", "itemList_showImagePreview ()");
	var oItemData = new ItemData ();
	oItemData.m_nItemId = nItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.get (oItemData, itemList_gotImagePreviewData);
}

function itemList_showImagePreview ()
{
	createPopup ('dialog', '', '', true);
	document.getElementById('dialog').style.position = "fixed";
}

function itemList_gotImagePreviewData (oResponse)
{
	var oItemData = oResponse.m_arrItems[0];
	if (oItemData != null && oItemData != undefined && oItemData.m_buffImgPhoto != null)
		$("#imagePreview_img_item").attr('src', oItemData.m_buffImgPhoto);
}

function itemList_cancelImagePreview ()
{
	HideDialog ("dialog");
}

function itemList_export ()
{
	navigate ('exportItemList','widgets/inventorymanagement/item/exportItems.js');
}


function itemList_import ()
{
	navigate ("importItems", "widgets/inventorymanagement/item/importItems.js");
}

function item_handleAfterSave ()
{
	document.getElementById("itemList_div_listDetail").innerHTML = "";
	itemList_list (m_oItemListMemberData.m_strSortColumn, m_oItemListMemberData.m_strSortOrder, 1, 10);
}

function item_handleAfterUpdate ()
{ 
	document.getElementById("itemList_div_listDetail").innerHTML = "";
	itemList_list (m_oItemListMemberData.m_strSortColumn, m_oItemListMemberData.m_strSortOrder, 1, 10);
}

function itemList_exportItemListAsXLS ()
{
	
}

function itemList_publicise (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");	
	m_oItemListMemberData.m_nSelectedItemId = nItemId;
	loadPage ("include/process.html", "ProcessDialog", "itemList_progressbarLoadedForPublicise ()");
}

function itemList_progressbarLoadedForPublicise ()
{
	createPopup ('ProcessDialog', '', '', true);
	navigate ("Itempublicise", "widgets/inventorymanagement/item/publiciseItem.js");
}