var stockMovementReport_includeDataObjects =
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/reportmanagement/stockmovement/StockMovementData.js'
]

 includeDataObjects (stockMovementReport_includeDataObjects, "stockMovementReport_init ()");

function stockMovementReport_memberData ()
{
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_bIsIncludeZeroMovementStockChecked = false;
	this.m_nSelectedItemId = -1;
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			stockMovementReport_filterStockMovementReport ();
			$('#stockMovementReport_input_articleNumber').combobox('textbox').unbind('keydown', m_oStockMovementReportMemberData.m_oKeyDownHandler);
		}
    };
	this.m_strXMLData = "";
}

var m_oStockMovementReportMemberData = new stockMovementReport_memberData ();

function stockMovementReport_init ()
{
	loadPage ("reportmanagement/stockmovement/stockMovementReport.html", "workarea", "stockMovementReport_initCombobox ()");
	loadPage ("reportmanagement/stockmovement/reportDuration.html", "dialog", "stockMovementReport_initDuration ()");
}

function stockMovementReport_initCombobox ()
{
	initArticleNumberCombobox ('#stockMovementReport_input_articleNumber', m_oStockMovementReportMemberData.m_oKeyDownHandler);
}

function stockMovementReport_initDuration ()
{
	createPopup("dialog", "#reportDuration_button_cancel", "#reportDuration_button_create", true);
	$("#reportDuration_input_dateFrom").datebox();
	$("#reportDuration_input_dateTo").datebox();
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
	$('#reportDuration_input_dateFrom').datebox('setValue', dCurrentDate);
	$('#reportDuration_input_dateTo').datebox('setValue', dCurrentDate);
}

function stockMovementReport_processReport ()
{
	m_oStockMovementReportMemberData.m_strFromDate = FormatDate ($('#reportDuration_input_dateFrom').datebox('getValue'));
	m_oStockMovementReportMemberData.m_strToDate = FormatDate ($('#reportDuration_input_dateTo').datebox('getValue'));
	m_oStockMovementReportMemberData.m_bIsIncludeZeroMovementStockChecked = $("#reportDuration_input_checkMovementStock").is(':checked') ? true : false;
	stockMovementReport_generateStockMovementReport ();
}

function stockMovementReport_getFilterFormData ()
{
	var oItemData = new ItemData ();
	oItemData.m_strArticleNumber = $('#stockMovementReport_input_articleNumber').combobox('getValue');
	oItemData.m_strItemName = $("#stockMovementReport_input_itemname").val();
	oItemData.m_strBrand = $("#stockMovementReport_input_brand").val();
	return oItemData;
}

function stockMovementReport_generateStockMovementReport ()
{
	var oItemData = new ItemData ();
	oItemData.m_strBrand = $("#reportDuration_input_brand").val();
	HideDialog ("dialog");
	stockMovementReport_getStockMovementReport (oItemData);
}

function stockMovementReport_filterStockMovementReport ()
{
	var oItemData = stockMovementReport_getFilterFormData ();
	stockMovementReport_getStockMovementReport (oItemData);
}

function stockMovementReport_getStockMovementReport (oItemData)
{
	assert.isObject(oItemData, "oItemData expected to be an Object.");
	assert( Object.keys(oItemData).length >0 , "oItemData cannot be an empty .");// checks for non emptyness 
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var strFromDate = (m_oStockMovementReportMemberData.m_strFromDate != "undefined--undefined") ? m_oStockMovementReportMemberData.m_strFromDate : null;
	var strToDate = (m_oStockMovementReportMemberData.m_strToDate != "undefined--undefined") ? m_oStockMovementReportMemberData.m_strToDate : null;
	var oUpdateGridName = document.getElementById("stockMovementReport_table_stockMovementReportDG");
	oUpdateGridName.title += " [ From : " + strFromDate + " - To : " + strToDate + " ]";
	stockMovementReport_initializeDataGrid ();
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	ItemDataProcessor.getStockMovementReport(strFromDate, strToDate, oItemData, m_oStockMovementReportMemberData.m_bIsIncludeZeroMovementStockChecked, stockMovementReport_gotData);
}

function stockMovementReport_gotData (oResponse)
{
	clearGridData ("#stockMovementReport_table_stockMovementReportDG");
	for (var nIndex = 0; nIndex < oResponse.m_arrStockMovementData.length; nIndex++)
	{
		var arrStockMovement = oResponse.m_arrStockMovementData;
		for (var nIndex = 0; nIndex < arrStockMovement.length; nIndex++)
		{
			arrStockMovement[nIndex].m_strArticleNumber = arrStockMovement[nIndex].m_oItemData.m_strArticleNumber;
			arrStockMovement[nIndex].m_strItemName = arrStockMovement[nIndex].m_oItemData.m_strItemName;
			arrStockMovement[nIndex].m_strBrand = arrStockMovement[nIndex].m_oItemData.m_strBrand;
			arrStockMovement[nIndex].m_strDetail = arrStockMovement[nIndex].m_oItemData.m_strDetail;
			arrStockMovement[nIndex].m_nReceivedQuantity = arrStockMovement[nIndex].m_nReceivedQuantity;
			arrStockMovement[nIndex].m_nIssuedQuantity = arrStockMovement[nIndex].m_nIssuedQuantity;
			arrStockMovement[nIndex].m_nBalanceQuantity = arrStockMovement[nIndex].m_nReceivedQuantity - arrStockMovement[nIndex].m_nIssuedQuantity;
			arrStockMovement[nIndex].m_nCurrentStock = parseFloat(stockMovementReport_calculateCurrentStock (arrStockMovement[nIndex]));
			$('#stockMovementReport_table_stockMovementReportDG').datagrid('appendRow',arrStockMovement[nIndex]);
		}

	}
	HideDialog ("dialog");
}

