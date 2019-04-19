var clientTransaction_includeDataObjects = 
[
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/ReceiptData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/quotationmanagement/quotation/QuotationData.js',
	'widgets/quotationmanagement/quotation/QuotationLineItemData.js',
	'widgets/quotationmanagement/logs/QuotationLogData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderStockLineItemData.js',
	'widgets/inventorymanagement/sales/CustomizedItemData.js',
	'widgets/inventorymanagement/returneditems/ReturnedData.js',
	'widgets/inventorymanagement/returneditems/ReturnedLineItemData.js',
	'widgets/inventorymanagement/returneditems/NonStockReturnedLineItemData.js',
	'widgets/purchaseordermanagement/supply/NonStockSupplyItemsData.js',
	'widgets/purchaseordermanagement/supply/SupplyData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/returneditems/ReturnedLineItem.js',
	'widgets/clientmanagement/DemographyData.js',
];

includeDataObjects (clientTransaction_includeDataObjects, "clientTransaction_loaded()");

function clientTransaction_memberData ()
{
	this.m_oSelectedClientId = -1;
	this.m_oClientData = ""
	this.m_strXmlData = "";
	this.m_oCustomizedItemData=null;
	this.m_oSelectedReceiptRowData = null;
	this.m_oSelectedPurchaseOrderRowData = null;
	this.m_oSelectedInvoiceRowData = null;
	this.m_nSalesDGPageNumber = 1;
    this.m_nSalesDGPageSize =5;
    this.m_nInvoiceDGPageNumber = 1;
    this.m_nInvoiceDGPageSize =5;
    this.m_nChallanDGPageNumber = 1;
    this.m_nChallanDGPageSize =5;
    this.m_nReceiptDGPageNumber = 1;
    this.m_nReceiptDGPageSize =5;
    this.m_nQuotationDGPageNumber = 1;
    this.m_nQuotationDGPageSize =5;
    this.m_nPurchaseOrderDGPageNumber = 1;
    this.m_nPurchaseOrderDGPageSize =5;
    this.m_nReturnedItemDGPageNumber = 1;
    this.m_nReturnedItemDGPageSize =5;
    this.m_strSortSalesDGColumn = "m_dCreatedOn";
    this.m_strSortSalesDGOrder = "desc";
    this.m_strSortInvoiceDGColumn = "m_dCreatedOn";
    this.m_strSortInvoiceDGOrder = "desc";
    this.m_strSortChallanDGColumn = "m_dCreatedOn";
    this.m_strSortChallanDGOrder = "desc";
    this.m_strSortReceiptDGColumn = "m_strCompanyName";
    this.m_strSortReceiptDGOrder = "desc";
    this.m_strSortQuotationDGColumn = "m_dCreatedOn";
    this.m_strSortQuotationDGOrder = "desc";
    this.m_strSortPurchaseOrderDGColumn = "m_strCompanyName";
    this.m_strSortPurchaseOrderDGOrder = "desc";
    this.m_strSortReturnedItemDGColumn = "m_dCreatedOn";
    this.m_strSortReturnedItemDGOrder = "desc";
    this.m_nSelectedInvoiceId = -1;
}

var m_oClientTransactionMemeberData = new clientTransaction_memberData ();

function clientTransaction_init ()
{
	createPopup ("dialog", "#clientTransaction_button__cancel", "#clientTransaction_button__cancel", true);
	clientTransaction_prepareContactsDD ("clientTransaction_select_contacts");
	clientTransaction_prepareSitesDD ("clientTransaction_select_sites");
	clientTransaction_initTabs ();
	clientTransaction_initClientDetails ();
}

function clientTransaction_initTabs ()
{
	$('#clientTransaction_div_tabs').tabs (
	{
		onSelect: function (oTitle)
		{
			if (oTitle.toLowerCase().search ('age wise') >= 0)
			{
//				loadPage ("inventorymanagement/progressbar.html", "secondDialog", "clientTransaction_progressbarLoaded ()");
				clientTransaction_initAgeWiseDG ();
			}
			else if (oTitle.toLowerCase().search ('sales and recepits') >= 0)
			{
				loadPage ("inventorymanagement/progressbar.html", "secondDialog", "clientTransaction_progressbarLoaded ()");
				clientTransaction_initSalesDetailsDG ();
				clientTransaction_initReceiptsDG ();
			}
			else if (oTitle.toLowerCase().search ('challan and invoice') >= 0)
			{
				loadPage ("inventorymanagement/progressbar.html", "secondDialog", "clientTransaction_progressbarLoaded ()");
				clientTransaction_initChallanDetailsDG ();
				clientTransaction_initInvoiceDetailsDG ();
			}
			else if (oTitle.toLowerCase().search ('quotation and order') >= 0)
			{
				loadPage ("inventorymanagement/progressbar.html", "secondDialog", "clientTransaction_progressbarLoaded ()");
				clientTransaction_initQuotationDetailsDG ();
				clientTransaction_initPurchaseOrderDG ();
			}
			else if (oTitle.toLowerCase().search ('items') >= 0 )
			{
				loadPage ("inventorymanagement/progressbar.html", "secondDialog", "clientTransaction_progressbarLoaded ()");
				clientTransaction_initItemDetailsDG ();
				clientTransaction_initReturnedItemsDG ();
			}
			else if (oTitle.toLowerCase().search ('client article info') >= 0 )
			{
				loadPage ("inventorymanagement/progressbar.html", "secondDialog", "clientTransaction_progressbarLoaded ()");
				clientTransaction_initArticleInfoDG ();
			}
		}
	});
}

function clientTransaction_prepareContactsDD (strContacsDD)
{
	var arrContacts = m_oClientTransactionMemeberData.m_oClientData.m_oContacts;
	var arrOptions = new Array ();
	arrOptions.push (CreateOption (-1,"All Contacts"));
	for (var nIndex = 0; nIndex < arrContacts.length; nIndex++)
		arrOptions.push (CreateOption (arrContacts [nIndex].m_nContactId, arrContacts [nIndex].m_strContactName));
	PopulateDD (strContacsDD, arrOptions);
}

function clientTransaction_prepareSitesDD (strSitesDD)
{
	var arrSites = m_oClientTransactionMemeberData.m_oClientData.m_oSites;
	var arrOptions = new Array ();
	arrOptions.push (CreateOption (-1,"All Sites"));
	for (var nIndex = 0; nIndex < arrSites.length; nIndex++)
		arrOptions.push (CreateOption (arrSites [nIndex].m_nSiteId, arrSites [nIndex].m_strSiteName));
	PopulateDD (strSitesDD, arrOptions);
}

