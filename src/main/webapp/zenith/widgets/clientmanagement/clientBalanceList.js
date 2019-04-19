var clientBalanceList_includeDataObjects = 
[
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
];

includeDataObjects (clientBalanceList_includeDataObjects, "clientBalanceList_loaded ()");

function clientBalanceList_memberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize = 15;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "asc";
    this.m_nEditIndexDG = undefined;
	this.m_bIsFilterSet = false;
}

var m_oClientBalanceListMemberData = new clientBalanceList_memberData ();

function clientBalanceList_loaded ()
{
	loadPage ("clientmanagement/clientBalanceList.html", "dialog", "clientBalanceList_init ()");
}

function clientBalanceList_init ()
{
	createPopup("dialog", "#clientBalanceList_button_cancel", "#clientBalanceList_button_save", true);
	clientBalanceList_initializeDetailsDG ();
}

function clientBalanceList_initializeDetailsDG ()
{
	$('#clientBalanceList_table_clientBalanceListDG').datagrid
	({
		columns:
		[[
			{field:'m_strCompanyName',title:'Client Name',sortable:true,width:150},
			{field:'m_strAddress',title:'Address',sortable:true,width:200},
			{field:'m_nOpeningBalance',title:'Opening Balance',sortable:true,width:100,align:'right',editor:{'type':'numberbox','options':{precision:'2'}},
				formatter:function(value,row,index)
	        	{
					var nOpeningBalance = Number(row.m_nOpeningBalance);
		  			return nOpeningBalance.toFixed(2);
	        	}
			},
			{field:'m_nCreditLimit',title:'Credit Limit',sortable:true,width:100,align:'right',editor:{'type':'numberbox','options':{precision:'2'}},
				formatter:function(value,row,index)
	        	{
					var nCreditLimit = Number(row.m_nCreditLimit);
		  			return nCreditLimit.toFixed(2);
	        	}
			},
		]],
		
		onSortColumn: function (strColumn, strOrder)
		{
			m_oClientBalanceListMemberData.m_strSortColumn = strColumn;
			m_oClientBalanceListMemberData.m_strSortOrder = strOrder;
			clientBalanceList_list (strColumn, strOrder, m_oClientBalanceListMemberData.m_nPageNumber, m_oClientBalanceListMemberData.m_nPageSize);
		}
	});
	clientBalanceList_list (m_oClientBalanceListMemberData.m_strSortColumn, m_oClientBalanceListMemberData.m_strSortOrder, 1, 15);
	clientBalanceList_initDGPagination ();
}

