var rejectedStudentListInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
];

includeDataObjects (rejectedStudentListInfo_includeDataObjects, "rejectedStudentListInfo_loaded()");

function rejectedStudentList_Info_MemberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;    
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
    this.m_strapplicationStatus= "rejected";
    this.m_nStudentId = -1;
    this.m_arrStudent = new Array();
}

var m_oRejectedStudentList_Info_MemberData = new rejectedStudentList_Info_MemberData();

function rejectedStudentListInfo_loaded ()
{
	loadPage("applicationstatus/rejectlist/rejectedlist.html","workarea","rejectedStudentInfo_init()");
}

function rejectedStudentInfo_init ()
{
	populateAcademicYearDropDown('selectRejectListAcademicYear');
	rejectedStudentListInfo_createDataGrid ();	
}

function rejectedStudentListInfo_createDataGrid ()
{
	initHorizontalSplitter("#rejectedStudentsList_div_horizontalSplitter", "#rejectedStudentsList_table_students");
	$('#rejectedStudentsList_table_students').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:150},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'m_strInstitutionName',title:'Institution Name',sortable:true,width:300,
					formatter:function(value,row,index)
							  {						
								return row.m_oAcademicDetails[0].m_oInstitutionInformationData.m_strInstitutionName;
							  }
					
				},
				{field:'m_strStatus',title:'Application Status',sortable:true,width:200,
					formatter:function(value,row,index)
							  {						
								return row.m_oZenithScholarshipDetails[0].m_strStatus;
							  }
				},
				{field:'m_strStudentRemark',title:'Remarks',sortable:true,width:200,
					formatter:function(value,row,index)
					  {						
						return row.m_oZenithScholarshipDetails[0].m_strStudentRemarks;
					  }
				},	
			]],				
		}
	);
	
	$('#rejectedStudentsList_table_students').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					rejectedStudentlistInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oStudentInformationData)
				{
					m_oRejectedStudentList_Info_MemberData.m_strSortColumn = strColumn;
					m_oRejectedStudentList_Info_MemberData.m_strSortOrder = strOrder;
					rejectedStudentListInfo_list (strColumn, strOrder, m_oRejectedStudentList_Info_MemberData.m_nPageNumber, m_oRejectedStudentList_Info_MemberData.m_nPageSize);
				}
			}
	)
	rejectedStudentList_initDGPagination();
	rejectedStudentListInfo_list(m_oRejectedStudentList_Info_MemberData.m_strSortColumn, m_oRejectedStudentList_Info_MemberData.m_strSortOrder, 1, 10);
}

function rejectedStudentList_initDGPagination ()
{
	$('#rejectedStudentsList_table_students').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oRejectedStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				rejectedStudentListInfo_list (m_oRejectedStudentList_Info_MemberData.m_strSortColumn, m_oRejectedStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("rejectedStudentsList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oRejectedStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				m_oRejectedStudentList_Info_MemberData.m_nPageSize = nPageSize;
				rejectedStudentListInfo_list (m_oRejectedStudentList_Info_MemberData.m_strSortColumn, m_oRejectedStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("rejectedStudentsList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function rejectedStudentlistInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oRejectedStudentList_Info_MemberData.m_nIndex = nIndex;
	document.getElementById("rejectedStudentsList_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectRejectListAcademicYear").val();
	m_oRejectedStudentList_Info_MemberData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,rejectedStudentListInfo_gotXML);	
}

function rejectedStudentListInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "applicationstatus/rejectlist/studentInfoRejected.xslt", 'rejectedStudentsList_div_listDetail');
}

function rejectedStudentListInfo_list (strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oRejectedStudentList_Info_MemberData.m_strSortColumn = strColumn;
	m_oRejectedStudentList_Info_MemberData.m_strSortOrder = strOrder;
	m_oRejectedStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
	m_oRejectedStudentList_Info_MemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "rejectedStudentListInfo_progressbarLoaded ()");
}


function searchStudentUID()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nUID = $("#StudentInfo_input_uid").val();
	if($("#StudentInfo_input_uid").val() != "")
		StudentInformationDataProcessor.getStudentUID(oStudentInformationData,studentUIDResponse);
	else
		alert("Please Enter Valid UID Number");
}

function studentUIDResponse(oStudentUIDResponse)
{
	if(oStudentUIDResponse.m_bSuccess)
	{
		document.getElementById("rejectedStudentsList_div_listDetail").innerHTML = "";
		var oStudentInformationData = new StudentInformationData () ;
		oStudentInformationData.m_nStudentId = m_oRejectedStudentList_Info_MemberData.m_nStudentId = oStudentUIDResponse.m_arrStudentInformationData[0].m_nStudentId;
		oStudentInformationData.m_strAcademicYear = $("#selectRejectListAcademicYear").val();
		StudentInformationDataProcessor.getXML (oStudentInformationData,rejectedStudentListInfo_gotXML);
		document.getElementById("StudentInfo_input_uid").value = "";
	}
	else
		alert("Student UID Does not exist");	
}

function reVerifyStudentInfo_Student ()
{
	var oZenith = new ZenithScholarshipDetails ();
	oZenith.m_nStudentId = m_oRejectedStudentList_Info_MemberData.m_nStudentId;
	ZenithStudentInformationDataProcessor.reVerifiedStatusUpdate(oZenith,studentReVerifiedResponse);
}

function studentReVerifiedResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Application Status Sent to Verify Successfully","kSuccess");
		navigate("rejectlist","widgets/applicationstatus/rejectedlist/rejectedlist.js");
	}
	else
		informUser("Application Status Sent to Verify Failed","kError");
	
}

function rejectedStudentListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectRejectListAcademicYear").val();
	oStudentInformationData.m_strStatus = m_oRejectedStudentList_Info_MemberData.m_strapplicationStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,rejectedStudentListInfo_listed);
}

function rejectedStudentListInfo_listed(oStudentResponseData)
{
	clearGridData ("#rejectedStudentsList_table_students");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#rejectedStudentsList_table_students').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	$('#rejectedStudentsList_table_students').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
	HideDialog("dialog");
}