function clientTransaction_progressbarLoaded ()
{
	createPopup ("secondDialog", "", "", true);
}

function clientTransaction_initClientDetails ()
{
	var oClientData = m_oClientTransactionMemeberData.m_oClientData;
	$('#clientTransaction_label_clientName').val(oClientData.m_strCompanyName);
	$('#clientTransaction_label_city').val(oClientData.m_strCity);
	$('#clientTransaction_label_address').val(oClientData.m_strAddress);
}

function clientTransaction_cancel ()
{
	HideDialog("dialog");
}

function clientTransaction_initItemDetailsDG ()
{
	$('#clientTransaction_table_itemDetails').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',sortable:true, title:'ArticleNumber',width:100},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:200},
			  	{field:'m_strBrand',title:'Brand',sortable:true,width:100},
			  	{field:'m_strDetail',title:'Detail',sortable:true,width:100},
				{field:'m_nTotalQuantity',title:'Total Quantity',sortable:true,align:'right',width:100,
					 formatter:function(value,row,index)
			        	{
							 return row.m_nTotalQuantity.toFixed(2);	
			        	}
			  	}
			]]
		}
	);
	clientTransaction_subGridDetails ();
	clientTransaction_getClientItem ();
}

function clientTransaction_initReturnedItemsDG ()
{
	$('#clientTransaction_table_returnItemDetails').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strCreditNoteNumber',sortable:true, title:'Credit Note Number',width:100},
				{field:'m_strDate',title:'Date',sortable:true,width:200}
			]]
		}
	);
	clientTransaction_initReturnedItemDetails ();
	clientTransaction_initReturnedItemsDGPagination ();
	clientTransaction_getClientReturnedItems (m_oClientTransactionMemeberData.m_strSortReturnedItemDGColumn, m_oClientTransactionMemeberData.m_strSortReturnedItemDGOrder, 1, 5);
}

function clientTransaction_initReturnedItemDetails ()
{
	$('#clientTransaction_table_returnItemDetails').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="clientTransaction_table_returnedItemDetailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  clientTransaction_table_returnedItemDetailViewDG = $(this).datagrid('getRowDetail',index).find('table.clientTransaction_table_returnedItemDetailViewDG');
	        clientTransaction_table_returnedItemDetailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,remoteSort:false, height:'auto',
	            columns:[[
	                      {field:'m_strArticleNumber',title:'Article#',sortable:true,width:80},
	                      {field:'m_strItemName',title:'Item Name',sortable:true,width:50},
	                      {field:'m_nShippedQuantity',title:'Shipped Qty',sortable:true,width:50,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nShippedQuantity.toFixed(2);	
					        	}
	                      },
	                      {field:'m_nQuantity',title:'Returned Qty',width:50,sortable:true,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nQuantity.toFixed(2);	
					        	}
	                      }
	            ]],
	            onResize:function()
	            {
	                $('#clientTransaction_table_returnItemDetails').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#clientTransaction_table_returnItemDetails').datagrid('fixDetailRowHeight',index);
	        clientTransaction_returnedItemDetailsListed (clientTransaction_table_returnedItemDetailViewDG, index, row);
	    }
	});
}

function clientTransaction_returnedItemDetailsListed (clientTransaction_table_returnedItemDetailViewDG, index, row)
{
	assert.isString(clientTransaction_table_returnedItemDetailViewDG, "clientTransaction_table_returnedItemDetailViewDG expected to be a string.");
	assert.isString(row, "row expected to be a string.");
	clearGridData (clientTransaction_table_returnedItemDetailViewDG);
	var arrReturns = clientTransaction_buildReturnedItemsArray (row);
	clientTransaction_table_returnedItemDetailViewDG.datagrid('loadData', arrReturns);
}

function clientTransaction_buildReturnedItemsArray (oReturnedData)
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

function clientTransaction_initReturnedItemsDGPagination ()
{
	$('#clientTransaction_table_returnItemDetails').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nReturnedItemDGPageNumber = nPageNumber;
				clientTransaction_getClientReturnedItems (m_oClientTransactionMemeberData.m_strSortReturnedItemDGColumn, m_oClientTransactionMemeberData.m_strSortReturnedItemDGOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nReturnedItemDGPageNumber = nPageNumber;
				m_oClientTransactionMemeberData.m_nReturnedItemDGPageSize = nPageSize;
				clientTransaction_getClientReturnedItems (m_oClientTransactionMemeberData.m_strSortReturnedItemDGColumn, m_oClientTransactionMemeberData.m_strSortReturnedItemDGOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function clientTransaction_getClientReturnedItems (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oReturnedData = new ReturnedData ();
	oReturnedData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	ReturnedDataProcessor.list(oReturnedData, strColumn, strOrder, nPageNumber, nPageSize, clientTransaction_gotClientReturnedItems);
}

function clientTransaction_gotClientReturnedItems (oResponse)
{
	$('#clientTransaction_table_returnItemDetails').datagrid('loadData', oResponse.m_arrReturnedData);
	$('#clientTransaction_table_returnItemDetails').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientTransactionMemeberData.m_nReturnedItemDGPageNumber});
}

function clientTransaction_initArticleInfoDG ()
{
	$('#clientTransaction_table_articleInfo').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strClientArticleNumber',sortable:true, title:'Client Article Number',width:100},
				{field:'m_strClientArticleDescription',title:'Client Article Description',sortable:true,width:200},
			  	{field:'m_strArticleNumber',title:'Item Article Number',sortable:true,width:100,
			  		formatter:function(value,row,index)
		        	{
						 return row.m_oItemData.m_strArticleNumber;	
		        	}
				},
			  	{field:'m_strDetail',title:'Item Article Description',sortable:true,width:100,
			  		formatter:function(value,row,index)
		        	{
						 return row.m_oItemData.m_strItemName + "|" + row.m_oItemData.m_strBrand + "|" +row.m_oItemData.m_strDetail;	
		        	}
				},
				{field:'Actions',title:'Action',width:50,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return clientTransactions_addActions (row, index);
		        	}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				m_oClientTransactionMemeberData.m_oCustomizedItemData = rowData;
			},
		}
	);
	clientTransaction_getClientArticleList ();
}

function clientTransaction_getClientArticleList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oCustomizedItemData = new CustomizedItemData ();
	oCustomizedItemData.m_oClientData = new ClientData ();
	oCustomizedItemData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	CustomizedItemDataProcessor.list(oCustomizedItemData, "", "", clientTransaction_gotClientArticleList);
}

