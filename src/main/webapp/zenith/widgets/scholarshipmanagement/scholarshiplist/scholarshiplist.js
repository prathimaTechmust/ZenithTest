var listScholarshipInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js',
	'widgets/scholarshipmanagement/courselist/CourseInformationData.js'
];

includeDataObjects (listScholarshipInfo_includeDataObjects, "listScholarshipInfo_loaded()");

function listScholarshipInfo_memberData ()
{
	this.m_nSelectedScholarshipId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strImageUrl = "";
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
}

var m_oScholarshipListMemberData = new listScholarshipInfo_memberData ();

function listScholarshipInfo_loaded ()
{
	loadPage ("scholarshipmanagement/scholarship/listScholarshipInfo.html", "workarea", "listScholarshipInfo_init ()");
}

function listScholarshipInfo_init ()
{
	listScholarshipInfo_createDataGrid ();
}

function listScholarshipInfo_createDataGrid ()
{
	initHorizontalSplitter("#scholarshipListInfo_div_horizontalSplitter", "#scholarshipListInfo_table");
	$('#scholarshipListInfo_table').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:200},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'{m_oAcademicDetails/m_strCourseName}',title:'Course Name',sortable:true,width:200},
				{field:'m_oAcademicDetails.m_fAnnualFee',title:'Total Fee',sortable:true,width:200},
				{field:'m_oAcademicDetails.m_fPaidFee',title:'Paid Fee',sortable:true,width:200},
				{field:'m_strScholarshipRequired',title:'Scholarship Required',sortable:true,width:200},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return listScholarshipInfo_displayImages (row.m_nStudentId,index);
		        	}
	            },
			]],				
		}
	);
	$('#scholarshipListInfo_table').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					listScholarshipInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oScholarshipInformationData)
				{
					m_oScholarshipListMemberData.m_strSortColumn = strColumn;
					m_oScholarshipListMemberData.m_strSortOrder = strOrder;
					listScholarshipInfo_list (strColumn, strOrder, m_oScholarshipListMemberData.m_nPageNumber, m_oScholarshipListMemberData.m_nPageSize);
				}
			}
	)	
	listscholarshipInfo_initDGPagination ();
	listScholarshipInfo_list (m_oScholarshipListMemberData.m_strSortColumn, m_oScholarshipListMemberData.m_strSortOrder, 1, 10);
}

function listscholarshipInfo_initDGPagination ()
{
	$('#scholarshipListInfo_table').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oScholarshipListMemberData.m_nPageNumber = nPageNumber;
				listScholarshipInfo_list (m_oScholarshipListMemberData.m_strSortColumn, m_oScholarshipListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("scholarshipListInfo_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oScholarshipListMemberData.m_nPageNumber = nPageNumber;
				m_oScholarshipListMemberData.m_nPageSize = nPageSize;
				listScholarshipInfo_list (m_oScholarshipListMemberData.m_strSortColumn, m_oScholarshipListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("scholarshipListInfo_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listScholarshipInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oScholarshipListMemberData.m_nIndex = nIndex;
	document.getElementById("scholarshipListInfo_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,listScholarshipInfo_gotXML);
}

function listScholarshipInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "scholarshipmanagement/scholarship/scholarshipInfoDetails.xslt", 'scholarshipListInfo_div_listDetail');
}

function listScholarshipInfo_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oScholarshipListMemberData.m_strSortColumn = strColumn;
	m_oScholarshipListMemberData.m_strSortOrder = strOrder;
	m_oScholarshipListMemberData.m_nPageNumber = nPageNumber;
	m_oScholarshipListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "listScholarshipInfo_progressbarLoaded ()");
}

function listScholarshipInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	StudentInformationDataProcessor.list(oStudentInformationData, m_oScholarshipListMemberData.m_strSortColumn, m_oScholarshipListMemberData.m_strSortOrder, m_oScholarshipListMemberData.m_nPageNumber, m_oScholarshipListMemberData.m_nPageSize, listScholarshipInfo_listed);
}

function listScholarshipInfo_listed (oStudentScholarshipInfoResponse)
{
	clearGridData ("#scholarshipListInfo_table");
	for (var nIndex = 0; nIndex < oStudentScholarshipInfoResponse.m_arrStudentInformationData.length; nIndex++)
		$('#scholarshipListInfo_table').datagrid('appendRow',oStudentScholarshipInfoResponse.m_arrStudentInformationData[nIndex]);
	$('#scholarshipListInfo_table').datagrid('getPager').pagination ({total:oStudentScholarshipInfoResponse.m_nRowCount, pageNumber:oStudentScholarshipInfoResponse.m_nPageNumber});
	HideDialog("dialog");
}

function listScholarshipInfo_displayImages(nStudentId,index)
{
	assert.isNumber(nStudentId, "nStudentId expected to be a Number.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img src="images/edit_database_24.png" width="20" align="center" id="editImageId" title="Edit" onClick="listScholarshipInfo_edit('+nStudentId+')"/> </td>'+
							'<td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete" onClick="listScholarshipInfo_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
	
}

function listScholarshipInfo_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");	
	var oListData = $("#scholarshipListInfo_table").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nStudentId = oData.m_nStudentId;
	var bUserConfirm = getUserConfirmation("Are you sure do you want to delete?");
	if(bUserConfirm)
		StudentInformationDataProcessor.deleteData(oStudentInformationData,student_delete_Response);
}