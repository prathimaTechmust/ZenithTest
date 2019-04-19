var itemPublicise_includeDataObjects = 
[
	'widgets/crmanagement/PubliciseData.js',
	'widgets/crmanagement/template/TemplateData.js',
	'widgets/crmanagement/template/TemplateCategoryData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/crmanagement/AttachmentData.js',
	'widgets/clientmanagement/ClientGroupData.js'
];

includeDataObjects (itemPublicise_includeDataObjects, "itemPublicise_loaded()");

function itemPublicise_memberData ()
{
	this.m_nDefaultDays = 10;
	this.m_nItemGroupId = -1;
	this.m_nClientGroupId = -1;
	this.m_nItemId = -1;
	this.m_arrSelectedGroup = new Array ();
}

var m_oItemPubliciseMemberData = new itemPublicise_memberData ();

function itemPublicise_new ()
{
	itemPublicise_init ();
	itemPublicise_initDaysSelectBox ();
	initFormValidateBoxes ("itemPublicise_form_id");
	HideDialog("ProcessDialog");
}

function itemPublicise_init ()
{
	createPopup("secondDialog", "#itemPublicise_button_publicise", "#itemPublicise_button_cancel", true);
	$('#div_accordian').accordion();
	itemPublicise_populateTemplateList ();
	itemPublicise_initAttachmentsDG ();
	itemPublicise_initClientGroupDG ();
}

function itemPublicise_populateTemplateList ()
{
	var oTemplateData = new TemplateData();
	oTemplateData.m_oCategoryData.m_strCategoryName = "PUBLICIZE";      // to get only those templates with category publicize
	TemplateDataProcessor.list(oTemplateData, "", "", "", "", itemPublicise_prepareTemplateDD);
}

function itemPublicise_prepareTemplateDD (oResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption(-1, "Select"));
	for (var nIndex = 0; nIndex < oResponse.m_arrTemplates.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrTemplates [nIndex].m_nTemplateId,
				oResponse.m_arrTemplates [nIndex].m_strTemplateName));
	PopulateDD ("itemPublicise_select_Template", arrOptions);
}

function itemPublicise_initAttachmentsDG ()
{
	$('#itemPublicise_table_attachmentsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strFileName',title:'Attachments',sortable:true,width:150},
			  	{field:'Action',title:'Action',width:30,
		        	formatter:function(value, oRow, nIndex)
		        	{
		        		return itemPublicise_addActions (oRow, nIndex);
		        	}
		         } 
			]]
		}
	);
}

function itemPublicise_addActions (oRow, nIndex)
{
	var oActions = '<table align="center">'+
						'<tr>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="emailMessage_deleteAttachment ('+nIndex+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oActions;
}

function emailMessage_deleteAttachment (nIndex)
{
	$('#itemPublicise_table_attachmentsDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#itemPublicise_table_attachmentsDG');
}

function itemPublicise_initDaysSelectBox ()
{
	for(var nIndex = 0; nIndex < 20; nIndex++) 
	{
		var nValue = (nIndex+1)*5;
        document.getElementById('itemPublicise_select_boughtDays').options[nIndex] = new Option(nValue,nValue);
        document.getElementById('itemPublicise_select_notBoughtDays').options[nIndex] = new Option(nValue,nValue);
	}
	document.getElementById('itemPublicise_select_boughtDays').value = m_oItemPubliciseMemberData.m_nDefaultDays;
	document.getElementById('itemPublicise_select_notBoughtDays').value = m_oItemPubliciseMemberData.m_nDefaultDays;
}

function itemPublicise_validate ()
{
	return validateForm("itemPublicise_form_id") && itemPublicise_validateTemplate ();
}

function itemPublicise_validateTemplate ()
{
	var bValid = true;
	if($("#itemPublicise_select_Template").val()== -1)
	{
		informUser("Please Select a Template", "kWarning");
		bValid = false;
	}
	return bValid;
}

