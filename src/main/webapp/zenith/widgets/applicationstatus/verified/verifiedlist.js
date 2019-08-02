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
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:200},
				{field:'m_strCity',title:'City',sortable:true,width:200}, 
				{field:'m_strStatus',title:'Application Status',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
		        		return row.m_oZenithScholarshipDetails[0].m_strStatus;
		        	}
				},		
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
	m_overifiedStudentList_Info_MemberData.strXMLData = strXMLData;
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
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "verifiedStudentListInfo_progressbarLoaded ()");
}

function printStudentDetails()
{
	populateXMLData (m_overifiedStudentList_Info_MemberData.strXMLData, "applicationstatus/verified/printStudentDetails.xslt", 'printdetailsInfo');
	printDocument();	
}

function verifyStudentInfo_Student()
{	
	createPopup('dialog', '', '', true);
	var oFormData = new FormData ();
	oFormData.append('scancopy',$("#ScanCopy")[0].files[0]);
	oFormData.append('studentId',m_overifiedStudentList_Info_MemberData.m_nStudentId);
	ZenithStudentInformationDataProcessor.verifiedStatusUpdate(oFormData,studentverifiedResponse);
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
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_overifiedStudentList_Info_MemberData.m_strapplicationStatus; 
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
	{
		alert("Student UID Does not exist in the list");
		document.getElementById("StudentInfo_input_uid").value = "";
	}
			
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

function scanImage(studentdocument,fileInputTypeID,tdId,buttonId)
{	
	var oFileSource = document.getElementById(fileInputTypeID.id);
	document.getElementById(buttonId.id).style.color = "white";
	var btnVerifyButton = document.getElementById(buttonId.id);
    btnVerifyButton.disabled = false;
	var studentInfo_input_document = document.getElementById(studentdocument.id);
	showImage(oFileSource, studentInfo_input_document);
}

function showImage(oFileSource,studentInfo_input_document)
{
     var fr=new FileReader();
     fr.onload = function(e)
                     {
                         studentInfo_input_document.src = this.result;
                     };
     fr.readAsDataURL(oFileSource.files[0]);    
}

function studentuploadScan_documentPreview(scan_input_previewimageId)
{
	 var viewImageId = document.getElementById(scan_input_previewimageId.id)
	 var fileInputimageurl = document.getElementById (scan_input_previewimageId.id).src;
	 m_overifiedStudentList_Info_MemberData.m_strScanDocUploadURL = fileInputimageurl;
	 loadPage("applicationstatus/verified/scanDocumentPreview.html","secondDialog","showScanDocumentPreview()");    
}


function showScanDocumentPreview ()
{
    createPopup ('secondDialog', '', '', true);
    document.getElementById('secondDialog').style.position = "fixed";
    $(".imagePreview").attr('src', m_overifiedStudentList_Info_MemberData.m_strScanDocUploadURL);    
}


function scanDocument_cancelImagePreview() 
{
	  HideDialog ("secondDialog");
}


function showChooseFile() {
	
	 loadPage("applicationstatus/verified/choosefile.html","dialog","chooseFileInit()");    
	
}
function chooseFileInit() 
{
	createPopup('dialog','','chooseFileDocument_cancel', true);
	
}

function chooseFileDocument_cancel()
{
	HideDialog("dialog")
}

