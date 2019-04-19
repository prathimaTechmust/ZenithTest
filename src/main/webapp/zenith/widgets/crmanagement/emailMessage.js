var emailMessage_includeDataObjects = 
[
	'widgets/crmanagement/EmailMessageData.js',
	'widgets/crmanagement/AttachmentData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/crmanagement/template/TemplateData.js',
	'widgets/crmanagement/template/TemplateCategoryData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

includeDataObjects (emailMessage_includeDataObjects, "emailMessage_loaded()");

function emailMessage_memberData ()
{
	this.m_nId = -1;
	this.m_arrClientwiseData = new Array ();
	this.m_arrContacts = new Array ();
}

var m_oEmailMessageMemberData = new emailMessage_memberData ();

function emailMessage_new ()
{
	emailMessage_init ();
	initFormValidateBoxes ("emailMessage_form_id");
}

function emailMessage_populateTemplateList ()
{
	var oTemplateData = new TemplateData();
	TemplateDataProcessor.list(oTemplateData, "", "", 1, 10, emailMessage_prepareTemplateDD);
}

function emailMessage_prepareTemplateDD (oResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption(-1, "Select"));
	for (var nIndex = 0; nIndex < oResponse.m_arrTemplates.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrTemplates [nIndex].m_nTemplateId,
				oResponse.m_arrTemplates [nIndex].m_strTemplateName));
	PopulateDD ("emailMessage_select_Template", arrOptions);
}

function emailMessage_edit ()
{
	emailMessage_init ();
	document.getElementById("emailMessage_button_save").setAttribute('update', true);
	document.getElementById("emailMessage_button_save").innerHTML = "Update";
	document.getElementById("emailMessage_button_send").setAttribute('update', true);
	document.getElementById("emailMessage_button_send").innerHTML = "Resend";
	var oEmailMessageData = new EmailMessageData ();
	oEmailMessageData.m_nId = m_oMessageListMemberData.m_nSelectedItemId;
	MessageDataProcessor.get (oEmailMessageData, emailMessage_gotData);
}

function emailMessage_gotData (oResponse)
{	
	oEmailMessageData = oResponse.m_arrEmailMessage[0];
	try
	{
		$("#emailMessage_select_Template").val(oEmailMessageData.m_oTemplateData.m_strTemplateName);
	}catch(oException){}
	$("#emailMessage_input_subject").val(oEmailMessageData.m_strSubject);
	$("#emailMessage_textarea_content").val(oEmailMessageData.m_strContent);
	$('#emailMessage_table_emailMessageDG').datagrid('loadData', oEmailMessageData.m_oAttachment);
	$('#emailMessage_table_emailMessageContactDG').datagrid('loadData', oEmailMessageData.m_oContact);
	emailMessage_displayButton(oEmailMessageData.m_oContact);
	initFormValidateBoxes ("emailMessage_form_id");
}

function emailMessage_displayButton (oContacts)
{
	assert.isArray(oContacts, "oContacts expected to be an Array.");
	var bEmailSendDisabled = true;
	if(oContacts.length >0)
		bEmailSendDisabled= false;
	emailMessage_enableSendButton (bEmailSendDisabled);
}

function emailMessage_enableSendButton (bEmailSendDisabled)
{
	assert.isBoolean(bEmailSendDisabled, "bEmailSendDisabled should be a boolean value");
	var oEmailSendButton = document.getElementById ("emailMessage_button_send");
	oEmailSendButton.disabled = bEmailSendDisabled;
	bEmailSendDisabled? oEmailSendButton.style.backgroundColor = "#c0c0c0" :  oEmailSendButton.style.backgroundColor = "#0E486E";
}

function emailMessage_init ()
{
	createPopup("secondDialog", "#emailMessage_button_save", "#emailMessage_button_cancel", true);
	emailMessage_initDataGrid ();
	emailMessage_populateTemplateList ();
	emailMessage_initContactDatagrid ();
}

function emailMessage_initDataGrid ()
{
	$('#emailMessage_table_emailMessageDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strFileName',title:'Attachments',sortable:true,width:150},
			  	{field:'Action',title:'Action',width:30,
		        	formatter:function(value,row,index)
		        	{
		        		return emailMessage_displayImages (row, index);
		        	}
		         } 
			]]
		}
	);
}

