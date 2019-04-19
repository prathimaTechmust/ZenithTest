navigate ("ExportImportProviders", "widgets/inventorymanagement/exportimport/exportImportProviders.js");

function exportImportProviders_loaded ()
{
	m_oExportImportProvidersMemberData.m_nId = m_oExportImportProviderListMemberData.m_nId;
	loadPage ("inventorymanagement/exportimport/exportImportProviders.html", "dialog", "exportImportProviders_edit ()");
}