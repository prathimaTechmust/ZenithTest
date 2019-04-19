var vendorSerialNumberList_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorCounterData.js'
];



 includeDataObjects (vendorSerialNumberList_includeDataObjects, "vendorSerialNumberList_loaded()");

function vendorSerialNumberList_memberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "asc";
    this.m_oVendorData = "";
}

var m_oVendorSerialNumberListMemberData = new vendorSerialNumberList_memberData ();

function vendorSerialNumberList_loaded ()
{
	loadPage ("marketplacemanagement/vendorSerialNumberList.html", "workarea", "vendorSerialNumberList_init()");
}

function vendorSerialNumberList_init ()
{
	vendorSerialNumberList_createDataGrid ();
}

function vendorSerialNumberList_createDataGrid ()
{
	initPanelWithoutSplitter ("#vendorSerialNumberList_div_dataGrid", "#vendorSerialNumberList_table_vendors");
	$('#vendorSerialNumberList_table_vendors').datagrid
	(
		{
			fit:true,
			columns:
			[[

				{field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:250,
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
				},
				{field:'m_strPrefix',title:'Prefix',sortable:true,width:200},
				{field:'m_nSerialNumber',title:'serial number',sortable:true,width:120},
				{field:'m_strSuffix',title:'suffix',sortable:true,width:200},
				{field:'Action',title:'Action',sortable:false,width:60,align:'center',
		        	formatter:function(value,row,index)
		        	{
		        		return vendorSerialNumberList_addActions (row, index);
		        	}
		         },
			]],
			onSortColumn: function (strColumn, strOrder, oVendorData)
			{
				m_oVendorSerialNumberListMemberData.m_strSortColumn = strColumn;
				m_oVendorSerialNumberListMemberData.m_strSortOrder = strOrder;
				vendorSerialNumberList_list (strColumn, strOrder, m_oVendorSerialNumberListMemberData.m_nPageNumber, m_oVendorSerialNumberListMemberData.m_nPageSize);
			}
		}
	);
	vendorSerialNumberList_initDGPagination ();
	vendorSerialNumberList_list (m_oVendorSerialNumberListMemberData.m_strSortColumn, m_oVendorSerialNumberListMemberData.m_strSortOrder, 1, 20);
}

function vendorSerialNumberList_addActions (oRow,nIndex)
{
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" onClick="vendorSerialNumberList_edit('+ oRow.m_nId  +')" width="20" /> </td>'+
						'</tr>'+
					'</table>'
    return oImage;
}

function vendorSerialNumberList_edit (nId)
{
	m_oVendorSerialNumberListMemberData.m_nId = nId;
	navigate("editVendorSerialNumber", "widgets/marketplacemanagement/editVendorSerialNumber.js");
}

function vendorSerialNumberList_initDGPagination ()
{
	$('#vendorSerialNumberList_table_vendors').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorSerialNumberListMemberData.m_nPageNumber = nPageNumber;
				vendorSerialNumberList_list (m_oVendorSerialNumberListMemberData.m_strSortColumn, m_oVendorSerialNumberListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorSerialNumberListMemberData.m_nPageNumber = nPageNumber;
				m_oVendorSerialNumberListMemberData.m_nPageSize = nPageSize;
				vendorSerialNumberList_list (m_oVendorSerialNumberListMemberData.m_strSortColumn, m_oVendorSerialNumberListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function vendorSerialNumberList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oVendorSerialNumberListMemberData.m_strSortColumn = strColumn;
	m_oVendorSerialNumberListMemberData.m_strSortOrder = strOrder;
	m_oVendorSerialNumberListMemberData.m_nPageNumber = nPageNumber;
	m_oVendorSerialNumberListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "vendorSerialNumberList_progressbarLoaded ()");
}

function vendorSerialNumberList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oVendorCounterData = vendorSerialNumberList_getFormData ();
	VendorCounterDataProcessor.list(oVendorCounterData, m_oVendorSerialNumberListMemberData.m_strSortColumn, m_oVendorSerialNumberListMemberData.m_strSortOrder, m_oVendorSerialNumberListMemberData.m_nPageNumber, m_oVendorSerialNumberListMemberData.m_nPageSize, vendorSerialNumberList_listed);
}

function vendorSerialNumberList_getFormData ()
{
	var oVendorCounterData = new VendorCounterData ();
	oVendorCounterData.m_oVendorData.m_strCompanyName = $("#filtervendorSerialNumberList_input_vendorName").val();
	return oVendorCounterData;
}

function vendorSerialNumberList_listed (oResponse)
{
	clearGridData ("#vendorSerialNumberList_table_vendors");
	var arrCounterData = oResponse.m_arrVendorCounterData;
	for (var nIndex = 0; nIndex < arrCounterData.length; nIndex++)
	{
		arrCounterData[nIndex].m_strCompanyName = arrCounterData[nIndex].m_oVendorData.m_strCompanyName;
		$('#vendorSerialNumberList_table_vendors').datagrid('appendRow',arrCounterData[nIndex]);
	}
	$('#vendorSerialNumberList_table_vendors').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oVendorSerialNumberListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function vendorSerialNumberList_filter ()
{
	vendorSerialNumberList_list (m_oVendorSerialNumberListMemberData.m_strSortColumn, m_oVendorSerialNumberListMemberData.m_strSortOrder, 1, 20);
}
