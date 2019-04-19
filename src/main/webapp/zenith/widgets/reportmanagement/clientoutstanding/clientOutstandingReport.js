var clientOutstandingReport_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/reportmanagement/clientoutstanding/ClientOutsatandingReortData.js',
	'widgets/reportmanagement/clientoutstanding/ClientInvoiceDetailsData.js'
];

 includeDataObjects (clientOutstandingReport_includeDataObjects, "clientOutstandingReport_loaded()");

function clientOutstandingReport_memberData ()
{
	this.bIsforDetails = false;
	this.m_strXMLData = "";
	this.m_nIndex = -1;
	this.m_oSelectedClientId =-1;
	this.m_strSelectedPeriod = "";
}

var m_oClientOutstandingMemberData = new clientOutstandingReport_memberData ();

function clientOutstandingReport_loaded ()
{
	loadPage ("reportmanagement/clientoutstanding/clientOutstandingReport.html", "workarea", "clientOutstandingReport_init ()");
}

function clientOutstandingReport_init ()
{
	clientOutstandingReport_initializeDataGrid ();
}

function clientOutstandingReport_onChangeReportTypeDD ()
{
	var strSelectedDD = $("#clientOutstandingReport_select_by").val();
	if(strSelectedDD == 'clientwise')
	{
		document.getElementById ("clientOutstandingReport_button_printReport").style.visibility="visible";
		m_oClientOutstandingMemberData.m_bIsReportAgeWise = false;
		clientOutstandingReport_initializeDataGrid ();
	}
	else
	{
		document.getElementById ("clientOutstandingReport_button_printReport").style.visibility="hidden";
		m_oClientOutstandingMemberData.m_bIsReportAgeWise = true;
		clientOutstandingReport_initializeAgeWiseDataGrid ();
	}
}

function clientOutstandingReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#clientOutstandingReport_table_clientOutstandingReportDG");
	var oDataGrid = $("#clientOutstandingReport_table_clientOutstandingReportDG").datagrid();
	clientOutstandingReport_initClientDetailsDG (oDataGrid);
	clientOutstandingReport_generateClientOutStandingReport ();
}

function clientOutstandingReport_initClientDetailsDG (oDataGrid)
{
	assert.isObject(oDataGrid, "oDataGrid expected to be an Object.");
	assert( Object.keys(oDataGrid).length >0 , "oDataGrid cannot be an empty .");// checks for non emptyness 
	oDataGrid.datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'Client Name',sortable:true,width:150},
			  	{field:'m_strCity',title:'Address',sortable:true,width:150},
				{field:'m_nOpeningBalance',title:'Opening Balance',sortable:true,width:100,align:'right',
			  		formatter:function(value,row,index)
		        	{
			  			try
			  			{	
			  				var m_nIndianNumber = formatNumber (row.m_nOpeningBalance.toFixed(2));
			  				if(row.m_strCompanyName != '')
			  					return m_nIndianNumber;
			  				else
								return '<span class="rupeeSign">R  </span> <b>' + m_nIndianNumber +'</b>';
			  			}
			  			catch (oException)
			  			{
			  				return '';
			  			}
		        	}	
				},
				{field:'m_nInvoiceAmount',title:'Invoiced Amount',sortable:true,align:'right',width:100,
					formatter:function(value,row,index)
		        	{
						try
						{
							var m_nIndianNumber = formatNumber (row.m_nInvoiceAmount.toFixed(2));
							if(row.m_strCompanyName != '')
								return m_nIndianNumber;
							else
								return '<span class="rupeeSign">R  </span> <b>' + m_nIndianNumber +'</b>';
						}
						catch (oException)
			  			{
			  				return '';
			  			}
		        	}
				},
				{field:'m_nReceiptAmount',title:'Receipts Amount',width:100,sortable:true,align:'right',
					formatter:function(value,row,index)
		        	{
						try
						{
							var m_nIndianNumber = formatNumber (row.m_nReceiptAmount.toFixed(2));
							if(row.m_strCompanyName != '')
								return m_nIndianNumber;
							else
								return '<span class="rupeeSign">R  </span> <b>' + m_nIndianNumber +'</b>';
						}
						catch (oException)
			  			{
			  				return '';
			  			}
		        	}
				},
				{field:'m_nBalanceAmount',title:'Outstanding',width:100,sortable:true,align:'right',
					formatter:function(value,row,index)
		        	{
						try
						{
							var m_nIndianNumber = formatNumber (row.m_nBalanceAmount.toFixed(2));
							if(row.m_strCompanyName != '')
								return  m_nIndianNumber;
							else
								return '<span class="rupeeSign">R  </span> <b>' + m_nIndianNumber +'</b>';
						}
						catch (oException)
			  			{
			  				return '';
			  			}
		        	}
				},
				{field:'Action',title:'Action',sortable:false,width:50,align:'center',
		        	formatter:function(value,row,index)
		        	{
		        		if(row.m_strCompanyName != '')
		        			return clientOutstandingReport_displayImages (row.m_nClientId);
		        	}
		        }
			]],
			onSelect: function (rowIndex, rowData)
			{
				m_oClientOutstandingMemberData.m_oClientData = rowData[0];
			}
		}
	);
	clientOutstandingReport_subGridDetails (oDataGrid);
}

