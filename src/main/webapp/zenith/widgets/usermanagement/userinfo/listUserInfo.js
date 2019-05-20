var listUserInfo_includeDataObjects = 
[
	'widgets/usermanagement/role/RoleData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

 includeDataObjects (listUserInfo_includeDataObjects, "listUserInfo_loaded()");

function listUserInfo_memberData ()
{
	this.m_nSelectedUserId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strUserName";
    this.m_strSortOrder = "asc";
    this.m_strUserPhotoName = "";
}

var m_oUserInfoListMemberData = new listUserInfo_memberData ();

function listUserInfo_loaded ()
{
	loadPage ("usermanagement/userinfo/listUserInfo.html", "workarea", "listUserInfo_init ()");
	listUserInfo_populateRoleNameList ('filterUserInfo_select_roleName');
}

function listUserInfo_init ()
{
	listUserInfo_createDataGrid ();
}

function listUserInfo_populateRoleNameList (strRoleDD)
{
	var oUserInfomationData = listUserInfo_getFormData ();
	RoleDataProcessor.list(oUserInfomationData.m_oRole, "m_strRoleName", "asc", 0, 0,
			function (oRoleResponse)
			{
		    	listUserInfo_prepareRoleNameDD  (strRoleDD, oRoleResponse);
			}				
		);
}

function listUserInfo_prepareRoleNameDD (strRoleDD, oRoleResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption(-1, "Select"));
	for (var nIndex = 0; nIndex < oRoleResponse.m_arrRoleData.length; nIndex++)
		arrOptions.push (CreateOption (oRoleResponse.m_arrRoleData [nIndex].m_nRoleId,
				oRoleResponse.m_arrRoleData [nIndex].m_strRoleName));
	PopulateDD (strRoleDD, arrOptions);
}

function listUserInfo_createDataGrid ()
{
	initHorizontalSplitter("#listUserInfo_div_horizontalSplitter", "#listUserInfo_table_users");
	$('#listUserInfo_table_users').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_strUserName',title:'User Name',sortable:true,width:300},
				{field:'RoleName',title:'Role',sortable:true,width:200,
					formatter:function(value,oUserInformationData,index)
					{
						return oUserInformationData.m_oRole.m_strRoleName;
					}
				 },
				{field:'m_strLoginId',title:'Login Name',sortable:true,width:200},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return listUserInfo_displayImages (row.m_nUserId, row.m_nUID, index);
		        	}
	            },
			]],
			onSelect: function (rowIndex, rowData)
			{
				listUserInfo_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder, oUserInformationData)
			{
				if(strColumn == 'RoleName')
					strColumn = 'm_nUserId';
				m_oUserInfoListMemberData.m_strSortColumn = strColumn;
				m_oUserInfoListMemberData.m_strSortOrder = strOrder;
				listUserInfo_list (strColumn, strOrder, m_oUserInfoListMemberData.m_nPageNumber, m_oUserInfoListMemberData.m_nPageSize);
			}
		}
	)
	
	listUserInfo_initDGPagination ();
	listUserInfo_list (m_oUserInfoListMemberData.m_strSortColumn, m_oUserInfoListMemberData.m_strSortOrder, 1, 10);
}

function listUserInfo_initDGPagination ()
{
	$('#listUserInfo_table_users').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oUserInfoListMemberData.m_nPageNumber = nPageNumber;
				listUserInfo_list (m_oUserInfoListMemberData.m_strSortColumn, m_oUserInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listUserInfo_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oUserInfoListMemberData.m_nPageNumber = nPageNumber;
				m_oUserInfoListMemberData.m_nPageSize = nPageSize;
				listUserInfo_list (m_oUserInfoListMemberData.m_strSortColumn, m_oUserInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listUserInfo_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listUserInfo_filter ()
{
	listUserInfo_list (m_oUserInfoListMemberData.m_strSortColumn, m_oUserInfoListMemberData.m_strSortOrder, 1, 10);
}

function listUserInfo_displayImages (nUserId, nUID, index)
{
 	assert.isNumber(nUserId, "nUserId expected to be a Number.");
	assert.isNumber(nUID, "nUID expected to be a Number.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img src="images/edit_database_24.png" width="20" align="center" id="editImageId" title="Edit" onClick="listUserInfo_edit('+nUserId+','+nUID+')"/> </td>'+
							'<td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete" onClick="listUserInfo_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listUserInfo_selectedRowData (oRowData, nIndex)
{
 	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oUserInfoListMemberData.m_nIndex = nIndex;
	document.getElementById("listUserInfo_div_listDetail").innerHTML = "";
	var oUserInformationData = listUserInfo_getFormData ();
	oUserInformationData.m_nUserId = oRowData.m_nUserId;
	UserInformationDataProcessor.getXML (oUserInformationData,	listUserInfo_gotXML);
}

function listUserInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "usermanagement/userinfo/userInfoDetails.xslt", 'listUserInfo_div_listDetail');
}

function listUserInfo_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oUserInfoListMemberData.m_strSortColumn = strColumn;
	m_oUserInfoListMemberData.m_strSortOrder = strOrder;
	m_oUserInfoListMemberData.m_nPageNumber = nPageNumber;
	m_oUserInfoListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "listUserInfo_progressbarLoaded ()");
}

function listUserInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oUserInformationData = listUserInfo_getFormData ();
	UserInformationDataProcessor.list(oUserInformationData, m_oUserInfoListMemberData.m_strSortColumn, m_oUserInfoListMemberData.m_strSortOrder, m_oUserInfoListMemberData.m_nPageNumber, m_oUserInfoListMemberData.m_nPageSize, listUserInfo_listed);
}

