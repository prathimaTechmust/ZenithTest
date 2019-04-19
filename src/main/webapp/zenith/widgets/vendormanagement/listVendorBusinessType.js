var listBusinessType_includeDataObjects = 
[
	'widgets/vendormanagement/VendorBusinessTypeData.js'
];

 includeDataObjects (listBusinessType_includeDataObjects, "listBusinessType_loaded()");

function listBusinessType_loaded ()
{
	loadPage ("vendormanagement/listVendorBusinessType.html", "workarea", "listBusinessType_init ()");
}

function listBusinessType_memberData ()
{
	this.m_nSelectedBusinessTypeId = -1;
	this.m_oBusinessTypeData = null;
	this.m_nIndex = -1;
}

var m_olistBusinessType_memberData = new listBusinessType_memberData ();

function listBusinessType_init ()
{
	listBusinessType_createDataGrid ();
}

function listBusinessType_getFormData () 
{
	var oBusinessTypeData = new BusinessTypeData ();
	oBusinessTypeData.m_strBusinessName = dwr.util.getValue("filterBusinessType_input_businessTypeName");
	return oBusinessTypeData;
}

function listBusinessType_createDataGrid ()
{
	$('#listVendorBusinessType_div_horizontalSplitter').css({width: '100%', height: '600'}).split ({orientation: 'horizontal', limit: 300, position:'50%'});
	$('#listBusinessType_table_business').datagrid
	(
		{
			columns:
			[[
				{field:'m_nBusinessTypeId',title:'Id',sortable:true,width:30,align:'center'},
				{field:'m_strBusinessName',title:'Business Name',sortable:true,width:750},
				{field:'Actions',title:'Action',width:180,align:'center',
					formatter:function(value,row,index)
					{
						return listBusinessType_displayImages (row, index);
					}
				},
			]]
		}
	);
	
	$('#listBusinessType_table_business').datagrid
	(
		{
			onSortColumn: function (strColumn, strOrder)
			{
				listBusinessType_list (strColumn, strOrder);
			}
		}
	)
	
	$('#listBusinessType_table_business').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				listBusinessType_selectedRowData (rowData,rowIndex);
			}
		}
	)
	
	listBusinessType_list ();
}

function listBusinessType_selectedRowData (oRowData, nIndex)
{
	m_olistBusinessType_memberData.m_oBusinessTypeData = oRowData;
	m_olistBusinessType_memberData.m_nIndex = nIndex;
	document.getElementById("listVendorBusinessType_div_listDetail").style.height = "auto";
	document.getElementById("listVendorBusinessType_div_listDetail").innerHTML = "";
	var oBusinessTypeData = new BusinessTypeData ();
	oBusinessTypeData.m_nBusinessTypeId = oRowData.m_nBusinessTypeId;
	BusinessTypeDataProcessor.getXML (oBusinessTypeData,	{
		async:false, 
		callback: function (strXMLData)
		{
		populateXMLData (oBusinessTypeData.m_strXMLData, "vendormanagement/vendorBusinessTypeDetails.xslt", 'listVendorBusinessType_div_listDetail');
		}
	});
}

function listBusinessType_list (strColumn, strOrder)
{
	clearGridData ("#listBusinessType_table_business");
	dwr.engine.setAsync(false);	
	var oBusinessTypeData = listBusinessType_getFormData ();
	filterBusinessType_cancel ();
	BusinessTypeDataProcessor.list(oBusinessTypeData, strColumn, strOrder, listBusinessType_listed );
	dwr.engine.setAsync(true);	
}

function listBusinessType_displayImages (row, index)
{
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" onClick="listBusinessType_edit('+ row.m_nBusinessTypeId +')"/> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="listBusinessType_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listBusinessType_edit (nBusinessTypeId)
{	
	m_olistBusinessType_memberData.m_nSelectedBusinessTypeId = nBusinessTypeId;
	navigate ("vendorBusinessInformation","widgets/vendormanagement/vendorEditBusinessType.js");
}

function listBusinessType_showFilterPopup ()
{
	loadPage ("vendormanagement/vendorFilterBusinessType.html", "dialog", "filterBusinessType_init ()");
}

function listBusinessType_showAddBusinessPopup ()
{
	navigate ("VendorBusinessType", "widgets/vendormanagement/vendorNewBusinessType.js");
}

function filterBusinessType_init ()
{
	createPopup('dialog', '#filterBusinessType_button_cancel', '#filterBusinessType_button_submit', true);
}

function filterBusinessType_cancel ()
{
	HideDialog("dialog");
}

function listBusinessType_listDetail_edit ()
{
	m_olistBusinessType_memberData.m_nSelectedBusinessTypeId = m_olistBusinessType_memberData.m_oBusinessTypeData.m_nBusinessTypeId;
	navigate ("vendorBusinessInformation","widgets/vendormanagement/vendorEditBusinessType.js");
}

function listBusinessType_listDetail_delete ()
{
	listBusinessType_delete (m_olistBusinessType_memberData.m_nIndex);
}

function listBusinessType_delete (nIndex)
{
	var oBusinessTypeData = new BusinessTypeData();
	var oListData = $("#listBusinessType_table_business").datagrid('getData');
	var oData = oListData.rows[nIndex];	
	oBusinessTypeData.m_nBusinessTypeId = oData.m_nBusinessTypeId;
	var bConfirm = getUserConfirmation("Do you really want to delete " + oData.m_strBusinessName + "?")
	if (bConfirm)
		BusinessTypeDataProcessor.deleteData(oBusinessTypeData,listBusinessType_deleted); 
}

function listBusinessType_listed (oBusinessTypeResponse)
{
		$('#listBusinessType_table_business').datagrid('loadData',oBusinessTypeResponse.m_arrBusinessType);

}

function listBusinessType_deleted (oBusinessTypeResponse)
{
	var oBusinessTypeData = new BusinessTypeData();
	if(oBusinessTypeResponse.m_bSuccess)
	informUser("Business type deleted successfully", "kSuccess");
	clearGridData ("#listBusinessType_table_business");
	listBusinessType_list();
}
