var itemGroupTransaction_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/item/ItemGroupData.js',
	'widgets/inventorymanagement/item/ChildItemData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js'
];

includeDataObjects (itemGroupTransaction_includeDataObjects, "itemGroupTransaction_loaded()");

function itemGroupTransaction_memberData ()
{
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_nSelectedItemId = -1;
	this.m_nSelectedItemGroupId = -1;
	this.m_nClientId = -1;
	this.m_strSortColumn = "m_nAmount";
	this.m_strSortOrder = "desc";
}

var m_oItemGroupTransactionData = new itemGroupTransaction_memberData ();

function itemGroupTransaction_init ()
{
	m_oItemGroupTransactionData.m_nSelectedItemGroupId = m_oItemGroupTransactionData.m_nSelectedItemId;
	createPopup("dialog", "#itemGroupTransaction_button_cancel", "#itemGroupTransaction_button_cancel", true);
	itemGroupTransaction_initializeClientsDG ();
	itemGroupTransaction_getClientTransactions (m_oItemGroupTransactionData.m_strSortColumn, m_oItemGroupTransactionData.m_strSortOrder);
}

function itemGroupTransaction_initializeClientsDG ()
{
	$('#itemGroupTransaction_table_clientsDG').datagrid
	(
		{
			columns:
			[[
				{field:'m_strTo',title:'Client Name',sortable:true,width:200,
					formatter:function(value,row,index)
					{
						try
						{
							var nCompanyName = row[0].m_strCompanyName;
							return nCompanyName;
						}
						catch(oException)
						{
							return value;
						}
					}	
				},
				{field:'m_nAmount',title:'Amount',sortable:true,align:'right',width:120,
					formatter:function(value,row,index)
		        	{
						try
						{
							var nIndianFormat = formatNumber (row[1].toFixed(2),row,index);
							return '<span class="rupeeSign">R  </span>' + nIndianFormat;
						}
						catch(oException)
						{
							var nIndianFormat = formatNumber (value.toFixed(2),row,index);
							return '<span class="rupeeSign">R  </span>' + nIndianFormat;
						}
		        	},
		        	styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}	
				},
				{field:'Actions',title:'Action',width:40,align:'center',
					formatter:function(value,row,index)
		        	{
						try
						{
							return itemGroupTransaction_displayImages (row, index);
						}
		        		catch(oException)
		        		{
		        			return '';
		        		}
		        	}
				}
			]],
			onSortColumn: function (strColumn, strOrder)
			{
				m_oItemGroupTransactionData.m_strSortColumn = strColumn;
				m_oItemGroupTransactionData.m_strSortOrder = strOrder;
				itemGroupTransaction_getClientTransactions (strColumn, strOrder);
			}
		}
	);
}

function itemGroupTransaction_getClientTransactions (strColumn, strOrder)
{
	m_oItemGroupTransactionData.m_strSortColumn = strColumn;
	m_oItemGroupTransactionData.m_strSortOrder = strOrder;
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "itemGroupTransaction_progressbarLoaded ()");
}

function itemGroupTransaction_progressbarLoaded ()
{
	createPopup("secondDialog", "", "", true);
	var oSalesData = new SalesData ();
	oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oSalesData.m_bIsForItemGroupClientProfile = true;
	oSalesData.m_nItemGroupId = m_oItemGroupTransactionData.m_nSelectedItemGroupId;
	SalesDataProcessor.listClientSales (oSalesData, m_oItemGroupTransactionData.m_strSortColumn, m_oItemGroupTransactionData.m_strSortOrder, itemGroupTransaction_gotClientTransactions);
}

function itemGroupTransaction_gotClientTransactions (oResponse)
{
	clearGridData ("#itemGroupTransaction_table_clientsDG");
	$('#itemGroupTransaction_table_clientsDG').datagrid('loadData',oResponse.m_arrSales)
	var nTotalAmount = 0;
	$('#itemGroupTransaction_table_clientsDG').datagrid('reloadFooter',[{m_strTo:'<b>Total</b>', m_nAmount:nTotalAmount, Actions:''}]);
	HideDialog("secondDialog");
}

function itemGroupTransaction_cancel ()
{
	HideDialog ("dialog");
}

function itemGroupTransaction_displayImages (oRow, nIndex)
{
	var oImage = '<table align="center">'+
	'<tr>'+
	'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="itemGroupTransaction_getInfo ('+oRow[0].m_nClientId+')"/> </td>'+
	'</tr>'+
	'</table>'
	return oImage;
}

function itemGroupTransaction_getInfo (nClientId)
{
	m_oItemGroupTransactionData.m_nClientId = nClientId;
	m_oItemGroupTransactionData.m_nItemGroupId = m_oItemGroupTransactionData.m_nSelectedItemGroupId;
	navigate ("clientwiseItemGroupTransaction", "widgets/inventorymanagement/item/clientwiseItemGroupTransaction.js");
}