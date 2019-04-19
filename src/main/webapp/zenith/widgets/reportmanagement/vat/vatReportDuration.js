function vatReportDuration_memberData ()
{
	this.m_strXMLData = "";
	this.m_strMonth = "";
	this.m_nYear = 0;
}

var m_oVatReportDurationMemberData = new vatReportDuration_memberData ();

vatReportDuration_loaded ();

function vatReportDuration_loaded ()
{
	loadPage ("reportmanagement/vat/reportDuration.html", "dialog", "vatReportDuration_initDuration ()");
}

function vatReportDuration_initDuration ()
{
	createPopup("dialog", "#reportDuration_button_cancel", "#reportDuration_button_generate", true);
	var dDate = new Date();
	var nCurrentMonth = dDate.getMonth();
	var nCurrentYear = dDate.getFullYear(); 
	var pos =0;
    for(var nYear = 2005; nYear <= nCurrentYear; nYear++) //2005 is the year VAT introduced in India
        document.getElementById('reportDuration_select_year').options[pos++] = new Option(nYear,nYear);
    if(nCurrentMonth == 0)
    {
    	nCurrentMonth = 11;
    	nCurrentYear -= 1; 
    }
    document.getElementById('reportDuration_select_month').value = nCurrentMonth+1;
    document.getElementById('reportDuration_select_year').value = nCurrentYear;
    
}

function vatReportDuration_cancel ()
{
	HideDialog ("dialog");
}

function vatReportDuration_generateVatReport ()
{
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "vatReportDuration_progressbarLoaded ()");
}

function vatReportDuration_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oSelectField = document.getElementById("reportDuration_select_month");
	m_oVatReportDurationMemberData.m_strMonth  = oSelectField.options[oSelectField.selectedIndex].innerHTML
	var nMonth = $("#reportDuration_select_month").val();
	m_oVatReportDurationMemberData.m_nYear = $("#reportDuration_select_year").val();
	VatReportDataProcessor.generateVatReportXML(nMonth, m_oVatReportDurationMemberData.m_nYear, vatReportDuration_gotVatReportData)
}

function vatReportDuration_gotVatReportData (oResponse)
{
	HideDialog ("secondDialog");
	if (oResponse.m_strXMLData.length > 0)
	{	
		HideDialog ("dialog");
		m_oVatReportDurationMemberData.m_strXMLData = oResponse.m_strXMLData;
		navigate ('vatReport','widgets/reportmanagement/vat/vatReport.js');
	}
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Failed To Generate VAT Report", "kError");
}
