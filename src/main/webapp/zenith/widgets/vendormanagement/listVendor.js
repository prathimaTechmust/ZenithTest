var listVendor_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/vendormanagement/VendorContactData.js'
];

 includeDataObjects (listVendor_includeDataObjects, "listVendor_loaded()");

function listVendor_memberData ()
{
	this.m_oSelectedVendorId = -1;
	this.m_oSelectedContactId = -1
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "listVendor_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "asc";
    this.m_oVendorData = "";
}

var m_oVendorListMemberData = new listVendor_memberData ();

function listVendor_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function listVendor_initAdmin ()
{
	m_oVendorListMemberData.m_strActionItemsFunction = "listVendor_addActions (row, index)";
	document.getElementById ("listVendor_button_add").style.visibility="visible";
	document.getElementById ("listVendor_button_import").style.visibility="visible";
	document.getElementById ("listVendor_button_export").style.visibility="visible";
	listVendor_createDataGrid ();
}

function listVendor_addActions (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="listVendor_edit ('+row.m_nClientId+')"/> </td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="listVendor_delete ('+index+')"/> </td>'+
							'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="listVendor_getInfo ('+row.m_nClientId+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function listVendor_initUser ()
{
	listVendor_createDataGrid ();
}

function listVendor_getFormData () 
{
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName =$("#filterVendor_input_vendorName").val();
	oVendorData.m_strCity = $("#filterVendor_input_city").val();
	var oDemographyData = new DemographyData ();
	oVendorData.m_oDemography = oDemographyData ;
	return oVendorData;
}

function listVendor_createDataGrid ()
{
	initHorizontalSplitter("#listVendor_horizontalSplitter", "#listVendor_table_vendors");
	$('#listVendor_table_vendors').datagrid
	(
		{
			fit:true,
			columns:
			[[

				{field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:270,
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
				},
				{field:'m_strCity',title:'City',sortable:true,width:160},
				{field:'m_strTelephone',title:'Telephone',sortable:true,width:180},
				{field:'m_strEmail',title:'Email',sortable:true,width:200},
				{field:'Action',title:'Action',sortable:false,width:130,align:'center',
		        	formatter:function(value,row,index)
		        	{
		        		return listVendor_displayImages (row, index);
		        	}
		         },
			]]
		}
	);
	
	$('#listVendor_table_vendors').datagrid
	(
		{
			onSortColumn: function (strColumn, strOrder, oVendorData)
			{
				m_oVendorListMemberData.m_strSortColumn = strColumn;
				m_oVendorListMemberData.m_strSortOrder = strOrder;
				listVendor_list (strColumn, strOrder, m_oVendorListMemberData.m_nPageNumber, m_oVendorListMemberData.m_nPageSize);
			}
		}
	)
	$('#listVendor_table_vendors').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				listVendor_selectedRowData (rowData, rowIndex);
			}
		}
	)
	
	listVendor_initDGPagination ();
	listVendor_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, 1, 10);
}

function listVendor_initDGPagination ()
{
	$('#listVendor_table_vendors').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorListMemberData.m_nPageNumber = nPageNumber;
				listVendor_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listVendor_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorListMemberData.m_nPageNumber = nPageNumber;
				m_oVendorListMemberData.m_nPageSize = nPageSize;
				listVendor_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("listVendor_div_listDetail").innerHTML = "";
			}
		}
	)
}

function listVendor_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oVendorListMemberData.m_oVendorData = oRowData;
	m_oVendorListMemberData.m_nIndex = nIndex;
	document.getElementById("listVendor_div_listDetail").innerHTML = "";
	var oVendorData = new VendorData ();
	oVendorData.m_nClientId = oRowData.m_nClientId; 
	PurchaseDataProcessor.getVendorDetails (oRowData.m_nClientId,function (strXMLData)
		{
			populateXMLData (strXMLData, "vendormanagement/vendorDetails.xslt", 'listVendor_div_listDetail');
			listVendor_initializeContactDetailsDG ();
			VendorDataProcessor.getVendor (oVendorData, vendorInfo_gotContactData);
		});	
}

function listVendor_initializeContactDetailsDG ()
{
	$('#vendorDetails_table_vendorDetailsDG').datagrid
	(
		{
			columns:
			[[
				{field:'m_nContactId',title:'Contact ID',sortable:true,width:100},
				{field:'m_strContactName',title:'Contact Name',sortable:true,width:130},
				{field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:150},
				{field:'m_strEmail',title:'E-mail',sortable:true,width:150}
			]]
		}
	);
}

function vendorInfo_gotContactData (oResponse)
{
	var oVendorData = oResponse.m_arrVendorData[0];
	$('#vendorDetails_table_vendorDetailsDG').datagrid('loadData',oVendorData.m_oContacts);
}

function listVendor_showToolTip (strValue, fieldId)
{
	assert.isString(fieldId, "fieldId expected to be a string.");
	assert.isString(strValue, "strValue expected to be a string.");
	var strName = "";
	if(strValue !=null && strValue.length > 38) 
	{
		strName = strValue.substr(0,38)+"...";
		document.getElementById(fieldId).title = strValue;
	}
	else
	{
		document.getElementById(fieldId).title = "";
		strName = strValue;
	}
	return strName;
}

