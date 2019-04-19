var salesItemReport_includeDataObjects =
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/reportmanagement/sales/SalesReportData.js'
]

 includeDataObjects (salesItemReport_includeDataObjects, "salesItemReport_loaded()");

function salesItemReport_memberData ()
{
}

var m_osalesItemReportData = new salesItemReport_memberData ();

function salesItemReport_loaded ()
{
	loadPage ("reportmanagement/sales/salesItemReport.html", "dialog", "salesItemReport_init()");
}

function salesItemReport_init ()
{
	createPopup("dialog", "#salesItemReport_button_cancel", "#salesItemReport_button_cancel", true);
	salesItemReport_initializeDG ();
}

function salesItemReport_initializeDG ()
{
	$('#salesItemReport_table_salesDG').datagrid
	(
		{
			columns:[[
	                    {field:'m_strArticleNumber',title:'Article#',width:120},
		                {field:'m_strItemName',title:'Item Name',width:160},
		                {field:'m_nQuantity',title:'Quantity',width:60,align:'right'},
		                {field:'m_nPrice',title:'Price',sortable:true,align:'right',width:60,
							formatter:function(value,row,index)
				        	{
							    var nPrice = row.m_nPrice;
							    try
								{
									if (!isNaN(value))
										nPrice = '<span class="rupeeSign">R </span>' + formatNumber (nPrice,row,index);
									else
										return value;
								}
							    catch(oException){}
								return nPrice;
				        	}	
						},
						{field:'m_nDiscount',title:'Disc(%)',sortable:true,align:'right',width:55},
		                {field:'m_nAmount',title:'Amount',width:120,align:'right',
		                	formatter:function(value,row,index)
				        	{
		                		var m_nIndianNumber = "";
		                    	try
		                    	{
		                    		m_nIndianNumber = formatNumber (row.m_nAmount.toFixed(2),row,index);
		                    		m_nIndianNumber = '<span class="rupeeSign">R  </span>' +m_nIndianNumber;
		                    	}
		                    	catch (oException)
		                    	{
		                    		
		                    	}
		                    	return  m_nIndianNumber;
				        	}
		                }
	                ]]
		}
	);
	salesItemReport_list (); 
}

function salesItemReport_list ()
{
	var oSalesData = new SalesData ();
	oSalesData.m_nId = m_oSalesReportMemberData.m_nSelectedSalesId;
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	SalesDataProcessor.get (oSalesData, salesItemReport_gotData)
}

function salesItemReport_gotData (oResponse)
{
	clearGridData ("#salesItemReport_table_salesDG");
	var arrSalesData = oResponse.m_arrSales;
	var nTotal =0;
	nTotal += salesItemReport_buildSalesLineItems (arrSalesData);
	$('#salesItemReport_table_salesDG').datagrid('reloadFooter',[{m_nDiscount:'<b>Grand Total</b>', m_nAmount: nTotal}]);
}

function salesItemReport_buildSalesLineItems (arrSalesData)
{
	var nTotal = 0;
	var arrSalesLineItemData = arrSalesData [0].m_oSalesLineItems.concat(arrSalesData [0].m_oNonStockSalesLineItems);
	arrSalesLineItemData = getOrderedLineItems (arrSalesLineItemData);
	for (var nIndex = 0; nIndex < arrSalesLineItemData.length; nIndex++)
	{
		try
		{
			arrSalesLineItemData[nIndex].m_strArticleNumber = arrSalesLineItemData[nIndex].m_oItemData.m_strArticleNumber;
		}
		catch (oException)
		{
			arrSalesLineItemData[nIndex].m_strArticleNumber = "";
		}
		arrSalesLineItemData[nIndex].m_strItemName = arrSalesLineItemData[nIndex].m_strArticleDescription;
		arrSalesLineItemData[nIndex].m_nQuantity = arrSalesLineItemData[nIndex].m_nQuantity.toFixed(2);
		arrSalesLineItemData[nIndex].m_nPrice = arrSalesLineItemData[nIndex].m_nPrice.toFixed(2);
		arrSalesLineItemData[nIndex].m_nDiscount = arrSalesLineItemData[nIndex].m_nDiscount.toFixed(2);
		arrSalesLineItemData[nIndex].m_nDiscountPrice = (arrSalesLineItemData[nIndex].m_nPrice * (arrSalesLineItemData[nIndex].m_nDiscount/100));
		arrSalesLineItemData[nIndex].m_nAmount = ((arrSalesLineItemData[nIndex].m_nPrice - arrSalesLineItemData[nIndex].m_nDiscountPrice) * arrSalesLineItemData[nIndex].m_nQuantity);
		nTotal += Number (arrSalesLineItemData[nIndex].m_nAmount);
		$('#salesItemReport_table_salesDG').datagrid('appendRow',arrSalesLineItemData[nIndex]);
	}
	return nTotal;
}

function salesItemReport_cancel ()
{
	HideDialog("dialog");
}

function salesItemReport_calculateRowAmount (oSalesLineItems)
{
	var nRowAmount = 0;
		nRowAmount += oSalesLineItems.m_nQuantity * oSalesLineItems.m_nPrice;
	return nRowAmount;
}