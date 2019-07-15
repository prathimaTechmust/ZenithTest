var listActionArea_includeDataObjects = 
	[
		'widgets/usermanagement/actionarea/ActionAreaData.js',
	];

 includeDataObjects (listActionArea_includeDataObjects, "listActionArea_loaded()");

function listActionArea_loaded ()
{
	loadPage ("usermanagement/actionarea/listActionArea.html", "workarea", "listActionArea_init()");
}
	
function listActionArea_MemberData ()
{
	this.m_nActionAreaId = -1;
	this.m_oActionAreaData = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strActionAreaName";
    this.m_strSortOrder = "asc";
}

var m_olistActionArea_MemberData = new listActionArea_MemberData ();

function listActionArea_init ()
{
	listActionArea_createDataGrid ();
}

function listActionArea_getFormData()
{
	var oActionAreaData = new ActionAreaData;
	oActionAreaData.m_strActionAreaName = $("#filterActionArea_input_actionAreaName").val();
	return oActionAreaData;
}

function listActionArea_createDataGrid ()
{
	initHorizontalSplitter("#listActionArea_div_horizontalSplitter", "#listActionArea_table_dataGridId");
	$('#listActionArea_table_dataGridId').datagrid({
		fit:true,
	    columns:[[  
	        {field:'m_strActionAreaName',title:'Action Area Name',sortable:true,width:630},
	        {field:'m_nSequenceNumber',title:'Sequence Number',sortable:true,width:130,align:'center'},
	        {field:'Action',title:'Action',width:200,align:'center',
	        	formatter:function(value,row,index)
	        	{
	        		 return listActionArea_displayImages (row, index);
	        	}
            },
	    ]]
	});
	$('#listActionArea_table_dataGridId').datagrid (
	{
		onSortColumn: function (strColumnName, strOrder)
		{
			m_olistActionArea_MemberData.m_strSortColumn = strColumnName;
			m_olistActionArea_MemberData.m_strSortOrder = strOrder;
			listActionArea_list (strColumnName, strOrder, m_olistActionArea_MemberData.m_nPageNumber, m_olistActionArea_MemberData.m_nPageSize);
		}
	})
	$('#listActionArea_table_dataGridId').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				listActionArea_selectedRowData (rowData, rowIndex);
			}
		}
	)
	
	listActionArea_initDGPagination ();
	listActionArea_list (m_olistActionArea_MemberData.m_strSortColumn, m_olistActionArea_MemberData.m_strSortOrder, 1, 10);
}

function listActionArea_initDGPagination ()
{
	$('#listActionArea_table_dataGridId').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_olistActionArea_MemberData.m_nPageNumber = nPageNumber;
				listActionArea_list (m_olistActionArea_MemberData.m_strSortColumn, m_olistActionArea_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listActionArea_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_olistActionArea_MemberData.m_nPageNumber = nPageNumber;
				m_olistActionArea_MemberData.m_nPageSize = nPageSize;
				listActionArea_list (m_olistActionArea_MemberData.m_strSortColumn, m_olistActionArea_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listActionArea_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listActionArea_filter ()
{
	listActionArea_list (m_olistActionArea_MemberData.m_strSortColumn, m_olistActionArea_MemberData.m_strSortOrder, 1, 10);
}

function listActionArea_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_olistActionArea_MemberData.m_oActionAreaData = oRowData;
	m_olistActionArea_MemberData.m_nIndex = nIndex;
	document.getElementById("listActionArea_div_listDetail").innerHTML = "";
	var oActionAreaData = new ActionAreaData ();
	oActionAreaData.m_nActionAreaId = oRowData.m_nActionAreaId;
	ActionAreaDataProcessor.getXML (oActionAreaData, listActionArea_gotXML);
}

function listActionArea_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "usermanagement/actionarea/actionAreaDetails.xslt", 'listActionArea_div_listDetail');
}

function listActionArea_list (strColumnName, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumnName, "strColumnName is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_olistActionArea_MemberData.m_strSortColumn = strColumnName;
	m_olistActionArea_MemberData.m_strSortOrder = strOrder;
	m_olistActionArea_MemberData.m_nPageNumber = nPageNumber;
	m_olistActionArea_MemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "listActionArea_progressbarLoaded ()");
}

function listActionArea_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oActionAreaData = listActionArea_getFormData ();
	ActionAreaDataProcessor.list(oActionAreaData, m_olistActionArea_MemberData.m_strSortColumn, m_olistActionArea_MemberData.m_strSortOrder, m_olistActionArea_MemberData.m_nPageNumber, m_olistActionArea_MemberData.m_nPageSize, listActionArea_listed);
}

function listActionArea_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index is expected to be of type number");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" onClick="listActionArea_edit('+ row.m_nActionAreaId  +')" width="20" /> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="listActionArea_delete('+index+')" width="20"/> </td>'+
						'</tr>'+
					'</table>'
   return oImage;
}

function listActionArea_edit (nActionAreaId)
{
	assert.isNumber(nActionAreaId, "nActionAreaId expected to be a Number.");
	m_olistActionArea_MemberData.m_nActionAreaId = nActionAreaId;
	navigate("actionarea", "widgets/usermanagement/actionarea/editActionArea.js");
}

function listActionArea_showFilterPopup () 
{
	loadPage ("usermanagement/actionarea/filterActionArea.html", "dialog", "filterActionArea_init ()");
}

function listActionArea_showAddPopup ()
{
	navigate ("newactionarea", "widgets/usermanagement/actionarea/newActionArea.js");
}

function filterActionArea_init ()
{
	createPopup('dialog', '#filterActionArea_button_cancel', '#filterActionArea_button_submit', true);
}

function filterActionArea_cancel ()
{
	HideDialog("dialog");
}

function listActionArea_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oActionAreaData = new ActionAreaData();
	var oListData = $("#listActionArea_table_dataGridId").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oActionAreaData.m_nActionAreaId = oData.m_nActionAreaId;
	var bConfirm = getUserConfirmation("usermessage_listactionarea_doyoureallywanttodelete")
	if (bConfirm == true)
		ActionAreaDataProcessor.deleteData(oActionAreaData,listActionArea_deleted);
}

function listActionArea_listed (oActionAreaResponse)
{
	clearGridData ("#listActionArea_table_dataGridId");
	for (var nRecord = 0; nRecord < oActionAreaResponse.m_arrActionArea.length; nRecord++)
		$('#listActionArea_table_dataGridId').datagrid ('appendRow', oActionAreaResponse.m_arrActionArea[nRecord]);
	$('#listActionArea_table_dataGridId').datagrid('getPager').pagination ({total:oActionAreaResponse.m_nRowCount, pageNumber:m_olistActionArea_MemberData.m_nPageNumber});
	HideDialog("dialog");
}

function listActionArea_deleted (oActionAreaResponse)
{
	if(oActionAreaResponse.m_bSuccess)
	{
		informUser("usermessage_listactionarea_deletedsuccessfully" , "kSuccess");
		clearGridData ("#listActionArea_table_dataGridId");
		listActionArea_list (m_olistActionArea_MemberData.m_strSortColumn, m_olistActionArea_MemberData.m_strSortOrder, 1, 10);
	}
}