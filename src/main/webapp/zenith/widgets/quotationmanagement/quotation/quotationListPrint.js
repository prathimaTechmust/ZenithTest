navigate ('printQuotations','widgets/quotationmanagement/quotation/printQuotations.js');

function printQuotations_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printQuotations_quotationPrint_list ()");
}