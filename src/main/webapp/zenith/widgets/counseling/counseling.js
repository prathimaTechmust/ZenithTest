var counselingStudentListInfo_includeDataObject  =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
];

includeDataObjects (counselingStudentListInfo_includeDataObject, "counselingStudentListInfo_loaded()");

function counselingStudentList_Info_MemberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;    
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
    this.m_strapplicationStatus= "counseling";
    this.m_nStudentId = -1;
    this.m_arrStudent = new Array();
}

var m_oCounselingStudentList_Info_MemberData = new counselingStudentList_Info_MemberData();


function counselingStudentListInfo_loaded()
{
	loadPage("applicationstatus/counseling/counseling.html","workarea","counselingStudentInfo_init()");
}

function counselingStudentInfo_init()
{
	counselingStudentListInfo_createDataGrid ();
	populateAcademicYearDropDown('selectCounselingAcademicyear');
}

function counselingStudentListInfo_createDataGrid ()
{
	initHorizontalSplitter("#CounselingStudents_div_horizontalSplitter", "#CounselingStudents_table_students");
	$('#CounselingStudents_table_students').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:150},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:200},
				{field:'m_strCity',title:'City',sortable:true,width:200}, 
				{field:'m_strStatus',title:'Application Status',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
		        		return row.m_oZenithScholarshipDetails[0].m_strStatus;
		        	}
				},	
				{field:'m_nCounselingDate',title:'Counseling Date',sortable:true,width:200},
			]],				
		}
	);
	$('#CounselingStudents_table_students').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					counselingStudentlistInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oStudentInformationData)
				{
					m_oCounselingStudentList_Info_MemberData.m_strSortColumn = strColumn;
					m_oCounselingStudentList_Info_MemberData.m_strSortOrder = strOrder;
					counselingStudentListInfo_list (strColumn, strOrder, m_oCounselingStudentList_Info_MemberData.m_nPageNumber, m_oCounselingStudentList_Info_MemberData.m_nPageSize);
				}
			}
	)
	counselingStudentList_initDGPagination();
	counselingStudentListInfo_list(m_oCounselingStudentList_Info_MemberData.m_strSortColumn, m_oCounselingStudentList_Info_MemberData.m_strSortOrder, 1, 10);
}

function counselingStudentList_initDGPagination ()
{
	$('#CounselingStudents_table_students').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oCounselingStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				counselingStudentListInfo_list (m_oCounselingStudentList_Info_MemberData.m_strSortColumn, m_oCounselingStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("CounselingStudents_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_overifiedStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				m_overifiedStudentList_Info_MemberData.m_nPageSize = nPageSize;
				counselingStudentListInfo_list (m_oCounselingStudentList_Info_MemberData.m_strSortColumn, m_oCounselingStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("CounselingStudents_div_listDetail").innerHTML = "";
			}
		}
	)
}

function counselingStudentlistInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oCounselingStudentList_Info_MemberData.m_nIndex = nIndex;
	document.getElementById("CounselingStudents_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectCounselingAcademicyear").val();
	m_oCounselingStudentList_Info_MemberData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,counselingStudentListInfo_gotXML);	
}


function counselingStudentListInfo_gotXML()
{
	
}

function counselingStudentListInfo_list (strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oCounselingStudentList_Info_MemberData.m_strSortColumn = strColumn;
	m_oCounselingStudentList_Info_MemberData.m_strSortOrder = strOrder;
	m_oCounselingStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
	m_oCounselingStudentList_Info_MemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "counselingStudentListInfo_progressbarLoaded ()");
}

function counselingStudentListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectCounselingAcademicyear").val();
	oStudentInformationData.m_strStatus = m_oCounselingStudentList_Info_MemberData.m_strapplicationStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,counselingStudentListInfo_listed);
}

function counselingStudentListInfo_listed(oStudentResponseData)
{
	clearGridData ("#CounselingStudents_table_students");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#CounselingStudents_table_students').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	$('#CounselingStudents_table_students').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
	HideDialog("dialog");
}
