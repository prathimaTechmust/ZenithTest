var supplyList_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/vendormanagement/VendorData.js'
];

 includeDataObjects (supplyList_includeDataObjects, "supplyList_loaded ()");

function supplyList_memberData ()
{
	this.m_nIndex = -1;
	this.m_strActionSupplyFunction = "supplyList_addHyphen()";
	this.m_nSelectedSupplyId = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
	this.m_strSortColumn = "m_dCreatedOn";
	this.m_strSortOrder = "desc";
}

var m_oSupplyListMemberData = new supplyList_memberData ();

function supplyList_init ()
{
	$("#filterSupply_input_fromDate").datebox();
	$("#filterSupply_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filterSupply_input_toDate").datebox();
	$("#filterSupply_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	supplyList_initializeDataGrid ();
	supplyList_list  (m_oSupplyListMemberData.m_strSortColumn, m_oSupplyListMemberData.m_strSortOrder, 1, 10);
}

function supplyList_initEdit ()
{
	m_oSupplyListMemberData.m_strActionSupplyFunction = "supplyList_addActions (row, index)";
	document.getElementById ("supplyList_button_add").style.visibility="visible";
	supplyList_init ();
}

function supplyList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	if(row.m_nBalanceAmount > 0)
	{
		var oActions = 
				'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="supplyList_edit ('+row.m_nId+')"/> </td>'+
							'<td> <img title="Make Payment" src="images/payment.png" width="20" align="center" id="paymentImageId" onClick="supplyList_makePayment ('+row.m_nId+')"/> </td>'+
						'</tr>'+
					'</table>'
		return oActions;
	}
	else
	{
		var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="supplyList_edit ('+row.m_nId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
	}
}

function supplyList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function supplyList_initializeDataGrid ()
{
	initHorizontalSplitter("#supplyList_div_horizontalSplitter", "#supplyList_table_supplyListDG");
	$('#supplyList_table_supplyListDG').datagrid
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
		        		return supplyList_displayImages (row, index);
		        	}
				},
			]],
			onSelect: function (rowIndex, rowData)
			{
				supplyList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oSupplyListMemberData.m_strSortColumn = strColumn;
				m_oSupplyListMemberData.m_strSortOrder = strOrder;
				supplyList_list (strColumn, strOrder, m_oSupplyListMemberData.m_nPageNumber, m_oSupplyListMemberData.m_nPageSize);
			}
		}
	);
	supplyList_initDGPagination ();
}

function supplyList_initDGPagination ()
{
	$('#supplyList_table_supplyListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oSupplyListMemberData.m_nPageNumber = nPageNumber;
				supplyList_list (m_oSupplyListMemberData.m_strSortColumn, m_oSupplyListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oSupplyListMemberData.m_nPageNumber = nPageNumber;
				m_oSupplyListMemberData.m_nPageSize = nPageSize;
				supplyList_list (m_oSupplyListMemberData.m_strSortColumn, m_oSupplyListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function supplyList_displayImages (row, index)
{
	var oImage = eval (m_oSupplyListMemberData.m_strActionSupplyFunction );
	return oImage;
}

function supplyList_selectedRowData (rowData, rowIndex)
{
	assert.isObject(rowData, "rowData expected to be an Object.");
	assert( Object.keys(rowData).length >0 , "rowData cannot be an empty .");// checks for non emptyness 
	document.getElementById("supplyList_div_listDetail").innerHTML = "";
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_nId = rowData.m_nId;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.getXML (oPurchaseData, function (strXMLData)
		{
			populateXMLData (strXMLData, "purchaseordermanagement/supply/SupplyDetails.xslt", 'supplyList_div_listDetail');
			supplyList_initializeDetailsDG ();
			PurchaseDataProcessor.get (oPurchaseData, supplyList_gotPurchaseLineItemData)
		});
}

function supplyList_gotPurchaseLineItemData (oResponse)
{
	var arrSupplyData = oResponse.m_arrPurchase;
	var arrNonStockSalesLineItemData = arrSupplyData [0].m_oNonStockPurchaseLineItems;
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrNonStockSalesLineItemData.length; nIndex++)
	{
		arrNonStockSalesLineItemData[nIndex].m_strItemName = arrNonStockSalesLineItemData[nIndex].m_strArticleDescription;
		arrNonStockSalesLineItemData[nIndex].m_nQuantity = Number(arrNonStockSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_nPrice = Number(arrNonStockSalesLineItemData[nIndex].m_nPrice).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_nAmount = supplyList_getAmount(arrNonStockSalesLineItemData[nIndex]);
		nTotal += Number(arrNonStockSalesLineItemData[nIndex].m_nAmount);
		$('#supplyDetails_table_supplyDetailsDG').datagrid('appendRow',arrNonStockSalesLineItemData[nIndex]);
	}
	$('#supplyDetails_table_supplyDetailsDG').datagrid('reloadFooter',[{m_nOtherCharges:'<b>Total</b>', m_nAmount: nTotal.toFixed(2)}]);
}

function supplyList_getAmount (oNonStockSalesLineItem)
{
	assert.isObject(oNonStockSalesLineItem, "oNonStockSalesLineItem expected to be an Object.");
	assert( Object.keys(oNonStockSalesLineItem).length >0 , "oNonStockSalesLineItem cannot be an empty .");// checks for non emptyness 
	var nAmount = 0;
	nAmount =  oNonStockSalesLineItem.m_nQuantity * oNonStockSalesLineItem.m_nPrice ;
	nAmount -= nAmount *(oNonStockSalesLineItem.m_nDiscount/100);
	nAmount += nAmount *(oNonStockSalesLineItem.m_nTax/100);
	nAmount += nAmount *(oNonStockSalesLineItem.m_nOtherCharges/100);
	return nAmount.toFixed(2);
}

function supplyList_edit (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	m_oSupplyListMemberData.m_nSelectedSupplyId = nItemId;
	navigate ("editSupply", "widgets/purchaseordermanagement/supply/editSupply.js");
}

function supplyList_getFormData ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_strFrom = $("#filterSupply_input_from").val();
	oPurchaseData.m_strInvoiceNo = $("#filterSupply_input_invoiceNo").val();
	oPurchaseData.m_strFromDate =FormatDate ($("#filterSupply_input_fromDate").datebox('getValue'));
	oPurchaseData.m_strToDate = FormatDate ($("#filterSupply_input_toDate").datebox('getValue'));
	oPurchaseData.m_nPurchaseType = "kSupply";
	return oPurchaseData;
}

function supplyList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oSupplyListMemberData.m_strSortColumn = strColumn;
	m_oSupplyListMemberData.m_strSortOrder = strOrder;
	m_oSupplyListMemberData.m_nPageNumber = nPageNumber;
	m_oSupplyListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "supplyList_progressbarLoaded ()");
}

function supplyList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oPurchaseData = supplyList_getFormData ();
	oPurchaseData.m_oUserCredentialsData = new UserInformationData ();
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.list(oPurchaseData, m_oSupplyListMemberData.m_strSortColumn, m_oSupplyListMemberData.m_strSortOrder, m_oSupplyListMemberData.m_nPageNumber, m_oSupplyListMemberData.m_nPageSize, supplyList_listed);
}

