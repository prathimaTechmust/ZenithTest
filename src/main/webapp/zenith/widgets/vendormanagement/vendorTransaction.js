var vendorTransaction_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/PaymentData.js'
];

 includeDataObjects (vendorTransaction_includeDataObjects, "vendorTransaction_loaded()");

function vendorTransaction_memberData ()
{
	this.m_strXmlData = "";
	this.m_oSelectedVendorPurchaseData = null;
	this.m_nPurchaseDGPageNumber = 1;
    this.m_nPurchaseDGPageSize =10;
    this.m_strSortPurchaseDGColumn = "m_dCreatedOn";
    this.m_strSortPurchaseDGOrder = "desc";
    this.m_nPaymentDGPageNumber = 1;
    this.m_nPaymentDGPageSize =10;
    this.m_strSortPaymentDGColumn = "m_dCreatedOn";
    this.m_strSortPaymentDGOrder = "desc";
    this.m_nVendorId = -1;
}

var m_oVendorTransactionMemeberData = new vendorTransaction_memberData ();

function vendorTransaction_init ()
{
	createPopup ("dialog", "", "#vendorTransaction_button_cancel", true);
	vendorTransaction_initVendorDetails ();
	vendorTransaction_initTabs ();
}

function vendorTransaction_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
}

function vendorTransaction_initVendorDetails ()
{
	var m_oVendorData = m_oVendorTransactionMemeberData.m_oVendorData;
	$('#vendorTransaction_label_vendorName').val(m_oVendorData.m_strCompanyName);
	$('#vendorTransaction_label_city').val(m_oVendorData.m_strCity);
	$('#vendorTransaction_label_address').val(m_oVendorData.m_strAddress);
}

function vendorTransaction_initTabs ()
{
	$('#vendorTransaction_div_tabs').tabs (
		{
			onSelect: function (oTitle)
			{
				if (oTitle.toLowerCase().search ('age wise') >= 0)
				{
					loadPage ("inventorymanagement/progressbar.html", "secondDialog", "vendorTransaction_progressbarLoaded ()");
					vendorTransaction_initAgeWiseDG ();
				}
				else if (oTitle.toLowerCase().search ('purchase details') >= 0)
				{
					loadPage ("inventorymanagement/progressbar.html", "secondDialog", "vendorTransaction_progressbarLoaded ()");
					vendorTransaction_initPurchaseDetailsDG ();
				}
				else if (oTitle.toLowerCase().search ('payment details') >= 0)
				{
					loadPage ("inventorymanagement/progressbar.html", "secondDialog", "vendorTransaction_progressbarLoaded ()");
					vendorTransaction_initPaymentDetailsDG ();
				}
			}
		});
}

function vendorTransaction_initPurchaseDetailsDG ()
{
	$('#vendorTransaction_table_purchaseDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:150},
			  	{field:'m_strDate',title:'Date',sortable:true,width:150},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return vendorTransaction_purchaseDetails_displayImages (row, index);
		        	}
				},
			]],
			onSelect: function (rowIndex, oRowData)
			{
				m_oVendorTransactionMemeberData.m_oSelectedVendorPurchaseData = oRowData;
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oVendorTransactionMemeberData.m_strSortPurchaseDGColumn = strColumn;
				m_oVendorTransactionMemeberData.m_strSortPurchaseDGOrder = strOrder;
				vendorTransaction_getPurchaseList (strColumn, strOrder, m_oVendorTransactionMemeberData.m_nPurchaseDGPageNumber, m_oVendorTransactionMemeberData.m_nPurchaseDGPageSize);
			}
		}
	);
	vendorTransaction_initPurchaseDGPagination ();
	vendorTransaction_getPurchaseList (m_oVendorTransactionMemeberData.m_strSortPurchaseDGColumn, m_oVendorTransactionMemeberData.m_strSortPurchaseDGOrder, 1, 10);
}

function vendorTransaction_getPurchaseList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oPurchaseData.m_oVendorData.m_nClientId = m_oVendorTransactionMemeberData.m_nVendorId;
	PurchaseDataProcessor.list(oPurchaseData, strColumn, strOrder, nPageNumber, nPageSize, vendorTransaction_gotPurchaseList);
}

