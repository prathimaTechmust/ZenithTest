var purchaseList_includeDataObjects = 
[
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
		'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/inventorymanagement/paymentsandreceipt/PurchasePaymentData.js'
];



 includeDataObjects (purchaseList_includeDataObjects, "purchaseList_loaded()");

function purchaseList_init ()
{
	$("#filterPurchase_input_fromDate").datebox();
	$("#filterPurchase_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filterPurchase_input_toDate").datebox();
	$("#filterPurchase_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	purchaseList_initializeDataGrid ();
	purchaseList_list  (m_oPurchaseListMemberData.m_strSortColumn, m_oPurchaseListMemberData.m_strSortOrder, 1, 10);
}

function purchaseList_memberData ()
{
	this.m_nSelectedPurchaseId = -1;
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "purchaseList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
}

var m_oPurchaseListMemberData = new purchaseList_memberData ();

function purchaseList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function purchaseList_initEdit ()
{
	m_oPurchaseListMemberData.m_strActionItemsFunction = "purchaseList_addActions (row, index)";
	document.getElementById ("purchaseList_button_add").style.visibility="visible";
	purchaseList_init ();
}

function purchaseList_addActions (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	if(row.m_nBalanceAmount > 0)
	{
		var oImage = 	'<table align="center">'+
							'<tr>'+
								'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="purchaseList_edit ('+row.m_nId+')"/> </td>'+
								'<td> <img title="Make Payment" src="images/payment.png" width="20" align="center" id="paymentImageId" onClick="purchaseList_makePayment ('+row.m_nId+')"/> </td>'+
								'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="purchaseList_delete ('+index+')"/> </td>'+
							'</tr>'+
						'</table>'
		return oImage;
	}
	else
	{
		var oImage = 	'<table align="center">'+
		'<tr>'+
			'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="purchaseList_edit ('+row.m_nId+')"/> </td>'+
			'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="purchaseList_delete ('+index+')"/> </td>'+
		'</tr>'+
		'</table>'
		return oImage;
	}
}

function purchaseList_delete ()
{
	
}

function purchaseList_initializeDataGrid ()
{
	initHorizontalSplitter("#purchaseList_div_horizontalSplitter", "#purchaseList_table_purchaseListDG");
	$('#purchaseList_table_purchaseListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strFrom',title:'From',sortable:true,width:250,
			  		styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}	
			  	},
			  	{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:150},
				{field:'m_strDate',title:'Date',sortable:true,width:80},
				{field:'Actions',title:'Action',width:50,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return purchaseList_displayImages (row, index);
		        	}
				}
			]]
		}
	);
	$('#purchaseList_table_purchaseListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				purchaseList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oPurchaseListMemberData.m_strSortColumn = strColumn;
				m_oPurchaseListMemberData.m_strSortOrder = strOrder;
				purchaseList_list (strColumn, strOrder, m_oPurchaseListMemberData.m_nPageNumber, m_oPurchaseListMemberData.m_nPageSize);
			}
		}
	)
	purchaseList_initDGPagination ();
}

