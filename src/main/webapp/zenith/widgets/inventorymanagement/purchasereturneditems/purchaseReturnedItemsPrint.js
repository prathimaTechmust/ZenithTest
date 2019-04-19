navigate ('printPurchaseReturned','widgets/inventorymanagement/purchasereturneditems/printPurchaseReturnedItems.js');

function printPurchaseReturnedItems_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printPurchaseReturnedItems_printReturned ()");
}