function vendorTransaction_gotPurchaseList (oResponse)
{
	clearGridData ("#vendorTransaction_table_purchaseDetailsDG");
		$('#vendorTransaction_table_purchaseDetailsDG').datagrid('loadData', oResponse.m_arrPurchase);
	$('#vendorTransaction_table_purchaseDetailsDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oVendorTransactionMemeberData.m_nPurchaseDGPageNumber});
	HideDialog("secondDialog");
}

function vendorTransaction_purchaseDetails_displayImages (oRow, nIndex)
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="vendorTransaction_getPurchaseInfo ()"/> </td>'+
					'</tr>'+
					'</table>'
	return oImage;
}

function vendorTransaction_initPurchaseDGPagination ()
{
	$('#vendorTransaction_table_purchaseDetailsDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorTransactionMemeberData.m_nPurchaseDGPageNumber = $('#vendorTransaction_table_purchaseDetailsDG').datagrid('getPager').pagination('options').pageNumber;
				vendorTransaction_getPurchaseList (m_oVendorTransactionMemeberData.m_strSortPurchaseDGColumn, m_oVendorTransactionMemeberData.m_strSortPurchaseDGOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorTransactionMemeberData.m_nPurchaseDGPageNumber = $('#vendorTransaction_table_purchaseDetailsDG').datagrid('getPager').pagination('options').pageNumber;
				m_oVendorTransactionMemeberData.m_nPurchaseDGPageSize = $('#vendorTransaction_table_purchaseDetailsDG').datagrid('getPager').pagination('options').pageSize;
				vendorTransaction_getPurchaseList (m_oVendorTransactionMemeberData.m_strSortPurchaseDGColumn, m_oVendorTransactionMemeberData.m_strSortPurchaseDGOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function vendorTransaction_initPaymentDetailsDG ()
{
	$('#vendorTransaction_table_paymentDetailsDG').datagrid
	(
		{
			columns:
				[[
				  	{field:'m_strDate',title:'Date',sortable:true,width:60},
				  	{field:'m_strPaymentNumber',title:'Payment Number',sortable:true,width:150},
					{field:'m_strModeName',title:'Mode',sortable:true,width:80},
					{field:'m_nAmount',title:'Amount',width:150,sortable:true,align:'right',
						formatter:function(value,row,index)
			        	{
							var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
							return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;
			        	}
					},
					{field:'Actions',title:'Action',width:80,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return vendorTransaction_payments_displayImages (row, index);
			        	}
					},
				]],
				onSelect: function (rowIndex, rowData)
				{
					m_oVendorTransactionMemeberData.m_oSelectedVendorPaymentData = rowData;
				},
				onSortColumn: function (strColumn, strOrder)
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oVendorTransactionMemeberData.m_strSortPaymentDGColumn = strColumn;
					m_oVendorTransactionMemeberData.m_strSortPaymentDGOrder = strOrder;
					vendorTransaction_getPayments (strColumn, strOrder, m_oVendorTransactionMemeberData.m_nPaymentDGPageNumber, m_oVendorTransactionMemeberData.m_nPaymentDGPageSize);
				}
		}
	);
	paymentList_initPaymentDGPagination ();
	vendorTransaction_getPayments (m_oVendorTransactionMemeberData.m_strSortPaymentDGColumn, m_oVendorTransactionMemeberData.m_strSortPaymentDGOrder, 1, 10);
}

function vendorTransaction_getPayments (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oPaymentData = new PaymentData ();
	oPaymentData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPaymentData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PaymentDataProcessor.list(oPaymentData, strColumn, strOrder,nPageNumber, nPageSize, vendorTransaction_gotPayments);
}

function vendorTransaction_gotPayments (oResponse)
{
	clearGridData ("#vendorTransaction_table_paymentDetailsDG");
	$('#vendorTransaction_table_paymentDetailsDG').datagrid('loadData', oResponse.m_arrPaymentData);
//	for(var nIndex = 0; nIndex< arrPayment.length; nIndex++)
//	{
//		if(arrPayment[nIndex].m_oVendorData.m_nClientId == m_oVendorTransactionMemeberData.m_nVendorId)
//		{
//			arrPayment[nIndex].m_strCompanyName = arrPayment[nIndex].m_oVendorData.m_strCompanyName;
//			arrPayment[nIndex].m_strModeName = arrPayment[nIndex].m_oMode.m_strModeName;
//			
//		}
//	}
	var nRows = $('#vendorTransaction_table_paymentDetailsDG').datagrid('getRows').length;
	$('#vendorTransaction_table_paymentDetailsDG').datagrid('getPager').pagination ({total:nRows, pageNumber:m_oVendorTransactionMemeberData.m_nPaymentDGPageNumber});
	HideDialog("secondDialog");
}

function paymentList_initPaymentDGPagination ()
{
	$('#vendorTransaction_table_paymentDetailsDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorTransactionMemeberData.m_nPaymentDGPageNumber = $('#vendorTransaction_table_paymentDetailsDG').datagrid('getPager').pagination('options').pageNumber;
				vendorTransaction_getPayments (m_oVendorTransactionMemeberData.m_strSortPaymentDGColumn, m_oVendorTransactionMemeberData.m_strSortPaymentDGOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorTransactionMemeberData.m_nPaymentDGPageNumber = $('#vendorTransaction_table_paymentDetailsDG').datagrid('getPager').pagination('options').pageNumber;
				m_oVendorTransactionMemeberData.m_nPaymentDGPageSize = $('#vendorTransaction_table_paymentDetailsDG').datagrid('getPager').pagination('options').pageSize;
				vendorTransaction_getPayments (m_oVendorTransactionMemeberData.m_strSortPaymentDGColumn, m_oVendorTransactionMemeberData.m_strSortPaymentDGOrder,nPageNumber, nPageSize);
			}
		}
	);
}

function vendorTransaction_cancel ()
{
	HideDialog("dialog");
}

function vendorTransaction_payments_displayImages (oRow, nIndex)
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="vendorTransaction_getPaymentInfo ()"/> </td>'+
					'</tr>'+
					'</table>'
	return oImage;
}

