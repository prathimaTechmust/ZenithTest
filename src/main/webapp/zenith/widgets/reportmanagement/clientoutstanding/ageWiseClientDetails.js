var ageWiseClientDetails_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/reportmanagement/clientoutstanding/ClientOutsatandingReortData.js',
	'widgets/reportmanagement/clientoutstanding/ClientInvoiceDetailsData.js'
];

 includeDataObjects (ageWiseClientDetails_includeDataObjects, "ageWiseClientDetails_loaded()");

function ageWiseClientDetails_loaded ()
{
	loadPage ("reportmanagement/clientoutstanding/ageWiseClientDetails.html", "ClientInfodialog", "ageWiseClientDetails_init ()");
}

function ageWiseClientDetails_init ()
{
	createPopup("ClientInfodialog", "#ageWiseClientDetails_button_cancel", "", true);
	initPanelWithoutSplitter ("#div_dataGrid", "#ageWiseClientDetails_table_clientDG");
	var oDataGrid = $("#ageWiseClientDetails_table_clientDG").datagrid();
	clientOutstandingReport_initClientDetailsDG (oDataGrid);
	ageWiseClientDetails_list();
}

function ageWiseClientDetails_list ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "clientOutstandingReport_progressbarLoaded ()");
	var oDataGrid = $("#ageWiseClientDetails_table_clientDG").datagrid();
	var oInvoiceData = clientOutstandingReport_getFormData ();
	oInvoiceData.m_bIsForClientOutstanding = true;
	var arrDates = m_oClientOutstandingMemberData.m_strSelectedPeriod.split("-");
	var strFromDate = arrDates.length > 1 ? getDateFromDays(-parseInt(arrDates[1])) : "";
	oInvoiceData.m_strFromDate = strFromDate;
	oInvoiceData.m_strToDate = getDateFromDays(-parseInt(arrDates[0]));
	InvoiceDataProcessor.list(oInvoiceData, "", "", {
		callback:function(oResponse) 
		{
			clientOutstandingReport_listed (oResponse, oDataGrid);
		}
	});
}

function ageWiseClientDetails_cancel ()
{
	HideDialog("ClientInfodialog");
	$('#clientOutstandingReport_table_clientOutstandingReportDG').datagrid('collapseRow',m_oClientOutstandingMemberData.m_nIndex);
}

function ageWiseClientDetails_print ()
{
	var arrReports = $('#ageWiseClientDetails_table_clientDG').datagrid('getRows');
	var arrClientData = clientOutstandingReport_getClientData (arrReports);
	 clientOutstandingReport_printClientList (arrClientData);
}