function listVendor_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oVendorListMemberData.m_strSortColumn = strColumn;
	m_oVendorListMemberData.m_strSortOrder = strOrder;
	m_oVendorListMemberData.m_nPageNumber = nPageNumber;
	m_oVendorListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "listVendor_progressbarLoaded ()");
}

function listVendor_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oVendorData = listVendor_getFormData ();
	VendorDataProcessor.listVendor(oVendorData, m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, m_oVendorListMemberData.m_nPageNumber, m_oVendorListMemberData.m_nPageSize, listVendor_listed);
}

function listVendor_listed (oVendorResponse)
{
	clearGridData ("#listVendor_table_vendors");
	$('#listVendor_table_vendors').datagrid('loadData',oVendorResponse.m_arrVendorData);
	$('#listVendor_table_vendors').datagrid('getPager').pagination ({total:oVendorResponse.m_nRowCount, pageNumber:m_oVendorListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function listVendor_displayImages (row, index)
{
	var oImage = eval (m_oVendorListMemberData.m_strActionItemsFunction);
	return oImage;
}

function listVendor_edit (nVendorId)
{
	assert.isNumber(nVendorId, "nVendorId expected to be a Number.");
	m_oVendorListMemberData.m_oSelectedVendorId = nVendorId;
	navigate ("vendorInfo", "widgets/vendormanagement/editVendorInfo.js");
}

function listVendor_showAddVendorPopup ()
{
	navigate ("vendorInfo", "widgets/vendormanagement/newVendorInfo.js");
}

function listVendor_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oListData = $("#listVendor_table_vendors").datagrid('getData');
	var oData = oListData.rows[nIndex];
	var oVendorData = new listVendor_getFormData ();
	oVendorData.m_nClientId = oData.m_nClientId;
	oVendorData.m_oDemography.m_nDemographyId = oData.m_oDemography.m_nDemographyId;
	oVendorData.m_arrContactData = listVendor_buildContactsArray (oData.m_oContacts);
	var bConfirm = getUserConfirmation("Do you really want to delete?")
	if(bConfirm)
		VendorDataProcessor.deleteVendorData(oVendorData, listVendor_deleted);
}

function listVendor_buildContactsArray (arrContacts)
{
	assert.isArray(arrContacts, "arrContacts expected to be an Array.");
	var oContactDataArray = new Array ();
	for (var nIndex = 0; nIndex < arrContacts.length; nIndex++)
	{
		var oContactData = new ContactData ();
		oContactData.m_nContactId = arrContacts [nIndex].m_nContactId;
		oContactData.m_strContactName = arrContacts [nIndex].m_strContactName;
		oContactData.m_strPhoneNumber = arrContacts [nIndex].m_strPhoneNumber;
		oContactData.m_strEmail = arrContacts [nIndex].m_strEmail;
		oContactData.m_strDepartment = arrContacts [nIndex].m_strDepartment;
		oContactData.m_strDesignation = arrContacts [nIndex].m_strDesignation;
		oContactDataArray.push (oContactData);
	}
	return oContactDataArray;
}
function listVendor_deleted (oVendorResponse)
{
	if(oVendorResponse.m_bSuccess)
		informUser("Vendor deleted Successfully.", "kSuccess");
	clearGridData ("#listVendor_table_vendors");
	listVendor_clearDetail ();
}

function listVendor_contactInfo_gotData (oContactResponse)
{
	var oContactData =  oContactResponse.m_arrContactData[0];
	$("#contactInfo_input_contactName").val(oContactData.m_strContactName);
	$("#contactInfo_input_phoneNumber").val(oContactData.m_strPhoneNumber);
	$("#contactInfo_input_emailAddress").val(oContactData.m_strEmail);
	$("#contactInfo_input_department").val(oContactData.m_strDepartment);
	$("#contactInfo_input_designation").val(oContactData.m_strDesignation);
}

function listVendor_clearDetail ()
{
	document.getElementById("listVendor_div_listDetail").innerHTML = "";
	listVendor_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, 1, 10);
}

function listVendor_filter ()
{
	listVendor_list (m_oVendorListMemberData.m_strSortColumn, m_oVendorListMemberData.m_strSortOrder, 1, 10);
}

function vendorInfo_handleAfterSave ()
{
	listVendor_clearDetail ();
}

function listVendor_getInfo (nVendorId)
{
	assert.isNumber(nVendorId, "nVendorId expected to be a Number.");
	m_oVendorListMemberData.m_oSelectedVendorId = nVendorId;
	navigate ("vendorTransactionForList", "widgets/vendormanagement/vendorTransactionForListVendor.js");
}

function listVendor_import ()
{
	navigate ('importVendorData','widgets/vendormanagement/importVendorDetails.js');
}

function listVendor_export ()
{
	navigate ('exportClientList','widgets/vendormanagement/exportVendorDetails.js');
}
