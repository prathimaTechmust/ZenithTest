var siteInfo_includeDataObjects = 
[
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
];

includeDataObjects (siteInfo_includeDataObjects, "siteInfo_loaded ()");

function siteInfo_memberData ()
{
	this.m_bIsNew = false;
}

var m_oSiteInfoMemberData = new siteInfo_memberData ();

function siteInfo_init ()
{
	initFormValidateBoxes ("siteInfo_form_id");
	createPopup("clientInfo_siteInfo_dialog", "#siteInfo_button_submit", "#siteInfo_button_cancel", true);
}

function siteInfo_edit ()
{
	createPopup('clientInfo_siteInfo_dialog', '#siteInfo_button_submit', '#siteInfo_button_cancel', true);
	m_oSiteInfoMemberData.m_bIsNew = false;
	siteInfo_loadElements(m_oSiteInfoMemberData.m_bIsNew);
	var oSiteData = $("#clientInfo_table_listSites").datagrid('getData');
	var oData = oSiteData.rows[m_oClientInfoMemberData.m_nIndex];
	siteInfo_setFormData (oData);
}

function siteInfo_new ()
{
	m_oSiteInfoMemberData.m_bIsNew = true;
	siteInfo_loadElements(m_oSiteInfoMemberData.m_bIsNew);
	siteInfo_init ();
}

function siteInfo_cancel()
{
	HideDialog ("clientInfo_siteInfo_dialog");
}

function siteInfo_loadElements (bIsNew)
{
	if(bIsNew)
	{
		document.getElementById("siteInfo_button_submit").setAttribute('update',false);
	}
	else
	{
		document.getElementById("siteInfo_button_submit").setAttribute('update',true);
		document.getElementById("siteInfo_button_submit").innerHTML = "Update";
	}
}

function siteInfo_getFormData ()
{
	var oSiteData = new SiteData ();
	var oClientData = new ClientData ();
	oSiteData.m_strSiteName =  $("#siteInfo_input_siteName").val();
	oSiteData.m_strSiteAddress = $("#siteInfo_textarea_address").val();
	if (document.getElementById("siteInfo_input_checkActive").checked)
		oSiteData.m_nSiteStatus = "kActive";
	else
		oSiteData.m_nSiteStatus = "kInactive";
	oSiteData.m_oClientData = oClientData;
	return oSiteData;
}

function siteInfo_submit ()
{
	if (siteInfo_validate ())
	{
		var oSiteData = siteInfo_getFormData ();
		if(document.getElementById("siteInfo_button_submit").getAttribute('update') == "false")
		{
			siteInfo_list (oSiteData);
			informUser("site added successfully.", "kSuccess");
			HideDialog ("clientInfo_siteInfo_dialog");
		}
		else
		{
			HideDialog ("clientInfo_siteInfo_dialog");
			$('#clientInfo_table_listSites').datagrid('updateRow',{index : m_oClientInfoMemberData.m_nIndex, row : oSiteData});
			informUser("Site updated successfully.", "kSuccess");
		}
	}
}
	
function siteInfo_validate()
{
	return validateForm ("siteInfo_form_id");
}

function siteInfo_setFormData (oSiteData)
{
	$("#siteInfo_input_siteName").val(oSiteData.m_strSiteName);
	$("#siteInfo_textarea_address").val(oSiteData.m_strSiteAddress);
	document.getElementById("siteInfo_input_checkActive").checked = oSiteData.m_nSiteStatus == "kInactive" ? false : true;
	initFormValidateBoxes ("siteInfo_form_id");
}

function siteInfo_list (oSiteData)
{
	$('#clientInfo_table_listSites').datagrid('appendRow', oSiteData);
}