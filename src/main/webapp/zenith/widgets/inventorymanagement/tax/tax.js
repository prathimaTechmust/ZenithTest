var tax_includeDataObjects = 
[
	'widgets/inventorymanagement/tax/TaxData.js'
];



 includeDataObjects (tax_includeDataObjects, "tax_loaded()");

function tax_MemberData ()
{
	this.m_strTaxName = "";
	this.m_strCurrentTaxName = "";
	this.m_nTaxId = -1;
}

var m_oTaxMemberData = new tax_MemberData ();

function tax_init()
{
	createPopup ("dialog", "#tax_button_create", "#tax_button_cancel", true);
	initFormValidateBoxes ("tax_form_Id");
}

function tax_new ()
{   
	tax_init();
}

function tax_edit()
{
	tax_init();
	document.getElementById("tax_button_create").setAttribute('update', true);
	document.getElementById("tax_button_create").innerHTML = "Update";
	var oTaxData = new TaxData ();
	oTaxData.m_nTaxId = m_oTaxMemberData.m_nTaxId;
	TaxDataProcessor.get(oTaxData,tax_gotData);
}

function tax_getFormData()
{
	var oTaxData = new TaxData();
	oTaxData.m_strTaxName = $("#tax_input_taxName").val();
	oTaxData.m_nPercentage = $("#tax_input_percentage").val();
	return oTaxData;
}

function tax_validate ()
{
	return validateForm("tax_form_Id");
}

function tax_submit()
{
	if (tax_validate())
	{
		var oTaxData = tax_getFormData ();
		if((document.getElementById("tax_button_create").getAttribute('update') == "false"))
			TaxDataProcessor.create(oTaxData, tax_created);
		else
		{
			oTaxData.m_nTaxId = m_oTaxList_MemberData.m_nTaxId;
			TaxDataProcessor.update(oTaxData, tax_updated);
		}
	}
}

function tax_created(oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("usermessage_tax_taxcreatedsuccessfully", "kSuccess");
		HideDialog("dialog");
		navigate("tax", "widgets/inventorymanagement/tax/taxList.js");
	}
	else if(oResponse.m_nErrorID == 1)
	{
		informUser ("usermessage_tax_creationfailed", "kError");
		tax_clear();
	}
}

function tax_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("usermessage_tax_taxupdatedsuccessfully", "kSuccess");
		document.getElementById("taxList_div_listDetail").innerHTML = "";
		clearGridData ("#taxList_table_taxListDG");
		taxList_list("", "", 1, 10);
	}
	HideDialog("dialog");
}

function tax_cancel()
{
	 HideDialog("dialog");
}

function tax_clear()
{
	document.getElementById("tax_input_taxName").value = "";
	document.getElementById("tax_input_percentage").value = "";
}

function tax_gotData(oResponse)
{
	var oTaxData = oResponse.m_arrTax[0];
	m_oTaxMemberData.m_strCurrentTaxName = oTaxData.m_strTaxName;
	$("#tax_input_taxName").val(oTaxData.m_strTaxName);
	$("#tax_input_percentage").val(oTaxData.m_nPercentage);
}