function clientTransaction_gotClientArticleList (oResponse)
{
	clearGridData ("#clientTransaction_table_articleInfo");
	$('#clientTransaction_table_articleInfo').datagrid('loadData',oResponse.m_arrCustomizeItemData);
	//$('#clientTransaction_table_articleInfo').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientTransactionMemeberData.m_nInvoiceDGPageNumber});
	HideDialog("secondDialog");
}

function clientTransactions_addActions (row,index)
{
	var oActions = 
				'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" align="center" onClick="clientTransaction_EditArticleInfo ('+row.m_nCustomizeId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function clientTransaction_addArticleInfo ()
{
	navigate ('articleCustomize','widgets/inventorymanagement/sales/articleCustomizeNew.js');
}

function clientTransaction_EditArticleInfo ()
{
	navigate ('articleCustomize','widgets/inventorymanagement/sales/articleCustomizeEdit.js');
}

function articleCustomize_handleAfterSave ()
{
	clientTransaction_getClientArticleList ();
}

function clientTransaction_getClientItem ()
{
	var oSalesData = new SalesData ();
	oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oSalesData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	oSalesData.m_oContactData.m_nContactId = $("#clientTransaction_select_contacts").val();
	oSalesData.m_oSiteData.m_nSiteId = $("#clientTransaction_select_sites").val();
	SalesDataProcessor.getClientItem(oSalesData, clientTransaction_gotClientItem);
}

function clientTransaction_gotClientItem (oResponse)
{
	clearGridData ("#clientTransaction_table_itemDetails");
	var arrClientItemData = oResponse.m_arrClientItemData;
	for(var nIndex = 0; nIndex < arrClientItemData.length; nIndex++)
	{
		arrClientItemData[nIndex].m_strArticleNumber = arrClientItemData[nIndex].m_oItemData.m_strArticleNumber
		arrClientItemData[nIndex].m_strItemName = arrClientItemData[nIndex].m_oItemData.m_strItemName;
		arrClientItemData[nIndex].m_strBrand = arrClientItemData[nIndex].m_oItemData.m_strBrand;
		arrClientItemData[nIndex].m_strDetail = arrClientItemData[nIndex].m_oItemData.m_strDetail
		$('#clientTransaction_table_itemDetails').datagrid('appendRow',arrClientItemData[nIndex]);
	}
	HideDialog("secondDialog");
}

function clientTransaction_subGridDetails ()
{
	$('#clientTransaction_table_itemDetails').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="clientTransaction_table_DetailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  clientTransaction_table_DetailViewDG = $(this).datagrid('getRowDetail',index).find('table.clientTransaction_table_DetailViewDG');
	        clientTransaction_table_DetailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,remoteSort:false, height:'auto',
	            columns:[[
	                      {field:'m_strDate',title:'Date',sortable:true,width:40},
	                      {field:'m_strInvoiceNo',title:'InvoiceNo',sortable:true,width:80},
	                      {field:'m_nQuantity',title:'Quantity',sortable:true,width:50,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nQuantity.toFixed(2);	
					        	}
	                      },
	                      {field:'m_nPrice',title:'Price',width:60,sortable:true,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nPrice.toFixed(2);	
					        	}
	                      },
	                      {field:'m_nDiscount',title:'Discount(%)',width:50,sortable:true,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nDiscount.toFixed(2);	
					        	}
	                      },
	                      {field:'m_nTax',title:'Tax(%)',width:50,sortable:true,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nTax.toFixed(2);	
					        	}
	                      },
	                      {field:'m_strTaxName',title:'TaxName',sortable:true,width:50}
	            ]],
	            onResize:function()
	            {
	                $('#clientTransaction_table_itemDetails').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        $('#clientTransaction_table_itemDetails').datagrid('fixDetailRowHeight',index);
	        clientTransaction_subgridListed (clientTransaction_table_DetailViewDG, index, row);
	    }
	});
}

function clientTransaction_subgridListed (clientTransaction_table_DetailViewDG, index, row)
{
	clearGridData (clientTransaction_table_DetailViewDG);
	for(var nIndex = 0; nIndex < row.m_arrClientItemSalesData.length; nIndex++)
		clientTransaction_table_DetailViewDG.datagrid('appendRow',row.m_arrClientItemSalesData[nIndex]);
}

function clientTransaction_itemDetails_displayImages (oRow, nIndex)
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="clientTransaction_getReceiptsInfo ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}

function clientTransaction_initSalesDetailsDG ()
{
	$('#clientTransaction_table_salesDetails').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strDate',title:'Date',sortable:true,width:100},
				{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:100},
				{field:'m_nSalesTotal',title:'Amount.',sortable:true,width:100,align:'right',
					formatter:function(value,row,index)
		        	{
						var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;
		        	}
				},
				{field:'Actions',title:'Action',width:60,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return clientTransaction_salesDetails_displayImages (row, index);
		        	}
				}
			]],
			onSortColumn: function (strColumn, strOrder)
			{
				if(strColumn != "m_nSalesTotal")
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oClientTransactionMemeberData.m_strSortSalesDGColumn = strColumn;
					m_oClientTransactionMemeberData.m_strSortSalesDGOrder = strOrder;
					clientTransaction_getSalesList (strColumn, strOrder, m_oClientTransactionMemeberData.m_nSalesDGPageNumber, m_oClientTransactionMemeberData.m_nSalesDGPageSize);
				}
			}
		}
	);
	clientTransaction_initSalesDetailsDGPagination ();
	clientTransaction_getSalesList (m_oClientTransactionMemeberData.m_strSortSalesDGColumn,m_oClientTransactionMemeberData.m_strSortSalesDGOrder, 1, 5 );
}

function clientTransaction_getSalesList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oSalesData = new SalesData ();
	oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oSalesData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	oSalesData.m_oContactData.m_nContactId = $("#clientTransaction_select_contacts").val();
	oSalesData.m_oSiteData.m_nSiteId = $("#clientTransaction_select_sites").val();
	SalesDataProcessor.list(oSalesData, strColumn, strOrder, nPageNumber, nPageSize, clientTransaction_gotSalesList);
}

function clientTransaction_gotSalesList (oResponse)
{
	clearGridData ("#clientTransaction_table_salesDetails");
	var arrSales = oResponse.m_arrSales;
	for(var nIndex = 0; nIndex < arrSales.length; nIndex++)
	{
		var arrLineItemData = arrSales[nIndex].m_oSalesLineItems.concat(arrSales[nIndex].m_oNonStockSalesLineItems);
		arrSales[nIndex].m_nSalesTotal = clientTransaction_getSalesTotal (arrLineItemData);
		$('#clientTransaction_table_salesDetails').datagrid('appendRow',arrSales[nIndex]);
	}
	$('#clientTransaction_table_salesDetails').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientTransactionMemeberData.m_nSalesDGPageNumber});
	HideDialog("secondDialog");
}

