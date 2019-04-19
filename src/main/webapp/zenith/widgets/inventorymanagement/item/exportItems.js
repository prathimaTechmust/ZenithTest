var exportItems_includeDataObjects = 
[
 	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
 	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

includeDataObjects (exportItems_includeDataObjects, "exportItems_loaded ()");

function exportItems_loaded ()
{
	loadPage ("inventorymanagement/item/exportItems.html", "dialog", "exportItems_init()");
}

function exportItems_init ()
{
	createPopup ("dialog", "#exportItems_button_export", "#exportItems_button_cancel", true);
	exportItems_populateProvidersList();
}

function exportItems_export ()
{
	var oExportImportProviderData = new ExportImportProviderData();
	oExportImportProviderData.m_nId = dwr.util.getValue ("exportItems_select_exportAs");
	exportItems_exportItemList(oExportImportProviderData);
}

function exportItems_cancel ()
{
	HideDialog ("dialog");
}

function exportItems_populateProvidersList ()
{
	dwr.engine.setAsync(false);
	var oExportImportProviderData = new ExportImportProviderData();
	ExportImportProviderDataProcessor.list(oExportImportProviderData, "m_strDescription", "",
			function (oResponse)
			{
				exportItems_prepareProvidersDD ("exportItems_select_exportAs", oResponse);
			}				
		);
	dwr.engine.setAsync(true);
}

function exportItems_prepareProvidersDD (strProvidersDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrExportImportProvider.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrExportImportProvider [nIndex].m_nId,
				oResponse.m_arrExportImportProvider[nIndex].m_strDescription));
	PopulateDD (strProvidersDD, arrOptions);
}

function exportItems_exportItemList (oExportImportProviderData)
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oItemData = itemList_getFormData ();
	ItemDataProcessor.exportItemData (oItemData, oExportImportProviderData,
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
