var invoiceList_includeDataObjects = 
[
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/InvoiceReceiptData.js',
	'widgets/inventorymanagement/paymentsandreceipt/ReceiptData.js'
];

includeDataObjects (invoiceList_includeDataObjects, "invoiceList_loaded()");

function invoiceList_memberData ()
{
	this.m_nIndex = -1;
	this.m_strXMLData = "";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_oRowData = null;
    this.m_nInvoiceAmount = 0;
    this.m_nReceivedAmount = 0;
    this.m_nOutstanding = 0;
    this.m_nInvoiceId = -1;
}

var m_oInvoiceListMemberData = new invoiceList_memberData ();

function invoiceList_loaded ()
{
	loadPage ("inventorymanagement/invoice/invoiceList.html", "workarea", "invoiceList_init ()");
}

function invoiceList_init ()
{
	$("#filterInvoice_input_fromDate").datebox();
	$("#filterInvoice_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filterInvoice_input_toDate").datebox();
	$("#filterInvoice_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	invoiceList_initializeDataGrid ();
}

function invoiceList_initializeDataGrid ()
{
	initHorizontalSplitter("#invoiceList_div_horizontalSplitter", "#invoiceList_table_invoiceListDG");
	$('#invoiceList_table_invoiceListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'To',sortable:true,width:250,
			  		formatter:function(value,row,index)
		        	{
		        		return row.m_oClientData.m_strCompanyName;
		        	},
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
			  	},
				{field:'m_strInvoiceNumber',title:'Invoice No.',sortable:true,width:120},
				{field:'m_strDate',title:'Date',sortable:true,width:80},
				{field:'Actions',title:'Action',width:50,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return invoiceList_displayImages (row, index);
		        	}
				},
			]],
			onSelect: function (rowIndex, rowData)
			{
				invoiceList_selectedRowData (rowData);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oInvoiceListMemberData.m_strSortColumn = strColumn;
				m_oInvoiceListMemberData.m_strSortOrder = strOrder;
				invoiceList_list (strColumn, strOrder, m_oInvoiceListMemberData.m_nPageNumber, m_oInvoiceListMemberData.m_nPageSize);
			}
		}
	);
	invoiceList_initDGPagination ();
	invoiceList_list (m_oInvoiceListMemberData.m_strSortColumn, m_oInvoiceListMemberData.m_strSortOrder, 1, 10);
}

