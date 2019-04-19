var stockTransferMemoList_includeDataObjects = 
[
	'widgets/inventorymanagement/location/LocationData.js',
	'widgets/inventorymanagement/stocktransfer/StockTransferMemoData.js'
];


includeDataObjects (stockTransferMemoList_includeDataObjects, "stockTransferMemoList_loaded()");

function stockTransferMemoList_loaded ()
{
	loadPage ("inventorymanagement/stocktransfer/stockTransferMemoList.html", "workarea", "stockTransferMemoList_init ()");
}

function stockTransferMemoListMemberData ()
{
	this.m_strActionItemsFunction = "stockTransferMemoList_addActions ()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dTransferredOn";
    this.m_strSortOrder = "desc";
    this.m_strXMLData = "";
}

var m_oStockTransferMemoListMemberData = new stockTransferMemoListMemberData ();

function stockTransferMemoList_init ()
{
	$("#stockTransferMemoList_input_fromDate").datebox();
	$("#stockTransferMemoList_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#stockTransferMemoList_input_toDate").datebox();
	$("#stockTransferMemoList_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	stockTransferMemoList_createDataGrid ();
}

function stockTransferMemoList_addActions (row,index)
{
	var oActions = 
			'<table align="center">'+
					'<tr>'+
					'<td> <img title="Print" src="images/print.jpg" id="printImageId" onClick="stockTransferMemoList_print ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function stockTransferMemoList_createDataGrid ()
{
	initHorizontalSplitter("#stockTransferMemoList_div_horizontalSplitter", "#stockTransferMemoList_table_stockTransferMemoListDG");
	$('#stockTransferMemoList_table_stockTransferMemoListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strTransferredFrom',title:'Transferred From',sortable:true,width:150,
						formatter:function(value,row,index)
			        	{
						    return 	row.m_oTransferredFrom.m_strName;						
			        	}	
				  	},
				  	{field:'m_strTransferredTo',title:'Transferred To',sortable:true,width:150,
						formatter:function(value,row,index)
			        	{
						    return 	row.m_oTransferredTo.m_strName;						
			        	}	
				  	},
				  	{field:'m_strDate',title:'Date',sortable:true,width:80},
				  	{field:'Actions',title:'Action',width:40,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return stockTransferMemoList_displayImages (row, index);
			        	}
					},
				]]
		}
	);
	$('#stockTransferMemoList_table_stockTransferMemoListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				stockTransferMemoList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dTransferredOn" : strColumn;
				m_oStockTransferMemoListMemberData.m_strSortColumn = strColumn;
				m_oStockTransferMemoListMemberData.m_strSortOrder = strOrder;
				stockTransferMemoList_list (strColumn, strOrder, m_oStockTransferMemoListMemberData.m_nPageNumber, m_oStockTransferMemoListMemberData.m_nPageSize);
			}
		}
	)
	stockTransferMemoList_initDGPagination ();
	stockTransferMemoList_list (m_oStockTransferMemoListMemberData.m_strSortColumn,m_oStockTransferMemoListMemberData.m_strSortOrder,1, 10);
}

function stockTransferMemoList_displayImages (row, index)
{
	var oImage = eval (m_oStockTransferMemoListMemberData.m_strActionItemsFunction);
	return oImage;
}

