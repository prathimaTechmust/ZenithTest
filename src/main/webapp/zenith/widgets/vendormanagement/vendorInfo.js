var vendorInfo_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	
];

 includeDataObjects (vendorInfo_includeDataObjects, "vendorInfo_loaded ()");

function vendorInfo_memberData ()
{
	this.m_oVendorData = null;
	this.m_nIndex = null;
	this.m_nVendorId = -1;
}

var m_ovendorInfoMemberData = new vendorInfo_memberData ();

function vendorInfo_init ()
{
	createPopup("secondDialog", "#vendorInfo_button_submit", "#vendorInfo_button_cancel", true);
	$('#vendorInfo_div_tabs').tabs ();
	$('#vendorInfo_textarea_address').addresspicker ({updateCallback: vendorInfo_showCallback});
	$('#vendorInfo_input_city').addresspicker ({updateCallback: vendorInfo_showCallback});
	initFormValidateBoxes ("vendorInfo_form_id");
	vendorInfo_populateBusinessTypeList ();
	vendorInfo_contactGridInit ();
}

function vendorInfo_showCallback (oGeocodeResult, oParsedGeocodeResult)
{
	assert.isObject(oParsedGeocodeResult, "oParsedGeocodeResult expected to be an Object.");
	assert( Object.keys(oParsedGeocodeResult).length >0 , "oParsedGeocodeResult cannot be an empty .");// checks for non emptyness 
	if (oParsedGeocodeResult.postal_code != false)
		$("#vendorInfo_input_pinCode").val(oParsedGeocodeResult.postal_code);
	if (oParsedGeocodeResult.administrative_area_level_2 && oParsedGeocodeResult.administrative_area_level_1 && oParsedGeocodeResult.country != false)
		$("#vendorInfo_input_city").val(oParsedGeocodeResult.administrative_area_level_2 + "," + oParsedGeocodeResult.administrative_area_level_1 + "," +oParsedGeocodeResult.country);
}

function vendorInfo_new ()
{
	m_ovendorInfoMemberData.m_bIsNew = true;
	vendorInfo_loadElements (m_ovendorInfoMemberData.m_bIsNew);
	vendorInfo_init ();
}

function vendorInfo_edit ()
{
	vendorInfo_init ();
	m_ovendorInfoMemberData.m_bIsNew = false;
	vendorInfo_loadElements (m_ovendorInfoMemberData.m_bIsNew);
	var oVendorData = new VendorData ();
	oVendorData.m_nClientId = m_ovendorInfoMemberData.m_nVendorId;
	VendorDataProcessor.getVendor (oVendorData, vendorInfo_gotData);
}

