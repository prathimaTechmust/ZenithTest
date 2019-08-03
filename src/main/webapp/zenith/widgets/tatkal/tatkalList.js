var tatkalStudentListInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js'
];

includeDataObjects (tatkalStudentListInfo_includeDataObjects, "tatkalStudentListInfo_loaded ()");

function tatkalStudentInfo_memberData ()
{
	this.m_nPageNumber = 0;
	this.m_nPageSize = 10;
	this.m_strSortColumn = "";
	this.m_strOrderBy = "asc";
}

var m_oTatkalStudentListInfo_memberData = new tatkalStudentInfo_memberData();

function tatkalStudentListInfo_loaded ()
{
	loadPage("tatkal/tatkalStudentsList.html","workarea","tatkalStudentListInfo_init ()");
}

function tatkalStudentListInfo_init ()
{
	createTatkalStudent_dataGrid ();
	populateAcademicyearsDropdown();
}

function createTatkalStudent_dataGrid ()
{
	initHorizontalSplitter("#tatkalStudentListInfo_div_horizontalSplitter", "#tatkalStudentListInformation");
	$('#tatkalStudentListInformation').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:200},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:300},
				{field:'m_strFacilitatorName',title:'Facililtator Name',sortable:true,width:300,
					formatter:function(value,row,index)
					{
						return row.m_oFacilitatorInformationData.m_strFacilitatorName;
					}
				},
				{field:'m_strStatus',title:'Application Status',sortable:true,width:300,
					formatter:function(value,row,index)
					{
						return row.m_oZenithScholarshipDetails[0].m_strStatus;
					}
				},
			]],
		}
	);
	$('#tatkalStudentListInformation').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					tatkalStudentListInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					m_oTatkalStudentListInfo_memberData.m_strSortColumn = strColumn;
					m_oTatkalStudentListInfo_memberData.m_strOrderBy = strOrder;
					tatkalStudentListInfo_SortList (strColumn, strOrder, m_oTatkalStudentListInfo_memberData.m_nPageNumber, m_oTatkalStudentListInfo_memberData.m_nPageSize);
				}
			}
	)	
	tatkalStudentListInfo_initDGPagination ();
	tatkalStudentListInfo_List (m_oTatkalStudentListInfo_memberData.m_strSortColumn, m_oTatkalStudentListInfo_memberData.m_strOrderBy, 1, 10);
}

function tatkalStudentListInfo_initDGPagination ()
{
	$('#tatkalStudentListInformation').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oTatkalStudentListInfo_memberData.m_nPageNumber = nPageNumber;
				tatkalStudentListInfo_List (m_oTatkalStudentListInfo_memberData.m_strSortColumn, m_oTatkalStudentListInfo_memberData.m_strOrderBy, nPageNumber, nPageSize);
				document.getElementById("tatkalStudentListInfo_div_listStudentDetails").innerHTML = "";
			},
		}
	)
}

function populateAcademicyearsDropdown ()
{
	var oAcademicYear = new AcademicYear ();
	AcademicYearProcessor.list(oAcademicYear,"","",0,10,academicYearResponse);	
}

function academicYearResponse (oYearResponse)
{
	populateDropDownValues("selectacademicyearwiseStudents",oYearResponse);
}

function populateDropDownValues (dropdownId,oResponse)
{
	var arrAcademicYears = new Array();
	for(var nIndex = 0; nIndex < oResponse.m_arrAcademicYear.length; nIndex++)
	{
		arrAcademicYears.push(CreateOption(oResponse.m_arrAcademicYear[nIndex].m_strAcademicYear,oResponse.m_arrAcademicYear[nIndex].m_strAcademicYear));		
	}
	PopulateDD(dropdownId,arrAcademicYears);
}

function tatkalStudentListInfo_List (strColumn,strOrderBy,nPageNumber,nPageSize)
{
	m_oTatkalStudentListInfo_memberData.m_strSortColumn = strColumn;
	m_oTatkalStudentListInfo_memberData.m_strOrderBy = strOrderBy;
	m_oTatkalStudentListInfo_memberData.m_nPageNumber = nPageNumber;
	m_oTatkalStudentListInfo_memberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "tatkalStudentListInfo_progressbarLoaded ()");
}

function tatkalStudentListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformation = new StudentInformationData ();
	StudentInformationDataProcessor.studentTatkalList(oStudentInformation,tatkalStudentListResponse);
}

function tatkalStudentListResponse (oTatkalStudentResponse)
{
	clearGridData("tatkalStudentListInformation");
	for(var nIndex = 0; nIndex < oTatkalStudentResponse.m_arrStudentInformationData.length; nIndex++)
		$("#tatkalStudentListInformation").datagrid('appendRow',oTatkalStudentResponse.m_arrStudentInformationData[nIndex]);
	HideDialog("dialog");	
}

function tatkalStudentListInfo_SortList(strColumn,strOrderBy,nPageNumber,nPageSize)
{
	var oZenithHelper = new ZenithHelper ();
	oZenithHelper.m_strSortColumn = strColumn;
	oZenithHelper.m_strOrderBy = strOrderBy;
	StudentInformationDataProcessor.sortingList(oZenithHelper,sortTatkalStudentListResponse);
}

