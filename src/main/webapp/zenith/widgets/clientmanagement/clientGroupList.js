var clientGroupList_includeDataObjects = 
[
	'widgets/clientmanagement/ClientGroupData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/inventorymanagement/item/ItemGroupData.js',
	'widgets/inventorymanagement/sales/DiscountStructureData.js'
];

includeDataObjects (clientGroupList_includeDataObjects, "clientGroupList_loaded()");

function clientGroupList_memberData ()
{
	this.m_oClientGroupData = null;
	this.nIndex = -1;
	this.m_nSelectedClientGroupId = -1;
	this.m_strActionItemsFunction = "clientGroupList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
}

var m_oClientGroupListMemberData = new clientGroupList_memberData ();

function clientGroupList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function clientGroupList_init ()
{
	clientGroupList_createDataGrid ();
	clientGroupList_list (m_oClientGroupListMemberData.m_strSortColumn,m_oClientGroupListMemberData.m_strSortOrder,1, 10);
}

function clientGroupList_initAdmin ()
{
	clientGroupList_init ();
	m_oClientGroupListMemberData.m_strActionItemsFunction = "clientGroupList_addActions (row, index)";
	document.getElementById ("clientGroupList_button_add").style.visibility="visible";
}

function clientGroupList_initUser ()
{
	clientGroupList_init ();
}

function clientGroupList_addActions (row,index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="clientGroupList_edit('+row.m_nGroupId+')" /> </td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="clientGroupList_delete('+ index  +')"/> </td>'+
							'<td> <img title="Publicise" src="images/globe.png" width="20" align="center" id="publiciseImageId" onClick="clientGroupList_publicise ('+row.m_nGroupId+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function clientGroupList_createDataGrid ()
{
	initHorizontalSplitter("#clientGroupList_div_horizontalSplitter", "#clientGroupList_table_clientGroupListDG");
	$('#clientGroupList_table_clientGroupListDG').datagrid({
		fit:true,
	    columns:[[  
	              
	        {field:'m_strGroupName',title:'Group Name',sortable:true,width:150},
	        {field:'m_strUserName',title:'Created By',sortable:true,width:200,
				formatter:function(value,row,index)
	        	{
	        		return row.m_oCreatedBy.m_strUserName;
	        	}
		  	},
		  	{field:'m_strDate',title:'Date',sortable:true,width:150},
	        {field:'Action',title:'Action',sortable:false,width:80,
	        	formatter:function(value,row,index)
	        	{
	        		return clientGroupList_displayImages (row, index);
	        	}
	         },
	    ]]
	});
	$('#clientGroupList_table_clientGroupListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				clientGroupList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				strColumn = (strColumn == "m_strDate") ? "m_dCreatedOn" : strColumn;
				m_oClientGroupListMemberData.m_strSortColumn = strColumn;
				m_oClientGroupListMemberData.m_strSortOrder = strOrder;
				clientGroupList_list (strColumn, strOrder, m_oClientGroupListMemberData.m_nPageNumber, m_oClientGroupListMemberData.m_nPageSize);
			}
		}
	)
	clientGroupList_initDGPagination ();
	clientGroupList_list (m_oClientGroupListMemberData.m_strSortColumn,m_oClientGroupListMemberData.m_strSortOrder,1, 10);
}

