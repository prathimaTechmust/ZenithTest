var vendorOutstandingReport_includeDataObjects =
[
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/vendorpurchaseorder/VendorPurchaseOrderData.js',
	'widgets/vendorpurchaseorder/VendorPOLineItemData.js',
	'widgets/reportmanagement/vendoroutstanding/VendorOutstandingReportData.js'
];

 includeDataObjects (vendorOutstandingReport_includeDataObjects, "vendorOutstandingReport_init()");

function vendorOutstandingReport_memberData ()
{
	this.m_oSelectedVendorId = -1;
	this.m_oVendorData = "";
	this.m_strXMLData = "";
	this.m_nIndex = -1;
}

var m_oVendorOutstandingReport_memberData = new vendorOutstandingReport_memberData ();

function vendorOutstandingReport_init ()
{
	loadPage ("reportmanagement/vendoroutstanding/vendorOutstandingReport.html", "workarea", "vendorOutstandingReport_loaded ()");
}

function vendorOutstandingReport_loaded ()
{
	vendorOutstandingReport_onChangeReportTypeDD ();
}

function vendorOutstandingReport_onChangeReportTypeDD ()
{
	var strSelectedDD = $("#vendorOutstandingReport_select_by").val ();
	if(strSelectedDD == 'vendorwise')
	{
		document.getElementById ("vendorOutstandingReport_button_print").style.visibility="visible";
		m_oVendorOutstandingReport_memberData.m_bIsReportAgeWise = false;
		vendorOutstandingReport_initializeDataGrid ();
	}
	else
	{
		document.getElementById ("vendorOutstandingReport_button_print").style.visibility="hidden";
		m_oVendorOutstandingReport_memberData.m_bIsReportAgeWise = true;
		vendorOutstandingReport_initializeAgeWiseDataGrid ();
	}
}

function vendorOutstandingReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#vendorOutstandingReport_table_vendorOutstandingReportDG");
	var oDataGrid = $("#vendorOutstandingReport_table_vendorOutstandingReportDG").datagrid();
	vendorOutstandingReport_initVendorDetailsDG (oDataGrid);
	vendorOutstandingReport_list ();
}


function vendorOutstandingReport_getFormData ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oVendorData.m_strCompanyName = $("#vendorOutstandingReport_input_vendorName").val();
	oPurchaseData.m_oVendorData.m_strCity = $("#vendorOutstandingReport_input_address").val();
	return oPurchaseData;
}

function vendorOutstandingReport_initVendorDetailsDG (oDataGrid)
{
	assert.isObject(oDataGrid, "oDataGrid expected to be an Object.");
	oDataGrid.datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:150,
			  		formatter:function(value,row,index)
		        	{
				  		try
			  			{
				  			return row.m_strCompanyName;
			  			}
			  			catch(oException)
			  			{
			  				return '';
			  			}
		        	}
			  	},
			  	{field:'m_strCity',title:'Address',sortable:true,width:150,
		        		formatter:function(value,row,index)
			        	{
					  		try
				  			{
					  			return row.m_strCity;
				  			}
				  			catch(oException)
				  			{
				  				return '';
				  			}
			        	}	
			  	},
			  	{field:'m_nOpeningBalance',title:'Opening Balance',sortable:true,align:'right',width:100,
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
				{field:'m_nInvoiceAmount',title:'Invoiced',sortable:true,align:'right',width:100,
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
				{field:'m_nPaymentAmount',title:'Payments',width:100,align:'right',sortable:true,
					formatter:function(value,row,index)
		        	{
			        	try
						{
							var m_nIndianNumber = formatNumber (row.m_nPaymentAmount.toFixed(2));
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
				{field:'m_nBalanceAmount',title:'Outstanding',sortable:true,width:100,align:'right',
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
		        {field:'Action',title:'Action',sortable:false,width:40,align:'center',
		        	formatter:function(value,row,index)
		        	{
		        		return vendorOutstandingReport_addActions (row.m_strCompanyName, row.m_nClientId);
		        	}
		        }
			]],
			onSelect: function (rowIndex, rowData)
			{
				m_oVendorOutstandingReport_memberData.m_oVendorData = rowData[0];
			}
		}
	);
	vendorOutstandingReport_subGridPurchase (oDataGrid);
}

function vendorOutstandingReport_addActions (strCompanyName, nClientId)
{
	assert.isString(strCompanyName, "strCompanyName expected to be a string.");
//	assert.isNumber(nClientId, "nClientId expected to be a Number."); // we don't assert and also check for the range of the number here because it shows undefined for the last row
	if(strCompanyName != "")
		return '<table align="center">'+
		'<tr>'+'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="vendorOutstandingReport_getInfo ('+nClientId+')"/> </td>'+
		'</tr>'+
		'</table>';
}

function vendorOutstandingReport_subGridPurchase (oDataGrid)
{
	assert.isObject(oDataGrid, "oDataGrid expected to be an Object.");
	oDataGrid.datagrid({view: detailview,detailFormatter:function(index,row)
		{
            return '<div style="padding:2px"><table class="vendorOutstandingReport_table_detailViewDG"></table></div>';
        },
        onExpandRow: function(index,row)
        {
            var vendorOutstandingReport_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.vendorOutstandingReport_table_detailViewDG');
            vendorOutstandingReport_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
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
            		oDataGrid.datagrid('fixDetailRowHeight',index);
                }
            });
            vendorOutstandingReport_subgridListed (oDataGrid,vendorOutstandingReport_table_detailViewDG, index, row);
        }
    });
}

