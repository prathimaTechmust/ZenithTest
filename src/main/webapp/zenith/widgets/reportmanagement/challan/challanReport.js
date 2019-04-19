var challanReport_includeDataObjects =
[
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/reportmanagement/challan/ChallanReportData.js'
]

 includeDataObjects (challanReport_includeDataObjects, "challanReport_loaded ()");

function ChallanReport_memberData ()
{
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			challanReport_generateChallanReport();
			$('#challanReport_input_soldBy').combobox('textbox').unbind('keydown', m_oChallanReportMemberData.m_oKeyDownHandler);
			$('#challanReport_input_soldBy').combobox('textbox').focus();
		}
    };
}

var m_oChallanReportMemberData = new ChallanReport_memberData ();

function challanReport_initOverallReport ()
{
	initUserCombobox ('#challanReport_input_soldBy', "Sold By", m_oChallanReportMemberData.m_oKeyDownHandler);
	challanReport_initReport ();
	var oUpdateGridName = document.getElementById("challanReport_table_challanReportDG");
	oUpdateGridName.title = "Overall Challan Report";
}

function challanReport_initReport ()
{
	loadPage ("reportmanagement/challan/reportDuration.html", "dialog", "challanReport_initDuration ()");
}

function challanReport_initDuration ()
{
	createPopup("dialog", "#reportDuration_button_cancel", "#reportDuration_button_generate", true);
	$("#reportDuration_input_dateFrom").datebox();
	$("#reportDuration_input_dateTo").datebox();
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
	$('#reportDuration_input_dateFrom').datebox('setValue', dCurrentDate);
	$('#reportDuration_input_dateTo').datebox('setValue', dCurrentDate);
}

function challanReport_processReport ()
{
	m_oChallanReportMemberData.m_strFromDate = FormatDate ($('#reportDuration_input_dateFrom').datebox('getValue'));
	m_oChallanReportMemberData.m_strToDate = FormatDate ($('#reportDuration_input_dateTo').datebox('getValue'));
	challanReport_generateChallanReport ();
}

function challanReport_generateChallanReport ()
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "challanReport_progressbarLoaded ()");
}
	
function challanReport_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oChallanData = challanReport_getFormData ();
	HideDialog ("dialog");
	var oUpdateGridName = document.getElementById("challanReport_table_challanReportDG");
	oUpdateGridName.title += " [ From : " + oChallanData.m_oSalesData.m_strFromDate + " - To : " + oChallanData.m_oSalesData.m_strToDate + " ]";
	challanReport_initializeDataGrid ();
	oChallanData.m_oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oChallanData.m_oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ChallanDataProcessor.list(oChallanData, "", "", "","", challanReport_listed);
}

function challanReport_getFormData ()
{
	var oChallanData = new ChallanData ();
	var strFromDate = m_oChallanReportMemberData.m_strFromDate;
	var strToDate = m_oChallanReportMemberData.m_strToDate;
	oChallanData.m_oSalesData.m_oCreatedBy.m_nUserId = $('#challanReport_input_soldBy').combobox('getValue');
	oChallanData.m_oSalesData.m_strTo =$("#challanReport_input_soldTo").val();
	oChallanData.m_oSalesData.m_strChallanNumber =$("#challanReport_input_challanNumber").val();
	oChallanData.m_oSalesData.m_strFromDate = (strFromDate != "undefined--undefined") ? strFromDate : null;
	oChallanData.m_oSalesData.m_strToDate = (strToDate != "undefined--undefined") ? strToDate : null;
	return oChallanData;
}

function challanReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#challanReport_table_challanReportDG");
	$('#challanReport_table_challanReportDG').datagrid
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
				  			return row.m_oSalesData.m_oCreatedBy.m_strUserName;
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
				{field:'m_strChallanNumber',title:'Challan No.',sortable:true,width:70},
				{field:'m_strDate',title:'Date',sortable:true,width:40},
				{field:'m_nAmount',title:'Amount',width:70,align:'right',sortable:true,
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
	$('#challanReport_table_challanReportDG').datagrid('reloadFooter',[{m_strDate:'Grand Total',m_nAmount:0}]);
	challanReport_subGridSales ();
}