function clientTransaction_getSalesTotal (arrSalesLineItems)
{
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrSalesLineItems.length; nIndex++)
		nTotal += (arrSalesLineItems[nIndex].m_nPrice * ((100 - arrSalesLineItems[nIndex].m_nDiscount)/100))*arrSalesLineItems[nIndex].m_nQuantity;
	return nTotal;
}

function clientTransaction_salesDetails_displayImages (oRow, nIndex)
{
	var oActions = 
		'<table align="center">'+
			'<tr>'+
				'<td> <img title="Print" src="images/print.jpg" width="20" align="center" id="InfoImageId" onClick="clientTransaction_getSalesInfo ('+oRow.m_nId+')"/> </td>'+
			'</tr>'+
		'</table>'
	return oActions;
}

function clientTransaction_initSalesDetailsDGPagination ()
{
	$('#clientTransaction_table_salesDetails').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nSalesDGPageNumber = $('#clientTransaction_table_salesDetails').datagrid('getPager').pagination('options').pageNumber;
				clientTransaction_getSalesList (m_oClientTransactionMemeberData.m_strSortSalesDGColumn, m_oClientTransactionMemeberData.m_strSortSalesDGOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nSalesDGPageNumber = $('#clientTransaction_table_salesDetails').datagrid('getPager').pagination('options').pageNumber;
				m_oClientTransactionMemeberData.m_nSalesDGPageSize = $('#clientTransaction_table_salesDetails').datagrid('getPager').pagination('options').pageSize;
				clientTransaction_getSalesList (m_oClientTransactionMemeberData.m_strSortSalesDGColumn, m_oClientTransactionMemeberData.m_strSortSalesDGOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function clientTransaction_initInvoiceDetailsDG () 
{
	$('#clientTransaction_table_invoiceDetails').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strDate',title:'Date',sortable:true,width:150},
				{field:'m_strInvoiceNumber',title:'Invoice No.',sortable:true,width:150},
				{field:'m_nInvoiceAmount',title:'Amount',sortable:true,width:150,align:'right',
					formatter:function(value,row,index)
		        	{
						value = Math.round(value);
						var m_nIndianNumber = formatNumber (row.m_nInvoiceAmount.toFixed(2));
						return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;
		        	}
				},
				{field:'Actions',title:'Action',width:60,align:'center',
					formatter:function(value,row,index)
					{
						return clientTransaction_invoiceDetails_displayImages (row, index);
					}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				m_oClientTransactionMemeberData.m_oSelectedInvoiceRowData = rowData;
				clientTransaction_selectedInvoiceRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oClientTransactionMemeberData.m_strSortInvoiceDGColumn = strColumn;
				m_oClientTransactionMemeberData.m_strSortInvoiceDGOrder = strOrder;
				clientTransaction_getInvoiceList (strColumn, strOrder, m_oClientTransactionMemeberData.m_nInvoiceDGPageNumber, m_oClientTransactionMemeberData.m_nInvoiceDGPageSize);
			}
		}
	);
	clientTransaction_initInvoiceDetailsDGPagination ();
	clientTransaction_getInvoiceList (m_oClientTransactionMemeberData.m_strSortInvoiceDGColumn, m_oClientTransactionMemeberData.m_strSortInvoiceDGOrder, 1, 5);
	
}

function clientTransaction_getInvoiceList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oUserCredentialsData = new UserInformationData ();
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	oInvoiceData.m_nContactId = $("#clientTransaction_select_contacts").val();
	oInvoiceData.m_nSiteId = $("#clientTransaction_select_sites").val();	
	InvoiceDataProcessor.list(oInvoiceData, strColumn, strOrder, nPageNumber, nPageSize, clientTransaction_gotInvoiceList);
}

function clientTransaction_gotInvoiceList (oResponse)
{
	clearGridData ("#clientTransaction_table_invoiceDetails");
	$('#clientTransaction_table_invoiceDetails').datagrid('loadData',oResponse.m_arrInvoice);
	$('#clientTransaction_table_invoiceDetails').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientTransactionMemeberData.m_nInvoiceDGPageNumber});
	HideDialog("secondDialog");
}

function clientTransaction_invoiceDetails_displayImages (oRow, nIndex)
{
	var oActions = 
		'<table align="center">'+
			'<tr>'+
				'<td> <img title="Print" src="images/print.jpg" width="20" align="center" id="InfoImageId" onClick="clientTransaction_getInvoiceInfo ()"/> </td>'+
			'</tr>'+
		'</table>'
	return oActions;
}

