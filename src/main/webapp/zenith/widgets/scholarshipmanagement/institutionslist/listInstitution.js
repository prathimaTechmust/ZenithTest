var listInstitutionsInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js'
];

 includeDataObjects (listInstitutionsInfo_includeDataObjects, "listInstitutionsInfo_loaded()");

function listInstitutionsInfo_memberData ()
{
	this.m_nSelectedInstitutionId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strSortColumn = "m_strInstitutionName";
    this.m_strSortOrder = "asc";
    this.m_nInstitutionId = -1;
}

var m_oInstitutionsInfoListMemberData = new listInstitutionsInfo_memberData ();

function listInstitutionsInfo_loaded ()
{
	loadPage ("scholarshipmanagement/institution/listInstitutionsInfo.html", "workarea", "listInstitutionsInfo_init ()");
}

function listInstitutionsInfo_init ()
{
	listInstitutionsInfo_createDataGrid ();
}

function listInstitutionsInfo_createDataGrid ()
{
	initHorizontalSplitter("#listInstitutionsInfo_div_horizontalSplitter", "#listInstitutionsInfo_table_institutions");
	$('#listInstitutionsInfo_table_institutions').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_strInstitutionName',title:'Institution Name',sortable:true,width:300},
				{field:'m_strInstitutionEmailAddress',title:'InstitutionEmail Address',sortable:true,width:200},
				{field:'m_strCity',title:'City',sortable:true,width:200},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return listInstitutionsInfo_displayImages (row.m_nInstitutionId,index);
		        	}
	            },
			]],				
		}
	);
	$('#listInstitutionsInfo_table_institutions').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					listInstitutionsInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oInstitutionsInformationData)
				{
					m_oInstitutionsInfoListMemberData.m_strSortColumn = strColumn;
					m_oInstitutionsInfoListMemberData.m_strSortOrder = strOrder;
					listInstitutionsInfo_list (strColumn, strOrder, m_oInstitutionsInfoListMemberData.m_nPageNumber, m_oInstitutionsInfoListMemberData.m_nPageSize);
				}
			}
	)	
	listInstitutionsInfo_initDGPagination ();
	listInstitutionsInfo_list (m_oInstitutionsInfoListMemberData.m_strSortColumn, m_oInstitutionsInfoListMemberData.m_strSortOrder, 1, 10);
}

function listInstitutionsInfo_initDGPagination ()
{
	$('#listInstitutionsInfo_table_institutions').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oInstitutionsInfoListMemberData.m_nPageNumber = nPageNumber;
				listInstitutionsInfo_list (m_oInstitutionsInfoListMemberData.m_strSortColumn, m_oInstitutionsInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listInstitutionsInfo_div_listDetail").innerHTML = "";
				clearInstitutionFilterBoxes ();
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oInstitutionsInfoListMemberData.m_nPageNumber = nPageNumber;
				m_oInstitutionsInfoListMemberData.m_nPageSize = nPageSize;
				listInstitutionsInfo_list (m_oInstitutionsInfoListMemberData.m_strSortColumn, m_oInstitutionsInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listInstitutionsInfo_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listInstitutionsInfo_selectedRowData (oRowData, nIndex)
{
 	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oInstitutionsInfoListMemberData.m_nIndex = nIndex;
	document.getElementById("listInstitutionsInfo_div_listDetail").innerHTML = "";
	var oInstitutionsInformationData = new InstitutionInformationData () ;
	oInstitutionsInformationData.m_nInstitutionId = oRowData.m_nInstitutionId;
	InstitutionInformationDataProcessor.getXML (oInstitutionsInformationData,listInstitutionsInfo_gotXML);
}

function listInstitutionsInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "scholarshipmanagement/institution/institutionsInfoDetails.xslt", 'listInstitutionsInfo_div_listDetail');
}

function listInstitutionsInfo_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oInstitutionsInfoListMemberData.m_strSortColumn = strColumn;
	m_oInstitutionsInfoListMemberData.m_strSortOrder = strOrder;
	m_oInstitutionsInfoListMemberData.m_nPageNumber = nPageNumber;
	m_oInstitutionsInfoListMemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "listInstitutionsInfo_progressbarLoaded ()");
}

