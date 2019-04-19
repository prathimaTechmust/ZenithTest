var exportClientList_includeDataObjects = 
[
 	'widgets/clientmanagement/ClientData.js',
 	'widgets/clientmanagement/DemographyData.js',
 	'widgets/clientmanagement/ContactData.js',
 	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

includeDataObjects (exportClientList_includeDataObjects, "exportClientList_loaded ()");

function exportClientList_loaded ()
{
	loadPage ("clientmanagement/exportClientList.html", "dialog", "exportClientList_init()");
}

function exportClientList_init ()
{
	createPopup ("dialog", "#exportClientList_button_export", "#exportClientList_button_cancel", true);
	exportClientList_populateProvidersList();
}

function exportClientList_populateProvidersList ()
{
	dwr.engine.setAsync(false);
	var oExportImportProviderData = new ExportImportProviderData();
	ExportImportProviderDataProcessor.list(oExportImportProviderData, "m_strDescription", "",
			function (oResponse)
			{
				exportClientList_prepareProvidersDD ("exportClientList_select_exportAs", oResponse);
			}				
		);
	dwr.engine.setAsync(true);
}

function exportClientList_prepareProvidersDD (strProvidersDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrExportImportProvider.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrExportImportProvider [nIndex].m_nId,
				oResponse.m_arrExportImportProvider[nIndex].m_strDescription));
	PopulateDD (strProvidersDD, arrOptions);
}

function exportClientList_export ()
{
	var oExportImportProviderData = new ExportImportProviderData();
	oExportImportProviderData.m_nId = dwr.util.getValue ("exportClientList_select_exportAs");
	exportClientList_exportClientData(oExportImportProviderData);
}

function exportClientList_exportClientData (oExportImportProviderData)
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oClientData = listClient_getFormData ();
	ClientDataProcessor.exportClientData (oClientData, oExportImportProviderData,
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

function exportClientList_cancel ()
{
	HideDialog ("dialog");
}
