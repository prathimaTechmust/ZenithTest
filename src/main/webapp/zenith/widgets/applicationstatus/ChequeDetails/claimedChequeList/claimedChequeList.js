var claimedChequesInfo_includeDataObjects =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
]

includeDataObjects(claimedChequesInfo_includeDataObjects,"claimedChequesInfo_Loaded()");

function claimedChequeList_MemberData ()
{
	this.m_strStatus = "cheque claimed";
}

var m_oClaimedChequeListMemberData = new claimedChequeList_MemberData();

function claimedChequesInfo_Loaded()
{
	loadPage("applicationstatus/chequeDetails/claimedCheque/claimedchequelist.html","workarea","claimedChequeList_init()");
}

function claimedChequeList_init ()
{
	createClaimedChequeList_DataGrid();
	populateAcademicYearDropDown('selectClaimedChequeAcademicYear');
}

function createClaimedChequeList_DataGrid()
{
	initHorizontalSplitter("#claimedChequeList_div_horizontalSplitter", "#claimedChequeList_div_horizontalSplitter");	
	$("#claimedChequeList_table").datagrid
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
				{field:'m_dClaimedDate',title:'Claimed Date',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
						var claimedDate = null;
						if(row.m_oZenithScholarshipDetails[0].m_dClaimedDate != null)
						{
							claimedDate = convertTimestampToDayMonthYear(row.m_oZenithScholarshipDetails[0].m_dClaimedDate);
						}
		        		return claimedDate;
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
	$("#claimedChequeList_table").datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				claimedChequeListInfo_selectedRowData (rowData, rowIndex);
			}
		}
	)
	claimedChequeList();
	applicationPriorityGridColor('claimedChequeList_table');
}

function claimedChequeList()
{
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "claimedChequeListInfo_progressbarLoaded ()");
}

function claimedChequeListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectClaimedChequeAcademicYear").val();
	oStudentInformationData.m_strStatus = m_oClaimedChequeListMemberData.m_strStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,claimedChequeListInfo_listed);
}

function claimedChequeListInfo_listed (oStudentResponseData)
{
	clearGridData ("#claimedChequeList_table");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#claimedChequeList_table').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	//$('#claimChequeList_table').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
	HideDialog("dialog");
}

function claimedChequeListInfo_selectedRowData(oRowData,nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oClaimedChequeListMemberData.m_nIndex = nIndex;
	document.getElementById("claimedChequeList_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectClaimedChequeAcademicYear").val();
	m_oClaimedChequeListMemberData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,claimedChequeListInfo_gotXML);
}

function claimedChequeListInfo_gotXML (strXMLData)
{
	m_oClaimedChequeListMemberData.oClaimedChequeData =  strXMLData;
	populateXMLData (strXMLData, "applicationstatus/chequeDetails/claimedCheque/claimedCheque.xslt", 'claimedChequeList_div_listDetail');
}

function searchStudentUID ()
{
	var oStudentInformationData = new StudentInformationData();
	oStudentInformationData.m_nUID = $("#StudentInfo_input_uid").val();
	oStudentInformationData.m_strAcademicYear = $("#selectClaimedChequeAcademicYear").val();
	oStudentInformationData.m_strStatus = m_oClaimedChequeListMemberData.m_strStatus;
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
		document.getElementById("claimedChequeList_div_listDetail").innerHTML = "";
		var oStudentInformationData = new StudentInformationData () ;
		oStudentInformationData.m_nStudentId = m_oClaimedChequeListMemberData.m_nStudentId = oResponse.m_arrStudentInformationData[0].m_nStudentId;
		oStudentInformationData.m_strAcademicYear = $("#selectClaimedChequeAcademicYear").val();
		StudentInformationDataProcessor.getXML (oStudentInformationData,claimedChequeListInfo_gotXML);
		document.getElementById("StudentInfo_input_uid").value = "";
	}
	else
	{
		alert("Student UID Does not exist in the list");
		document.getElementById("StudentInfo_input_uid").value = "";
	}
}

function printClaimedChequeList()
{
	populateXMLData (m_oClaimedChequeListMemberData.oClaimedChequeData, "applicationstatus/chequeDetails/claimedCheque/PrintClaimedList.xslt", 'printdetailsInfo');
	printDocument();	
}
