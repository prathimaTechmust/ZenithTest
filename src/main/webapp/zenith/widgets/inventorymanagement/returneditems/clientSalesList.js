var clientSalesList_includeDataObjects = 
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
	'widgets/clientmanagement/BusinessTypeData.js'];


includeDataObjects (clientSalesList_includeDataObjects, "clientSalesList_loaded ()");

function clientSalesList_memberData ()
{
	this.m_bIsForfilter = false;
	this.m_arrCheckedLineItems = new Array ();
	this.m_arrSelectedData = new Array ();
}
var m_oClientSalesListMemberData = new clientSalesList_memberData ();

function clientSalesList_loaded ()
{
	loadPage ("inventorymanagement/returneditems/clientSalesList.html", "secondDialog", "clientSalesList_init()");
}

function clientSalesList_init ()
{
	var arrItems = $('#returnedItems_table_itemDG').datagrid('getRows');
	m_oClientSalesListMemberData.m_arrSelectedData = arrItems;
	createPopup("secondDialog", "#clientSalesList_button_cancel", "#clientSalesList_button_add", true);
	$("#clientSalesList_input_date").datebox();
	initUserCombobox ('#clientSalesList_input_soldBy', "Sold By");
	clientSalesList_initializeDataGrid ();
	clientSalesList_getSalesList ('m_dCreatedOn', 'desc', 1, 10);
}

function clientSalesList_initializeDataGrid ()
{
	$('#clientSalesList_table_salesItemsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strUserName',title:'Sold By',sortable:true,width:100,
			  		formatter:function(value,row,index)
		        	{
						 return row.m_oCreatedBy.m_strUserName;	
		        	}
			  	},
				{field:'m_strChallanNo',title:'Challan No.',sortable:true,width:80},
				{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:80},
				{field:'m_strDate',title:'Date',sortable:true,width:40},
			]]
		}
	);
	clientSalesList_subGridSales ();
}

function clientSalesList_subGridSales ()
{
	$('#clientSalesList_table_salesItemsDG').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="clientSalesList_table_lineItemsDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  clientSalesList_table_lineItemsDG = $(this).datagrid('getRowDetail',index).find('table.clientSalesList_table_lineItemsDG');
	        clientSalesList_table_lineItemsDG.datagrid({fitColumns:true,rownumbers:true,height:'auto',
	            columns:[[
	                {field:'ckBox',checkbox:true},
	                {field:'m_strArticleNumber',title:'Article Number',width:100},
	                {field:'m_strArticleDescription',title:'Item Name',width:120},
	                {field:'m_nQuantity',title:'Quantity',width:60,align:'right'},
	                {field:'m_nReturnedQty',title:'Returned Qty',width:80,align:'right'}
	            ]],
	            onResize:function()
	            {
	                $('#clientSalesList_table_salesItemsDG').datagrid('fixDetailRowHeight',index);
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
                	clientSalesList_holdCheckedData(rowData, true);
                	//clientSalesList_setSelectedItem (index, rowIndex, 1, rowData.m_oItemData);
    			},
    			onUncheck: function (rowIndex, rowData)
    			{
    				clientSalesList_holdCheckedData(rowData, false);
    				//clientSalesList_setSelectedItem (index, rowIndex, 0, rowData.m_oItemData);
    			},
    			onCheckAll: function (rows)
    			{
    				clientSalesList_holdAllCheckedData (arrRows);
    			},
    			onUncheckAll: function (rows)
    			{
    				clientSalesList_holdAllUnCheckedData (arrRows)
    			}
	        });
	        $('#clientSalesList_table_salesItemsDG').datagrid('fixDetailRowHeight',index);
	        clientSalesList_populateLineItems (clientSalesList_table_lineItemsDG, index, row);
	        clientSalesList_checkDGRow (clientSalesList_table_lineItemsDG);
	    }
	});
}

function clientSalesList_checkDGRow (clientSalesList_table_lineItemsDG)
{
	var arrItems = clientSalesList_table_lineItemsDG.datagrid('getRows');
	for (nIndex = 0; nIndex < arrItems.length; nIndex++)
	{
		if(clientSalesList_isRowSelectable(arrItems[nIndex].m_nLineItemId))
			clientSalesList_table_lineItemsDG.datagrid('checkRow', nIndex);
	}
}

function clientSalesList_isRowSelectable (nItemId)
{
	assert.isNumber(nItemId, "nItemId expected to be a Number.");
//	assert(nNoOfDays !== 0, "nNoOfDays cannot be equal to zero.");
//	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oClientSalesListMemberData.m_arrSelectedData.length && !bIsSelectable; nIndex++)
	{
		if(m_oClientSalesListMemberData.m_arrSelectedData[nIndex].m_nLineItemId == nItemId)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function clientSalesList_setSelectedItem(oMainRowIndex, nLineItemIndex , nSelect, oItemData) 
{
	assert.isNumber(nLineItemIndex, "nLineItemIndex expected to be a Number.");
	assert.isNumber(nSelect, "nSelect expected to be a Number.");
	assert.isObject(oItemData, "oItemData expected to be an Object.");
	var oLineItemData = $('#clientSalesList_table_salesItemsDG').datagrid ('getRows')[oMainRowIndex];
	if (oItemData != null)
		oLineItemData.m_oSalesLineItems[nLineItemIndex].ckBox = nSelect;
	else
		oLineItemData.m_oNonStockSalesLineItems[nLineItemIndex - oLineItemData.m_oSalesLineItems.length].ckBox = nSelect;
}

function clientSalesList_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	if(bIsForAdd)
	{
		if(!clientSalesList_isRowAdded (oRowData))
			m_oClientSalesListMemberData.m_arrSelectedData.push(oRowData);
	}
	else
		clientSalesList_remove (oRowData);
}

function clientSalesList_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oClientSalesListMemberData.m_arrSelectedData.length; nIndex++)
		bIsRowAdded = (m_oClientSalesListMemberData.m_arrSelectedData [nIndex].m_nLineItemId == oRowData.m_nLineItemId);
	return bIsRowAdded;
}

