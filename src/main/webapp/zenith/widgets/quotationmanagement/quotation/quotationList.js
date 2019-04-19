var quotationList_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/quotationmanagement/quotation/QuotationData.js',
	'widgets/quotationmanagement/quotation/QuotationLineItemData.js',
	'widgets/quotationmanagement/logs/QuotationLogData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderStockLineItemData.js',
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js'
];

 includeDataObjects (quotationList_includeDataObjects, "quotationList_loaded ()");

function quotationList_memberData ()
{
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "quotationList_addHyphen()";
	this.m_strDGId = "";
	this.m_strXMLData = "";
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "";
    this.m_nLogPageNumber = 1;
    this.m_nLogPageSize = 10;
    this.m_bIsArchived = false;
    this.m_nId = -1;
    this.m_oPurchaseOrderData = new PurchaseOrderData ();
    this.m_oInvoiceData = new InvoiceData ();
	this.m_nInvoiceId = -1;
	this.m_nChallanId = -1;
}
var m_oQuotationListMemberData = new quotationList_memberData ();

function quotationList_init ()
{
	$("#quotationList_input_fromDate").datebox();
	$('#quotationList_input_fromDate').datebox('textbox')[0].placeholder = "From Date";
	$("#quotationList_input_toDate").datebox();
	$('#quotationList_input_toDate').datebox('textbox')[0].placeholder = "To Date";
	initHorizontalSplitterWithTabs("#quotationList_div_horizontalSplitter", "#quotationList_div_quotationTabs");
	$('#quotationList_div_quotationTabs').tabs (
			{
				fit :true,
				onSelect: function (oTitle)
				{
					if (oTitle.toLowerCase().search ('pending') >= 0 )
					{
						document.getElementById("quotationList_div_listDetail").innerHTML = "";
						m_oQuotationListMemberData.m_bIsArchived = false;
						quotationList_initializeDataGrid ("#quotationList_table_quotationPending");
					}
					if (oTitle.toLowerCase().search ('archived') >= 0 )
					{
						document.getElementById("quotationList_div_listDetail").innerHTML = "";
						m_oQuotationListMemberData.m_bIsArchived = true;
						quotationList_initializeDataGrid ("#quotationList_table_quotationArchived");
					}
				}
			});
	$('#quotationList_div_quotationTabs').tabs ('resize');
}

function quotationList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function quotationList_initUser ()
{
	quotationList_init ();
}

function quotationList_initAdmin ()
{
	m_oQuotationListMemberData.m_strActionItemsFunction = "quotationList_addActions (row, index)";
	document.getElementById ("quotationList_button_add").style.visibility="visible";
	quotationList_init ();
}

