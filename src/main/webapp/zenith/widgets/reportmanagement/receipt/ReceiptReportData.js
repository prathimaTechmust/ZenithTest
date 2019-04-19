function ReceiptReportData()
{
	this.m_strCompanyName = "";
	this.m_strModeName = "";
	this.m_nAmount = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("ReceiptReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "ReceiptReportData", "m_strCompanyName", this.m_strCompanyName);
		addChild(xmlDoc, "ReceiptReportData", "m_strModeName", this.m_strModeName);
		addChild(xmlDoc, "ReceiptReportData", "m_strDate", this.m_strDate);
		addChild(xmlDoc, "ReceiptReportData", "m_nAmount", this.m_nAmount);
	}
}

dataObjectLoaded ();