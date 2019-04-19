var clientInfo_includeDataObjects = 
[
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/clientmanagement/SiteData.js'
];

includeDataObjects (clientInfo_includeDataObjects, "clientInfo_loaded ()");

function clientInfo_memberData ()
{
	this.m_oClientData = null;
	this.m_nIndex = -1;
	this.m_bIsFromSales = false;
	this.m_bIsNew = false;
	this.m_bIsFromPurchaseOrder = false;
	this.m_nClientId = -1;
}

var m_oClientInfoMemberData = new clientInfo_memberData ();

function clientInfo_showCallback (oGeocodeResult, oParsedGeocodeResult)
{
	assert.isObject(oParsedGeocodeResult, "oParsedGeocodeResult expected to be an Object.");
	assert( Object.keys(oParsedGeocodeResult).length >0 , "oParsedGeocodeResult cannot be an empty .");// checks for non emptyness 
		$("#clientInfo_input_pinCode").val(oParsedGeocodeResult.postal_code);
	if (oParsedGeocodeResult.administrative_area_level_2 && oParsedGeocodeResult.administrative_area_level_1 && oParsedGeocodeResult.country != false)
		$("#clientInfo_input_city").val( oParsedGeocodeResult.administrative_area_level_2 + "," + oParsedGeocodeResult.administrative_area_level_1 + "," +oParsedGeocodeResult.country);
}

function clientInfo_new ()
{
	m_oClientInfoMemberData.m_bIsNew = true;
	clientInfo_loadElements (m_oClientInfoMemberData.m_bIsNew);
	clientInfo_init ();
}


function clientInfo_edit ()
{
	
	clientInfo_init ();
	m_oClientInfoMemberData.m_bIsNew = false;
	clientInfo_loadElements (m_oClientInfoMemberData.m_bIsNew);
	var oClientData = new ClientData ();
	oClientData.m_nClientId = m_oClientListMemberData.m_oSelectedClientId;
	ClientDataProcessor.get (oClientData, clientInfo_gotData);
}

function clientInfo_addSites ()
{
	clientInfo_init ();
	m_oClientInfoMemberData.m_bIsNew = false;
	clientInfo_loadElements (m_oClientInfoMemberData.m_bIsNew);
	var oClientData = new ClientData ();
	oClientData.m_nClientId = m_oClientInfoMemberData.m_nClientId;
	$('#clientInfo_div_tabs').tabs('select', 3);
	ClientDataProcessor.get (oClientData, clientInfo_gotData);
}

function clientInfo_addContacts ()
{
	clientInfo_init ();
	m_oClientInfoMemberData.m_bIsNew = false;
	clientInfo_loadElements (m_oClientInfoMemberData.m_bIsNew);
	var oClientData = new ClientData ();
	oClientData.m_nClientId = m_oClientInfoMemberData.m_nClientId;
	$('#clientInfo_div_tabs').tabs('select', 2);
	ClientDataProcessor.get (oClientData, clientInfo_gotData);
}
function clientInfo_init ()
{
	createPopup ("thirdDialog", "#clientInfo_button_submit", "#clientInfo_button_cancel", true);
	$('#clientInfo_div_tabs').tabs ();
	$( "#clientInfo_input_address" ).addresspicker ({updateCallback: clientInfo_showCallback});
	$('#clientInfo_input_city').addresspicker ({updateCallback: clientInfo_showCallback});
	initFormValidateBoxes ("clientInfo_form_id");
	clientInfo_populateBusinessTypeList ();
	clientInfo_contactGridInit ();
	clientInfo_sitesGridinit ();
}
function clientInfo_contactGridInit ()
{
	$('#clientInfo_table_listContactPersons').datagrid 
	(
		{
		    columns:
		    [[  
				{
					field:'m_strContactName',title:'Contact Name',sortable:true,width:100,
				},
				{
					field:'m_strPhoneNumber',title:'Phone Number',sortable:true,width:100
				},
				{
					field:'m_strEmail',title:'Email',sortable:true,width:120
				},
				{
					field:'m_strDepartment',title:'Department',sortable:true,width:70
				},
				{
					field:'m_strDesignation',title:'Designation',sortable:true,width:70
				},
				{
					field:'Action',title:'Action',sortable:false,width:70,
		        	formatter:function(value,row,index)
		        	{
		        		return clientInfo_contactInfo_displayImages (row, index);
		        	}
		         },
			]]
		}
	);
	
	$('#clientInfo_table_listContactPersons').datagrid
	(
		{
			onSortColumn: function (strColumn, strOrder) 
			{
				clientInfo_contactInfo_sort (strColumn, strOrder);
			}
		}
	)
}

