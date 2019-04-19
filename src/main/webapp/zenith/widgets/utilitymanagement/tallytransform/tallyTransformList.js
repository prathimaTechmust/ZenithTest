var tallyTransformList_includeDataObjects = 
[
	'widgets/utilitymanagement/tallytransform/TallyTransformData.js'
];

 includeDataObjects (tallyTransformList_includeDataObjects, "tallyTransformList_loaded()");

function tallyTransformList_loaded ()
{
	loadPage ("utilitymanagement/tallytransform/tallyTransformList.html", "workarea", "tallyTransformList_init()");
}
	
function tallyTransformList_MemberData ()
{
	this.m_nTallyTranformId = -1;
	this.m_oTallyTranform = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strKey";
    this.m_strSortOrder = "asc";
}

var m_oTallyTransformListMemberData = new tallyTransformList_MemberData ();

function tallyTransformList_init ()
{
	tallyTransformList_createDataGrid ();
}

function tallyTransformList_getFormData ()
{
	var oTallyTransformData = new TallyTransformData ();
	oTallyTransformData.m_strTaxName = dwr.util.getValue("tallyTransformList_input_taxname");
	oTallyTransformData.m_nPercentage = dwr.util.getValue("tallyTransformList_input_percentage");
	return oTallyTransformData;
}

function tallyTransformList_createDataGrid ()
{
	initHorizontalSplitter("#tallyTransformList_div_horizontalSplitter", "#tallyTransformList_table_tallyTransformDG");
	$('#tallyTransformList_table_tallyTransformDG').datagrid({
		fit:true,
	    columns:[[  
            {field:'m_strKey',title:'Key',sortable:true,width:140},
	        {field:'m_strTaxName',title:'Tax Name',sortable:true,width:70},
	        {field:'m_nPercentage',title:'Percentage',sortable:true,width:70,align:'center'},
	        {field:'m_strTallyKey',title:'Tally Key',sortable:true,width:140},
	        {field:'Action',title:'Action',width:60,align:'center',
	        	formatter:function(value,row,index)
	        	{
	        		 return tallyTransformList_displayImages (row, index);
	        	}
            },
	    ]]
	});
	$('#tallyTransformList_table_tallyTransformDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				tallyTransformList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				m_oTallyTransformListMemberData.m_strSortColumn = strColumn;
				m_oTallyTransformListMemberData.m_strSortOrder = strOrder;
				tallyTransformList_list (strColumn, strOrder, m_oTallyTransformListMemberData.m_nPageNumber, m_oTallyTransformListMemberData.m_nPageSize);
			}
			
		}
	)
	tallyTransformList_initDGPagination ();
	tallyTransformList_list (m_oTallyTransformListMemberData.m_strSortColumn, m_oTallyTransformListMemberData.m_strSortOrder, 1, 10);
}

function tallyTransformList_initDGPagination ()
{
	$('#tallyTransformList_table_tallyTransformDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oTallyTransformListMemberData.m_nPageNumber = $('#tallyTransformList_table_tallyTransformDG').datagrid('getPager').pagination('options').pageNumber;
				tallyTransformList_list (m_oTallyTransformListMemberData.m_strSortColumn, m_oTallyTransformListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("tallyTransformList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oTallyTransformListMemberData.m_nPageNumber = $('#tallyTransformList_table_tallyTransformDG').datagrid('getPager').pagination('options').pageNumber;
				m_oTallyTransformListMemberData.m_nPageSize = $('#tallyTransformList_table_tallyTransformDG').datagrid('getPager').pagination('options').pageSize;
				tallyTransformList_list (m_oTallyTransformListMemberData.m_strSortColumn, m_oTallyTransformListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("tallyTransformList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function tallyTransformList_filter ()
{
	tallyTransformList_list (m_oTallyTransformListMemberData.m_strSortColumn, m_oTallyTransformListMemberData.m_strSortOrder, 1, 10);
}

function tallyTransformList_selectedRowData (oRowData, nIndex)
{
	m_oTallyTransformListMemberData.m_oTallyTranform = oRowData;
	m_oTallyTransformListMemberData.m_nIndex = nIndex;
	document.getElementById("tallyTransformList_div_listDetail").innerHTML = "";
	var oTallyTransformData = new TallyTransformData ();
	oTallyTransformData.m_nTallyTranformId = oRowData.m_nTallyTranformId;
	TallyTransformDataProcessor.getXML (oTallyTransformData,	
	{
		async:false, 
		callback: function (strXMLData)
		{
			populateXMLData (strXMLData, "utilitymanagement/tallytransform/TallyTransformDetails.xslt", 'tallyTransformList_div_listDetail');
		}
	});
}

function tallyTransformList_list (strColumnName, strOrder, nPageNumber, nPageSize)
{
	var oTallyTransformData = tallyTransformList_getFormData ();
	tallyTransformList_cancel ();
	TallyTransformDataProcessor.list(oTallyTransformData, strColumnName, strOrder, nPageNumber, nPageSize, tallyTransformList_listed);
	dwr.engine.setAsync(true);
}

function tallyTransformList_listed (oResponse)
{
	$('#tallyTransformList_table_tallyTransformDG').datagrid ('loadData', oResponse.m_arrTallyTransformData);
	$('#tallyTransformList_table_tallyTransformDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oTallyTransformListMemberData.m_nPageNumber});
}

function tallyTransformList_displayImages (row, index)
{
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" onClick="tallyTransformList_edit('+ row.m_nTallyTranformId  +')" width="20" /> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="tallyTransformList_delete('+index+')" width="20"/> </td>'+
						'</tr>'+
					'</table>'
   return oImage;
}

function tallyTransformList_edit (nTaxId)
{
	m_oTallyTransformListMemberData.m_nTallyTranformId = nTaxId;
	navigate("EditTallyTransform", "widgets/utilitymanagement/tallytransform/editTallyTransform.js");
}

function tallyTransformList_listDetail_edit ()
{
	m_oTallyTransformListMemberData.m_nTallyTranformId = m_oTallyTransformListMemberData.m_oTallyTranform.m_nTallyTranformId;
	navigate("EditTallyTransform", "widgets/utilitymanagement/tallytransform/editTallyTransform.js");
}

function tallyTransformList_showAddPopup ()
{
	navigate ("newTallyTransform", "widgets/utilitymanagement/tallytransform/newTallyTransform.js");
}

function tallyTransformList_cancel ()
{
	HideDialog("dialog");
}

function tallyTransformList_listDetail_delete ()
{
	tallyTransformList_delete(m_oTallyTransformListMemberData.m_nIndex);
}

function tallyTransformList_delete (nIndex)
{
	var oTallyTransformData = new TallyTransformData();
	var oListData = $("#tallyTransformList_table_tallyTransformDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oTallyTransformData.m_nTallyTranformId = oData.m_nTallyTranformId;
	var bConfirm = getUserConfirmation("Do You Really want to Delete ?")
	if (bConfirm == true)
		TallyTransformDataProcessor.deleteData(oTallyTransformData,tallyTransformList_deleted);
}

function tallyTransformList_deleted (oResponse)
{
	var oTallyTransformData = new TallyTransformData ();
	if(oResponse.m_bSuccess)
	{
		informUser("Tally Transform Information Deleted Successfully", "kSuccess");
		document.getElementById("tallyTransformList_div_listDetail").innerHTML = "";
		clearGridData ("#tallyTransformList_table_tallyTransformDG");
		TallyTransformDataProcessor.list(oTallyTransformData, "", "", tallyTransformList_listed);
	}
}