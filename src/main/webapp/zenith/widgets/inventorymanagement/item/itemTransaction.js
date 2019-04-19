var itemTransaction_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js'
];

includeDataObjects (itemTransaction_includeDataObjects, "itemTransaction_loaded()");

function itemTransaction_memberData ()
{
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_nSelectedItemId = -1;
	this.m_nClientId = -1;
	this.m_arrSelectedData = new Array ();
	this.m_strSortColumn = "";
	this.m_strSortOrder = "";
}

var m_oItemTransactionData = new itemTransaction_memberData ();

function itemTransaction_init ()
{
	createPopup("dialog", "#itemTransaction_button_cancel", "#itemTransaction_button_cancel", true);
	itemTransaction_initTabs ();
	HideDialog ("ProcessDialog");
}

function itemTransaction_initTabs ()
{
	$('#itemTransaction_div_itemInfoTabs').tabs (
			{
				onSelect: function (oTitle)
				{
					if (oTitle.toLowerCase().search ('sales') >= 0)
						loadPage ("inventorymanagement/progressbar.html", "secondDialog", "itemTransaction_progressbarPopupForSales ()");
					else if (oTitle.toLowerCase().search ('purchase') >= 0)
						loadPage ("inventorymanagement/progressbar.html", "secondDialog", "itemTransaction_progressbarPopupForPurchases ()");
					else if (oTitle.toLowerCase().search ('clientwise') >= 0)
						loadPage ("inventorymanagement/progressbar.html", "secondDialog", "itemTransaction_progressbarPopupForClientwise ()");
				}
			});
}

function itemTransaction_initializeSalesDG ()
{
	$('#itemTransaction_table_salesDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strDate',title:'Date',sortable:true,width:120},
				{field:'m_strTo',title:'To',sortable:true,width:200},
				{field:'m_strInvoiceNo',title:'INV#', align:'right', sortable:true,width:100},
				{field:'m_nQuantity',title:'Qty',sortable:true,align:'right',width:100,
					formatter:function(value,row,index)
					{
						var nQuantity = row.m_nQuantity;
						try
						{
							nQuantity = nQuantity.toFixed(2);
						}
						catch(oException){}
						return nQuantity;
					},
		        	styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}	
				},
				{field:'m_nPrice',title:'Price',sortable:true,align:'right',width:120,
					formatter:function(value,row,index)
		        	{
					    var nPrice = row.m_nPrice;
					    try
						{
							if (!isNaN(value))
								nPrice = '<span class="rupeeSign">R </span>' + formatNumber (nPrice.toFixed(2),row,index);
							else
								return value;
						}
					    catch(oException){}
						return nPrice;
		        	}},
				{field:'m_nDiscount',title:'Disc(%)',sortable:true,align:'right',width:120,
					formatter:function(value,row,index)
					{
						var nDiscount = row.m_nDiscount;
						try
						{
							nDiscount = nDiscount.toFixed(2);
						}
						catch(oException){}
						return nDiscount;
					}},
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

function itemTransaction_initializePurchaseDG ()
{
	$('#itemTransaction_table_purchaseDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strDate',title:'Date',sortable:true,width:80},
				{field:'m_strFrom',title:'From',sortable:true,width:150},
				{field:'m_strInvoiceNo',title:'INV#', align:'right', sortable:true,width:80},
				{field:'m_nQuantity',title:'Quantity',sortable:true,width:80,align:'right',
					formatter:function(value,row,index)
					{
						var nQuantity = row.m_nQuantity;
						try
						{
							nQuantity = nQuantity.toFixed(2);
						}
						catch(oException){}
						return nQuantity;
					},
		        	styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}	
				},
				{field:'m_nPrice',title:'Price',sortable:true,width:90,align:'right',
					formatter:function(value,row,index)
		        	{
					    var nPrice = row.m_nPrice;
					    try
						{
							if (!isNaN(value))
								nPrice = '<span class="rupeeSign">R </span>' + formatNumber (nPrice.toFixed(2),row,index);
							else
								return value;
						}
					    catch(oException){}
						return nPrice;
		        	}	
				},
				{field:'m_nAmount',title:'Amount',width:100,sortable:true,align:'right',
					formatter:function(value,row,index)
		        	{
						var nIndianFormat = formatNumber (value,row,index);
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

function itemTransaction_initializeClientwiseDG ()
{
	$('#itemTransaction_table_clientwiseDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'ckBox',checkbox:true,width:100},
				{field:'m_strCompanyName',title:'Client Name',sortable:true,width:150,
			  		formatter:function(value,row,index)
					{
						var nCompanyName = row[0].m_strCompanyName;
						return nCompanyName;
					}
				},
				{field:'m_nQuantity',title:'Total Quantity',width:80,align:'right',
					formatter:function(value,row,index)
					{
						var nQuantity = row[1];
						try
						{
							nQuantity = nQuantity.toFixed(2);
						}
						catch(oException){}
						return nQuantity;
					},
		        	styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}	
				},
				{field:'m_nPrice',title:'Price',align:'right',width:120,
					formatter:function(value,row,index)
		        	{
					    var nPrice = row[2];
					    try
						{
							if (!isNaN(nPrice))
								nPrice = '<span class="rupeeSign">R </span>' + formatNumber (nPrice.toFixed(2),row,index);
							else
								return value;
						}
					    catch(oException){}
						return nPrice;
		        	}},
				{field:'m_nAmount',title:'Total Amount',width:100,align:'right',
					formatter:function(value,row,index)
		        	{
						var total = row[2] * row[1];
						var nIndianFormat = formatNumber (total.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	},
		        	styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}	
				},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return itemTransaction_displayImages (row, index);
		        	}
				},
			]],
			onCheck: function (rowIndex, rowData)
			{
				itemTransaction_holdCheckedData (rowData, true); 
				itemTransaction_addSendMessageButton ();
			},
			onUncheck: function (rowIndex, rowData)
			{
				itemTransaction_holdCheckedData (rowData, false); 
				itemTransaction_addSendMessageButton ();
			},
			onCheckAll: function (arrRows)
			{
				itemTransaction_holdAllCheckedData (arrRows);
				itemTransaction_addSendMessageButton ();
			},
			onUncheckAll: function (arrRows)
			{
				itemTransaction_holdAllUnCheckedData (arrRows); 
				itemTransaction_addSendMessageButton ();
			},
			onSortColumn: function (strColumn, strOrder)
			{
					m_oItemTransactionData.m_strSortColumn = strColumn;
					m_oItemTransactionData.m_strSortOrder = strOrder;
					itemTransaction_getClientwise (strColumn, strOrder);
			}
		}
	);
}

