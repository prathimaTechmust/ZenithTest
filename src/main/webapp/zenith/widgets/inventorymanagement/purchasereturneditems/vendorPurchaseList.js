var vendorPurchaseList_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js'
];



 includeDataObjects (vendorPurchaseList_includeDataObjects, "vendorPurchaseList_loaded ()");

function vendorPurchaseList_memberData ()
{
	this.m_bIsForfilter = false;
	this.m_arrSelectedData = new Array();
}
var m_oVendorPurchaseListMemberData = new vendorPurchaseList_memberData ();

function vendorPurchaseList_loaded ()
{
	loadPage ("inventorymanagement/purchasereturneditems/vendorPurchaseList.html", "secondDialog", "vendorPurchaseList_init()");
}

function vendorPurchaseList_init ()
{
	var arrItems = $('#purchaseReturnedItems_table_itemDG').datagrid('getRows');
	m_oVendorPurchaseListMemberData.m_arrSelectedData = arrItems;
	createPopup("secondDialog", "#vendorPurchaseList_button_cancel", "#vendorPurchaseList_button_add", true);
//	 $("#vendorPurchaseList_input_date").datebox();
	$( "#vendorPurchaseList_input_date" ).datepicker({minDate:"-90y", maxDate: new Date()});
	initUserCombobox ('#vendorPurchaseList_input_receivedBy', "Received By");
	vendorPurchaseList_initializeDataGrid ();
	vendorPurchaseList_getPurchaseList ('m_dCreatedOn', 'desc', 1, 10);
}

function vendorPurchaseList_initializeDataGrid ()
{
	$('#vendorPurchaseList_table_purchaseItemsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strUserName',title:'Received By',sortable:true,width:100,
			  		formatter:function(value,row,index)
		        	{
						 return row.m_oCreatedBy.m_strUserName;	
		        	}
			  	},
				{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:80},
				{field:'m_strDate',title:'Date',sortable:true,width:40},
			]]
		}
	);
	vendorPurchaseList_subGridPurchase ();
}

function vendorPurchaseList_subGridPurchase ()
{
	$('#vendorPurchaseList_table_purchaseItemsDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="vendorPurchaseList_table_lineItemsDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  vendorPurchaseList_table_lineItemsDG = $(this).datagrid('getRowDetail',index).find('table.vendorPurchaseList_table_lineItemsDG');
	        vendorPurchaseList_table_lineItemsDG.datagrid({fitColumns:true,rownumbers:true,height:'auto',
	            columns:[[
	                {field:'ckBox',checkbox:true},
	                {field:'m_strArticleNumber',title:'Article Number',width:100},
	                {field:'m_strArticleDescription',title:'Item Name',width:120},
	                {field:'m_nQuantity',title:'Quantity',width:60,align:'right'},
	                {field:'m_nReturnQuantity',title:'Returned Qty',width:80,align:'right'}
	            ]],
	            onResize:function()
	            {
	                $('#vendorPurchaseList_table_purchaseItemsDG').datagrid('fixDetailRowHeight',index);
	            },
	            onLoadSuccess: function (data) {
                    for (i = 0; i < data.rows.length; ++i) 
                    {
                        if (data.rows[i]['ckBox'] == 1) 
                        	$(this).datagrid('checkRow', i);
                    }
                },
                onCheck: function (rowIndex, rowData)
    			{
                	vendorPurchaseList_holdCheckedData(rowData, true);
    			},
    			onUncheck: function (rowIndex, rowData)
    			{
    				vendorPurchaseList_holdCheckedData(rowData, false);
    			},
    			onCheckAll: function (arrRows)
    			{
    				vendorPurchaseList_holdAllCheckedData (arrRows);
    			},
    			onUncheckAll: function (arrRows)
    			{
    				vendorPurchaseList_holdAllUnCheckedData (arrRows)
    			}
	        });
	        $('#vendorPurchaseList_table_purchaseItemsDG').datagrid('fixDetailRowHeight',index);
	        vendorPurchaseList_populateLineItems (vendorPurchaseList_table_lineItemsDG, index, row);
	        vendorPurchaseList_checkDGRow (vendorPurchaseList_table_lineItemsDG);
	    }
	});
}

function vendorPurchaseList_getPurchaseList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oPurchaseData = vendorPurchaseList_getFormData ();
	oPurchaseData.m_oVendorData.m_nClientId = m_oTrademustMemberData.m_nSelectedClientId;
	PurchaseDataProcessor.list(oPurchaseData, strColumn, strOrder, nPageNumber, nPageSize, vendorPurchaseList_listed);
}

