var purchaseReport_includeDataObjects =
[
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/reportmanagement/purchase/PurchaseReportData.js'
]

 includeDataObjects (purchaseReport_includeDataObjects, "purchaseReport_loaded()");

function purchaseReport_initMyReport ()
{
	var oUpdateGridName = document.getElementById("purchaseReport_table_purchaseReportDG");
	oUpdateGridName.title = "Purchase Report ";
	purchaseReport_initReport ();
	m_oPurchaseReportMemberData.bIsMyPurchaseReport = true;
	initUserCombobox ('#purchaseReport_input_purchaseBy', "Purchased By", m_oPurchaseReportMemberData.m_oKeyDownHandler);
	$('#purchaseReport_input_purchaseBy').combobox('destroy');
}

function purchaseReport_initOverallReport ()
{
	initUserCombobox ('#purchaseReport_input_purchaseBy', "Purchased By", m_oPurchaseReportMemberData.m_oKeyDownHandler);
	purchaseReport_initReport ();
	var oUpdateGridName = document.getElementById("purchaseReport_table_purchaseReportDG");
	oUpdateGridName.title = "Overall Purchase Report ";
}

function purchaseReport_initReport ()
{
	loadPage ("reportmanagement/purchase/reportDuration.html", "dialog", "purchaseReport_initDuration ()");
}

function purchaseReport_memberData ()
{
	this.bIsMyPurchaseReport = false;
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			purchaseReport_generatePurchaseReport ();
			$('#purchaseReport_input_purchaseBy').combobox('textbox').unbind('keydown', m_oPurchaseReportMemberData.m_oKeyDownHandler);
			$('#purchaseReport_input_purchaseBy').combobox('textbox').focus();
		}
    };
	this.m_strXMLData = "";
	this.bIsVendorSelected = false;
	this.m_nSelectedId = -1;
	this.m_strColumn = "m_strCompanyName";
	this.m_strOrderBy = "asc";
}

var m_oPurchaseReportMemberData = new purchaseReport_memberData ();

function purchaseReport_initDuration ()
{
	createPopup("dialog", "#reportDuration_button_cancel", "#reportDuration_button_create", true);
	$("#reportDuration_input_dateFrom").datebox();
	$("#reportDuration_input_dateTo").datebox();
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
	$('#reportDuration_input_dateFrom').datebox('setValue', dCurrentDate);
	$('#reportDuration_input_dateTo').datebox('setValue', dCurrentDate);
}

function purchaseReport_processReport ()
{
	m_oPurchaseReportMemberData.m_strFromDate = FormatDate ($('#reportDuration_input_dateFrom').datebox('getValue'));
	m_oPurchaseReportMemberData.m_strToDate = FormatDate ($('#reportDuration_input_dateTo').datebox('getValue'));
	var strSelectedDD = $("#reportDuration_select_by").val();
	if(strSelectedDD == "Vendors")
		m_oPurchaseReportMemberData.bIsVendorSelected = true;
	purchaseReport_generatePurchaseReport ();
}

function purchaseReport_generatePurchaseReport ()
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "purchaseReport_progressbarLoaded ()");
}

function purchaseReport_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oPurchaseData = purchaseReport_getFormData ();
	HideDialog ("dialog");
	var oUpdateGridName = document.getElementById("purchaseReport_table_purchaseReportDG");
	oUpdateGridName.title += " [ From : " + oPurchaseData.m_strFromDate + " - To : " + oPurchaseData.m_strToDate + " ]";
	if(m_oPurchaseReportMemberData.bIsVendorSelected == true)
	{
		var oUpdateGridName = document.getElementById("purchaseReport_table_purchaseReportDG");
		oUpdateGridName.title = "Vendor Purchase Report " + " [ From : " + oPurchaseData.m_strFromDate + " - To : " + oPurchaseData.m_strToDate + " ]";
		purchaseReport_initializeVendorDataGrid ();
		$('#purchaseReport_input_purchaseBy').combobox('destroy');
	}
	else
		purchaseReport_initializeDataGrid ();
	
	if(m_oPurchaseReportMemberData.bIsVendorSelected == true)
		PurchaseDataProcessor.getVendorReport(oPurchaseData,m_oPurchaseReportMemberData.m_strColumn, m_oPurchaseReportMemberData.m_strOrderBy, purchaseReport_listedVendorReport);
	else
		PurchaseDataProcessor.list(oPurchaseData,"","", "", "", purchaseReport_listed);
}