function vendorTransaction_getPurchaseInfo ()
{
	navigate ('purchaseInfo','widgets/vendormanagement/vendorTransactionPurchaseInfo.js');
}

function vendorTransaction_getPaymentInfo ()
{
	navigate ('paymentInfo','widgets/vendormanagement/vendorTransactionPaymentInfo.js');
}

function vendorTransaction_initAgeWiseDG ()
{
	$('#vendorTransaction_table_ageWiseDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strPeriod',title:'Duration (days)',sortable:true,width:150,
			  		formatter:function(value,row,index)
		        	{
			  			if(value != undefined && value.localeCompare("91-") == 0)
			  				value =  "91 and above";
			  			return value;
		        	}
			  	},
			  	{field:'m_nInvoicesCount',title:'No. of Invoice',sortable:true,width:150,align:'right',
			  		formatter:function(value,row,index)
		        	{
			  			return value;
		        	}
			  	},
				{field:'m_nAmount',title:'Amount',sortable:true,width:100,align:'right',
			  		formatter:function(value,row,index)
		        	{
			  			var nAmount = 0;
						if(value > 0)
							nAmount = value;
						var m_nIndianNumber = formatNumber (nAmount.toFixed(2));
						return '<span class="rupeeSign">R  </span> <b>' + m_nIndianNumber +'</b>';
		        	}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				m_oVendorOutstandingReport_memberData.m_oVendorData = rowData[0];
			}
		}
	);
	vendorTransaction_invoiceSubGridDetails ();
	vendorTransaction_getAgeWiseData ();
}

