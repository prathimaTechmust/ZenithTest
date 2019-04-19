var purchaseAdd_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
];

includeDataObjects (purchaseAdd_includeDataObjects, "purchaseAdd_loaded ()");

function purchaseAdd_loaded ()
{
	loadPage ("inventorymanagement/paymentsandreceipt/purchaseAdd.html", "payment_addPurchases_dialog", "purchaseAdd_init ()");
}

function purchaseAdd_memberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize = 20;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "asc";
    this.m_arrSelectedData = new Array ();
}

var m_oPurchaseAddMemberData = new purchaseAdd_memberData ();

function purchaseAdd_init ()
{
	var arrInvoiceData = $('#payment_table_purchaseDG').datagrid('getRows');
	m_oPurchaseAddMemberData.m_arrSelectedData = arrInvoiceData;
	createPopup ("payment_addPurchases_dialog", "#purchaseAdd_button_add", "#purchaseAdd_button_cancel", true);
	purchaseAdd_initDatagrid ();
}

function purchaseAdd_initDatagrid ()
{
	$('#purchaseAdd_table_invoiceDG').datagrid ({
	    columns:
	    [[  
	        {field:'ckBox',checkbox:true},
	        {field:'m_strDate',title:'Date',sortable:true,width:50},
	        {field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:150},
	        {field:'m_nTotalAmount',title:'Invoiced',sortable:true,width:100,align:'right',
	        	formatter:function(value,row,index)
	        	{
					var nIndianFormat = formatNumber (value.toFixed(2),row,index);
					return '<span class="rupeeSign">R  </span>' + nIndianFormat;
	        	}
	        },
	        {field:'m_nPaymentAmount',title:'Paid',sortable:true,width:100,align:'right',
	        	formatter:function(value,row,index)
	        	{
					var nIndianFormat = formatNumber (value.toFixed(2),row,index);
					return '<span class="rupeeSign">R  </span>' + nIndianFormat;
	        	}
	        },
	        {field:'m_nBalanceAmount',title:'Balance',sortable:true,width:100,align:'right',
	        	formatter:function(value,row,index)
	        	{
	        		var nBalanceAmount = row.m_nTotalAmount - row.m_nPaymentAmount;
					var nIndianFormat = formatNumber (nBalanceAmount.toFixed(2));
					return '<span class="rupeeSign">R  </span>' + nIndianFormat;
	        	}
	        }
	    ]],
	    onSortColumn: function (strColumn, strOrder)
		{
			strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
			m_oPurchaseAddMemberData.m_strSortColumn = strColumn;
			m_oPurchaseAddMemberData.m_strSortOrder = strOrder;
			purchaseAdd_buildInvoiceList (m_oPurchaseAddMemberData.m_strSortColumn,m_oPurchaseAddMemberData.m_strSortOrder, 1, 20);
		},
	    onCheck: function (rowIndex, rowData)
		{
			purchaseAdd_holdCheckedData(rowData, true);
		},
		onUncheck: function (rowIndex, rowData)
		{
			purchaseAdd_holdCheckedData (rowData, false);
		},
		onCheckAll: function (rows)
		{
			purchaseAdd_holdAllCheckedData (rows);
		},
		onUncheckAll: function (rows)
		{
			purchaseAdd_holdAllUnCheckedData (rows);
		}
	});
	purchaseAdd_initDGPagination ();
	purchaseAdd_buildInvoiceList (m_oPurchaseAddMemberData.m_strSortColumn,m_oPurchaseAddMemberData.m_strSortOrder, m_oPurchaseAddMemberData.m_nPageNumber, m_oPurchaseAddMemberData.m_nPageSize);
}

