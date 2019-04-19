navigate ('Payment Report','widgets/reportmanagement/payment/paymentReport.js');

function paymentReport_loaded ()
{
	loadPage ("reportmanagement/payment/paymentReport.html", "workarea", "paymentReport_initOverallReport ()");
}