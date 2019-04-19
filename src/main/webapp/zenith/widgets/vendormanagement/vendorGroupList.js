var vendorGroupList_includeDataObjects = 
[
	'widgets/vendormanagement/VendorGroupData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

 includeDataObjects (vendorGroupList_includeDataObjects, "vendorGroupList_loaded()");

function vendorGroupList_memberData ()
{
	this.m_nSelectedItemId = -1;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
    this.m_strActionItemsFunction = "vendorGroupList_addHyphen()";
    this.m_oVendorGroupData = null;
    this.m_nEditIndex = undefined;
}

var m_oVendorGroupListMemberData = new vendorGroupList_memberData ();

function vendorGroupList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function vendorGroupList_initAdmin ()
{
	vendorGroupList_init ();
	m_oVendorGroupListMemberData.m_strActionItemsFunction = "vendorGroupList_addActions (row, index)";
	document.getElementById ("vendorGroupList_input_addNewGroup").style.visibility="visible";
}

function vendorGroupList_initUser ()
{
	vendorGroupList_init ();
}

function vendorGroupList_init ()
{
	vendorGroupList_createDataGrid ();
}

function vendorGroupList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
			'<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="vendorGroupList_edit ('+row.m_nGroupId+')"/> </td>'+
						'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="vendorGroupList_delete ('+index+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oActions;
}

function vendorGroupList_createDataGrid ()
{
	initHorizontalSplitter("#vendorGroupList_div_horizontalSplitter", "#vendorGroupList_table_vendorGroupListDG");
	$('#vendorGroupList_table_vendorGroupListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
					{field:'m_strGroupName',title:'Group Name',sortable:true,width:300,
				  		styler: function(value,row,index)
				  		{
				  			return {class:'DGcolumn'};
				  		}
					},
					{field:'m_strUserName',title:'Created By',sortable:true,width:150,
						formatter:function(value,row,index)
			        	{
			        		return row.m_oCreatedBy.m_strUserName;
			        	}
					},
					{field:'m_strDate',title:'Date',sortable:true,width:70},
					{field:'Actions',title:'Action',width:50,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return vendorGroupList_displayImages (row, index);
			        	}
					}
				]]
		}
	);
	$('#vendorGroupList_table_vendorGroupListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				vendorGroupList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oVendorGroupListMemberData.m_strSortColumn = strColumn;
				m_oVendorGroupListMemberData.m_strSortOrder = strOrder;
				vendorGroupList_list (strColumn, strOrder, m_oVendorGroupListMemberData.m_nPageNumber, m_oVendorGroupListMemberData.m_nPageSize);
			}
		}
	)
	vendorGroupList_initDGPagination ();
	vendorGroupList_list (m_oVendorGroupListMemberData.m_strSortColumn,m_oVendorGroupListMemberData.m_strSortOrder,1, 10);
}

