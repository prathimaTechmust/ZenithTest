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
	this.m_nEditRowCount = -1;
}

var m_oInstitutionInfoMemberData = new institutionInfo_memberData ();

function institutionInfo_new ()
{
	institutionInfo_init();
	initFormValidateBoxes ("institutionInfo_form_id");
	checkRadioButtonChecked ();
}

/*function chequeFavourOfStudent ()
{
	document.getElementById("chequeInFavourOf").deleteRow(0);
	$(document).on("click", '#chequefavourofInstitution_input_radio', function ()
			{
			    $('#chequeInFavourOf').append('<tr><td class="fieldHeading">In Favour of<td><input type="text" id="chequeInFavourOf0" class="zenith"/></td><td><img src= "images/add.PNG" id="addChequeFavourof" onclick = "addChequeInFavourOf ()"</td></tr>');
			});
}*/
/*
function addChequeInFavourOf ()
{
	 m_oInstitutionInfoMemberData.chequeInFavourOfId = 1;
	$('#chequeInFavourOf').append('<tr><td class="fieldHeading">In Favour of<td><input type="text" id="chequeInFavourOf'+(m_oInstitutionInfoMemberData.chequeInFavourOfId++)+'" class="zenith"/></td><td><img src= "images/add.PNG" id="addChequeFavourof" onclick = "addChequeInFavourOf ()"</td><td><img src ="images/delete.png" id="deletechequefavour" onclick = "deleteChequeInFavourOf (this)"/></td></tr>');
	 m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId = m_oInstitutionInfoMemberData.chequeInFavourOfId;
}*/

function deleteChequeInFavourOf ()
{
	$("#chequeInFavourOf").on('click','.deleteChequeInFavourOf',function() { $(this).parent().parent().remove(); });
}

function checkRadioButtonChecked ()
{
	
}

function addNewChequeInFavourOf ()
{
	if(m_oInstitutionInfoMemberData.m_nEditRowCount != -1)
	{
		$('#chequeInFavourOf').append('<tr><td class="fieldHeading">Cheque Favourof<td><input type="text" id="chequeInFavourOf'+(m_oInstitutionInfoMemberData.m_nEditRowCount++)+'" class="zenith"/></td><td><img src ="images/delete.png" align = "center" style="width:20px;padding-left:5px" id="deletechequefavour" class= "deleteChequeInFavourOf" onclick = "deleteChequeInFavourOf (this)"/></td></tr>');
		m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId = m_oInstitutionInfoMemberData.m_nEditRowCount;
	}
	else
	{
		$('#chequeInFavourOf').append('<tr><td class="fieldHeading">Cheque Favourof<td><input type="text" id="chequeInFavourOf'+(m_oInstitutionInfoMemberData.chequeInFavourOfId++)+'" class="zenith"/></td><td><img src ="images/delete.png" align = "center" style="width:20px;padding-left:5px" id="deletechequefavour" class= "deleteChequeInFavourOf" onclick = "deleteChequeInFavourOf (this)"/></td></tr>');
		m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId = m_oInstitutionInfoMemberData.chequeInFavourOfId;
	}
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
        oInstitutionInformationData.m_strInstitutionType = $("#institutionInfo_input_institutionPrivate").val();
    else
        oInstitutionInformationData.m_strInstitutionType = $("#institutionInfo_input_institutionGovernment").val();
	oInstitutionInformationData.m_strInstitutionAddress = $("#institutionInfo_textarea_address").val();
	oInstitutionInformationData.m_strContactPersonName = $("#institutionInfo_input_contactpersonname").val();
	oInstitutionInformationData.m_strContactPersonEmail = $("#institutionInfo_input_contactpersonemail").val();
	oInstitutionInformationData.m_strPhoneNumber = $("#institutionInfo_input_phoneNumber").val();
	oInstitutionInformationData.m_strCity = $("#institutionInfo_input_cityName").val();
	oInstitutionInformationData.m_strState = $("#institutionInfo_input_stateName").val();
	oInstitutionInformationData.m_nPincode = $("#institutionInfo_input_pincode").val();	
	if($("#chequefavourofStudent_input_radio").is(':checked'))
	{
		oInstitutionInformationData.m_oChequeInFavourOf = getDetailsChequeFavourOfStudent ();
		oInstitutionInformationData.m_bChequeFavouOf = false;
	}		
	else
		oInstitutionInformationData.m_oChequeInFavourOf = getDetailsChequeInFavourOf ();		    
	return oInstitutionInformationData;
}

