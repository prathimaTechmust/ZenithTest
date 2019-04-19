var publicise_includeDataObjects = 
[
	'widgets/crmanagement/PubliciseData.js',
	'widgets/crmanagement/template/TemplateData.js',
	'widgets/crmanagement/template/TemplateCategoryData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/crmanagement/AttachmentData.js'
];

includeDataObjects (publicise_includeDataObjects, "publicise_loaded()");

function publicise_memberData ()
{
	this.m_nDefaultDays = 10;
	this.m_nItemGroupId = -1;
	this.m_nClientGroupId = -1;
	this.m_nItemId = -1;
}

var m_oPubliciseMemberData = new publicise_memberData ();

function publicise_new ()
{
	publicise_init ();
	publicise_initDaysSelectBox ();
	initFormValidateBoxes ("publicise_form_id");
}

function publicise_init ()
{
	createPopup("secondDialog", "#publicise_button_publicise", "#publicise_button_cancel", true);
	publicise_populateTemplateList ();
	publicise_initAttachmentsDG ();
}

function publicise_populateTemplateList ()
{
	var oTemplateData = new TemplateData();
	oTemplateData.m_oCategoryData.m_strCategoryName = "PUBLICIZE";      // to get only those templates with category publicize
	TemplateDataProcessor.list(oTemplateData, "","","", "", publicise_prepareTemplateDD);
}

function publicise_prepareTemplateDD (oResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption(-1, "Select"));
	for (var nIndex = 0; nIndex < oResponse.m_arrTemplates.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrTemplates [nIndex].m_nTemplateId,
				oResponse.m_arrTemplates [nIndex].m_strTemplateName));
	PopulateDD ("publicise_select_Template", arrOptions);
}

function publicise_initAttachmentsDG ()
{
	$('#publicise_table_attachmentsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strFileName',title:'Attachments',sortable:true,width:150},
			  	{field:'Action',title:'Action',width:30,
		        	formatter:function(value, oRow, nIndex)
		        	{
		        		return publicise_addActions (oRow, nIndex);
		        	}
		         } 
			]]
		}
	);
}

function publicise_addActions (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oActions = '<table align="center">'+
						'<tr>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="emailMessage_deleteAttachment ('+nIndex+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oActions;
}

function emailMessage_deleteAttachment (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	$('#publicise_table_attachmentsDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#publicise_table_attachmentsDG');
}

function publicise_initDaysSelectBox ()
{
	for(var nIndex = 0; nIndex < 20; nIndex++) 
	{
		var nValue = (nIndex+1)*5;
        document.getElementById('publicise_select_boughtDays').options[nIndex] = new Option(nValue,nValue);
        document.getElementById('publicise_select_notBoughtDays').options[nIndex] = new Option(nValue,nValue);
	}
	document.getElementById('publicise_select_boughtDays').value = m_oPubliciseMemberData.m_nDefaultDays;
	document.getElementById('publicise_select_notBoughtDays').value = m_oPubliciseMemberData.m_nDefaultDays;
}

function publicise_validate ()
{
	return validateForm("publicise_form_id") && publicise_validateTemplate ();
}

function publicise_validateTemplate ()
{
	var bValid = true;
	if($("#publicise_select_Template").val()== -1)
	{
		informUser("Please Select a Template", "kWarning");
		bValid = false;
	}
	return bValid;
}

function publicise_getAttachments ()
{
	var arrAttachmentData = new Array ();
	var arrAttachment = $('#publicise_table_attachmentsDG').datagrid('getData').rows;
	for(nIndex = 0; nIndex < arrAttachment.length; nIndex ++)
	{
		var oAttachmentData = new AttachmentData ();
		oAttachmentData.m_nAttachmentId = arrAttachment[nIndex].m_nAttachmentId;
		oAttachmentData.m_strFileName = arrAttachment[nIndex].m_strFileName;
		oAttachmentData.m_buffImgPhoto = arrAttachment[nIndex].m_buffImage;
		oAttachmentData.m_oFile = arrAttachment[nIndex].m_oFile;
		arrAttachmentData.push(oAttachmentData);
	}
	return arrAttachmentData;
}

function publicise_getFormData ()
{
	var oPubliciseData = new PubliciseData ();
	oPubliciseData.m_oTemplateData.m_nTemplateId = $("#publicise_select_Template").val();
	oPubliciseData.m_strSubject = $("#publicise_input_subject").val();
	oPubliciseData.m_nItemGroupId = m_oPubliciseMemberData.m_nItemGroupId;
	oPubliciseData.m_nClientGroupId = m_oPubliciseMemberData.m_nClientGroupId;
	oPubliciseData.m_nItemId = m_oPubliciseMemberData.m_nItemId;
	oPubliciseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPubliciseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oPubliciseData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPubliciseData.m_arrAttachmentData = publicise_getAttachments ();
	if(document.getElementById("publicise_input_bought").checked)
	{
		oPubliciseData.m_bIsBought = true;
		oPubliciseData.m_nNoOfDays = $('#publicise_select_boughtDays').val();
		
	}
	else
	{
		oPubliciseData.m_bIsBought = false;
		oPubliciseData.m_nNoOfDays = $('#publicise_select_notBoughtDays').val();
	}
	return oPubliciseData;
}

function publicise_send ()
{
	if (publicise_validate())
	{
		var oPubliciseData = publicise_getFormData ();
		CRMDataProcessor.publicise (oPubliciseData, publicise_sent);
	}
}

function publicise_sent (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Publicised successfully. <br> Mails sent for " +oResponse.m_nRowCount+ " contacts", "kSuccess");
		publicise_cancel () ;
	}
	else if(oResponse.m_nRowCount == 0)
		informUser("Found Zero contacts for Publicizing");
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Publicise Failed!", "kError");
}

function publicise_cancel ()
{
	HideDialog ("secondDialog");
}

function publicise_Preview ()
{
	var oPubliciseData = publicise_getFormData ();
	if(oPubliciseData.m_oTemplateData.m_nTemplateId > 0)
		CRMDataProcessor.getPublicisePreview (oPubliciseData, publicise_gotPreview);
	else
		document.getElementById("publicise_div_preview").innerHTML = "";
}

function publicise_gotPreview (oResponse)
{
	document.getElementById("publicise_div_preview").innerHTML = oResponse.m_arrEmailMessage[0].m_strHTML;
}

function publicise_addAttachment ()
{
	navigate ("newAttachment", "widgets/crmanagement/newAttachment.js");
}

function attachment_insertDataToAttachmentDG (oAttachmentData)
{
	assert.isObject(oAttachmentData, "oAttachmentData expected to be an Object.");
	if(attachment_validate() &&  !publicise_isAttachmentExists (oAttachmentData))
	{
		HideDialog ("thirdDialog");
		$('#publicise_table_attachmentsDG').datagrid('appendRow', oAttachmentData);
	}
}

function publicise_isAttachmentExists (oAttachmentData)
{
	assert.isObject(oAttachmentData, "oAttachmentData expected to be an Object.");
	assert( Object.keys(oAttachmentData).length >0 , "oAttachmentData cannot be an empty .");// checks for non emptyness 
	var bIsExists = false;
	var arrAttachmentData = $('#publicise_table_attachmentsDG').datagrid('getData').rows;
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