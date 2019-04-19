var receiptList_includeDataObjects = 
[
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/ReceiptData.js'
];



 includeDataObjects (receiptList_includeDataObjects, "receiptList_loaded()");

function receiptList_memberData ()
{
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "receiptList_addHyphen ()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =50;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_nSelectedReceiptId = -1;
    this.m_strXMLData = "";
    this.m_strEmailAddress = "";
    this.m_strSubject = "";    
}
var m_oReceiptListMemberData = new receiptList_memberData ();

function receiptList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
		'<tr>' +
			'<td style="border-style:none;">-</td>'+
		'</tr>'+
	  '</table>'
	return oHyphen;
}

function receiptList_initAdmin ()
{
	m_oReceiptListMemberData.m_strActionItemsFunction = "receiptList_addActions (row, index)";
	document.getElementById ("receiptList_button_add").style.visibility="visible";
	receiptList_init ();
}

function receiptList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="receiptList_edit ('+row.m_nReceiptId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="receiptList_delete ('+index+')"/> </td>'+
						'<td> <img title="Print" src="images/print.jpg" width="20" id="printImageId" onClick="receiptList_print ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function receiptList_init ()
{
	$("#filterReceipt_input_fromDate").datebox();
	$("#filterReceipt_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filterReceipt_input_toDate").datebox();
	$("#filterReceipt_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	receiptList_createDataGrid ();
}

function receiptList_createDataGrid ()
{
	initHorizontalSplitter("#receiptList_div_horizontalSplitter", "#receiptList_table_receiptListDG");
	$('#receiptList_table_receiptListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strReceiptNumber',title:'Receipt Number',sortable:true,width:150},
				  	{field:'m_strCompanyName',title:'Client Name',sortable:true,width:200,
				  		formatter:function(value,row,index)
			        	{
			        		return row.m_oClientData.m_strCompanyName;
			        	},
				  	},
					{field:'m_strModeName',title:'Mode',sortable:true,width:100,
				  		formatter:function(value,row,index)
			        	{
			        		return row.m_oMode.m_strModeName;
			        	},
					},
					{field:'m_strDate',title:'Date',sortable:true,width:120},
					{field:'m_nAmount',title:'Amount',width:120,sortable:true,align:'right',
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
					{field:'Actions',title:'Action',width:80,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return receiptList_displayImages (row, index);
			        	}
					},
				]],
				onSelect: function (rowIndex, rowData)
				{
					receiptList_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oReceiptListMemberData.m_strSortColumn = strColumn;
					m_oReceiptListMemberData.m_strSortOrder = strOrder;
					receiptList_list (strColumn, strOrder, m_oReceiptListMemberData.m_nPageNumber, m_oReceiptListMemberData.m_nPageSize);
				}
		}
	);
	receiptList_initDGPagination ();
	receiptList_list (m_oReceiptListMemberData.m_strSortColumn,m_oReceiptListMemberData.m_strSortOrder,1, 10);
}

function receiptList_initDGPagination ()
{
	$('#receiptList_table_receiptListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oReceiptListMemberData.m_nPageNumber = nPageNumber;
				receiptList_list (m_oReceiptListMemberData.m_strSortColumn, m_oReceiptListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("receiptList_div_receiptDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oReceiptListMemberData.m_nPageNumber = nPageNumber;
				m_oReceiptListMemberData.m_nPageSize = nPageSize;
				receiptList_list (m_oReceiptListMemberData.m_strSortColumn, m_oReceiptListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("receiptList_div_receiptDetail").innerHTML = "";
			}
		}
	);
}

function receiptList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oReceiptListMemberData.m_strSortColumn = strColumn;
	m_oReceiptListMemberData.m_strSortOrder = strOrder;
	m_oReceiptListMemberData.m_nPageNumber = nPageNumber;
	m_oReceiptListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "receiptList_progressbarLoaded ()");
}

function receiptList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oReceiptData = receiptList_getFormData ();
	oReceiptData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ReceiptDataProcessor.list(oReceiptData, m_oReceiptListMemberData.m_strSortColumn, m_oReceiptListMemberData.m_strSortOrder, m_oReceiptListMemberData.m_nPageNumber, m_oReceiptListMemberData.m_nPageSize, receiptList_listed);
}

