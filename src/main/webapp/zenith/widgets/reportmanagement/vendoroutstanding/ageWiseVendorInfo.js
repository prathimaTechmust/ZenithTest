var ageWiseVendorInfo_includeDataObjects = 
[
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/vendorpurchaseorder/VendorPurchaseOrderData.js',
	'widgets/vendorpurchaseorder/VendorPOLineItemData.js',
	'widgets/reportmanagement/vendoroutstanding/VendorOutstandingReportData.js'
];

 includeDataObjects (ageWiseVendorInfo_includeDataObjects, "ageWiseVendorInfo_loaded()");

function ageWiseVendorInfo_loaded ()
{
	loadPage ("reportmanagement/vendoroutstanding/ageWiseVendorInfo.html", "VendorInfodialog", "ageWiseVendorInfo_init ()");
}

function ageWiseVendorInfo_init ()
{
	createPopup("VendorInfodialog", "#ageWiseVendorInfo_button_cancel", "", true);
	initPanelWithoutSplitter ("#div_dataGrid", "#ageWiseVendorInfo_table_vendorDG");
	var oDataGrid = $("#ageWiseVendorInfo_table_vendorDG").datagrid();
	vendorOutstandingReport_initVendorDetailsDG (oDataGrid);
	ageWiseVendorInfo_list();
}

function ageWiseVendorInfo_list ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oDataGrid = $("#ageWiseVendorInfo_table_vendorDG").datagrid();
	var oPurchaseData = vendorOutstandingReport_getFormData ();
	oPurchaseData.m_bIsForVendorOutstanding = true;
	var arrDates = m_oVendorOutstandingReport_memberData.m_oSelectedPeriod.split("-");
	var strFromDate = arrDates.length > 1 ? getDateFromDays(-parseInt(arrDates[1])) : "";
	oPurchaseData.m_strFromDate = strFromDate;
	oPurchaseData.m_strToDate = getDateFromDays(-parseInt(arrDates[0]));
	PurchaseDataProcessor.list(oPurchaseData, "", "",{
		callback:function(oResponse) 
		{
			vendorOutstandingReport_gotVendorOutstandingList (oResponse, oDataGrid);
		}
	});
}

function ageWiseVendorInfo_cancel ()
{
	HideDialog("VendorInfodialog");
	$('#vendorOutstandingReport_table_vendorOutstandingReportDG').datagrid('collapseRow',m_oVendorOutstandingReport_memberData.m_nIndex);
}

function ageWiseVendorInfo_print ()
{
	var arrReports = $('#ageWiseVendorInfo_table_vendorDG').datagrid('getRows');
	var arrVendorOutstandingData = vendorOutstandingReport_getReportData (arrReports);
	vendorOutstandingReport_printVendorList (arrVendorOutstandingData);
}