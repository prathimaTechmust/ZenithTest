var vendorBalanceList_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js'
];



 includeDataObjects (vendorBalanceList_includeDataObjects, "vendorBalanceList_loaded()");

function vendorBalanceList_loaded ()
{
	loadPage ("inventorymanagement/purchase/vendorBalanceList.html", "dialog", "vendorBalanceList_init ()");
}

function vendorBalanceList_memberData ()
{
	this.m_nEditIndexDG = undefined;
	this.m_bIsFilterSet = false;
	this.m_nPageNumber = 1;
    this.m_nPageSize =15;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "desc";
}

var m_oVendorBalanceListMemberData = new vendorBalanceList_memberData ();

function vendorBalanceList_init ()
{
	createPopup("dialog", "#vendorBalanceList_button_cancel", "#vendorBalanceList_button_save", true);
	//vendorBalanceList_list (m_oVendorBalanceListMemberData.m_strSortColumn, m_oVendorBalanceListMemberData.m_strSortOrder, 1, 15);
	vendorBalanceList_initDataGrid ();
}

function vendorBalanceList_initDataGrid ()
{
	$('#vendorBalanceList_table_vendorBalanceListDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:100,
					styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
			  	},
				{field:'m_strAddress',title:'Address',sortable:true,width:300},
				{field:'m_nOpeningBalance',title:'Opening Balance',sortable:true,width:100,align:'right',editor:{'type':'numberbox','options':{precision:'2'}},
					formatter:function(value,row,index)
		        	{
						var nOpeningBalance = Number(row.m_nOpeningBalance);
						var nIndianFormat = formatNumber (nOpeningBalance.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	}
				}
			]],
			onSortColumn: function (strColumn, strOrder)
			{
				m_oVendorBalanceListMemberData.m_strSortColumn = strColumn;
				m_oVendorBalanceListMemberData.m_strSortOrder = strOrder;
				vendorBalanceList_list (m_oVendorBalanceListMemberData.m_strSortColumn, m_oVendorBalanceListMemberData.m_strSortOrder, m_oVendorBalanceListMemberData.m_nPageNumber, m_oVendorBalanceListMemberData.m_nPageSize);
			}
		}
	);
	
	$('#vendorBalanceList_table_vendorBalanceListDG').datagrid
	(
		{
			onClickCell: function (nRowIndex, oRowData)
			{
				vendorBalanceList_editRowDG (nRowIndex);
			}
		}
	)
	
	vendorBalanceList_list ('m_strCompanyName','desc', 1, 15);
	vendorBalanceList_initDGPagination ();
}

