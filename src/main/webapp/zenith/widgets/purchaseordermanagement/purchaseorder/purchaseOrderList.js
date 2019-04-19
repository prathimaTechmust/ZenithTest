var purchaseOrderList_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderStockLineItemData.js',
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js'
];

 includeDataObjects (purchaseOrderList_includeDataObjects, "purchaseOrderList_loaded ()");

var m_oPurchaseOrderListMemberData = new purchaseOrderList_memberData ();

function purchaseOrderList_memberData ()
{
	this.m_nSelectedPurchaseOrderId = -1;
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "purchaseOrderList_addHyphen()";
	this.m_strStatus = "";
	this.m_strDGId = "";
	this.m_strXMLData = "";
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
	this.m_oSelectedInvoiceData = null;
}

var m_oPurchaseOrderListMemberData = new purchaseOrderList_memberData ();

function purchaseOrderList_init ()
{
	$("#filterPurchaseOrder_input_fromDate").datebox();
	$('#filterPurchaseOrder_input_fromDate').datebox('textbox')[0].placeholder = "From Date";
	$("#filterPurchaseOrder_input_toDate").datebox();
	$('#filterPurchaseOrder_input_toDate').datebox('textbox')[0].placeholder = "To Date";
	initHorizontalSplitterWithTabs("#purchaseOrderList_div_horizontalSplitter", "#purchaseOrderList_div_POStatusTabs");
	$('#purchaseOrderList_div_POStatusTabs').tabs (
			{
				fit :true,
				onSelect: function (oTitle)
				{
					if (oTitle.toLowerCase().search ('pending') >= 0 )
					{
						document.getElementById("purchaseOrderList_div_listDetail").innerHTML = "";
						m_oPurchaseOrderListMemberData.m_strStatus = "kPending";
						purchaseOrderList_initializeDataGrid ("#purchaseOrderList_table_purchaseOrderPendingListDG");
					}
					if (oTitle.toLowerCase().search ('completed') >= 0 )
					{
						document.getElementById("purchaseOrderList_div_listDetail").innerHTML = "";
						m_oPurchaseOrderListMemberData.m_strStatus = "kDelivered";
						purchaseOrderList_initializeDataGrid ("#purchaseOrderList_table_purchaseOrderCompletedListDG");
					}
					if (oTitle.toLowerCase().search ('cancelled') >= 0 )
					{
						document.getElementById("purchaseOrderList_div_listDetail").innerHTML = "";
						m_oPurchaseOrderListMemberData.m_strStatus = "kCancelled";
						purchaseOrderList_initializeDataGrid ("#purchaseOrderList_table_purchaseOrderCancelledListDG");
					}
				}
			});
	$('#purchaseOrderList_div_POStatusTabs').tabs ('resize');
}

function purchaseOrderList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function purchaseOrderList_initUser ()
{
	purchaseOrderList_init ();
}

function purchaseOrderList_initAdmin ()
{
    var m_strActionItemsFunction = "purchaseOrderList_addActions (row, index)";
	document.getElementById ("purchaseOrderList_button_add").style.visibility="visible";
	purchaseOrderList_init ();
}

function purchaseOrderList_addActions (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	if(isQuantityShipped(row.m_oPurchaseOrderLineItems))
	{
		var oImage = '<table>'+
					'<tr>'+
						'<td> <button class="filterButton" type="button" onclick="purchaseOrderList_showPurchaseOrderPopup ('+row.m_nPurchaseOrderId+')">Execute</button></td>'+
					'</tr>'+
				'</table>'
	}
	else
	{
	    var oImage = '<table>'+
						'<tr>'+
							'<td> <button class="filterButton" type="button" onclick="purchaseOrderList_showPurchaseOrderPopup ('+row.m_nPurchaseOrderId+')">Execute</button></td>'+
							'<td> <img title="Cancel Order" src="images/close.png" width="20" align="center" onClick="purchaseOrderList_getUserConfirmation ('+row.m_nPurchaseOrderId+')"/> </td>'+
						'</tr>'+
					'</table>'
	}
	return oImage;
}

