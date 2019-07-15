var approveStudentListInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
];

includeDataObjects (approveStudentListInfo_includeDataObjects, "approveStudentListInfo_loaded()");

function approveStudentList_Info_MemberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;    
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
    this.m_strapplicationStatus= "verified";
    this.m_nStudentId = -1;
}

var m_oApproveStudentList_Info_MemberData = new approveStudentList_Info_MemberData();

function approveStudentListInfo_loaded ()
{
	loadPage("applicationstatus/approve/approvelist.html","workarea","approveStudentInfo_init()");
}

function approveStudentInfo_init ()
{
	approveStudentListInfo_createDataGrid ();
	dropdownacademicyear();
	$("#zenithInfo_approvedamount").focus();
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

function approveStudentListInfo_createDataGrid ()
{
	initHorizontalSplitter("#listApproveStudents_div_horizontalSplitter", "#listApproveStudents_table_students");
	$('#listApproveStudents_table_students').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:150},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'m_strFatherName',title:'Father Name',sortable:true,width:300},
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:200},
				{field:'m_strCity',title:'City',sortable:true,width:200},				
			]],				
		}
	);
	
	$('#listApproveStudents_table_students').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					approveStudentlistInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oStudentInformationData)
				{
					m_oApproveStudentList_Info_MemberData.m_strSortColumn = strColumn;
					m_oApproveStudentList_Info_MemberData.m_strSortOrder = strOrder;
					approveStudentListInfo_list (strColumn, strOrder, m_oApproveStudentList_Info_MemberData.m_nPageNumber, m_oApproveStudentList_Info_MemberData.m_nPageSize);
				}
			}
	)
	approveStudentList_initDGPagination();
	approveStudentListInfo_list(m_oApproveStudentList_Info_MemberData.m_strSortColumn, m_oApproveStudentList_Info_MemberData.m_strSortOrder, 1, 10);
}

function approveStudentList_initDGPagination ()
{
	$('#listApproveStudents_table_students').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oApproveStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				approveStudentListInfo_list (m_oApproveStudentList_Info_MemberData.m_strSortColumn, m_oApproveStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listApproveStudents_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oApproveStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				m_oApproveStudentList_Info_MemberData.m_nPageSize = nPageSize;
				approveStudentListInfo_list (m_oApproveStudentList_Info_MemberData.m_strSortColumn, m_oApproveStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listApproveStudents_div_listDetail").innerHTML = "";
			}
		}
	)
}

function approveStudentlistInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oApproveStudentList_Info_MemberData.m_nIndex = nIndex;
	document.getElementById("listApproveStudents_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	m_oApproveStudentList_Info_MemberData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,approveStudentListInfo_gotXML);	
}

function approveStudentListInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "applicationstatus/approve/studentInfoApprove.xslt", 'listApproveStudents_div_listDetail');
}

function approveStudentListInfo_list (strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oApproveStudentList_Info_MemberData.m_strSortColumn = strColumn;
	m_oApproveStudentList_Info_MemberData.m_strSortOrder = strOrder;
	m_oApproveStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
	m_oApproveStudentList_Info_MemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "approveStudentListInfo_progressbarLoaded ()");
}


function approveStudentInfo_Student()
{
	createPopup('dialog', '', '', true);	
	var oZenith = new ZenithScholarshipDetails ();
	if($("#zenithInfo_approvedamount").val() != 0)
	{
		oZenith.m_fApprovedAmount = $("#zenithInfo_approvedamount").val();
		oZenith.m_nStudentId = m_oApproveStudentList_Info_MemberData.m_nStudentId;
		ZenithStudentInformationDataProcessor.approvedStatusUpdate(oZenith,studentapproveResponse);
	}
	else
	{
		alert("Please Enter Valid Amount");
		approveStudentInfo_init ();		
	}	
}

function recjectStudentInfo_Student ()
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
	oZenith.m_nStudentId = m_oApproveStudentList_Info_MemberData.m_nStudentId;
	oZenith.m_strStudentRemarks = $("#studentRemarkInfo_input_Remark").val();
	ZenithStudentInformationDataProcessor.rejectStatusUpdate(oZenith,studentrejectResponse);
}

function studentrejectResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("student rejected successfully", "kSuccess");
		document.getElementById("listApproveStudents_div_listDetail").innerHTML = "";		
		navigate("approvedlist","widgets/applicationstatus/approve/approvelist.js");
	}
	else
		informUser ("student reject Failed", "kError");
}

function searchStudentUID ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nUID = $("#StudentInfo_input_uid").val();
	oStudentInformationData.m_strStatus = m_oApproveStudentList_Info_MemberData.m_strapplicationStatus;
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	if($("#StudentInfo_input_uid").val() != "")
		StudentInformationDataProcessor.getStudentUID(oStudentInformationData,studentUIDResponse);
	else
		alert("Please Enter UID Number");
}

function studentUIDResponse (oStudentUIDResponse)
{
	if(oStudentUIDResponse.m_bSuccess)
	{
		document.getElementById("listApproveStudents_div_listDetail").innerHTML = "";
		var oStudentInformationData = new StudentInformationData () ;
		oStudentInformationData.m_nStudentId = m_oApproveStudentList_Info_MemberData.m_nStudentId = oStudentUIDResponse.m_arrStudentInformationData[0].m_nStudentId;
		oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
		StudentInformationDataProcessor.getXML (oStudentInformationData,approveStudentListInfo_gotXML);
		document.getElementById("StudentInfo_input_uid").value = "";
	}
	else
	{
		alert("Student UID Does not Exist in the List!!");
		document.getElementById("StudentInfo_input_uid").value = "";
	}
		
}

function studentapproveResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("student approved successfully", "kSuccess");
		document.getElementById("listApproveStudents_div_listDetail").innerHTML = "";		
		navigate("approvedlist","widgets/applicationstatus/approve/approvelist.js");
	}
	else
		informUser ("student approved Failed", "kError");
	
}

function studentRemarkValidate ()
{
	return validateForm("studentRemarkForm");
}

function approveStudentListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_oApproveStudentList_Info_MemberData.m_strapplicationStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,approveStudentListInfo_listed);
}

function approveStudentListInfo_listed(oStudentResponseData)
{
	clearGridData ("#listApproveStudents_table_students");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#listApproveStudents_table_students').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	$('#listApproveStudents_table_students').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
	HideDialog("dialog");
}

function studentRemarkInfo_cancel ()
{
	HideDialog("dialog");
}


function reVerifyStudentInfo_Student(){
	
	var oZenith = new ZenithScholarshipDetails ();
	oZenith.m_nStudentId = m_oApproveStudentList_Info_MemberData.m_nStudentId;
	ZenithStudentInformationDataProcessor.reVerifiedStatusUpdate(oZenith,studentReVerifiedResponse);
	
}


function studentReVerifiedResponse(oResponse) {
	
	if(oResponse.m_bSuccess)
	{
		informUser("Application Status Sent to Verify Successfully","kSuccess");
		navigate("approvelist","widgets/applicationstatus/approve/approvelist.js");
	}
	else
		informUser("Application Status Sent to Verify Failed","kError");
	
	
	
}




