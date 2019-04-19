function InvoiceReportData()
{
	this.m_strSoldBy = "";
	this.m_strSoldTo = "";
	this.m_strInvoiceNumber = "";
	this.m_strDate = "";
	this.m_nAmount = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("InvoiceReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "InvoiceReportData", "m_strSoldBy", this.m_strSoldBy);
		addChild(xmlDoc, "InvoiceReportData", "m_strSoldTo", this.m_strSoldTo);
		addChild(xmlDoc, "InvoiceReportData", "m_strInvoiceNumber", this.m_strInvoiceNumber);
		addChild(xmlDoc, "InvoiceReportData", "m_strDate", this.m_strDate);
		addChild(xmlDoc, "InvoiceReportData", "m_nAmount", this.m_nAmount);
	}
}

dataObjectLoaded ();