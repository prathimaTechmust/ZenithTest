var quotationAttachment_includeDataObjects = 
[
	'widgets/quotationmanagement/quotation/QuotationAttachmentData.js'
];

 includeDataObjects (quotationAttachment_includeDataObjects, "quotationAttachment_loaded()");

function quotationAttachment_memberData ()
{
	this.m_nAttachmentId = -1;
	this.m_oFile = null;
}

var m_oQuotationAttachmentMemberData = new quotationAttachment_memberData ();

function quotationAttachment_new ()
{
	createPopup("secondDialog", "#quotationAttachment_button_submit", "#quotationAttachment_button_cancel", true);
	initFormValidateBoxes ("quotationAttachment_form_id");
}

function quotationAttachment_submit ()
{
	var oQuotationAttachmentData = quotationAttachment_getFormData ();
	quotationAttachment_insertDataToAttachmentDG (oQuotationAttachmentData);
}

function quotationAttachment_getFormData ()
{
	var oQuotationAttachmentData = new QuotationAttachmentData ();
	oQuotationAttachmentData.m_strFileName = $("#quotationAttachment_input_fileName").val();
	oQuotationAttachmentData.m_strFileName = getImageName(oQuotationAttachmentData.m_strFileName.value);
	oQuotationAttachmentData.m_oFile = m_oQuotationAttachmentMemberData.m_oFile;
	return oQuotationAttachmentData;
}

function quotationAttachment_insertDataToAttachmentDG (oQuotationAttachmentData)
{
	assert.isObject(oQuotationAttachmentData, "oQuotationAttachmentData expected to be an Object.");
	if(quotationAttachment_validate() &&  !quotationAttachment_isAttachmentExists (oQuotationAttachmentData))
	{
		HideDialog ("secondDialog");
		$('#quotation_table_quotationAttachmentsDG').datagrid('appendRow', oQuotationAttachmentData);
	}
}

function quotationAttachment_validate ()
{
	return validateForm("quotationAttachment_form_id");
}

function quotationAttachment_cancel ()
{
	HideDialog("secondDialog");
}

function quotationAttachment_getAttachment ()
{
	m_oQuotationAttachmentMemberData.m_oFile = $("#quotationAttachment_input_fileName").val();
	validateQuotation($("#quotationAttachment_input_fileName")[0].files);
}

function quotationAttachment_isAttachmentExists (oQuotationAttachmentData)
{
	assert.isObject(oQuotationAttachmentData, "oQuotationAttachmentData expected to be an Object.");
	assert( Object.keys(oQuotationAttachmentData).length >0 , "oQuotationAttachmentData cannot be an empty .");// checks for non emptyness 
	var bIsExists = false;
	var arrAttachmentData = $('#quotation_table_quotationAttachmentsDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrAttachmentData.length; nIndex++)
	{
		if(arrAttachmentData [nIndex].m_strFileName == oQuotationAttachmentData.m_strFileName)
		{
			bIsExists = true;
			document.getElementById("quotationAttachment_td_fileName").innerHTML = "This file is already added";
			break;
		}
	}
	return bIsExists;
}
function validateQuotation(file) 
{
    var file =$('#quotationAttachment_input_fileName');
    var sFileName = file[0].files;
    var sFileName = sFileName[0].name;
    var sFileExtension = sFileName.split('.')[sFileName.split('.').length - 1].toLowerCase();
    var iFileSize = file[0].size;
    var iConvert = ((file[0].size / 10485760).toFixed(10))*10000000;
    if (!(sFileExtension === "pdf" || sFileExtension === "doc" || sFileExtension === "docx" || sFileExtension === "xls"  || sFileExtension === "txt" || sFileExtension === "xlsx")|| iconvert <10)
    {
	   var strText = "File type:" + 	sFileName + "\n\n";
	   strText += "Size:" + iConvert + "MB\n\n";
	   strText += "Files greater than 10MB are not allowed";
	   alert(strText);
	   $('#quotationAttachment_input_fileName').val('');
    }
}
    