function isQuantityShipped(oPurchaseOrderLineItems) 
{
	assert.isArray(oPurchaseOrderLineItems, "oPurchaseOrderLineItems expected to be an Array.");
	var bQuantityShipped = false;
	for (var nIndex = 0; nIndex < oPurchaseOrderLineItems.length; nIndex++)
	{
		 if( oPurchaseOrderLineItems[nIndex].m_nShippedQty > 0)
		 {
			 bQuantityShipped = true;
			 break;
		 }
	}
	return bQuantityShipped;
}

function purchaseOrderList_getUserConfirmation (nPurchaseOrderId)
{
	assert.isNumber(nPurchaseOrderId, "nPurchaseOrderId expected to be a Number.");
	m_oPurchaseOrderListMemberData.m_nSelectedPurchaseOrderId = nPurchaseOrderId;
	processConfirmation ('Yes', 'No', 'Do you want to cancel this order ?', purchaseOrderList_cancelOrder);
}

function purchaseOrderList_cancelOrder (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if(bConfirm)
	{
		var oPurchaseOrderData = new PurchaseOrderData ();
		oPurchaseOrderData.m_nPurchaseOrderId = m_oPurchaseOrderListMemberData.m_nSelectedPurchaseOrderId;
		PurchaseOrderDataProcessor.cancelOrder(oPurchaseOrderData, purchaseOrderList_cancelled);
	}
}

function purchaseOrderList_cancelled (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Order cancelled successfully", "kSuccess");
		purchaseOrderList_list (m_oPurchaseOrderListMemberData.m_strSortColumn, m_oPurchaseOrderListMemberData.m_strSortOrder, 1, 10);
	}
}

function purchaseOrderList_initializeDataGrid (strDataGridId)
{
	assert.isString(strDataGridId, "strDataGridId expected to be a string.");
	m_oPurchaseOrderListMemberData.m_strDGId = strDataGridId;
	$(strDataGridId).datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'From',sortable:true,width:250,
			  		styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}	
			  	},
			  	{field:'m_strSiteName',title:'Site',sortable:true,width:150},
			  	{field:'m_strPurchaseOrderNumber',title:'Purchase Order No.',sortable:true,width:120},
				{field:'m_strPurchaseOrderDate',title:'Date',sortable:true,width:80},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
						var oImage = purchaseOrderList_addHyphen ()
						if(strDataGridId == "#purchaseOrderList_table_purchaseOrderPendingListDG")
							oImage = purchaseOrderList_displayImages (row, index);
						else if(strDataGridId == "#purchaseOrderList_table_purchaseOrderCancelledListDG")
							oImage = purchaseOrderList_displayCancelTabImages (row, index);
						return oImage;
		        	}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				purchaseOrderList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strPurchaseOrderDate") ? "m_dCreatedOn" : strColumn;
				m_oPurchaseOrderListMemberData.m_strSortColumn = strColumn;
				m_oPurchaseOrderListMemberData.m_strSortOrder = strOrder;
				purchaseOrderList_list (strColumn, strOrder, m_oPurchaseOrderListMemberData.m_nPageNumber, m_oPurchaseOrderListMemberData.m_nPageSize);
			}
		}
	);
	purchaseOrderList_initDGPagination (strDataGridId);
	purchaseOrderList_list (m_oPurchaseOrderListMemberData.m_strSortColumn, m_oPurchaseOrderListMemberData.m_strSortOrder, 1, 10);
}

