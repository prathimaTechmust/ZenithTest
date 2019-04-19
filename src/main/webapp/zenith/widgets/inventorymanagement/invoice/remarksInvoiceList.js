navigate ('invoice','widgets/inventorymanagement/invoice/remarks.js');

function remarks_loaded ()
{
	m_oRemarksMemberData.m_oInvoiceData = m_oInvoiceListMemberData.m_oRowData;
	loadPage ("inventorymanagement/invoice/remarks.html", "thirdDialog", "remarks_init ()");
}

