var importInvoiceDetails_includeDataObjects = 
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

includeDataObjects (importInvoiceDetails_includeDataObjects, "importInvoiceDetails_init()");

function importInvoiceDetails_memberData ()
{
	this.m_strFileExtention = "";
}
var m_oImportInvoiceDetailsMemberData = new importInvoiceDetails_memberData ();


function importInvoiceDetails_init ()
{
	loadPage ("inventorymanagement/invoice/importInvoiceDetails.html", "dialog", "importInvoiceDetails_loaded ()");
}

function importInvoiceDetails_loaded ()
{
	createPopup("dialog", "#importInvoiceDetails_button_import", "#importInvoiceDetails_button_cancel", true);
	importInvoiceDetails_populateProviderList ();
	initFormValidateBoxes ("importInvoiceDetails_form_id");
}

function importInvoiceDetails_cancel ()
{
	HideDialog ("dialog");
}

function importInvoiceDetails_populateProviderList ()
{
	dwr.engine.setAsync(false);
	var oProviders = new ExportImportProviderData ();
	ExportImportProviderDataProcessor.list(oProviders, "m_strDescription", "", 
			function (oResponse)
			{
				importInvoiceDetails_prepareProviderDD ("importInvoiceDetails_select_provider", oResponse);
			}			
	);
	dwr.engine.setAsync(true);
}

function importInvoiceDetails_prepareProviderDD (strProviderDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrExportImportProvider.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrExportImportProvider [nIndex].m_nId, oResponse.m_arrExportImportProvider [nIndex].m_strDescription));
	PopulateDD (strProviderDD, arrOptions);
}

function importInvoiceDetails_import ()
{
	var oInvoiceData = new InvoiceData ();
	if(importInvoiceDetails_validate ())
	{
		var oProviders = new ExportImportProviderData ();
		oInvoiceData.m_oUserCredentialsData = new UserInformationData ();
		oInvoiceData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oInvoiceData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oInvoiceData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		oProviders.m_nId = dwr.util.getValue ("importInvoiceDetails_select_provider");
		var strProvider = document.getElementById("importInvoiceDetails_select_provider");
		var strDesc = strProvider.options[strProvider.selectedIndex].text;
		strDesc = (strDesc == "Excel") ? "xls" : strDesc;
		var oFile = dwr.util.getValue ("importInvoiceDetails_input_importFile");
		if(strDesc.toLowerCase() == m_oImportInvoiceDetailsMemberData.m_strFileExtention)
			InvoiceDataProcessor.importInvoiceData(oInvoiceData, oProviders, oFile, importInvoiceDetails_importedFile);
		else 
		{
			informUser("Please Choose the file Correctly.", "kWarning");
		}
		
	}
}

function importInvoiceDetails_validate ()
{
	return validateForm("importInvoiceDetails_form_id")&& importInvoiceDetails_validateItemFile () ;
}

function importInvoiceDetails_validateItemFile ()
{
	var bIsFileValid = true;
	if( dwr.util.getValue ("importInvoiceDetails_td_fileName") == "" )
	{
		informUser("Please Select a File.", "kWarning");
		bIsFileValid = false;
	}
	return bIsFileValid;
}

function importInvoiceDetails_getFileName ()
{
	var oImportFile = dwr.util.getValue("importInvoiceDetails_input_importFile")
	document.getElementById("importInvoiceDetails_td_fileName").innerHTML = getImageName (oImportFile.value);
	m_oImportInvoiceDetailsMemberData.m_strFileExtention = oImportFile.value.replace(/^.*\./, '');
}

function importInvoiceDetails_importedFile (oResponse)
{
	var strException = "";
	var arrException = oResponse.m_arrException;
	var nSkippedCount = arrException.length;
	for (var nIndex = 0; nIndex < arrException.length; nIndex++)
		 strException += arrException[nIndex] + "\n";
	informUser("Import Details : " + "\n" 
			+ "Inserted - " + oResponse.m_nInsertedCount + "\n"
			+ "Duplicate - " + oResponse.m_nDuplicateCount + "\n" 
			+ "Skipped - " + nSkippedCount + "\n" 
			+ strException, "kAlert" );
	HideDialog ("dialog");
}
