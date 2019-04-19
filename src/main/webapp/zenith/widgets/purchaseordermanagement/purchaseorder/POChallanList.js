var POChallanList_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderStockLineItemData.js',
];

 includeDataObjects (POChallanList_includeDataObjects, "POChallanList_loaded ()");

function POChallanList_memberData ()
{
	this.m_nPurchaseOrderId = -1;
	this.m_nGrandTotal = 0;
}

var m_oPOChallanListMemberData = new POChallanList_memberData ();

function POChallanList_init ()
{
	createPopup('ProcessDialog', '', '', true);
	createPopup("secondDialog", "#POChallanList_button_makeBill", "#POChallanList_button_cancel", true);
	POChallanList_initializeDataGrid ();
	POChallanList_list ();
}

function POChallanList_initializeDataGrid ()
{
	$('#POChallanList_table_ChallanListDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'ckBox',checkbox:true},
				{field:'m_strChallanNumber',title:'Challan No.',sortable:true,width:300},
				{field:'m_dCreatedOn',title:'Date',sortable:true,width:80},
				{field:'m_nAmount',title:'Amount',sortable:true,width:100,align:'right',
					formatter:function(value,row,index)
		        	{
           		 		var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
           		 		return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
		        	}
				},
			]],
			onCheck: function (rowIndex, rowData)
			{
				POChallanList_updateFooter (rowData, true);
			},
			onUncheck: function (rowIndex, rowData)
			{
				POChallanList_updateFooter (rowData, false);
			},
			onCheckAll: function (rows)
			{
				m_oPOChallanListMemberData.m_nGrandTotal += POChallanList_getTotal (rows);
				POChallanList_reloadFooter ();	
			},
			onUncheckAll: function (rows)
			{
				m_oPOChallanListMemberData.m_nGrandTotal = 0;
				POChallanList_reloadFooter ();	
			}
		}
	);
	POChallanList_lineItemsDG ();
}

function POChallanList_updateFooter (oRowData, bIsAdd)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isBoolean(bIsAdd, "bIsAdd should be a boolean value");
	if(bIsAdd)
		m_oPOChallanListMemberData.m_nGrandTotal += oRowData.m_nAmount;
	else
		m_oPOChallanListMemberData.m_nGrandTotal -= oRowData.m_nAmount;
	POChallanList_reloadFooter ();
}

function POChallanList_getTotal(arrRows)
{
	var nAmount = 0;
	for(var nIndex=0; nIndex < arrRows.length; nIndex++)
		nAmount += arrRows[nIndex].m_nAmount;
	return nAmount;
}

function POChallanList_reloadFooter ()
{
	$('#POChallanList_table_ChallanListDG').datagrid('reloadFooter',[{m_strChallanNumber:'<b>Total To Be Billed :</b>', m_nAmount:m_oPOChallanListMemberData.m_nGrandTotal}]);
}

function POChallanList_list ()
{
	var oPurchaseOrderData = new PurchaseOrderData ();
	oPurchaseOrderData.m_nPurchaseOrderId = m_oPOChallanListMemberData.m_nPurchaseOrderId;
	POChallanDataProcessor.getUnbilledChallans (oPurchaseOrderData, POChallanList_listed);
}

function POChallanList_listed (oResponse)
{
	HideDialog ("ProcessDialog");
	clearGridData ("#POChallanList_table_ChallanListDG");
	$('#POChallanList_table_ChallanListDG').datagrid('loadData',oResponse.m_arrChallanData);
}

function purchaseReport_calculateSalesLineItemsAmount (arrSalesLineItemData)
{
	var nRowAmount = 0;
	for (var nIndex = 0 ; nIndex < arrSalesLineItemData.length; nIndex++)
	{
		arrSalesLineItemData[nIndex].m_nTaxPrice = (arrSalesLineItemData[nIndex].m_nPrice * (arrSalesLineItemData[nIndex].m_nTax/100));
		arrSalesLineItemData[nIndex].m_nAmount = (arrSalesLineItemData[nIndex].m_nPrice + arrSalesLineItemData[nIndex].m_nTaxPrice) * arrSalesLineItemData[nIndex].m_nQuantity;
		nRowAmount += arrSalesLineItemData[nIndex].m_nAmount;
	}
	return nRowAmount;
}

function purchaseReport_calculateNonStockSalesLineItemsAmount (arrNonStockSalesLineItemData)
{
	var nRowAmount = 0;
	for (var nIndex = 0 ; nIndex < arrNonStockSalesLineItemData.length; nIndex++)
	{
		arrNonStockSalesLineItemData[nIndex].m_nTaxPrice = (arrNonStockSalesLineItemData[nIndex].m_nPrice * (arrNonStockSalesLineItemData[nIndex].m_nTax/100));
		arrNonStockSalesLineItemData[nIndex].m_nAmount = (arrNonStockSalesLineItemData[nIndex].m_nPrice + arrNonStockSalesLineItemData[nIndex].m_nTaxPrice) * arrNonStockSalesLineItemData[nIndex].m_nQuantity;
		nRowAmount += arrNonStockSalesLineItemData[nIndex].m_nAmount;
	}
	return nRowAmount;
}