function emailMessage_initContactDatagrid ()
{
	$('#emailMessage_table_emailMessageContactDG').datagrid ({
	    columns:[[  
	        {field:'m_strCompanyName',title:'Client Name',sortable:true,width:140,
	        	formatter:function(value,row,index)
	        	{
	        		try
	        		{
	        			var strCompanyName = row.m_oClientData.m_strCompanyName;
	        		} 
	        		catch(oException)
	        		{
	        			strCompanyName = "";
	        		}
	        		return strCompanyName;
	        	}
	        },
	        {field:'m_strContactName',title:'Contact Name',sortable:true,width:130},
	        {field:'m_strEmail',title:'Email Address',sortable:true,width:160},
	        {field:'Action',title:'Action',width:40,
	        	formatter:function(value,row,index)
	        	{
	        		return emailMessage_displayDeleteImages (row, index);
	        	}
	         } 
	    ]],
	});
}

function emailMessage_addContacts ()
{
	navigate ("", "widgets/crmanagement/clientAdd.js");
}

function emailMessage_send ()
{
	if (emailMessage_validate())
	{
		oEmailMessageData = emailMessage_getFormData ();
		if(document.getElementById("emailMessage_button_save").getAttribute('update') == "false")
			MessageDataProcessor.sendMail (oEmailMessageData, emailMessage_sent);
		else
		{
			oEmailMessageData.m_nId = m_oMessageListMemberData.m_nSelectedItemId;
			MessageDataProcessor.resendMail(oEmailMessageData, emailMessage_resent);
		}
	}
}

function emailMessage_save ()
{
	if (emailMessage_validate())
	{
		oEmailMessageData = emailMessage_getFormData ();
		if(document.getElementById("emailMessage_button_save").getAttribute('update') == "false")
			MessageDataProcessor.create (oEmailMessageData, emailMessage_created);
		else
		{
			oEmailMessageData.m_nId = m_oMessageListMemberData.m_nSelectedItemId;
			MessageDataProcessor.update(oEmailMessageData, emailMessage_updated);
		}
	}
}

function emailMessage_validate ()
{
	return validateForm("emailMessage_form_id") && emailMessage_validateContact ();
}

function emailMessage_validateContact ()
{
	var oEmailMessageData = new EmailMessageData ();
	oEmailMessageData.m_arrContactData = emailMessage_getContactData ();
	var bValidate = oEmailMessageData.m_arrContactData.length > 0;;
	if (!bValidate)
		informUser("Please Add the contact.", "kWarning");
	return bValidate;
}

function emailMessage_getFormData ()
{
	var oEmailMessageData = new EmailMessageData ();
	oEmailMessageData.m_oTemplateData = new TemplateData ();
	var nTemplateId = $("#emailMessage_select_Template").val();
	var oTemplateData = new TemplateData (); 
	oTemplateData.m_nTemplateId = nTemplateId;
	oEmailMessageData.m_oTemplateData = (nTemplateId > 0) ? oTemplateData : null;
	oEmailMessageData.m_strSubject = $("#emailMessage_input_subject").val();
	oEmailMessageData.m_strContent = $("#emailMessage_textarea_content").val();
	oEmailMessageData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oEmailMessageData.m_arrAttachmentData = emailMessage_getAttachmentData ();
	oEmailMessageData.m_arrContactData = emailMessage_getContactData ();
	oEmailMessageData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oEmailMessageData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oEmailMessageData;
}

function emailMessage_getAttachmentData ()
{
	var arrAttachmentData = new Array ();
	var arrAttachment = $('#emailMessage_table_emailMessageDG').datagrid('getData').rows;
	for(nIndex = 0; nIndex < arrAttachment.length; nIndex ++)
	{
		var oAttachmentData = new AttachmentData ();
		oAttachmentData.m_nAttachmentId = arrAttachment[nIndex].m_nAttachmentId;
		oAttachmentData.m_strFileName = arrAttachment[nIndex].m_strFileName;
		oAttachmentData.m_oFile = arrAttachment[nIndex].m_oFile;
		arrAttachmentData.push(oAttachmentData);
	}
	return arrAttachmentData;
}

function emailMessage_getContactData ()
{
	var arrContactData = new Array ();
	var arrContacts = $('#emailMessage_table_emailMessageContactDG').datagrid('getData').rows;
	for(nIndex = 0; nIndex < arrContacts.length; nIndex ++)
	{
		var oContactaData = new ContactData ();
		oContactaData.m_nContactId = arrContacts[nIndex].m_nContactId;
		arrContactData.push(oContactaData);
	}
	return arrContactData;
}

function emailMessage_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Message saved successfully.", "kSuccess");
		navigate ("", "widgets/crmanagement/messageListAdmin.js");
		HideDialog ("secondDialog");
	}
	else
		informUser("Email failed to send.", "kError");
}

function emailMessage_updated (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Message updated successfully.", "kSuccess");
		navigate ("", "widgets/crmanagement/messageListAdmin.js");
		HideDialog ("secondDialog");
	}
	else
		informUser("Email failed to send.", "kError");
}

