var itemGroup_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/ItemGroupData.js'
];

includeDataObjects (itemGroup_includeDataObjects, "itemGroup_loaded()");

function itemGroup_memberData ()
{
	this.m_nItemId = -1;
	this.m_arrItemGroupLineItems = new Array ();
	this.m_strCurrentGroupName = "";
}

var m_oItemGroupMemberData = new itemGroup_memberData ();

function itemGroup_new ()
{
	createPopup("thirdDialog", "#itemGroup_button_save", "#itemGroup_button_cancel", true);
	itemGroup_init ();
}

function itemGroup_init ()
{
	initFormValidateBoxes ("itemGroup_form_id");
	itemGroup_initDataGrid ();
}

function itemGroup_initDataGrid ()
{
	$('#itemGroup_table_groupItems').datagrid ({
	    columns:[[  
	          	{field:'m_strArticleNumber',title:'Article Number',sortable:true,width:150},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:350,
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
				}, 
				{field:'Actions',title:'Action',width:50,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return itemGroup_addActions (row, index);
		        	}
				}
	    ]]
	});
}
	
function itemGroup_addActions (row, index)
{
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="itemGroup_delete ('+index+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function itemGroup_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	$('#itemGroup_table_groupItems').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#itemGroup_table_groupItems');
}

function itemGroup_edit ()
{
	var oItemGroupData = new ItemGroupData ();
	oItemGroupData.m_nItemGroupId = m_oItemGroupListMemberData.m_nSelectedItemGroupId;
	oItemGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	createPopup ("thirdDialog", "#itemGroup_button_save", "#itemGroup_button_cancel", true);
	itemGroup_init ();
	document.getElementById("itemGroup_button_save").setAttribute('update', true);
	document.getElementById("itemGroup_button_save").innerHTML = "Update";
	ItemGroupDataProcessor.get (oItemGroupData, itemGroup_gotData);
}

function itemGroup_gotData (oResponse)
{	
	var oItemGroupData = oResponse.m_arrItemGroupData[0];
	$("#itemGroup_input_groupName").val(oItemGroupData.m_strGroupName);
	$('#itemGroup_table_groupItems').datagrid('loadData',oItemGroupData.m_oGroupItems);
	initFormValidateBoxes ("itemGroup_form_id");
}

function itemGroup_submit ()
{
	if (itemGroup_validate())
		loadPage ("include/process.html", "ProcessDialog", "itemGroup_progressbarLoaded ()");
}

function itemGroup_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	oItemGroupData = itemGroup_getFormData ();
	if(document.getElementById("itemGroup_button_save").getAttribute('update') == "false")
		ItemGroupDataProcessor.create (oItemGroupData, itemGroup_created);
	else
	{
		oItemGroupData.m_nItemGroupId = m_oItemGroupListMemberData.m_nSelectedItemGroupId;
		ItemGroupDataProcessor.update(oItemGroupData, itemGroup_updated);
	}
}

function itemGroup_updated (oResponse)
{
	HideDialog("ProcessDialog");
	if(oResponse.m_bSuccess)
	{
		informUser("Item group updated successfully.", "kSuccess");
		document.getElementById("itemGroupList_div_listDetail").innerHTML = "";
		clearGridData ("#itemGroupList_table_itemGroupListDG");
		navigate("", "widgets/inventorymanagement/item/itemGroupList.js")
	}
	HideDialog ("thirdDialog");
}

function itemGroup_created (oResponse)
{
	HideDialog("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("Item group created successfully.", "kSuccess");
		HideDialog ("thirdDialog");
		navigate("itemList", "widgets/inventorymanagement/item/itemGroupList.js");
		try
		{
			itemGroup_handleAfterSave ();
		}
		catch (oException)
		{
			
		}
	}
	else
		informUser("Item group creation failed.", "kError");
}

function itemGroup_validate ()
{
	return validateForm("itemGroup_form_id") && itemGroup_checkGroupName ();
}

function itemGroup_getFormData ()
{
	var oItemGroupData = new ItemGroupData ();
	oItemGroupData.m_oUserCredentialsData = new UserInformationData ();
	oItemGroupData.m_strGroupName = $("#itemGroup_input_groupName").val();
	oItemGroupData.m_arrGroupItems = itemGroup_getGroupDataArray ();
	oItemGroupData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oItemGroupData;
}

function itemGroup_getGroupDataArray ()
{
	var oGroupDataArray = new Array ();
	var arrGroupData = $('#itemGroup_table_groupItems').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrGroupData.length; nIndex++)
	{
		var oItemData = new ItemData ();
		oItemData.m_nItemId = arrGroupData [nIndex].m_nItemId;
		oGroupDataArray.push (oItemData);
	}
	return oGroupDataArray;
}

function itemGroup_addItems ()
{
	navigate ('addedSupply','widgets/inventorymanagement/item/listItemsForGroup.js');
}

function itemGroup_cancel ()
{
	HideDialog ("thirdDialog");
}

function itemGroup_checkGroupName ()
{
	var strGroupName = $("#itemGroup_input_groupName").val();
	if (strGroupName != m_oItemGroupMemberData.m_strCurrentGroupName)
	{
		var oItemGroupData = new ItemGroupData ();
		oItemGroupData.m_strGroupName = strGroupName;
		oItemGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		ItemGroupDataProcessor.list(oItemGroupData, "", "", 1,10, function (oResponse){
			if (oResponse.m_arrItemGroupData.length > 0)
			{
				informUser ("Group name already exists.", "kWarning");
				$("#itemGroup_input_groupName", "").val();
				document.getElementById("itemGroup_input_groupName").focus();
			}
			else
				itemGroup_progressbarLoaded ();
		});
	}
}
