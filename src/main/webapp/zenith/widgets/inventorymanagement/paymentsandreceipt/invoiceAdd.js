var invoiceAdd_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
    'widgets/inventorymanagement/invoice/InvoiceData.js',
    'widgets/clientmanagement/ClientData.js',
    'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/SiteData.js'
];

includeDataObjects (invoiceAdd_includeDataObjects, "invoiceAdd_loaded ()");

function invoiceAdd_loaded ()
{
	loadPage ("inventorymanagement/paymentsandreceipt/invoiceAdd.html", "receipt_addInvoices_dialog", "invoiceAdd_init ()");
}

function invoiceAdd_memberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize = 20;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "asc";
    this.m_arrSelectedData = new Array ();
}

var m_oInvoiceAddMemberData = new invoiceAdd_memberData ();

function invoiceAdd_init ()
{
	var arrInvoiceData = $('#receipt_table_invoiceDG').datagrid('getRows');
	m_oInvoiceAddMemberData.m_arrSelectedData = arrInvoiceData;
	createPopup ("receipt_addInvoices_dialog", "#invoiceAdd_button_add", "#invoiceAdd_button_cancel", true);
	invoiceAdd_initDatagrid ();
}

function invoiceAdd_initDatagrid ()
{
	$('#invoiceAdd_table_invoiceDG').datagrid ({
	    columns:
	    [[  
	        {field:'ckBox',checkbox:true},
	        {field:'m_strInvoiceNumber',title:'Invoice No.',sortable:true,width:80},
	        {field:'m_strDate',title:'Date',sortable:true,width:80},
	        {field:'m_nInvoiceAmount',title:'Invoice Amount',sortable:true,width:100,align:'right',
	        	formatter:function(value,row,index)
	        	{
					var nIndianFormat = formatNumber (value.toFixed(2),row,index);
					return '<span class="rupeeSign">R  </span>' + nIndianFormat;
	        	}
	        },
	        {field:'m_nReceiptAmount',title:'Total Received',sortable:true,width:100,align:'right',
	        	formatter:function(value,row,index)
	        	{
					var nIndianFormat = formatNumber (value.toFixed(2),row,index);
					return '<span class="rupeeSign">R  </span>' + nIndianFormat;
	        	}
	        },
	        {field:'m_nBalanceAmount',title:'Balance Amount',sortable:true,width:100,align:'right',
	        	formatter:function(value,row,index)
	        	{
					var nIndianFormat = formatNumber (value.toFixed(2),row,index);
					return '<span class="rupeeSign">R  </span>' + nIndianFormat;
	        	}
	        }
	    ]],
	    onSortColumn: function (strColumn, strOrder)
		{
			strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
			m_oInvoiceAddMemberData.m_strSortColumn = strColumn;
			m_oInvoiceAddMemberData.m_strSortOrder = strOrder;
			invoiceAdd_buildInvoiceList (m_oInvoiceAddMemberData.m_strSortColumn,m_oInvoiceAddMemberData.m_strSortOrder, 1, 20);
		},
	    onCheck: function (rowIndex, rowData)
		{
			invoiceAdd_holdCheckedData(rowData, true);
		},
		onUncheck: function (rowIndex, rowData)
		{
			invoiceAdd_holdCheckedData (rowData, false);
		},
		onCheckAll: function (rows)
		{
			invoiceAdd_holdAllCheckedData (rows);
		},
		onUncheckAll: function (rows)
		{
			invoiceAdd_holdAllUnCheckedData (rows);
		}
	});
	invoiceAdd_initDGPagination ();
	invoiceAdd_buildInvoiceList (m_oInvoiceAddMemberData.m_strSortColumn,m_oInvoiceAddMemberData.m_strSortOrder, m_oInvoiceAddMemberData.m_nPageNumber, m_oInvoiceAddMemberData.m_nPageSize);
}

