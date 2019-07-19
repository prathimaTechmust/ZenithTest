var claimChequesInfo_includeDataObjects =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
]

includeDataObjects(claimChequesInfo_includeDataObjects,"claimChequesInfo_Loaded()");

function claimChequeList_MemberData ()
{
	this.m_strapplicationStatus = "cheque prepared";
}

var m_oClaimChequeListMemberData = new claimChequeList_MemberData();

function claimChequesInfo_Loaded()
{
	loadPage("applicationstatus/claimcheque/claimchequelist.html","workarea","claimChequeList_init()");
}

function claimChequeList_init ()
{
	createClaimChequeList_DataGrid();
	populateAcademicYearDropDown ();
}

function createClaimChequeList_DataGrid()
{
	initHorizontalSplitter("#claimChequeList_div_horizontalSplitter", "#claimChequeList_div_horizontalSplitter");
	
	$("#claimChequeList_table").datagrid
	(
		{
			fit:true,
			columns:
			[[
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
				{field:'m_fApprovedDate',title:'Sanctioned Date',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
		        		return convertTimestampToDate(row.m_oZenithScholarshipDetails[0].m_dApprovedDate);
		        	}	
				},
			]],				
		}
			
	);
	$("#claimChequeList_table").datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				claimChequeListInfo_selectedRowData (rowData, rowIndex);
			},
		}
	)
	claimChequeList();
}

function claimChequeList()
{
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "claimChequeListInfo_progressbarLoaded ()");
}

function claimChequeListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_oClaimChequeListMemberData.m_strapplicationStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,claimChequeListInfo_listed);
}

function claimChequeListInfo_listed (oStudentResponseData)
{
	clearGridData ("#claimChequeList_table");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#claimChequeList_table').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	//$('#claimChequeList_table').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
	HideDialog("dialog");
}

function claimChequeListInfo_selectedRowData(oRowData,nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oClaimChequeListMemberData.m_nIndex = nIndex;
	document.getElementById("claimChequeList_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	m_oClaimChequeListMemberData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,claimChequeListInfo_gotXML);
}

function claimChequeListInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "applicationstatus/claimcheque/claimCheque.xslt", 'claimChequeList_div_listDetail');
}

function claimCheque ()
{
	var oZenithScholarshipDetails = new ZenithScholarshipDetails ();
	oZenithScholarshipDetails.m_nStudentId = m_oClaimChequeListMemberData.m_nStudentId;
	ZenithStudentInformationDataProcessor.claimCheque(oZenithScholarshipDetails,claimChequeResponse);	
}

function claimChequeResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Cheque Claimed Successfully","kSuccess");
		navigate("chequeclaimedlist","widgets/applicationstatus/claimcheque/claimchequelist.js");
	}
	else
		informUser("Cheque Claimed unsuccessfully","kError");
		
}