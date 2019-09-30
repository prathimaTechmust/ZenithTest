var institutionInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js',
	'widgets/scholarshipmanagement/chequeInFavourOf/ChequeInFavourOf.js'
];

 includeDataObjects (institutionInfo_includeDataObjects, "institutionInfo_loaded()");

function institutionInfo_memberData ()
{
	this.m_nInstitutionId = -1;
	this.chequeInFavourOfId = 1;
	this.m_nEditRowCount = 0;
	this.UpdateCountChequeInFavourOfId = 0;
	this.UpdateEditCountChequeInFavourOfId = 0;
}

var m_oInstitutionInfoMemberData = new institutionInfo_memberData ();

function institutionInfo_new ()
{
	institutionInfo_init();
	initFormValidateBoxes ("institutionInfo_form_id");
}

function institutionInfo_init ()
{
	createPopup("dialog", "#institutionInfo_button_submit", "#institutionInfo_button_cancel", true);
}

function institutionInfo_submit ()
{
	if (institutionInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "institution_progressbarLoaded ()");
}

function institution_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	oInstitutionInformationData = institutionInfo_getFormData ();
	if(document.getElementById("institutionInfo_button_submit").getAttribute('update') == "false")
		InstitutionInformationDataProcessor.create(oInstitutionInformationData, institutionInfo_created);
	else
		InstitutionInformationDataProcessor.update (oInstitutionInformationData,institutionInfo_updated);
}

function institutionInfo_edit ()
{
	institutionInfo_init();
	var oInstitutionInformationData = new InstitutionInformationData ();
	oInstitutionInformationData.m_nInstitutionId = m_oInstitutionsInfoListMemberData.m_nSelectedInstitutionId;
	document.getElementById("institutionInfo_button_submit").setAttribute('update', true);
	document.getElementById("institutionInfo_button_submit").innerHTML = "Update";
	InstitutionInformationDataProcessor.get (oInstitutionInformationData, institutionInfo_gotData);
}

function institutionInfo_getFormData ()
{
	var oInstitutionInformationData = new InstitutionInformationData ();
	oInstitutionInformationData.m_nInstitutionId = m_oInstitutionInfoMemberData.m_nInstitutionId;
	oInstitutionInformationData.m_strInstitutionName = $("#institutionInfo_input_institutionName").val();
	oInstitutionInformationData.m_strInstitutionEmailAddress = $("#institutionInfo_input_institutionemail").val();
	if(document.getElementById("institutionInfo_input_institutionPrivate").checked)
	{
        oInstitutionInformationData.m_strInstitutionType = $("#institutionInfo_input_institutionPrivate").val();
	}
    else
    {
        oInstitutionInformationData.m_strInstitutionType = $("#institutionInfo_input_institutionGovernment").val();
    }
	oInstitutionInformationData.m_strInstitutionAddress = $("#institutionInfo_textarea_address").val();
	oInstitutionInformationData.m_strContactPersonName = $("#institutionInfo_input_contactpersonname").val();
	oInstitutionInformationData.m_strContactPersonEmail = $("#institutionInfo_input_contactpersonemail").val();
	oInstitutionInformationData.m_strPhoneNumber = $("#institutionInfo_input_phoneNumber").val();
	oInstitutionInformationData.m_strCity = $("#institutionInfo_input_cityName").val();
	oInstitutionInformationData.m_strState = $("#institutionInfo_input_stateName").val();
	oInstitutionInformationData.m_nPincode = $("#institutionInfo_input_pincode").val();	
	if($("#chequefavourofInstitution_input_radio").is(':checked'))
	{
		oInstitutionInformationData.m_oChequeInFavourOf = getDetailsChequeInFavourOf ();
		oInstitutionInformationData.m_bChequeFavouOf = true;
	}
	var oLoginUserData = getLoginUserData ();
	if(m_oInstitutionInfoMemberData.m_nInstitutionId != -1)
	{
		oInstitutionInformationData.m_dCreatedOn = m_oInstitutionInfoMemberData.dCreatedOn;
		oInstitutionInformationData.m_oUserCreatedBy = m_oInstitutionInfoMemberData.oUserCreatedBy;
		oInstitutionInformationData.m_oUserUpdatedBy = oLoginUserData;
	}
	else
	{
		oInstitutionInformationData.m_oUserCreatedBy = oLoginUserData;
		oInstitutionInformationData.m_oUserUpdatedBy = oLoginUserData;
	}	
	return oInstitutionInformationData;
}

