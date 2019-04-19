var exportImportProviderList_includeDataObjects = 
[
	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];


includeDataObjects (exportImportProviderList_includeDataObjects, "exportImportProviderList_loaded()");

function exportImportProviderList_loaded ()
{
	loadPage ("inventorymanagement/exportimport/exportImportProvidersList.html", "workarea", "exportImportProviderList_init()");
}
	
function exportImportProviderList_MemberData ()
{
	this.m_nId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_nId";
    this.m_strSortOrder = "asc";
}

var m_oExportImportProviderListMemberData = new exportImportProviderList_MemberData ();

function exportImportProviderList_init ()
{
	exportImportProviderList_createDataGrid ();
}

function exportImportProviderList_getFormData ()
{
	var oExportImportProviderData = new ExportImportProviderData();
	oExportImportProviderData.m_strProviderName = dwr.util.getValue("exportImportProvidersList_input_providerName");
	return oExportImportProviderData;
}

function exportImportProviderList_createDataGrid ()
{
	initHorizontalSplitter("#exportImportProvidersList_div_horizontalSplitter", "#exportImportProvidersList_table_providerDG");
	$('#exportImportProvidersList_table_providerDG').datagrid({
		fit:true,
	    columns:[[  
	        {field:'m_strProviderName',title:'Provider Name',sortable:true,width:630},
	        {field:'m_strDescription',title:'Description',sortable:true,width:130,align:'center'},
	        {field:'Action',title:'Action',width:200,align:'center',
	        	formatter:function(value,row,index)
	        	{
	        		 return exportImportProviderList_displayImages (row, index);
	        	}
            },
	    ]]
	});
	$('#exportImportProvidersList_table_providerDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				exportImportProviderList_selectedRowData (rowIndex, rowData);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				m_oExportImportProviderListMemberData.m_strSortColumn = strColumn;
				m_oExportImportProviderListMemberData.m_strSortOrder = strOrder;
				exportImportProviderList_list (strColumn, strOrder, m_oExportImportProviderListMemberData.m_nPageNumber, m_oExportImportProviderListMemberData.m_nPageSize);
			}
			
		}
	)
	exportImportProviderList_initDGPagination ();
	exportImportProviderList_list (m_oExportImportProviderListMemberData.m_strSortColumn, m_oExportImportProviderListMemberData.m_strSortOrder, 1, 10);
}

function exportImportProviderList_initDGPagination ()
{
	$('#exportImportProvidersList_table_providerDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oExportImportProviderListMemberData.m_nPageNumber = nPageNumber;
				exportImportProviderList_list (m_oExportImportProviderListMemberData.m_strSortColumn, m_oExportImportProviderListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("exportImportProvidersList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oExportImportProviderListMemberData.m_nPageNumber = nPageNumber;
				m_oExportImportProviderListMemberData.m_nPageSize = nPageSize;
				exportImportProviderList_list (m_oExportImportProviderListMemberData.m_strSortColumn, m_oExportImportProviderListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("exportImportProvidersList_div_listDetail").innerHTML = "";
			}
		}
	)
	
}

function exportImportProvidersList_filter ()
{
	exportImportProviderList_list (m_oExportImportProviderListMemberData.m_strSortColumn, m_oExportImportProviderListMemberData.m_strSortOrder, 1, 10);
}


function exportImportProviderList_displayImages (row, index)
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" onClick="exportImportProviderList_edit('+ row.m_nId  +')" width="20" /> </td>'+
						'<td></td>'+
						'<td></td>'+
						'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="exportImportProviderList_delete('+row.m_nId +')" width="20"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}

function exportImportProviderList_selectedRowData (nIndex,oRowData)
{
	m_oExportImportProviderListMemberData.m_nIndex = nIndex;
	document.getElementById("exportImportProvidersList_div_listDetail").innerHTML = "";
	var oExportImportProviderData = new ExportImportProviderData();
	oExportImportProviderData.m_nId = oRowData.m_nId;
	ExportImportProviderDataProcessor.getXML (oExportImportProviderData,{
		async:false, 
		callback : function (strXMLData)
		{
			populateXMLData (strXMLData, "inventorymanagement/exportimport/exportImportProvidersDetails.xslt", 'exportImportProvidersList_div_listDetail');
			exportImportProviderList_initializeDetailsDG ();
		}
	});
	ExportImportProviderDataProcessor.get (oExportImportProviderData, exportImportProviderList_gotData);
}

function exportImportProviderList_gotData (oResponse)
{
	var oExportImportProviderData = oResponse.m_arrExportImportProvider[0];
	$('#exportImportProviderDetails_table_classesDG').datagrid ('loadData',oExportImportProviderData.m_oDataExchangeClasses);
}

function exportImportProviderList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oExportImportProviderListMemberData.m_strSortColumn = strColumn;
	m_oExportImportProviderListMemberData.m_strSortOrder = strOrder;
	m_oExportImportProviderListMemberData.m_nPageNumber = nPageNumber;
	m_oExportImportProviderListMemberData.m_nPageSize = nPageSize; 
	loadPage ("inventorymanagement/progressbar.html", "dialog", "exportImportProviderList_progressbarLoaded ()");
}

function exportImportProviderList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oExportImportProviderData = exportImportProviderList_getFormData ();
	ExportImportProviderDataProcessor.list(oExportImportProviderData, m_oExportImportProviderListMemberData.m_strSortColumn, m_oExportImportProviderListMemberData.m_strSortOrder, m_oExportImportProviderListMemberData.m_nPageNumber, m_oExportImportProviderListMemberData.m_nPageSize, exportImportProviderList_listed);
}

function exportImportProviderList_listed (oResponse)
{
	$('#exportImportProvidersList_table_providerDG').datagrid ('loadData', oResponse.m_arrExportImportProvider);
	$('#exportImportProvidersList_table_providerDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oExportImportProviderListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function exportImportProviderList_edit (nId)
{
	m_oExportImportProviderListMemberData.m_nId = nId;
	navigate("EditExportImportProvider", "widgets/inventorymanagement/exportimport/exportImportProvidersEdit.js");
}

function exportImportProviderList_delete (nId)
{
	var oExportImportProviderData = new ExportImportProviderData();
	oExportImportProviderData.m_nId = nId;
	var bConfirm = getUserConfirmation("Do You Really want to Delete ?")
	if (bConfirm == true)
		ExportImportProviderDataProcessor.deleteData(oExportImportProviderData,exportImportProviderList_deleted);
}

function exportImportProviderList_deleted (oResponse)
{
	var oExportImportProviderData = new ExportImportProviderData();
	if(oResponse.m_bSuccess)
	{
		clearGridData ("#salesList_table_salesListDG");
		informUser("Deleted Successfully", "kSuccess");
		document.getElementById("exportImportProvidersList_div_listDetail").innerHTML = "";
		exportImportProviderList_list (m_oExportImportProviderListMemberData.m_strSortColumn, m_oExportImportProviderListMemberData.m_strSortOrder, 1, 10);
	}
}

function exportImportProvidersList_addProvider ()
{
	navigate ("newExportImportProviders", "widgets/inventorymanagement/exportimport/newExportImportProviders.js");
}

function exportImportProviderList_initializeDetailsDG ()
{
	$('#exportImportProviderDetails_table_classesDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strRegisteredClassName',title:'Registered Classes',sortable:true,width:120}
			  	]]
		}
	);
}