function vendorInfo_contactGridInit ()
{
	$('#vendorInfo_table_listContactPersons').datagrid 
	(
		{
		    columns:
		    [[  
				{
					field:'m_strContactName',title:'Contact Name',sortable:true,width:90,
				},
				{
					field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:80
				},
				{
					field:'m_strEmail',title:'Email',sortable:true,width:50
				},
				{
					field:'m_strDepartment',title:'Department',sortable:true,width:80
				},
				{
					field:'m_strDesignation',title:'Designation',sortable:true,width:70
				},
				{
					field:'Action',title:'Action',sortable:false,width:70,
		        	formatter:function(value,row,index)
		        	{
		        		return vendorInfo_contactInfo_displayImages (row, index);
		        	}
		         },
			]]
		}
	);
	
	$('#vendorInfo_table_listContactPersons').datagrid
	(
		{
			onSortColumn: function (strColumn, strOrder) 
			{
				vendorInfo_contactInfo_sort (strColumn, strOrder);
			}
		}
	)
}

 function vendorInfo_contactInfo_sort (strColumn, strOrder)
 {
	 assert.isString(strColumn, "strColumn expected to be a string.");
	 assert.isString(strOrder, "strOrder expected to be a string.");
		var oListData = $("#vendorInfo_table_listContactPersons").datagrid('getData');
		var oArrayData = new Array();
		for (var nIndex = 0; nIndex < oListData.rows.length; nIndex++)
		{
			oArrayData.push(oListData.rows[nIndex])
		}
		var osortedData = oArrayData.sort(function (oData1, oData2)
				{
					var bResult = false;
					if (strColumn == "m_strContactName")
					{
						var nResult = oData1.m_strContactName.localeCompare (oData2.m_strContactName);
						bResult =  nResult >= 0 ? true : false;
					}
					else if (strColumn == "m_strPhoneNumber")
					{
						var nResult = oData1.m_strPhoneNumber.localeCompare (oData2.m_strPhoneNumber);
						bResult =  nResult >= 0 ? true : false;
					}
					else if (strColumn == "m_strEmail")
					{
						var nResult = oData1.m_strEmail.localeCompare (oData2.m_strEmail);
						bResult =  nResult >= 0 ? true : false;
					}
					else if (strColumn == "m_strDepartment")
					{
						var nResult = oData1.m_strDepartment.localeCompare (oData2.m_strDepartment);
						bResult =  nResult >= 0 ? true : false;
					}
					else if (strColumn == "m_strDesignation")
					{
						var nResult = oData1.m_strDesignation.localeCompare (oData2.m_strDesignation);
						bResult =  nResult >= 0 ? true : false;
					}
					if (strOrder=="desc")	
						bResult= !bResult;
					return bResult;
			});
		clearGridData ("#vendorInfo_table_listContactPersons");
		vendorInfo_contactInfo_sorted (osortedData);
 }

 function vendorInfo_contactInfo_sorted (oContactData)
 {
	 assert.isArray(oContactData, "oContactData expected to be an Array.");
	 for (var nIndex = 0; nIndex < oContactData.length; nIndex++)
	 {
		 $('#vendorInfo_table_listContactPersons').datagrid('appendRow',oContactData[nIndex]);
	 }
 }
 
function vendorInfo_getFormData ()
{
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = $("#vendorInfo_input_vendorName").val();
	oVendorData.m_strAddress = $("#vendorInfo_textarea_address").val();
	oVendorData.m_strCity = $("#vendorInfo_input_city").val();
	oVendorData.m_strPinCode = $("#vendorInfo_input_pinCode").val();
	oVendorData.m_strTelephone = $("#vendorInfo_input_telephone").val();
	oVendorData.m_strMobileNumber = $("#vendorInfo_input_mobile").val();
	oVendorData.m_strEmail = $("#vendorInfo_input_email").val();
	oVendorData.m_strWebAddress = $("#vendorInfo_input_webUrl").val();
	oVendorData.m_strTinNumber = $("#vendorInfo_input_tinNumber").val();
	oVendorData.m_strVatNumber = $("#vendorInfo_input_vatNumber").val();
	oVendorData.m_strCSTNumber = $("#vendorInfo_input_cstNumber").val();
	oVendorData.m_bAllowOnlineAccess = $("#vendorInfo_input_onlineAccess").is(":checked");
	oVendorData.m_bClientLock = $("#vendorInfo_input_vendorLock").is(":checked");
	oVendorData.m_bOutstationClient = $("#vendorInfo_input_outStationVendor").is(":checked");
	oVendorData.m_bVerified = $("#vendorInfo_input_verified").is(":checked");
	var oDemographyData = new DemographyData ();
	var oBusinessTypeData = new BusinessTypeData ();
	oBusinessTypeData.m_nBusinessTypeId = $("#vendorInfo_select_businessType").val();
	oDemographyData.m_oBusinessType = oBusinessTypeData;
	if(!m_ovendorInfoMemberData.m_bIsNew)
		oDemographyData.m_nDemographyId = m_ovendorInfoMemberData.m_oVendorData.m_oDemography.m_nDemographyId;
	oVendorData.m_oDemography = oDemographyData;
	return oVendorData;
}

function vendorInfo_validate ()
{
	return validateForm("vendorInfo_form_id");
}