function clientOutstandingReport_displayImages (nClientId)
{
//  	assert.isObject(row, "row expected to be an Object.");
	return '<table align="center">'+
			'<tr>'+
				'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="clientOutstandingReport_getInfo ('+nClientId+')"/> </td>'+
				'<td> <img title="Print" width="20" src="images/print.jpg" onClick="clientOutstandingReport_print ('+nClientId+')"/> </td>'+
			'</tr>'+
		'</table>'
}

function clientOutstandingReport_print (nClientId)
{
	assert.isNumber(nClientId, "nClientId expected to be a Number.");
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oClientData.m_nClientId = nClientId;
	InvoiceDataProcessor.list(oInvoiceData, "", "", 1, 10, clientOutstandingReport_printClientInvoiceDetail);
}

function clientOutstandingReport_printPopUp ()
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oClientData.m_nClientId = m_oClientOutstandingMemberData.m_nClientId;
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	InvoiceDataProcessor.getXML (oInvoiceData, clientOutstandingReport_gotXML);
		
}

function clientOutstandingReport_gotXML(strXMLData)
{
	//  server response
	
	populateXMLData (strXMLData, "reportmanagement/clientoutstanding/clientOutstandingDetails.xslt", 'print_div_listDetail');
}


function clientOutstandingReport_printClientInvoiceDetail (oResponse)
{
	var arrInvoiceData = clientOutstandingReport_getClientInvoiceDetail (oResponse.m_arrInvoice);
	var xmlDoc = loadXMLDoc("include/default.xml");
	m_oClientOutstandingMemberData.bIsforDetails  = true;
	m_oClientOutstandingMemberData.m_strXMLData = generateXML (xmlDoc, arrInvoiceData, "root", "ClientOutstandingList");;
	navigate ('Print client outstanding','widgets/reportmanagement/clientoutstanding/clientOutstandingReportPrint.js');
}

function clientOutstandingReport_getClientInvoiceDetail (arrReports)
{
	assert.isArray(arrReports, "arrReports expected to be an Array.");
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		if(Math.round(arrReports [nIndex].m_nBalanceAmount) > 0)
		{
			var oClientInvoiceDetailsData = new ClientInvoiceDetailsData ();
			oClientInvoiceDetailsData.m_strDate = arrReports [nIndex].m_strDate;
			oClientInvoiceDetailsData.m_strInvoiceNumber = arrReports [nIndex].m_strInvoiceNumber;
			oClientInvoiceDetailsData.m_nInvoiceAmount = arrReports [nIndex].m_nInvoiceAmount;
			oClientInvoiceDetailsData.m_nReceiptAmount =  arrReports [nIndex].m_nReceiptAmount;
			oClientInvoiceDetailsData.m_nBalanceAmount = arrReports [nIndex].m_nBalanceAmount;
			arrReportData.push (oClientInvoiceDetailsData);
		}
	}
	return arrReportData;
}

