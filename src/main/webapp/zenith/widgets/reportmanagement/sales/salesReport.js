var salesReport_includeDataObjects =
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/reportmanagement/sales/SalesReportData.js'
]

 includeDataObjects (salesReport_includeDataObjects, "salesReport_loaded ()");

function salesReport_initMyReport ()
{
	var oUpdateGridName = document.getElementById("salesReport_table_salesReportDG");
	oUpdateGridName.title = "Sales Report ";
	m_oSalesReportMemberData.m_bIsMyReport = true;
	salesReport_initReport ();
	initUserCombobox ('#salesReport_input_soldBy', "Sold By", m_oSalesReportMemberData.m_oKeyDownHandler);
	$('#salesReport_input_soldBy').combobox('destroy');
}

function salesReport_initOverallReport ()
{
	initUserCombobox ('#salesReport_input_soldBy', "Sold By", m_oSalesReportMemberData.m_oKeyDownHandler);
	salesReport_initReport ();
	var oUpdateGridName = document.getElementById("salesReport_table_salesReportDG");
	oUpdateGridName.title = "Overall Sales Report";
}

function salesReport_initReport ()
{
	loadPage ("reportmanagement/sales/reportDuration.html", "dialog", "salesReport_initDuration ()");
}

function MySalesReport_memberData ()
{
	this.m_bIsMyReport = false;
	this.m_arrSoldBy = new Array ();
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_strXMLData = "";
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			salesReport_generateSalesReport ();
			$('#salesReport_input_soldBy').combobox('textbox').unbind('keydown', m_oSalesReportMemberData.m_oKeyDownHandler);
			$('#salesReport_input_soldBy').combobox('textbox').focus();
		}
    };
    this.bIsClientSelected = false;
    this.m_nSelectedSalesId = -1;
    this.m_strColumn = "m_strCompanyName";
    this.m_strOrderBy = "asc";
}

var m_oSalesReportMemberData = new MySalesReport_memberData ();

function salesReport_initDuration ()
{
	createPopup("dialog", "#reportDuration_button_cancel", "#reportDuration_button_create", true);
	$("#reportDuration_input_dateFrom").datebox();
	$("#reportDuration_input_dateTo").datebox();
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
	$('#reportDuration_input_dateFrom').datebox('setValue', dCurrentDate);
	$('#reportDuration_input_dateTo').datebox('setValue', dCurrentDate);
}

function salesReport_processReport ()
{
	m_oSalesReportMemberData.m_strFromDate = FormatDate ($('#reportDuration_input_dateFrom').datebox('getValue'));
	m_oSalesReportMemberData.m_strToDate = FormatDate ($('#reportDuration_input_dateTo').datebox('getValue'));
	var strSelectedDD = $("#reportDuration_select_by").val();
	if(strSelectedDD == 'Clients')
		m_oSalesReportMemberData.bIsClientSelected = true;
	salesReport_generateSalesReport ();
}

function salesReport_getFormData ()
{
	var oSalesData = new SalesData ();
	oSalesData = salesReport_getData ();
	if(!m_oSalesReportMemberData.bIsClientSelected)
	{
		var nUserId = m_oSalesReportMemberData.m_bIsMyReport  ? m_oTrademustMemberData.m_nUserId : $('#salesReport_input_soldBy').combobox('getValue');
		oSalesData.m_oCreatedBy.m_nUserId = nUserId > 0 ? nUserId : -1;
	}
	return oSalesData;
}

function salesReport_getData ()
{
	var oSalesData = new SalesData ();
	oSalesData.m_strTo =$("#salesReport_input_soldTo").val();
	oSalesData.m_strInvoiceNo = $("#salesReport_input_invoiceNumber").val();
	oSalesData.m_strFromDate = (m_oSalesReportMemberData.m_strFromDate != "undefined--undefined") ? m_oSalesReportMemberData.m_strFromDate : null;
	oSalesData.m_strToDate = (m_oSalesReportMemberData.m_strToDate != "undefined--undefined") ? m_oSalesReportMemberData.m_strToDate : null;
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oSalesData;
}