function clientBalanceList_initDGPagination ()
{
	$('#clientBalanceList_table_clientBalanceListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oClientBalanceListMemberData.m_nPageNumber = nPageNumber;
				clientBalanceList_list (m_oClientBalanceListMemberData.m_strSortColumn, m_oClientBalanceListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientBalanceListMemberData.m_nPageNumber = nPageNumber;
				m_oClientBalanceListMemberData.m_nPageSize = nPageSize;
				clientBalanceList_list (m_oClientBalanceListMemberData.m_strSortColumn, m_oClientBalanceListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function clientBalanceList_listFilter ()
{
	m_oClientBalanceListMemberData.m_bIsFilterSet = true;
	clientBalanceList_bIsEditEnd();
	var arrClientBalanceList = $('#clientBalanceList_table_clientBalanceListDG').datagrid('getChanges');
	if (arrClientBalanceList.length > 0)
		clientBalanceList_saveChangesOnUserConfirmation ();
	else
	clientBalanceList_list (m_oClientBalanceListMemberData.m_strSortColumn, m_oClientBalanceListMemberData.m_strSortOrder, 1, 15);
}

function clientBalanceList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oClientBalanceListMemberData.m_nPageNumber = nPageNumber;
	m_oClientBalanceListMemberData.m_nPageSize =nPageSize;
	m_oClientBalanceListMemberData.m_strSortColumn = strColumn;
	m_oClientBalanceListMemberData.m_strSortOrder = strOrder;
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "clientBalanceList_progressbarLoaded ()");
}

function clientBalanceList_getFormData ()
{
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = $("#clientBalanceList_input_clientname").val();
	oClientData.m_strAddress = $("#clientBalanceList_input_address").val();
	return oClientData;
}

function clientBalanceList_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oClientData = clientBalanceList_getFormData ();
	ClientDataProcessor.list(oClientData, m_oClientBalanceListMemberData.m_strSortColumn, m_oClientBalanceListMemberData.m_strSortOrder, m_oClientBalanceListMemberData.m_nPageNumber, m_oClientBalanceListMemberData.m_nPageSize, clientBalanceList_listed);
}

function clientBalanceList_listed (oResponse)
{
	$('#clientBalanceList_table_clientBalanceListDG').datagrid('loadData',oResponse.m_arrClientData);
	$('#clientBalanceList_table_clientBalanceListDG').datagrid('acceptChanges');
	$('#clientBalanceList_table_clientBalanceListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientBalanceListMemberData.m_nPageNumber});
	HideDialog("secondDialog");
}

function clientBalanceList_bIsEditEnd ()
{
	 if (m_oClientBalanceListMemberData.m_nEditIndexDG == undefined)
	    {
	    	return true
	    }
	    if ($('#clientBalanceList_table_clientBalanceListDG').datagrid('validateRow', m_oClientBalanceListMemberData.m_nEditIndexDG))
	    {
	        $('#clientBalanceList_table_clientBalanceListDG').datagrid('endEdit', m_oClientBalanceListMemberData.m_nEditIndexDG);
	        m_oClientBalanceListMemberData.m_nEditIndexDGeditIndex = -1;
	        return true;
	    } 
	    else 
	    {
	        return false;
	    }
}

function clientBalanceList_saveChangesOnUserConfirmation ()
{
	processConfirmation ('Yes', 'No', 'Do you want to save the changes ?', 
			clientBalanceList_saveChangesOnUserFilterConfirm);
}

function clientBalanceList_cancel ()
{
	clientBalanceList_bIsEditEnd();
	var arrClientBalanceList = $('#clientBalanceList_table_clientBalanceListDG').datagrid('getChanges');
	if (arrClientBalanceList.length > 0)
		clientBalanceList_saveChangesOnUserCancel ();
	else
		HideDialog ("dialog");
}

function clientBalanceList_saveChangesOnUserCancel ()
{
	processConfirmation ('Yes', 'No', 'Do you want to save the changes ?', 
			clientBalanceList_saveChangesOnUserCancelConfirm);
}

function clientBalanceList_saveChangesOnUserCancelConfirm (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if (bConfirm)
		clientBalanceList_updateClientBalanceList ();
	else
		HideDialog("dialog");
}


function clientBalanceList_saveChangesOnUserFilterConfirm (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if (bConfirm)
		clientBalanceList_updateClientBalanceList ();
	else
		clientBalanceList_list (m_oClientBalanceListMemberData.m_strSortColumn, m_oClientBalanceListMemberData.m_strSortOrder, 1, 15);
}

function clientBalanceList_editDG_cell (index, field)
{
	if (clientBalanceList_bIsEditEnd())
	{
        $('#clientBalanceList_table_clientBalanceListDG').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
        m_oClientBalanceListMemberData.m_nEditIndexDG = index;
        clientBalanceList_setEditing(m_oClientBalanceListMemberData.m_nEditIndexDG);
    }
}

function clientBalanceList_updateClientBalanceList ()
{
	$('#clientBalanceList_table_clientBalanceListDG').datagrid('endEdit', m_oClientBalanceListMemberData.m_nEditIndexDG);
	m_oClientBalanceListMemberData.m_nEditIndexDGeditIndex = -1;
	var oClientData = new ClientData ();
	oClientData.m_arrClientBalanceData = clientBalanceList_getClientBalanceDataArray ();
	if(oClientData.m_arrClientBalanceData.length > 0)
	{
		ClientDataProcessor.updateClientBalanceData(oClientData, clientBalanceList_updatedClientData);
	}
	else
	{
		informUser("Client Balance Data Updated Successfully", "kSuccess");
		clientBalanceList_closeDialog ();
	}
}

function clientBalanceList_getClientBalanceDataArray ()
{
	 var oClintDataArray = new Array ();
	 var arrClientBalanceData = $('#clientBalanceList_table_clientBalanceListDG').datagrid('getChanges');
	 for (var nIndex = 0; nIndex < arrClientBalanceData.length; nIndex++)
	 {
	 	var oClientData = new ClientData ();
	 	oClientData.m_nClientId = arrClientBalanceData[nIndex].m_nClientId;
	 	oClientData.m_nOpeningBalance = arrClientBalanceData[nIndex].m_nOpeningBalance;
	 	oClientData.m_nCreditLimit = arrClientBalanceData[nIndex].m_nCreditLimit;
	 	oClintDataArray.push (oClientData);
	 }
	return oClintDataArray;
}

function clientBalanceList_updatedClientData (oResponse, bHideDialog)
{
	if(oResponse.m_bSuccess)
	{
		informUser("client balance list updated successfully", "kSuccess");
		clientBalanceList_closeDialog ();
	}
	else
		informUser("client balance list updation failed", "kError");
}

function clientBalanceList_closeDialog ()
{
	if(!m_oClientBalanceListMemberData.m_bIsFilterSet)
		HideDialog ("dialog");
	else
		clientBalanceList_list (m_oClientBalanceListMemberData.m_strSortColumn, m_oClientBalanceListMemberData.m_strSortOrder, 1, 15);
}

function clientBalanceList_setEditing (nRowIndex)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	var arrEditors = $('#clientBalanceList_table_clientBalanceListDG').datagrid('getEditors', nRowIndex);
	var oOpeningBalanceEditor = arrEditors[0];
	var oCreditLimitEditor = arrEditors[1];
	$(oOpeningBalanceEditor.target).select();
	
	oOpeningBalanceEditor.target.bind('focus', function ()
    		{
				$(oOpeningBalanceEditor.target).select();
    		});
	
	oCreditLimitEditor.target.bind('focus', function ()
    		{
				$(oCreditLimitEditor.target).select();
    		});
	
	oCreditLimitEditor.target.bind('keydown', function (oEvent)
    		{
		    	if(oEvent.keyCode == 9)
		    	{
		    		oEvent.preventDefault();
		    		clientBalanceList_editDG_cell (nRowIndex +1);
		    	}
    		});
}


function clientBalanceList_save ()
{
	m_oClientBalanceListMemberData.m_bIsFilterSet = false;
	clientBalanceList_updateClientBalanceList ();
}