function clientTransaction_initInvoiceDetailsDGPagination ()
{
	$('#clientTransaction_table_invoiceDetails').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nInvoiceDGPageNumber = $('#clientTransaction_table_invoiceDetails').datagrid('getPager').pagination('options').pageNumber;
				clientTransaction_getInvoiceList (m_oClientTransactionMemeberData.m_strSortInvoiceDGColumn, m_oClientTransactionMemeberData.m_strSortInvoiceDGOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nInvoiceDGPageNumber = $('#clientTransaction_table_invoiceDetails').datagrid('getPager').pagination('options').pageNumber;
				m_oClientTransactionMemeberData.m_nInvoiceDGPageSize = $('#clientTransaction_table_invoiceDetails').datagrid('getPager').pagination('options').pageSize;
				clientTransaction_getInvoiceList (m_oClientTransactionMemeberData.m_strSortInvoiceDGColumn, m_oClientTransactionMemeberData.m_strSortInvoiceDGOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function clientTransaction_initChallanDetailsDG () 
{
	$('#clientTransaction_table_challanDetails').datagrid
	(
		{
			columns:
			[[
				{field:'m_strDate',title:'Date',sortable:true,width:100,
					formatter:function(value,row,index)
					{
						return row.m_oSalesData.m_strDate;
					}
				},
				{field:'m_strChallanNumber',title:'Challan No.',sortable:true,width:100},
				{field:'m_nChallanAmount',title:'Amount',sortable:true,width:100,align:'right',
					formatter:function(value,row,index)
		        	{
						value = Math.round(value);
						var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;
		        	}
				},
				{field:'Actions',title:'Action',width:60,align:'center',
					formatter:function(value,row,index)
					{
						return clientTransaction_challanDetails_displayImages (row, index);
					}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				clientTransaction_selectedChallanRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oClientTransactionMemeberData.m_strSortChallanDGColumn = strColumn;
				m_oClientTransactionMemeberData.m_strSortChallanDGOrder = strOrder;
				challanList_list (strColumn, strOrder, m_oClientTransactionMemeberData.m_nChallanDGPageNumber, m_oClientTransactionMemeberData.m_nChallanDGPageSize);
			}
		}
	);
	clientTransaction_initChallanDGPagination ();
	clientTransaction_getChallanList (m_oClientTransactionMemeberData.m_strSortChallanDGColumn, m_oClientTransactionMemeberData.m_strSortChallanDGOrder, 1, 5);
}

function clientTransaction_getChallanList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oChallanData = new ChallanData ();
	oChallanData.m_oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oChallanData.m_oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oChallanData.m_oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oChallanData.m_oSalesData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	oChallanData.m_oSalesData.m_oContactData.m_nContactId = $("#clientTransaction_select_contacts").val();
	oChallanData.m_oSalesData.m_oSiteData.m_nSiteId = $("#clientTransaction_select_sites").val();
	ChallanDataProcessor.list(oChallanData, strColumn, strOrder, nPageNumber, nPageSize, clientTransaction_gotChallanList);
}

function clientTransaction_gotChallanList (oResponse)
{
	clearGridData ("#clientTransaction_table_challanDetails");
	$('#clientTransaction_table_challanDetails').datagrid('loadData',oResponse.m_arrChallan);
	$('#clientTransaction_table_challanDetails').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientTransactionMemeberData.m_nChallanDGPageNumber});
}

function clientTransaction_challanDetails_displayImages (oRow, nIndex)
{
	var oActions = 
		'<table align="center">'+
			'<tr>'+
				'<td> <img title="Print" src="images/print.jpg" width="20" align="center" id="InfoImageId" onClick="clientTransaction_printChallan ()"/> </td>'+
			'</tr>'+
		'</table>'
	return oActions;
}

function clientTransaction_initChallanDGPagination ()
{
	$('#clientTransaction_table_challanDetails').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nChallanDGPageNumber = $('#clientTransaction_table_challanDetails').datagrid('getPager').pagination('options').pageNumber;
				clientTransaction_getChallanList (m_oClientTransactionMemeberData.m_strSortChallanDGColumn, m_oClientTransactionMemeberData.m_strSortChallanDGOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nChallanDGPageNumber = $('#clientTransaction_table_challanDetails').datagrid('getPager').pagination('options').pageNumber;
				m_oClientTransactionMemeberData.m_nChallanDGPageSize = $('#clientTransaction_table_challanDetails').datagrid('getPager').pagination('options').pageSize;
				clientTransaction_getChallanList (m_oClientTransactionMemeberData.m_strSortChallanDGColumn, m_oClientTransactionMemeberData.m_strSortChallanDGOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function clientTransaction_initReceiptsDG () 
{
	$('#clientTransaction_table_receipts').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strDate',title:'Date',sortable:true,width:150},
				{field:'m_strModeName',title:'Mode',sortable:true,width:150},
				{field:'m_nAmount',title:'Amount',sortable:true,width:150,align:'right',
					formatter:function(value,row,index)
		        	{
						value = Math.round(value);
						var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;
		        	}
				},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
					{
						return clientTransaction_Receipts_displayImages (row, index);
					}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				m_oClientTransactionMemeberData.m_oSelectedReceiptRowData = rowData;
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oClientTransactionMemeberData.m_strSortReceiptDGColumn = strColumn;
				m_oClientTransactionMemeberData.m_strSortReceiptDGOrder = strOrder;
				clientTransaction_getReceipts (strColumn, strOrder, m_oClientTransactionMemeberData.m_nReceiptDGPageNumber, m_oClientTransactionMemeberData.m_nReceiptDGPageSize);
			}
		}
	);
	clientTransaction_initReceiptsDGPagination ();
	clientTransaction_getReceipts (m_oClientTransactionMemeberData.m_strSortReceiptDGColumn, m_oClientTransactionMemeberData.m_strSortReceiptDGOrder, 1, 5);
}

function clientTransaction_getReceipts (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oReceiptData = new ReceiptData ();
	oReceiptData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oReceiptData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	oReceiptData.m_nContactId = $("#clientTransaction_select_contacts").val();
	oReceiptData.m_nSiteId =$("#clientTransaction_select_sites").val();
	ReceiptDataProcessor.list(oReceiptData, strColumn, strOrder, nPageNumber, nPageSize, clientTransaction_gotReceipts);
}

function clientTransaction_gotReceipts (oResponse)
{
	clearGridData ("#clientTransaction_table_receipts");
	var arrReceipt = oResponse.m_arrReceiptData;
	for(var nIndex = 0; nIndex< arrReceipt.length; nIndex++)
	{
		arrReceipt[nIndex].m_strCompanyName = arrReceipt[nIndex].m_oClientData.m_strCompanyName;
		arrReceipt[nIndex].m_strModeName = arrReceipt[nIndex].m_oMode.m_strModeName;
		$('#clientTransaction_table_receipts').datagrid('appendRow', arrReceipt[nIndex]);
	}
	$('#clientTransaction_table_receipts').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientTransactionMemeberData.m_nReceiptDGPageNumber});
	HideDialog("secondDialog");
}

function clientTransaction_Receipts_displayImages (oRow, nIndex)
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="clientTransaction_getReceiptsInfo ()"/> </td>'+
					'</tr>'+
					'</table>'
	return oImage;
}

function clientTransaction_initReceiptsDGPagination ()
{
	$('#clientTransaction_table_receipts').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nReceiptDGPageNumber = $('#clientTransaction_table_receipts').datagrid('getPager').pagination('options').pageNumber;
				clientTransaction_getReceipts (m_oClientTransactionMemeberData.m_strSortReceiptDGColumn, m_oClientTransactionMemeberData.m_strSortReceiptDGOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nReceiptDGPageNumber = $('#clientTransaction_table_receipts').datagrid('getPager').pagination('options').pageNumber;
				m_oClientTransactionMemeberData.m_nReceiptDGPageSize = $('#clientTransaction_table_receipts').datagrid('getPager').pagination('options').pageSize;
				clientTransaction_getReceipts (m_oClientTransactionMemeberData.m_strSortReceiptDGColumn, m_oClientTransactionMemeberData.m_strSortReceiptDGOrder,nPageNumber, nPageSize);
			}
		}
	);
}

