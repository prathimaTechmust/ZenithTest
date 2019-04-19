var property_includeDataObjects = 
[
	'widgets/propertymanagement/PropertyData.js',
	'widgets/propertymanagement/PropertyDetailData.js',
	'widgets/propertymanagement/PropertyPhotoData.js',
	'widgets/propertymanagement/propertytype/PropertyTypeData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js'
];



 includeDataObjects (property_includeDataObjects, "property_loaded()");

function property_memberData ()
{
	this.m_nPropertyId = -1;
	this.m_arrClientDetails = new Array ();
	this.m_nAttachmentId = -1;
	this.m_oFile = null;
}

var m_oPropertyMemberData = new property_memberData ();

function property_new ()
{
	createPopup("dialog", "#property_button_create", "#property_button_cancel", true);
	property_init ();
}

function property_init ()
{
	$('#property_div_propertyDetails').tabs ();
	initFormValidateBoxes ("property_form_id");
	property_initClientCombobox ()
	property_populatePropertyTypeList ();
	property_initPhotoDG ();
}

function property_initClientCombobox ()
{
	$('#property_input_clientName').combobox
	({
		valueField:'m_nClientId',
	    textField:'m_strTo',
	    selectOnNavigation: false,
	    loader: getFilteredClientData,
		mode: 'remote',
	    formatter:function(row)
    	{
        	var opts = $(this).combobox('options');
        	return decodeURIComponent(row[opts.textField]);
    	},
		onSelect:function(row)
	    {
    		var oUpdateButton = document.getElementById("property_button_create");
    		if(row.m_bClientLock && oUpdateButton.getAttribute('update') == "false")
    		{
    			informUser ("This Client is locked!", "");
    			$("#property_input_clientName").combobox('setText',"");
    			$('#property_input_clientName').combobox('textbox').focus ();
    		}
    		else
    		{
    			m_oPropertyMemberData.m_arrClientDetails = row;
				property_setClientInfo(m_oPropertyMemberData.m_arrClientDetails);
    		}
	    }
	});
	var toTextBox = $('#property_input_clientName').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Client Name";
	toTextBox.focus ();
	toTextBox.bind('keydown', function (e)
		    {
		      	property_handleKeyboardNavigation (e);
		    });
}

var getFilteredClientData = function(param, success, error)
{
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oClientData = new ClientData ();
		oClientData.m_strCompanyName = strQuery;
		ClientDataProcessor.getClientSuggesstions (oClientData, "", "", function(oResponse)
		{
			var arrClientInfo = new Array ();
			for(var nIndex=0; nIndex< oResponse.m_arrClientData.length; nIndex++)
		    {
				arrClientInfo.push(oResponse.m_arrClientData[nIndex]);
				arrClientInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrClientData[nIndex].m_strCompanyName + " "+"|" +" "+
		    											oResponse.m_arrClientData[nIndex].m_strTinNumber);
		    }
			success(arrClientInfo);
		});
	}
	else
		success(new Array ());
}

function property_handleKeyboardNavigation (e)
{
	if(e.keyCode == 13)
		property_setClientInfo(m_oPropertyMemberData.m_arrClientDetails);
}

function property_setClientInfo(oClientData)
{
	$("#property_input_clientName").combobox('setText',oClientData.m_strCompanyName);
	$("#property_input_address").focus ();
}

function property_populatePropertyTypeList ()
{
	var oPropertyTypeData = new PropertyTypeData ();
	PropertyTypeDataProcessor.list(oPropertyTypeData, "", "", 1, 10, property_prepareDD );
}

function property_prepareDD (oResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption(-1, "Select"));
	for (var nIndex = 0; nIndex < oResponse.m_arrPropertyTypeData.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrPropertyTypeData [nIndex].m_nPropertyTypeId,
				oResponse.m_arrPropertyTypeData [nIndex].m_strPropertyType));
	PopulateDD ("property_select_propertyType", arrOptions);
}

function property_initPhotoDG ()
{
	$('#property_table_photoDG').datagrid ({
	    columns:[[  
		        {field:'m_strFileName',title:'Description', width:80},
		        {field:'Actions',title:'Action',align:'center',
					formatter:function(value,row,index)
					{
	        			return property_displayAttachmentDGAction (row, index);
					}
				}
	        ]]
	});
}

function property_displayAttachmentDGAction (nRowId, nIndex)
{
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="property_deleteAttachment ('+nIndex+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function property_deleteAttachment (nIndex)
{
	$('#property_table_photoDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#property_table_photoDG');
}

function property_addPhotos ()
{
	loadPage ("propertymanagement/photoAttachment.html", "secondDialog", "property_showPopup()");
}

function property_showPopup ()
{
	createPopup("dialog", "", "", true);
}

function Property_addPhoto ()
{
	var oPropertyPhotoData = photoAttachment_getFormData ();
	photoAttachment_insertDataToAttachmentDG (oPropertyPhotoData);
}

function property_getFormData ()
{
	var oPropertyPhotoData = new PropertyPhotoData ();
	oPropertyPhotoData.m_strFileName = dwr.util.getValue("property_input_fileName");
	oPropertyPhotoData.m_strFileName = getImageName(oPropertyPhotoData.m_strFileName.value);
	oPropertyPhotoData.m_oFile = m_oQuotationAttachmentMemberData.m_oFile;
	return oPropertyPhotoData;
}

function property_insertDataToAttachmentDG (oPropertyPhotoData)
{
	if(property_validate() &&  !property_isAttachmentExists (oPropertyPhotoData))
	{
		HideDialog ("secondDialog");
		$('#property_table_photoDG').datagrid('appendRow', oPropertyPhotoData);
	}
}

function property_validate ()
{
	return validateForm("property_form_id");
}

function property_getAttachment ()
{
	m_oQuotationAttachmentMemberData.m_oFile = dwr.util.getValue("property_input_fileName");
}

function property_isAttachmentExists (oPropertyPhotoData)
{
	var bIsExists = false;
	var arrAttachmentData = $('#property_table_photoDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrAttachmentData.length; nIndex++)
	{
		if(arrAttachmentData [nIndex].m_strFileName == oPropertyPhotoData.m_strFileName)
		{
			bIsExists = true;
			document.getElementById("property_td_fileName").innerHTML = "This file is already added";
			break;
		}
	}
	return bIsExists;
}

function property_addPhoto ()
{
	HideDialog("secondDialog");
}

function property_cancel ()
{
	HideDialog ("dialog");
}