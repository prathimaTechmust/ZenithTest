var location_includeDataObjects = 
[
	'widgets/inventorymanagement/location/LocationData.js'
];

includeDataObjects (location_includeDataObjects, "location_loaded ()");

function location_MemberData ()
{
	this.m_strName = "";
	this.m_strCurrentLocation = "";
}

var m_oLocationMemberData = new location_MemberData ();

function location_new ()
{
	createPopup("dialog", "#location_button_submit", "#location_button_cancel", true);
	location_init ();
}

function location_setDefault ()
{
	createPopup("dialog", "#defaultLocation_button_set", "#defaultLocation_button_cancel", true);
	location_populateLocations ();
}

function location_edit ()
{
	createPopup ("dialog", "#location_button_submit", "#location_button_cancel", true);
	location_init ();
	document.getElementById("location_button_submit").setAttribute('update', true);
	document.getElementById("location_button_submit").innerHTML = "Update";
	var oLocationData = new LocationData();
	oLocationData.m_nLocationId = m_oLocationList_memberData.m_nLocationId;
	LocationDataProcessor.get(oLocationData, Location_gotData);
}

function Location_gotData (oResponse)
{
	m_oLocationMemberData.m_strCurrentLocation = oResponse.m_arrLocations [0].m_strName;
	$("#location_textarea_address").val(oResponse.m_arrLocations [0].m_strAddress);
	$("#location_input_name").val(oResponse.m_arrLocations [0].m_strName);
	location_init ();
}

function location_init ()
{
	initFormValidateBoxes ("location_form_id");
}

function location_cancel ()
{
	HideDialog ("dialog");
}

function location_submit ()
{
	if (location_validate ())
	{
		var oLocationData = location_getFormData ();
		var strTest = document.getElementById("location_button_submit").update;
		if(document.getElementById("location_button_submit").getAttribute('update') == "false")
			LocationDataProcessor.create(oLocationData, location_created);
		else
		{
			oLocationData.m_nLocationId = m_oLocationList_memberData.m_nLocationId;
			LocationDataProcessor.update(oLocationData, location_updated);
		}
	}
}

function location_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser ("usermessage_location_updated", "kSuccess");
		HideDialog("dialog");
		navigate("location list", "widgets/inventorymanagement/location/locationList.js");
	}
	else
		informUser ("usermessage_location_updatedfailed", "kError");
}

function location_validate ()
{
	return validateForm ("location_form_id");
}

function location_getFormData ()
{
	var oLocationData = new LocationData ();
	oLocationData.m_strName = $("#location_input_name").val();
	oLocationData.m_strAddress = $("#location_textarea_address").val();
	return oLocationData;
}

function location_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser ("usermessage_location_created", "kSuccess");
		HideDialog ("dialog");
		navigate("location list", "widgets/inventorymanagement/location/locationList.js");
	}
	else
		informUser ("usermessage_location_createdfailed", "kError");
}

function location_clear()
{
	document.getElementById("location_input_name").value = "";
	document.getElementById("location_textarea_address").value = "";
}

function location_checkLocation ()
{
	var strName = $("#location_input_name").val();
	if (strName != m_oLocationMemberData.m_strCurrentLocation)
	{
		var oLocationData = new LocationData ();
		oLocationData.m_strName = strName;
		oLocationData.m_bMatchEqual = true;
		LocationDataProcessor.list(oLocationData, "", "", "", "", location_listed);
	}
}

function location_listed (oResponse)
{
	if (oResponse.m_arrLocations.length > 0)
	{
		informUser ("usermessage_location_alreadyexists", "kWarning");
		$("#location_input_name").val("");
		document.getElementById("location_input_name").focus();
	}
}

function location_setDefaultLocation ()
{
	var oLocationData = new LocationData ();
	oLocationData.m_nLocationId = $("#defaultLocation_select_location").val();
	LocationDataProcessor.setDefaultLocation (oLocationData, location_defaultSet);
}

function location_defaultSet (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser ("Default Location Set Successfully", "kSuccess");
		HideDialog ("dialog");
	}
	else
		informUser (oResponse.m_strError_Desc, "kError");
}

function location_populateLocations ()
{
	var oLocationData = new  LocationData ();
	LocationDataProcessor.list (oLocationData, "", "", "", "", location_gotList);
}

function location_gotList (oResponse)
{
	location_prepareLocationDD ("defaultLocation_select_location", oResponse);
}

function location_prepareLocationDD (strLocationID, oResponse)
{
	var oDefaultSelectLocationId = document.getElementById ("defaultLocation_select_location");
	var nDefaultLocationIndex = -1;
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrLocations.length; nIndex++)
	{
		if (oResponse.m_arrLocations [nIndex].m_bIsDefault)
			nDefaultLocationIndex = nIndex;
		arrOptions.push (CreateOption (oResponse.m_arrLocations [nIndex].m_nLocationId,
					oResponse.m_arrLocations[nIndex].m_strName));
	}
	PopulateDD (strLocationID, arrOptions);
	oDefaultSelectLocationId.selectedIndex = nDefaultLocationIndex;
}