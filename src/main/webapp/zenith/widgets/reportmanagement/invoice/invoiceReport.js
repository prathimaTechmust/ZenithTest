var invoiceReport_includeDataObjects =
[
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/reportmanagement/invoice/InvoiceReportData.js'
]

 includeDataObjects (invoiceReport_includeDataObjects, "invoiceReport_loaded ()");

function invoiceReport_memberData ()
{
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_strXMLData = "";
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			invoiceReport_generateInvoiceReport();
			$('#invoiceReport_input_soldBy').combobox('textbox').unbind('keydown', m_oInvoiceReportMemberData.m_oKeyDownHandler);
			$('#invoiceReport_input_soldBy').combobox('textbox').focus();
		}
    };
}

var m_oInvoiceReportMemberData = new invoiceReport_memberData ();

function invoiceReport_initDuration ()
{
	createPopup("dialog", "#reportDuration_button_cancel", "#reportDuration_button_create", true);
	$("#reportDuration_input_dateFrom").datebox();
	$("#reportDuration_input_dateTo").datebox();
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
	$('#reportDuration_input_dateFrom').datebox('setValue', dCurrentDate);
	$('#reportDuration_input_dateTo').datebox('setValue', dCurrentDate);
}

function invoiceReport_initOverallReport ()
{
	initUserCombobox ('#invoiceReport_input_soldBy', "Sold By", m_oInvoiceReportMemberData.m_oKeyDownHandler);
	invoiceReport_initReport ();
}

function invoiceReport_initReport ()
{
	loadPage ("reportmanagement/invoice/reportDuration.html", "dialog", "invoiceReport_initDuration ()");
}

function invoiceReport_processReport ()
{
	m_oInvoiceReportMemberData.m_strFromDate = FormatDate ($('#reportDuration_input_dateFrom').datebox('getValue'));
	m_oInvoiceReportMemberData.m_strToDate = FormatDate ($('#reportDuration_input_dateTo').datebox('getValue'));
	invoiceReport_generateInvoiceReport ();
}

function invoiceReport_getFormData ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oCreatedBy.m_nUserId =$('#invoiceReport_input_soldBy').combobox('getValue');;
	oInvoiceData.m_oClientData.m_strCompanyName =$("#invoiceReport_input_soldTo").val();
	oInvoiceData.m_strInvoiceNumber = $("#invoiceReport_input_invoiceNumber").val();
	oInvoiceData.m_strFromDate = (m_oInvoiceReportMemberData.m_strFromDate != "undefined--undefined") ? m_oInvoiceReportMemberData.m_strFromDate : null;
	oInvoiceData.m_strToDate = (m_oInvoiceReportMemberData.m_strToDate != "undefined--undefined") ? m_oInvoiceReportMemberData.m_strToDate : null;
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	HideDialog ("dialog");
	return oInvoiceData;
}

function invoiceReport_generateInvoiceReport ()
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "invoiceReport_progressbarLoaded ()");
}

function invoiceReport_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oInvoiceData = invoiceReport_getFormData ();
	var oUpdateGridName = document.getElementById("invoiceReport_table_invoiceReportDG");
	oUpdateGridName.title += " [ From : " + oInvoiceData.m_strFromDate + " - To : " + oInvoiceData.m_strToDate + " ]";
	invoiceReport_initializeDataGrid ();
	InvoiceDataProcessor.list(oInvoiceData, "", "", "", "", invoiceReport_listed);
}

function invoiceReport_listed (oResponse)
{
	clearGridData ("#invoiceReport_table_invoiceReportDG");
	var nGrandTotal = 0;
	var arrInvoice = oResponse.m_arrInvoice;
	for (var nIndex = 0; nIndex < arrInvoice.length; nIndex++)
	{
		nGrandTotal += Number (arrInvoice[nIndex].m_nInvoiceAmount);
		$('#invoiceReport_table_invoiceReportDG').datagrid('appendRow',arrInvoice[nIndex]);
	}
	$('#invoiceReport_table_invoiceReportDG').datagrid('reloadFooter',[{m_strDate:'<b>Grand Total :</b>', m_nInvoiceAmount:nGrandTotal}]);
	HideDialog ("secondDialog");
}

function invoiceReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#invoiceReport_table_invoiceReportDG");
	$('#invoiceReport_table_invoiceReportDG').datagrid
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
			  	{field:'m_strCompanyName',title:'Sold To',sortable:true,width:110,
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
		        	},
			  		styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}	
			  	},
				{field:'m_strInvoiceNumber',title:'Invoice No.',sortable:true,width:70},
				{field:'m_strDate',title:'Date',sortable:true,width:40},
				{field:'m_nInvoiceAmount',title:'Amount',width:70,align:'right',sortable:true,
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
	$('#invoiceReport_table_invoiceReportDG').datagrid('reloadFooter',[{m_strDate:'Grand Total',m_nInvoiceAmount:0}]);
	invoiceReport_subGridSales ();
}

function invoiceReport_subGridSales ()
{
	$('#invoiceReport_table_invoiceReportDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="invoiceReport_table_detailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  invoiceReport_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.invoiceReport_table_detailViewDG');
	        invoiceReport_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
	            columns:[[
	                {field:'m_strItemName',title:'Item Name',width:230},
	                {field:'m_strDetail',title:'Details',width:70},
	                {field:'m_nQuantity',title:'Quantity',width:50,align:'right'},
	                {field:'m_nPrice',title:'Price',width:60,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                	     var m_nIndianNumber = formatNumber (value,row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}	
	                },
	                {field:'m_nDiscount',title:'Disc(%)',width:40,align:'right'},
	                {field:'m_nTax',title:'Tax(%)',width:40,align:'right',
	                	formatter:function (value,row,index)
						{
							var nTax = row.m_nTax;
							try
							{
								if (!isNaN(value))
									nTax = nTax.toFixed(2);
								else
									return value;
							}
							 catch(oException){}
								return nTax;
						}	
	                },
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
	                $('#invoiceReport_table_invoiceReportDG').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#invoiceReport_table_invoiceReportDG').datagrid('fixDetailRowHeight',index);
	        invoiceReport_subgridListed (invoiceReport_table_detailViewDG, index, row);
	    }
	});
}

function invoiceReport_subgridListed (invoiceReport_table_detailViewDG, nGridIndex, row)
{
//  	assert.isObject(row, "row expected to be an Object.");
//	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var arrSalesLine = new Array ();
	for (var nIndex = 0; nIndex < row.m_oSalesSet.length; nIndex++)
	{
		var arrSalesLineItemData = row.m_oSalesSet[nIndex].m_oSalesLineItems;
		var arrNonStockSalesLineItemData = row.m_oSalesSet[nIndex].m_oNonStockSalesLineItems;
		invoiceReport_buildLineItems (arrSalesLineItemData, arrNonStockSalesLineItemData, arrSalesLine, invoiceReport_table_detailViewDG);
	}
}

