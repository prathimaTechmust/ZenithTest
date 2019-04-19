var remarks_includeDataObjects = 
[
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
];

includeDataObjects (remarks_includeDataObjects, "remarks_loaded()");

function remarks_memberData ()
{
    this.m_oInvoiceData = new InvoiceData ();
}

var m_oRemarksMemberData = new remarks_memberData ();

function remarks_init ()
{
	createPopup("thirdDialog", "#remarks_button_save", "#item_button_cancel", true);
	initFormValidateBoxes ("remarks_form_id");
	remarks_loadData ();
}

function remarks_loadData ()
{
	var oInvoiceData = m_oRemarksMemberData.m_oInvoiceData;
	$("#remarks_input_invoiceTo").val(oInvoiceData.m_oClientData.m_strCompanyName);
	$("#remarks_input_invoiceNumber").val(oInvoiceData.m_strInvoiceNumber);
	$("#remarks_input_date").val(oInvoiceData.m_strDate);
	$("#remarks_textarea_remarks").val(oInvoiceData.m_strRemarks);
	$("#remarks_input_lRNumber").val(oInvoiceData.m_strLRNumber);
	$("#remarks_input_sugamNumber").val(oInvoiceData.m_strESugamNumber);
	initFormValidateBoxes ("remarks_form_id");
}

function remarks_submit ()
{
	if (remarks_validate())
	{
		oInvoiceData = remarks_getFormData ();
		InvoiceDataProcessor.updateRemarks (oInvoiceData, remarks_created);
	}
}

function remarks_validate ()
{
	return validateForm("remarks_form_id");
}

function remarks_getFormData ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_nInvoiceId = m_oRemarksMemberData.m_oInvoiceData.m_nInvoiceId;
	oInvoiceData.m_strRemarks = $("#remarks_textarea_remarks").val();
	oInvoiceData.m_strLRNumber = $("#remarks_input_lRNumber").val();
	oInvoiceData.m_strESugamNumber = $("#remarks_input_sugamNumber").val();
	return oInvoiceData;
}

function remarks_created  (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Remarks added successfully.", "kSuccess");
		HideDialog ("thirdDialog");
		try
		{
			document.getElementById("print_div_listDetail").innerHTML = "";
			populateXMLData (oResponse.m_strXMLData, "inventorymanagement/invoice/invoice.xslt", "print_div_listDetail");
		}
		catch(oException){}
	}
	else
		informUser("Remarks failed to add.", "kError");
}

function remarks_cancel ()
{
	HideDialog ("thirdDialog");
}