function itemTransaction_getSalesList ()
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "itemTransaction_progressbarLoaded ()");
}

function itemTransaction_progressbarLoaded ()
{
	createPopup("secondDialog", "", "", true);
	var oItemData = new ItemData ();
	oItemData.m_nItemId = m_oItemTransactionData.m_nSelectedItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var oSalesLineItemData = new SalesLineItemData ();
	oSalesLineItemData.m_oItemData.m_nItemId = m_oItemTransactionData.m_nSelectedItemId;
	oSalesLineItemData.m_strFromDate = m_oItemTransactionData.m_strFromDate;
	oSalesLineItemData.m_strToDate = m_oItemTransactionData.m_strToDate;
	SalesLineItemDataProcessor.list (oSalesLineItemData, "m_dCreatedOn", "desc",itemTransaction_gotSalesTransactions)
}

function itemTransaction_gotSalesTransactions (oResponse)
{
	clearGridData ("#itemTransaction_table_salesDG");
	$('#itemTransaction_table_salesDG').datagrid('loadData',oResponse.m_arrSalesLineItem);
	var nTotal = 0;
	var nTotalQty = 0;
	$('#itemTransaction_table_salesDG').datagrid('reloadFooter',[{m_strInvoiceNo:'<b>Total Qty</b>', m_nQuantity:nTotalQty, m_nDiscount:'<b>Total Amount</b>', m_nAmount:nTotal}]);
	HideDialog("secondDialog");
}

function itemTransaction_getPurchaseList ()
{
	var oItemData = new ItemData ();
	oItemData.m_nItemId = m_oItemTransactionData.m_nSelectedItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var oPurchaseLineItem = new PurchaseLineItem ();
	oPurchaseLineItem.m_oItemData = new ItemData ();
	oPurchaseLineItem.m_oItemData.m_nItemId = m_oItemTransactionData.m_nSelectedItemId;
	oPurchaseLineItem.m_strFromDate = m_oItemTransactionData.m_strFromDate;
	oPurchaseLineItem.m_strToDate = m_oItemTransactionData.m_strToDate;
	PurchaseLineItemDataProcessor.list (oPurchaseLineItem, "m_dCreatedOn", "desc",itemTransaction_gotPurchaseTransactions)
}

function itemTransaction_gotPurchaseTransactions (oResponse)
{
	clearGridData ("#itemTransaction_table_purchaseDG");
	$('#itemTransaction_table_purchaseDG').datagrid('loadData',oResponse.m_arrPurchaseLineItem);
	var nTotal = 0;
	var nTotalQty = 0;
	$('#itemTransaction_table_purchaseDG').datagrid('reloadFooter',[{m_strInvoiceNo:'<b>Total Qty</b>', m_nQuantity:nTotalQty, m_nPrice:'<b>Total Amount</b>', m_nAmount: nTotal.toFixed(2)}]);
	HideDialog ("secondDialog");
}

function itemTransaction_cancel ()
{
	HideDialog ("dialog");
}

function itemTransaction_displayImages (oRow, nIndex)
{
	assert.isArray(oRow, "oRow expected to be an Array.");
	assert(oRow.length >= 1, "oRow array cannot be empty.");
		var oImage = '<table align="center">'+
		'<tr>'+
		'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="itemTransaction_getInfo ('+oRow[0].m_nClientId+')"/> </td>'+
		'</tr>'+
	'</table>'
	return oImage;
}

