var claimedChequesInfo_includeDataObjects =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
]

includeDataObjects(claimedChequesInfo_includeDataObjects,"claimedChequesInfo_Loaded()");

function claimedChequeList_MemberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;    
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
	this.m_strStatus = "cheque claimed";
}

var m_oClaimedChequeListMemberData = new claimedChequeList_MemberData();

function claimedChequesInfo_Loaded()
{
	loadPage("applicationstatus/chequeDetails/claimedCheque/claimedchequelist.html","workarea","claimedChequeList_init()");
}

function claimedChequeList_init ()
{
	populateAcademicYearDropDown('selectClaimedChequeAcademicYear');
	createClaimedChequeList_DataGrid();	
}

function createClaimedChequeList_DataGrid()
{
	initHorizontalSplitter("#claimedChequeList_div_horizontalSplitter", "#claimedChequeList_table");	
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
	applicationPriorityGridColor('claimedChequeList_table');
	claimedChequeInfo_initDGPagination();
	claimedCheque_List(m_oClaimedChequeListMemberData.m_strSortColumn, m_oClaimedChequeListMemberData.m_strSortOrder, 1, 10);
	
}

function claimedChequeInfo_initDGPagination()
{
	$('#claimedChequeList_table').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oClaimedChequeListMemberData.m_nPageNumber = nPageNumber;
				claimedCheque_List(m_oClaimedChequeListMemberData.m_strSortColumn, m_oClaimedChequeListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("claimedChequeList_div_listDetail").innerHTML = "";
				clearFilterBoxes ();
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClaimedChequeListMemberData.m_nPageNumber = nPageNumber;
				m_oClaimedChequeListMemberData.m_nPageSize = nPageSize;				
				claimedCheque_List(m_oClaimedChequeListMemberData.m_strSortColumn, m_oClaimedChequeListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("claimedChequeList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function claimedCheque_List(strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oClaimedChequeListMemberData.m_strSortColumn = strColumn;
	m_oClaimedChequeListMemberData.m_strSortOrder = strOrder;
	m_oClaimedChequeListMemberData.m_nPageNumber = nPageNumber;
	m_oClaimedChequeListMemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "claimedChequeListInfo_progressbarLoaded ()");
}

function claimedChequeListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nAcademicYearId = $("#selectClaimedChequeAcademicYear").val();
	oStudentInformationData.m_strStatus = m_oClaimedChequeListMemberData.m_strStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,m_oClaimedChequeListMemberData.m_strSortColumn, m_oClaimedChequeListMemberData.m_strSortOrder,m_oClaimedChequeListMemberData.m_nPageNumber,m_oClaimedChequeListMemberData.m_nPageSize,claimedChequeListInfo_listed);
}

function claimedChequeListInfo_listed (oStudentResponseData)
{
	clearGridData ("#claimedChequeList_table");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#claimedChequeList_table').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	$('#claimedChequeList_table').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:m_oClaimedChequeListMemberData.m_nPageNumber});
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
	oStudentInformationData.m_nAcademicYearId = $("#selectClaimedChequeAcademicYear").val();
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
	oStudentInformationData.m_nAcademicYearId = $("#selectClaimedChequeAcademicYear").val();
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
		oStudentInformationData.m_nAcademicYearId = $("#selectClaimedChequeAcademicYear").val();
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