function clientInfo_sitesGridinit ()
{
	$('#clientInfo_table_listSites').datagrid 
	(
		{
		    columns:
		    [[  
				{
					field:'m_strSiteName',title:'Site No',sortable:true,width:100
				},
				{
					field:'m_strSiteAddress',title:'Address',sortable:true,width:400
				},
				{
					field:'Action',title:'Action',sortable:false,width:70,
		        	formatter:function(value,row,index)
		        	{
		        		return clientInfo_siteInfo_displayImages (row, index);
		        	}
		        }
			]]
		}
	);
	
	$('#clientInfo_table_listSites').datagrid
	(
		{
			onSortColumn: function (strColumn, strOrder) 
			{
				
			}
		}
	)
}

function clientInfo_siteInfo_displayImages (row , index)
{
   assert.isNumber(index, "index expected to be a Number.");
   var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId'+ index +'" onClick="clientInfo_siteInfo_edit('+ index +')" /> </td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId'+ index +'" onClick="clientInfo_siteInfo_delete('+ index  +')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function clientInfo_siteInfo_edit (nIndex)
{
	 assert.isNumber(nIndex, "nIndex expected to be a Number.");
	m_oClientInfoMemberData.m_nIndex = nIndex;
	navigate ("siteInfo", "widgets/clientmanagement/editSiteInfo.js");
}

function clientInfo_siteInfo_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var bConfirm = getUserConfirmation ("Do you really want to delete ?")
	if (bConfirm == true)
	{
		$('#clientInfo_table_listSites').datagrid('deleteRow',nIndex);
		var nRows = $('#clientInfo_table_listSites').datagrid('getRows');
		if(nRows.length != 0)
			clientInfo_siteInfo_updateRowIndex(nRows)
		informUser("site deleted successfully.", "kSuccess");
	}
}

function clientInfo_siteInfo_updateRowIndex (nRows)
{
	assert.isArray(nRows, "nRows expected to be an Array.");
	for(var nIndex = 0; nIndex < nRows.length; nIndex++)
	{
		$('#clientInfo_table_listSites').datagrid('updateRow',{index : nIndex});
	}
}

function clientInfo_contactInfo_sort (strColumn, strOrder)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	var oListData = $("#clientInfo_table_listContactPersons").datagrid('getData');
	var oArrayData = new Array();
	for (var nIndex = 0; nIndex < oListData.rows.length; nIndex++)
	{
		oArrayData.push(oListData.rows[nIndex])
	}
	var oSortedData = oArrayData.sort(function (oData1, oData2)
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
	clearGridData ("#clientInfo_table_listContactPersons");
	clientInfo_contactInfo_sorted (oSortedData);
}

function clientInfo_contactInfo_sorted (oContactData)
{
	assert.isArray(oContactData, "oContactData expected to be an Array.");
	for (var nIndex = 0; nIndex < oContactData.length; nIndex++)
	{
		$('#clientInfo_table_listContactPersons').datagrid('appendRow',oContactData[nIndex]);
	}
}
 
function clientInfo_getFormData ()
{
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = $("#clientInfo_input_clientName").val();
	oClientData.m_strAddress = $("#clientInfo_input_address").val();
	oClientData.m_strCity = $("#clientInfo_input_city").val();
	oClientData.m_strPinCode = $("#clientInfo_input_pinCode").val();
	//oClientData.m_strState = $("#clientInfo_input_state").val(); // fields not available
	//oClientData.m_strCountry = $("#clientInfo_input_country").val();
	oClientData.m_strTelephone = $("#clientInfo_input_telephone").val();
	oClientData.m_strMobileNumber = $("#clientInfo_input_mobile").val();
	oClientData.m_strEmail = $("#clientInfo_input_email").val();
	oClientData.m_strWebAddress = $("#clientInfo_input_webUrl").val();
	oClientData.m_strTinNumber = $("#clientInfo_input_tinNumber").val();
	oClientData.m_strVatNumber = $("#clientInfo_input_vatNumber").val();
	oClientData.m_strCSTNumber = $("#clientInfo_input_cstNumber").val();
	oClientData.m_bAllowOnlineAccess = $("#clientInfo_input_onlineAccess").is(":checked");
	oClientData.m_bClientLock = $("#clientInfo_input_clientLock").is(":checked");
	oClientData.m_bOutstationClient = $("#clientInfo_input_outStationClient").is(":checked");
	var oDemographyData = new DemographyData ();
	var oBusinessTypeData = new BusinessTypeData ();
	oBusinessTypeData.m_nBusinessTypeId = $("#clientInfo_select_businessType").val();
	oDemographyData.m_oBusinessType = oBusinessTypeData;
	if(!m_oClientInfoMemberData.m_bIsNew)
		oDemographyData.m_nDemographyId = m_oClientInfoMemberData.m_oClientData.m_oDemography.m_nDemographyId;
	oClientData.m_oDemography = oDemographyData;
	return oClientData;
}