function quotationList_addActions (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var oImage = 	'<table align="center">'+
						'<tr>'+
						'<td style="border:none"> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" onClick="quotationList_edit ('+row.m_nQuotationId+')"/> </td>'+
						'<td style="border:none"> <img title="Add New Comment" src="images/WriteMessage.png" width="20" align="center" onClick="quotationList_createLog ('+row.m_nQuotationId+')"/> </td>'+
						'<td style="border:none"> <img title="Print" src="images/print.jpg" width="20" align="center" onClick="quotationList_print ()"/> </td>'+
						'<td style="border:none"> <img title="Archive" src="images/Archive.png" width="20" align="center" onClick="quotationList_makeArchive ('+row.m_nQuotationId+')"/> </td>'+
						'<td style="border:none"> <img title="Order" src="images/order.png" width="20" align="center" onClick="quotationList_processOrder ('+row.m_nQuotationId+')"/> </td>'+
						'<td style="border:none"> <img title="Sale" src="images/sale.png" width="20" align="center" onClick="quotationList_processSales ('+row.m_nQuotationId+')"/> </td>'+
						'<td style="border:none"> <img title="mail Quotation" id="quotationList_img_mail'+row.m_nQuotationId+'" style="visibility: hidden;" src="images/Mail.png" width="20" align="center" onClick="quotationList_sendmail ('+row.m_nQuotationId+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function quotationList_sendmail (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	m_oQuotationListMemberData.m_nId = nId;
	processConfirmation ('Yes', 'No', 'Are you sure want to mail this quotation ?', quotationList_processQuotationMail);
}

function quotationList_processQuotationMail (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if(bConfirm)
	{
		var oQuotationData = new QuotationData ();
		oQuotationData.m_nQuotationId  = m_oQuotationListMemberData.m_nId;
		QuotationDataProcessor.sendMail(oQuotationData, quotationList_sentMail);
	}
}

function quotationList_sentMail (oResponse)
{
	if(oResponse.m_bSuccess)
		informUser ("Quotation Details Mail Sent Successfully.", "kSuccess");
}

function quotationList_makeUnArchive (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	var oQuotationData = new QuotationData ();
	oQuotationData.m_nQuotationId  = nId;
	QuotationDataProcessor.unArchive(oQuotationData, quotationList_unArchived);
}

function quotationList_unArchived (oResponse)
{
	if(oResponse.m_bSuccess)
		quotationList_list (m_oQuotationListMemberData.m_strSortColumn, m_oQuotationListMemberData.m_strSortOrder, 1, 10);
}

function quotationList_makeArchive (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
		var oQuotationData = new QuotationData ();
		oQuotationData.m_nQuotationId  = nId;
		QuotationDataProcessor.archive(oQuotationData, quotationList_archived);
}

function quotationList_archived (oResponse)
{
	if(oResponse.m_bSuccess)
		quotationList_list (m_oQuotationListMemberData.m_strSortColumn, m_oQuotationListMemberData.m_strSortOrder, 1, 10);
}

function quotationList_initializeDataGrid (strDataGridId)
{
	assert.isString(strDataGridId, "strDataGridId expected to be a string.");
	m_oQuotationListMemberData.m_strDGId = strDataGridId;
	$(strDataGridId).datagrid
	(
		{
			fit :true,
			columns:
			[[
			  	{field:'m_strQuotationNumber',title:'QuotationNumber',sortable:true,width:80},
			  	{field:'m_strCompanyName',title:'To',sortable:true,width:150,
			  		styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}	
			  	},
			  	{field:'m_strSiteName',title:'Site',sortable:true,width:130},
				{field:'m_strDate',title:'Date',sortable:true,width:70},
				{field:'Actions',title:'Action',width:120,align:'center',
					formatter:function(value,row,index)
		        	{
						var oImage = quotationList_addHyphen ()
						if(strDataGridId == "#quotationList_table_quotationPending")
							oImage = quotationList_displayImages (row, index);
						else if (strDataGridId == "#quotationList_table_quotationArchived")
							oImage = quotationList_displayArchiveImages (row, index);
						return oImage;
		        	}
				}
			]]
		}
	);
	$(strDataGridId).datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				quotationList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oQuotationListMemberData.m_strSortColumn = strColumn;
				m_oQuotationListMemberData.m_strSortOrder = strOrder;
				quotationList_list (strColumn, strOrder, m_oQuotationListMemberData.m_nPageNumber, m_oQuotationListMemberData.m_nPageSize);
			}
		}
	)
	quotationList_initDGPagination (strDataGridId);
	quotationList_list (m_oQuotationListMemberData.m_strSortColumn, m_oQuotationListMemberData.m_strSortOrder, 1, 10);
}

function quotationList_displayArchiveImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	var oImage = '<table align="center">'+
					'<tr>'+
					'<td style="border:none"> <img title="Print" src="images/print.jpg" width="20" align="center" onClick="quotationList_print ()"/> </td>'+
					'<td style="border:none"> <img title="UnArchive" src="images/UnArchive.png" width="20" align="center" onClick="quotationList_makeUnArchive ('+row.m_nQuotationId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}

function quotationList_processOrder ()
{
	navigate ("purchaseOrder", "widgets/purchaseordermanagement/purchaseorder/purchaseOrderForQuotation.js");
}