function clientOutstandingReport_subGridDetails (oMainDataGrid)
{
	assert.isObject(oMainDataGrid, "oMainDataGrid expected to be an Object.");
	assert( Object.keys(oMainDataGrid).length >0 , "oMainDataGrid cannot be an empty .");// checks for non emptyness 
	oMainDataGrid.datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="clientOutstandingReport_table_DetailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  clientOutstandingReport_table_DetailViewDG = $(this).datagrid('getRowDetail',index).find('table.clientOutstandingReport_table_DetailViewDG');
	        clientOutstandingReport_table_DetailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,remoteSort:false, height:'auto',
	            columns:[[
	                      {field:'m_strDate',title:'Date',sortable:true,width:40},
	                      {field:'m_strInvoiceNumber',title:'InvoiceNo',sortable:true,width:80},
	                      {field:'m_nInvoiceAmount',title:'Invoice Amount',sortable:true,width:100,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nInvoiceAmount.toFixed(2);	
					        	}
	                      },
	                      {field:'m_nReceiptAmount',title:'Receipt Amount',width:100,sortable:true,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nReceiptAmount.toFixed(2);	
					        	}
	                      },
	                      {field:'m_nBalanceAmount',title:'Balance Amount',width:100,sortable:true,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									return row.m_nBalanceAmount.toFixed(2);	
					        	}
	                      }
	            ]],
	            onResize:function()
	            {
	        		oMainDataGrid.datagrid('fixDetailRowHeight',index);
	            }
	        });
            clientOutstandingReport_getInvoiceList (oMainDataGrid, clientOutstandingReport_table_DetailViewDG, index, row.m_nClientId);
	    }
	});
}

function clientOutstandingReport_getInvoiceList (oMainDataGrid, clientOutstandingReport_table_DetailViewDG, index, nClientId)
{
	assert.isObject(oMainDataGrid, "oMainDataGrid expected to be an Object.");
	assert( Object.keys(oMainDataGrid).length >0 , "oMainDataGrid cannot be an empty .");// checks for non emptyness 
//	assert.isObject(row, "row expected to be an Object.");
//	assert.isString(clientOutstandingReport_table_DetailViewDG, "clientOutstandingReport_table_DetailViewDG expected to be a string.");
//	assert(clientOutstandingReport_table_DetailViewDG !== "", "clientOutstandingReport_table_DetailViewDG cannot be an empty string");
//	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	clearGridData (clientOutstandingReport_table_DetailViewDG);
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oClientData.m_nClientId = nClientId;
	if(m_oClientOutstandingMemberData.m_bIsReportAgeWise)
	{
		var arrDates = m_oClientOutstandingMemberData.m_strSelectedPeriod.split("-");
		var strFromDate = arrDates.length > 1 ? getDateFromDays(-parseInt(arrDates[1])) : "";
		oInvoiceData.m_strFromDate = strFromDate;
		oInvoiceData.m_strToDate = getDateFromDays(-parseInt(arrDates[0]));
	}
	InvoiceDataProcessor.list(oInvoiceData, "", "", "", "", function (oResponse)
	{
		var arrInvoiceData = oResponse.m_arrInvoice;
		for(var nIndex = 0; nIndex < arrInvoiceData.length; nIndex++)
		{
			var oInvoiceData = new InvoiceData ();
			oInvoiceData.m_strDate = arrInvoiceData[nIndex].m_strDate;
			oInvoiceData.m_strInvoiceNumber = arrInvoiceData[nIndex].m_strInvoiceNumber;
			oInvoiceData.m_nInvoiceAmount = arrInvoiceData[nIndex].m_nInvoiceAmount;
			oInvoiceData.m_nReceiptAmount = arrInvoiceData[nIndex].m_nReceiptAmount;
			oInvoiceData.m_nBalanceAmount = oInvoiceData.m_nInvoiceAmount - oInvoiceData.m_nReceiptAmount;
			if(Math.round(oInvoiceData.m_nBalanceAmount) > 0)
				clientOutstandingReport_table_DetailViewDG.datagrid('appendRow',oInvoiceData);
		}
		oMainDataGrid.datagrid('fixDetailRowHeight',index);
		HideDialog ("dialog");
	})
}

