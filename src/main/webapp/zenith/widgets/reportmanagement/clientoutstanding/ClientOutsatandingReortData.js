function ClientOutstandingReportData()
{
	this.m_strCompanyName = "";
	this.m_strCity = "";
	this.m_nOpeningBalance = 0;
	this.m_nInvoiced = 0;
	this.m_nReceipts = 0;
	this.m_nOutstandingAmount = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("ClientOutstandingReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "ClientOutstandingReportData", "m_strCompanyName", this.m_strCompanyName);
		addChild(xmlDoc, "ClientOutstandingReportData", "m_strCity", this.m_strCity);
		addChild(xmlDoc, "ClientOutstandingReportData", "m_nOpeningBalance", this.m_nOpeningBalance);
		addChild(xmlDoc, "ClientOutstandingReportData", "m_nInvoiced", this.m_nInvoiced);
		addChild(xmlDoc, "ClientOutstandingReportData", "m_nReceipts", this.m_nReceipts);
		addChild(xmlDoc, "ClientOutstandingReportData", "m_nOutstandingAmount", this.m_nOutstandingAmount);
	}
}

dataObjectLoaded ();