function vendorInfo_submit ()
{
	var oVendorData = vendorInfo_getFormData ();
	oVendorData.m_arrContactData = vendorInfo_getContactDataArray (); 
	do
	{
		if (!(vendorInfo_validate () && vendorInfo_checkMandatoryFieldes (oVendorData)))
			break;
		var oVendorData = vendorInfo_getFormData ();
		oVendorData.m_arrContactData = vendorInfo_getContactDataArray (); 
		if(document.getElementById("vendorInfo_button_submit").getAttribute('update') == "false")
			VendorDataProcessor.createVendor(oVendorData, vendorInfo_created);
		else
		{
			oVendorData.m_nClientId = m_ovendorInfoMemberData.m_nVendorId;
			oVendorData.m_nOpeningBalance = m_ovendorInfoMemberData.m_oVendorData.m_nOpeningBalance;
			oVendorData.m_nCreditLimit = m_ovendorInfoMemberData.m_oVendorData.m_nCreditLimit;
			oVendorData.m_strPassword = m_ovendorInfoMemberData.m_oVendorData.m_strPassword;
			oVendorData.m_bAllowAutomaticPublishing = m_ovendorInfoMemberData.m_oVendorData.m_bAllowAutomaticPublishing;
			VendorDataProcessor.updateVendor (oVendorData, vendorInfo_updated);
		}
	}while (false);
}

function vendorInfo_cancel ()
{
	HideDialog ("secondDialog");
}

function vendorInfo_populateBusinessTypeList ()
{
	var oBusinessTypeData = new BusinessTypeData ();
	BusinessTypeDataProcessor.list(oBusinessTypeData, "", "", 0, 10,listedBusinessData);
}

function listedBusinessData (oResponse)
{
	vendorInfo_prepareBusinessTypeDD ("vendorInfo_select_businessType", oResponse);
}

function vendorInfo_prepareBusinessTypeDD (strBusinessTypeDD, oVendorResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oVendorResponse.m_arrBusinessType.length; nIndex++)
		arrOptions.push (CreateOption (oVendorResponse.m_arrBusinessType [nIndex].m_nBusinessTypeId,
				oVendorResponse.m_arrBusinessType [nIndex].m_strBusinessName));
	PopulateDD (strBusinessTypeDD, arrOptions);
}

function vendorInfo_contactInfo_displayImages (rowId, index)
{
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId'+ index +'" onClick="vendorInfo_contactInfo_edit('+ index +')" /> </td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId'+ index +'" onClick="vendorInfo_contactInfo_delete('+ index  +')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function vendorInfo_contactInfo_edit (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_ovendorInfoMemberData.m_nIndex = nIndex;
	navigate ("vendorContactInfo", "widgets/vendormanagement/vendorEditContactInfo.js");
}

function vendorInfo_contactInfo_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var bConfirm = getUserConfirmation ("Do you really want to delete ?")
	if (bConfirm == true)
	{
		$('#vendorInfo_table_listContactPersons').datagrid('deleteRow',nIndex);
		var nRows = $('#vendorInfo_table_listContactPersons').datagrid('getRows');
		if(nRows.length != 0)
			vendorInfo_contactInfo_updateRowIndex(nRows)
		informUser("Contact deleted successfully.", "kSuccess");
	}
}

function vendorInfo_contactInfo_updateRowIndex (nRows)
{
	assert.isArray(nRows, "nRows expected to be an Array.");
	for(var nIndex = 0; nIndex<nRows.length; nIndex++)
	{
		$('#vendorInfo_table_listContactPersons').datagrid('updateRow',{index : nIndex});
	}
}

function vendorInfo_getContactDataArray ()
{
	var oContactDataArray = new Array ();
	var arrContactData = $('#vendorInfo_table_listContactPersons').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrContactData.length; nIndex++)
	{
		var oContactData = new ContactData ();
		if(!m_ovendorInfoMemberData.m_bIsNew)
			oContactData.m_nContactId = arrContactData [nIndex].m_nContactId;
		oContactData.m_strContactName = arrContactData [nIndex].m_strContactName;
		oContactData.m_strPhoneNumber = arrContactData [nIndex].m_strPhoneNumber;
		oContactData.m_strEmail = arrContactData [nIndex].m_strEmail;
		oContactData.m_strDepartment = arrContactData [nIndex].m_strDepartment;
		oContactData.m_strDesignation = arrContactData [nIndex].m_strDesignation;
		oContactDataArray.push (oContactData);
	}
	return oContactDataArray;
}

