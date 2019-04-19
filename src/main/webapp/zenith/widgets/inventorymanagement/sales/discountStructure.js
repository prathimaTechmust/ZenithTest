var discountStructure_includeDataObjects = 
[
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/ClientGroupData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/item/ItemGroupData.js',
	'widgets/inventorymanagement/sales/DiscountStructureData.js'
];



 includeDataObjects (discountStructure_includeDataObjects, "discountStructure_loaded ()");

function discountStructure_loaded ()
{
	loadPage ("inventorymanagement/sales/discountStructure.html", "dialog", "discountStructure_init ()");
}

function discountStructure_memberData ()
{
	this.m_nEditIndex = undefined;
	this.m_arrClientGroupDetails = new Array ();
	this.m_nSelectedClientId = -1;
}
var m_oDiscountStructureMemberData = new discountStructure_memberData ();

function discountStructure_init ()
{
	createPopup("dialog", "#discountStructure_button_save", "#discountStructure_button_cancel", true);
	initFormValidateBoxes ("discountStructure_form_id");
	discountStructure_initClientGroupCombobox ();
	discountStructure_initializeItemGroupDataGrid ();
}

function discountStructure_initializeClientGroupDataGrid ()
{
	$('#discountStructure_table_clientGroupListDG').datagrid({
	    columns:[[  
	              
	        {field:'m_strGroupName',title:'Group Name',sortable:true,width:150}
	    ]],
	    onSelect: function (rowIndex, rowData)
		{
			discountStructure_selectedRowData (rowData);
		}
	});
	discountStructure_list ();
}

function discountStructure_selectedRowData (oClientGroupData)
{
	discountStructure_setClientGroupInfo(oClientGroupData);
	$("#discountStructure_div_clientGroupInfo").hide();
}

function  discountStructure_list ()
{
	var oClientGroupData = new ClientGroupData ();
	oClientGroupData.m_oUserCredentialsData = new UserInformationData ();
	oClientGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oClientGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ClientGroupDataProcessor.list(oClientGroupData, "","", "", "", discountStructure_listed);
}

function discountStructure_listed (oResponse)
{
	$("#discountStructure_table_clientGroupListDG").datagrid('loadData', oResponse.m_arrGroupData);
}

function discountStructure_initClientGroupCombobox ()
{
	$('#discountStructure_input_clientGroup').combobox
	({
		valueField:'m_nGroupId',
	    textField:'m_strGroupName',
	    selectOnNavigation: false,
	    loader: getFilteredClientGroupData,
		mode: 'remote',
		onSelect:function(row)
	    {
    		m_oDiscountStructureMemberData.m_arrClientGroupDetails = row;
    		discountStructure_setClientGroupInfo(row);
	    }
	});
	var toTextBox = $('#discountStructure_input_clientGroup').combobox('textbox');
	toTextBox[0].placeholder = "Enter Client Group Name";
	toTextBox.focus ();
	toTextBox.bind('keydown', function (e)
		    {
				discountStructure_handleKeyboardNavigation (e);
		    });
}

var getFilteredClientGroupData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oClientGroupData = new ClientGroupData ();
		oClientGroupData.m_strGroupName = strQuery;
		ClientGroupDataProcessor.getClientGroupSuggesstions (oClientGroupData,"", "","", "", function(oResponse)
				{
					var arrClientGroupInfo = new Array ();
					for(var nIndex=0; nIndex< oResponse.m_arrGroupData.length; nIndex++)
				    {
						arrClientGroupInfo.push(oResponse.m_arrGroupData[nIndex]);
				    }
					success(arrClientGroupInfo);
				});
	}
	else
		success(new Array ());
}

function discountStructure_handleKeyboardNavigation (oEvent)
{
	assert.isObject(oEvent, "oEvent expected to be an Object.");
	assert( Object.keys(oEvent).length >0 , "oEvent cannot be an empty .");// checks for non emptyness 
	if(oEvent.keyCode == 13)
		discountStructure_setClientGroupInfo(m_oDiscountStructureMemberData.m_arrClientGroupDetails);
}

function discountStructure_setClientGroupInfo(oClientGroupData)
{
	assert.isObject(oClientGroupData, "oClientGroupData expected to be an Object.");
	m_oTrademustMemberData.m_nSelectedClientId = oClientGroupData.m_nGroupId;
	$("#discountStructure_input_clientGroup").combobox('setValue',oClientGroupData.m_nGroupId);
	$("#discountStructure_input_clientGroup").combobox('setText',oClientGroupData.m_strGroupName);
	discountStructure_getItemGroupData ();
}

function discountStructure_initializeItemGroupDataGrid ()
{
	$('#discountStructure_table_itemGroupTable').datagrid ({
	    columns:[[  
	              	{field:'m_strGroupName',title:'Group Name',width:300},
	             	{field:'m_nDiscount',title:'Disc(%)',width:100,align:'right',editor:{'type':'numberbox','options':{precision:'2'}}}
	    ]],
	    onClickRow: function (rowIndex, rowData)
		{
			discountStructure_editRowDG (rowData, rowIndex);
		}
	});
	discountStructure_populateItemGroupData ()
}

function discountStructure_populateItemGroupData ()
{
	var oItemGroupData = new ItemGroupData ();
	oItemGroupData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemGroupData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemGroupDataProcessor.list(oItemGroupData, "", "", 1, 10, discountStructure_gotThelist);
}

function discountStructure_gotThelist (oResponse)
{	
	$('#discountStructure_table_itemGroupTable').datagrid('loadData', oResponse.m_arrItemGroupData);
}

