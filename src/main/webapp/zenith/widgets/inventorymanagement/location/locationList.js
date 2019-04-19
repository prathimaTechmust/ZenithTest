var locationList_includeDataObjects = 
[
	'widgets/inventorymanagement/location/LocationData.js'
];


includeDataObjects (locationList_includeDataObjects, "locationList_loaded()");

function locationList_loaded ()
{
	loadPage ("inventorymanagement/location/locationList.html", "workarea", "locationList_init ()");
}

function locationList_init ()
{
	locationList_createDataGrid ();
}

function locationList_MemberData ()
{
	this.m_nLocationId = -1;
	this.m_oLocationData = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = 'm_strName';
    this.m_strSortOrder = 'desc';
}

var m_oLocationList_memberData = new locationList_MemberData ();

function locationList_createDataGrid ()
{
	initHorizontalSplitter("#locationList_div_horizontalSplitter", "#locationList_table_locationListDG");
	$('#locationList_table_locationListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strName',title:'Name',sortable:true,width:150},
			  	{field:'m_strAddress',title:'Address',sortable:true,width:250},
			  	{field:'Action',title:'Action',width:60,align:'center',
		        	formatter:function(value,row,index)
		        	{
		        		 return listLocation_displayImages (row, index);
		        	}
	            }
			]]
		}
	);
	
	$('#locationList_table_locationListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				locationList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				m_oLocationList_memberData.m_strSortColumn = strColumn;
				m_oLocationList_memberData.m_strSortOrder = strOrder;
				locationList_list (strColumn, strOrder, m_oLocationList_memberData.m_nPageNumber, m_oLocationList_memberData.m_nPageSize);
			}
		}
	)
	
	locationList_initDGPagination ()
	locationList_list (m_oLocationList_memberData.m_strSortColumn, m_oLocationList_memberData.m_strSortOrder, 1, 10);
}

function locationList_initDGPagination ()
{
	$('#locationList_table_locationListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oLocationList_memberData.m_nPageNumber = nPageNumber;
				locationList_list (m_oLocationList_memberData.m_strSortColumn, m_oLocationList_memberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("locationList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oLocationList_memberData.m_nPageNumber = nPageNumber;
				m_oLocationList_memberData.m_nPageSize = nPageSize;
				locationList_list (m_oLocationList_memberData.m_strSortColumn, m_oLocationList_memberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("locationList_div_listDetail").innerHTML = "";
			}
		}
	)
	
}

function locationList_filter ()
{
	locationList_list (m_oLocationList_memberData.m_strSortColumn, m_oLocationList_memberData.m_strSortOrder, 1, 10);
}

function locationList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oLocationList_memberData.m_strSortColumn = strColumn;
	m_oLocationList_memberData.m_strSortOrder = strOrder;
	m_oLocationList_memberData.m_nPageNumber = nPageNumber;
	m_oLocationList_memberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "locationList_progressbarLoaded ()");
}

function locationList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oLocationData = locationList_getLocationData ();
	LocationDataProcessor.list(oLocationData, m_oLocationList_memberData.m_strSortColumn, m_oLocationList_memberData.m_strSortOrder, m_oLocationList_memberData.m_nPageNumber, m_oLocationList_memberData.m_nPageSize, locationList_listed);
}

function locationList_getLocationData ()
{
	var oLocationData = new LocationData ();
	oLocationData.m_strName = $("#filterLocation_input_name").val();
	return oLocationData;
}

function locationList_listed (oResponse)
{
	clearGridData ("#locationList_table_locationListDG");
	$('#locationList_table_locationListDG').datagrid('loadData',oResponse.m_arrLocations);
	$('#locationList_table_locationListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oLocationList_memberData.m_nPageNumber});
	HideDialog ("dialog");
}

function locationList_cancel ()
{
	HideDialog ("dialog");
}

function  listLocation_displayImages (nRow , nIndex)
{
	assert.isObject(nRow, "nRow expected to be an Object.");
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage = 	'<table align="center">'+
		'<tr>'+
			'<td> <img title="Edit" src="images/edit_database_24.png" onClick="locationList_edit('+ nRow.m_nLocationId  +')" width="20" /> </td>'+
			'<td></td>'+
			'<td></td>'+
			'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="locationList_delete('+nIndex+')" width="20"/> </td>'+
		'</tr>'+
	'</table>'
	return oImage;
}

function locationList_edit (nLocationId)
{
 	assert.isNumber(nLocationId, "nLocationId expected to be a Number.");
 	assert(nLocationId !== 0, "nLocationId cannot be equal to zero.");
	m_oLocationList_memberData.m_nLocationId = nLocationId;
	navigate("location", "widgets/inventorymanagement/location/editLocation.js");
}

function locationList_delete (nIndex)
{
 	assert.isNumber(nIndex, "nIndex expected to be a Number.");
 	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oLocationData = new LocationData();
	var oListData = $("#locationList_table_locationListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oLocationData.m_nLocationId = oData.m_nLocationId;
	var bConfirm = getUserConfirmation("usermessage_locationList_doyoureallywanttodelete")
	if (bConfirm == true)
		LocationDataProcessor.deleteData(oLocationData,
				function (oResponse)
				{
					locationList_deleted(oResponse, oData.m_strName)
				});
}

function locationList_deleted (oResponse, strName)
{
	var oLocationData = new LocationData();
	if(oResponse.m_bSuccess)
	{
		informUser("usermessage_locationList_deletedsuccessfully", "kSuccess");
		document.getElementById ("locationList_div_listDetail").innerHTML = "";
		locationList_list (m_oLocationList_memberData.m_strSortColumn, m_oLocationList_memberData.m_strSortOrder, 1, 10);
	}
}

function locationList_selectedRowData (oRowData, rowIndex)
{
 	assert.isObject(oRowData, "oRowData expected to be an Object.");
 	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	assert.isOk(rowIndex > -1, "rowIndex must be a positive value.");
	m_oLocationList_memberData.m_oLocationData = oRowData;
	m_oLocationList_memberData.m_oLocationData.m_nLocationId = oRowData.m_nLocationId;
	m_oLocationList_memberData.m_nIndex = rowIndex;
	document.getElementById("locationList_div_listDetail").innerHTML = "";
	var oLocationData = new LocationData();
	oLocationData.m_nLocationId = oRowData.m_nLocationId;
	LocationDataProcessor.getXML (oLocationData, locationList_gotXML);
}

function locationList_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "inventorymanagement/location/locationDetails.xslt", 'locationList_div_listDetail');
}

function locationList_listDetail_edit ()
{
	m_oLocationList_memberData.m_nLocationId = m_oLocationList_memberData.m_oLocationData.m_nLocationId;
	navigate("location", "widgets/inventorymanagement/location/editLocation.js");
}

function locationList_listDetail_delete ()
{
	locationList_delete (m_oLocationList_memberData.m_nIndex);
}

function location_showFilterPopup () 
{
	loadPage ("inventorymanagement/location/filterLocation.html", "dialog", "filterLocation_init ()");
}

function filterLocation_init ()
{
	createPopup('dialog', '#filterLocation_button_cancel', '#filterLocation_button_submit', true);
}

function filterLocation_cancel ()
{
	HideDialog ("dialog");
}

function locationList_addNewPopUp ()
{
	navigate ('location','widgets/inventorymanagement/location/newLocation.js');
}