function vendorInfo_created (oVendorInfoResponse)
{
	if(oVendorInfoResponse.m_bSuccess)
	{
		informUser("Vendor created successfully.", "kSuccess");
		HideDialog ("secondDialog");
		try
		{
			vendorInfo_handleAfterSave ();
		}
		catch(oException){}
	}
	else if(oVendorInfoResponse.m_nErrorID == 1)
	{
		informUser("error message", "kError");
	}
}

function vendorInfo_updated (oVendorInfoResponse)
{
	if(oVendorInfoResponse.m_bSuccess)
	{
		informUser("Vendor updated successfully.", "kSuccess");
		clearGridData ("#listVendor_table_vendors");
		HideDialog ("secondDialog");
		listVendor_clearDetail ();
	}
	else if(oVendorInfoResponse.m_nErrorID)
	{
		informUser("Error message", "kError");
	}
}

function vendorInfo_gotData (oVendorResponse)
{	
	m_ovendorInfoMemberData.m_oVendorData = oVendorResponse.m_arrVendorData[0];
	$("#vendorInfo_input_vendorName").val(m_ovendorInfoMemberData.m_oVendorData.m_strCompanyName);
	$("#vendorInfo_textarea_address").val(m_ovendorInfoMemberData.m_oVendorData.m_strAddress);
	$("#vendorInfo_input_city").val(m_ovendorInfoMemberData.m_oVendorData.m_strCity);
	$("#vendorInfo_input_pinCode").val(m_ovendorInfoMemberData.m_oVendorData.m_strPinCode);
	$("#vendorInfo_input_state").val(m_ovendorInfoMemberData.m_oVendorData.m_strState);
	$("#vendorInfo_input_country").val(m_ovendorInfoMemberData.m_oVendorData.m_strCountry);
	$("#vendorInfo_input_telephone").val(m_ovendorInfoMemberData.m_oVendorData.m_strTelephone);
	$("#vendorInfo_input_mobile").val(m_ovendorInfoMemberData.m_oVendorData.m_strMobileNumber);
	$("#vendorInfo_input_email").val(m_ovendorInfoMemberData.m_oVendorData.m_strEmail);
	$("#vendorInfo_input_webUrl").val(m_ovendorInfoMemberData.m_oVendorData.m_strWebAddress);
	$("#vendorInfo_input_tinNumber").val(m_ovendorInfoMemberData.m_oVendorData.m_strTinNumber);
	$("#vendorInfo_input_vatNumber").val(m_ovendorInfoMemberData.m_oVendorData.m_strVatNumber);
	$("#vendorInfo_input_cstNumber").val(m_ovendorInfoMemberData.m_oVendorData.m_strCSTNumber);
	document.getElementById("vendorInfo_input_onlineAccess").checked =m_ovendorInfoMemberData.m_oVendorData.m_bAllowOnlineAccess;
	document.getElementById("vendorInfo_input_vendorLock").checked = m_ovendorInfoMemberData.m_oVendorData.m_bClientLock;
	document.getElementById("vendorInfo_input_outStationVendor").checked = m_ovendorInfoMemberData.m_oVendorData.m_bOutstationClient;
	document.getElementById("vendorInfo_input_verified").checked = m_ovendorInfoMemberData.m_oVendorData.m_bVerified;
	$("#vendorInfo_select_businessType").val(m_ovendorInfoMemberData.m_oVendorData.m_oDemography.m_oBusinessType.m_nBusinessTypeId);
	vendorInfo_contactInfo_setContacts (m_ovendorInfoMemberData.m_oVendorData.m_oContacts);
	initFormValidateBoxes ("vendorInfo_form_id");
}

function vendorInfo_contactInfo_setContacts (arrContacts)
{
	assert.isArray(arrContacts, "arrContacts expected to be an Array.");
	for(var nIndex = 0; nIndex < arrContacts.length; nIndex++)
	{
		$("#vendorInfo_table_listContactPersons").datagrid('appendRow', arrContacts[nIndex]);
	}
}