function vendorTransaction_invoiceSubGridDetails ()
{
	$('#vendorTransaction_table_ageWiseDG').datagrid({view: detailview,detailFormatter:function(index,row)
		{
            return '<div style="padding:2px"><table class="vendorTransaction_table_detailViewDG"></table></div>';
        },
        onExpandRow: function(index,row)
        {
            var vendorTransaction_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.vendorTransaction_table_detailViewDG');
            vendorTransaction_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
                columns:[[
                    {field:'m_strDate',title:'Date',width:50},
	                {field:'m_strInvoiceNo',title:'Invoice No',width:80},
	                {field:'m_nTotalAmount',title:'Total Amount',width:100,align:'right',
	                	formatter:function(value,row,index)
			        	{
		                	try
				  			{
		                		return row.m_nTotalAmount.toFixed(2);		
				  			}
				  			catch (oException)
				  			{
				  				return '';
				  			}
			        	}
	                },
	                {field:'m_nPaymentAmount',title:'Paid Amount',width:100,align:'right',
			        		formatter:function(value,row,index)
				        	{
			                	try
					  			{
			                		return row.m_nPaymentAmount.toFixed(2);	
					  			}
					  			catch (oException)
					  			{
					  				return '';
					  			}
				        	}		
	                },
	                {field:'m_nBalanceAmount',title:'Balance Amount',width:100,align:'right',
	                	formatter:function(value,row,index)
			        	{
		                	try
				  			{
			                	var nBalanceAmount = row.m_nTotalAmount - row.m_nPaymentAmount;
								return nBalanceAmount.toFixed(2);	
				        	}
				  			catch (oException)
				  			{
				  				return '';
				  			}
			        	}
	                }
                ]],
                onResize:function()
                {
            		$('#vendorTransaction_table_ageWiseDG').datagrid('fixDetailRowHeight',index);
                }
            });
            vendorTransaction_subgridInvoiceListed (vendorTransaction_table_detailViewDG, index, row);
        }
    });
}

function vendorTransaction_getAgeWiseData ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oPurchaseData.m_oVendorData.m_nClientId = m_oVendorTransactionMemeberData.m_nVendorId;
	PurchaseDataProcessor.getAgeWiseInvoices(oPurchaseData, vendorTransaction_gotAgeWiseData)
}

function vendorTransaction_gotAgeWiseData (arrAgeWiseData)
{
	var nTotalInvoices = 0;
	var nTotalInvoiceAmount = 0;
	clearGridData ("#vendorTransaction_table_ageWiseDG");
	for(var nIndex = 0; nIndex < arrAgeWiseData.length; nIndex++)
	{
		var oPurchaseData = new PurchaseData ();
		oPurchaseData.m_strPeriod = arrAgeWiseData[nIndex][0];
		oPurchaseData.m_nInvoicesCount = arrAgeWiseData[nIndex][1];
		oPurchaseData.m_nAmount = arrAgeWiseData[nIndex][2];
		nTotalInvoices += arrAgeWiseData[nIndex][1];
		nTotalInvoiceAmount += arrAgeWiseData[nIndex][2];
		if(oPurchaseData.m_nInvoicesCount > 0)
			$('#vendorTransaction_table_ageWiseDG').datagrid('',oPurchaseData);
	}
	HideDialog ("secondDialog");
}

function vendorTransaction_subgridInvoiceListed (vendorTransaction_table_detailViewDG, index, row)
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "progressbarLoaded ()");
	clearGridData (vendorTransaction_table_detailViewDG);
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oVendorData.m_nClientId = m_oVendorTransactionMemeberData.m_nVendorId;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var arrDates = row.m_strPeriod.split("-");
	var strFromDate = arrDates.length > 1 ? getDateFromDays(-parseInt(arrDates[1])) : "";
	oPurchaseData.m_strFromDate = strFromDate;
	oPurchaseData.m_strToDate = getDateFromDays(-parseInt(arrDates[0]));
	PurchaseDataProcessor.list(oPurchaseData, "", "", "", "", function (oResponse)
	{
		var arrInvoiceData = oResponse.m_arrPurchase;
		for(var nIndex = 0; nIndex < arrInvoiceData.length; nIndex++)
		{
			var oPurchaseData = new PurchaseData ();
			oPurchaseData.m_strDate = arrInvoiceData[nIndex].m_strDate;
			oPurchaseData.m_strInvoiceNo = arrInvoiceData[nIndex].m_strInvoiceNo;
			oPurchaseData.m_nTotalAmount = arrInvoiceData[nIndex].m_nTotalAmount;
			oPurchaseData.m_nPaymentAmount = arrInvoiceData[nIndex].m_nPaymentAmount;
			oPurchaseData.m_nBalanceAmount = oPurchaseData.m_nTotalAmount - oPurchaseData.m_nPaymentAmount;
			if(Math.round(oPurchaseData.m_nBalanceAmount) > 0)
				vendorTransaction_table_detailViewDG.datagrid('',oPurchaseData);
		}
		$('#vendorTransaction_table_ageWiseDG').datagrid('fixDetailRowHeight',index);
		HideDialog ("secondDialog");
	});
}

