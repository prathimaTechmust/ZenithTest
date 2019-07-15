var listAction_includeDataObjects = 
[
	'widgets/usermanagement/action/ActionData.js',
	'widgets/usermanagement/actionarea/ActionAreaData.js'
];

includeDataObjects (listAction_includeDataObjects, "listAction_loaded()");

function listAction_loaded ()
{
	loadPage ("usermanagement/action/listAction.html", "workarea", "listAction_init ()");
	listAction_populateActionAreaList ('filterAction_select_actionAreaName');
}

function listAction_memberData ()
{
	this.m_nSelectedActionId = -1;
	this.m_oActionData = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 50;
    this.m_strSortColumn = "m_strActionName";
    this.m_strSortOrder = "asc";
}

var m_oActionListMemberData = new listAction_memberData ();

function listAction_init ()
{
	listAction_createDataGrid ();
}

function listAction_getFormData ()
{
	var oActionData = new ActionData ();
	oActionData.m_oActionArea = new ActionAreaData ();
	oActionData.m_oActionArea.m_nActionAreaId = $("#filterAction_select_actionAreaName").val();
	oActionData.m_strActionName = $("#filterAction_input_actionName").val();
	oActionData.m_strActionTarget = $("#filterAction_input_actionTarget").val();
	return oActionData;
}

function listAction_createDataGrid ()
{
	initHorizontalSplitter("#listAction_div_horizontalSplitter", "#listAction_table_actions");
	$('#listAction_table_actions').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'ActionAreaName',title:'Area Name',sortable:true,width:150,
					formatter:function(value,oActionData,index)
					{
						return oActionData.m_oActionArea.m_strActionAreaName;
					}
				},
				{field:'m_strActionName',title:'Action Name',sortable:true,width:110},
				{field:'m_strActionTarget',title:'Target',sortable:true,width:300},
				{field:'m_nSequenceNumber',title:'Sequence Number',sortable:true,width:110,align:'center'},
				{field:'Actions',title:'Action',width:80,
					formatter:function(value,row,index)
		        	{
		        		return listAction_displayImages (row, index);
		        	}
				},
			]],
			onSortColumn: function (strColumn, strOrder) 
			{
				if(strColumn == 'ActionAreaName')
					strColumn = 'm_nSequenceNumber';
				m_oActionListMemberData.m_strSortColumn = strColumn;
				m_oActionListMemberData.m_strSortOrder = strOrder;
				listAction_list (strColumn, strOrder, m_oActionListMemberData.m_nPageNumber, m_oActionListMemberData.m_nPageSize);
			},
			onSelect: function (rowIndex, rowData)
			{
				listAction_selectedRowData (rowData, rowIndex);
			}
		}
	);
	
	listAction_initDGPagination ();
	listAction_list (m_oActionListMemberData.m_strSortColumn, m_oActionListMemberData.m_strSortOrder, m_oActionListMemberData.m_nPageNumber, m_oActionListMemberData.m_nPageSize);
}

function listAction_initDGPagination ()
{
	$('#listAction_table_actions').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
//				$('#listAction_table_actions').datagrid('getPager').pagination ('refresh');
				m_oActionListMemberData.m_nPageNumber = nPageNumber;
				listAction_list (m_oActionListMemberData.m_strSortColumn, m_oActionListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listAction_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oActionListMemberData.m_nPageNumber = nPageNumber;
				m_oActionListMemberData.m_nPageSize = nPageSize;
				listAction_list (m_oActionListMemberData.m_strSortColumn, m_oActionListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listAction_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listAction_selectedRowData (oRowData, nIndex)
{ 
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oActionListMemberData.m_oActionData = oActionData;
	m_oActionListMemberData.m_nIndex = nIndex;
	document.getElementById("listAction_div_listDetail").innerHTML = "";
	var oActionData = new ActionData ();
	oActionData.m_nActionId = oRowData.m_nActionId;
	ActionDataProcessor.getXML (oActionData, listAction_gotXML);
}

function listAction_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "usermanagement/action/actionDetails.xslt", 'listAction_div_listDetail');
}

function listAction_filter ()
{
	listAction_list (m_oActionListMemberData.m_strSortColumn, m_oActionListMemberData.m_strSortOrder, 1, 10);
}

function listAction_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oActionListMemberData.m_strSortColumn = strColumn;
	m_oActionListMemberData.m_strSortOrder = strOrder;
	m_oActionListMemberData.m_nPageNumber = nPageNumber;
	m_oActionListMemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "listAction_progressbarLoaded ()");
}

