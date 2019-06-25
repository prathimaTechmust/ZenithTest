var disburseChequeListInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
];

includeDataObjects(disburseChequeListInfo_includeDataObjects,"disburseChequeList_Loaded()");

function disburseChequeList_Info_MemberData ()
{
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;    
    this.m_strSortColumn = "m_strStudentName";
    this.m_strSortOrder = "asc";
    this.m_strStatus= "cheque prepared";
    this.m_nStudentId = -1;
    this.m_arrStudent = new Array();
    this.m_nUID = -1;
    this.m_nChequeNumber = -1;
    this.m_strStudentName = "";
}

var m_odisburseChequeList_Info_MemberData = new disburseChequeList_Info_MemberData ();

function disburseChequeList_Loaded ()
{	
	loadPage("applicationstatus/disburse/disburseChequelist.html","workarea","disburseChequeList_init ()");
}

function disburseChequeList_init()
{
	disburseChequeListInfo_createDataGrid ();
	dropdownacademicyear ();
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

function disburseChequeListInfo_createDataGrid ()
{
	initHorizontalSplitter("#disburseChequeList_div_horizontalSplitter", "#disburseChequeList_table_students");
	$('#disburseChequeList_table_students').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:150},
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
			]],				
		}
	);
	$('#disburseChequeList_table_students').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					disburseChequeListInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oStudentInformationData)
				{
					m_odisburseChequeList_Info_MemberData.m_strSortColumn = strColumn;
					m_odisburseChequeList_Info_MemberData.m_strSortOrder = strOrder;
					disburseChequeListInfo_list (strColumn, strOrder, m_odisburseChequeList_Info_MemberData.m_nPageNumber, m_odisburseChequeList_Info_MemberData.m_nPageSize);
				}
			}
	)
	disburseChequeListInfo_list(m_odisburseChequeList_Info_MemberData.m_strSortColumn, m_odisburseChequeList_Info_MemberData.m_strSortOrder, 1, 10);
}

function searchStudentUID ()
{
	if($("StudentInfo_input_uid").val() != "")
		navigate("searchuid","widgets/applicationstatus/disbursecheque/searchUID.js");
	else
	{
		alert("Please Enter UID Number");
		document.getElementById("StudentInfo_input_uid").value = "";		
	}
}

function disburseChequeListInfo_list (strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_odisburseChequeList_Info_MemberData.m_strSortColumn = strColumn;
	m_odisburseChequeList_Info_MemberData.m_strSortOrder = strOrder;
	m_odisburseChequeList_Info_MemberData.m_nPageNumber = nPageNumber;
	m_odisburseChequeList_Info_MemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "disburseChequeListInfo_progressbarLoaded ()");
}

function disburseChequeListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	oStudentInformationData.m_strStatus = m_odisburseChequeList_Info_MemberData.m_strStatus;
	StudentInformationDataProcessor.getStudentStatuslist(oStudentInformationData,disburseChequeListInfo_listed);
}

function disburseChequeListInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_odisburseChequeList_Info_MemberData.m_nIndex = nIndex;
	document.getElementById("disburseChequeList_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	m_odisburseChequeList_Info_MemberData.m_nStudentId = oRowData.m_nStudentId;
	m_odisburseChequeList_Info_MemberData.m_nUID = oRowData.m_nUID;
	m_odisburseChequeList_Info_MemberData.m_strStudentName = oRowData.m_strStudentName;
	m_odisburseChequeList_Info_MemberData.m_nChequeNumber = oRowData.m_oAcademicDetails[0].m_oStudentScholarshipAccount[0].m_nChequeNumber;
	StudentInformationDataProcessor.getXML (oStudentInformationData,approvalStudentListInfo_gotXML);
}

function approvalStudentListInfo_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "applicationstatus/disburse/studentChequeDisburse.xslt", 'disburseChequeList_div_listDetail');
}

function disburseStudent_Cheque ()
{
	navigate("dispursecheque","widgets/applicationstatus/disbursecheque/disbursechequeadd.js");
}

function disburseChequeListInfo_listed (oStudentResponseData)
{
	clearGridData ("#disburseChequeList_table_students");
	for (var nIndex = 0; nIndex < oStudentResponseData.m_arrStudentInformationData.length; nIndex++)
		$('#disburseChequeList_table_students').datagrid('appendRow',oStudentResponseData.m_arrStudentInformationData[nIndex]);
	$('#disburseChequeList_table_students').datagrid('getPager').pagination ({total:oStudentResponseData.m_nRowCount, pageNumber:oStudentResponseData.m_nPageNumber});
	HideDialog("dialog");
}