function invoiceReport_buildLineItems (arrSalesLineItemData, arrNonStockSalesLineItemData, arrSalesLine, invoiceReport_table_detailViewDG)
{
//	assert.isArray(arrSalesLineItemData, "arrSalesLineItemData expected to be an Array.");
	for (var nIndex = 0; nIndex < arrSalesLineItemData.length; nIndex++)
	{
		arrSalesLineItemData[nIndex].m_strItemName = arrSalesLineItemData[nIndex].m_oItemData.m_strItemName;
		arrSalesLineItemData[nIndex].m_strDetail = arrSalesLineItemData[nIndex].m_oItemData.m_strDetail;
		arrSalesLineItemData[nIndex].m_nQuantity = Number(arrSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrSalesLineItemData[nIndex].m_nPrice = Number(arrSalesLineItemData[nIndex].m_nPrice).toFixed(2);
		arrSalesLineItemData[nIndex].m_nDiscount = Number(arrSalesLineItemData[nIndex].m_nDiscount).toFixed(2);
		arrSalesLineItemData[nIndex].m_nDiscountPrice = (arrSalesLineItemData[nIndex].m_nPrice -(arrSalesLineItemData[nIndex].m_nPrice * (arrSalesLineItemData[nIndex].m_nDiscount/100)));
		arrSalesLineItemData[nIndex].m_nTaxPrice = (arrSalesLineItemData[nIndex].m_nDiscountPrice * (arrSalesLineItemData[nIndex].m_nTax/100));
		arrSalesLineItemData[nIndex].m_nAmount = ((arrSalesLineItemData[nIndex].m_nDiscountPrice + arrSalesLineItemData[nIndex].m_nTaxPrice )* arrSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrSalesLine.push(arrSalesLineItemData[nIndex]);
	}
//	invoiceReport_listNonStockSalesLineItem (invoiceReport_table_detailViewDG, arrNonStockSalesLineItemData, arrSalesLine);
}

function invoiceReport_listNonStockSalesLineItem (invoiceReport_table_detailViewDG, arrNonStockSalesLineItemData, arrSalesLine)
{
//	assert.isArray(arrNonStockSalesLineItemData, "arrNonStockSalesLineItemData expected to be an Array.");
	for (var nIndex = 0; nIndex < arrNonStockSalesLineItemData.length; nIndex++)
	{
		arrNonStockSalesLineItemData[nIndex].m_strItemName = arrNonStockSalesLineItemData[nIndex].m_strArticleDescription;
		arrNonStockSalesLineItemData[nIndex].m_strDetail = '<center>'+ '--' + '</center>';
		arrNonStockSalesLineItemData[nIndex].m_nQuantity = Number(arrNonStockSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_nPrice = Number(arrNonStockSalesLineItemData[nIndex].m_nPrice).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_nDiscount = Number(arrNonStockSalesLineItemData[nIndex].m_nDiscount).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_nDiscountPrice = (arrNonStockSalesLineItemData[nIndex].m_nPrice -(arrNonStockSalesLineItemData[nIndex].m_nPrice * (arrNonStockSalesLineItemData[nIndex].m_nDiscount/100)));
		arrNonStockSalesLineItemData[nIndex].m_nTaxPrice = (arrNonStockSalesLineItemData[nIndex].m_nDiscountPrice * (arrNonStockSalesLineItemData[nIndex].m_nTax/100));
		arrNonStockSalesLineItemData[nIndex].m_nAmount = ((arrNonStockSalesLineItemData[nIndex].m_nDiscountPrice + arrNonStockSalesLineItemData[nIndex].m_nTaxPrice) * arrNonStockSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrSalesLine.push(arrNonStockSalesLineItemData[nIndex]);
	}
	invoiceReport_table_detailViewDG.datagrid('loadData',arrSalesLine);
}

function invoiceReport_cancel ()
{
	HideDialog ("dialog");
}

function invoiceReport_printInvoiceReport ()
{
	var arrInvoiceData = invoiceReport_getReportData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strFromDate", m_oInvoiceReportMemberData.m_strFromDate);
	addChild(xmlDoc, "root", "m_strToDate", m_oInvoiceReportMemberData.m_strToDate);
	addChild(xmlDoc, "root", "m_strSoldByFilterBox", $('#invoiceReport_input_soldBy').combobox('getText'));
	addChild(xmlDoc, "root", "m_strSoldToFilterBox", $("#invoiceReport_input_soldTo").val());
	addChild(xmlDoc, "root", "m_strInvoiceNoFilterBox", $("#invoiceReport_input_invoiceNumber").val());
	m_oInvoiceReportMemberData.m_strXMLData = generateXML (xmlDoc, arrInvoiceData, "root", "InvoiceReportDataList");;
	navigate ('reportPrint','widgets/reportmanagement/invoice/invoiceReportPrint.js');
}

function invoiceReport_getReportData ()
{
	var arrReports = $('#invoiceReport_table_invoiceReportDG').datagrid('getRows');
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oInvoiceReportData = new InvoiceReportData ();
		oInvoiceReportData.m_strSoldBy = arrReports [nIndex].m_oCreatedBy.m_strUserName;
		oInvoiceReportData.m_strSoldTo = arrReports [nIndex].m_oClientData.m_strCompanyName;
		oInvoiceReportData.m_strInvoiceNumber = arrReports [nIndex].m_strInvoiceNumber;
		oInvoiceReportData.m_strDate =  arrReports [nIndex].m_strDate;
		oInvoiceReportData.m_nAmount = arrReports [nIndex].m_nInvoiceAmount;
		arrReportData.push (oInvoiceReportData);
	}
	return arrReportData;
}

function invoiceReport_exportToTally ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oInvoiceData = invoiceReport_getFormData ();
	InvoiceDataProcessor.exportToTally (oInvoiceData, 
			{ 
		        callback: function(oResponse) 
		        { 
					HideDialog ("dialog");
		        }, 
		        errorHandler: function(strErrMsg, oException) 
		        { 
		        	HideDialog ("dialog");
		        	informUser ("exportToTally - oException : "+ oException, "kError");
		        } 
			});
}