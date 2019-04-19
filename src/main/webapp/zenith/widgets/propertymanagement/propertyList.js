var propertyList_includeDataObjects = 
[
	'widgets/propertymanagement/PropertyData.js',
	'widgets/propertymanagement/propertytype/PropertyTypeData.js',
	'widgets/propertymanagement/PropertyDetailData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];



 includeDataObjects (propertyList_includeDataObjects, "propertyList_loaded()");

function propertyList_memberData ()
{
	this.m_nSelectedPropertyId = -1;
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "propertyList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "desc";
}

var m_oPropertyListMemberData = new propertyList_memberData ();

function propertyList_init ()
{
	propertyList_createDataGrid ();
}

function propertyList_initEdit ()
{
	m_oPropertyListMemberData.m_strActionItemsFunction = "propertyList_addActions (row, index)";
	document.getElementById ("propertyList_button_add").style.visibility="visible";
	propertyList_init ();
}

function propertyList_addActions (row,index)
{
	var oActions = '<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="propertyList_edit ('+row.m_nPropertyId+')"/> </td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="propertyList_delete ('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oActions;
}

function propertyList_createDataGrid ()
{
	initHorizontalSplitter("#propertyList_div_horizontalSplitter", "#propertyList_table_propertyListDG");
	$('#propertyList_table_propertyListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strCompanyName',title:'Client Name',sortable:true,width:125,
				  		formatter:function(value,row,index)
			        	{
			        		return row.m_oClientData.m_strCompanyName;
			        	}
				  	},
					{field:'m_strAddress',title:'Address',sortable:true,width:125},
					{field:'m_strLocality',title:'Locality',width:100},
					{field:'Actions',title:'Action',width:50,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return propertyList_displayImages (row, index);
			        	}
					},
				]]
		}
	);
	$('#propertyList_table_propertyListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				propertyList_selectedRowData (rowData, rowIndex);
			},
			onSortColumn: function (strColumn, strOrder)
			{
				m_oPropertyListMemberData.m_strSortColumn = strColumn;
				m_oPropertyListMemberData.m_strSortOrder = strOrder;
				propertyList_list (strColumn, strOrder, m_oPropertyListMemberData.m_nPageNumber, m_oPropertyListMemberData.m_nPageSize);
			}
		}
	)
	propertyList_initDGPagination ();
	propertyList_list (m_oPropertyListMemberData.m_strSortColumn,m_oPropertyListMemberData.m_strSortOrder,1, 10);
}

function propertyList_initDGPagination ()
{
	$('#propertyList_table_propertyListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oPropertyListMemberData.m_nPageNumber = nPageNumber;
				propertyList_list (m_oPropertyListMemberData.m_strSortColumn, m_oPropertyListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("propertyList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oPropertyListMemberData.m_nPageNumber = nPageNumber;
				m_oPropertyListMemberData.m_nPageSize = nPageSize;
				propertyList_list (m_oPropertyListMemberData.m_strSortColumn, m_oPropertyListMemberData.m_strSortOrder,nPageNumber, nPageSize);
				document.getElementById("propertyList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function propertyList_displayImages (row, index)
{
	var oImage = eval (m_oPropertyListMemberData.m_strActionItemsFunction);
	return oImage;
}

function propertyList_filter ()
{
	propertyList_list (m_oPropertyListMemberData.m_strSortColumn,m_oPropertyListMemberData.m_strSortOrder,1, 10);
}

function propertyList_list (strSortColumn, strSortOrder, nPageNumber, nPageSize)
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "progressbarLoaded ()");
	var oPropertyData = new PropertyData ();
	oPropertyData.m_oClientData.m_strCompanyName =  $("#filterProperty_input_clientName").val();
	oPropertyData.m_oPropertyType.m_strPropertyType = $("#filterProperty_input_propertyType").val();
	oPropertyData.m_oUserCredentialsData = new UserInformationData ();
	oPropertyData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPropertyData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PropertyDataProcessor.list(oPropertyData, strSortColumn, strSortOrder, nPageNumber, nPageSize, propertyList_listed);
}

function propertyList_listed (oResponse)
{
	$('#propertyList_table_propertyListDG').datagrid('loadData', oResponse.m_arrProperty);
	$('#propertyList_table_propertyListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oPropertyListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function propertyList_selectedRowData (rowData, rowIndex)
{
	m_oPropertyListMemberData.m_nIndex = nIndex;
	document.getElementById("propertyList_div_listDetail").innerHTML = "";
	var oPropertyData = new PropertyData ();
	oPropertyData.m_nPropertyId = oRowData.m_nPropertyId;
	oPropertyData.m_oUserCredentialsData = new UserInformationData ();
	oPropertyData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPropertyData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PropertyDataProcessor.getXML (oPropertyData,propertyList_gotXML);
}
function propertyList_gotXML(strXMLData)
{
	populateXMLData (strXMLData, "inventorymanagement/item/itemDetails.xslt", 'itemList_div_listDetail');
	propertyList_initializeDetailsDG ();
	PropertyDataProcessor.get(oPropertyData, propertyList_gotPropertyDetail )
	
}
function propertyList_initializeDetailsDG ()
{
	$('#propertyDetails_table_propertyDetailsDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strDescription',title:'Description',sortable:true,width:120},
				{field:'m_nBHK',title:'BHK',sortable:true,width:200},
				{field:'m_strSize',title:'Size',sortable:true,width:100}
			]]
		}
	);
}

function propertyList_gotPropertyDetail (oResponse)
{
	clearGridData ("#propertyDetails_table_propertyDetailsDG");
	var arrPropertyData = oResponse.m_arrProperty;
}


function propertyList_edit (nPropertyId)
{
	m_oPropertyListMemberData.m_nSelectedPropertyId = nPropertyId;
	//navigate ("editProperty", "widgets/propertymanagement/editProperty.js");
}

function propertyList_delete (nIndex)
{
	var oPropertyData = new PropertyData ();
	var oListData = $("#propertyList_table_propertyListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oPropertyData.m_nPropertyId = oData.m_nPropertyId;
	oPropertyData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPropertyData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	var bConfirm = getUserConfirmation("Do you really want to delete the property")
	if(bConfirm)
		PropertyDataProcessor.deleteData(oPropertyData, propertyList_deleted);
}

function propertyList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("Property deleted successfully", "kSuccess");
		document.getElementById("propertyList_div_listDetail").innerHTML = "";
		propertyList_list (m_oPropertyListMemberData.m_strSortColumn, m_oPropertyListMemberData.m_strSortOrder, 1, 10);
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}