function discountStructure_getItemGroupData ()
{
	var oDiscountStructureData = new DiscountStructureData ();
	oDiscountStructureData.m_oClientGroupData.m_nGroupId = m_oTrademustMemberData.m_nSelectedClientId;
	oDiscountStructureData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oDiscountStructureData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	DiscountStructureDataProcessor.list(oDiscountStructureData, "", "", "", "", discountStructure_gotlist);
}

function discountStructure_gotlist (oResponse)
{	
	var arrDiscountData = new Array ();
	m_oTrademustMemberData.m_nSelectedClientId = -1;
	var arrItemGroupData = $('#discountStructure_table_itemGroupTable').datagrid('getRows');
	for(var nIndex = 0; nIndex< arrItemGroupData.length; nIndex++)
	{
		var nValue = discountStructure_getDiscountValue (arrItemGroupData[nIndex], oResponse);
		$('#discountStructure_table_itemGroupTable').datagrid('updateRow',{
			index: nIndex,
			row: 
			{
				m_nDiscount : nValue
			}
		});
	}
}

function discountStructure_getDiscountValue (oItemGroupData, oResponse)
{
	assert.isObject(oItemGroupData, "oItemGroupData expected to be an Object.");
	var nDiscount = 0;
	for(var nIndex = 0; nIndex < oResponse.m_arrDiscountStructureData.length; nIndex++)
	{
		if(oItemGroupData.m_nItemGroupId == oResponse.m_arrDiscountStructureData[nIndex].m_oItemGroupData.m_nItemGroupId)
		{
			nDiscount = oResponse.m_arrDiscountStructureData[nIndex].m_nDiscount.toFixed(2);
			break;
		}
	}
	return nDiscount;
}

function discountStructure_editRowDG (rowData, rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	 if (m_oDiscountStructureMemberData.m_nEditIndex != rowIndex)
	    {
	        if (discountStructure_endEdit ())
	        {
	        	m_oDiscountStructureMemberData.m_nEditIndex = rowIndex;
	            $('#discountStructure_table_itemGroupTable').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
	            discountStructure_setEditing(m_oDiscountStructureMemberData.m_nEditIndex);
	        } 
	        else 
	            $('#discountStructure_table_itemGroupTable').datagrid('selectRow', m_oDiscountStructureMemberData.m_nEditIndex);
	    }
}

function discountStructure_endEdit ()
{
    if (m_oDiscountStructureMemberData.m_nEditIndex == undefined)
    	return true
    if ($('#discountStructure_table_itemGroupTable').datagrid('validateRow', m_oDiscountStructureMemberData.m_nEditIndex))
    {
        $('#discountStructure_table_itemGroupTable').datagrid('endEdit',  m_oDiscountStructureMemberData.m_nEditIndex);
        m_oDiscountStructureMemberData.m_nEditIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function discountStructure_setEditing(nRowIndex)
{
    var editors = $('#discountStructure_table_itemGroupTable').datagrid('getEditors', nRowIndex);
    var discountEditor = editors[0];
    discountEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
}

function discountStructure_submit ()
{
	if (discountStructure_validate ())
	{
		var arrDiscountStructureData = discountStructure_getFormData ();
		DiscountStructureDataProcessor.save (arrDiscountStructureData, discountStructure_created);
	}
}

function discountStructure_validate ()
{
	return validateForm("discountStructure_form_id")  && discountStructure_validateRequiredField ();
}

function discountStructure_validateRequiredField ()
{
	var bIsSelectFieldValid = false;
	var nClientId = $('#discountStructure_input_clientGroup').combobox('getValue');
	if(nClientId > 0)
		bIsSelectFieldValid = true;
	else
	{
		informUser("Please select client group name.", "kWarning");
		$('#discountStructure_input_clientGroup').combobox('textbox').focus ();
	}
	return bIsSelectFieldValid;
}

function discountStructure_acceptDGchanges()
{
	if (discountStructure_endEdit())
        $('#discountStructure_table_itemGroupTable').datagrid('acceptChanges');
}

function discountStructure_getFormData ()
{
	var oDiscountStructureDataArray = new Array ();
	discountStructure_acceptDGchanges ();
	var arrGroupItemsData = $('#discountStructure_table_itemGroupTable').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrGroupItemsData.length; nIndex++)
	{
		var oDiscountStructureData = new DiscountStructureData ();
		oDiscountStructureData.m_oUserCredentialsData = new UserInformationData ();
		oDiscountStructureData.m_oClientGroupData.m_nGroupId = $('#discountStructure_input_clientGroup').combobox('getValue');
		oDiscountStructureData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oDiscountStructureData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oDiscountStructureData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		var nDiscount = parseFloat(arrGroupItemsData [nIndex].m_nDiscount)
		oDiscountStructureData.m_nDiscount = nDiscount > 0 ? nDiscount : 0;
		oDiscountStructureData.m_oItemGroupData.m_nItemGroupId = arrGroupItemsData[nIndex].m_nItemGroupId;
		oDiscountStructureDataArray.push (oDiscountStructureData);
	}
	return oDiscountStructureDataArray;
}

function discountStructure_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{	
		HideDialog ("dialog");
		informUser ("Discount structure saved succesfully.", "kSuccess");
	}
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Discount structure save failed.", "kError");
}

function discountStructure_cancel()
{
	HideDialog ("dialog");
}

function discountStructure_getclientGroupInfo ()
{
	 $("#discountStructure_div_clientGroupInfo").toggle();
	 discountStructure_initializeClientGroupDataGrid ();
}