function purchaseOrderList_displayCancelTabImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Reallow" src="images/reallow.png" width="20" align="center" onClick="purchaseOrderList_reallowConfirmation ('+row.m_nPurchaseOrderId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}

function purchaseOrderList_reallowConfirmation (nPurchaseOrderId)
{
	assert.isNumber(nPurchaseOrderId, "nPurchaseOrderId expected to be a Number.");
	m_oPurchaseOrderListMemberData.m_nSelectedPurchaseOrderId = nPurchaseOrderId;
	processConfirmation ('Yes', 'No', 'Do you want to reallow this order ?', purchaseOrderList_reallowOrder);
}

function purchaseOrderList_reallowOrder (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if(bConfirm)
	{
		var oPurchaseOrderData = new PurchaseOrderData ();
		oPurchaseOrderData.m_nPurchaseOrderId = m_oPurchaseOrderListMemberData.m_nSelectedPurchaseOrderId;
		PurchaseOrderDataProcessor.reallow(oPurchaseOrderData, purchaseOrderList_reallowed);
	}
}

function purchaseOrderList_reallowed (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Order reallowed successfully", "kSuccess");
		purchaseOrderList_list (m_oPurchaseOrderListMemberData.m_strSortColumn, m_oPurchaseOrderListMemberData.m_strSortOrder, 1, 10);
	}
}

function purchaseOrderList_filter ()
{
	purchaseOrderList_list (m_oPurchaseOrderListMemberData.m_strSortColumn, m_oPurchaseOrderListMemberData.m_strSortOrder, 1, 10);
}