function purchaseAdd_initDGPagination ()
{
	$('#purchaseAdd_table_invoiceDG').datagrid('getPager').pagination
	(
		{ 
			pageSize : 20,
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oPurchaseAddMemberData.m_nPageNumber = $('#purchaseAdd_table_invoiceDG').datagrid('getPager').pagination('options').pageNumber;
				purchaseAdd_buildInvoiceList (m_oPurchaseAddMemberData.m_strSortColumn, m_oPurchaseAddMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oPurchaseAddMemberData.m_nPageNumber = $('#purchaseAdd_table_invoiceDG').datagrid('getPager').pagination('options').pageNumber;
				m_oPurchaseAddMemberData.m_nPageSize = $('#purchaseAdd_table_invoiceDG').datagrid('getPager').pagination('options').pageSize;
				purchaseAdd_buildInvoiceList (m_oPurchaseAddMemberData.m_strSortColumn, m_oPurchaseAddMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function purchaseAdd_getFormData ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oVendorData = new VendorData ();
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oPurchaseData.m_oVendorData.m_nClientId = $('#payment_input_vendor').datebox('getValue');
	oPurchaseData.m_strInvoiceNo = $("#purchaseAdd_input_invoiceNumber").val();
	oPurchaseData.m_bIsForPayment = true;
	return oPurchaseData;
}

function purchaseAdd_buildInvoiceList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oPurchaseData = purchaseAdd_getFormData ();
	PurchaseDataProcessor.list(oPurchaseData, strColumn, strOrder, nPageNumber, nPageSize, purchaseAdd_gotInvoiceList);
}

function purchaseAdd_gotInvoiceList (oResponse)
{
	clearGridData ("#purchaseAdd_table_invoiceDG");
	$('#purchaseAdd_table_invoiceDG').datagrid('loadData',oResponse.m_arrPurchase);
	$('#purchaseAdd_table_invoiceDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oPurchaseAddMemberData.m_nPageNumber});
	purchaseAdd_checkDGRow ();
}

function purchaseAdd_cancel ()
{
	HideDialog("payment_addPurchases_dialog");
}

function purchaseAdd_addPurchases ()
{
	if(m_oPurchaseAddMemberData.m_arrSelectedData.length >0)
		$('#payment_table_purchaseDG').datagrid('loadData', m_oPurchaseAddMemberData.m_arrSelectedData);
	else
	    alert("please select the item")	
		HideDialog ("payment_addPurchases_dialog");
		payment_updateInvoiceDG ();
}

function purchaseAdd_filter ()
{
	purchaseAdd_buildInvoiceList (m_oPurchaseAddMemberData.m_strSortColumn, m_oPurchaseAddMemberData.m_strSortOrder,1, 20);
}

function purchaseAdd_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	if(bIsForAdd)
	{
		if(!purchaseAdd_isRowAdded (oRowData))
			m_oPurchaseAddMemberData.m_arrSelectedData.push(purchaseAdd_getData (oRowData));
	}
	else
		purchaseAdd_remove (oRowData);
}

function purchaseAdd_getData (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var oPurchaseData = oRowData;
	for(var nIndex=0; nIndex < m_oPaymentMemberData.m_arrDeletedLineItems.length; nIndex++)
	{
		if (m_oPaymentMemberData.m_arrDeletedLineItems[nIndex].m_nId == oRowData.m_nId)
		{
			oPurchaseData = m_oPaymentMemberData.m_arrDeletedLineItems[nIndex];
			break;
		}
	}
	return oPurchaseData;
}

function purchaseAdd_remove (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	for (var nIndex = 0; nIndex < m_oPurchaseAddMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oPurchaseAddMemberData.m_arrSelectedData[nIndex].m_nId == oRowData.m_nId)
		{
			m_oPurchaseAddMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function purchaseAdd_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oPurchaseAddMemberData.m_arrSelectedData.length; nIndex++)
		bIsRowAdded = (m_oPurchaseAddMemberData.m_arrSelectedData [nIndex].m_nId == oRowData.m_nId);
	return bIsRowAdded;
}

function purchaseAdd_checkDGRow ()
{
	var arrPurchaseData = $('#purchaseAdd_table_invoiceDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrPurchaseData.length; nIndex++)
	{
		if(purchaseAdd_isRowSelectable(arrPurchaseData[nIndex].m_nId))
			$("#purchaseAdd_table_invoiceDG").datagrid('checkRow', nIndex);
	}
}

function purchaseAdd_isRowSelectable (nPurchaseId)
{
	assert.isNumber(nPurchaseId, "nPurchaseId expected to be a Number.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oPurchaseAddMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oPurchaseAddMemberData.m_arrSelectedData[nIndex].m_nId == nPurchaseId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function purchaseAdd_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows expected to be an Array.");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		purchaseAdd_holdCheckedData(arrRows[nIndex], true);
}

function purchaseAdd_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows expected to be an Array.");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		purchaseAdd_holdCheckedData(arrRows[nIndex], false);
}