function vendorGroupList_initDGPagination ()
{
	$('#vendorGroupList_table_vendorGroupListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorGroupListMemberData.m_nPageNumber = $('#vendorGroupList_table_vendorGroupListDG').datagrid('getPager').pagination('options').pageNumber;
				vendorGroupList_list (m_oVendorGroupListMemberData.m_strSortColumn, m_oVendorGroupListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("vendorGroupList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorGroupListMemberData.m_nPageNumber = $('#vendorGroupList_table_vendorGroupListDG').datagrid('getPager').pagination('options').pageNumber;
				m_oVendorGroupListMemberData.m_nPageSize = $('#vendorGroupList_table_vendorGroupListDG').datagrid('getPager').pagination('options').pageSize;
				vendorGroupList_list (m_oVendorGroupListMemberData.m_strSortColumn, m_oVendorGroupListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("vendorGroupList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function vendorGroupList_displayImages (row, index)
{
	var oImage = eval (m_oVendorGroupListMemberData.m_strActionItemsFunction);
	return oImage;
}

function vendorGroupList_edit (nGroupId)
{
	assert.isNumber(nGroupId, "nGroupId expected to be a Number.");
	m_oVendorGroupListMemberData.m_nSelectedItemId = nGroupId;
	loadPage ("include/process.html", "ProcessDialog", "vendorGroupList_edit_progressbarLoaded ()");
}

function vendorGroupList_edit_progressbarLoaded ()
{
	navigate ("VendorGroup", "widgets/vendormanagement/vendorGroupEdit.js");
}
function vendorGroupList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oVendorGroupListMemberData.m_nPageNumber = nPageNumber;
	m_oVendorGroupListMemberData.m_nPageSize =nPageSize;
	m_oVendorGroupListMemberData.m_strSortColumn = strColumn;
	m_oVendorGroupListMemberData.m_strSortOrder = strOrder;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "vendorGroupList_progressbarLoaded ()");
}

function vendorGroupList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oVendorGroupData = new VendorGroupData ();
	oVendorGroupData.m_oUserCredentialsData = new UserInformationData ();
	document.getElementById("vendorGroupList_div_listDetail").innerHTML = "";
	oVendorGroupData.m_strGroupName = $("#vendorGroupList_input_name").val();
	oVendorGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	VendorGroupDataProcessor.list(oVendorGroupData, m_oVendorGroupListMemberData.m_strSortColumn, m_oVendorGroupListMemberData.m_strSortOrder, m_oVendorGroupListMemberData.m_nPageNumber, m_oVendorGroupListMemberData.m_nPageSize, vendorGroupList_listed);
}

function vendorGroupList_listed (oResponse)
{
	clearGridData("#vendorGroupList_table_vendorGroupListDG")
	$('#vendorGroupList_table_vendorGroupListDG').datagrid('loadData', oResponse.m_arrGroupData);
	$('#vendorGroupList_table_vendorGroupListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oVendorGroupListMemberData.m_nPageNumber});
	HideDialog("dialog");
}

function vendorGroupList_filter ()
{
	vendorGroupList_list (m_oVendorGroupListMemberData.m_strSortColumn,m_oVendorGroupListMemberData.m_strSortOrder,1, 10);
}

function vendorGroupList_selectedRowData (rowData, rowIndex)
{
	assert.isObject(rowData, "rowData expected to be an Object.");
	assert( Object.keys(rowData).length >0 , "rowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	m_oVendorGroupListMemberData.m_oVendorGroupData = oVendorGroupData;
	m_oVendorGroupListMemberData.m_nIndex = rowIndex;
	document.getElementById("vendorGroupList_div_listDetail").innerHTML = "";
	var oVendorGroupData = new VendorGroupData ();
	oVendorGroupData.m_oUserCredentialsData = new UserInformationData ();
	oVendorGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oVendorGroupData.m_nGroupId = rowData.m_nGroupId;
	VendorGroupDataProcessor.getXML (oVendorGroupData, function (strXMLData)
		{
			populateXMLData (strXMLData, "vendormanagement/vendorGroupDetails.xslt", 'vendorGroupList_div_listDetail');
			vendorGroupList_initializeDetailsDG ();
			VendorGroupDataProcessor.get (oVendorGroupData, vendorGroupList_gotVendorGroupData)
		});
}

function vendorGroupList_gotVendorGroupData (oResponse)
{
	try
	{ 
		var arrVendorGroupData = arrGroupData [0].m_oVendors;
		$('#vendorGroupDetails_table_vendorGroupDetailsDG').datagrid('loadData',oResponse.m_arrGroupData);
	}
	catch(oException)
	{
		
	}
}

function vendorGroupList_addNewPopUp ()
{
	navigate ("newVendorGroup", "widgets/vendormanagement/vendorGroupNew.js");
}

function vendorGroupList_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oVendorGroupData = new VendorGroupData ();
	var oListData = $("#vendorGroupList_table_vendorGroupListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oVendorGroupData.m_oUserCredentialsData = new UserInformationData ();
	oVendorGroupData.m_nGroupId = oData.m_nGroupId;
	oVendorGroupData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var bConfirm = getUserConfirmation("Do you really want to delete the vendor group")
	if(bConfirm)
		VendorGroupDataProcessor.deleteData(oVendorGroupData, vendorGroupList_deleted);
}

function vendorGroupList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("Vendor group deleted successfully.", "kSuccess");
		document.getElementById("vendorGroupList_div_listDetail").innerHTML = "";
		vendorGroupList_list (m_oVendorGroupListMemberData.m_strSortColumn,m_oVendorGroupListMemberData.m_strSortOrder,1, 10);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");	
}

function vendorGroupList_initializeDetailsDG ()
{
	$('#vendorGroupDetails_table_vendorGroupDetailsDG').datagrid 
	({
	    columns:[[  
	              	{field:'m_strVendorCompanyName',title:'Vendor Name', width:100},
	              	{field:'m_strVendorCity',title:'city', width:100},
	              	{field:'m_strVendorTelephone',title:'Telephone Number', width:100},
	              	{field:'m_strVendorEmail',title:'Email', width:100},
	        	]]
	});
}
