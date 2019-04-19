var transactionMode_includeDataObjects = 
[
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js'
];

includeDataObjects (transactionMode_includeDataObjects, "transactionMode_loaded ()");

function transactionMode_MemberData ()
{
	this.m_nSelectedTransactionModeId = -1;
	this.m_oTransactionModeData = new TransactionModeData ();
}

var m_oTransactionModeMemberData = new transactionMode_MemberData ();

function transactionMode_init ()
{
	initFormValidateBoxes ("transactionMode_form_id");
}

function transactionMode_new ()
{
	createPopup("dialog", "#transactionMode_button_create", "#transactionMode_cancel", true);
	transactionMode_init ();
}

function transactionMode_edit ()
{
	createPopup ("dialog", "#transactionMode_button_create", "#transactionMode_cancel", true);
	transactionMode_init ();
	document.getElementById("transactionMode_button_create").setAttribute('update', true);
	document.getElementById("transactionMode_button_create").innerHTML = "Update";
	var oTransactionModeData = new TransactionModeData ();
	oTransactionModeData.m_nModeId = m_oTransactionModeMemberData.m_nSelectedTransactionModeId;
	TransactionModeDataProcessor.get(oTransactionModeData, transactionMode_gotData);
}

function transactionMode_gotData (oResponse)
{
	m_oTransactionModeMemberData.m_oTransactionModeData = oResponse.m_arrTransactionMode[0];
	$("#transactionMode_input_modeName").val(oResponse.m_arrTransactionMode[0].m_strModeName);
	transactionMode_init ();
}

function transactionMode_cancel ()
{
	HideDialog ("dialog");
}

function transactionMode_validate ()
{
	return validateForm ("transactionMode_form_id")&& transcationMode_checkMode();
}

function transactionMode_getFormData ()
{
	var oTransactionModeData = new TransactionModeData ();
	oTransactionModeData.m_strModeName = $("#transactionMode_input_modeName").val();
	return oTransactionModeData;
}

function transactionMode_create ()
{
	if (transactionMode_validate ())
	{
		loadPage ("include/process.html", "ProcessDialog", "transactionMode_progressbarLoaded ()");
	}
}

function transactionMode_progressbarLoaded ()
{
	var oTransactionModeData = transactionMode_getFormData ();
	if(document.getElementById("transactionMode_button_create").getAttribute('update') == "false")
		TransactionModeDataProcessor.create(oTransactionModeData, transactionMode_created);
	else
	{
		oTransactionModeData.m_nModeId = m_oTransactionModeMemberData.m_nSelectedTransactionModeId;
		TransactionModeDataProcessor.update(oTransactionModeData, transactionMode_updated);
	}
}

function transactionMode_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		informUser ("Transaction Mode Created Successfully", "kSuccess");
		navigate("Transaction Mode list", "widgets/inventorymanagement/paymentsandreceipt/transactionModeList.js");
	}
	else
		informUser ("Transaction Mode Creation Failed!", "kError");
}

function transactionMode_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		HideDialog("dialog");
		informUser ("Transaction Mode Updated Successfully", "kSuccess");
		navigate("Transaction Mode list", "widgets/inventorymanagement/paymentsandreceipt/transactionModeList.js");
	}
	else
		informUser ("Transaction Mode Updation Failed!", "kError");
}


function transcationMode_checkMode ()
{
	var strModeName = $("#transactionMode_input_modeName").val();
	if (strModeName.trim() != m_oTransactionModeMemberData.m_oTransactionModeData.m_strModeName)
	{
		var oTransactionModeData = new TransactionModeData ();
		oTransactionModeData.m_strModeName = strModeName;
		TransactionModeDataProcessor.list(oTransactionModeData, "", "", function (oResponse)
				{
					if (oResponse.m_arrTransactionMode.length > 0)
					{
						informUser ("Transaction Mode Name Already Exist", "kWarning");
						$("#transactionMode_input_modeName", "").val();
						document.getElementById("transactionMode_input_modeName").focus();
					}
					else
						transactionMode_progressbarLoaded ()
				});
	}
}
