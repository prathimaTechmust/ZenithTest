var logList_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/logmanagement/logData.js'
];



 includeDataObjects (logList_includeDataObjects, "logList_loaded()");

function logList_memberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			logList_list ("", "", 1, 20);
			$('#filterlog_input_userName').combobox('textbox').unbind('keydown', m_oLogListMemberData.m_oKeyDownHandler);
			$('#filterlog_input_userName').combobox('textbox').focus();
		}
    };
}
var m_oLogListMemberData = new logList_memberData ();

function logList_loaded ()
{
	loadPage ("logmanagement/logList.html", "workarea", "logList_init ()");
}

function logList_init ()
{
	initUserCombobox ("#filterlog_input_userName", "UserName", m_oLogListMemberData.m_oKeyDownHandler);
	$("#filterlog_input_fromDate").datebox();
	$("#filterlog_input_fromDate").datebox('textbox')[0].placeholder = "From Date";
	$("#filterlog_input_toDate").datebox();
	$("#filterlog_input_toDate").datebox('textbox')[0].placeholder = "To Date";
	logList_createDataGrid ();
}

function logList_createDataGrid ()
{
	initPanelWithoutSplitter ("#div_dataGrid", "#logList_table_logListDG");
	$('#logList_table_logListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strUserName',title:'User Name',sortable:true,width:150,
				  		formatter:function(value,row,index)
			        	{
					  		try
				  			{
					  			return row.m_oUserData.m_strUserName;
				  			}
				  			catch(oException)
				  			{
				  				return '';
				  			}
			        	}
				  	},
					{field:'m_strFunctionName',title:'Function Name',sortable:true,width:200},
					{field:'m_strDate',title:'Date',sortable:true,width:150},
				]],
				onSortColumn: function (strColumn, strOrder)
				{
					strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
					m_oLogListMemberData.m_strSortColumn = strColumn;
					m_oLogListMemberData.m_strSortOrder = strOrder;
					logList_list (strColumn, strOrder, m_oLogListMemberData.m_nPageNumber, m_oLogListMemberData.m_nPageSize);
				}
		}
	)
	logList_initDGPagination ();
	logList_list (m_oLogListMemberData.m_strSortColumn,m_oLogListMemberData.m_strSortOrder,1, 20);
}

function logList_initDGPagination ()
{
	$('#logList_table_logListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oLogListMemberData.m_nPageNumber = $('#logList_table_logListDG').datagrid('getPager').pagination('options').pageNumber;
				logList_list (m_oLogListMemberData.m_strSortColumn, m_oLogListMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oLogListMemberData.m_nPageNumber = $('#logList_table_logListDG').datagrid('getPager').pagination('options').pageNumber;
				m_oLogListMemberData.m_nPageSize = $('#logList_table_logListDG').datagrid('getPager').pagination('options').pageSize;
				logList_list (m_oLogListMemberData.m_strSortColumn, m_oLogListMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function logList_filter ()
{
	logList_list (m_oLogListMemberData.m_strSortColumn,m_oLogListMemberData.m_strSortOrder, 1, 20);
}

function logList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oLogListMemberData.m_strSortColumn = strColumn;
	m_oLogListMemberData.m_strSortOrder = strOrder;
	m_oLogListMemberData.m_nPageNumber = nPageNumber;
	m_oLogListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "logList_progressbarLoaded ()");
}

function logList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oLogData = logList_getFormData ();
	LogDataProcessor.list(oLogData, m_oLogListMemberData.m_strSortColumn, m_oLogListMemberData.m_strSortOrder, m_oLogListMemberData.m_nPageNumber, m_oLogListMemberData.m_nPageSize, logList_listed);
}

function logList_getFormData ()
{
	var oLogData = new LogData ();
	oLogData.m_oUserData.m_nUserId = $('#filterlog_input_userName').combobox('getValue');
	oLogData.m_strFromDate =  FormatDate ($('#filterlog_input_fromDate').datebox('getValue'));
	oLogData.m_strToDate =  FormatDate ($('#filterlog_input_toDate').datebox('getValue'));
	return oLogData;
}

function logList_listed (oResponse)
{
	$('#logList_table_logListDG').datagrid('loadData', oResponse.m_arrLogData);
	$('#logList_table_logListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oLogListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}