function salesReport_generateSalesReport ()
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "salesReport_progressbarLoaded ()");
}

function salesReport_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oSalesData = salesReport_getFormData ();
	HideDialog ("dialog");
	var oUpdateGridName = document.getElementById("salesReport_table_salesReportDG");
	oUpdateGridName.title += " [ From : " + oSalesData.m_strFromDate + " - To : " + oSalesData.m_strToDate + " ]";
	if(m_oSalesReportMemberData.bIsClientSelected == true)
	{
		var oUpdateGridName = document.getElementById("salesReport_table_salesReportDG");
		oUpdateGridName.title = "Client Sales Report " + " [ From : " + oSalesData.m_strFromDate + " - To : " + oSalesData.m_strToDate + " ]";
		salesReport_initializeClientDataGrid ();
		$('#salesReport_input_soldBy').combobox('destroy');
		SalesDataProcessor.getClientReport(oSalesData, m_oSalesReportMemberData.m_strColumn, m_oSalesReportMemberData.m_strOrderBy, salesReport_clientListed);
	}
	else
	{
		salesReport_initializeDataGrid ();
		SalesDataProcessor.list(oSalesData, "", "", "", "", salesReport_listed);
	}
}

function salesReport_getClientReport (strColumn, strOrder)
{
	var oSalesData = new SalesData ();
	oSalesData = salesReport_getData ();
	SalesDataProcessor.getClientReport(oSalesData, strColumn, strOrder, salesReport_clientListed);
}

function salesReport_initializeClientDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#salesReport_table_salesReportDG");
	$('#salesReport_table_salesReportDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'Client Name',sortable:true,width:80,
			  		formatter:function(value,row,index)
		        	{
				  		try
			  			{
				  			return row.m_oClientData.m_strCompanyName;
			  			}
			  			catch(oException)
			  			{
			  				return row.m_strCompanyName;
			  			}
		        	}
			  	},
			  	{field:'m_nAmount',title:'Total Sales',width:20,align:'right',sortable:true,
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
			]],
			onSortColumn: function (strColumn, strOrder)
			{
				if(strColumn != "m_nAmount")
					salesReport_getClientReport (strColumn, strOrder);
			}
		}
	);
	salesReport_subGridClientSales ();
}

function salesReport_subGridClientSales ()
{
	$('#salesReport_table_salesReportDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="salesReport_table_clientDetailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  salesReport_table_clientDetailViewDG = $(this).datagrid('getRowDetail',index).find('table.salesReport_table_clientDetailViewDG');
	        salesReport_table_clientDetailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
	            columns:[[
	            {field:'m_strDate',title:'Date',sortable:true,width:40},
	                {field:'m_strInvoiceNo',title:'Invoice No',width:60,align:'right',sortable:true},
	                {field:'m_nAmount',title:'Amount',width:120,align:'right',sortable:true,
	                	formatter:function(value,row,index)
			        	{
							 var nAmount = getTotalAmount (row);
							 var value = nAmount.toFixed(2);
							 var m_nIndianNumber = formatNumber (value,row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}
	                },
	                {field:'Actions',title:'Action',width:20,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return salesReport_displayImages (row, index);
			        	}
					}
	            ]],
	            onResize:function()
	            {
	                $('#salesReport_table_salesReportDG').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#salesReport_table_salesReportDG').datagrid('fixDetailRowHeight',index);
	        salesReport_subgridClientListed (salesReport_table_clientDetailViewDG, index, row);
	    }
	});
}

function getTotalAmount(oRow)
{
	assert.isObject(oRow, "oRow expected to be an Object.");
	assert( Object.keys(oRow).length >0 , "oRow cannot be an empty .");// checks for non emptyness 
	var nAmount = 0;
	nAmount += salesReport_calculateSalesLineItemsTotal (oRow.m_oSalesLineItems);
	nAmount += salesReport_calculateSalesLineItemsTotal (oRow.m_oNonStockSalesLineItems);
	return nAmount;
}