function listUserInfo_getFormData () 
{
	var oUserInfomationData = new UserInformationData ();
	oUserInfomationData.m_strLoginId = $("#filterUserInfo_input_loginName").val();
	oUserInfomationData.m_strUserName = $("#filterUserInfo_input_userName").val();
	oUserInfomationData.m_oRole = new RoleData ();
	oUserInfomationData.m_oRole.m_nRoleId = $("#filterUserInfo_select_roleName").val();
	return oUserInfomationData;
}

function listUserInfo_listed (oUserInfoResponse)
{
	clearGridData ("#listUserInfo_table_users");
	for (var nIndex = 0; nIndex < oUserInfoResponse.m_arrUserInformationData.length; nIndex++)
		$('#listUserInfo_table_users').datagrid('appendRow',oUserInfoResponse.m_arrUserInformationData[nIndex]);
	$('#listUserInfo_table_users').datagrid('getPager').pagination ({total:oUserInfoResponse.m_nRowCount, pageNumber:m_oUserInfoListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function listUserInfo_edit (nUserId, nUID)
{
	assert.isNumber(nUserId, "nUserId expected to be a Number.");
	assert.isNumber(nUID, "nUID expected to be a Number.");
	m_oUserInfoListMemberData.m_nSelectedUserId = nUserId;
	m_oUserInfoListMemberData.m_nSelectedUID = nUID;
	navigate ("actionInformation", "widgets/usermanagement/userinfo/editUserInfo.js");
}

function listUserInfo_listDetail_delete ()
{
	listUserInfo_delete (m_oUserInfoListMemberData.m_nIndex);
}

function listUserInfo_delete (nIndex)
{
 	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	assert.isOk(nIndex > -1, "nIndex must be greater than zero.");
	var oListData = $("#listUserInfo_table_users").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oUserInformationData = new UserInformationData ();
	oUserInformationData.m_nUserId = oData.m_nUserId;
	alert("User cannot be deleted");
	/*var bConfirm = getUserConfirmation("user cannot be deleted")
	if(bConfirm)
		UserInformationDataProcessor.deleteData(oUserInformationData, listUserInfo_deleted);*/
}

function listUserInfo_deleted (oUserInfoResponse)
{
	if(oUserInfoResponse.m_bSuccess)
	{
		document.getElementById("listUserInfo_div_listDetail").innerHTML = "";
		informUser("usermessage_listuserinfo_userdeletedsuccessfully", "kSuccess");
		navigate("userList", "widgets/usermanagement/userinfo/listUserInfo.js");
	}
}

function userImage_setPreview (m_strUserImageUrl)
{
	m_oUserInfoListMemberData.m_strUserPhotoName = m_strUserImageUrl;
	loadPage ("usermanagement/userinfo/userImagePreview.html", "dialog", "user_showImagePreview ()");
}

function user_showImagePreview ()
{
	createPopup ('dialog', '', '', true);
	document.getElementById('dialog').style.position = "fixed";
	$(".imagePreview").attr('src', m_oUserInfoListMemberData.m_strUserPhotoName);
}

function listUserInfo_showAddPopup ()
{
	navigate ("newuser", "widgets/usermanagement/userinfo/newUserInfo.js");
}

function userList_cancelImagePreview ()
{
	HideDialog ("dialog");
}