function vendorPurchaseList_getFormData ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oUserCredentialsData = new UserInformationData ();
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oPurchaseData.m_oCreatedBy.m_nUserId = $('#vendorPurchaseList_input_receivedBy').combobox('getValue');
	oPurchaseData.m_strDate =  FormatDate ($('#vendorPurchaseList_input_date').val());
	return oPurchaseData;
}

function vendorPurchaseList_listed (oResponse)
{	
	$('#vendorPurchaseList_table_purchaseItemsDG').datagrid('loadData', oResponse.m_arrPurchase);
}

function vendorPurchaseList_populateLineItems (vendorPurchaseList_table_detailViewDG, index, row)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var arrPurchaseLine = new Array ();
	var arrPurchaseLineItemData = row.m_oPurchaseLineItems;
	for (var nIndex = 0; nIndex < arrPurchaseLineItemData.length; nIndex++)
	{
		arrPurchaseLineItemData[nIndex].m_strArticleDescription = arrPurchaseLineItemData[nIndex].m_oItemData.m_strItemName;
		arrPurchaseLineItemData[nIndex].m_strArticleNumber = arrPurchaseLineItemData[nIndex].m_oItemData.m_strArticleNumber;
		arrPurchaseLineItemData[nIndex].m_nQuantity = Number(arrPurchaseLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrPurchaseLineItemData[nIndex].m_nReturnQuantity = Number(arrPurchaseLineItemData[nIndex].m_nReturnedQuantity).toFixed(2);
		arrPurchaseLine.push(arrPurchaseLineItemData[nIndex]);
	}
	vendorPurchaseList_table_detailViewDG.datagrid('loadData',arrPurchaseLine);
}

function vendorPurchaseList_addLineItems ()
{
	var arrLineItems = new Array ();
	for(var nIndex = 0; nIndex < m_oVendorPurchaseListMemberData.m_arrSelectedData.length; nIndex++)
	{
		(m_oVendorPurchaseListMemberData.m_arrSelectedData[nIndex].m_nReturnQuantity > 0) ? m_oVendorPurchaseListMemberData.m_arrSelectedData[nIndex].m_nReturnQuantity : "0";
		arrLineItems.push(m_oVendorPurchaseListMemberData.m_arrSelectedData[nIndex]);
	}
	$('#purchaseReturnedItems_table_itemDG').datagrid('loadData', arrLineItems);
	purchaseReturnedItems_loadFooterDG ();
	HideDialog("secondDialog");
}

function vendorPurchaseList_filter ()
{
	vendorPurchaseList_getPurchaseList ('m_dCreatedOn', 'desc', 1, 10);
}

function vendorPurchaseList_cancel ()
{
	HideDialog("secondDialog");
}

function vendorPurchaseList_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	if(bIsForAdd)
	{
		if(!vendorPurchaseList_isRowAdded (oRowData))
			m_oVendorPurchaseListMemberData.m_arrSelectedData.push(oRowData);
	}
	else
		vendorPurchaseList_remove (oRowData);
}

function vendorPurchaseList_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oVendorPurchaseListMemberData.m_arrSelectedData.length; nIndex++)
		bIsRowAdded = (m_oVendorPurchaseListMemberData.m_arrSelectedData [nIndex].m_nLineItemId == oRowData.m_nLineItemId);
	return bIsRowAdded;
}

function  vendorPurchaseList_remove (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	for (var nIndex = 0; nIndex < m_oVendorPurchaseListMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oVendorPurchaseListMemberData.m_arrSelectedData[nIndex].m_nLineItemId == oRowData.m_nLineItemId)
		{
			m_oVendorPurchaseListMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function vendorPurchaseList_checkDGRow (vendorPurchaseList_table_detailViewDG)
{
	var arrItems = vendorPurchaseList_table_detailViewDG.datagrid('getRows');
	for (nIndex = 0; nIndex < arrItems.length; nIndex++)
	{
		if(vendorPurchaseList_isRowSelectable(arrItems[nIndex].m_nLineItemId))
			vendorPurchaseList_table_detailViewDG.datagrid('checkRow', nIndex);
	}
}

function vendorPurchaseList_isRowSelectable (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oVendorPurchaseListMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oVendorPurchaseListMemberData.m_arrSelectedData[nIndex].m_nLineItemId == nItemId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function vendorPurchaseList_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows expected to be an Array.");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		vendorPurchaseList_holdCheckedData(arrRows[nIndex], true);
}

function vendorPurchaseList_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows expected to be an Array.");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		vendorPurchaseList_holdCheckedData(arrRows[nIndex], false);
}