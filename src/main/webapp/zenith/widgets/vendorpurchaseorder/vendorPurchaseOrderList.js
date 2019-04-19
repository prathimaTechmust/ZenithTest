var vendorPurchaseOrderList_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/vendorpurchaseorder/VendorPurchaseOrderData.js',
	'widgets/vendorpurchaseorder/VendorPOLineItemData.js',
];

 includeDataObjects (vendorPurchaseOrderList_includeDataObjects, "vendorPurchaseOrderList_loaded ()");

function vendorPurchaseOrderList_memberData ()
{
	this.m_nSelectedPurchaseOrderId = -1;
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "vendorPurchaseOrderList_addHyphen()";
	this.m_bIsPOStatusPending = true;
	this.m_strDGId = "";
	this.m_strXMLData = "";
	this.m_strEmailAddress = "";
	this.m_strSubject = "";
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
	this.m_oVendorPurchaseOrderData = null;
}

var m_oVendorPurchaseOrderListMemberData = new vendorPurchaseOrderList_memberData ();

function vendorPurchaseOrderList_init ()
{
	$("#vendorPurchaseOrderList_input_fromDate").datebox();
	$("#vendorPurchaseOrderList_input_toDate").datebox();
	initHorizontalSplitterWithTabs("#vendorPurchaseOrderList_div_horizontalSplitter", "#vendorPurchaseOrderList_div_POStatusTabs");
	$('#vendorPurchaseOrderList_div_POStatusTabs').tabs (
			{
				fit :true,
				onSelect: function (oTitle)
				{
					if (oTitle.toLowerCase().search ('pending') >= 0 )
					{
						document.getElementById("vendorPurchaseOrderList_div_listDetail").innerHTML = "";
						m_oVendorPurchaseOrderListMemberData.m_bIsPOStatusPending = true;
						vendorPurchaseOrderList_initializeDataGrid ("#vendorPurchaseOrderList_table_purchaseOrderPendingListDG");
					}
					if (oTitle.toLowerCase().search ('completed') >= 0 )
					{
						document.getElementById("vendorPurchaseOrderList_div_listDetail").innerHTML = "";
						m_oVendorPurchaseOrderListMemberData.m_bIsPOStatusPending = false;
						vendorPurchaseOrderList_initializeDataGrid ("#vendorPurchaseOrderList_table_purchaseOrderCompletedListDG");
					}
				}
			});
	$('#vendorPurchaseOrderList_div_POStatusTabs').tabs ('resize');
}

function vendorPurchaseOrderList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function vendorPurchaseOrderList_initUser ()
{
	vendorPurchaseOrderList_init ();
}

function vendorPurchaseOrderList_initAdmin ()
{
	m_oVendorPurchaseOrderListMemberData.m_strActionItemsFunction = "vendorPurchaseOrderList_addActions (row, index)";
	document.getElementById ("vendorPurchaseOrderList_button_add").style.visibility="visible";
	vendorPurchaseOrderList_init ();
}

function vendorPurchaseOrderList_addActions (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <button class="filterButton" type="button" onclick="vendorPurchaseOrderList_showPurchaseOrderPopup ('+row.m_nPurchaseOrderId+')">Execute</button></td>'+
							'<td> <img title="Print" src="images/print.jpg" width="20" id="printImageId" onClick="vendorPurchaseOrderList_print ()"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function vendorPurchaseOrderList_initializeDataGrid (strDataGridId)
{
	assert.isString(strDataGridId, "strDataGridId expected to be a string.");
	m_oVendorPurchaseOrderListMemberData.m_strDGId = strDataGridId;
	$(strDataGridId).datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'To',sortable:true,width:200,
			  		formatter:function(value,row,index)
		        	{
		        		return row.m_oVendorData.m_strCompanyName;
		        	},
			  		styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}	
			  	},
			  	{field:'m_strPurchaseOrderNumber',title:'Purchase Order No.',sortable:true,width:140},
				{field:'m_strPurchaseOrderDate',title:'Date',sortable:true,width:90},
				{field:'Actions',title:'Action',width:70,align:'center',
					formatter:function(value,row,index)
		        	{
						var oImage = vendorPurchaseOrderList_addHyphen ()
						if(strDataGridId == "#vendorPurchaseOrderList_table_purchaseOrderPendingListDG")
							oImage = vendorPurchaseOrderList_displayImages (row, index);
						return oImage;
		        	}
				}
			]]
		}
	);
	$(strDataGridId).datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				vendorPurchaseOrderList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strPurchaseOrderDate") ? "m_dCreatedOn" : strColumn;
				m_oVendorPurchaseOrderListMemberData.m_strSortColumn = strColumn;
				m_oVendorPurchaseOrderListMemberData.m_strSortOrder = strOrder;
				vendorPurchaseOrderList_list (strColumn, strOrder, m_oVendorPurchaseOrderListMemberData.m_nPageNumber, m_oVendorPurchaseOrderListMemberData.m_nPageSize);
			}
		}
	)
	vendorPurchaseOrderList_initDGPagination (strDataGridId);
	vendorPurchaseOrderList_list (m_oVendorPurchaseOrderListMemberData.m_strSortColumn, m_oVendorPurchaseOrderListMemberData.m_strSortOrder, 1, 10);
}

