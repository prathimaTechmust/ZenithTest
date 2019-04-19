var exportVendortList_includeDataObjects = 
[
 	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
 	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

 includeDataObjects (exportVendortList_includeDataObjects, "exportVendortList_loaded ()");

function exportVendortList_loaded ()
{
	loadPage ("vendormanagement/exportVendorDetails.html", "dialog", "exportVendortList_init()");
}

function exportVendortList_init ()
{
	createPopup ("dialog", "#exportVendorDetails_button_export", "#exportVendorDetails_button_cancel", true);
	exportVendortList_populateProvidersList();
}

function exportVendortList_populateProvidersList ()
{
	dwr.engine.setAsync(false);
	var oExportImportProviderData = new ExportImportProviderData();
	ExportImportProviderDataProcessor.list(oExportImportProviderData, "m_strDescription", "",
			function (oResponse)
			{
				exportVendortList_prepareProvidersDD ("exportVendorDetails_select_exportAs", oResponse);
			}				
		);
	dwr.engine.setAsync(true);
}

function exportVendortList_prepareProvidersDD (strProvidersDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrExportImportProvider.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrExportImportProvider [nIndex].m_nId,
				oResponse.m_arrExportImportProvider[nIndex].m_strDescription));
	PopulateDD (strProvidersDD, arrOptions);
}

function exportVendorDetails_export ()
{
	var oExportImportProviderData = new ExportImportProviderData();
	oExportImportProviderData.m_nId = dwr.util.getValue ("exportVendorDetails_select_exportAs");
	exportVendorDetails_exportVendorData(oExportImportProviderData);
}

function exportVendorDetails_exportVendorData (oExportImportProviderData)
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oVendorData = listVendor_getFormData ();
	VendorDataProcessor.exportVendorData (oVendorData, oExportImportProviderData,
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

function exportVendorDetails_cancel ()
{
	HideDialog ("dialog");
}
