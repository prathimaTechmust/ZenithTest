var toBeClaimChequesInfo_includeDataObjects =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
]

includeDataObjects(toBeClaimChequesInfo_includeDataObjects,"toBeClaimChequesInfo_Loaded()");

function toBeClaimChequeList_MemberData ()
{
	this.m_strapplicationStatus = "cheque disbursed";
}

var m_oToBeClaimChequeListMemberData = new toBeClaimChequeList_MemberData();

function toBeClaimChequesInfo_Loaded()
{
	loadPage("applicationstatus/chequeDetails/toBeClaimCheque/toBeClaimChequeList.html","workarea","toBeClaimChequeList_init()");
}

function toBeClaimChequeList_init ()
{
	createToBeClaimChequeList_DataGrid();
	dropdownacademicyear();
}

function createToBeClaimChequeList_DataGrid()
{
	initHorizontalSplitter("#toBeClaimChequeList_div_horizontalSplitter", "#toBeClaimChequeList_div_horizontalSplitter");
	
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
				{field:'m_nChequeNumber',title:'Cheque Number',sortable:true,width:200,
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
	toBeClaimChequeList();
	applicationPriorityGridColor('toBeClaimChequeList_table');
}

function toBeClaimChequeList()
{
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "toBeClaimChequeListInfo_progressbarLoaded ()");
}

function toBeClaimChequeListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_oToBeClaimChequeListMemberData.m_strapplicationStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,toBeClaimChequeListInfo_listed);
}

function toBeClaimChequeListInfo_listed (oResponseData)
{
	clearGridData ("#toBeClaimChequeList_table");
	for (var nIndex = 0; nIndex < oResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#toBeClaimChequeList_table').datagrid('appendRow',oResponseData.m_arrStudentInformationData[nIndex]);
	//$('#claimChequeList_table').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
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
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	m_oToBeClaimChequeListMemberData.m_nStudentId = oRowData.m_nStudentId;
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
	var oZenithScholarshipDetails = new ZenithScholarshipDetails ();
	oZenithScholarshipDetails.m_nStudentId = m_oToBeClaimChequeListMemberData.m_nStudentId;
	oZenithScholarshipDetails.m_dClaimedDate = $("#studentChequeInfo_input_chequeDate").val();
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
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
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
		oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
		StudentInformationDataProcessor.getXML (oStudentInformationData,toBeClaimChequeListInfo_gotXML);
		document.getElementById("StudentInfo_input_uid").value = "";
	}
	else
	{
		alert("Student UID Does not exist in the list");
		document.getElementById("StudentInfo_input_uid").value = "";
	}
}

function printToBeClaimedChequeList()
{
	populateXMLData (m_oToBeClaimChequeListMemberData.oTobeclaimedChequeData, "applicationstatus/chequeDetails/toBeClaimCheque/PrintToBeClaimed.xslt", 'printdetailsInfo');
	printDocument();	
}