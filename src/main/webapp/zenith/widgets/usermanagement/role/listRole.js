var listRole_includeDataObjects = 
	[
		'widgets/usermanagement/role/RoleData.js',
	];

 includeDataObjects (listRole_includeDataObjects, "listRole_loaded ()");

function listRole_loaded ()
{
	loadPage ("usermanagement/role/listRole.html", "workarea", "listRole_init ()");
}

function listRole_memberData ()
{	
	this.m_nSelectedRoleId = -1;
	this.m_oRoleData = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strRoleName";
    this.m_strSortOrder = "asc";
}

var m_oRoleListMemberData = new listRole_memberData ();

function listRole_getFormData ()
{
	var oRoleData = new RoleData ();
	oRoleData.m_strRoleName = $("#filterRole_input_roleName").val();
	oRoleData.m_nRoleId = -1;
	return oRoleData;
}

function listRole_init ()
{
	initHorizontalSplitter("#listRole_div_horizontalSplitter", "#listRole_table_roleListDG");
	$('#listRole_table_roleListDG').datagrid({
		fit:true,
	    columns:[[  
	              
	        {field:'m_strRoleName',title:'Role Name',sortable:true,width:700},
	        {field:'Action',title:'Action',sortable:false,width:50,
	        	formatter:function(value,row,index)
	        	{
	        		return listRole_displayImages (row.m_nRoleId, index);
	        	}
	         },
	    ]]
	});
	
	$('#listRole_table_roleListDG').datagrid
	(
		{
			onSortColumn: function (strColumnName, strOrder)
			{
			 	m_oRoleListMemberData.m_strSortColumn = strColumnName;
			 	m_oRoleListMemberData.m_strSortOrder = strOrder;
			 	listRole_list (strColumnName, strOrder, m_oRoleListMemberData.m_nPageNumber, m_oRoleListMemberData.m_nPageSize);
			}
		}
	)
	$('#listRole_table_roleListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				listRole_selectedRowData (rowData,rowIndex);
			}
		}
	)
	
	listRole_initDGPagination ();
	listRole_list (m_oRoleListMemberData.m_strSortColumn, m_oRoleListMemberData.m_strSortOrder, 1, 10);
}

function listRole_initDGPagination ()
{
	$('#listRole_table_roleListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oRoleListMemberData.m_nPageNumber = nPageNumber;
				listRole_list (m_oRoleListMemberData.m_strSortColumn, m_oRoleListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listRole_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oRoleListMemberData.m_nPageNumber = nPageNumber;
				m_oRoleListMemberData.m_nPageSize = nPageSize;
				listRole_list (m_oRoleListMemberData.m_strSortColumn, m_oRoleListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listRole_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listRole_filter ()
{
	listRole_list (m_oRoleListMemberData.m_strSortColumn, m_oRoleListMemberData.m_strSortOrder, 1, 10);
}

function listRole_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oRoleListMemberData.m_oRoleData = oRowData;
	m_oRoleListMemberData.m_nIndex = nIndex;
	document.getElementById("listRole_div_listDetail").innerHTML = "";
	var oRoleData = new RoleData ();
	oRoleData.m_nRoleId = oRowData.m_nRoleId;
	RoleDataProcessor.getXML (oRoleData,function (strXMLData)
			{
		populateXMLData (strXMLData, "usermanagement/role/roleDetails.xslt", 'listRole_div_listDetail');
		listRole_initializeRoleDetailsDG ();
		RoleDataProcessor.get(oRoleData, listRole_gotActionData);
	});
}

function listRole_initializeRoleDetailsDG ()
{
	$('#roleDetails_table_roleDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strActionAreaName',title:'Action Area Name',sortable:true,width:50,
			  		formatter:function(value,row,index)
		        	{
						try
						{
							return row.m_oActionArea.m_strActionAreaName;
						}
						catch(oException)
						{
							
						}
		        	}		
			  	},
				{field:'m_strActionName',title:'Action Name',sortable:true,width:50},
				{field:'m_strActionTarget',title:'Target',sortable:true,width:150}
			]]
		}
	);
}

function listRole_gotActionData (oResponse)
{
	var oRoleData = oResponse.m_arrRoleData[0];
	$('#roleDetails_table_roleDetailsDG').datagrid('loadData',oRoleData.m_oActions);
}

function listRole_list (strColumnName, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumnName, "strColumnName is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oRoleListMemberData.m_strSortColumn = strColumnName;
	m_oRoleListMemberData.m_strSortOrder = strOrder;
	m_oRoleListMemberData.m_nPageNumber = nPageNumber;
	m_oRoleListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "listRole_progressbarLoaded ()");
}

function listRole_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oRoleData = listRole_getFormData ();
	RoleDataProcessor.list (oRoleData,m_oRoleListMemberData.m_strSortColumn, m_oRoleListMemberData.m_strSortOrder, m_oRoleListMemberData.m_nPageNumber, m_oRoleListMemberData.m_nPageSize, listRole_listed);
}

function listRole_displayImages (nRoleId, index)
{
	assert.isNumber(nRoleId, "nRoleId expected to be an isNumber.");
	assert.isNumber(index, "index is expected to be of type number");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId'+ nRoleId.m_nRoleId +'" onClick="listRole_edit('+ nRoleId  +')" /> </td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId'+ index +'" onClick="listRole_delete('+ index  +')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listRole_edit (nRoleId)
{
	assert.isNumber(nRoleId, "nRoleId expected to be a Number.");
	m_oRoleListMemberData.m_nSelectedRoleId = nRoleId;
	navigate ("rolelist", "widgets/usermanagement/role/editRole.js");
}

function listRole_showFilterPopup ()
{
	loadPage ("usermanagement/role/filterRole.html", "dialog", "filterRole_init ()");
}

function listRole_showAddPopup ()
{
	navigate ("newrole", "widgets/usermanagement/role/newRole.js");
}

function filterRole_init ()
{
	initFormValidateBoxes ("filterRole_form_id");
	createPopup('dialog', '#filterRole_button_cancel', '#filterRole_button_submit', true);
}

function filterRole_cancel ()
{
	HideDialog("dialog");
}

function listRole_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oRoleData = new RoleData ();
	var oListData = $("#listRole_table_roleListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oRoleData.m_nRoleId = oData.m_nRoleId;
	var bConfirm = getUserConfirmation ("usermessage_listactionarea_doyoureallywanttodelete")
	if (bConfirm == true)
		RoleDataProcessor.deleteData(oRoleData, listRole_deleted);
}

function listRole_listed (oRoleResponse)
{
	clearGridData ("#listRole_table_roleListDG");
	for(var nIndex = 0; nIndex < oRoleResponse.m_arrRoleData.length; nIndex++)
	{
		oRoleResponse.m_arrRoleData[nIndex].m_strRoleName;
		$("#listRole_table_roleListDG").datagrid('appendRow', oRoleResponse.m_arrRoleData[nIndex]);
	}
	$('#listRole_table_roleListDG').datagrid('getPager').pagination ({total:oRoleResponse.m_nRowCount, pageNumber:m_oRoleListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function listRole_deleted (oRoleResponse)
{
	if (oRoleResponse.m_bSuccess)
		informUser ("usermessage_listrole_roledeletedsuccessfully", "kSuccess");
	clearGridData ("#listRole_table_roleListDG");
	listRole_list (m_oRoleListMemberData.m_strSortColumn, m_oRoleListMemberData.m_strSortOrder, 1, 10);
}