function invoiceList_initDGPagination ()
{
	$('#invoiceList_table_invoiceListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oInvoiceListMemberData.m_nPageNumber = $('#invoiceList_table_invoiceListDG').datagrid('getPager').pagination('options').pageNumber;
				invoiceList_list (m_oInvoiceListMemberData.m_strSortColumn, m_oInvoiceListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("invoiceList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oInvoiceListMemberData.m_nPageNumber = $('#invoiceList_table_invoiceListDG').datagrid('getPager').pagination('options').pageNumber;
				m_oInvoiceListMemberData.m_nPageSize = $('#invoiceList_table_invoiceListDG').datagrid('getPager').pagination('options').pageSize;
				invoiceList_list (m_oInvoiceListMemberData.m_strSortColumn, m_oInvoiceListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("invoiceList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function invoiceList_filter ()
{
	invoiceList_list (m_oInvoiceListMemberData.m_strSortColumn, m_oInvoiceListMemberData.m_strSortOrder, 1, 10);
}

function invoiceList_getFormData ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oUserCredentialsData = new UserInformationData ();
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oInvoiceData.m_oClientData.m_strCompanyName = $("#filterInvoice_input_to").val();
	oInvoiceData.m_strInvoiceNumber =$("#filterInvoice_input_invoiceNumber").val();
	oInvoiceData.m_strRemarks = $("#filterInvoice_input_remarks").val();
	oInvoiceData.m_strFromDate =FormatDate ($("#filterInvoice_input_fromDate").datebox('getValue'));
	oInvoiceData.m_strToDate = FormatDate ($("#filterInvoice_input_toDate").datebox('getValue'));
	return oInvoiceData;
}

function invoiceList_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	if(row.m_nBalanceAmount > 0)
	{
		var oImage = '<table align="center">'+
						'<tr>'+
							'<td> <img title="Print" src="images/print.jpg" onClick="invoiceList_print ('+row.m_nInvoiceId+')"/> </td>'+
							'<td> <img title="Add Remarks" src="images/remarks.jpg" onClick="invoiceList_AddRemarks ()"/> </td>'+
							'<td> <img title="Make Receipt" src="images/receipt.png" onClick="invoiceList_MakeReceipt ('+row.m_nInvoiceId+')"/> </td>'
						'</tr>'+
					'</table>'
		return oImage;
	}
	else
	{
		var oImage = '<table align="center">'+
						'<tr>'+
							'<td> <img title="Print" src="images/print.jpg" onClick="invoiceList_print ('+row.m_nInvoiceId+')"/> </td>'+
							'<td> <img title="Add Remarks" src="images/remarks.jpg" onClick="invoiceList_AddRemarks ()"/> </td>'+
						'</tr>'+
					'</table>'
		return oImage;
	}
}

function invoiceList_AddRemarks ()
{
	navigate ('invoice','widgets/inventorymanagement/invoice/remarksInvoiceList.js');
}

function invoiceList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oInvoiceListMemberData.m_strSortColumn = strColumn;
	m_oInvoiceListMemberData.m_strSortOrder = strOrder;
	m_oInvoiceListMemberData.m_nPageNumber = nPageNumber;
	m_oInvoiceListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "invoiceList_progressbarLoaded ()");
}

function invoiceList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oInvoiceData = invoiceList_getFormData ();
	InvoiceDataProcessor.list(oInvoiceData, m_oInvoiceListMemberData.m_strSortColumn, m_oInvoiceListMemberData.m_strSortOrder, m_oInvoiceListMemberData.m_nPageNumber, m_oInvoiceListMemberData.m_nPageSize, invoiceList_listed);
}

function invoiceList_listed (oResponse)
{
	clearGridData ("#invoiceList_table_invoiceListDG");
	var arrInvoice = oResponse.m_arrInvoice;
	for (var nIndex = 0; nIndex < arrInvoice.length; nIndex++)
		$('#invoiceList_table_invoiceListDG').datagrid('appendRow',arrInvoice[nIndex]);
	$('#invoiceList_table_invoiceListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oInvoiceListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function invoiceList_print ()
{
	navigate ('invoice','widgets/inventorymanagement/invoice/invoicePrint.js');
}

function invoiceList_selectedRowData (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	m_oInvoiceListMemberData.m_oRowData = oRowData;
	document.getElementById("invoiceList_div_listDetail").innerHTML = "";
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_nInvoiceId = oRowData.m_nInvoiceId;
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	InvoiceDataProcessor.getXML (oInvoiceData, function (strXMLData)
		{
			m_oInvoiceListMemberData.m_strXMLData = strXMLData;
			populateXMLData (strXMLData, "inventorymanagement/invoice/invoiceDetails.xslt", 'invoiceList_div_listDetail');
			invoiceList_initializeDetailsDG ();
			InvoiceDataProcessor.get (oInvoiceData, invoiceList_gotSalesLineItemData);
			var oInvoiceReceiptData = new InvoiceReceiptData;
			oInvoiceReceiptData.m_oInvoiceData = oInvoiceData;
			InvoiceReceiptDataProcessor.list(oInvoiceReceiptData, "", "", invoiceList_InvoiceReceiptListed)
	});
}

function invoiceList_InvoiceReceiptListed (oResponse)
{
//		server response
	
	var arrInvoiceReceiptData = oResponse.m_arrInvoiceReceiptData;
	var nTotal = 0;
	for(var nIndex = 0; nIndex< arrInvoiceReceiptData.length; nIndex++)
	{
		var oReceiptData = new ReceiptData ();
		oReceiptData.m_strModeName = arrInvoiceReceiptData[nIndex].m_oReceiptData.m_oMode.m_strModeName;
		oReceiptData.m_strDetails = arrInvoiceReceiptData[nIndex].m_oReceiptData.m_strDetails;
		oReceiptData.m_strDate = arrInvoiceReceiptData[nIndex].m_oReceiptData.m_strDate;
		oReceiptData.m_nAmount = arrInvoiceReceiptData[nIndex].m_oReceiptData.m_nAmount.toFixed(2);
		nTotal += Number (oReceiptData.m_nAmount);
		$('#invoicedetails_table_recepitDetailsDG').datagrid('appendRow', oReceiptData);
	}
	m_oInvoiceListMemberData.m_nReceivedAmount = nTotal.toFixed(2);
	$('#invoicedetails_table_recepitDetailsDG').datagrid('reloadFooter',[{m_strDate:'<b style="text-align:right">Total</b>',m_nAmount: nTotal.toFixed(2)}]);
}

function invoiceList_calculateOutstandingAmount (oInvoiceData)
{
	assert.isObject(oInvoiceData, "oInvoiceData expected to be an Object.");
	assert( Object.keys(oInvoiceData).length >0 , "oInvoiceData cannot be an empty .");// checks for non emptyness 
	var nOutstanding = (oInvoiceData.m_nInvoiceAmount - oInvoiceData.m_nReceiptAmount).toFixed(2);
	$("#invoiceDetails_span_InvoiceAmount").val(oInvoiceData.m_nInvoiceAmount.toFixed(2));
	$("#invoiceDetails_span_ReceivedAmount").val(oInvoiceData.m_nReceiptAmount.toFixed(2));
	$("#invoiceDetails_span_OutstandingAmount").val(nOutstanding);
	nOutstanding > 0 ? document.getElementById("invoiceDetails_span_OutstandingAmount").style.color = "red" : document.getElementById("invoiceDetails_span_OutstandingAmount").style.color = "green";
}

function invoiceList_initializeDetailsDG ()
{
	$('#invoicedetails_table_salesDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:50},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:100},
				{field:'m_nQuantity',title:'Quantity',sortable:true,align:'right',width:50},
				{field:'m_nPrice',title:'Price',sortable:true,align:'right',width:60,
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
		        	}	
				},
				{field:'m_nTax',title:'Tax(%)',sortable:true,align:'right',width:50},
				{field:'m_nDiscount',title:'Disc(%)',sortable:true,align:'right',width:50},
				{field:'m_nAmount',title:'Amount',width:60,align:'right',
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
	
	$('#invoicedetails_table_recepitDetailsDG').datagrid
	(
		{
			columns:
				[[
					{field:'m_strModeName',title:'Mode',sortable:true,width:100},
					{field:'m_strDetails',title:'Details',sortable:true,width:200},
					{field:'m_strDate',title:'Date',sortable:true,width:60 ,align:'right'},
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

function invoiceList_gotSalesLineItemData (oResponse)
{
	clearGridData ("#invoicedetails_table_salesDetailsDG");
	var arrInvoice = oResponse.m_arrInvoice;
	invoiceList_calculateOutstandingAmount (arrInvoice[0]);
	for (var nIndex = 0; nIndex < arrInvoice[0].m_oSalesSet.length; nIndex++)
		invoiceList_buildSalesLineItems (arrInvoice[0].m_oSalesSet[nIndex]);
	$('#invoicedetails_table_salesDetailsDG').datagrid('reloadFooter',[{m_nDiscount:'<b>Total</b>', m_nAmount: arrInvoice[0].m_nInvoiceAmount}]);
}

function invoiceList_buildSalesLineItems (oSalesData)
{
	assert.isObject(oSalesData, "oSalesData expected to be an Object.");
	assert( Object.keys(oSalesData).length >0 , "oSalesData cannot be an empty .");// checks for non emptyness 
	var arrSalesLineItemData = oSalesData.m_oSalesLineItems.concat(oSalesData.m_oNonStockSalesLineItems);
	arrSalesLineItemData = getOrderedLineItems (arrSalesLineItemData);
	for (var nIndex = 0; nIndex < arrSalesLineItemData.length; nIndex++)
	{
		try
		{
			arrSalesLineItemData[nIndex].m_strArticleNumber = arrSalesLineItemData[nIndex].m_oItemData.m_strArticleNumber;
		}
		catch (oException)
		{
			arrSalesLineItemData[nIndex].m_strArticleNumber = "";
		}
		arrSalesLineItemData[nIndex].m_strItemName = arrSalesLineItemData[nIndex].m_strArticleDescription;
		arrSalesLineItemData[nIndex].m_nQuantity = arrSalesLineItemData[nIndex].m_nQuantity.toFixed(2);
		arrSalesLineItemData[nIndex].m_nPrice = arrSalesLineItemData[nIndex].m_nPrice.toFixed(2);
		arrSalesLineItemData[nIndex].m_nDiscount = arrSalesLineItemData[nIndex].m_nDiscount.toFixed(2);
		arrSalesLineItemData[nIndex].m_nDiscountPrice = (arrSalesLineItemData[nIndex].m_nPrice * (arrSalesLineItemData[nIndex].m_nDiscount/100));
		var nDiscountedPrice = (arrSalesLineItemData[nIndex].m_nPrice - arrSalesLineItemData[nIndex].m_nDiscountPrice);
		arrSalesLineItemData[nIndex].m_nTaxPrice = (nDiscountedPrice * (arrSalesLineItemData[nIndex].m_nTax/100));
		var nPrice = (Number(arrSalesLineItemData[nIndex].m_nPrice)- Number(arrSalesLineItemData[nIndex].m_nDiscountPrice) + Number(arrSalesLineItemData[nIndex].m_nTaxPrice) );
		arrSalesLineItemData[nIndex].m_nAmount = (arrSalesLineItemData[nIndex].m_nQuantity * nPrice);
		$('#invoicedetails_table_salesDetailsDG').datagrid('appendRow',arrSalesLineItemData[nIndex]);
	}
}

function invoiceList_import ()
{
	navigate ("importInvoices", "widgets/inventorymanagement/invoice/importInvoiceDetails.js");
}

function invoiceList_export ()
{
	navigate ('exportInvoices','widgets/inventorymanagement/invoice/exportInvoiceDetails.js');
}

function invoiceList_MakeReceipt (nInvoiceId)
{
	assert.isNumber(nInvoiceId, "nInvoiceId expected to be a Number.");
	m_oInvoiceListMemberData.m_nInvoiceId = nInvoiceId;
	navigate ('makeReceipt','widgets/inventorymanagement/paymentsandreceipt/receiptForInvoice.js');
}

function receipt_handleAfterSave ()
{
	document.getElementById("invoiceList_div_listDetail").innerHTML = "";
	invoiceList_list (m_oInvoiceListMemberData.m_strSortColumn, m_oInvoiceListMemberData.m_strSortOrder, 1, 10);
}
