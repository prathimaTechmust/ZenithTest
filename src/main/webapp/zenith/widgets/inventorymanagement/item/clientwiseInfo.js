var clientwiseInfo_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js'
];

includeDataObjects (clientwiseInfo_includeDataObjects, "clientwiseInfo_loaded()");

var m_oClientwiseInfo = new clientwiseInfo_memberData ();

function clientwiseInfo_memberData ()
{
	this.m_arrSelectedData = new Array ();
}

function clientwiseInfo_loaded ()
{
	loadPage ("inventorymanagement/item//clientwiseInfo.html", "secondDialog", "clientwiseInfo_init ()");
}

function clientwiseInfo_init ()
{
	createPopup("secondDialog", "", "", true);
	clientwiseInfo_initDG ();
}

function clientwiseInfo_initDG ()
{
	$('#clientwiseInfo_table_salesDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strDate',title:'Date',sortable:true,width:120},
				{field:'m_strInvoiceNo',title:'INV#', align:'right', sortable:true,width:100},
				{field:'m_nQuantity',title:'Qty',sortable:true,align:'right',width:100,
					formatter:function(value,row,index)
					{
						return row.m_nQuantity.toFixed(2);
					}
				}
			]]
		}
	);
	clientwiseInfo_getSalesInfo ();
}

function clientwise_getQuantity (arrSalesLineItems)
{
	assert.isArray(arrSalesLineItems, "arrSalesLineItems expected to be an Array.");
	var nQuantity = 0;
	for (var nIndex = 0; nIndex < arrSalesLineItems.length; nIndex++)
	{
		if(arrSalesLineItems[nIndex].m_oItemData.m_nItemId == m_oItemTransactionData.m_nSelectedItemId)
		{
			nQuantity = arrSalesLineItems[nIndex].m_nQuantity;
			break;
		}
	}
	return nQuantity;
}

function clientwiseInfo_cancel ()
{
	HideDialog ("secondDialog");
}

function clientwiseInfo_getSalesInfo ()
{
	var oSalesData = new SalesData ();
	oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oSalesData.m_nItemId = m_oItemTransactionData.m_nSelectedItemId;
	oSalesData.m_oClientData.m_nClientId = m_oItemTransactionData.m_nClientId;
	SalesDataProcessor.list(oSalesData, "", "", 0,10, clientwiseInfo_gotSalesTransactions);
}

function clientwiseInfo_gotSalesTransactions (oResponse)
{
	clearGridData ("#clientwiseInfo_table_salesDG");
	$('#clientwiseInfo_table_salesDG').datagrid('loadData',oResponse.m_arrSales);
	var nQuantity = 0;
	$('#clientwiseInfo_table_salesDG').datagrid('reloadFooter',[{m_strInvoiceNo:'<b>Total Qty</b>', m_nQuantity:nQuantity }]);
}
