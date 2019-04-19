var localPurchaseList_includeDataObjects = 
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
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderStockLineItemData.js',
	'widgets/inventorymanagement/sales/NonStockSalesLineItemData.js',
	'widgets/inventorymanagement/localpurchase/LocalPurchaseData.js'
];


includeDataObjects (localPurchaseList_includeDataObjects, "localPurchaseList_loaded()");

function localPurchaseList_memberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_strXMLData = "";
    this.m_strXMLDataForInfo = "";
    this.m_oSelectedRowData = null;
}

var m_oLocalPurchaseListMemberData = new localPurchaseList_memberData ();

function localPurchaseList_loaded ()
{
	loadPage ("inventorymanagement/localpurchase/localPurchaseList.html", "workarea", "localPurchaseList_init ()");
}

function localPurchaseList_init ()
{
	localPurchaseList_createDataGrid ();
}

function localPurchaseList_createDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#localPurchaseList_table_localPurchaseListDG");
	$('#localPurchaseList_table_localPurchaseListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
					{field:'m_strArticleDescription',title:'Item Name',sortable:true,width:350},
					{field:'m_nQty',title:'Order Quantity',sortable:true,width:150,align:'right',
						formatter:function(value,row,index)
			        	{
			        		return (row.m_oPOLineItem.m_nQty).toFixed(2);
			        	}
					},
					{field:'m_nShippedQty',title:'Shipped Quantity',sortable:true,width:150,align:'right',
						formatter:function(value,row,index)
			        	{
			        		return (row.m_oPOLineItem.m_nShippedQty).toFixed(2);
			        	}
					},
					{field:'m_nBalance',title:'Balance Quantity',width:150,align:'right',
						formatter:function(value,row,index)
			        	{
			        		return (row.m_oPOLineItem.m_nQty - row.m_oPOLineItem.m_nShippedQty).toFixed(2);
			        	}
					},
					{field:'Actions',title:'Action',width:60,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return localPurchaseList_displayImages (row, index);
			        	}
					}
				]],
				onSelect: function (rowIndex, rowData)
				{
					localPurchaseList_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					m_oLocalPurchaseListMemberData.m_strSortColumn = strColumn;
					m_oLocalPurchaseListMemberData.m_strSortOrder = strOrder;
					localPurchaseList_list (strColumn, strOrder, m_oLocalPurchaseListMemberData.m_nPageNumber, m_oLocalPurchaseListMemberData.m_nPageSize);
				}
	});
	localPurchaseList_initDGPagination ();
	localPurchaseList_list (m_oLocalPurchaseListMemberData.m_strSortColumn,m_oLocalPurchaseListMemberData.m_strSortOrder,1, 20);
}

function localPurchaseList_filter ()
{
	localPurchaseList_list (m_oLocalPurchaseListMemberData.m_strSortColumn,m_oLocalPurchaseListMemberData.m_strSortOrder,1, 20);
}
		
function localPurchaseList_initDGPagination ()
{
	$('#localPurchaseList_table_localPurchaseListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oLocalPurchaseListMemberData.m_nPageNumber = nPageNumber;
				localPurchaseList_list(m_oLocalPurchaseListMemberData.m_strSortColumn, m_oLocalPurchaseListMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oLocalPurchaseListMemberData.m_nPageNumber = nPageNumber;
				m_oLocalPurchaseListMemberData.m_nPageSize = nPageSize;
				localPurchaseList_list (m_oLocalPurchaseListMemberData.m_strSortColumn, m_oLocalPurchaseListMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function localPurchaseList_displayImages ()
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="localPurchaseList_getPOInfo ()"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}

function localPurchaseList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oLocalPurchaseListMemberData.m_strSortColumn = strColumn;
	m_oLocalPurchaseListMemberData.m_strSortOrder = strOrder;
	m_oLocalPurchaseListMemberData.m_nPageNumber = nPageNumber;
	m_oLocalPurchaseListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "localPurchaseList_progressbarLoaded ()");
}
function localPurchaseList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oNonStockSalesLineItemData =  new NonStockSalesLineItemData  ();
	oNonStockSalesLineItemData.m_strArticleDescription = $("#filterlocalPurchaseList_input_itemname").val();
	oNonStockSalesLineItemData.m_bIsForLocalPurchaseList = true;
	NonStockSalesLineItemDataProcessor.list(oNonStockSalesLineItemData, m_oLocalPurchaseListMemberData.m_strSortColumn,m_oLocalPurchaseListMemberData.m_strSortOrder, m_oLocalPurchaseListMemberData.m_nPageNumber, m_oLocalPurchaseListMemberData.m_nPageSize, localPurchaseList_listed);
}

function localPurchaseList_listed (oResponse)
{
	$('#localPurchaseList_table_localPurchaseListDG').datagrid('loadData',oResponse.m_arrNonStockSalesLineItems);
	$('#localPurchaseList_table_localPurchaseListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oLocalPurchaseListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function localPurchaseList_getPOInfo ()
{
	navigate ('purchaseOrderInfo','widgets/inventorymanagement/localpurchase/localPurchaseListInfo.js');
}

function localPurchaseList_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oLocalPurchaseListMemberData.m_nIndex = nIndex;
	var oPurchaseOrderData = new PurchaseOrderData ();
	oPurchaseOrderData.m_nPurchaseOrderId = oRowData.m_oSalesData.m_oPOData.m_nPurchaseOrderId;
	oPurchaseOrderData.m_oUserCredentialsData = new UserInformationData ();
	oPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	m_oLocalPurchaseListMemberData.m_oSelectedRowData = oPurchaseOrderData;
	PurchaseOrderDataProcessor.getXML (oPurchaseOrderData,	
	 function (strXMLData)
		{
			m_oLocalPurchaseListMemberData.m_strXMLDataForInfo = strXMLData;
		}
	);
}

function localPurchaseList_printLocalPurchaseList ()
{
	var arrLocalPurchaseData = localPurchaseList_getData ();
	var xmlDoc = loadXMLDoc("include/default.xml");
	addChild(xmlDoc, "root", "m_strItemNameFilterBox",$("#filterlocalPurchaseList_input_itemname").val());
	strXML = generateXML (xmlDoc, arrLocalPurchaseData, "root", "LocalPurchaseListData");
	m_oLocalPurchaseListMemberData.m_strXMLData = strXML;
	navigate ('reportPrint','widgets/inventorymanagement/localpurchase/localPurchaseListPrint.js');
}

function localPurchaseList_getData ()
{
	var arrLocalPurchase = $('#localPurchaseList_table_localPurchaseListDG').datagrid('getRows');
	var arrLocalPurchaseData = new Array ();
	for (var nIndex = 0; nIndex < arrLocalPurchase.length; nIndex++)
	{
		var oLocalPurchaseData = new LocalPurchaseData ();
		oLocalPurchaseData.m_strArticleDescription = arrLocalPurchase [nIndex].m_strArticleDescription;
		oLocalPurchaseData.m_nQty = arrLocalPurchase [nIndex].m_oPOLineItem.m_nQty;
		oLocalPurchaseData.m_nShippedQty = arrLocalPurchase [nIndex].m_oPOLineItem.m_nShippedQty;
		oLocalPurchaseData.m_nBalance =  arrLocalPurchase [nIndex].m_oPOLineItem.m_nQty - arrLocalPurchase [nIndex].m_oPOLineItem.m_nShippedQty;
		arrLocalPurchaseData.push (oLocalPurchaseData);
	}
	return arrLocalPurchaseData;
}