function emailMessage_sent (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Message sent successfully.", "kSuccess");
		navigate ("", "widgets/crmanagement/messageListAdmin.js");
		HideDialog ("secondDialog");
	}
	else
		informUser("Email failed to send.", "kError");
}

function emailMessage_resent (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Message resent successfully.", "kSuccess");
		navigate ("", "widgets/crmanagement/messageListAdmin.js");
		HideDialog ("secondDialog");
	}
	else
		informUser("Email failed to send", "kError");
}

function emailMessage_cancel ()
{
	HideDialog ("secondDialog");
}

function emailMessage_addAttachments ()
{
	navigate ("newAttachment", "widgets/crmanagement/newAttachment.js");
}

function emailMessage_displayImages (nRowId, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="emailMessage_delete ('+nIndex+')"/> </td>'+
				'</tr>'+
			'</table>'
return oActions;
}

function emailMessage_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	$('#emailMessage_table_emailMessageDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#emailMessage_table_emailMessageDG');
}

function emailMessage_displayDeleteImages (nRowId, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="emailMessage_deleteContact ('+nIndex+')"/> </td>'+
				'</tr>'+
			'</table>'
return oActions;
}

function emailMessage_deleteContact (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	$('#emailMessage_table_emailMessageContactDG').datagrid ('deleteRow', nIndex);
	var arrContacts = $('#emailMessage_table_emailMessageContactDG').datagrid('getRows');
	emailMessage_displayButton (arrContacts);
	refreshDataGrid ('#emailMessage_table_emailMessageContactDG');
}

function emailMessage_Preview ()
{
	if (emailMessage_validate())
	{
		oEmailMessageData = emailMessage_getFormData ();
		MessageDataProcessor.getPreview (oEmailMessageData, emailMessage_gotPreview);
	}
}

function emailMessage_gotPreview (oResponse)
{
	m_oEmailMessageMemberData.oEmailMessage = oResponse.m_arrEmailMessage[0];
	loadPage ("crmanagement/template/preview.html", "thirdDialog", "emailMessage_intiPreview ()");
}

function emailMessage_intiPreview ()
{
	createPopup("thirdDialog", "#", "#", true);
	document.getElementById("preview_div_viewData").innerHTML = m_oEmailMessageMemberData.oEmailMessage.m_strHTML;
}

function emailMessage_cancelPreview ()
{
	HideDialog ("thirdDialog");
}

function emailMessage_initContacts ()
{
	emailMessage_new ();
	emailMessage_loadContacts ();
	var arrClientData = $('#emailMessage_table_emailMessageContactDG').datagrid('getRows');
	emailMessage_displayButton (arrClientData);
}

function emailMessage_loadContacts ()
{
	clearGridData ("#emailMessage_table_emailMessageContactDG");
	for(var nIndex = 0; nIndex < m_oEmailMessageMemberData.m_arrClientwiseData.length; nIndex ++)
	{
		var oClientData = m_oEmailMessageMemberData.m_arrClientwiseData[nIndex][0];
		emailMessage_loadedContacts (oClientData);
	}
}

function emailMessage_loadedContacts (oClientData)
{
	assert.isObject(oClientData, "oClientData expected to be an Object.");
	assert( Object.keys(oClientData).length >0 , "oClientData cannot be an empty .");// checks for non emptyness 
	for(var nIndex = 0; nIndex < oClientData.m_oContacts.length; nIndex ++)
	{
		m_oEmailMessageMemberData.m_arrContacts = oClientData.m_oContacts;
		if(oClientData.m_oContacts[nIndex].m_strEmail != "")
			$('#emailMessage_table_emailMessageContactDG').datagrid('appendRow',oClientData.m_oContacts[nIndex]);
		
	}
}

function attachment_insertDataToAttachmentDG (oAttachmentData)
{
	assert.isObject(oAttachmentData, "oAttachmentData expected to be an Object.");
	if(attachment_validate() &&  !emailMessage_isAttachmentExists (oAttachmentData))
	{
		HideDialog ("thirdDialog");
		$('#emailMessage_table_emailMessageDG').datagrid('appendRow', oAttachmentData);
	}
}

function emailMessage_isAttachmentExists (oAttachmentData)
{
	assert.isObject(oAttachmentData, "oAttachmentData expected to be an Object.");
	assert( Object.keys(oAttachmentData).length >0 , "oAttachmentData cannot be an empty .");// checks for non emptyness 
	var bIsExists = false;
	var arrAttachmentData = $('#emailMessage_table_emailMessageDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrAttachmentData.length; nIndex++)
	{
		if(arrAttachmentData [nIndex].m_strFileName == oAttachmentData.m_strFileName)
		{
			bIsExists = true;
			document.getElementById("attachment_td_fileName").innerHTML = "This file is already added";
			break;
		}
	}
	return bIsExists;
}