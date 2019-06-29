var verifiedStudentListInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
];

includeDataObjects (verifiedStudentListInfo_includeDataObjects, "verifiedStudentListInfo_loaded()");

function verifiedStudentList_Info_MemberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;    
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
    this.m_strapplicationStatus= "pending";
    this.m_nStudentId = -1;
    this.m_arrStudent = new Array();
}

var m_overifiedStudentList_Info_MemberData = new verifiedStudentList_Info_MemberData();

function verifiedStudentListInfo_loaded ()
{
	loadPage("applicationstatus/verified/verifiedlist.html","workarea","verifiedStudentInfo_init()");
}

function verifiedStudentInfo_init ()
{
	verifiedStudentListInfo_createDataGrid ();
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

function verifiedStudentListInfo_createDataGrid ()
{
	initHorizontalSplitter("#listVerifiedStudents_div_horizontalSplitter", "#listVerifiedStudents_table_students");
	$('#listVerifiedStudents_table_students').datagrid
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
				/*{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return verifiedListInfo_displayImages (row.m_nStudentId);
		        	}
	            },*/
			]],				
		}
	);
	
	$('#listVerifiedStudents_table_students').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					verifiedStudentlistInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oStudentInformationData)
				{
					m_overifiedStudentList_Info_MemberData.m_strSortColumn = strColumn;
					m_overifiedStudentList_Info_MemberData.m_strSortOrder = strOrder;
					verifiedStudentListInfo_list (strColumn, strOrder, m_overifiedStudentList_Info_MemberData.m_nPageNumber, m_overifiedStudentList_Info_MemberData.m_nPageSize);
				}
			}
	)
	verifiedStudentList_initDGPagination();
	verifiedStudentListInfo_list(m_overifiedStudentList_Info_MemberData.m_strSortColumn, m_overifiedStudentList_Info_MemberData.m_strSortOrder, 1, 10);
}

function verifiedStudentList_initDGPagination ()
{
	$('#listVerifiedStudents_table_students').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_overifiedStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				verifiedStudentListInfo_list (m_overifiedStudentList_Info_MemberData.m_strSortColumn, m_overifiedStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listVerifiedStudents_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_overifiedStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
				m_overifiedStudentList_Info_MemberData.m_nPageSize = nPageSize;
				verifiedStudentListInfo_list (m_overifiedStudentList_Info_MemberData.m_strSortColumn, m_overifiedStudentList_Info_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listVerifiedStudents_div_listDetail").innerHTML = "";
			}
		}
	)
}

function verifiedStudentlistInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_overifiedStudentList_Info_MemberData.m_nIndex = nIndex;
	document.getElementById("listVerifiedStudents_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	m_overifiedStudentList_Info_MemberData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,verifiedStudentListInfo_gotXML);	
}

function verifiedStudentListInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "applicationstatus/verified/studentInfoVerified.xslt", 'listVerifiedStudents_div_listDetail');
}

function verifiedStudentListInfo_list (strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_overifiedStudentList_Info_MemberData.m_strSortColumn = strColumn;
	m_overifiedStudentList_Info_MemberData.m_strSortOrder = strOrder;
	m_overifiedStudentList_Info_MemberData.m_nPageNumber = nPageNumber;
	m_overifiedStudentList_Info_MemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "verifiedStudentListInfo_progressbarLoaded ()");
}

function verifyStudentInfo_Student()
{
	var oZenith = new ZenithScholarshipDetails ();
	oZenith.m_nStudentId = m_overifiedStudentList_Info_MemberData.m_nStudentId;
	ZenithStudentInformationDataProcessor.verifiedStatusUpdate(oZenith,studentverifiedResponse);
}

function studentverifiedResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("student verified successfully", "kSuccess");
		document.getElementById("listVerifiedStudents_div_listDetail").innerHTML = "";
		navigate("list","widgets/applicationstatus/verified/verifiedlist.js");
	}
	else
		informUser ("student verification Failed", "kError");
	
}

function searchStudentUID()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nUID = $("#StudentInfo_input_uid").val();
	if($("#StudentInfo_input_uid").val() != "")
		StudentInformationDataProcessor.getStudentUID(oStudentInformationData,studentUIDResponse);
	else
		alert("Please Enter UID Number");
}

function studentUIDResponse(oStudentUIDResponse)
{
	if(oStudentUIDResponse.m_bSuccess)
	{
		document.getElementById("listVerifiedStudents_div_listDetail").innerHTML = "";
		var oStudentInformationData = new StudentInformationData () ;
		oStudentInformationData.m_nStudentId = m_overifiedStudentList_Info_MemberData.m_nStudentId = oStudentUIDResponse.m_arrStudentInformationData[0].m_nStudentId;
		oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
		StudentInformationDataProcessor.getXML (oStudentInformationData,verifiedStudentListInfo_gotXML);
		document.getElementById("StudentInfo_input_uid").value = "";
	}
	else
		alert("Student UID Does not exist");	
}

function verifiedStudentListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_overifiedStudentList_Info_MemberData.m_strapplicationStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,verifiedStudentListInfo_listed);
}

function verifiedStudentListInfo_listed(oStudentResponseData)
{
	clearGridData ("#listVerifiedStudents_table_students");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#listVerifiedStudents_table_students').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	$('#listVerifiedStudents_table_students').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
	HideDialog("dialog");
}