function challanReport_listed (oResponse)
{
	clearGridData ("#challanReport_table_challanReportDG");
	var nGrandTotal = 0;
	var arrChallan = oResponse.m_arrChallan;
	for (var nIndex = 0; nIndex < arrChallan.length; nIndex++)
	{
		arrChallan[nIndex].m_strTo = arrChallan[nIndex].m_oSalesData.m_strTo;
		arrChallan[nIndex].m_strChallanNumber = arrChallan[nIndex].m_oSalesData.m_strChallanNumber;
		arrChallan[nIndex].m_strDate = arrChallan[nIndex].m_oSalesData.m_strDate;
		arrChallan[nIndex].m_nAmount = challanReport_calculateRowTotal (arrChallan[nIndex].m_oSalesData.m_oSalesLineItems);
		nGrandTotal += Number (arrChallan[nIndex].m_nAmount);
		$('#challanReport_table_challanReportDG').datagrid('appendRow',arrChallan[nIndex]);
	}
	$('#challanReport_table_challanReportDG').datagrid('reloadFooter',[{m_strDate:'<b>Grand Total :</b>', m_nAmount:nGrandTotal}]);
	HideDialog ("secondDialog");
}


function challanReport_calculateRowTotal (arrSalesLineItems)
{
	assert.isArray(arrSalesLineItems, "arrSalesLineItems expected to be an Array.");
	var nRowAmount = 0;
	for (var nIndex = 0 ; nIndex < arrSalesLineItems.length; nIndex++)
		nRowAmount += arrSalesLineItems[nIndex].m_nQuantity * (arrSalesLineItems[nIndex].m_nPrice - (arrSalesLineItems[nIndex].m_nPrice * (arrSalesLineItems[nIndex].m_nDiscount/100)));
	return nRowAmount;
}

function challanReport_subGridSales ()
{
	$('#challanReport_table_challanReportDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="challanReport_table_detailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  challanReport_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.challanReport_table_detailViewDG');
	        challanReport_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
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
	                $('#challanReport_table_challanReportDG').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#challanReport_table_challanReportDG').datagrid('fixDetailRowHeight',index);
	        challanReport_subgridListed (challanReport_table_detailViewDG, index, row);
	    }
	});
}

function challanReport_subgridListed (challanReport_table_detailViewDG, index, row)
{
	assert.isObject(row, "row expected to be an Object.");
	var arrSalesLine = new Array ();
	var arrSalesLineItemData = row.m_oSalesData.m_oSalesLineItems;
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
	challanReport_table_detailViewDG.datagrid('loadData',arrSalesLine);
}

function challanReport_cancel ()
{
	HideDialog ("dialog");
}

function challanReport_printChallanReport ()
{
	var arrChallanData = ChallanReport_getReportData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strFromDate", m_oChallanReportMemberData.m_strFromDate);
	addChild(xmlDoc, "root", "m_strToDate", m_oChallanReportMemberData.m_strToDate);
	addChild(xmlDoc, "root", "m_strSoldByFilterBox", $('#challanReport_input_soldBy').combobox('getText'));
	addChild(xmlDoc, "root", "m_strSoldToFilterBox", $("#challanReport_input_soldTo").val());
	addChild(xmlDoc, "root", "m_strChallanNoFilterBox",$("#challanReport_input_challanNumber").val());
	strXML = generateXML (xmlDoc, arrChallanData, "root", "ChallanReportDataList");
	m_oChallanReportMemberData.m_strXMLData = strXML;
	navigate ('reportPrint','widgets/reportmanagement/challan/challanReportPrint.js');
}

function ChallanReport_getReportData ()
{
	var arrReports = $('#challanReport_table_challanReportDG').datagrid('getRows');
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oChallanReportData = new ChallanReportData ();
		oChallanReportData.m_strSoldBy = arrReports [nIndex].m_oSalesData.m_oCreatedBy.m_strUserName;
		oChallanReportData.m_strSoldTo = arrReports [nIndex].m_oSalesData.m_strTo;
		oChallanReportData.m_strChallanNumber = arrReports [nIndex].m_oSalesData.m_strChallanNumber;
		oChallanReportData.m_strDate =  arrReports [nIndex].m_oSalesData.m_strDate;
		oChallanReportData.m_nAmount = arrReports [nIndex].m_nAmount;
		arrReportData.push (oChallanReportData);
	}
	return arrReportData;
}
