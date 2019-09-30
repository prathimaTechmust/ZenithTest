var toBeClaimChequesInfo_includeDataObjects =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
]

includeDataObjects(toBeClaimChequesInfo_includeDataObjects,"toBeClaimChequesInfo_Loaded()");

function toBeClaimChequeList_MemberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;    
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
	this.m_strapplicationStatus = "cheque disbursed";
}

var m_oToBeClaimChequeListMemberData = new toBeClaimChequeList_MemberData();

function toBeClaimChequesInfo_Loaded()
{
	loadPage("applicationstatus/chequeDetails/toBeClaimCheque/toBeClaimChequeList.html","workarea","toBeClaimChequeList_init()");
}

function toBeClaimChequeList_init ()
{
	populateAcademicYearDropDown('selectToBeClaimAcademicYear');
	createToBeClaimChequeList_DataGrid();	
}

function createToBeClaimChequeList_DataGrid()
{
	initHorizontalSplitter("#toBeClaimChequeList_div_horizontalSplitter", "#toBeClaimChequeList_table");
	
	$("#toBeClaimChequeList_table").datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:200},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'m_strInstitutionName',title:'Institution Name',sortable:true,width:300,
					formatter:function(value,row,index)
		        	{
		        		return row.m_oAcademicDetails[0].m_oInstitutionInformationData.m_strInstitutionName;
		        	}					
				},
				{field:'m_strPaymentType',title:'Payment Type',sortable:true,width:200,
					formatter:function(value,row,index)
					{
						return row.m_oZenithScholarshipDetails[0].m_strPaymentType;
					}					
				},
				{field:'m_nChequeNumber',title:'Cheque/DD Number',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
		        		return row.m_oAcademicDetails[0].m_oStudentScholarshipAccount[0].m_nChequeNumber;
		        	}
				},				
				{field:'m_fSanctionedAmount',title:'Sanctioned Amount',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
		        		return row.m_oAcademicDetails[0].m_oStudentScholarshipAccount[0].m_fSanctionedAmount;
		        	}	
				},
				{field:'m_dApprovedDate',title:'Sanctioned Date',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
		        		return convertTimestampToDayMonthYear(row.m_oZenithScholarshipDetails[0].m_dApprovedDate);
		        	}	
				},
				
				{field:'m_strStatus',title:'Application Status',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
		        		return row.m_oZenithScholarshipDetails[0].m_strStatus;
		        	}
				},		 
			]],				
		}
			
	);
	$("#toBeClaimChequeList_table").datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				toBeClaimChequeListInfo_selectedRowData (rowData, rowIndex);
			}
		}
	)
	applicationPriorityGridColor('toBeClaimChequeList_table');
	toBeClaimChequeInfo_initDGPagination();
	toBeClaimCheque_List(m_oToBeClaimChequeListMemberData.m_strSortColumn, m_oToBeClaimChequeListMemberData.m_strSortOrder, 1, 10);	
}

function toBeClaimChequeInfo_initDGPagination() 
{
	$('#toBeClaimChequeList_table').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oToBeClaimChequeListMemberData.m_nPageNumber = nPageNumber;
				toBeClaimCheque_List(m_oToBeClaimChequeListMemberData.m_strSortColumn, m_oToBeClaimChequeListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("toBeClaimChequeList_div_listDetail").innerHTML = "";
				clearFilterBoxes ();
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oToBeClaimChequeListMemberData.m_nPageNumber = nPageNumber;
				m_oToBeClaimChequeListMemberData.m_nPageSize = nPageSize;				
				toBeClaimCheque_List(m_oToBeClaimChequeListMemberData.m_strSortColumn, m_oToBeClaimChequeListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("toBeClaimChequeList_div_listDetail").innerHTML = "";
			}
		}
	)
}
function toBeClaimCheque_List(strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oToBeClaimChequeListMemberData.m_strSortColumn = strColumn;
	m_oToBeClaimChequeListMemberData.m_strSortOrder = strOrder;
	m_oToBeClaimChequeListMemberData.m_nPageNumber = nPageNumber;
	m_oToBeClaimChequeListMemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "toBeClaimChequeListInfo_progressbarLoaded ()");
}


function toBeClaimChequeListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nAcademicYearId = $("#selectToBeClaimAcademicYear").val();
	oStudentInformationData.m_strStatus = m_oToBeClaimChequeListMemberData.m_strapplicationStatus;
	oStudentInformationData.m_nStudentId = m_oToBeClaimChequeListMemberData.m_nStudentId;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,m_oToBeClaimChequeListMemberData.m_strSortColumn, m_oToBeClaimChequeListMemberData.m_strSortOrder,m_oToBeClaimChequeListMemberData.m_nPageNumber,m_oToBeClaimChequeListMemberData.m_nPageSize,toBeClaimChequeListInfo_listed);
}

function toBeClaimChequeListInfo_listed (oResponseData)
{
	clearGridData ("#toBeClaimChequeList_table");
	for (var nIndex = 0; nIndex < oResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#toBeClaimChequeList_table').datagrid('appendRow',oResponseData.m_arrStudentInformationData[nIndex]);
	$('#toBeClaimChequeList_table').datagrid('getPager').pagination ({total:oResponseData.m_nRowCount, pageNumber:m_oToBeClaimChequeListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function toBeClaimChequeListInfo_selectedRowData(oRowData,nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oToBeClaimChequeListMemberData.m_nIndex = nIndex;
	document.getElementById("toBeClaimChequeList_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_nAcademicYearId = $("#selectToBeClaimAcademicYear").val();
	m_oToBeClaimChequeListMemberData.m_nStudentId = oRowData.m_nStudentId;
	m_oToBeClaimChequeListMemberData.m_oStudentInformationData = oRowData;
	StudentInformationDataProcessor.getXML (oStudentInformationData,toBeClaimChequeListInfo_gotXML);
}

function toBeClaimChequeListInfo_gotXML (strXMLData)
{
	m_oToBeClaimChequeListMemberData.oTobeclaimedChequeData = strXMLData;
	populateXMLData (strXMLData, "applicationstatus/chequeDetails/toBeClaimCheque/toBeClaimCheque.xslt", 'toBeClaimChequeList_div_listDetail');
}

function claimCheque ()
{
	loadPage("applicationstatus/chequeDetails/toBeClaimCheque/toBeClaimChequeDate.html", "dialog", "claimChequeDateNew()");
}
function claimChequeDateNew() 
{
	claimChequeDateInit();
}

function claimChequeDateInit()
{
	createPopup('dialog','claimChequeDate_submit','claimChequeDate_cancle', true);
	validateDate();
	initFormValidateBoxes("claimChequeForm");
}

function validateDate() 
{
	var date = new Date();
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	var today = year + "-" + month + "-" + day;
	document.getElementById('studentChequeInfo_input_chequeDate').value = today;
}

function claimChequeDate_submit() 
{ 
	if(claimChequeInfo_validate())
	{
			loadPage ("include/process.html", "ProcessDialog", "claimCheque_progressbarLoaded ()");
	}
	else
	{
		alert("Please Fill Mandiatory Fields");
		$('#claimChequeForm').focus();
	}
}

function claimChequeInfo_validate() 
{
	return validateForm("claimChequeForm");
	
}

function claimCheque_progressbarLoaded()
{
	createPopup('ProcessDialog', '', '', true);
	var oUserClaimedBy = getLoginUserData ();
	var oZenithScholarshipDetails = new ZenithScholarshipDetails ();
	oZenithScholarshipDetails.m_nStudentId = m_oToBeClaimChequeListMemberData.m_nStudentId;
	oZenithScholarshipDetails.m_nAcademicYearId = $("#selectToBeClaimAcademicYear").val();
	oZenithScholarshipDetails.m_dClaimedDate = $("#studentChequeInfo_input_chequeDate").val();
	oZenithScholarshipDetails.m_oUserUpdatedBy = oUserClaimedBy;
	ZenithStudentInformationDataProcessor.claimCheque(oZenithScholarshipDetails,toBeClaimChequeResponse);		
}

function claimChequeDate_cancel()
{
	HideDialog("dialog")
}

function toBeClaimChequeResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{ 
		HideDialog ("ProcessDialog");
		informUser("Cheque Claimed Successfully","kSuccess");
		HideDialog("dialog")
		navigate("tobeClaimChequeList","widgets/applicationstatus/ChequeDetails/toBeClaimedChequeList/toBeClaimchequelist.js");
		
	}
	else
		informUser("Cheque Claimed unsuccessfully","kError");		
}

function searchStudentUID ()
{
	var oStudentInformationData = new StudentInformationData();
	oStudentInformationData.m_nUID = $("#StudentInfo_input_uid").val();
	oStudentInformationData.m_nAcademicYearId = $("#selectToBeClaimAcademicYear").val();
	oStudentInformationData.m_strStatus = m_oToBeClaimChequeListMemberData.m_strapplicationStatus;
	if($("StudentInfo_input_uid").val() != "")
		StudentInformationDataProcessor.getStudentUID (oStudentInformationData, studentUIDInformation);	
	else
	{
		alert("Please Enter UID Number");
		document.getElementById("StudentInfo_input_uid").value = "";
	}		
}

function studentUIDInformation (oResponse)
{	
	if(oResponse.m_bSuccess)
	{
		document.getElementById("toBeClaimChequeList_div_listDetail").innerHTML = "";
		var oStudentInformationData = new StudentInformationData () ;
		oStudentInformationData.m_nStudentId = m_oToBeClaimChequeListMemberData.m_nStudentId = oResponse.m_arrStudentInformationData[0].m_nStudentId;
		oStudentInformationData.m_nAcademicYearId = $("#selectToBeClaimAcademicYear").val();
		StudentInformationDataProcessor.getXML (oStudentInformationData,toBeClaimChequeListInfo_gotXML);
		document.getElementById("StudentInfo_input_uid").value = "";
	}
	else
	{
		alert("Student UID Does not exist in the list");
		document.getElementById("StudentInfo_input_uid").value = "";
	}
}

function reIssueCheque_Details() 
{	
	loadPage("applicationstatus/reIssueCheque/reIssueChequeRemark.html","dialog","reIssueCheque_init()");		
}

function reIssueCheque_init()
{	
	createPopup('dialog','#chequeRemarkInfo_button_submit','chequeRemarkInfo_button_cancel', true);
	initFormValidateBoxes('chequeRemarkForm');
	m_oToBeClaimChequeListMemberData.m_nStudentId = m_oToBeClaimChequeListMemberData.m_oStudentInformationData.m_nStudentId;
	m_oToBeClaimChequeListMemberData.m_nAcademicId =  m_oToBeClaimChequeListMemberData.m_oStudentInformationData.m_oAcademicDetails[0].m_nAcademicId;
}

function chequeRemarkInfo_submit()
{
	
	if(chequeRemarkValidate ())
		loadPage ("include/process.html", "ProcessDialog", "chequeRemark_progressbarLoaded ()");
	else
	{
		alert("Please Enter Remarks");
		$("#chequeRemarkForm").focus();
	}	
}
function chequeRemarkValidate ()
{
	return validateForm("chequeRemarkForm");
}

function chequeRemarkInfo_cancel() {
	
	HideDialog("dialog");
}

function chequeRemark_progressbarLoaded() {
	
	createPopup('dialog','','',true);
	var oUserChequeReissuedBy = getLoginUserData ();
	var oZenith = new ZenithScholarshipDetails ();	
	oZenith.m_nStudentId = m_oToBeClaimChequeListMemberData.m_nStudentId;
	oZenith.m_nAcademicId = m_oToBeClaimChequeListMemberData.m_nAcademicId;
	oZenith.m_strChequeRemark = $("#chequeRemarkInfo_input_Remark").val();
	oZenith.m_nAcademicYearId = $("#selectToBeClaimAcademicYear").val();
	oZenith.m_oUserUpdatedBy = oUserChequeReissuedBy;
	ZenithStudentInformationDataProcessor.reIssueChequeStatusUpdate(oZenith,reIssueChequeResponse);
}

function reIssueChequeResponse (oResponse)
{
		
	if(oResponse.m_bSuccess)
	{
		informUser("Application Status Sent to prepareCheque Successfully","kSuccess");
		HideDialog("dialog");
		navigate("reIssueCheque","widgets/applicationstatus/disbursecheque/disbursechequelist.js");
	}
	else
		informUser("Application Status Sent to prepareCheque Failed","kError");
		
}


function printToBeClaimedChequeList()
{
	populateXMLData (m_oToBeClaimChequeListMemberData.oTobeclaimedChequeData, "applicationstatus/chequeDetails/toBeClaimCheque/PrintToBeClaimed.xslt", 'printdetailsInfo');
	printDocument();	
}