function purchaseOrderList_initDGPagination (strDataGridId)
{
	assert.isString(strDataGridId, "strDataGridId expected to be a string.");
	$(strDataGridId).datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oPurchaseOrderListMemberData.m_nPageNumber = nPageNumber;
				purchaseOrderList_list (m_oPurchaseOrderListMemberData.m_strSortColumn, m_oPurchaseOrderListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("purchaseOrderList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oPurchaseOrderListMemberData.m_nPageNumber = nPageNumber;
				m_oPurchaseOrderListMemberData.m_nPageSize = nPageSize;
				purchaseOrderList_list (m_oPurchaseOrderListMemberData.m_strSortColumn, m_oPurchaseOrderListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("purchaseOrderList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function purchaseOrderList_displayImages (row, index)
{
	var oImage = eval (m_oPurchaseOrderListMemberData.m_strActionItemsFunction);
	return oImage;
}

function purchaseOrderList_selectedRowData (rowData, rowIndex)
{
	purchaseOrderList_showPoData (rowData, "purchaseOrderList_div_listDetail");
}

function purchaseOrderList_showPoData (oRowData, strDivId)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isString(strDivId, "strDivId expected to be a string.");
	document.getElementById(strDivId).innerHTML = "";
	var oPurchaseOrderData = new PurchaseOrderData ();
//	m_oPurchaseOrderListMemberData.m_oPurchaseOrderData = oRowData;
	oPurchaseOrderData.m_nPurchaseOrderId = oRowData.m_nPurchaseOrderId;
	oPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseOrderDataProcessor.getXML (oPurchaseOrderData,	{
		async:false, 
		callback: function (strXMLData)
		{
			populateXMLData (strXMLData, "purchaseordermanagement/purchaseorder/PurchaseOrderDetails.xslt", strDivId);
			purchaseOrderList_initializeDetailsDG ();
			PurchaseOrderDataProcessor.get (oPurchaseOrderData, purchaseOrderList_gotPurchaseOrderLineItemData)
			POChallanDataProcessor.getChallans(oPurchaseOrderData, purchaseOrderList_gotChallanList);
			POInvoiceDataProcessor.getInvoices(oPurchaseOrderData, purchaseOrderList_gotInvoiceList);
			PurchaseOrderDataProcessor.getPurchases (oPurchaseOrderData, purchaseOrderList_gotPurchaseList)
		}
	});
}

function purchaseOrderList_gotPurchaseOrderLineItemData (oResponse)
{
	clearGridData ("#purchaseOrderDetails_table_purchaseOrderDetailsDG");
	$('#purchaseOrderDetails_table_purchaseOrderDetailsDG').datagrid('loadData',oResponse.m_arrPurchaseOrder);
	var arrPurchaseOrderLineItemData = getOrderedLineItems (arrPurchaseOrderData [0].m_oPurchaseOrderLineItems);
	var nTotal = 0;
	$('#purchaseOrderDetails_table_purchaseOrderDetailsDG').datagrid('reloadFooter',[{m_nInvoiceQty:'<b>Total</b>', m_nAmount: nTotal.toFixed(2)}]);
}

function purchaseOrderList_gotChallanList (oResponse)
{
	clearGridData ("#purchaseOrderDetails_table_purchaseOrderChallanDG");
	$('#purchaseOrderDetails_table_purchaseOrderChallanDG').datagrid('loadData', oResponse.m_arrChallanData);
}
function purchaseOrderList_gotInvoiceList (oResponse)
{
	clearGridData ("#purchaseOrderDetails_table_purchaseOrderInvoiceDG");
	$('#purchaseOrderDetails_table_purchaseOrderInvoiceDG').datagrid('loadData',oResponse.m_arrInvoice);
}

function purchaseOrderList_gotPurchaseList (oResponse)
{
	clearGridData ("#purchaseOrderDetails_table_purchaseOrderPurchaseListDG");
	$('#purchaseOrderDetails_table_purchaseOrderPurchaseListDG').datagrid('loadData',oResponse.m_arrPurchase);
	var nGrandTotal = 0;
	for (var nIndex = 0; nIndex < oResponse.m_arrPurchase.length; nIndex++)
		nGrandTotal += purchaseOrderList_getNSPurchaseLineAmount(oResponse.m_arrPurchase[nIndex].m_oNonStockPurchaseLineItems)
	$('#purchaseOrderDetails_table_purchaseOrderPurchaseListDG').datagrid('reloadFooter',[{m_strDate:'<b>Grand Total :</b>', m_nAmount:nGrandTotal}]);
}

function purchaseOrderList_initializeDetailsDG ()
{
	$('#purchaseOrderDetails_table_purchaseOrderDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:70,
			  		formatter:function(value,row,index)
		        	{
			  		try
			  		{
			  			return row.m_oPurchaseOrderStockLineItems[0].m_oItemData.m_strArticleNumber;
			  		}
			  		catch(oException){}
		        	}	
			  	},
				{field:'m_strDesc',title:'Item Name',sortable:true,width:250},
				{field:'m_nQty',title:'Qty',sortable:true,width:50,align:'right',
					formatter:function (value,row,index)
					{
						var nQty = row.m_nQty;
						try
						{
							if (!isNaN(value))
								nQty = nQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nQty;
					}	
				},
				{field:'m_nPrice',title:'Price',sortable:true,width:100,align:'right',
					formatter:function(value,row,index)
		        	{
					    var nPrice = row.m_nPrice;
					    try
						{
							if (!isNaN(nPrice))
								nPrice = '<span class="rupeeSign">R </span>' + formatNumber (nPrice.toFixed(2),row,index);
							else
								return value;
						}
					    catch(oException){}
						return nPrice;
		        	}	
				},
				{field:'m_nDiscount',title:'Disc(%)',width:50,align:'right',
					formatter:function (value,row,index)
					{
						var nDiscount = row.m_nDiscount;
						try
						{
							if (!isNaN(value))
								nDiscount = nDiscount.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
						 return nDiscount;
					}
				},
				{field:'m_nTax',title:'Tax(%)',sortable:true,width:70,align:'right',
					formatter:function (value,row,index)
					{
						var nTax = row.m_nTax;
						try
						{
							if (!isNaN(value))
								nTax = nTax.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nTax;
					}
				},
				{field:'m_nShipQty',title:'Ship Qty',sortable:true,width:70,align:'right',
					formatter:function (value,row,index)
					{
						var nShipQty = row.m_nShipQty;
						try
						{
							if (!isNaN(value))
								nShipQty = nShipQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nShipQty;
					}	
				},
				{field:'m_nShippedQty',title:'Shipped Qty',sortable:true,width:80,align:'right',
					formatter:function (value,row,index)
					{
						var nShippedQty = row.m_nShippedQty;
						try
						{
							if (!isNaN(value))
								nShippedQty = nShippedQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nShippedQty;
					}	
				},
				{field:'m_nChallanQty',title:'Challaned Qty',sortable:true,width:100,align:'right',
					formatter:function (value,row,index)
					{
						var nChallanQty = row.m_nChallanQty;
						try
						{
							if (!isNaN(value))
								nChallanQty = nChallanQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nChallanQty;
					}	
				},
				{field:'m_nInvoiceQty',title:'Invoiced Qty',sortable:true,width:100,align:'right',
					formatter:function (value,row,index)
					{
						var nInvoiceQty = row.m_nInvoiceQty;
						try
						{
							if (!isNaN(value))
								nInvoiceQty = nInvoiceQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nInvoiceQty;
					}	
				},
				{field:'m_nAmount',title:'Amount',width:120,align:'right',
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
	
	$('#purchaseOrderDetails_table_purchaseOrderChallanDG').datagrid
	(
		{
			columns:
			[[
				{field:'m_strChallanNumber',title:'Challan No.',sortable:true,width:150},
				{field:'m_strDate',title:'Date',sortable:true,width:150},
				{field:'Actions',title:'Action',width:40,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return purchaseOrderList_displayImagesForChallan (row, index);
		        	}
				},
			]]
		}
	);
	
	$('#purchaseOrderDetails_table_purchaseOrderInvoiceDG').datagrid
	(
		{
			columns:
			[[
				{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:150},
				{field:'m_strDate',title:'Date',sortable:true,width:150},
				{field:'Actions',title:'Action',width:40,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return purchaseOrderList_displayImagesForInvoice (row, index);
		        	}
				},
			]]
		}
	);
	
	$('#purchaseOrderDetails_table_purchaseOrderInvoiceDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				m_oPurchaseOrderListMemberData.m_oSelectedInvoiceData = rowData;
			}
	});
	
	$('#purchaseOrderDetails_table_purchaseOrderPurchaseListDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strFrom',title:'From',sortable:true,width:150,
		        		styler: function(value,row,index)
				  		{
				  		 	return {class:'DGcolumn'};
				  		}
			  	},
			  	{field:'m_strInvoiceNo',title:'Invoice#',sortable:true,width:80},
				{field:'m_strDate',title:'Date',sortable:true,width:60},
				{field:'m_nAmount',title:'Amount',sortable:true,width:70,align:'right',
					formatter:function(value,row,index)
		        	{
						var nAmount = 0;
						try
						{
							nAmount = purchaseOrderList_getNSPurchaseLineAmount(row.m_oNonStockPurchaseLineItems);
						}
						catch (oException)
						{
							nAmount = row.m_nAmount;
						}
						var m_nIndianNumber = formatNumber (nAmount.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;
		        	},
		        	styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}	
				}	
			]]
		}
	);
	purchaseOrderList_initPurchaseSubGrid ();
}

function purchaseOrderList_getNSPurchaseLineAmount (arrNSPurchaseLineItems)
{
	assert.isArray(arrNSPurchaseLineItems, "arrNSPurchaseLineItems expected to be an Array.");
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrNSPurchaseLineItems.length; nIndex++)
	{
		var nAmount = 0;
    	nAmount =  arrNSPurchaseLineItems[nIndex].m_nPrice * arrNSPurchaseLineItems[nIndex].m_nQuantity;
    	nAmount -= nAmount *(arrNSPurchaseLineItems[nIndex].m_nDiscount/100);
    	nAmount += nAmount *(arrNSPurchaseLineItems[nIndex].m_nTax/100);
    	nAmount += nAmount *(arrNSPurchaseLineItems[nIndex].m_nOtherCharges/100);
    	nTotal += nAmount;
	}
	return nTotal;
}

function purchaseOrderList_displayImagesForChallan (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	var nInvoiceId = row.m_oInvoiceData != null ? row.m_oInvoiceData.m_nInvoiceId : -1;
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Print" src="images/print.jpg" width="20" align="center" id="editImageId" onClick="purchaseOrderList_printChallan ('+row.m_nChallanId+ ',' +nInvoiceId+ ')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function purchaseOrderList_displayImagesForInvoice (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Print" src="images/print.jpg" id="printImageId" onClick="purchaseOrderList_printInvoice ('+row.m_nInvoiceId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}

function purchaseOrderList_initPurchaseSubGrid ()
{
	$('#purchaseOrderDetails_table_purchaseOrderPurchaseListDG').datagrid({view: detailview,detailFormatter:function(index,row)
		{
            return '<div style="padding:2px"><table class="purchaseOrderDetails_table_detailViewDG"></table></div>';
        },
        onExpandRow: function(index,row)
        {
            var purchaseOrderDetails_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.purchaseOrderDetails_table_detailViewDG');
            purchaseOrderDetails_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
                columns:[[
                    {field:'m_strArticleDescription',title:'Item Name',width:120},
	                {field:'m_nQuantity',title:'Quantity',width:60,align:'right',
			        		formatter:function(value,row,index)
				        	{
								 return row.m_nQuantity.toFixed(2);
				        	}		
	                },
	                {field:'m_nPrice',title:'Price',width:100,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                		 var value = row.m_nPrice.toFixed(2);
	                	     var m_nIndianNumber = formatNumber (value,row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}
	                },
	                {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',
		        		formatter:function(value,row,index)
			        	{
							 return row.m_nDiscount.toFixed(2);
			        	}		
	                },
			        {field:'m_strTaxName',title:'Tax Name',width:70,align:'right'},
			        {field:'m_nTax',title:'Tax(%)',width:60,align:'right',
		        		formatter:function(value,row,index)
			        	{
							 return row.m_nTax.toFixed(2);
			        	}		
	                },
			        {field:'m_nOtherCharges',title:'Other Chgs(%)',width:95,align:'right',
		        		formatter:function(value,row,index)
			        	{
							 return row.m_nOtherCharges.toFixed(2);
			        	}		
	                },
	                {field:'m_nAmount',title:'Amount',width:120,align:'right',
	                	formatter:function(value,row,index)
			        	{
		                	var nAmount = 0;
		                	nAmount =  row.m_nPrice * row.m_nQuantity;
		                	nAmount -= nAmount *(row.m_nDiscount/100);
		                	nAmount += nAmount *(row.m_nTax/100);
		                	nAmount += nAmount *(row.m_nOtherCharges/100);
		                	return '<span class="rupeeSign">R  </span>' + formatNumber (nAmount.toFixed(2), row, index);
			        	}
	                }
                ]],
                onResize:function()
                {
                    $('#purchaseOrderDetails_table_purchaseOrderPurchaseListDG').datagrid('fixDetailRowHeight',index);
                }
            });
            purchaseOrderList_listPurchaseSubGrid (purchaseOrderDetails_table_detailViewDG, index, row);
        }
    });
}

function purchaseOrderList_listPurchaseSubGrid (purchaseOrderDetails_table_detailViewDG, index, row)
{
	purchaseOrderDetails_table_detailViewDG.datagrid('loadData', row.m_oNonStockPurchaseLineItems);
    $('#purchaseOrderDetails_table_purchaseOrderPurchaseListDG').datagrid('fixDetailRowHeight',index);
}

function purchaseOrderList_printChallan  (nChallanId, nInvoiceId)
{
	assert.isNumber(nChallanId, "nChallanId expected to be a Number.");
	assert.isNumber(nInvoiceId, "nInvoiceId expected to be a Number.");
	m_oPurchaseOrderListMemberData.m_nChallanId = nChallanId;
	m_oPurchaseOrderListMemberData.m_nInvoiceId = nInvoiceId;
	navigate ('print challan','widgets/inventorymanagement/challan/POPrintChallan.js');
}

function purchaseOrderList_printInvoice  (nInvoiceId)
{
	assert.isNumber(nInvoiceId, "nInvoiceId expected to be a Number.");
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_nInvoiceId = nInvoiceId;
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	InvoiceDataProcessor.getXML (oInvoiceData,	
	{	
		async:false, 
		callback: function (strXMLData)
		{
			m_oPurchaseOrderListMemberData.m_strXMLData = strXMLData;
			navigate ('print invoice','widgets/inventorymanagement/invoice/POPrintInvoice.js');
		}
	});
}

function purchaseOrderList_list  (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oPurchaseOrderListMemberData.m_strSortColumn = strColumn;
	m_oPurchaseOrderListMemberData.m_strSortOrder = strOrder;
	m_oPurchaseOrderListMemberData.m_nPageNumber = nPageNumber;
	m_oPurchaseOrderListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "purchaseOrderList_progressbarLoaded ()");
}

function purchaseOrderList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oPurchaseOrderData = purchaseOrderList_getFormData ();
	oPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseOrderDataProcessor.list(oPurchaseOrderData, m_oPurchaseOrderListMemberData.m_strSortColumn, m_oPurchaseOrderListMemberData.m_strSortOrder, m_oPurchaseOrderListMemberData.m_nPageNumber, m_oPurchaseOrderListMemberData.m_nPageSize, purchaseOrderList_listed);
}

function purchaseOrderList_listed (oResponse)
{
	document.getElementById("purchaseOrderList_div_listDetail").innerHTML = "";
	$(m_oPurchaseOrderListMemberData.m_strDGId).datagrid('loadData', oResponse.m_arrPurchaseOrder);
	clearGridData (m_oPurchaseOrderListMemberData.m_strDGId);
	$(m_oPurchaseOrderListMemberData.m_strDGId).datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oPurchaseOrderListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function purchaseOrderList_getFormData () 
{
	var oPurchaseOrderData = new PurchaseOrderData ();
	oPurchaseOrderData.m_oClientData.m_strCompanyName = $("#filterPurchaseOrder_input_clientName").val();
	oPurchaseOrderData.m_strPurchaseOrderNumber = $("#filterPurchaseOrder_input_purchaseOrderNumber").val();
	oPurchaseOrderData.m_strFromDate = FormatDate ($('#filterPurchaseOrder_input_fromDate').datebox('getValue'));
	oPurchaseOrderData.m_strToDate = FormatDate ($('#filterPurchaseOrder_input_toDate').datebox('getValue'));
	oPurchaseOrderData.m_oSiteData.m_strSiteName = $("#filterPurchaseOrder_input_siteName").val();
	oPurchaseOrderData.m_nPurchaseOrderStatus = m_oPurchaseOrderListMemberData.m_strStatus;
	return oPurchaseOrderData;
}

function purchaseOrderList_cancel ()
{
	HideDialog("dialog");
}

function purchaseOrderList_showAddPopup ()
{
	navigate ("purchaseOrder", "widgets/purchaseordermanagement/purchaseorder/purchaseOrderNew.js");
}

function purchaseOrderList_showPurchaseOrderPopup (nPurchaseOrderId)
{
	assert.isNumber(nPurchaseOrderId, "nPurchaseOrderId expected to be a Number.");
	m_oPurchaseOrderListMemberData.m_nSelectedPurchaseOrderId = nPurchaseOrderId;
	loadPage ("include/process.html", "ProcessDialog", "purchaseOrderList_showAddPopup_progressbarLoaded ()");
}

function purchaseOrderList_showAddPopup_progressbarLoaded ()
{
	navigate ("purchaseOrder", "widgets/purchaseordermanagement/purchaseorder/purchaseOrderForAssignStocks.js");
}
function purchaseOrder_handleAfterSave ()
{
	document.getElementById("purchaseOrderList_div_listDetail").innerHTML = "";
	clearGridData (m_oPurchaseOrderListMemberData.m_strDGId);
	purchaseOrderList_list (m_oPurchaseOrderListMemberData.m_strSortColumn, m_oPurchaseOrderListMemberData.m_strSortOrder,1, 10);
}