function stockMovementReport_calculateCurrentStock (oStockMovementdata)
{
	assert.isObject(oStockMovementdata, "oStockMovementdata expected to be an Object.");
	assert( Object.keys(oStockMovementdata).length >0 , "oStockMovementdata cannot be an empty .");// checks for non emptyness 
	var nCurrentStock = 0;
	nCurrentStock = (oStockMovementdata.m_oItemData.m_nOpeningStock + (oStockMovementdata.m_oItemData.m_nReceived - oStockMovementdata.m_oItemData.m_nIssued));
	return nCurrentStock;
}

function stockMovementReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#stockMovementReport_table_stockMovementReportDG");
	$('#stockMovementReport_table_stockMovementReportDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article Number',sortable:true,width:60},
			  	{field:'m_strItemName',title:'Item Name',width:120,sortable:true,
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
			  	},
			  	{field:'m_strBrand',title:'Item Brand',width:50},
				{field:'m_strDetail',title:'Item Details',width:50},
				{field:'m_nReceivedQuantity',title:'Received Qty',sortable:true,width:60,align:'right',
					formatter:function(value,row,index)
					{
						return row.m_nReceivedQuantity.toFixed(2);
					}
				},
				{field:'m_nIssuedQuantity',title:'Issued Qty',sortable:true,width:60,align:'right',
					formatter:function(value,row,index)
					{
						return row.m_nIssuedQuantity.toFixed(2);
					}
				},
				{field:'m_nBalanceQuantity',title:'Balance Qty',sortable:true,width:60,align:'right',
					formatter:function(value,row,index)
					{
						return row.m_nBalanceQuantity.toFixed(2);
					}
				},
				{field:'m_nCurrentStock',title:'Current Stock',sortable:true,width:60,align:'right',
					formatter:function(value,row,index)
					{
						return row.m_nCurrentStock.toFixed(2);
					},
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
				},
				{field:'Actions',title:'Action',width:30,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return stockMovementReport_displayImages (row, index);
		        	}
				},
			]]
		}
	);
}

function stockMovementReport_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var oActions = '<table align="center">'+
						'<tr>'+
						'<td> <img title="Info" src="images/messager_info.gif" onClick="stockMovementReport_getInfo ('+row.m_oItemData.m_nItemId+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oActions;
}

function stockMovementReport_getInfo (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	m_oStockMovementReportMemberData.m_nSelectedItemId = nItemId;
	navigate ("ItemTransaction", "widgets/inventorymanagement/item/itemTransactionForReport.js");
}

function stockMovementReport_cancel ()
{
	HideDialog ("dialog");
}

function stockMovementReport_printStockMovementReport ()
{
	var arrItems = stockMovementReport_getReportData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strFromDate", m_oStockMovementReportMemberData.m_strFromDate);
	addChild(xmlDoc, "root", "m_strToDate", m_oStockMovementReportMemberData.m_strToDate);
	addChild(xmlDoc, "root", "m_strArticleNumberFilterBox", $('#stockMovementReport_input_articleNumber').combobox('getValue'));
	addChild(xmlDoc, "root", "m_strItemNameFilterBox", $("#stockMovementReport_input_itemname").val ());
	addChild(xmlDoc, "root", "m_strItemBrandFilterBox", $("#stockMovementReport_input_brand").val());
	m_oStockMovementReportMemberData.m_strXMLData = generateXML (xmlDoc, arrItems, "root", "StockMovementDataList");;
	navigate ('reportPrint','widgets/reportmanagement/stockmovement/stockMovementReportPrint.js');
}

function stockMovementReport_getReportData ()
{
	var arrReports = $('#stockMovementReport_table_stockMovementReportDG').datagrid('getRows');
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oStockMovementData = new StockMovementData ();
		oStockMovementData.m_strArticleNumber = arrReports [nIndex].m_strArticleNumber;
		oStockMovementData.m_strItemName = arrReports [nIndex].m_strItemName;
		oStockMovementData.m_strBrand = arrReports [nIndex].m_strBrand;
		oStockMovementData.m_strDetail =  arrReports [nIndex].m_strDetail;
		oStockMovementData.m_nReceivedQuantity = arrReports [nIndex].m_nReceivedQuantity;
		oStockMovementData.m_nIssuedQuantity = arrReports [nIndex].m_nIssuedQuantity;
		oStockMovementData.m_nBalanceQuantity = arrReports [nIndex].m_nBalanceQuantity;
		oStockMovementData.m_nCurrentStock = arrReports [nIndex].m_nCurrentStock;
		arrReportData.push (oStockMovementData);
	}
	return arrReportData;
}