function salesReport_subgridClientListed (salesReport_table_clientDetailViewDG, index, row)
{
 	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	clearGridData (salesReport_table_clientDetailViewDG);
	for(var nIndex = 0; nIndex < row.m_arrSalesData.length; nIndex++)
		salesReport_table_clientDetailViewDG.datagrid('appendRow',row.m_arrSalesData[nIndex]);
}

function salesReport_clientListed (arrReportClientData)
{
	assert.isArray(arrReportClientData, "arrReportClientData expected to be an Array.");
	clearGridData ("#salesReport_table_salesReportDG");
	var nGrandTotal = 0;
	for (var nIndex = 0; nIndex < arrReportClientData.length; nIndex++)
	{
		arrReportClientData[nIndex].m_nAmount = salesReport_calculateSalesClientRowTotal (arrReportClientData[nIndex].m_arrSalesData);
		arrReportClientData[nIndex].m_nAmount += salesReport_calculateNSClientRowTotal (arrReportClientData[nIndex].m_arrSalesData);
		nGrandTotal += Number (arrReportClientData[nIndex].m_nAmount);
		$('#salesReport_table_salesReportDG').datagrid('appendRow',arrReportClientData[nIndex]);
	}
	$('#salesReport_table_salesReportDG').datagrid('reloadFooter',[{m_strCompanyName:'<div style="font-weight:bold; text-align:right">Grand Total :</div>', m_nAmount:nGrandTotal}]);
	HideDialog ("secondDialog"); 
}

function salesReport_calculateSalesClientRowTotal (arrSalesLineItems)
{
	assert.isArray(arrSalesLineItems, "arrSalesLineItems expected to be an Array.");
	var nRowAmount = 0;
	for (var nIndex = 0 ; nIndex < arrSalesLineItems.length; nIndex++)
		nRowAmount += salesReport_calculateSalesLineItemsTotal(arrSalesLineItems[nIndex].m_oSalesLineItems);
	return nRowAmount;
}

function salesReport_calculateNSClientRowTotal (arrSalesLineItems)
{
	assert.isArray(arrSalesLineItems, "arrSalesLineItems expected to be an Array.");
	var nRowAmount = 0;
	for (var nIndex = 0 ; nIndex < arrSalesLineItems.length; nIndex++)
		nRowAmount += salesReport_calculateSalesLineItemsTotal(arrSalesLineItems[nIndex].m_oNonStockSalesLineItems);
	return nRowAmount;
}

function salesReport_calculateSalesLineItemsTotal (arrSalesLineItems)
{
	assert.isArray(arrSalesLineItems, "arrSalesLineItems expected to be an Array.");
	var nTotal = 0;
	for (var nIndex = 0 ; nIndex < arrSalesLineItems.length; nIndex++)
		nTotal += arrSalesLineItems[nIndex].m_nQuantity * (arrSalesLineItems[nIndex].m_nPrice - (arrSalesLineItems[nIndex].m_nPrice * (arrSalesLineItems[nIndex].m_nDiscount/100)));
	return nTotal;
}

function salesReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#salesReport_table_salesReportDG");
	$('#salesReport_table_salesReportDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strUserName',title:'Sold By',sortable:true,width:60,
			  		formatter:function(value,row,index)
		        	{
				  		try
			  			{
				  			return row.m_oCreatedBy.m_strUserName;
			  			}
			  			catch(oException)
			  			{
			  				return '';
			  			}
		        	}
			  	},
			  	{field:'m_strTo',title:'Sold To',sortable:true,width:110,
			  		styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}	
			  	},
				{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:70},
				{field:'m_strDate',title:'Date',sortable:true,width:40},
				{field:'m_nAmount',title:'Amount',sortable:true,width:70,align:'right',
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
	$('#salesReport_table_salesReportDG').datagrid('reloadFooter',[{m_strDate:'Grand Total',m_nAmount:0}]);
	salesReport_subGridSales ();
}

