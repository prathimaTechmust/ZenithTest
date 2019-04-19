var vendorList_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
];



 includeDataObjects (vendorList_includeDataObjects, "vendorList_loaded()");

function vendorList_memberData ()
{
	this.m_oSelectedVendorId = -1;
	this.m_oSelectedContactId = -1
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "asc";
    this.m_oVendorData = "";
    this.m_bVerify = false;
    this.m_bAllowPublishing = false;
}

var m_oVendorListMemberData = new vendorList_memberData ();

function vendorList_loaded ()
{
	loadPage ("marketplacemanagement/vendorList.html", "workarea", "vendorList_initAdmin ()");
}

function vendorList_initAdmin ()
{
	m_oVendorListMemberData.m_strActionItemsFunction = "vendorList_addActions (row, index)";
	vendorList_createDataGrid ();
}

function vendorList_getFormData () 
{
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = $("#filterVendor_input_vendorName").val();
	oVendorData.m_strCity = $("#filterVendor_input_city").val();
	var oDemographyData = new DemographyData ();
	oVendorData.m_oDemography = oDemographyData ;
	return oVendorData;
}

function vendorList_createDataGrid ()
{
	initPanelWithoutSplitter ("#vendorList_div_dataGrid", "#vendorList_table_vendors");
	$('#vendorList_table_vendors').datagrid
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
				{field:'m_strCity',title:'City',sortable:true,width:200},
				{field:'m_strTelephone',title:'Telephone',sortable:true,width:150},
				{field:'m_strEmail',title:'Email',sortable:true,width:200},
				{field:'Status',title:'Status',sortable:true,width:70,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return row.m_bVerified ? '<b style="color:green">Verified</b>' : '<b style="color:red">Blocked</b>';
		        	}
				}
			]],
			onSortColumn: function (strColumn, strOrder, oVendorData)
			{
				m_oVendorListMemberData.m_strSortColumn = strColumn;
				m_oVendorListMemberData.m_strSortOrder = strOrder;
				vendorList_list (strColumn, strOrder, m_oVendorListMemberData.m_nPageNumber, m_oVendorListMemberData.m_nPageSize);
			}
		}
	);
	vendorList_DetailsSubGrid ();
	vendorList_initDGPagination ();
	vendorList_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, 1, 20);
}

function vendorList_DetailsSubGrid ()
{
	$('#vendorList_table_vendors').datagrid({
	    view: detailview,
	    detailFormatter:function(index,row)
	    {
	        return '<div id="vendorList_div_vendorDetailSubGrid' + index + '" style="width:100%"></div>';
	    },
	    onExpandRow: function(index,row)
	    {
	    	var oDivision = $(this).datagrid('getRowDetail',index).find('#vendorList_div_vendorDetailSubGrid'+index);
	    	document.getElementById("vendorList_div_vendorDetailSubGrid"+index).innerHTML = "";
	    	oDivision.panel({
            	width:'100%',
                height:'100%',
                border:false,
                cache:false,
                href:vendorList_selectedRowData (row, index),
                onLoad:function()
                {
                    $('#vendorList_table_vendors').datagrid('fixDetailRowHeight',index);
                }
            });
            $('#vendorList_table_vendors').datagrid('fixDetailRowHeight',index);
	    }
	});
}

function vendorList_initDGPagination ()
{
	$('#vendorList_table_vendors').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorListMemberData.m_nPageNumber = nPageNumber;
				vendorList_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorListMemberData.m_nPageNumber = nPageNumber;
				m_oVendorListMemberData.m_nPageSize = nPageSize;
				vendorList_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function vendorList_selectedRowData (oRowData, nIndex)
{
	loadPage ("include/process.html", "ProcessDialog", "vendorList_DetailprogressbarLoaded ()");
	m_oVendorListMemberData.m_oVendorData = oRowData;
	m_oVendorListMemberData.m_nIndex = nIndex;
}

function vendorList_DetailprogressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var oVendorData = new VendorData ();
	oVendorData.m_nClientId = m_oVendorListMemberData.m_oVendorData.m_nClientId; 
	PurchaseDataProcessor.getVendorDetails (m_oVendorListMemberData.m_oVendorData.m_nClientId,	{
		async:false, 
		callback: function (strXMLData)
		{
			populateXMLData (strXMLData, "marketplacemanagement/vendorDetails.xslt", 'vendorList_div_vendorDetailSubGrid'+m_oVendorListMemberData.m_nIndex);
		}
	});
	HideDialog ("ProcessDialog");
	$('#vendorList_table_vendors').datagrid('fixDetailRowHeight',m_oVendorListMemberData.m_nIndex);
}

