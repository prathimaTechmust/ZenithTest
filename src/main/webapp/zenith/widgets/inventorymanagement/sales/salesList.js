var salesList_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/SiteData.js'
];

includeDataObjects (salesList_includeDataObjects, "salesList_loaded()");



function salesList_memberData ()
{
	this.m_nSelectedSalesId = -1;
	this.m_nPurchaseOrderId = -1;
	this.m_nIndex = -1;
	this.m_strActionSalesFunction = "salesList_addPrintAction()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
}

var m_oSalesListMemberData = new salesList_memberData ();

function salesList_init ()
{
	$("#filterSales_input_fromDate").datebox();
	$("#filterSales_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filtersales_input_toDate").datebox();
	$("#filtersales_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	salesList_initializeDataGrid ();
}

function salesList_addPrintAction ()
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Print" src="images/print.jpg" id="printImageId" onClick="salesList_print ()"/> </td>'+
					'</tr>'+
				  '</table>'
	return oImage;
}

function salesList_initEdit ()
{
	m_oSalesListMemberData.m_strActionSalesFunction = "salesList_addActions (row, index)";
	document.getElementById ("salesList_button_add").style.visibility="visible";
	salesList_init ();
}

function salesList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var oActions = 
				'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" align="center" id="editImageId" onClick="salesList_edit ('+row.m_nId+', '+(row.m_oPOData == null ? -1 : row.m_oPOData.m_nPurchaseOrderId)+')"/> </td>'+
						'<td> <img title="Print" src="images/print.jpg" id="printImageId" onClick="salesList_print ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function salesList_initializeDataGrid ()
{
	initHorizontalSplitter("#salesList_div_horizontalSplitter", "#salesList_table_salesListDG");
	$('#salesList_table_salesListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strTo',title:'To',sortable:true,width:300,
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}	
			  	},
				{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:150},
				{field:'m_strDate',title:'Date',sortable:true,width:70},
				{field:'Actions',title:'Action',width:50,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return salesList_displayImages (row, index);
		        	}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				salesList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oSalesListMemberData.m_strSortColumn = strColumn;
				m_oSalesListMemberData.m_strSortOrder = strOrder;
				salesList_list (strColumn, strOrder, m_oSalesListMemberData.m_nPageNumber, m_oSalesListMemberData.m_nPageSize);
			}
		}
	);
	salesList_initDGPagination ();
	salesList_list (m_oSalesListMemberData.m_strSortColumn, m_oSalesListMemberData.m_strSortOrder, 1, 10);
}

function salesList_getFormData ()
{
	var oSalesData = new SalesData ();
	oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oSalesData.m_strTo = $("#filterSales_input_to").val();
	oSalesData.m_strInvoiceNo = $("#filterSales_input_invoiceNumber").val();
	oSalesData.m_strFromDate = FormatDate ($('#filterSales_input_fromDate').datebox('getValue'));
	oSalesData.m_strToDate = FormatDate ($("#filtersales_input_toDate").datebox('getValue'));
	return oSalesData;
}

function salesList_filter ()
{
	salesList_list (m_oSalesListMemberData.m_strSortColumn, m_oSalesListMemberData.m_strSortOrder, 1, 10);
}

function salesList_displayImages (row, index)
{
	var oImage = eval (m_oSalesListMemberData.m_strActionSalesFunction);
	return oImage;
}

function salesList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oSalesListMemberData.m_strSortColumn = strColumn;
	m_oSalesListMemberData.m_strSortOrder = strOrder;
	m_oSalesListMemberData.m_nPageNumber = nPageNumber;
	m_oSalesListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "salesList_progressbarLoaded ()");
}

function salesList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oSalesData = salesList_getFormData ();
	SalesDataProcessor.list(oSalesData, m_oSalesListMemberData.m_strSortColumn, m_oSalesListMemberData.m_strSortOrder, m_oSalesListMemberData.m_nPageNumber, m_oSalesListMemberData.m_nPageSize, salesList_listed);
}