function purchaseList_initDGPagination ()
{
	$('#purchaseList_table_purchaseListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oPurchaseListMemberData.m_nPageNumber = nPageNumber;
				purchaseList_list (m_oPurchaseListMemberData.m_strSortColumn, m_oPurchaseListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("purchaseList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oPurchaseListMemberData.m_nPageNumber = nPageNumber;
				m_oPurchaseListMemberData.m_nPageSize = nPageSize;
				purchaseList_list (m_oPurchaseListMemberData.m_strSortColumn, m_oPurchaseListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("purchaseList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function purchaseList_filter ()
{
	purchaseList_list  (m_oPurchaseListMemberData.m_strSortColumn, m_oPurchaseListMemberData.m_strSortOrder, 1, 10);
}

function purchaseList_displayImages (row, index)
{
	var oImage = eval (m_oPurchaseListMemberData.m_strActionItemsFunction);
	return oImage;
}

function purchaseList_edit (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	m_oPurchaseListMemberData.m_nSelectedPurchaseId = nId;
	loadPage ("include/process.html", "ProcessDialog", "purchaseList_edit_progressbarLoaded ()");
}

function purchaseList_edit_progressbarLoaded ()
{
	navigate ("purchase", "widgets/inventorymanagement/purchase/editPurchase.js");
}

function purchaseList_selectedRowData (rowData, rowIndex)
{
	purchaseList_showPurchaseDetails (rowData, "purchaseList_div_listDetail");
}

function purchaseList_showPurchaseDetails (oRowData, strDivId)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isString(strDivId, "strDivId expected to be a string.");
	document.getElementById(strDivId).innerHTML = "";
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_nId = oRowData.m_nId;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.getXML (oPurchaseData, function (strXMLData)
		{
			populateXMLData (strXMLData, "inventorymanagement/purchase/PurchaseDetails.xslt", strDivId);
			purchaseList_initializeDetailsDG ();
			PurchaseDataProcessor.get (oPurchaseData, purchaseList_gotPurchaseLineItemData)
			var oPurchasePaymentData = new PurchasePaymentData;
			oPurchasePaymentData.m_oPurchaseData = oPurchaseData;
			PurchasePaymentDataProcessor.list(oPurchasePaymentData, "", "", purchaseList_PurchasePaymentListed)
		});
}

function purchaseList_gotPurchaseLineItemData (oResponse)
{
	var arrPurchaseData = oResponse.m_arrPurchase;
	var arrPurchaseLineItemData = getOrderedLineItems (arrPurchaseData [0].m_oPurchaseLineItems);
	var nTotal = 0;
	$('#purchaseDetails_table_purchaseDetailsDG').datagrid('loadData',arrPurchaseLineItemData);
	$('#purchaseDetails_table_purchaseDetailsDG').datagrid('reloadFooter',[{m_nOtherCharges:'<b>Total</b>', m_nAmount: nTotal.toFixed(2)}]);
}

function purchaseList_getAmount (oPurchaseOrderLineItem)
{
	assert.isObject(oPurchaseOrderLineItem, "oPurchaseOrderLineItem expected to be an Object.");
	assert( Object.keys(oPurchaseOrderLineItem).length >0 , "oPurchaseOrderLineItem cannot be an empty .");// checks for non emptyness 
	var nAmount = 0;
	nAmount =  oPurchaseOrderLineItem.m_nQuantity * oPurchaseOrderLineItem.m_nPrice ;
	nAmount -= nAmount *(oPurchaseOrderLineItem.m_nDiscount/100);
	nAmount += nAmount *(oPurchaseOrderLineItem.m_nExcise/100);
	nAmount += nAmount *(oPurchaseOrderLineItem.m_nTax/100);
	nAmount += nAmount *(oPurchaseOrderLineItem.m_nOtherCharges/100);
	return nAmount.toFixed(2);
}

function purchaseList_initializeDetailsDG ()
{
	$('#purchaseDetails_table_purchaseDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:110},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:220},
				{field:'m_nQuantity',title:'Quantity',sortable:true,width:70,align:'right'},
				{field:'m_nPrice',title:'Price',sortable:true,width:90,align:'right',
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
				{field:'m_nDiscount',title:'Disc(%)',width:60,align:'right'},
		        {field:'m_nExcise',title:'Excise(%)',width:70,align:'right'},
		        {field:'m_nTax',title:'Tax(%)',width:60,align:'right'},
		        {field:'m_nOtherCharges',title:'Other Chgs(%)',width:95,align:'right'},
				{field:'m_nAmount',title:'Amount',width:100,align:'right',
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
	
	$('#purchaseDetails_table_paymentDetailsDG').datagrid
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

function purchaseList_list  (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oPurchaseListMemberData.m_strSortColumn = strColumn;
	m_oPurchaseListMemberData.m_strSortOrder = strOrder;
	m_oPurchaseListMemberData.m_nPageNumber = nPageNumber;
	m_oPurchaseListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "purchaseList_progressbarLoaded ()");
}

function purchaseList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oPurchaseData = purchaseList_getFormData ();
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.list(oPurchaseData, m_oPurchaseListMemberData.m_strSortColumn, m_oPurchaseListMemberData.m_strSortOrder, m_oPurchaseListMemberData.m_nPageNumber, m_oPurchaseListMemberData.m_nPageSize, purchaseList_listed);
}

function purchaseList_listed (oResponse)
{
	clearGridData ("#purchaseList_table_purchaseListDG");
	$('#purchaseList_table_purchaseListDG').datagrid('loadData', oResponse.m_arrPurchase);
	$('#purchaseList_table_purchaseListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oPurchaseListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function purchaseList_getFormData () 
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_strFrom = $("#filterPurchase_input_from").val();
	oPurchaseData.m_strInvoiceNo = $("#filterPurchase_input_invoiceNo").val();
	oPurchaseData.m_strFromDate =FormatDate ($("#filterPurchase_input_fromDate").datebox('getValue'));
	oPurchaseData.m_strToDate = FormatDate ($("#filterPurchase_input_toDate").datebox('getValue'));
	oPurchaseData.m_nPurchaseType = "kPurchase";
	return oPurchaseData;
}

function purchaseList_cancel ()
{
	HideDialog("dialog");
}

function purchaseList_showFilterPopup ()
{
	loadPage ("inventorymanagement/purchase/filterPurchase.html", "dialog", "purchaseList_filterInit ()");
}

function purchaseList_filterInit ()
{
	createPopup ('dialog', '#filterPurchase_button_cancel', '#filterPurchase_button_create', true);
}

function filterPurchase_cancel ()
{
	HideDialog("dialog");
}

function purchaseList_showAddPopup ()
{
	navigate ("newPurchase", "widgets/inventorymanagement/purchase/purchaseAdmin.js");
}

function purchaseList_PurchasePaymentListed (oResponse)
{
	$('#purchaseDetails_table_paymentDetailsDG').datagrid('loadData', oResponse.m_arrPurchasePaymentData);
	var nTotal = 0;
	$('#purchaseDetails_table_paymentDetailsDG').datagrid('reloadFooter',[{m_strDate:'<b style="text-align:right">Total</b>',m_nAmount: nTotal.toFixed(2)}]);
}

function purchaseList_makePayment (nPurchaseId)
{
	assert.isNumber(nPurchaseId, "nPurchaseId expected to be a Number.");
	m_oPurchaseListMemberData.m_nSelectedPurchaseId = nPurchaseId;
	navigate ("", "widgets/inventorymanagement/paymentsandreceipt/paymentForPurchase.js");
}

function payment_handleAfterSave ()
{
	document.getElementById("purchaseList_div_listDetail").innerHTML = "";
	purchaseList_list  (m_oPurchaseListMemberData.m_strSortColumn, m_oPurchaseListMemberData.m_strSortOrder, 1, 10);
}

