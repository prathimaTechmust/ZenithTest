var tallyTransformKeyList_includeDataObjects = 
[
	'widgets/utilitymanagement/tallytransform/TallyTransformKeyData.js'
];

 includeDataObjects (tallyTransformKeyList_includeDataObjects, "tallyTransformKeyList_loaded()");

function tallyTransformKeyList_loaded ()
{
	loadPage ("utilitymanagement/tallytransform/tallyTransformKeyList.html", "workarea", "tallyTransformKeyList_init()");
}
	
function tallyTransformKeyList_MemberData ()
{
	this.m_nTallyTranformKeyId = -1;
	this.m_oTallyTranformKey = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strKey";
    this.m_strSortOrder = "asc";
}

var m_oTallyTransformKeyListMemberData = new tallyTransformKeyList_MemberData ();

function tallyTransformKeyList_init ()
{
	tallyTransformKeyList_createDataGrid ();
}

function tallyTransformKeyList_getFormData ()
{
	var oTallyTransformKeyData = new TallyTransformKeyData ();
	oTallyTransformKeyData.m_strKey = dwr.util.getValue("tallyTransformKeyList_input_key");
	return oTallyTransformKeyData;
}

function tallyTransformKeyList_createDataGrid ()
{
	initHorizontalSplitter("#tallyTransformKeyList_div_horizontalSplitter", "#tallyTransformKeyList_table_tallyTransformKeyDG");
	$('#tallyTransformKeyList_table_tallyTransformKeyDG').datagrid({
		fit:true,
	    columns:[[  
            {field:'m_strKey',title:'Key',sortable:true,width:140},
	        {field:'Action',title:'Action',width:60,align:'center',
	        	formatter:function(value,row,index)
	        	{
	        		 return tallyTransformKeyList_displayImages (row, index);
	        	}
            },
	    ]]
	});
	$('#tallyTransformKeyList_table_tallyTransformKeyDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				tallyTransformKeyList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				m_oTallyTransformKeyListMemberData.m_strSortColumn = strColumn;
				m_oTallyTransformKeyListMemberData.m_strSortOrder = strOrder;
				tallyTransformKeyList_list (strColumn, strOrder, m_oTallyTransformKeyListMemberData.m_nPageNumber, m_oTallyTransformKeyListMemberData.m_nPageSize);
			}
			
		}
	)
	tallyTransformKeyList_initDGPagination ();
	tallyTransformKeyList_list (m_oTallyTransformKeyListMemberData.m_strSortColumn, m_oTallyTransformKeyListMemberData.m_strSortOrder, 1, 10);
}

function tallyTransformKeyList_initDGPagination ()
{
	$('#tallyTransformKeyList_table_tallyTransformKeyDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oTallyTransformKeyListMemberData.m_nPageNumber = nPageNumber;
				tallyTransformKeyList_list (m_oTallyTransformKeyListMemberData.m_strSortColumn, m_oTallyTransformKeyListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("tallyTransformKeyList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oTallyTransformKeyListMemberData.m_nPageNumber = nPageNumber;
				m_oTallyTransformKeyListMemberData.m_nPageSize = nPageSize;
				tallyTransformKeyList_list (m_oTallyTransformKeyListMemberData.m_strSortColumn, m_oTallyTransformKeyListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("tallyTransformKeyList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function tallyTransformKeyList_filter ()
{
	tallyTransformKeyList_list (m_oTallyTransformKeyListMemberData.m_strSortColumn, m_oTallyTransformKeyListMemberData.m_strSortOrder, 1, 10);
}

function tallyTransformKeyList_selectedRowData (oRowData, nIndex)
{
	m_oTallyTransformKeyListMemberData.m_oTallyTranformKey = oRowData;
	m_oTallyTransformKeyListMemberData.m_nIndex = nIndex;
	document.getElementById("tallyTransformKeyList_div_listDetail").innerHTML = "";
	var oTallyTransformKeyData = new TallyTransformKeyData ();
	oTallyTransformKeyData.m_nTallyTranformKeyId = oRowData.m_nTallyTranformKeyId;
	TallyTransformKeyDataProcessor.getXML (oTallyTransformKeyData,	
	{
		async:false, 
		callback: function (strXMLData)
		{
			populateXMLData (strXMLData, "utilitymanagement/tallytransform/TallyTransformKeyDetails.xslt", 'tallyTransformKeyList_div_listDetail');
		}
	});
}

function tallyTransformKeyList_list (strColumnName, strOrder, nPageNumber, nPageSize)
{
	var oTallyTransformKeyData = tallyTransformKeyList_getFormData ();
	tallyTransformKeyList_cancel ();
	TallyTransformKeyDataProcessor.list(oTallyTransformKeyData, strColumnName, strOrder, nPageNumber, nPageSize, tallyTransformKeyList_listed);
	dwr.engine.setAsync(true);
}

function tallyTransformKeyList_listed (oResponse)
{
	$('#tallyTransformKeyList_table_tallyTransformKeyDG').datagrid ('loadData', oResponse.m_arrTallyTransformKeyData);
	$('#tallyTransformKeyList_table_tallyTransformKeyDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oTallyTransformKeyListMemberData.m_nPageNumber});
}

function tallyTransformKeyList_displayImages (row, index)
{
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" onClick="tallyTransformKeyList_edit('+ row.m_nTallyTranformKeyId  +')" width="20" /> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="tallyTransformKeyList_delete('+index+')" width="20"/> </td>'+
						'</tr>'+
					'</table>'
   return oImage;
}

function tallyTransformKeyList_edit (nKeyId)
{
	m_oTallyTransformKeyListMemberData.m_nTallyTranformKeyId = nKeyId;
	navigate("EditTallyTransformKey", "widgets/utilitymanagement/tallytransform/editTallyTransformKey.js");
}

function tallyTransformKeyList_showAddPopup ()
{
	navigate ("newTallyTransform", "widgets/utilitymanagement/tallytransform/newTallyTransformKey.js");
}

function tallyTransformKeyList_cancel ()
{
	HideDialog("dialog");
}

function tallyTransformKeyList_delete (nIndex)
{
	var oTallyTransformKeyData = new TallyTransformKeyData ();
	var oListData = $("#tallyTransformKeyList_table_tallyTransformKeyDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oTallyTransformKeyData.m_nTallyTranformKeyId = oData.m_nTallyTranformKeyId;
	var bConfirm = getUserConfirmation("Do You Really want to Delete ?")
	if (bConfirm == true)
		TallyTransformKeyDataProcessor.deleteData(oTallyTransformKeyData,tallyTransformKeyList_deleted);
}

function tallyTransformKeyList_deleted (oResponse)
{
	var oTallyTransformKeyData = new TallyTransformKeyData ();
	if(oResponse.m_bSuccess)
	{
		informUser("Tally Transform Key Deleted Successfully", "kSuccess");
		document.getElementById("tallyTransformKeyList_div_listDetail").innerHTML = "";
		clearGridData ("#tallyTransformKeyList_table_tallyTransformKeyDG");
		TallyTransformKeyDataProcessor.list(oTallyTransformKeyData, "", "", tallyTransformKeyList_listed);
	}
}