function clientInfo_validate ()
{
	return validateForm("clientInfo_form_id")
}

function clientInfo_submit ()
{
	var oClientData = clientInfo_getFormData ();
	oClientData.m_arrContactData = clientInfo_getContactDataArray (); 
	do
	{
		if (!(clientInfo_validate () && clientInfo_checkMandatoryFieldes (oClientData)))
			break;
		var oClientData = clientInfo_getFormData ();
		oClientData.m_arrContactData = clientInfo_getContactDataArray() ;
		oClientData.m_arrSiteData = clientInfo_getSiteDataArray() ;
		if(document.getElementById("clientInfo_button_submit").getAttribute('update') == "false")
			ClientDataProcessor.create (oClientData, clientInfo_created);
		else
		{
			oClientData.m_nClientId = m_oClientInfoMemberData.m_nClientId;
			oClientData.m_nOpeningBalance = m_oClientInfoMemberData.m_oClientData.m_nOpeningBalance;
			oClientData.m_nCreditLimit = m_oClientInfoMemberData.m_oClientData.m_nCreditLimit;
			oClientData.m_strPassword = m_oClientInfoMemberData.m_oClientData.m_strPassword;
			ClientDataProcessor.update (oClientData, clientInfo_updated);
		}
	} while (false);
}
function clientInfo_created (oClientInfoResponse)     
{
	if(oClientInfoResponse.m_bSuccess)
	{
		informUser("client created successfully.", "kSuccess");
		HideDialog ("thirdDialog");
		try
		{
			clientInfo_handleAfterSave ();
		}
		catch(oException){}
	}
	else if(oClientInfoResponse.m_nErrorID == 1)
	{
		informUser("Creation Failed!", "kError");
	}
}

function clientInfo_getSiteDataArray ()
{
	var oSiteDataArray = new Array ();
	var arrSiteData = $('#clientInfo_table_listSites').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrSiteData.length; nIndex++)
	{
		var oSiteData = new SiteData ();
		if(!m_oClientInfoMemberData.m_bIsNew)
			oSiteData.m_nSiteId = arrSiteData [nIndex].m_nSiteId;
		oSiteData.m_strSiteName = arrSiteData [nIndex].m_strSiteName;
		oSiteData.m_strSiteAddress = arrSiteData [nIndex].m_strSiteAddress;
		oSiteData.m_nSiteStatus = arrSiteData [nIndex].m_nSiteStatus;
		oSiteDataArray.push (oSiteData);
	}
	return oSiteDataArray;
}

function clientInfo_cancel ()
{
	HideDialog ("thirdDialog");
}

function clientInfo_populateBusinessTypeList ()
{
	var oBusinessTypeData = new BusinessTypeData ();
	BusinessTypeDataProcessor.list(oBusinessTypeData, "", "", 1, 10, clientInfo_listedBusinessData);
}

function clientInfo_listedBusinessData (oResponse)
{
	clientInfo_prepareBusinessTypeDD ("clientInfo_select_businessType", oResponse);
}

function clientInfo_prepareBusinessTypeDD (strBusinessTypeDD, oClientResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oClientResponse.m_arrBusinessType.length; nIndex++)
		arrOptions.push (CreateOption (oClientResponse.m_arrBusinessType [nIndex].m_nBusinessTypeId,
				oClientResponse.m_arrBusinessType [nIndex].m_strBusinessName));
	PopulateDD (strBusinessTypeDD, arrOptions);
}

function clientInfo_contactInfo_displayImages (rowId, index)
{
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table>'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId'+ index +'" onClick="clientInfo_contactInfo_edit('+ index +')" /> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId'+ index +'" onClick="clientInfo_contactInfo_delete('+ index  +')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function clientInfo_contactInfo_edit (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert(nIndex !== 0, "nIndex cannot be equal to zero.");
	m_oClientInfoMemberData.m_nIndex = nIndex;
	navigate ("contactInfo", "widgets/clientmanagement/editContactInfo.js");
}

function clientInfo_contactInfo_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var bConfirm = getUserConfirmation ("Do you really want to delete ?")
	if (bConfirm == true)
	{
		$('#clientInfo_table_listContactPersons').datagrid('deleteRow',nIndex);
		var nRows = $('#clientInfo_table_listContactPersons').datagrid('getRows');
		if(nRows.length != 0)
			clientInfo_contactInfo_updateRowIndex(nRows)
		informUser("contact deleted successfully.", "kSuccess");
	}
}