function clientOutstandingReport_filter ()
{
	if(m_oClientOutstandingMemberData.m_bIsReportAgeWise)
		clientOutstandingReport_generateAgeWiseReport ();
	else
		clientOutstandingReport_generateClientOutStandingReport ();
}

function clientOutstandingReport_generateClientOutStandingReport ()
{
//	loadPage ("inventorymanagement/progressbar.html", "dialog", "clientOutstandingReport_progressbarLoaded ()");
	var oDataGrid = $("#clientOutstandingReport_table_clientOutstandingReportDG").datagrid();
	var oInvoiceData = clientOutstandingReport_getFormData ();
	oInvoiceData.m_bIsForClientOutstanding = true;
	InvoiceDataProcessor.list(oInvoiceData, "", "", "", "", function(oResponse) 
		{
			clientOutstandingReport_listed (oResponse, oDataGrid);
		});
}

function clientOutstandingReport_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
}

function clientOutstandingReport_getFormData ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oClientData.m_strCompanyName =$("#clientOutstandingReport_input_clientName").val();
	return oInvoiceData;
}

function clientOutstandingReport_listed (oResponse, oDataGrid)
{
	//server response
	//assert.isString(oDataGrid, "oDataGrid expected to be a string.");
	clearGridData (oDataGrid);
	var nOpeningBalanceTotal = 0;
	var nInvoicedAmountTotal = 0;
	var nReceiptAmountTotal = 0;
	var nOutstandingTotal = 0;
	for(var nIndex = 0; nIndex < oResponse.m_arrInvoice.length; nIndex++)
	{
		var oInvoiceData = oResponse.m_arrInvoice[nIndex];
		oInvoiceData.m_nClientId = oInvoiceData[0].m_nClientId;
		oInvoiceData.m_strCompanyName = oInvoiceData[0].m_strCompanyName;
		oInvoiceData.m_strCity = oInvoiceData[0].m_strCity;
		oInvoiceData.m_nOpeningBalance = oInvoiceData[0].m_nOpeningBalance;
		oInvoiceData.m_nInvoiceAmount = oInvoiceData[1];
		oInvoiceData.m_nReceiptAmount = oInvoiceData[2];
		oInvoiceData.m_nBalanceAmount = oInvoiceData[3];
		nOpeningBalanceTotal += oInvoiceData.m_nOpeningBalance;
		nInvoicedAmountTotal += oInvoiceData[1];
		nReceiptAmountTotal	+= oInvoiceData[2];
		nOutstandingTotal += oInvoiceData[3];
		oDataGrid.datagrid('appendRow',oInvoiceData);
	}
	oDataGrid.datagrid('reloadFooter',[{m_strCompanyName:'',m_strCity:'<b>Total :</b>', m_nOpeningBalance:nOpeningBalanceTotal,m_nInvoiceAmount:nInvoicedAmountTotal,m_nReceiptAmount:nReceiptAmountTotal,m_nBalanceAmount:nOutstandingTotal}]);
	HideDialog ("dialog");
}

function clientOutstandingReport_getInfo (nClientId)
{
	assert.isNumber(nClientId, "nClientId expected to be a Number.");
	m_oClientOutstandingMemberData.m_oSelectedClientId = nClientId;
	navigate ("clientTransactionForReport", "widgets/clientmanagement/clientTransactionForReport.js");
}

function clientOutstandingReport_printReport ()
{
	var arrReports = $('#clientOutstandingReport_table_clientOutstandingReportDG').datagrid('getRows');
	var arrClientData = clientOutstandingReport_getClientData (arrReports);
	clientOutstandingReport_printClientList (arrClientData);
}

function clientOutstandingReport_printClientList (arrClientData)
{
	var xmlDoc = loadXMLDoc("include/default.xml");
	m_oClientOutstandingMemberData.m_strXMLData = generateXML (xmlDoc, arrClientData, "root", "ClientOutstandingList");;
	navigate ('Print client outstanding','widgets/reportmanagement/clientoutstanding/clientOutstandingReportPrint.js');
}