function salesReport_subGridSales ()
{
	$('#salesReport_table_salesReportDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="salesReport_table_detailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  salesReport_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.salesReport_table_detailViewDG');
	        salesReport_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
	            columns:[[
	                {field:'m_strItemName',title:'Item Name',width:80},
	                {field:'m_strDetail',title:'Details',width:170},
	                {field:'m_nQuantity',title:'Quantity',width:60,align:'right'},
	                {field:'m_nPrice',title:'Price',width:60,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                	     var m_nIndianNumber = formatNumber (value,row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}	
	                },
	                {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right'},
	                {field:'m_nAmount',title:'Amount',width:60,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                		 var m_nIndianNumber = formatNumber (value,row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}
	                	}
	            ]],
	            onResize:function()
	            {
	                $('#salesReport_table_salesReportDG').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#salesReport_table_salesReportDG').datagrid('fixDetailRowHeight',index);
	        salesReport_subgridListed (salesReport_table_detailViewDG, index, row);
	    }
	});
}

function salesReport_subgridListed (salesReport_table_detailViewDG, index, row)
{
	 assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var arrSalesLine = new Array ();
	var arrSalesLineItemData = row.m_oSalesLineItems;
	var arrNonStockSalesLineItems = row.m_oNonStockSalesLineItems;
	for (var nIndex = 0; nIndex < arrSalesLineItemData.length; nIndex++)
	{
		arrSalesLineItemData[nIndex].m_strItemName = arrSalesLineItemData[nIndex].m_oItemData.m_strItemName;
		arrSalesLineItemData[nIndex].m_strDetail = arrSalesLineItemData[nIndex].m_oItemData.m_strDetail;
		arrSalesLineItemData[nIndex].m_nQuantity = Number(arrSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrSalesLineItemData[nIndex].m_nPrice = Number(arrSalesLineItemData[nIndex].m_nPrice).toFixed(2);
		arrSalesLineItemData[nIndex].m_nDiscount = Number(arrSalesLineItemData[nIndex].m_nDiscount).toFixed(2);
		arrSalesLineItemData[nIndex].m_nDiscountPrice = (arrSalesLineItemData[nIndex].m_nPrice -(arrSalesLineItemData[nIndex].m_nPrice * (arrSalesLineItemData[nIndex].m_nDiscount/100)));
		arrSalesLineItemData[nIndex].m_nAmount = (arrSalesLineItemData[nIndex].m_nDiscountPrice * arrSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrSalesLine.push(arrSalesLineItemData[nIndex]);
	}
	for (var nIndex = 0; nIndex < arrNonStockSalesLineItems.length; nIndex++)
	{
		arrNonStockSalesLineItems[nIndex].m_strItemName = arrNonStockSalesLineItems[nIndex].m_strArticleDescription;
		arrNonStockSalesLineItems[nIndex].m_nQuantity = Number(arrNonStockSalesLineItems[nIndex].m_nQuantity).toFixed(2);
		arrNonStockSalesLineItems[nIndex].m_nPrice = Number(arrNonStockSalesLineItems[nIndex].m_nPrice).toFixed(2);
		arrNonStockSalesLineItems[nIndex].m_nDiscount = Number(arrNonStockSalesLineItems[nIndex].m_nDiscount).toFixed(2);
		arrNonStockSalesLineItems[nIndex].m_nDiscountPrice = (arrNonStockSalesLineItems[nIndex].m_nPrice -(arrNonStockSalesLineItems[nIndex].m_nPrice * (arrNonStockSalesLineItems[nIndex].m_nDiscount/100)));
		arrNonStockSalesLineItems[nIndex].m_nAmount = (arrNonStockSalesLineItems[nIndex].m_nDiscountPrice * arrNonStockSalesLineItems[nIndex].m_nQuantity).toFixed(2);
		arrSalesLine.push(arrNonStockSalesLineItems[nIndex]);
	}
	salesReport_table_detailViewDG.datagrid('loadData',arrSalesLine);
}

function salesReport_listed (oResponse)
{
	clearGridData ("#salesReport_table_salesReportDG");
	var nGrandTotal = 0;
	var arrSales = oResponse.m_arrSales;
	for (var nIndex = 0; nIndex < arrSales.length; nIndex++)
	{
		arrSales[nIndex].m_nAmount = salesReport_calculateSalesLineItemsTotal (arrSales[nIndex].m_oSalesLineItems);
		arrSales[nIndex].m_nAmount += salesReport_calculateSalesLineItemsTotal (arrSales[nIndex].m_oNonStockSalesLineItems);
		nGrandTotal += Number (arrSales[nIndex].m_nAmount);
		$('#salesReport_table_salesReportDG').datagrid('appendRow',arrSales[nIndex]);
	}
	$('#salesReport_table_salesReportDG').datagrid('reloadFooter',[{m_strDate:'<b>Grand Total :</b>', m_nAmount:nGrandTotal}]);
	HideDialog ("secondDialog"); 
}

function salesReport_cancel ()
{
	HideDialog ("dialog");
}

function salesReport_initSoldByCombobox ()
{
	$('#salesReport_input_soldBy').combobox
	({
		data:m_oSalesReportMemberData.m_arrSoldBy,
		valueField:'m_nUserId',
	    textField:'m_strUserName',
	    formatter: function(row)
	    {
			var opts = $(this).combobox('options');
			return row[opts.textField];
	    },
	    filter:function(q,row)
    	{
    		var opts = $(this).combobox('options');
    		return row[opts.textField].toUpperCase().indexOf(q.trim().toUpperCase()) >= 0;
    	}
	});
	var soldByTextBox = $('#salesReport_input_soldBy').combobox('textbox');
	soldByTextBox[0].placeholder = "Sold By";
}

function salesReport_printSalesReport ()
{
	var arrSalesData = salesReport_getReportData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strFromDate", m_oSalesReportMemberData.m_strFromDate);
	addChild(xmlDoc, "root", "m_strToDate", m_oSalesReportMemberData.m_strToDate);
	if(!m_oSalesReportMemberData.bIsClientSelected)
		addChild(xmlDoc, "root", "m_strSoldByFilterBox", $('#salesReport_input_soldBy').combobox('getText'));
	addChild(xmlDoc, "root", "m_strSoldToFilterBox", $("#salesReport_input_soldTo").val());
	addChild(xmlDoc, "root", "m_strInvoiceNoFilterBox", $("#salesReport_input_invoiceNumber").val());
	strXML = generateXML (xmlDoc, arrSalesData, "root", "SalesReportDataList");
	m_oSalesReportMemberData.m_strXMLData = strXML;
	navigate ('reportPrint','widgets/reportmanagement/sales/salesReportPrint.js');
}

function salesReport_getReportData ()
{
	var arrReports = $('#salesReport_table_salesReportDG').datagrid('getRows');
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oSalesReportData = new SalesReportData ();
		if(!m_oSalesReportMemberData.bIsClientSelected)
		{
			oSalesReportData.m_strSoldBy = arrReports [nIndex].m_oCreatedBy.m_strUserName;
			oSalesReportData.m_strSoldTo = arrReports [nIndex].m_strTo;
			oSalesReportData.m_strInvoiceNo = arrReports [nIndex].m_strInvoiceNo;
			oSalesReportData.m_strDate =  arrReports [nIndex].m_strDate;
			oSalesReportData.m_nAmount = arrReports [nIndex].m_nAmount;
		}
		else
		{
			oSalesReportData.m_strCompanyName = arrReports[nIndex].m_oClientData.m_strCompanyName;
			oSalesReportData.m_nAmount = arrReports[nIndex].m_nAmount;
		}
		arrReportData.push (oSalesReportData);
	}
	return arrReportData;
}

function salesReport_displayImages (oRow, nIndex)
{
 	assert.isObject(oRow, "oRow expected to be an Object.");
 	assert( Object.keys(oRow).length >0 , "oRow cannot be an empty .");// checks for non emptyness 
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="salesReport_getItemDetails ('+oRow.m_nId+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function salesReport_getItemDetails (nSalesId)
{
//	assert.isNumber(nSalesId, "nSalesId expected to be a Number.");
	m_oSalesReportMemberData.m_nSelectedSalesId = nSalesId;
	navigate("","widgets/reportmanagement/sales/salesItemReports.js");
}