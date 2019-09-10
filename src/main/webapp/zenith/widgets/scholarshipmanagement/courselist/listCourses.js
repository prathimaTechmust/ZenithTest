var listCoursesInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/courselist/CourseInformationData.js'
];

 includeDataObjects (listCoursesInfo_includeDataObjects, "listCourseInfo_loaded()");

function listCourseInfo_memberData ()
{
	this.m_nSelectedCourseId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strSortColumn = "m_strShortCourseName";
    this.m_strSortOrder = "asc";
}

var m_oCourseInfoListMemberData = new listCourseInfo_memberData ();

function listCourseInfo_loaded ()
{
	loadPage ("scholarshipmanagement/course/listCourseInfo.html", "workarea", "listCourseInfo_init ()");
}

function listCourseInfo_init ()
{
	listCourseInfo_createDataGrid ();
}

function listCourseInfo_createDataGrid ()
{
	initHorizontalSplitter("#listCourseInfo_div_horizontalSplitter", "#listCourseInfo_table_courses");
	$('#listCourseInfo_table_courses').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_strShortCourseName',title:'Short Course Name',sortable:true,width:300},
				{field:'m_strLongCourseName',title:'Long Course Name',sortable:true,width:200},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return listCourseInfo_displayImages (row.m_nCourseId,index);
		        	}
	            },
			]],				
		}
	);
	$('#listCourseInfo_table_courses').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					listCourseInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oCourseInformationData)
				{
					m_oCourseInfoListMemberData.m_strSortColumn = strColumn;
					m_oCourseInfoListMemberData.m_strSortOrder = strOrder;
					listCourseInfo_list (strColumn, strOrder, m_oCourseInfoListMemberData.m_nPageNumber, m_oCourseInfoListMemberData.m_nPageSize);
				}
			}
	)	
	listCourseInfo_initDGPagination ();
	listCourseInfo_list (m_oCourseInfoListMemberData.m_strSortColumn, m_oCourseInfoListMemberData.m_strSortOrder, 1, 10);
}

function listCourseInfo_initDGPagination ()
{
	$('#listCourseInfo_table_courses').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oCourseInfoListMemberData.m_nPageNumber = nPageNumber;
				listCourseInfo_list (m_oCourseInfoListMemberData.m_strSortColumn, m_oCourseInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listCourseInfo_div_listDetail").innerHTML = "";
				clearCourseFilterBoxes ();
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oCourseInfoListMemberData.m_nPageNumber = nPageNumber;
				m_oCourseInfoListMemberData.m_nPageSize = nPageSize;
				listCourseInfo_list (m_oCourseInfoListMemberData.m_strSortColumn, m_oCourseInfoListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listCourseInfo_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listCourseInfo_selectedRowData (oRowData, nIndex)
{
 	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oCourseInfoListMemberData.m_nIndex = nIndex;
	document.getElementById("listCourseInfo_div_listDetail").innerHTML = "";
	var oCourseInformationData = new CourseInformationData () ;
	oCourseInformationData.m_nCourseId = oRowData.m_nCourseId;
	CourseInformationDataProcessor.getXML (oCourseInformationData,listCourseInfo_gotXML);
}

function listCourseInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "scholarshipmanagement/course/courseInfoDetails.xslt", 'listCourseInfo_div_listDetail');
}

function listCourseInfo_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oCourseInfoListMemberData.m_strSortColumn = strColumn;
	m_oCourseInfoListMemberData.m_strSortOrder = strOrder;
	m_oCourseInfoListMemberData.m_nPageNumber = nPageNumber;
	m_oCourseInfoListMemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "listCourseInfo_progressbarLoaded ()");
}

