var activityLogListInfo_includeDataObjects = 
[
	'widgets/activitylog/ActivityLogInformation.js'
];

includeDataObjects (activityLogListInfo_includeDataObjects, "activityLogListInfo_loaded ()");

function activityLogInfo_memberData ()
{
	this.m_nPageNumber = 1;
	this.m_nPageSize = 10;
	this.m_strSortColumn = "m_dCreatedOn";
	this.m_strOrderBy = "desc";
}

var m_oActivityLogInfo_memberData = new activityLogInfo_memberData();

function activityLogListInfo_loaded ()
{
	loadPage("activityLog/activityLogList.html","workarea","activityLogListInfo_init ()");
}

function activityLogListInfo_init ()
{
	createActivityLog_dataGrid ();
	loadLoginUsersDropDown ();
}

function createActivityLog_dataGrid ()
{
	initHorizontalSplitter("#activityLogInfo_div_horizontalSplitter", "#activityLogInfo_table_logs");
	$('#activityLogInfo_table_logs').datagrid
	(
		{
			fit:true,
			columns:
			[[
				{field:'m_nActivityId',title:'ID',sortable:true,width:200},
				{field:'m_strLoginUserName',title:'User Name',sortable:true,width:200},
				{field:'m_strTaskPerformed',title:'Function Name',sortable:true,width:500},
				{field:'m_dCreatedOn',title:'Date',sortable:true,width:200,
					formatter:function(row,value,index)
					{
						return convertTimestampToDateTime(row);
					}
				},				
			]],
		}
	);
	$('#activityLogInfo_table_logs').datagrid
	(
			{
				onSelect: function (rowIndex, rowData)
				{
					activityLogListInfo_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					m_oActivityLogInfo_memberData.m_strSortColumn = strColumn;
					m_oActivityLogInfo_memberData.m_strOrderBy = strOrder;
					activityLogListInfo_SortList (strColumn, strOrder, m_oActivityLogInfo_memberData.m_nPageNumber, m_oActivityLogInfo_memberData.m_nPageSize);
				}
			}
	)	
	activityLogListInfo_initDGPagination ();
	activityLogListInfo_List (m_oActivityLogInfo_memberData.m_strSortColumn, m_oActivityLogInfo_memberData.m_strOrderBy, 1, 10);
}

function activityLogListInfo_initDGPagination ()
{
	$('#activityLogInfo_table_logs').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function (nPageNumber, nPageSize)
			{
				m_oActivityLogInfo_memberData.m_nPageNumber = nPageNumber;
				activityLogListInfo_List (m_oActivityLogInfo_memberData.m_strSortColumn, m_oActivityLogInfo_memberData.m_strOrderBy, nPageNumber, nPageSize);
				document.getElementById("activityLogInfo_div_listDetail").innerHTML = "";
				clearFilterBoxes ();
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oActivityLogInfo_memberData.m_nPageNumber = nPageNumber;
				m_oActivityLogInfo_memberData.m_nPageSize = nPageSize;
				activityLogListInfo_List (m_oActivityLogInfo_memberData.m_strSortColumn, m_oActivityLogInfo_memberData.m_strOrderBy, nPageNumber, nPageSize);
				document.getElementById("activityLogInfo_div_listDetail").innerHTML = "";
			}
		}
	)
}

function loadLoginUsersDropDown ()
{
	var oActivityLog = new ActivityLogInformation ();
	ActivityLogInformationDataProcessor.getLoginUsers(oActivityLog,loginUsersResponse);
}

function loginUsersResponse(oLoginUsersResponse)
{															
	$(document).ready(function ()
			{
		   $("#filterActivityLogInfo_select_loginUser").jqxComboBox({	source :oLoginUsersResponse.m_arrActivityLog,
			   															placeHolder:'Select User Name',
																		displayMember: "m_strLoginUserName",
																		valueMember: "m_strLoginUserName",
																		autoComplete:true,
																		searchMode :"startswithignorecase",
																		width :"200px",
																		height:"25px"});
			}
		);	
}

function activityLogListInfo_List (strColumn,strOrderBy,nPageNumber,nPageSize)
{
	m_oActivityLogInfo_memberData.m_strSortColumn = strColumn;
	m_oActivityLogInfo_memberData.m_strOrderBy = strOrderBy;
	m_oActivityLogInfo_memberData.m_nPageNumber = nPageNumber;
	m_oActivityLogInfo_memberData.m_nPageSize = nPageSize;
	loadPage ("progressbarmanagement/progressbar.html", "dialog", "activityLogListInfo_progressbarLoaded ()");
}

