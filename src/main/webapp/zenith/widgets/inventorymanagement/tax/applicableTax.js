var applicableTax_includeDataObjects = 
[
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/inventorymanagement/tax/TaxData.js'
];



 includeDataObjects (applicableTax_includeDataObjects, "applicableTax_loaded ()");

function applicableTax_memberData ()
{
	this.m_arrTax = null;
	this.m_arrSelectedData = new Array ();
	this.m_strCurrentApplicableTaxName = "";
	this.m_nApplicableTaxId = -1;
}

var m_oApplicableTaxMemberData = new applicableTax_memberData ();

function applicableTax_new ()
{
	initFormValidateBoxes ("applicableTax_form_id");
	createPopup ("dialog", "#applicableTax_button_submit", "#applicableTax_button_cancel", true);
	$('#applicableTax_table_listofapplicabletaxDG').datagrid ({
	    columns:[[  
	        {field:'ckBox',checkbox:true,width:100},
	        {field:'m_strTaxName',title:'Tax Name',sortable:true,width:150},
	        {field:'m_nPercentage',title:'Percentage',sortable:true,width:150}
	    ]]
	});
	
	$('#applicableTax_table_listofapplicabletaxDG').datagrid (
			{
				onCheck: function (rowIndex, rowData)
				{
				m_oApplicableTaxMemberData.m_arrSelectedData = $('#applicableTax_table_listofapplicabletaxDG').datagrid('getChecked'); 
				applicableTax_hideErrorLabel ();
				}
			});
	
	$('#applicableTax_table_listofapplicabletaxDG').datagrid (
			{
				onUncheck: function (rowIndex, rowData)
				{
				m_oApplicableTaxMemberData.m_arrSelectedData = $('#applicableTax_table_listofapplicabletaxDG').datagrid('getChecked'); 
				applicableTax_hideErrorLabel ();
				}
			});
	$('#applicableTax_table_listofapplicabletaxDG').datagrid (
			{
				onCheckAll: function (rows)
				{
				m_oApplicableTaxMemberData.m_arrSelectedData = rows; 
				applicableTax_hideErrorLabel ();
				}
			});
	
	$('#applicableTax_table_listofapplicabletaxDG').datagrid (
			{
				onUncheckAll: function (rows)
				{
				m_oApplicableTaxMemberData.m_arrSelectedData = "";
				applicableTax_hideErrorLabel ();
				}
			});
	
	applicableTax_buildApplicableTaxList ();
}

function applicableTax_edit ()
{
	applicableTax_new ();
	document.getElementById("applicableTax_button_submit").setAttribute('update', true);
	document.getElementById("applicableTax_button_submit").innerHTML = "Update";
	var oApplicableTaxData = new ApplicableTaxData ();
	oApplicableTaxData.m_nId = m_oApplicableTaxMemberData.m_nApplicableTaxId;
	ApplicableTaxDataProcessor.get(oApplicableTaxData, applicableTax_gotData);
}

function applicableTax_getFormData ()
{
	var arrTax = $('#applicableTax_table_listofapplicabletaxDG').datagrid('getData');
	var oApplicableTaxData = new ApplicableTaxData ();
	oApplicableTaxData.m_strApplicableTaxName = $ ("#applicableTax_input_applicableTaxName").val();
	oApplicableTaxData.m_nId = -1;
	return oApplicableTaxData;
}

function applicableTax_validate ()
{
	var bIsValidate = validateForm ("applicableTax_form_id");
	if (applicableTax_validateDG () && bIsValidate)
		bIsValidate = true;
	else 
		bIsValidate = false;
	return bIsValidate;
}

function applicableTax_submit ()
{
	if (applicableTax_validate ())
	{
		var oApplicableTaxData = applicableTax_getFormData ();
		oApplicableTaxData.m_arrTax = applicableTax_getTaxDataArray (); 
		if((document.getElementById("applicableTax_button_submit").getAttribute('update') == "false"))
			ApplicableTaxDataProcessor.create(oApplicableTaxData, applicableTax_created);
		else
		{
			oApplicableTaxData.m_nId = m_oApplicableTaxListMemberData.m_nSelectedApplicableTaxId;
			ApplicableTaxDataProcessor.update(oApplicableTaxData, applicableTax_updated);
		}
	}
}

function applicableTax_cancel ()
{
    HideDialog("dialog");
}

function applicableTax_clear ()
{
	document.getElementById("applicableTax_input_applicableTaxName").value = "";
}

