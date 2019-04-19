var paymentList_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/PaymentData.js'
];



 includeDataObjects (paymentList_includeDataObjects, "paymentList_loaded()");

function paymentList_memberData ()
{
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "paymentList_addHyphen ()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_nSelectedPaymentId = -1;
    this.m_strXMLData = "";
    this.m_strEmailAddress = "";
    this.m_strSubject = ""; 
}
var m_oPaymentListMemberData = new paymentList_memberData ();

function paymentList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function paymentList_initAdmin ()
{
	m_oPaymentListMemberData.m_strActionItemsFunction = "paymentList_addActions (row, index)";
	document.getElementById ("paymentList_button_add").style.visibility="visible";
	paymentList_init ();
}

function paymentList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="paymentList_edit ('+row.m_nPaymentId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="paymentList_delete ('+index+')"/> </td>'+
						'<td> <img title="Print" src="images/print.jpg" width="20" id="printImageId" onClick="paymentList_print ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function paymentList_init ()
{
	$("#filterPayment_input_fromDate").datebox();
	$("#filterPayment_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filterPayment_input_toDate").datebox();
	$("#filterPayment_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	paymentList_createDataGrid ();
}

function paymentList_createDataGrid ()
{
	initHorizontalSplitter("#paymentList_div_horizontalSplitter", "#paymentList_table_paymentListDG");
	$('#paymentList_table_paymentListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strPaymentNumber',title:'Payment Number',sortable:true,width:200},
				  	{field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:200,
				  		formatter:function(value,row,index)
			        	{
			        		return row.m_oVendorData.m_strCompanyName;
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
			        		return paymentList_displayImages (row, index);
			        	}
					},
				]],
				onSelect: function (rowIndex, rowData)
				{
					paymentList_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oPaymentListMemberData.m_strSortColumn = strColumn;
					m_oPaymentListMemberData.m_strSortOrder = strOrder;
					paymentList_list (strColumn, strOrder, m_oPaymentListMemberData.m_nPageNumber, m_oPaymentListMemberData.m_nPageSize);
				}
		}
	);
	paymentList_initDGPagination ();
	paymentList_list (m_oPaymentListMemberData.m_strSortColumn,m_oPaymentListMemberData.m_strSortOrder,1, 10);
}

function paymentList_initDGPagination ()
{
	$('#paymentList_table_paymentListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oPaymentListMemberData.m_nPageNumber = nPageNumber;
				paymentList_list (m_oPaymentListMemberData.m_strSortColumn, m_oPaymentListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("paymentList_div_paymentDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oPaymentListMemberData.m_nPageNumber = nPageNumber;
				m_oPaymentListMemberData.m_nPageSize = nPageSize;
				paymentList_list (m_oPaymentListMemberData.m_strSortColumn, m_oPaymentListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("paymentList_div_paymentDetail").innerHTML = "";
			}
		}
	);
}

function paymentList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oPaymentListMemberData.m_strSortColumn = strColumn;
	m_oPaymentListMemberData.m_strSortOrder = strOrder;
	m_oPaymentListMemberData.m_nPageNumber = nPageNumber;
	m_oPaymentListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "paymentList_progressbarLoaded ()");
}

function paymentList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oPaymentData = paymentList_getFormData ();
	oPaymentData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPaymentData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PaymentDataProcessor.list(oPaymentData, m_oPaymentListMemberData.m_strSortColumn, m_oPaymentListMemberData.m_strSortOrder, m_oPaymentListMemberData.m_nPageNumber, m_oPaymentListMemberData.m_nPageSize, paymentList_listed);
}