function clientOutstandingReport_getClientData (arrReports)
{
	assert.isArray(arrReports, "arrReports expected to be an Array.");
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oClientOutstandingReportData = new ClientOutstandingReportData ();
		oClientOutstandingReportData.m_strCompanyName = arrReports [nIndex].m_strCompanyName;
		oClientOutstandingReportData.m_strCity = arrReports [nIndex].m_strCity;
		oClientOutstandingReportData.m_nOpeningBalance = arrReports [nIndex].m_nOpeningBalance;
		oClientOutstandingReportData.m_nInvoiced =  arrReports [nIndex].m_nInvoiceAmount;
		oClientOutstandingReportData.m_nReceipts = arrReports [nIndex].m_nReceiptAmount;
		oClientOutstandingReportData.m_nOutstandingAmount = arrReports [nIndex].m_nBalanceAmount;
		arrReportData.push (oClientOutstandingReportData);
	}
	return arrReportData;
}

function clientOutstandingReport_initializeAgeWiseDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#clientOutstandingReport_table_clientOutstandingReportDG");
	$('#clientOutstandingReport_table_clientOutstandingReportDG').datagrid
	(
		{
			fit:true,
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
				{field:'m_nAmount',title:'Outstanding Amount',sortable:true,width:100,align:'right',
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
				m_oClientOutstandingMemberData.m_oClientData = rowData[0];
			}
		}
	);
	clientOutstandingReport_generateAgeWiseReport ();
	clientOutstandingReport_initAgeWiseClientsubGrid ();
}

function clientOutstandingReport_generateAgeWiseReport ()
{
//	loadPage ("inventorymanagement/progressbar.html", "dialog", "clientOutstandingReport_progressbarLoadedAgeWise ()");
	clientOutstandingReport_progressbarLoadedAgeWise ();
}

function clientOutstandingReport_progressbarLoadedAgeWise ()
{
	createPopup('dialog', '', '', true);
	var oInvoiceData = clientOutstandingReport_getFormData ();
	InvoiceDataProcessor.getAgeWiseInvoices(oInvoiceData, clientOutstandingReport_listedAgeWise)
}

function clientOutstandingReport_listedAgeWise (arrAgeWiseData)
{
	assert.isArray(arrAgeWiseData, "arrAgeWiseData expected to be an Array.");
	var nTotalInvoices = 0;
	var nTotalInvoiceAmount = 0;
	clearGridData ("#clientOutstandingReport_table_clientOutstandingReportDG");
	for(var nIndex = 0; nIndex < arrAgeWiseData.length; nIndex++)
	{
		var oInvoiceData = new InvoiceData ();
		oInvoiceData.m_strPeriod = arrAgeWiseData[nIndex][0];
		oInvoiceData.m_nInvoicesCount = arrAgeWiseData[nIndex][1];
		oInvoiceData.m_nAmount = arrAgeWiseData[nIndex][2];
		nTotalInvoices += arrAgeWiseData[nIndex][1];
		nTotalInvoiceAmount += arrAgeWiseData[nIndex][2];
		$('#clientOutstandingReport_table_clientOutstandingReportDG').datagrid('appendRow',oInvoiceData);
	}
	$('#clientOutstandingReport_table_clientOutstandingReportDG').datagrid('reloadFooter',[{m_strPeriod : '<b>Total :</b>', m_nInvoicesCount:'<b>'+ nTotalInvoices +'</b>', m_nAmount : nTotalInvoiceAmount}]);
	HideDialog ("dialog");
}

function clientOutstandingReport_initAgeWiseClientsubGrid ()
{
	$('#clientOutstandingReport_table_clientOutstandingReportDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="clientOutstandingReport_table_AgeWiseClintSubGrid"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	    	m_oClientOutstandingMemberData.m_nIndex = index;
	    	clientOutstandingReport_getAgeWiseClientInfo (row.m_strPeriod);
	    }
	});
}

function clientOutstandingReport_getAgeWiseClientInfo (strPeriod)
{
	assert.isString(strPeriod, "strPeriod is expected to be of type string.");
	m_oClientOutstandingMemberData.m_strSelectedPeriod = strPeriod;
	navigate ("ageWiseClientInformation", "widgets/reportmanagement/clientoutstanding/ageWiseClientDetails.js");
}
