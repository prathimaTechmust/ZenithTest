var stockTransferList_includeDataObjects = 
[
	'widgets/inventorymanagement/stocktransfer/StockTransferData.js',
	'widgets/inventorymanagement/location/LocationData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];


includeDataObjects (stockTransferList_includeDataObjects, "stockTransferList_loaded ()");

function stockTransferList_loaded ()
{
	loadPage ("inventorymanagement/stocktransfer/stockTransferList.html", "workarea", "stockTransferList_init ()");
}

function stockTransferList_memberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize = 20;
    this.m_strSortColumn = "m_nStockTransferID";
    this.m_strSortOrder = "desc";
}

var m_oStockTransferListMemberData = new stockTransferList_memberData ();

function stockTransferList_init ()
{
	stockTransferList_initializeDetailsDG ();
	stockTransferList_list (m_oStockTransferListMemberData.m_strSortColumn, m_oStockTransferListMemberData.m_strSortOrder, 1, 20);
}

function stockTransferList_initializeDetailsDG ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#stockTransferList_table_stockTransferListDG");
	$('#stockTransferList_table_stockTransferListDG').datagrid
	({
		fit:true,
		columns:
		[[
		  	{field:'m_strDate',title:'Date',sortable:true,width:100},
		  	{field:'m_strArticleNumber',title:'Article #',sortable:true,width:100},
			{field:'m_strItemName',title:'Item Name',sortable:true,width:150,
		  		styler: function(value,row,index)
		  		{
		  			return {class:'DGcolumn'};
		  		}	
			},
			{field:'m_strTransferredFrom',title:'Transferred From',sortable:true,width:100},
			{field:'m_strTransferredTo',title:'Transferred To',sortable:true,width:100},
			{field:'m_nQuantity',title:'Quantity',sortable:true,align:'right',width:80},
			{field:'m_strUserName',title:'Transferred By',sortable:true,width:100}
		]],
		
		onSortColumn: function (strColumn, strOrder)
		{
			strColumn = (strColumn == "m_strDate") ? "m_dDate" : strColumn;
			m_oStockTransferListMemberData.m_strSortColumn = strColumn;
			m_oStockTransferListMemberData.m_strSortOrder = strOrder;
			stockTransferList_list (strColumn, strOrder, m_oStockTransferListMemberData.m_nPageNumber, m_oStockTransferListMemberData.m_nPageSize);
		}
	});
	stockTransferList_initDGPagination ()
}

function stockTransferList_initDGPagination()
{
	$('#stockTransferList_table_stockTransferListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oStockTransferListMemberData.m_nPageNumber = nPageNumber;
				stockTransferList_list (m_oStockTransferListMemberData.m_strSortColumn, m_oStockTransferListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function(nPageNumber, nPageSize)
			{
				m_oStockTransferListMemberData.m_nPageNumber = nPageNumber;
				m_oStockTransferListMemberData.m_nPageSize = nPageSize;
				stockTransferList_list (m_oStockTransferListMemberData.m_strSortColumn, m_oStockTransferListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			}
		}
	)	
}

function stockTransferList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oStockTransferListMemberData.m_strSortColumn = strColumn;
	m_oStockTransferListMemberData.m_strSortOrder = strOrder;
	m_oStockTransferListMemberData.m_nPageNumber = nPageNumber;
	m_oStockTransferListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "stockTransferList_progressbarLoaded ()");
}

function stockTransferList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStockTransferData = new StockTransferData ();
	oStockTransferData.strOrderColumnName = m_oStockTransferListMemberData.m_strSortColumn;
	StockTransferDataProcessor.list(oStockTransferData, m_oStockTransferListMemberData.m_strSortColumn, m_oStockTransferListMemberData.m_strSortOrder , m_oStockTransferListMemberData.m_nPageNumber, m_oStockTransferListMemberData.m_nPageSize, stockTransferList_listed);
}

function stockTransferList_listed (oResponse)
{
	clearGridData ("#stockTransferList_table_stockTransferListDG");
	$('#stockTransferList_table_stockTransferListDG').datagrid('loadData',oResponse.m_arrStockTransfer);
	$('#stockTransferList_table_stockTransferListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oStockTransferListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}