function receiptList_listed (oResponse)
{
	clearGridData("#receiptList_table_receiptListDG");
	$('#receiptList_table_receiptListDG').datagrid('loadData', oResponse.m_arrReceiptData);
	document.getElementById("receiptList_div_receiptDetail").innerHTML = "";
	$('#receiptList_table_receiptListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oReceiptListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function receiptList_getFormData ()
{
	var oReceiptData = new ReceiptData ();
	oReceiptData.m_oClientData.m_strCompanyName = $('#filterReceipt_input_clientName').val();
	oReceiptData.m_strFromDate = FormatDate ($('#filterReceipt_input_fromDate').datebox('getValue'));
	oReceiptData.m_strToDate = FormatDate ($('#filterReceipt_input_toDate').datebox('getValue'));
	return oReceiptData;
}

function receiptList_cancel ()
{
	HideDialog("dialog");
}

function receiptList_displayImages (row, index)
{
	var oImage = eval (m_oReceiptListMemberData.m_strActionItemsFunction);
	return oImage;
}

function receiptList_selectedRowData (oRowData, nIndex)
{
	receiptList_showReceiptDetails (oRowData, "receiptList_div_receiptDetail");
}

function receiptList_showReceiptDetails (oRowData, strDivId)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isString(strDivId, "strDivId expected to be a string.");
	document.getElementById(strDivId).innerHTML = "";
	var oReceiptData = new ReceiptData ();
	oReceiptData.m_nReceiptId = oRowData.m_nReceiptId;
	oReceiptData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ReceiptDataProcessor.getXML (oReceiptData,	function (strXMLData)
		{
			m_oReceiptListMemberData.m_strXMLData = strXMLData;
			m_oReceiptListMemberData.m_strEmailAddress = oRowData.m_oClientData.m_strEmail;
			m_oReceiptListMemberData.m_strSubject = "Receipt Details";
			populateXMLData (strXMLData, "inventorymanagement/paymentsandreceipt/receiptDetails.xslt", strDivId);
			receiptList_initializeDetailsDG ();
		});
	ReceiptDataProcessor.get (oReceiptData,receiptList_gotInvoiceList)
}

function receiptList_gotInvoiceList (oResponse)
{
	var oReceiptData = oResponse.m_arrReceiptData[0];
	var arrInvoiceReceipts = getOrderedLineItems(oReceiptData.m_oInvoiceReceipts);
	$('#receiptListDetails_table_receiptDetailsDG').datagrid('loadData',arrInvoiceReceipts);
	var nInvoiceTotal = 0;
	var nReceivedTotal = 0;
	var nCurrentReceiptTotal = 0;
	for(var nIndex = 0; nIndex < oReceiptData.m_oInvoiceReceipts.length; nIndex ++)
	{
		nInvoiceTotal += oReceiptData.m_oInvoiceReceipts[nIndex].m_oInvoiceData.m_nReceiptAmount + oReceiptData.m_oInvoiceReceipts[nIndex].m_oInvoiceData.m_nBalanceAmount;
		nReceivedTotal += oReceiptData.m_oInvoiceReceipts[nIndex].m_oInvoiceData.m_nReceiptAmount;
		nCurrentReceiptTotal += oReceiptData.m_oInvoiceReceipts[nIndex].m_nAmount;
	}
	$('#receiptListDetails_table_receiptDetailsDG').datagrid('reloadFooter',[{m_strDate:'<b>Total</b>',m_nTotalAmount:nInvoiceTotal, m_nReceiptAmount:nReceivedTotal, m_nAmount: nCurrentReceiptTotal}]);
	
	
}

function receiptList_initializeDetailsDG ()
{
	$('#receiptListDetails_table_receiptDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strInvoiceNumber',title:'INV#',sortable:true,width:100,
			  		formatter:function(value,row,index)
		        	{
			  			var strInvoiceNumber = value;
			  			try
			  			{
			  				strInvoiceNumber = row.m_oInvoiceData.m_strInvoiceNumber;
			  			}
			  			catch (oException)
		        		{
		        			
		        		}
			  			return strInvoiceNumber;
		        	}
		        },
			  	{field:'m_strDate',title:'Date',sortable:true,width:100,
			  		formatter:function(value,row,index)
		        	{
		        		var strDate = value;
			        	try
			        	{
			        		strDate = row.m_oInvoiceData.m_strDate;
			        	}
			        	catch (oException)
		        		{
		        			
		        		}
			  			return strDate;
		        	}
		        },
				{field:'m_nTotalAmount',title:'Invoice Amount',sortable:true,align:'right',width:120,
					formatter:function(value,row,index)
		        	{
		        		var nTotalAmount = value;
		        		try
		        		{
		        			nTotalAmount = row.m_oInvoiceData.m_nReceiptAmount + row.m_oInvoiceData.m_nBalanceAmount;
		        		}
		        		catch (oException)
		        		{
		        			
		        		}
						var nIndianFormat = formatNumber (nTotalAmount.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	}
				},
		        {field:'m_nReceiptAmount',title:'Total Received',width:120,align:'right',
		        	formatter:function(value,row,index)
		        	{
						var nReceiptAmount = value;
						try
						{
							nReceiptAmount = row.m_oInvoiceData.m_nReceiptAmount;
						}
						catch (oException)
		        		{
		        			
		        		}
						var nIndianFormat = formatNumber (nReceiptAmount.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	}
		        },
				{field:'m_nAmount',title:'Current Receipt Amt',sortable:true,align:'right',width:100,
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

function receiptList_showAddPopup ()
{
	navigate ("", "widgets/inventorymanagement/paymentsandreceipt/newReceipt.js");
}

function receipt_handleAfterSave ()
{
	receiptList_list (m_oReceiptListMemberData.m_strSortColumn,m_oReceiptListMemberData.m_strSortOrder,1, 10);
}

function receiptList_filter ()
{
	receiptList_list (m_oReceiptListMemberData.m_strSortColumn,m_oReceiptListMemberData.m_strSortOrder,1, 10);
}

function receiptList_edit (nReceiptId)
{
	assert.isNumber(nReceiptId, "nReceiptId expected to be a Number.");
	m_oReceiptListMemberData.m_nSelectedReceiptId = nReceiptId;
	loadPage ("include/process.html", "ProcessDialog", "receiptList_edit_progressbarLoaded ()");
}

function receiptList_edit_progressbarLoaded ()
{
	navigate ("editReceipt", "widgets/inventorymanagement/paymentsandreceipt/editReceipt.js");
}

function receiptList_print ()
{
	navigate ('Print Receipt','widgets/inventorymanagement/paymentsandreceipt/receiptListPrint.js');
}