function vendorOutstandingReport_filter ()
{
	if(m_oVendorOutstandingReport_memberData.m_bIsReportAgeWise)
		vendorOutstandingReport_generateAgeWiseReport ();
	else
		vendorOutstandingReport_list ();
}

function vendorOutstandingReport_list ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "vendorOutstandingReport_progressbarLoaded ()");
	var oMainDataGrid = $("#vendorOutstandingReport_table_vendorOutstandingReportDG").datagrid();
	var oPurchaseData = vendorOutstandingReport_getFormData ();
	oPurchaseData.m_bIsForVendorOutstanding = true;
	PurchaseDataProcessor.list(oPurchaseData, "" , "", "", "", function(oResponse) 
		{
			vendorOutstandingReport_gotVendorOutstandingList(oResponse, oMainDataGrid);
		});
}

function vendorOutstandingReport_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
}

function vendorOutstandingReport_gotVendorOutstandingList (oResponse, oMainDataGrid)
{
	assert.isObject(oMainDataGrid, "oMainDataGrid expected to be an Object.");
	clearGridData (oMainDataGrid);
	var nTotalOpeningBalance = 0;
	var nTotalInvoicedAmount = 0;
	var nTotalPaymentAmount = 0;
	var nTotalOutstandingAmount = 0;
	for (var nIndex = 0; nIndex < oResponse.m_arrPurchase.length; nIndex++)
	{
		var oPurchaseData = oResponse.m_arrPurchase[nIndex];
		oPurchaseData.m_nClientId = oPurchaseData[0].m_nClientId;
		oPurchaseData.m_strCompanyName = oPurchaseData[0].m_strCompanyName;
		oPurchaseData.m_strCity = oPurchaseData[0].m_strCity;
		oPurchaseData.m_nOpeningBalance = oPurchaseData[0].m_nOpeningBalance;
		oPurchaseData.m_nInvoiceAmount = oPurchaseData[1];
		oPurchaseData.m_nPaymentAmount = oPurchaseData[2];
		oPurchaseData.m_nBalanceAmount = (oPurchaseData.m_nInvoiceAmount + oPurchaseData.m_nOpeningBalance) - oPurchaseData.m_nPaymentAmount;
		nTotalOpeningBalance += oPurchaseData.m_nOpeningBalance;
		nTotalInvoicedAmount += oPurchaseData.m_nInvoiceAmount;
		nTotalPaymentAmount	+= oPurchaseData.m_nPaymentAmount;
		nTotalOutstandingAmount += oPurchaseData.m_nBalanceAmount;
		oMainDataGrid.datagrid('appendRow',oPurchaseData);
	}
	oMainDataGrid.datagrid('reloadFooter',[{m_strCompanyName:'',m_strCity:'<b>Grand Total</b>', m_nOpeningBalance: nTotalOpeningBalance, m_nInvoiceAmount:nTotalInvoicedAmount, m_nPaymentAmount: nTotalPaymentAmount, m_nBalanceAmount: nTotalOutstandingAmount}]);
	HideDialog ("dialog");
}

function vendorOutstandingReport_subgridListed (oDataGrid,vendorOutstandingReport_table_detailViewDG, index, row)
{
 	assert.isObject(row, "row expected to be an Object.");
 	assert.isNumber(index, "index expected to be a Number.");
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	clearGridData (vendorOutstandingReport_table_detailViewDG);
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oVendorData.m_nClientId = row.m_nClientId;
	oPurchaseData.m_bIsForAll = true;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	if(m_oVendorOutstandingReport_memberData.m_bIsReportAgeWise)
	{
		var arrDates = m_oVendorOutstandingReport_memberData.m_oSelectedPeriod.split("-");
		var strFromDate = arrDates.length > 1 ? getDateFromDays(-parseInt(arrDates[1])) : "";
		oPurchaseData.m_strFromDate = strFromDate;
		oPurchaseData.m_strToDate = getDateFromDays(-parseInt(arrDates[0]));
	}
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
				vendorOutstandingReport_table_detailViewDG.datagrid('appendRow',oPurchaseData);
		}
		oDataGrid.datagrid('fixDetailRowHeight',index);
		HideDialog ("dialog");
	})
}

function vendorOutstandingReport_getInfo (nVendorId)
{
	assert.isNumber(nVendorId, "nVendorId expected to be a Number.");
	m_oVendorOutstandingReport_memberData.m_oSelectedVendorId = nVendorId;
	navigate ("vendorTransactionForReport", "widgets/vendormanagement/vendorTransactionForReport.js");
}

