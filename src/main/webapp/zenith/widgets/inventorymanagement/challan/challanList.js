var challanList_includeDataObjects = 
[
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/SiteData.js'
];

includeDataObjects (challanList_includeDataObjects, "challanList_loaded()");

function challanList_memberData ()
{
	this.m_nIndex = -1;
	this.m_strXMLData = "";
	this.m_nChallanId = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_nInvoiceId = -1;
}

var m_oChallanListMemberData = new challanList_memberData ();

function challanList_loaded ()
{
	loadPage ("inventorymanagement/challan/challanList.html", "workarea", "challanList_init ()");
}

function challanList_init ()
{
	$("#filterChallan_input_fromDate").datebox();
	$("#filterChallan_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filterChallan_input_toDate").datebox();
	$("#filterChallan_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	challanList_initializeDataGrid ();
	challanList_list (m_oChallanListMemberData.m_strSortColumn, m_oChallanListMemberData.m_strSortOrder, 1, 10);
}

function challanList_initializeDataGrid ()
{
	initHorizontalSplitter("#challanList_div_horizontalSplitter", "#challanList_table_challanListDG");
	$('#challanList_table_challanListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strTo',title:'To',sortable:true,width:250,
			  	styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}	
			  	},
				{field:'m_strChallanNumber',title:'Challan No.',sortable:true,width:120},
				{field:'m_strDate',title:'Date',sortable:true,width:80},
				{field:'Actions',title:'Action',width:40,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return challanList_displayImages (row, index);
		        	}
				},
			]]
		}
	);
	
	$('#challanList_table_challanListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				challanList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oChallanListMemberData.m_strSortColumn = strColumn;
				m_oChallanListMemberData.m_strSortOrder = strOrder;
				challanList_list (strColumn, strOrder, m_oChallanListMemberData.m_nPageNumber, m_oChallanListMemberData.m_nPageSize);
			}
		}
	)
	challanList_initDGPagination ();
}

function challanList_initDGPagination ()
{
	$('#challanList_table_challanListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oChallanListMemberData.m_nPageNumber = $('#challanList_table_challanListDG').datagrid('getPager').pagination('options').pageNumber;
				challanList_list (m_oChallanListMemberData.m_strSortColumn, m_oChallanListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("challanList_div_challanDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oChallanListMemberData.m_nPageNumber = $('#challanList_table_challanListDG').datagrid('getPager').pagination('options').pageNumber;
				m_oChallanListMemberData.m_nPageSize = $('#challanList_table_challanListDG').datagrid('getPager').pagination('options').pageSize;
				challanList_list (m_oChallanListMemberData.m_strSortColumn, m_oChallanListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("challanList_div_challanDetail").innerHTML = "";
			}
		}
	)
}

function challanList_filter ()
{
	challanList_list (m_oChallanListMemberData.m_strSortColumn, m_oChallanListMemberData.m_strSortOrder,1, 10);
}

function challanList_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	if(row.m_oInvoiceData != null && row.m_oInvoiceData.m_nInvoiceId > 0)
	{

		var oImage = '<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/print.jpg" width="20" align="center" id="editImageId" onClick=" challanList_printChallan ('+row.m_nChallanId+')"/> </td>'+
						'</tr>'+
					'</table>'
	}
	else
	{
		var oImage = '<table align="center">'+
						'<tr>'+
							'<td> <img title="Print" src="images/print.jpg" width="20" align="center" id="editImageId" onClick=" challanList_printChallan ('+row.m_nChallanId+')"/> </td>'+
							'<td> <img title="Make Invoice" src="images/invoice.jpg" width="20" align="center" id="invoiceImageId" onClick=" challanList_getUserConfirmation ('+row.m_nChallanId+')"/> </td>'+
						'</tr>'+
					'</table>'
	}
	return oImage;
}

function challanList_getFormData ()
{
	var oChallanData = new ChallanData ();
	oChallanData.m_oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oChallanData.m_oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oChallanData.m_oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oChallanData.m_oSalesData.m_strTo =$("#filterChallan_input_to").val();
	oChallanData.m_oSalesData.m_strChallanNumber =$("#filterChallan_input_challanNumber").val();
	oChallanData.m_strFromDate =FormatDate ($("#filterChallan_input_fromDate").datebox('getValue'));
	oChallanData.m_strToDate = FormatDate ($("#filterChallan_input_toDate").datebox('getValue'));
	return oChallanData;
}

function challanList_list (strColumn, strOrder, nPageNumber, nPageSize)
{ 
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oChallanListMemberData.m_strSortColumn = strColumn ;
	m_oChallanListMemberData.m_strSortOrder = strOrder;
	m_oChallanListMemberData.m_nPageNumber = nPageNumber;
	m_oChallanListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "challanList_progressbarLoaded ()");
}

function challanList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oChallanData = challanList_getFormData ();
	ChallanDataProcessor.list(oChallanData, m_oChallanListMemberData.m_strSortColumn, m_oChallanListMemberData.m_strSortOrder, m_oChallanListMemberData.m_nPageNumber, m_oChallanListMemberData.m_nPageSize, challanList_listed);
}

