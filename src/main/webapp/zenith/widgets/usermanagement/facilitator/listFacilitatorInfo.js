var listFacilitatorInfo_includeDataObjects = 
[
	'widgets/usermanagement/role/RoleData.js',
	'widgets/usermanagement/facilitator/FacilitatorInformationData.js'
];

 includeDataObjects (listFacilitatorInfo_includeDataObjects, "listFacilitatorInfo_loaded()");

function listFacilitatorInfo_memberData ()
{
	this.m_nSelectedFacilitatorId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strFacilitatorName";
    this.m_strSortOrder = "asc";
}

var m_oFacilitatorInfoListMemberData = new listFacilitatorInfo_memberData ();

function listFacilitatorInfo_loaded ()
{
	loadPage ("usermanagement/facilitator/listFacilitatorInfo.html", "workarea", "listFacilitatorInfo_init ()");
}

function listFacilitatorInfo_init ()
{
	listFacilitatorInfo_createDataGrid ();
}

function listFacilitatorInfo_createDataGrid ()
{
	initHorizontalSplitter("#listFacilitatorInfo_div_horizontalSplitter", "#listFacilitatorInfo_table_facilitators");
	$('#listFacilitatorInfo_table_facilitators').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_strFacilitatorName',title:'Facilitator Name',sortable:true,width:300},
				{field:'m_strEmail',title:'Email',sortable:true,width:200},
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:200},
				{field:'m_strCity',title:'City',sortable:true,width:200},
				{field:'m_strState',title:'State',sortable:true,width:200},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return listFacilitatorInfo_displayImages (row.m_nFacilitatorId,index);
		        	}
	            },
			]],				
		}
	);
	$('#listFacilitatorInfo_table_facilitators').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					listFacilitatorInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oUserInformationData)
				{
					if(strColumn == 'RoleName')
						strColumn = 'm_nUserId';
					m_oFacilitatorInfoListMemberData.m_strSortColumn = strColumn;
					m_oFacilitatorInfoListMemberData.m_strSortOrder = strOrder;
					listFacililtatorInfo_list (strColumn, strOrder, m_oFacilitatorInfoListMemberData.m_nPageNumber, m_oFacilitatorInfoListMemberData.m_nPageSize);
				}
			}
	)	
	listFacilitatorInfo_initDGPagination ();
	listFacililtatorInfo_list (m_oFacilitatorInfoListMemberData.m_strSortColumn, m_oFacilitatorInfoListMemberData.m_strSortOrder, 1, 10);
}

function listFacilitatorInfo_initDGPagination ()
{
	$('#listFacilitatorInfo_table_facilitators').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oFacilitatorInfoListMemberData.m_nPageNumber = nPageNumber;
				listFacililtatorInfo_list (m_oFacilitatorInfoListMemberData.m_strSortColumn, m_oFacilitatorInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listFacilitatorInfo_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oFacilitatorInfoListMemberData.m_nPageNumber = nPageNumber;
				m_oFacilitatorInfoListMemberData.m_nPageSize = nPageSize;
				listFacililtatorInfo_list (m_oFacilitatorInfoListMemberData.m_strSortColumn, m_oFacilitatorInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listFacilitatorInfo_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listFacilitatorInfo_selectedRowData (oRowData, nIndex)
{
 	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oFacilitatorInfoListMemberData.m_nIndex = nIndex;
	document.getElementById("listFacilitatorInfo_div_listDetail").innerHTML = "";
	var oFacilitatorInformationData = new FacilitatorInformationData ;
	oFacilitatorInformationData.m_nFacilitatorId = oRowData.m_nFacilitatorId;
	FacilitatorInformationDataProcessor.getXML (oFacilitatorInformationData,listFacilitatorInfo_gotXML);
}

function listFacilitatorInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "usermanagement/facilitator/facilitatorInfoDetails.xslt", 'listFacilitatorInfo_div_listDetail');
}

function listFacililtatorInfo_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oFacilitatorInfoListMemberData.m_strSortColumn = strColumn;
	m_oFacilitatorInfoListMemberData.m_strSortOrder = strOrder;
	m_oFacilitatorInfoListMemberData.m_nPageNumber = nPageNumber;
	m_oFacilitatorInfoListMemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "listFacilitatorInfo_progressbarLoaded ()");
}