function vendorOutstandingReport_print ()
{
	var arrReports = $('#vendorOutstandingReport_table_vendorOutstandingReportDG').datagrid('getRows');
	var arrVendorOutstandingData = vendorOutstandingReport_getReportData (arrReports);
	vendorOutstandingReport_printVendorList (arrVendorOutstandingData);
}

function vendorOutstandingReport_printVendorList (arrVendorOutstandingData) 
{
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strVendorFilterBox", $("#vendorOutstandingReport_input_vendorName").val());
	addChild(xmlDoc, "root", "m_strAddressFilterBox", $("#vendorOutstandingReport_input_address").val());
	m_oVendorOutstandingReport_memberData.m_strXMLData = generateXML (xmlDoc, arrVendorOutstandingData, "root", "VendorOutstandingReportDataList");
	navigate ('reportPrint','widgets/reportmanagement/vendoroutstanding/vendorOutstandingReportPrint.js');
}

function vendorOutstandingReport_getReportData (arrReports)
{
	assert.isArray(arrReports, "arrReports expected to be an Array.");
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oVendorOutstandingReportData = new VendorOutstandingReportData ();
		oVendorOutstandingReportData.m_strCompanyName = arrReports [nIndex].m_strCompanyName;
		oVendorOutstandingReportData.m_strCity = arrReports [nIndex].m_strCity;
		oVendorOutstandingReportData.m_nOpeningBalance = arrReports [nIndex].m_nOpeningBalance;
		oVendorOutstandingReportData.m_nInvoicedAmount =  arrReports [nIndex].m_nInvoiceAmount;
		oVendorOutstandingReportData.m_nPaymentAmount = arrReports [nIndex].m_nPaymentAmount;
		oVendorOutstandingReportData.m_nOutstandingAmount = arrReports [nIndex].m_nBalanceAmount;
		arrReportData.push (oVendorOutstandingReportData);
	}
	return arrReportData;
}

function vendorOutstandingReport_initializeAgeWiseDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#vendorOutstandingReport_table_vendorOutstandingReportDG");
	$('#vendorOutstandingReport_table_vendorOutstandingReportDG').datagrid
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
				m_oVendorOutstandingReport_memberData.m_oVendorData = rowData[0];
			}
		}
	);
	vendorOutstandingReport_generateAgeWiseReport ();
	vendorOutstandingReport_initAgeWiseVendorsubGrid ();
}

function vendorOutstandingReport_initAgeWiseVendorsubGrid ()
{
	$('#vendorOutstandingReport_table_vendorOutstandingReportDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="vendorOutstandingReport_table_AgeWiseVendorSubGrid"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	    	m_oVendorOutstandingReport_memberData.m_nIndex = index;
	    	vendorOutstandingReport_popupVendorDG (row.m_strPeriod);
	    }
	});
}

function vendorOutstandingReport_generateAgeWiseReport ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "vendorOutstandingReport_progressbarLoadedAgeWise ()");
}

function vendorOutstandingReport_progressbarLoadedAgeWise ()
{
	createPopup('dialog', '', '', true);
	var oPurcahseData = vendorOutstandingReport_getFormData ();
	PurchaseDataProcessor.getAgeWiseInvoices(oPurcahseData, vendorOutstandingReport_listedAgeWise)
}

function vendorOutstandingReport_listedAgeWise (arrAgeWiseData)
{
	assert.isArray(arrAgeWiseData, "arrAgeWiseData expected to be an Array.");
	var nTotalInvoices = 0;
	var nTotalInvoiceAmount = 0;
	clearGridData ("#vendorOutstandingReport_table_vendorOutstandingReportDG");
	$('#vendorOutstandingReport_table_vendorOutstandingReportDG').datagrid('reloadFooter');
	for(var nIndex = 0; nIndex < arrAgeWiseData.length; nIndex++)
	{
		var oPurchaseData = new PurchaseData ();
		oPurchaseData.m_strPeriod = arrAgeWiseData[nIndex][0];
		oPurchaseData.m_nInvoicesCount = arrAgeWiseData[nIndex][1];
		oPurchaseData.m_nAmount = arrAgeWiseData[nIndex][2];
		nTotalInvoices += arrAgeWiseData[nIndex][1];
		nTotalInvoiceAmount += arrAgeWiseData[nIndex][2];
		$('#vendorOutstandingReport_table_vendorOutstandingReportDG').datagrid('appendRow',oPurchaseData);
	}
	$('#vendorOutstandingReport_table_vendorOutstandingReportDG').datagrid('reloadFooter',[{m_strPeriod : '<b>Total :</b>', m_nInvoicesCount:'<b>'+ nTotalInvoices +'</b>', m_nAmount : nTotalInvoiceAmount}]);
	HideDialog ("dialog");
}

function vendorOutstandingReport_popupVendorDG (strPeriod)
{
	assert.isString(strPeriod, "strPeriod expected to be a string.");
	m_oVendorOutstandingReport_memberData.m_oSelectedPeriod = strPeriod;
	navigate ("ageWiseVendorInformation", "widgets/reportmanagement/vendoroutstanding/ageWiseVendorInfo.js");
}