function listAction_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oActionData = listAction_getFormData ();
	ActionDataProcessor.list(oActionData, m_oActionListMemberData.m_strSortColumn, m_oActionListMemberData.m_strSortOrder, m_oActionListMemberData.m_nPageNumber, m_oActionListMemberData.m_nPageSize, listAction_listed);
}

function listAction_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index is expected to be of type number");
	var oImage = 	'<table align="center">'+
						'<tr>'+
//							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="listAction_edit ('+row.m_nActionId+')"/> </td>'+
							'<td> <i class="edit_database_24" width="20" align="center" id="editImageId" onClick="listAction_edit ('+row.m_nActionId+')"/> </td>'+	
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="listAction_delete ('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listAction_edit (nActionId)
{
	assert.isNumber(nActionId, "nActionId expected to be a Number.");
	m_oActionListMemberData.m_nSelectedActionId = nActionId;
	navigate ("actionInformation", "widgets/usermanagement/action/editAction.js");
}

function listAction_showFilterPopup ()
{
	loadPage ("usermanagement/action/filterAction.html", "dialog", "filterAction_init ()");
	listAction_populateActionAreaList ();
}

function listAction_showAddPopup ()
{
	navigate ("newaction", "widgets/usermanagement/action/newAction.js");
}

function filterAction_init ()
{
	createPopup('dialog', '#filterAction_button_cancel', '#filterAction_button_submit', true);
}

function filterAction_cancel ()
{
	HideDialog("dialog");
}

function listAction_delete (nIndex)
{ 
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
 	var oActionData = new ActionData ();
	var oListData = $("#listAction_table_actions").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oActionData.m_nActionId = oData.m_nActionId;
	var bConfirm = getUserConfirmation("usermessage_listactionarea_doyoureallywanttodelete")
	if(bConfirm)
		ActionDataProcessor.deleteData(oActionData, listAction_deleted);
}

function listAction_listed (oActionResponse)
{
	clearGridData ("#listAction_table_actions");
	$('#listAction_table_actions').datagrid('loadData',oActionResponse.m_arrActionData);
	$('#listAction_table_actions').datagrid('getPager').pagination ({total:oActionResponse.m_nRowCount, pageNumber:m_oActionListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function listAction_deleted (oActionResponse)
{
	if(oActionResponse.m_bSuccess)
		informUser ("usermessage_listaction_actiondeletedsuccessfully", "kSuccess");
	document.getElementById("listAction_div_listDetail").innerHTML = "";
	clearGridData ("#listAction_table_actions");
	listAction_list (m_oActionListMemberData.m_strSortColumn, m_oActionListMemberData.m_strSortOrder, 1, 10);
}

function listAction_populateActionAreaList ()
{
	var oActionData = listAction_getFormData ();
	ActionAreaDataProcessor.list (oActionData.m_oActionArea, "m_strActionAreaName", "asc", 0, 0,listAction_prepareActionAreaDD) 
}

function listAction_prepareActionAreaDD (oActionAreaResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption(-1, "Select"));
	for (var nIndex = 0; nIndex < oActionAreaResponse.m_arrActionArea.length; nIndex++)
		arrOptions.push (CreateOption (oActionAreaResponse.m_arrActionArea [nIndex].m_nActionAreaId,
				oActionAreaResponse.m_arrActionArea [nIndex].m_strActionAreaName));
	PopulateDD ('filterAction_select_actionAreaName', arrOptions);
}