function itemPublicise_getAttachments ()
{
	var arrAttachmentData = new Array ();
	var arrAttachment = $('#itemPublicise_table_attachmentsDG').datagrid('getData').rows;
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

function itemPublicise_getFormData ()
{
	var oPubliciseData = new PubliciseData ();
	oPubliciseData.m_oTemplateData.m_nTemplateId =$("#itemPublicise_select_Template").val();
	oPubliciseData.m_strSubject = $("#itemPublicise_input_subject").val();
	oPubliciseData.m_nItemGroupId = m_oItemPubliciseMemberData.m_nItemGroupId;
	oPubliciseData.m_nClientGroupId = m_oItemPubliciseMemberData.m_nClientGroupId;
	oPubliciseData.m_nItemId = m_oItemPubliciseMemberData.m_nItemId;
	oPubliciseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPubliciseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oPubliciseData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPubliciseData.m_arrAttachmentData = itemPublicise_getAttachments ();
	oPubliciseData.m_arrClientGroup = item_getCheckedClientGroups ();
	if(document.getElementById("itemPublicise_input_bought").checked)
	{
		oPubliciseData.m_bIsBought = true;
		oPubliciseData.m_nNoOfDays = $('#itemPublicise_select_boughtDays').val();
		
	}
	else
	{
		oPubliciseData.m_bIsBought = false;
		oPubliciseData.m_nNoOfDays = $('#itemPublicise_select_notBoughtDays').val();
	}
	return oPubliciseData;
}

function item_getCheckedClientGroups ()
{
	var oClientGroupDataArray = new Array ();
	var arrClientGroups = $('#itemPublicise_table_clientGroupDG').datagrid('getChecked');
	for (var nIndex = 0; nIndex < arrClientGroups.length; nIndex++)
	{
		var oClientGroupData = new ClientGroupData ();
		oClientGroupData.m_nGroupId = arrClientGroups [nIndex].m_nGroupId;
		oClientGroupData.m_strGroupName = arrClientGroups [nIndex].m_strGroupName;
		oClientGroupDataArray.push(oClientGroupData);
	}
	return oClientGroupDataArray;
}

function itemPublicise_send ()
{
	if (itemPublicise_validate())
	{
		var oPubliciseData = itemPublicise_getFormData ();
		CRMDataProcessor.publicise (oPubliciseData, itemPublicise_sent);
	}
}

function itemPublicise_sent (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Publicised successfully. <br> Mails sent for " +oResponse.m_nRowCount+ " contacts", "kSuccess");
		itemPublicise_cancel () ;
	}
	else if(oResponse.m_nRowCount == 0)
		informUser("Found Zero contacts for Publicizing");
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Publicise Failed!", "kError");
}

function itemPublicise_cancel ()
{
	HideDialog ("secondDialog");
}

function itemPublicise_Preview ()
{
	var oPubliciseData = itemPublicise_getFormData ();
	if(oPubliciseData.m_oTemplateData.m_nTemplateId > 0)
		CRMDataProcessor.getPublicisePreview (oPubliciseData, itemPublicise_gotPreview);
	else
		document.getElementById("itemPublicise_div_preview").innerHTML = "";
}

function itemPublicise_gotPreview (oResponse)
{
	document.getElementById("itemPublicise_div_preview").innerHTML = oResponse.m_arrEmailMessage[0].m_strHTML;
}

function itemPublicise_addAttachment ()
{
	navigate ("newAttachment", "widgets/crmanagement/newAttachment.js");
}

function attachment_insertDataToAttachmentDG (oAttachmentData)
{
	if(attachment_validate() &&  !itemPublicise_isAttachmentExists (oAttachmentData))
	{
		HideDialog ("thirdDialog");
		$('#itemPublicise_table_attachmentsDG').datagrid('loadData', oAttachmentData);
	}
}

