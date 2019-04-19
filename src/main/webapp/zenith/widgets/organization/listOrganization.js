var listOrganization_includeDataObjects = 
[
	'widgets/usermanagement/role/RoleData.js',
	'widgets/organization/organizationInformationData.js'
];

 includeDataObjects (listOrganization_includeDataObjects, "listOrganization_loaded()");

function listOrganization_memberData ()
{
	this.m_nSelectedUserId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strOrganizationName";
    this.m_strSortOrder = "asc";
}

var m_oOrganizationInfoListMemberData = new listOrganization_memberData ();

function listOrganization_loaded ()
{
	loadPage ("organization/listOrganization.html", "workarea", "listOrganization_init ()");
}

function listOrganization_init ()
{
	listOrganization_createDataGrid ();
}

function listOrganization_createDataGrid ()
{
	initHorizontalSplitter("#listOrganizationInfo_div_horizontalSplitter", "#listOrganization_table_users");
	$('#listOrganization_table_users').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_strOrganizationName',title:'Organization Name',sortable:true,width:300},
				{field:'m_strAddress',title:'Address',sortable:true,width:300},
				{field:'m_strPhoneNumber',title:'PhoneNumber',sortable:true,width:300},
				{field:'m_strEmailAddress',title:'Email',sortable:true,width:300},
			]],
			onSelect: function (rowIndex, rowData)
			{
				listOrganization_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder, oOrganizationInformationData)
			{
				if(strColumn == 'OrganizationName')
					strColumn = 'm_nOrganizationId';
				m_oOrganizationInfoListMemberData.m_strSortColumn = strColumn;
				m_oOrganizationInfoListMemberData.m_strSortOrder = strOrder;
				listOrganization_list (strColumn, strOrder, m_oOrganizationInfoListMemberData.m_nPageNumber, m_oOrganizationInfoListMemberData.m_nPageSize);
			}
		}
	)
	
	listOrganization_initDGPagination ();
	listOrganization_list (m_oOrganizationInfoListMemberData.m_strSortColumn, m_oOrganizationInfoListMemberData.m_strSortOrder, 1, 10);
}

function listOrganization_initDGPagination ()
{
	$('#listOrganization_table_users').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oOrganizationInfoListMemberData.m_nPageNumber = nPageNumber;
				listOrganization_list (m_oOrganizationInfoListMemberData.m_strSortColumn, m_oOrganizationInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listOrganization_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oOrganizationInfoListMemberData.m_nPageNumber = nPageNumber;
				m_oOrganizationInfoListMemberData.m_nPageSize = nPageSize;
				listOrganization_list (m_oOrganizationInfoListMemberData.m_strSortColumn, m_oOrganizationInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listOrganization_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listOrganization_filter ()
{
	listOrganization_list (m_oOrganizationInfoListMemberData.m_strSortColumn, m_oOrganizationInfoListMemberData.m_strSortOrder, 1, 10);
}

function listOrganization_displayImages (nUserId, nUID, index)
{
 	assert.isNumber(nUserId, "nUserId expected to be a Number.");
	assert.isNumber(nUID, "nUID expected to be a Number.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img src="images/edit_database_24.png" width="20" align="center" id="editImageId" title="Edit" onClick="listOrganization_edit('+nUserId+','+nUID+')"/> </td>'+
							'<td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete" onClick="listOrganization_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listOrganization_selectedRowData (oRowData, nIndex)
{
 	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oOrganizationInfoListMemberData.m_nIndex = nIndex;
	document.getElementById("listOrganization_div_listDetail").innerHTML = "";
	var oOrganizationInformationData = new organizationInformationData();
	oOrganizationInformationData.m_strOrganizationName = oRowData.m_strOrganizationName;
	OrganizationDataProcessor.getXML (oOrganizationInformationData,	listOrganization_gotXML);
}

function listOrganization_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "organization/organizationInfoDetails.xslt", 'listOrganization_div_listDetail');
}

function listOrganization_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oOrganizationInfoListMemberData.m_strSortColumn = strColumn;
	m_oOrganizationInfoListMemberData.m_strSortOrder = strOrder;
	m_oOrganizationInfoListMemberData.m_nPageNumber = nPageNumber;
	m_oOrganizationInfoListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "listOrganization_progressbarLoaded ()");
}

function listOrganization_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oOrganizationInformationData = new organizationInformationData();
	OrganizationDataProcessor.list(oOrganizationInformationData, m_oOrganizationInfoListMemberData.m_strSortColumn, m_oOrganizationInfoListMemberData.m_strSortOrder, m_oOrganizationInfoListMemberData.m_nPageNumber, m_oOrganizationInfoListMemberData.m_nPageSize, listOrganization_listed);
}

function listOrganization_listed (oOrganizationInfoResponse)
{
	clearGridData ("#listOrganization_table_users");
	for (var nIndex = 0; nIndex < oOrganizationInfoResponse.m_arrOrganizationInformationData.length; nIndex++)
		$('#listOrganization_table_users').datagrid('appendRow',oOrganizationInfoResponse.m_arrOrganizationInformationData[nIndex]);
	$('#listOrganization_table_users').datagrid('getPager').pagination ({total:oOrganizationInfoResponse.m_nRowCount, pageNumber:m_oOrganizationInfoListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function listOrganization_edit (nUserId, nUID)
{
	assert.isNumber(nUserId, "nUserId expected to be a Number.");
	assert.isNumber(nUID, "nUID expected to be a Number.");
	m_oOrganizationInfoListMemberData.m_nSelectedUserId = nUserId;
	m_oOrganizationInfoListMemberData.m_nSelectedUID = nUID;
	navigate ("actionInformation", "widgets/usermanagement/userinfo/editUserInfo.js");
}

function listOrganization_listDetail_delete ()
{
	listOrganization_delete (m_oOrganizationInfoListMemberData.m_nIndex);
}

function listOrganization_delete (nIndex)
{
 	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	assert.isOk(nIndex > -1, "nIndex must be greater than zero.");
	var oListData = $("#listOrganization_table_users").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oOrganizationInformationData = new UserInformationData ();
	oOrganizationInformationData.m_nUserId = oData.m_nUserId;
	alert("User cannot be deleted");
	/*var bConfirm = getUserConfirmation("user cannot be deleted")
	if(bConfirm)
		UserInformationDataProcessor.deleteData(oOrganizationInformationData, listOrganization_deleted);*/
}

function listOrganization_deleted (oUserInfoResponse)
{
	if(oUserInfoResponse.m_bSuccess)
	{
		document.getElementById("listOrganization_div_listDetail").innerHTML = "";
		informUser("usermessage_listOrganization_userdeletedsuccessfully", "kSuccess");
		navigate("organizationList", "widgets/organization/listOrganization.js");
	}
}

function listOrganizationInfo_showAddPopup ()
{
	navigate ("neworganization", "widgets/organization/newOrganization.js");
}