function clientTransaction_getSalesInfo (nSalesId)
{
	var oSalesData = new SalesData ();
	oSalesData.m_nId = nSalesId;
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	SalesDataProcessor.getXML (oSalesData,	{
		async:false, 
		callback: function (strXMLData)
		{
			m_oClientTransactionMemeberData.m_strXmlData = strXMLData;
		}
	});
	navigate ('salesPrint','widgets/clientmanagement/clientTransactionSalesPrint.js');
}

function clientTransaction_selectedInvoiceRowData (oRowData, nRowIndex)
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_nInvoiceId = oRowData.m_nInvoiceId;
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	InvoiceDataProcessor.getXML (oInvoiceData,	
	{
		async:false, 
		callback: function (strXMLData)
		{
			m_oClientTransactionMemeberData.m_strXmlData = strXMLData;
		}
	});
}

function clientTransaction_getInvoiceInfo ()
{
	navigate ('invoicePrint','widgets/clientmanagement/clientTransactionInvoicePrint.js');
}

function clientTransaction_selectedChallanRowData (oRowData, nIndex)
{
	m_oClientTransactionMemeberData.m_nChallanId = oRowData.m_nChallanId;
	m_oClientTransactionMemeberData.m_nInvoiceId = oRowData.m_oInvoiceData != null ? oRowData.m_oInvoiceData.m_nInvoiceId : -1;
}

function clientTransaction_printChallan ()
{
	navigate ('challanPrint','widgets/clientmanagement/clientTransactionChallanPrint.js');
}
	
function clientTransaction_getReceiptsInfo ()
{
	navigate ('purchaseInfo','widgets/clientmanagement/clientTransactionReceiptInfo.js');
}


function clientTransaction_initQuotationDetailsDG ()
{
	$('#clientTransaction_table_QuotationDetails').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strSiteName',title:'Site',sortable:true,width:150},
				{field:'m_strDate',title:'Date',sortable:true,width:70},
				{field:'m_nQuotationAmount',title:'Amount',sortable:true,width:120,align:'right',
					formatter:function(value,row,index)
		        	{
						value = Math.round(value);
						var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;
		        	}
				},
			  	{field:'Actions',title:'Action',width:60,align:'center',
				formatter:function(value,row,index)
	        	{
	        		return clientTransaction_quotationDetails_displayImages (row, index);
	        	}
			}
			]],
			onSelect: function (rowIndex, rowData)
			{
				clientTransaction_selectedQuotationRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				if(strColumn != "m_nQuotationAmount")
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oClientTransactionMemeberData.m_strSortQuotationDGColumn = strColumn;
					m_oClientTransactionMemeberData.m_strSortQuotationDGOrder = strOrder;
					clientTransaction_getQuotationList (strColumn, strOrder, m_oClientTransactionMemeberData.m_nQuotationDGPageNumber, m_oClientTransactionMemeberData.m_nQuotationDGPageSize);
				}
			}
		}
	);
		clientTransaction_initQuotationDetailsDGPagination ();
		clientTransaction_getQuotationList (m_oClientTransactionMemeberData.m_strSortQuotationDGColumn,m_oClientTransactionMemeberData.m_strSortQuotationDGOrder, 1, 5 );
}

function clientTransaction_getQuotationList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oQuotationData = new QuotationData ();
	oQuotationData.m_oUserCredentialsData = new UserInformationData ();
	oQuotationData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oQuotationData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oQuotationData.m_oClientData = new ClientData ();
	oQuotationData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	oQuotationData.m_oContactData.m_nContactId =$("#clientTransaction_select_contacts").val();
	oQuotationData.m_oSiteData.m_nSiteId = $("#clientTransaction_select_sites").val();
	oQuotationData.m_bIsForAllList = true;
	QuotationDataProcessor.list(oQuotationData, strColumn, strOrder, nPageNumber, nPageSize, clientTransaction_gotQuotationList);
}

function clientTransaction_gotQuotationList (oResponse)
{
	clearGridData ("#clientTransaction_table_QuotationDetails");
	var arrQuotations = oResponse.m_arrQuotations;
	for(var nIndex = 0; nIndex < arrQuotations.length; nIndex++)
	{
			arrQuotations[nIndex].m_strCompanyName = arrQuotations[nIndex].m_oClientData.m_strCompanyName;
			arrQuotations[nIndex].m_strSiteName = arrQuotations[nIndex].m_oSiteData.m_strSiteName;
			arrQuotations[nIndex].m_nQuotationAmount = clientTransaction_getQuotationAmount (arrQuotations[nIndex].m_oQuotationLineItems);
			$('#clientTransaction_table_QuotationDetails').datagrid('appendRow', arrQuotations[nIndex]);
	}
	$('#clientTransaction_table_QuotationDetails').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientTransactionMemeberData.m_nQuotationDGPageNumber});
	HideDialog("secondDialog");
}

function clientTransaction_getQuotationAmount (arrQuotationLineItems)
{
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrQuotationLineItems.length; nIndex++)
	{
		var nAmount = (arrQuotationLineItems[nIndex].m_nPrice * ((100 - arrQuotationLineItems[nIndex].m_nDiscount)/100))*arrQuotationLineItems[nIndex].m_nQuantity;
		nTotal += nAmount + (nAmount *(arrQuotationLineItems[nIndex].m_nTax/100));
	}
	return nTotal;
}

function clientTransaction_quotationDetails_displayImages (oRow, nIndex)
{
	var oActions = 
		'<table align="center">'+
			'<tr>'+
				'<td> <img title="Print" src="images/print.jpg" width="20" align="center" id="InfoImageId" onClick="clientTransaction_getQuotationInfo ('+oRow.m_nQuotationId+')"/> </td>'+
			'</tr>'+
		'</table>'
	return oActions;
}