function clientGroupList_initDGPagination ()
{
	$('#clientGroupList_table_clientGroupListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oClientGroupListMemberData.m_nPageNumber = $('#clientGroupList_table_clientGroupListDG').datagrid('getPager').pagination('options').pageNumber;
				clientGroupList_list (m_oClientGroupListMemberData.m_strSortColumn, m_oClientGroupListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("clientGroupList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oClientGroupListMemberData.m_nPageNumber = $('#clientGroupList_table_clientGroupListDG').datagrid('getPager').pagination('options').pageNumber;
				m_oClientGroupListMemberData.m_nPageSize = $('#clientGroupList_table_clientGroupListDG').datagrid('getPager').pagination('options').pageSize;
				clientGroupList_list (m_oClientGroupListMemberData.m_strSortColumn, m_oClientGroupListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("clientGroupList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function clientGroupList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn is expected to be of type string.");
	assert.isString(strOrder, "strOrder is expected to be of type string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oClientGroupListMemberData.m_nPageNumber = nPageNumber;
	m_oClientGroupListMemberData.m_nPageSize =nPageSize;
	m_oClientGroupListMemberData.m_strSortColumn = strColumn;
	m_oClientGroupListMemberData.m_strSortOrder = strOrder;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "clientGroupList_progressbarLoaded ()");
}

function  clientGroupList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oClientGroupData = new ClientGroupData ();
	oClientGroupData.m_oUserCredentialsData = new UserInformationData ();
	oClientGroupData.m_strGroupName = $("#filtergroup_input_groupName").val();
	oClientGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ClientGroupDataProcessor.list(oClientGroupData, m_oClientGroupListMemberData.m_strSortColumn, m_oClientGroupListMemberData.m_strSortOrder, m_oClientGroupListMemberData.m_nPageNumber, m_oClientGroupListMemberData.m_nPageSize, clientGroupList_listed);
}

function clientGroupList_listed (oResponse)
{
	// server response
	
	clearGridData ("#clientGroupList_table_clientGroupListDG");
	$("#clientGroupList_table_clientGroupListDG").datagrid('loadData', oResponse.m_arrGroupData);
	$('#clientGroupList_table_clientGroupListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oClientGroupListMemberData.m_nPageNumber});
	HideDialog("dialog");
}


function clientGroupList_filter ()
{
	clientGroupList_list (m_oClientGroupListMemberData.m_strSortColumn,m_oClientGroupListMemberData.m_strSortOrder,1, 10);
}

function clientGroupList_showAddPopup ()
{
	navigate ("newClientGroup", "widgets/clientmanagement/newClientGroup.js");
}

function clientGroupList_edit (nGroupId)
{
	assert.isNumber(nGroupId, "nGroupId expected to be a Number.");
	m_oClientGroupListMemberData.m_nSelectedClientGroupId = nGroupId;
	loadPage ("include/process.html", "ProcessDialog", "clientGroupList_edit_progressbarLoaded ()");
}

function clientGroupList_edit_progressbarLoaded ()
{
	navigate ("editClientGroup", "widgets/clientmanagement/editClientGroup.js");
}
function clientGroupList_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oClientGroupData = new ClientGroupData ();
	var oListData = $("#clientGroupList_table_clientGroupListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oClientGroupData.m_nGroupId = oData.m_nGroupId;
	oClientGroupData.m_oUserCredentialsData = new UserInformationData ();
	oClientGroupData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var bConfirm = getUserConfirmation ("usermessage_listclient_doyoureallywanttodelete")
	if (bConfirm == true)
		ClientGroupDataProcessor.deleteData(oClientGroupData, clientGroupList_deleted);
}

function clientGroupList_deleted (oResponse)
{
	// server response
	
	if (oResponse.m_bSuccess)
	{
		informUser ("usermessage_listclientgroup_deletedsuccessfully", "kSuccess");
		document.getElementById("clientGroupList_div_listDetail").innerHTML = "";
		clientGroupList_list (m_oClientGroupListMemberData.m_strSortColumn,m_oClientGroupListMemberData.m_strSortOrder,1, 10);
	}
	else
		informUser(oResponse.m_strError_Desc, "kError");
}

function clientGroupList_selectedRowData (oRowData, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isObject(oRowData, 'oRowData is expected to be an object');
	m_oClientGroupListMemberData.m_oClientGroupData = oRowData;
	m_oClientGroupListMemberData.m_nIndex = nIndex;
	document.getElementById("clientGroupList_div_listDetail").innerHTML = "";
	var oClientGroupData = new ClientGroupData ();
	oClientGroupData.m_nGroupId = oRowData.m_nGroupId;
	oClientGroupData.m_oUserCredentialsData = new UserInformationData ();
	oClientGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ClientGroupDataProcessor.getXML (oClientGroupData, function(strXMLData)
			{
				populateXMLData (strXMLData, "clientmanagement/clientGroupDetails.xslt", 'clientGroupList_div_listDetail');
				clientGroupList_initializeClientsDetailsDG ();
				clientGroupList_initializeItemsDetailsDG ();
				ClientGroupDataProcessor.get (oClientGroupData, clientGroupList_gotClientListData);
				clientGroupList_getItemsGroupList (oRowData.m_nGroupId);
			});
}

function clientGroupList_getItemsGroupList (nGroupId)
{
	assert.isNumber(nGroupId, "nGroupId expected to be a Number.");
	var oDiscountStructureData = new DiscountStructureData ();
	oDiscountStructureData.m_oClientGroupData.m_nGroupId = nGroupId;
	oDiscountStructureData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oDiscountStructureData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	DiscountStructureDataProcessor.list (oDiscountStructureData, "", "", "", "", clientGroupList_gotItemsGroupList);
}

function clientGroupList_gotItemsGroupList (oResponse)
{
	//server response
	
	$('#clientGroupDetails_table_itemsGroupDetailsDG').datagrid('loadData',oResponse.m_arrDiscountStructureData);
}

function clientGroupList_gotClientListData (oResponse)
{
	// server response
	
	
	try
	{
		var arrClientGroup = oResponse.m_arrGroupData;
		var arrClients = arrClientGroup[0].m_oClientSet;
		for (var nIndex = 0; nIndex < arrClients.length; nIndex++)
		{
			$('#clientGroupDetails_table_clientGroupDetailsDG').datagrid('appendRow',arrClients[nIndex]);
		}
	}
	catch(oException)
	{
		
	}
}

function clientGroupList_initializeClientsDetailsDG ()
{
	$('#clientGroupDetails_table_clientGroupDetailsDG').datagrid ({
	    columns:[[  
	        {field:'m_strCompanyName',title:'Client Name',sortable:true,width:150},
	        {field:'m_strCity',title:'City',sortable:true,width:240},
			{field:'m_strTelephone',title:'Telephone',sortable:true,width:150},
			{field:'m_strEmail',title:'Email',sortable:true,width:190}
	    ]]
	});
}

function clientGroupList_initializeItemsDetailsDG ()
{
	$('#clientGroupDetails_table_itemsGroupDetailsDG').datagrid ({
	    columns:[[  
	              	{field:'m_strGroupName',title:'Group Name',sortable:true,width:250,
	              		formatter:function(value,row,index)
			        	{
			        		return row.m_oItemGroupData.m_strGroupName;
			        	}
	              	},
					{field:'m_nDiscount',title:'Discount',sortable:true,width:70,
						formatter:function(value,row,index)
			        	{
			        		return row.m_nDiscount.toFixed(2);
			        	}
	              	}
	    ]]
	});
}

function clientGroupList_displayImages (row, index)    //dont no what parameters are doing
{
	var oImage = eval (m_oClientGroupListMemberData.m_strActionItemsFunction);
	return oImage;
}

function clientGroupList_publicise (nClientGroupId)
{
	assert.isNumber(nClientGroupId, "nClientGroupId expected to be a Number.");
	m_oClientGroupListMemberData.m_nSelectedClientGroupId = nClientGroupId;
	navigate ("publicise", "widgets/clientmanagement/clientGroupPublicise.js");
}
