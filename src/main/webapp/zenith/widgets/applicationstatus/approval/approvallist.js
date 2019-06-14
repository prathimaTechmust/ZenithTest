var approvalStudentListInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
];

includeDataObjects (approvalStudentListInfo_includeDataObjects, "approvalStudentListInfo_loaded()");

function approvalStudentList_Info_MemberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;    
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
    this.m_strapplicationStatus= "verified";
}

var m_oapprovalStudentList_Info_MemberData = new approvalStudentList_Info_MemberData();

function approvalStudentListInfo_loaded ()
{
	loadPage("applicationstatus/approval/approvallist.html","workarea","approvalStudentInfo_init()");
}

function approvalStudentInfo_init ()
{
	approvalStudentListInfo_createDataGrid ();
	dropdownacademicyear();
}

function dropdownacademicyear ()
{
	var oAcademicYear = new AcademicYear();
	AcademicYearProcessor.list(oAcademicYear,"m_strAcademicYear","asc",0,0,academicyearResponse);	
}

function academicyearResponse(oYearResponse)
{
	populateYear("selectacademicyear",oYearResponse);
}

function populateYear(academicyear,oYearResponse)
{
	var arrAcademicYears = new Array();
	for(var nIndex = 0; nIndex < oYearResponse.m_arrAcademicYear.length; nIndex++)
	{
		arrAcademicYears.push(CreateOption(oYearResponse.m_arrAcademicYear[nIndex].m_strAcademicYear,oYearResponse.m_arrAcademicYear[nIndex].m_strAcademicYear));		
	}
	PopulateDD(academicyear,arrAcademicYears);	
}

function approvalStudentListInfo_createDataGrid ()
{
	initHorizontalSplitter("#listApprovalStudents_div_horizontalSplitter", "#listApprovalStudents_table_students");
	$('#listApprovalStudents_table_students').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:200},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'m_strFatherName',title:'Father Name',sortable:true,width:200},
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:200},
				{field:'m_strCity',title:'City',sortable:true,width:200},
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return approvalListInfo_displayImages (row.m_nStudentId,index);
		        	}
	            },
			]],				
		}
	);
	
	$('#listApprovalStudents_table_students').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					approvalStudentlistInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oStudentInformationData)
				{
					m_oapprovalStudentList_Info_MemberData.m_strSortColumn = strColumn;
					m_oapprovalStudentList_Info_MemberData.m_strSortOrder = strOrder;
					approvalStudentListInfo_list (strColumn, strOrder, m_oapprovalStudentList_Info_MemberData.m_nPageNumber, m_oapprovalStudentList_Info_MemberData.m_nPageSize);
				}
			}
	)
	approvalStudentList_initDGPagination();
	approvalStudentListInfo_list(m_oapprovalStudentList_Info_MemberData.m_strSortColumn, m_oapprovalStudentList_Info_MemberData.m_strSortOrder, 1, 10);
}

function approvalStudentList_initDGPagination ()
{
	$('#listApprovalStudents_table_students').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oapprovalStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				approvalStudentListInfo_list (m_oapprovalStudentList_Info_MemberData.m_strSortColumn, m_oapprovalStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listApprovalStudents_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oapprovalStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				m_oapprovalStudentList_Info_MemberData.m_nPageSize = nPageSize;
				approvalStudentListInfo_list (m_oapprovalStudentList_Info_MemberData.m_strSortColumn, m_oapprovalStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listApprovalStudents_div_listDetail").innerHTML = "";
			}
		}
	)
}

function approvalStudentlistInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oapprovalStudentList_Info_MemberData.m_nIndex = nIndex;
	document.getElementById("listApprovalStudents_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	StudentInformationDataProcessor.getXML (oStudentInformationData,approvalStudentListInfo_gotXML);	
}

function approvalStudentListInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "scholarshipmanagement/student/studentInfoDetails.xslt", 'listApprovalStudents_div_listDetail');
}

function approvalStudentListInfo_list (strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oapprovalStudentList_Info_MemberData.m_strSortColumn = strColumn;
	m_oapprovalStudentList_Info_MemberData.m_strSortOrder = strOrder;
	m_oapprovalStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
	m_oapprovalStudentList_Info_MemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "approvalStudentListInfo_progressbarLoaded ()");
}

function approvalListInfo_displayImages (nStudentId,index)
{
	assert.isNumber(nStudentId, "nStudentId expected to be a Number.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <button type="button" width="20" align="center" class = "zenith addButton" style=width:100px;" id="approveStudent" title="Approve" onClick="approveStudentInfo_Student('+nStudentId+')">Approve</button> </td>'+							
						'</tr>'+
					'</table>'
	return oImage;	
}

function approveStudentInfo_Student(nStudentId)
{
	createPopup('dialog', '', '', true);	
	var oZenith = new ZenithScholarshipDetails ();
	oZenith.m_nStudentId = nStudentId;
	ZenithStudentInformationDataProcessor.approvedStatusUpdate(oZenith,studentapprovalResponse);
}

function studentapprovalResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("student approved successfully", "kSuccess");
		document.getElementById("listApprovalStudents_div_listDetail").innerHTML = "";
		/*var oStudentInformationData = new StudentInformationData ();
		oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
		oStudentInformationData.m_strStatus = m_oapprovalStudentList_Info_MemberData.m_strapplicationStatus;
		StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,approvalStudentListInfo_listed);*/
		navigate("approvallist","widgets/applicationstatus/approval/approvallist.js");
	}
	else
		informUser ("student approval Failed", "kError");
	
}

function approvalStudentListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_oapprovalStudentList_Info_MemberData.m_strapplicationStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,approvalStudentListInfo_listed);
}

function approvalStudentListInfo_listed(oStudentResponseData)
{
	clearGridData ("#listApprovalStudents_table_students");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#listApprovalStudents_table_students').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	$('#listApprovalStudents_table_students').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
	HideDialog("dialog");
}