function vendorInfo_errorHandler (msg, oException) 
{
	assert.isObject(msg, "msg expected to be an Object.");
	informUser("Error message is: " + msg + " - Error Details: "+ "kError");
}

function vendorInfo_contactInfo_addContact ()
{
	navigate ("vendorContactInfo", "widgets/vendormanagement/vendorNewContactInfo.js");
}

function vendorInfo_loadElements (bIsNew)
{
	assert.isBoolean(bIsNew, "bIsNew should be a boolean value");
	if(bIsNew)
	{
		document.getElementById("vendorInfo_button_submit").setAttribute('update','false');
	}
	else
	{
		document.getElementById("vendorInfo_button_submit").setAttribute('update','true');
		document.getElementById("vendorInfo_button_submit").innerHTML = "Update";
	}
}

function vendorInfo_checkMandatoryFieldes (oVendorData)
{
	assert.isObject(oVendorData, "oVendorData expected to be an Object.");
	assert( Object.keys(oVendorData).length >0 , "oVendorData cannot be an empty .");// checks for non emptyness 
	var bIsMandatory = true;
	if(oVendorData.m_oDemography.m_oBusinessType.m_nBusinessTypeId == "-1")
	{
		vendorInfo_showTab("vendorInfo_div_demographyInfo", "vendorInfo_div_demographyInfoTab");
		vendorInfo_showErrorLable ("vendorInfo_div_demographyInfo", "vendorInfo_label_error");
		bIsMandatory = false;
	}
	else
	{
		var oErrorLabel = document.getElementById("vendorInfo_label_error");
		oErrorLabel.style.display = "none";
	}
	return bIsMandatory;
}

function vendorInfo_showErrorLable (divId, errorLabel)
{
	assert.isString(divId, "divId expected to be a string.");
	assert.isString(errorLabel, "errorLabel expected to be a string.");
	var oDivision = document.getElementById(divId);
	var oErrorLabel = document.getElementById(errorLabel);
	if(oDivision.id == "vendorInfo_div_demographyInfo")
		oErrorLabel.innerHTML = "**select business type";
	else
		oErrorLabel.innerHTML = "**Fill the required fields";
	oErrorLabel.style.display = "block";
	oDivision.appendChild(oErrorLabel);
}

function vendorInfo_removeLabel ()
{
	var oErrorLabel = document.getElementById("vendorInfo_label_error");
	oErrorLabel.style.display = "none";
}

function vendorInfo_showVerifyIcon ()
{
	var strTinNumber = $("#vendorInfo_input_tinNumber").val();
	if(strTinNumber.trim() != "")
		document.getElementById ("vendorInfo_img_verifyTin").style.visibility="visible";
	else
		document.getElementById ("vendorInfo_img_verifyTin").style.visibility="hidden";
}

function vendorInfo_verifyTinNumber ()
{
	m_ovendorInfoMemberData.m_strTinNumber = $("#vendorInfo_input_tinNumber").val();
	loadPage ("clientmanagement/verifyTin.html", "thirdDialog", "vendorInfo_showPopUp ()");
}

function vendorInfo_showPopUp ()
{
	createPopup ("thirdDialog", "", "", true);
	var strURL = "https://www.tinxsys.com/TinxsysInternetWeb/dealerControllerServlet?searchBy=TIN&tinNumber=" + m_ovendorInfoMemberData.m_strTinNumber;
	var strIframe = '<iframe class="frame" frameborder="0" src="'+ strURL +'"></iframe>';
	document.getElementById("verifyTin_div_verifyURL").innerHTML = strIframe;
}

function vendorInfo_checkEmailAddress ()
{
	var strEmail = $("#vendorInfo_input_email").val();
	if (strEmail == "")
	{
		informUser("Please provide email address.", "kWarning");
		document.getElementById("vendorInfo_input_onlineAccess").checked = false;
		$("#vendorInfo_input_email").focus ();
	}
}

function clientInfo_verifyNotOk ()
{
	HideDialog ("thirdDialog");
	$("#vendorInfo_input_tinNumber").val("");
	$("#vendorInfo_input_tinNumber").focus ();
}

function clientInfo_closePreview ()
{
	HideDialog ("thirdDialog");
}