function supplyList_listed (oResponse)
{
	clearGridData ("#supplyList_table_supplyListDG");
	document.getElementById("supplyList_div_listDetail").innerHTML = "";
	$('#supplyList_table_supplyListDG').datagrid('loadData', oResponse.m_arrPurchase);
	$('#supplyList_table_supplyListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oSupplyListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function supplyList_cancel ()
{
	HideDialog("dialog");
}

function supplyList_initializeDetailsDG ()
{
	$('#supplyDetails_table_supplyDetailsDG').datagrid ({
	    columns:[[  
	        {field:'m_strArticleDescription',title:'Item Name', width:210},
	        {field:'m_nQuantity',title:'Quantity',width:70,align:'right'},
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
			{field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',
        		formatter:function(value,row,index)
	        	{
					try
					{
						 return row.m_nDiscount.toFixed(2);
					}
					catch(oException)
					{
						
					}
	        	}		
			},
	        {field:'m_nTax',title:'Tax(%)',width:60,align:'right',
        		formatter:function(value,row,index)
	        	{
					try
					{
						return row.m_nTax.toFixed(2);
					}
					catch(oException)
					{
						
					}
	        	}		
			},
	        {field:'m_nOtherCharges',title:'Other Chgs(%)',width:95,align:'right',
        		formatter:function(value,row,index)
	        	{
					var nOtherCharges = row.m_nOtherCharges;
					try
					{
						return nOtherCharges.toFixed(2);
					}
					catch(oException)
					{
						return nOtherCharges;
					}
	        	}		
	        },
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
	});
}

function supplyList_showAddPopup ()
{
	navigate ("newSupply", "widgets/purchaseordermanagement/supply/supplyAdmin.js");
}

function supplyList_filter ()
{
	supplyList_list  (m_oSupplyListMemberData.m_strSortColumn, m_oSupplyListMemberData.m_strSortOrder, 1, 10);
}

function supplyList_makePayment (nSupplyId)
{
	assert.isNumber(nSupplyId, "nSupplyId expected to be a Number.");
	m_oSupplyListMemberData.m_nSelectedSupplyId = nSupplyId;
	navigate ("", "widgets/inventorymanagement/paymentsandreceipt/paymentForSupply.js");
}

function payment_handleAfterSave ()
{
	document.getElementById("supplyList_div_listDetail").innerHTML = "";
	supplyList_list  (m_oSupplyListMemberData.m_strSortColumn, m_oSupplyListMemberData.m_strSortOrder, 1, 10);
}