function vendorList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oVendorListMemberData.m_strSortColumn = strColumn;
	m_oVendorListMemberData.m_strSortOrder = strOrder;
	m_oVendorListMemberData.m_nPageNumber = nPageNumber;
	m_oVendorListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "vendorList_progressbarLoaded ()");
}

function vendorList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oVendorData = vendorList_getFormData ();
	VendorDataProcessor.listVendor(oVendorData, m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, m_oVendorListMemberData.m_nPageNumber, m_oVendorListMemberData.m_nPageSize, vendorList_listed); 
}

function vendorList_showAddVendorPopup ()
{
	navigate ("vendorInfo", "widgets/vendormanagement/newVendorInfo.js");
}

function vendorList_listed (oVendorResponse)
{
	clearGridData ("#vendorList_table_vendors");
	$('#vendorList_table_vendors').datagrid('loadData',oVendorResponse.m_arrVendorData);
	$('#vendorList_table_vendors').datagrid('getPager').pagination ({total:oVendorResponse.m_nRowCount, pageNumber:m_oVendorListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function vendorList_filter ()
{
	vendorList_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, 1, 20);
}

function vendorInfo_handleAfterSave ()
{
	vendorList_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, 1, 20);
}

function vendorList_verifyVendor ()
{
	m_oVendorListMemberData.m_bVerify = true;
	loadPage ("include/process.html", "ProcessDialog", "vendorList_initProgressbar ()");
}

function vendorList_blockVendor ()
{
	m_oVendorListMemberData.m_bVerify = false;
	loadPage ("include/process.html", "ProcessDialog", "vendorList_initProgressbar ()");
}

function vendorList_initProgressbar ()
{
	createPopup('ProcessDialog', '', '', true);
	var oVendorData = new VendorData ();
	oVendorData.m_nClientId = m_oVendorListMemberData.m_oVendorData.m_nClientId;
	VendorDataProcessor.verify (oVendorData, m_oVendorListMemberData.m_bVerify, vendorList_vendorVerified);
}

function vendorList_vendorVerified (oResponse)
{
	if(oResponse.m_bSuccess)
	{
    	document.getElementById("vendorList_div_vendorDetailSubGrid"+m_oVendorListMemberData.m_nIndex).innerHTML = "";
		vendorList_selectedRowData (oResponse.m_arrVendorData[0], m_oVendorListMemberData.m_nIndex);
		vendorList_updateRow (oResponse.m_arrVendorData[0]);
	}
	HideDialog("ProcessDialog");
}

function vendorList_blockPublishing ()
{
	m_oVendorListMemberData.m_bAllowPublishing = false;
	loadPage ("include/process.html", "ProcessDialog", "vendorList_initPublishingProgressbar ()");
}

function vendorList_allowPublishing ()
{
	m_oVendorListMemberData.m_bAllowPublishing = true;
	loadPage ("include/process.html", "ProcessDialog", "vendorList_initPublishingProgressbar ()");
}

function vendorList_initPublishingProgressbar ()
{
	createPopup('ProcessDialog', '', '', true);
	var oVendorData = new VendorData ();
	oVendorData.m_nClientId = m_oVendorListMemberData.m_oVendorData.m_nClientId;
	VendorDataProcessor.AllowAutomaticPublishing (oVendorData, m_oVendorListMemberData.m_bAllowPublishing, vendorList_checkedPublishing);
}

function vendorList_checkedPublishing (oResponse)
{
	if(oResponse.m_bSuccess)
	{
    	document.getElementById("vendorList_div_vendorDetailSubGrid"+m_oVendorListMemberData.m_nIndex).innerHTML = "";
		vendorList_selectedRowData (oResponse.m_arrVendorData[0], m_oVendorListMemberData.m_nIndex);
		vendorList_updateRow (oResponse.m_arrVendorData[0]);
	}
	HideDialog("ProcessDialog");
}

function vendorList_updateRow (oRowData)
{
	$('#vendorList_table_vendors').datagrid('updateRow',{
		index: m_oVendorListMemberData.m_nIndex,
		row: oRowData
	});
}