function vendorBalanceList_initDGPagination ()
{
	$('#vendorBalanceList_table_vendorBalanceListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorBalanceListMemberData.m_nPageNumber = nPageNumber;
				vendorBalanceList_list (m_oVendorBalanceListMemberData.m_strSortColumn, m_oVendorBalanceListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorBalanceListMemberData.m_nPageNumber = nPageNumber;
				m_oVendorBalanceListMemberData.m_nPageSize = nPageSize;
				vendorBalanceList_list (m_oVendorBalanceListMemberData.m_strSortColumn, m_oVendorBalanceListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function vendorBalanceList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
 	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oVendorBalanceListMemberData.m_strSortColumn = strColumn;
	m_oVendorBalanceListMemberData.m_strSortOrder = strOrder;
	m_oVendorBalanceListMemberData.m_nPageNumber = nPageNumber;
	m_oVendorBalanceListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "vendorBalanceList_progressbarLoaded ()");
}

function vendorBalanceList_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oVendorData = vendorBalanceList_getFormData ();
	VendorDataProcessor.listVendor(oVendorData, m_oVendorBalanceListMemberData.m_strSortColumn, m_oVendorBalanceListMemberData.m_strSortOrder, m_oVendorBalanceListMemberData.m_nPageNumber, m_oVendorBalanceListMemberData.m_nPageSize, vendorBalanceList_listed);
}

function vendorBalanceList_listed (oResponse)
{
	clearGridData ("#vendorBalanceList_table_vendorBalanceListDG");
	$('#vendorBalanceList_table_vendorBalanceListDG').datagrid('loadData',oResponse.m_arrVendorData);
	$('#vendorBalanceList_table_vendorBalanceListDG').datagrid('acceptChanges');
	$('#vendorBalanceList_table_vendorBalanceListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oVendorBalanceListMemberData.m_nPageNumber});
	HideDialog("secondDialog");
}

function vendorBalanceList_listFilter ()
{
	m_oVendorBalanceListMemberData.m_bIsFilterSet = true;
	vendorBalanceList_bIsEditEnd();
	var arrVendorData = $('#vendorBalanceList_table_vendorBalanceListDG').datagrid('getChanges');
	if (arrVendorData.length > 0)
		vendorBalanceList_saveChangesOnUserConfirmation ();
	else
		vendorBalanceList_list (m_oVendorBalanceListMemberData.m_strSortColumn, m_oVendorBalanceListMemberData.m_strSortOrder , 1, 15);
}

function vendorBalanceList_saveChangesOnUserConfirmation ()
{
	processConfirmation ('Yes', 'No', 'Do you want to save the changes ?', 
			vendorBalanceList_saveChangesOnUserFilterConfirm);
}

function vendorBalanceList_saveChangesOnUserFilterConfirm (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if (bConfirm)
		vendorBalanceList_updateVendorEntries ();
	else
		vendorBalanceList_list (m_oVendorBalanceListMemberData.m_strSortColumn, m_oVendorBalanceListMemberData.m_strSortOrder, 1, 15);
}

function vendorBalanceList_getFormData ()
{
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = $("#vendorBalanceList_input_vendorName").val();
	oVendorData.m_strAddress = $("#vendorBalanceList_input_address").val();
	return oVendorData;
}

function vendorBalanceList_cancel ()
{
	vendorBalanceList_bIsEditEnd();
	var arrItems = $('#vendorBalanceList_table_vendorBalanceListDG').datagrid('getChanges');
	if (arrItems.length > 0)
		vendorBalanceList_saveChangesOnUserCancel ();
	else
		HideDialog ("dialog");
}

function vendorBalanceList_saveChangesOnUserCancel ()
{
	processConfirmation ('Yes', 'No', 'Do you want to save the changes ?', 
			vendorBalanceList_saveChangesOnUserCancelConfirm);
}

function vendorBalanceList_saveChangesOnUserCancelConfirm (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if (bConfirm)
		vendorBalanceList_updateVendorEntries ();
	else
		HideDialog("dialog");
}

function vendorBalanceList_editRowDG (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	if (vendorBalanceList_bIsEditEnd())
	{
        $('#vendorBalanceList_table_vendorBalanceListDG').datagrid('selectRow', nIndex).datagrid('beginEdit', nIndex);
        m_oVendorBalanceListMemberData.m_nEditIndexDG = nIndex;
        vendorBalanceList_setEditing(m_oVendorBalanceListMemberData.m_nEditIndexDG);
    }
}

function vendorBalanceList_bIsEditEnd()
{
    if (m_oVendorBalanceListMemberData.m_nEditIndexDG == undefined)
    {
    	return true
    }
    if ($('#vendorBalanceList_table_vendorBalanceListDG').datagrid('validateRow', m_oVendorBalanceListMemberData.m_nEditIndexDG))
    {
        $('#vendorBalanceList_table_vendorBalanceListDG').datagrid('endEdit', m_oVendorBalanceListMemberData.m_nEditIndexDG);
        m_oVendorBalanceListMemberData.m_nEditIndexDGeditIndex = -1;
        return true;
    } 
    else 
    {
        return false;
    }
}

function vendorBalanceList_updateVendorEntries ()
{
	$('#vendorBalanceList_table_vendorBalanceListDG').datagrid('endEdit', m_oVendorBalanceListMemberData.m_nEditIndexDG);
	m_oVendorBalanceListMemberData.m_nEditIndexDGeditIndex = -1;
	var oVendorData = new VendorData ();
	oVendorData.m_arrClientBalanceData = vendorBalanceList_getVendorBalanceEntries ();
	if(oVendorData.m_arrClientBalanceData.length > 0)
	    VendorDataProcessor.updateVendorBalanceData(oVendorData , vendorBalanceList_updatedEntries);
	else
	{
		informUser("Vendor Opening Balance Updated Successfully", "kSuccess");
		vendorBalanceList_closeDialog ();
	}
}

function vendorBalanceList_getVendorBalanceEntries ()
{
	 var arrVendorEntries = new Array ();
	 var arrVendorData = $('#vendorBalanceList_table_vendorBalanceListDG').datagrid('getChanges');
	 for (var nIndex = 0; nIndex < arrVendorData.length; nIndex++)
	 {
	 	var oVendorData = new VendorData ();
	 	oVendorData.m_nClientId = arrVendorData[nIndex].m_nClientId;
	 	oVendorData.m_nOpeningBalance = arrVendorData[nIndex].m_nOpeningBalance;
	 	arrVendorEntries.push (oVendorData);
	 }
	return arrVendorEntries;
}

function vendorBalanceList_updatedEntries (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Vendor Opening Balance Updated Successfully", "kSuccess");
		vendorBalanceList_closeDialog ();
	}
	else
		informUser("Vendor Opening Balance Updation Failed", "kError");
}

function vendorBalanceList_closeDialog ()
{
	if(!m_oVendorBalanceListMemberData.m_bIsFilterSet)
		HideDialog ("dialog");
	else
		vendorBalanceList_list (m_oVendorBalanceListMemberData.m_strSortColumn, m_oVendorBalanceListMemberData.m_strSortOrder, 1, 15);
}

function vendorBalanceList_save ()
{
	m_oVendorBalanceListMemberData.m_bIsFilterSet = false;
	vendorBalanceList_updateVendorEntries ();
}

function vendorBalanceList_setEditing(nRowIndex)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	var arrEditors = $('#vendorBalanceList_table_vendorBalanceListDG').datagrid('getEditors', nRowIndex);
	var oOpeningBalanceEditor = arrEditors[0];
	$(oOpeningBalanceEditor.target).select();
	
	oOpeningBalanceEditor.target.bind('focus', function ()
    		{
				$(oOpeningBalanceEditor.target).select();
    		});
	
	oOpeningBalanceEditor.target.bind('keydown', function (oEvent)
    		{
		    	if(oEvent.keyCode == 9)
		    	{
		    		oEvent.preventDefault();
		    		vendorBalanceList_editRowDG (nRowIndex +1);
		    	}
    		});
}