function purchaseReport_listed (oResponse)
{
	clearGridData ('#purchaseReport_table_purchaseReportDG');
	var nGrandTotal = 0;
	var arrPurchase = oResponse.m_arrPurchase;
	for (var nIndex = 0; nIndex < arrPurchase.length; nIndex++)
	{
		arrPurchase[nIndex].m_nAmount = getTotalAmount (arrPurchase[nIndex]);
		nGrandTotal += Number (arrPurchase[nIndex].m_nAmount);
		$('#purchaseReport_table_purchaseReportDG').datagrid('appendRow',arrPurchase[nIndex]);
	}
	$('#purchaseReport_table_purchaseReportDG').datagrid('reloadFooter',[{m_strDate:'<b>Grand Total :</b>', m_nAmount:nGrandTotal}]);
	HideDialog ("secondDialog"); 
}

function purchaseReport_getFormData ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData = purchaseReport_getData ();
	if(!m_oPurchaseReportMemberData.bIsVendorSelected)
	{
		var nUserId = m_oPurchaseReportMemberData.bIsMyPurchaseReport  ? m_oTrademustMemberData.m_nUserId : $('#purchaseReport_input_purchaseBy').combobox('getValue');
		oPurchaseData.m_oCreatedBy.m_nUserId = nUserId > 0 ? nUserId : -1;
	}
	return oPurchaseData;
}

function purchaseReport_getData ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_strFromDate = (m_oPurchaseReportMemberData.m_strFromDate != "undefined--undefined") ? m_oPurchaseReportMemberData.m_strFromDate : null;
	oPurchaseData.m_strToDate = (m_oPurchaseReportMemberData.m_strToDate != "undefined--undefined") ? m_oPurchaseReportMemberData.m_strToDate : null;
	oPurchaseData.m_strFrom = $("#purchaseReport_input_purchaseFrom").val();
	oPurchaseData.m_strInvoiceNo = $("#purchaseReport_input_invoiceNumber").val();
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oPurchaseData;
}

function purchaseReport_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#purchaseReport_table_purchaseReportDG");
	$('#purchaseReport_table_purchaseReportDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_oCreatedBy',title:'Purchased By',sortable:true,width:70,
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
		        	}},
			  	{field:'m_strFrom',title:'Purchased From',sortable:true,width:110,
		        		styler: function(value,row,index)
				  		{
				  		 	return {class:'DGcolumn'};
				  		}		
			  	},
				{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:70},
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
	purchaseReport_subGridPurchase ();
}

function purchaseReport_subGridPurchase ()
{
	$('#purchaseReport_table_purchaseReportDG').datagrid({view: detailview,detailFormatter:function(index,row)
		{
            return '<div style="padding:2px"><table class="purchaseReport_table_detailViewDG"></table></div>';
        },
        onExpandRow: function(index,row)
        {
            var purchaseReport_table_detailViewDG = $(this).datagrid('getRowDetail',index).find('table.purchaseReport_table_detailViewDG');
            purchaseReport_table_detailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
                columns:[[
                    {field:'m_strItemName',title:'Item Name',width:120,
                    	formatter:function(value,row,index)
    		        	{
    			  			return row.m_oItemData.m_strItemName;
    		        	}},
	                {field:'m_strDetail',title:'Details',width:160,
	                	formatter:function(value,row,index)
			        	{
						  	 return row.m_oItemData.m_strDetail;
			        	}},
	                {field:'m_nQuantity',title:'Quantity',width:60,align:'right',
			        		formatter:function(value,row,index)
				        	{
								 return row.m_nQuantity.toFixed(2);
				        	}		
	                },
	                {field:'m_nPrice',title:'Price',width:100,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                		 var value = row.m_nPrice.toFixed(2);
	                	     var m_nIndianNumber = formatNumber (value,row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}
	                },
	                {field:'m_nAmount',title:'Amount',width:120,align:'right',
	                	formatter:function(value,row,index)
			        	{
							 var nAmount = row.m_nPrice * row.m_nQuantity;
							 var value = nAmount.toFixed(2);
							 var m_nIndianNumber = formatNumber (value,row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}
	                }
                ]],
                onResize:function()
                {
                    $('#purchaseReport_table_purchaseReportDG').datagrid('fixDetailRowHeight',index);
                }
            });
            $('#purchaseReport_table_purchaseReportDG').datagrid('fixDetailRowHeight',index);
            purchaseReport_subgridListed (purchaseReport_table_detailViewDG, index, row);
        }
    });
}