function institutionInfo_created (oInstitutionInfoResponse)
{
	HideDialog ("ProcessDialog");
	if (oInstitutionInfoResponse.m_bSuccess)
	{
		institutionInfo_displayInfo("institution created successfully", "kSuccess");
	}
	else
	{
	institutionInfo_displayInfo("institution creation failed", "kError");
	}
}

function institutionInfo_updated (oInstitutionInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oInstitutionInfoResponse.m_bSuccess)
		institutionInfo_displayInfo ("institution updated successfully");
	else
		institutionInfo_displayInfo ("institution updation failed");
	
}

function institutionInfo_validate ()
{
	return validateForm("institutionInfo_form_id");
}

function institutionInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("institutionList", "widgets/scholarshipmanagement/institutionslist/listInstitution.js")
}


function institutionInfo_gotData (oInstitutionInfoResponse)
{	
	var oInstitutionInfoData = oInstitutionInfoResponse.m_arrInstitutionInformationData[0];
	m_oInstitutionInfoMemberData.m_nInstitutionId = oInstitutionInfoData.m_nInstitutionId;
	arrChequeFavourOf = m_oInstitutionInfoMemberData.arrChequeFavourOf = oInstitutionInfoData.m_oChequeInFavourOf;   
	m_oInstitutionInfoMemberData.dCreatedOn = oInstitutionInfoData.m_dCreatedOn;
	m_oInstitutionInfoMemberData.dUpdatedOn = oInstitutionInfoData.m_dUpdatedOn;
	m_oInstitutionInfoMemberData.oUserCreatedBy = oInstitutionInfoData.m_oUserCreatedBy;
	m_oInstitutionInfoMemberData.oUserUpdatedBy = oInstitutionInfoData.m_oUserUpdatedBy;
	m_oInstitutionInfoMemberData.m_nEditRowCount = m_oInstitutionInfoMemberData.arrChequeFavourOf.length;
	$("#institutionInfo_input_institutionName").val(oInstitutionInfoData.m_strInstitutionName);
	m_oInstitutionInfoMemberData.m_arrChequeFavourDetails = oInstitutionInfoData.m_oChequeInFavourOf;
	$("#institutionInfo_input_institutionemail").val(oInstitutionInfoData.m_strInstitutionEmailAddress);
	if(oInstitutionInfoData.m_strInstitutionType == "Government")
	{
		var radiobutton = document.getElementById("institutionInfo_input_institutionGovernment");
		radiobutton.checked = true;
	}
	else
	{
		var radiobutton = document.getElementById("institutionInfo_input_institutionPrivate");
		radiobutton.checked = true;
	}
	 $("#institutionInfo_input_institutionType").val(oInstitutionInfoData.m_strInstitutionType);
	 $("#institutionInfo_textarea_address").val(oInstitutionInfoData.m_strInstitutionAddress);
	 $("#institutionInfo_input_contactpersonname").val(oInstitutionInfoData.m_strContactPersonName);
	 $("#institutionInfo_input_contactpersonemail").val(oInstitutionInfoData.m_strContactPersonEmail);
	 $("#institutionInfo_input_phoneNumber").val(oInstitutionInfoData.m_strPhoneNumber);
	 $("#institutionInfo_input_cityName").val(oInstitutionInfoData.m_strCity);
	 $("#institutionInfo_input_stateName").val(oInstitutionInfoData.m_strState);
	 $("#institutionInfo_input_pincode").val(oInstitutionInfoData.m_nPincode);
	 initFormValidateBoxes ("institutionInfo_form_id");
	 if(oInstitutionInfoData.m_bChequeFavouOf == true)
	 {
		 document.getElementById("chequefavourofInstitution_input_radio").checked = true;
		 displayCheque(cheque);
		 gotChequeInFavourof (arrChequeFavourOf);				 
	 }
    else
     {
         document.getElementById("chequefavourofStudent_input_radio").checked = true;
    	 gotChequeInFavourof (arrChequeFavourOf);    	
     }

}