function itemPublicise_isAttachmentExists (oAttachmentData)
{
	var bIsExists = false;
	var arrAttachmentData = $('#itemPublicise_table_attachmentsDG').datagrid('getData').rows;
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

function itemPublicise_initClientGroupDG () 
{
	$('#itemPublicise_table_clientGroupDG').datagrid
	(
		{
			columns:
				[[
				  	{field:'ckBox',checkbox:true,width:30},
				  	{field:'m_strGroupName',title:'Group Name',sortable:true,width:60}
				]],
				onCheck: function (rowIndex, rowData)
				{
					itemPublicise_holdCheckedData (rowData, true); 
				},
				onUncheck: function (rowIndex, rowData)
				{
					itemPublicise_holdCheckedData (rowData, false); 
				},
				onCheckAll: function (arrRows)
				{
					itemPublicise_holdAllCheckedData (arrRows);
				},
				onUncheckAll: function (arrRows)
				{
					itemPublicise_holdAllUnCheckedData (arrRows); 
				}
		});
	itemPublicise_getClientGroupList ();
}

function itemPublicise_getClientGroupList ()
{
	var oClientGroupData = new ClientGroupData ();
	oClientGroupData.m_oUserCredentialsData = new UserInformationData ();
	oClientGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ClientGroupDataProcessor.list(oClientGroupData, "", "", "","", itemPublicise_gotClientGroupList);
}

function itemPublicise_gotClientGroupList (oResponse)
{
	$('#itemPublicise_table_clientGroupDG').datagrid ('loadData',oResponse.m_arrGroupData);
	itemPublicise_checkDGRow ();
}

function itemPublicise_checkDGRow ()
{
	var arrGroupData = $('#itemPublicise_table_clientGroupDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrGroupData.length; nIndex++)
	{
		if(itemPublicise_isRowSelectable(arrGroupData[nIndex].m_strGroupName))
			$("#itemPublicise_table_clientGroupDG").datagrid('checkRow', nIndex);
	}
}

function itemPublicise_isRowSelectable (strGroupName)
{
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oItemPubliciseMemberData.m_arrSelectedGroup.length && !bIsSelectable; nIndex++)
	{
		if(m_oItemPubliciseMemberData.m_arrSelectedGroup[nIndex].m_strGroupName == strGroupName)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function itemPublicise_addActions (row, index)
{
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" onClick="itemPublicise_deleteGroup ('+index+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function itemPublicise_deleteGroup ()
{
	$('#itemPublicise_table_clientGroupDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#itemPublicise_table_clientGroupDG');
}

function itemPublicise_holdCheckedData (oRowData, bIsForAdd)
{
	if(bIsForAdd)
	{
		if(!ItemPublicise_isRowAdded (oRowData))
			m_oItemPubliciseMemberData.m_arrSelectedGroup.push(oRowData);
	}
	else
		ItemPublicise_removeGroup (oRowData);
}

function ItemPublicise_isRowAdded (oRowData)
{
	var bIsadded = false;
	for (var nIndex = 0; !bIsadded && nIndex < m_oItemPubliciseMemberData.m_arrSelectedGroup.length; nIndex++)
		bIsadded = (m_oItemPubliciseMemberData.m_arrSelectedGroup [nIndex].m_strGroupName == oRowData.m_strGroupName);
	return bIsadded;
}

function ItemPublicise_removeGroup (oRowData)
{
	for (var nIndex = 0; nIndex < m_oItemPubliciseMemberData.m_arrSelectedGroup.length; nIndex++)
	{
		if(m_oItemPubliciseMemberData.m_arrSelectedGroup[nIndex].m_strGroupName == oRowData.m_strGroupName)
		{
			m_oItemPubliciseMemberData.m_arrSelectedGroup.splice(nIndex, 1);
			break;
		}
	}
}

function itemPublicise_holdAllCheckedData (arrRows)
{
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		itemPublicise_holdCheckedData(arrRows[nIndex], true);
}

function Itempublicise_holdAllUnCheckedData (arrRows)
{
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		itemPublicise_holdCheckedData(arrRows[nIndex], false);
}

