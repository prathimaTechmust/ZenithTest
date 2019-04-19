var returnedItemsList_includeDataObjects = 
[
	'widgets/inventorymanagement/returneditems/ReturnedData.js',
	'widgets/inventorymanagement/returneditems/ReturnedLineItemData.js',
	'widgets/inventorymanagement/returneditems/NonStockReturnedLineItemData.js',
	'widgets/purchaseordermanagement/supply/NonStockSupplyItemsData.js',
	'widgets/purchaseordermanagement/supply/SupplyData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/inventorymanagement/returneditems/ReturnedLineItem.js'
];


includeDataObjects (returnedItemsList_includeDataObjects, "returnedItemsList_loaded()");

function returnedItemsList_memberData ()
{
	this.m_nIndex = -1;
	this.m_strActionFunction = "returnedItemsList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_nSelectedId = -1;
}

var m_oReturnedItemsListMemberData = new returnedItemsList_memberData ();

function returnedItemsList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function returnedItemsList_initAdmin ()
{
	m_oReturnedItemsListMemberData.m_strActionFunction = "returnedItemsList_addActions (row, index)";
	document.getElementById ("returnedItemsList_button_add").style.visibility="visible";
	returnedItemsList_init ();
}

function returnedItemsList_initUser ()
{
	returnedItemsList_init ();
}

function returnedItemsList_init ()
{
	$("#returnedItemsList_input_fromDate").datebox();
	$("#returnedItemsList_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#returnedItemsList_input_toDate").datebox();
	$("#returnedItemsList_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	returnedItemsList_initializeDataGrid ();
}

function returnedItemsList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");	
	var oActions = 
				'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" align="center" onClick="returnedItemsList_edit ('+row.m_nId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png"  onClick="returnedItemsList_delete ()"/> </td>'+
						'<td> <img title="Print" src="images/print.jpg"  onClick="returnedItemsList_print ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function returnedItemsList_initializeDataGrid ()
{
	initHorizontalSplitter("#returnedItemsList_div_horizontalSplitter", "#returnedItemsList_table_salesListDG");
	$('#returnedItemsList_table_salesListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strCompanyName',title:'From',sortable:true,width:150,
			  		formatter:function(value,row,index)
					{
			        	return row.m_oClientData.m_strCompanyName;
					}
			  	},
			  	{field:'m_strCreditNoteNumber',title:'Credit Note Number',sortable:true,width:70},
				{field:'m_strDate',title:'Date',sortable:true,width:70},
				{field:'Actions',title:'Action',width:40,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return returnedItemsList_displayImages (row, index);
		        	}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				returnedItemsList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oReturnedItemsListMemberData.m_strSortColumn = strColumn;
				m_oReturnedItemsListMemberData.m_strSortOrder = strOrder;
				returnedItemsList_list (strColumn, strOrder, m_oReturnedItemsListMemberData.m_nPageNumber, m_oReturnedItemsListMemberData.m_nPageSize);
			}
		}
	);
	returnedItemsList_initDGPagination ();
	returnedItemsList_list (m_oReturnedItemsListMemberData.m_strSortColumn, m_oReturnedItemsListMemberData.m_strSortOrder, 1, 10);
}

function returnedItemsList_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oReturnedItemsListMemberData.m_nIndex = nIndex;
	document.getElementById("returnedItemsList_div_listDetail").innerHTML = "";
	var oReturnedData = new ReturnedData ();
	oReturnedData.m_nId = oRowData.m_nId;
	ReturnedDataProcessor.getXML (oReturnedData,function (strXMLData)
		{
			m_oReturnedItemsListMemberData.m_strXMLData = strXMLData;
			populateXMLData (strXMLData, "inventorymanagement/returneditems/returnedItemDetails.xslt", 'returnedItemsList_div_listDetail');
			returnedItemsList_initializeDetailsDG ();
			ReturnedDataProcessor.get (oReturnedData, returnedItemsList_gotReturnedLineItemData)
	});
}

function returnedItemsList_gotReturnedLineItemData (oResponse)
{
	clearGridData ("#returnedItemDetails_table_returnedItemDetailsDG");
	var arrReturns = returnedItemsList_buildReturnedItemsArray (oResponse.m_arrReturnedData[0]);
	$('#returnedItemDetails_table_returnedItemDetailsDG').datagrid('loadData', arrReturns);
}