function institutionInfo_cancel()
{
	HideDialog ("dialog");
}

function displayCheque(divId) 
{
	  document.getElementById(divId.id).style.display = "block";
}

function hideCheque(divId) 
{

	 document.getElementById(divId.id).style.display = "none";
}

function deleteChequeInFavourOf ()
{
	$("#chequeInFavourOf").on('click','.deleteChequeInFavourOf',function() { $(this).parent().parent().remove(); });
}

function addNewChequeInFavourOf ()
{
	if(m_oInstitutionInfoMemberData.m_nEditRowCount != 0)
	{
		$('#chequeInFavourOf').append('<tr><td class="fieldHeading">Cheque Favourof<td><input type="text" id="chequeInFavourOf'+(m_oInstitutionInfoMemberData.m_nEditRowCount++)+'" class="zenith"/></td><td><img src ="images/delete.png" align = "center" style="width:21px;padding-left:10px" id="deletechequefavour" class= "deleteChequeInFavourOf" onclick = "deleteChequeInFavourOf (this)"/></td></tr>');

		m_oInstitutionInfoMemberData.UpdateEditCountChequeInFavourOfId = m_oInstitutionInfoMemberData.m_nEditRowCount;
	}
	else
	{
		$('#chequeInFavourOf').append('<tr><td class="fieldHeading">Cheque Favourof<td><input type="text" id="chequeInFavourOf'+(m_oInstitutionInfoMemberData.chequeInFavourOfId++)+'" class="zenith"/></td><td><img src ="images/delete.png" align = "center" style="width:21px;padding-left:10px" id="deletechequefavour" class= "deleteChequeInFavourOf" onclick = "deleteChequeInFavourOf (this)"/></td></tr>');
		m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId = m_oInstitutionInfoMemberData.chequeInFavourOfId;
	}
}

function getDetailsChequeInFavourOf ()
{
	var arrFavourof = m_oInstitutionInfoMemberData.arrChequeFavourOf;
	var arrChequeInFavourOf = new Array ();
	if(arrFavourof == undefined)
		arrChequeInFavourOf = getNewChequeFavours ();
	else
		arrChequeInFavourOf = getAddNewChequeFavours ();	
	return arrChequeInFavourOf;
}


function getAddNewChequeFavours ()
{
	var oArray = new Array ();
	var arrChequeFavourData = m_oInstitutionInfoMemberData.arrChequeFavourOf;
	var chequeFavours = document.getElementById("chequeInFavourOf");
	var oLoginUser = getLoginUserData ();
	if(chequeFavours.rows.length == arrChequeFavourData.length)
	{
		for(var nIndex = 0; nIndex < arrChequeFavourData.length; nIndex++)
		{
			var oChequeFavour = new ChequeInFavourOf ();
			oChequeFavour.m_nChequeFavourId = arrChequeFavourData[nIndex].m_nChequeFavourId;
			oChequeFavour.m_oUserCreatedBy = m_oInstitutionInfoMemberData.oUserCreatedBy;
			oChequeFavour.m_dCreatedOn = m_oInstitutionInfoMemberData.dCreatedOn;
			oChequeFavour.m_oUserUpdatedBy = oLoginUser;
			oChequeFavour.m_strChequeFavourOf = $("#chequeInFavourOf"+nIndex).val();
			oArray.push(oChequeFavour);
		}
	}
	else
	{		
		for(var nIndex = 0; nIndex <= m_oInstitutionInfoMemberData.UpdateEditCountChequeInFavourOfId; nIndex++)
		{
			var oChequeFavour = new ChequeInFavourOf ();
			var favourValue = $('#chequeInFavourOf'+nIndex).val();
			if(favourValue != ''  &&  favourValue != undefined )
			{
				oChequeFavour.m_strChequeFavourOf = favourValue;
				oArray.push(oChequeFavour);
			}
			if(nIndex < arrChequeFavourData.length)
			{
				oChequeFavour.m_nChequeFavourId = arrChequeFavourData[nIndex].m_nChequeFavourId;
				oChequeFavour.m_dCreatedOn = m_oInstitutionInfoMemberData.dCreatedOn;
				oChequeFavour.m_oUserCreatedBy = m_oInstitutionInfoMemberData.oUserCreatedBy;
				oChequeFavour.m_oUserUpdatedBy = oLoginUser;
			}
			else
			{
				oChequeFavour.m_oUserCreatedBy = oLoginUser;
				oChequeFavour.m_oUserUpdatedBy = oLoginUser;
			}
				
		}
	}	
	return oArray;
}

