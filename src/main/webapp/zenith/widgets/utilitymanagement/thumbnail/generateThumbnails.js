var generateThumbnails_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	 'widgets/inventorymanagement/invoice/InvoiceData.js'
];

 includeDataObjects (generateThumbnails_includeDataObjects, "generateThumbnails_loaded()");


function generateThumbnails_loaded ()
{
	loadPage ("utilitymanagement/thumbnail/generateThumbnails.html", "workarea", "generateThumbnails_init ()");
}

function generateThumbnails_init ()
{
	document.getElementById ("generateThumbnails_button_generate").style.visibility="visible";
	document.getElementById ("generateThumbnails_button_updateInvoiceTable").style.visibility="visible";
	document.getElementById ("generateThumbnails_button_updatePurchaseTable").style.visibility="visible";
}

function generateThumbnails_generate ()
{
	var oItemData = new ItemData ();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.generateThumbnails(oItemData, generateThumbnails_generated);
}

function generateThumbnails_generated (oResponse)
{
	if(oResponse.m_bSuccess)
		document.getElementById("generateThumbnails_div_loadMsg").innerHTML = "<table class=\"trademust\"><tr><td  class=\"trademust\"><center><b>Thumbnails generated successfully to " + oResponse.m_nRowCount + " items.</b></center></td><tr></table>";
}

function generateThumbnails_updateInvoiceTable ()
{
	InvoiceDataProcessor.updateInvoiceTable(generateThumbnails_updatedInvoiceTable);
}

function generateThumbnails_updatedInvoiceTable (oResponse)
{
	if(oResponse.m_bSuccess)
		document.getElementById("generateThumbnails_div_loadMsg").innerHTML = "<table class=\"trademust\"><tr><td  class=\"trademust\"><center><b>Invoice Table Updated successfully to " + oResponse.m_nRowCount + " rows.</b></center></td><tr></table>";
}

function generateThumbnails_updatePurchaseTable ()
{
	PurchaseDataProcessor.updatePurchaseTable(generateThumbnails_updatedPurchaseTable);
}

function generateThumbnails_updatedPurchaseTable (oResponse)
{
	if(oResponse.m_bSuccess)
		document.getElementById("generateThumbnails_div_loadMsg").innerHTML = "<table class=\"trademust\"><tr><td  class=\"trademust\"><center><b>Invoice Table Updated successfully to " + oResponse.m_nRowCount + " rows.</b></center></td><tr></table>";
}

function generateThumbnails_updateVendorItemTable ()
{
	loadPage ("include/process.html", "ProcessDialog", "generateThumbnails_processUpdateVendorItemTable ()");
}

function generateThumbnails_processUpdateVendorItemTable ()
{
	createPopup('ProcessDialog', '', '', true);
	OwnItemDataProcessor.updateVendorItemTable(generateThumbnails_updatedVendorItemTable);
}

function generateThumbnails_updatedVendorItemTable (oResponse)
{
	HideDialog("ProcessDialog");
	if(oResponse.m_bSuccess)
		document.getElementById("generateThumbnails_div_loadMsg").innerHTML = "<table class=\"trademust\"><tr><td  class=\"trademust\"><center><b>Invoice Table Updated successfully to " + oResponse.m_nRowCount + " rows.</b></center></td><tr></table>";
}