function vendorPurchaseOrderList_filter ()
{
	vendorPurchaseOrderList_list (m_oVendorPurchaseOrderListMemberData.m_strSortColumn, m_oVendorPurchaseOrderListMemberData.m_strSortOrder, 1, 10);
}

function vendorPurchaseOrderList_initDGPagination (strDataGridId)
{
	assert.isString(strDataGridId, "strDataGridId expected to be a string.");
	$(strDataGridId).datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorPurchaseOrderListMemberData.m_nPageNumber = $(strDataGridId).datagrid('getPager').pagination('options').pageNumber;
				vendorPurchaseOrderList_list (m_oVendorPurchaseOrderListMemberData.m_strSortColumn, m_oVendorPurchaseOrderListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("vendorPurchaseOrderList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorPurchaseOrderListMemberData.m_nPageNumber = $(strDataGridId).datagrid('getPager').pagination('options').pageNumber;
				m_oVendorPurchaseOrderListMemberData.m_nPageSize = $(strDataGridId).datagrid('getPager').pagination('options').pageSize;
				vendorPurchaseOrderList_list (m_oVendorPurchaseOrderListMemberData.m_strSortColumn, m_oVendorPurchaseOrderListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("vendorPurchaseOrderList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function vendorPurchaseOrderList_displayImages (row, index)
{
	var oImage = eval (m_oVendorPurchaseOrderListMemberData.m_strActionItemsFunction);
	return oImage;
}

function vendorPurchaseOrderList_selectedRowData (rowData, rowIndex)
{
	assert.isObject(rowData, "rowData expected to be an Object.");
	assert( Object.keys(rowData).length >0 , "rowData cannot be an empty .");// checks for non emptyness 
	document.getElementById("vendorPurchaseOrderList_div_listDetail").innerHTML = "";
	var oVendorPurchaseOrderData = new VendorPurchaseOrderData ();
	m_oVendorPurchaseOrderListMemberData.m_oVendorPurchaseOrderData = rowData;
	oVendorPurchaseOrderData.m_nPurchaseOrderId = rowData.m_nPurchaseOrderId;
	oVendorPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	VendorPurchaseOrderDataProcessor.getXML (oVendorPurchaseOrderData,	function (strXMLData)
		{
			m_oVendorPurchaseOrderListMemberData.m_strXMLData = strXMLData;
			m_oVendorPurchaseOrderListMemberData.m_strEmailAddress = rowData.m_oVendorData.m_strEmail;
			m_oVendorPurchaseOrderListMemberData.m_strSubject = " Vendor Purchase Order Details"
			populateXMLData (strXMLData, "vendorpurchaseorder/vendorPurchaseOrderDetails.xslt", 'vendorPurchaseOrderList_div_listDetail');
			vendorPurchaseOrderList_initializeDetailsDG ();
			VendorPurchaseOrderDataProcessor.get (oVendorPurchaseOrderData, vendorPurchaseOrderList_gotPurchaseOrderLineItemData);
			vendorPurchaseOrderList_purchaseInvoiceList (rowData);
		});
}

function vendorPurchaseOrderList_gotPurchaseOrderLineItemData (oResponse)
{
	clearGridData ("#vendorPurchaseOrderDetails_table_purchaseOrderDetailsDG");
	var arrPurchaseOrderLineItemData = getOrderedLineItems (arrVendorPurchaseOrderData [0].m_oVendorPOLineItemData);
	var nTotal = 0;
	$('#vendorPurchaseOrderDetails_table_purchaseOrderDetailsDG').datagrid('loadData',oResponse.m_arrVendorPurchaseOrderData);
//	for (var nIndex = 0; nIndex < arrPurchaseOrderLineItemData.length; nIndex++)
//	{
//		var nDiscPrice = arrPurchaseOrderLineItemData[nIndex].m_nPrice - (arrPurchaseOrderLineItemData[nIndex].m_nPrice * (arrPurchaseOrderLineItemData[nIndex].m_nDiscount/100));
//		arrPurchaseOrderLineItemData[nIndex].m_nTaxPrice = (nDiscPrice * (arrPurchaseOrderLineItemData[nIndex].m_nTax/100));
//		arrPurchaseOrderLineItemData[nIndex].m_nAmount = ((nDiscPrice + arrPurchaseOrderLineItemData[nIndex].m_nTaxPrice) * arrPurchaseOrderLineItemData[nIndex].m_nQuantity).toFixed(2);
//		nTotal += Number (arrPurchaseOrderLineItemData[nIndex].m_nAmount);
//		arrPurchaseOrderLineItemData[nIndex].m_strArticleNumber = arrPurchaseOrderLineItemData[nIndex].m_oItemData.m_strArticleNumber;
//		arrPurchaseOrderLineItemData[nIndex].m_strDesc = arrPurchaseOrderLineItemData[nIndex].m_oItemData.m_strItemName + " | " + arrPurchaseOrderLineItemData[nIndex].m_oItemData.m_strDetail;
//	
//	}
	$('#vendorPurchaseOrderDetails_table_purchaseOrderDetailsDG').datagrid('reloadFooter',[{m_nInvoiceQty:'<b>Total</b>', m_nAmount: nTotal.toFixed(2)}]);
}


function vendorPurchaseOrderList_initializeDetailsDG ()
{
	$('#vendorPurchaseOrderDetails_table_purchaseOrderDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:70},
				{field:'m_strDesc',title:'Item Name',sortable:true,width:250},
				{field:'m_nQuantity',title:'Qty',sortable:true,width:50,align:'right',
					formatter:function (value,row,index)
					{
						var nQty = row.m_nQuantity;
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
				{field:'m_nReceiveQty',title:'Receive Qty',sortable:true,width:70,align:'right',
					formatter:function (value,row,index)
					{
						var nReceiveQty = row.m_nReceiveQty;
						try
						{
							if (!isNaN(value))
								nReceiveQty = nReceiveQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nReceiveQty;
					}	
				},
				{field:'m_nReceivedQty',title:'Received Qty',sortable:true,width:80,align:'right',
					formatter:function (value,row,index)
					{
						var nReceivedQty = row.m_nReceivedQty;
						try
						{
							if (!isNaN(value))
								nReceivedQty = nReceivedQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nReceivedQty;
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
	
	$('#vendorPurchaseOrderDetails_table_purchaseListDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:150},
				{field:'m_strDate',title:'Date',sortable:true,width:150},
				{field:'m_nTotalAmount',title:'Amount',width:150, align:'right',
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
	
	$('#vendorPurchaseOrderDetails_table_purchaseListDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="vendorPurchaseOrderDetails_table_purchaseListdetailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  vendorPurchaseOrderDetails_table_purchaseListdetailViewDG = $(this).datagrid('getRowDetail',index).find('table.vendorPurchaseOrderDetails_table_purchaseListdetailViewDG');
	        vendorPurchaseOrderDetails_table_purchaseListdetailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
	            columns:[[
					{field:'m_strArticleNo',title:'Article#', width:120},
					{field:'m_strName',title:'Name',width:210},
					{field:'m_strDetail',title:'Detail',width:80},
					{field:'m_nQuantity',title:'Qty',align:'right', width:60},
					{field:'m_nPrice',title:'Price',align:'right',width:80,
						formatter:function(value,row,index)
			        	{
							var nIndianFormat = formatNumber (value.toFixed(2),row,index);
							return '<span class="rupeeSign">R  </span>' + nIndianFormat;
			        	},
			        	styler: function(value,row,index)
				  		{
				  			return {class:'DGcolumn'};
				  		}		
					},
					{field:'m_nDiscount',title:'Disc(%)',width:50,align:'right'},
					{field:'m_nExcise',title:'Excise(%)',width:60,align:'right'},
					{field:'m_nTax',title:'Tax(%)',width:45,align:'right'},
					{field:'m_nOtherCharges',title:'Other Chgs(%)',width:85,align:'right'},
					{field:'m_nAmount',title:'Amount',width:90,align:'right',
						formatter:function(value,row,index)
			        	{
							var nIndianFormat = formatNumber (value,row,index);
							return '<span class="rupeeSign">R  </span>' + nIndianFormat;
			        	},
			        	styler: function(value,row,index)
				  		{
				  			return {class:'DGcolumn'};
				  		}		
					},
	            ]],
	            onResize:function()
	            {
	                $('#vendorPurchaseOrderDetails_table_purchaseListDG').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        vendorPurchaseOrderList_showDetails (vendorPurchaseOrderDetails_table_purchaseListdetailViewDG, index, row);
	    }
	});
}

function vendorPurchaseOrderList_showDetails (strSubGridId, index, row)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_nId = row.m_nId;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.get (oPurchaseData, 
			function (oResponse)
			{
				vendorPurchaseOrderList_gotPurchaseData(strSubGridId, index, oResponse);
			}
	);
}

function vendorPurchaseOrderList_gotPurchaseData (strSubGridId, index, oResponse)
{
	assert.isString(strSubGridId, "strSubGridId expected to be a string.");
	var arrPurchase = oResponse.m_arrPurchase[0];
	var nTotal = 0;
	var arrOrderedLineItem = getOrderedLineItems (arrPurchase.m_oPurchaseLineItems);
	for (var nIndex = 0; nIndex < arrPurchase.m_oPurchaseLineItems.length; nIndex++)
	{
		arrOrderedLineItem[nIndex].m_strArticleNo = arrOrderedLineItem[nIndex].m_oItemData.m_strArticleNumber;
		arrOrderedLineItem[nIndex].m_strName = arrOrderedLineItem[nIndex].m_oItemData.m_strItemName;
		arrOrderedLineItem[nIndex].m_strDetail = arrOrderedLineItem[nIndex].m_oItemData.m_strDetail;
		arrOrderedLineItem[nIndex].m_nAmount =  vendorPurchaseOrderList_getAmount(arrOrderedLineItem[nIndex]);
	}
	strSubGridId.datagrid('loadData',arrOrderedLineItem);
	$('#vendorPurchaseOrderDetails_table_purchaseListDG').datagrid('fixDetailRowHeight', index);
}

function vendorPurchaseOrderList_getAmount (oPurchaseOrderLineItem)
{
	assert.isObject(oPurchaseOrderLineItem, "oPurchaseOrderLineItem expected to be an Object.");
	assert( Object.keys(oPurchaseOrderLineItem).length >0 , "oPurchaseOrderLineItem cannot be an empty .");// checks for non emptyness 
	var nAmount = 0;
	nAmount =  oPurchaseOrderLineItem.m_nQuantity * oPurchaseOrderLineItem.m_nPrice ;
	nAmount -= nAmount *(oPurchaseOrderLineItem.m_nDiscount/100);
	nAmount += nAmount *(oPurchaseOrderLineItem.m_nTax/100);
	return nAmount.toFixed(2);
}

function vendorPurchaseOrderInvoice_cancel ()
{
	HideDialog ("dialog");
}

function vendorPurchaseOrderList_purchaseInvoiceList (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oVendorPurchaseOrderData = new VendorPurchaseOrderData ();
	oPurchaseData.m_oUserCredentialsData = new UserInformationData (); 
	oPurchaseData.m_oVendorPurchaseOrderData.m_nPurchaseOrderId = oRowData.m_nPurchaseOrderId
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.list(oPurchaseData, "", "", "", "", vendorPurchaseOrderList_purchaseInvoiceListed);
}

function vendorPurchaseOrderList_purchaseInvoiceListed (oResponse)
{
	$('#vendorPurchaseOrderDetails_table_purchaseListDG').datagrid('loadData', oResponse.m_arrPurchase);
}

function vendorPurchaseOrderList_list  (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oVendorPurchaseOrderListMemberData.m_strSortColumn = strColumn;
	m_oVendorPurchaseOrderListMemberData.m_strSortOrder = strOrder;
	m_oVendorPurchaseOrderListMemberData.m_nPageNumber = nPageNumber;
	m_oVendorPurchaseOrderListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "vendorPurchaseOrderList_progressbarLoaded ()");
	
}

function vendorPurchaseOrderList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oVendorPurchaseOrderData = vendorPurchaseOrderList_getFormData ();
	oVendorPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	VendorPurchaseOrderDataProcessor.list(oVendorPurchaseOrderData, m_oVendorPurchaseOrderListMemberData.m_strSortColumn, m_oVendorPurchaseOrderListMemberData.m_strSortOrder, m_oVendorPurchaseOrderListMemberData.m_nPageNumber, m_oVendorPurchaseOrderListMemberData.m_nPageSize, vendorPurchaseOrderList_listed);
}

function vendorPurchaseOrderList_getFormData () 
{
	var oVendorPurchaseOrderData = new VendorPurchaseOrderData ();
	oVendorPurchaseOrderData.m_oVendorData.m_strCompanyName = $("#vendorPurchaseOrderList_input_vendorName").val ();
	oVendorPurchaseOrderData.m_strPurchaseOrderNumber = $("#vendorPurchaseOrderList_input_purchaseOrderNumber").val ();
	oVendorPurchaseOrderData.m_strFromDate = FormatDate ($('#vendorPurchaseOrderList_input_fromDate').datebox('getValue'));
	oVendorPurchaseOrderData.m_strToDate = FormatDate ($('#vendorPurchaseOrderList_input_toDate').datebox('getValue'));
	oVendorPurchaseOrderData.m_nVendorPurchaseOrderStatus = m_oVendorPurchaseOrderListMemberData.m_bIsPOStatusPending ? "kPending" : "kDelivered";
	return oVendorPurchaseOrderData;
}

function vendorPurchaseOrderList_listed (oResponse)
{
	document.getElementById("vendorPurchaseOrderList_div_listDetail").innerHTML = "";
	clearGridData (m_oVendorPurchaseOrderListMemberData.m_strDGId);
	$(m_oVendorPurchaseOrderListMemberData.m_strDGId).datagrid('loadData',oResponse.m_arrVendorPurchaseOrderData);
	var strDataGridId = m_oVendorPurchaseOrderListMemberData.m_bIsPOStatusPending == true ? ("#vendorPurchaseOrderList_table_purchaseOrderPendingListDG") : ("#vendorPurchaseOrderList_table_purchaseOrderCompletedListDG");
	$(strDataGridId).datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oVendorPurchaseOrderListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function vendorPurchaseOrderList_cancel ()
{
	HideDialog("dialog");
}

function vendorPurchaseOrderList_showAddPopup ()
{
	navigate ("vendorPurchaseOrderNew", "widgets/vendorpurchaseorder/vendorPurchaseOrderNew.js");
}

function vendorPurchaseOrderList_showPurchaseOrderPopup (nPurchaseOrderId)
{
	assert.isNumber(nPurchaseOrderId, "nPurchaseOrderId expected to be a Number.");
	m_oVendorPurchaseOrderListMemberData.m_nSelectedPurchaseOrderId = nPurchaseOrderId;
	navigate ("vendorPurchaseOrderEdit", "widgets/vendorpurchaseorder/vendorPurchaseOrderEdit.js");
}

function vendorPurchaseOrder_handleAfterSave ()
{
	document.getElementById("vendorPurchaseOrderList_div_listDetail").innerHTML = "";
	m_oVendorPurchaseOrderListMemberData.m_bIsPOStatusPending ? clearGridData ("#vendorPurchaseOrderList_table_purchaseOrderPendingListDG") : clearGridData ("#vendorPurchaseOrderList_table_purchaseOrderCompletedListDG");
	vendorPurchaseOrderList_list (m_oVendorPurchaseOrderListMemberData.m_strSortColumn, m_oVendorPurchaseOrderListMemberData.m_strSortOrder,1, 10);
}

function vendorPurchaseOrderList_print ()
{
	navigate ('printVendorPurchaseOrder','widgets/vendorpurchaseorder/vendorPurchaseOrderListPrint.js');
}