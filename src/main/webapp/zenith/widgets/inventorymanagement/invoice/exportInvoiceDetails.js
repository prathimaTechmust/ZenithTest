var exportInvoiceDetails_includeDataObjects = 
[
 	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/InvoiceReceiptData.js',
	'widgets/inventorymanagement/paymentsandreceipt/ReceiptData.js',
 	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

includeDataObjects (exportInvoiceDetails_includeDataObjects, "exportInvoiceDetails_loaded ()");

function exportInvoiceDetails_loaded ()
{
	loadPage ("inventorymanagement/invoice/exportInvoiceDetails.html", "dialog", "exportInvoiceDetails_init()");
}

function exportInvoiceDetails_init ()
{
	createPopup ("dialog", "#exportInvoiceDetails_button_export", "#exportInvoiceDetails_button_cancel", true);
	exportInvoiceDetails_populateProvidersList();
}

function exportInvoiceDetails_populateProvidersList ()
{
	dwr.engine.setAsync(false);
	var oExportImportProviderData = new ExportImportProviderData();
	ExportImportProviderDataProcessor.list(oExportImportProviderData, "m_strDescription", "",
			function (oResponse)
			{
				exportClientLis_prepareProvidersDD ("exportInvoiceDetails_select_exportAs", oResponse);
			}				
		);
	dwr.engine.setAsync(true);
}

function exportClientLis_prepareProvidersDD (strProvidersDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrExportImportProvider.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrExportImportProvider [nIndex].m_nId,
				oResponse.m_arrExportImportProvider[nIndex].m_strDescription));
	PopulateDD (strProvidersDD, arrOptions);
}

function exportInvoiceDetails_export ()
{
	var oExportImportProviderData = new ExportImportProviderData();
	oExportImportProviderData.m_nId = dwr.util.getValue ("exportInvoiceDetails_select_exportAs");
	exportInvoiceDetails_exportInvoiceList(oExportImportProviderData);
}

function exportInvoiceDetails_exportInvoiceList (oExportImportProviderData)
{	
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oInvoiceData = invoiceList_getFormData ();
	InvoiceDataProcessor.exportInvoiceData (oInvoiceData, oExportImportProviderData,
			{ 
		        callback: function(oResponse) 
		        { 
					HideDialog ("dialog");
					dwr.engine.openInDownload(oResponse);
		        }, 
		        errorHandler: function(strErrMsg, oException) 
		        { 
		        	HideDialog ("dialog");
		            displayErrorMessage(strErrMsg); 
		        } 
			});
}

function exportInvoiceDetails_cancel ()
{
	HideDialog ("dialog");
}