function sortTatkalStudentListResponse (oResponse)
{
	clearGridData("tatkalStudentListInformation");
	for(var nIndex = 0; nIndex < oResponse.m_arrStudentInformationData.length; nIndex++)
		$("#tatkalStudentListInformation").datagrid('appendRow',oResponse.m_arrStudentInformationData[nIndex]);
}

function tatkalStudentListInfo_selectedRowData (oRowData, rowIndex)
{
	m_oTatkalStudentListInfo_memberData.m_nIndex = rowIndex;
	document.getElementById("tatkalStudentListInfo_div_listStudentDetails").innerHTML = "";
	var oStudentInformation = new StudentInformationData ();
	oStudentInformation.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformation.m_strAcademicYear = $("#selectacademicyearwiseStudents").val();
	StudentInformationDataProcessor.getXML (oStudentInformation,tatkalStudentListInfo_gotXML);
}

function tatkalStudentListInfo_gotXML(strXMLData)
{
	m_oTatkalStudentListInfo_memberData.m_PrintDetails =  strXMLData;
	populateXMLData (strXMLData, "tatkal/tatkalStudentListInfo.xslt", 'tatkalStudentListInfo_div_listStudentDetails');
}

function changePriority (nStudentId,applicationPriority)
{
	m_oTatkalStudentListInfo_memberData.m_strApplicationPriority = applicationPriority;
	loadPage("tatkal/tatkalPriorityInfo.html","dialog",'loadTatkalPriority('+nStudentId+')');	
}

function loadTatkalPriority (nStudentId)
{
	createPopup("dialog","priority_change_submitButton","priority_change_cancelButton",true);	
	m_oTatkalStudentListInfo_memberData.m_nStudentId = nStudentId;
	if(m_oTatkalStudentListInfo_memberData.m_strApplicationPriority == 'High')
		document.getElementById("select_priority_HighRadioButton").checked = true;
	else if(m_oTatkalStudentListInfo_memberData.m_strApplicationPriority == 'Normal')
		document.getElementById("select_priority_MediumRadioButton").checked = true;
	else
		document.getElementById("select_priority_NormalRadioButton").checked = true;		
}

function changePriority_submit()
{
	var oStudentInformationData = new StudentInformationData();
	oStudentInformationData.m_nStudentId = m_oTatkalStudentListInfo_memberData.m_nStudentId;
	if(document.getElementById("select_priority_HighRadioButton").checked)
		oStudentInformationData.m_nApplicationPriority = $("#select_priority_HighRadioButton").val();
	else if(document.getElementById("select_priority_MediumRadioButton").checked)
		oStudentInformationData.m_nApplicationPriority = $("#select_priority_MediumRadioButton").val();
	else
		oStudentInformationData.m_nApplicationPriority = $("#select_priority_NormalRadioButton").val();	
	StudentInformationDataProcessor.updateTatkalPriority(oStudentInformationData,tatkalUpdateResponse);
}

function tatkalUpdateResponse(oStudentTatkalResponse)
{
	HideDialog("dialog");
	if(oStudentTatkalResponse.m_bSuccess)
	{
		informUser("Application Priority is Changed Successfully","kSuccess");
		navigate('tatkalList','widgets/tatkal/tatkalList.js');
	}
	else
		informUser("Application Priority is Changed UnSuccess","kError");
}

function changePriority_cancel ()
{
	HideDialog("dialog");
}

function tatkalStudentListInfo_filter ()
{
	var oStudentFilterData = new StudentInformationData();
	oStudentFilterData.m_strAcademicYear = $("#selectacademicyearwiseStudents").val();
	if($("#filterTatkalStudentListInfo_input_studentName").val() != '')
		oStudentFilterData.m_strStudentName = $("#filterTatkalStudentListInfo_input_studentName").val();
	else if($("#filterTatkalStudentListInfo_input_studentAadhar").val() != '')
		oStudentFilterData.m_nStudentAadharNumber = $("#filterTatkalStudentListInfo_input_studentAadhar").val();
	else
		oStudentFilterData.m_strPhoneNumber = $("#filterTatkalStudentListInfo_input_PhoneNumber").val();
	StudentInformationDataProcessor.filterStudentData(oStudentFilterData,studentFilteredResponse);	
}

function studentFilteredResponse(oResponse)
{
	if(oResponse.m_bSuccess)
	{
		document.getElementById("filterTatkalStudentListInfo_input_studentName").value = "";
		document.getElementById("filterTatkalStudentListInfo_input_PhoneNumber").value = "";
		document.getElementById("filterTatkalStudentListInfo_input_studentAadhar").value = "";
		clearGridData ("#tatkalStudentListInformation");
		for (var nIndex = 0; nIndex < oResponse.m_arrStudentInformationData.length; nIndex++)
			$('#tatkalStudentListInformation').datagrid('appendRow',oResponse.m_arrStudentInformationData[nIndex]);
		$('#tatkalStudentListInformation').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:oResponse.m_nPageNumber});
	}
	else
		informUser("no search result found","kError");	
}

function printTatkal()
{
	populateXMLData (m_oTatkalStudentListInfo_memberData.m_PrintDetails, "applicationstatus/chequeDetails/claimedCheque/PrintClaimedList.xslt", 'printdetailsInfo');
	printDocument();	
}
