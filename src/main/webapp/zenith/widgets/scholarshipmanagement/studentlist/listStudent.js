var listStudentInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js',
	'widgets/scholarshipmanagement/courselist/CourseInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js'
];

 includeDataObjects (listStudentInfo_includeDataObjects, "listStudentInfo_loaded()");

function listStudentInfo_memberData ()
{
	this.m_nSelectedStudentId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize = 10;
    this.m_strImageUrl = "";
    this.m_strSortColumn = "m_strStudentName";
    this.m_strOrderBy = "asc";
}

var m_oStudentInfoListMemberData = new listStudentInfo_memberData ();

function listStudentInfo_loaded ()
{
	loadPage ("scholarshipmanagement/student/listStudentInfo.html", "workarea", "listStudentInfo_init ()");
}

function listStudentInfo_init ()
{
	listStudentInfo_createDataGrid ();
	dropdownacademicyear();
}
/*function academicyear ()
{	
		  var oDate = new Date();
		  var ocurrentAcademicYear = oDate.getFullYear();
		  var selectyear = document.getElementById("selectacademicyearwiseStudents");
		  var option = document.createElement('option');
		  option.text = option.value = ocurrentAcademicYear +"-"+(++ocurrentAcademicYear);
		  selectyear.add(option,0);		
}*/

function listStudentInfo_createDataGrid ()
{
	initHorizontalSplitter("#listStudentInfo_div_horizontalSplitter", "#listStudentInfo_table_students");
	$('#listStudentInfo_table_students').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nUID',title:'UID',sortable:true,width:200},
				{field:'m_strStudentName',title:'Student Name',sortable:true,width:300},
				{field:'m_strFatherName',title:'Father Name',sortable:true,width:200},
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:200},
				{field:'m_strCity',title:'City',sortable:true,width:200},
				{field:'m_strStatus',title:'Application Status',sortable:true,width:200,
					formatter:function(value,row,index)
		        	{
		        		return row.m_oZenithScholarshipDetails[0].m_strStatus;
		        	}
				},		
				{field:'Actions',title:'Action',width:80,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return listStudentInfo_displayImages (row.m_nStudentId,index);
		        	}
	            },
			]],				
		}
	);
	$('#listStudentInfo_table_students').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					listStudentInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder, oStudentInformationData)
				{
					m_oStudentInfoListMemberData.m_strSortColumn = strColumn;
					m_oStudentInfoListMemberData.m_strOrderBy = strOrder;
					listStudentInfo_list (strColumn, strOrder, m_oStudentInfoListMemberData.m_nPageNumber, m_oStudentInfoListMemberData.m_nPageSize);
				}
			}
	)
	applicationPriorityGridColor('listStudentInfo_table_students');
	listStudentInfo_initDGPagination ();
	listStudentInfo_list (m_oStudentInfoListMemberData.m_strSortColumn, m_oStudentInfoListMemberData.m_strOrderBy, 1, 10);
}