function POChallanList_lineItemsDG ()
{
	$('#POChallanList_table_ChallanListDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="POChallanList_table_lineItemsDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  POChallanList_table_lineItemsDG = $(this).datagrid('getRowDetail',index).find('table.POChallanList_table_lineItemsDG');
	        POChallanList_table_lineItemsDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',
	            columns:[[
	                {field:'m_strItemName',title:'Item Name',width:130},
	                {field:'m_nQuantity',title:'Qty',width:30,align:'right',
	                	formatter:function(value,row,index)
			        	{
							 return row.m_nQuantity.toFixed(2)
			        	}
	                },
	                {field:'m_nPrice',title:'Price',width:50,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                	     var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}	
	                },
	                {field:'m_nTax',title:'Tax(%)',width:30,align:'right',
	                	formatter:function(value,row,index)
			        	{
							 return row.m_nTax.toFixed(2)
			        	}
	                },
	                {field:'m_nAmount',title:'Amount',width:60,align:'right',
	                	formatter:function(value,row,index)
			        	{
	                		 var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
							 return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;	
			        	}
	                	}
	            ]],
	            onResize:function()
	            {
	                $('#POChallanList_table_ChallanListDG').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#POChallanList_table_ChallanListDG').datagrid('fixDetailRowHeight',index);
	        POChallanList_populateLineItems (POChallanList_table_lineItemsDG, row);
	    }
	});
}

function POChallanList_populateLineItems (POChallanList_table_lineItemsDG, oRowData)
{
	var arrSalesLineItems = new Array ();
	POChallanList_populateSalesLineItems (oRowData.m_oSalesData, arrSalesLineItems);
	POChallanList_populateNonStockSalesLineItems (oRowData.m_oSalesData, arrSalesLineItems)
	POChallanList_table_lineItemsDG.datagrid('loadData',arrSalesLineItems);
}

function POChallanList_populateSalesLineItems (oSalesData, arrSalesLineItems)
{
	var arrSalesLineItemData = oSalesData.m_oSalesLineItems;
	for (var nIndex = 0; nIndex < arrSalesLineItemData.length; nIndex++)
	{
		arrSalesLineItemData[nIndex].m_strItemName = arrSalesLineItemData[nIndex].m_oItemData.m_strItemName + " | " + arrSalesLineItemData[nIndex].m_oItemData.m_strDetail;
		arrSalesLineItemData[nIndex].m_nTaxPrice = (arrSalesLineItemData[nIndex].m_nPrice * (arrSalesLineItemData[nIndex].m_nTax/100));
		arrSalesLineItemData[nIndex].m_nAmount = (arrSalesLineItemData[nIndex].m_nPrice + arrSalesLineItemData[nIndex].m_nTaxPrice) * arrSalesLineItemData[nIndex].m_nQuantity;
		arrSalesLineItems.push(arrSalesLineItemData[nIndex]);
	}
}

function POChallanList_populateNonStockSalesLineItems (oSalesData, arrSalesLineItems)
{
	var arrNonStockSalesLineItemData = oSalesData.m_oNonStockSalesLineItems;
	for (var nIndex = 0; nIndex < arrNonStockSalesLineItemData.length; nIndex++)
	{
		arrNonStockSalesLineItemData[nIndex].m_strItemName = arrNonStockSalesLineItemData[nIndex].m_strArticleDescription;
		arrNonStockSalesLineItemData[nIndex].m_nTaxPrice = (arrNonStockSalesLineItemData[nIndex].m_nPrice * (arrNonStockSalesLineItemData[nIndex].m_nTax/100));
		arrNonStockSalesLineItemData[nIndex].m_nAmount = (arrNonStockSalesLineItemData[nIndex].m_nPrice + arrNonStockSalesLineItemData[nIndex].m_nTaxPrice) * arrNonStockSalesLineItemData[nIndex].m_nQuantity;
		arrSalesLineItems.push(arrNonStockSalesLineItemData[nIndex]);
	}
}

function POChallanList_makeBill ()
{
	var arrSelectedChallans = $('#POChallanList_table_ChallanListDG').datagrid('getChecked');
	HideDialog("secondDialog");
	try
	{
		POChallanList_handleMakeBill (arrSelectedChallans);
	}
	catch (oException)
	{
		// handler not defined;
	}
}

function POChallanList_cancel ()
{
    HideDialog("secondDialog");
}