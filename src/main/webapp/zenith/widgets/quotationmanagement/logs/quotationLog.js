var quotationLog_includeDataObjects = 
[
	'widgets/quotationmanagement/quotation/QuotationData.js',
	'widgets/quotationmanagement/logs/QuotationLogData.js'
];

 includeDataObjects (quotationLog_includeDataObjects, "quotationLog_loaded()");

function quotationLog_memberData ()
{
	this.m_nQuotationId = -1;
    this.m_nQuotationLogId = -1;
}

var m_oQuotationLogMemberData = new quotationLog_memberData ();

function quotationLog_new ()
{
	createPopup('ProcessDialog', '', '', true);
	createPopup("dialog", "#quotationLog_button_save", "#quotationLog_button_cancel", true);
	quotationLog_init ();
}

function quotationLog_init ()
{
	HideDialog ("ProcessDialog");
	initFormValidateBoxes ("quotationLog_form_id");
}

function quotationLog_validate ()
{
	return validateForm("quotationLog_form_id");
}

function quotationLog_getFormData ()
{
	var oQuotationLogData = new QuotationLogData ();
	oQuotationLogData.m_oQuotationData.m_nQuotationId = m_oQuotationLogMemberData.m_nQuotationId;
	oQuotationLogData.m_strComment = $("#quotationLog_textarea_comment").val();
	return oQuotationLogData;
}

function quotationLog_submit ()
{
	if (quotationLog_validate())
	{
		disable ("quotationLog_button_save");
		var oQuotationLogData = quotationLog_getFormData ();
		QuotationLogDataProcessor.create (oQuotationLogData, quotationLog_created);
	}
}

function quotationLog_created  (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Comment added successfully.", "kSuccess");
		HideDialog ("dialog");
		quotationList_listQuotationLogs (m_oQuotationListMemberData.m_nQuotationId, 1, 10);
	}
	else
	{
		informUser("Failed to add Logs.", "kError");
		enable ("quotationLog_button_save");
	}
}

function quotationLog_cancel ()
{
	HideDialog ("dialog");
}