function purchaseReport_listedVendorReport (arrReportVendorData)
{
	assert.isArray(arrReportVendorData, "arrReportVendorData expected to be an Array.");
	clearGridData ('#purchaseReport_table_purchaseReportDG');
	var nGrandTotal = 0;
	for (var nIndex = 0; nIndex < arrReportVendorData.length; nIndex++)
	{
		arrReportVendorData[nIndex].m_nAmount = purchaseReport_calculateRowAmount (arrReportVendorData[nIndex].m_arrPurchaseData);
		nGrandTotal += Number (arrReportVendorData[nIndex].m_nAmount);
		$('#purchaseReport_table_purchaseReportDG').datagrid('appendRow',arrReportVendorData[nIndex]);
	}
	$('#purchaseReport_table_purchaseReportDG').datagrid('reloadFooter',[{m_strCompanyName:'<div style="font-weight:bold; text-align:right">Grand Total :</div>', m_nAmount:nGrandTotal}]);
	HideDialog ("secondDialog");
}

function purchaseReport_calculateRowAmount (arrPurchaseData)
{
	assert.isArray(arrPurchaseData, "arrPurchaseData expected to be an Array.");
	var nRowAmount = 0;
	for (var nIndex = 0 ; nIndex < arrPurchaseData.length; nIndex++)
		nRowAmount += getTotalAmount(arrPurchaseData[nIndex]);
	return nRowAmount;
}

function purchaseReport_cancel ()
{
	HideDialog ("dialog");
}

function purchaseReport_subgridListed (purchaseReport_table_detailViewDG, index, row)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	purchaseReport_table_detailViewDG.datagrid('loadData',row.m_oPurchaseLineItems);
}

function purchaseReport_printPurchaseReport ()
{
	var arrPurchaseData = purchaseReport_getReportData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strFromDate", m_oPurchaseReportMemberData.m_strFromDate);
	addChild(xmlDoc, "root", "m_strToDate", m_oPurchaseReportMemberData.m_strToDate);
	addChild(xmlDoc, "root", "m_strPurchasedFromFilterBox", $("#purchaseReport_input_purchaseFrom").val ());
	try
	{
		addChild(xmlDoc, "root", "m_strPurchasedByFilterBox", $('#purchaseReport_input_purchaseBy').combobox('getText'));
	}
	catch(oException)
	{
		
	}
	addChild(xmlDoc, "root", "m_strInvoiceNoFilterBox", $("#purchaseReport_input_invoiceNumber").val ());
	m_oPurchaseReportMemberData.m_strXMLData = generateXML (xmlDoc, arrPurchaseData, "root", "PurchaseReportDataList");
	navigate ('reportPrint','widgets/reportmanagement/purchase/purchaseReportPrint.js');
}

