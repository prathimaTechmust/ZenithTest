var inventoryReport_includeDataObjects =
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/location/LocationData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/inventorymanagement/stocktransfer/StockTransferData.js',
	'widgets/inventorymanagement/stocktransfer/ItemLocationData.js',
	'widgets/reportmanagement/inventory/InventoryReportData.js'
]

 includeDataObjects (inventoryReport_includeDataObjects, "inventoryReport_loaded ()");

function inventoryReport_loaded ()
{
	loadPage ("reportmanagement/inventory/inventoryReport.html", "workarea", "inventoryReport_init ()");
}

function InventoryReport_memberData ()
{
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			inventoryReport_list ();
			$('#inventoryReport_input_articleNumber').combobox('textbox').unbind('keydown', m_oInventoryReportMemberData.m_oKeyDownHandler);
			$('#inventoryReport_input_Location').combobox('textbox').unbind('keydown', m_oInventoryReportMemberData.m_oKeyDownHandler);
		}
    };
	this.m_strXMLData = "";
}

var m_oInventoryReportMemberData = new InventoryReport_memberData ();

function inventoryReport_init ()
{
	inventoryReport_initializeDataGrid ();
	initArticleNumberCombobox ('#inventoryReport_input_articleNumber', m_oInventoryReportMemberData.m_oKeyDownHandler);
	inventoryReport_initLocationCombobox ();
	inventoryReport_list ();
}

function inventoryReport_initLocationCombobox ()
{
	$('#inventoryReport_input_Location').combobox
	({
		valueField:'m_nLocationId',
	    textField:'m_strName',
	    selectOnNavigation: false,
	    loader: getFilteredLocationData,
		mode: 'remote',
	    onSelect: function()
    	{
    		$("#inventoryReport_input_Location").combobox('textbox').focus();	
    		$("#inventoryReport_input_Location").combobox('textbox').bind('keydown', m_oInventoryReportMemberData.m_oKeyDownHandler);
    	}
	});
	$('#inventoryReport_input_Location').combobox('textbox')[0].placeholder = "Location";
}

var getFilteredLocationData = function(param, success, error)
{
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oLocationData = new LocationData ();
		oLocationData.m_strName = strQuery;
		LocationDataProcessor.getLocationSuggesstions (oLocationData, "", "", function(oResponse)
				{
					success(oResponse.m_arrLocations);
				});
	}
	else
		success(new Array ());
}

function inventoryReport_list ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", " inventoryReport_progressbarLoaded ()");
}

function inventoryReport_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oItemData = inventoryReport_getFormData ();
	var oItemLocationData = inventoryReport_getItemLocationData ();
	oItemLocationData.m_oItemData = oItemData;
	if(oItemLocationData.m_oLocationData.m_nLocationId > 0)
		ItemLocationDataProcessor.getInventoryReport(oItemLocationData, inventoryReport_listed);
	else
		ItemDataProcessor.list(oItemData, "", "", "", "", inventoryReport_listed);
}

function inventoryReport_getFormData ()
{
	var oItemData = new ItemData ();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oItemData.m_strArticleNumber = $('#inventoryReport_input_articleNumber').combobox('getValue');
	oItemData.m_strItemName = $("#inventoryReport_input_itemname").val ();
	oItemData.m_strBrand = $("#inventoryReport_input_brand").val();
	return oItemData;
}

function inventoryReport_getItemLocationData ()
{
	var oItemLocationData = new ItemLocationData ();
	oItemLocationData.m_oLocationData.m_nLocationId = $('#inventoryReport_input_Location').combobox('getValue');
	return oItemLocationData;
}

function inventoryReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#inventoryReport_table_inventoryReportDG");
	$('#inventoryReport_table_inventoryReportDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article No.',width:50,sortable:true},
				{field:'m_strItemName',title:'Item Name',width:120,sortable:true,
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
			  	},
			  	{field:'m_strBrand',title:'Brand',width:70,sortable:true},
				{field:'m_strDetail',title:'Details',width:40,sortable:true},
				{field:'m_nCurrentStock',title:'Current Stock',width:70,sortable:true,align:'right',
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		},
			  		formatter:function(value,row,index)
					{
						var nCurrentStock = row.m_nCurrentStock;
						try
						{
							nCurrentStock = nCurrentStock.toFixed(2);
						}
						catch(oException){}
						
						return nCurrentStock;
					}
				},
				{field:'m_nSellingPrice',title:'Selling Price',width:70,sortable:true,align:'right',
					formatter:function(value,row,index)
					{
						var nSellingPrice = row.m_nSellingPrice;
						try
						{
							if (!isNaN(value))
								nSellingPrice = '<span class="rupeeSign">R </span>' + formatNumber (nSellingPrice.toFixed(2),row,index);
							else
								return value;
						}	
						catch(oException){}
						return nSellingPrice;
					}},
				{field:'m_nAmount',title:'Amount',width:70,sortable:true,align:'right',
					formatter:function(value,row,index)
					{
						var nAmount = row.m_nAmount;
						var m_nIndianNumber = formatNumber (nAmount,row,index);
						return '<span class="rupeeSign">R </span>' + m_nIndianNumber;
					},
					styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}}
			]]
		}
	);
	$('#inventoryReport_table_inventoryReportDG').datagrid('reloadFooter',[{m_nSellingPrice:'Grand Total',m_nAmount:0}]);
	inventoryReport_subGrid ();
}

function inventoryReport_subGrid ()
{
	$('#inventoryReport_table_inventoryReportDG').datagrid({view: detailview,detailFormatter:function(index,row)
		{
            return '<div style="padding:2px"><table class="inventoryReport_table_inventoryReportSubDG"></table></div>';
        },
        onExpandRow: function(index,row)
        {
        	loadPage ("inventorymanagement/progressbar.html", "dialog", "inventoryReport_subGridprogressbarLoaded ()");
            var inventoryReport_table_inventoryReportSubDG = $(this).datagrid('getRowDetail',index).find('table.inventoryReport_table_inventoryReportSubDG');
            inventoryReport_table_inventoryReportSubDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',showFooter:true,
                columns:[[
                    {field:'m_strLocation',title:'Location',width:120,
    			  		styler: function(value,row,index)
    			  		{
    			  			return {class:'DGcolumn'};
    			  		}
                    },
	                {field:'m_nQuantity',title:'Quantity',align:'right',width:160}
                ]],
                onResize:function()
                {
                    $('#inventoryReport_table_inventoryReportDG').datagrid('fixDetailRowHeight',index);
                }
            });
            inventoryReport_getItemLocations (inventoryReport_table_inventoryReportSubDG, index, row);
        }
    });
}

function inventoryReport_subGridprogressbarLoaded ()
{
	createPopup('dialog', '', '', true);
}
	        
function inventoryReport_getItemLocations (inventoryReport_table_inventoryReportSubDG, index, row)
{
	var oItemLocationData = inventoryReport_getItemLocationData ();
	oItemLocationData.m_oItemData.m_nItemId = row.m_nItemId;
	ItemLocationDataProcessor.list(oItemLocationData, "", "", function (oResponse)
			{
				var arrItemLocationData = new Array ();
				var arrItemLocation = oResponse.m_arrItemLocation;
				var nTotal = 0;
				for (var nIndex = 0; nIndex < arrItemLocation.length; nIndex++)
				{
					arrItemLocation[nIndex].m_strLocation = arrItemLocation[nIndex].m_oLocationData.m_strName;
					if(arrItemLocation[nIndex].m_oLocationData.m_bIsDefault)
						arrItemLocation[nIndex].m_nQuantity = (row.m_nCurrentStock + (arrItemLocation[nIndex].m_nReceived - arrItemLocation[nIndex].m_nIssued)).toFixed(2);
					else
						arrItemLocation[nIndex].m_nQuantity = (arrItemLocation[nIndex].m_nReceived - arrItemLocation[nIndex].m_nIssued).toFixed(2);
					nTotal += Number (arrItemLocation[nIndex].m_nQuantity);
					arrItemLocationData.push(arrItemLocation[nIndex]);
				}
				inventoryReport_table_inventoryReportSubDG.datagrid('loadData',arrItemLocationData);
				inventoryReport_table_inventoryReportSubDG.datagrid('reloadFooter',[{m_strLocation:'', m_nQuantity:'<table class="trademust" style="border-style:none"><tr><td style="border-style:none; width:80%; font-weight:bold;" align="right">Total : </td><td style="border-style:none; width:20%" align="right">'+ nTotal.toFixed(2) +'</td></tr></table>'}]);
				$('#inventoryReport_table_inventoryReportDG').datagrid('fixDetailRowHeight',index);
				HideDialog("dialog");
			});
}