function paymentList_listed (oResponse)
{
	clearGridData("#paymentList_table_paymentListDG");
	document.getElementById("paymentList_div_paymentDetail").innerHTML = "";
	$('#paymentList_table_paymentListDG').datagrid('loadData', oResponse.m_arrPaymentData);
	$('#paymentList_table_paymentListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oPaymentListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function paymentList_getFormData ()
{
	var oPaymentData = new PaymentData ();
	oPaymentData.m_oVendorData.m_strCompanyName =$('#filterPayment_input_vendorName').val();
	oPaymentData.m_strFromDate = FormatDate ($('#filterPayment_input_fromDate').datebox('getValue'));
	oPaymentData.m_strToDate = FormatDate ($('#filterPayment_input_toDate').datebox('getValue'));
	return oPaymentData;
}

function paymentList_cancel ()
{
	HideDialog("dialog");
}

function paymentList_displayImages (row, index)
{
	var oImage = eval (m_oPaymentListMemberData.m_strActionItemsFunction);
	return oImage;
}

function paymentList_selectedRowData (oRowData, nIndex)
{
	paymentList_showPaymentDetails (oRowData, "paymentList_div_paymentDetail");
}

function paymentList_showPaymentDetails (oRowData, strDivId) 
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isString(strDivId, "strDivId expected to be a string.");
	document.getElementById(strDivId).innerHTML = "";
	var oPaymentData = new PaymentData ();
	oPaymentData.m_nPaymentId = oRowData.m_nPaymentId;
	oPaymentData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPaymentData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PaymentDataProcessor.getXML (oPaymentData, function (strXMLData)
		{
			m_oPaymentListMemberData.m_strXMLData = strXMLData;
			m_oPaymentListMemberData.m_strEmailAddress = oRowData.m_oVendorData.m_strEmail;
			m_oPaymentListMemberData.m_strSubject = "Payment Details";
			populateXMLData (strXMLData, "inventorymanagement/paymentsandreceipt/paymentDetails.xslt", strDivId);
			paymentList_initializeDetailsDG ();
		});
	PaymentDataProcessor.get (oPaymentData, paymentList_gotData)
}

function paymentList_gotData(oResponse)
{
	var oPaymentData = oResponse.m_arrPaymentData[0];
	var arrPaymentData = getOrderedLineItems(oPaymentData.m_oPurchases);
	$('#paymentListDetails_table_paymentDetailsDG').datagrid('loadData',arrPaymentData);
	var nInvoiceTotal = 0;
	var nPaidTotal = 0;
	var nCurrentPaidTotal = 0;
	for(var nIndex = 0; nIndex < oPaymentData.m_oPurchases.length; nIndex ++)
	{
		nInvoiceTotal += oPaymentData.m_oPurchases[nIndex].m_oPurchaseData.m_nPaymentAmount + oPaymentData.m_oPurchases[nIndex].m_oPurchaseData.m_nBalanceAmount;
		nPaidTotal += oPaymentData.m_oPurchases[nIndex].m_oPurchaseData.m_nPaymentAmount;
		nCurrentPaidTotal += oPaymentData.m_oPurchases[nIndex].m_nAmount;
	}
	$('#paymentListDetails_table_paymentDetailsDG').datagrid('reloadFooter',[{m_strDate:'<b>Total</b>',m_nTotalAmount:nInvoiceTotal, m_nPaymentAmount:nPaidTotal, m_nAmount: nCurrentPaidTotal}]);
}

function paymentList_initializeDetailsDG ()
{
	$('#paymentListDetails_table_paymentDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strInvoiceNo',title:'INV#',sortable:true,width:100,
			  		formatter:function(value,row,index)
			  		{
				  		var strInvoiceNo = "";
			  			try
			  			{
			  				strInvoiceNo = row.m_oPurchaseData.m_strInvoiceNo;
			  			}
						catch (oException)
						{
							
						}
						return strInvoiceNo;
			  		}
			  	},
			  	{field:'m_strDate',title:'Date',sortable:true,width:100,
			  		formatter:function(value,row,index)
			  		{
				  		var strDate = value;
			  			try
			  			{
			  				strDate = row.m_oPurchaseData.m_strDate;
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
			  				nTotalAmount = row.m_oPurchaseData.m_nPaymentAmount + row.m_oPurchaseData.m_nBalanceAmount;
			  			}
			  			catch (oException)
			  			{
			  				
			  			}
						var nIndianFormat = formatNumber (nTotalAmount.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	}
				},
				{field:'m_nPaymentAmount',title:'Total Paid',sortable:true,align:'right',width:100,
					formatter:function(value,row,index)
		        	{
						var nPaymentAmount = value;
						try
						{
							var nPaymentAmount = row.m_oPurchaseData.m_nPaymentAmount;
						}
						catch (oException)
						{
							
						}
						var nIndianFormat = formatNumber (nPaymentAmount.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	}	
				},
				{field:'m_nAmount',title:'Current Paid Amt',sortable:true,align:'right',width:100,
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

function paymentList_showAddPopup ()
{
	navigate ("", "widgets/inventorymanagement/paymentsandreceipt/newPayment.js");
}

function paymentList_filter ()
{
	paymentList_list (m_oPaymentListMemberData.m_strSortColumn,m_oPaymentListMemberData.m_strSortOrder,1, 10);
}
function paymentList_edit (nPaymentId)
{
	assert.isNumber(nPaymentId, "nPaymentId expected to be a Number.");
	m_oPaymentListMemberData.m_nSelectedPaymentId = nPaymentId;
	loadPage ("include/process.html", "ProcessDialog", "paymentList_edit_progressbarLoaded ()");
}

function paymentList_edit_progressbarLoaded ()
{
	navigate ("editPayment", "widgets/inventorymanagement/paymentsandreceipt/editPayment.js");
}
function paymentList_print ()
{
	navigate ('Print payment','widgets/inventorymanagement/paymentsandreceipt/paymentListPrint.js');
}

function payment_handleAfterSave ()
{
	paymentList_list (m_oPaymentListMemberData.m_strSortColumn,m_oPaymentListMemberData.m_strSortOrder,1, 10);
}