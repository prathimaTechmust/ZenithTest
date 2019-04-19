function VendorOutstandingReportData()
{
	this.m_strCompanyName = "";
	this.m_strCity = "";
	this.m_nOpeningBalance = "";
	this.m_nInvoicedAmount = "";
	this.m_nPaymentAmount = 0;
	this.m_nOutstandingAmount = "";
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("VendorOutstandingReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "VendorOutstandingReportData", "m_strCompanyName", this.m_strCompanyName);
		addChild(xmlDoc, "VendorOutstandingReportData", "m_strCity", this.m_strCity);
		addChild(xmlDoc, "VendorOutstandingReportData", "m_nOpeningBalance", this.m_nOpeningBalance);
		addChild(xmlDoc, "VendorOutstandingReportData", "m_nInvoicedAmount", this.m_nInvoicedAmount);
		addChild(xmlDoc, "VendorOutstandingReportData", "m_nPaymentAmount", this.m_nPaymentAmount);
		addChild(xmlDoc, "VendorOutstandingReportData", "m_nOutstandingAmount", this.m_nOutstandingAmount);
	}
}

dataObjectLoaded ();