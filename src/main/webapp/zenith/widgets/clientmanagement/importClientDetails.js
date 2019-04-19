var importClientDetails_includeDataObjects = 
[
 	'widgets/clientmanagement/ClientData.js',
 	'widgets/clientmanagement/DemographyData.js',
 	'widgets/clientmanagement/ContactData.js',
 	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

includeDataObjects (importClientDetails_includeDataObjects, "importClientDetails_loaded ()");

function importClientDetails_memberData ()
{
	this.m_strFileExtention = "";
}
var m_oImportClientDetailMemberData = new importClientDetails_memberData ();

function importClientDetails_loaded ()
{
	loadPage ("clientmanagement/importClientDetails.html", "dialog", "importClientDetails_lodedClientDetails ()");
}

function importClientDetails_lodedClientDetails ()
{
	createPopup ("dialog", "#importClientDetails_button_import", "#importClientDetails_button_cancel", true);
	importClientDetails_populateProviderList ();
	initFormValidateBoxes ("importClientDetails_form_id");
}

function importClientDetails_populateProviderList ()
{
	dwr.engine.setAsync(false);
	var oProviders = new ExportImportProviderData ();
	ExportImportProviderDataProcessor.list(oProviders, "m_strDescription", "", 
			function (oResponse)
			{
				importClientDetails_prepareProviderDD ("importClientDetails_select_provider", oResponse);
			}			
	);
	dwr.engine.setAsync(true);
}

function importClientDetails_prepareProviderDD (strProviderDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrExportImportProvider.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrExportImportProvider [nIndex].m_nId, oResponse.m_arrExportImportProvider [nIndex].m_strDescription));
	PopulateDD (strProviderDD, arrOptions);
}

function importClientDetails_import ()
{
	var oClientData = new ClientData ();
	if (importClientDetails_validate())
	{
		var oProviders = new ExportImportProviderData ();
		oProviders.m_nId = dwr.util.getValue ("importClientDetails_select_provider");
		var strProvider = document.getElementById("importClientDetails_select_provider");
		var strDesc = strProvider.options[strProvider.selectedIndex].text;
		strDesc = (strDesc == "Excel") ? "xls" : strDesc;
		var oFile = dwr.util.getValue ("importClientDetails_input_importFile");
		if(strDesc.toLowerCase() == m_oImportClientDetailMemberData.m_strFileExtention)
			ClientDataProcessor.importClientData(oClientData, oProviders, oFile, importClientDetails_importedFile);
		else 
		{
			informUser("Please Choose the file Correctly.", "kWarning");
		}
			
	}
}

function importClientDetails_importedFile (oResponse)
{
	if(oResponse)
	{
		informUser("Import Details : " + "\n" 
				+ "Inserted - " + oResponse.m_nInsertedCount + "\n"
				+ "Duplicate - " + oResponse.m_nDuplicateCount , "kAlert" );
		HideDialog ("dialog");
	}
}

function importClientDetails_validate ()
{
	return validateForm("importClientDetails_form_id")&& importClientDetails_validateFile () ;
}

function importClientDetails_validateFile ()
{
	var bIsFileValid = true;
	if(dwr.util.getValue ("importClientDetails_td_fileName")== "")
	{
		informUser("Please Select File.", "kWarning");
		bIsFileValid = false;
	}
	return bIsFileValid;
}

function importClientDetails_getFileName ()
{
	var oFileName = dwr.util.getValue("importClientDetails_input_importFile");
	document.getElementById("importClientDetails_td_fileName").innerHTML = getImageName (oFileName.value);
	m_oImportClientDetailMemberData.m_strFileExtention = oFileName.value.replace(/^.*\./, '');
}

function importClientDetails_cancel ()
{
	HideDialog ("dialog");
}

