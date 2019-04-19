var receiptReport_includeDataObjects =
[
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/ReceiptData.js',
	'widgets/reportmanagement/receipt/ReceiptReportData.js',
	'widgets/reportmanagement/receipt/ReportReceiptData.js'
]

 includeDataObjects (receiptReport_includeDataObjects, "receiptReport_loaded ()");

function receiptReport_memberData ()
{
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_strXMLData = "";
	this.m_nReceiptId = -1;
}

var m_oReceiptReportMemberData = new receiptReport_memberData ();

function receiptReport_initDuration ()
{
	createPopup("dialog", "#receiptReport_cancel", "#receiptReport_processReport", true);
	$("#reportDuration_input_dateFrom").datebox();
	$("#reportDuration_input_dateTo").datebox();
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
	$('#reportDuration_input_dateFrom').datebox('setValue', dCurrentDate);
	$('#reportDuration_input_dateTo').datebox('setValue', dCurrentDate);
}

function receiptReport_initOverallReport ()
{
	receiptReport_initReport ();
}

function receiptReport_initReport ()
{
	loadPage ("reportmanagement/receipt/reportDuration.html", "dialog", "receiptReport_initDuration ()");
}

function receiptReport_processReport ()
{
	m_oReceiptReportMemberData.m_strFromDate = FormatDate ($('#reportDuration_input_dateFrom').datebox('getValue'));
	m_oReceiptReportMemberData.m_strToDate = FormatDate ($('#reportDuration_input_dateTo').datebox('getValue'));
	receiptReport_generateReceiptReport ();
}

function receiptReport_getFormData ()
{
	var oReceiptData = new ReceiptData ();
	oReceiptData.m_oClientData.m_strCompanyName = $("#receiptReport_input_clientName").val();
	oReceiptData.m_strFromDate = (m_oReceiptReportMemberData.m_strFromDate != "undefined--undefined") ? m_oReceiptReportMemberData.m_strFromDate : null;
	oReceiptData.m_strToDate = (m_oReceiptReportMemberData.m_strToDate != "undefined--undefined") ? m_oReceiptReportMemberData.m_strToDate : null;
	oReceiptData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oReceiptData;
}

function receiptReport_generateReceiptReport ()
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "receiptReport_progressbarLoaded ()");
}
function receiptReport_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oReceiptData = receiptReport_getFormData ();
	HideDialog ("dialog");
	var oUpdateGridName = document.getElementById("receiptReport_table_receiptReportDG");
	oUpdateGridName.title += " [ From : " + oReceiptData.m_strFromDate + " - To : " + oReceiptData.m_strToDate + " ]";
	receiptReport_initializeDataGrid ();
	ReceiptDataProcessor.getReports(oReceiptData, receiptReport_listed);
}

function receiptReport_listed (oResponse)
{
	clearGridData ("#receiptReport_table_receiptReportDG");
	var nGrandTotal = 0;
	var arrReceipt = oResponse.m_arrReportReceiptData;
	for (var nIndex = 0; nIndex < arrReceipt.length; nIndex++)
	{
		nGrandTotal += Number (arrReceipt[nIndex].m_nAmount);
		$('#receiptReport_table_receiptReportDG').datagrid('appendRow',arrReceipt[nIndex]);
	}
	$('#receiptReport_table_receiptReportDG').datagrid('reloadFooter',[{m_strDate:'<b>Grand Total :</b>', m_nAmount:nGrandTotal}]);
	HideDialog ("secondDialog");
}

function receiptReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#receiptReport_table_receiptReportDG");
	$('#receiptReport_table_receiptReportDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'Client Name',sortable:true,width:450,
			  		formatter:function(value,row,index)
		        	{
				  		try
			  			{
				  			return row.m_oClientData.m_strCompanyName;
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
	$('#receiptReport_table_receiptReportDG').datagrid('reloadFooter',[{m_strDate:'Grand Total',m_nAmount:0}]);
	receiptReport_subGridSales ();
}

function receiptReport_subGridSales ()
{
	$('#receiptReport_table_receiptReportDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="receiptReport_table_detailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  receiptReport_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.receiptReport_table_detailViewDG');
	        receiptReport_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
	            columns:[[
	                {field:'m_strDate',title:'Date',sortable:true,width:60},
	                {field:'m_strReceiptNumber',title:'Receipt Number',sortable:true,width:150},
	                {field:'m_strModeName',title:'Mode Name',width:150,align:'right',
	                	formatter:function(value,row,index)
			        	{
							 return row.m_oMode.m_strModeName;
			        	}	
	                },
	                {field:'m_nAmount',title:'Amount',width:150,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                		var nAmount = row.m_nAmount;
	                		 var m_nIndianNumber = formatNumber (nAmount.toFixed(2),row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}
	                },
	                {field:'Actions',title:'Action',width:50,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return receiptReport_displayImages (row);
			        	}
					}
	            ]],
	            onResize:function()
	            {
	                $('#receiptReport_table_receiptReportDG').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#receiptReport_table_receiptReportDG').datagrid('fixDetailRowHeight',index);
	        receiptReport_subgridListed (receiptReport_table_detailViewDG, index, row);
	    }
	});
}