function getDetailsChequeFavourOfStudent ()
{
	var arrFavourOf = new Array ();
	var oChequeInFavourOf = new ChequeInFavourOf ();
	oChequeInFavourOf.m_strChequeFavourOf = "in favour of student";
	arrFavourOf.push(oChequeInFavourOf);
	return arrFavourOf;
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
	if(chequeFavours.rows.length == arrChequeFavourData.length)
	{
		oArray = m_oInstitutionInfoMemberData.arrChequeFavourOf;
	}
	else
	{
		for(var nIndex = 0; nIndex < m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId; nIndex++)
		{
			var oChequeFavour = new ChequeInFavourOf ();
			var favourValue = $('#chequeInFavourOf'+nIndex).val();
			if(favourValue != '' && favourValue != undefined)
			{
				oChequeFavour.m_strChequeFavourOf = favourValue;
				oArray.push(oChequeFavour);
			}
			if(nIndex < arrChequeFavourData.length)
				oChequeFavour.m_nChequeFavourId = arrChequeFavourData[nIndex].m_nChequeFavourId;
		}
	}	
	return oArray;
}

function getNewChequeFavours ()
{
	var oArray = new Array ();
	checkRowCount();
	for(var nIndex = 0; nIndex < m_oInstitutionInfoMemberData.UpdateCountChequeInFavourOfId;nIndex++)
	{
		if($("#chequeInFavourOf"+nIndex).val() != '' && $("#chequeInFavourOf"+nIndex).val())
		{
			var oChequeInFavourOf = new ChequeInFavourOf ();
			oChequeInFavourOf.m_strChequeFavourOf = $("#chequeInFavourOf"+nIndex).val();
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

function institutionInfo_created (oInstitutionInfoResponse)
{
	HideDialog ("ProcessDialog");
	if (oInstitutionInfoResponse.m_bSuccess)
	{
		institutionInfo_displayInfo("institution created successfully", "kSuccess");
	}
	else
		institutionInfo_displayInfo("institution creation failed", "kError");
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
	m_oInstitutionInfoMemberData.m_nEditRowCount = m_oInstitutionInfoMemberData.arrChequeFavourOf.length;
	 $("#institutionInfo_input_institutionName").val(oInstitutionInfoData.m_strInstitutionName);
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
	 if(oInstitutionInfoData.m_bChequeFavouOf == true)
	 {
		 document.getElementById("chequefavourofInstitution_input_radio").checked = true;
		 gotChequeInFavourof (arrChequeFavourOf);
				 
	 }
	 else
	 {
		 document.getElementById("chequefavourofStudent_input_radio").checked = true;
		 document.getElementById("chequeInFavourOf").deleteRow(0);
	 }		 		
	 initFormValidateBoxes ("institutionInfo_form_id");	
}

function gotChequeInFavourof (chequeFavourArray)
{
	 for(var nIndex = 0; nIndex < chequeFavourArray.length; nIndex++)
	 {
		 if(nIndex != 0)
			 $('#chequeInFavourOf').append('<tr><td class="fieldHeading">Cheque Favourof<td><input type="text" id="chequeInFavourOf'+(nIndex)+'" class="zenith"/></td><td><img src ="images/delete.png" align = "center" style ="width:20px;padding-left:5px;" id="deletechequefavour" class= "deleteChequeInFavourOf" onclick = "deleteChequeInFavourOf (this)"/></td></tr>');
		 $("#chequeInFavourOf"+nIndex).val(chequeFavourArray[nIndex].m_strChequeFavourOf);
	 }		
}

function institutionInfo_cancel()
{
	HideDialog ("dialog");
}
