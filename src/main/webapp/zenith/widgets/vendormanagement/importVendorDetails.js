var importVendorDetails_includeDataObjects = 
[
 	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
 	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

 includeDataObjects (importVendorDetails_includeDataObjects, "importVendorDetails_loaded ()");

function importVendorDetails_memberData ()
{
	this.m_strFileExtention = "";
}
var m_oImportVendorDetailsMemberData = new importVendorDetails_memberData ();

function importVendorDetails_loaded ()
{
	loadPage ("vendormanagement/importVendorDetails.html", "dialog", "importVendorDetails_lodedVendorDetails ()");
}

function importVendorDetails_lodedVendorDetails ()
{
	createPopup ("dialog", "#importVendorDetails_button_import", "#importVendorDetails_button_cancel", true);
	importVendorDetails_populateProviderList ();
	initFormValidateBoxes ("importVendorDetails_form_id");
}

function importVendorDetails_populateProviderList ()
{
	dwr.engine.setAsync(false);
	var oProviders = new ExportImportProviderData ();
	ExportImportProviderDataProcessor.list(oProviders, "m_strDescription", "", 
			function (oResponse)
			{
				importVendorDetails_prepareProviderDD ("importVendorDetails_select_provider", oResponse);
			}			
	);
	dwr.engine.setAsync(true);
}

function importVendorDetails_prepareProviderDD (strProviderDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrExportImportProvider.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrExportImportProvider [nIndex].m_nId, oResponse.m_arrExportImportProvider [nIndex].m_strDescription));
	PopulateDD (strProviderDD, arrOptions);
}

function importVendorDetails_import ()
{
	var oVendorData = new VendorData ();
	if (importVendorDetails_validate())
	{
		var oProviders = new ExportImportProviderData ();
		oProviders.m_nId = dwr.util.getValue ("importVendorDetails_select_provider");
		var strProvider = document.getElementById("importVendorDetails_select_provider");
		var strDesc = strProvider.options[strProvider.selectedIndex].text;
		strDesc = (strDesc == "Excel") ? "xls" : strDesc;
		var oFile = dwr.util.getValue ("importVendorDetails_input_importFile");
		if(strDesc.toLowerCase() == m_oImportVendorDetailsMemberData.m_strFileExtention)
			VendorDataProcessor.importVendorData(oVendorData, oProviders, oFile, importVendorDetails_importedFile);
		else 
		{
			informUser("Please Choose the file Correctly.", "kWarning");
		}
	}
}

function importVendorDetails_importedFile (oResponse)
{
	if(oResponse)
	{
		informUser("Import Details : " + "\n" 
				+ "Inserted - " + oResponse.m_nInsertedCount + "\n"
				+ "Duplicate - " + oResponse.m_nDuplicateCount , "kAlert" );
		HideDialog ("dialog");
	}
}

function importVendorDetails_validate ()
{
	return validateForm("importVendorDetails_form_id")&& importVendorDetails_validateFile () ;
}

function importVendorDetails_validateFile ()
{
	var bIsFileValid = true;
	if(dwr.util.getValue ("importVendorDetails_td_fileName")== "")
	{
		informUser("Please Select File.", "kWarning");
		bIsFileValid = false;
	}
	return bIsFileValid;
}

function importVendorDetails_getFileName ()
{
	var oFileName = dwr.util.getValue("importVendorDetails_input_importFile");
	document.getElementById("importVendorDetails_td_fileName").innerHTML = getImageName (oFileName.value);
	m_oImportVendorDetailsMemberData.m_strFileExtention = oFileName.value.replace(/^.*\./, '');
}

function importVendorDetails_cancel ()
{
	HideDialog ("dialog");
}