function activityLogListInfo_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oActivityLogInformation = new ActivityLogInformation ();
	ActivityLogInformationDataProcessor.list(oActivityLogInformation, m_oActivityLogInfo_memberData.m_strSortColumn, m_oActivityLogInfo_memberData.m_strOrderBy,m_oActivityLogInfo_memberData.m_nPageNumber,m_oActivityLogInfo_memberData.m_nPageSize,activityLogListResponse)
}

function activityLogListResponse (oActivityLogResponse)
{
	clearGridData("#activityLogInfo_table_logs");
	for(var nIndex = 0; nIndex < oActivityLogResponse.m_arrActivityLog.length; nIndex++)
		$("#activityLogInfo_table_logs").datagrid('appendRow',oActivityLogResponse.m_arrActivityLog[nIndex]);
	$('#activityLogInfo_table_logs').datagrid('getPager').pagination ({total:oActivityLogResponse.m_nRowCount, pageNumber:m_oActivityLogInfo_memberData.m_nPageNumber});
	HideDialog("dialog");	
}

function activityLogListInfo_SortList(strColumn,strOrderBy,nPageNumber,nPageSize)
{
	var oZenithHelper = new ZenithHelper ();
	oZenithHelper.m_strSortColumn = strColumn;
	oZenithHelper.m_strOrderBy = strOrderBy;
	oZenithHelper.m_nPageNo = nPageNumber;
	oZenithHelper.m_nPageSize = nPageSize;
	ActivityLogInformationDataProcessor.sortingList(oZenithHelper,activityLogListResponse);
}

function activityLogListInfo_selectedRowData (oRowData, rowIndex)
{
	m_oActivityLogInfo_memberData.m_nIndex = rowIndex;
	document.getElementById("activityLogInfo_div_listDetail").innerHTML = "";
	var oActivityLogInformation = new ActivityLogInformation ();
	oActivityLogInformation.m_nActivityId = oRowData.m_nActivityId;
	ActivityLogInformationDataProcessor.getXML (oActivityLogInformation,activityLogListInfo_gotXML);
}

function activityLogListInfo_gotXML(strXMLData)
{
	populateXMLData (strXMLData, "activityLog/activityLog.xslt", 'activityLogInfo_div_listDetail');
}

function activityLogListInfo_filter ()
{
	var bIsEmpty = true;
	var oActivityLogInformation = new ActivityLogInformation ();
	if($("#filterActivityLogInfo_select_loginUser").val() != '')
	{
		bIsEmpty = false;
		oActivityLogInformation.m_strLoginUserName = $("#filterActivityLogInfo_select_loginUser").val();
	}	
	 if ($("#filterActivityLogInfo_input_functionName").val() != '')
	{
		 bIsEmpty = false;
		oActivityLogInformation.m_strTaskPerformed = $("#filterActivityLogInfo_input_functionName").val();
	}	
	if($("#filterActivityLogInfo_input_fromdate").val() != '' && $("#filterActivityLogInfo_input_todate").val() != '')
	{
		bIsEmpty = false;
		oActivityLogInformation.m_dFromDate = $("#filterActivityLogInfo_input_fromdate").val();
		oActivityLogInformation.m_dToDate	= $("#filterActivityLogInfo_input_todate").val();
	}
	if(bIsEmpty)
		alert("Please Enter Valid Data!");
	else
		ActivityLogInformationDataProcessor.getFilteredActivityLog(oActivityLogInformation,activityLogFilteredResponse);
}

function activityLogFilteredResponse(oFilteredActivityLogResponse)
{
	if(oFilteredActivityLogResponse.m_bSuccess)
	{		
		clearGridData("#activityLogInfo_table_logs");
		for(var nIndex = 0; nIndex < oFilteredActivityLogResponse.m_arrActivityLog.length; nIndex++)
			$("#activityLogInfo_table_logs").datagrid('appendRow',oFilteredActivityLogResponse.m_arrActivityLog[nIndex]);
	}
	else
		informUser("no search result found","kError");
}

function clearFilterBoxes ()
{
	$("#filterActivityLogInfo_select_loginUser").jqxComboBox('clearSelection');
	document.getElementById("filterActivityLogInfo_input_functionName").value = "";
	document.getElementById("filterActivityLogInfo_input_fromdate").value = "";
	document.getElementById("filterActivityLogInfo_input_todate").value = "";
}
