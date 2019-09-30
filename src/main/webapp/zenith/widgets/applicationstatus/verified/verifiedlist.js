var verifiedStudentListInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js',
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js'
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
    this.m_bChequeFavouOf = false;
}

var m_overifiedStudentList_Info_MemberData = new verifiedStudentList_Info_MemberData();

function verifiedStudentListInfo_loaded ()
{
	loadPage("applicationstatus/verified/verifiedlist.html","workarea","verifiedStudentInfo_init()");
}

function verifiedStudentInfo_init ()
{
	populateAcademicYearDropDown('selectVerifiedAcademicyear');
	verifiedStudentListInfo_createDataGrid ();	
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
	applicationPriorityGridColor('listVerifiedStudents_table_students');
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
				clearFilterBoxes ();
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
	oStudentInformationData.m_nAcademicYearId = $("#selectVerifiedAcademicyear").val();
	m_overifiedStudentList_Info_MemberData.m_nStudentId = oRowData.m_nStudentId;
	m_overifiedStudentList_Info_MemberData.m_nInstitutionId = oRowData.m_oAcademicDetails[0].m_oInstitutionInformationData.m_nInstitutionId;
	m_overifiedStudentList_Info_MemberData.m_bChequeFavouOf  = oRowData.m_oAcademicDetails[0].m_oInstitutionInformationData.m_bChequeFavouOf;	
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
	document.getElementById("verifyProgress").style.display = "block";
	document.getElementById("verifyStudent").style.display = "none";
	document.getElementById("chooseFileDocument_button_cancel").style.display = "none";
	setTimeout(function(){verifyGetFormData()},500);
}

function verifyGetFormData()
{
	var oFormData = new FormData ();
	var oLoginUser = loginUserId ();
	var nAcademicYearId = $("#selectVerifiedAcademicyear").val();
	oFormData.append('scancopy',$("#ScanCopy")[0].files[0]);
	oFormData.append('studentId',m_overifiedStudentList_Info_MemberData.m_nStudentId);
	if(checkInFavour ())
	{
		oFormData.append('chequefavourId',$("#select_cheque_inFavour_of").val());
	}
	if(document.getElementById("select_radio_Cheque").checked)
		oFormData.append('strPaymentType',$("#select_radio_Cheque").val());
	else
		oFormData.append('strPaymentType',$("#select_radio_DD").val());
	
	oFormData.append('strVerifyRemarks', $("#studentVerify_input_remarks").val());
	oFormData.append('nLoginUserId',oLoginUser);
	oFormData.append('nAcademicyearId',nAcademicYearId);
	ZenithStudentInformationDataProcessor.verifiedStatusUpdate(oFormData,studentverifiedResponse);
}

function checkInFavour ()
{
  var bInFavourOf = false;
   if($("#select_cheque_inFavour_of").val() != "null")
   {
		bInFavourOf = true;
   }
	return bInFavourOf;
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
	oStudentInformationData.m_nAcademicYearId = $("#selectVerifiedAcademicyear").val();
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
		oStudentInformationData.m_nAcademicYearId = $("#selectVerifiedAcademicyear").val();
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
	oStudentInformationData.m_nAcademicYearId = $("#selectVerifiedAcademicyear").val();
	oStudentInformationData.m_strStatus = m_overifiedStudentList_Info_MemberData.m_strapplicationStatus;	
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,m_overifiedStudentList_Info_MemberData.m_strSortColumn, m_overifiedStudentList_Info_MemberData.m_strOrderBy, m_overifiedStudentList_Info_MemberData.m_nPageNumber, m_overifiedStudentList_Info_MemberData.m_nPageSize,verifiedStudentListInfo_listed);
}

function verifiedStudentListInfo_listed(oStudentResponseData)
{
	clearGridData ("#listVerifiedStudents_table_students");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#listVerifiedStudents_table_students').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	$('#listVerifiedStudents_table_students').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:m_overifiedStudentList_Info_MemberData.m_nPageNumber});
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
	populateChequeinFavourof ();	
	
}
function populateChequeinFavourof ()
{
	var oInstitution = new InstitutionInformationData ();
	oInstitution.m_nInstitutionId = m_overifiedStudentList_Info_MemberData.m_nInstitutionId;
	InstitutionInformationDataProcessor.getChequeInFavourOf (oInstitution,chequeFavourOfResponse);
}

function chequeFavourOfResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		if(m_overifiedStudentList_Info_MemberData.m_bChequeFavouOf)
		{
			populateChequeFavour ("select_cheque_inFavour_of",oResponse.m_arrChequeInFavourOf);
			
		}
		else
		{
			populateChequeFavourStudent ("select_cheque_inFavour_of");
		}
	}
	else
	{
		populateChequeFavourStudent ("select_cheque_inFavour_of");
	}
}

function populateChequeFavourStudent (strDropDownId)
{
	var arrChequeFavour = new Array ();
	arrChequeFavour.push(CreateOption(null,"In Favour Of Student"));
	PopulateDD(strDropDownId,arrChequeFavour);
}

function populateChequeFavour (strDropDownId,favourResponse)
{
	var arrChequeFavour = new Array ();
	for(var nIndex = 0; nIndex < favourResponse.length; nIndex++)
	{
		arrChequeFavour.push(CreateOption(favourResponse[nIndex].m_nChequeFavourId,favourResponse[nIndex].m_strChequeFavourOf));
	}
	PopulateDD(strDropDownId,arrChequeFavour);
}

function chooseFileDocument_cancel()
{
	HideDialog("dialog")
}

function reject_Student()
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
	var oUserRejectBy = getLoginUserData ();
	var oZenith = new ZenithScholarshipDetails ();		
	oZenith.m_nStudentId = m_overifiedStudentList_Info_MemberData.m_nStudentId;
	oZenith.m_strStudentRemarks = $("#studentRemarkInfo_input_Remark").val();
	oZenith.m_nAcademicYearId = $("#selectVerifiedAcademicyear").val();
	oZenith.m_oUserUpdatedBy = oUserRejectBy;
	ZenithStudentInformationDataProcessor.rejectStatusUpdate(oZenith,studentrejectResponse);
}

function studentrejectResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		HideDialog("dialog");
		informUser ("student rejected successfully", "kSuccess");
		document.getElementById("listVerifiedStudents_div_listDetail").innerHTML = "";	
		navigate("approvedlist","widgets/applicationstatus/verified/verifiedlist.js");
		
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