function quotationList_processSales ()
{
	navigate ("purchaseOrderSales", "widgets/inventorymanagement/sales/salesForQuotation.js");
}

function quotationList_filter ()
{
	quotationList_list (m_oQuotationListMemberData.m_strSortColumn, m_oQuotationListMemberData.m_strSortOrder, 1, 10);
}

function quotationList_initDGPagination (strDataGridId)
{
	assert.isString(strDataGridId, "strDataGridId expected to be a string.");
	$(strDataGridId).datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oQuotationListMemberData.m_nPageNumber = $(strDataGridId).datagrid('getPager').pagination('options').pageNumber;
				quotationList_list (m_oQuotationListMemberData.m_strSortColumn, m_oQuotationListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("quotationList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oQuotationListMemberData.m_nPageNumber = $(strDataGridId).datagrid('getPager').pagination('options').pageNumber;
				m_oQuotationListMemberData.m_nPageSize = $(strDataGridId).datagrid('getPager').pagination('options').pageSize;
				quotationList_list (m_oQuotationListMemberData.m_strSortColumn, m_oQuotationListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("quotationList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function quotationList_displayImages (row, index)
{
	var oImage = eval (m_oQuotationListMemberData.m_strActionItemsFunction);
	return oImage;
}

function quotationList_edit (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	assert(nId !== 0, "nId cannot be equal to zero.");
	m_oQuotationListMemberData.m_nSelectedId = nId;
	loadPage ("include/process.html", "ProcessDialog", "quotationList_edit_progressbarLoaded ()");
}

function quotationList_edit_progressbarLoaded ()
{
	navigate ("Quotation Order", "widgets/quotationmanagement/quotation/quotationEdit.js");
}

function quotationList_selectedRowData (rowData, rowIndex)
{
	assert.isObject(rowData, "rowData expected to be an Object.");
	assert( Object.keys(rowData).length >0 , "rowData cannot be an empty .");// checks for non emptyness
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	m_oQuotationListMemberData.m_nLogPageNumber = 1;
	m_oQuotationListMemberData.m_nIndex = rowIndex;
	document.getElementById("quotationList_div_listDetail").innerHTML = "";
	var oQuotationData = new QuotationData ();
	m_oQuotationListMemberData.m_oQuotationData = rowData;
	oQuotationData.m_nQuotationId = rowData.m_nQuotationId;
	oQuotationData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oQuotationData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	QuotationDataProcessor.getXML (oQuotationData, function (strXMLData)
		{
			m_oQuotationListMemberData.m_strXMLData = strXMLData;
			populateXMLData (strXMLData, "quotationmanagement/quotation/QuotationDetails.xslt", 'quotationList_div_listDetail');
			quotationList_initializeDetailsDG ();
			quotationList_initializePODataGrid ();
			quotationList_listQuotationLogs (oQuotationData.m_nQuotationId, m_oQuotationListMemberData.m_nLogPageNumber, m_oQuotationListMemberData.m_nLogPageSize);
			QuotationDataProcessor.get (oQuotationData, quotationList_gotLineItemData);
		});
}

function quotationList_initializePODataGrid ()
{
	$("#QuotationDetails_table_itemsDG").datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strCompanyName',title:'From',sortable:true,width:150,
			  		styler: function(value,row,index)
			  		{
			  		 	return {class:'DGcolumn'};
			  		}	
			  	},
			  	{field:'m_strSiteName',title:'Site',sortable:true,width:150},
			  	{field:'m_strPurchaseOrderNumber',title:'Purchase Order No.',sortable:true,width:150},
				{field:'Action',title:'Action',sortable:false,width:70,
		        	formatter:function(value,row,index)
		        	{
		        		return quotationList_addPOActions (row, index);
		        	}
		         },
			]],
			onSelect: function (rowIndex, rowData)
			{
				m_oQuotationListMemberData.m_oPurchaseOrderData = rowData;
			}
		}
	);
}