function salesList_listed (oResponse)
{	
	document.getElementById("salesList_div_listDetail").innerHTML = "";
	clearGridData ("#salesList_table_salesListDG");
	$('#salesList_table_salesListDG').datagrid('loadData', oResponse.m_arrSales);
	$('#salesList_table_salesListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oSalesListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function salesList_initDGPagination ()
{
	$('#salesList_table_salesListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oSalesListMemberData.m_nPageNumber = nPageNumber;
				salesList_list (m_oSalesListMemberData.m_strSortColumn, m_oSalesListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("salesList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oSalesListMemberData.m_nPageNumber = nPageNumber;
				m_oSalesListMemberData.m_nPageSize = nPageSize;
				salesList_list (m_oSalesListMemberData.m_strSortColumn, m_oSalesListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("salesList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function salesList_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oSalesListMemberData.m_nIndex = nIndex;
	document.getElementById("salesList_div_listDetail").innerHTML = "";
	var oSalesData = new SalesData ();
	oSalesData.m_nId = oRowData.m_nId;
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	SalesDataProcessor.getXML (oSalesData,function (strXMLData)
		{
			m_oSalesListMemberData.m_strXMLData = strXMLData;
			populateXMLData (strXMLData, "inventorymanagement/sales/SalesDetails.xslt", 'salesList_div_listDetail');
			salesList_initializeDetailsDG ();
			SalesDataProcessor.get (oSalesData, salesList_gotSalesLineItemData)
	});
}

function salesList_initializeDetailsDG ()
{
	$('#salesDetails_table_salesDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:50},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:100},
				{field:'m_nQuantity',title:'Quantity',sortable:true,align:'right',width:50},
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
				{field:'m_nDiscount',title:'Disc(%)',sortable:true,align:'right',width:50},
				{field:'m_nAmount',title:'Amount',width:60,align:'right',
					formatter:function(value,row,index)
		        	{
						var nIndianFormat = formatNumber (value.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	},
		        	styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
				}
			]]
		}
	);
}

function salesList_gotSalesLineItemData (oResponse)
{
	clearGridData ("#salesDetails_table_salesDetailsDG");
	var arrSalesData = oResponse.m_arrSales;
	var nTotal =0;
	nTotal += salesList_buildSalesLineItems (arrSalesData);
	$('#salesDetails_table_salesDetailsDG').datagrid('reloadFooter',[{m_nDiscount:'<b>Total</b>', m_nAmount: nTotal}]);
}

function salesList_buildSalesLineItems (arrSalesData)
{
	assert.isArray(arrSalesData, "arrSalesData expected to be an Array.");
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
	}
	$('#salesDetails_table_salesDetailsDG').datagrid('loadData',arrSalesLineItemData);
	return nTotal;
}

function salesList_edit (nSalesId, nPOId)
{
	assert.isNumber(nPOId, "nPOId expected to be a Number.");
	assert.isNumber(nSalesId, "nSalesId expected to be a Number.");
	assert(nSalesId !== 0, "nSalesId cannot be equal to zero.");
	if (nPOId > 0)
	{
		m_oSalesListMemberData.m_nPurchaseOrderId = nPOId;
		loadPage ("include/process.html", "ProcessDialog", "salesList_edit_progressbarLoaded ()");
	}
	else
	{
		m_oSalesListMemberData.m_nSelectedSalesId = nSalesId;
		loadPage ("include/process.html", "ProcessDialog", "salesList_edit_progressbarLoaded_edit ()");
	}
}
function salesList_edit_progressbarLoaded ()
{
	navigate ("purchaseOrder", "widgets/purchaseordermanagement/purchaseorder/purchaseOrderForSales.js");
}

function salesList_edit_progressbarLoaded_edit ()
{
	navigate ("editSales", "widgets/inventorymanagement/sales/editSales.js");
}

function salesList_showAddPopup ()
{
	navigate ("newSales", "widgets/inventorymanagement/sales/salesAdmin.js");
}

function salesList_listDetail_delete ()
{
	salesList_delete (m_oSalesListMemberData.m_nIndex);
}

function salesList_delete (nIndex)
{ 
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oSalesData = new SalesData ();
	var oListData = $("#salesList_table_salesListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oSalesData.m_nId = oData.m_nId;
	var bConfirm = getUserConfirmation("usermessage_salesList_doyoureallywanttodeletethesaleitem")
	if(bConfirm)
		SalesDataProcessor.deleteData(oSalesData, salesList_deleted);
}

function salesList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("usermessage_salesList_saleitemdeletedsuccessfully", "kSuccess");
		document.getElementById("salesList_div_listDetail").innerHTML = "";
		salesList_list (m_oSalesListMemberData.m_strSortColumn, m_oSalesListMemberData.m_strSortOrder, 1, 10);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function salesList_print()
{
	navigate ('salesPrint','widgets/inventorymanagement/sales/salesListPrint.js');
}

function sales_handleAftersave ()
{
	document.getElementById("salesList_div_listDetail").innerHTML = "";
	clearGridData ("#salesList_table_salesListDG");
	salesList_list (m_oSalesListMemberData.m_strSortColumn, m_oSalesListMemberData.m_strSortOrder, 1, 10);
}

function purchaseOrder_handleAfterSave () //Handler Function
{
	document.getElementById("salesList_div_listDetail").innerHTML = "";
	clearGridData ("#salesList_table_salesListDG");
	salesList_list (m_oSalesListMemberData.m_strSortColumn, m_oSalesListMemberData.m_strSortOrder, 1, 10);
}