function clientTransaction_initQuotationDetailsDGPagination ()
{
	$('#clientTransaction_table_QuotationDetails').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nQuotationDGPageNumber = nPageNumber;
				clientTransaction_getQuotationList (m_oClientTransactionMemeberData.m_strSortQuotationDGColumn, m_oClientTransactionMemeberData.m_strSortQuotationDGOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nQuotationDGPageNumber = nPageNumber;
				m_oClientTransactionMemeberData.m_nQuotationDGPageSize = nPageSize;
				clientTransaction_getQuotationList (m_oClientTransactionMemeberData.m_strSortQuotationDGColumn, m_oClientTransactionMemeberData.m_strSortQuotationDGOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function clientTransaction_selectedQuotationRowData (oRowData, nRowIndex)
{
	var oQuotationData = new QuotationData ();
	oQuotationData.m_nQuotationId = oRowData.m_nQuotationId;
	oQuotationData.m_oUserCredentialsData = new UserInformationData ();
	oQuotationData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oQuotationData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	QuotationDataProcessor.getXML (oQuotationData,	
	{
		async:false, 
		callback: function (strXMLData)
		{
			m_oClientTransactionMemeberData.m_strXMLData = strXMLData;
			m_oClientTransactionMemeberData.m_strEmailAddress = oRowData.m_oContactData != null ? oRowData.m_oContactData.m_strEmail : "";
			m_oClientTransactionMemeberData.m_strSubject = "Quotation Details";
		}
	});
}

function clientTransaction_getQuotationInfo ()
{
	navigate ('invoicePrint','widgets/clientmanagement/clientTransactionQuotationPrint.js');
}


function clientTransaction_initPurchaseOrderDG ()
{
	$('#clientTransaction_table_purchaseOrderDetails').datagrid
	(
		{
			columns:
			[[
				{field:'m_strPurchaseOrderDate',title:'Date',sortable:true,width:150},
			  	{field:'m_strPurchaseOrderNumber',title:'Purchase Order No.',sortable:true,width:150},
			  	{field:'m_nAmount',title:'Amount',sortable:true,width:120,align:'right',
					formatter:function(value,row,index)
		        	{
						var m_nIndianNumber = formatNumber (value.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + m_nIndianNumber;
		        	}
				},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
					{
						return clientTransaction_PurchaseOrder_displayImages (row, index);
					}
				}
			]],
			onSelect: function (rowIndex, rowData)
			{
				m_oClientTransactionMemeberData.m_oSelectedPurchaseOrderRowData = rowData;
			},
			onSortColumn: function (strColumn, strOrder)
			{
				if(strColumn != "m_nAmount")
				{
					strColumn = (strColumn == "m_strPurchaseOrderDate") ? "m_dCreatedOn" : strColumn;
					m_oClientTransactionMemeberData.m_strSortPurchaseOrderDGColumn = strColumn;
					m_oClientTransactionMemeberData.m_strSortPurchaseOrderDGOrder = strOrder;
					clientTransaction_getPurchaseOrderList (strColumn, strOrder, m_oClientTransactionMemeberData.m_nPurchaseOrderDGPageNumber, m_oClientTransactionMemeberData.m_nPurchaseOrderDGPageSize);
				}
			}
		}
	);
		clientTransaction_initPurchaseOrderDGPagination ();
		clientTransaction_getPurchaseOrderList (m_oClientTransactionMemeberData.m_strSortPurchaseOrderDGColumn,m_oClientTransactionMemeberData.m_strSortPurchaseOrderDGOrder, 1, 5 );
}

function clientTransaction_getPurchaseOrderList (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oPurchaseOrderData = new PurchaseOrderData ();
	oPurchaseOrderData.m_oUserCredentialsData = new UserInformationData ();
	oPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oPurchaseOrderData.m_oClientData = new ClientData ();
	oPurchaseOrderData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	oPurchaseOrderData.m_oContactData.m_nContactId = $("#clientTransaction_select_contacts").val();
	oPurchaseOrderData.m_oSiteData.m_nSiteId = $("#clientTransaction_select_sites").val();
	oPurchaseOrderData.m_bIsForAllList = true;
	PurchaseOrderDataProcessor.list(oPurchaseOrderData, strColumn, strOrder, nPageNumber, nPageSize, clientTransaction_gotPurchaseOrderList);
}

function clientTransaction_gotPurchaseOrderList (oResponse)
{
	clearGridData ("#clientTransaction_table_purchaseOrderDetails");
	var arrPurchaseOrder = oResponse.m_arrPurchaseOrder;
	for(var nIndex = 0; nIndex < arrPurchaseOrder.length; nIndex++)
	{
			arrPurchaseOrder[nIndex].m_strCompanyName = arrPurchaseOrder[nIndex].m_oClientData.m_strCompanyName;
			arrPurchaseOrder[nIndex].m_strSiteName = arrPurchaseOrder[nIndex].m_oSiteData.m_strSiteName;
			arrPurchaseOrder[nIndex].m_nAmount = clientTransaction_getOrderAmount (arrPurchaseOrder[nIndex].m_oPurchaseOrderLineItems);
			$('#clientTransaction_table_purchaseOrderDetails').datagrid('appendRow', arrPurchaseOrder[nIndex]);
	}
	$('#clientTransaction_table_purchaseOrderDetails').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientTransactionMemeberData.m_nPurchaseOrderDGPageNumber});
}

function clientTransaction_getOrderAmount (arrLineItems)
{
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrLineItems.length; nIndex++)
	{
		var nAmount = (arrLineItems[nIndex].m_nPrice * ((100 - arrLineItems[nIndex].m_nDiscount)/100))*arrLineItems[nIndex].m_nQty;
		nTotal += nAmount + (nAmount *(arrLineItems[nIndex].m_nTax/100));
	}
	return nTotal;
}

