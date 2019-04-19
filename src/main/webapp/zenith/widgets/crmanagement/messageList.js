var messageList_includeDataObjects = 
[
	'widgets/crmanagement/EmailMessageData.js',
	'widgets/crmanagement/AttachmentData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/crmanagement/template/TemplateData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];


includeDataObjects (messageList_includeDataObjects, "messageList_loaded()");

function messageList_memberData ()
{
	this.m_nIndex = -1;
	this.m_nSelectedItemId = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_strActions = "messageList_addHyphen()";
}

var m_oMessageListMemberData = new messageList_memberData ();

function messageList_initAdmin ()
{
	document.getElementById ("messageList_button_add").style.visibility="visible";
	m_oMessageListMemberData.m_strActions = "messageList_addActions (row, index)";
	messageList_init ();
}

function messageList_init ()
{
	messageList_createDataGrid ();
	$("#filterMessageList_input_fromDate").datebox();
	$("#filterMessageList_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filterMessageList_input_toDate").datebox();
	$("#filterMessageList_input_toDate").datebox('textbox')[0].placeholder = "To Date";
}

function messageList_initUser ()
{
	messageList_init ();
}

function messageList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function messageList_addActions (row,index)
{
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="messageList_edit ('+row.m_nId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="messageList_delete ('+index+')"/> </td>'+
						'<td> <img title="Preview" src="images/preview.png" width="20" align="center" id="previewImageId" onClick="messageList_preview ('+row.m_nId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function messageList_createDataGrid ()
{
	initHorizontalSplitter("#messageList_div_horizontalSplitter", "#messageList_table_messageListDG");
	$('#messageList_table_messageListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
					{field:'m_strSubject',title:'Subject',sortable:true,width:250,
				  		styler: function(value,row,index)
				  		{
				  			return {class:'DGcolumn'};
				  		}
					},
					{field:'m_strDate',title:'Date',sortable:true,width:150},
					{field:'Actions',title:'Action',width:80,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return messageList_displayImages (row, index);
			        	}
					},
				]],
				onSelect: function (rowIndex, rowData)
				{
					messageList_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oMessageListMemberData.m_strSortColumn = strColumn;
					m_oMessageListMemberData.m_strSortOrder = strOrder;
					messageList_list (strColumn, strOrder, m_oMessageListMemberData.m_nPageNumber, m_oMessageListMemberData.m_nPageSize);
				}
		}
	);
	messageList_initDGPagination ();
	messageList_list (m_oMessageListMemberData.m_strSortColumn,m_oMessageListMemberData.m_strSortOrder,1, 10);
}