function applicableTax_created (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("usermessage_applicabletax_applicabletaxcreatedsuccessfully", "kSuccess");
		navigate ("applicabletaxlist", "widgets/inventorymanagement/tax/applicableTaxList.js");
		applicableTax_clear ();
	    HideDialog("dialog");
	}
	else if(oResponse.m_nErrorID == 1)
	{
		informUser("usermessage_applicabletax_Applicabletaxcreationfailed", "kError");
		applicableTax_clear ();
	}
}

function applicableTax_updated(oResponse)
{
	if(oResponse.m_bSuccess);
	{
		informUser("usermessage_applicabletax_applicabletaxupdatedsuccessfully", "kSuccess");
		navigate ("applicabletaxlist", "widgets/inventorymanagement/tax/applicableTaxList.js");
	}
		
	applicableTax_clear ();
    HideDialog("dialog");
}

function applicableTax_gotData (oApplicableTaxResponse)
{	
	m_oApplicableTaxMemberData.m_strCurrentApplicableTaxName = oApplicableTaxResponse.m_arrApplicableTax[0].m_strApplicableTaxName;
	$("#applicableTax_input_applicableTaxName").val(oApplicableTaxResponse.m_arrApplicableTax[0].m_strApplicableTaxName);
	applicableTax_selectTax (oApplicableTaxResponse.m_arrApplicableTax[0].m_oTaxes);
}

function applicableTax_buildApplicableTaxList ()
{
	var oApplicableTaxData = new ApplicableTaxData ();
	ApplicableTaxDataProcessor.listApplicableTaxData(oApplicableTaxData, applicableTax_gotList);
} 

function applicableTax_gotList (oApplicableTaxResponse)
{
	m_oApplicableTaxMemberData.m_arrTax = oApplicableTaxResponse.m_arrTax;
	$("#applicableTax_table_listofapplicabletaxDG").datagrid('loadData', oApplicableTaxResponse.m_arrTax);

}

function applicableTax_hideErrorLabel ()
{
	var hideLabel = document.getElementById("applicableTax_div_lblError");
	hideLabel.style.display = "none";
}

function applicableTax_validateDG ()
{
	var bIsValidateDG = true;
	if(m_oApplicableTaxMemberData.m_arrSelectedData == "")
	{
		bIsValidateDG = false;
		var addLabel = document.getElementById("applicableTax_div_lblError");
		addLabel.style.display = "block";
		addLabel.innerHTML = "<Label style='float: bottom;padding-left:.5em; color: red; vertical-align: top; font-size:14px'>Select list of Tax</Label>";
	}
	else
		applicableTax_hideErrorLabel ();
	return bIsValidateDG;
}

function applicableTax_getTaxDataArray ()
{
	var oTaxDataArray = new Array ();
	var arrSelectedTaxData = $('#applicableTax_table_listofapplicabletaxDG').datagrid('getChecked');
	for (var nIndex = 0; nIndex < arrSelectedTaxData.length; nIndex++)
	{
		var oTaxData = new TaxData ();
		oTaxData.m_nTaxId = arrSelectedTaxData [nIndex].m_nTaxId;
		oTaxData.m_strTaxName = arrSelectedTaxData [nIndex].m_strTaxName;
		oTaxData.m_nPercentage = arrSelectedTaxData [nIndex].m_nPercentage;
		oTaxDataArray.push (oTaxData);
	}
	return oTaxDataArray;
}

function applicableTax_selectTax (arrTaxToSelect)
{
	assert.isArray(arrTaxToSelect, "arrTaxToSelect expected to be an Array.");
	var arrTax = $('#applicableTax_table_listofapplicabletaxDG').datagrid('getData').rows;
	for (var nIndex= 0; nIndex < arrTax.length; nIndex++)
		if (applicableTax_isTaxSelected (arrTax[nIndex].m_nTaxId, arrTaxToSelect))
			$('#applicableTax_table_listofapplicabletaxDG').datagrid ('selectRow', nIndex);
}

function applicableTax_isTaxSelected (nTaxId, arrSelectedTax)
{
	assert.isArray(arrSelectedTax, "arrSelectedTax expected to be an Array.");
	assert.isNumber(nTaxId, "nTaxId expected to be a Number.");
	var bTaxSelected = false;
	for (var nIndex=0; nIndex < arrSelectedTax.length && !bTaxSelected; nIndex++)
		bTaxSelected = arrSelectedTax[nIndex].m_nTaxId == nTaxId;
	return bTaxSelected
}