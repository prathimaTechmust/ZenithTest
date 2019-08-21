var facilitatorInfo_includeDataObjects =
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/usermanagement/facilitator/FacilitatorInformationData.js'
]

includeDataObjects(facilitatorInfo_includeDataObjects,"facilitatorWiseInfo_Loaded()");

function facilitatorList_MemberData ()
{
	this.m_nIndex = -1;
	 this.m_strSortColumn = "m_strStudentName";
	 this.m_strSortOrder = "asc";
	 this.m_nPageSize = 1;
	 this.m_nPageNumber = 10;
	 
		
}
var m_ofacilitatorList_MemberData = new facilitatorList_MemberData();

function facilitatorWiseInfo_Loaded()
{
	loadPage("reports/facilitatorReports.html","workarea", "facilitatorWise_init()");
}

function facilitatorWise_init ()
{
	populateAcademicYearDropDown ('selectReportAcademicYear');
	createFacilitatorWiseList_DataGrid ();	
	facilitatorListToDropDown();
}

function facilitatorListToDropDown()
{
	var oFacilitatorInformationData = new FacilitatorInformationData();
	FacilitatorInformationDataProcessor.list(oFacilitatorInformationData,"","",1,10, loadFacilitatorWiseDropDownResponse);

}

function loadFacilitatorWiseDropDownResponse(oResponse) 
{
	
	$(document).ready(function ()
				{
			   $("#facilitatorWiseInfo_input_name").jqxComboBox({	source :oResponse.m_arrFacilitatorInformationData,
																				displayMember: "m_strFacilitatorName",
																				valueMember: "m_nFacilitatorId",
																				autoComplete:true,
																				searchMode :"startswithignorecase",
																				width :"200px",
																				height:"25px"});
				}
			);	
}


function createFacilitatorWiseList_DataGrid ()
{
	initHorizontalSplitter("#facilitatorWiseList_div_horizontalSplitter", "#facilitatorWiseList_table");
	$('#facilitatorWiseList_table').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:150},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:200},
				{field:'m_strInstitutionName',title:'Institution Name',sortable:true,width:200,
					formatter:function(value,row,index)
					  {						
						return row.m_oAcademicDetails[0].m_oInstitutionInformationData.m_strInstitutionName;
					  }
				},
				{field:'m_strShortCourseName',title:'Course Name',sortable:true,width:200,
					formatter:function(value,row,index)
					  {						
						return row.m_oAcademicDetails[0].m_oCourseInformationData.m_strShortCourseName;
					  }
				},
				{field:'m_fApprovedAmount',title:'Scholarship Amount',sortable:true,width:200,
					
					formatter:function(value,row,index)
					  {						
						return row.m_oZenithScholarshipDetails[0].m_fApprovedAmount;
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

$('#facilitatorWiseList_table').datagrid
(
		{
			onSelect: function (rowIndex, rowData)
			{
				facilitatorlistInfo_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder, oStudentInformationData)
			{
				m_ofacilitatorList_MemberData.m_strSortColumn = strColumn;
				m_ofacilitatorList_MemberData.m_strSortOrder = strOrder;
				facilitatorWiseInfo_list (strColumn, strOrder, m_ofacilitatorList_MemberData.m_nPageNumber, m_ofacilitatorList_MemberData.m_nPageSize);
			}
		}
)
facilitatorWiseList_initDGPagination();
facilitatorWiseInfo_list(m_ofacilitatorList_MemberData.m_strSortColumn, m_ofacilitatorList_MemberData.m_strSortOrder, 1, 10);
}

function facilitatorWiseList_initDGPagination()
{
	
	$('#facilitatorWiseList_table').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_ofacilitatorList_MemberData.m_nPageNumber = nPageNumber;
				facilitatorWiseInfo_list (m_ofacilitatorList_MemberData.m_strSortColumn, m_ofacilitatorList_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("facilitatorWiseList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_ofacilitatorList_MemberData.m_nPageNumber = nPageNumber;
				m_ofacilitatorList_MemberData.m_nPageSize = nPageSize;
				facilitatorWiseInfo_list (m_ofacilitatorList_MemberData.m_strSortColumn, m_ofacilitatorList_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("facilitatorWiseList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function facilitatorlistInfo_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_ofacilitatorList_MemberData.m_nIndex = nIndex;
	document.getElementById("facilitatorWiseList_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectReportAcademicYear").val();
	m_ofacilitatorList_MemberData.m_nStudentId = oRowData.m_nStudentId;
	StudentInformationDataProcessor.getXML (oStudentInformationData,facilitatorListInfo_gotXML);	
}
function facilitatorListInfo_gotXML(strXMLData) 
{
	m_ofacilitatorList_MemberData.m_PrintData = strXMLData;
	populateXMLData (strXMLData, "reports/facilitatorReports.xslt", 'facilitatorWiseList_div_listDetail');
}

function facilitatorWiseInfo_list (strColumn,strOrder,nPageNumber,nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_ofacilitatorList_MemberData.m_strSortColumn = strColumn;
	m_ofacilitatorList_MemberData.m_strSortOrder = strOrder;
	m_ofacilitatorList_MemberData.m_nPageNumber = nPageNumber;
	m_ofacilitatorList_MemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "facilitatorWiseListInfo_progressbarLoaded ()");
}

function facilitatorWiseListInfo_progressbarLoaded ()
{
		createPopup('dialog', '', '', true);
		var oStudentInformationData = new StudentInformationData ();
		StudentInformationDataProcessor.list(oStudentInformationData, m_ofacilitatorList_MemberData.m_strSortColumn, m_ofacilitatorList_MemberData.m_strSortOrder, m_ofacilitatorList_MemberData.m_nPageNumber, m_ofacilitatorList_MemberData.m_nPageSize, facilitatorWiseListInfo_listed);
}
function facilitatorWiseListInfo_listed (oResponseData)
{
	clearGridData ("#facilitatorWiseList_table");
	HideDialog("dialog");	
}

function searchfacilitatorName() 
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nFacilitatorId = $("#facilitatorWiseInfo_input_name").val();
	oStudentInformationData.m_strAcademicYear = $("#selectReportAcademicYear").val();
	StudentInformationDataProcessor.getFacilitatorWiseStudent(oStudentInformationData, getFacilitatorWiseStudentDetails);
	
}

function getFacilitatorWiseStudentDetails (oResponse)
{
	clearGridData ("#facilitatorWiseList_table");
	for (var nIndex = 0; nIndex < oResponse.m_arrStudentInformationData.length; nIndex++)
		$('#facilitatorWiseList_table').datagrid('appendRow',oResponse.m_arrStudentInformationData[nIndex]);
	$('#facilitatorWiseList_table').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:oResponse.m_nPageNumber});
	HideDialog("dialog");
}

function printStudentDetails()
{
	populateXMLData (m_ofacilitatorList_MemberData.m_PrintData , "applicationstatus/chequeDetails/claimedCheque/PrintClaimedList.xslt", 'printdetailsInfo');
	printDocument();	
}
