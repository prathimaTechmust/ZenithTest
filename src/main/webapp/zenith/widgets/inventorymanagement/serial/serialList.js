var serialList_includeDataObjects = 
[
	'widgets/inventorymanagement/serial/SerialNumberData.js'
];



includeDataObjects (serialList_includeDataObjects, "serialList_loaded()");

function serialList_loaded ()
{
	loadPage ("inventorymanagement/serial/serialList.html", "workarea", "serialList_init ()");
}

function serialList_memberData ()
{
	this.m_nSelectedId = -1;
}

var m_oSerialListMemberData = new serialList_memberData ();

function serialList_init ()
{
	serialList_initializeDataGrid ();
}

function serialList_initializeDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#serialList_table_serialListDG");
	$('#serialList_table_serialListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strSerialTypeName',title:'Serial Type',width:200},
				{field:'m_nSerialNumber',title:'Serial Number',width:150,align:'right'},
				{field:'m_strPrefix',title:'Prefix',width:150,align:'right'},
				{field:'Actions',title:'Action',width:50,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return serialList_addActions (row, index);
		        	}
				},
			]]
		}
	);
	
	serialList_list ("", "");
}

function serialList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="serialList_edit ('+row.m_nSerialId+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function serialList_edit (nSerialId)
{
	assert.isNumber(nSerialId, "nSerialId expected to be a Number.");
	assert(nSerialId !== 0, "nSerialId cannot be equal to zero.")
	m_oSerialListMemberData.m_nSelectedId = nSerialId;
	navigate ("serialEdit", "widgets/inventorymanagement/serial/serialEdit.js");
}

function serialList_list ()
{
	clearGridData ("#serialList_table_serialListDG");
	var oSerialNumberData = new SerialNumberData ();
	SerialNumberDataProcessor.list(oSerialNumberData, "", "", serialList_listed);
}

function serialList_listed (oResponse)
{
	$('#serialList_table_serialListDG').datagrid('loadData',oResponse.m_arrSerialNumber);
	$('#serialList_table_serialListDG').datagrid('acceptChanges');
}

function serialList_showAddPopup ()
{
	navigate ("serialNew", "widgets/inventorymanagement/serial/serialNew.js");
}