function returnedItemsList_buildReturnedItemsArray (oReturnedData)
{
	assert.isObject(oReturnedData, "oReturnedData expected to be an Object.");
	assert( Object.keys(oReturnedData).length >0 , "oReturnedData cannot be an empty .");// checks for non emptyness 
	var arrReturns = new Array ();
	for(var nIndex=0; nIndex < oReturnedData.m_oReturnedLineItems.length; nIndex++)
	{
		var oReturnedLineItem = new ReturnedLineItem ();
		oReturnedLineItem.m_strArticleNumber = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_oItemData.m_strArticleNumber;
		oReturnedLineItem.m_strItemName = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_oItemData.m_strItemName;
		oReturnedLineItem.m_nShippedQuantity = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_nQuantity;
		oReturnedLineItem.m_nQuantity = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_nReturnedQuantity;
		arrReturns.push(oReturnedLineItem);
	}
	for(var nIndex=0; nIndex < oReturnedData.m_oNonStockReturnedLineItems.length; nIndex++)
	{
		var oReturnedLineItem = new ReturnedLineItem ();
		oReturnedLineItem.m_strArticleNumber = "";
		oReturnedLineItem.m_strItemName = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_strArticleDescription;
		oReturnedLineItem.m_nShippedQuantity = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_nQuantity;
		oReturnedLineItem.m_nQuantity = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_nReturnedQuantity;
		arrReturns.push(oReturnedLineItem);
	}
	return arrReturns;
}

function returnedItemsList_initializeDetailsDG ()
{
	$('#returnedItemDetails_table_returnedItemDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:50},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:100},
				{field:'m_nShippedQuantity',title:'Shipped Qty',sortable:true,align:'right',width:50},
				{field:'m_nQuantity',title:'Returned Qty',width:80,align:'right'}
			]]
		}
	);
}

function returnedItemsList_displayImages (row, index)
{
	var oImage = eval (m_oReturnedItemsListMemberData.m_strActionFunction);
	return oImage;
}

function returnedItemsList_initDGPagination ()
{
	$('#returnedItemsList_table_salesListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oReturnedItemsListMemberData.m_nPageNumber = nPageNumber;
				returnedItemsList_list (m_oReturnedItemsListMemberData.m_strSortColumn, m_oReturnedItemsListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("returnedItemsList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oReturnedItemsListMemberData.m_nPageNumber = nPageNumber;
				m_oReturnedItemsListMemberData.m_nPageSize = nPageSize;
				returnedItemsList_list (m_oReturnedItemsListMemberData.m_strSortColumn, m_oReturnedItemsListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("returnedItemsList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function returnedItemsList_filter ()
{
	returnedItemsList_list (m_oReturnedItemsListMemberData.m_strSortColumn, m_oReturnedItemsListMemberData.m_strSortOrder, 1, 10);
}

function returnedItemsList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oReturnedItemsListMemberData.m_strSortColumn = strColumn;
	m_oReturnedItemsListMemberData.m_strSortOrder = strOrder;
	m_oReturnedItemsListMemberData.m_nPageNumber = nPageNumber;
	m_oReturnedItemsListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "returnedItemsList_progressbarLoaded ()");
}

function returnedItemsList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oReturnedData = new returnedItemsList_getFormData ();
	ReturnedDataProcessor.list(oReturnedData, m_oReturnedItemsListMemberData.m_strSortColumn, m_oReturnedItemsListMemberData.m_strSortOrder, m_oReturnedItemsListMemberData.m_nPageNumber, m_oReturnedItemsListMemberData.m_nPageSize, returnedItemsList_listed);
}

function returnedItemsList_getFormData ()
{
	var oReturnedData = new ReturnedData ();
	oReturnedData.m_oClientData.m_strCompanyName = $("#filterReturns_input_from").val();
	oReturnedData.m_strFromDate = FormatDate ($('#returnedItemsList_input_fromDate').datebox('getValue'));
	oReturnedData.m_strToDate = FormatDate ($('#returnedItemsList_input_toDate').datebox('getValue'));
	return oReturnedData;
}

function returnedItemsList_listed (oResponse)
{	
	clearGridData ("#returnedItemsList_table_salesListDG");
	$('#returnedItemsList_table_salesListDG').datagrid('loadData', oResponse.m_arrReturnedData);
	$('#returnedItemsList_table_salesListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oReturnedItemsListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function returnedItemsList_showAddPopup ()
{
	navigate ("", "widgets/inventorymanagement/returneditems/returnedItemsAdmin.js");
}

function returnedItemsList_edit (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	m_oReturnedItemsListMemberData.m_nSelectedId = nId;
	loadPage ("include/process.html", "ProcessDialog", "returnedItemsList_edit_progressbarLoaded ()");
}

function returnedItemsList_edit_progressbarLoaded ()
{
	navigate ("returned Items", "widgets/inventorymanagement/returneditems/editReturnedItems.js");
}
function returnedItemsList_print ()
{
	navigate ('printReturned','widgets/inventorymanagement/returneditems/returnedItemsListPrint.js');
}