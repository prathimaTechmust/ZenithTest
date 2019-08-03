var listPrepareChequeInfo_includeDataObjects = 
[
	'widgets/applicationstatus/preparecheque/PrepareChequeInformationData.js',
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js'
];

includeDataObjects (listPrepareChequeInfo_includeDataObjects, "listPrepareChequeInfo_loaded()");

function listPrepareChequeInfo_memberData ()
{
	
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strImageUrl = "";
    this.m_strSortColumn = "m_strPayeeName";
    this.m_strSortOrder = "asc";
    this.m_nStudentId = -1;
    this.m_oStudentData = "";
    this.m_strStatus = "approved";
}

var m_oPrepareChequeListMemberData = new listPrepareChequeInfo_memberData ();

function listPrepareChequeInfo_loaded ()
{
	loadPage ("applicationstatus/preparecheque/listPrepareCheque.html", "workarea", "listPrepareChequeInfo_init ()");
}

function listPrepareChequeInfo_init ()
{
	listPrepareChequeInfo_createDataGrid ();
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

function listPrepareChequeInfo_createDataGrid ()
{
	initHorizontalSplitter("#listPrepareCheque_div_horizontalSplitter", "#listPrepareCheque_table_students");
	$('#listPrepareCheque_table_students').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:150},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'m_fSactionAmount',title:'Sanctioned Amount',sortable:true,width:200,
					formatter:function(value,row,index)
					{
						return row.m_oZenithScholarshipDetails[0].m_fApprovedAmount;
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
	$('#listPrepareCheque_table_students').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					listPrepareChequeInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oScholarshipInformationData)
				{
					m_oPrepareChequeListMemberData.m_strSortColumn = strColumn;
					m_oPrepareChequeListMemberData.m_strSortOrder = strOrder;
					listPrepareChequeInfo_list (strColumn, strOrder, m_oPrepareChequeListMemberData.m_nPageNumber, m_oPrepareChequeListMemberData.m_nPageSize);
				}
			}
	)	
	listPrepareChequeInfo_initDGPagination ();
	listPrepareChequeInfo_list (m_oPrepareChequeListMemberData.m_strSortColumn, m_oPrepareChequeListMemberData.m_strSortOrder, 1, 10);
}

function listPrepareChequeInfo_initDGPagination ()
{
	$('#listPrepareCheque_table_students').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oPrepareChequeListMemberData.m_nPageNumber = nPageNumber;
				listPrepareChequeInfo_list (m_oPrepareChequeListMemberData.m_strSortColumn, m_oPrepareChequeListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listPrepareCheque_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oPrepareChequeListMemberData.m_nPageNumber = nPageNumber;
				m_oPrepareChequeListMemberData.m_nPageSize = nPageSize;
				listPrepareChequeInfo_list (m_oPrepareChequeListMemberData.m_strSortColumn, m_oPrepareChequeListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listPrepareCheque_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listPrepareChequeInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oPrepareChequeListMemberData.m_nIndex = nIndex;
	document.getElementById("listPrepareCheque_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	m_oPrepareChequeListMemberData.m_oStudentInformationData = oRowData;
	StudentInformationDataProcessor.getXML (oStudentInformationData,prepareChequeInfo_gotXML);
}

function prepareChequeInfo_gotXML (strXMLData)
{
	m_oPrepareChequeListMemberData.strApprovedAmountXmlData = strXMLData;
	populateXMLData (strXMLData, "applicationstatus/preparecheque/studentChequePrepare.xslt", 'listPrepareCheque_div_listDetail');
}

function listPrepareChequeInfo_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oPrepareChequeListMemberData.m_strSortColumn = strColumn;
	m_oPrepareChequeListMemberData.m_strSortOrder = strOrder;
	m_oPrepareChequeListMemberData.m_nPageNumber = nPageNumber;
	m_oPrepareChequeListMemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "listPrepareChequeInfo_progressbarLoaded ()");
}

function prepareCheque ()
{
	navigate("addaccount","widgets/applicationstatus/preparecheque/addPrepareCheque.js");
}

/*function searchStudentUIDInfo ()
{
	if($("StudentInfo_input_uid").val() != "")
		navigate("searchuid","widgets/applicationstatus/preparecheque/searchUID.js");
	else
		alert("Please Enter UID Number");
}*/

function searchStudentUID ()
{
	var oStudentInformationData = new StudentInformationData();
	oStudentInformationData.m_nUID = $("#StudentInfo_input_uid").val();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_oPrepareChequeListMemberData.m_strStatus;
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
		document.getElementById("listPrepareCheque_div_listDetail").innerHTML = "";
		var oStudentInformationData = new StudentInformationData () ;
		oStudentInformationData.m_nStudentId = m_oPrepareChequeListMemberData.m_nStudentId = oResponse.m_arrStudentInformationData[0].m_nStudentId;
		oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
		StudentInformationDataProcessor.getXML (oStudentInformationData,prepareChequeInfo_gotXML);
		document.getElementById("StudentInfo_input_uid").value = "";
	}
	else
	{
		alert("Student UID Does not exist in the list");
		document.getElementById("StudentInfo_input_uid").value = "";
	}
}

function listPrepareChequeInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oPrepareChequeInformationData = new PrepareChequeInformationData ();
	var oStudentInformationData = new StudentInformationData();
	oStudentInformationData.m_nAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_oPrepareChequeListMemberData.m_strStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,listPrepareChequeInfo_listed);
}

function listPrepareChequeInfo_listed (oStudentPrepareChequeInfoResponse)
{
	clearGridData ("#listPrepareCheque_table_students");
	for (var nIndex = 0; nIndex < oStudentPrepareChequeInfoResponse.m_arrStudentInformationData.length; nIndex++)
		$('#listPrepareCheque_table_students').datagrid('appendRow',oStudentPrepareChequeInfoResponse.m_arrStudentInformationData[nIndex]);
	$('#listPrepareCheque_table_students').datagrid('getPager').pagination ({total:oStudentPrepareChequeInfoResponse.m_nRowCount, pageNumber:oStudentPrepareChequeInfoResponse.m_nPageNumber});
	HideDialog("dialog");
}

function printChequeDetails()
{
	populateXMLData (m_oPrepareChequeListMemberData.strApprovedAmountXmlData, "applicationstatus/preparecheque/PrintPrepareCheck.xslt", 'printdetailsInfo');
	printDocument();	
}


