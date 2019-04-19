var emailClient_includeDataObjects = 
[
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js'
];

includeDataObjects (emailClient_includeDataObjects, "emailClient_loaded ()");

function emailClient_memberData ()
{
	this.m_strXMLData = "";
	this.m_strFileName = "";
	this.m_strSubject = "";
	this.m_oSelectedContact = null;
	this.m_arrContacts = new Array ();
	this.m_nClientId = -1;
	this.m_arrSelectedContacts = new Array();
}

var m_oEmailClientMemberData = new emailClient_memberData ();

function emailClient_new ()
{
	createPopup("print_div_dialog", "#emailClient_button_sendMail", "#emailClient_button_cancel", true);
	emailClient_init ();
}

function emailClient_init ()
{
	initFormValidateBoxes ("emailClient_form_id");
	emailClient_initializeContactListDG ();
	$('#emailClient_table_emailIdListDG').datagrid('loadData', m_oEmailClientMemberData.m_arrContacts);
	if(m_oEmailClientMemberData.m_oSelectedContact != null)
		emailClient_checkDGRow (m_oEmailClientMemberData.m_oSelectedContact)
}

function emailClient_getClientInfo ()
{
	var oClientData = new ClientData ();
	oClientData.m_nClientId = m_oEmailClientMemberData.m_nClientId;
	ClientDataProcessor.get (oClientData, function(oResponse)
			{
				var arrContacts = emailClient_buildContacts (oResponse.m_arrClientData[0]);
				$('#emailClient_table_emailIdListDG').datagrid('loadData', arrContacts);
				for (var nIndex = 0; nIndex < m_oEmailClientMemberData.m_arrSelectedContacts.length; nIndex++)
					emailClient_checkDGRow (m_oEmailClientMemberData.m_arrSelectedContacts[nIndex]);
			});
}

function emailClient_buildContacts(oClientData)
{
	var arrContacts = new Array ();
	if(oClientData.m_strEmail.trim() != "")
	{
		var oContactData = new ContactData ();
		oContactData.m_strContactName = oClientData.m_strCompanyName;
		oContactData.m_strEmail = oClientData.m_strEmail;
		arrContacts.push(oContactData);
	}
	for (var nIndex  = 0; nIndex < oClientData.m_oContacts.length; nIndex++)
			arrContacts.push(oClientData.m_oContacts[nIndex]);
	return arrContacts;

}

function emailClient_checkDGRow (oSelectedContact)
{
	var arrConatctData = $('#emailClient_table_emailIdListDG').datagrid('getRows');
	for (var nIndex = 0; nIndex < arrConatctData.length; nIndex++)
	{
		if(oSelectedContact.m_nContactId == arrConatctData[nIndex].m_nContactId)
		{
			$("#emailClient_table_emailIdListDG").datagrid('checkRow', nIndex);
			break;
		}
	}
}

function emailClient_initializeContactListDG ()
{
	$('#emailClient_table_emailIdListDG').datagrid
	({
		columns:
			[[
			    {field:'ckBox',checkbox:true,width:20},
			    {field:'m_strContactName',title:'Name',sortable:true,width:80},
			    {field:'m_strEmail',title:'Email Address',sortable:true,width:120},
			]],
			onCheck: function (rowIndex, rowData)
			{
				emailClient_holdCheckedData (rowData); 
			},
			onUncheck: function (rowIndex, rowData)
			{
				emailClient_removeCheckedData (rowData); 
			},
			onCheckAll: function (arrRows)
			{
				emailClient_holdAllCheckedData (arrRows);
			},
			onUncheckAll: function (arrRows)
			{
				emailClient_holdAllUnCheckedData (arrRows); 
			}
	});
}

function emailClient_isRowAdded (oRowData)
{
	var bIsadded = false;
	for (var nIndex = 0; !bIsadded && nIndex < m_oEmailClientMemberData.m_arrSelectedContacts.length; nIndex++)
		bIsadded = oRowData.m_nContactId == m_oEmailClientMemberData.m_arrSelectedContacts[nIndex].m_nContactId;
	return bIsadded;
}

function emailClient_holdCheckedData (oRowData)
{
	if(!emailClient_isRowAdded (oRowData))
		m_oEmailClientMemberData.m_arrSelectedContacts.push(oRowData);
}

function emailClient_removeCheckedData (oRowData)
{
	for (var nIndex = 0; nIndex < m_oEmailClientMemberData.m_arrSelectedContacts.length; nIndex++)
	{
		if(oRowData.m_nContactId == m_oEmailClientMemberData.m_arrSelectedContacts[nIndex].m_nContactId)
		{
			m_oEmailClientMemberData.m_arrSelectedContacts.splice(nIndex, 1);
			break;
		}
	}
}

function emailClient_holdAllCheckedData (arrRows)
{
	m_oEmailClientMemberData.m_arrSelectedContacts = new Array ();
	var arrRows = m_oEmailClientMemberData.m_arrSelectedContacts.concat(arrRows);
	m_oEmailClientMemberData.m_arrSelectedContacts = arrRows;
}

function emailClient_holdAllUnCheckedData (arrRows)
{
	m_oEmailClientMemberData.m_arrSelectedContacts = new Array ();
}

function emailClient_getFormData ()
{
	var arrMailIds = new Array ();
	var arrContacts = $('#emailClient_table_emailIdListDG').datagrid('getChecked');
	for (var nIndex = 0; nIndex < arrContacts.length; nIndex++)
		arrMailIds.push(arrContacts[nIndex].m_strEmail);
	return arrMailIds;
}

function emailClient_validate ()
{
	return validateForm("emailClient_form_id") && emailClient_validateContact ();
}

function emailClient_validateContact ()
{
	var bValid = $('#emailClient_table_emailIdListDG').datagrid('getChecked').length > 0;
	if (!bValid)
		informUser("Please Select contact.", "kWarning");
	return bValid;
}

function emailClient_sendMail ()
{
	if (emailClient_validate())
	{
		var arrMailIds = emailClient_getFormData ();
		TraderpUtil.sendEMails(arrMailIds, m_oEmailClientMemberData.m_strSubject, m_oEmailClientMemberData.m_strXMLData, m_oEmailClientMemberData.m_strFileName, emailClient_sentMail);
	}
}

function emailClient_sentMail (bSuccess)
{
	if(bSuccess == true)
	{
		informUser ("Mail(s) Sent Successfully.", "kSuccess");
		emailClient_cancel ();
	}
	else
		informUser ("Failed to Send Mail.", "kError");
}

function emailClient_cancel ()
{
	HideDialog ("print_div_dialog");
}

function emailClient_showAddContactPopUp ()
{
	m_oTrademustMemberData.m_nSelectedClientId = m_oEmailClientMemberData.m_nClientId;
	navigate ("addContacts", "widgets/clientmanagement/addContacts.js");
}

function clientInfo_handleAfterUpdate ()
{
	emailClient_getClientInfo ();
}
