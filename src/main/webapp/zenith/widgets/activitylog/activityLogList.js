var activityLogListInfo_includeDataObjects = 
[
	'widgets/activitylog/ActivityLogInformation.js'
];

includeDataObjects (activityLogListInfo_includeDataObjects, "activityLogListInfo_loaded ()");

function activityLogInfo_memberData ()
{
	this.m_nPageNumber = 0;
	this.m_nPageSize = 10;
	this.m_strSortColumn = "m_strLoginUserName";
	this.m_strOrderBy = "asc";
}

var m_oActivityLogInfo_memberData = new activityLogInfo_memberData();

function activityLogListInfo_loaded ()
{
	loadPage("activityLog/activityLogList.html","workarea","activityLogListInfo_init ()");
}

function activityLogListInfo_init ()
{
	createActivityLog_dataGrid ();
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
				{field:'m_strLoginUserName',title:'User Name',sortable:true,width:300},
				{field:'m_strTaskPerformed',title:'Function Name',sortable:true,width:300},
				{field:'m_dDate',title:'Date',sortable:true,width:200,
					formatter:function(row,value,index)
					{
						return convertTimestampToDate(row);
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
			},
		}
	)
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
	ActivityLogInformationDataProcessor.list(oActivityLogInformation, m_oActivityLogInfo_memberData.m_strSortColumn, m_oActivityLogInfo_memberData.m_strOrderBy,1,10,activityLogListResponse)
}

function activityLogListResponse (oActivityLogResponse)
{
	clearGridData("activityLogInfo_table_logs");
	for(var nIndex = 0; nIndex < oActivityLogResponse.m_arrActivityLog.length; nIndex++)
		$("#activityLogInfo_table_logs").datagrid('appendRow',oActivityLogResponse.m_arrActivityLog[nIndex]);
	HideDialog("dialog");	
}

function activityLogListInfo_SortList(strColumn,strOrderBy,nPageNumber,nPageSize)
{
	var oZenithHelper = new ZenithHelper ();
	oZenithHelper.m_strSortColumn = strColumn;
	oZenithHelper.m_strOrderBy = strOrderBy;
	ActivityLogInformationDataProcessor.sortingList(oZenithHelper,sortActivityLogListResponse);
}

function sortActivityLogListResponse (oResponse)
{
	clearGridData("activityLogInfo_table_logs");
	for(var nIndex = 0; nIndex < oActivityLogResponse.m_arrActivityLog.length; nIndex++)
		$("#activityLogInfo_table_logs").datagrid('appendRow',oActivityLogResponse.m_arrActivityLog[nIndex]);
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