function  clientSalesList_remove (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	for (var nIndex = 0; nIndex < m_oClientSalesListMemberData.m_arrSelectedData.length; nIndex++)
	{
		if(m_oClientSalesListMemberData.m_arrSelectedData[nIndex].m_nLineItemId == oRowData.m_nLineItemId)
		{
			m_oClientSalesListMemberData.m_arrSelectedData.splice(nIndex, 1);
			break;
		}
	}
}

function clientSalesList_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows expected to be an Array.");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		clientSalesList_holdCheckedData(arrRows[nIndex], true);
}

function clientSalesList_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows expected to be an Array.");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		clientSalesList_holdCheckedData(arrRows[nIndex], false);
}

function clientSalesList_getSalesList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oSalesData = clientSalesList_getFormData ();
	oSalesData.m_oClientData.m_nClientId = m_oTrademustMemberData.m_nSelectedClientId;
	SalesDataProcessor.list(oSalesData, strColumn, strOrder, nPageNumber, nPageSize, clientSalesList_listed);
}

function clientSalesList_getFormData ()
{
	var oSalesData = new SalesData ();
	oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oSalesData.m_oCreatedBy.m_nUserId = $('#clientSalesList_input_soldBy').combobox('getValue');
	oSalesData.m_strInvoiceNo = $("#clientSalesList_input_invoiceNumber").val();
	oSalesData.m_strChallanNumber = $("#clientSalesList_input_challanNumber").val();
	oSalesData.m_strDate =  FormatDate ($('#clientSalesList_input_date').datebox('getValue'));
	return oSalesData;
}

function clientSalesList_listed (oResponse)
{	
	$('#clientSalesList_table_salesItemsDG').datagrid('loadData', oResponse.m_arrSales);
}

function clientSalesList_populateLineItems (salesReport_table_detailViewDG, index, row)
{
	assert.isObject(row, "row expected to be an Object.");
	var arrSalesLine = new Array ();
	var arrSalesLineItemData = row.m_oSalesLineItems;
	for (var nIndex = 0; nIndex < arrSalesLineItemData.length; nIndex++)
	{
		arrSalesLineItemData[nIndex].m_strArticleDescription = arrSalesLineItemData[nIndex].m_oItemData.m_strItemName;
		arrSalesLineItemData[nIndex].m_strArticleNumber = arrSalesLineItemData[nIndex].m_oItemData.m_strArticleNumber;
		arrSalesLineItemData[nIndex].m_nQuantity = Number(arrSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrSalesLineItemData[nIndex].m_nReturnedQty = Number(arrSalesLineItemData[nIndex].m_nReturnedQuantity).toFixed(2);
		arrSalesLine.push(arrSalesLineItemData[nIndex]);
	}
	var arrNonStockSalesLineItemData = row.m_oNonStockSalesLineItems;
	for (var nIndex = 0; nIndex < arrNonStockSalesLineItemData.length; nIndex++)
	{
		arrNonStockSalesLineItemData[nIndex].m_nReturnedQty = Number(arrNonStockSalesLineItemData[nIndex].m_nReturnedQuantity).toFixed(2);
		arrNonStockSalesLineItemData[nIndex].m_nQuantity = Number(arrNonStockSalesLineItemData[nIndex].m_nQuantity).toFixed(2);
		arrSalesLine.push(arrNonStockSalesLineItemData[nIndex]);
	}
	salesReport_table_detailViewDG.datagrid('loadData',arrSalesLine);
}

function clientSalesList_addLineItems ()
{
	var arrLineItems = new Array ();
	for(var nIndex = 0; nIndex < m_oClientSalesListMemberData.m_arrSelectedData.length; nIndex++)
	{
		(m_oClientSalesListMemberData.m_arrSelectedData[nIndex].m_nReturnQuantity > 0) ? m_oClientSalesListMemberData.m_arrSelectedData[nIndex].m_nReturnQuantity : "0";
		arrLineItems.push(m_oClientSalesListMemberData.m_arrSelectedData[nIndex]);
	}
	$('#returnedItems_table_itemDG').datagrid('loadData', arrLineItems);
	returnedItems_loadFooterDG ();
	HideDialog("secondDialog");
}

function clientSalesList_filter ()
{
	clientSalesList_getSalesList ('m_dCreatedOn', 'desc', 1, 10);
}

function clientSalesList_cancel ()
{
	HideDialog("secondDialog");
}