function itemTransaction_getInfo (nClientId)
{
	assert.isNumber(nClientId, "nClientId expected to be a Number.");
	m_oItemTransactionData.m_nClientId = nClientId;
	navigate ("clientwiseInfo", "widgets/inventorymanagement/item/clientwiseInfo.js");
}

function itemTransaction_getClientwise (strColumn, strOrder)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	var oSalesData = new SalesData ();
	oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oSalesData.m_bIsForClientwise = true;
	oSalesData.m_nItemId = m_oItemTransactionData.m_nSelectedItemId;
	m_oItemTransactionData.m_strSortColumn = strColumn;
	m_oItemTransactionData.m_strSortOrder = strOrder;
	SalesDataProcessor.list(oSalesData, m_oItemTransactionData.m_strSortColumn, m_oItemTransactionData.m_strSortOrder, "", "", itemTransaction_gotClientwiseList);
}

function itemTransaction_gotClientwiseList (oResponse)
{
	clearGridData ("#itemTransaction_table_clientwiseDG");
	$('#itemTransaction_table_clientwiseDG').datagrid('loadData', oResponse.m_arrSales);
	HideDialog ("secondDialog");
	itemTransaction_checkDGRow ()
}

function itemTransaction_progressbarPopupForSales ()
{
	createPopup ("secondDialog", "", "", true);
	itemTransaction_initializeSalesDG ();
	itemTransaction_getSalesList ();
}

function itemTransaction_progressbarPopupForPurchases ()
{
	createPopup ("secondDialog", "", "", true);
	itemTransaction_initializePurchaseDG ();
	itemTransaction_getPurchaseList ();
}

function itemTransaction_progressbarPopupForClientwise ()
{
	createPopup ("secondDialog", "", "", true);
	itemTransaction_initializeClientwiseDG ();
	itemTransaction_getClientwise (m_oItemTransactionData.m_strSortColumn, m_oItemTransactionData.m_strSortOrder);
}

function itemTransaction_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isArray(oRowData, "oRowData expected to be an Array.");
	assert(oRowData.length >= 1, "oRowData array cannot be empty.");
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	if(bIsForAdd)
	{
		if(!itemTransaction_isRowAdded (oRowData))
			m_oItemTransactionData.m_arrSelectedData.push(oRowData);
	}
	else
		itemTransaction_removeItemGroup (oRowData);
}

function itemTransaction_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		itemTransaction_holdCheckedData(arrRows[nIndex], true);
}

function itemTransaction_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		itemTransaction_holdCheckedData(arrRows[nIndex], false);
}

function itemTransaction_isRowAdded (oRowData)
{
	assert.isArray(oRowData, "oRowData expected to be an Array.");
	assert(oRowData.length >= 1, "oRowData array cannot be empty.");
	var bIsadded = false;
	for (var nIndex = 0; !bIsadded && nIndex < m_oItemTransactionData.m_arrSelectedData.length; nIndex++)
		bIsadded = (m_oItemTransactionData.m_arrSelectedData [nIndex][0].m_strCompanyName == oRowData[0].m_strCompanyName);
	return bIsadded;
}

function itemTransaction_removeItemGroup (oRowData)
{
	assert.isArray(oRowData, "oRowData expected to be an Array.");
	assert(oRowData.length >= 1, "oRowData array cannot be empty.");
	for (var nIndex = 0; nIndex < m_oItemTransactionData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oItemTransactionData.m_arrSelectedData[nIndex][0].m_strCompanyName == oRowData[0].m_strCompanyName)
		{
			m_oItemTransactionData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function itemTransaction_checkDGRow ()
{
	var arrClientwiseData = $('#itemTransaction_table_clientwiseDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrClientwiseData.length; nIndex++)
	{
		if(itemTransaction_isRowSelectable(arrClientwiseData[nIndex][0].m_strCompanyName))
			$("#itemTransaction_table_clientwiseDG").datagrid('checkRow', nIndex);
	}
}
                    
function itemTransaction_isRowSelectable (strCompanyName)
{
	assert.isString(strCompanyName, "strCompanyName is expected to be of type string.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oItemTransactionData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oItemTransactionData.m_arrSelectedData[nIndex][0].m_strCompanyName == strCompanyName)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function itemTransaction_addSendMessageButton ()
{
	var bDisableMessageButton = true;
	var oSendMessageButton = document.getElementById ("itemTransaction_button_sendMessage");
	if(m_oItemTransactionData.m_arrSelectedData.length > 0)
		bDisableMessageButton = false;
	oSendMessageButton.disabled = bDisableMessageButton;
	oSendMessageButton.disabled ? oSendMessageButton.style.backgroundColor = "#c0c0c0" : oSendMessageButton.style.backgroundColor = "#0E486E";
}

function itemTransaction_sendMessage ()
{
	if(m_oItemTransactionData.m_arrSelectedData.length > 0)
		navigate ("clientwiseContactMail", "widgets/inventorymanagement/item/clientwiseContactMail.js");
}