function listStudentInfo_initDGPagination ()
{
	$('#listStudentInfo_table_students').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oStudentInfoListMemberData.m_nPageNumber = nPageNumber;
				listStudentInfo_list (m_oStudentInfoListMemberData.m_strSortColumn, m_oStudentInfoListMemberData.m_strOrderBy, nPageNumber, nPageSize);
				document.getElementById("listStudentInfo_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oStudentInfoListMemberData.m_nPageNumber = nPageNumber;
				m_oStudentInfoListMemberData.m_nPageSize = nPageSize;
				listStudentInfo_list (m_oStudentInfoListMemberData.m_strSortColumn, m_oStudentInfoListMemberData.m_strOrderBy, nPageNumber, nPageSize);
				document.getElementById("listStudentInfo_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listStudentInfo_selectedRowData (oRowData, nIndex)
{
 	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	m_oStudentInfoListMemberData.m_nIndex = nIndex; 
	document.getElementById("listStudentInfo_div_listDetail").innerHTML = "";
	var oStudentInformationData = new StudentInformationData () ;
	oStudentInformationData.m_nStudentId = oRowData.m_nStudentId;
	oStudentInformationData.m_strAcademicYear = $("#selectacademicyear").val();
	StudentInformationDataProcessor.getXML (oStudentInformationData,listStudentInfo_gotXML);
}

function listStudentInfo_gotXML (strXMLData)
{
	m_oStudentInfoListMemberData.m_PrintData =  strXMLData;
	populateXMLData (strXMLData, "scholarshipmanagement/student/studentInfoDetails.xslt", 'listStudentInfo_div_listDetail');
}

function listStudentInfo_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber is expected to be of type number");
	assert.isNumber(nPageSize, "nPageSize is expected to be of type number");
	m_oStudentInfoListMemberData.m_strSortColumn = strColumn;
	m_oStudentInfoListMemberData.m_strOrderBy = strOrder;
	m_oStudentInfoListMemberData.m_nPageNumber = nPageNumber;
	m_oStudentInfoListMemberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "listStudentInfo_progressbarLoaded ()");
}

function listStudentInfo_displayImages (nStudentId,index)
{
 	assert.isNumber(nStudentId, "nStudentId expected to be a Number.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img src="images/edit_database_24.png" width="20" align="center" id="editImageId" title="Edit" onClick="listStudentInfo_edit('+nStudentId+')"/> </td>'+
							'<td> <img src="images/delete.png" width="20" align="center" id="deleteImageId" title="Delete" onClick="listStudentInfo_delete('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listStudentInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oStudentInformationData = new StudentInformationData ();
	//StudentInformationDataProcessor.list(oStudentInformationData, m_oStudentInfoListMemberData.m_strSortColumn, m_oStudentInfoListMemberData.m_strOrderBy, m_oStudentInfoListMemberData.m_nPageNumber, m_oStudentInfoListMemberData.m_nPageSize, listStudentInfo_listed);
	StudentInformationDataProcessor.studentTatkalList(oStudentInformationData,listStudentInfo_listed);
	
}

function listStudentInfo_listed (oStudentInfoResponse)
{
	clearGridData ("#listStudentInfo_table_students");
	for (var nIndex = 0; nIndex < oStudentInfoResponse.m_arrStudentInformationData.length; nIndex++)
		$('#listStudentInfo_table_students').datagrid('appendRow',oStudentInfoResponse.m_arrStudentInformationData[nIndex]);
	$('#listStudentInfo_table_students').datagrid('getPager').pagination ({total:oStudentInfoResponse.m_nRowCount, pageNumber:oStudentInfoResponse.m_nPageNumber});
	HideDialog("dialog");
}

function listStudentInfo_edit (nStudentId)
{
	assert.isNumber(nStudentId, "nStudentId expected to be a Number.");
	m_oStudentInfoListMemberData.m_nSelectedStudentId = nStudentId;
	m_oStudentInfoListMemberData.m_strAcademicYear = $("#selectacademicyear").val();
	navigate ("actionInformation", "widgets/scholarshipmanagement/studentlist/editStudentInfo.js");
}

function listStudentInfo_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");	
	var oListData = $("#listStudentInfo_table_students").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nStudentId = oData.m_nStudentId;
	var bUserConfirm = getUserConfirmation("Are you sure do you want to delete?");
	if(bUserConfirm)
		StudentInformationDataProcessor.deleteData(oStudentInformationData,student_delete_Response);
}

function student_delete_Response (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("student deleted successfully", "kSuccess");
		document.getElementById("listStudentInfo_div_listDetail").innerHTML = "";
		navigate("studentlist","widgets/scholarshipmanagement/studentlist/listStudent.js");
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function listStudentInfo_showAddPopup ()
{
	navigate ("newstudent", "widgets/scholarshipmanagement/studentlist/newStudentInfo.js");
}

function listOldStudentInfo_showAddPopup ()
{
	navigate ("oldstudent", "widgets/scholarshipmanagement/studentlist/oldStudentInfo.js");
}

function printStudentDetails()
{
	populateXMLData (m_oStudentInfoListMemberData.m_PrintData , "applicationstatus/chequeDetails/claimedCheque/PrintClaimedList.xslt", 'printdetailsInfo');
	printDocument();	
}

