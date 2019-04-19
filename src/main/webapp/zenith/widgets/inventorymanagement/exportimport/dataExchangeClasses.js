var dataExchangeClasses_includeDataObjects = 
[
	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

includeDataObjects (dataExchangeClasses_includeDataObjects, "dataExchangeClasses_loaded()");

function dataExchangeClasses_loaded ()
{
	loadPage ("inventorymanagement/exportimport/dataExchangeClasses.html", "exportImportProviders_div_dialog", "dataExchangeClasses_init ()");
}

function dataExchangeClasses_init ()
{
	createPopup("exportImportProviders_div_dialog", "#dataExchangeClasses_button_add", "#dataExchangeClasses_button_cancel", true);
	initFormValidateBoxes ("dataExchangeClasses_form_Id");
}

function dataExchangeClasses_add ()
{
	if(dataExchangeClasses_validate ())
	{
		var arrClasses = new Array ();
		var oDataExchangeClassesData = new DataExchangeClassesData ();
		oDataExchangeClassesData.m_strRegisteredClassName = dwr.util.getValue ("dataExchangeClasses_input_className");
		arrClasses.push(oDataExchangeClassesData)
		$('#exportImportProviders_table_classesDG').datagrid('appendRow',arrClasses[0]);
		HideDialog ("exportImportProviders_div_dialog");
	}
}

function dataExchangeClasses_validate ()
{
	return validateForm("dataExchangeClasses_form_Id");
}

function dataExchangeClasses_cancel ()
{
	HideDialog ("exportImportProviders_div_dialog");
}