function receiptReport_subgridListed (receiptReport_table_detailViewDG, nGridIndex, oRow)
{
	assert.isObject(receiptReport_table_detailViewDG, "receiptReport_table_detailViewDG expected to be an Object.");
	assert.isObject(oRow, "oRow expected to be an Object.");
	assert( Object.keys(oRow).length >0 , "oRow cannot be an empty .");// checks for non emptyness
	var arrReceiptData = oRow.m_arrReceiptData;
	receiptReport_table_detailViewDG.datagrid('loadData',arrReceiptData);
}

function receiptReport_cancel ()
{
	HideDialog ("dialog");
}

function receiptReport_printReceiptReport ()
{
	var arrReceiptData = receiptReport_getReportData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strFromDate", m_oReceiptReportMemberData.m_strFromDate);
	addChild(xmlDoc, "root", "m_strToDate", m_oReceiptReportMemberData.m_strToDate);
	addChild(xmlDoc, "root", "m_strClientNameFilterBox", $("#receiptReport_input_clientName").val());
	m_oReceiptReportMemberData.m_strXMLData = generateXML (xmlDoc, arrReceiptData, "root", "ReceiptReportDataList");;
	navigate ('reportPrint','widgets/reportmanagement/receipt/receiptReportPrint.js');
}

function receiptReport_getReportData ()
{
	var arrReports = $('#receiptReport_table_receiptReportDG').datagrid('getRows');
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oReceiptReportData = new ReceiptReportData ();
		oReceiptReportData.m_strCompanyName = arrReports [nIndex].m_oClientData.m_strCompanyName;
		oReceiptReportData.m_strDate =  arrReports [nIndex].m_strDate;
		oReceiptReportData.m_nAmount = arrReports [nIndex].m_nAmount;
		arrReportData.push (oReceiptReportData);
	}
	return arrReportData;
}

function receiptReport_displayImages (row)
{
	assert.isObject(row, "row expected to be an Object.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/messager_info.gif" width="20" align="center" id="deleteImageId" onClick="receiptReport_getreceiptDetails ('+row.m_nReceiptId+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function receiptReport_getreceiptDetails (nReceiptId)
{
	assert.isNumber(nReceiptId, "nReceiptId expected to be a Number.");
	assert(nReceiptId !== 0, "nReceiptId cannot be equal to zero.");
	m_oReceiptReportMemberData.m_nReceiptId = nReceiptId;
	loadPage ("reportmanagement/receipt/receiptDetails.html", "dialog", "receiptReport_showReceiptDetails ()");
}

function receiptReport_showReceiptDetails ()
{
	createPopup("dialog", "", "", true);
	var oReceiptData = new ReceiptData ();
	oReceiptData.m_nReceiptId = m_oReceiptReportMemberData.m_nReceiptId;
	oReceiptData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ReceiptDataProcessor.getXML (oReceiptData, function (strXMLData)
		{
			populateXMLData (strXMLData, "inventorymanagement/paymentsandreceipt/receiptDetails.xslt", 'receiptDetails_div_receiptDetail');
			receiptReport_initializeDetailsDG ();
		});
	ReceiptDataProcessor.get (oReceiptData,receiptReport_gotInvoiceList)
}

function receiptReport_gotInvoiceList (oResponse)
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

function receiptReport_initializeDetailsDG ()
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


function receiptReport_exportToTally ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oReceiptData = receiptReport_getFormData ();
	ReceiptDataProcessor.exportToTally (oReceiptData, 
			{ 
		        callback: function(oResponse) 
		        { 
					HideDialog ("dialog");
		        }, 
		        errorHandler: function(strErrMsg, oException) 
		        { 
		        	HideDialog ("dialog");
		            displayErrorMessage(strErrMsg); 
		        } 
			});
}