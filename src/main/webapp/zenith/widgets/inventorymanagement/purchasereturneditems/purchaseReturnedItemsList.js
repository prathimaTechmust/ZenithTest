var purchaseReturnedItemsList_includeDataObjects = 
[
	'widgets/inventorymanagement/purchasereturneditems/PurchaseReturnedData.js',
	'widgets/inventorymanagement/purchasereturneditems/PurchaseReturnedLineItemData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js'
];



 includeDataObjects (purchaseReturnedItemsList_includeDataObjects, "purchaseReturnedItemsList_loaded()");

function purchaseReturnedItemsList_memberData ()
{
	this.m_nIndex = -1;
	this.m_strActionFunction = "purchaseReturnedItemsList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_nSelectedId = -1;
    this.m_strXMLData = "";
}

var m_oPurchaseReturnedItemsListMemberData = new purchaseReturnedItemsList_memberData ();

function purchaseReturnedItemsList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function purchaseReturnedItemsList_initAdmin ()
{
	m_oPurchaseReturnedItemsListMemberData.m_strActionFunction = "purchaseReturnedItemsList_addActions (row, index)";
	document.getElementById ("purchaseReturnedItemsList_button_add").style.visibility="visible";
	purchaseReturnedItemsList_init ();
}

function purchaseReturnedItemsList_initUser ()
{
	purchaseReturnedItemsList_init ();
}

function purchaseReturnedItemsList_init ()
{
	$("#purchaseReturnedItemsList_input_fromDate").datebox();
	$("#purchaseReturnedItemsList_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#purchaseReturnedItemsList_input_toDate").datebox();
	$("#purchaseReturnedItemsList_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	purchaseReturnedItemsList_initializeDataGrid ();
}

function purchaseReturnedItemsList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	var oActions = 
				'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" align="center" onClick="purchaseReturnedItemsList_edit ('+row.m_nId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png"  onClick="purchaseReturnedItemsList_delete ('+row.m_nId+')"/> </td>'+
						'<td> <img title="Print" src="images/print.jpg"  onClick="purchaseReturnedItemsList_print ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function purchaseReturnedItemsList_initializeDataGrid ()
{
	initHorizontalSplitter("#purchaseReturnedItemsList_div_horizontalSplitter", "#purchaseReturnedItemsList_table_purchaseListDG");
	$('#purchaseReturnedItemsList_table_purchaseListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'From',sortable:true,width:150,
			  		formatter:function(value,row,index)
					{
			        	return row.m_oVendorData.m_strCompanyName;
					}
			  	},
			  	{field:'m_strDebitNoteNumber',title:'Debit Note Number',sortable:true,width:70},
				{field:'m_strDate',title:'Date',sortable:true,width:70},
				{field:'Actions',title:'Action',width:40,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return purchaseReturnedItemsList_displayImages (row, index);
		        	}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				purchaseReturnedItemsList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oPurchaseReturnedItemsListMemberData.m_strSortColumn = strColumn;
				m_oPurchaseReturnedItemsListMemberData.m_strSortOrder = strOrder;
				purchaseReturnedItemsList_list (strColumn, strOrder, m_oPurchaseReturnedItemsListMemberData.m_nPageNumber, m_oPurchaseReturnedItemsListMemberData.m_nPageSize);
			}
		}
	);
	purchaseReturnedItemsList_initDGPagination ();
	purchaseReturnedItemsList_list (m_oPurchaseReturnedItemsListMemberData.m_strSortColumn, m_oPurchaseReturnedItemsListMemberData.m_strSortOrder, 1, 10);
}

