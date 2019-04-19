var paymentReport_includeDataObjects =
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/PaymentData.js',
	'widgets/inventorymanagement/paymentsandreceipt/PurchasePaymentData.js',
	'widgets/reportmanagement/payment/paymentReportData.js',
	'widgets/reportmanagement/payment/ReportPaymentData.js'
]

 includeDataObjects (paymentReport_includeDataObjects, "paymentReport_loaded ()");

function paymentReport_memberData ()
{
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_strXMLData = "";
	this.m_nPaymentId = -1;
}

var m_oPaymentReportMemberData = new paymentReport_memberData ();

function paymentReport_initDuration ()
{
	createPopup("dialog", "#reportDuration_button_cancel", "#reportDuration_button_create", true);
	$("#reportDuration_input_dateFrom").datebox();
	$("#reportDuration_input_dateTo").datebox();
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
	$('#reportDuration_input_dateFrom').datebox('setValue', dCurrentDate);
	$('#reportDuration_input_dateTo').datebox('setValue', dCurrentDate);
}

function paymentReport_initOverallReport ()
{
	paymentReport_initReport ();
}

function paymentReport_initReport ()
{
	loadPage ("reportmanagement/payment/reportDuration.html", "dialog", "paymentReport_initDuration ()");
}

function paymentReport_processReport ()
{
	m_oPaymentReportMemberData.m_strFromDate = FormatDate ($('#reportDuration_input_dateFrom').datebox('getValue'));
	m_oPaymentReportMemberData.m_strToDate = FormatDate ($('#reportDuration_input_dateTo').datebox('getValue'));
	paymentReport_generatePaymentReport ();
}

function paymentReport_getFormData ()
{
	var oPaymentData = new PaymentData ();
	oPaymentData.m_oVendorData.m_strCompanyName = $("#paymentReport_input_vendorName").val ();
	oPaymentData.m_strFromDate = (m_oPaymentReportMemberData.m_strFromDate != "undefined--undefined") ? m_oPaymentReportMemberData.m_strFromDate : null;
	oPaymentData.m_strToDate = (m_oPaymentReportMemberData.m_strToDate != "undefined--undefined") ? m_oPaymentReportMemberData.m_strToDate : null;
	oPaymentData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPaymentData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oPaymentData;
}

function paymentReport_generatePaymentReport ()
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "paymentReport_progressbarLoaded ()");
}

function paymentReport_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oPaymentData = paymentReport_getFormData ();
	HideDialog ("dialog");
	var oUpdateGridName = document.getElementById("paymentReport_table_paymentReportDG");
	oUpdateGridName.title += " [ From : " + oPaymentData.m_strFromDate + " - To : " + oPaymentData.m_strToDate + " ]";
	paymentReport_initializeDataGrid ();
	PaymentDataProcessor.getReports(oPaymentData, paymentReport_listed);
}

function paymentReport_listed (oResponse)
{
	clearGridData ("#paymentReport_table_paymentReportDG");
	var nGrandTotal = 0;
	var arrPayment = oResponse.m_arrReportPaymentData;
	for (var nIndex = 0; nIndex < arrPayment.length; nIndex++)
	{
		nGrandTotal += Number (arrPayment[nIndex].m_nAmount);
		$('#paymentReport_table_paymentReportDG').datagrid('appendRow',arrPayment[nIndex]);
	}
	$('#paymentReport_table_paymentReportDG').datagrid('reloadFooter',[{m_strDate:'<b>Grand Total :</b>', m_nAmount:nGrandTotal}]);
	HideDialog ("secondDialog");
}

function paymentReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#paymentReport_table_paymentReportDG");
	$('#paymentReport_table_paymentReportDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:500,
			  		formatter:function(value,row,index)
		        	{
				  		try
			  			{
				  			return row.m_oVendorData.m_strCompanyName;
			  			}
			  			catch(oException)
			  			{
			  				return '';
			  			}
		        	}
			  	},
				{field:'m_nAmount',title:'Total Amount',width:150,align:'right',sortable:true,
					formatter:function(value,row,index)
		        	{
						var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
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
	$('#paymentReport_table_paymentReportDG').datagrid('reloadFooter',[{m_strDate:'Grand Total',m_nAmount:0}]);
	paymentReport_subGridSales ();
}

function paymentReport_subGridSales ()
{
	$('#paymentReport_table_paymentReportDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="paymentReport_table_detailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  paymentReport_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.paymentReport_table_detailViewDG');
	        paymentReport_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
	            columns:[[
	                {field:'m_strDate',title:'Date',width:60},
	                {field:'m_strPaymentNumber',title:'Payment Number',sortable:true,width:150},
	                {field:'m_strModeName',title:'Mode',width:150,align:'right',
	                	formatter:function(value,row,index)
			        	{
							 return row.m_oMode.m_strModeName;
			        	}	
	                },
	                {field:'m_nAmount',title:'Amount',width:150,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                		var nAmount = row.m_nAmount;
	                		var m_nIndianNumber = formatNumber (nAmount.toFixed(2),value,index);
							return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}
	                },
	                {field:'Actions',title:'Action',width:50,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return paymentReport_displayImages (row, index);
			        	}
					}
	            ]],
	            onResize:function()
	            {
	                $('#paymentReport_table_paymentReportDG').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#paymentReport_table_paymentReportDG').datagrid('fixDetailRowHeight',index);
	        paymentReport_subgridListed (paymentReport_table_detailViewDG, index, row);
	    }
	});
}

function paymentReport_displayImages (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Delete" src="images/messager_info.gif" width="20" align="center" id="deleteImageId" onClick="paymentReport_getPaymentDetails ('+row.m_nPaymentId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function paymentReport_subgridListed (paymentReport_table_detailViewDG, nGridIndex, row)
{
	assert.isObject(paymentReport_table_detailViewDG, "paymentReport_table_detailViewDG expected to be an Object.");
	assert.isObject(row, "row expected to be an Object.");
	var arrPaymentData = row.m_arrPaymentData;
	paymentReport_table_detailViewDG.datagrid('loadData',arrPaymentData);
}


function paymentReport_cancel ()
{
	HideDialog ("dialog");
}

function paymentReport_printPaymentReport ()
{
	var arrPaymentData = paymentReport_getReportData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strFromDate", m_oPaymentReportMemberData.m_strFromDate);
	addChild(xmlDoc, "root", "m_strToDate", m_oPaymentReportMemberData.m_strToDate);
	addChild(xmlDoc, "root", "m_strVendorName", $("#paymentReport_input_vendorName").val());
	m_oPaymentReportMemberData.m_strXMLData = generateXML (xmlDoc, arrPaymentData, "root", "PaymentReportDataList");;
	navigate ('reportPrint','widgets/reportmanagement/payment/paymentReportPrint.js');
}

function paymentReport_getReportData ()
{
	var arrReports = $('#paymentReport_table_paymentReportDG').datagrid('getRows');
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oPaymentReportData = new PaymentReportData ();
		oPaymentReportData.m_strVendorName = arrReports [nIndex].m_oVendorData.m_strCompanyName;
		oPaymentReportData.m_nAmount = arrReports [nIndex].m_nAmount;
		arrReportData.push (oPaymentReportData);
	}
	return arrReportData;
}

function paymentReport_exportToTally ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oPaymentData = paymentReport_getFormData ();
	PaymentDataProcessor.exportToTally (oPaymentData, 
			{ 
		        callback: function(oResponse) 
		        { 
					HideDialog ("dialog");
					dwr.engine.openInDownload(oResponse);
		        }, 
		        errorHandler: function(strErrMsg, oException) 
		        { 
		        	HideDialog ("dialog");
		            displayErrorMessage(strErrMsg); 
		        } 
			});
}

function paymentReport_getPaymentDetails (nPaymentId)
{
	assert.isNumber(nPaymentId, "nPaymentId expected to be a Number.");
	m_oPaymentReportMemberData.m_nPaymentId = nPaymentId;
	loadPage ("reportmanagement/payment/paymentDetails.html", "dialog", "paymentReport_showPaymentDetails ()");
}

function paymentReport_showPaymentDetails ()
{
	createPopup("dialog", "", "", true);
	var oPaymentData = new PaymentData ();
	oPaymentData.m_nPaymentId = m_oPaymentReportMemberData.m_nPaymentId;
	oPaymentData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPaymentData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PaymentDataProcessor.getXML (oPaymentData,	function (strXMLData)
		{
			populateXMLData (strXMLData, "inventorymanagement/paymentsandreceipt/paymentDetails.xslt", 'paymentDetails_div_paymentDetail');
			paymentReport_initializeDetailsDG ();
		});
	PaymentDataProcessor.get (oPaymentData, paymentReport_gotData)
}

function paymentReport_gotData(oResponse)
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

function paymentReport_initializeDetailsDG ()
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