navigate ('invoice Report','widgets/reportmanagement/invoice/invoiceReport.js');

function invoiceReport_loaded ()
{
	loadPage ("reportmanagement/invoice/invoiceReport.html", "workarea", "invoiceReport_initOverallReport ()");
}