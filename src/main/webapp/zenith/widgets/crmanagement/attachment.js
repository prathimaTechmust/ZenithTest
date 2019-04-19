var attachment_includeDataObjects = 
[
	'widgets/crmanagement/AttachmentData.js',
];

includeDataObjects (attachment_includeDataObjects, "attachment_loaded()");

function attachment_memberData ()
{
	this.m_nAttachmentId = -1;
	this.m_oFile = null;
}

var m_oAttachmentMemberData = new attachment_memberData ();

function atachment_new ()
{
	createPopup("thirdDialog", "#attachment_submit", "#attachment_button_cancel", true);
	initFormValidateBoxes ("attachment_form_id");
}

function attachment_submit ()
{
	var oAttachmentData = attachment_getFormData ();
	attachment_insertDataToAttachmentDG (oAttachmentData);
}

function attachment_getFormData ()
{
	var oAttachmentData = new AttachmentData ();
	oAttachmentData.m_strFileName = $("#attachment_input_fileName").val();
	oAttachmentData.m_strFileName = getImageName(oAttachmentData.m_strFileName.value);
	oAttachmentData.m_oFile = m_oAttachmentMemberData.m_oFile;
	return oAttachmentData;
}

function attachment_validate ()
{
	return validateForm("attachment_form_id");
}

function attachment_cancel ()
{
	HideDialog("thirdDialog");
}

function attachment_getAttachment ()
{
	m_oAttachmentMemberData.m_oFile = $("#attachment_input_fileName").val();
}