function messageList_initDGPagination ()
{
	$('#messageList_table_messageListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oMessageListMemberData.m_nPageNumber = $('#messageList_table_messageListDG').datagrid('getPager').pagination('options').pageNumber;
				messageList_list (m_oMessageListMemberData.m_strSortColumn, m_oMessageListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("messageList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oMessageListMemberData.m_nPageNumber = $('#messageList_table_messageListDG').datagrid('getPager').pagination('options').pageNumber;
				m_oMessageListMemberData.m_nPageSize = $('#messageList_table_messageListDG').datagrid('getPager').pagination('options').pageSize;
				messageList_list (m_oMessageListMemberData.m_strSortColumn, m_oMessageListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("messageList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function messageList_selectedRowData (oRowData, nIndex)
{
	m_oMessageListMemberData.m_nIndex = nIndex;
	document.getElementById("messageList_div_listDetail").innerHTML = "";
	var oEmailMessageData = new EmailMessageData ();
	oEmailMessageData.m_nId = oRowData.m_nId;
	oEmailMessageData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oEmailMessageData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	MessageDataProcessor.getXML (oEmailMessageData,	{
		async:false, 
		callback: function (strXMLData)
		{
			populateXMLData (strXMLData, "crmanagement/messageDetails.xslt", 'messageList_div_listDetail');
			messageList_attachmentDetailsDG ();
			messageList_contactDetailsDG ();
			MessageDataProcessor.get(oEmailMessageData,messageList_gotEmailMessageData);
		}
	});
}

function messageList_attachmentDetailsDG ()
{
	$('#messageDetails_table_messageDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strFileName',title:'Attachments',sortable:true,width:150}
			]]
		}
	);
}

function messageList_contactDetailsDG ()
{
	$('#messageDetails_table_messageDetailsContactDG').datagrid ({
	    columns:[[  
	        {field:'m_strCompanyName',title:'Client Name',sortable:true,width:100,
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
	        {field:'m_strContactName',title:'Contact Name',sortable:true,width:100},
	        {field:'m_strEmail',title:'Email Address',sortable:true,width:100}
	    ]],
	});
}

function messageList_gotEmailMessageData (oResponse)
{
	var oEmailMessageData = oResponse.m_arrEmailMessage[0];
	$('#messageDetails_table_messageDetailsDG').datagrid('loadData', oEmailMessageData.m_oAttachment);
	$('#messageDetails_table_messageDetailsContactDG').datagrid('loadData', oEmailMessageData.m_oContact);
}

function messageList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oMessageListMemberData.m_strSortColumn = strColumn;
	m_oMessageListMemberData.m_strSortOrder = strOrder;
	m_oMessageListMemberData.m_nPageNumber = nPageNumber;
	m_oMessageListMemberData.m_nPageSize = nPageSize
	loadPage ("inventorymanagement/progressbar.html", "dialog", "messageList_progressbarLoaded ()");
}

function messageList_progressbarLoaded ()
{
	createPopup ("dialog", "", "", true);
	var oEmailMessageData = new EmailMessageData ();
	oEmailMessageData.m_strSubject  = $("#messageList_input_subject").val();
	oEmailMessageData.m_strClientName = $("#messageList_input_Client").val();
	oEmailMessageData.m_strFromDate = FormatDate ($('#filterMessageList_input_fromDate').datebox('getValue'));
	oEmailMessageData.m_strToDate = FormatDate ($('#filterMessageList_input_toDate').datebox('getValue'));
	MessageDataProcessor.list(oEmailMessageData, m_oMessageListMemberData.m_strSortColumn, m_oMessageListMemberData.m_strSortOrder, m_oMessageListMemberData.m_nPageNumber, m_oMessageListMemberData.m_nPageSize, messageList_listed);
}

function messageList_listed (oResponse)
{
	clearGridData ("#messageList_table_messageListDG");
	$('#messageList_table_messageListDG').datagrid('loadData', oResponse.m_arrEmailMessage);
	$('#messageList_table_messageListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oMessageListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function messageList_filter()
{
	messageList_list (m_oMessageListMemberData.m_strSortColumn, m_oMessageListMemberData.m_strSortOrder, 1, 10);
}

function messageList_displayImages (row, index)
{
	var oImage = eval (m_oMessageListMemberData.m_strActions);
	return oImage;
}

function messageList_edit (nItemId)
{
	m_oMessageListMemberData.m_nSelectedItemId = nItemId;
	navigate ("editItem", "widgets/crmanagement/emailMessageEdit.js");
}

function messageList_showAddPopup ()
{
	navigate ("newItem", "widgets/crmanagement/emailMessageNew.js");
}

function messageList_delete (nIndex)
{
	var nMessageId = $("#messageList_table_messageListDG").datagrid('getRows')[nIndex].m_nId;
	var oEmailMessageData = new EmailMessageData ();
	oEmailMessageData.m_nId = nMessageId;
	oEmailMessageData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId; 
	oEmailMessageData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oEmailMessageData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var bConfirm = getUserConfirmation("Do you really want to delete this Message?")
	if(bConfirm)
		MessageDataProcessor.deleteData(oEmailMessageData, messageList_deleted);
}

function messageList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		document.getElementById("messageList_div_listDetail").innerHTML = "";
		informUser("Message deleted successfully.", "kSuccess");
		messageList_list (m_oMessageListMemberData.m_strSortColumn,m_oMessageListMemberData.m_strSortOrder,1, 10);
	}
}

function messageList_preview (nId)
{
	var oEmailMessageData = new EmailMessageData ();
	oEmailMessageData.m_nId = nId;
	oEmailMessageData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oEmailMessageData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	MessageDataProcessor.get(oEmailMessageData,messageList_gotMessageData);
}

function messageList_gotMessageData (oResponse)
{
	var oData = oResponse.m_arrEmailMessage[0];
	var oEmailMessageData = new EmailMessageData ();
	oEmailMessageData.m_oTemplateData = new TemplateData ();
	var nTemplateId = oData.m_oTemplateData.m_nTemplateId;
	var oTemplateData = new TemplateData (); 
	oTemplateData.m_nTemplateId = nTemplateId;
	oEmailMessageData.m_oTemplateData = (nTemplateId > 0) ? oTemplateData : null;
	oEmailMessageData.m_strSubject = oData.m_strSubject;
	oEmailMessageData.m_strContent = oData.m_strContent;
	oEmailMessageData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oEmailMessageData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	MessageDataProcessor.getPreview (oEmailMessageData, messageList_gotPreview);
}

function messageList_gotPreview (oResponse)
{
	m_oMessageListMemberData.oEmailMessage = oResponse.m_arrEmailMessage[0];
	loadPage ("crmanagement/template/preview.html", "secondDialog", "messageList_intiPreview ()");
}

function messageList_intiPreview ()
{
	createPopup("secondDialog", "#", "#", true);
	document.getElementById("preview_div_viewData").innerHTML = m_oMessageListMemberData.oEmailMessage.m_strHTML;
}

function emailMessage_cancelPreview ()
{
	HideDialog ("secondDialog");
}