function purchaseReturnedItemsList_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oPurchaseReturnedItemsListMemberData.m_nIndex = nIndex;
	document.getElementById("purchaseReturnedItemsList_div_listDetail").innerHTML = "";
	var oPurchaseReturnedData = new PurchaseReturnedData ();
	oPurchaseReturnedData.m_nId = oRowData.m_nId;
	PurchaseReturnedDataProcessor.getXML (oPurchaseReturnedData, function (strXMLData)
		{
			m_oPurchaseReturnedItemsListMemberData.m_strXMLData = strXMLData;
			populateXMLData (strXMLData, "inventorymanagement/purchasereturneditems/purchaseReturnedItemDetails.xslt", 'purchaseReturnedItemsList_div_listDetail');
			purchaseReturnedItemsList_initializeDetailsDG ();
			PurchaseReturnedDataProcessor.get (oPurchaseReturnedData, purchaseReturnedItemsList_gotReturnedLineItemData)
		});
}

function purchaseReturnedItemsList_gotReturnedLineItemData (oResponse)
{
	clearGridData ("#purchaseReturnedItemDetails_table_itemDetailsDG");
	var arrPurchaseReturns = purchaseReturnedItemsList_buildReturnedItemsArray (oResponse.m_arrPurchaseReturnedData[0]);
	$('#purchaseReturnedItemDetails_table_itemDetailsDG').datagrid('loadData', arrPurchaseReturns);
}

function purchaseReturnedItemsList_buildReturnedItemsArray (oPurchaseReturnedData)
{
	assert.isObject(oPurchaseReturnedData, "oPurchaseReturnedData expected to be an Object.");
	assert( Object.keys(oPurchaseReturnedData).length >0 , "oPurchaseReturnedData cannot be an empty .");// checks for non emptyness 
	var arrPurchaseReturns = new Array ();
	for(var nIndex=0; nIndex < oPurchaseReturnedData.m_oPurchaseReturnedLineItemData.length; nIndex++)
	{
		var oPurchaseReturnedLineItem = new PurchaseReturnedLineItemData ();
		oPurchaseReturnedLineItem.m_strArticleNumber = oPurchaseReturnedData.m_oPurchaseReturnedLineItemData[nIndex].m_oPurchaseLineItem.m_oItemData.m_strArticleNumber;
		oPurchaseReturnedLineItem.m_strItemName = oPurchaseReturnedData.m_oPurchaseReturnedLineItemData[nIndex].m_oPurchaseLineItem.m_oItemData.m_strItemName;
		oPurchaseReturnedLineItem.m_nShippedQuantity = oPurchaseReturnedData.m_oPurchaseReturnedLineItemData[nIndex].m_oPurchaseLineItem.m_nQuantity;
		oPurchaseReturnedLineItem.m_nQuantity = oPurchaseReturnedData.m_oPurchaseReturnedLineItemData[nIndex].m_oPurchaseLineItem.m_nReturnedQuantity;
		arrPurchaseReturns.push(oPurchaseReturnedLineItem);
	}
	return arrPurchaseReturns;
}

function purchaseReturnedItemsList_initializeDetailsDG ()
{
	$('#purchaseReturnedItemDetails_table_itemDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:50},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:100},
				{field:'m_nShippedQuantity',title:'Received Qty',sortable:true,align:'right',width:50},
				{field:'m_nQuantity',title:'Returned Qty',width:80,align:'right'}
			]]
		}
	);
}

function purchaseReturnedItemsList_displayImages (row, index)
{
	var oImage = eval (m_oPurchaseReturnedItemsListMemberData.m_strActionFunction);
	return oImage;
}

