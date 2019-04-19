var nonStockSupplyItems_includeDataObjects = 
	[
		'widgets/inventorymanagement/sales/SalesData.js',
		'widgets/inventorymanagement/item/itemData.js',
		'widgets/inventorymanagement/item/ItemCategoryData.js',
		'widgets/inventorymanagement/tax/ApplicableTaxData.js',
		'widgets/usermanagement/userinfo/UserInformationData.js',
		'widgets/purchaseordermanagement/supply/SupplyData.js',
		'widgets/purchaseordermanagement/supply/NonStockSupplyItemsData.js'
	];

 includeDataObjects (nonStockSupplyItems_includeDataObjects, "nonStockSupplyItems_loaded ()");

function nonStockSupplyItems_memberData ()
{
	this.m_arrPOLineItemIds = new Array ();
	this.m_arrNonStockSalesItems = new Array ();
}

var m_oNonStockSupplyItemsMemberData = new nonStockSupplyItems_memberData ();

function nonStockSupplyItems_loaded ()
{
	m_oNonStockSupplyItemsMemberData.m_arrPOLineItemIds = m_oSupplyMemberData.m_arrPOLineItemIds;
	m_oNonStockSupplyItemsMemberData.m_arrNonStockSalesItems = $('#supply_table_articles').datagrid('getRows');
	loadPage ("purchaseordermanagement/supply/supplyItemsList.html", "secondDialog", "nonStockSupplyItems_init ()");
}

function nonStockSupplyItems_init ()
{
	createPopup("secondDialog", "#supplyItemsList_button_add", "#supplyItemsList_button_cancel", true);
	nonStockSupplyItems_initializeDataGrid ();
}

function nonStockSupplyItems_initializeDataGrid ()
{
	$('#supplyItemsList_table_nonStockSalesItemsListDG').datagrid ({
	    columns:
	    [[ 
	        {field:'ckBox',checkbox:true},
	        {field:'m_strArticleDescription',title:'Item Name', width:200},
	        {field:'m_nQuantity',title:'Order Qty', align:'right', width:60},
	        {field:'m_nShippedQty',title:'Shipped Qty', align:'right', width:60,
	        	formatter:function(value,row,index)
	        	{
	        		return row.m_nShippedQty.toFixed(2);
	        	}
	        },
	        {field:'m_nPurchasedQty',title:'Purchased Qty', align:'right', width:70,
	        	formatter:function(value,row,index)
	        	{
	        		return row.m_nPurchasedQty.toFixed(2);
	        	}
	        },
	        {field:'m_nBalanceQuantity',title:'Bal Qty', align:'right', width:60,
	        	formatter:function(value,row,index)
	        	{
	        		var nBalanceQuantity = row.m_nShippedQty - row.m_nPurchasedQty;
	        		return nBalanceQuantity.toFixed(2);
	        	}
	        },
	        {field:'Info',title:'Info',width:30,align:'center',
				formatter:function(value,row,index)
	        	{
	        		return nonStockSupplyItems_addActions (value,row, index);
	        	}
			},
	    ]],
	    onLoadSuccess:function()
	    {
			nonStockSupplyItems_createTooltip();
	    },
	    onUncheck: function (nRowIndex, oRowData)
		{
	    	nonStockSupplyItems_unCheckNSSalesItemsDGRow (nRowIndex, oRowData);
		},
		onUncheckAll: function (arrRows)
		{
			nonStockSupplyItems_unCheckAllNSSalesItemsDGRow ();
		}
	});
	nonStockSupplyItems_getUnbilledNonStockItemList ();
}

function nonStockSupplyItems_addActions (value,row,index)
{
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td><img title="info" src="images/messager_info.gif" data-p1='+index+' class="easyui-tooltip" width="20" align="center"/></td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function nonStockSupplyItems_createTooltip()
{
    $('#supplyItemsList_table_nonStockSalesItemsListDG').datagrid('getPanel').find('.easyui-tooltip').each(function()
    		{
		        var nIndex = parseInt($(this).attr('data-p1'));
		        $(this).tooltip({content: $('<div></div>'), onUpdate: function(oContainer)
		        	{
		                var oRowData = $('#supplyItemsList_table_nonStockSalesItemsListDG').datagrid('getRows')[nIndex];
		                var oContent = '<div></div>';
		                oContent +='<table>'+
										'<tr>'+
											'<td align="right">Client :</td>'+ '<td><b>' + oRowData.m_oPurchaseOrderData.m_oClientData.m_strCompanyName + '</b></td>'+
										'</tr>'+
										'<tr>'+
											'<td align="right">Site :</td>'+ '<td><b>' + oRowData.m_oPurchaseOrderData.m_oSiteData.m_strSiteName + '</b></td>'+
										'</tr>'+
										'<tr>'+
											'<td align="right">Order No. :</td>'+ '<td><b>' + oRowData.m_oPurchaseOrderData.m_strPurchaseOrderNumber + '</b></td>'+
										'</tr>'+
										'<tr>'+
											'<td align="right">Date :</td>'+ '<td><b>' + oRowData.m_oPurchaseOrderData.m_strPurchaseOrderDate + '</b></td>'+
										'</tr>'+
									'</table>'
		                oContainer.panel({width:250,border: false,content:oContent});
		            },
		            position:'bottom'
		        });
    });
}

function nonStockSupplyItems_getUnbilledNonStockItemList ()
{
	var oNonStockSalesLineItemData = new NonStockSalesLineItemData ();
	oNonStockSalesLineItemData.m_oCreatedBy = new UserInformationData ();
	oNonStockSalesLineItemData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oNonStockSalesLineItemData.m_oCreatedBy.m_nUID = m_oTrademustMemberData.m_nUID;
	NonStockSalesLineItemDataProcessor.getUnbilledNonStockItemList (oNonStockSalesLineItemData, nonStockSupplyItems_gotUnbilledList);
}

function nonStockSupplyItems_gotUnbilledList (oResponse)
{
	var arrNonStockSalesLineItemData = oResponse.m_arrPurchaseOrderLineItems;
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrNonStockSalesLineItemData.length; nIndex++)
	{
		arrNonStockSalesLineItemData[nIndex].m_strArticleDescription = arrNonStockSalesLineItemData[nIndex].m_strDesc;
		arrNonStockSalesLineItemData[nIndex].m_nQuantity = Number(arrNonStockSalesLineItemData[nIndex].m_nQty).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_nPrice = Number(arrNonStockSalesLineItemData[nIndex].m_nPrice).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_nTax = Number(arrNonStockSalesLineItemData[nIndex].m_nTax).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_strTaxName = arrNonStockSalesLineItemData[nIndex].m_strTaxName;
	}
	$('#supplyItemsList_table_nonStockSalesItemsListDG').datagrid('loadData',arrNonStockSalesLineItemData);
	nonStockSupplyItems_checkNonStockSalesItemsDGRow ();
}

