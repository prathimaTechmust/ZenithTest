var academicyear_includeDataObjects = 
[
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js'
];

 includeDataObjects (academicyear_includeDataObjects, "academicyear_loaded()");
 
 function academicYearMemberData ()
 {
	 
	 this.m_nRowCount = 0;
	 this.m_nRadioCount = 0;
	 this.m_AcademicYearRowCount = 0;
 }
 
 var m_oAcademicyearInfoMemberData = new academicYearMemberData ();

function academicyear_init ()
{
	initFormValidateBoxes ("AcademicYear_form_id");
	academicyear_populate();
	
}

function addyear_new ()
{
	createPopup ("dialog", "#academicYear_button_submit", "#academicYearInfo_button_cancel", true);
	academicyear_init ();
}

function academicyear_populate()
{
	var oAcademicYear = new AcademicYear();
	AcademicYearProcessor.list(oAcademicYear,"m_strAcademicYear","asc",0,0,academicyearResponse);
}

function academicyear_getFormData ()
{
	var oAcademicYear = new AcademicYear ();
	var rowCount = document.getElementById("academicyeartableid");
	for(var nIndex = 0; nIndex < rowCount.rows.length; nIndex++)
	{
		if($("#academicyearInfo_input"+nIndex).val() != "")
		{
			
			oAcademicYear.m_strAcademicYear = $("#academicyearInfo_input"+nIndex).val();			
		}				
	}	
	return oAcademicYear;
}

function academicyear_validate ()
{
	return validateForm ("AcademicYear_form_id");
}

function academicYearInfo_submit ()
{
	if (academicyear_validate())
	{
		var oAcademicYear = academicyear_getFormData ();
		if(document.getElementById("academicYear_button_submit").getAttribute('update') == "false")
			AcademicYearProcessor.create(oAcademicYear, academicyear_created);
		else
		{			
			AcademicYearProcessor.create(oAcademicYear, academicyear_update);
		}
	}
}

function academicyear_update (oAcademicyearResponse)
{
	if(oAcademicyearResponse.m_bSuccess)
	{
		informUser("academic year update successfully", "kSuccess");
		HideDialog ("dialog");
	}
}

function addNewAcademicYear()
{	
	if(m_oAcademicyearInfoMemberData.m_AcademicYearRowCount > 1)
	{
		$("#academicyeartableid").append('<tr><td><input  type="text" id="academicyearInfo_input'+(m_oAcademicyearInfoMemberData.m_AcademicYearRowCount++)+'" class="zenith"/></td><td style="padding-right: 10px"> </td><td style="padding-right: 10px"> </td></tr>');
	}
	else
	{
		$("#academicyeartableid").append('<tr><td><input  type="text" id="academicyearInfo_input'+(m_oAcademicyearInfoMemberData.m_nRowCount++)+'" class="zenith"/></td><td style="padding-right: 10px"> </td><td style="padding-right: 10px"> </td></tr>');
	}	
}

function academicyearResponse (oAcademicYearResponse)
{
	m_oAcademicyearInfoMemberData.m_AcademicYearRowCount = oAcademicYearResponse.m_arrAcademicYear.length;
	for(var nIndex = 0; nIndex < oAcademicYearResponse.m_arrAcademicYear.length; nIndex++)
	{
		$("#academicyeartableid").append('<tr><td><input  type="text" id="academicyearInfo_input'+(nIndex)+'" class="zenith"/></td><td style="padding-right: 10px"> </td><td style="padding-right: 10px"> </td></tr>');
		$("#academicyearInfo_input"+nIndex).val(oAcademicYearResponse.m_arrAcademicYear[nIndex].m_strAcademicYear);
	}
	if(oAcademicYearResponse.m_arrAcademicYear.length != 0)
	{	
		document.getElementById("academicYear_button_submit").setAttribute('update', true);
		document.getElementById("academicYear_button_submit").innerHTML = "Update";		
	}
		
}

function academicYearInfo_cancel ()
{
	HideDialog ("dialog");
}

function academicyear_created (oAcademicyearResponse)
{
	if(oAcademicyearResponse.m_bSuccess)
	{
		informUser("academic year created successfully", "kSuccess");
		HideDialog ("dialog");
	}
}

function academicyear_updated (oAcademicyearResponse)
{
	if(oAcademicyearResponse.m_bSuccess)
	{
		informUser("academic year updated successfully", "kSuccess");
		HideDialog ("dialog");
	}
}

function academicyear_gotData (oAcademicyearResponse)
{	
	var oAcademicyear = oAcademicyearResponse.m_arrAcademicYear[0];
	$("#academicyearInfo_input").val(oAcademicyear);
	$("#academicyearInfo_inputradio").val(oAcademicyear);	
}