function listInstitutionsInfo_displayImages (nInstitutionId,index)
{
 	assert.isNumber(nInstitutionId, "nInstitutionId expected to be a Number.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img src="images/edit_database_24.png" width="20" align="center" id="editImageId" title="Edit" onClick="listInstitutionsInfo_edit('+nInstitutionId+')"/> </td>'+
							'<td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete" onClick="listInstitutionsInfo_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listInstitutionsInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oInstitutionsInformationData = new InstitutionInformationData () ;
	InstitutionInformationDataProcessor.list(oInstitutionsInformationData, m_oInstitutionsInfoListMemberData.m_strSortColumn, m_oInstitutionsInfoListMemberData.m_strSortOrder, m_oInstitutionsInfoListMemberData.m_nPageNumber, m_oInstitutionsInfoListMemberData.m_nPageSize, listInstitutionsInfo_listed);
}

function listInstitutionsInfo_listed (oInstitutionsInfoResponse)
{
	clearGridData ("#listInstitutionsInfo_table_institutions");
	for (var nIndex = 0; nIndex < oInstitutionsInfoResponse.m_arrInstitutionInformationData.length; nIndex++)
		$('#listInstitutionsInfo_table_institutions').datagrid('appendRow',oInstitutionsInfoResponse.m_arrInstitutionInformationData[nIndex]);
	$('#listInstitutionsInfo_table_institutions').datagrid('getPager').pagination ({total:oInstitutionsInfoResponse.m_nRowCount, pageNumber:oInstitutionsInfoResponse.m_nPageNumber});
	HideDialog("dialog");
}

function listInstitutionsInfo_edit (nInstitutionId)
{
	assert.isNumber(nInstitutionId, "nInstitutionId expected to be a Number.");
	m_oInstitutionsInfoListMemberData.m_nSelectedInstitutionId = nInstitutionId;
	navigate ("actionInformation", "widgets/scholarshipmanagement/institutionslist/editInstitutionInfo.js");
}

function listInstitutionsInfo_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");	
	var oListData = $("#listInstitutionsInfo_table_institutions").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oInstitutionsInformationData = new InstitutionInformationData () ;
	oInstitutionsInformationData.m_nInstitutionId = oData.m_nInstitutionId;
	var bUserConfirm = getUserConfirmation("Are you sure do you want to delete?");
	if(bUserConfirm)
		InstitutionInformationDataProcessor.deleteData(oInstitutionsInformationData,institution_delete_Response);
}

function institution_delete_Response (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("institution deleted successfully", "kSuccess");
		document.getElementById("listInstitutionsInfo_div_listDetail").innerHTML = "";
		navigate("institutionlist","widgets/scholarshipmanagement/institutionslist/listInstitution.js");
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function listInstitutionsInfo_showAddPopup ()
{
	navigate ("newinstitution", "widgets/scholarshipmanagement/institutionslist/newInstitutionsInfo.js");
}

function institutionsListInfo_filter ()
{
	var bSuccess = false;
	var oInstitutionsInformationData = new InstitutionInformationData () ;
	if($("#filterInstitutionsInfo_input_institutionName").val() != "")
	{
		oInstitutionsInformationData.m_strInstitutionName = $("#filterInstitutionsInfo_input_institutionName").val();
		bSuccess = true;
	}		
	else if($("#filterInstitutionsInfo_input_phonenumber").val() != "")
	{
		oInstitutionsInformationData.m_strPhoneNumber = $("#filterInstitutionsInfo_input_phonenumber").val();
		bSuccess = true;
	}		
	else if($("#filterInstitutionsInfo_input_city").val() != "")
	{
		oInstitutionsInformationData.m_strCity = $("#filterInstitutionsInfo_input_city").val();
		bSuccess = true;
	}
	if(bSuccess)
		InstitutionInformationDataProcessor.institutionFilterData(oInstitutionsInformationData, institutionFilteredResponse);
	else
		alert("Please Enter any one of textBox to Filter");
}

function institutionFilteredResponse(oResponse)
{
	if(oResponse.m_bSuccess)
	{		
		clearGridData ("#listInstitutionsInfo_table_institutions");
		for (var nIndex = 0; nIndex < oResponse.m_arrInstitutionInformationData.length; nIndex++)
			$('#listInstitutionsInfo_table_institutions').datagrid('appendRow',oResponse.m_arrInstitutionInformationData[nIndex]);
		$('#listInstitutionsInfo_table_institutions').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:oResponse.m_nPageNumber});
	}
	else
		informUser("no search result found","kError");
	
}

function clearInstitutionFilterBoxes ()
{
	document.getElementById("filterInstitutionsInfo_input_institutionName").value = "";
	document.getElementById("filterInstitutionsInfo_input_phonenumber").value = "";
	document.getElementById("filterInstitutionsInfo_input_city").value = "";
}