function invoiceAdd_initDGPagination ()
{
	$('#invoiceAdd_table_invoiceDG').datagrid('getPager').pagination
	(
		{ 
			pageSize : 20,
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oInvoiceAddMemberData.m_nPageNumber = $('#invoiceAdd_table_invoiceDG').datagrid('getPager').pagination('options').pageNumber;
				invoiceAdd_buildInvoiceList (m_oInvoiceAddMemberData.m_strSortColumn, m_oInvoiceAddMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oInvoiceAddMemberData.m_nPageNumber = $('#invoiceAdd_table_invoiceDG').datagrid('getPager').pagination('options').pageNumber;
				m_oInvoiceAddMemberData.m_nPageSize = $('#invoiceAdd_table_invoiceDG').datagrid('getPager').pagination('options').pageSize;
				invoiceAdd_buildInvoiceList (m_oInvoiceAddMemberData.m_strSortColumn, m_oInvoiceAddMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function invoiceAdd_getFormData ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oInvoiceData.m_bIsForReceipt = true;
	oInvoiceData.m_oClientData.m_nClientId = $('#receipt_input_client').datebox('getValue');
	oInvoiceData.m_strInvoiceNumber = $("#invoiceAdd_input_invoiceNumber").val();
	return oInvoiceData;
}

function invoiceAdd_buildInvoiceList (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	var oInvoiceData = invoiceAdd_getFormData ();
	InvoiceDataProcessor.list(oInvoiceData,strColumn, strOrder, nPageNumber, nPageSize, invoiceAdd_gotInvoiceList);
}

function invoiceAdd_gotInvoiceList (oResponse)
{
	clearGridData ("#invoiceAdd_table_invoiceDG");
	$('#invoiceAdd_table_invoiceDG').datagrid('loadData',oResponse.m_arrInvoice);
	$('#invoiceAdd_table_invoiceDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oInvoiceAddMemberData.m_nPageNumber});
	invoiceAdd_checkDGRow ();
}

function invoiceAdd_cancel ()
{
	HideDialog("receipt_addInvoices_dialog");
}

function invoiceAdd_addInvoices ()
{
	if(m_oInvoiceAddMemberData.m_arrSelectedData.length >0)
	$('#receipt_table_invoiceDG').datagrid('loadData', m_oInvoiceAddMemberData.m_arrSelectedData);
	else
	alert("please select the invoices")
	HideDialog ("receipt_addInvoices_dialog");
	receipt_updateInvoiceDG ();
}

function invoiceAdd_filter ()
{
	invoiceAdd_buildInvoiceList (m_oInvoiceAddMemberData.m_strSortColumn, m_oInvoiceAddMemberData.m_strSortOrder,1, 20);
}

function invoiceAdd_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	if(bIsForAdd)
	{
		if(!invoiceAdd_isRowAdded (oRowData))
			m_oInvoiceAddMemberData.m_arrSelectedData.push(invoiceAdd_getData (oRowData));
	}
	else
		invoiceAdd_remove (oRowData);
}

function invoiceAdd_getData (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	var oInvoiceData = oRowData;
	for(var nIndex=0; nIndex < m_oReceiptMemberData.m_arrDeletedLineItems.length; nIndex++)
	{
		if (m_oReceiptMemberData.m_arrDeletedLineItems[nIndex].m_nInvoiceId == oRowData.m_nInvoiceId)
		{
			oInvoiceData = m_oReceiptMemberData.m_arrDeletedLineItems[nIndex];
			break;
		}
	}
	return oInvoiceData;
}

function invoiceAdd_remove (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	for (var nIndex = 0; nIndex < m_oInvoiceAddMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oInvoiceAddMemberData.m_arrSelectedData[nIndex].m_nInvoiceId == oRowData.m_nInvoiceId)
		{
			m_oInvoiceAddMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function invoiceAdd_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oInvoiceAddMemberData.m_arrSelectedData.length; nIndex++)
		bIsRowAdded = (m_oInvoiceAddMemberData.m_arrSelectedData [nIndex].m_nInvoiceId == oRowData.m_nInvoiceId);
	return bIsRowAdded;
}

function invoiceAdd_checkDGRow ()
{
	var arrInvoiceData = $('#invoiceAdd_table_invoiceDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrInvoiceData.length; nIndex++)
	{
		if(invoiceAdd_isRowSelectable(arrInvoiceData[nIndex].m_nInvoiceId))
			$("#invoiceAdd_table_invoiceDG").datagrid('checkRow', nIndex);
	}
}

function invoiceAdd_isRowSelectable (nInvoiceId)
{
	assert.isNumber(nInvoiceId, "nInvoiceId expected to be a Number.");
	assert(nInvoiceId !== 0, "nInvoiceId cannot be equal to zero.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oInvoiceAddMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oInvoiceAddMemberData.m_arrSelectedData[nIndex].m_nInvoiceId == nInvoiceId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function invoiceAdd_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows expected to be an Array.");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		invoiceAdd_holdCheckedData(arrRows[nIndex], true);
}

function invoiceAdd_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows expected to be an Array.");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		invoiceAdd_holdCheckedData(arrRows[nIndex], false);
}