function purchaseReport_getReportData ()
{
	var arrReports = $('#purchaseReport_table_purchaseReportDG').datagrid('getRows');
	var arrReportData = new Array ();
	for (var nIndex = 0; nIndex < arrReports.length; nIndex++)
	{
		var oPurchaseReportData = new PurchaseReportData ();
		if(!m_oPurchaseReportMemberData.bIsVendorSelected)
		{
			oPurchaseReportData.m_strPurchasedBy = arrReports [nIndex].m_oCreatedBy.m_strUserName;
			oPurchaseReportData.m_strPurchasedFrom = arrReports [nIndex].m_strFrom;
			oPurchaseReportData.m_strInvoiceNo = arrReports [nIndex].m_strInvoiceNo;
			oPurchaseReportData.m_strDate =  arrReports [nIndex].m_strDate;
			oPurchaseReportData.m_nAmount = arrReports [nIndex].m_nAmount;
		}
		else
		{
			oPurchaseReportData.m_strCompanyName = arrReports[nIndex].m_oVendorData.m_strCompanyName;
			oPurchaseReportData.m_nAmount = arrReports[nIndex].m_nAmount;
		}
		
		arrReportData.push (oPurchaseReportData);
	}
	return arrReportData;
}

function purchaseReport_initializeVendorDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#purchaseReport_table_purchaseReportDG");
	$('#purchaseReport_table_purchaseReportDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:80,
			  		formatter:function(value,row,index)
		        	{
				  		try
			  			{
				  			return row.m_oVendorData.m_strCompanyName;
			  			}
			  			catch(oException)
			  			{
			  				return row.m_strCompanyName;
			  			}
		        	}},
		        	{field:'m_nAmount',title:'Total Purchase',width:20,align:'right',sortable:true,
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
					purchaseReport_getVendorReport (strColumn, strOrder);
			}
		}
	);
	purchaseReport_subGridVendorPurchase ();
}

function purchaseReport_getVendorReport (strColumn, strOrder)
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData = purchaseReport_getData ();
	PurchaseDataProcessor.getVendorReport(oPurchaseData,strColumn, strOrder, purchaseReport_listedVendorReport);
}

function purchaseReport_subGridVendorPurchase ()
{
	$('#purchaseReport_table_purchaseReportDG').datagrid({view: detailview,detailFormatter:function(index,row)
		{
            return '<div style="padding:2px"><table class="purchaseReport_table_vendorDetailViewDG"></table></div>';
        },
        onExpandRow: function(index,row)
        {
            var purchaseReport_table_vendorDetailViewDG = $(this).datagrid('getRowDetail',index).find('table.purchaseReport_table_vendorDetailViewDG');
            purchaseReport_table_vendorDetailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
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
			        		return purchaseReport_displayImages (row, index);
			        	}
					}
                ]],
                onResize:function()
                {
                    $('#purchaseReport_table_purchaseReportDG').datagrid('fixDetailRowHeight',index);
                }
            });
            $('#purchaseReport_table_purchaseReportDG').datagrid('fixDetailRowHeight',index);
            purchaseReport_subgridVendorListed (purchaseReport_table_vendorDetailViewDG, index, row);
        }
    });
}

function getTotalAmount (row)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var nCount = 0;
	for(var nIndex =0; nIndex< row.m_oPurchaseLineItems.length; nIndex++)
		nCount += row.m_oPurchaseLineItems[nIndex].m_nPrice * row.m_oPurchaseLineItems[nIndex].m_nQuantity;
	return nCount;
}

function purchaseReport_displayImages (oRow, nIndex)
{
	assert.isObject(oRow, "oRow expected to be an Object.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="purchaseReport_getItemDetails ('+oRow.m_nId+')"/> </td>'+
				'</tr>'+
			'</table>'
return oActions;
}

function purchaseReport_subgridVendorListed (purchaseReport_table_vendorDetailViewDG, index, row)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	clearGridData (purchaseReport_table_vendorDetailViewDG);
	for(var nIndex = 0; nIndex < row.m_arrPurchaseData.length; nIndex++)
		purchaseReport_table_vendorDetailViewDG.datagrid('appendRow',row.m_arrPurchaseData[nIndex]);
}

function purchaseReport_getItemDetails (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	m_oPurchaseReportMemberData.m_nSelectedId = nId;
	navigate("","widgets/reportmanagement/purchase/purchaseItemReport.js");
}

function purchaseReport_exportToTally ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oPurchaseData = purchaseReport_getFormData ();
	PurchaseDataProcessor.exportToTally (oPurchaseData, 
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