function clientTransaction_initPurchaseOrderDGPagination ()
{
	$('#clientTransaction_table_purchaseOrderDetails').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nPurchaseOrderDGPageNumber = nPageNumber;
				clientTransaction_getPurchaseOrderList (m_oClientTransactionMemeberData.m_strSortPurchaseOrderDGColumn, m_oClientTransactionMemeberData.m_strSortPurchaseOrderDGOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientTransactionMemeberData.m_nPurchaseOrderDGPageNumber = nPageNumber;
				m_oClientTransactionMemeberData.m_nPurchaseOrderDGPageSize = nPageSize;
				clientTransaction_getPurchaseOrderList (m_oClientTransactionMemeberData.m_strSortPurchaseOrderDGColumn, m_oClientTransactionMemeberData.m_strSortPurchaseOrderDGOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function clientTransaction_PurchaseOrder_displayImages ()
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="clientTransaction_getPurchaseOrderInfo ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}


function clientTransaction_getPurchaseOrderInfo ()
{
	navigate ('purchaseInfo','widgets/clientmanagement/clientTransactionPurchaseOrderInfo.js');
}

function clientTransaction_listOnContacts ()
{
	clientTransaction_initTabs ();
}

function clientTransaction_makeReceipt (nInvoiceId)
{
	m_oClientTransactionMemeberData.m_nSelectedInvoiceId = nInvoiceId;
	navigate ('makeReceipt','widgets/inventorymanagement/paymentsandreceipt/receiptForClientInvoice.js');
}

function receipt_handleAfterSave ()
{
	clientTransaction_getInvoiceList (m_oClientTransactionMemeberData.m_strSortInvoiceDGColumn, m_oClientTransactionMemeberData.m_strSortInvoiceDGOrder, 1, 5);
}

function clientTransaction_initAgeWiseDG ()
{
	$('#clientTransaction_table_ageWise').datagrid
	(
		{
			columns:
				[[
				  	{field:'m_strPeriod',title:'Duration(days)',sortable:true,width:150,
				  		formatter:function(value,row,index)
			        	{
					  		if(value != undefined && value.localeCompare("91-") == 0)
				  				value =  "91 and above";
				  			return value;
			        	}
				  	},
				  	{field:'m_nInvoicesCount',title:'No. of Invoice',sortable:true,width:150,align:'right',
				  		formatter:function(value,row,index)
			        	{
				  			return value;
			        	}
				  	},
					{field:'m_nAmount',title:'Amount',sortable:true,width:100,align:'right',
				  		formatter:function(value,row,index)
			        	{
				  			var nAmount = 0;
							if(value > 0)
								nAmount = value;
							var m_nIndianNumber = formatNumber (nAmount.toFixed(2));
							return '<span class="rupeeSign">R  </span> <b>' + m_nIndianNumber +'</b>';
			        	}
					}
				]]
		}
	);
	clientTransaction_invoiceSubGridDetails ();
	clientTransaction_getAgeWiseData ();
}

function clientTransaction_getAgeWiseData ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oUserCredentialsData = new UserInformationData ();
	oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	oInvoiceData.m_nContactId = $("#clientTransaction_select_contacts").val();
	oInvoiceData.m_nSiteId =$("#clientTransaction_select_sites").val();	
	InvoiceDataProcessor.getAgeWiseInvoices(oInvoiceData, clientTransaction_listedAgeWise)
}

function clientTransaction_listedAgeWise (arrAgeWiseData)
{
	var nTotalInvoices = 0;
	var nTotalInvoiceAmount = 0;
	clearGridData ("#clientTransaction_table_ageWise");
	for(var nIndex = 0; nIndex < arrAgeWiseData.length; nIndex++)
	{
		var oInvoiceData = new InvoiceData ();
		oInvoiceData.m_strPeriod = arrAgeWiseData[nIndex][0];
		oInvoiceData.m_nInvoicesCount = arrAgeWiseData[nIndex][1];
		oInvoiceData.m_nAmount = arrAgeWiseData[nIndex][2];
		nTotalInvoices += arrAgeWiseData[nIndex][1];
		nTotalInvoiceAmount += arrAgeWiseData[nIndex][2];
		if(oInvoiceData.m_nInvoicesCount > 0)
			$('#clientTransaction_table_ageWise').datagrid('appendRow',oInvoiceData);
	}
	$('#clientTransaction_table_ageWise').datagrid('reloadFooter',[{m_strPeriod : '<b>Total :</b>', m_nInvoicesCount:nTotalInvoices, m_nAmount : nTotalInvoiceAmount}]);
	HideDialog("secondDialog");
}

function clientTransaction_invoiceSubGridDetails ()
{
	$('#clientTransaction_table_ageWise').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div style="padding:2px"><table class="clientTransaction_table_DetailViewDG"></table></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	        var  clientTransaction_table_DetailViewDG = $(this).datagrid('getRowDetail',index).find('table.clientTransaction_table_DetailViewDG');
	        clientTransaction_table_DetailViewDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,remoteSort:false, height:'auto',
	            columns:[[
	                      {field:'m_strDate',title:'Date',sortable:true,width:40},
	                      {field:'m_strInvoiceNumber',title:'InvoiceNo',sortable:true,width:80},
	                      {field:'m_nInvoiceAmount',title:'Invoice Amount',sortable:true,width:100,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nInvoiceAmount.toFixed(2);	
					        	}
	                      },
	                      {field:'m_nReceiptAmount',title:'Receipt Amount',width:100,sortable:true,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									 return row.m_nReceiptAmount.toFixed(2);	
					        	}
	                      },
	                      {field:'m_nBalanceAmount',title:'Balance Amount',width:100,sortable:true,align:'right',
	                    	  formatter:function(value,row,index)
					        	{
									return row.m_nBalanceAmount.toFixed(2);	
					        	}
	                      }
	            ]],
	            onResize:function()
	            {
	                $('#clientTransaction_table_ageWise').datagrid('fixDetailRowHeight',index);
	            }
	        });
	        clientTransaction_getAgeWiseInvoiceList (clientTransaction_table_DetailViewDG, index, row);
	    }
	});
}

function clientTransaction_getAgeWiseInvoiceList (clientTransaction_table_DetailViewDG, index, row)
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "clientTransaction_progressbarLoaded ()");
	clearGridData (clientTransaction_table_DetailViewDG);
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_oClientData = new ClientData ();
	oInvoiceData.m_oClientData.m_nClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	var arrDates = row.m_strPeriod.split("-");
	var strFromDate = arrDates.length > 1 ? getDateFromDays(-parseInt(arrDates[1])) : "";
	oInvoiceData.m_strFromDate = strFromDate;
	oInvoiceData.m_strToDate = getDateFromDays(-parseInt(arrDates[0]));
	InvoiceDataProcessor.list(oInvoiceData, "", "", "", "", function (oResponse)
	{
		var arrInvoiceData = oResponse.m_arrInvoice;
		for(var nIndex = 0; nIndex < arrInvoiceData.length; nIndex++)
		{
			var oInvoiceData = new InvoiceData ();
			oInvoiceData.m_strDate = arrInvoiceData[nIndex].m_strDate;
			oInvoiceData.m_strInvoiceNumber = arrInvoiceData[nIndex].m_strInvoiceNumber;
			oInvoiceData.m_nInvoiceAmount = arrInvoiceData[nIndex].m_nInvoiceAmount;
			oInvoiceData.m_nReceiptAmount = arrInvoiceData[nIndex].m_nReceiptAmount;
			oInvoiceData.m_nBalanceAmount = oInvoiceData.m_nInvoiceAmount - oInvoiceData.m_nReceiptAmount;
			if(Math.round(oInvoiceData.m_nBalanceAmount) > 0)
				clientTransaction_table_DetailViewDG.datagrid('appendRow',oInvoiceData);
		}
		$('#clientTransaction_table_ageWise').datagrid('fixDetailRowHeight',index);
		HideDialog("secondDialog");
	})
}