function purchaseReturnedItemsList_initDGPagination ()
{
	$('#purchaseReturnedItemsList_table_purchaseListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oPurchaseReturnedItemsListMemberData.m_nPageNumber = nPageNumber;
				purchaseReturnedItemsList_list (m_oPurchaseReturnedItemsListMemberData.m_strSortColumn, m_oPurchaseReturnedItemsListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("purchaseReturnedItemsList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oPurchaseReturnedItemsListMemberData.m_nPageNumber = nPageNumber;
				m_oPurchaseReturnedItemsListMemberData.m_nPageSize = nPageSize;
				purchaseReturnedItemsList_list (m_oPurchaseReturnedItemsListMemberData.m_strSortColumn, m_oPurchaseReturnedItemsListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("purchaseReturnedItemsList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function purchaseReturnedItemsList_filter ()
{
	purchaseReturnedItemsList_list (m_oPurchaseReturnedItemsListMemberData.m_strSortColumn, m_oPurchaseReturnedItemsListMemberData.m_strSortOrder, 1, 10);
}

function purchaseReturnedItemsList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oPurchaseReturnedItemsListMemberData.m_strSortColumn = strColumn;
	m_oPurchaseReturnedItemsListMemberData.m_strSortOrder = strOrder;
	m_oPurchaseReturnedItemsListMemberData.m_nPageNumber = nPageNumber;
	m_oPurchaseReturnedItemsListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "purchaseReturnedItemsList_progressbarLoaded ()");
}

function purchaseReturnedItemsList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oPurchaseReturnedData = new purchaseReturnedItemsList_getFormData ();
	PurchaseReturnedDataProcessor.list(oPurchaseReturnedData, m_oPurchaseReturnedItemsListMemberData.m_strSortColumn, m_oPurchaseReturnedItemsListMemberData.m_strSortOrder, m_oPurchaseReturnedItemsListMemberData.m_nPageNumber, m_oPurchaseReturnedItemsListMemberData.m_nPageSize, purchaseReturnedItemsList_listed);
}

function purchaseReturnedItemsList_getFormData ()
{
	var oPurchaseReturnedData = new PurchaseReturnedData ();
	oPurchaseReturnedData.m_oVendorData.m_strCompanyName = $("#filterReturns_input_to").val();
	oPurchaseReturnedData.m_strFromDate = FormatDate ($('#purchaseReturnedItemsList_input_fromDate').datebox('getValue'));
	oPurchaseReturnedData.m_strToDate = FormatDate ($('#purchaseReturnedItemsList_input_toDate').datebox('getValue'));
	return oPurchaseReturnedData;
}

function purchaseReturnedItemsList_listed (oResponse)
{	
	clearGridData('#purchaseReturnedItemsList_table_purchaseListDG');
	$('#purchaseReturnedItemsList_table_purchaseListDG').datagrid('loadData', oResponse.m_arrPurchaseReturnedData);
	$('#purchaseReturnedItemsList_table_purchaseListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oPurchaseReturnedItemsListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function purchaseReturnedItemsList_showAddPopup ()
{
	navigate ("", "widgets/inventorymanagement/purchasereturneditems/purchaseReturnedItemsAdmin.js");
}

function purchaseReturnedItemsList_edit (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	m_oPurchaseReturnedItemsListMemberData.m_nSelectedId = nId;
	navigate ("returned Items", "widgets/inventorymanagement/purchasereturneditems/purchaseReturnedItemsEdit.js");
}

function purchaseReturnedItemsList_print ()
{
	navigate ('printReturned','widgets/inventorymanagement/purchasereturneditems/purchaseReturnedItemListPrint.js');
}

function purchaseReturnedItemsList_delete (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	var oPurchaseReturnedData = new PurchaseReturnedData ();
	oPurchaseReturnedData.m_nId = nId;
	var bConfirm = getUserConfirmation("Do you really want to delete purchase returned item")
	if(bConfirm)
		PurchaseReturnedDataProcessor.deleteData(oPurchaseReturnedData, purchaseReturnedItemsList_deleted);
}

function purchaseReturnedItemsList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("purchase returned item deleted successfully", "kSuccess");
		document.getElementById("purchaseReturnedItemsList_div_listDetail").innerHTML = "";
		purchaseReturnedItemsList_list (m_oPurchaseReturnedItemsListMemberData.m_strSortColumn, m_oPurchaseReturnedItemsListMemberData.m_strSortOrder, 1, 10);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

