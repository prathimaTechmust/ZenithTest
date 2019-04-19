var vendorGroup_includeDataObjects = 
[
	'widgets/vendormanagement/VendorGroupData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

 includeDataObjects (vendorGroup_includeDataObjects, "vendorGroup_loaded()");

function vendorGroup_MemberData ()
{
	this.m_arrActions = null;
	this.m_arrSelectedData = new Array ();
	this.m_strCurrentVendorGroupName = "";
	this.editIndex = undefined;
	this.m_nVendorId = -1;
	this.m_nVendorGroupName = "";
}

var m_oVendorGroupMemberData = new vendorGroup_MemberData ();

function vendorGroup_init ()
{
	createPopup ("dialog", "#vendorGroup_button_save", "#vendorGroup_button_cancel", true);
	vendorGroup_initDataGrid ();
}

function vendorGroup_new ()
{   
	vendorGroup_init ();
	initFormValidateBoxes ("vendorGroup_form_id");
}

function vendorGroup_edit ()
{
	createPopup('ProcessDialog', '', '', true);
	vendorGroup_init ();
	document.getElementById("vendorGroup_button_save").setAttribute('update', true);
	document.getElementById("vendorGroup_button_save").innerHTML = "Update";
	var oVendorGroupData = new VendorGroupData ();
	oVendorGroupData.m_nGroupId = m_oVendorGroupListMemberData.m_nSelectedItemId;
	oVendorGroupData.m_oUserCredentialsData = new UserInformationData ();
	oVendorGroupData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	VendorGroupDataProcessor.get (oVendorGroupData, vendorGroup_gotData);
}

function vendorGroup_gotData (oResponse)
{	
	HideDialog ("ProcessDialog");
	var oVendorGroupData = oResponse.m_arrGroupData[0];
	m_oVendorGroupMemberData.m_nVendorGroupName = oVendorGroupData.m_strGroupName;
	$("#vendorGroup_input_groupName").val(oVendorGroupData.m_strGroupName);
	$('#vendorGroup_table_listofvendorGroupDG').datagrid('loadData', oVendorGroupData.m_oVendors);
	initFormValidateBoxes ("vendorGroup_form_id");
}

function vendorGroup_initDataGrid ()
{
	$('#vendorGroup_table_listofvendorGroupDG').datagrid 
	({
	    columns:[[  
	        {field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:350},
	        {field:'Actions',title:'Action',width:50,align:'center',
				formatter:function(value,row,index)
	        	{
	        		return vendorGroupList_addActions (row, index);
	        	}
			}
	    ]]
	});
}

function vendorGroupList_addActions (row, index)
{
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="vendorGroup_delete ('+index+')"/> </td>'+
				'</tr>'+
			'</table>'
		return oActions;
}

function vendorGroup_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	$('#vendorGroup_table_listofvendorGroupDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#vendorGroup_table_listofvendorGroupDG');
}

function vendorGroup_submit ()
{
	if (vendorGroup_validate () && !vendorGroup_checkGroupName () )
	{
		loadPage ("include/process.html", "ProcessDialog", "vendorGroup_submit_progressbarLoaded ()");
	}
}

function vendorGroup_submit_progressbarLoaded ()
{ 
	createPopup('ProcessDialog', '', '', true);
	var oVendorGroupData = vendorGroup_getFormData ();
	if((document.getElementById("vendorGroup_button_save").getAttribute('update') == "false"))
		VendorGroupDataProcessor.create(oVendorGroupData, vendorGroup_created);
	else
	{
		oVendorGroupData.m_nGroupId = m_oVendorGroupListMemberData.m_nSelectedItemId;
		VendorGroupDataProcessor.update(oVendorGroupData, vendorGroup_updated);
	}
}

function vendorGroup_checkGroupName ()
{
	var bIsGroupNameExist = false;
	var oVendorGroupData = new VendorGroupData ();
	oVendorGroupData.m_oUserCredentialsData = new UserInformationData ();
	oVendorGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	VendorGroupDataProcessor.list(oVendorGroupData, "", "", "", "", 
		function (oResponse)
			{
				var strGroupName = $("#vendorGroup_input_groupName").val();
				if(m_oVendorGroupMemberData.m_nVendorGroupName != strGroupName )
				{
					for(var nIndex=0; nIndex <oResponse.m_arrGroupData.length ; nIndex++)
					{
						if (strGroupName == oResponse.m_arrGroupData[nIndex].m_strGroupName)
						{
							informUser ("Group Name Already Exists", "kWarning");
							$("#vendorGroup_input_groupName").val("");
							document.getElementById("vendorGroup_input_groupName").focus();
							bIsGroupNameExist = true;
						}
					}
				}
			});
	return bIsGroupNameExist;
}

function vendorGroup_validate ()
{
	return validateForm ("vendorGroup_form_id");
}

function vendorGroup_getFormData ()
{
	var oVendorGroupData = new VendorGroupData ();
	oVendorGroupData.m_oUserCredentialsData = new UserInformationData ();
	oVendorGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oVendorGroupData.m_strGroupName = $("#vendorGroup_input_groupName").val();
	oVendorGroupData.m_oCreatedBy = new UserInformationData ();
	oVendorGroupData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorGroupData.m_arrVendorData = vendorGroup_getVendorDataArray (); 
	return oVendorGroupData;
}

function vendorGroup_getVendorDataArray ()
{
	var oVendorDataArray = new Array ();
	var arrSelectedVendorData = $('#vendorGroup_table_listofvendorGroupDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrSelectedVendorData.length; nIndex++)
	{
		var oVendorData = new VendorData ();
		oVendorData.m_nClientId = arrSelectedVendorData[nIndex].m_nClientId;
		oVendorDataArray.push (oVendorData);
	}
	return oVendorDataArray;
}

function vendorGroup_cancel ()
{
    HideDialog("dialog");
}

function vendorGroup_add ()
{
	navigate ('added','widgets/vendormanagement/vendorGroupAdd.js');
}

function vendorGroup_created (oResponse)
{
	HideDialog ("ProcessDialog");
	if(oResponse.m_bSuccess)
	{
		informUser("Vendor Group Created Successfully", "kSuccess");
		navigate ("vendorGroupList", "widgets/vendormanagement/vendorGroupList.js");
	    HideDialog("dialog");
	}
	else if(oResponse.m_nErrorID == 1)
	{
		informUser("vendor group name already exists", "kError");
	}
}

function vendorGroup_updated(oResponse)
{
	HideDialog ("ProcessDialog");
	if(oResponse.m_bSuccess);
	{
		informUser("Vendor Group Updated Successfully", "kSuccess");
		navigate ("vendorGroupList", "widgets/vendormanagement/vendorGroupList.js");
		 HideDialog("dialog");
	}
}