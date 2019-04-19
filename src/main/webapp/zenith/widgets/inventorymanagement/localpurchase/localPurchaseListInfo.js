var localPurchaseListInfo_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderStockLineItemData.js',
	'widgets/inventorymanagement/sales/NonStockSalesLineItemData.js',
	'widgets/inventorymanagement/localpurchase/LocalPurchaseData.js'
];


includeDataObjects (localPurchaseListInfo_includeDataObjects, "localPurchaseListInfo_loaded()");

function localPurchaseListInfo_loaded ()
{
	loadPage ("inventorymanagement/localpurchase/localPurchaseInfo.html", "dialog", "localPurchaseListInfo_init ()");
}

function localPurchaseListInfo_init ()
{
	var strXml = m_oLocalPurchaseListMemberData.m_strXMLDataForInfo;
	var oPurchaseOrderData = m_oLocalPurchaseListMemberData.m_oSelectedRowData ;
	createPopup ("dialog", "", "", true);
	populateXMLData (strXml, "inventorymanagement/localpurchase/localPurchaseDetails.xslt", 'localPurchaseInfo_div_listDetail');
	localPurchaseList_initializeDetailsDG ();
	PurchaseOrderDataProcessor.get (oPurchaseOrderData, localPurchaseList_gotPurchaseOrderLineItemData)	
}

function localPurchaseList_initializeDetailsDG ()
{
	$('#localPurchaseDetails_table_PODetailsDG').datagrid
	(
		{
			columns:
			[[
				{field:'m_strDesc',title:'Item Name',sortable:true,width:200},
				{field:'m_nQty',title:'Qty',sortable:true,width:50,align:'right',
					formatter:function (value,row,index)
					{
						var nQty = row.m_nQty;
						try
						{
							if (!isNaN(value))
								nQty = nQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nQty;
					}	
				},
				{field:'m_nPrice',title:'Price',sortable:true,width:100,align:'right',
					formatter:function(value,row,index)
		        	{
					    var nPrice = row.m_nPrice;
					    try
						{
							if (!isNaN(nPrice))
								nPrice = '<span class="rupeeSign">R </span>' + formatNumber (nPrice.toFixed(2),row,index);
							else
								return value;
						}
					    catch(oException){}
						return nPrice;
		        	}	
				},
				{field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',
					formatter:function (value,row,index)
					{
						var nDiscount = row.m_nDiscount;
						try
						{
							if (!isNaN(value))
								nDiscount = nDiscount.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
						 return nDiscount;
					}
				},
				{field:'m_nTax',title:'Tax(%)',sortable:true,width:70,align:'right',
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
				{field:'m_nShipQty',title:'Ship Qty',sortable:true,width:80,align:'right',
					formatter:function (value,row,index)
					{
						var nShipQty = row.m_nShipQty;
						try
						{
							if (!isNaN(value))
								nShipQty = nShipQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nShipQty;
					}	
				},
				{field:'m_nShippedQty',title:'Shipped Qty',sortable:true,width:90,align:'right',
					formatter:function (value,row,index)
					{
						var nShippedQty = row.m_nShippedQty;
						try
						{
							if (!isNaN(value))
								nShippedQty = nShippedQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nShippedQty;
					}	
				},
				{field:'m_nChallanQty',title:'Challaned Qty',sortable:true,width:100,align:'right',
					formatter:function (value,row,index)
					{
						var nChallanQty = row.m_nChallanQty;
						try
						{
							if (!isNaN(value))
								nChallanQty = nChallanQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nChallanQty;
					}	
				},
				{field:'m_nInvoiceQty',title:'Invoiced Qty',sortable:true,width:100,align:'right',
					formatter:function (value,row,index)
					{
						var nInvoiceQty = row.m_nInvoiceQty;
						try
						{
							if (!isNaN(value))
								nInvoiceQty = nInvoiceQty.toFixed(2);
							else
								return value;
						}
						 catch(oException){}
							return nInvoiceQty;
					}	
				},
				{field:'m_nAmount',title:'Amount',width:120,align:'right',
					formatter:function(value,row,index)
		        	{
						var nIndianFormat = formatNumber (value,row,index);
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

function localPurchaseList_gotPurchaseOrderLineItemData (oResponse)
{
	clearGridData ("#localPurchaseDetails_table_PODetailsDG");
	$('#localPurchaseDetails_table_PODetailsDG').datagrid('loadData', oResponse.m_arrPurchaseOrder);
	var arrPurchaseOrderLineItemData = getOrderedLineItems (arrPurchaseOrderData [0].m_oPurchaseOrderLineItems);
	var nTotal = 0;
	$('#localPurchaseDetails_table_PODetailsDG').datagrid('reloadFooter',[{m_nInvoiceQty:'<b>Total</b>', m_nAmount: nTotal.toFixed(2)}]);
}

function localPurchaseInfo_close ()
{
	HideDialog ("dialog");
}