function nonStockSupplyItems_checkNonStockSalesItemsDGRow ()
{
	var arrNonStockSalesItems = $('#supplyItemsList_table_nonStockSalesItemsListDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrNonStockSalesItems.length; nIndex++)
	{
		if(nonStockSupplyItems_isRowSelectable(arrNonStockSalesItems[nIndex].m_nId))
			$("#supplyItemsList_table_nonStockSalesItemsListDG").datagrid('checkRow', nIndex);
	}
}

function nonStockSupplyItems_isRowSelectable (nPOlineItemId)
{
	assert.isNumber(nPOlineItemId, "nPOlineItemId expected to be a Number.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oNonStockSupplyItemsMemberData.m_arrPOLineItemIds.length && !bIsSelectable; nIndex++)
	{
		if(m_oNonStockSupplyItemsMemberData.m_arrPOLineItemIds[nIndex] == nPOlineItemId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function nonStockSupplyItems_unCheckNSSalesItemsDGRow (nRowIndex, oRowData)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	if (nonStockSupplyItems_isRowSelectable(oRowData.m_nId))
	{
		processConfirmation ('Yes', 'No', 'Do you want to Remove this Item ?', function(bConfirm)
				{
					if (!bConfirm)
						$("#supplyItemsList_table_nonStockSalesItemsListDG").datagrid('checkRow', nRowIndex);
					else
						nonStockSupplyItems_removeNSSalesItemsInArray (oRowData.m_nId);
				}
		);
	}
}

function nonStockSupplyItems_unCheckAllNSSalesItemsDGRow ()
{
	if (m_oNonStockSupplyItemsMemberData.m_arrPOLineItemIds.length > 0)
	{
		processConfirmation ('Yes', 'No', 'This Action will Remove already Added Supplies. Do you want to maintain the Added Supplies ?', function(bConfirm)
				{
					if (bConfirm)
						nonStockSupplyItems_checkNonStockSalesItemsDGRow ();
					else
						m_oNonStockSupplyItemsMemberData.m_arrNonStockSalesItems = new Array();
				}
		);
	}
}

function nonStockSupplyItems_removeNSSalesItemsInArray (nLineItemId)
{
	assert.isNumber(nLineItemId, "nLineItemId expected to be a Number.");
	for(nIndex = 0; nIndex < m_oNonStockSupplyItemsMemberData.m_arrNonStockSalesItems.length; nIndex++)
	{
		if(m_oNonStockSupplyItemsMemberData.m_arrNonStockSalesItems[nIndex].m_nId == nLineItemId)
		{
			m_oNonStockSupplyItemsMemberData.m_arrNonStockSalesItems.splice(nIndex, 1);
			break;
		}
	}
}

function nonStockSupplyItems_addNonStockSalesItems ()
{
	var arrNonStockSalesItems = m_oNonStockSupplyItemsMemberData.m_arrNonStockSalesItems;
	m_oSupplyMemberData.m_arrPOLineItemIds = new Array();
	nonStockSupplyItems_buildNonStockSalesItems(arrNonStockSalesItems);
	$('#supply_table_articles').datagrid('loadData',arrNonStockSalesItems);
	HideDialog ("secondDialog");
}

function nonStockSupplyItems_buildNonStockSalesItems(arrNonStockSalesItems)
{
	assert.isArray(arrNonStockSalesItems, "arrNonStockSalesItems expected to be an Array.");
	var arrNSSalesItems = $('#supplyItemsList_table_nonStockSalesItemsListDG').datagrid('getChecked');
	for(nIndex = 0; nIndex < arrNSSalesItems.length; nIndex++)
	{
		m_oSupplyMemberData.m_arrPOLineItemIds.push(arrNSSalesItems[nIndex].m_nId);
		arrNSSalesItems[nIndex].m_nPrice = 0;
		arrNSSalesItems[nIndex].m_nDiscount = 0;
		arrNSSalesItems[nIndex].m_nOtherCharges = 0;
		arrNSSalesItems[nIndex].m_nQuantity = arrNSSalesItems[nIndex].m_nShippedQty - arrNSSalesItems[nIndex].m_nPurchasedQty;
		arrNSSalesItems[nIndex].m_nBalanceQuantity = arrNSSalesItems[nIndex].m_nShippedQty - arrNSSalesItems[nIndex].m_nPurchasedQty;
		if (!nonStockSupplyItems_isRowSelectable(arrNSSalesItems[nIndex].m_nId))
			arrNonStockSalesItems.push(arrNSSalesItems[nIndex]);
	}
	return arrNonStockSalesItems;
}