function quotationList_addPOActions (row, index)
{
	var oActions = 
				'<table align="center">'+
					'<tr>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="quotationList_loadPODetailsPage ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function quotationList_loadPODetailsPage ()
{
	navigate("purchaseOrderList","widgets/purchaseordermanagement/purchaseorder/purchaseOrderList.js");
}

function purchaseOrderList_loaded ()
{
	loadPage ("inventorymanagement/detailsPopup.html", "dialog", "quotation_initPopUp()");
}

function quotation_initPopUp ()
{
	createPopup("dialog", "", "", true);
	purchaseOrderList_showPoData (m_oQuotationListMemberData.m_oPurchaseOrderData, "detaisPopup_div_details");
}

function detaisPopup_cancel ()
{
	HideDialog("dialog")
}

function quotationList_listQuotationLogs (nQuotationId, nPageNumber, nPageSize)
{
	assert.isNumber(nQuotationId, "nQuotationId expected to be a Number.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	if(!m_oQuotationListMemberData.m_bIsArchived)
	{
		quotationDetails_img_edit.style.visibility="visible";
		quotationDetails_img_createLog.style.visibility="visible";
	}
	m_oQuotationListMemberData.m_nId = nQuotationId;
	var oQuotationLogData = new QuotationLogData ();
	oQuotationLogData.m_oQuotationData.m_nQuotationId = nQuotationId;
	QuotationLogDataProcessor.list (oQuotationLogData, "m_nId", "desc", nPageNumber, nPageSize, quotationList_listedQuotationLogs);
}

function quotationList_listedQuotationLogs (oResponse)
{
	if(oResponse.m_nRowCount == 0)
	{
		document.getElementById("quotationDetails_div_logDetails").innerHTML = "<table align=\"center\"><tr><td><br/><br/>No results to display</td></tr></table>";
	}
	else
		quotationList_showLogDetails(oResponse);
}

function quotationList_showLogDetails (oResponse)
{
	if(oResponse.m_nRowCount > 10)
		quotationDetails_div_loadMoreDetails.style.visibility = "visible";
	if(oResponse.m_arrQuotationLogs.length < 10)
		quotationDetails_div_loadMoreDetails.style.visibility = "hidden";
	document.getElementById("quotationDetails_div_logDetails").innerHTML = "";
	quotationList_loadLogDetails (oResponse.m_arrQuotationLogs);
	m_oQuotationListMemberData.m_nRowCount = oResponse.m_nRowCount;
}

function quotationList_loadLogDetails (arrQuotationLogs)
{
	assert.isArray(arrQuotationLogs, "arrQuotationLogs expected to be an Array.");
	var strUIList = "";
	for (var nIndex = 0; nIndex < arrQuotationLogs.length; nIndex++)
	{
		strUIList += "<table class=\"trademust\">";
		strUIList += "<tr>";
		strUIList += "<td class=\"trademust\">";
		strUIList += "<b>" + arrQuotationLogs[nIndex].m_strDate + " " + arrQuotationLogs[nIndex].m_strTime + " - " + arrQuotationLogs[nIndex].m_oQuotationData.m_oCreatedBy.m_strUserName + "</b>";
		strUIList += "</td>";
		strUIList += "</tr>";
		strUIList += "<tr>";
		strUIList += "<td class=\"trademust\">";
		strUIList += arrQuotationLogs[nIndex].m_strComment;
		strUIList += "<hr/>";
		strUIList += "</td>";
		strUIList += "</tr>";
		strUIList += "</table>";
	}
	$("#quotationDetails_div_logDetails").append(strUIList);
}

function quotationDetails_loadMoreContent ()
{
	quotationDetails_div_loadMoreDetails.style.visibility = "hidden";
	m_oQuotationListMemberData.m_nLogPageNumber++;
	var nTotalPages = ((m_oQuotationListMemberData.m_nRowCount)%(m_oQuotationListMemberData.m_nLogPageSize)) <= 0 ? ((m_oQuotationListMemberData.m_nRowCount)/(m_oQuotationListMemberData.m_nLogPageSize)) : Math.ceil((m_oQuotationListMemberData.m_nRowCount)/(m_oQuotationListMemberData.m_nLogPageSize));
	if((m_oQuotationListMemberData.m_nLogPageNumber-1)* 10 < m_oQuotationListMemberData.m_nRowCount)
		quotationList_listQuotationLogs (m_oQuotationListMemberData.m_nId,m_oQuotationListMemberData.m_nLogPageNumber,m_oQuotationListMemberData.m_nLogPageSize);
	if(m_oQuotationListMemberData.m_nLogPageNumber == nTotalPages)
	{
		quotationDetails_div_loadMoreDetails.style.visibility = "hidden";
	}
}

function quotationList_gotLineItemData (oResponse)
{
	var oQuotationData = oResponse.m_arrQuotations[0];
	var nTotal = quotationList_buildQuationLineItems (oQuotationData.m_oQuotationLineItems);
//	quotationList_loadPurchaseOrderList (oQuotationData.m_oPurchaseOrders)
	$('#QuotationDetails_table_quotationDetailsDG').datagrid('reloadFooter',[{m_nDiscount:'<b>Total</b>', m_nAmount: nTotal}]);
}

function quotationList_loadPurchaseOrderList (arrPurcahseOrder)
{
	assert.isArray(arrPurcahseOrder, "arrPurcahseOrder expected to be an Array.");
	for (var nIndex = 0; nIndex < arrPurcahseOrder.length; nIndex++)
	{
		arrPurcahseOrder[nIndex].m_strCompanyName = arrPurcahseOrder[nIndex].m_oClientData.m_strCompanyName;
		arrPurcahseOrder[nIndex].m_strSiteName = arrPurcahseOrder[nIndex].m_oSiteData.m_strSiteName;
		$("#QuotationDetails_table_itemsDG").datagrid('appendRow',arrPurcahseOrder[nIndex]);
	}
}

function quotationList_buildQuationLineItems (arrQuotationLineItems)
{
	assert.isArray(arrQuotationLineItems, "arrQuotationLineItems expected to be an Array.");
	var nTotal = 0;
	arrQuotationLineItems = getOrderedLineItems (arrQuotationLineItems);
	for (var nIndex = 0; nIndex < arrQuotationLineItems.length; nIndex++)
	{
		try
		{
			arrQuotationLineItems[nIndex].m_strArticleNumber = arrQuotationLineItems[nIndex].m_oItemData.m_strArticleNumber;
		}
		catch (oException)
		{
			arrQuotationLineItems[nIndex].m_strArticleNumber = "";
		}
		arrQuotationLineItems[nIndex].m_strItemName = arrQuotationLineItems[nIndex].m_strArticleDescription;
		arrQuotationLineItems[nIndex].m_nQuantity = arrQuotationLineItems[nIndex].m_nQuantity.toFixed(2);
		arrQuotationLineItems[nIndex].m_nPrice = arrQuotationLineItems[nIndex].m_nPrice.toFixed(2);
		arrQuotationLineItems[nIndex].m_nDiscount = arrQuotationLineItems[nIndex].m_nDiscount.toFixed(2);
		arrQuotationLineItems[nIndex].m_nDiscountPrice = (arrQuotationLineItems[nIndex].m_nPrice * (arrQuotationLineItems[nIndex].m_nDiscount/100));
		arrQuotationLineItems[nIndex].m_nAmount = ((arrQuotationLineItems[nIndex].m_nPrice - arrQuotationLineItems[nIndex].m_nDiscountPrice) * arrQuotationLineItems[nIndex].m_nQuantity);
		nTotal += Number (arrQuotationLineItems[nIndex].m_nAmount);
		$('#QuotationDetails_table_quotationDetailsDG').datagrid('appendRow',arrQuotationLineItems[nIndex]);
	}
	return nTotal;
}

function quotationList_initializeDetailsDG ()
{
	$('#QuotationDetails_table_quotationDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article#',sortable:true,width:70},
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

function quotationList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a String.");
	assert.isString(strOrder, "strOrder expected to be a String.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oQuotationListMemberData.m_strSortColumn = strColumn;
	m_oQuotationListMemberData.m_strSortOrder = strOrder;
	m_oQuotationListMemberData.m_nPageNumber = nPageNumber;
	m_oQuotationListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "quotationList_progressbarLoaded ()");
}
function quotationList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oQuotationData = quotationList_getFormData ();
	oQuotationData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oQuotationData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	QuotationDataProcessor.list(oQuotationData, m_oQuotationListMemberData.m_strSortColumn, m_oQuotationListMemberData.m_strSortOrder, m_oQuotationListMemberData.m_nPageNumber, m_oQuotationListMemberData.m_nPageSize, quotationList_listed);
}

function quotationList_listed (oResponse)
{
	document.getElementById("quotationList_div_listDetail").innerHTML = "";
	clearGridData (m_oQuotationListMemberData.m_strDGId);
	var arrPurcahseOrder = oResponse.m_arrQuotations;
	for (var nIndex = 0; nIndex < arrPurcahseOrder.length; nIndex++)
	{
		arrPurcahseOrder[nIndex].m_strCompanyName = arrPurcahseOrder[nIndex].m_oClientData.m_strCompanyName;
		arrPurcahseOrder[nIndex].m_strSiteName = arrPurcahseOrder[nIndex].m_oSiteData.m_strSiteName;
		$(m_oQuotationListMemberData.m_strDGId).datagrid('appendRow',arrPurcahseOrder[nIndex]);
		if(arrPurcahseOrder[nIndex].m_oContactData != null && arrPurcahseOrder[nIndex].m_oContactData.m_strEmail != "" && !m_oQuotationListMemberData.m_bIsArchived)
		{
			try
			{
				var strId = "quotationList_img_mail" + arrPurcahseOrder[nIndex].m_nQuotationId;
				document.getElementById (strId).style.visibility="visible";
			}
			catch (oException)
			{
				
			}
		}
	}
	var strDataGridId = m_oQuotationListMemberData.m_bIsArchived == true ? ("#quotationList_table_quotationArchived"): ("#quotationList_table_quotationPending");
	$(strDataGridId).datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oQuotationListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function quotationList_getFormData () 
{
	var oQuotationData = new QuotationData ();
//	oQuotationData.m_oClientData.m_strCompanyName =$("#quotationList_input_clientName").val();
	oQuotationData.m_strFromDate = FormatDate ($('#quotationList_input_fromDate').datebox('getValue'));
	oQuotationData.m_strToDate = FormatDate ($('#quotationList_input_toDate').datebox('getValue'));
//	oQuotationData.m_oSiteData.m_strSiteName = $("#quotationList_input_siteName").val();
	oQuotationData.m_bIsArchived = m_oQuotationListMemberData.m_bIsArchived;
	oQuotationData.m_bIsForAllList = false;
	return oQuotationData;
}

function quotationList_cancel ()
{
	HideDialog("dialog");
}

function quotationList_showAddPopup ()
{
	navigate ("Quotation List", "widgets/quotationmanagement/quotation/quotationAdmin.js");
}

function quotationList_createLog (nQuotationId)
{
	assert.isNumber(nQuotationId, "nQuotationId expected to be a Number.");
	m_oQuotationListMemberData.m_nQuotationId = nQuotationId;
	loadPage ("include/process.html", "ProcessDialog", "quotationList_createLog_progressbarLoaded ()");
}

function quotationList_createLog_progressbarLoaded ()
{
	navigate ("QuotationLog", "widgets/quotationmanagement/logs/newQuotationLog.js");
}
function quotation_handleAftersave ()
{
	document.getElementById("quotationList_div_listDetail").innerHTML = "";
	clearGridData (m_oQuotationListMemberData.m_strDGId);
	quotationList_list (m_oQuotationListMemberData.m_strSortColumn, m_oQuotationListMemberData.m_strSortOrder, 1, 10);
}

function quotationList_print ()
{
	navigate ('Print Quotation','widgets/quotationmanagement/quotation/quotationListPrint.js');
}
