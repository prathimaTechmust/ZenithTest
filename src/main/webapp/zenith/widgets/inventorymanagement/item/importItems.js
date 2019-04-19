var importItems_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/item/ItemGroupData.js',
	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

includeDataObjects (importItems_includeDataObjects, "importItems_init()");

function importItems_memberData ()
{
	this.m_strFileExtention = "";
}

var m_oImportItemsMemberData = new importItems_memberData ();

function importItems_init ()
{
	loadPage ("inventorymanagement/item/importItems.html", "dialog", "importItems_loaded ()");
}

function importItems_loaded ()
{
	createPopup("dialog", "#importItems_button_import", "#importItems_button_cancel", true);
	importItems_populateProviderList ();
	initFormValidateBoxes ("importItems_form_id");
}

function importItems_cancel ()
{
	HideDialog ("dialog");
}

function importItems_populateProviderList ()
{
	dwr.engine.setAsync(false);
	var oProviders = new ExportImportProviderData ();
	ExportImportProviderDataProcessor.list(oProviders, "m_strDescription", "", 
			function (oResponse)
			{
				importItems_prepareProviderDD ("importFile_select_provider", oResponse);
			}			
	);
	dwr.engine.setAsync(true);
}

function importItems_prepareProviderDD (strProviderDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrExportImportProvider.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrExportImportProvider [nIndex].m_nId, oResponse.m_arrExportImportProvider [nIndex].m_strDescription));
	PopulateDD (strProviderDD, arrOptions);
}

function importItems_importFile ()
{
	var oItemData = new ItemData ();
	if(importItems_validate ())
	{
		var oProviders = new ExportImportProviderData ();
		oItemData.m_oUserCredentialsData = new UserInformationData ();
		oItemData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		oProviders.m_nId = dwr.util.getValue ("importFile_select_provider");
		var strProvider = document.getElementById("importFile_select_provider");
		var strDesc = strProvider.options[strProvider.selectedIndex].text;
		if(strDesc == "Excel")
			strDesc = "xls";
		var oFile = dwr.util.getValue ("importItems_input_importFile");
		if(strDesc.toLowerCase() == m_oImportItemsMemberData.m_strFileExtention)
			ItemDataProcessor.importItemData(oItemData, oProviders, oFile, importItems_importedFile);
		else 
		{
			informUser("Please Choose the file Correctly.", "kWarning");
		}
		
	}
}

function importItems_validate ()
{
	return validateForm("importItems_form_id")&& importItems_validateItemFile () ;
}

function importItems_validateItemFile ()
{
	var bIsFileValid = true;
	if( dwr.util.getValue ("importItems_td_fileName") == "" )
	{
		informUser("Please Select a File.", "kWarning");
		bIsFileValid = false;
	}
	return bIsFileValid;
}

function importItems_getFileName ()
{
	var oImportFile = dwr.util.getValue("importItems_input_importFile")
	document.getElementById("importItems_td_fileName").innerHTML = getImageName (oImportFile.value);
	m_oImportItemsMemberData.m_strFileExtention = oImportFile.value.replace(/^.*\./, '');
}

function importItems_importedFile (oResponse)
{
	if(oResponse)
	{
		informUser("Import Details : " + "\n" 
				+ "Inserted - " + oResponse.m_nInsertedCount + "\n"
				+ "Duplicate - " + oResponse.m_nDuplicateCount , "kAlert" );
		HideDialog ("dialog");
	}
}