function stockTransferMemoList_initDGPagination ()
{
	$('#stockTransferMemoList_table_stockTransferMemoListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oStockTransferMemoListMemberData.m_nPageNumber = nPageNumber;
				stockTransferMemoList_list (m_oStockTransferMemoListMemberData.m_strSortColumn, m_oStockTransferMemoListMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oStockTransferMemoListMemberData.m_nPageNumber = nPageNumber;
				m_oStockTransferMemoListMemberData.m_nPageSize = nPageSize;
				stockTransferMemoList_list (m_oStockTransferMemoListMemberData.m_strSortColumn, m_oStockTransferMemoListMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function stockTransferMemoList_selectedRowData (oRowData, nIndex)
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "stockTransferMemoList_detailsProgressbarLoaded ()");
	document.getElementById("stockTransferMemoList_div_listDetail").innerHTML = "";
	var oStockTransferMemoData = new StockTransferMemoData ();
	oStockTransferMemoData.m_nStockTransferMemoId = oRowData.m_nStockTransferMemoId;
	StockTransferMemoDataProcessor.getXML (oStockTransferMemoData, function (strXMLData)
		{
			m_oStockTransferMemoListMemberData.m_strXMLData = strXMLData;
			populateXMLData (strXMLData, "inventorymanagement/stocktransfer/stockTransferMemoListDetails.xslt", 'stockTransferMemoList_div_listDetail');
			stockTransferMemoList_initializeDetailsDG ();
			StockTransferMemoDataProcessor.get (oStockTransferMemoData, stockTransferMemoList_gotData)
		});
}

function stockTransferMemoList_detailsProgressbarLoaded ()
{
	createPopup("dialog", "", "", true);
}

function stockTransferMemoList_gotData (oResponse)
{
	clearGridData ("#stockTransferMemoListDetails_table_memoListDetailsDG");
	var arrStockTransferMemo = oResponse.m_arrStockTransferMemo[0];
	$('#stockTransferMemoListDetails_table_memoListDetailsDG').datagrid('loadData',arrStockTransferMemo.m_oStockTransferSet);
	HideDialog("dialog");
}

function stockTransferMemoList_getFormData () 
{
	var oStockTransferMemoData = new StockTransferMemoData ();
	oStockTransferMemoData.m_oTransferredFrom.m_strName = $("#stockTransferMemoList_input_transferredFrom").val();
	oStockTransferMemoData.m_oTransferredTo.m_strName = $("#stockTransferMemoList_input_transferredTo").val ();
	oStockTransferMemoData.m_strFromDate = FormatDate ($('#stockTransferMemoList_input_fromDate').datebox('getValue'));
	oStockTransferMemoData.m_strToDate = FormatDate ($('#stockTransferMemoList_input_toDate').datebox('getValue'));
	return oStockTransferMemoData;
}

function stockTransferMemoList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oStockTransferMemoListMemberData.m_strSortColumn = strColumn;
	m_oStockTransferMemoListMemberData.m_strSortOrder = strOrder;
	m_oStockTransferMemoListMemberData.m_nPageNumber = nPageNumber;
	m_oStockTransferMemoListMemberData.m_nPageSize = nPageSize;
	document.getElementById("stockTransferMemoList_div_listDetail").innerHTML = "";
	loadPage ("inventorymanagement/progressbar.html", "dialog", "stockTransferMemoList_progressbarLoaded ()");
}

function stockTransferMemoList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStockTransferMemoData = stockTransferMemoList_getFormData ();
	StockTransferMemoDataProcessor.list(oStockTransferMemoData, m_oStockTransferMemoListMemberData.m_strSortColumn, m_oStockTransferMemoListMemberData.m_strSortOrder, m_oStockTransferMemoListMemberData.m_nPageNumber, m_oStockTransferMemoListMemberData.m_nPageSize, stockTransferMemoList_listed);
}

function stockTransferMemoList_listed (oResponse)
{
	clearGridData ("#stockTransferMemoList_table_stockTransferMemoListDG");
	$('#stockTransferMemoList_table_stockTransferMemoListDG').datagrid('loadData', oResponse.m_arrStockTransferMemo);		
	$('#stockTransferMemoList_table_stockTransferMemoListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oStockTransferMemoListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function stockTransferMemoList_cancel ()
{
	HideDialog("dialog");
}

function stockTransferMemoList_initializeDetailsDG ()
{
	$('#stockTransferMemoListDetails_table_memoListDetailsDG').datagrid
	(
		{
			columns:
				[[
				  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:80,
				  		formatter:function(value,row,index)
			        	{
					  		return row.m_oItemData.m_strArticleNumber;
			        	}	
				  	},
					{field:'m_strItemName',title:'Item Name',sortable:true,width:150,
				  		formatter:function(value,row,index)
			        	{
					  		return row.m_oItemData.m_strItemName;
			        	}	
				  	},
				  	{field:'m_strBrand',title:'Brand',sortable:true,width:150,
				  		formatter:function(value,row,index)
			        	{
					  		return row.m_oItemData.m_strBrand;
			        	}	
				  	},
				  	{field:'m_strDetail',title:'Detail',sortable:true,width:200,
				  		formatter:function(value,row,index)
			        	{
					  		return row.m_oItemData.m_strDetail;
			        	}	
				  	},
					{field:'m_nQuantity',title:'Qty',sortable:true,width:80,align:'right',
				  		formatter:function(value,row,index)
			        	{
					  		return value.toFixed(2);
			        	}	
					}
				]]
		}
	);
}

function stockTransferMemoList_print ()
{
	navigate ('Print Stock Transfer Memo','widgets/inventorymanagement/stocktransfer/stockTransferMemoListPrint.js');
}


function stockTransferMemoList_showAddPopup ()
{
	navigate ('Stock Transfer','widgets/inventorymanagement/stocktransfer/newStockTransfer.js');
}

function stockTransferMemoList_filter ()
{
	stockTransferMemoList_list (m_oStockTransferMemoListMemberData.m_strSortColumn,m_oStockTransferMemoListMemberData.m_strSortOrder,1, 10);
}

function stockTransfer_handleAftersave ()
{
	stockTransferMemoList_list (m_oStockTransferMemoListMemberData.m_strSortColumn,m_oStockTransferMemoListMemberData.m_strSortOrder,1, 10);
}