function listCourseInfo_displayImages (nCourseId,index)
{
 	assert.isNumber(nCourseId, "nCourseId expected to be a Number.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img src="images/edit_database_24.png" width="20" align="center" id="editImageId" title="Edit" onClick="listCourseInfo_edit('+nCourseId+')"/> </td>'+
							'<td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete" onClick="listCourseInfo_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listCourseInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oCourseInformationData = new CourseInformationData () ;
	CourseInformationDataProcessor.list(oCourseInformationData, m_oCourseInfoListMemberData.m_strSortColumn, m_oCourseInfoListMemberData.m_strSortOrder, m_oCourseInfoListMemberData.m_nPageNumber, m_oCourseInfoListMemberData.m_nPageSize, listCourseInfo_listed);
}

function listCourseInfo_listed (oCourseInfoResponse)
{
	clearGridData ("#listCourseInfo_table_courses");
	for (var nIndex = 0; nIndex < oCourseInfoResponse.m_arrCourseInformationData.length; nIndex++)
		$('#listCourseInfo_table_courses').datagrid('appendRow',oCourseInfoResponse.m_arrCourseInformationData[nIndex]);
	$('#listCourseInfo_table_courses').datagrid('getPager').pagination ({total:oCourseInfoResponse.m_nRowCount, pageNumber:oCourseInfoResponse.m_nPageNumber});
	HideDialog("dialog");
}

function listCourseInfo_edit (nCourseId)
{
	assert.isNumber(nCourseId, "nCourseId expected to be a Number.");
	m_oCourseInfoListMemberData.m_nSelectedCourseId = nCourseId;
	navigate ("actionInformation", "widgets/scholarshipmanagement/courselist/editCourseInfo.js");
}

function listCourseInfo_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");	
	var oListData = $("#listCourseInfo_table_courses").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oCourseInformationData = new CourseInformationData () ;
	oCourseInformationData.m_nCourseId = oData.m_nCourseId;
	var bUserConfirm = getUserConfirmation("Are you sure do you want to delete?");
	if(bUserConfirm)
		CourseInformationDataProcessor.deleteData(oCourseInformationData,course_delete_Response);
}

function course_delete_Response (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("course deleted successfully", "kSuccess");		
		document.getElementById("listCourseInfo_div_listDetail").innerHTML = "";
		navigate("courselist","widgets/scholarshipmanagement/courselist/listCourses.js");
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function listCourseInfo_showAddPopup ()
{
	navigate ("newcourse", "widgets/scholarshipmanagement/courselist/newCourseInfo.js");
}

function courseLitInfo_filter ()
{
	var bSuccess = false;
	var oCourseInformationData = new CourseInformationData () ;
	if($("#filterCourseInfo_input_shortcourseName").val() !="")
	{
		oCourseInformationData.m_strShortCourseName = $("#filterCourseInfo_input_shortcourseName").val();
		bSuccess = true;
	}		
	else
	{
		oCourseInformationData.m_strLongCourseName = $("#filterCourseInfo_input_longcourseName").val();
		bSuccess = true;
	}	
	if(bSuccess)
		CourseInformationDataProcessor.courseFilterData(oCourseInformationData,filteredCourseResponse);
	else
		alert("Enter any one of textBox to Filter");
}

function filteredCourseResponse(oCourseInfoResponse)
{
	if(oCourseInfoResponse.m_bSuccess)
	{		
		clearGridData ("#listCourseInfo_table_courses");
		for (var nIndex = 0; nIndex < oCourseInfoResponse.m_arrCourseInformationData.length; nIndex++)
			$('#listCourseInfo_table_courses').datagrid('appendRow',oCourseInfoResponse.m_arrCourseInformationData[nIndex]);
		$('#listCourseInfo_table_courses').datagrid('getPager').pagination ({total:oCourseInfoResponse.m_nRowCount, pageNumber:oCourseInfoResponse.m_nPageNumber});
	}
	else
		informUser("no search result found","kError");	
}

function clearCourseFilterBoxes ()
{
	document.getElementById("filterCourseInfo_input_shortcourseName").value = "";
	document.getElementById("filterCourseInfo_input_longcourseName").value = "";
}
