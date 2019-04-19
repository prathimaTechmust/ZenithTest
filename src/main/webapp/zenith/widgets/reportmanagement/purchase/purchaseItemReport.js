var purchaseItemReport_includeDataObjects =
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

 includeDataObjects (purchaseItemReport_includeDataObjects, "purchaseItemReport_loaded()");

function purchaseItemReport_memberData ()
{
	this.m_nGrandTotal = 0;
}

var m_oPurchaseItemReportData = new purchaseItemReport_memberData ();

function purchaseItemReport_loaded ()
{
	loadPage ("reportmanagement/purchase/purchaseItemReport.html", "dialog", "purchaseItemReport_init()");
}

function purchaseItemReport_init ()
{
	createPopup("dialog", "#purchaseItemReport_button_cancel", "#purchaseItemReport_button_cancel", true);
	purchaseItemReport_initializeDG ();
}

function purchaseItemReport_initializeDG ()
{
	$('#purchaseItemReport_table_purchaseDG').datagrid
	(
		{
			columns:[[
			          	{field:'m_strArticleNumber',title:'Article#',width:120},
	                    {field:'m_strItemName',title:'Item Name',width:120},
		                {field:'m_nQuantity',title:'Quantity',width:60,align:'right'},
		                {field:'m_nPrice',title:'Price',width:100,align:'right',
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
		                {field:'m_nAmount',title:'Amount',width:120,align:'right',
		                	formatter:function(value,row,index)
				        	{
		                		var m_nIndianNumber = "";
		                    	try
		                    	{
		                    		m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
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
	purchaseItemReport_list (); 
}

function purchaseItemReport_list ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_nId = m_oPurchaseReportMemberData.m_nSelectedId;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.get (oPurchaseData,purchaseItemReport_gotData)
}

function purchaseItemReport_gotData (oResponse)
{
	var arrPurchaseData = oResponse.m_arrPurchase;
	var arrPurchaseLineItemData = getOrderedLineItems (arrPurchaseData [0].m_oPurchaseLineItems);
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrPurchaseLineItemData.length; nIndex++)
	{
		arrPurchaseLineItemData[nIndex].m_strArticleNumber = arrPurchaseLineItemData[nIndex].m_oItemData.m_strArticleNumber;
		arrPurchaseLineItemData[nIndex].m_strItemName = arrPurchaseLineItemData[nIndex].m_oItemData.m_strItemName;
		arrPurchaseLineItemData[nIndex].m_nQuantity = Number(arrPurchaseLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrPurchaseLineItemData[nIndex].m_nPrice = Number(arrPurchaseLineItemData[nIndex].m_nPrice).toFixed(2);
		arrPurchaseLineItemData[nIndex].m_nAmount = purchaseItemReport_getAmount(arrPurchaseLineItemData[nIndex]);
		nTotal += Number(arrPurchaseLineItemData[nIndex].m_nAmount);
		$('#purchaseItemReport_table_purchaseDG').datagrid('appendRow',arrPurchaseLineItemData[nIndex]);
	}
	
	$('#purchaseItemReport_table_purchaseDG').datagrid('reloadFooter',[{m_nPrice:'<b>Grand Total :</b>', m_nAmount:nTotal}]);
}

function purchaseItemReport_getAmount (oPurchaseOrderLineItem)
{
	var nAmount = 0;
	nAmount =  oPurchaseOrderLineItem.m_nQuantity * oPurchaseOrderLineItem.m_nPrice ;
	return nAmount;
}

function purchaseItemReport_cancel ()
{
	HideDialog("dialog");
}