function challanList_listed (oResponse)
{
	clearGridData ("#challanList_table_challanListDG");
	var arrChallan = oResponse.m_arrChallan;
	for (var nIndex = 0; nIndex < arrChallan.length; nIndex++)
	{
		arrChallan[nIndex].m_strTo = arrChallan[nIndex].m_oSalesData.m_strTo;
		arrChallan[nIndex].m_strChallanNumber = arrChallan[nIndex].m_oSalesData.m_strChallanNumber;
		arrChallan[nIndex].m_strDate = arrChallan[nIndex].m_oSalesData.m_strDate;
		$('#challanList_table_challanListDG').datagrid('appendRow',arrChallan[nIndex]);
	}
	$('#challanList_table_challanListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oChallanListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function challanList_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oChallanListMemberData.m_nIndex = nIndex;
	document.getElementById("challanList_div_challanDetail").innerHTML = "";
	var oSalesData = new SalesData ();
	var oChallanData = new ChallanData ();
	m_oChallanListMemberData.m_oRowData = oRowData;
	m_oChallanListMemberData.m_nChallanId = oRowData.m_nChallanId
	m_oChallanListMemberData.m_nInvoiceId = oRowData.m_oInvoiceData != null ? oRowData.m_oInvoiceData.m_nInvoiceId : -1;
	oChallanData.m_nChallanId = oRowData.m_nChallanId;
	oSalesData.m_nId = oRowData.m_oSalesData.m_nId;
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oChallanData.m_oSalesData = oSalesData;
	ChallanDataProcessor.getXML (oChallanData, function (strXMLData)
		{
			m_oChallanListMemberData.m_strXMLData = strXMLData;
			populateXMLData (strXMLData, "inventorymanagement/challan/challanDetails.xslt", 'challanList_div_challanDetail');
			challanList_initializeDetailsDG ();
			ChallanDataProcessor.get (oChallanData, challanList_gotSalesLineItemData);
	});
}

function challanList_printChallan ()
{
	navigate ('print challan','widgets/inventorymanagement/challan/challanPrint.js');
}

function challanList_getUserConfirmation (nChallanId)
{
	assert.isNumber(nChallanId, "nChallanId expected to be a Number.");
	m_oChallanListMemberData.m_nChallanId = nChallanId;
	processConfirmation ('Yes', 'No', 'Do you want to make invoice ?', challanList_makeInvoice);
}

function challanList_makeInvoice (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if(bConfirm)
	{
		var oChallanData = new ChallanData ();
		oChallanData.m_nChallanId = m_oChallanListMemberData.m_nChallanId;
		var oCreatedby = new UserInformationData ();
		oCreatedby.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oCreatedby.m_nUID = m_oTrademustMemberData.m_nUID;
		ChallanDataProcessor.makeInvoice (oChallanData, oCreatedby, challanList_madeInvoice);
	}
}

function challanList_madeInvoice (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		m_oChallanListMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oChallanListMemberData.m_strEmailAddress = m_oChallanListMemberData.m_oRowData.m_oSalesData.m_oContactData != null ? m_oChallanListMemberData.m_oRowData.m_oSalesData.m_oContactData.m_strEmail : "";
		m_oChallanListMemberData.m_strSubject = "Invoice";
		navigate ('challan','widgets/inventorymanagement/challan/challanInvoice.js');
		challanList_list (m_oChallanListMemberData.m_strSortColumn, m_oChallanListMemberData.m_strSortOrder, 1, 10);
	}
	else
		informUser (" Invoice generation failed! ", "kSuccess");
}

function challanList_initializeDetailsDG ()
{
	$('#challandetails_table_salesDetailsDG').datagrid
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
				{field:'m_nTax',title:'Tax(%)',sortable:true,align:'right',width:50},
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

function challanList_gotSalesLineItemData (oResponse)
{
	clearGridData ("#challandetails_table_salesDetailsDG");
	var arrChallan = oResponse.m_arrChallan;
	var nTotal =0;
	nTotal += challanList_buildSalesLineItems (arrChallan[0].m_oSalesData);
	$('#challandetails_table_salesDetailsDG').datagrid('reloadFooter',[{m_nDiscount:'<b>Total</b>', m_nAmount: nTotal}]);
}

function challanList_buildSalesLineItems (arrSalesData)
{
	assert.isObject(arrSalesData, "arrSalesData expected to be an Object.");
	var nTotal = 0;
	var arrSalesLineItemData = arrSalesData .m_oSalesLineItems.concat(arrSalesData.m_oNonStockSalesLineItems);
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
		var nDiscountedPrice = (arrSalesLineItemData[nIndex].m_nPrice - arrSalesLineItemData[nIndex].m_nDiscountPrice);
		arrSalesLineItemData[nIndex].m_nTaxPrice = (nDiscountedPrice * (arrSalesLineItemData[nIndex].m_nTax/100));
		var nPrice = (Number(arrSalesLineItemData[nIndex].m_nPrice)- Number(arrSalesLineItemData[nIndex].m_nDiscountPrice) + Number(arrSalesLineItemData[nIndex].m_nTaxPrice) );
		arrSalesLineItemData[nIndex].m_nAmount = (nPrice * arrSalesLineItemData[nIndex].m_nQuantity);
		nTotal += arrSalesLineItemData[nIndex].m_nAmount;
		$('#challandetails_table_salesDetailsDG').datagrid('appendRow',arrSalesLineItemData[nIndex]);
	}
	return nTotal;
}