function getNewChequeFavours ()
{
	var oArray = new Array ();
	checkRowCount();
	var oLoginUser = getLoginUserData ();
	for(var nIndex = 0; nIndex < m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId;nIndex++)
	{
		if(($("#chequeInFavourOf"+nIndex).val() != '')&&($("#chequeInFavourOf"+nIndex).val() != undefined ))
		{
			var oChequeInFavourOf = new ChequeInFavourOf ();
			oChequeInFavourOf.m_strChequeFavourOf = $("#chequeInFavourOf"+nIndex).val();
			oChequeInFavourOf.m_oUserCreatedBy = oLoginUser;
			oChequeInFavourOf.m_oUserUpdatedBy = oLoginUser;
			oArray.push(oChequeInFavourOf);
		}		
	}
	return oArray;	
}

function checkRowCount ()
{
	var result = m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId;
	if(result == 0)
		m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId = 1;
}

function gotChequeInFavourof (arrChequeFavourOf) 
{
	//displayCheque(cheque);
	//document.getElementById("chequefavourofInstitution_input_radio").checked = true;
	for(var nIndex = 0; nIndex < arrChequeFavourOf.length; nIndex++ )
	 {
		 if(nIndex !=0)
			 $('#chequeInFavourOf').append('<tr><td class="fieldHeading">Cheque Favourof<td><input type="text" id="chequeInFavourOf'+(nIndex)+'" class="zenith"/></td><td><img src ="images/delete.png" align = "center" style ="width:21px;padding-left:10px;" id="deletechequefavour" class= "deleteChequeInFavourOf" onclick = "deleteChequeInFavourOf (this)"/></td></tr>');
		 $("#chequeInFavourOf"+nIndex).val(arrChequeFavourOf[nIndex].m_strChequeFavourOf); 	 
	 }
}

//function getDetailsChequeFavourOfStudent()
//{
//	var arrFavourOf = new Array ();
//	var arrChequeFavourData = m_oInstitutionInfoMemberData.arrChequeFavourOf;
//	if(arrChequeFavourData == undefined )
//		arrChequeInFavourOf = getNewChequeFavourOfStudent ();
//	else
//		arrChequeInFavourOf = getAddNewChequeFavourOfStudent ();	
//	return arrChequeInFavourOf;	
//	
//}

//function getNewChequeFavourOfStudent() 
//{
//	var arrFavourOf = new Array ();
//	var oChequeInFavourOf = new ChequeInFavourOf ();
//	oChequeInFavourOf.m_strChequeFavourOf = "in favour of student";
//	arrFavourOf.push(oChequeInFavourOf);
//	return arrFavourOf;
//}
//
//function getAddNewChequeFavourOfStudent() 
//{
//	var arrFavourOf = new Array ();
//	var arrChequeFavourData = m_oInstitutionInfoMemberData.arrChequeFavourOf;
//	
//	if(arrChequeFavourData.length > 0)
//	{
//		for(var nIndex = 0; nIndex < 1;nIndex++)
//		{
//			var oChequeInFavourOf = new ChequeInFavourOf ();
//			oChequeInFavourOf.m_nChequeFavourId = arrChequeFavourData[nIndex].m_nChequeFavourId;
//			oChequeInFavourOf.m_strChequeFavourOf = "in favour of student";
//			arrFavourOf.push(oChequeInFavourOf);
//		}	
//	}
//	 return arrFavourOf;
//}




