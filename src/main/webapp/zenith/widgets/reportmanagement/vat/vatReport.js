function vatReportMemberData ()
{
	this.m_strXMLData = "";
}

var m_oVatReportMemberData = new vatReportMemberData ();

vatReport_loaded ();

function vatReport_loaded ()
{
	m_oVatReportMemberData.m_strXMLData = m_oVatReportDurationMemberData.m_strXMLData;
	loadPage ("reportmanagement/vat/vatReport.html", "workarea", "vatReport_initVatReport ()");
}

function vatReport_initVatReport ()
{
	var oVatReportHeader = document.getElementById("vatReport_td_headerId");
	oVatReportHeader.innerHTML += " [ For : "+m_oVatReportDurationMemberData.m_strMonth+ " / " +m_oVatReportDurationMemberData.m_nYear+ " ]";
	$(window).unbind ('resize');
	populateXMLData (m_oVatReportMemberData.m_strXMLData, "reportmanagement/vat/vatReport.xslt", "vatReport_div_listDetail");
}

function vatReport_showPrintPopup ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "vatReport_initPopup ()");
}

function vatReport_initPopup ()
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (m_oVatReportMemberData.m_strXMLData, "reportmanagement/vat/vatReport.xslt", "print_div_listDetail");
}

function vatReport_exportToCSV () 
{
	var oTableData = document.getElementById("vatReportXSLT_table_report");
	var oRows = oTableData.rows;
	var strCSVContent = "data:text/csv;charset=utf-8,\n";
	strCSVContent += "VAT Report ( " +m_oVatReportDurationMemberData.m_strMonth+ " / " +m_oVatReportDurationMemberData.m_nYear+ " )\n";
	for(var nParentIndex = 0; nParentIndex < oRows.length; nParentIndex ++)
	{
		var oCells = oRows[nParentIndex].cells;
		for(var nIndex = 0; nIndex < oCells.length; nIndex++)
		{
			strCSVContent += nIndex < (oCells.length-1) ? oCells[nIndex].innerText + "," : oCells[nIndex].innerText;
		}
		strCSVContent += "\n";
	}
	var oEncodedURI = encodeURI(strCSVContent);
	var oHyperlink = document.createElement("a");
	oHyperlink.setAttribute("href", oEncodedURI);
	var strFileName = "VatReport_"+new Date().getTime()+".csv";
	oHyperlink.setAttribute("download", strFileName);
	oHyperlink.click();

}

function sales_closePrint ()
{
	HideDialog ("secondDialog");
}

function sales_print ()
{
	document.getElementById("secondDialog").style.top = "0";
	document.getElementById("secondDialog").style.bottom = "0";
	document.getElementById("secondDialog").style.left = "0";
	document.getElementById("secondDialog").style.right = "0";
	window.print();
}