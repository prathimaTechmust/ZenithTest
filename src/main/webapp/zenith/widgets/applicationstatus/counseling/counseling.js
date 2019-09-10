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
	populateAcademicYearDropDown('selectCounselingAcademicyear');
	counselingStudentListInfo_createDataGrid ();	
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
	applicationPriorityGridColor('CounselingStudents_table_students');
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
				clearFilterBoxes ();
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
	oStudentInformationData.m_nAcademicYearId = $("#selectCounselingAcademicyear").val();
	m_oCounselingStudentList_Info_MemberData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,counselingStudentListInfo_gotXML);	
}


function counselingStudentListInfo_gotXML(strXMLData)
{
	populateXMLData (strXMLData, "applicationstatus/counseling/counselingList.xslt", 'CounselingStudents_div_listDetail');
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
	oStudentInformationData.m_nAcademicYearId = $("#selectCounselingAcademicyear").val();
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

function recjectInfo_Student ()
{
	loadPage("applicationstatus/rejectlist/studentRemarkInfo.html","dialog","rejectStudentRemarks_init()");		
}

function rejectStudentRemarks_init()
{
	createPopup('dialog','#remarkInfo_button_submit','remarkInfo_button_cancel',true);
	initFormValidateBoxes('studentRemarkForm');
}

function studentRemarkInfo_submit ()
{
	if(studentRemarkValidate ())
		loadPage ("include/process.html", "ProcessDialog", "studentremark_progressbarLoaded ()");
	else
	{
		alert("Please Enter Remarks");
		$("#studentRemarkForm").focus();
	}	
}
function studentremark_progressbarLoaded ()
{
	createPopup('dialog','','',true);
	var oZenith = new ZenithScholarshipDetails ();		
	oZenith.m_nStudentId = m_oCounselingStudentList_Info_MemberData.m_nStudentId;
	oZenith.m_strStudentRemarks = $("#studentRemarkInfo_input_Remark").val();
	ZenithStudentInformationDataProcessor.rejectStatusUpdate(oZenith,studentrejectResponse);
}

function studentrejectResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("student rejected successfully", "kSuccess");
		document.getElementById("CounselingStudents_div_listDetail").innerHTML = "";		
		navigate("counseling","widgets/applicationstatus/counseling/counseling.js");
	}
	else
		informUser ("student reject Failed", "kError");
}

function studentRemarkValidate ()
{
	return validateForm("studentRemarkForm");
}

function studentRemarkInfo_cancel ()
{
	HideDialog("dialog");
}

function approve_counselingStudent ()
{
	var oZenith = new ZenithScholarshipDetails ();
	oZenith.m_nStudentId = m_oCounselingStudentList_Info_MemberData.m_nStudentId;
	ZenithStudentInformationDataProcessor.aproveCounselingStudentUpdate(oZenith,studentCounselingResponse);
}

function studentCounselingResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Application Status Sent to Verify Successfully","kSuccess");
		navigate("rejectlist","widgets/applicationstatus/rejectedlist/rejectedlist.js");
	}
	else
		informUser("Application Status Sent to Verify Failed","kError");
	
}