function clientInfo_contactInfo_updateRowIndex (nRows)
{
	assert.isArray(nRows, "nRows expected to be an Array.");
	for(var nIndex = 0; nIndex<nRows.length; nIndex++)
	{
		$('#clientInfo_table_listContactPersons').datagrid('updateRow',{index : nIndex});
	}
}

function clientInfo_getContactDataArray ()
{
	var oContactDataArray = new Array ();
	var arrContactData = $('#clientInfo_table_listContactPersons').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrContactData.length; nIndex++)
	{
		var oContactData = new ContactData ();
		if(!m_oClientInfoMemberData.m_bIsNew)
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

function clientInfo_updated (oClientInfoResponse)
{
	if(oClientInfoResponse.m_bSuccess)
	{
		informUser("client updated successfully.", "kSuccess");
		HideDialog ("thirdDialog");
		clientInfo_handleAfterUpdate ();
	}
	else if(oClientInfoResponse.m_nErrorID)
	{
		informUser("Updation Failed!", "kError");
	}
}

function clientInfo_gotData (oClientResponse)
{	
	m_oClientInfoMemberData.m_oClientData = oClientResponse.m_arrClientData[0];
	$("#clientInfo_input_clientName").val( m_oClientInfoMemberData.m_oClientData.m_strCompanyName);
	$("#clientInfo_input_address").val( m_oClientInfoMemberData.m_oClientData.m_strAddress);
	$("#clientInfo_input_city").val( m_oClientInfoMemberData.m_oClientData.m_strCity);
	$("#clientInfo_input_pinCode").val( m_oClientInfoMemberData.m_oClientData.m_strPinCode);
	$("#clientInfo_input_state").val( m_oClientInfoMemberData.m_oClientData.m_strState);
	$("#clientInfo_input_country").val( m_oClientInfoMemberData.m_oClientData.m_strCountry);
	$("#clientInfo_input_telephone").val( m_oClientInfoMemberData.m_oClientData.m_strTelephone);
	$("#clientInfo_input_mobile").val( m_oClientInfoMemberData.m_oClientData.m_strMobileNumber);
	$("#clientInfo_input_email").val( m_oClientInfoMemberData.m_oClientData.m_strEmail);
	$("#clientInfo_input_webUrl").val( m_oClientInfoMemberData.m_oClientData.m_strWebAddress);
	$("#clientInfo_input_tinNumber").val( m_oClientInfoMemberData.m_oClientData.m_strTinNumber);
	$("#clientInfo_input_vatNumber").val( m_oClientInfoMemberData.m_oClientData.m_strVatNumber);
	$("#clientInfo_input_cstNumber").val( m_oClientInfoMemberData.m_oClientData.m_strCSTNumber);
	document.getElementById("clientInfo_input_onlineAccess").checked = m_oClientInfoMemberData.m_oClientData.m_bAllowOnlineAccess;
	document.getElementById("clientInfo_input_clientLock").checked = m_oClientInfoMemberData.m_oClientData.m_bClientLock;
	document.getElementById("clientInfo_input_outStationClient").checked = m_oClientInfoMemberData.m_oClientData.m_bOutstationClient;
	$("#clientInfo_select_businessType").val( m_oClientInfoMemberData.m_oClientData.m_oDemography.m_oBusinessType.m_nBusinessTypeId);
	clientInfo_contactInfo_setContacts (m_oClientInfoMemberData.m_oClientData.m_oContacts);
	clientInfo_siteInfo_setSites (m_oClientInfoMemberData.m_oClientData.m_oSites);
	initFormValidateBoxes ("clientInfo_form_id");
}

function clientInfo_contactInfo_setContacts (arrContacts)
{
	assert.isArray(arrContacts, "arrContacts expected to be an Array.");
	for(var nIndex = 0; nIndex < arrContacts.length; nIndex++)
	{
		$("#clientInfo_table_listContactPersons").datagrid('appendRow', arrContacts[nIndex]);
	}
}

function clientInfo_siteInfo_setSites (arrSites)
{
	assert.isArray(arrSites, "arrSites expected to be an Array.");
	for(var nIndex = 0; nIndex < arrSites.length; nIndex++)
	{
		$("#clientInfo_table_listSites").datagrid('appendRow', arrSites[nIndex]);
	}
}

function clientInfo_errorHandler (msg, oException) 
{
	assert.isObject(msg, "msg expected to be an Object.");
	informUser("Error message is: " + msg + " - Error Details: " + "kError");
}


function clientInfo_contactInfo_addContact ()
{
	navigate ("contactInfo", "widgets/clientmanagement/newContactInfo.js");
}

function clientInfo_siteinfo_addSites ()
{
	navigate ("siteInfo", "widgets/clientmanagement/newSiteInfo.js");
}

function clientInfo_loadElements (bIsNew)
{
	assert.isBoolean(bIsNew, "bIsNew should be a boolean value");
	if(bIsNew)
	{
		document.getElementById("clientInfo_button_submit").setAttribute('update',false);
	}
	else
	{
		document.getElementById("clientInfo_button_submit").setAttribute('update',true);
		document.getElementById("clientInfo_button_submit").innerHTML = "Update";
	}
}

function clientInfo_checkMandatoryFieldes (oClientData)
{
	assert.isObject(oClientData, "oClientData expected to be an Object.");
	assert( Object.keys(oClientData).length >0 , "oClientData cannot be an empty .");// checks for non emptyness 
	var bIsMandatory = true;
	if(oClientData.m_oDemography.m_oBusinessType.m_nBusinessTypeId == "-1")
	{
		$('#clientInfo_div_tabs').tabs('select',1);
		clientInfo_showErrorLable ("clientInfo_div_demographyInfo", "clientInfo_label_error");
		bIsMandatory = false;
	}
	else
	{
		var oErrorLabel = document.getElementById("clientInfo_label_error");
		oErrorLabel.style.display = "none";
	}
	return bIsMandatory;
}

function clientInfo_showErrorLable (divId, errorLabel)
{
	assert.isString(divId, "divId expected to be a string.");
	assert.isString(errorLabel, "errorLabel expected to be a string.");
	var oDivision = document.getElementById(divId);
	var oErrorLabel = document.getElementById(errorLabel);
	if(oDivision.id == "clientInfo_div_demographyInfo")
		oErrorLabel.innerHTML = "**select business type";
	else
		oErrorLabel.innerHTML = "**Fill the required fields";
	oErrorLabel.style.display = "block";
	oDivision.appendChild(oErrorLabel);
}

function clientInfo_removeLabel ()
{
	var oErrorLabel = document.getElementById("clientInfo_label_error");
	oErrorLabel.style.display = "none";
}

function clientInfo_validateEmail ()
{
	var oClientData = new ClientData ();
	oClientData.m_strEmail = $("#clientInfo_input_email").val();
	ClientDataProcessor.list(oClientData, "", "",0,10,clientInfo_listed);
}

function clientInfo_listed (oClientResponse)
{
	if(oClientResponse.m_arrClientData.length > 0)
	{
		informUser("Email already exist!", "kWarning");
		$("#clientInfo_input_email").val("");
		$("#clientInfo_input_email").focus ();
	}
}

function clientInfo_checkEmailAddress ()
{
	var strEmail = $("#clientInfo_input_email").val();
	if (strEmail == "")
	{
		informUser("Please provide email address.", "kWarning");
		document.getElementById("clientInfo_input_onlineAccess").checked = false;
		$("#clientInfo_input_email").focus ();
	}
}

function clientInfo_showVerifyIcon ()
{
	var strTinNumber = $("#clientInfo_input_tinNumber").val();
	if(strTinNumber.trim() != "")
		document.getElementById ("clientInfo_img_verifyTin").style.visibility="visible";
	else
		document.getElementById ("clientInfo_img_verifyTin").style.visibility="hidden";
}

function clientInfo_verifyTinNumber ()
{
	m_oClientInfoMemberData.m_strTinNumber = $("#clientInfo_input_tinNumber").val();
	loadPage ("clientmanagement/verifyTin.html", "clientInfo_tinVerfication_dialog", "clientInfo_showPopUp ()");
}

function clientInfo_showPopUp ()
{
	createPopup ("clientInfo_tinVerfication_dialog", "", "", true);
	var strURL = "http://www.tinxsys.com/TinxsysInternetWeb/dealerControllerServlet?tinNumber="+m_oClientInfoMemberData.m_strTinNumber+"&searchBy=TIN&backPage=searchByTin_Inter.jsp"
	var strIframe = '<iframe class="frame" frameborder="0" src="'+ strURL +'"></iframe>';
	document.getElementById("verifyTin_div_verifyURL").innerHTML = strIframe;
}

function clientInfo_verifyNotOk ()
{
	HideDialog ("clientInfo_tinVerfication_dialog");
	$("#clientInfo_input_tinNumber").val("");
	$("#clientInfo_input_tinNumber").focus ();
}

function clientInfo_closePreview ()
{
	HideDialog ("clientInfo_tinVerfication_dialog");
}