function listFacilitatorInfo_displayImages (nFacilitatorId,index)
{
 	assert.isNumber(nFacilitatorId, "nFacilitatorId expected to be a Number.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img src="images/edit_database_24.png" width="20" align="center" id="editImageId" title="Edit" onClick="listFacilitatorInfo_edit('+nFacilitatorId+')"/> </td>'+
							'<td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete" onClick="listFacilitatorInfo_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listFacilitatorInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oFacililtatorInformationData = new FacilitatorInformationData ();
	FacilitatorInformationDataProcessor.list(oFacililtatorInformationData, m_oFacilitatorInfoListMemberData.m_strSortColumn, m_oFacilitatorInfoListMemberData.m_strSortOrder, m_oFacilitatorInfoListMemberData.m_nPageNumber, m_oFacilitatorInfoListMemberData.m_nPageSize, listFacilitatorInfo_listed);
}

function listFacilitatorInfo_listed (oFacilitatorInfoResponse)
{
	clearGridData ("#listFacilitatorInfo_table_facilitators");
	for (var nIndex = 0; nIndex < oFacilitatorInfoResponse.m_arrFacilitatorInformationData.length; nIndex++)
		$('#listFacilitatorInfo_table_facilitators').datagrid('appendRow',oFacilitatorInfoResponse.m_arrFacilitatorInformationData[nIndex]);
	$('#listFacilitatorInfo_table_facilitators').datagrid('getPager').pagination ({total:oFacilitatorInfoResponse.m_nRowCount, pageNumber:oFacilitatorInfoResponse.m_nPageNumber});
	HideDialog("dialog");
}

function listFacilitatorInfo_edit (nFacilitatorId)
{
	assert.isNumber(nFacilitatorId, "nFacilitatorId expected to be a Number.");
	m_oFacilitatorInfoListMemberData.m_nSelectedFacilitatorId = nFacilitatorId;
	navigate ("actionInformation", "widgets/usermanagement/facilitator/editFacilitatorInfo.js");
}

function listFacilitatorInfo_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");	
	var oListData = $("#listFacilitatorInfo_table_facilitators").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oFacililtatorInformationData = new FacilitatorInformationData ();
	oFacililtatorInformationData.m_nFacilitatorId = oData.m_nFacilitatorId;
	FacilitatorInformationDataProcessor.deleteData(oFacililtatorInformationData,facilitator_delete_Response);
}

function facilitator_delete_Response (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("facilitator deleted successfully", "kSuccess");
		document.getElementById("listFacilitatorInfo_div_listDetail").innerHTML = "";
		listFacililtatorInfo_list (m_oFacilitatorInfoListMemberData.m_strSortColumn, m_oFacilitatorInfoListMemberData.m_strSortOrder, 1, 10);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function listFacilitatorInfo_showAddPopup ()
{
	navigate ("newfacilitator", "widgets/usermanagement/facilitator/newFacilitatorInfo.js");
}

function listFacilitatorInfo_filter ()
{
	var oFacililtatorFilteredData = new FacilitatorInformationData ();
	if($("#filterFacilitatorInfo_input_facilitatorName").val() != "")
	{
		oFacililtatorFilteredData.m_strFacilitatorName = $("#filterFacilitatorInfo_input_facilitatorName").val();
	}
	else if($("#filterFacilitatorInfo_input_phoneNumber").val() != "")
	{
		oFacililtatorFilteredData.m_strPhoneNumber = $("#filterFacilitatorInfo_input_phoneNumber").val();
	}
	else
		oFacililtatorFilteredData.m_strCity = $("#filterFacilitatorInfo_input_city").val();
	FacilitatorInformationDataProcessor.filterFacilitatorData(oFacililtatorFilteredData,facililtatorFilteredDataResponse);
}


function facililtatorFilteredDataResponse(oFacilitatorResponse)
{
	if(oFacilitatorResponse.m_bSuccess)
	{
		document.getElementById("filterFacilitatorInfo_input_facilitatorName").value = "";
		document.getElementById("filterFacilitatorInfo_input_phoneNumber").value = "";
		document.getElementById("filterFacilitatorInfo_input_city").value = "";
		clearGridData ("#listFacilitatorInfo_table_facilitators");
		for (var nIndex = 0; nIndex < oFacilitatorResponse.m_arrFacilitatorInformationData.length; nIndex++)
			$('#listFacilitatorInfo_table_facilitators').datagrid('appendRow',oFacilitatorResponse.m_arrFacilitatorInformationData[nIndex]);
		$('#listFacilitatorInfo_table_facilitators').datagrid('getPager').pagination ({total:oFacilitatorResponse.m_nRowCount, pageNumber:oFacilitatorResponse.m_nPageNumber});
	}
	else
		informUser("no search result found","kError");
	
}