function inventoryReport_gotItemLocation (oResponse)
{
	var arrItemLocationData = new Array ();
	var arrItemLocation = oResponse.m_arrItemLocation;
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrItemLocation.length; nIndex++)
	{
		arrItemLocation[nIndex].m_strLocation = arrItemLocation[nIndex].m_oLocationData.m_strName;
		if(arrItemLocation[nIndex].m_oLocationData.m_bIsDefault)
			arrItemLocation[nIndex].m_nQuantity = (row.m_nCurrentStock + (arrItemLocation[nIndex].m_nReceived - arrItemLocation[nIndex].m_nIssued)).toFixed(2);
		else
			arrItemLocation[nIndex].m_nQuantity = (arrItemLocation[nIndex].m_nReceived - arrItemLocation[nIndex].m_nIssued).toFixed(2);
		nTotal += Number (arrItemLocation[nIndex].m_nQuantity);
		arrItemLocationData.push(arrItemLocation[nIndex]);
	}
	inventoryReport_table_inventoryReportSubDG.datagrid('loadData',arrItemLocationData);
	inventoryReport_table_inventoryReportSubDG.datagrid('reloadFooter',[{m_strLocation:'', m_nQuantity:'<table class="trademust" style="border-style:none"><tr><td style="border-style:none; width:80%; font-weight:bold;" align="right">Total : </td><td style="border-style:none; width:20%" align="right">'+ nTotal.toFixed(2) +'</td></tr></table>'}]);
	$('#inventoryReport_table_inventoryReportDG').datagrid('fixDetailRowHeight',index);
	HideDialog("dialog");
}

function inventoryReport_listed (oResponse)
{
	clearGridData ("#inventoryReport_table_inventoryReportDG");
	var nGrandTotal = 0;
	$('#inventoryReport_table_inventoryReportDG').datagrid('loadData',oResponse.m_arrItems);
	$('#inventoryReport_table_inventoryReportDG').datagrid('reloadFooter',[{m_nCurrentStock: '', m_nSellingPrice:'<b>Grand Total :</b>', m_nAmount:nGrandTotal}]);
	HideDialog ("dialog");
}

function inventoryReport_printInventoryReport ()
{
	var arrInventoryData = inventoryReport_getReportData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strArticleNumberFilterBox", $('#inventoryReport_input_articleNumber').combobox('getText'));
	addChild(xmlDoc, "root", "m_strItemNameFilterBox", $("#inventoryReport_input_itemname").val());
	addChild(xmlDoc, "root", "m_strBrandFilterBox", $("#inventoryReport_input_brand").val());
	m_oInventoryReportMemberData.m_strXMLData = generateXML (xmlDoc, arrInventoryData, "root", "InventoryReportDataList");
	navigate ('reportPrint','widgets/reportmanagement/inventory/inventoryReportPrint.js');
}

function inventoryReport_getReportData ()
{
	var arrReports = $('#inventoryReport_table_inventoryReportDG').datagrid('getRows');
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oInventoryReportData = new InventoryReportData ();
		oInventoryReportData.m_strArticleNumber = arrReports [nIndex].m_strArticleNumber;
		oInventoryReportData.m_strItemName = arrReports [nIndex].m_strItemName;
		oInventoryReportData.m_strBrand = arrReports [nIndex].m_strBrand;
		oInventoryReportData.m_strDetail =  arrReports [nIndex].m_strDetail;
		oInventoryReportData.m_nCurrentStock = arrReports [nIndex].m_nCurrentStock;
		oInventoryReportData.m_nSellingPrice = arrReports [nIndex].m_nSellingPrice;
		oInventoryReportData.m_nAmount = arrReports [nIndex].m_nAmount;
		arrReportData.push (oInventoryReportData);
	}
	return arrReportData;
}